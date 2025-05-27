package com.playermanagement.service.impl;

import com.playermanagement.dto.request.*;
import com.playermanagement.dto.response.*;
import com.playermanagement.exception.BadRequestException;
import com.playermanagement.exception.ForbiddenException;
import com.playermanagement.exception.NotFoundException;
import com.playermanagement.mapper.PlayerMapper;
import com.playermanagement.model.Player;
import com.playermanagement.model.Gender;
import com.playermanagement.model.Role;
import com.playermanagement.repository.PlayerRepository;
import com.playermanagement.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        logger.info("PlayerServiceImpl initialized");
    }

    @Override
    public PlayerCreateResponseDto createPlayer(PlayerCreateDto playerCreateDto, String editorLogin) {
        logger.info("SERVICE: createPlayer - Editor: {}, New player login: {}, role: {}", 
                   editorLogin, playerCreateDto.getLogin(), playerCreateDto.getRole());
        
        try {
            Player editor = getPlayerByLogin(editorLogin);
            logger.debug("Editor found: {}, role: {}", editorLogin, editor.getRole());
            
            validateCreatePermissions(editor);
            validateCreateRole(playerCreateDto.getRole());
            validateUniqueFields(playerCreateDto.getLogin(), playerCreateDto.getScreenName());
            
            Player player = playerMapper.toEntity(playerCreateDto);
            Player savedPlayer = playerRepository.save(player);
            
            logger.info("SUCCESS: Player created - ID: {}, Login: {}, Role: {}", 
                       savedPlayer.getId(), savedPlayer.getLogin(), savedPlayer.getRole());
            
            return playerMapper.toCreateResponseDto(savedPlayer);
        } catch (Exception e) {
            logger.error("ERROR: createPlayer failed - Editor: {}, Login: {}, Error: {}", 
                        editorLogin, playerCreateDto.getLogin(), e.getMessage());
            throw e;
        }
    }

    @Override
    public PlayerGetByPlayerIdResponseDto getPlayerById(Long playerId) {
        logger.info("SERVICE: getPlayerById - Player ID: {}", playerId);
        
        try {
            Player player = findPlayerById(playerId);
            logger.info("SUCCESS: Player found - ID: {}, Login: {}, Role: {}", 
                       player.getId(), player.getLogin(), player.getRole());
            return playerMapper.toGetByIdResponseDto(player);
        } catch (Exception e) {
            logger.error("ERROR: getPlayerById failed - Player ID: {}, Error: {}", playerId, e.getMessage());
            throw e;
        }
    }

    @Override
    public PlayerUpdateResponseDto updatePlayer(Long id, PlayerUpdateRequestDto playerUpdateDto, String editorLogin) {
        logger.info("SERVICE: updatePlayer - Editor: {}, Player ID: {}, Fields to update: {}", 
                    editorLogin, id, getUpdateFieldsLog(playerUpdateDto));
        
        try {
            Player editor = getPlayerByLogin(editorLogin);
            Player playerToUpdate = findPlayerById(id);
            
            logger.debug("Update validation - Editor: {} (role: {}), Target: {} (role: {})", 
                        editor.getLogin(), editor.getRole(), 
                        playerToUpdate.getLogin(), playerToUpdate.getRole());
            
            validateUpdatePermissions(editor, playerToUpdate, editorLogin);
            updatePlayerFields(playerToUpdate, playerUpdateDto);
            
            Player updatedPlayer = playerRepository.save(playerToUpdate);
            
            logger.info("SUCCESS: Player updated - ID: {}, Login: {}, Updated fields: {}", 
                       updatedPlayer.getId(), updatedPlayer.getLogin(), 
                       getUpdatedFieldsLog(playerUpdateDto));
            
            return playerMapper.toUpdateResponseDto(updatedPlayer);
        } catch (Exception e) {
            logger.error("ERROR: updatePlayer failed - Editor: {}, Player ID: {}, Error: {}", 
                        editorLogin, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean deletePlayer(Long playerId, String editorLogin) {
        logger.info("SERVICE: deletePlayer - Editor: {}, Target Player ID: {}", editorLogin, playerId);
        
        try {
            Player editor = getPlayerByLogin(editorLogin);
            Player playerToDelete = findPlayerById(playerId);
            
            logger.debug("Delete validation - Editor: {} (role: {}), Target: {} (role: {})", 
                        editor.getLogin(), editor.getRole(), 
                        playerToDelete.getLogin(), playerToDelete.getRole());
            
            validateDeletePermissions(editor, playerToDelete);
            
            playerRepository.delete(playerToDelete);
            
            logger.info("SUCCESS: Player deleted - ID: {}, Login: {}, Role: {}", 
                       playerToDelete.getId(), playerToDelete.getLogin(), playerToDelete.getRole());
            
            return true;
        } catch (Exception e) {
            logger.error("ERROR: deletePlayer failed - Editor: {}, Player ID: {}, Error: {}", 
                        editorLogin, playerId, e.getMessage());
            throw e;
        }
    }

    @Override
    public PlayerGetAllResponseDto getAllPlayers() {
        logger.info("SERVICE: getAllPlayers");
        
        try {
            List<PlayerItem> playerItems = playerRepository.findAll()
                    .stream()
                    .map(playerMapper::toPlayerItem)
                    .collect(Collectors.toList());
            
            logger.info("SUCCESS: Retrieved {} players from database", playerItems.size());
            return new PlayerGetAllResponseDto(playerItems);
        } catch (Exception e) {
            logger.error("ERROR: getAllPlayers failed - Error: {}", e.getMessage());
            throw e;
        }
    }

    private Player getPlayerByLogin(String login) {
        logger.debug("Looking up player by login: {}", login);
        return playerRepository.findByLogin(login)
                .orElseThrow(() -> {
                    logger.warn("Player not found with login: {}", login);
                    return new NotFoundException("Player not found with login: " + login);
                });
    }

    private Player findPlayerById(Long id) {
        logger.debug("Looking up player by ID: {}", id);
        return playerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Player not found with ID: {}", id);
                    return new NotFoundException("Player not found with id: " + id);
                });
    }

    private void validateCreatePermissions(Player editor) {
        logger.debug("Validating create permissions for editor: {} (role: {})", editor.getLogin(), editor.getRole());
        if (editor.getRole() != Role.SUPERVISOR && editor.getRole() != Role.ADMIN) {
            logger.warn("Permission denied: {} (role: {}) tried to create player", editor.getLogin(), editor.getRole());
            throw new ForbiddenException("Only supervisor or admin can create players");
        }
    }

    private void validateCreateRole(String role) {
        logger.debug("Validating create role: {}", role);
        String roleLower = role.toLowerCase();
        if (!roleLower.equals("admin") && !roleLower.equals("user")) {
            logger.warn("Invalid role for creation: {}", role);
            throw new BadRequestException("Player can only be created with 'admin' or 'user' role");
        }
    }

    private void validateUniqueFields(String login, String screenName) {
        logger.debug("Validating unique fields - login: {}, screenName: {}", login, screenName);
        if (playerRepository.existsByLogin(login)) {
            logger.warn("Login already exists: {}", login);
            throw new BadRequestException("User with this login already exists");
        }
        if (playerRepository.existsByScreenName(screenName)) {
            logger.warn("Screen name already exists: {}", screenName);
            throw new BadRequestException("User with this screen name already exists");
        }
    }

    private void validateUpdatePermissions(Player editor, Player playerToUpdate, String editorLogin) {
        logger.debug("Validating update permissions - Editor: {} (role: {}), Target: {} (role: {})", 
                    editor.getLogin(), editor.getRole(), playerToUpdate.getLogin(), playerToUpdate.getRole());
        
        if (playerToUpdate.getRole() == Role.SUPERVISOR && editor.getRole() != Role.SUPERVISOR) {
            logger.warn("Permission denied: {} (role: {}) tried to update supervisor {}", 
                       editor.getLogin(), editor.getRole(), playerToUpdate.getLogin());
            throw new ForbiddenException("Only supervisor can update supervisor");
        }

        if (playerToUpdate.getRole() == Role.ADMIN && 
            editor.getRole() != Role.SUPERVISOR && 
            !editor.getId().equals(playerToUpdate.getId())) {
            logger.warn("Permission denied: {} (role: {}) tried to update admin {} (not self)", 
                       editor.getLogin(), editor.getRole(), playerToUpdate.getLogin());
            throw new ForbiddenException("Admin can only be updated by supervisor or the admin himself");
        }

        if (editor.getRole() == Role.USER && !editor.getId().equals(playerToUpdate.getId())) {
            logger.warn("Permission denied: user {} tried to update another player {}", 
                       editor.getLogin(), playerToUpdate.getLogin());
            throw new ForbiddenException("User can only update himself");
        }
        
        logger.debug("Update permissions validated successfully");
    }

    private void validateDeletePermissions(Player editor, Player playerToDelete) {
        logger.debug("Validating delete permissions - Editor: {} (role: {}), Target: {} (role: {})", 
                    editor.getLogin(), editor.getRole(), playerToDelete.getLogin(), playerToDelete.getRole());
        
        if (playerToDelete.getRole() == Role.SUPERVISOR) {
            logger.warn("Permission denied: attempted to delete supervisor {}", playerToDelete.getLogin());
            throw new ForbiddenException("Supervisor cannot be deleted");
        }

        if (playerToDelete.getRole() == Role.ADMIN && editor.getRole() != Role.SUPERVISOR) {
            logger.warn("Permission denied: {} (role: {}) tried to delete admin {}", 
                       editor.getLogin(), editor.getRole(), playerToDelete.getLogin());
            throw new ForbiddenException("Only supervisor can delete admin");
        }

        if (editor.getRole() == Role.USER) {
            logger.warn("Permission denied: user {} tried to delete player {}", 
                       editor.getLogin(), playerToDelete.getLogin());
            throw new ForbiddenException("User cannot delete any players");
        }
        
        logger.debug("Delete permissions validated successfully");
    }

    private void updatePlayerFields(Player playerToUpdate, PlayerUpdateRequestDto playerUpdateDto) {
        logger.debug("Updating player fields for player ID: {}", playerToUpdate.getId());
        
        if (playerUpdateDto.getLogin() != null && !playerUpdateDto.getLogin().equals(playerToUpdate.getLogin())) {
            logger.debug("Updating login from '{}' to '{}'", playerToUpdate.getLogin(), playerUpdateDto.getLogin());
            if (playerRepository.existsByLogin(playerUpdateDto.getLogin())) {
                logger.warn("Login update failed: '{}' already exists", playerUpdateDto.getLogin());
                throw new IllegalArgumentException("Login already exists: " + playerUpdateDto.getLogin());
            }
            playerToUpdate.setLogin(playerUpdateDto.getLogin());
        }
        
        if (playerUpdateDto.getScreenName() != null && !playerUpdateDto.getScreenName().equals(playerToUpdate.getScreenName())) {
            logger.debug("Updating screenName from '{}' to '{}'", playerToUpdate.getScreenName(), playerUpdateDto.getScreenName());
            if (playerRepository.existsByScreenName(playerUpdateDto.getScreenName())) {
                logger.warn("ScreenName update failed: '{}' already exists", playerUpdateDto.getScreenName());
                throw new IllegalArgumentException("Screen name already exists: " + playerUpdateDto.getScreenName());
            }
            playerToUpdate.setScreenName(playerUpdateDto.getScreenName());
        }
        
        if (playerUpdateDto.getPassword() != null && !playerUpdateDto.getPassword().trim().isEmpty()) {
            logger.debug("Updating password");
            playerToUpdate.setPassword(playerUpdateDto.getPassword());
        }
        
        if (playerUpdateDto.getAge() != null) {
            logger.debug("Updating age from {} to {}", playerToUpdate.getAge(), playerUpdateDto.getAge());
            playerToUpdate.setAge(playerUpdateDto.getAge());
        }
        
        if (playerUpdateDto.getGender() != null) {
            logger.debug("Updating gender from '{}' to '{}'", playerToUpdate.getGender(), playerUpdateDto.getGender());
            playerToUpdate.setGender(Gender.valueOf(playerUpdateDto.getGender()));
        }
        
        if (playerUpdateDto.getRole() != null) {
            logger.debug("Updating role from '{}' to '{}'", playerToUpdate.getRole(), playerUpdateDto.getRole());
            playerToUpdate.setRole(Role.valueOf(playerUpdateDto.getRole()));
        }
    }

    private String getUpdateFieldsLog(PlayerUpdateRequestDto dto) {
        StringBuilder fields = new StringBuilder();
        if (dto.getLogin() != null) fields.append("login,");
        if (dto.getScreenName() != null) fields.append("screenName,");
        if (dto.getPassword() != null) fields.append("password,");
        if (dto.getAge() != null) fields.append("age,");
        if (dto.getGender() != null) fields.append("gender,");
        if (dto.getRole() != null) fields.append("role,");
        return fields.length() > 0 ? fields.substring(0, fields.length() - 1) : "none";
    }
    
    private String getUpdatedFieldsLog(PlayerUpdateRequestDto dto) {
        return getUpdateFieldsLog(dto);
    }
} 
package com.playermanagement.service.impl;

import com.playermanagement.dto.request.*;
import com.playermanagement.dto.response.*;
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
    public PlayerCreateResponseDto createPlayer(PlayerCreateDto playerCreateDto) {
        logger.info("SERVICE: createPlayer - New player login: {}",
                    playerCreateDto.getLogin());
        
        try {
            
            Player player = playerMapper.toEntity(playerCreateDto);
            Player savedPlayer = playerRepository.save(player);
            
            logger.info("SUCCESS: Player created - ID: {}, Login: {}", 
                       savedPlayer.getId(), savedPlayer.getLogin());
            
            return playerMapper.toCreateResponseDto(savedPlayer);
        } catch (Exception e) {
            logger.error("ERROR: createPlayer failed - Login: {}, Error: {}",
                        playerCreateDto.getLogin(), e.getMessage());
            throw e;
        }
    }

    @Override
    public PlayerGetByPlayerIdResponseDto getPlayerById(Long playerId) {
        logger.info("SERVICE: getPlayerById - Player ID: {}", playerId);
        
        try {
            Player player = findPlayerById(playerId);
            logger.info("SUCCESS: Player found - ID: {}, Login: {}", 
                       player.getId(), player.getLogin());
            return playerMapper.toGetByIdResponseDto(player);
        } catch (Exception e) {
            logger.error("ERROR: getPlayerById failed - Player ID: {}, Error: {}", playerId, e.getMessage());
            throw e;
        }
    }

    @Override
    public PlayerUpdateResponseDto updatePlayer(Long id, PlayerUpdateRequestDto playerUpdateDto) {
        logger.info("SERVICE: updatePlayer -Player ID: {}",
                    id);
        try {
            Player playerToUpdate = findPlayerById(id);

            // Update fields if provided
            if (playerUpdateDto.getAge() != null) {
                playerToUpdate.setAge(playerUpdateDto.getAge());
            }
            if (playerUpdateDto.getLogin() != null && !playerUpdateDto.getLogin().trim().isEmpty()) {
                playerToUpdate.setLogin(playerUpdateDto.getLogin());
            }
            if (playerUpdateDto.getScreenName() != null && !playerUpdateDto.getScreenName().trim().isEmpty()) {
                playerToUpdate.setScreenName(playerUpdateDto.getScreenName());
            }
            if (playerUpdateDto.getPassword() != null && !playerUpdateDto.getPassword().trim().isEmpty()) {
                playerToUpdate.setPassword(playerUpdateDto.getPassword());
            }
            if (playerUpdateDto.getGender() != null && !playerUpdateDto.getGender().trim().isEmpty()) {
                playerToUpdate.setGender(Gender.valueOf(playerUpdateDto.getGender().toUpperCase()));
            }

            
            Player updatedPlayer = playerRepository.save(playerToUpdate);
            
            logger.info("SUCCESS: Player updated - ID: {}, Login: {}",
                       updatedPlayer.getId(), updatedPlayer.getLogin());
            
            return playerMapper.toUpdateResponseDto(updatedPlayer);
        } catch (Exception e) {
            logger.error("ERROR: updatePlayer failed - Player ID: {}, Error: {}", 
                        id, e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean deletePlayer(Long playerId) {
        logger.info("SERVICE: deletePlayer - Target Player ID: {}", playerId);
        
        try {
            Player playerToDelete = findPlayerById(playerId);

            playerRepository.delete(playerToDelete);
            
            logger.info("SUCCESS: Player deleted - ID: {}, Login: {}", 
                       playerToDelete.getId(), playerToDelete.getLogin());
            
            return true;
        } catch (Exception e) {
            logger.error("ERROR: deletePlayer failed - Player ID: {}, Error: {}", 
                       playerId, e.getMessage());
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

    private Player findPlayerById(Long id) {
        logger.debug("Looking up player by ID: {}", id);
        return playerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Player not found with ID: {}", id);
                    return new NotFoundException("Player not found with id: " + id);
                });
    }
} 
package com.playermanagement.controller;

import com.playermanagement.dto.request.*;
import com.playermanagement.dto.response.*;
import com.playermanagement.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
        logger.info("PlayerController initialized");
    }

    @PostMapping("/create/{editor}")
    public ResponseEntity<PlayerCreateResponseDto> createPlayer(
            @PathVariable String editor,
            @Valid @RequestBody PlayerCreateDto playerCreateDto) {
        
        PlayerCreateResponseDto createdPlayer = playerService.createPlayer(playerCreateDto, editor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
    }

    @GetMapping("/get")
    public ResponseEntity<PlayerGetByPlayerIdResponseDto> getPlayer(@RequestParam Long playerId) {
        PlayerGetByPlayerIdResponseDto player = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(player);
    }

    @PatchMapping("/update/{editor}/{id}")
    public ResponseEntity<PlayerUpdateResponseDto> updatePlayer(
            @PathVariable String editor,
            @PathVariable Long id,
            @Valid @RequestBody PlayerUpdateRequestDto playerUpdateDto) {
        
        logger.info("START updatePlayer - Editor: {}, Player ID: {}, Update fields: {}", 
                    editor, id, getUpdateFields(playerUpdateDto));
        
        try {
            PlayerUpdateResponseDto updatedPlayer = playerService.updatePlayer(id, playerUpdateDto, editor);
            logger.info("SUCCESS updatePlayer - Updated player: {}, New data: {}", 
                       updatedPlayer.getLogin(), getPlayerInfo(updatedPlayer));
            return ResponseEntity.ok(updatedPlayer);
        } catch (Exception e) {
            logger.error("ERROR updatePlayer - Editor: {}, Player ID: {}, Error: {}", 
                        editor, id, e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete/{editor}")
    public ResponseEntity<Boolean> deletePlayer(
            @PathVariable String editor,
            @RequestParam Long playerId) {
        
        boolean deleted = playerService.deletePlayer(playerId, editor);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/all")
    public ResponseEntity<PlayerGetAllResponseDto> getAllPlayers() {
        PlayerGetAllResponseDto players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    // Helper methods for logging
    private String getUpdateFields(PlayerUpdateRequestDto dto) {
        StringBuilder fields = new StringBuilder();
        if (dto.getScreenName() != null) fields.append("screenName,");
        if (dto.getAge() != null) fields.append("age,");
        if (dto.getGender() != null) fields.append("gender,");
        if (dto.getRole() != null) fields.append("role,");
        if (dto.getPassword() != null) fields.append("password,");
        return fields.length() > 0 ? fields.substring(0, fields.length() - 1) : "none";
    }

    private String getPlayerInfo(PlayerResponseDto player) {
        return "Login: " + player.getLogin() + ", Screen Name: " + player.getScreenName() +
               ", Age: " + player.getAge() + ", Gender: " + player.getGender() +
               ", Role: " + player.getRole();
    }
} 
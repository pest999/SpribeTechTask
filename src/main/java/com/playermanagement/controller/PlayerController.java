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

    @PostMapping("/create")
    public ResponseEntity<PlayerCreateResponseDto> createPlayer(
            @Valid @RequestBody PlayerCreateDto playerCreateDto) {
        
        PlayerCreateResponseDto createdPlayer = playerService.createPlayer(playerCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
    }

    @GetMapping("/get/{playerId}")
    public ResponseEntity<PlayerGetByPlayerIdResponseDto> getPlayer(@PathVariable Long playerId) {
        PlayerGetByPlayerIdResponseDto player = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(player);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<PlayerUpdateResponseDto> updatePlayer(
            @PathVariable Long id,
            @Valid @RequestBody PlayerUpdateRequestDto playerUpdateDto) {
        
        try {
            PlayerUpdateResponseDto updatedPlayer = playerService.updatePlayer(id, playerUpdateDto);
            logger.info("SUCCESS updatePlayer - Updated player: {}",
                       updatedPlayer.getLogin());
            return ResponseEntity.ok(updatedPlayer);
        } catch (Exception e) {
            logger.error("ERROR updatePlayer - Player ID: {}, Error: {}", 
                        id, e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deletePlayer(
            @Valid @RequestBody PlayerDeleteRequestDto deleteRequest) {
        
        boolean deleted = playerService.deletePlayer(deleteRequest.getPlayerId());
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/get/all")
    public ResponseEntity<PlayerGetAllResponseDto> getAllPlayers() {
        PlayerGetAllResponseDto players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }
} 
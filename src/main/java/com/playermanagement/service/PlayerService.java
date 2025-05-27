package com.playermanagement.service;

import com.playermanagement.dto.request.*;
import com.playermanagement.dto.response.*;
import com.playermanagement.model.Player;

public interface PlayerService {
    
    PlayerCreateResponseDto createPlayer(PlayerCreateDto playerCreateDto, String editor);
    
    PlayerGetByPlayerIdResponseDto getPlayerById(Long playerId);
    
    PlayerUpdateResponseDto updatePlayer(Long id, PlayerUpdateRequestDto playerUpdateDto, String editor);
    
    boolean deletePlayer(Long playerId, String editor);
    
    PlayerGetAllResponseDto getAllPlayers();
} 
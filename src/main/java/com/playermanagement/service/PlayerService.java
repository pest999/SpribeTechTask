package com.playermanagement.service;

import com.playermanagement.dto.request.*;
import com.playermanagement.dto.response.*;

public interface PlayerService {
    
    PlayerCreateResponseDto createPlayer(PlayerCreateDto playerCreateDto);
    
    PlayerGetByPlayerIdResponseDto getPlayerById(Long playerId);
    
    PlayerUpdateResponseDto updatePlayer(Long id, PlayerUpdateRequestDto playerUpdateDto);
    
    boolean deletePlayer(Long playerId);
    
    PlayerGetAllResponseDto getAllPlayers();
} 
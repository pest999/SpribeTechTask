package com.playermanagement.mapper;

import com.playermanagement.dto.request.PlayerCreateDto;
import com.playermanagement.dto.response.*;
import com.playermanagement.model.Player;
import com.playermanagement.model.Gender;
import com.playermanagement.model.Role;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public PlayerCreateResponseDto toCreateResponseDto(Player player) {
        if (player == null) {
            return null;
        }
        
        return new PlayerCreateResponseDto(
                player.getAge(),
                player.getGender().name().toLowerCase(),
                player.getId(),
                player.getLogin(),
                player.getRole().name().toLowerCase(),
                player.getScreenName(),
                player.getPassword()
        );
    }

    public PlayerGetByPlayerIdResponseDto toGetByIdResponseDto(Player player) {
        if (player == null) {
            return null;
        }
        
        return new PlayerGetByPlayerIdResponseDto(
                player.getAge(),
                player.getGender().name().toLowerCase(),
                player.getId(),
                player.getLogin(),
                player.getRole().name().toLowerCase(),
                player.getScreenName(),
                player.getPassword()
        );
    }

    public PlayerUpdateResponseDto toUpdateResponseDto(Player player) {
        if (player == null) {
            return null;
        }
        
        return new PlayerUpdateResponseDto(
                player.getAge(),
                player.getGender().name().toLowerCase(),
                player.getId(),
                player.getLogin(),
                player.getRole().name().toLowerCase(),
                player.getScreenName()
        );
    }

    public PlayerItem toPlayerItem(Player player) {
        if (player == null) {
            return null;
        }
        
        return new PlayerItem(
                player.getAge(),
                player.getGender().name().toLowerCase(),
                player.getId(),
                player.getRole().name().toLowerCase(),
                player.getScreenName()
        );
    }

    public Player toEntity(PlayerCreateDto playerCreateDto) {
        if (playerCreateDto == null) {
            return null;
        }
        
        return new Player(
                playerCreateDto.getLogin(),
                playerCreateDto.getScreenName(),
                playerCreateDto.getPassword(),
                playerCreateDto.getAge(),
                Gender.valueOf(playerCreateDto.getGender().toUpperCase()),
                Role.valueOf(playerCreateDto.getRole().toUpperCase())
        );
    }
} 
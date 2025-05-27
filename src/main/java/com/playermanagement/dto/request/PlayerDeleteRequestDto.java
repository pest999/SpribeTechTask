package com.playermanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDeleteRequestDto {
    
    @NotNull(message = "Player ID is required")
    private Long playerId;
} 
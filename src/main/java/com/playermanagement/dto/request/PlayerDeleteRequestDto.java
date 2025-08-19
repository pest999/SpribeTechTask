package com.playermanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDeleteRequestDto {
    
    @NotNull(message = "Player ID is required")
    private Long playerId;
} 
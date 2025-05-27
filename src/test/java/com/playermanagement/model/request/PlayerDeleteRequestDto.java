package com.playermanagement.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for player deletion request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDeleteRequestDto {
    
    private Long playerId;
    
    /**
     * Factory method for creating delete request
     */
    public static PlayerDeleteRequestDto createDeleteRequest(Long playerId) {
        return PlayerDeleteRequestDto.builder()
            .playerId(playerId)
            .build();
    }
} 
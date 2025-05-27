package com.playermanagement.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerUpdateResponseDto extends PlayerResponseDto {
    public PlayerUpdateResponseDto(Integer age, String gender, Long id, String login, String role, String screenName) {
        super(age, gender, id, login, role, screenName);
    }
} 
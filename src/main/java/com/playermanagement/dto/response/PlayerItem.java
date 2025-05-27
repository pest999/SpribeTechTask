package com.playermanagement.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerItem extends PlayerResponseDto {
    public PlayerItem(Integer age, String gender, Long id, String role, String screenName) {
        super(age, gender, id, null, role, screenName);
    }
} 
package com.playermanagement.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerCreateResponseDto extends PlayerResponseDto {
    private String password;
    
    public PlayerCreateResponseDto(Integer age, String gender, Long id, String login, String screenName, String password) {
        super(age, gender, id, login, screenName);
        this.password = password;
    }
} 
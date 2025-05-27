package com.playermanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResponseDto {
    private Integer age;
    private String gender;
    private Long id;
    private String login;
    private String role;
    private String screenName;
} 
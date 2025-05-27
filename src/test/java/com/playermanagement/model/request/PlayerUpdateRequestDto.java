package com.playermanagement.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerUpdateRequestDto {
    private Integer age;
    private String gender;
    private String login;
    private String password;
    private String role;
    private String screenName;
} 
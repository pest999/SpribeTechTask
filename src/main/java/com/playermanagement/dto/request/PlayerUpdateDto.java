package com.playermanagement.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerUpdateDto {
    private String screenName;
    private String password;
    private Integer age;
    private String gender;
    private String role;
} 
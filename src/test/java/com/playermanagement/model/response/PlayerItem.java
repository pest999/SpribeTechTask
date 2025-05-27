package com.playermanagement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerItem {
    private Integer age;
    private String gender;
    private Long id;
    private String role;
    private String screenName;
} 
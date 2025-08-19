package com.playermanagement.utils;

import com.github.javafaker.Faker;
import com.playermanagement.dto.request.PlayerUpdateRequestDto;

public class PlayerUpdateRequestBuilder {
    
    private static final Faker faker = new Faker();
    
    private PlayerUpdateRequestBuilder() {
    }
    
    public static PlayerUpdateRequestDto createValidUpdateRequest() {
        return PlayerUpdateRequestDto.builder()
            .screenName(faker.name().firstName())
            .age(faker.number().numberBetween(16, 60))
            .gender("MALE")
            .role("USER")
            .password(generateValidPassword())
            .build();
    }
    
    public static PlayerUpdateRequestDto createMinimalUpdateRequest() {
        return PlayerUpdateRequestDto.builder()
            .screenName(faker.name().firstName())
            .age(faker.number().numberBetween(20, 50))
            .build();
    }
    
    public static PlayerUpdateRequestDto createUpdateRequestWithRole(String role) {
        return PlayerUpdateRequestDto.builder()
            .screenName(faker.name().firstName())
            .age(faker.number().numberBetween(16, 60))
            .gender("MALE")
            .role(role)
            .build();
    }
    
    private static String generateValidPassword() {
        String letters = faker.lorem().characters(4, 8, false, false);
        String digits = String.valueOf(faker.number().numberBetween(100, 999));
        return letters + digits;
    }
} 
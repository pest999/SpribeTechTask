package com.playermanagement.utils;

import com.github.javafaker.Faker;
import com.playermanagement.dto.request.PlayerCreateDto;

public class ValidationDataProvider {
    private static final Faker faker = new Faker();
    
    private ValidationDataProvider() {
    }
    
    public static PlayerCreateDto getRequestWithEmptyLogin() {
        PlayerCreateDto request = PlayerRequestBuilder.createUserRequestPojo();
        request.setLogin(null);
        return request;
    }
    
    public static PlayerCreateDto getRequestWithEmptyScreenName() {
        PlayerCreateDto request = PlayerRequestBuilder.createUserRequestPojo();
        request.setScreenName(null);
        return request;
    }
    
    public static PlayerCreateDto getRequestWithShortPassword() {
        PlayerCreateDto request = PlayerRequestBuilder.createUserRequestPojo();
        request.setPassword("pass1");
        return request;
    }
    
    public static PlayerCreateDto getRequestWithAgeBelowMinimum() {
        PlayerCreateDto request = PlayerRequestBuilder.createUserRequestPojo();
        request.setAge(15);
        return request;
    }
    
    public static PlayerCreateDto getRequestWithAgeAboveMaximum() {
        PlayerCreateDto request = PlayerRequestBuilder.createUserRequestPojo();
        request.setAge(61);
        return request;
    }
    
    public static PlayerCreateDto getRequestWithInvalidGender() {
        PlayerCreateDto request = PlayerRequestBuilder.createUserRequestPojo();
        request.setGender("invalid");
        return request;
    }
    
    // Role validation removed - no role field in DTO
    // public static PlayerCreateDto getRequestWithInvalidRole() {
    //     PlayerCreateDto request = PlayerRequestBuilder.createUserRequestPojo();
    //     request.setRole("INVALID_ROLE");
    //     return request;
    // }
} 
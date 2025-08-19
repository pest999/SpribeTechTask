package com.playermanagement.utils;

import com.github.javafaker.Faker;
import com.playermanagement.dto.request.PlayerUpdateRequestDto;

public class ValidationUpdateDataProvider {
    private static final Faker faker = new Faker();
    
    private ValidationUpdateDataProvider() {
    }
    
    public static PlayerUpdateRequestDto getRequestWithAgeBelowMinimum() {
        PlayerUpdateRequestDto request = PlayerUpdateRequestBuilder.createValidUpdateRequest();
        request.setAge(15);
        return request;
    }
    
    public static PlayerUpdateRequestDto getRequestWithAgeAboveMaximum() {
        PlayerUpdateRequestDto request = PlayerUpdateRequestBuilder.createValidUpdateRequest();
        request.setAge(61);
        return request;
    }
    
    public static PlayerUpdateRequestDto getRequestWithInvalidGender() {
        PlayerUpdateRequestDto request = PlayerUpdateRequestBuilder.createValidUpdateRequest();
        request.setGender("invalid");
        return request;
    }
    
    public static PlayerUpdateRequestDto getRequestWithInvalidPassword() {
        PlayerUpdateRequestDto request = PlayerUpdateRequestBuilder.createValidUpdateRequest();
        request.setPassword("short");
        return request;
    }
    
    public static PlayerUpdateRequestDto getRequestWithInvalidRole() {
        PlayerUpdateRequestDto request = PlayerUpdateRequestBuilder.createValidUpdateRequest();
        request.setRole("INVALID_ROLE");
        return request;
    }
} 
package com.playermanagement.utils;

import com.github.javafaker.Faker;
import com.playermanagement.model.request.PlayerCreateRequest;

public class ValidationDataProvider {
    private static final Faker faker = new Faker();
    
    private ValidationDataProvider() {
    }
    
    public static PlayerCreateRequest getRequestWithEmptyLogin() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        request.setLogin(null);
        return request;
    }
    
    public static PlayerCreateRequest getRequestWithEmptyScreenName() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        request.setScreenName(null);
        return request;
    }
    
    public static PlayerCreateRequest getRequestWithShortPassword() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        request.setPassword("pass1");
        return request;
    }
    
    public static PlayerCreateRequest getRequestWithAgeBelowMinimum() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        request.setAge(15);
        return request;
    }
    
    public static PlayerCreateRequest getRequestWithAgeAboveMaximum() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        request.setAge(61);
        return request;
    }
    
    public static PlayerCreateRequest getRequestWithInvalidGender() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        request.setGender("invalid");
        return request;
    }
    
    public static PlayerCreateRequest getRequestWithInvalidRole() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        request.setRole("INVALID_ROLE");
        return request;
    }
} 
package com.playermanagement.util;

public final class ValidationConstants {
    
    public static final String LOGIN_REQUIRED = "Login is required";
    public static final String SCREEN_NAME_REQUIRED = "Screen name is required";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_SIZE = "Password must be between 7 and 15 characters";
    public static final String PASSWORD_PATTERN = "Password must contain latin letters and numbers, min 7 max 15 characters";
    public static final String AGE_MIN = "Age should be at least 16";
    public static final String AGE_MAX = "Age should be less than 60";
    public static final String GENDER_REQUIRED = "Gender is required";

    
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{7,15}$";

} 
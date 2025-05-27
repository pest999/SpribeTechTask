package com.playermanagement.utils;

import com.github.javafaker.Faker;
import com.playermanagement.model.request.PlayerCreateRequest;
import com.playermanagement.model.request.PlayerDeleteRequestDto;

public class PlayerRequestBuilder {
    
    private static final Faker faker = new Faker();
    
    private PlayerRequestBuilder() {
    }
    
    public static PlayerCreateRequest createUserRequestPojo() {
        return PlayerCreateRequest.createUser(
            faker.name().username(),
            faker.name().firstName(),
            generateValidPassword(),
            faker.number().numberBetween(16, 60),
            "male"
        );
    }
    
    /**
     * Creates PlayerCreateRequest for ADMIN with unique data
     */
    public static PlayerCreateRequest createAdminRequestPojo() {
        return PlayerCreateRequest.createAdmin(
            faker.name().username(),
            faker.name().firstName(),
            generateValidPassword(),
            faker.number().numberBetween(16, 60),
            "female"
        );
    }
    
    /**
     * Creates PlayerCreateRequest for TC-R-01
     */
    public static PlayerCreateRequest createTcR01RequestPojo() {
        return PlayerCreateRequest.createTcR01(
            faker.name().username(),
            faker.name().firstName()
        );
    }
    
    /**
     * Creates PlayerCreateRequest for TC-R-02 (ADMIN role)
     */
    public static PlayerCreateRequest createTcR02RequestPojo() {
        return PlayerCreateRequest.createAdmin(
            faker.name().username(),
            faker.name().firstName(),
            generateValidPassword(),
            faker.number().numberBetween(25, 50),
            "female"
        );
    }
    
    /**
     * Creates PlayerCreateRequest for TC-R-03 (USER role by Admin)
     */
    public static PlayerCreateRequest createTcR03RequestPojo() {
        return PlayerCreateRequest.createUser(
            faker.name().username(),
            faker.name().firstName(),
            generateValidPassword(),
            faker.number().numberBetween(20, 40),
            "female"
        );
    }
    
    /**
     * Creates PlayerCreateRequest for TC-R-04 (SUPERVISOR role - negative test)
     */
    public static PlayerCreateRequest createTcR04RequestPojo() {
        return PlayerCreateRequest.builder()
            .login(faker.name().username())
            .screenName(faker.name().firstName())
            .password(generateValidPassword())
            .age(faker.number().numberBetween(25, 45))
            .gender("male")
            .role("SUPERVISOR")
            .build();
    }
    
    /**
     * Creates PlayerCreateRequest for TC-R-05 (USER role by User - negative test)
     */
    public static PlayerCreateRequest createTcR05RequestPojo() {
        return PlayerCreateRequest.createUser(
            faker.name().username(),
            faker.name().firstName(),
            generateValidPassword(),
            faker.number().numberBetween(18, 35),
            "female"
        );
    }
    
    /**
     * Creates PlayerDeleteRequestDto for TC-R-06 (Delete supervisor - negative test)
     */
    public static PlayerDeleteRequestDto createTcR06RequestPojo() {
        return PlayerDeleteRequestDto.createDeleteRequest(1L);
    }
    
    private static String generateValidPassword() {
        String letters = faker.lorem().characters(4, 8, false, false);
        String digits = String.valueOf(faker.number().numberBetween(100, 999));
        return letters + digits;
    }
} 
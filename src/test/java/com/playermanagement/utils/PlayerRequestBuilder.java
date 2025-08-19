package com.playermanagement.utils;

import com.github.javafaker.Faker;
import com.playermanagement.dto.request.PlayerCreateDto;
import com.playermanagement.dto.request.PlayerDeleteRequestDto;

public class PlayerRequestBuilder {
    
    private static final Faker faker = new Faker();
    
    private PlayerRequestBuilder() {
    }
    
    public static PlayerCreateDto createUserRequestPojo() {
        return PlayerCreateDto.builder()
            .login(faker.name().username())
            .screenName(faker.name().firstName())
            .password(generateValidPassword())
            .age(faker.number().numberBetween(16, 60))
            .gender("MALE")
            .build();
    }
    
    /**
     * Creates PlayerCreateDto for ADMIN with unique data
     */
    public static PlayerCreateDto createAdminRequestPojo() {
        return PlayerCreateDto.builder()
            .login(faker.name().username())
            .screenName(faker.name().firstName())
            .password(generateValidPassword())
            .age(faker.number().numberBetween(16, 60))
            .gender("FEMALE")
            .build();
    }
    
    /**
     * Creates PlayerCreateDto for TC-R-01
     */
    public static PlayerCreateDto createTcR01RequestPojo() {
        return PlayerCreateDto.builder()
            .login(faker.name().username())
            .screenName(faker.name().firstName())
            .password("pass12345")
            .age(25)
            .gender("MALE")
            .build();
    }
    
    /**
     * Creates PlayerCreateDto for TC-R-02 (ADMIN role)
     */
    public static PlayerCreateDto createTcR02RequestPojo() {
        return PlayerCreateDto.builder()
            .login(faker.name().username())
            .screenName(faker.name().firstName())
            .password(generateValidPassword())
            .age(faker.number().numberBetween(25, 50))
            .gender("FEMALE")
            .build();
    }

    /**
     * Creates PlayerCreateDto for TC-R-03 (USER role by Admin)
     */
    public static PlayerCreateDto createTcR03RequestPojo() {
        return PlayerCreateDto.builder()
            .login(faker.name().username())
            .screenName(faker.name().firstName())
            .password(generateValidPassword())
            .age(faker.number().numberBetween(20, 40))
            .gender("FEMALE")
            .build();
    }

    /**
     * Creates PlayerCreateDto for TC-R-04 (SUPERVISOR role - negative test)
     */
    public static PlayerCreateDto createTcR04RequestPojo() {
        return PlayerCreateDto.builder()
            .login(faker.name().username())
            .screenName(faker.name().firstName())
            .password(generateValidPassword())
            .age(faker.number().numberBetween(25, 45))
            .gender("MALE")
            .build();
    }

    /**
     * Creates PlayerCreateDto for TC-R-05 (USER role by User - negative test)
     */
    public static PlayerCreateDto createTcR05RequestPojo() {
        return PlayerCreateDto.builder()
            .login(faker.name().username())
            .screenName(faker.name().firstName())
            .password(generateValidPassword())
            .age(faker.number().numberBetween(20, 40))
            .gender("FEMALE")
            .build();
    }
    
    /**
     * Creates PlayerDeleteRequestDto for TC-R-06 (Delete supervisor - negative test)
     */
    public static PlayerDeleteRequestDto createTcR06RequestPojo() {
        return PlayerDeleteRequestDto.builder()
            .playerId(1L)
            .build();
    }
    
    private static String generateValidPassword() {
        String letters = faker.lorem().characters(4, 8, false, false);
        String digits = String.valueOf(faker.number().numberBetween(100, 999));
        return letters + digits;
    }
} 
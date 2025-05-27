package com.playermanagement.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO for user creation request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerCreateRequest {
    
    private String login;
    private String screenName;
    private String password;
    private Integer age;
    private String gender;
    private String role;
    
    /**
     * Factory method for creating USER
     */
    public static PlayerCreateRequest createUser(String login, String screenName, String password, 
                                               Integer age, String gender) {
        return PlayerCreateRequest.builder()
            .login(login)
            .screenName(screenName)
            .password(password)
            .age(age)
            .gender(gender)
            .role("USER")
            .build();
    }
    
    /**
     * Factory method for creating ADMIN
     */
    public static PlayerCreateRequest createAdmin(String login, String screenName, String password, 
                                                Integer age, String gender) {
        return PlayerCreateRequest.builder()
            .login(login)
            .screenName(screenName)
            .password(password)
            .age(age)
            .gender(gender)
            .role("ADMIN")
            .build();
    }
    
    /**
     * Factory method for TC-R-01 specific data
     */
    public static PlayerCreateRequest createTcR01(String login, String screenName) {
        return PlayerCreateRequest.builder()
            .login(login)
            .screenName(screenName)
            .password("pass12345")
            .age(25)
            .gender("male")
            .role("USER")
            .build();
    }
} 
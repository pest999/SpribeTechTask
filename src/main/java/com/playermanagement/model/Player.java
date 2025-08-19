package com.playermanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "players", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "login"),
           @UniqueConstraint(columnNames = "screen_name")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Login cannot be blank")
    @Size(min = 1, max = 255, message = "Login must be between 1 and 255 characters")
    @Column(name = "login", nullable = false, unique = true, length = 255)
    private String login;

    @NotBlank(message = "Screen name cannot be blank")
    @Size(min = 1, max = 255, message = "Screen name must be between 1 and 255 characters")
    @Column(name = "screen_name", nullable = false, unique = true, length = 255)
    private String screenName;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 7, max = 15, message = "Password must be between 7 and 15 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{7,15}$", 
             message = "Password must contain latin letters and numbers, min 7 max 15 characters")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Min(value = 16, message = "Age should be at least 16")
    @Max(value = 60, message = "Age should be less than 60")
    @Column(name = "age", nullable = false)
    private int age;

    @NotNull(message = "Gender cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;



    public Player(String login, String screenName, String password, int age, Gender gender) {
        this.login = login;
        this.screenName = screenName;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }
} 
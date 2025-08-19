package com.playermanagement.dto.request;

import com.playermanagement.util.ValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerCreateDto {

    @NotBlank(message = ValidationConstants.LOGIN_REQUIRED)
    private String login;

    @NotBlank(message = ValidationConstants.SCREEN_NAME_REQUIRED)
    private String screenName;

    @NotBlank(message = ValidationConstants.PASSWORD_REQUIRED)
    @Size(min = 7, max = 15, message = ValidationConstants.PASSWORD_SIZE)
    @Pattern(regexp = ValidationConstants.PASSWORD_REGEX, 
             message = ValidationConstants.PASSWORD_PATTERN)
    private String password;

    @Min(value = 16, message = ValidationConstants.AGE_MIN)
    @Max(value = 60, message = ValidationConstants.AGE_MAX)
    private int age;

    @NotNull(message = ValidationConstants.GENDER_REQUIRED)
    private String gender;
} 
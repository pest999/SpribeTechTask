package com.playermanagement.tests;

import com.playermanagement.api.ApiCalls;
import com.playermanagement.api.ApiEndpoints;
import com.playermanagement.api.UserRole;
import com.playermanagement.config.TestConstants;
import com.playermanagement.dto.request.PlayerCreateDto;
import com.playermanagement.dto.request.PlayerUpdateRequestDto;
import com.playermanagement.dto.response.PlayerCreateResponseDto;
import com.playermanagement.utils.PlayerRequestBuilder;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Epic("Player Management API")
@Feature("Player Validation Tests")
public class PlayerValidationParameterizedTests extends BaseTest {

    // Authorization tests removed - no role-based access control implemented

    @Test
    @Tag("regression")
    @CsvSource({
        "15, Age should be at least 16",
        "61, Age should be less than 60"
    })
    @DisplayName("TC-P-02: Age validation tests")
    void testAgeValidation(int age, String expectedError) {
        PlayerCreateDto request = PlayerRequestBuilder.createUserRequestPojo();
        request.setAge(age);
        log.info("Testing age validation with age: {}", age);
        
        Response response = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request
        );
        
        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_BAD_REQUEST);
        assertThat(response.getBody().asString()).contains(expectedError);
        log.info("Age validation error received as expected: {}", expectedError);
    }

    @Test
    @Tag("regression")
    @MethodSource("invalidPlayerData")
    @DisplayName("TC-P-03: Invalid player data validation")
    void testInvalidPlayerData(String login, String password, String gender, String expectedError) {
        PlayerCreateDto request = PlayerRequestBuilder.createUserRequestPojo();
        request.setLogin(login);
        request.setPassword(password);
        request.setGender(gender);
        log.info("Testing invalid player data: login={}, password={}, gender={}", login, password, gender);
        
        Response response = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request
        );
        
        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_BAD_REQUEST);
        assertThat(response.getBody().asString()).contains(expectedError);
        log.info("Validation error received as expected: {}", expectedError);
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> invalidPlayerData() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of("", "ValidPass123", "MALE", "Login cannot be empty"),
            org.junit.jupiter.params.provider.Arguments.of("user1", "short", "MALE", "Password must be at least 8 characters"),
            org.junit.jupiter.params.provider.Arguments.of("user1", "ValidPass123", "invalid", "Invalid gender value")
        );
    }

    @Test
    @Tag("regression")
    @ValueSource(longs = {-1L, 0L, 99999L})
    @DisplayName("TC-P-04: Get player by invalid ID")
    void testGetPlayerByInvalidId(long invalidId) {
        log.info("Testing get player with invalid ID: {}", invalidId);
        
        Response response = ApiCalls.get(
            ApiEndpoints.GET_PLAYER_BY_ID,
            invalidId
        );
        
        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_NOT_FOUND);
        log.info("Not found status received as expected for ID: {}", invalidId);
    }

    private PlayerCreateResponseDto createTestUser() {
        PlayerCreateDto request = PlayerRequestBuilder.createUserRequestPojo();
        Response response = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request
        );
        return response.as(PlayerCreateResponseDto.class);
    }
} 
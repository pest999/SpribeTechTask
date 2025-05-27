package com.playermanagement.tests;

import com.playermanagement.api.ApiCalls;
import com.playermanagement.api.ApiEndpoints;
import com.playermanagement.api.UserRole;
import com.playermanagement.config.TestConstants;
import com.playermanagement.model.request.PlayerCreateRequest;
import com.playermanagement.utils.PlayerRequestBuilder;
import com.playermanagement.utils.ValidationDataProvider;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Epic("Player Management API")
@Feature("Player Creation Validation Tests")
public class PlayerCreateValidationTests extends BaseTest {

    @Test
    @Tag("validation")
    @DisplayName("TC-V-01: Validation test - Empty login")
    void testEmptyLoginValidation() {
        PlayerCreateRequest request = ValidationDataProvider.getRequestWithEmptyLogin();
        verifyValidationError(request);
    }

    @Test
    @Tag("validation")
    @DisplayName("TC-V-02: Validation test - Empty screen name")
    void testEmptyScreenNameValidation() {
        PlayerCreateRequest request = ValidationDataProvider.getRequestWithEmptyScreenName();
        verifyValidationError(request);
    }

    @Test
    @Tag("validation")
    @DisplayName("TC-V-03: Validation test - Short password")
    void testShortPasswordValidation() {
        PlayerCreateRequest request = ValidationDataProvider.getRequestWithShortPassword();
        verifyValidationError(request);
    }

    @Test
    @Tag("validation")
    @DisplayName("TC-V-04: Validation test - Age below minimum")
    void testAgeBelowMinimumValidation() {
        PlayerCreateRequest request = ValidationDataProvider.getRequestWithAgeBelowMinimum();
        verifyValidationError(request);
    }

    @Test
    @Tag("validation")
    @DisplayName("TC-V-05: Validation test - Age above maximum")
    void testAgeAboveMaximumValidation() {
        PlayerCreateRequest request = ValidationDataProvider.getRequestWithAgeAboveMaximum();
        verifyValidationError(request);
    }

    @Test
    @Tag("validation")
    @DisplayName("TC-V-06: Validation test - Invalid gender")
    void testInvalidGenderValidation() {
        PlayerCreateRequest request = ValidationDataProvider.getRequestWithInvalidGender();
        verifyValidationError(request);
    }

    @Test
    @Tag("validation")
    @DisplayName("TC-V-07: Validation test - Invalid role")
    void testInvalidRoleValidation() {
        PlayerCreateRequest request = ValidationDataProvider.getRequestWithInvalidRole();
        verifyValidationError(request);
    }

    @Test
    @Tag("validation")
    @DisplayName("TC-V-08: Validation test - Valid data")
    void testValidDataValidation() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        verifySuccessfulCreation(request);
    }

    @Step("Send player creation request")
    private Response sendPlayerCreationRequest(PlayerCreateRequest request) {
        log.info("Sending player creation request: {}", request);
        return ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request,
            UserRole.SUPERVISOR
        );
    }

    @Step("Verify validation error")
    private void verifyValidationError(PlayerCreateRequest request) {
        Response response = sendPlayerCreationRequest(request);
        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_BAD_REQUEST);
        String errorMessage = response.getBody().asString();
        assertThat(errorMessage).isNotNull().isNotEmpty();
        log.info("Validation error received: {}", errorMessage);
    }

    @Step("Verify successful player creation")
    private void verifySuccessfulCreation(PlayerCreateRequest request) {
        Response response = sendPlayerCreationRequest(request);
        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_CREATED);
        log.info("Successfully created player with valid data");
    }
}
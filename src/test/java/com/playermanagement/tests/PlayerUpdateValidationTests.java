package com.playermanagement.tests;

import com.playermanagement.api.ApiCalls;
import com.playermanagement.api.ApiEndpoints;
import com.playermanagement.api.UserRole;
import com.playermanagement.config.TestConstants;
import com.playermanagement.dto.request.PlayerUpdateRequestDto;
import com.playermanagement.dto.request.PlayerCreateDto;
import com.playermanagement.dto.response.PlayerCreateResponseDto;
import com.playermanagement.utils.PlayerRequestBuilder;
import com.playermanagement.utils.ValidationUpdateDataProvider;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Epic("Player Management API")
@Feature("Player Update Validation Tests")
public class PlayerUpdateValidationTests extends BaseTest {

    private PlayerCreateResponseDto testPlayer;

    @BeforeEach
    void setUp() {
        PlayerCreateDto createRequest = PlayerRequestBuilder.createUserRequestPojo();
        Response createResponse = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            createRequest,
            UserRole.SUPERVISOR
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_CREATED);
        testPlayer = createResponse.as(PlayerCreateResponseDto.class);
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-UV-01: Update validation - Age below minimum")
    void testAgeBelowMinimumValidation() {
        PlayerUpdateRequestDto updateRequest = ValidationUpdateDataProvider.getRequestWithAgeBelowMinimum();
        verifyValidationError(updateRequest);
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-UV-02: Update validation - Age above maximum")
    void testAgeAboveMaximumValidation() {
        PlayerUpdateRequestDto updateRequest = ValidationUpdateDataProvider.getRequestWithAgeAboveMaximum();
        verifyValidationError(updateRequest);
    }





    @Step("Verify validation error for update request")
    private void verifyValidationError(PlayerUpdateRequestDto request) {
        Response response = ApiCalls.patch(
            ApiEndpoints.UPDATE_PLAYER,
            request,
            UserRole.SUPERVISOR,
            testPlayer.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_BAD_REQUEST);
        String errorMessage = response.getBody().asString();
        assertThat(errorMessage).isNotNull().isNotEmpty();
        log.info("Validation error received: {}", errorMessage);
    }
} 
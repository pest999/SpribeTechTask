package com.playermanagement.tests;

import com.playermanagement.api.ApiCalls;
import com.playermanagement.api.ApiEndpoints;
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
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Epic("Player Management API")
@Feature("User Basic CRUD Tests")
public class UserTests extends BaseTest {

    @Test
    @Tag("regression")
    @DisplayName("TC-U-01: User updates their own data")
    void testUserUpdatesSelf() {
        PlayerCreateResponseDto testUser = createTestUser();
        log.info("Created test user with ID: {}", testUser.getId());

        PlayerUpdateRequestDto updateRequest = PlayerUpdateRequestDto.builder()
            .screenName("Updated Screen Name")
            .build();

        Response response = ApiCalls.patch(
            ApiEndpoints.UPDATE_PLAYER,
            updateRequest,
            testUser.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_OK);
        PlayerCreateResponseDto updatedUser = response.as(PlayerCreateResponseDto.class);
        assertThat(updatedUser.getScreenName()).isEqualTo("Updated Screen Name");
        log.info("User successfully updated their own data");
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

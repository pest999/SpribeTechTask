package com.playermanagement.tests;

import com.playermanagement.api.ApiCalls;
import com.playermanagement.api.ApiEndpoints;
import com.playermanagement.config.TestConstants;
import com.playermanagement.model.request.PlayerCreateRequest;
import com.playermanagement.model.request.PlayerUpdateRequestDto;
import com.playermanagement.model.response.PlayerCreateResponse;
import com.playermanagement.model.response.PlayerUpdateResponseDto;
import com.playermanagement.utils.PlayerRequestBuilder;
import com.playermanagement.utils.PlayerUpdateRequestBuilder;
import com.playermanagement.util.ValidationConstants;
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
@Feature("Player Update Tests")
public class PlayerUpdateTests extends BaseTest {

    private static final String SUPERVISOR_LOGIN = "supervisorLogin";

    @Test
    @Tag("regression")
    @DisplayName("TC-U-01: Update player - successful (200 OK)")
    void testSuccessfulUpdate() {
        // Create a player first
        PlayerCreateRequest createRequest = PlayerRequestBuilder.createUserRequestPojo();
        Response createResponse = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            createRequest,
            SUPERVISOR_LOGIN
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_CREATED);
        PlayerCreateResponse createdPlayer = createResponse.as(PlayerCreateResponse.class);

        // Update the player with random valid data
        PlayerUpdateRequestDto updateRequest = PlayerUpdateRequestBuilder.createValidUpdateRequest();

        Response updateResponse = ApiCalls.patch(
            ApiEndpoints.UPDATE_PLAYER,
            updateRequest,
            SUPERVISOR_LOGIN,
            createdPlayer.getId()
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_OK);
        PlayerUpdateResponseDto updatedPlayer = updateResponse.as(PlayerUpdateResponseDto.class);
        
        assertThat(updatedPlayer)
            .satisfies(player -> {
                assertThat(player.getId()).isEqualTo(createdPlayer.getId());
                assertThat(player.getScreenName()).isEqualTo(updateRequest.getScreenName());
                assertThat(player.getAge()).isEqualTo(updateRequest.getAge());
                assertThat(player.getGender()).isEqualTo(updateRequest.getGender());
                assertThat(player.getRole()).isEqualTo(updateRequest.getRole().toLowerCase());
            });
        
        log.info("Successfully updated player with ID: {}", updatedPlayer.getId());
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-U-02: Update player - no changes (204 No Content)")
    void testUpdateWithNoChanges() {
        // Create a player first
        PlayerCreateRequest createRequest = PlayerRequestBuilder.createUserRequestPojo();
        Response createResponse = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            createRequest,
            SUPERVISOR_LOGIN
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_CREATED);
        PlayerCreateResponse createdPlayer = createResponse.as(PlayerCreateResponse.class);

        // Update with same data
        PlayerUpdateRequestDto updateRequest = PlayerUpdateRequestDto.builder()
            .screenName(createdPlayer.getScreenName())
            .age(createdPlayer.getAge())
            .gender(createdPlayer.getGender())
            .role(createdPlayer.getRole())
            .build();

        Response updateResponse = ApiCalls.patch(
            ApiEndpoints.UPDATE_PLAYER,
            updateRequest,
            SUPERVISOR_LOGIN,
            createdPlayer.getId()
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_NO_CONTENT);
        log.info("Received expected No Content response for unchanged update");
    }
} 
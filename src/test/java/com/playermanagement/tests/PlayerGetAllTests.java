package com.playermanagement.tests;

import com.playermanagement.api.ApiCalls;
import com.playermanagement.api.ApiEndpoints;
import com.playermanagement.config.TestConstants;
import com.playermanagement.dto.request.PlayerCreateDto;
import com.playermanagement.dto.response.PlayerCreateResponseDto;
import com.playermanagement.dto.response.PlayerGetAllResponseDto;
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
@Feature("Player GetAll Tests")
public class PlayerGetAllTests extends BaseTest {

    private static final String SUPERVISOR_LOGIN = "supervisorLogin";

    @Test
    @Tag("regression")
    @DisplayName("TC-G-01: Get all players - successful")
    void testGetAllPlayers() {
        // Create a test player first
        PlayerCreateDto createRequest = PlayerRequestBuilder.createUserRequestPojo();
        Response createResponse = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            createRequest,
            SUPERVISOR_LOGIN
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_CREATED);
        PlayerCreateResponseDto createdPlayer = createResponse.as(PlayerCreateResponseDto.class);

        // Get all players
        Response getAllResponse = ApiCalls.get(ApiEndpoints.GET_ALL_PLAYERS);
        assertThat(getAllResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_OK);

        PlayerGetAllResponseDto allPlayers = getAllResponse.as(PlayerGetAllResponseDto.class);
        
        assertThat(allPlayers.getPlayers())
            .isNotEmpty()
            .anySatisfy(player -> {
                assertThat(player.getId()).isEqualTo(createdPlayer.getId());
                assertThat(player.getScreenName()).isEqualTo(createdPlayer.getScreenName());
                assertThat(player.getAge()).isEqualTo(createdPlayer.getAge());
                assertThat(player.getGender()).isEqualTo(createdPlayer.getGender());
            });

        log.info("Successfully retrieved {} players", allPlayers.getPlayers().size());
    }
} 
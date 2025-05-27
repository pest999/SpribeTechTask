package com.playermanagement.tests;

import com.playermanagement.api.ApiCalls;
import com.playermanagement.api.ApiEndpoints;
import com.playermanagement.api.UserRole;
import com.playermanagement.config.TestConstants;
import com.playermanagement.utils.PlayerRequestBuilder;
import com.playermanagement.model.request.PlayerCreateRequest;
import com.playermanagement.model.response.PlayerCreateResponse;
import com.playermanagement.model.request.PlayerDeleteRequestDto;
import com.playermanagement.model.request.PlayerUpdateDto;
import com.playermanagement.model.response.PlayerUpdateResponseDto;
import com.playermanagement.model.response.PlayerGetByPlayerIdResponseDto;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Epic("Player Management API")
@Feature("Supervisor Role Tests")
public class SupervisorTests extends BaseTest {

    @Test
    @Tag("regression")
    @DisplayName("TC-R-01: Supervisor creates user with USER role")
    void testSupervisorCreatesUser() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        log.info("Request POJO prepared: {}", request);

        Response response = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request,
            UserRole.SUPERVISOR
        );

        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_CREATED);
        PlayerCreateResponse responseData = response.as(PlayerCreateResponse.class);
        verifyPlayerData(responseData, request);
        log.info("User successfully created with ID: {}", responseData.getId());
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-R-02: Supervisor creates user with ADMIN role")
    void testSupervisorCreatesAdmin() {
        PlayerCreateRequest request = PlayerRequestBuilder.createAdminRequestPojo();
        log.info("Request POJO prepared: {}", request);

        Response response = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request,
            UserRole.SUPERVISOR
        );

        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_CREATED);
        PlayerCreateResponse responseData = response.as(PlayerCreateResponse.class);
        verifyPlayerData(responseData, request);
        log.info("Admin successfully created with ID: {}", responseData.getId());
    }

    @Step("Verify player data")
    private void verifyPlayerData(PlayerCreateResponse actual, PlayerCreateRequest expected) {
        assertThat(actual.getLogin()).isEqualTo(expected.getLogin());
        assertThat(actual.getScreenName()).isEqualTo(expected.getScreenName());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
        assertThat(actual.getGender()).isEqualTo(expected.getGender());
        assertThat(actual.getRole()).isEqualTo(expected.getRole().toLowerCase());
    }
    
    @Test
    @Tag("regression")
    @DisplayName("TC-R-06: Supervisor attempts to delete supervisor user (negative)")
    void testSupervisorCannotDeleteSupervisor() {
        PlayerDeleteRequestDto deleteRequest = PlayerRequestBuilder.createTcR06RequestPojo();
        log.info("Delete request DTO prepared: {}", deleteRequest);
        
        Response response = ApiCalls.delete(
            ApiEndpoints.DELETE_PLAYER, 
            deleteRequest,
            UserRole.SUPERVISOR
        );
        
        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_FORBIDDEN);
        assertThat(response.getBody().asString()).contains("Supervisor cannot be deleted");
        
        log.info("Supervisor correctly denied deleting supervisor user. Status: {}, Message: {}", 
                response.getStatusCode(), response.getBody().asString());
    }

    @Test
    @Tag("smoke")
    @DisplayName("TC-R-12: Get all players (positive)")
    void testGetAllPlayers() {
        PlayerCreateRequest userRequest = PlayerRequestBuilder.createUserRequestPojo();
        Response createResponse = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            userRequest,
            UserRole.SUPERVISOR
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_CREATED);
        
        Response getAllResponse = ApiCalls.get(ApiEndpoints.GET_ALL_PLAYERS);
        
        assertThat(getAllResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_OK);
        List<PlayerItem> players = getAllResponse.jsonPath().getList("players", PlayerItem.class);
        assertThat(players).isNotEmpty();
        log.info("Successfully retrieved {} players", players.size());
    }

    @Test
    @Tag("smoke")
    @DisplayName("TC-R-13: Get player by ID (positive)")
    void testGetPlayerById() {
        PlayerCreateRequest userRequest = PlayerRequestBuilder.createUserRequestPojo();
        Response createResponse = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            userRequest,
            UserRole.SUPERVISOR
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_CREATED);
        PlayerCreateResponse createdUser = createResponse.as(PlayerCreateResponse.class);
        
        Response getResponse = ApiCalls.get(ApiEndpoints.GET_PLAYER_BY_ID + "/" + createdUser.getId());
        
        assertThat(getResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_OK);
        PlayerGetByPlayerIdResponseDto player = getResponse.as(PlayerGetByPlayerIdResponseDto.class);
        assertThat(player.getId()).isEqualTo(createdUser.getId());
        assertThat(player.getLogin()).isEqualTo(createdUser.getLogin());
        log.info("Successfully retrieved player with ID: {}", player.getId());
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-R-14: Get player by non-existent ID (negative)")
    void testGetPlayerByNonExistentId() {
        Long nonExistentId = 99999L;
        Response getResponse = ApiCalls.get(ApiEndpoints.GET_PLAYER_BY_ID + "/" + nonExistentId);
        
        assertThat(getResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_NOT_FOUND);
        assertThat(getResponse.getBody().asString()).contains("Player not found");
        log.info("Correctly handled non-existent player request");
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-R-15: Supervisor updates USER (positive)")
    void testSupervisorUpdatesUser() {
        PlayerCreateRequest userRequest = PlayerRequestBuilder.createUserRequestPojo();
        Response createResponse = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            userRequest,
            UserRole.SUPERVISOR
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_CREATED);
        PlayerCreateResponse createdUser = createResponse.as(PlayerCreateResponse.class);
        
        PlayerUpdateDto updateRequest = new PlayerUpdateDto();
        updateRequest.setScreenName("Updated Name");
        updateRequest.setAge(30);
        
        Response updateResponse = ApiCalls.put(
            ApiEndpoints.UPDATE_PLAYER + "/" + createdUser.getId(),
            updateRequest,
            UserRole.SUPERVISOR
        );
        
        assertThat(updateResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_OK);
        PlayerUpdateResponseDto updatedPlayer = updateResponse.as(PlayerUpdateResponseDto.class);
        assertThat(updatedPlayer.getScreenName()).isEqualTo("Updated Name");
        assertThat(updatedPlayer.getAge()).isEqualTo(30);
        log.info("Successfully updated user with ID: {}", updatedPlayer.getId());
    }
} 
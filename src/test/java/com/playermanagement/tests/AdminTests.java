package com.playermanagement.tests;

import com.playermanagement.api.ApiCalls;
import com.playermanagement.api.ApiEndpoints;
import com.playermanagement.api.UserRole;
import com.playermanagement.config.TestConstants;
import com.playermanagement.model.request.PlayerCreateRequest;
import com.playermanagement.model.request.PlayerUpdateRequestDto;
import com.playermanagement.model.response.PlayerCreateResponse;
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
@Feature("Admin Role Tests")
public class AdminTests extends BaseTest {

    @Test
    @Tag("regression")
    @DisplayName("TC-R-03: Admin creates user with USER role")
    void testAdminCreatesUser() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        log.info("Request POJO prepared: {}", request);

        Response response = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request,
            UserRole.ADMIN
        );

        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_CREATED);
        PlayerCreateResponse responseData = response.as(PlayerCreateResponse.class);
        verifyPlayerData(responseData, request);
        log.info("User successfully created by admin with ID: {}", responseData.getId());
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-R-04: Admin attempts to create user with SUPERVISOR role")
    void testAdminCannotCreateSupervisor() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        request.setRole("SUPERVISOR");
        log.info("Request POJO prepared: {}", request);

        Response response = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request,
            UserRole.ADMIN
        );

        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_FORBIDDEN);
        assertThat(response.getBody().asString()).contains("Player can only be created with 'admin' or 'user' role");
        log.info("Admin correctly denied creating supervisor role");
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-R-16: Admin updates USER")
    void testAdminUpdatesUser() {
        PlayerCreateResponse testUser = createTestUser();
        log.info("Created test user with ID: {}", testUser.getId());

        PlayerUpdateRequestDto updateRequest = new PlayerUpdateRequestDto();
        updateRequest.setScreenName("Updated By Admin");

        Response response = ApiCalls.patch(
            ApiEndpoints.UPDATE_PLAYER,
            updateRequest,
            UserRole.ADMIN,
            testUser.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_OK);
        PlayerCreateResponse updatedUser = response.as(PlayerCreateResponse.class);
        assertThat(updatedUser.getScreenName()).isEqualTo("Updated By Admin");
        log.info("Admin successfully updated user with ID: {}", updatedUser.getId());
    }

    private PlayerCreateResponse createTestUser() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        Response response = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request,
            UserRole.SUPERVISOR
        );
        return response.as(PlayerCreateResponse.class);
    }

    private void verifyPlayerData(PlayerCreateResponse actual, PlayerCreateRequest expected) {
        assertThat(actual.getLogin()).isEqualTo(expected.getLogin());
        assertThat(actual.getScreenName()).isEqualTo(expected.getScreenName());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
        assertThat(actual.getGender()).isEqualTo(expected.getGender());
        assertThat(actual.getRole()).isEqualTo(expected.getRole().toLowerCase());
    }
} 
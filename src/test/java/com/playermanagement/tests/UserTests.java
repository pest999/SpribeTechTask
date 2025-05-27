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
@Feature("User Role Tests")
public class UserTests extends BaseTest {

    @Test
    @Tag("regression")
    @DisplayName("TC-R-11: User cannot create another user")
    void testUserCannotCreateUser() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        log.info("Request POJO prepared: {}", request);

        Response response = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request,
            UserRole.USER
        );

        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_FORBIDDEN);
        assertThat(response.getBody().asString()).contains("Only supervisor or admin can create players");
        log.info("User correctly denied creating another user");
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-R-12: User updates their own data")
    void testUserUpdatesSelf() {
        PlayerCreateResponse testUser = createTestUser();
        log.info("Created test user with ID: {}", testUser.getId());

        PlayerUpdateRequestDto updateRequest = new PlayerUpdateRequestDto();
        updateRequest.setScreenName("Updated Screen Name");

        Response response = ApiCalls.patch(
            ApiEndpoints.UPDATE_PLAYER,
            updateRequest,
            UserRole.USER,
            testUser.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(TestConstants.HTTP_OK);
        PlayerCreateResponse updatedUser = response.as(PlayerCreateResponse.class);
        assertThat(updatedUser.getScreenName()).isEqualTo("Updated Screen Name");
        log.info("User successfully updated their own data");
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
}
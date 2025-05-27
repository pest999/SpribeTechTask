package com.playermanagement.tests;

import com.playermanagement.api.ApiCalls;
import com.playermanagement.api.ApiEndpoints;
import com.playermanagement.api.UserRole;
import com.playermanagement.config.TestConstants;
import com.playermanagement.model.request.PlayerCreateRequest;
import com.playermanagement.model.response.PlayerCreateResponse;
import com.playermanagement.utils.PlayerRequestBuilder;
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
@Feature("Player Delete Tests")
public class PlayerDeleteTests extends BaseTest {

    @Test
    @Tag("regression")
    @DisplayName("TC-R-08: Supervisor deletes ADMIN")
    void testSupervisorDeletesAdmin() {
        PlayerCreateResponse createdAdmin = createTestAdmin();
        log.info("Created test admin with ID: {}", createdAdmin.getId());

        Response deleteResponse = deletePlayer(createdAdmin.getId(), UserRole.SUPERVISOR);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_OK);
        log.info("Admin successfully deleted by supervisor");
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-R-09: Admin deletes USER")
    void testAdminDeletesUser() {
        PlayerCreateResponse createdUser = createTestUser();
        log.info("Created test user with ID: {}", createdUser.getId());

        Response deleteResponse = deletePlayer(createdUser.getId(), UserRole.ADMIN);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_OK);
        log.info("User successfully deleted by admin");
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-R-10: Admin tries to delete ADMIN")
    void testAdminDeletesAdmin() {
        PlayerCreateResponse createdAdmin = createTestAdmin();
        log.info("Created test admin with ID: {}", createdAdmin.getId());

        Response deleteResponse = deletePlayer(createdAdmin.getId(), UserRole.ADMIN);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_FORBIDDEN);
        assertThat(deleteResponse.getBody().asString()).contains("Admin cannot delete other admins");
        log.info("Admin correctly denied deleting another admin");
    }

    @Test
    @Tag("regression")
    @DisplayName("TC-R-13: User tries to delete any user")
    void testUserDeletesAnyUser() {
        PlayerCreateResponse createdUser = createTestUser();
        log.info("Created test user with ID: {}", createdUser.getId());

        Response deleteResponse = deletePlayer(createdUser.getId(), UserRole.USER);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(TestConstants.HTTP_FORBIDDEN);
        assertThat(deleteResponse.getBody().asString()).contains("Only supervisor or admin can delete players");
        log.info("User correctly denied deleting players");
    }

    @Step("Create test user")
    private PlayerCreateResponse createTestUser() {
        PlayerCreateRequest request = PlayerRequestBuilder.createUserRequestPojo();
        Response response = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request,
            UserRole.SUPERVISOR
        );
        return response.as(PlayerCreateResponse.class);
    }

    @Step("Create test admin")
    private PlayerCreateResponse createTestAdmin() {
        PlayerCreateRequest request = PlayerRequestBuilder.createAdminRequestPojo();
        Response response = ApiCalls.post(
            ApiEndpoints.CREATE_PLAYER,
            request,
            UserRole.SUPERVISOR
        );
        return response.as(PlayerCreateResponse.class);
    }

    @Step("Delete player")
    private Response deletePlayer(Long playerId, UserRole role) {
        return ApiCalls.delete(
            ApiEndpoints.DELETE_PLAYER,
            role,
            playerId
        );
    }
} 
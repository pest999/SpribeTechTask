package com.playermanagement.model.response;

import com.playermanagement.model.request.PlayerCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO for user creation response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerCreateResponseDto {
    
    private Integer age;
    private String gender;
    private Long id;
    private String login;
    private String password;
    private String role;
    private String screenName;
    
    /**
     * Checks if response matches expected request
     */
    public boolean matchesRequest(PlayerCreateRequest request) {
        return this.login.equals(request.getLogin()) &&
               this.screenName.equals(request.getScreenName()) &&
               this.password.equals(request.getPassword()) &&
               this.age.equals(request.getAge()) &&
               this.gender.equals(request.getGender()) &&
               this.role.equalsIgnoreCase(request.getRole());
    }
    
    /**
     * Checks required response fields
     */
    public boolean hasRequiredFields() {
        return id != null && id > 0 &&
               login != null && !login.trim().isEmpty() &&
               screenName != null && !screenName.trim().isEmpty() &&
               role != null && !role.trim().isEmpty();
    }
    
    /**
     * Returns role in lowercase (as API returns)
     */
    public String getRoleExpected(String requestRole) {
        return requestRole.toLowerCase();
    }
} 
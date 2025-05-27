package com.playermanagement.api;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static com.playermanagement.config.TestConstants.CONTENT_TYPE_JSON;
import static io.restassured.RestAssured.given;

/**
 * Simple API calls wrapper with logging and Allure reporting
 */
@Slf4j
public class ApiCalls {
    
    @Step("GET {endpoint}")
    public static Response get(ApiEndpoints endpoint, Object... pathParams) {
        String path = pathParams.length > 0 ? 
            endpoint.getPathWithParams(pathParams) : 
            endpoint.getPath();
            
        logRequest("GET", path, null);
        
        Response response = given()
            .filter(new AllureRestAssured())
            .when()
                .get(path);
                
        logResponse(response);
        
        return response;
    }
    
    @Step("POST {endpoint}")
    public static Response post(ApiEndpoints endpoint, Object requestBody) {
        String path = endpoint.getPath();
        
        logRequest("POST", path, requestBody);
        
        Response response = given()
            .filter(new AllureRestAssured())
            .contentType(CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
                .post(path);
                
        logResponse(response);
        
        return response;
    }
    
    @Step("POST {endpoint}")
    public static Response post(ApiEndpoints endpoint, Object requestBody, Object... pathParams) {
        String path = pathParams.length > 0 ? 
            endpoint.getPathWithParams(pathParams) : 
            endpoint.getPath();
            
        logRequest("POST", path, requestBody);
        
        Response response = given()
            .filter(new AllureRestAssured())
            .contentType(CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
                .post(path);
                
        logResponse(response);
        
        return response;
    }
    
    @Step("PATCH {endpoint}")
    public static Response patch(ApiEndpoints endpoint, Object requestBody, Object... pathParams) {
        String path = pathParams.length > 0 ? 
            endpoint.getPathWithParams(pathParams) : 
            endpoint.getPath();
            
        logRequest("PATCH", path, requestBody);
        
        Response response = given()
            .filter(new AllureRestAssured())
            .contentType(CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
                .patch(path);
                
        logResponse(response);
        
        return response;
    }
    
    @Step("DELETE {endpoint}")
    public static Response delete(ApiEndpoints endpoint, Object requestBody, Object... pathParams) {
        String path = pathParams.length > 0 ? 
            endpoint.getPathWithParams(pathParams) : 
            endpoint.getPath();
            
        logRequest("DELETE", path, requestBody);
        
        Response response = given()
            .filter(new AllureRestAssured())
            .contentType(CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
                .delete(path);
                
        logResponse(response);
        
        return response;
    }
    
    private static void logRequest(String method, String endpoint, Object requestBody) {
        log.info("Executing {} request to: {}", method, endpoint);
        if (requestBody != null) {
            log.debug("Request body: {}", requestBody);
        }
    }
    
    private static void logResponse(Response response) {
        log.info("Request completed. Status: {}, Response time: {}ms", 
                response.getStatusCode(), response.getTime());
        log.debug("Response body: {}", response.asString());
    }
} 
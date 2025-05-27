package com.playermanagement.tests;

import com.playermanagement.config.DatabaseConnection;
import com.playermanagement.config.RestAssuredConfig;
import com.playermanagement.core.ThreadSafeTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public abstract class BaseTest {
    
    @BeforeAll
    static void setupTests() {
        log.info("Setting up test environment");
        RestAssuredConfig.configure();
        log.info("Test environment ready");
    }
    
    @AfterAll
    static void cleanupAfterAllTests() {
        log.info("🧹 Cleaning up database after all tests...");
        DatabaseConnection.cleanupTestData();
    }
    
    @BeforeEach
    void setupTest(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String testClass = testInfo.getTestClass()
            .map(Class::getSimpleName)
            .orElse("Unknown");
        
        log.info("🚀 STARTING TEST: {} in {}", testName, testClass);
        ThreadSafeTestContext.createUniqueUser("test_user");
    }
    
    @AfterEach
    void cleanupTest(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String testClass = testInfo.getTestClass()
            .map(Class::getSimpleName)
            .orElse("Unknown");
            
        ThreadSafeTestContext.cleanup();
        log.info("✅ COMPLETED TEST: {} in {}", testName, testClass);
    }
    
    protected String getCurrentUser() {
        return ThreadSafeTestContext.getData(ThreadSafeTestContext.CURRENT_USER);
    }
}
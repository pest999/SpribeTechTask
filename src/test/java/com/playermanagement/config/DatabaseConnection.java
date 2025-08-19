package com.playermanagement.config;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.playermanagement.config.TestConstants.*;

@Slf4j
public class DatabaseConnection {
    

    public static void executeQuery(String sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            
            log.info("Executing SQL: {}", sql);
            statement.execute(sql);
            log.info("SQL executed successfully");
            
        } catch (SQLException e) {
            log.error("Failed to execute SQL: {}", sql, e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public static void cleanupTestData() {
        log.info("🧹 Starting database cleanup...");
        executeQuery("DELETE FROM players WHERE id > 3");        
        log.info("✅ Database cleanup completed");
    }
} 
package ru.erma.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * This abstract class provides a base configuration for integration tests that use Testcontainers.
 * It starts a PostgreSQL container before all tests and stops it after all tests.
 */
@SpringBootTest
@Testcontainers
public abstract class AbstractTestContainerConfig {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1-alpine");
}
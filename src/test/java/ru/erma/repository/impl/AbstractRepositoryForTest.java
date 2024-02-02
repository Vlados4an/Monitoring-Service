package ru.erma.repository.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.erma.config.DBConnectionProvider;
import ru.erma.config.DBMigrationService;

public abstract class AbstractRepositoryForTest {

    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.1-alpine");
    protected DBConnectionProvider connectionProvider;

    @BeforeEach
    void setUp() {
        connectionProvider = new DBConnectionProvider(
                container.getJdbcUrl(), container.getUsername(), container.getPassword()
        );

        DBMigrationService migrationService = new DBMigrationService(connectionProvider, "migration", "db.changelog/db.changelog-master.xml");
        migrationService.migration();
    }
    @BeforeAll
    static void init() {
        container.start();
    }

    @AfterAll
    static void tearDown() {
        container.stop();
    }
}
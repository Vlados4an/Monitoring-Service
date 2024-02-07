package ru.erma.repository.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.erma.config.DBConnectionProvider;
import ru.erma.config.DBMigrationService;
/**
 * The AbstractRepositoryForTest class provides a base for all repository test classes.
 * It sets up a PostgreSQL test container and a DBConnectionProvider before each test,
 * and performs a database migration using a DBMigrationService.
 * The test container is started before all tests and stopped after all tests.
 */
public abstract class AbstractRepositoryForTest {
    /**
     * The PostgreSQL test container.
     */
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.1-alpine");
    /**
     * The provider for database connections.
     */
    protected DBConnectionProvider connectionProvider;
    /**
     * The setUp method initializes the DBConnectionProvider and performs a database migration before each test.
     * It creates a new DBConnectionProvider with the JDBC URL, username, and password from the test container,
     * and a new DBMigrationService with the connection provider.
     * It then calls the migration method of the DBMigrationService to perform the migration.
     */
    @BeforeEach
    void setUp() {
        connectionProvider = new DBConnectionProvider(
                container.getJdbcUrl(), container.getUsername(), container.getPassword()
        );

        DBMigrationService migrationService = new DBMigrationService(connectionProvider, "migration", "db.changelog/db.changelog-master.xml");
        migrationService.migration();
    }
    /**
     * The init method starts the PostgreSQL test container before all tests.
     */
    @BeforeAll
    static void init() {
        container.start();
    }
    /**
     * The tearDown method stops the PostgreSQL test container after all tests.
     */
    @AfterAll
    static void tearDown() {
        container.stop();
    }
}
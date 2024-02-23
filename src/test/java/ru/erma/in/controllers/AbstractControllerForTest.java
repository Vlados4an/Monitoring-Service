package ru.erma.in.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


/**
 * This abstract class is used as a base for repository tests.
 * It sets up a PostgreSQL test container and a JdbcTemplate for executing SQL queries.
 * The test container is started before all tests and stopped after all tests.
 * Before each test, a new JdbcTemplate is created and the database schema is updated using Liquibase.
 */
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.liquibase.liquibase-schema=public")
public abstract class AbstractControllerForTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1-alpine");

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }


}
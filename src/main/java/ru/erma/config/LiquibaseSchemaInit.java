package ru.erma.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * This class is responsible for creating the Liquibase schema before Liquibase starts its migrations.
 * It implements the BeanPostProcessor interface to intercept the creation of the DataSource bean.
 */
@Slf4j
@Component
public class LiquibaseSchemaInit implements BeanPostProcessor {

    @Value("${spring.liquibase.liquibase-schema}")
    private String schemaName;

    /**
     * This method is called after a new bean instance is created and all its properties have been set.
     * It checks if the bean is a DataSource, and if so, it creates a new schema in the database if it doesn't exist.
     *
     * @param bean the newly created bean
     * @param beanName the name of the bean
     * @return the bean itself
     * @throws BeansException if an error occurs during bean creation
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StringUtils.hasText(schemaName) && bean instanceof DataSource dataSource) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            try {
                log.info("Going to create DB schema '{}' if not exists.", schemaName);
                jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create schema '" + schemaName + "'", e);
            }
        }
        return bean;
    }
}
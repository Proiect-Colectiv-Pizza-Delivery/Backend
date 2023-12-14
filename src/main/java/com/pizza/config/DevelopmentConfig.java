package com.pizza.config;


import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
@Profile("dev")
public class DevelopmentConfig {
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create().username(System.getenv("DB_USER")).password(System.getenv("DB_PASSWORD")).url("jdbc:postgresql://education.postgres.database.azure.com:5432/pizza-security")
                .driverClassName("org.postgresql.Driver").build();
    }

}
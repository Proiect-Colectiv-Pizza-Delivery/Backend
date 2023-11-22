package com.pizza.config;


import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
public class DevelopmentConfig {
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create().username(System.getenv("DB_USER")).password(System.getenv("DB_PASSWORD")).url(System.getenv("DB_URL"))
                .driverClassName("org.postgresql.Driver").build();
    }
}
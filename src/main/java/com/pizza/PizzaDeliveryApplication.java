package com.pizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class PizzaDeliveryApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(PizzaDeliveryApplication.class, args);
	}

	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create().username(System.getenv("DB_USER")).password(System.getenv("DB_PASSWORD")).url(System.getenv("DB_URL"))
				.driverClassName("org.postgresql.Driver").build();
	}

}

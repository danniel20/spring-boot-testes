package com.securityteste.securityspringteste.config.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmptyMigrationStrategyConfig {

	@Value("${spring.profiles.active}")
	private String environment;

	@Bean
	public FlywayMigrationStrategy flywayMigrationStrategy() {

		if (this.environment.equals("development")) {
			return flyway -> {
				// do nothing
			};
		}
		else{
			return flyway -> {
				flyway.migrate();
			};
		}
	}
}

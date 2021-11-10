package com.securityteste.securityspringteste.config.database;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("development")
public class DataSourceDev {
    
    @Bean
    public DataSource getDataSource(){
        return DataSourceBuilder.create()
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:testedb")
            .username("sa")
            .password("")
            .build();
    }
}

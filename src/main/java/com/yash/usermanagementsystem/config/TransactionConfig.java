package com.yash.usermanagementsystem.config;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;
import javax.sql.DataSource;

@Factory
public class TransactionConfig {

    @Bean
    @Singleton
    @Primary
    public DataSource dataSource(DataSource originalDataSource) {
        return originalDataSource;
    }
} 
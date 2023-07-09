package com.hibernate.jpa.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("clean")
@Configuration
public class FlywayDbClean {
    @Bean
    public FlywayMigrationStrategy clean() {
        // run
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }
}

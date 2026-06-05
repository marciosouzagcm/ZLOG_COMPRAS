package com.zlogcompras.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

@Configuration
public class FlywayConfig {

    private static final Logger logger = LoggerFactory.getLogger(FlywayConfig.class);

    @Bean
    public FlywayConfigurationCustomizer flywayConfigurationCustomizer(Environment env) {
        boolean flywayEnabled = env.getProperty("spring.flyway.enabled", Boolean.class, true);
        // Skip repair during local/test profiles or when flyway explicitly disabled
        if (!flywayEnabled || env.acceptsProfiles(Profiles.of("local", "test"))) {
            logger.info("Skipping Flyway repair because flyway.enabled={} or active profile is local/test", flywayEnabled);
            return configuration -> {};
        }

        return configuration -> {
            try {
                Flyway flyway = new Flyway(configuration);
                flyway.repair();
            } catch (Exception e) {
                logger.warn("Flyway repair skipped due to error: {}", e.getMessage());
            }
        };
    }
}
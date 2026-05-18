package com.zlogcompras.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public FlywayConfigurationCustomizer flywayConfigurationCustomizer() {
        return configuration -> {
            // Cria uma instância temporária para rodar o repair antes do fluxo padrão do Spring
            Flyway flyway = new Flyway(configuration);
            flyway.repair();
        };
    }
}
package com.zlogcompras.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 🚨 ATUALIZADO: Incluído o IP de rede local para evitar rejeições do navegador
                .allowedOrigins(
                    "http://localhost:3000", 
                    "http://localhost:4200", 
                    "http://localhost:5173", 
                    "http://localhost:8080",
                    "http://localhost:8081",
                    "http://192.168.0.159:5173"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // Cache de resposta CORS por 1 hora para otimizar a performance do Front-end
    }
}
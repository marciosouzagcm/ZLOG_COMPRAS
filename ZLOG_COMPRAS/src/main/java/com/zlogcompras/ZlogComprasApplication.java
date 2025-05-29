package com.zlogcompras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; // ADICIONE ESTA LINHA
import org.springframework.data.jpa.repository.config.EnableJpaAuditing; // Esta você já adicionou

@SpringBootApplication
@EnableJpaAuditing
public class ZlogComprasApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZlogComprasApplication.class, args);
    }
}
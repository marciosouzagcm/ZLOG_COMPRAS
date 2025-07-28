package com.zlogcompras.config;

import com.zlogcompras.model.Role;
import com.zlogcompras.model.RoleName; // Importe o seu enum RoleName
import com.zlogcompras.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            // Verifica e cria ROLE_USER
            if (roleRepository.findByName(RoleName.ROLE_USER.name()).isEmpty()) {
                roleRepository.save(new Role(null, RoleName.ROLE_USER.name()));
                System.out.println("Role '" + RoleName.ROLE_USER.name() + "' criada com sucesso!");
            }

            // Verifica e cria ROLE_ADMIN
            if (roleRepository.findByName(RoleName.ROLE_ADMIN.name()).isEmpty()) {
                roleRepository.save(new Role(null, RoleName.ROLE_ADMIN.name()));
                System.out.println("Role '" + RoleName.ROLE_ADMIN.name() + "' criada com sucesso!");
            }

            // Se você tiver outros papéis no seu enum, adicione-os aqui.
            // Ex: if (roleRepository.findByName(RoleName.ROLE_GERENTE_COMPRAS.name()).isEmpty()) { ... }
        };
    }
}
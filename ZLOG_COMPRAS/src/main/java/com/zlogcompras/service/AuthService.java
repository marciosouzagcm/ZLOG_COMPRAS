package com.zlogcompras.service;

import com.zlogcompras.model.User;
import com.zlogcompras.model.Role;
import com.zlogcompras.model.RoleName;
import com.zlogcompras.model.dto.RegisterRequestDTO;
import com.zlogcompras.repository.RoleRepository;
import com.zlogcompras.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(RegisterRequestDTO registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Nome de usuário já existe!");
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER.name())
            .orElseThrow(() -> new RuntimeException("Erro: Role 'USER' não encontrada."));
            
        newUser.setRoles(Collections.singleton(defaultRole));

        return userRepository.save(newUser);
    }
}
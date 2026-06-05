package com.zlogcompras.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zlogcompras.model.User;
import com.zlogcompras.model.dto.RegisterRequestDTO;
import com.zlogcompras.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(RegisterRequestDTO registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username já está em uso.");
        }

        User user = new User();
        user.setNome(registerRequest.getNome());
        user.setEmail(registerRequest.getEmail()); // CORREÇÃO: Repassando o e-mail para a entidade
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(true);

        return userRepository.save(user);
    }
}
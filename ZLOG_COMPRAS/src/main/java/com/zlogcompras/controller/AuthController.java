package com.zlogcompras.controller;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zlogcompras.model.Role;
import com.zlogcompras.model.RoleName; // Certifique-se de ter este enum!
import com.zlogcompras.model.User;
import com.zlogcompras.model.dto.LoginRequestDTO;
import com.zlogcompras.model.dto.LoginResponseDTO;
import com.zlogcompras.model.dto.RegisterRequestDTO;
import com.zlogcompras.repository.RoleRepository;
import com.zlogcompras.repository.UserRepository;
import com.zlogcompras.service.JwtService;
import com.zlogcompras.service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthController(AuthenticationManager authenticationManager,
            UserDetailsServiceImpl userDetailsService,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        // Tenta autenticar o usuário
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Carrega os detalhes do usuário
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        // Gera o token JWT
        String jwt = jwtService.generateToken(userDetails);

        // Retorna o token na resposta
        return ResponseEntity.ok(new LoginResponseDTO(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO registerRequest) {
        // Verifica se o nome de usuário já existe
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Nome de usuário já existe!");
        }

        // Cria e configura o novo usuário
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // --- Configuração de Roles ---
        // Busca a role padrão "USER". É CRÍTICO que esta role exista no seu banco de
        // dados.
        // CORREÇÃO: Usar .name() para obter a representação String do enum
        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER.name()) // <--- ALTERADO AQUI!
                .orElseThrow(() -> new RuntimeException(
                        "Erro: Role 'USER' não encontrada. Certifique-se de que ela existe ou crie-a na inicialização da aplicação."));
        newUser.setRoles(Collections.singleton(defaultRole));

        // Define o usuário como habilitado (se aplicável ao seu modelo User)
        newUser.setEnabled(true);
        userRepository.save(newUser); // Salva o novo usuário

        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }
}
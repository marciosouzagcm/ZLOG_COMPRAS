package com.zlogcompras.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zlogcompras.model.dto.LoginRequestDTO;
import com.zlogcompras.model.dto.LoginResponseDTO;
import com.zlogcompras.model.dto.RegisterRequestDTO;
import com.zlogcompras.service.AuthService;
import com.zlogcompras.service.JwtService;
import com.zlogcompras.service.UserDetailsServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final AuthService authService; 

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsServiceImpl userDetailsService,
                          JwtService jwtService,
                          AuthService authService) { 
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String jwt = jwtService.generateToken(userDetails);
        
        return ResponseEntity.ok(new LoginResponseDTO(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        try {
            // Executa a lógica de persistência e validação de negócio
            authService.registerNewUser(registerRequest);
            
            // Retorna o status HTTP 201 (Created) ideal para APIs RESTful
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso!");
            
        } catch (IllegalArgumentException e) {
            // Captura erros de validação negocial conhecidos (ex: "Username já existe")
            return ResponseEntity.badRequest().body(e.getMessage());
            
        } catch (Exception e) {
            // Intercepta qualquer erro inesperado (SQL, NullPointer, tabelas ausentes)
            // e garante o log detalhado no console do Spring Boot
            e.printStackTrace(); 
            
            // Força a resposta a expor o motivo real no corpo da resposta do Swagger
            String causaReal = e.getCause() != null ? e.getCause().getMessage() : "Não especificada";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno capturado: " + e.getMessage() + " | Causa raiz: " + causaReal);
        }
    }
}
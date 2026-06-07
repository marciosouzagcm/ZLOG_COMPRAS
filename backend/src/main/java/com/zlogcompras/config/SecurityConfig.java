package com.zlogcompras.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 🚨 ATUALIZADO: Permite requisições tanto de localhost quanto do IP da sua rede local
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173", 
            "http://127.0.0.1:5173",
            "http://192.168.0.159:5173"
        ));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Desabilita proteção CSRF para arquiteturas baseadas em Tokens/JWT (Stateless)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Vincula as configurações globais de CORS atualizadas
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 3. Incremento OWASP: Proteção de Cabeçalhos HTTP (HSTS, Clickjacking e MIME Sniffing)
            .headers(headers -> headers
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000) // 1 Ano de retenção de cache de segurança
                )
                .frameOptions(frame -> frame.deny()) // Previne ataques de Clickjacking (OWASP A05)
            )
            
            // 4. Regras estritas de interceptação de requisições HTTP
            .authorizeHttpRequests(auth -> auth
                // Permite requisições de checagem prévia (Pre-flight OPTIONS) feitas pelos navegadores
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // [Controlador de Autenticação] -> Totalmente Público
                .requestMatchers("/api/auth/**").permitAll()
                
                // Rotas globais de erro do Spring
                .requestMatchers("/error").permitAll()
                
                // Documentação e Swagger UI -> Totalmente Público para testes locais
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                
                // [Solicitação de Compra] -> Exige autenticação JWT
                .requestMatchers("/api/solicitacoes-compra/**").authenticated()
                
                // [Processo de Compra] -> Exige autenticação JWT
                .requestMatchers("/api/processo-compra/**").authenticated()
                
                // [Pedido de Compra] -> Exige autenticação JWT
                .requestMatchers("/api/pedidos-compra/**").authenticated()
                
                // [Orquestração / Orçamentos] -> Exige autenticação JWT
                .requestMatchers("/api/orcamentos/**").authenticated()
                
                // [Fornecedores] -> Exige autenticação JWT
                .requestMatchers("/api/fornecedores/**").authenticated()
                
                // [Estoque] -> Exige autenticação JWT (Trata variações com e sem acento)
                .requestMatchers("/api/estoques/**", "/api/estóques/**").authenticated()
                
                // [Produtos] -> Exige autenticação JWT
                .requestMatchers("/api/produtos/**").authenticated()
                
                // [Painel de Controle / Dashboard] -> Exige autenticação JWT
                .requestMatchers("/api/dashboard/**", "/api/painel/**").authenticated()
                
                // Qualquer outro endpoint não explicitado acima exigirá autenticação por padrão
                .anyRequest().authenticated()
            )
            
            // 5. Desabilita gerenciamento de estados no servidor (Garante arquitetura REST pura)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 6. Acopla o Provedor customizado e insere o Filtro de validação do Token antes do padrão
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
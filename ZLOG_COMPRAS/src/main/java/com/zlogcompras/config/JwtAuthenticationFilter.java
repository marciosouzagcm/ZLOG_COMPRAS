package com.zlogcompras.config; // Certifique-se de que este pacote corresponde ao seu projeto

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zlogcompras.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Lista de URLs que o filtro JWT deve IGNORAR (correspondência EXATA)
    private static final List<String> EXACT_EXCLUDED_URLS = Arrays.asList(
            "/api/auth/login",
            "/api/auth/register",
            "/error");

    // Lista de URLs que o filtro JWT deve IGNORAR (correspondência por PREFIXO,
    // para Swagger, etc.)
    private static final List<String> PREFIX_EXCLUDED_URLS = Arrays.asList(
            "/swagger-ui/",
            "/v3/api-docs/",
            "/swagger-resources/",
            "/webjars/");

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // --- MÉTODO CRÍTICO AJUSTADO PARA CORRESPONDÊNCIA MAIS ROBUSTA ---
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // Verifica se a URI da requisição corresponde a uma URL de exclusão exata
        if (EXACT_EXCLUDED_URLS.contains(path)) {
            logger.debug("Should not filter (exact match): {}", path);
            return true;
        }

        // Verifica se a URI da requisição começa com uma das URLs de exclusão de
        // prefixo
        if (PREFIX_EXCLUDED_URLS.stream().anyMatch(path::startsWith)) {
            logger.debug("Should not filter (prefix match): {}", path);
            return true;
        }

        logger.debug("Should filter: {}", path);
        return false;
    }
    // --- FIM DO MÉTODO CRÍTICO AJUSTADO ---

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Se shouldNotFilter retornou true, este método não será executado.
        // Se shouldNotFilter retornou false (ou seja, a URL não é pública),
        // então a lógica de validação JWT abaixo será aplicada.

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        String userEmail = null;

        logger.debug("Requisição recebida: {}", request.getRequestURI());

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("Cabeçalho Authorization ausente ou inválido. Passando para o próximo filtro.");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        logger.debug("Token JWT extraído: {}", jwt);

        try {
            userEmail = jwtService.extractUsername(jwt);
            logger.debug("Username extraído do token: {}", userEmail);
        } catch (Exception e) {
            logger.error("Erro ao extrair username ou token inválido/expirado: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                logger.debug("Detalhes do usuário carregados para: {}", userEmail);
                logger.debug("Autoridades do usuário: {}", userDetails.getAuthorities());
            } catch (Exception e) {
                logger.error("Erro ao carregar UserDetails para {}: {}", userEmail, e.getMessage());
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Usuário {} autenticado com sucesso no SecurityContext.", userEmail);
            } else {
                logger.warn("Token JWT é inválido para o usuário: {}", userEmail);
            }
        } else if (SecurityContextHolder.getContext().getAuthentication() != null) {
            logger.debug("Usuário já autenticado no SecurityContext: {}",
                    SecurityContextHolder.getContext().getAuthentication().getName());
        } else {
            logger.debug("UserEmail é nulo ou SecurityContext já está vazio.");
        }

        filterChain.doFilter(request, response);
    }
}
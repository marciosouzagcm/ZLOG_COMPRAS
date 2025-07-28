package com.zlogcompras.config;

import java.io.IOException;

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

import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class); // Instanciar Logger

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        String userEmail = null;

        logger.debug("Requisição recebida: {}", request.getRequestURI()); // Logar URI

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("Cabeçalho Authorization ausente ou inválido. Passando para o próximo filtro.");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        logger.debug("Token JWT extraído: {}", jwt); // Logar o token (CUIDADO em produção!)

        try {
            userEmail = jwtService.extractUsername(jwt);
            logger.debug("Username extraído do token: {}", userEmail);
        } catch (Exception e) {
            logger.error("Erro ao extrair username ou token inválido/expirado: {}", e.getMessage());
            // Se o token for inválido/expirado, o SecurityContext permanecerá nulo,
            // e a requisição será tratada como não autenticada.
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                logger.debug("Detalhes do usuário carregados para: {}", userEmail);
                logger.debug("Autoridades do usuário: {}", userDetails.getAuthorities()); // Logar as autoridades
            } catch (Exception e) {
                logger.error("Erro ao carregar UserDetails para {}: {}", userEmail, e.getMessage());
                // Não conseguimos carregar o usuário, não podemos autenticar.
                filterChain.doFilter(request, response);
                return; // Importante: pare o processamento se o UserDetails falhar
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
             logger.debug("Usuário já autenticado no SecurityContext: {}", SecurityContextHolder.getContext().getAuthentication().getName());
        } else {
            logger.debug("UserEmail é nulo ou SecurityContext já está vazio.");
        }
        
        filterChain.doFilter(request, response);
    }
}
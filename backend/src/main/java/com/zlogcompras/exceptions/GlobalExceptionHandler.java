package com.zlogcompras.exceptions;

import com.zlogcompras.model.dto.ErroResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Trata erros de recurso não encontrado (Ex: Produto ou Estoque inexistente)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroResponseDTO> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("Recurso não encontrado: {}", ex.getMessage());
        
        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Não Encontrado",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    // 2. Trata argumentos inválidos ou falhas de regras de negócio (Ex: quantidade negativa)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDTO> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Violação de regra de negócio/validação: {}", ex.getMessage());
        
        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição Inválida",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    // 3. BLINDAGEM CONTRA OWASP A05: Captura QUALQUER erro inesperado do servidor (Erro 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleGlobalException(Exception ex, WebRequest request) {
        // Loga o erro COMPLETO com stack trace internamente no servidor/arquivo para auditoria (OWASP A09)
        logger.error("ERRO CRÍTICO NÃO TRATADO DETECTADO: ", ex);

        // Devolve uma mensagem genérica e segura para o cliente, ocultando a estrutura do código
        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno do Servidor",
                "Ocorreu um erro interno no servidor. A equipe de segurança operacional já foi notificada.",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
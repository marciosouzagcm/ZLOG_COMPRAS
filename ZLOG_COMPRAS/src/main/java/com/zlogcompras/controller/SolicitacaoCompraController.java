package com.zlogcompras.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; // Importar MediaType
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.service.SolicitacaoCompraService;

import jakarta.persistence.EntityNotFoundException; // Importar para tratamento de 404

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoCompraController {

    @Autowired
    private SolicitacaoCompraService solicitacaoCompraService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // Adicionado 'consumes' explicitamente
    public ResponseEntity<SolicitacaoCompra> criarSolicitacao(@RequestBody SolicitacaoCompra solicitacaoCompra) {
        System.out.println("DEBUG: Recebida requisição POST para /api/solicitacoes.");

        try {
            SolicitacaoCompra novaSolicitacao = solicitacaoCompraService.criarSolicitacao(solicitacaoCompra);
            System.out.println("DEBUG: Solicitação criada com ID: " + novaSolicitacao.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(novaSolicitacao);
        } catch (IllegalArgumentException e) {
            System.err.println("ERRO: Requisição inválida ao criar solicitação: " + e.getMessage());
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request para dados inválidos
        } catch (EntityNotFoundException e) {
            System.err.println("ERRO: Recurso não encontrado ao criar solicitação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build(); // Retorna 422 se o produto não for
                                                                                   // encontrado
        } catch (Exception e) { // Captura qualquer outra exceção inesperada
            System.err.println("ERRO INESPERADO ao criar solicitação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 Internal Server Error
        }
    }

    @GetMapping
    public List<SolicitacaoCompra> listar() {
        System.out.println("DEBUG: Recebida requisição GET para /api/solicitacoes (listar todas).");
        List<SolicitacaoCompra> solicitacoes = solicitacaoCompraService.listarTodas();
        System.out.println("DEBUG: Total de solicitações encontradas: " + solicitacoes.size());
        return solicitacoes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoCompra> buscarSolicitacaoPorId(@PathVariable Long id) {
        System.out.println("DEBUG: Recebida requisição GET para /api/solicitacoes/" + id);
        Optional<SolicitacaoCompra> solicitacao = solicitacaoCompraService.buscarSolicitacaoPorId(id);

        if (solicitacao.isPresent()) {
            System.out.println("DEBUG: Solicitação com ID " + id + " encontrada.");
            return ResponseEntity.ok(solicitacao.get());
        } else {
            System.out.println("DEBUG: Solicitação com ID " + id + " NÃO encontrada.");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE) // Adicionado 'consumes' explicitamente
    public ResponseEntity<SolicitacaoCompra> atualizarSolicitacao(@PathVariable Long id,
            @RequestBody SolicitacaoCompra solicitacaoCompra) {
        System.out.println("DEBUG: Recebida requisição PUT para /api/solicitacoes/" + id + ".");

        try {
            SolicitacaoCompra solicitacaoAtualizada = solicitacaoCompraService.atualizarSolicitacao(id,
                    solicitacaoCompra);
            System.out.println("DEBUG: Solicitação com ID " + id + " atualizada com sucesso.");
            return ResponseEntity.ok(solicitacaoAtualizada);
        } catch (EntityNotFoundException e) {
            System.err.println(
                    "ERRO: Solicitação com ID " + id + " não encontrada para atualização. Detalhes: " + e.getMessage());
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found
        } catch (IllegalArgumentException e) {
            System.err.println("ERRO: Dados inválidos para atualização da solicitação com ID " + id + ". Detalhes: "
                    + e.getMessage());
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request para dados inválidos
        } catch (Exception e) { // Captura qualquer outra exceção inesperada
            System.err.println("ERRO INESPERADO ao atualizar solicitação com ID " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSolicitacao(@PathVariable Long id) {
        System.out.println("DEBUG: Recebida requisição DELETE para /api/solicitacoes/" + id);
        try {
            boolean deletado = solicitacaoCompraService.deletarSolicitacao(id);
            if (deletado) {
                System.out.println("DEBUG: Solicitação com ID " + id + " deletada com sucesso.");
                return ResponseEntity.noContent().build(); // Retorna 204 No Content
            } else {
                // Embora o serviço já lance EntityNotFoundException, este else trata o caso de
                // 'existsById' ser falso
                System.out
                        .println("DEBUG: Solicitação com ID " + id + " não encontrada para deleção (ou já deletada).");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) { // Captura exceções, como se a deleção falhar por dependências
            System.err.println("ERRO INESPERADO ao deletar solicitação com ID " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 Internal Server Error
        }
    }

    // Endpoint para atualizar apenas o status da solicitação
    @PutMapping("/{id}/status/{novoStatus}")
    public ResponseEntity<Void> atualizarStatusSolicitacao(@PathVariable Long id, @PathVariable String novoStatus) {
        System.out.println(
                "DEBUG: Recebida requisição PUT para atualizar status da solicitação " + id + " para " + novoStatus);
        try {
            solicitacaoCompraService.atualizarStatusSolicitacao(id, novoStatus);
            System.out
                    .println("DEBUG: Status da solicitação " + id + " atualizado para " + novoStatus + " com sucesso.");
            return ResponseEntity.noContent().build(); // 204 No Content, pois não há corpo a retornar
        } catch (EntityNotFoundException e) {
            System.err.println("ERRO: Solicitação com ID " + id + " não encontrada para atualizar status. Detalhes: "
                    + e.getMessage());
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found
        } catch (Exception e) { // Captura qualquer outra exceção inesperada
            System.err.println("ERRO INESPERADO ao atualizar status da solicitação " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 Internal Server Error
        }
    }
}
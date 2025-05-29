package com.zlogcompras.controller;

import com.zlogcompras.model.dto.SolicitacaoCompraRequestDTO;
import com.zlogcompras.model.dto.SolicitacaoCompraResponseDTO;
import com.zlogcompras.service.SolicitacaoCompraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes-compra") // Mapeamento base para o controller
public class SolicitacaoCompraController {

    private final SolicitacaoCompraService solicitacaoCompraService;

    @Autowired
    public SolicitacaoCompraController(SolicitacaoCompraService solicitacaoCompraService) {
        this.solicitacaoCompraService = solicitacaoCompraService;
    }

    @PostMapping
    public ResponseEntity<SolicitacaoCompraResponseDTO> criarSolicitacao(@Valid @RequestBody SolicitacaoCompraRequestDTO solicitacaoRequestDTO) {
        SolicitacaoCompraResponseDTO novaSolicitacao = solicitacaoCompraService.criarSolicitacao(solicitacaoRequestDTO);
        return new ResponseEntity<>(novaSolicitacao, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoCompraResponseDTO> buscarSolicitacaoPorId(@PathVariable Long id) {
        SolicitacaoCompraResponseDTO solicitacao = solicitacaoCompraService.buscarSolicitacaoPorId(id);
        return ResponseEntity.ok(solicitacao);
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoCompraResponseDTO>> listarTodasSolicitacoes() {
        List<SolicitacaoCompraResponseDTO> solicitacoes = solicitacaoCompraService.listarTodasSolicitacoes();
        return ResponseEntity.ok(solicitacoes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoCompraResponseDTO> atualizarSolicitacao(@PathVariable Long id, @Valid @RequestBody SolicitacaoCompraRequestDTO solicitacaoRequestDTO) {
        SolicitacaoCompraResponseDTO solicitacaoAtualizada = solicitacaoCompraService.atualizarSolicitacao(id, solicitacaoRequestDTO);
        return ResponseEntity.ok(solicitacaoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSolicitacao(@PathVariable Long id) {
        solicitacaoCompraService.deletarSolicitacao(id);
        return ResponseEntity.noContent().build();
    }
}
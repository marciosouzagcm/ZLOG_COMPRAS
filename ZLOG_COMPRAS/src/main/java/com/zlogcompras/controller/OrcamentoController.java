package com.zlogcompras.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zlogcompras.model.dto.OrcamentoLoteRequestDTO; // Certifique-se de que este DTO existe e é necessário
import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoResponseDTO;
import com.zlogcompras.service.OrcamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    public OrcamentoController(OrcamentoService orcamentoService) {
        this.orcamentoService = orcamentoService;
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponseDTO> criarOrcamento(
            @Valid @RequestBody OrcamentoRequestDTO orcamentoRequestDTO) {
        OrcamentoResponseDTO novoOrcamento = orcamentoService.criarOrcamento(orcamentoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoOrcamento);
    }

    @PostMapping("/lote")
    public ResponseEntity<List<OrcamentoResponseDTO>> criarOrcamentosEmLote(
            @Valid @RequestBody OrcamentoLoteRequestDTO orcamentoLoteRequestDTO) {
        // O método no serviço agora retorna List<OrcamentoResponseDTO>
        List<OrcamentoResponseDTO> orcamentosCriados = orcamentoService.criarOrcamentosEmLote(orcamentoLoteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamentosCriados);
    }

    @GetMapping
    public ResponseEntity<List<OrcamentoResponseDTO>> listarOrcamentos() {
        // Corrigido para OrcamentoResponseDTO, conforme o Service retorna
        List<OrcamentoResponseDTO> orcamentos = orcamentoService.listarTodosOrcamentos();
        return ResponseEntity.ok(orcamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> buscarOrcamentoPorId(@PathVariable Long id) {
        OrcamentoResponseDTO orcamento = orcamentoService.buscarOrcamentoPorId(id);
        return ResponseEntity.ok(orcamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> atualizarOrcamento(@PathVariable Long id,
            @Valid @RequestBody OrcamentoRequestDTO orcamentoRequestDTO) {
        OrcamentoResponseDTO orcamentoAtualizado = orcamentoService.atualizarOrcamento(id, orcamentoRequestDTO);
        return ResponseEntity.ok(orcamentoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOrcamento(@PathVariable Long id) {
        orcamentoService.deletarOrcamento(id);
        return ResponseEntity.noContent().build(); // Retorno sem conteúdo (204 No Content)
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<Void> aprovarOrcamento(@PathVariable Long id) {
        orcamentoService.aprovarOrcamento(id);
        return ResponseEntity.noContent().build(); // Retorno sem conteúdo (204 No Content)
    }

    @PatchMapping("/{id}/recusar")
    public ResponseEntity<Void> recusarOrcamento(@PathVariable Long id) {
        orcamentoService.recusarOrcamento(id);
        return ResponseEntity.noContent().build(); // Retorno sem conteúdo (204 No Content)
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Void> concluirOrcamento(@PathVariable Long id) {
        orcamentoService.concluirOrcamento(id);
        return ResponseEntity.noContent().build(); // Retorno sem conteúdo (204 No Content)
    }
}
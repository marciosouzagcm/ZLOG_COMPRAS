package com.zlogcompras.controller;

import com.zlogcompras.mapper.OrcamentoMapper;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.dto.OrcamentoListaDTO;
import com.zlogcompras.model.dto.OrcamentoLoteRequestDTO; // NOVO: Importar o DTO para o lote
import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoResponseDTO;
import com.zlogcompras.service.OrcamentoService;
import jakarta.validation.Valid; // NOVO: Importar para usar a validação @Valid
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;
    private final OrcamentoMapper orcamentoMapper;

    @Autowired
    public OrcamentoController(OrcamentoService orcamentoService, OrcamentoMapper orcamentoMapper) {
        this.orcamentoService = orcamentoService;
        this.orcamentoMapper = orcamentoMapper;
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponseDTO> criarOrcamento(@Valid @RequestBody OrcamentoRequestDTO orcamentoRequestDTO) {
        // O @Valid foi adicionado aqui para garantir que as validações do OrcamentoRequestDTO sejam aplicadas.
        OrcamentoResponseDTO novoOrcamento = orcamentoService.criarOrcamento(orcamentoRequestDTO);
        return new ResponseEntity<>(novoOrcamento, HttpStatus.CREATED);
    }

    // ---
    // NOVO MÉTODO PARA CRIAR ORÇAMENTOS EM LOTE
    @PostMapping("/lote")
    public ResponseEntity<List<OrcamentoResponseDTO>> criarOrcamentosEmLote(@Valid @RequestBody OrcamentoLoteRequestDTO orcamentoLoteRequestDTO) {
        // O @Valid garante que as validações definidas em OrcamentoLoteRequestDTO (e dentro dos itens da lista) sejam aplicadas.
        List<OrcamentoResponseDTO> novosOrcamentos = orcamentoService.criarOrcamentosEmLote(orcamentoLoteRequestDTO);
        return new ResponseEntity<>(novosOrcamentos, HttpStatus.CREATED);
    }
    // ---

    @GetMapping
    public ResponseEntity<List<OrcamentoListaDTO>> listarTodosOrcamentos() {
        List<OrcamentoListaDTO> orcamentos = orcamentoService.listarTodosOrcamentos();
        return ResponseEntity.ok(orcamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> buscarOrcamentoPorId(@PathVariable Long id) {
        Optional<Orcamento> orcamentoOptional = orcamentoService.buscarOrcamentoPorId(id);
        Orcamento orcamento = orcamentoOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Orçamento não encontrado com o ID: " + id));
        return ResponseEntity.ok(orcamentoMapper.toResponseDto(orcamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> atualizarOrcamento(@PathVariable Long id, @Valid @RequestBody OrcamentoRequestDTO orcamentoRequestDTO) {
        // O @Valid foi adicionado aqui para garantir que as validações do OrcamentoRequestDTO sejam aplicadas durante a atualização.
        OrcamentoResponseDTO orcamentoAtualizado = orcamentoService.atualizarOrcamento(id, orcamentoRequestDTO);
        return ResponseEntity.ok(orcamentoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOrcamento(@PathVariable Long id) {
        orcamentoService.deletarOrcamento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<OrcamentoResponseDTO> aprovarOrcamento(@PathVariable Long id) {
        OrcamentoResponseDTO orcamentoAprovado = orcamentoService.aprovarOrcamento(id);
        return ResponseEntity.ok(orcamentoAprovado);
    }
}
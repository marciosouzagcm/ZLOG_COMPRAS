package com.zlogcompras.controller;

import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoResponseDTO;
import com.zlogcompras.model.dto.OrcamentoListaDTO;
import com.zlogcompras.service.OrcamentoService;
import com.zlogcompras.mapper.OrcamentoMapper;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;
    private final OrcamentoMapper orcamentoMapper;

    public OrcamentoController(OrcamentoService orcamentoService, OrcamentoMapper orcamentoMapper) {
        this.orcamentoService = orcamentoService;
        this.orcamentoMapper = orcamentoMapper;
    }

    /**
     * Busca todos os orçamentos existentes.
     * Retorna uma lista de OrcamentoListaDTO.
     *
     * @return ResponseEntity contendo uma lista de OrcamentoListaDTO e status OK.
     */
    @GetMapping
    public ResponseEntity<List<OrcamentoListaDTO>> listarTodosOrcamentos() {
        List<OrcamentoListaDTO> orcamentos = orcamentoService.listarTodosOrcamentos();
        return ResponseEntity.ok(orcamentos);
    }

    /**
     * Busca um orçamento pelo seu ID.
     * Retorna um OrcamentoResponseDTO se encontrado, ou 404 Not Found caso contrário.
     *
     * @param id O ID do orçamento a ser buscado.
     * @return ResponseEntity contendo OrcamentoResponseDTO e status OK, ou lança ResponseStatusException para 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> buscarOrcamentoPorId(@PathVariable Long id) {
        OrcamentoResponseDTO orcamentoResponseDTO = orcamentoService.buscarOrcamentoPorId(id)
                .map(orcamentoMapper::toResponseDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orçamento não encontrado com ID: " + id));

        return ResponseEntity.ok(orcamentoResponseDTO);
    }

    /**
     * Cria um novo orçamento.
     * Retorna o OrcamentoResponseDTO do orçamento recém-criado com status 201 Created.
     *
     * @param orcamentoRequestDTO O DTO contendo os dados para criação do orçamento.
     * @return ResponseEntity contendo OrcamentoResponseDTO e status Created.
     */
    @PostMapping
    public ResponseEntity<OrcamentoResponseDTO> criarOrcamento(@Valid @RequestBody OrcamentoRequestDTO orcamentoRequestDTO) {
        OrcamentoResponseDTO novoOrcamento = orcamentoService.criarOrcamento(orcamentoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoOrcamento);
    }

    /**
     * Atualiza um orçamento existente pelo seu ID.
     * Retorna o OrcamentoResponseDTO do orçamento atualizado com status 200 OK.
     * Lança 404 Not Found se o orçamento não for encontrado.
     *
     * @param id O ID do orçamento a ser atualizado.
     * @param orcamentoRequestDTO O DTO contendo os dados atualizados do orçamento.
     * @return ResponseEntity contendo OrcamentoResponseDTO e status OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> atualizarOrcamento(@PathVariable Long id, @Valid @RequestBody OrcamentoRequestDTO orcamentoRequestDTO) {
        OrcamentoResponseDTO orcamentoAtualizado = orcamentoService.atualizarOrcamento(id, orcamentoRequestDTO);
        return ResponseEntity.ok(orcamentoAtualizado);
    }

    /**
     * Deleta um orçamento pelo seu ID.
     * Retorna 204 No Content se a exclusão for bem-sucedida.
     * Lança 404 Not Found se o orçamento não for encontrado.
     *
     * @param id O ID do orçamento a ser deletado.
     * @return ResponseEntity com status No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOrcamento(@PathVariable Long id) {
        orcamentoService.deletarOrcamento(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Aprova um orçamento específico e rejeita os demais da mesma solicitação de compra.
     * Retorna o OrcamentoResponseDTO do orçamento aprovado com status 200 OK.
     * Lança 404 Not Found se o orçamento não for encontrado.
     * Lança 400 Bad Request se a solicitação de compra associada não for encontrada ou
     * se houver algum erro de lógica de negócio (ex: orçamento já aprovado/rejeitado).
     *
     * @param id O ID do orçamento a ser aprovado.
     * @return ResponseEntity contendo OrcamentoResponseDTO e status OK.
     */
    @PatchMapping("/{id}/aprovar") // Usando PATCH para atualização parcial (status)
    public ResponseEntity<OrcamentoResponseDTO> aprovarOrcamento(@PathVariable Long id) {
        OrcamentoResponseDTO orcamentoAprovado = orcamentoService.aprovarOrcamento(id);
        return ResponseEntity.ok(orcamentoAprovado);
    }
}
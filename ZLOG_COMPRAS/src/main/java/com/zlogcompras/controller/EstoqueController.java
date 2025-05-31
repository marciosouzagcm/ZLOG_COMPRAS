package com.zlogcompras.controller;

import com.zlogcompras.model.dto.EstoqueRequestDTO;
import com.zlogcompras.model.dto.EstoqueResponseDTO;
import com.zlogcompras.model.dto.EstoqueMovimentacaoDTO; // <-- CONFIRME ESTE IMPORT!
import com.zlogcompras.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map; // Embora o EstoqueMovimentacaoDTO dispense o Map para a quantidade, pode ser que você use para outras coisas

@RestController
@RequestMapping("/api/estoques") // <-- CONFIRME O CAMINHO BASE
public class EstoqueController {

    private final EstoqueService estoqueService;

    @Autowired
    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping
    public ResponseEntity<EstoqueResponseDTO> criarEstoque(@RequestBody @Valid EstoqueRequestDTO estoqueRequestDTO) {
        EstoqueResponseDTO novoEstoque = estoqueService.criarEstoque(estoqueRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEstoque);
    }

    // --- ESTES SÃO OS CRÍTICOS PARA SEU PROBLEMA ATUAL ---
    @PutMapping("/{id}/adicionar-quantidade") // <-- CONFIRME ESTE CAMINHO
    public ResponseEntity<EstoqueResponseDTO> adicionarQuantidadeEstoque(@PathVariable Long id,
                                                                          @RequestBody @Valid EstoqueMovimentacaoDTO movimentacaoDTO) {
        EstoqueResponseDTO estoqueAtualizado = estoqueService.adicionarQuantidadeEstoque(id, movimentacaoDTO.getQuantidade());
        return ResponseEntity.ok(estoqueAtualizado);
    }

    @PutMapping("/{id}/retirar-quantidade") // <-- CONFIRME ESTE CAMINHO
    public ResponseEntity<EstoqueResponseDTO> retirarQuantidadeEstoque(@PathVariable Long id,
                                                                         @RequestBody @Valid EstoqueMovimentacaoDTO movimentacaoDTO) {
        EstoqueResponseDTO estoqueAtualizado = estoqueService.retirarQuantidadeEstoque(id, movimentacaoDTO.getQuantidade());
        return ResponseEntity.ok(estoqueAtualizado);
    }
    // --- FIM DOS CRÍTICOS ---


    @PostMapping("/batch")
    public ResponseEntity<List<EstoqueResponseDTO>> criarMultiplosEstoques(@RequestBody @Valid List<EstoqueRequestDTO> estoqueRequestDTOs) {
        List<EstoqueResponseDTO> novosEstoques = estoqueService.criarMultiplosEstoques(estoqueRequestDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body(novosEstoques);
    }

    @GetMapping
    public ResponseEntity<List<EstoqueResponseDTO>> listarTodosEstoques() {
        List<EstoqueResponseDTO> estoques = estoqueService.listarTodosEstoques();
        return ResponseEntity.ok(estoques);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstoqueResponseDTO> buscarEstoquePorId(@PathVariable Long id) {
        EstoqueResponseDTO estoque = estoqueService.buscarEstoquePorId(id);
        return ResponseEntity.ok(estoque);
    }

    @GetMapping("/produto/{codigoProduto}")
    public ResponseEntity<EstoqueResponseDTO> buscarEstoquePorCodigoProduto(@PathVariable String codigoProduto) {
        EstoqueResponseDTO estoque = estoqueService.buscarEstoquePorCodigoProduto(codigoProduto);
        return ResponseEntity.ok(estoque);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstoqueResponseDTO> atualizarEstoque(@PathVariable Long id, @RequestBody @Valid EstoqueRequestDTO estoqueRequestDTO) {
        EstoqueResponseDTO estoqueAtualizado = estoqueService.atualizarEstoque(id, estoqueRequestDTO);
        return ResponseEntity.ok(estoqueAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEstoque(@PathVariable Long id) {
        estoqueService.deletarEstoque(id);
        return ResponseEntity.noContent().build();
    }
}
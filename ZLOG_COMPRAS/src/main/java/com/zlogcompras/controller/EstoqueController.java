package com.zlogcompras.controller;

import com.zlogcompras.model.Estoque;
import com.zlogcompras.model.dto.EstoqueRequestDTO;
import com.zlogcompras.model.dto.EstoqueResponseDTO;
import com.zlogcompras.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;
    // Assumindo que você terá um EstoqueMapper para converter DTOs para entidades e vice-versa
    // private final EstoqueMapper estoqueMapper;

    @Autowired
    public EstoqueController(EstoqueService estoqueService /*, EstoqueMapper estoqueMapper */) {
        this.estoqueService = estoqueService;
        // this.estoqueMapper = estoqueMapper;
    }

    @PostMapping
    public ResponseEntity<EstoqueResponseDTO> criarEstoque(@RequestBody EstoqueRequestDTO estoqueRequestDTO) {
        EstoqueResponseDTO novoEstoque = estoqueService.criarEstoque(estoqueRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEstoque);
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

    @GetMapping("/produto/{codigoProduto}") // Alterado para buscar por código do produto
    public ResponseEntity<EstoqueResponseDTO> buscarEstoquePorCodigoProduto(@PathVariable String codigoProduto) {
        EstoqueResponseDTO estoque = estoqueService.buscarEstoquePorCodigoProduto(codigoProduto);
        return ResponseEntity.ok(estoque);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstoqueResponseDTO> atualizarEstoque(@PathVariable Long id, @RequestBody EstoqueRequestDTO estoqueRequestDTO) {
        EstoqueResponseDTO estoqueAtualizado = estoqueService.atualizarEstoque(id, estoqueRequestDTO);
        return ResponseEntity.ok(estoqueAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEstoque(@PathVariable Long id) {
        estoqueService.deletarEstoque(id);
        return ResponseEntity.noContent().build();
    }
}
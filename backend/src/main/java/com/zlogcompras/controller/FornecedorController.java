package com.zlogcompras.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.mapper.FornecedorMapper;
import com.zlogcompras.model.dto.FornecedorRequestDTO;
import com.zlogcompras.model.dto.FornecedorResponseDTO;
import com.zlogcompras.service.FornecedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;
    private final FornecedorMapper fornecedorMapper;

    @Autowired
    public FornecedorController(FornecedorService fornecedorService, FornecedorMapper fornecedorMapper) {
        this.fornecedorService = fornecedorService;
        this.fornecedorMapper = fornecedorMapper;
    }

    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> criarFornecedor(
            @Valid @RequestBody FornecedorRequestDTO fornecedorRequestDTO) {
        FornecedorResponseDTO novoFornecedor = fornecedorService.criarFornecedor(fornecedorRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFornecedor);
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> listarTodosFornecedores() {
        List<FornecedorResponseDTO> fornecedores = fornecedorService.listarTodosFornecedores();
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> buscarFornecedorPorId(@PathVariable Long id) {
        FornecedorResponseDTO fornecedor = fornecedorService.buscarFornecedorPorId(id)
                .map(fornecedorMapper::toResponseDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Fornecedor não encontrado com ID: " + id));
        return ResponseEntity.ok(fornecedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> atualizarFornecedor(@PathVariable Long id,
            @Valid @RequestBody FornecedorRequestDTO fornecedorRequestDTO) {
        FornecedorResponseDTO fornecedorAtualizado = fornecedorService.atualizarFornecedor(id, fornecedorRequestDTO);
        return ResponseEntity.ok(fornecedorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFornecedor(@PathVariable Long id) {
        fornecedorService.deletarFornecedor(id);
        return ResponseEntity.noContent().build();
    }
}
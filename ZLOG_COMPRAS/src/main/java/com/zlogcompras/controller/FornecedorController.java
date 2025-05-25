package com.zlogcompras.controller;

import com.zlogcompras.model.dto.FornecedorRequestDTO;
import com.zlogcompras.model.dto.FornecedorResponseDTO;
import com.zlogcompras.mapper.FornecedorMapper; // Importe o mapper
import com.zlogcompras.service.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;
    private final FornecedorMapper fornecedorMapper; // Injetar o mapper

    @Autowired
    public FornecedorController(FornecedorService fornecedorService, FornecedorMapper fornecedorMapper) {
        this.fornecedorService = fornecedorService;
        this.fornecedorMapper = fornecedorMapper;
    }

    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> criarFornecedor(@Valid @RequestBody FornecedorRequestDTO fornecedorRequestDTO) {
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
        // AJUSTADO: Usar map para converter Optional<Fornecedor> para Optional<FornecedorResponseDTO>
        // e then .orElseThrow() para lidar com o caso de não encontrado
        FornecedorResponseDTO fornecedor = fornecedorService.buscarFornecedorPorId(id)
                .map(fornecedorMapper::toResponseDto) // Converter Fornecedor para FornecedorResponseDTO
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado com ID: " + id));
        return ResponseEntity.ok(fornecedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> atualizarFornecedor(@PathVariable Long id, @Valid @RequestBody FornecedorRequestDTO fornecedorRequestDTO) {
        FornecedorResponseDTO fornecedorAtualizado = fornecedorService.atualizarFornecedor(id, fornecedorRequestDTO);
        return ResponseEntity.ok(fornecedorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFornecedor(@PathVariable Long id) {
        fornecedorService.deletarFornecedor(id);
        return ResponseEntity.noContent().build();
    }
}
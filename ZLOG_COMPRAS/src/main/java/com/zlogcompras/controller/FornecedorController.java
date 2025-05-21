package com.zlogcompras.controller;

import java.util.List;
import java.util.Optional; // Importe o FornecedorService

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

import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.service.FornecedorService;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService; // Injete o serviço

    @Autowired // Injeção de dependência via construtor é preferível
    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    public ResponseEntity<Fornecedor> criarFornecedor(@RequestBody Fornecedor fornecedor) {
        System.out.println("Controller: Recebida requisição POST para /api/fornecedores.");
        System.out.println("Controller: Payload recebido (POST): " + fornecedor);
        Fornecedor novoFornecedor = fornecedorService.criarFornecedor(fornecedor);
        System.out.println("Controller: Fornecedor criado com ID: " + novoFornecedor.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFornecedor);
    }

    @GetMapping
    public ResponseEntity<List<Fornecedor>> listarTodosFornecedores() {
        System.out.println("Controller: Recebida requisição GET para /api/fornecedores (listar todos).");
        List<Fornecedor> fornecedores = fornecedorService.listarTodosFornecedores();
        System.out.println("Controller: Total de fornecedores encontrados: " + fornecedores.size());
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscarFornecedorPorId(@PathVariable Long id) {
        System.out.println("Controller: Recebida requisição GET para /api/fornecedores/" + id);
        Optional<Fornecedor> fornecedor = fornecedorService.buscarFornecedorPorId(id);

        if (fornecedor.isPresent()) {
            System.out.println("Controller: Fornecedor com ID " + id + " encontrado.");
            return ResponseEntity.ok(fornecedor.get());
        } else {
            System.out.println("Controller: Fornecedor com ID " + id + " NÃO encontrado.");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> atualizarFornecedor(@PathVariable Long id, @RequestBody Fornecedor fornecedor) {
        System.out.println("Controller: Recebida requisição PUT para /api/fornecedores/" + id + ".");
        System.out.println("Controller: Payload recebido (PUT): " + fornecedor);
        try {
            Fornecedor fornecedorAtualizado = fornecedorService.atualizarFornecedor(id, fornecedor);
            System.out.println("Controller: Fornecedor com ID " + id + " atualizado com sucesso.");
            return ResponseEntity.ok(fornecedorAtualizado);
        } catch (RuntimeException e) {
            System.out.println("Controller: Erro ao atualizar fornecedor com ID " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build(); // Retorna 404 se o fornecedor não for encontrado
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFornecedor(@PathVariable Long id) {
        System.out.println("Controller: Recebida requisição DELETE para /api/fornecedores/" + id);
        boolean deletado = fornecedorService.deletarFornecedor(id);
        if (deletado) {
            System.out.println("Controller: Fornecedor com ID " + id + " deletado com sucesso.");
            return ResponseEntity.noContent().build();
        } else {
            System.out.println("Controller: Erro: Fornecedor com ID " + id + " não encontrado para deleção.");
            return ResponseEntity.notFound().build();
        }
    }
}
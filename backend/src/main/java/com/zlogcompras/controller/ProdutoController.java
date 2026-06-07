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

import com.zlogcompras.exceptions.ResourceNotFoundException;
import com.zlogcompras.model.Produto;
import com.zlogcompras.service.ProdutoService;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /**
     * Cria um novo produto.
     * @param produto Objeto com os dados do novo produto.
     * @return ResponseEntity com o produto criado e status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.save(produto); // Usa o método save do Service
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }

    /**
     * Lista todos os produtos cadastrados.
     * @return ResponseEntity com a lista de produtos e status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = produtoService.findAll();
        return ResponseEntity.ok(produtos);
    }

    /**
     * Busca um produto por ID.
     * @param id ID do produto a ser buscado.
     * @return ResponseEntity com o produto encontrado e status 200 (OK).
     * @throws ResourceNotFoundException se o produto não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Produto produto = produtoService.findById(id); // Nome de método ajustado
        return ResponseEntity.ok(produto);
    }
    
    // Obs: Métodos PUT e DELETE foram removidos para simplificar, mas podem ser adicionados novamente
    
}
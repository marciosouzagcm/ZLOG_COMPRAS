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
import com.zlogcompras.model.dto.ProdutoRequestDTO;
import com.zlogcompras.service.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /**
     * Cria um novo produto.
     * @param produtoRequestDTO DTO com os dados do novo produto.
     * @return ResponseEntity com o produto criado e status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        Produto novoProduto = produtoService.criar(produtoRequestDTO);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }

    /**
     * Cria múltiplos produtos de uma vez (em lote).
     * @param produtosRequestDTO Lista de DTOs com os dados dos produtos.
     * @return ResponseEntity com a lista de produtos criados e status 201 (Created).
     */
    @PostMapping("/lote")
    public ResponseEntity<List<Produto>> criarEmLote(@Valid @RequestBody List<ProdutoRequestDTO> produtosRequestDTO) {
        List<Produto> novosProdutos = produtoService.criarMultiplos(produtosRequestDTO);
        return new ResponseEntity<>(novosProdutos, HttpStatus.CREATED);
    }

    /**
     * Lista todos os produtos cadastrados.
     * @return ResponseEntity com a lista de produtos e status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = produtoService.listarTodos();
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
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    /**
     * Atualiza um produto existente.
     * @param id ID do produto a ser atualizado.
     * @param produtoRequestDTO DTO com os novos dados do produto.
     * @return ResponseEntity com o produto atualizado e status 200 (OK).
     * @throws ResourceNotFoundException se o produto não for encontrado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        Produto produtoAtualizado = produtoService.atualizar(id, produtoRequestDTO);
        return ResponseEntity.ok(produtoAtualizado);
    }

    /**
     * Deleta um produto por ID.
     * @param id ID do produto a ser deletado.
     * @return ResponseEntity com status 204 (No Content) se a exclusão for bem-sucedida.
     * @throws ResourceNotFoundException se o produto não for encontrado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
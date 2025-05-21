package com.zlogcompras.controller;

import java.util.List;
import java.util.Optional;

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

import com.zlogcompras.model.Produto;
import com.zlogcompras.service.ProdutoService;

@RestController
@RequestMapping("/api/produtos") // Endpoint base para Produtos
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        System.out.println("Controller: Recebida requisição POST para /api/produtos.");
        System.out.println("Controller: Payload recebido (POST): " + produto);
        Produto novoProduto = produtoService.criarProduto(produto);
        System.out.println("Controller: Produto criado com ID: " + novoProduto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodosProdutos() {
        System.out.println("Controller: Recebida requisição GET para /api/produtos (listar todos).");
        List<Produto> produtos = produtoService.listarTodosProdutos();
        System.out.println("Controller: Total de produtos encontrados: " + produtos.size());
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        System.out.println("Controller: Recebida requisição GET para /api/produtos/" + id);
        Optional<Produto> produto = produtoService.buscarProdutoPorId(id);

        if (produto.isPresent()) {
            System.out.println("Controller: Produto com ID " + id + " encontrado.");
            return ResponseEntity.ok(produto.get());
        } else {
            System.out.println("Controller: Produto com ID " + id + " NÃO encontrado.");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        System.out.println("Controller: Recebida requisição PUT para /api/produtos/" + id + ".");
        System.out.println("Controller: Payload recebido (PUT): " + produto);

        // A validação de ID no path vs. ID no corpo da requisição pode ser adicionada aqui
        // se você quiser garantir que o cliente não tente atualizar um ID diferente do path.
        // Por exemplo:
        // if (produto.getId() != null && !id.equals(produto.getId())) {
        //     System.out.println("Controller: Erro - ID no path (" + id + ") não corresponde ao ID no corpo (" + produto.getId() + ").");
        //     return ResponseEntity.badRequest().build();
        // }

        try {
            Produto produtoAtualizado = produtoService.atualizarProduto(id, produto);
            System.out.println("Controller: Produto com ID " + id + " atualizado com sucesso.");
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) { // Captura a exceção lançada pelo service se o produto não for encontrado
            System.out.println("Controller: Erro ao atualizar produto com ID " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build(); // Retorna 404 se o produto não for encontrado
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        System.out.println("Controller: Recebida requisição DELETE para /api/produtos/" + id);
        boolean deletado = produtoService.deletarProduto(id);
        if (deletado) {
            System.out.println("Controller: Produto com ID " + id + " deletado com sucesso.");
            return ResponseEntity.noContent().build();
        } else {
            System.out.println("Controller: Erro: Produto com ID " + id + " não encontrado para deleção.");
            return ResponseEntity.notFound().build();
        }
    }
}
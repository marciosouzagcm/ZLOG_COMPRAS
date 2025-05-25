package com.zlogcompras.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // Importar DeleteMapping
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Importar PutMapping
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zlogcompras.model.Estoque;
import com.zlogcompras.service.EstoqueService;

import jakarta.persistence.EntityNotFoundException; // Importar EntityNotFoundException

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    @Autowired
    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping("/{codigoMaterial}")
    public ResponseEntity<Estoque> buscarEstoquePorCodigo(@PathVariable String codigoMaterial) {
        System.out.println("Controller: Recebida requisição GET para /api/estoque/" + codigoMaterial);
        Optional<Estoque> estoque = estoqueService.buscarPorCodigoMaterial(codigoMaterial);

        if (estoque.isPresent()) {
            System.out.println("Controller: Estoque para material " + codigoMaterial + " encontrado.");
            return ResponseEntity.ok(estoque.get());
        } else {
            System.out.println("Controller: Estoque para material " + codigoMaterial + " NÃO encontrado.");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Estoque> criarEstoque(@RequestBody Estoque estoque) {
        System.out.println("Controller: Recebida requisição POST para /api/estoque com dados: " + estoque.toString());
        try {
            Estoque novoEstoque = estoqueService.criarEstoque(estoque);
            System.out.println("Controller: Estoque criado com sucesso para o material: " + novoEstoque.getCodigoMaterial());
            return ResponseEntity.status(HttpStatus.CREATED).body(novoEstoque);
        } catch (Exception e) {
            System.err.println("ERRO: Falha ao criar estoque: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Ou HttpStatus.CONFLICT se for por duplicidade
        }
    }

    /**
     * Endpoint PUT para atualizar um registro de estoque existente.
     * Recebe o código do material na URL e o objeto Estoque atualizado no corpo da requisição.
     *
     * @param codigoMaterial O código do material a ser atualizado.
     * @param estoque O objeto Estoque com os dados atualizados (ex: nova quantidade).
     * @return ResponseEntity com o Estoque atualizado e status 200 OK, ou 404 Not Found se não existir, ou 500 Internal Server Error.
     */
    @PutMapping("/{codigoMaterial}")
    public ResponseEntity<Estoque> atualizarEstoque(@PathVariable String codigoMaterial, @RequestBody Estoque estoque) {
        System.out.println("Controller: Recebida requisição PUT para /api/estoque/" + codigoMaterial + " com dados: " + estoque.toString());
        try {
            Estoque estoqueAtualizado = estoqueService.atualizarEstoque(codigoMaterial, estoque);
            System.out.println("Controller: Estoque para material " + codigoMaterial + " atualizado.");
            return ResponseEntity.ok(estoqueAtualizado);
        } catch (EntityNotFoundException e) {
            System.err.println("ERRO: Estoque para material " + codigoMaterial + " não encontrado para atualização.");
            return ResponseEntity.notFound().build(); // Retorna 404 se não encontrar
        } catch (Exception e) {
            System.err.println("ERRO: Falha ao atualizar estoque para " + codigoMaterial + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Erro genérico do servidor
        }
    }

    /**
     * Endpoint DELETE para deletar um registro de estoque existente.
     * Recebe o código do material na URL.
     *
     * @param codigoMaterial O código do material a ser deletado.
     * @return ResponseEntity com status 204 No Content em caso de sucesso, 404 Not Found se não existir, ou 500 Internal Server Error.
     */
    @DeleteMapping("/{codigoMaterial}")
    public ResponseEntity<Void> deletarEstoque(@PathVariable String codigoMaterial) {
        System.out.println("Controller: Recebida requisição DELETE para /api/estoque/" + codigoMaterial);
        try {
            estoqueService.deletarEstoque(codigoMaterial);
            System.out.println("Controller: Estoque para material " + codigoMaterial + " deletado com sucesso.");
            return ResponseEntity.noContent().build(); // Retorna 204 No Content (sucesso sem conteúdo)
        } catch (EntityNotFoundException e) {
            System.err.println("ERRO: Estoque para material " + codigoMaterial + " não encontrado para exclusão.");
            return ResponseEntity.notFound().build(); // Retorna 404 se não encontrar
        } catch (Exception e) {
            System.err.println("ERRO: Falha ao deletar estoque para " + codigoMaterial + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Erro genérico do servidor
        }
    }
}
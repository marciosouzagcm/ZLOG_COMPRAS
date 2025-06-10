package com.zlogcompras.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; // Importado para MediaType
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.dto.ItemPedidoCompraRequestDTO;
import com.zlogcompras.model.dto.PedidoCompraRequestDTO; // Importado o DTO de requisição
import com.zlogcompras.service.PedidoCompraService;

import jakarta.validation.Valid; // Importado para validação do DTO

@RestController
@RequestMapping("/api/pedidos-compra")
public class PedidoCompraController {

    @Autowired
    private PedidoCompraService pedidoCompraService;

    // --- GET: Listar todos os pedidos de compra ---
    @GetMapping
    public ResponseEntity<List<PedidoCompra>> listarTodosPedidosCompra() {
        List<PedidoCompra> pedidos = pedidoCompraService.listarTodosPedidosCompra();
        if (pedidos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retorna 204 se não houver conteúdo
        }
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    // --- GET: Buscar pedido de compra por ID ---
    @GetMapping("/{id}")
    public ResponseEntity<PedidoCompra> buscarPedidoCompraPorId(@PathVariable Long id) {
        Optional<PedidoCompra> pedido = pedidoCompraService.buscarPedidoCompraPorId(id);
        // Usando o método map do Optional para um retorno mais limpo
        return pedido.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // --- POST: Criar um novo pedido de compra ---
    // Agora aceita PedidoCompraRequestDTO e valida-o
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // Explicitamente consome JSON
    public ResponseEntity<PedidoCompra> criarPedidoCompra(@Valid @RequestBody PedidoCompraRequestDTO pedidoRequestDTO) {
        // O serviço deve ser capaz de converter o DTO para a entidade PedidoCompra
        // ou criar uma nova entidade PedidoCompra a partir do DTO.
        // Assumimos que o serviço faz essa conversão ou a criação.
        PedidoCompra novoPedido = pedidoCompraService.criarPedidoCompra(pedidoRequestDTO);
        return new ResponseEntity<>(novoPedido, HttpStatus.CREATED);
    }

    // --- PUT: Atualizar um pedido de compra existente ---
    // Agora aceita PedidoCompraRequestDTO para atualização e valida-o
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE) // Explicitamente consome JSON
    public ResponseEntity<PedidoCompra> atualizarPedidoCompra(@PathVariable Long id,
            @Valid @RequestBody PedidoCompraRequestDTO pedidoRequestDTO) {
        // Assumimos que o serviço também lida com a conversão DTO para entidade para
        // atualização.
        Optional<PedidoCompra> pedidoExistente = pedidoCompraService.buscarPedidoCompraPorId(id);
        if (pedidoExistente.isPresent()) {
            // No serviço, você pode usar o DTO para atualizar os campos da entidade
            // existente
            PedidoCompra pedidoSalvo = pedidoCompraService.atualizarPedidoCompra(id, pedidoRequestDTO);
            return new ResponseEntity<>(pedidoSalvo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --- PUT: Atualizar apenas o status de um pedido de compra ---
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatusPedidoCompra(@PathVariable Long id, @RequestParam String novoStatus) {
        // Você pode adicionar validações aqui se o status for de um conjunto
        // pré-definido.
        // Ex: if (!Arrays.asList("Pendente", "Aprovado",
        // "Cancelado").contains(novoStatus)) { ... }
        pedidoCompraService.atualizarStatusPedidoCompra(id, novoStatus);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content para operações de sucesso que não retornam
                                                            // corpo
    }

    // --- GET (Novo): Buscar pedidos de compra por fornecedor ---
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<PedidoCompra>> buscarPedidosCompraPorFornecedor(@PathVariable Long fornecedorId) {
        List<PedidoCompra> pedidos = pedidoCompraService.buscarPedidosPorFornecedor(fornecedorId);
        if (pedidos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    // --- GET (Novo): Buscar pedidos de compra por status ---
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PedidoCompra>> buscarPedidosCompraPorStatus(@PathVariable String status) {
        List<PedidoCompra> pedidos = pedidoCompraService.buscarPedidosPorStatus(status);
        if (pedidos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    // --- Endpoint para adicionar itens a um pedido de compra existente (exemplo de
    // como seria) ---

    @PostMapping(value = "/{pedidoId}/itens", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PedidoCompra> adicionarItensAoPedido(@PathVariable Long pedidoId,
            @Valid @RequestBody List<ItemPedidoCompraRequestDTO> novosItensDTO) {
        // Esta lógica deve ser implementada no PedidoCompraService
        PedidoCompra pedidoAtualizado = pedidoCompraService.adicionarItens(pedidoId, novosItensDTO);
        return new ResponseEntity<>(pedidoAtualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedidoCompra(@PathVariable Long id) {
        pedidoCompraService.deletar(id); // Chama o serviço para deletar o pedido
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}
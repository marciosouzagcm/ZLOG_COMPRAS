package com.zlogcompras.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // <--- ADICIONADA ESTA IMPORTAÇÃO!
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.mapper.PedidoCompraMapper;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.StatusPedidoCompra;
import com.zlogcompras.model.dto.ItemPedidoCompraRequestDTO;
import com.zlogcompras.model.dto.PedidoCompraRequestDTO;
import com.zlogcompras.model.dto.PedidoCompraResponseDTO;
import com.zlogcompras.service.PedidoCompraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos-compra")
public class PedidoCompraController {

    private final PedidoCompraService pedidoCompraService;
    private final PedidoCompraMapper pedidoCompraMapper;

    @Autowired
    public PedidoCompraController(PedidoCompraService pedidoCompraService, PedidoCompraMapper pedidoCompraMapper) {
        this.pedidoCompraService = pedidoCompraService;
        this.pedidoCompraMapper = pedidoCompraMapper;
    }

    @PostMapping
    public ResponseEntity<PedidoCompra> criarPedidoCompra(@Valid @RequestBody PedidoCompraRequestDTO pedidoRequestDTO) {
        PedidoCompra novoPedido = pedidoCompraService.criarPedidoCompra(pedidoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    @GetMapping
    public ResponseEntity<List<PedidoCompra>> listarTodosPedidosCompra() {
        List<PedidoCompra> pedidos = pedidoCompraService.listarTodosPedidosCompra();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoCompra> buscarPedidoCompraPorId(@PathVariable Long id) {
        return pedidoCompraService.buscarPedidoCompraPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido de compra não encontrado com ID: " + id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoCompraResponseDTO> atualizarStatusPedidoCompra(@PathVariable Long id, 
                                                                              @RequestParam String novoStatus) { 
        StatusPedidoCompra novoStatusEnum;
        try {
            novoStatusEnum = StatusPedidoCompra.valueOf(novoStatus.toUpperCase()); 
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + novoStatus); 
        }
        
        PedidoCompra updatedPedido = pedidoCompraService.atualizarStatusPedidoCompra(id, novoStatusEnum);
        return ResponseEntity.ok(pedidoCompraMapper.toResponseDto(updatedPedido));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PedidoCompraResponseDTO>> buscarPedidosPorStatus(@PathVariable String status) { 
        StatusPedidoCompra statusEnum;
        try {
            statusEnum = StatusPedidoCompra.valueOf(status.toUpperCase()); 
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido para busca: " + status); 
        }
        
        List<PedidoCompra> pedidos = pedidoCompraService.buscarPedidosPorStatus(statusEnum);
        return ResponseEntity.ok(pedidoCompraMapper.toResponseDtoList(pedidos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoCompra> atualizarPedidoCompra(@PathVariable Long id, @Valid @RequestBody PedidoCompraRequestDTO pedidoRequestDTO) {
        PedidoCompra updatedPedido = pedidoCompraService.atualizarPedidoCompra(id, pedidoRequestDTO);
        return ResponseEntity.ok(updatedPedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedidoCompra(@PathVariable Long id) {
        pedidoCompraService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Método para adicionar itens a um pedido existente (linha 98 do seu erro)
    @PostMapping("/{id}/itens")
    public ResponseEntity<PedidoCompra> adicionarItensAoPedido(@PathVariable Long id, @Valid @RequestBody List<ItemPedidoCompraRequestDTO> novosItensDTO) {
        PedidoCompra updatedPedido = pedidoCompraService.adicionarItens(id, novosItensDTO);
        return ResponseEntity.ok(updatedPedido);
    }
}
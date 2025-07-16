package com.zlogcompras.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zlogcompras.mapper.PedidoCompraMapper;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.StatusPedidoCompra;
import com.zlogcompras.model.dto.ItemPedidoCompraRequestDTO;
import com.zlogcompras.model.dto.PedidoCompraRequestDTO;
import com.zlogcompras.model.dto.PedidoCompraResponseDTO;
import com.zlogcompras.model.dto.PedidoCompraUpdateDTO; // Importar PedidoCompraUpdateDTO
import com.zlogcompras.service.PedidoCompraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos-compra")
public class PedidoCompraController {

    private final PedidoCompraService pedidoCompraService;
    private final PedidoCompraMapper pedidoCompraMapper;

    @Autowired
    public PedidoCompraController(PedidoCompraService pedidoCompraService, PedidoCompraMapper pedidoCompraMapper) {
        this.pedidoCompraService = pedidoCompraService;
        this.pedidoCompraMapper = pedidoCompraMapper;
    }

    @PostMapping
    public ResponseEntity<PedidoCompraResponseDTO> criarPedidoCompra(
            @Valid @RequestBody PedidoCompraRequestDTO requestDTO) {
        PedidoCompra pedidoCompra = pedidoCompraService.criarPedidoCompra(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCompraMapper.toResponseDto(pedidoCompra));
    }

    @GetMapping
    public ResponseEntity<List<PedidoCompraResponseDTO>> listarTodosPedidos() { // CORREÇÃO: Nome do método
        List<PedidoCompra> pedidos = pedidoCompraService.listarTodosPedidos(); // CORREÇÃO: Nome do método
        List<PedidoCompraResponseDTO> dtos = pedidos.stream()
                .map(pedidoCompraMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoCompraResponseDTO> buscarPedidoPorId(@PathVariable Long id) { // CORREÇÃO: Nome do
                                                                                              // método
        PedidoCompra pedido = pedidoCompraService.buscarPedidoPorId(id); // CORREÇÃO: Nome do método
        return ResponseEntity.ok(pedidoCompraMapper.toResponseDto(pedido));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoCompraResponseDTO> atualizarStatusPedido( // CORREÇÃO: Nome do método
            @PathVariable Long id,
            @RequestParam StatusPedidoCompra status) { // CORREÇÃO: Tipo de parâmetro
        PedidoCompra pedidoAtualizado = pedidoCompraService.atualizarStatusPedido(id, status); // CORREÇÃO: Nome do
                                                                                               // método
        return ResponseEntity.ok(pedidoCompraMapper.toResponseDto(pedidoAtualizado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoCompraResponseDTO> atualizarPedidoCompra(
            @PathVariable Long id,
            @Valid @RequestBody PedidoCompraUpdateDTO updateDTO) { // CORREÇÃO: Tipo do DTO aqui
        PedidoCompra pedidoAtualizado = pedidoCompraService.atualizarPedidoCompra(id, updateDTO);
        return ResponseEntity.ok(pedidoCompraMapper.toResponseDto(pedidoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) { // CORREÇÃO: Nome do método
        pedidoCompraService.deletarPedido(id); // CORREÇÃO: Nome do método
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<PedidoCompraResponseDTO> adicionarItensAoPedido( // CORREÇÃO: Nome do método
            @PathVariable Long id,
            @Valid @RequestBody List<ItemPedidoCompraRequestDTO> novosItensDto) { // CORREÇÃO: Lista de DTOs
        PedidoCompra pedidoAtualizado = pedidoCompraService.adicionarItensAoPedido(id, novosItensDto); // CORREÇÃO: Nome
                                                                                                       // do método
        return ResponseEntity.ok(pedidoCompraMapper.toResponseDto(pedidoAtualizado));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedidoCompra(@PathVariable Long id) {
        pedidoCompraService.cancelarPedidoCompra(id);
        return ResponseEntity.noContent().build();
    }
}
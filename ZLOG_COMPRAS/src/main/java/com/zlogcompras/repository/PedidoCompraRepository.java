package com.zlogcompras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.StatusPedidoCompra;

@Repository
public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {

    // Método para buscar pedidos por status, aceitando o enum StatusPedidoCompra
    List<PedidoCompra> findByStatus(StatusPedidoCompra status);

    // Método para buscar pedidos por ID do fornecedor
    List<PedidoCompra> findByFornecedorId(Long fornecedorId);

    // Método para verificar se existe um PedidoCompra associado a um determinado ID de orçamento
    boolean existsByOrcamentoId(Long orcamentoId);
}
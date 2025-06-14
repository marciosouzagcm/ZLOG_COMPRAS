package com.zlogcompras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; // Importe o enum
import org.springframework.stereotype.Repository;

import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.StatusPedidoCompra;

@Repository
public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {

    // Altere a assinatura deste método para aceitar o enum diretamente
    List<PedidoCompra> findByStatus(StatusPedidoCompra status); // <--- CORRIGIDO AQUI!

    List<PedidoCompra> findByFornecedorId(Long fornecedorId);
}
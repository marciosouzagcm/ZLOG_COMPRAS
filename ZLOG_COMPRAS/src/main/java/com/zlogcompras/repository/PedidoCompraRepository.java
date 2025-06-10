package com.zlogcompras.repository;

import com.zlogcompras.model.PedidoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {
    List<PedidoCompra> findByFornecedorId(Long fornecedorId);
    List<PedidoCompra> findByStatus(String status);
}
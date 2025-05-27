package com.zlogcompras.repository;

import com.zlogcompras.model.SolicitacaoCompra; // Import correto da entidade
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoCompraRepository extends JpaRepository<SolicitacaoCompra, Long> {
    // Se você tiver métodos de busca personalizados, eles iriam aqui.
    // Exemplo: Optional<SolicitacaoCompra> findBySolicitante(String solicitante);
}
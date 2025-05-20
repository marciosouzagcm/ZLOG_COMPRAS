package com.zlogcompras.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zlogcompras.model.SolicitacaoCompra;

public interface SolicitacaoCompraRepository extends JpaRepository<SolicitacaoCompra, Long> {

    @Override
    @EntityGraph(attributePaths = "itens")
    List<SolicitacaoCompra> findAll();

    @Override
    @EntityGraph(attributePaths = "itens")
    Optional<SolicitacaoCompra> findById(Long id);
}
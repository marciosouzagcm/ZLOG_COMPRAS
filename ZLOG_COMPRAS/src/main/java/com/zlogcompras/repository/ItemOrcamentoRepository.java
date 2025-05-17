package com.zlogcompras.repository;

import java.util.List; // Importe sua classe de entidade

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zlogcompras.model.ItemOrcamento;

@Repository
public interface ItemOrcamentoRepository extends JpaRepository<ItemOrcamento, Long> {

    List<ItemOrcamento> findByOrcamento_Id(Long orcamentoId);
}
package com.zlogcompras.repository;

import java.util.List; // Importação adicionada

import com.zlogcompras.model.ItemOrcamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOrcamentoRepository extends JpaRepository<ItemOrcamento, Long> {

    // Sugestão: Buscar itens de orçamento por um produto específico
    List<ItemOrcamento> findByProduto_Id(Long produtoId);

    // Sugestão: Buscar itens de orçamento por um orçamento específico
    List<ItemOrcamento> findByOrcamento_Id(Long orcamentoId);
}
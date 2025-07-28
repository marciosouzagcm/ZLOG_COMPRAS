package com.zlogcompras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional; // Importe Transactional aqui

import com.zlogcompras.model.ItemOrcamento;

public interface ItemOrcamentoRepository extends JpaRepository<ItemOrcamento, Long> {

    // Método para buscar itens de orçamento por um produto específico
    List<ItemOrcamento> findByProduto_Id(Long produtoId);

    // Método para buscar itens de orçamento por um orçamento específico
    List<ItemOrcamento> findByOrcamento_Id(Long orcamentoId);

    // **NOVO MÉTODO PARA DELETAR ITENS DE ORÇAMENTO POR PRODUTO_ID**
    // É crucial que esta operação seja transacional para garantir que a exclusão
    // seja atômica.
    @Transactional
    void deleteByProduto_Id(Long produtoId);
}
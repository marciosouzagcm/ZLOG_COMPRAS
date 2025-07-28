package com.zlogcompras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional; // Importe Transactional aqui

import com.zlogcompras.model.ItemSolicitacaoCompra;

public interface ItemSolicitacaoCompraRepository extends JpaRepository<ItemSolicitacaoCompra, Long> {

    // Busca itens de solicitação de compra por ID da solicitação de compra.
    List<ItemSolicitacaoCompra> findBySolicitacaoCompraId(Long solicitacaoCompraId);

    // Método para buscar itens de solicitação de compra por ID do produto
    List<ItemSolicitacaoCompra> findByProduto_Id(Long produtoId);

    // NOVO MÉTODO PARA DELETAR ITENS DE SOLICITAÇÃO POR PRODUTO_ID
    @Transactional // É crucial para operações de deleção em lote
    void deleteByProduto_Id(Long produtoId);
}
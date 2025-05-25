package com.zlogcompras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.SolicitacaoCompra; // Importação da entidade

public interface ItemSolicitacaoCompraRepository extends JpaRepository<ItemSolicitacaoCompra, Long> {

    // Busca itens de solicitação de compra por ID da solicitação de compra.
    List<ItemSolicitacaoCompra> findBySolicitacaoCompraId(Long solicitacaoCompraId);

    // Considerar REMOVER este método se o gerenciamento dos itens for feito
    // via a coleção 'itens' na entidade SolicitacaoCompra com orphanRemoval = true,
    // pois a remoção dos itens é automática.
    // void deleteBySolicitacaoCompra(SolicitacaoCompra solicitacaoExistente);
}
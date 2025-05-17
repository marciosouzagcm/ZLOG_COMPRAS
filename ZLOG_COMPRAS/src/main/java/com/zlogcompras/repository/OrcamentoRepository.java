package com.zlogcompras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.SolicitacaoCompra;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    List<Orcamento> findBySolicitacaoCompra(SolicitacaoCompra solicitacaoCompra);

    List<Orcamento> findBySolicitacaoCompra_Id(Long solicitacaoCompraId);

    // Outros métodos personalizados, como buscar por fornecedor ou status poderiam ser adicionados aqui
    // Exemplo:
    // List<Orcamento> findByFornecedor_Id(Long fornecedorId); // Assumindo relacionamento com Fornecedor
    // List<Orcamento> findByStatus(String status);
}
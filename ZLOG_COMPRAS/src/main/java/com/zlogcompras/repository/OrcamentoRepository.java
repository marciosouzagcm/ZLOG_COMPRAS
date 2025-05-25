package com.zlogcompras.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.SolicitacaoCompra;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    List<Orcamento> findBySolicitacaoCompra(SolicitacaoCompra solicitacaoCompra);

    List<Orcamento> findBySolicitacaoCompra_Id(Long solicitacaoCompraId);

    List<Orcamento> findByFornecedor_Id(Long fornecedorId);

    List<Orcamento> findByDataCotacaoBetween(LocalDate dataInicio, LocalDate dataFim);

        List<Orcamento> findByStatus(String status);

        List<Orcamento> findBySolicitacaoCompraId(Long solicitacaoId);
    }
package com.zlogcompras.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.StatusOrcamento; // Importe o Enum StatusOrcamento

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    // ... (seus métodos existentes) ...

    List<Orcamento> findBySolicitacaoCompra_Id(Long solicitacaoCompraId);

    List<Orcamento> findByFornecedor_Id(Long fornecedorId);

    List<Orcamento> findByDataCotacaoBetween(LocalDate dataInicio, LocalDate dataFim);

    // Se StatusOrcamento for Enum, mude para:
    List<Orcamento> findByStatus(StatusOrcamento status);
    // Se 'status' no Orcamento é String, então mantenha:
    // List<Orcamento> findByStatus(String status);

    List<Orcamento> findBySolicitacaoCompraId(Long solicitacaoId);

    List<Orcamento> findBySolicitacaoCompraIdAndIdNot(Long solicitacaoCompraId, Long idDoOrcamentoExcluir);

    // --- NOVO MÉTODO ESSENCIAL PARA O FLUXO DE APROVAÇÃO ---
    /**
     * Busca todos os orçamentos associados a uma Solicitação de Compra,
     * exceto aquele cujo ID é fornecido, e que estejam em um status específico.
     * Usado para encontrar os orçamentos que podem ser rejeitados quando um é
     * aprovado.
     *
     * @param solicitacaoCompraId  O ID da Solicitação de Compra.
     * @param idDoOrcamentoExcluir O ID do orçamento que deve ser excluído do
     *                             resultado.
     * @param status               O status dos orçamentos a serem buscados (e.g.,
     *                             AGUARDANDO_APROVACAO).
     * @return Uma lista de orçamentos.
     */
    List<Orcamento> findBySolicitacaoCompraIdAndIdNotAndStatus(Long solicitacaoCompraId, Long idDoOrcamentoExcluir,
            StatusOrcamento status);
}
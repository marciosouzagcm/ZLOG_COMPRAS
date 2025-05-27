package com.zlogcompras.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.StatusSolicitacaoCompra; // Certifique-se que está importado e é o Enum correto

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    // Este método pode estar com a assinatura incorreta se você realmente quer buscar
    // orçamentos por uma solicitacaoCompra_Id através de um StatusSolicitacaoCompra.
    // O mais comum seria buscar por solicitacaoCompra.id
    // List<Orcamento> findBySolicitacaoCompra(StatusSolicitacaoCompra solicitacaoCompra);

    // Busca orçamentos pelo ID da solicitação de compra (já existe e está correto)
    List<Orcamento> findBySolicitacaoCompra_Id(Long solicitacaoCompraId);

    // Busca orçamentos pelo ID do fornecedor (já existe e está correto)
    List<Orcamento> findByFornecedor_Id(Long fornecedorId);

    // Busca orçamentos por período de data de cotação (já existe e está correto)
    List<Orcamento> findByDataCotacaoBetween(LocalDate dataInicio, LocalDate dataFim);

    // Busca orçamentos por status (já existe e está correto, assumindo que `status` é um Enum ou String)
    List<Orcamento> findByStatus(String status); // Se StatusOrcamento for Enum, mude para List<Orcamento> findByStatus(StatusOrcamento status);

    // Busca orçamentos para uma solicitação de compra específica (já existe e está correto)
    List<Orcamento> findBySolicitacaoCompraId(Long solicitacaoId);


    // --- NOVO MÉTODO NECESSÁRIO PARA O FLUXO DE APROVAÇÃO ---
    /**
     * Busca todos os orçamentos associados a uma Solicitação de Compra,
     * exceto aquele cujo ID é fornecido.
     * Usado para encontrar os orçamentos a serem rejeitados quando um é aprovado.
     *
     * @param solicitacaoCompraId O ID da Solicitação de Compra.
     * @param idDoOrcamentoExcluir O ID do orçamento que deve ser excluído do resultado.
     * @return Uma lista de orçamentos.
     */
    List<Orcamento> findBySolicitacaoCompraIdAndIdNot(Long solicitacaoCompraId, Long idDoOrcamentoExcluir);
}
package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid; // Para validar itens aninhados
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty; // Para validar que a lista não é vazia
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class OrcamentoRequestDTO { // Renomeado para OrcamentoRequestDTO

    // ID geralmente não é enviado na criação. Em atualizações (PUT), pode vir do path.
    // private Long id; // Removido para POST, o id para PUT virá do @PathVariable

    @NotNull(message = "O ID da Solicitação de Compra é obrigatório.")
    @Positive(message = "O ID da Solicitação de Compra deve ser positivo.")
    private Long solicitacaoCompraId;

    @NotNull(message = "O ID do Fornecedor é obrigatório.")
    @Positive(message = "O ID do Fornecedor deve ser positivo.")
    private Long fornecedorId;

    // dataCotacao pode ser nulo na entrada, para ser gerado no backend com LocalDate.now()
    private LocalDate dataCotacao;

    @NotNull(message = "O valor total é obrigatório.")
    @Positive(message = "O valor total deve ser positivo.")
    private BigDecimal valorTotal;

    @NotBlank(message = "O número do orçamento é obrigatório.")
    @Size(max = 50, message = "O número do orçamento não pode exceder 50 caracteres.")
    private String numeroOrcamento;

    @Size(max = 100, message = "O prazo de entrega não pode exceder 100 caracteres.")
    private String prazoEntrega; // Opcional

    @Size(max = 255, message = "As condições de pagamento não podem exceder 255 caracteres.")
    private String condicoesPagamento; // Opcional

    @Size(max = 500, message = "As observações não podem exceder 500 caracteres.")
    private String observacoes; // Opcional

    // Status pode ser definido no backend na criação, ou pode ser atualizável via PUT.
    // Dependendo da sua regra de negócio, pode ter @NotBlank ou não.
    private String status;

    @NotEmpty(message = "Um orçamento deve ter pelo menos um item.")
    @Valid // Garante que cada item na lista também será validado
    private List<ItemOrcamentoRequestDTO> itensOrcamento; // Usamos o DTO de Request para os itens

    // Construtor padrão
    public OrcamentoRequestDTO() {
        this.dataCotacao = LocalDate.now(); // Pode ser predefinido no Request DTO
    }

    // Construtor completo (opcional, pode ser útil para testes)
    public OrcamentoRequestDTO(Long solicitacaoCompraId, Long fornecedorId, LocalDate dataCotacao,
                               BigDecimal valorTotal, String numeroOrcamento, String prazoEntrega,
                               String condicoesPagamento, String observacoes, String status,
                               List<ItemOrcamentoRequestDTO> itensOrcamento) {
        this.solicitacaoCompraId = solicitacaoCompraId;
        this.fornecedorId = fornecedorId;
        this.dataCotacao = dataCotacao;
        this.valorTotal = valorTotal;
        this.numeroOrcamento = numeroOrcamento;
        this.prazoEntrega = prazoEntrega;
        this.condicoesPagamento = condicoesPagamento;
        this.observacoes = observacoes;
        this.status = status;
        this.itensOrcamento = itensOrcamento;
    }

    // --- Getters e Setters ---
    // Removido o getter/setter de ID para RequestDTO por padrão. Se necessário para PUT,
    // o MapStruct pode lidar com o ID do @PathVariable.

    public Long getSolicitacaoCompraId() {
        return solicitacaoCompraId;
    }

    public void setSolicitacaoCompraId(Long solicitacaoCompraId) {
        this.solicitacaoCompraId = solicitacaoCompraId;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public LocalDate getDataCotacao() {
        return dataCotacao;
    }

    public void setDataCotacao(LocalDate dataCotacao) {
        this.dataCotacao = dataCotacao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getNumeroOrcamento() {
        return numeroOrcamento;
    }

    public void setNumeroOrcamento(String numeroOrcamento) {
        this.numeroOrcamento = numeroOrcamento;
    }

    public String getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(String prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    public String getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(String condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemOrcamentoRequestDTO> getItensOrcamento() {
        return itensOrcamento;
    }

    public void setItensOrcamento(List<ItemOrcamentoRequestDTO> itensOrcamento) {
        this.itensOrcamento = itensOrcamento;
    }

    @Override
    public String toString() {
        return "OrcamentoRequestDTO{" +
               "solicitacaoCompraId=" + solicitacaoCompraId +
               ", fornecedorId=" + fornecedorId +
               ", valorTotal=" + valorTotal +
               ", numeroOrcamento='" + numeroOrcamento + '\'' +
               ", status='" + status + '\'' +
               ", itensOrcamento=" + (itensOrcamento != null ? itensOrcamento.size() : 0) +
               '}';
    }
}
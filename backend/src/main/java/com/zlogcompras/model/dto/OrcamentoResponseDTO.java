package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.zlogcompras.model.Orcamento;

public class OrcamentoResponseDTO {

    private Long id;
    private Long solicitacaoCompraId;
    private String descricaoSolicitacaoCompra;
    private Long fornecedorId; // Adicionado conforme esperado nos testes
    private String nomeFornecedor;
    private String cnpjFornecedor;
    private LocalDate dataCotacao;
    private String numeroOrcamento;
    private String status;
    private BigDecimal valorTotal;
    private String observacoes;
    private String condicoesPagamento;
    private String prazoEntrega;
    private Long version;
    private List<ItemOrcamentoResponseDTO> itensOrcamento;
    private Orcamento orcamentoAprovado;

    // Construtor padrão
    public OrcamentoResponseDTO() {
    }

    // Construtor completo (ajuste conforme os parâmetros usados nos testes)
    public OrcamentoResponseDTO(Long id, Long solicitacaoCompraId, Long fornecedorId,
            LocalDate dataCotacao, BigDecimal valorTotal, String numeroOrcamento,
            String prazoEntrega, String condicoesPagamento, String observacoes,
            String status, List<ItemOrcamentoResponseDTO> itensOrcamento) {
        this.id = id;
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getDescricaoSolicitacaoCompra() {
        return descricaoSolicitacaoCompra;
    }

    public void setDescricaoSolicitacaoCompra(String descricaoSolicitacaoCompra) {
        this.descricaoSolicitacaoCompra = descricaoSolicitacaoCompra;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }

    public LocalDate getDataCotacao() {
        return dataCotacao;
    }

    public void setDataCotacao(LocalDate dataCotacao) {
        this.dataCotacao = dataCotacao;
    }

    public String getNumeroOrcamento() {
        return numeroOrcamento;
    }

    public void setNumeroOrcamento(String numeroOrcamento) {
        this.numeroOrcamento = numeroOrcamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(String condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public String getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(String prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<ItemOrcamentoResponseDTO> getItensOrcamento() {
        return itensOrcamento;
    }

    public void setItensOrcamento(List<ItemOrcamentoResponseDTO> itensOrcamento) {
        this.itensOrcamento = itensOrcamento;
    }

    public Orcamento getOrcamentoAprovado() {
        return orcamentoAprovado;
    }

    public void setOrcamentoAprovado(Orcamento orcamentoAprovado) {
        this.orcamentoAprovado = orcamentoAprovado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrcamentoResponseDTO that = (OrcamentoResponseDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrcamentoResponseDTO{" +
                "id=" + id +
                ", solicitacaoCompraId=" + solicitacaoCompraId +
                ", fornecedorId=" + fornecedorId +
                ", nomeFornecedor='" + nomeFornecedor + '\'' +
                ", status='" + status + '\'' +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
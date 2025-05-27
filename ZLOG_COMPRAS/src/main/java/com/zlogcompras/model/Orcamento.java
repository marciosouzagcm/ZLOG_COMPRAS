package com.zlogcompras.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "orcamentos")
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CORRIGIDO: Mapeia para a entidade SolicitacaoCompra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_compra_id", nullable = false)
    private SolicitacaoCompra solicitacaoCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "data_cotacao", nullable = false)
    private LocalDate dataCotacao;

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "numero_orcamento", nullable = false)
    private String numeroOrcamento;

    @Column(name = "prazo_entrega")
    private String prazoEntrega;

    @Column(name = "condicoes_pagamento")
    private String condicoesPagamento;

    @Column(name = "observacoes")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrcamento status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @Version
    private Long version;

    @JsonManagedReference
    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemOrcamento> itensOrcamento = new ArrayList<>();

    // Construtor padrão
    public Orcamento() {
        this.status = StatusOrcamento.AGUARDANDO_APROVACAO;
    }

    // Construtor com campos
    // CORRIGIDO: O primeiro parâmetro deve ser a entidade SolicitacaoCompra
    public Orcamento(SolicitacaoCompra solicitacaoCompra, Fornecedor fornecedor, LocalDate dataCotacao,
                     BigDecimal valorTotal, String numeroOrcamento, String prazoEntrega,
                     String condicoesPagamento, String observacoes, List<ItemOrcamento> itensOrcamento) {
        this.solicitacaoCompra = solicitacaoCompra;
        this.fornecedor = fornecedor;
        this.dataCotacao = dataCotacao;
        this.valorTotal = valorTotal;
        this.numeroOrcamento = numeroOrcamento;
        this.prazoEntrega = prazoEntrega;
        this.condicoesPagamento = condicoesPagamento;
        this.observacoes = observacoes;
        this.status = StatusOrcamento.AGUARDANDO_APROVACAO;
        if (itensOrcamento != null) {
            this.setItensOrcamento(itensOrcamento);
        }
    }

    // PrePersist e PreUpdate para gerenciar datas de criação e atualização
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // CORRIGIDO: Retorna a entidade SolicitacaoCompra
    public SolicitacaoCompra getSolicitacaoCompra() {
        return solicitacaoCompra;
    }

    // CORRIGIDO: Recebe a entidade SolicitacaoCompra
    public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
        this.solicitacaoCompra = solicitacaoCompra;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
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

    public StatusOrcamento getStatus() {
        return status;
    }

    public void setStatus(StatusOrcamento status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<ItemOrcamento> getItensOrcamento() {
        return itensOrcamento;
    }

    public void setItensOrcamento(List<ItemOrcamento> itensOrcamento) {
        this.itensOrcamento.clear();
        if (itensOrcamento != null) {
            for (ItemOrcamento item : itensOrcamento) {
                addItemOrcamento(item);
            }
        }
    }

    public void addItemOrcamento(ItemOrcamento item) {
        if (item != null) {
            this.itensOrcamento.add(item);
            item.setOrcamento(this);
        }
    }

    public void removeItemOrcamento(ItemOrcamento item) {
        if (item != null && this.itensOrcamento.contains(item)) {
            this.itensOrcamento.remove(item);
            item.setOrcamento(null);
        }
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orcamento orcamento = (Orcamento) o;
        return Objects.equals(id, orcamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Orcamento{" +
                "id=" + id +
                ", solicitacaoCompraId=" + (solicitacaoCompra != null ? solicitacaoCompra.getId() : "null") +
                ", fornecedorId=" + (fornecedor != null ? fornecedor.getId() : "null") +
                ", dataCotacao=" + dataCotacao +
                ", valorTotal=" + valorTotal +
                ", numeroOrcamento='" + numeroOrcamento + '\'' +
                ", status='" + status + '\'' +
                ", itensOrcamentoCount=" + (itensOrcamento != null ? itensOrcamento.size() : 0) +
                ", version=" + version +
                '}';
    }
}
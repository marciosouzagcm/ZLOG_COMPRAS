package com.zlogcompras.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "itens_orcamento")
public class ItemOrcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantidade;

    @Column(name = "preco_unitario_cotado", nullable = false, precision = 12, scale = 2) // Campo renomeado
    private BigDecimal precoUnitarioCotado; // Campo renomeado para 'precoUnitarioCotado'

    @Column(length = 255)
    private String observacoes;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orcamento_id", nullable = false)
    private Orcamento orcamento;

    @Version
    private Long version;

    public ItemOrcamento() {
    }

    public ItemOrcamento(Produto produto, BigDecimal quantidade, BigDecimal precoUnitarioCotado, String observacoes, // Construtor ajustado
                         Orcamento orcamento) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitarioCotado = precoUnitarioCotado; // Inicializa precoUnitarioCotado
        this.observacoes = observacoes;
        this.orcamento = orcamento;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitarioCotado() { // Getter renomeado
        return precoUnitarioCotado;
    }

    public void setPrecoUnitarioCotado(BigDecimal precoUnitarioCotado) { // Setter renomeado
        this.precoUnitarioCotado = precoUnitarioCotado;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ItemOrcamento that = (ItemOrcamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ItemOrcamento{" +
                "id=" + id +
                ", produtoId=" + (produto != null ? produto.getId() : "null") +
                ", quantidade=" + quantidade +
                ", precoUnitarioCotado=" + precoUnitarioCotado + // Ajustado para novo nome
                ", observacoes='" + observacoes + '\'' +
                ", orcamentoId=" + (orcamento != null ? orcamento.getId() : "null") +
                ", version=" + version +
                '}';
    }
}
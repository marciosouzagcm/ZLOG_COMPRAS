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

/**
 * Entidade JPA que representa a tabela 'itens_orcamento'.
 * Mapeamento refatorado para remover campos replicados de dados cadastrais do produto,
 * consolidando uma arquitetura relacional limpa e unificando as precisões decimais.
 */
@Entity
@Table(name = "itens_orcamento")
public class ItemOrcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento relacional centralizado com o cadastro de produtos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // Precisão ajustada para 4 casas decimais em conformidade com o script V1__init.sql
    @Column(name = "quantidade", nullable = false, precision = 15, scale = 4)
    private BigDecimal quantidade;

    @Column(name = "preco_unitario_cotado", nullable = false, precision = 15, scale = 4)
    private BigDecimal precoUnitarioCotado;

    @Column(name = "observacoes", length = 255)
    private String observacoes;

    // Relacionamento com o cabeçalho da cotação/orçamento
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orcamento_id", nullable = false)
    private Orcamento orcamento;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    /**
     * CONSTRUTORES
     */
    
    // Construtor padrão obrigatório para o ciclo de vida do Hibernate
    public ItemOrcamento() {
    }

    // Construtor completo limpo, sem os atributos obsoletos de texto do produto
    public ItemOrcamento(Produto produto, BigDecimal quantidade, BigDecimal precoUnitarioCotado, 
                         String observacoes, Orcamento orcamento) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitarioCotado = precoUnitarioCotado;
        this.observacoes = observacoes;
        this.orcamento = orcamento;
    }

    /**
     * GETTERS E SETTERS
     */
    
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

    public BigDecimal getPrecoUnitarioCotado() {
        return precoUnitarioCotado;
    }

    public void setPrecoUnitarioCotado(BigDecimal precoUnitarioCotado) {
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

    /**
     * CONTRATOS DE IDENTIDADE E COMPARAÇÃO
     */
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
                ", precoUnitarioCotado=" + precoUnitarioCotado +
                ", orcamentoId=" + (orcamento != null ? orcamento.getId() : "null") +
                ", version=" + version +
                '}';
    }
}
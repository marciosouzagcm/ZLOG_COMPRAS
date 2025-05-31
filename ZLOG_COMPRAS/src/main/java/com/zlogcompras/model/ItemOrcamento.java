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

    // A relação com Produto ainda é importante para buscar detalhes ou para validação
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // --- NOVOS CAMPOS PARA ARMAZENAR DADOS DO PRODUTO NO MOMENTO DA COTAÇÃO ---
    @Column(name = "nome_produto", nullable = false, length = 255) // Adicionado nome_produto
    private String nomeProduto;

    @Column(name = "codigo_produto", length = 50) // Adicionado codigo_produto
    private String codigoProduto;

    @Column(name = "unidade_medida_produto", length = 20) // Adicionado unidade_medida_produto
    private String unidadeMedidaProduto;
    // --- FIM DOS NOVOS CAMPOS ---


    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantidade;

    @Column(name = "preco_unitario_cotado", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitarioCotado;

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

    // Você pode querer atualizar este construtor para incluir os novos campos,
    // ou criar um novo construtor mais completo se usar múltiplos.
    public ItemOrcamento(Produto produto, BigDecimal quantidade, BigDecimal precoUnitarioCotado, String observacoes,
                         Orcamento orcamento, String nomeProduto, String codigoProduto, String unidadeMedidaProduto) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitarioCotado = precoUnitarioCotado;
        this.observacoes = observacoes;
        this.orcamento = orcamento;
        this.nomeProduto = nomeProduto;
        this.codigoProduto = codigoProduto;
        this.unidadeMedidaProduto = unidadeMedidaProduto;
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

    // --- Getters e Setters para os novos campos ---
    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getUnidadeMedidaProduto() {
        return unidadeMedidaProduto;
    }

    public void setUnidadeMedidaProduto(String unidadeMedidaProduto) {
        this.unidadeMedidaProduto = unidadeMedidaProduto;
    }
    // --- FIM dos Getters e Setters para novos campos ---


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
                ", nomeProduto='" + nomeProduto + '\'' + // Adicionado no toString
                ", codigoProduto='" + codigoProduto + '\'' + // Adicionado no toString
                ", unidadeMedidaProduto='" + unidadeMedidaProduto + '\'' + // Adicionado no toString
                ", quantidade=" + quantidade +
                ", precoUnitarioCotado=" + precoUnitarioCotado +
                ", observacoes='" + observacoes + '\'' +
                ", orcamentoId=" + (orcamento != null ? orcamento.getId() : "null") +
                ", version=" + version +
                '}';
    }
}
package com.zlogcompras.model.dto;

import java.math.BigDecimal;

public class OrcamentoItemDTO {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private String codigoProduto;
    private String unidadeMedidaProduto;
    private BigDecimal precoUnitario;
    private Integer quantidade;

    // Construtor padrão
    public OrcamentoItemDTO() {
    }

    // Construtor com todos os campos (útil para mapeamento e testes)
    public OrcamentoItemDTO(Long id, Long produtoId, String nomeProduto, String codigoProduto, String unidadeMedidaProduto, BigDecimal precoUnitario, Integer quantidade) {
        this.id = id;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.codigoProduto = codigoProduto;
        this.unidadeMedidaProduto = unidadeMedidaProduto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

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

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "OrcamentoItemDTO{" +
                "id=" + id +
                ", produtoId=" + produtoId +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", codigoProduto='" + codigoProduto + '\'' +
                ", unidadeMedidaProduto='" + unidadeMedidaProduto + '\'' +
                ", precoUnitario=" + precoUnitario +
                ", quantidade=" + quantidade +
                '}';
    }
}
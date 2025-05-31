package com.zlogcompras.model.dto;

import java.math.BigDecimal;

public class ItemOrcamentoDTO {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private String codigoProduto; // Adicionado para consistência
    private String unidadeMedidaProduto; // Adicionado para consistência
    private BigDecimal precoUnitario;
    private BigDecimal quantidade; // Alterado para BigDecimal

    public ItemOrcamentoDTO() {}

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public String getCodigoProduto() { return codigoProduto; }
    public void setCodigoProduto(String codigoProduto) { this.codigoProduto = codigoProduto; }

    public String getUnidadeMedidaProduto() { return unidadeMedidaProduto; }
    public void setUnidadeMedidaProduto(String unidadeMedidaProduto) { this.unidadeMedidaProduto = unidadeMedidaProduto; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
}
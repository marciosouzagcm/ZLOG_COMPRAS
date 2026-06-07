package com.zlogcompras.model.dto;

public class ItemOrcamentoListaDTO {
    private Long produtoId;
    private String nomeProduto;
    private String codigoProduto;
    private String unidadeMedidaProduto;

    // Getters e Setters
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
}
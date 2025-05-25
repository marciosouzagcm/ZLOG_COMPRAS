package com.zlogcompras.model.dto;

import java.math.BigDecimal;

public class ItemSolicitacaoCompraResponseDTO {

    private Long id;
    private Long produtoId;
    private String nomeProduto; // Nome do produto para facilitar a exibição
    private String codigoProduto; // Código do produto, se relevante
    private String unidadeMedidaProduto; // Unidade de medida do produto
    private BigDecimal quantidade;
    private String descricaoAdicional;
    private String status;

    // Construtor padrão
    public ItemSolicitacaoCompraResponseDTO() {
    }

    // Construtor com todos os campos
    public ItemSolicitacaoCompraResponseDTO(Long id, Long produtoId, String nomeProduto, String codigoProduto,
                                            String unidadeMedidaProduto, BigDecimal quantidade,
                                            String descricaoAdicional, String status) {
        this.id = id;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.codigoProduto = codigoProduto;
        this.unidadeMedidaProduto = unidadeMedidaProduto;
        this.quantidade = quantidade;
        this.descricaoAdicional = descricaoAdicional;
        this.status = status;
    }

    // --- Getters e Setters ---
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

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricaoAdicional() {
        return descricaoAdicional;
    }

    public void setDescricaoAdicional(String descricaoAdicional) {
        this.descricaoAdicional = descricaoAdicional;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ItemSolicitacaoCompraResponseDTO{" +
               "id=" + id +
               ", produtoId=" + produtoId +
               ", nomeProduto='" + nomeProduto + '\'' +
               ", quantidade=" + quantidade +
               ", status='" + status + '\'' +
               '}';
    }
}
package com.zlogcompras.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// A CORREÇÃO ESTÁ AQUI: a classe foi declarada como 'public'
public class ItemPedidoCompraRequestDTO {

    @NotNull(message = "O ID do produto é obrigatório.")
    @Min(value = 1, message = "O ID do produto deve ser um número positivo.")
    private Long produtoId; // Referência ao ID do Produto

    @NotBlank(message = "O nome do produto é obrigatório.")
    private String nomeProduto;

    @NotBlank(message = "O código do produto é obrigatório.")
    private String codigoProduto;

    @NotBlank(message = "A unidade de medida é obrigatória.")
    private String unidadeMedida;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser de pelo menos 1.")
    private Integer quantidade;

    @NotNull(message = "O preço unitário é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero.")
    private BigDecimal precoUnitario;

    @NotNull(message = "O subtotal é obrigatório.")
    @DecimalMin(value = "0.00", message = "O subtotal não pode ser negativo.")
    private BigDecimal subtotal;

    private String observacoes; // Campo opcional

    // Construtor padrão
    public ItemPedidoCompraRequestDTO() {
    }

    // --- Getters e Setters ---
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

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
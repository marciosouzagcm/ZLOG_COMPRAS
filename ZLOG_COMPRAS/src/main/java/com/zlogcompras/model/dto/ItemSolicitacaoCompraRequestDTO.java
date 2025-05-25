package com.zlogcompras.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ItemSolicitacaoCompraRequestDTO {

    @NotNull(message = "O ID do produto é obrigatório.")
    @Min(value = 1, message = "O ID do produto deve ser um número positivo.")
    private Long produtoId; // Referencia o Produto pelo ID

    @NotNull(message = "A quantidade é obrigatória.")
    @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero.")
    private BigDecimal quantidade;

    private String descricaoAdicional; // Opcional, para adicionar detalhes além da descrição do Produto

    // Construtor padrão
    public ItemSolicitacaoCompraRequestDTO() {
    }

    // Construtor com todos os campos
    public ItemSolicitacaoCompraRequestDTO(Long produtoId, BigDecimal quantidade, String descricaoAdicional) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.descricaoAdicional = descricaoAdicional;
    }

    // --- Getters e Setters ---
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
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

    @Override
    public String toString() {
        return "ItemSolicitacaoCompraRequestDTO{" +
               "produtoId=" + produtoId +
               ", quantidade=" + quantidade +
               ", descricaoAdicional='" + descricaoAdicional + '\'' +
               '}';
    }
}
package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ItemOrcamentoRequestDTO { // Renomeado para ItemOrcamentoRequestDTO

    // private Long id; // ID não é enviado em requisições de criação/atualização de itens aninhados

    @NotNull(message = "O ID do produto é obrigatório.")
    @Positive(message = "O ID do produto deve ser um número positivo.")
    private Long produtoId; // Referencia o Produto pelo ID

    @NotNull(message = "A quantidade é obrigatória.")
    @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero.")
    private BigDecimal quantidade;

    @NotNull(message = "O preço unitário cotado é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço unitário cotado deve ser maior que zero.")
    private BigDecimal precoUnitarioCotado;

    @Size(max = 500, message = "As observações não podem exceder 500 caracteres.")
    private String observacoes; // Opcional

    // Construtor padrão
    public ItemOrcamentoRequestDTO() {
    }

    // Construtor completo
    public ItemOrcamentoRequestDTO(Long produtoId, BigDecimal quantidade, BigDecimal precoUnitarioCotado, String observacoes) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitarioCotado = precoUnitarioCotado;
        this.observacoes = observacoes;
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

    @Override
    public String toString() {
        return "ItemOrcamentoRequestDTO{" +
               "produtoId=" + produtoId +
               ", quantidade=" + quantidade +
               ", precoUnitarioCotado=" + precoUnitarioCotado +
               '}';
    }

    public Object getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
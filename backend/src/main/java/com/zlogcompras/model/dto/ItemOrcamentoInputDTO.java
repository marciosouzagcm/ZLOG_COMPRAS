package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import java.util.Objects;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) para representar os dados de entrada de um item de orçamento.
 * Utilizado para receber informações de itens de orçamento de requisições.
 */
public class ItemOrcamentoInputDTO {

    private Long id; // Adicionado o campo ID para casos de atualização de itens

    @NotNull(message = "O ID do produto é obrigatório.")
    @Positive(message = "O ID do produto deve ser um número positivo.")
    private Long produtoId;

    @NotNull(message = "A quantidade é obrigatória.")
    @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero.")
    private BigDecimal quantidade;

    @NotNull(message = "O preço unitário cotado é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço unitário cotado deve ser maior que zero.")
    private BigDecimal precoUnitarioCotado; // Renomeado para consistência com ItemOrcamentoRequestDTO

    @Size(max = 500, message = "As observações não podem exceder 500 caracteres.")
    private String observacoes;

    /**
     * Construtor padrão.
     */
    public ItemOrcamentoInputDTO() {
    }

    /**
     * Construtor completo.
     * @param id O ID do item de orçamento (nulo para novos itens).
     * @param produtoId O ID do produto.
     * @param quantidade A quantidade do produto.
     * @param precoUnitarioCotado O preço unitário cotado do produto.
     * @param observacoes Observações adicionais sobre o item.
     */
    public ItemOrcamentoInputDTO(Long id, Long produtoId, BigDecimal quantidade, BigDecimal precoUnitarioCotado, String observacoes) {
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitarioCotado = precoUnitarioCotado;
        this.observacoes = observacoes;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemOrcamentoInputDTO that = (ItemOrcamentoInputDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ItemOrcamentoInputDTO{" +
                "id=" + id +
                ", produtoId=" + produtoId +
                ", quantidade=" + quantidade +
                ", precoUnitarioCotado=" + precoUnitarioCotado +
                ", observacoes='" + observacoes + '\'' +
                '}';
    }
}
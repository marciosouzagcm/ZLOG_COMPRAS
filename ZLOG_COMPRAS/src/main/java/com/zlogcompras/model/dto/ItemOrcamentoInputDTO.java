package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO (Data Transfer Object) para representar os dados de entrada de um item de orçamento.
 * Utilizado para receber informações de itens de orçamento de requisições.
 */
public class ItemOrcamentoInputDTO {

    private Long id; // Adicionado o campo ID para casos de atualização de itens
    private Long produtoId;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;
    private String observacoes; // Adicionado para consistência

    /**
     * Construtor padrão.
     */
    public ItemOrcamentoInputDTO() {
    }

    /**
     * Retorna o ID do produto associado a este item de orçamento.
     * @return O ID do produto.
     */
    public Long getProdutoId() {
        return produtoId;
    }

    /**
     * Define o ID do produto para este item de orçamento.
     * @param produtoId O ID do produto a ser definido.
     */
    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    /**
     * Retorna a quantidade do produto neste item de orçamento.
     * @return A quantidade do produto.
     */
    public BigDecimal getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade do produto para este item de orçamento.
     * @param quantidade A quantidade a ser definida.
     */
    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Retorna o preço unitário do produto neste item de orçamento.
     * @return O preço unitário do produto.
     */
    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    /**
     * Define o preço unitário do produto para este item de orçamento.
     * @param precoUnitario O preço unitário a ser definido.
     */
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    // --- Métodos corrigidos ---
    public Long getId() { // Deve retornar Long, não Object
        return id;
    }

    public void setId(Long id) { // Adicionado setter para ID
        this.id = id;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // Você pode adicionar outros métodos, como toString(), equals(), hashCode() se necessário.
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
}
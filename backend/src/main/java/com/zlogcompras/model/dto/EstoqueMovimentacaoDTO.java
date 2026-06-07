package com.zlogcompras.model.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EstoqueMovimentacaoDTO {

    @NotNull(message = "A quantidade é obrigatória para a movimentação.")
    @Positive(message = "A quantidade deve ser um valor positivo.")
    private Integer quantidade;

    public EstoqueMovimentacaoDTO() {}

    public EstoqueMovimentacaoDTO(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
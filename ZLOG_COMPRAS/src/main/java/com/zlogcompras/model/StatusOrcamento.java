package com.zlogcompras.model;

public enum StatusOrcamento {
    ABERTO("Aberto"),
    AGUARDANDO_APROVACAO("Aguardando Aprovação"),
    APROVADO("Aprovado"),
    REJEITADO("Rejeitado"),
    CANCELADO("Cancelado"),
    COTADO("Cotado");

    private final String descricao;

    StatusOrcamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

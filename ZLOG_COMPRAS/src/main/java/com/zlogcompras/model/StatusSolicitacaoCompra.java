package com.zlogcompras.model;

public enum StatusSolicitacaoCompra {
    PENDENTE("Pendente"),
    ORCAMENTO_APROVADO("Orçamento Aprovado"),
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusSolicitacaoCompra(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
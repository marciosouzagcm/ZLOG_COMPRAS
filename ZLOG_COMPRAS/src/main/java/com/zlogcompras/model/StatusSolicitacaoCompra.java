package com.zlogcompras.model;

public enum StatusSolicitacaoCompra {
    ABERTA("Aberta"), // Adicionado para uso nos testes e sistema
    PENDENTE("Pendente"),
    ORCAMENTO_APROVADO("Orçamento Aprovado"),
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada"),
    AGUARDANDO_PARECER("Aguardando Parecer"),
    SUSPENSO("Suspenso"),
    REJEITADO("Rejeitado");

    private final String descricao;

    StatusSolicitacaoCompra(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
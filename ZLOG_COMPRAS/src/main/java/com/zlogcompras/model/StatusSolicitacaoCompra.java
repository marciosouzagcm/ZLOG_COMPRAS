package com.zlogcompras.model;

public enum StatusSolicitacaoCompra {
    // Status existentes no seu enum e padronizados no banco
    PENDENTE("Pendente"),
    ORCAMENTO_APROVADO("Orçamento Aprovado"),
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada"),
    AGUARDANDO_PARECER("Aguardando Parecer"),
    SUSPENSO("Suspenso"), // Padronizado de "Suspenso"
    REJEITADO("Rejeitado"); // Adicione esta linha se "REJEITADO" for um status válido no seu sistema

    private final String descricao;

    StatusSolicitacaoCompra(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
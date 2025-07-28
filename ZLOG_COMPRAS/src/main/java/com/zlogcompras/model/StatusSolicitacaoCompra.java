package com.zlogcompras.model;

public enum StatusSolicitacaoCompra {
    ABERTA("Aberta"), // Provavelmente não está no banco se você não adicionou manualmente.
                      // O DDL do Hibernate com EnumType.STRING salva o nome da constante.
    PENDENTE("Pendente"),
    ORCAMENTO_APROVADO("Orçamento Aprovado"),
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada"),
    AGUARDANDO_PARECER("Aguardando Parecer"),
    SUSPENSO("Suspenso"),
    REJEITADO("Rejeitado"),
    PEDIDO_GERADO("Pedido Gerado"); // Esta é a constante que faltava no ENUM do BD!

    private final String descricao;

    StatusSolicitacaoCompra(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
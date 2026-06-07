package com.zlogcompras.model;

/**
 * Enum que representa os possíveis status de um item dentro de uma SolicitacaoCompra.
 */
public enum StatusItemSolicitacao {
    PENDENTE("Pendente"),
    AGUARDANDO_ORCAMENTO("Aguardando Orçamento"), // <--- ADICIONADO
    ORCAMENTO_RECEBIDO("Orçamento Recebido"),
    APROVADO("Aprovado"),
    REJEITADO("Rejeitado"),
    EM_COMPRA("Em Compra"),
    COMPRADO("Comprado"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusItemSolicitacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusItemSolicitacao fromDescricao(String descricao) {
        for (StatusItemSolicitacao status : StatusItemSolicitacao.values()) {
            if (status.descricao.equalsIgnoreCase(descricao)) {
                return status;
            }
        }
        return null;
    }
}
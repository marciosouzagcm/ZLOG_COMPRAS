package com.zlogcompras.model;

public enum StatusOrcamento {
    ABERTO("Aberto"),
    COTADO("Cotado"),
    AGUARDANDO_APROVACAO("Aguardando Aprovação"), // ou o status inicial que você usa
    APROVADO("Aprovado"),
    RECUSADO("Recusado"),
    CANCELADO("Cancelado"), 
    SELECIONADO("Selecionado"),
    EM_ANDAMENTO("Em Andamento"),
    FINALIZADO("Finalizado"),
    EM_ANALISE("Em Análise"),
    EM_NEGOCIACAO("Em Negociação"),
    EM_TRANSPORTE("Em Transporte"),
    EM_ENTREGA("Em Entrega"),
    EM_FATURAMENTO("Em Faturamento"),
    EM_PAGAMENTO("Em Pagamento"),
    EM_AUDITORIA("Em Auditoria"),
    EM_CONFERENCIA("Em Conferência");

    private final String descricao;

    StatusOrcamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
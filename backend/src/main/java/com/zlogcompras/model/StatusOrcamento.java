package com.zlogcompras.model;

public enum StatusOrcamento {
    PENDENTE("Pendente"),
    ABERTO("Aberto"),
    AGUARDANDO_APROVACAO("Aguardando Aprovação"),
    APROVADO("Aprovado"),
    RECUSADO("Recusado"), // Mantenha RECUSADO se usado em outros lugares
    REJEITADO("Rejeitado"), // ADICIONADO/GARANTIDO para o teste e outros usos
    CANCELADO("Cancelado"),
    COTADO("Cotado"),
    PEDIDO_GERADO("Pedido Gerado"),
    CONCLUIDO("Concluído"); // Garanta que CONCLUIDO também existe

    private final String descricao;

    StatusOrcamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
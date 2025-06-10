package com.zlogcompras.model;

public enum StatusPedidoCompra {
    PENDENTE("Pendente"),
    APROVADO("Aprovado"),
    REJEITADO("Rejeitado"),
    ENVIADO("Enviado"),
    RECEBIDO("Recebido"), // Este é o status final que o serviço estava procurando
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPedidoCompra(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
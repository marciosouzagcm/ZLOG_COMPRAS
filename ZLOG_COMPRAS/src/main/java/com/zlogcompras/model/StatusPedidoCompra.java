package com.zlogcompras.model;

public enum StatusPedidoCompra {
    // As constantes do enum devem ser declaradas aqui.
    // PENDENTE é o valor que você está usando no PedidoCompraService.
    PENDENTE("Pendente"),
    APROVADO("Aprovado"),
    REJEITADO("Rejeitado"),
    ENVIADO("Enviado"),
    RECEBIDO("Recebido"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPedidoCompra(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
package com.zlogcompras.model;

public enum StatusPedidoCompra {
    // Status iniciais/de processamento
    PENDENTE("Pendente"), // <-- CORREÇÃO: Adicionado para corresponder ao valor do banco de dados
    AGUARDANDO_APROVACAO("Aguardando Aprovação"),
    APROVADO("Aprovado"),
    AGUARDANDO_ENVIO("Aguardando Envio"),
    ENVIADO("Enviado"),

    // Status de recebimento
    RECEBIDO_PARCIALMENTE("Recebido Parcialmente"),
    RECEBIDO_TOTALMENTE("Recebido Totalmente"),

    // Status de exceção
    CANCELADO("Cancelado"),
    RECUSADO("Recusado"),
    REJEITADO_PELO_FORNECEDOR("Rejeitado pelo Fornecedor"),

    // Status de conclusão
    CONCLUIDO("Concluído"),
    FINALIZADO("Finalizado");

    private final String descricao;

    StatusPedidoCompra(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
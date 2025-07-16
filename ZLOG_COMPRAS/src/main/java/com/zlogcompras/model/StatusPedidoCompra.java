package com.zlogcompras.model;

public enum StatusPedidoCompra {
    // Status iniciais/de processamento
    AGUARDANDO_APROVACAO("Aguardando Aprovação"), // ADICIONADO para corresponder ao serviço
    AGUARDANDO_ENVIO("Aguardando Envio"),
    ENVIADO("Enviado"),

    // Status de recebimento
    RECEBIDO_PARCIALMENTE("Recebido Parcialmente"),
    RECEBIDO_TOTALMENTE("Recebido Totalmente"),

    // Status de exceção
    CANCELADO("Cancelado"),
    RECUSADO("Recusado"), // ADICIONADO/REATIVADO para corresponder ao serviço (se for o mesmo que
                          // REJEITADO_PELO_FORNECEDOR)
    REJEITADO_PELO_FORNECEDOR("Rejeitado pelo Fornecedor"),

    // Status de conclusão
    CONCLUIDO("Concluído"), // ADICIONADO para corresponder ao serviço
    FINALIZADO("Finalizado");

    private final String descricao;

    StatusPedidoCompra(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
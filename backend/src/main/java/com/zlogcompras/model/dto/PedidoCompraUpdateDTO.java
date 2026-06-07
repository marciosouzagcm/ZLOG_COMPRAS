package com.zlogcompras.model.dto;

import com.zlogcompras.model.StatusPedidoCompra; // Importe se você for usar o enum diretamente

import java.math.BigDecimal;
import java.util.List;

public class PedidoCompraUpdateDTO {

    private Long fornecedorId;
    private String status; // Ou StatusPedidoCompra status; se preferir receber o enum diretamente
    private String observacoes;
    private List<ItemPedidoCompraRequestDTO> itens; // Use o mesmo DTO de requisição de item

    // Getters e Setters
    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<ItemPedidoCompraRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoCompraRequestDTO> itens) {
        this.itens = itens;
    }
}
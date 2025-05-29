package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PedidoCompraRequestDTO {

    @NotNull(message = "O ID do fornecedor é obrigatório.")
    @Min(value = 1, message = "O ID do fornecedor deve ser um número positivo.")
    private Long fornecedorId;

    // A data do pedido pode ser enviada, ou você pode defini-la automaticamente no serviço
    // @NotNull(message = "A data do pedido é obrigatória.")
    private LocalDate dataPedido;

    // O status inicial geralmente é definido no serviço (ex: "Pendente")
    // @NotBlank(message = "O status é obrigatório.")
    private String status;

    @NotNull(message = "O valor total é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor total deve ser maior que zero.")
    private BigDecimal valorTotal;

    @Valid // Garante que as validações dentro de ItemPedidoCompraRequestDTO também sejam aplicadas
    @NotNull(message = "A lista de itens do pedido não pode ser nula.")
    private List<ItemPedidoCompraRequestDTO> itens;

    // Construtor padrão (necessário para serialização/desserialização JSON)
    public PedidoCompraRequestDTO() {
    }

    // --- Getters e Setters ---
    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemPedidoCompraRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoCompraRequestDTO> itens) {
        this.itens = itens;
    }
}
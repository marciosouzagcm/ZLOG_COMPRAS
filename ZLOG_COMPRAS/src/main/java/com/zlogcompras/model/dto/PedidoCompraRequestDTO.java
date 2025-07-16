package com.zlogcompras.model.dto;

import java.math.BigDecimal; 
import java.time.LocalDate; 
import java.util.List;

import jakarta.validation.Valid; 
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive; 
import jakarta.validation.constraints.Size; 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class PedidoCompraRequestDTO {

    @NotNull(message = "O ID do fornecedor é obrigatório.")
    @Positive(message = "O ID do fornecedor deve ser um número positivo.")
    private Long fornecedorId;

    // Adicionado o campo observacoes, que estava faltando.
    private String observacoes; 

    @Valid 
    @NotNull(message = "A lista de itens do pedido não pode ser nula.")
    @Size(min = 1, message = "O pedido deve conter ao menos um item.") 
    private List<ItemPedidoCompraRequestDTO> itens;
}
package com.zlogcompras.model.dto;

import java.math.BigDecimal; // Importado apenas para demonstrar que seria removido o valorTotal
import java.time.LocalDate; // Importado apenas para demonstrar que seria removido a dataPedido
import java.util.List;

import jakarta.validation.Valid; // Essencial para validar os itens da lista
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive; // Mais específico que @Min(1) para IDs
import jakarta.validation.constraints.Size; // Para validar o tamanho da lista de itens

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Gera Getters, Setters, toString(), equals() e hashCode() automaticamente
@NoArgsConstructor // Gera o construtor padrão sem argumentos, necessário para desserialização JSON
@AllArgsConstructor // Opcional: Gera um construtor com todos os argumentos (útil para builders ou testes)
public class PedidoCompraRequestDTO {

    @NotNull(message = "O ID do fornecedor é obrigatório.")
    @Positive(message = "O ID do fornecedor deve ser um número positivo.")
    private Long fornecedorId;

    // Campos REMOVIDOS do DTO de REQUEST, pois devem ser calculados/preenchidos pelo serviço:
    // private LocalDate dataPedido; // Definida automaticamente no serviço (LocalDate.now())
    // private String status;       // Definido automaticamente no serviço (StatusPedidoCompra.PENDENTE)
    // private BigDecimal valorTotal; // Calculado no serviço com base nos itens

    @Valid // Garante que as validações dentro de ItemPedidoCompraRequestDTO também sejam aplicadas
    @NotNull(message = "A lista de itens do pedido não pode ser nula.")
    @Size(min = 1, message = "O pedido deve conter ao menos um item.") // Garante que a lista não é vazia
    private List<ItemPedidoCompraRequestDTO> itens;
}
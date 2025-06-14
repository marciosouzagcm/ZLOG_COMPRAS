package com.zlogcompras.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive; // Mais específico que @Min(1) para IDs
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Gera Getters, Setters, toString(), equals() e hashCode() automaticamente
@NoArgsConstructor // Gera o construtor padrão sem argumentos, necessário para desserialização JSON
@AllArgsConstructor // Opcional: Gera um construtor com todos os argumentos (útil para builders ou testes)
public class ItemPedidoCompraRequestDTO {

    @NotNull(message = "O ID do produto é obrigatório.")
    @Positive(message = "O ID do produto deve ser um número positivo.") // Melhor que @Min(1) para IDs
    private Long produtoId; // Referência ao ID do Produto

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser de pelo menos 1.") // Assumindo quantidade inteira
    private Integer quantidade;

    @NotNull(message = "O preço unitário é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero.")
    private BigDecimal precoUnitario;

    @Size(max = 500, message = "As observações não podem exceder 500 caracteres.")
    private String observacoes; // Campo opcional

    // Campos REMOVIDOS do DTO de REQUEST, pois devem ser calculados/preenchidos pelo serviço:
    // private BigDecimal subtotal;
    // private String nomeProduto;
    // private String codigoProduto;
    // private String unidadeMedida;
}
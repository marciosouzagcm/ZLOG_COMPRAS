package com.zlogcompras.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor; // Opcional: para construtor com todos os argumentos
import lombok.Data; // Inclui @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
import lombok.NoArgsConstructor; // Para construtor sem argumentos

@Data // Gera Getters, Setters, toString(), equals() e hashCode() automaticamente
@NoArgsConstructor // Gera o construtor padrão sem argumentos, necessário para desserialização JSON
@AllArgsConstructor // Opcional: Gera um construtor com todos os argumentos (útil para builders ou testes)
public class ItemPedidoCompraRequestDTO {

    @NotNull(message = "O ID do produto é obrigatório.")
    @Min(value = 1, message = "O ID do produto deve ser um número positivo.")
    private Long produtoId; // Referência ao ID do Produto

    @NotBlank(message = "O nome do produto é obrigatório.")
    private String nomeProduto;

    @NotBlank(message = "O código do produto é obrigatório.")
    private String codigoProduto;

    @NotBlank(message = "A unidade de medida é obrigatória.")
    private String unidadeMedida;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser de pelo menos 1.")
    private Integer quantidade;

    @NotNull(message = "O preço unitário é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero.")
    private BigDecimal precoUnitario;

    @NotNull(message = "O subtotal é obrigatório.")
    @DecimalMin(value = "0.00", message = "O subtotal não pode ser negativo.")
    private BigDecimal subtotal;

    private String observacoes; // Campo opcional

    // Os construtores padrão, getters e setters são gerados automaticamente pelo Lombok (@NoArgsConstructor e @Data).
    // Não há necessidade de declará-los explicitamente.
}
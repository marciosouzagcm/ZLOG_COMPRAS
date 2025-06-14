package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor // Gera construtor com todos os argumentos
@Builder // Permite construir objetos de forma fluente (ex: ItemOrcamentoRequestDTO.builder()...build())
public class ItemOrcamentoRequestDTO {

    private Long id; // Adicionar o ID para permitir atualizações de itens existentes

    @NotNull(message = "O ID do produto é obrigatório.")
    @Positive(message = "O ID do produto deve ser um número positivo.")
    private Long produtoId;

    @NotNull(message = "A quantidade é obrigatória.")
    @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero.")
    private BigDecimal quantidade;

    @NotNull(message = "O preço unitário cotado é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço unitário cotado deve ser maior que zero.")
    private BigDecimal precoUnitarioCotado;

    @Size(max = 500, message = "As observações não podem exceder 500 caracteres.")
    private String observacoes;

    // Campos como nomeProduto, codigoProduto, unidadeMedidaProduto não devem estar aqui
    // pois são derivados do produtoId e devem ser populados pelo serviço.
}
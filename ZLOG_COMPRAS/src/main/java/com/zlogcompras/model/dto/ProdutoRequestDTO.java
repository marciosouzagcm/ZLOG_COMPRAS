package com.zlogcompras.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero; // Importar PositiveOrZero

public class ProdutoRequestDTO {
    @NotBlank(message = "O código do produto é obrigatório")
    private String codigoProduto;
    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;
    private String descricao;
    @NotBlank(message = "A unidade de medida é obrigatória")
    private String unidadeMedida;
    @NotNull(message = "O preço unitário é obrigatório")
    @PositiveOrZero(message = "O preço unitário deve ser um valor positivo ou zero")
    private BigDecimal precoUnitario;
    @NotBlank(message = "A categoria do produto é obrigatória")
    private String categoria;
    @NotNull(message = "O estoque é obrigatório") // <--- ADICIONE ESTA LINHA
    @PositiveOrZero(message = "O estoque deve ser um valor positivo ou zero") // <--- ADICIONE ESTA LINHA
    private Integer estoque; // <--- ADICIONE ESTA LINHA

    // Construtor padrão
    public ProdutoRequestDTO() {
    }

    // Getters e Setters
    public String getCodigoProduto() { return codigoProduto; }
    public void setCodigoProduto(String codigoProduto) { this.codigoProduto = codigoProduto; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Integer getEstoque() { return estoque; } // <--- NOVO GETTER
    public void setEstoque(Integer estoque) { this.estoque = estoque; } // <--- NOVO SETTER
}
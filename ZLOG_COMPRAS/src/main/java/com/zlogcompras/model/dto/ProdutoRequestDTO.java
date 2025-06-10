package com.zlogcompras.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ProdutoRequestDTO {
    // Campo 'codigo' (para o código interno)
    @NotBlank(message = "O código é obrigatório") // Apenas 'código' como identificador primário, se for o caso
    private String codigo;

    // Campo 'codigoProduto' (se for um código de produto externo ou secundário)
    // Se o banco de dados e a entidade 'Produto' também têm 'codigo_produto',
    // então este campo deve existir e ser preenchido.
    @NotBlank(message = "O código do produto é obrigatório")
    private String codigoProduto; // <-- Adicione este campo se você quer mapear o 'codigoProduto' do JSON

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
    @NotNull(message = "O estoque é obrigatório")
    @PositiveOrZero(message = "O estoque deve ser um valor positivo ou zero")
    private Integer estoque;

    // Construtor padrão
    public ProdutoRequestDTO() {
    }

    // --- Getters e Setters CORRIGIDOS ---

    // Getters e Setters para o campo 'codigo' (interno)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    // Getters e Setters para o campo 'codigoProduto' (do JSON)
    public String getCodigoProduto() { // <-- Nome do getter agora reflete 'codigoProduto'
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) { // <-- Nome do setter agora reflete 'codigoProduto'
        this.codigoProduto = codigoProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getgetDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }
}
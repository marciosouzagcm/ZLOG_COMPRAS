package com.zlogcompras.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class ProdutoRequestDTO {
    // Campo 'codigo' (para o código interno do sistema, por exemplo)
    @NotBlank(message = "O código é obrigatório")
    @Size(max = 50, message = "O código não pode exceder 50 caracteres")
    private String codigo;

    // Campo 'codigoProduto' (se for um código de produto externo ou secundário,
    // vindo do JSON. Se você tem apenas 'codigo' no seu Produto e está usando
    // 'codigoProduto' no ItemOrcamento para salvar o código do produto no momento
    // da cotação, pode ser que este campo não seja necessário aqui ou tenha
    // um propósito diferente).
    // Se o propósito é receber um segundo código para o produto, mantenha.
    // Se for o mesmo 'codigo', considere remover ou renomear um dos dois.
    @NotBlank(message = "O código do produto (externo) é obrigatório") // Mensagem mais específica
    @Size(max = 50, message = "O código do produto (externo) não pode exceder 50 caracteres")
    private String codigoProduto;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(max = 255, message = "O nome do produto não pode exceder 255 caracteres")
    private String nome;

    @Size(max = 500, message = "A descrição do produto não pode exceder 500 caracteres") // Adicionado @Size
    private String descricao;

    @NotBlank(message = "A unidade de medida é obrigatória")
    @Size(max = 20, message = "A unidade de medida não pode exceder 20 caracteres") // Adicionado @Size
    private String unidadeMedida;

    @NotNull(message = "O preço unitário é obrigatório")
    @PositiveOrZero(message = "O preço unitário deve ser um valor positivo ou zero")
    private BigDecimal precoUnitario;

    @NotBlank(message = "A categoria do produto é obrigatória")
    @Size(max = 100, message = "A categoria do produto não pode exceder 100 caracteres") // Adicionado @Size
    private String categoria;

    @NotNull(message = "O estoque é obrigatório")
    @PositiveOrZero(message = "O estoque deve ser um valor positivo ou zero")
    private Integer estoque;

    // Construtor padrão
    public ProdutoRequestDTO() {
    }

    // Construtor completo (opcional, mas útil)
    public ProdutoRequestDTO(String codigo, String codigoProduto, String nome, String descricao,
            String unidadeMedida, BigDecimal precoUnitario, String categoria, Integer estoque) {
        this.codigo = codigo;
        this.codigoProduto = codigoProduto;
        this.nome = nome;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
        this.precoUnitario = precoUnitario;
        this.categoria = categoria;
        this.estoque = estoque;
    }

    // --- Getters e Setters CORRIGIDOS ---

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // CORRIGIDO: O getter estava com nome errado 'getgetDescricao'
    public String getDescricao() {
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
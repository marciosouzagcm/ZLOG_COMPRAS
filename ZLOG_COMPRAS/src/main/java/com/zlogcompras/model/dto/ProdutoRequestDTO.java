package com.zlogcompras.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal; // Importe BigDecimal

public class ProdutoRequestDTO {

    // ID geralmente não é enviado na criação. Em atualizações (PUT), pode vir no corpo,
    // mas o @PathVariable é a forma mais comum de passar o ID para PUT.
    // Pode ser incluído se o cenário de uso exigir que o ID venha no corpo para PUT/PATCH.
    private Long id;

    @NotBlank(message = "O código do produto é obrigatório.")
    @Size(max = 50, message = "O código deve ter no máximo 50 caracteres.")
    private String codigo;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    private String descricao; // Opcional, não precisa de @NotBlank

    @NotBlank(message = "A unidade de medida é obrigatória.")
    @Size(max = 20, message = "A unidade de medida deve ter no máximo 20 caracteres.")
    private String unidadeMedida;

    @NotNull(message = "O preço unitário é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero.")
    private BigDecimal precoUnitario;

    // Construtor padrão
    public ProdutoRequestDTO() {
    }

    // Construtor com todos os campos (útil para testes)
    public ProdutoRequestDTO(Long id, String codigo, String nome, String descricao, String unidadeMedida, BigDecimal precoUnitario) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
        this.precoUnitario = precoUnitario;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome; // CORRIGIDO: Atribuição correta do valor
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) { // CORRIGIDO: Nome do método e tipo de retorno
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    @Override
    public String toString() {
        return "ProdutoRequestDTO{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", unidadeMedida='" + unidadeMedida + '\'' +
                ", precoUnitario=" + precoUnitario +
                '}';
    }
}
package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ProdutoResponseDTO { // Renomeado para ProdutoResponseDTO

    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private String unidadeMedida;
    private BigDecimal precoUnitario;
    private Long version; // Adicionado para consistência, se sua entidade Produto tiver um campo 'version'

    // Construtor padrão
    public ProdutoResponseDTO() {
    }

    // Construtor com todos os campos
    public ProdutoResponseDTO(Long id, String codigo, String nome, String descricao,
                              String unidadeMedida, BigDecimal precoUnitario, Long version) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
        this.precoUnitario = precoUnitario;
        this.version = version;
    }

    // Getters e Setters
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
        this.nome = nome;
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

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoResponseDTO that = (ProdutoResponseDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProdutoResponseDTO{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", unidadeMedida='" + unidadeMedida + '\'' +
                ", precoUnitario=" + precoUnitario +
                '}';
    }
}
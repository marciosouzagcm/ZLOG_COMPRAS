package com.zlogcompras.model.dto;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) para representar os dados de um produto.
 * Usado para transferir dados de produtos entre as camadas da aplicação (e.g., para a API).
 */
public class ProdutoDTO {
    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private String unidadeMedida;
    private BigDecimal precoUnitario;
    private Long version; // Para controle de concorrência otimista, se aplicável

    // Construtor padrão
    public ProdutoDTO() {
    }

    // Construtor completo (opcional, mas útil)
    public ProdutoDTO(Long id, String codigo, String nome, String descricao, String unidadeMedida, BigDecimal precoUnitario, Long version) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
        this.precoUnitario = precoUnitario;
        this.version = version;
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
}  

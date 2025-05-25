package com.zlogcompras.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo; // Código do produto (ex: SKU)

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private String unidadeMedida;

    @Column(nullable = false, precision = 10, scale = 2) // Precisão para valores monetários
    private BigDecimal precoUnitario; // Alterado para BigDecimal

    @Version
    private Long version;

    // Construtor padrão
    public Produto() {
    }

    // Construtor com campos
    public Produto(String codigo, String nome, String descricao, String unidadeMedida, BigDecimal precoUnitario) {
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

    // --- Métodos equals e hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- Método toString ---
    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", unidadeMedida='" + unidadeMedida + '\'' +
                ", precoUnitario=" + precoUnitario +
                ", version=" + version +
                '}';
    }
}
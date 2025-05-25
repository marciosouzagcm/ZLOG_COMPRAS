package com.zlogcompras.model;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; // Importar BigDecimal
import jakarta.persistence.Version;

@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoMaterial; // Código do material em estoque

    @Column(nullable = false, precision = 10, scale = 2) // Corrigido para 2 casas decimais
    private BigDecimal quantidade; // Alterado para BigDecimal

    @Version
    private Long version;

    // Construtor padrão
    public Estoque() {
    }

    // Construtor com campos
    public Estoque(String codigoMaterial, BigDecimal quantidade) {
        this.codigoMaterial = codigoMaterial;
        this.quantidade = quantidade;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoMaterial() {
        return codigoMaterial;
    }

    public void setCodigoMaterial(String codigoMaterial) {
        this.codigoMaterial = codigoMaterial;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estoque estoque = (Estoque) o;
        return Objects.equals(id, estoque.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- Método toString ---
    @Override
    public String toString() {
        return "Estoque{" +
                "id=" + id +
                ", codigoMaterial='" + codigoMaterial + '\'' +
                ", quantidade=" + quantidade +
                ", version=" + version +
                '}';
    }
}
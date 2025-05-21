package com.zlogcompras.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "produtos") // Nome da tabela no banco de dados
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Material/Serviço é único
    private String nome; // Ex: "Caneta Esferográfica", "Serviço de Limpeza"

    @Column(nullable = true) // Descrição pode ser opcional dependendo da sua necessidade
    private String descricao; // Ex: "Caneta BIC Azul", "Limpeza diária de escritórios"

    @Column(nullable = false)
    private String unidadeMedida; // Ex: "unidade", "caixa", "hora", "serviço"

    @Column(nullable = false)
    private Double precoUnitario; // Adicionando preço para um produto

    @Version // Para controle de concorrência otimista (evitar sobrescrever alterações)
    private Long version;

    // Construtor padrão (necessário para JPA)
    public Produto() {
    }

    // Construtor com campos para facilitar a criação (opcional, mas útil)
    public Produto(String nome, String descricao, String unidadeMedida, Double precoUnitario) {
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

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    // --- Métodos equals e hashCode (Importante para coleções e comparação) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id); // Comparar produtos pelo ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash baseado no ID
    }

    @Override
    public String toString() {
        return "Produto{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", descricao='" + descricao + '\'' +
               ", unidadeMedida='" + unidadeMedida + '\'' +
               ", precoUnitario=" + precoUnitario +
               ", version=" + version +
               '}';
    }
}
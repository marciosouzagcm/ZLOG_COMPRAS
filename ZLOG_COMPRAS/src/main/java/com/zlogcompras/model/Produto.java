package com.zlogcompras.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDate;
import java.util.Objects;
import java.math.BigDecimal; // Importe BigDecimal

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String codigoProduto; // Campo correto

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false, length = 20)
    private String unidadeMedida; // Adicionado

    @Column(nullable = false, precision = 10, scale = 2) // Usar BigDecimal para precisão monetária
    private BigDecimal precoUnitario; // Alterado para BigDecimal

    @Column(nullable = false, updatable = false)
    private LocalDate dataCriacao; // Adicionado

    @Column(nullable = false)
    private LocalDate dataAtualizacao; // Adicionado

    @Version
    private Long version; // Adicionado

    public Produto() {
    }

    public Produto(String codigoProduto, String nome, String descricao,
                   String unidadeMedida, BigDecimal precoUnitario) { // Ajuste no construtor
        this.codigoProduto = codigoProduto;
        this.nome = nome;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
        this.precoUnitario = precoUnitario;
    }

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDate.now();
        this.dataAtualizacao = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDate.now();
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getPrecoUnitario() { // Retorno BigDecimal
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) { // Parâmetro BigDecimal
        this.precoUnitario = precoUnitario;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    protected void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataAtualizacao() {
        return dataAtualizacao;
    }

    protected void setDataAtualizacao(LocalDate dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
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
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Produto{" +
               "id=" + id +
               ", codigoProduto='" + codigoProduto + '\'' +
               ", nome='" + nome + '\'' +
               ", descricao='" + descricao + '\'' +
               ", unidadeMedida='" + unidadeMedida + '\'' +
               ", precoUnitario=" + precoUnitario +
               ", dataCriacao=" + dataCriacao +
               ", dataAtualizacao=" + dataAtualizacao +
               ", version=" + version +
               '}';
    }
}
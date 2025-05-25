package com.zlogcompras.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; // Importar BigDecimal
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "movimentacoes_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigoMaterial;

    @Column(nullable = false, precision = 10, scale = 3) // Precisão para quantidades
    private BigDecimal quantidade; // Alterado para BigDecimal

    @Column(nullable = false)
    private String tipoMovimentacao; // Ex: "ENTRADA", "SAIDA"

    @Column(nullable = false)
    private LocalDateTime dataMovimentacao;

    private String referencia; // Ex: Número da OC, NF, Solicitação

    @Version
    private Long version;

    // Construtor padrão
    public MovimentacaoEstoque() {
    }

    // Construtor com campos
    public MovimentacaoEstoque(String codigoMaterial, BigDecimal quantidade, String tipoMovimentacao, LocalDateTime dataMovimentacao, String referencia) {
        this.codigoMaterial = codigoMaterial;
        this.quantidade = quantidade;
        this.tipoMovimentacao = tipoMovimentacao;
        this.dataMovimentacao = dataMovimentacao;
        this.referencia = referencia;
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

    public String getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
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
        MovimentacaoEstoque that = (MovimentacaoEstoque) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- Método toString ---
    @Override
    public String toString() {
        return "MovimentacaoEstoque{" +
               "id=" + id +
               ", codigoMaterial='" + codigoMaterial + '\'' +
               ", quantidade=" + quantidade +
               ", tipoMovimentacao='" + tipoMovimentacao + '\'' +
               ", dataMovimentacao=" + dataMovimentacao +
               ", referencia='" + referencia + '\'' +
               ", version=" + version +
               '}';
    }
}

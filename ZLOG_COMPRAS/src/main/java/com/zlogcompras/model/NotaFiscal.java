package com.zlogcompras.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "notas_fiscais")
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroNotaFiscal;

    @Column(nullable = false)
    private LocalDate dataEmissao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal; // Alterado para BigDecimal

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_compra_id", nullable = false)
    private PedidoCompra pedidoCompra; // Relaciona a nota fiscal a um pedido de compra

    @Version
    private Long version;

    // Construtor padrão
    public NotaFiscal() {
    }

    // Construtor com campos
    public NotaFiscal(String numeroNotaFiscal, LocalDate dataEmissao, BigDecimal valorTotal, PedidoCompra pedidoCompra) {
        this.numeroNotaFiscal = numeroNotaFiscal;
        this.dataEmissao = dataEmissao;
        this.valorTotal = valorTotal;
        this.pedidoCompra = pedidoCompra;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroNotaFiscal() {
        return numeroNotaFiscal;
    }

    public void setNumeroNotaFiscal(String numeroNotaFiscal) {
        this.numeroNotaFiscal = numeroNotaFiscal;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public PedidoCompra getPedidoCompra() {
        return pedidoCompra;
    }

    public void setPedidoCompra(PedidoCompra pedidoCompra) {
        this.pedidoCompra = pedidoCompra;
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
        NotaFiscal notaFiscal = (NotaFiscal) o;
        return Objects.equals(id, notaFiscal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- Método toString ---
    @Override
    public String toString() {
        return "NotaFiscal{" +
               "id=" + id +
               ", numeroNotaFiscal='" + numeroNotaFiscal + '\'' +
               ", dataEmissao=" + dataEmissao +
               ", valorTotal=" + valorTotal +
               ", pedidoCompraId=" + (pedidoCompra != null ? pedidoCompra.getId() : "null") +
               ", version=" + version +
               '}';
    }
}

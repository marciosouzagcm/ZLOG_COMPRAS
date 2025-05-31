package com.zlogcompras.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "itens_pedido_compra")
public class ItemPedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_compra_id", nullable = false)
    @JsonBackReference // Lado "filho" do relacionamento bidirecional
    private PedidoCompra pedidoCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private BigDecimal quantidade;

    @Column(name = "preco_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "observacoes", length = 500) // Adicione este campo
    private String observacoes;

    @Version
    private Long version;

    public ItemPedidoCompra() {
    }

    public ItemPedidoCompra(PedidoCompra pedidoCompra, Produto produto, BigDecimal quantidade, BigDecimal precoUnitario, String observacoes) {
        this.pedidoCompra = pedidoCompra;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.observacoes = observacoes;
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PedidoCompra getPedidoCompra() { return pedidoCompra; }
    public void setPedidoCompra(PedidoCompra pedidoCompra) { this.pedidoCompra = pedidoCompra; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public String getObservacoes() { return observacoes; } // Adicione este getter
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; } // Adicione este setter

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedidoCompra that = (ItemPedidoCompra) o;
        return Objects.equals(id, that.id); // Comparar por ID é crucial para sets/listas
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
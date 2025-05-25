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
import jakarta.persistence.ManyToOne; // Para evitar recursão JSON
import jakarta.persistence.Table; // Importar BigDecimal
import jakarta.persistence.Version;

@Entity
@Table(name = "itens_pedido_compra")
public class ItemPedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento ManyToOne com Produto
    // Um ItemPedidoCompra refere-se a um Produto específico.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false) // Chave estrangeira para a tabela de produtos
    private Produto produto; // Referência à entidade Produto

    @Column(nullable = false, precision = 10, scale = 3) // Precisão para quantidades
    private BigDecimal quantidade; // Alterado para BigDecimal

    @Column(nullable = false, precision = 12, scale = 2) // Precisão para valores monetários
    private BigDecimal precoUnitario; // Alterado para BigDecimal

    @JsonBackReference // Usado para evitar loop infinito na serialização JSON
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_compra_id", nullable = false)
    private PedidoCompra pedidoCompra; // Referência ao pedido pai

    @Version
    private Long version;

    // Construtor padrão
    public ItemPedidoCompra() {
    }

    // Construtor com campos
    public ItemPedidoCompra(Produto produto, BigDecimal quantidade, BigDecimal precoUnitario, PedidoCompra pedidoCompra) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.pedidoCompra = pedidoCompra;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
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
        ItemPedidoCompra that = (ItemPedidoCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- Método toString ---
    @Override
    public String toString() {
        return "ItemPedidoCompra{" +
               "id=" + id +
               ", produtoId=" + (produto != null ? produto.getId() : "null") +
               ", quantidade=" + quantidade +
               ", precoUnitario=" + precoUnitario +
               ", pedidoCompraId=" + (pedidoCompra != null ? pedidoCompra.getId() : "null") +
               ", version=" + version +
               '}';
    }
}
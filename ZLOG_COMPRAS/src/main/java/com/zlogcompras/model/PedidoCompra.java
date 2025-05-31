package com.zlogcompras.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference; // Adicionado para evitar recursão JSON no OneToOne

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne; // Adicionado para o relacionamento com Orcamento
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "pedidos_compra")
public class PedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    // Adicionado relacionamento bidirecional com Orcamento
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orcamento_id", unique = true) // unique = true garante que um orçamento só gere um pedido
    @JsonBackReference
    private Orcamento orcamento;

    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @Column(nullable = false)
    private String status;

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "observacoes", length = 1000) // Adicione este campo se precisar de observações
    private String observacoes;

    @JsonManagedReference
    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedidoCompra> itens = new ArrayList<>();

    @Version
    private Long version;

    public PedidoCompra() {
        this.dataPedido = LocalDate.now();
        this.status = "Pendente";
    }

    public PedidoCompra(Fornecedor fornecedor, LocalDate dataPedido, String status, BigDecimal valorTotal, Orcamento orcamento, String observacoes) {
        this.fornecedor = fornecedor;
        this.dataPedido = dataPedido;
        this.status = status;
        this.valorTotal = valorTotal;
        this.orcamento = orcamento;
        this.observacoes = observacoes;
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public Orcamento getOrcamento() { return orcamento; }
    public void setOrcamento(Orcamento orcamento) { this.orcamento = orcamento; }

    public LocalDate getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDate dataPedido) { this.dataPedido = dataPedido; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public List<ItemPedidoCompra> getItens() { return itens; }
    public void setItens(List<ItemPedidoCompra> itens) {
        this.itens.clear();
        if (itens != null) {
            for (ItemPedidoCompra item : itens) {
                addItem(item);
            }
        }
    }

    public void addItem(ItemPedidoCompra item) {
        if (item != null && !this.itens.contains(item)) {
            this.itens.add(item);
            item.setPedidoCompra(this);
        }
    }

    public void removeItem(ItemPedidoCompra item) {
        if (item != null && this.itens.contains(item)) {
            this.itens.remove(item);
            item.setPedidoCompra(null);
        }
    }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoCompra that = (PedidoCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PedidoCompra{" +
                "id=" + id +
                ", fornecedorId=" + (fornecedor != null ? fornecedor.getId() : "null") +
                ", dataPedido=" + dataPedido +
                ", status='" + status + '\'' +
                ", valorTotal=" + valorTotal +
                ", itensCount=" + itens.size() +
                ", version=" + version +
                '}';
    }
}
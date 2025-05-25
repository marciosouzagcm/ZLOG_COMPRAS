package com.zlogcompras.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; // Para evitar recursão JSON
import jakarta.persistence.JoinColumn; // Importar BigDecimal
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany; // Usar ArrayList para List
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

    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @Column(nullable = false)
    private String status; // Ex: "Pendente", "Enviado", "Recebido", "Cancelado"

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2) // Precisão para valores monetários
    private BigDecimal valorTotal; // Alterado para BigDecimal

    @JsonManagedReference // Lado "pai" do relacionamento bidirecional
    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoCompra> itens = new ArrayList<>(); // Usar List para itens

    @Version
    private Long version;

    // Construtor padrão
    public PedidoCompra() {
        this.dataPedido = LocalDate.now();
        this.status = "Pendente";
    }

    // Construtor com campos
    public PedidoCompra(Fornecedor fornecedor, LocalDate dataPedido, String status, BigDecimal valorTotal) {
        this.fornecedor = fornecedor;
        this.dataPedido = dataPedido;
        this.status = status;
        this.valorTotal = valorTotal;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemPedidoCompra> getItens() {
        return itens;
    }

    // Setter para a coleção de itens, gerenciando a bidirecionalidade
    public void setItens(List<ItemPedidoCompra> itens) {
        this.itens.clear(); // Limpa os itens existentes
        if (itens != null) {
            for (ItemPedidoCompra item : itens) {
                addItem(item); // Adiciona cada novo item, garantindo a bidirecionalidade
            }
        }
    }

    // Método auxiliar para adicionar item (garante bidirecionalidade)
    public void addItem(ItemPedidoCompra item) {
        if (item != null && !this.itens.contains(item)) {
            this.itens.add(item);
            item.setPedidoCompra(this); // Garante a bidirecionalidade
        }
    }

    // Método auxiliar para remover item (garante bidirecionalidade e orphanRemoval)
    public void removeItem(ItemPedidoCompra item) {
        if (item != null && this.itens.contains(item)) {
            this.itens.remove(item);
            item.setPedidoCompra(null); // Crucial para orphanRemoval
        }
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
        PedidoCompra that = (PedidoCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- Método toString ---
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

    public void setNumeroNotaFiscal(String numeroNotaFiscal) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDataNotaFiscal(LocalDate dataNotaFiscal) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setValorNotaFiscal(BigDecimal valorNota) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

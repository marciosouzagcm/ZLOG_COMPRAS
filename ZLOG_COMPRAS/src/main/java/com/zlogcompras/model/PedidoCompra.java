package com.zlogcompras.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal; // Importação adicionada
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pedidos_compra")
public class PedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @OneToOne
    @JoinColumn(name = "orcamento_id") // Relacionamento com Orçamento (se um pedido é gerado de um orçamento)
    private Orcamento orcamento;

    private LocalDate dataPedido;
    private BigDecimal valorTotal; // Alterado de Double para BigDecimal
    private String status; // Usar String para o nome do Enum StatusPedidoCompra
    private String observacoes;

    // Relacionamento OneToMany com ItemPedidoCompra
    // orphanRemoval = true: se um item for removido da coleção 'itens', ele será deletado do banco de dados.
    // cascade = CascadeType.ALL: todas as operações (PERSIST, MERGE, REMOVE, REFRESH, DETACH) são propagadas para os itens.
    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoCompra> itens = new ArrayList<>(); // Inicialize para evitar NullPointerException

    // Construtor padrão (necessário para JPA)
    public PedidoCompra() {
    }

    // Construtor com campos (opcional, para conveniência)
    public PedidoCompra(Fornecedor fornecedor, Orcamento orcamento, LocalDate dataPedido, BigDecimal valorTotal, String status, String observacoes) {
        this.fornecedor = fornecedor;
        this.orcamento = orcamento;
        this.dataPedido = dataPedido;
        this.valorTotal = valorTotal;
        this.status = status;
        this.observacoes = observacoes;
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

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public BigDecimal getValorTotal() { // Alterado o tipo de retorno
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) { // Alterado o tipo do parâmetro
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // --- Métodos para gerenciar a lista de itens e a bidirecionalidade ---

    public List<ItemPedidoCompra> getItens() {
        return itens;
    }

    /**
     * Define a lista de itens para o pedido de compra, garantindo a bidirecionalidade
     * e o funcionamento correto do cascade e orphanRemoval.
     * @param itens A nova lista de itens.
     */
    public void setItens(List<ItemPedidoCompra> itens) {
        this.itens.clear(); // Remove todos os itens existentes (e os órfãos serão excluídos pelo JPA)
        if (itens != null) {
            itens.forEach(this::addItem); // Adiciona cada item novo usando o método addItem
        }
    }

    /**
     * Adiciona um ItemPedidoCompra à coleção, configurando a relação bidirecional.
     * @param item O item a ser adicionado.
     */
    public void addItem(ItemPedidoCompra item) {
        this.itens.add(item);
        item.setPedidoCompra(this); // Garante a referência de volta ao PedidoCompra
    }

    /**
     * Remove um ItemPedidoCompra da coleção, desvinculando a relação bidirecional.
     * @param item O item a ser removido.
     */
    public void removeItem(ItemPedidoCompra item) {
        this.itens.remove(item);
        item.setPedidoCompra(null); // Desvincula explicitamente
    }

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
               ", dataPedido=" + dataPedido +
               ", valorTotal=" + valorTotal +
               ", status='" + status + '\'' +
               '}';
    }
}
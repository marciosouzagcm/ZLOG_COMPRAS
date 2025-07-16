package com.zlogcompras.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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

    @Column(name = "data_pedido") // Sugestão: explicitando o nome da coluna no banco
    private LocalDate dataPedido;

    @Column(name = "valor_total") // Sugestão: explicitando o nome da coluna no banco
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING) // Esta linha é crucial para persistir o enum como String
    @Column(name = "status") // É uma boa prática explicitar o nome da coluna no banco
    private StatusPedidoCompra status; // O tipo agora é o Enum

    private String observacoes;

    // Relacionamento OneToMany com ItemPedidoCompra
    // mappedBy aponta para o campo 'pedidoCompra' na classe ItemPedidoCompra
    // cascade = CascadeType.ALL: Operações como persist, merge, remove no PedidoCompra
    //                          serão propagadas para os ItemPedidoCompra associados.
    // orphanRemoval = true: Se um ItemPedidoCompra for removido da lista 'itens',
    //                       ele será excluído do banco de dados automaticamente.
    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoCompra> itens = new ArrayList<>();

    // Construtor padrão (necessário para JPA e frameworks como Spring)
    public PedidoCompra() {
    }

    // Construtor com campos (opcional, para conveniência, pode ser útil em testes)
    public PedidoCompra(Fornecedor fornecedor, Orcamento orcamento, LocalDate dataPedido, BigDecimal valorTotal,
                        StatusPedidoCompra status, String observacoes) {
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

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StatusPedidoCompra getStatus() {
        return status;
    }

    public void setStatus(StatusPedidoCompra status) {
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
     * Define a lista de itens para o pedido de compra, garantindo a
     * bidirecionalidade e o funcionamento correto do cascade e orphanRemoval.
     * <p>
     * Este método limpa a lista existente e adiciona os novos itens.
     * Para cada item, ele configura o relacionamento bidirecional.
     *
     * @param itens A nova lista de itens.
     */
    public void setItens(List<ItemPedidoCompra> itens) {
        this.itens.clear(); // Limpa a lista existente (ativa orphanRemoval)
        if (itens != null) {
            itens.forEach(this::addItem); // Adiciona os novos itens, garantindo a bidirecionalidade
        }
    }

    /**
     * Adiciona um ItemPedidoCompra à coleção, configurando a relação bidirecional.
     *
     * @param item O item a ser adicionado.
     */
    public void addItem(ItemPedidoCompra item) {
        // Evita adicionar o mesmo item múltiplas vezes (opcional, dependendo do caso)
        if (!this.itens.contains(item)) {
            this.itens.add(item);
            item.setPedidoCompra(this); // Garante a bidirecionalidade
        }
    }

    /**
     * Remove um ItemPedidoCompra da coleção, desvinculando a relação bidirecional.
     *
     * @param item O item a ser removido.
     */
    public void removeItem(ItemPedidoCompra item) {
        if (this.itens.contains(item)) {
            this.itens.remove(item);
            item.setPedidoCompra(null); // Desvincula o item do pedido (ativa orphanRemoval)
        }
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
                ", status=" + (status != null ? status.name() : "null") +
                '}';
    }
}
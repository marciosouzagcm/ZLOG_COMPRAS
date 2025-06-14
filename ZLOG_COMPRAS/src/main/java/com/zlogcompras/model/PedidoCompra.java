package com.zlogcompras.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column; // <--- Adicione esta importação
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType; // <--- Adicione esta importação
import jakarta.persistence.Enumerated; // <--- Adicione esta importação
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

    private LocalDate dataPedido;
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING) // <<< ESTA LINHA É CRUCIAL!
    @Column(name = "status") // <<< É uma boa prática explicitar o nome da coluna no banco
    private StatusPedidoCompra status; // <<< ALTERADO: O tipo agora é o Enum

    private String observacoes;

    // Relacionamento OneToMany com ItemPedidoCompra
    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoCompra> itens = new ArrayList<>();

    // Construtor padrão (necessário para JPA)
    public PedidoCompra() {
    }

    // Construtor com campos (opcional, para conveniência)
    // O construtor também precisa ser atualizado para receber o enum
    public PedidoCompra(Fornecedor fornecedor, Orcamento orcamento, LocalDate dataPedido, BigDecimal valorTotal,
                        StatusPedidoCompra status, String observacoes) { // <<< ALTERADO: Tipo do parâmetro 'status'
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

    public StatusPedidoCompra getStatus() { // <<< ALTERADO: Tipo de retorno agora é o Enum
        return status;
    }

    public void setStatus(StatusPedidoCompra status) { // <<< ALTERADO: Tipo do parâmetro agora é o Enum
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
     * bidirecionalidade
     * e o funcionamento correto do cascade e orphanRemoval.
     *
     * @param itens A nova lista de itens.
     */
    public void setItens(List<ItemPedidoCompra> itens) {
        this.itens.clear();
        if (itens != null) {
            itens.forEach(this::addItem);
        }
    }

    /**
     * Adiciona um ItemPedidoCompra à coleção, configurando a relação bidirecional.
     *
     * @param item O item a ser adicionado.
     */
    public void addItem(ItemPedidoCompra item) {
        this.itens.add(item);
        item.setPedidoCompra(this);
    }

    /**
     * Remove um ItemPedidoCompra da coleção, desvinculando a relação bidirecional.
     *
     * @param item O item a ser removido.
     */
    public void removeItem(ItemPedidoCompra item) {
        this.itens.remove(item);
        item.setPedidoCompra(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
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
                ", status=" + (status != null ? status.name() : "null") + // Melhor forma de exibir o enum no toString
                '}';
    }
}
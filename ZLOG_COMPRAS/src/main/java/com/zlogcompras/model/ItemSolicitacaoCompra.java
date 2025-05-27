package com.zlogcompras.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "itens_solicitacao_compra")
public class ItemSolicitacaoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference("solicitacao-item") // Nome da referência deve corresponder ao SolicitacaoCompra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_compra_id", nullable = false)
    private SolicitacaoCompra solicitacaoCompra; // <--- TIPO CORRIGIDO PARA SolicitacaoCompra

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantidade;

    @Column(name = "descricao_adicional", length = 255)
    private String descricaoAdicional;

    @Enumerated(EnumType.STRING) // Mapeia o Enum para String no banco de dados
    @Column(nullable = false)
    private StatusItemSolicitacao status; // Tipo Enum

    @Version
    private Long version;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    public ItemSolicitacaoCompra() {
        this.status = StatusItemSolicitacao.PENDENTE; // Status padrão
    }

    public ItemSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra, Produto produto, BigDecimal quantidade, String descricaoAdicional) {
        this.solicitacaoCompra = solicitacaoCompra;
        this.produto = produto;
        this.quantidade = quantidade;
        this.descricaoAdicional = descricaoAdicional;
        this.status = StatusItemSolicitacao.PENDENTE; // Status padrão
    }

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SolicitacaoCompra getSolicitacaoCompra() {
        return solicitacaoCompra;
    }

    public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
        this.solicitacaoCompra = solicitacaoCompra;
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

    public String getDescricaoAdicional() {
        return descricaoAdicional;
    }

    public void setDescricaoAdicional(String descricaoAdicional) {
        this.descricaoAdicional = descricaoAdicional;
    }

    public StatusItemSolicitacao getStatus() {
        return status;
    }

    public void setStatus(StatusItemSolicitacao status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemSolicitacaoCompra that = (ItemSolicitacaoCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ItemSolicitacaoCompra{" +
                "id=" + id +
                ", solicitacaoCompraId=" + (solicitacaoCompra != null ? solicitacaoCompra.getId() : "null") +
                ", produtoId=" + (produto != null ? produto.getId() : "null") +
                ", quantidade=" + quantidade +
                ", descricaoAdicional='" + descricaoAdicional + '\'' +
                ", status=" + status +
                ", version=" + version +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}
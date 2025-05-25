package com.zlogcompras.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference; // Importar JsonBackReference
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "itens_solicitacao_compra")
public class ItemSolicitacaoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "solicitacao_compra_id", nullable = false)
    @JsonBackReference // Adicionado para lidar com a referência de volta na relação bidirecional
    private SolicitacaoCompra solicitacaoCompra;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false, precision = 10, scale = 3)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal quantidade;

    private String descricaoAdicional; // Campo opcional

    private String status; // Status do item na solicitação (ex: "Pendente", "Aprovado", "Rejeitado")

    @Version
    private Long version;

    // Construtor padrão
    public ItemSolicitacaoCompra() {
    }

    // Construtor com campos
    public ItemSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra, Produto produto, BigDecimal quantidade,
            String descricaoAdicional, String status) {
        this.solicitacaoCompra = solicitacaoCompra;
        this.produto = produto;
        this.quantidade = quantidade;
        this.descricaoAdicional = descricaoAdicional;
        this.status = status;
    }

    // --- Getters ---
    public Long getId() {
        return id;
    }

    public SolicitacaoCompra getSolicitacaoCompra() {
        return solicitacaoCompra;
    }

    public Produto getProduto() {
        return produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public String getDescricaoAdicional() {
        return descricaoAdicional;
    }

    public String getStatus() {
        return status;
    }

    public Long getVersion() {
        return version;
    }

    // --- Setters ---
    public void setId(Long id) {
        this.id = id;
    }

    public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
        this.solicitacaoCompra = solicitacaoCompra;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public void setDescricaoAdicional(String descricaoAdicional) {
        this.descricaoAdicional = descricaoAdicional;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    // --- Métodos equals e hashCode (apenas pelo ID para consistência com
    // SolicitacaoCompra) ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ItemSolicitacaoCompra that = (ItemSolicitacaoCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- Método toString ---
    @Override
    public String toString() {
        return "ItemSolicitacaoCompra{" +
                "id=" + id +
                ", solicitacaoCompraId=" + (solicitacaoCompra != null ? solicitacaoCompra.getId() : "null") +
                ", produtoId=" + (produto != null ? produto.getId() : "null") +
                ", quantidade=" + quantidade +
                ", descricaoAdicional='" + descricaoAdicional + '\'' +
                ", status='" + status + '\'' +
                ", version=" + version +
                '}';
    }
}

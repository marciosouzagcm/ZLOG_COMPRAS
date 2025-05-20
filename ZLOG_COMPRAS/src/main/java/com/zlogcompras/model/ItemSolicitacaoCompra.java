package com.zlogcompras.model;

import java.util.Optional;
import java.util.Set;

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
@Table(name = "itens_solicitacao_compra")
public class ItemSolicitacaoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String materialServico;

    @Column(nullable = false)
    private Integer quantidade;

    private String descricao;

    @Column(nullable = false)
    private String status;

    @Version
    private Long version;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_compra_id", nullable = false)
    private SolicitacaoCompra solicitacaoCompra;

    public ItemSolicitacaoCompra() {
        this.status = "Pendente";
    }

    // Construtor com campos básicos
    public ItemSolicitacaoCompra(String materialServico, Integer quantidade, String descricao, String status) {
        this.materialServico = materialServico;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaterialServico() { return materialServico; }
    public void setMaterialServico(String materialServico) { this.materialServico = materialServico; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public SolicitacaoCompra getSolicitacaoCompra() { return solicitacaoCompra; }
    public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) { this.solicitacaoCompra = solicitacaoCompra; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemSolicitacaoCompra that = (ItemSolicitacaoCompra) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    // Atualize cada item existente individualmente
    public void atualizarItens(SolicitacaoCompra solicitacaoAtualizada, SolicitacaoCompra solicitacaoExistente) {
        Set<ItemSolicitacaoCompra> itensAtualizados = solicitacaoAtualizada.getItens();
        for (ItemSolicitacaoCompra itemAtualizado : itensAtualizados) {
            Optional<ItemSolicitacaoCompra> itemExistenteOpt = solicitacaoExistente.getItens().stream()
                .filter(i -> i.getId().equals(itemAtualizado.getId()))
                .findFirst();
            if (itemExistenteOpt.isPresent()) {
                ItemSolicitacaoCompra itemExistente = itemExistenteOpt.get();
                itemExistente.setMaterialServico(itemAtualizado.getMaterialServico());
                itemExistente.setQuantidade(itemAtualizado.getQuantidade());
                itemExistente.setDescricao(itemAtualizado.getDescricao());
                itemExistente.setStatus(itemAtualizado.getStatus());
                // ...atualize os campos necessários...
            } else {
                // Se for um novo item, adicione normalmente
                solicitacaoExistente.addItem(itemAtualizado);
            }
        }
    }
}
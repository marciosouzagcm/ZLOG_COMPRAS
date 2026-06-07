package com.zlogcompras.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference; // Manter este se ItemSolicitacaoCompra tiver @JsonBackReference

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "solicitacoes_compra")
public class SolicitacaoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDate dataSolicitacao;

    @Column(nullable = false)
    private String solicitante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacaoCompra status;

    @Column(name = "descricao", nullable = true)
    private String descricao;

    @Version
    private Long version;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    // REMOVIDO: @JsonManagedReference para 'orcamentos'
    // Se você não precisa serializar orçamentos diretamente da SolicitacaoCompra,
    // essa anotação não é necessária e pode causar LazyInitializationException.
    @OneToMany(mappedBy = "solicitacaoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Orcamento> orcamentos = new HashSet<>();

    @JsonManagedReference("solicitacao-item") // Mantenha este se ItemSolicitacaoCompra tiver @JsonBackReference e você
                                              // quer serializar itens.
    @OneToMany(mappedBy = "solicitacaoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemSolicitacaoCompra> itens = new HashSet<>();

    public SolicitacaoCompra() {
        this.dataSolicitacao = LocalDate.now();
        this.status = StatusSolicitacaoCompra.PENDENTE;
    }

    public SolicitacaoCompra(String solicitante, String descricao) {
        this();
        this.solicitante = solicitante;
        this.descricao = descricao;
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

    // --- Getters ---
    public Long getId() {
        return id;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public StatusSolicitacaoCompra getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }

    public Long getVersion() {
        return version;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public Set<Orcamento> getOrcamentos() {
        return orcamentos;
    }

    public Set<ItemSolicitacaoCompra> getItens() {
        return itens;
    }

    // --- Setters ---
    public void setId(Long id) {
        this.id = id;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public void setStatus(StatusSolicitacaoCompra status) {
        this.status = status;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public void setOrcamentos(Set<Orcamento> orcamentos) {
        if (orcamentos == null) {
            this.orcamentos.clear();
            return;
        }
        this.orcamentos.clear();
        orcamentos.forEach(this::addOrcamento);
    }

    /**
     * IMPORTANTE: Este setter agora gerencia a coleção de itens de forma mais
     * idiomática com `orphanRemoval = true`.
     * Ele limpa a coleção existente e adiciona os novos itens. O JPA/Hibernate
     * cuidará
     * automaticamente de deletar os "órfãos" (itens que não estão mais presentes) e
     * persistir/atualizar os demais.
     *
     * @param novosItens A nova lista de ItemSolicitacaoCompra a ser aplicada.
     */
    public void setItens(Set<ItemSolicitacaoCompra> novosItens) {
        // Garante que a coleção esteja sempre sincronizada com o pai
        this.itens.clear();
        if (novosItens != null) {
            for (ItemSolicitacaoCompra item : novosItens) {
                this.addItem(item); // Usa o método auxiliar para manter a bidirecionalidade
            }
        }
    }

    // --- Métodos auxiliares para gerenciar a coleção de itens e a
    // bidirecionalidade ---
    public void addItem(ItemSolicitacaoCompra item) {
        if (item != null) {
            if (!this.itens.contains(item)) {
                this.itens.add(item);
            }
            item.setSolicitacaoCompra(this); // Garante a bidirecionalidade
        }
    }

    public void removeItem(ItemSolicitacaoCompra item) {
        if (item != null && this.itens.contains(item)) {
            this.itens.remove(item);
            item.setSolicitacaoCompra(null); // Crucial para orphanRemoval
        }
    }

    // Métodos auxiliares para gerenciar a coleção de orçamentos
    public void addOrcamento(Orcamento orcamento) {
        if (orcamento != null) {
            if (!this.orcamentos.contains(orcamento)) {
                this.orcamentos.add(orcamento);
            }
            orcamento.setSolicitacaoCompra(this); // Garante a bidirecionalidade
        }
    }

    public void removeOrcamento(Orcamento orcamento) {
        if (orcamento != null && this.orcamentos.contains(orcamento)) {
            this.orcamentos.remove(orcamento);
            orcamento.setSolicitacaoCompra(null);
        }
    }

    // --- Métodos equals e hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SolicitacaoCompra that = (SolicitacaoCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- Método toString ---
    @Override
    public String toString() {
        return "SolicitacaoCompra{" +
                "id=" + id +
                ", dataSolicitacao=" + dataSolicitacao +
                ", solicitante='" + solicitante + '\'' +
                ", status=" + status +
                ", descricao='" + descricao + '\'' +
                ", version=" + version +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                ", orcamentosSize=" + (orcamentos != null ? orcamentos.size() : 0) +
                ", itensSize=" + (itens != null ? itens.size() : 0) +
                '}';
    }
}
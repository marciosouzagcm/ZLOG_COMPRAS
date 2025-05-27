package com.zlogcompras.model;

import java.time.LocalDate;
import java.time.LocalDateTime; // Import para LocalDateTime
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType; // Import para EnumType
import jakarta.persistence.Enumerated; // Import para Enumerated
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist; // Import para PrePersist
import jakarta.persistence.PreUpdate;  // Import para PreUpdate
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

    @Enumerated(EnumType.STRING) // Mapeia o Enum para String no banco de dados
    @Column(nullable = false)
    private StatusSolicitacaoCompra status; // Alterado para o Enum StatusSolicitacaoCompra

    @Column(name = "descricao", nullable = true)
    private String descricao;

    @Version
    private Long version;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao; // Adicionado: Data de criação

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao; // Adicionado: Data de última atualização

    @JsonManagedReference("solicitacao-orcamento") // Nomeie a referência para evitar conflitos
    @OneToMany(mappedBy = "solicitacaoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Orcamento> orcamentos = new HashSet<>(); // Adicionado: Relacionamento com Orçamentos

    @JsonManagedReference("solicitacao-item") // Nomeie a referência para evitar conflitos
    @OneToMany(mappedBy = "solicitacaoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemSolicitacaoCompra> itens = new HashSet<>();

    public SolicitacaoCompra() {
        this.dataSolicitacao = LocalDate.now();
        this.status = StatusSolicitacaoCompra.PENDENTE; // Inicializa com um status padrão do Enum
    }

    // Construtor com campos básicos para facilitar a criação (opcional, mas útil)
    public SolicitacaoCompra(String solicitante, String descricao) {
        this(); // Chama o construtor padrão para inicializar data e status
        this.solicitante = solicitante;
        this.descricao = descricao;
    }

    // Métodos de callback JPA para preencher automaticamente as datas
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

    public StatusSolicitacaoCompra getStatus() { // Retorna o Enum
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

    public void setStatus(StatusSolicitacaoCompra status) { // Recebe o Enum
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
     * IMPORTANTE: Este setter gerencia a coleção de itens para atualizações
     * completas (PUT).
     * Ele lida com a adição de novos itens, atualização de itens existentes
     * e remoção de itens que não estão mais presentes na lista fornecida.
     *
     * @param novosItens A nova lista de ItemSolicitacaoCompra a ser aplicada.
     */
    public void setItens(Set<ItemSolicitacaoCompra> novosItens) {
        if (novosItens == null) {
            novosItens = new HashSet<>(); // Garante que a coleção não seja nula
        }

        // Crie um set temporário para itens que devem ser mantidos ou adicionados
        Set<ItemSolicitacaoCompra> itensParaManterOuAdicionar = new HashSet<>();

        for (ItemSolicitacaoCompra novoOuAtualizadoItem : novosItens) {
            novoOuAtualizadoItem.setSolicitacaoCompra(this); // Garante a bidirecionalidade

            if (novoOuAtualizadoItem.getId() == null) {
                // É um item novo
                itensParaManterOuAdicionar.add(novoOuAtualizadoItem);
            } else {
                // É um item existente (possui ID), encontre-o na coleção gerenciada para atualização
                this.itens.stream()
                    .filter(itemExistente -> novoOuAtualizadoItem.getId().equals(itemExistente.getId()))
                    .findFirst()
                    .ifPresentOrElse(existingItem -> {
                        // Atualiza as propriedades do item existente
                        existingItem.setProduto(novoOuAtualizadoItem.getProduto());
                        existingItem.setQuantidade(novoOuAtualizadoItem.getQuantidade());
                        existingItem.setDescricaoAdicional(novoOuAtualizadoItem.getDescricaoAdicional());
                        existingItem.setStatus(novoOuAtualizadoItem.getStatus());
                        itensParaManterOuAdicionar.add(existingItem); // Adiciona o item atualizado para manter
                    }, () -> {
                        // Se o item tem ID, mas não está na coleção atual, adiciona como novo
                        itensParaManterOuAdicionar.add(novoOuAtualizadoItem);
                    });
            }
        }
        // Limpa a coleção atual e adiciona todos os itens que devem ser mantidos ou adicionados
        this.itens.clear();
        this.itens.addAll(itensParaManterOuAdicionar);
    }

    // --- Métodos auxiliares para gerenciar a coleção de itens e a bidirecionalidade ---
    public void addItem(ItemSolicitacaoCompra item) {
        if (item != null) {
            if (!this.itens.contains(item)) {
                this.itens.add(item);
                item.setSolicitacaoCompra(this);
            }
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
                orcamento.setSolicitacaoCompra(this);
            }
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
                ", status=" + status + // Exibir o Enum
                ", descricao='" + descricao + '\'' +
                ", version=" + version +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                ", orcamentosSize=" + (orcamentos != null ? orcamentos.size() : 0) +
                ", itensSize=" + (itens != null ? itens.size() : 0) +
                '}';
    }
}

package com.zlogcompras.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

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

    @Column(nullable = false)
    private String status;

    @Version
    private Long version;

    @JsonManagedReference
    @OneToMany(mappedBy = "solicitacaoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemSolicitacaoCompra> itens = new HashSet<>();

    public SolicitacaoCompra() {
        this.dataSolicitacao = LocalDate.now();
        this.status = "Pendente";
    }

    // Getters
    public Long getId() { return id; }
    public LocalDate getDataSolicitacao() { return dataSolicitacao; }
    public String getSolicitante() { return solicitante; }
    public String getStatus() { return status; }
    public Long getVersion() { return version; }
    public Set<ItemSolicitacaoCompra> getItens() { return itens; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setDataSolicitacao(LocalDate dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }
    public void setStatus(String status) { this.status = status; }
    public void setVersion(Long version) { this.version = version; }

    /**
     * IMPORTANTE: Este setter gerencia a coleção de itens para atualizações completas (PUT).
     * Ele lida com a adição de novos itens, atualização de itens existentes
     * e remoção de itens que não estão mais presentes na lista fornecida.
     *
     * @param novosItens A nova lista de ItemSolicitacaoCompra a ser aplicada.
     */
    public void setItens(Set<ItemSolicitacaoCompra> novosItens) {
        if (novosItens == null) {
            novosItens = new HashSet<>(); // Garante que a coleção não seja nula
        }

        // 1. Identificar e remover itens que não estão mais na nova lista
        // Cria uma cópia da coleção atual para evitar ConcurrentModificationException
        Set<ItemSolicitacaoCompra> itensAtuais = new HashSet<>(this.itens);
        final Set<ItemSolicitacaoCompra> finalNovosItens = novosItens;
        itensAtuais.stream()
            .filter(itemAtual -> !finalNovosItens.contains(itemAtual)) // Usa equals/hashCode de ItemSolicitacaoCompra
            .collect(Collectors.toSet()) // Coleta os itens para remover
            .forEach(this::removeItem); // Chama removeItem para cada um

        // 2. Adicionar ou atualizar itens da nova lista
        for (ItemSolicitacaoCompra novoOuAtualizadoItem : novosItens) {
            if (novoOuAtualizadoItem.getId() == null) {
                // É um item novo, adicione à coleção
                this.addItem(novoOuAtualizadoItem);
            } else {
                // É um item existente (possui ID), encontre-o na coleção gerenciada para atualização
                // Evita NPE verificando se o ID é nulo no item sendo processado
                this.itens.stream()
                    .filter(itemExistente -> novoOuAtualizadoItem.getId().equals(itemExistente.getId()))
                    .findFirst()
                    .ifPresentOrElse(existingItem -> {
                        // Atualiza os campos do item existente
                        existingItem.setDescricao(novoOuAtualizadoItem.getDescricao());
                        existingItem.setMaterialServico(novoOuAtualizadoItem.getMaterialServico());
                        existingItem.setQuantidade(novoOuAtualizadoItem.getQuantidade());
                        existingItem.setStatus(novoOuAtualizadoItem.getStatus());
                        // Pode adicionar outros campos aqui conforme necessário
                    }, () -> {
                        // Se o item tem ID, mas não está na coleção atual, ele pode ter sido desvinculado
                        // ou é um item existente que não foi carregado. Adicione-o.
                        this.addItem(novoOuAtualizadoItem);
                    });
            }
        }
    }


    // Métodos auxiliares para gerenciar a coleção de itens e a bidirecionalidade
    public void addItem(ItemSolicitacaoCompra item) {
        // Evita adicionar o mesmo item duas vezes se ele já estiver na coleção
        if (!this.itens.contains(item)) {
            this.itens.add(item);
            item.setSolicitacaoCompra(this); // Garante a bidirecionalidade
        }
    }

    public void removeItem(ItemSolicitacaoCompra item) {
        if (this.itens.contains(item)) {
            this.itens.remove(item);
            // IMPORTANTE: Definir o lado "Many" como null é crucial para o orphanRemoval
            // Se o item for removido da coleção e não tiver um novo pai, ele será deletado.
            item.setSolicitacaoCompra(null);
        }
    }
}
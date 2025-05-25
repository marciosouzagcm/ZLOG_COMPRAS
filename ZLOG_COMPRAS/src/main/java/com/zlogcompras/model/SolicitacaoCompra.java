package com.zlogcompras.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(nullable = false)
    private String status;

    // NOVO CAMPO: Descrição da solicitação de compra
    @Column(name = "descricao", nullable = true) // Pode ser nullable=false se for obrigatório
    private String descricao;

    @Version
    private Long version;

    @JsonManagedReference
    @OneToMany(mappedBy = "solicitacaoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemSolicitacaoCompra> itens = new HashSet<>();

    public SolicitacaoCompra() {
        this.dataSolicitacao = LocalDate.now();
        this.status = "Pendente";
    }

    // Construtor com campos básicos para facilitar a criação (opcional, mas útil)
    public SolicitacaoCompra(String solicitante) {
        this(); // Chama o construtor padrão para inicializar data e status
        this.solicitante = solicitante;
    }

    // Construtor atualizado para incluir a descrição (opcional)
    public SolicitacaoCompra(String solicitante, String descricao) {
        this(solicitante); // Chama o construtor anterior
        this.descricao = descricao;
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

    public String getStatus() {
        return status;
    }

    // Getter para o novo campo descricao
    public String getDescricao() {
        return descricao;
    }

    public Long getVersion() {
        return version;
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

    public void setStatus(String status) {
        this.status = status;
    }

    // Setter para o novo campo descricao
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setVersion(Long version) {
        this.version = version;
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
    // Mantidos para clareza, embora o 'setItens' já faça a maior parte do trabalho
    public void addItem(ItemSolicitacaoCompra item) {
        if (item != null) { // Apenas verifica se o item não é nulo
            if (!this.itens.contains(item)) { // Garante unicidade no Set
                this.itens.add(item);
                item.setSolicitacaoCompra(this);
            }
        }
    }

    public void removeItem(ItemSolicitacaoCompra item) {
        if (item != null && this.itens.contains(item)) { // Garante que o item existe na coleção
            this.itens.remove(item);
            item.setSolicitacaoCompra(null); // Crucial para orphanRemoval
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
                ", status='" + status + '\'' +
                ", descricao='" + descricao + '\'' + // Adicionado ao toString
                ", version=" + version +
                ", itens=" + (itens != null ? itens.size() : 0) +
                '}';
    }
}
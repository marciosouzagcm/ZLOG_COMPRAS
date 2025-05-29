package com.zlogcompras.model;

import java.time.LocalDateTime; // Alterado para LocalDateTime para timestamps
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate; // Importe para @Column
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener; // Importe para @EntityListeners

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne; // Importe para auditoria
import jakarta.persistence.Table; // Importe para auditoria
import jakarta.persistence.Version; // Importe para auditoria

@Entity
@Table(name = "estoque")
@EntityListeners(AuditingEntityListener.class) // Habilita a auditoria para esta entidade
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false) // Geralmente a quantidade não pode ser nula
    private Integer quantidade;

    private String localizacao;

    @Column(name = "data_ultima_entrada") // Nome da coluna no banco, se diferente do nome do campo
    private LocalDateTime dataUltimaEntrada; // Mantido como LocalDateTime para precisão

    @Column(name = "data_ultima_saida") // Nome da coluna no banco, se diferente do nome do campo
    private LocalDateTime dataUltimaSaida; // Alterado para LocalDateTime para precisão

    private String observacoes;

    @Version
    private Long version;

    // Campos de Auditoria
    @CreatedDate // Preenche automaticamente a data de criação
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @LastModifiedDate // Preenche automaticamente a data de última atualização
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    // Construtor padrão
    public Estoque() {
    }

    // Construtor com campos (não inclua dataCriacao e dataAtualizacao aqui, pois são gerados automaticamente)
    public Estoque(Produto produto, Integer quantidade, String localizacao,
                   LocalDateTime dataUltimaEntrada, LocalDateTime dataUltimaSaida, String observacoes) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.localizacao = localizacao;
        this.dataUltimaEntrada = dataUltimaEntrada;
        this.dataUltimaSaida = dataUltimaSaida;
        this.observacoes = observacoes;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public LocalDateTime getDataUltimaEntrada() {
        return dataUltimaEntrada;
    }

    public void setDataUltimaEntrada(LocalDateTime dataUltimaEntrada) {
        this.dataUltimaEntrada = dataUltimaEntrada;
    }

    public LocalDateTime getDataUltimaSaida() {
        return dataUltimaSaida;
    }

    public void setDataUltimaSaida(LocalDateTime dataUltimaSaida) {
        this.dataUltimaSaida = dataUltimaSaida;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    // Getters para os novos campos de auditoria
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    // Não há setter para dataCriacao, pois é definido automaticamente na criação

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    // Não há setter para dataAtualizacao, pois é definido automaticamente na atualização

    // Opcional: Adicione equals e hashCode se ainda não tiver
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Estoque estoque = (Estoque) o;
        return Objects.equals(id, estoque.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
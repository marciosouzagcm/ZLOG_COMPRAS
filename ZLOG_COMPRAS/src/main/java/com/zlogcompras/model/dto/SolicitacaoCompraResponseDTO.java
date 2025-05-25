package com.zlogcompras.model.dto;

import java.time.LocalDate;
import java.util.Set; // Usar Set para itens

public class SolicitacaoCompraResponseDTO {

    private Long id;
    private LocalDate dataSolicitacao;
    private String solicitante;
    private String status;
    private Long version; // Pode ser útil expor a versão para controle de concorrência no cliente
    private Set<ItemSolicitacaoCompraResponseDTO> itens; // Usamos o DTO de Response para os itens

    // Construtor padrão
    public SolicitacaoCompraResponseDTO() {
    }

    // Construtor com todos os campos
    public SolicitacaoCompraResponseDTO(Long id, LocalDate dataSolicitacao, String solicitante, String status, Long version, Set<ItemSolicitacaoCompraResponseDTO> itens) {
        this.id = id;
        this.dataSolicitacao = dataSolicitacao;
        this.solicitante = solicitante;
        this.status = status;
        this.version = version;
        this.itens = itens;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Set<ItemSolicitacaoCompraResponseDTO> getItens() {
        return itens;
    }

    public void setItens(Set<ItemSolicitacaoCompraResponseDTO> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "SolicitacaoCompraResponseDTO{" +
               "id=" + id +
               ", dataSolicitacao=" + dataSolicitacao +
               ", solicitante='" + solicitante + '\'' +
               ", status='" + status + '\'' +
               ", version=" + version +
               ", itens=" + (itens != null ? itens.size() : 0) +
               '}';
    }
}
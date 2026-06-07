package com.zlogcompras.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime; // Importe LocalDateTime
import java.util.List;

public class SolicitacaoCompraResponseDTO {

    private Long id;
    private Long solicitacaoCompraId; // Mapeado do 'id' da entidade
    private LocalDate dataSolicitacao;
    private String solicitante;
    private String status; // Será mapeado do Enum StatusSolicitacaoCompra para String
    private String descricaoSolicitacaoCompra; // Mapeado da 'descricao' da entidade
    private Long version;
    private LocalDateTime dataCriacao; // ADICIONADO: Campo dataCriacao
    private LocalDateTime dataAtualizacao; // ADICIONADO: Campo dataAtualizacao
    private List<ItemSolicitacaoCompraResponseDTO> itens; // Lista de itens de resposta

    // Construtor padrão
    public SolicitacaoCompraResponseDTO() {
    }

    // Getters e Setters (Certifique-se de que os getters e setters para dataCriacao e dataAtualizacao também estejam aqui)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSolicitacaoCompraId() { return solicitacaoCompraId; }
    public void setSolicitacaoCompraId(Long solicitacaoCompraId) { this.solicitacaoCompraId = solicitacaoCompraId; }

    public LocalDate getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDate dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescricaoSolicitacaoCompra() { return descricaoSolicitacaoCompra; }
    public void setDescricaoSolicitacaoCompra(String descricaoSolicitacaoCompra) { this.descricaoSolicitacaoCompra = descricaoSolicitacaoCompra; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    // Novos Getters e Setters para as datas
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public List<ItemSolicitacaoCompraResponseDTO> getItens() { return itens; }
    public void setItens(List<ItemSolicitacaoCompraResponseDTO> itens) { this.itens = itens; }
}
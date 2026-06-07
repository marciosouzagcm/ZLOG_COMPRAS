package com.zlogcompras.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SolicitacaoCompraListaDTO {
    private Long id;
    private String status;
    private LocalDateTime dataSolicitacao;
    private List<ItemSolicitacaoDTO> itensSolicitacao;

    public SolicitacaoCompraListaDTO() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public List<ItemSolicitacaoDTO> getItensSolicitacao() {
        return itensSolicitacao;
    }
    public void setItensSolicitacao(List<ItemSolicitacaoDTO> itensSolicitacao) {
        this.itensSolicitacao = itensSolicitacao;
    }
}
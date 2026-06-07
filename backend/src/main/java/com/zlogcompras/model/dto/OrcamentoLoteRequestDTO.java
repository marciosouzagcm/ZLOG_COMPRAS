package com.zlogcompras.model.dto;

import jakarta.validation.Valid; // Certifique-se de importar isso se estiver usando validações
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class OrcamentoLoteRequestDTO {

    @NotNull(message = "O ID da Solicitação de Compra é obrigatório.")
    private Long solicitacaoCompraId;

    @NotNull(message = "A lista de orçamentos não pode ser nula.")
    @Size(min = 1, message = "Deve haver pelo menos um orçamento na lista.")
    @Valid // Garante que as validações dentro de OrcamentoRequestDTO sejam aplicadas
    private List<OrcamentoRequestDTO> orcamentos;

    // Construtores
    public OrcamentoLoteRequestDTO() {
    }

    public OrcamentoLoteRequestDTO(Long solicitacaoCompraId, List<OrcamentoRequestDTO> orcamentos) {
        this.solicitacaoCompraId = solicitacaoCompraId;
        this.orcamentos = orcamentos;
    }

    // Getters e Setters
    public Long getSolicitacaoCompraId() {
        return solicitacaoCompraId;
    }

    public void setSolicitacaoCompraId(Long solicitacaoCompraId) {
        this.solicitacaoCompraId = solicitacaoCompraId;
    }

    public List<OrcamentoRequestDTO> getOrcamentos() {
        return orcamentos;
    }

    public void setOrcamentos(List<OrcamentoRequestDTO> orcamentos) {
        this.orcamentos = orcamentos;
    }
}
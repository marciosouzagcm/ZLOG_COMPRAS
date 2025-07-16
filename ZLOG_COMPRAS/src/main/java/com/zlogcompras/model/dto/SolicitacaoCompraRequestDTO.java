package com.zlogcompras.model.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolicitacaoCompraRequestDTO {

    @NotBlank(message = "O solicitante é obrigatório")
    private String solicitante;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "O status é obrigatório")
    private String status; // Será mapeado para o Enum StatusSolicitacaoCompra

    @NotNull(message = "Os itens da solicitação são obrigatórios")
    @Valid // Para validar os itens dentro da lista
    private List<ItemSolicitacaoCompraRequestDTO> itens;

    // Construtor padrão
    public SolicitacaoCompraRequestDTO() {
    }

    // Getters e Setters
    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemSolicitacaoCompraRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemSolicitacaoCompraRequestDTO> itens) {
        this.itens = itens;
    }
}
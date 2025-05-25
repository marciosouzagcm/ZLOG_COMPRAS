package com.zlogcompras.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set; // Usar Set para itens

public class SolicitacaoCompraRequestDTO {

    // Em uma requisição de criação, o ID geralmente não é enviado.
    // Em uma requisição de atualização (PUT), o ID pode vir no corpo, mas o do @PathVariable é o que manda.
    private Long id; // Opcional, para casos de PUT onde o cliente pode replicar o ID no corpo

    @NotNull(message = "A data da solicitação é obrigatória.")
    private LocalDate dataSolicitacao;

    @NotBlank(message = "O nome do solicitante é obrigatório.")
    private String solicitante;

    // O status pode ser predefinido pelo backend na criação, mas pode ser atualizável via PUT.
    // Se você não quer que o status seja enviado na criação, remova o @NotBlank aqui e o defina no Service.
    private String status;

    @NotEmpty(message = "Uma solicitação de compra deve ter pelo menos um item.")
    @Valid // Valida cada ItemSolicitacaoCompraRequestDTO dentro do Set
    private Set<ItemSolicitacaoCompraRequestDTO> itens; // Usamos o DTO de Request para os itens

    // Construtor padrão
    public SolicitacaoCompraRequestDTO() {
        this.dataSolicitacao = LocalDate.now(); // Pode ser predefinido no Request DTO
    }

    // Construtor com todos os campos (útil para testes ou mapeamento)
    public SolicitacaoCompraRequestDTO(Long id, LocalDate dataSolicitacao, String solicitante, String status, Set<ItemSolicitacaoCompraRequestDTO> itens) {
        this.id = id;
        this.dataSolicitacao = dataSolicitacao;
        this.solicitante = solicitante;
        this.status = status;
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

    public Set<ItemSolicitacaoCompraRequestDTO> getItens() {
        return itens;
    }

    public void setItens(Set<ItemSolicitacaoCompraRequestDTO> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "SolicitacaoCompraRequestDTO{" +
               "id=" + id +
               ", dataSolicitacao=" + dataSolicitacao +
               ", solicitante='" + solicitante + '\'' +
               ", status='" + status + '\'' +
               ", itens=" + (itens != null ? itens.size() : 0) + // Mostra apenas a contagem de itens
               '}';
    }
}
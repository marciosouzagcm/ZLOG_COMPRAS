package com.zlogcompras.model.dto;

import java.time.LocalDateTime; // Importe LocalDateTime

public class EstoqueRequestDTO {

    private Long produtoId; // Para ligar com o Produto existente
    private Integer quantidade;
    private String localizacao;

    // ALTERE AQUI: de LocalDate para LocalDateTime
    private LocalDateTime dataUltimaEntrada; 
    
    // ALTERE AQUI: de LocalDate para LocalDateTime
    private LocalDateTime dataUltimaSaida; 
    
    private String observacoes;

    // Se você tiver um campo "quantidadeAtual" no DTO e não quiser mapeá-lo
    // para a entidade Estoque, você pode lidar com o warning do MapStruct
    // mais tarde. Por enquanto, focaremos na compilação.
    // private Integer quantidadeAtual; 
    // private LocalDate dataEntrada; // Se tiver, pode ser removido ou alterado

    // Construtor, Getters e Setters
    public EstoqueRequestDTO() {
    }

    // Se você tem um construtor com todos os campos, ajuste-o também
    public EstoqueRequestDTO(Long produtoId, Integer quantidade, String localizacao,
                             LocalDateTime dataUltimaEntrada, LocalDateTime dataUltimaSaida,
                             String observacoes) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.localizacao = localizacao;
        this.dataUltimaEntrada = dataUltimaEntrada;
        this.dataUltimaSaida = dataUltimaSaida;
        this.observacoes = observacoes;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
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

    public LocalDateTime getDataUltimaEntrada() { // Altere o retorno
        return dataUltimaEntrada;
    }

    public void setDataUltimaEntrada(LocalDateTime dataUltimaEntrada) { // Altere o parâmetro
        this.dataUltimaEntrada = dataUltimaEntrada;
    }

    public LocalDateTime getDataUltimaSaida() { // Altere o retorno
        return dataUltimaSaida;
    }

    public void setDataUltimaSaida(LocalDateTime dataUltimaSaida) { // Altere o parâmetro
        this.dataUltimaSaida = dataUltimaSaida;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
package com.zlogcompras.model.dto;

import java.time.LocalDateTime; // <-- Certifique-se de importar LocalDateTime

public class EstoqueResponseDTO {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private String codigoProduto;
    private Integer quantidadeAtual;
    
    // --- ALTERE AQUI: de LocalDate para LocalDateTime ---
    private LocalDateTime dataEntrada; 
    
    // Se você tiver este campo no seu DTO de resposta
    // private LocalDateTime dataSaida; // Exemplo, se você tiver esse campo também

    private String localizacao;
    private Long version; // Opcional, mas se tiver, mantenha Long para corresponder à entidade

    // Adicione ou verifique se você tem os campos dataCriacao e dataAtualizacao aqui,
    // se você quiser que eles apareçam na resposta
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    // Construtor, Getters e Setters
    public EstoqueResponseDTO() {
    }

    // Se tiver um construtor com todos os campos, ajuste-o também
    public EstoqueResponseDTO(Long id, Long produtoId, String nomeProduto, String codigoProduto,
                              Integer quantidadeAtual, LocalDateTime dataEntrada, // <-- Ajuste aqui
                              String localizacao, Long version,
                              LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) { // Se incluir auditoria
        this.id = id;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.codigoProduto = codigoProduto;
        this.quantidadeAtual = quantidadeAtual;
        this.dataEntrada = dataEntrada; // <-- Ajuste aqui
        this.localizacao = localizacao;
        this.version = version;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    // --- Getters e Setters para os campos de data/hora DEVE RETORNAR/RECEBER LocalDateTime ---
    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
    
    // Se tiver dataSaida também
    // public LocalDateTime getDataSaida() { return dataSaida; }
    // public void setDataSaida(LocalDateTime dataSaida) { this.dataSaida = dataSaida; }

    // ... (outros getters e setters existentes) ...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public Integer getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(Integer quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
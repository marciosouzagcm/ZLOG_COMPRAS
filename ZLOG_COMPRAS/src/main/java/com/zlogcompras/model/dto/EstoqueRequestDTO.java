package com.zlogcompras.model.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull; // Importar esta anotação
import jakarta.validation.constraints.Positive; // Importar esta anotação (para IDs)
import jakarta.validation.constraints.PositiveOrZero; // Importar esta anotação (para quantidades)

public class EstoqueRequestDTO {

    // Adicione @NotNull para produtoId, pois produto_id na tabela é NOT NULL
    @NotNull(message = "O ID do produto é obrigatório para criar um registro de estoque.")
    @Positive(message = "O ID do produto deve ser um valor positivo.") // Garante que o ID é válido (> 0)
    private Long produtoId;

    // Adicione @NotNull e @PositiveOrZero para quantidade, pois é NOT NULL no DB
    @NotNull(message = "A quantidade é obrigatória.")
    @PositiveOrZero(message = "A quantidade deve ser um valor positivo ou zero.")
    private Integer quantidade;

    private String localizacao; // 'localizacao' é NULLABLE no DB, então @NotNull não é estritamente necessário aqui

    private LocalDateTime dataUltimaEntrada; // 'data_ultima_entrada' é NULLABLE no DB
    private LocalDateTime dataUltimaSaida;   // 'data_ultima_saida' é NULLABLE no DB

    private String observacoes; // 'observacoes' é NULLABLE no DB

    // Construtor padrão
    public EstoqueRequestDTO() {
    }

    // Construtor com todos os campos (bom para testes e clareza)
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

    // Getters e Setters (já estão corretos no seu código para LocalDateTime)
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
}
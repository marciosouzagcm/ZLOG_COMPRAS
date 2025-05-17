package com.zlogcompras.model.dto;

import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.Fornecedor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrcamentoTabelaDTO {

    private Long id;
    private LocalDate dataCotacao;
    private String nomeFornecedor;
    private BigDecimal precoTotal;
    private String prazoEntrega; // Pode ser String inicialmente
    private String condicoesPagamento;
    private String observacoes;
    private List<ItemOrcamento> itensOrcamento; // Podemos usar isso para detalhes, se necessário

    // Construtores
    public OrcamentoTabelaDTO() {
    }

    public OrcamentoTabelaDTO(Long id, LocalDate dataCotacao, String nomeFornecedor, BigDecimal precoTotal, String prazoEntrega, String condicoesPagamento, String observacoes, List<ItemOrcamento> itensOrcamento) {
        this.id = id;
        this.dataCotacao = dataCotacao;
        this.nomeFornecedor = nomeFornecedor;
        this.precoTotal = precoTotal;
        this.prazoEntrega = prazoEntrega;
        this.condicoesPagamento = condicoesPagamento;
        this.observacoes = observacoes;
        this.itensOrcamento = itensOrcamento;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataCotacao() {
        return dataCotacao;
    }

    public void setDataCotacao(LocalDate dataCotacao) {
        this.dataCotacao = dataCotacao;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    public BigDecimal getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(BigDecimal precoTotal) {
        this.precoTotal = precoTotal;
    }

    public String getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(String prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    public String getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(String condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<ItemOrcamento> getItensOrcamento() {
        return itensOrcamento;
    }

    public void setItensOrcamento(List<ItemOrcamento> itensOrcamento) {
        this.itensOrcamento = itensOrcamento;
    }
}
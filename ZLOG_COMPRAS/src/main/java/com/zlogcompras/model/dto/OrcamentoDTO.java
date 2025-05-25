package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrcamentoDTO {
    private Long id;
    private String numeroOrcamento;
    private String nomeFornecedor;
    private LocalDate dataCotacao;
    private BigDecimal valorTotal;
    private String status;
    private Long solicitacaoCompraId;
    private List<ItemOrcamentoDTO> itens; // Supondo que exista um DTO para itens

    public OrcamentoDTO() {}

    public OrcamentoDTO(Long id, String numeroOrcamento, String nomeFornecedor, LocalDate dataCotacao,
                        BigDecimal valorTotal, String status, Long solicitacaoCompraId, List<ItemOrcamentoDTO> itens) {
        this.id = id;
        this.numeroOrcamento = numeroOrcamento;
        this.nomeFornecedor = nomeFornecedor;
        this.dataCotacao = dataCotacao;
        this.valorTotal = valorTotal;
        this.status = status;
        this.solicitacaoCompraId = solicitacaoCompraId;
        this.itens = itens;
    }

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroOrcamento() { return numeroOrcamento; }
    public void setNumeroOrcamento(String numeroOrcamento) { this.numeroOrcamento = numeroOrcamento; }

    public String getNomeFornecedor() { return nomeFornecedor; }
    public void setNomeFornecedor(String nomeFornecedor) { this.nomeFornecedor = nomeFornecedor; }

    public LocalDate getDataCotacao() { return dataCotacao; }
    public void setDataCotacao(LocalDate dataCotacao) { this.dataCotacao = dataCotacao; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getSolicitacaoCompraId() { return solicitacaoCompraId; }
    public void setSolicitacaoCompraId(Long solicitacaoCompraId) { this.solicitacaoCompraId = solicitacaoCompraId; }

    public List<ItemOrcamentoDTO> getItens() { return itens; }
    public void setItens(List<ItemOrcamentoDTO> itens) { this.itens = itens; }

    public Object getFornecedorId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterable<ItemOrcamentoDTO> getItensOrcamento() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
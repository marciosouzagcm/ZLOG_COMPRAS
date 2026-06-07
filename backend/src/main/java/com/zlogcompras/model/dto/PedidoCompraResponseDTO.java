package com.zlogcompras.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zlogcompras.model.StatusPedidoCompra; // Importe o enum
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class PedidoCompraResponseDTO {
    private Long id;
    private Long orcamentoAprovadoId; // ID do orçamento que gerou este pedido
    private Long fornecedorId;
    private String nomeFornecedor;
    private StatusPedidoCompra status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataPedido;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal valorTotal;
    private String condicoesPagamento;
    private String prazoEntrega;
    private String observacoes;
    private List<ItemPedidoCompraResponseDTO> itensPedido; // Lista de itens do pedido
    private Long version;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCadastro;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataAtualizacao;

    // Construtor padrão
    public PedidoCompraResponseDTO() {}

    // Construtor completo (opcional, pode ser gerado pela IDE)
    // ...

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrcamentoAprovadoId() { return orcamentoAprovadoId; }
    public void setOrcamentoAprovadoId(Long orcamentoAprovadoId) { this.orcamentoAprovadoId = orcamentoAprovadoId; }
    public Long getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Long fornecedorId) { this.fornecedorId = fornecedorId; }
    public String getNomeFornecedor() { return nomeFornecedor; }
    public void setNomeFornecedor(String nomeFornecedor) { this.nomeFornecedor = nomeFornecedor; }
    public StatusPedidoCompra getStatus() { return status; }
    public void setStatus(StatusPedidoCompra status) { this.status = status; }
    public LocalDate getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDate dataPedido) { this.dataPedido = dataPedido; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public String getCondicoesPagamento() { return condicoesPagamento; }
    public void setCondicoesPagamento(String condicoesPagamento) { this.condicoesPagamento = condicoesPagamento; }
    public String getPrazoEntrega() { return prazoEntrega; }
    public void setPrazoEntrega(String prazoEntrega) { this.prazoEntrega = prazoEntrega; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public List<ItemPedidoCompraResponseDTO> getItensPedido() { return itensPedido; }
    public void setItensPedido(List<ItemPedidoCompraResponseDTO> itensPedido) { this.itensPedido = itensPedido; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    // Implemente equals, hashCode e toString se desejar
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoCompraResponseDTO that = (PedidoCompraResponseDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
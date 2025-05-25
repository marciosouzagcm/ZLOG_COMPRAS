package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects; // Importar Objects se não estiver usando Lombok para equals/hashCode

// Removendo Lombok novamente para evitar problemas de compatibilidade e usar getters/setters manuais
// import lombok.Getter;
// import lombok.Setter;
// import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;
// import lombok.Builder;

// Se você optou por não usar Lombok, remova as anotações
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
public class OrcamentoResponseDTO {

    private Long id;
    private Long solicitacaoCompraId;
    private String descricaoSolicitacaoCompra;
    // Removendo fornecedorId, pois nomeFornecedor e cnpjFornecedor já dão contexto.
    // Se for absolutamente necessário no DTO de resposta, adicione de volta.
    // private Long fornecedorId;
    private String nomeFornecedor;
    private String cnpjFornecedor;
    private LocalDate dataCotacao;
    private String numeroOrcamento;
    private String status;
    private BigDecimal valorTotal;
    private String observacoes; // Mantido, se existir na entidade e for mapeado
    private String condicoesPagamento;
    private String prazoEntrega;
    private Long version;
    private List<ItemOrcamentoResponseDTO> itensOrcamento;

    // Construtor padrão (necessário se não usar @NoArgsConstructor do Lombok)
    public OrcamentoResponseDTO() {
    }

    // Construtor completo (opcional, mas bom para testes e inicialização manual)
    public OrcamentoResponseDTO(Long id, Long solicitacaoCompraId, String descricaoSolicitacaoCompra,
                               String nomeFornecedor, String cnpjFornecedor, LocalDate dataCotacao,
                               String numeroOrcamento, String status, BigDecimal valorTotal,
                               String observacoes, String condicoesPagamento, String prazoEntrega,
                               Long version, List<ItemOrcamentoResponseDTO> itensOrcamento) {
        this.id = id;
        this.solicitacaoCompraId = solicitacaoCompraId;
        this.descricaoSolicitacaoCompra = descricaoSolicitacaoCompra;
        this.nomeFornecedor = nomeFornecedor;
        this.cnpjFornecedor = cnpjFornecedor;
        this.dataCotacao = dataCotacao;
        this.numeroOrcamento = numeroOrcamento;
        this.status = status;
        this.valorTotal = valorTotal;
        this.observacoes = observacoes;
        this.condicoesPagamento = condicoesPagamento;
        this.prazoEntrega = prazoEntrega;
        this.version = version;
        this.itensOrcamento = itensOrcamento;
    }


    // --- Getters e Setters para todos os campos (removidos para brevidade, mas devem estar presentes) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSolicitacaoCompraId() { return solicitacaoCompraId; }
    public void setSolicitacaoCompraId(Long solicitacaoCompraId) { this.solicitacaoCompraId = solicitacaoCompraId; }

    public String getDescricaoSolicitacaoCompra() { return descricaoSolicitacaoCompra; }
    public void setDescricaoSolicitacaoCompra(String descricaoSolicitacaoCompra) { this.descricaoSolicitacaoCompra = descricaoSolicitacaoCompra; }

    // Getter/Setter de fornecedorId seria removido se a decisão for mantê-lo fora do DTO.
    // public Long getFornecedorId() { return fornecedorId; }
    // public void setFornecedorId(Long fornecedorId) { this.fornecedorId = fornecedorId; }

    public String getNomeFornecedor() { return nomeFornecedor; }
    public void setNomeFornecedor(String nomeFornecedor) { this.nomeFornecedor = nomeFornecedor; }

    public String getCnpjFornecedor() { return cnpjFornecedor; }
    public void setCnpjFornecedor(String cnpjFornecedor) { this.cnpjFornecedor = cnpjFornecedor; }

    public LocalDate getDataCotacao() { return dataCotacao; }
    public void setDataCotacao(LocalDate dataCotacao) { this.dataCotacao = dataCotacao; }

    public String getNumeroOrcamento() { return numeroOrcamento; }
    public void setNumeroOrcamento(String numeroOrcamento) { this.numeroOrcamento = numeroOrcamento; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getCondicoesPagamento() { return condicoesPagamento; }
    public void setCondicoesPagamento(String condicoesPagamento) { this.condicoesPagamento = condicoesPagamento; }

    public String getPrazoEntrega() { return prazoEntrega; }
    public void setPrazoEntrega(String prazoEntrega) { this.prazoEntrega = prazoEntrega; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public List<ItemOrcamentoResponseDTO> getItensOrcamento() { return itensOrcamento; }
    public void setItensOrcamento(List<ItemOrcamentoResponseDTO> itensOrcamento) { this.itensOrcamento = itensOrcamento; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrcamentoResponseDTO that = (OrcamentoResponseDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrcamentoResponseDTO{" +
                "id=" + id +
                ", solicitacaoCompraId=" + solicitacaoCompraId +
                ", nomeFornecedor='" + nomeFornecedor + '\'' +
                ", status='" + status + '\'' +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
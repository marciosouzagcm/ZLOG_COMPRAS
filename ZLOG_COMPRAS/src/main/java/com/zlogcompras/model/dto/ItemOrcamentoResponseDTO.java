package com.zlogcompras.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat; // Importe esta classe
import java.math.BigDecimal;
import java.util.Objects;

public class ItemOrcamentoResponseDTO {

    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private String codigoProduto;
    private String unidadeMedidaProduto;

    // Campos BigDecimal que devem ter duas casas decimais no JSON
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal quantidade;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal precoUnitarioCotado;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal subtotal;

    private String observacoes;
    private Long version;

    // Construtor padrão
    public ItemOrcamentoResponseDTO() {
    }

    // Construtor completo
    public ItemOrcamentoResponseDTO(Long id, Long produtoId, String nomeProduto, String codigoProduto,
                                    String unidadeMedidaProduto, BigDecimal quantidade,
                                    BigDecimal precoUnitarioCotado, BigDecimal subtotal, String observacoes, Long version) {
        this.id = id;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.codigoProduto = codigoProduto;
        this.unidadeMedidaProduto = unidadeMedidaProduto;
        this.quantidade = quantidade;
        this.precoUnitarioCotado = precoUnitarioCotado;
        this.subtotal = subtotal;
        this.observacoes = observacoes;
        this.version = version;
    }

    // --- Getters e Setters (inalterados, pois a formatação é feita na serialização) ---
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

    public String getUnidadeMedidaProduto() {
        return unidadeMedidaProduto;
    }

    public void setUnidadeMedidaProduto(String unidadeMedidaProduto) {
        this.unidadeMedidaProduto = unidadeMedidaProduto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitarioCotado() {
        return precoUnitarioCotado;
    }

    public void setPrecoUnitarioCotado(BigDecimal precoUnitarioCotado) {
        this.precoUnitarioCotado = precoUnitarioCotado;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemOrcamentoResponseDTO that = (ItemOrcamentoResponseDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ItemOrcamentoResponseDTO{" +
                "id=" + id +
                ", produtoId=" + produtoId +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", quantidade=" + quantidade +
                ", precoUnitarioCotado=" + precoUnitarioCotado +
                '}';
    }
}
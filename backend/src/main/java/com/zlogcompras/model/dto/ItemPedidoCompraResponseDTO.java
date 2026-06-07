package com.zlogcompras.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO de Saída utilizado para estruturar a listagem de itens de um Pedido de Compra.
 * 
 * Mesmo sem os campos redundantes armazenados na tabela do banco, este objeto projeta 
 * os dados agregados do Produto associado para simplificar a renderização no frontend.
 */
public class ItemPedidoCompraResponseDTO {

    private Long id;
    private Long produtoId;
    
    // Propriedades resolvidas via relacionamento relacional no Mapper
    private String nomeProduto;
    private String codigoProduto;
    private String unidadeMedida; // CORRIGIDO: de unitadeMedida para unidadeMedida
    
    // Formatação adjusted com 4 casas para preservar a precisão decimal de suprimentos
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0000")
    private BigDecimal quantidade;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0000")
    private BigDecimal precoUnitario;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0000")
    private BigDecimal subtotal;
    
    private String observacoes;
    private Long version;

    /**
     * CONSTRUTORES
     */
    public ItemPedidoCompraResponseDTO() {
    }

    public ItemPedidoCompraResponseDTO(Long id, Long produtoId, String nomeProduto, String codigoProduto, 
                                       String unidadeMedida, BigDecimal quantidade, BigDecimal precoUnitario, 
                                       BigDecimal subtotal, String observacoes, Long version) { // CORRIGIDO: unidadeMedida
        this.id = id;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.codigoProduto = codigoProduto;
        this.unidadeMedida = unidadeMedida; // CORRIGIDO
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = subtotal;
        this.observacoes = observacoes;
        this.version = version;
    }

    /**
     * GETTERS E SETTERS
     */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public String getCodigoProduto() { return codigoProduto; }
    public void setCodigoProduto(String codigoProduto) { this.codigoProduto = codigoProduto; }

    public String getUnidadeMedida() { return unidadeMedida; } // CORRIGIDO
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; } // CORRIGIDO

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    /**
     * IDENTIDADE E EQUALS
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedidoCompraResponseDTO that = (ItemPedidoCompraResponseDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ItemPedidoCompraResponseDTO{" +
                "id=" + id +
                ", produtoId=" + produtoId +
                ", codigoProduto='" + codigoProduto + '\'' +
                ", subtotal=" + subtotal +
                '}';
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.zlogcompras.model.dto;

import java.math.BigDecimal;

public class ItemOrcamentoDTO {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private BigDecimal precoUnitario;
    private Integer quantidade;

    public ItemOrcamentoDTO() {}

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}

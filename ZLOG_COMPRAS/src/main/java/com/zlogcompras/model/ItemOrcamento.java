package com.zlogcompras.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class ItemOrcamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Integer quantidade;
    private BigDecimal precoUnitario;

    @ManyToOne
    @JoinColumn(name = "orcamento_id")
    private Orcamento orcamento;

    // getters e setters

    public Object getPrecoUnitario() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getQuantidade() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getMaterialServico() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
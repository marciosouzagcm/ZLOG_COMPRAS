package com.zlogcompras.model;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidade JPA que representa a tabela 'itens_pedido_compra'.
 * Mapeamento refatorado para remover snapshots redundantes em formato String,
 * unificar a precisão aritmética de quantidades e valores para DECIMAL(15,4)
 * e otimizar as estratégias de carregamento (FetchType.LAZY).
 */
@Entity
@Table(name = "itens_pedido_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
@ToString(exclude = {"pedidoCompra"})
public class ItemPedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com o cabeçalho do pedido de compra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_compra_id", nullable = false)
    @JsonBackReference
    private PedidoCompra pedidoCompra;

    // Relacionamento centralizado com o cadastro de produtos (Substitui as colunas String redundantes)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // CORREÇÃO: Modificado de Integer para BigDecimal para suportar fracionamentos e unificar mappers
    @Column(name = "quantidade", nullable = false, precision = 15, scale = 4)
    private BigDecimal quantidade;

    // Precisão estendida de 4 casas decimais em conformidade com o DDL V1__init.sql
    @Column(name = "preco_unitario", nullable = false, precision = 15, scale = 4)
    private BigDecimal precoUnitario;

    @Column(name = "subtotal", nullable = false, precision = 15, scale = 4)
    private BigDecimal subtotal;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
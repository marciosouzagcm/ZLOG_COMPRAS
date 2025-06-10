package com.zlogcompras.model;

import java.math.BigDecimal;
import java.util.Objects;

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
import lombok.Data; // Inclui @Getter, @Setter, @ToString, @EqualsAndHashCode
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "itens_pedido_compra")
@Data // ESSENCIAL: Garante Getters e Setters para 'subtotal'
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
@ToString(exclude = {"pedidoCompra"})
public class ItemPedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_compra_id", nullable = false)
    @JsonBackReference
    private PedidoCompra pedidoCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade; // Mantemos como Integer

    @Column(name = "preco_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitario;

    // *** CERTIFIQUE-SE DE QUE ESTE CAMPO ESTÁ PRESENTE! ***
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "observacoes", length = 500)
    private String observacoes;
    
    @Column(name = "nome_produto", length = 255)
    private String nomeProduto;

    @Column(name = "codigo_produto", length = 100)
    private String codigoProduto;

    @Column(name = "unidade_medida", length = 50)
    private String unidadeMedida;

    @Version
    private Long version;

    // Se você estiver usando Lombok e a anotação @Data, não precisa escrever
    // manualmente os getters e setters para 'subtotal'. Eles serão gerados.
}
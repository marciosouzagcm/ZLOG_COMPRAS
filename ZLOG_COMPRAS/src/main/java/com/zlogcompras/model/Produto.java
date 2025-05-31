package com.zlogcompras.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.LocalDateTime; // Importar LocalDateTime
import java.math.BigDecimal; // Importar BigDecimal

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;

@Entity
@Table(name = "produtos")
@EntityListeners(AuditingEntityListener.class) // Se você está usando auditoria
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mapeia o campo 'codigoProduto' da entidade para a coluna 'codigo_produto' no banco de dados
    @Column(name = "codigo_produto", nullable = false, unique = true, length = 50)
    private String codigoProduto; // Nome do campo na entidade

    private String nome;
    private String descricao;

    @Column(name = "unidade_medida", nullable = false)
    private String unidadeMedida;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "categoria", nullable = false, length = 50) // <--- NOVO CAMPO E MAPEAMENTO
    private String categoria;

    @Column(name = "estoque", nullable = false) // Se você ainda tem estoque no DB
    private Integer estoque;

    @Version
    private Long version;

    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    // Construtor padrão
    public Produto() {
    }

    // Construtor completo (ajuste conforme seus campos)
    public Produto(String codigoProduto, String nome, String descricao, String unidadeMedida,
                   BigDecimal precoUnitario, String categoria, Integer estoque) {
        this.codigoProduto = codigoProduto;
        this.nome = nome;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
        this.precoUnitario = precoUnitario;
        this.categoria = categoria;
        this.estoque = estoque;
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoProduto() { return codigoProduto; }
    public void setCodigoProduto(String codigoProduto) { this.codigoProduto = codigoProduto; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public String getCategoria() { return categoria; } // <--- NOVO GETTER
    public void setCategoria(String categoria) { this.categoria = categoria; } // <--- NOVO SETTER

    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }

    // Métodos de auditoria (se estiver usando @EntityListeners(AuditingEntityListener.class))
    @jakarta.persistence.PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    @jakarta.persistence.PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    public String getCodigo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
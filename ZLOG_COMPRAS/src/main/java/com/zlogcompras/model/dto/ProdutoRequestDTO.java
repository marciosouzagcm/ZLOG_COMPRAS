package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) utilizado para capturar e validar as requisições
 * de criação e atualização de produtos no sistema ZLOG Compras.
 * 
 * Adequado estruturalmente para corresponder ao script de migração V1__init.sql,
 * removendo redundâncias de códigos e isolando a volumetria de estoque.
 */
public class ProdutoRequestDTO {

    // Identificador alfanumérico universal e único do produto (ex: SKUs, Part Numbers)
    @NotBlank(message = "O código é obrigatório")
    @Size(max = 50, message = "O código não pode exceder 50 caracteres")
    private String codigo;

    // Nome descritivo comercial do material ou serviço
    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(max = 255, message = "O nome do produto não pode exceder 255 caracteres")
    private String nome;

    // Detalhes técnicos adicionais sobre as especificações do item
    @Size(max = 500, message = "A descrição do produto não pode exceder 500 caracteres")
    private String descricao;

    // Unidade de estocagem e compras (ex: UN, KG, PCT, CX)
    @NotBlank(message = "A unidade de medida é obrigatória")
    @Size(max = 20, message = "A unidade de medida não pode exceder 20 caracteres")
    private String unidadeMedida;

    // Preço de tabela ou última compra histórica do produto
    @NotNull(message = "O preço unitário é obrigatório")
    @PositiveOrZero(message = "O preço unitário deve ser um valor positivo ou zero")
    private BigDecimal precoUnitario;

    // Agrupamento lógico de suprimentos (ex: ELETRONICOS, ESCRITORIO, FERRAMENTAS)
    @NotBlank(message = "A categoria do produto é obrigatória")
    @Size(max = 50, message = "A categoria do produto não pode exceder 50 caracteres")
    private String categoria;

    /**
     * CONSTRUTORES
     */
    
    // Construtor padrão necessário para serialização/deserialização do Jackson (JSON)
    public ProdutoRequestDTO() {
    }

    // Construtor completo para simplificar a instanciação em cenários de testes unitários
    public ProdutoRequestDTO(String codigo, String nome, String descricao, String unidadeMedida, 
                             BigDecimal precoUnitario, String categoria) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
        this.precoUnitario = precoUnitario;
        this.categoria = categoria;
    }

    /**
     * GETTERS E SETTERS ENCAPSULADOS
     */

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
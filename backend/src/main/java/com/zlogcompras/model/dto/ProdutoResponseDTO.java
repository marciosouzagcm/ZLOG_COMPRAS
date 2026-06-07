package com.zlogcompras.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) de Saída utilizado para expor os dados de um produto
 * nas respostas das APIs do sistema ZLOG Compras.
 * 
 * Totalmente alinhado à entidade Produto e livre de redundâncias de código,
 * garantindo o mapeamento direto e performático via MapStruct.
 */
public class ProdutoResponseDTO {

    private Long id;
    
    // Alinhado com a coluna única e universal 'codigo' do banco e da entidade
    private String codigo;
    
    private String nome;
    private String descricao;
    private String unidadeMedida;
    private BigDecimal precoUnitario;
    
    // Modificado para LocalDateTime para refletir fielmente o carimbo de tempo completo da auditoria
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    private Long version;

    /**
     * CONSTRUTORES
     */
    
    // Construtor padrão obrigatório para deserializadores
    public ProdutoResponseDTO() {
    }

    // Construtor completo útil para mapeamentos manuais e estruturas de testes
    public ProdutoResponseDTO(Long id, String codigo, String nome, String descricao, String unidadeMedida,
                              BigDecimal precoUnitario, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, Long version) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
        this.precoUnitario = precoUnitario;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.version = version;
    }

    /**
     * GETTERS E SETTERS ENCAPSULADOS
     */

    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

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

    public LocalDateTime getDataCriacao() { 
        return dataCriacao; 
    }

    public void setDataCriacao(LocalDateTime dataCriacao) { 
        this.dataCriacao = dataCriacao; 
    }

    public LocalDateTime getDataAtualizacao() { 
        return dataAtualizacao; 
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { 
        this.dataAtualizacao = dataAtualizacao; 
    }

    public Long getVersion() { 
        return version; 
    }

    public void setVersion(Long version) { 
        this.version = version; 
    }
}
package com.zlogcompras.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.zlogcompras.model.Produto;
import com.zlogcompras.model.dto.ProdutoRequestDTO;
import com.zlogcompras.model.dto.ProdutoResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProdutoMapper {

    // Este método estático não é normalmente usado diretamente pelo MapStruct para mapeamento de campos
    // Ele seria usado se você especificasse um "expression" ou "qualifiedByName" em um @Mapping
    // Se não há um uso específico para ele, pode ser removido ou movido para uma classe de utilidade.
    // public static String getCodigo(String codigo) {
    //     return codigo;
    // }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "version", ignore = true)
    // Mapeamento correto: DTO.nome -> Entity.nome
    @Mapping(source = "nome", target = "nome")
    // Mapeamento correto: DTO.codigoProduto -> Entity.codigoProduto
    @Mapping(source = "codigoProduto", target = "codigoProduto")
    @Mapping(source = "categoria", target = "categoria") // Assumindo que Produto.categoria existe
    Produto toEntity(ProdutoRequestDTO dto);

    // No toResponseDto, se o campo na entidade for "codigoProduto", não precisa de @Mapping
    // se o nome do campo no DTO for o mesmo. Se for diferente, aí sim.
    // Ex: Produto.codigoProduto -> ProdutoResponseDTO.codigoProduto
    @Mapping(source = "codigoProduto", target = "codigoProduto") // Pode ser redundante se os nomes são iguais
    ProdutoResponseDTO toResponseDto(Produto entity);

    @Mapping(target = "id", ignore = true) // ID não deve ser atualizado
    @Mapping(target = "dataCriacao", ignore = true) // Data de criação não deve ser atualizada
    @Mapping(target = "dataAtualizacao", ignore = true) // Data de atualização será gerenciada automaticamente (JPA)
    @Mapping(target = "version", ignore = true) // Versão para concorrência será gerenciada pelo JPA
    // Mapeamento correto: DTO.nome -> Entity.nome
    @Mapping(source = "nome", target = "nome")
    // Mapeamento correto: DTO.codigoProduto -> Entity.codigoProduto
    @Mapping(source = "codigoProduto", target = "codigoProduto")
    @Mapping(source = "categoria", target = "categoria") // Assumindo que Produto.categoria existe
    void updateEntityFromDto(ProdutoRequestDTO dto, @MappingTarget Produto entity);
    
}
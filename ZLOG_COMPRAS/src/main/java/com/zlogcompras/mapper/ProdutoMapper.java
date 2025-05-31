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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "version", ignore = true)
    // Mapeia codigoProduto do DTO para o campo 'codigoProduto' da entidade
    @Mapping(source = "codigoProduto", target = "codigoProduto") // <--- ATENÇÃO AQUI!
    @Mapping(source = "categoria", target = "categoria")
    Produto toEntity(ProdutoRequestDTO dto);

    // Mapeia codigoProduto da entidade para o DTO de resposta
    @Mapping(source = "codigoProduto", target = "codigoProduto") // <--- ATENÇÃO AQUI!
    ProdutoResponseDTO toResponseDto(Produto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "version", ignore = true)
    // Mapeia codigoProduto do DTO para o campo 'codigoProduto' da entidade
    @Mapping(source = "codigoProduto", target = "codigoProduto") // <--- ATENÇÃO AQUI!
    @Mapping(source = "categoria", target = "categoria")
    void updateEntityFromDto(ProdutoRequestDTO dto, @MappingTarget Produto entity);
}
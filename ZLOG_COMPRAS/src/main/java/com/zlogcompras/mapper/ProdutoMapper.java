package com.zlogcompras.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy; // Importar ReportingPolicy

import com.zlogcompras.model.Produto;
import com.zlogcompras.model.dto.ProdutoRequestDTO;
import com.zlogcompras.model.dto.ProdutoResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN) // Adicionado ReportingPolicy.WARN
public interface ProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "version", ignore = true)
    // Se o DTO de requisição não tiver codigoProduto, e ele não deve ser mapeado diretamente:
    // @Mapping(target = "codigoProduto", ignore = true)
    Produto toEntity(ProdutoRequestDTO dto);

    @Mapping(target = "codigoProduto", source = "codigoProduto") // Mapeia codigoProduto da entidade para o DTO
    ProdutoResponseDTO toResponseDto(Produto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "version", ignore = true)
    // Se o DTO de requisição não tiver codigoProduto, e ele não deve ser mapeado diretamente:
    // @Mapping(target = "codigoProduto", ignore = true)
    void updateEntityFromDto(ProdutoRequestDTO dto, @MappingTarget Produto entity);
}
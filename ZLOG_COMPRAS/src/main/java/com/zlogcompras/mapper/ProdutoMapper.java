package com.zlogcompras.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.zlogcompras.model.Produto;
import com.zlogcompras.model.dto.ProdutoRequestDTO;
import com.zlogcompras.model.dto.ProdutoResponseDTO;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "version", ignore = true) // Ignorar a propriedade 'version' ao mapear do DTO para a entidade
    Produto toEntity(ProdutoRequestDTO dto);

    ProdutoResponseDTO toResponseDto(Produto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "version", ignore = true) // Ignorar a propriedade 'version' ao atualizar a entidade
    void updateEntityFromDto(ProdutoRequestDTO dto, @MappingTarget Produto entity);
}

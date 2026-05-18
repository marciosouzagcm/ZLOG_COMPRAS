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
    // Mapeamento correto: DTO.codigo -> Entity.codigo
    @Mapping(source = "codigo", target = "codigo")
    Produto toEntity(ProdutoRequestDTO dto);

    // Mapeamento automático por nomes de propriedades idênticos (nome, codigo, categoria, etc.)
    ProdutoResponseDTO toResponseDto(Produto entity);

    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "dataCriacao", ignore = true) 
    @Mapping(target = "dataAtualizacao", ignore = true) 
    @Mapping(target = "version", ignore = true) 
    // Mapeamento correto: DTO.codigo -> Entity.codigo
    @Mapping(source = "codigo", target = "codigo")
    void updateEntityFromDto(ProdutoRequestDTO dto, @MappingTarget Produto entity);
    
}
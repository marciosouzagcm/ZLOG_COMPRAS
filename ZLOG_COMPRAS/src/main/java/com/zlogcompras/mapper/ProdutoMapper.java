package com.zlogcompras.mapper;

import com.zlogcompras.model.Produto;
import com.zlogcompras.model.dto.ProdutoRequestDTO;
import com.zlogcompras.model.dto.ProdutoResponseDTO;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProdutoMapper {

    // Se o 'id' é gerado automaticamente pelo banco de dados (e.g., @GeneratedValue),
    // você deve ignorá-lo ao mapear do DTO de requisição para a entidade.
    @Mapping(target = "id", ignore = true) // <<< Mantido para ignorar o ID se ele for auto-gerado
    Produto toEntity(ProdutoRequestDTO dto);

    // Se você tem um método de atualização, considere ignorar o ID também
    // @Mapping(target = "id", ignore = true)
    // void updateEntityFromDto(ProdutoRequestDTO dto, @MappingTarget Produto entity);

    // Mapeamento para o DTO de resposta
    ProdutoResponseDTO toResponseDto(Produto entity);

    List<ProdutoResponseDTO> toResponseDtoList(List<Produto> produtos);

}
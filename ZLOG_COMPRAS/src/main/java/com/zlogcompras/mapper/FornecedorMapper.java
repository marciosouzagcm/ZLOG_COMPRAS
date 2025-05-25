package com.zlogcompras.mapper;

import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.dto.FornecedorRequestDTO;
import com.zlogcompras.model.dto.FornecedorResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FornecedorMapper {

    Fornecedor toEntity(FornecedorRequestDTO dto);

    FornecedorResponseDTO toResponseDto(Fornecedor entity);

    // Método para atualizar uma entidade existente a partir de um DTO
    @Mapping(target = "id", ignore = true) // O ID não deve ser atualizado pelo DTO
    void updateEntityFromDto(FornecedorRequestDTO dto, @MappingTarget Fornecedor entity);

    // NOVO MÉTODO: Mapeia uma lista de Fornecedor para uma lista de FornecedorResponseDTO
    List<FornecedorResponseDTO> toListaDtoList(List<Fornecedor> entities);
}
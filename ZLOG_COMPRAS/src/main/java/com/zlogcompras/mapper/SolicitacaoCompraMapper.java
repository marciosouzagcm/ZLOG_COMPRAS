package com.zlogcompras.mapper;

import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.dto.SolicitacaoCompraRequestDTO;
import com.zlogcompras.model.dto.SolicitacaoCompraResponseDTO;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SolicitacaoCompraMapper {

    // Se 'descricao' não faz parte do seu DTO de request e é intencional, ignore-a
    @Mapping(target = "descricao", ignore = true) // <<< Mantido para ignorar a warning
    SolicitacaoCompra toEntity(SolicitacaoCompraRequestDTO dto);

    // Se 'descricao' não faz parte do seu DTO de request para atualização e é intencional, ignore-a
    @Mapping(target = "descricao", ignore = true) // <<< Mantido para ignorar a warning
    void updateEntityFromDto(SolicitacaoCompraRequestDTO dto, @MappingTarget SolicitacaoCompra entity);

    SolicitacaoCompraResponseDTO toResponseDto(SolicitacaoCompra entity);

    List<SolicitacaoCompraResponseDTO> toResponseDtoList(List<SolicitacaoCompra> solicitacoes);
}
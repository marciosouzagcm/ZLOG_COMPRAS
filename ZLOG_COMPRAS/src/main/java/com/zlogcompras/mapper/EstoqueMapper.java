package com.zlogcompras.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy; // Importar ReportingPolicy

import com.zlogcompras.model.Estoque;
import com.zlogcompras.model.dto.EstoqueRequestDTO;
import com.zlogcompras.model.dto.EstoqueResponseDTO;

@Mapper(componentModel = "spring", uses = { ProdutoMapper.class }, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface EstoqueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true) // Será setado no serviço
    @Mapping(target = "version", ignore = true) // RE-ADICIONADO
    @Mapping(target = "dataUltimaEntrada", ignore = true)
    @Mapping(target = "dataUltimaSaida", ignore = true)
    Estoque toEntity(EstoqueRequestDTO dto);

    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "nomeProduto", source = "produto.nome")
    @Mapping(target = "codigoProduto", source = "produto.codigoProduto") // CORRIGIDO: produto.codigo para produto.codigoProduto
    EstoqueResponseDTO toResponseDto(Estoque entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true) // Não atualiza o produto diretamente pelo mapper
    @Mapping(target = "version", ignore = true) // RE-ADICIONADO
    @Mapping(target = "dataUltimaEntrada", ignore = true)
    @Mapping(target = "dataUltimaSaida", ignore = true)
    void updateEntityFromDto(EstoqueRequestDTO dto, @MappingTarget Estoque entity);
}
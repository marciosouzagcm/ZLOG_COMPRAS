package com.zlogcompras.mapper;

import com.zlogcompras.model.Estoque;
import com.zlogcompras.model.dto.EstoqueRequestDTO;
import com.zlogcompras.model.dto.EstoqueResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ProdutoMapper.class})
public interface EstoqueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true) // Será setado no serviço
    @Mapping(target = "version", ignore = true)
    Estoque toEntity(EstoqueRequestDTO dto);

    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "nomeProduto", source = "produto.nome")
    @Mapping(target = "codigoProduto", source = "produto.codigo")
    EstoqueResponseDTO toResponseDto(Estoque entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true) // Não atualiza o produto diretamente pelo mapper
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(EstoqueRequestDTO dto, @MappingTarget Estoque entity);
}
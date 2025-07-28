package com.zlogcompras.mapper;

import java.util.List; // Usar List, já que seu DTO de resposta tem List

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.dto.ItemSolicitacaoCompraResponseDTO;
import com.zlogcompras.model.dto.SolicitacaoCompraResponseDTO;

@Mapper(componentModel = "spring")
public interface SolicitacaoMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "solicitacaoCompraId", source = "id") // Mapeando o ID da entidade para este campo
    @Mapping(target = "dataSolicitacao", source = "dataSolicitacao")
    @Mapping(target = "solicitante", source = "solicitante")
    @Mapping(target = "status", expression = "java(solicitacao.getStatus().name())") // Converte o ENUM para String
    @Mapping(target = "descricaoSolicitacaoCompra", source = "descricao") // CORRIGIDO: Mapeando 'descricao' da entidade
                                                                          // para 'descricaoSolicitacaoCompra' no DTO
    @Mapping(target = "version", source = "version")
    @Mapping(target = "dataCriacao", source = "dataCriacao") // CORRIGIDO: Mapeando para o campo dataCriacao no DTO
    @Mapping(target = "dataAtualizacao", source = "dataAtualizacao") // CORRIGIDO: Mapeando para o campo dataAtualizacao
                                                                     // no DTO
    @Mapping(target = "itens", source = "itens", qualifiedByName = "mapItemSolicitacaoSet") // Mapeia a coleção de itens
    SolicitacaoCompraResponseDTO toResponseDto(SolicitacaoCompra solicitacao);

    @Named("mapItemSolicitacaoSet") // O nome do método @Named deve ser o mesmo que em 'qualifiedByName'
    default List<ItemSolicitacaoCompraResponseDTO> mapItemSolicitacaoSet(java.util.Set<ItemSolicitacaoCompra> itens) {
        if (itens == null) {
            return null;
        }
        return itens.stream()
                .map(this::toItemSolicitacaoCompraResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    // Este método é essencial para mapear cada ItemSolicitacaoCompra para seu DTO
    // correspondente.
    // Se ItemSolicitacaoCompraResponseDTO tiver campos com nomes diferentes de
    // ItemSolicitacaoCompra,
    // você precisará adicionar @Mapping aqui também.
    ItemSolicitacaoCompraResponseDTO toItemSolicitacaoCompraResponseDTO(ItemSolicitacaoCompra item);

    // Se você tiver um DTO de requisição, adicione mapeamentos de DTO para Entidade
    // aqui
    // Exemplo: SolicitacaoCompra toEntity(SolicitacaoCompraRequestDTO dto);
}
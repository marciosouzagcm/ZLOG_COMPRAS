package com.zlogcompras.mapper;

import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusSolicitacaoCompra;
import com.zlogcompras.model.StatusItemSolicitacao;
import com.zlogcompras.model.dto.ItemSolicitacaoCompraRequestDTO;
import com.zlogcompras.model.dto.SolicitacaoCompraRequestDTO;
import com.zlogcompras.model.dto.SolicitacaoCompraResponseDTO;
import com.zlogcompras.model.dto.ItemSolicitacaoCompraResponseDTO; // Import adicionado
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set; // Import para Set

@Mapper(componentModel = "spring", uses = {ProdutoMapper.class})
public interface SolicitacaoCompraMapper {

    // Mapeia SolicitacaoCompraRequestDTO para SolicitacaoCompra (entidade)
    // Ignora campos que são gerados pelo banco de dados ou gerenciados por JPA callbacks
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "orcamentos", ignore = true) // Ignorar orçamentos na criação/atualização da solicitação
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStringToStatusSolicitacaoCompra")
    @Mapping(target = "itens", source = "itens", qualifiedByName = "mapItemSolicitacaoCompraRequestDtoListToEntitySet")
    SolicitacaoCompra toEntity(SolicitacaoCompraRequestDTO dto);

    // Mapeia SolicitacaoCompra (entidade) para SolicitacaoCompraResponseDTO
    // Ajusta os mapeamentos para acessar propriedades diretamente da entidade SolicitacaoCompra
    @Mapping(target = "solicitacaoCompraId", source = "id") // Corrigido: source é o ID da própria entidade
    // Removido: nomeFornecedor e cnpjFornecedor - SolicitacaoCompra não possui Fornecedor diretamente.
    // Se precisar desses campos, eles devem vir de Orcamento ou ItemSolicitacaoCompra
    @Mapping(target = "descricaoSolicitacaoCompra", source = "descricao") // Corrigido
    @Mapping(target = "itens", source = "itens", qualifiedByName = "mapItemSolicitacaoCompraSetToResponseDtoList")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatusSolicitacaoCompraToString")
    SolicitacaoCompraResponseDTO toResponseDto(SolicitacaoCompra entity);

    // Mapeia ItemSolicitacaoCompraRequestDTO para ItemSolicitacaoCompra (entidade)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "solicitacaoCompra", ignore = true) // Será setado manualmente no serviço
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStringToStatusItemSolicitacao")
    ItemSolicitacaoCompra toItemEntity(ItemSolicitacaoCompraRequestDTO dto);

    // Mapeia ItemSolicitacaoCompra (entidade) para ItemSolicitacaoCompraResponseDTO
    // Renomeado para 'toItemResponseDto' e alterado o tipo de retorno para ItemSolicitacaoCompraResponseDTO
    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "nomeProduto", source = "produto.nome")
    @Mapping(target = "codigoProduto", source = "produto.codigo")
    @Mapping(target = "unidadeMedidaProduto", source = "produto.unidadeMedida")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatusItemSolicitacaoToString")
    ItemSolicitacaoCompraResponseDTO toItemResponseDto(ItemSolicitacaoCompra entity);

    // Mapeia SolicitacaoCompraRequestDTO para atualização de SolicitacaoCompra (entidade)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "orcamentos", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStringToStatusSolicitacaoCompra")
    @Mapping(target = "itens", source = "itens", qualifiedByName = "mapItemSolicitacaoCompraRequestDtoListToEntitySet")
    void updateEntityFromDto(SolicitacaoCompraRequestDTO dto, @MappingTarget SolicitacaoCompra entity);

    // --- Métodos de Mapeamento de Coleções ---

    // Mapeia Set<ItemSolicitacaoCompra> para List<ItemSolicitacaoCompraResponseDTO>
    // O nome do método @Named deve corresponder ao qualifiedByName usado no @Mapping
    @Named("mapItemSolicitacaoCompraSetToResponseDtoList")
    List<ItemSolicitacaoCompraResponseDTO> mapItemSolicitacaoCompraSetToResponseDtoList(Set<ItemSolicitacaoCompra> itens);

    // Mapeia List<ItemSolicitacaoCompraRequestDTO> para Set<ItemSolicitacaoCompra>
    // Este método é usado para mapear os itens do DTO de requisição para a entidade SolicitacaoCompra
    @Named("mapItemSolicitacaoCompraRequestDtoListToEntitySet")
    Set<ItemSolicitacaoCompra> mapItemSolicitacaoCompraRequestDtoListToEntitySet(List<ItemSolicitacaoCompraRequestDTO> itemDtos);


    // --- Métodos de mapeamento para Enums ---
    @Named("mapStringToStatusSolicitacaoCompra")
    default StatusSolicitacaoCompra mapStringToStatusSolicitacaoCompra(String status) {
        if (status == null) {
            return null;
        }
        try {
            return StatusSolicitacaoCompra.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status de Solicitação de Compra inválido: " + status);
        }
    }

    @Named("mapStatusSolicitacaoCompraToString")
    default String mapStatusSolicitacaoCompraToString(StatusSolicitacaoCompra status) {
        return status != null ? status.name() : null;
    }

    @Named("mapStringToStatusItemSolicitacao")
    default StatusItemSolicitacao mapStringToStatusItemSolicitacao(String status) {
        if (status == null) {
            return null;
        }
        try {
            return StatusItemSolicitacao.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status de Item de Solicitação inválido: " + status);
        }
    }

    @Named("mapStatusItemSolicitacaoToString")
    default String mapStatusItemSolicitacaoToString(StatusItemSolicitacao status) {
        return status != null ? status.name() : null;
    }
}
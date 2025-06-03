package com.zlogcompras.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusItemSolicitacao;
import com.zlogcompras.model.StatusSolicitacaoCompra;
import com.zlogcompras.model.dto.ItemSolicitacaoCompraRequestDTO;
import com.zlogcompras.model.dto.ItemSolicitacaoCompraResponseDTO;
import com.zlogcompras.model.dto.SolicitacaoCompraRequestDTO;
import com.zlogcompras.model.dto.SolicitacaoCompraResponseDTO;

@Mapper(componentModel = "spring", uses = { ProdutoMapper.class })
public interface SolicitacaoCompraMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "orcamentos", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStringToStatusSolicitacaoCompra")
    @Mapping(target = "itens", ignore = true)
    @Mapping(target = "dataSolicitacao", ignore = true)
    SolicitacaoCompra toEntity(SolicitacaoCompraRequestDTO dto);

    @Mapping(target = "solicitacaoCompraId", source = "id")
    @Mapping(target = "descricaoSolicitacaoCompra", source = "descricao")
    @Mapping(target = "itens", source = "itens", qualifiedByName = "mapItemSolicitacaoCompraSetToResponseDtoList")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatusSolicitacaoCompraToString")
    SolicitacaoCompraResponseDTO toResponseDto(SolicitacaoCompra entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "solicitacaoCompra", ignore = true)
    @Mapping(target = "produto", ignore = true) // Ignorar o mapeamento direto de produto aqui
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStringToStatusItemSolicitacao")
    ItemSolicitacaoCompra toItemEntity(ItemSolicitacaoCompraRequestDTO dto);

    // Mapeamentos diretos para as propriedades do produto no DTO, o MapStruct pode
    // fazer isso se os nomes batem ou com 'source'
    // O AfterMapping é útil para lógicas mais complexas ou para garantir
    // inicialização de proxies Lazy
    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.nome", target = "nomeProduto")
    @Mapping(source = "produto.codigoProduto", target = "codigoProduto") // <-- CORREÇÃO AQUI! DEVE SER codigoProduto
    @Mapping(source = "produto.unidadeMedida", target = "unidadeMedidaProduto")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatusItemSolicitacaoToString")
    ItemSolicitacaoCompraResponseDTO toItemResponseDto(ItemSolicitacaoCompra entity);

    // O método @AfterMapping para preencher detalhes do produto não é mais
    // estritamente necessário
    // se os @Mapping acima já resolvem. No entanto, se houver lógica adicional ou
    // para depuração
    // de proxies Lazy, ele pode ser mantido, mas com a CORREÇÃO do método
    // getCodigoProduto().
    // Se preferir, pode remover o @AfterMapping abaixo e confiar apenas nos
    // @Mapping diretos.
    // Eu vou mantê-lo com a correção para mostrar como seria, mas ele se tornaria
    // redundante
    // se os @Mapping diretos funcionarem perfeitamente.

    // *** REVISADO: Método @AfterMapping para preencher detalhes do produto ***
    // Este método será executado automaticamente pelo MapStruct APÓS o mapeamento
    // inicial
    // de ItemSolicitacaoCompra para ItemSolicitacaoCompraResponseDTO.
    @AfterMapping
    default void mapProdutoDetailsAfter(@MappingTarget ItemSolicitacaoCompraResponseDTO dto,
            ItemSolicitacaoCompra entity) {
        // Garante que o produto existe e não é um proxy não inicializado
        if (entity != null && entity.getProduto() != null) {
            // Acessa os getters do Produto. Se LAZY, a inicialização ocorre aqui dentro da
            // transação.
            // Apenas para ter certeza, caso o MapStruct direto não consiga por algum motivo
            // (ex: proxy não inicializado)
            if (dto.getProdutoId() == null)
                dto.setProdutoId(entity.getProduto().getId());
            if (dto.getNomeProduto() == null)
                dto.setNomeProduto(entity.getProduto().getNome());
            if (dto.getCodigoProduto() == null)
                dto.setCodigoProduto(entity.getProduto().getCodigoProduto()); // <-- CORRIGIDO AQUI!
            if (dto.getUnidadeMedidaProduto() == null)
                dto.setUnidadeMedidaProduto(entity.getProduto().getUnidadeMedida());
        } else {
            // Define valores padrão se o produto for nulo por algum motivo
            dto.setProdutoId(null);
            dto.setNomeProduto("Produto Indisponível");
            dto.setCodigoProduto("N/A");
            dto.setUnidadeMedidaProduto("N/A");
        }
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "orcamentos", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStringToStatusSolicitacaoCompra")
    @Mapping(target = "itens", ignore = true)
    @Mapping(target = "dataSolicitacao", ignore = true)
    void updateEntityFromDto(SolicitacaoCompraRequestDTO dto, @MappingTarget SolicitacaoCompra entity);

    @Named("mapItemSolicitacaoCompraSetToResponseDtoList")
    List<ItemSolicitacaoCompraResponseDTO> mapItemSolicitacaoCompraSetToResponseDtoList(
            Set<ItemSolicitacaoCompra> itens);

    @Named("mapItemSolicitacaoCompraRequestDtoListToEntitySet")
    @Mapping(target = "produto", ignore = true)
    Set<ItemSolicitacaoCompra> mapItemSolicitacaoCompraRequestDtoListToEntitySet(
            List<ItemSolicitacaoCompraRequestDTO> itemDtos);

    // --- Métodos de mapeamento para Enums ---
    @Named("mapStringToStatusSolicitacaoCompra")
    default StatusSolicitacaoCompra mapStringToStatusSolicitacaoCompra(String status) {
        if (status == null)
            return null;
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
        if (status == null)
            return null;
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
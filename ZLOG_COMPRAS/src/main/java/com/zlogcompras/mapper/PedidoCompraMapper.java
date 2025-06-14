package com.zlogcompras.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.zlogcompras.model.ItemPedidoCompra;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.StatusPedidoCompra;
import com.zlogcompras.model.dto.ItemPedidoCompraResponseDTO;
import com.zlogcompras.model.dto.PedidoCompraResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoCompraMapper {

    // Mapeia entidade para DTO de resposta
    // O ID do orçamento aprovado vem do orçamento associado ao pedido.
    @Mapping(target = "orcamentoAprovadoId", source = "orcamento.id") 
    @Mapping(target = "fornecedorId", source = "fornecedor.id")
    @Mapping(target = "nomeFornecedor", source = "fornecedor.razaoSocial") 
    // O status será mapeado automaticamente pelo MapStruct usando o método 'map' padrão ou enum.name()
    // Dependendo de como StatusPedidoCompra é representado no DTO (String ou StatusPedidoCompra)
    @Mapping(target = "status", source = "status") // Garante o mapeamento do enum para o DTO
    PedidoCompraResponseDTO toResponseDto(PedidoCompra pedidoCompra);

    // Mapeia lista de entidades para lista de DTOs de resposta
    List<PedidoCompraResponseDTO> toResponseDtoList(List<PedidoCompra> pedidosCompra);

    // Mapeia ItemPedidoCompra para ItemPedidoCompraResponseDTO
    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "nomeProduto", source = "produto.nome")
    @Mapping(target = "codigoProduto", source = "produto.codigoProduto") // Adicionado, se necessário
    @Mapping(target = "unidadeMedida", source = "produto.unidadeMedida") // Adicionado, se necessário
    // Se ItemPedidoCompraResponseDTO.quantidade for BigDecimal, MapStruct converte Integer automaticamente
    ItemPedidoCompraResponseDTO toItemPedidoCompraResponseDto(ItemPedidoCompra itemPedidoCompra);

    List<ItemPedidoCompraResponseDTO> toItemPedidoCompraResponseDtoList(List<ItemPedidoCompra> itensPedidoCompra);

    // REMOVIDO: O método toEntity(PedidoCompraRequestDTO dto) foi removido.
    // A criação e atualização de PedidoCompra a partir de PedidoCompraRequestDTO
    // é responsabilidade do PedidoCompraService, que define data, status e valorTotal.

    // Método para mapear String para StatusPedidoCompra (usado internamente pelo MapStruct)
    default StatusPedidoCompra map(String value) {
        if (value == null) return null;
        return StatusPedidoCompra.valueOf(value);
    }
    
    // Opcional: Se StatusPedidoCompra no DTO de resposta for String, o MapStruct lida por padrão.
    // Mas você pode ser explícito se quiser um formato específico, por exemplo:
    // default String map(StatusPedidoCompra status) {
    //     if (status == null) return null;
    //     return status.getDescricao(); // Assumindo que seu enum tem um método getDescricao()
    // }
}
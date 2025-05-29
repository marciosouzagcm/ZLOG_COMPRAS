package com.zlogcompras.mapper;

import java.util.List;

import org.mapstruct.Mapper; // Ajustado: PedidoDeCompra -> PedidoCompra
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy; // Ajustado: PedidoDeCompraResponseDTO -> PedidoCompraResponseDTO

import com.zlogcompras.model.ItemPedidoCompra;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.StatusPedidoCompra;
import com.zlogcompras.model.dto.ItemPedidoCompraResponseDTO;
import com.zlogcompras.model.dto.PedidoCompraRequestDTO;
import com.zlogcompras.model.dto.PedidoCompraResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoCompraMapper { // Ajustado: PedidoDeCompraMapper -> PedidoCompraMapper

    // Mapeia entidade para DTO de resposta
    @Mapping(target = "orcamentoAprovadoId", source = "fornecedor.id")
    @Mapping(target = "fornecedorId", source = "fornecedor.id")
    @Mapping(target = "nomeFornecedor", source = "fornecedor.nome") // Adapte para o campo nome do seu Fornecedor
    PedidoCompraResponseDTO toResponseDto(PedidoCompra pedidoCompra); // Ajustado: PedidoDeCompra -> PedidoCompra

    // Mapeia lista de entidades para lista de DTOs de resposta
    List<PedidoCompraResponseDTO> toResponseDtoList(List<PedidoCompra> pedidosCompra); // Ajustado: PedidoDeCompra -> PedidoCompra

    // Mapeia ItemPedidoCompra para ItemPedidoCompraResponseDTO
    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "nomeProduto", source = "produto.nome") // Adapte para o campo nome do seu Produto
    ItemPedidoCompraResponseDTO toItemPedidoCompraResponseDto(ItemPedidoCompra itemPedidoCompra);

    List<ItemPedidoCompraResponseDTO> toItemPedidoCompraResponseDtoList(List<ItemPedidoCompra> itensPedidoCompra);

    // Você também pode adicionar métodos para mapear de RequestDTO para Entidade se for expor um POST/PUT para PedidoCompra
    // Exemplo:
    // @Mapping(target = "id", ignore = true)
    // @Mapping(target = "orcamentoAprovado", ignore = true) // Cuidado ao mapear relacionamentos complexos
    // PedidoCompra toEntity(PedidoCompraRequestDTO dto);

    @Mapping(source = "status", target = "status")
    PedidoCompra toEntity(PedidoCompraRequestDTO dto);

    default StatusPedidoCompra map(String value) {
        if (value == null) return null;
        return StatusPedidoCompra.valueOf(value);
    }
}
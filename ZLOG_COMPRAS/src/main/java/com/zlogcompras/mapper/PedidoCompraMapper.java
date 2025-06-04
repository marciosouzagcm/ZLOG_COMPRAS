package com.zlogcompras.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.zlogcompras.model.ItemPedidoCompra;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.StatusPedidoCompra;
import com.zlogcompras.model.dto.ItemPedidoCompraResponseDTO;
import com.zlogcompras.model.dto.PedidoCompraRequestDTO;
import com.zlogcompras.model.dto.PedidoCompraResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoCompraMapper {

    // Mapeia entidade para DTO de resposta
    @Mapping(target = "orcamentoAprovadoId", source = "fornecedor.id")
    @Mapping(target = "fornecedorId", source = "fornecedor.id")
    @Mapping(target = "nomeFornecedor", source = "fornecedor.razaoSocial") // <--- AJUSTE AQUI
    PedidoCompraResponseDTO toResponseDto(PedidoCompra pedidoCompra);

    // Mapeia lista de entidades para lista de DTOs de resposta
    List<PedidoCompraResponseDTO> toResponseDtoList(List<PedidoCompra> pedidosCompra);

    // Mapeia ItemPedidoCompra para ItemPedidoCompraResponseDTO
    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "nomeProduto", source = "produto.nome") // Adapte para o campo nome do seu Produto (se produto.nome estiver correto, mantém)
    ItemPedidoCompraResponseDTO toItemPedidoCompraResponseDto(ItemPedidoCompra itemPedidoCompra);

    List<ItemPedidoCompraResponseDTO> toItemPedidoCompraResponseDtoList(List<ItemPedidoCompra> itensPedidoCompra);

    @Mapping(source = "status", target = "status")
    PedidoCompra toEntity(PedidoCompraRequestDTO dto);

    default StatusPedidoCompra map(String value) {
        if (value == null) return null;
        return StatusPedidoCompra.valueOf(value);
    }
}
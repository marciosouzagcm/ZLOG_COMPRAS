package com.zlogcompras.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.StatusOrcamento;
import com.zlogcompras.model.dto.ItemOrcamentoRequestDTO;
import com.zlogcompras.model.dto.ItemOrcamentoResponseDTO;
import com.zlogcompras.model.dto.OrcamentoListaDTO;
import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface OrcamentoMapper {

    // --- Mapeamento para criar/atualizar Orcamento (RequestDTO para Entidade) ---
    @Mapping(source = "solicitacaoCompraId", target = "solicitacaoCompra.id")
    @Mapping(source = "fornecedorId", target = "fornecedor.id")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStringToStatusOrcamento")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "valorTotal", ignore = true)
    @Mapping(source = "observacoes", target = "observacoes")
    @Mapping(source = "condicoesPagamento", target = "condicoesPagamento")
    @Mapping(source = "prazoEntrega", target = "prazoEntrega")
    @Mapping(target = "itensOrcamento", ignore = true)
    Orcamento toEntity(OrcamentoRequestDTO orcamentoRequestDTO);

    // Mapeamento para atualizar uma entidade existente
    @Mapping(source = "solicitacaoCompraId", target = "solicitacaoCompra.id")
    @Mapping(source = "fornecedorId", target = "fornecedor.id")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStringToStatusOrcamento")
    @Mapping(target = "itensOrcamento", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "valorTotal", ignore = true)
    @Mapping(source = "observacoes", target = "observacoes")
    @Mapping(source = "condicoesPagamento", target = "condicoesPagamento")
    @Mapping(source = "prazoEntrega", target = "prazoEntrega")
    void updateEntityFromDto(OrcamentoRequestDTO orcamentoRequestDTO, @MappingTarget Orcamento orcamento);

    // --- Mapeamento de Entidade para OrcamentoResponseDTO ---
    @Mapping(source = "solicitacaoCompra.id", target = "solicitacaoCompraId")
    @Mapping(source = "fornecedor.razaoSocial", target = "nomeFornecedor") // <--- AJUSTE AQUI
    @Mapping(source = "fornecedor.cnpj", target = "cnpjFornecedor")
    @Mapping(source = "solicitacaoCompra.descricao", target = "descricaoSolicitacaoCompra")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatusOrcamentoToString")
    @Mapping(source = "itensOrcamento", target = "itensOrcamento")
    OrcamentoResponseDTO toResponseDto(Orcamento orcamento);

    // --- Mapeamento de Entidade para OrcamentoListaDTO (visão de lista/resumo) ---
    @Mapping(source = "solicitacaoCompra.id", target = "solicitacaoCompraId")
    @Mapping(source = "fornecedor.razaoSocial", target = "nomeFornecedor") // <--- AJUSTE AQUI
    OrcamentoListaDTO toListaDto(Orcamento orcamento);

    List<OrcamentoListaDTO> toListaDtoList(List<Orcamento> orcamentos);

    // --- Mapeamento de ItemOrcamentoRequestDTO para ItemOrcamento (Entidade) ---
    @Mapping(source = "precoUnitarioCotado", target = "precoUnitarioCotado")
    @Mapping(target = "orcamento", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "produtoId", target = "produto", qualifiedByName = "mapProdutoIdToProduto")
    ItemOrcamento toItemOrcamentoEntity(ItemOrcamentoRequestDTO itemOrcamentoRequestDTO);

    // --- Mapeamento de ItemOrcamento (Entidade) para ItemOrcamentoResponseDTO ---
    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.nome", target = "nomeProduto")
    @Mapping(source = "produto.codigoProduto", target = "codigoProduto")
    @Mapping(source = "produto.unidadeMedida", target = "unidadeMedidaProduto")
    @Mapping(source = "precoUnitarioCotado", target = "precoUnitarioCotado")
    @Mapping(source = "observacoes", target = "observacoes")
    @Mapping(source = "version", target = "version")
    // CORRIGIDO: Removido BigDecimal.valueOf() redundante. Agora multiplica BigDecimal por BigDecimal.
    @Mapping(target = "subtotal", expression = "java(itemOrcamento.getQuantidade() != null && itemOrcamento.getPrecoUnitarioCotado() != null ? " +
            "itemOrcamento.getQuantidade().multiply(itemOrcamento.getPrecoUnitarioCotado()) " +
            ": null)")
    ItemOrcamentoResponseDTO toItemOrcamentoResponseDto(ItemOrcamento itemOrcamento);

    List<ItemOrcamentoResponseDTO> toItemOrcamentoResponseDtoList(List<ItemOrcamento> itensOrcamento);

    // --- Métodos de Conversão para Enum/String ---
    @Named("mapStringToStatusOrcamento")
    default StatusOrcamento mapStringToStatusOrcamento(String statusString) {
        if (statusString == null || statusString.isEmpty()) {
            return null;
        }
        try {
            return StatusOrcamento.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status de orçamento inválido: " + statusString);
        }
    }

    @Named("mapStatusOrcamentoToString")
    default String mapStatusOrcamentoToString(StatusOrcamento statusEnum) {
        if (statusEnum == null) {
            return null;
        }
        return statusEnum.name();
    }

    // --- Método auxiliar para mapear um ID para uma entidade Produto (referência) ---
    @Named("mapProdutoIdToProduto")
    default Produto mapProdutoIdToProduto(Long produtoId) {
        if (produtoId == null) {
            return null;
        }
        Produto produto = new Produto();
        produto.setId(produtoId);
        return produto;
    }
}
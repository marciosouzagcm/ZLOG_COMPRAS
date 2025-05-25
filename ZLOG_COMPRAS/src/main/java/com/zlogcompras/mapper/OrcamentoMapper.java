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
import com.zlogcompras.model.dto.ItemOrcamentoRequestDTO; // Renomeado
import com.zlogcompras.model.dto.OrcamentoRequestDTO; // Renomeado
import com.zlogcompras.model.dto.ItemOrcamentoResponseDTO;
import com.zlogcompras.model.dto.OrcamentoListaDTO;
import com.zlogcompras.model.dto.OrcamentoResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface OrcamentoMapper {

    // --- Mapeamento para criar/atualizar Orcamento (RequestDTO para Entidade) ---
    @Mapping(source = "solicitacaoCompraId", target = "solicitacaoCompra.id")
    @Mapping(source = "fornecedorId", target = "fornecedor.id")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStringToStatusOrcamento")
    @Mapping(target = "id", ignore = true) // ID é gerado pelo banco, não pelo DTO
    @Mapping(target = "version", ignore = true) // Version é gerenciado pelo JPA, não pelo DTO
    @Mapping(target = "dataCriacao", ignore = true) // Data de criação é setada na entidade
    @Mapping(target = "dataAtualizacao", ignore = true) // Data de atualização é setada na entidade
    @Mapping(target = "valorTotal", ignore = true) // Valor total é calculado no serviço/entidade, não vem do DTO
    @Mapping(source = "observacoes", target = "observacoes") // Ajustado para "observacoes"
    @Mapping(source = "condicoesPagamento", target = "condicoesPagamento")
    @Mapping(source = "prazoEntrega", target = "prazoEntrega")
    @Mapping(target = "itensOrcamento", ignore = true) // Itens são tratados separadamente no serviço para criação
    Orcamento toEntity(OrcamentoRequestDTO orcamentoRequestDTO); // Renomeado

    // Mapeamento para atualizar uma entidade existente
    @Mapping(source = "solicitacaoCompraId", target = "solicitacaoCompra.id")
    @Mapping(source = "fornecedorId", target = "fornecedor.id")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStringToStatusOrcamento")
    @Mapping(target = "itensOrcamento", ignore = true) // Itens são atualizados separadamente no serviço
    @Mapping(target = "id", ignore = true) // Nunca atualize o ID da entidade através do DTO de input
    @Mapping(target = "version", ignore = true) // Versão gerenciada pelo JPA
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "valorTotal", ignore = true)
    @Mapping(source = "observacoes", target = "observacoes") // Ajustado para "observacoes"
    @Mapping(source = "condicoesPagamento", target = "condicoesPagamento")
    @Mapping(source = "prazoEntrega", target = "prazoEntrega")
    void updateEntityFromDto(OrcamentoRequestDTO orcamentoRequestDTO, @MappingTarget Orcamento orcamento); // Renomeado

    // --- Mapeamento de Entidade para OrcamentoResponseDTO ---
    @Mapping(source = "solicitacaoCompra.id", target = "solicitacaoCompraId")
    @Mapping(source = "fornecedor.nome", target = "nomeFornecedor")
    @Mapping(source = "fornecedor.cnpj", target = "cnpjFornecedor")
    @Mapping(source = "solicitacaoCompra.descricao", target = "descricaoSolicitacaoCompra")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatusOrcamentoToString")
    @Mapping(source = "itensOrcamento", target = "itensOrcamento")
    OrcamentoResponseDTO toResponseDto(Orcamento orcamento);

    // --- Mapeamento de Entidade para OrcamentoListaDTO (visão de lista/resumo) ---
    @Mapping(source = "solicitacaoCompra.id", target = "solicitacaoCompraId")
    @Mapping(source = "fornecedor.nome", target = "nomeFornecedor")
    OrcamentoListaDTO toListaDto(Orcamento orcamento);

    List<OrcamentoListaDTO> toListaDtoList(List<Orcamento> orcamentos);

    // --- Mapeamento de ItemOrcamentoRequestDTO para ItemOrcamento (Entidade) ---
    @Mapping(source = "precoUnitarioCotado", target = "precoUnitarioCotado") // Garante o mapeamento
    @Mapping(target = "orcamento", ignore = true) // Orcamento é setado manualmente no serviço
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "id", ignore = true) // ID para novos itens é gerado
    @Mapping(source = "produtoId", target = "produto", qualifiedByName = "mapProdutoIdToProduto")
    ItemOrcamento toItemOrcamentoEntity(ItemOrcamentoRequestDTO itemOrcamentoRequestDTO); // Renomeado

    // --- Mapeamento de ItemOrcamento (Entidade) para ItemOrcamentoResponseDTO ---
    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.nome", target = "nomeProduto")
    @Mapping(source = "produto.codigo", target = "codigoProduto")
    @Mapping(source = "produto.unidadeMedida", target = "unidadeMedidaProduto")
    @Mapping(source = "precoUnitarioCotado", target = "precoUnitarioCotado")
    @Mapping(source = "observacoes", target = "observacoes")
    @Mapping(source = "version", target = "version")
    @Mapping(target = "subtotal", expression = "java(itemOrcamento.getQuantidade().multiply(itemOrcamento.getPrecoUnitarioCotado()))") // Calcula subtotal no DTO
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
            // Considere logar o erro aqui
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
        // O serviço (OrcamentoService) é responsável por buscar o Produto completo
        // antes de salvar o ItemOrcamento. Aqui, apenas criamos uma referência com o ID.
        return produto;
    }
}
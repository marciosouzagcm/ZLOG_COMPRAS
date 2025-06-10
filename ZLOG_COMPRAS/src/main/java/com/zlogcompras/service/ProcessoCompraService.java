package com.zlogcompras.service;

import java.math.BigDecimal; // Importe BigDecimal
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusOrcamento;
import com.zlogcompras.model.StatusSolicitacaoCompra;
import com.zlogcompras.model.dto.ItemOrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.repository.FornecedorRepository;
import com.zlogcompras.repository.SolicitacaoCompraRepository;

@Service
public class ProcessoCompraService {

    private final SolicitacaoCompraRepository solicitacaoCompraRepository;
    private final OrcamentoService orcamentoService;
    private final FornecedorRepository fornecedorRepository;
    // private final EstoqueService estoqueService; // Se necessário para verificar
    // estoque

    @Autowired
    public ProcessoCompraService(SolicitacaoCompraRepository solicitacaoCompraRepository,
            OrcamentoService orcamentoService,
            FornecedorRepository fornecedorRepository) {
        this.solicitacaoCompraRepository = solicitacaoCompraRepository;
        this.orcamentoService = orcamentoService;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional
    public SolicitacaoCompra iniciarProcessoCompraPorSolicitacaoId(Long solicitacaoId) {
        SolicitacaoCompra solicitacao = solicitacaoCompraRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Solicitação de Compra não encontrada com ID: " + solicitacaoId));

        if (solicitacao.getStatus() != StatusSolicitacaoCompra.PENDENTE &&
                solicitacao.getStatus() != StatusSolicitacaoCompra.ORCAMENTO_APROVADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é possível iniciar o processo de compra para a solicitação com status: "
                            + solicitacao.getStatus().getDescricao());
        }

        if (solicitacao.getStatus() != StatusSolicitacaoCompra.EM_ANDAMENTO) {
            solicitacao.setStatus(StatusSolicitacaoCompra.EM_ANDAMENTO);
            solicitacaoCompraRepository.save(solicitacao);
        }

        Fornecedor fornecedorParaOrcamento = fornecedorRepository.findById(1L) // **Mantenha sua lógica de seleção de
                                                                               // fornecedor**
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nenhum fornecedor padrão encontrado para criar orçamento."));

        List<ItemOrcamentoRequestDTO> itensParaOrcamento = new ArrayList<>();
        for (ItemSolicitacaoCompra itemSolicitacao : solicitacao.getItens()) {
            // Assegure-se de que a quantidade está definida
            if (itemSolicitacao.getQuantidade() == null
                    || itemSolicitacao.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                // Tratar erro ou pular item se a quantidade não for válida
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Quantidade do item da solicitação é inválida: " + itemSolicitacao.getProduto().getNome());
            }

            ItemOrcamentoRequestDTO itemOrcamentoDTO = new ItemOrcamentoRequestDTO();
            itemOrcamentoDTO.setProdutoId(itemSolicitacao.getProduto().getId());
            itemOrcamentoDTO.setQuantidade(itemSolicitacao.getQuantidade()); // Use a quantidade da solicitação

            // **IMPORTANTE: DEFINIR O PREÇO UNITÁRIO COTADO INICIALMENTE**
            // Se o produto já tem um preço de referência, você pode usá-lo.
            // Caso contrário, use BigDecimal.ZERO ou BigDecimal.valueOf(0.0)
            if (itemSolicitacao.getProduto() != null && itemSolicitacao.getProduto().getPrecoUnitario() != null) {
                itemOrcamentoDTO.setPrecoUnitarioCotado(itemSolicitacao.getProduto().getPrecoUnitario());
            } else {
                itemOrcamentoDTO.setPrecoUnitarioCotado(BigDecimal.ZERO); // Valor inicial zero ou outro valor padrão
                // Ou, se isso indicar que o preço precisa ser obtido via cotação real,
                // você pode querer lançar uma exceção ou registrar um log.
            }

            itemOrcamentoDTO.setObservacoes("Item solicitado para orçamento da solicitação: " + solicitacaoId);
            itensParaOrcamento.add(itemOrcamentoDTO);
        }

        if (!itensParaOrcamento.isEmpty()) {
            OrcamentoRequestDTO orcamentoRequestDTO = new OrcamentoRequestDTO();
            orcamentoRequestDTO.setSolicitacaoCompraId(solicitacao.getId());
            orcamentoRequestDTO.setFornecedorId(fornecedorParaOrcamento.getId());
            orcamentoRequestDTO.setDataCotacao(LocalDate.now());
            orcamentoRequestDTO.setObservacoes("Orçamento inicial gerado para solicitação " + solicitacaoId);
            orcamentoRequestDTO.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO.name());
            orcamentoRequestDTO.setItensOrcamento(itensParaOrcamento);

            orcamentoService.criarOrcamento(orcamentoRequestDTO);
        } else {
            System.out.println("Nenhum item da solicitação " + solicitacaoId + " precisa de orçamento neste momento.");
        }

        return solicitacao;
    }

    @Transactional
    public SolicitacaoCompra atualizarStatusSolicitacao(Long solicitacaoId, StatusSolicitacaoCompra novoStatus) {
        SolicitacaoCompra solicitacao = solicitacaoCompraRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Solicitação de Compra não encontrada com ID: " + solicitacaoId));

        solicitacao.setStatus(novoStatus);
        return solicitacaoCompraRepository.save(solicitacao);
    }
}
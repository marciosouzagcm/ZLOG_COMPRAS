package com.zlogcompras.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.mapper.SolicitacaoMapper; // Importe seu mapper
import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.ItemPedidoCompra;
import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusOrcamento;
import com.zlogcompras.model.StatusPedidoCompra;
import com.zlogcompras.model.StatusSolicitacaoCompra;
import com.zlogcompras.model.dto.ItemOrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.model.dto.SolicitacaoCompraResponseDTO; // Importe o DTO de resposta
import com.zlogcompras.repository.FornecedorRepository;
import com.zlogcompras.repository.ItemPedidoCompraRepository;
import com.zlogcompras.repository.OrcamentoRepository;
import com.zlogcompras.repository.PedidoCompraRepository;
import com.zlogcompras.repository.SolicitacaoCompraRepository;

@Service
public class ProcessoCompraService {

    private final SolicitacaoCompraRepository solicitacaoCompraRepository;
    private final OrcamentoService orcamentoService;
    private final FornecedorRepository fornecedorRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final PedidoCompraRepository pedidoCompraRepository;
    private final ItemPedidoCompraRepository itemPedidoCompraRepository;
    private final SolicitacaoMapper solicitacaoMapper; // Novo: Injeção do mapper

    @Autowired
    public ProcessoCompraService(SolicitacaoCompraRepository solicitacaoCompraRepository,
            OrcamentoService orcamentoService,
            FornecedorRepository fornecedorRepository,
            OrcamentoRepository orcamentoRepository,
            PedidoCompraRepository pedidoCompraRepository,
            ItemPedidoCompraRepository itemPedidoCompraRepository,
            SolicitacaoMapper solicitacaoMapper) { // Novo: Adicionado ao construtor
        this.solicitacaoCompraRepository = solicitacaoCompraRepository;
        this.orcamentoService = orcamentoService;
        this.fornecedorRepository = fornecedorRepository;
        this.orcamentoRepository = orcamentoRepository;
        this.pedidoCompraRepository = pedidoCompraRepository;
        this.itemPedidoCompraRepository = itemPedidoCompraRepository;
        this.solicitacaoMapper = solicitacaoMapper; // Novo: Inicialização
    }

    /**
     * Inicia o processo de compra para uma solicitação, alterando seu status e
     * criando um orçamento inicial.
     *
     * @param solicitacaoId ID da solicitação de compra.
     * @return A SolicitacaoCompra atualizada.
     * @throws ResponseStatusException se a solicitação não for encontrada, se o
     *                                 status for inválido,
     *                                 se a quantidade de um item for inválida ou se
     *                                 o fornecedor padrão não for encontrado.
     */
    @Transactional
    public SolicitacaoCompra iniciarProcessoCompraPorSolicitacaoId(Long solicitacaoId) {
        SolicitacaoCompra solicitacao = solicitacaoCompraRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Solicitação de Compra não encontrada com ID: " + solicitacaoId));

        // Impede que o processo de compra seja iniciado para solicitações já
        // concluídas, canceladas ou com pedido gerado.
        if (solicitacao.getStatus() == StatusSolicitacaoCompra.CONCLUIDA ||
                solicitacao.getStatus() == StatusSolicitacaoCompra.CANCELADA ||
                solicitacao.getStatus() == StatusSolicitacaoCompra.PEDIDO_GERADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é possível iniciar o processo de compra para a solicitação com status: "
                            + solicitacao.getStatus().getDescricao());
        }

        // Altera o status da solicitação para EM_ANDAMENTO, se ainda não estiver.
        if (solicitacao.getStatus() != StatusSolicitacaoCompra.EM_ANDAMENTO) {
            solicitacao.setStatus(StatusSolicitacaoCompra.EM_ANDAMENTO);
            solicitacaoCompraRepository.save(solicitacao);
        }

        // Busca um fornecedor padrão para a criação do orçamento.
        Fornecedor fornecedorParaOrcamento = fornecedorRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nenhum fornecedor padrão encontrado para criar orçamento. Configure um fornecedor padrão (ID 1)."));

        List<ItemOrcamentoRequestDTO> itensParaOrcamento = new ArrayList<>();
        for (ItemSolicitacaoCompra itemSolicitacao : solicitacao.getItens()) {
            if (itemSolicitacao.getQuantidade() == null
                    || itemSolicitacao.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Quantidade do item '" + itemSolicitacao.getProduto().getNome()
                                + "' da solicitação é inválida. A quantidade deve ser maior que zero.");
            }

            ItemOrcamentoRequestDTO itemOrcamentoDTO = new ItemOrcamentoRequestDTO();
            itemOrcamentoDTO.setProdutoId(itemSolicitacao.getProduto().getId());
            itemOrcamentoDTO.setQuantidade(itemSolicitacao.getQuantidade());

            if (itemSolicitacao.getProduto() != null && itemSolicitacao.getProduto().getPrecoUnitario() != null) {
                itemOrcamentoDTO.setPrecoUnitarioCotado(itemSolicitacao.getProduto().getPrecoUnitario());
            } else {
                itemOrcamentoDTO.setPrecoUnitarioCotado(BigDecimal.ZERO);
            }

            itemOrcamentoDTO.setObservacoes("Item solicitado para orçamento da solicitação: " + solicitacaoId);
            itensParaOrcamento.add(itemOrcamentoDTO);
        }

        if (!itensParaOrcamento.isEmpty()) {
            OrcamentoRequestDTO orcamentoRequestDTO = new OrcamentoRequestDTO();
            orcamentoRequestDTO.setSolicitacaoCompraId(solicitacao.getId());
            orcamentoRequestDTO.setFornecedorId(fornecedorParaOrcamento.getId());
            orcamentoRequestDTO.setDataCotacao(LocalDate.now());
            orcamentoRequestDTO
                    .setObservacoes("Orçamento inicial gerado automaticamente para solicitação " + solicitacaoId);
            orcamentoRequestDTO.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO.name());
            orcamentoRequestDTO.setItensOrcamento(itensParaOrcamento);

            orcamentoService.criarOrcamento(orcamentoRequestDTO);
            System.out.println("Orçamento gerado com sucesso para solicitação " + solicitacaoId);
        } else {
            System.out.println("A solicitação " + solicitacaoId + " não possui itens válidos para gerar um orçamento.");
        }

        return solicitacao;
    }

    /**
     * Gera um Pedido de Compra a partir de um orçamento aprovado.
     *
     * @param orcamentoId ID do orçamento aprovado.
     * @return O PedidoCompra recém-criado.
     * @throws ResponseStatusException se o orçamento não for encontrado, não
     *                                 estiver APROVADO,
     *                                 já houver um pedido gerado para ele, ou se
     *                                 houver itens inválidos.
     */
    @Transactional
    public PedidoCompra gerarPedidoDeCompra(Long orcamentoId) {
        Orcamento orcamento = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Orçamento não encontrado com ID: " + orcamentoId));

        if (orcamento.getStatus() != StatusOrcamento.APROVADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é possível gerar um pedido de compra para um orçamento com status: "
                            + orcamento.getStatus().getDescricao() + ". O orçamento deve estar APROVADO.");
        }

        if (pedidoCompraRepository.existsByOrcamentoId(orcamentoId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe um pedido de compra gerado para o orçamento " + orcamentoId + ".");
        }

        PedidoCompra pedidoCompra = new PedidoCompra();
        pedidoCompra.setDataPedido(LocalDate.now());
        pedidoCompra.setFornecedor(orcamento.getFornecedor());
        pedidoCompra.setOrcamento(orcamento);
        pedidoCompra.setStatus(StatusPedidoCompra.AGUARDANDO_ENVIO);

        BigDecimal valorTotalPedido = BigDecimal.ZERO;
        List<ItemPedidoCompra> itensPedido = new ArrayList<>();

        if (orcamento.getItensOrcamento() != null && !orcamento.getItensOrcamento().isEmpty()) {
            for (ItemOrcamento itemOrcamento : orcamento.getItensOrcamento()) {
                Produto produto = itemOrcamento.getProduto();
                BigDecimal quantidade = itemOrcamento.getQuantidade();
                BigDecimal precoUnitarioCotado = itemOrcamento.getPrecoUnitarioCotado();

                if (produto == null || quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0 ||
                        precoUnitarioCotado == null || precoUnitarioCotado.compareTo(BigDecimal.ZERO) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Item do orçamento inválido para geração do pedido: " +
                                    (produto != null ? produto.getNome() : "Produto Nulo") +
                                    ". Verifique produto, quantidade e preço.");
                }

                ItemPedidoCompra itemPedido = new ItemPedidoCompra();
                itemPedido.setProduto(produto);
                itemPedido.setQuantidade(quantidade.intValueExact());

                itemPedido.setPrecoUnitario(precoUnitarioCotado);
                itemPedido.setObservacoes(itemOrcamento.getObservacoes());

                itemPedido.setNomeProduto(produto.getNome());
                itemPedido.setCodigoProduto(produto.getCodigo());
                if (produto.getUnidadeMedida() != null) {
                    itemPedido.setUnidadeMedida(produto.getUnidadeMedida());
                } else {
                    itemPedido.setUnidadeMedida("N/A");
                }

                BigDecimal subtotalItem = precoUnitarioCotado.multiply(new BigDecimal(itemPedido.getQuantidade()));
                itemPedido.setSubtotal(subtotalItem);

                itensPedido.add(itemPedido);
                valorTotalPedido = valorTotalPedido.add(subtotalItem);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O orçamento aprovado " + orcamentoId + " não contém itens para gerar um pedido de compra.");
        }

        pedidoCompra.setItens(itensPedido);
        pedidoCompra.setValorTotal(valorTotalPedido);

        pedidoCompra = pedidoCompraRepository.save(pedidoCompra);

        SolicitacaoCompra solicitacao = orcamento.getSolicitacaoCompra();
        if (solicitacao != null) {
            solicitacao.setStatus(StatusSolicitacaoCompra.PEDIDO_GERADO);
            solicitacaoCompraRepository.save(solicitacao);
        }

        orcamento.setStatus(StatusOrcamento.PEDIDO_GERADO);
        orcamentoRepository.save(orcamento);

        return pedidoCompra;
    }

    /**
     * Atualiza o status de uma solicitação de compra.
     *
     * @param solicitacaoId ID da solicitação.
     * @param novoStatus    O novo status a ser definido.
     * @return A SolicitacaoCompraResponseDTO atualizada.
     * @throws ResponseStatusException se a solicitação não for encontrada.
     */
    @Transactional
    public SolicitacaoCompraResponseDTO atualizarStatusSolicitacao(Long solicitacaoId,
            StatusSolicitacaoCompra novoStatus) {
        SolicitacaoCompra solicitacao = solicitacaoCompraRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Solicitação de Compra não encontrada com ID: " + solicitacaoId));

        solicitacao.setStatus(novoStatus);
        solicitacao.setDataAtualizacao(LocalDateTime.now());
        SolicitacaoCompra solicitacaoAtualizada = solicitacaoCompraRepository.save(solicitacao);

        // CORRIGIDO: Chame o método toResponseDto() na instância do solicitacaoMapper
        return solicitacaoMapper.toResponseDto(solicitacaoAtualizada);
    }
}
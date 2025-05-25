package com.zlogcompras.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.ItemPedidoCompra;
import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusOrcamento;
import static com.zlogcompras.model.StatusOrcamento.SELECIONADO;
import com.zlogcompras.model.dto.ItemOrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoRequestDTO;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProcessoCompraService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessoCompraService.class);

    private final SolicitacaoCompraService solicitacaoCompraService;
    private final ItemSolicitacaoCompraService itemSolicitacaoCompraService;
    private final PedidoCompraService pedidoCompraService;
    private final OrcamentoService orcamentoService;
    private final FornecedorService fornecedorService;
    private final EstoqueService estoqueService;
    private final ProdutoService produtoService;

    @Autowired
    public ProcessoCompraService(SolicitacaoCompraService solicitacaoCompraService,
                                 ItemSolicitacaoCompraService itemSolicitacaoCompraService,
                                 PedidoCompraService pedidoCompraService,
                                 OrcamentoService orcamentoService,
                                 FornecedorService fornecedorService,
                                 EstoqueService estoqueService,
                                 ProdutoService produtoService) {
        this.solicitacaoCompraService = solicitacaoCompraService;
        this.itemSolicitacaoCompraService = itemSolicitacaoCompraService;
        this.pedidoCompraService = pedidoCompraService;
        this.orcamentoService = orcamentoService;
        this.fornecedorService = fornecedorService;
        this.estoqueService = estoqueService;
        this.produtoService = produtoService;
    }

    @Transactional
    public void iniciarProcessoCompra(ItemSolicitacaoCompra itemSolicitacao) {
        logger.info("Iniciando processo de compra para o item de solicitação ID: {}", itemSolicitacao.getId());
        SolicitacaoCompra solicitacao = solicitacaoCompraService
                .buscarSolicitacaoPorId(itemSolicitacao.getSolicitacaoCompra().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Solicitação de compra pai não encontrada para o item."));

        solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Compra Autorizada");
        solicitarOrcamentos(List.of(itemSolicitacao));
    }

    @Transactional
    public void iniciarProcessoCompraPorSolicitacaoId(Long solicitacaoId) {
        logger.info("Iniciando processo de compra para a solicitação ID: {}", solicitacaoId);
        SolicitacaoCompra solicitacao = solicitacaoCompraService.buscarSolicitacaoPorId(solicitacaoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Solicitação de compra não encontrada com ID: " + solicitacaoId));

        List<ItemSolicitacaoCompra> itensParaComprar = solicitacao.getItens().stream()
                .filter(item -> "Aguardando Compra".equalsIgnoreCase(item.getStatus()))
                .collect(Collectors.toList());

        if (!itensParaComprar.isEmpty()) {
            solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Compra em Andamento");
            solicitarOrcamentos(itensParaComprar);
        } else {
            logger.info("Não há itens aguardando compra na solicitação {}.", solicitacaoId);
        }
    }

    @Transactional
    public void solicitarOrcamentos(List<ItemSolicitacaoCompra> itens) {
        if (!itens.isEmpty()) {
            Long solicitacaoId = itens.get(0).getSolicitacaoCompra().getId();
            logger.info("Solicitando orçamentos para a solicitação ID: {}", solicitacaoId);
            solicitacaoCompraService.atualizarStatusSolicitacao(solicitacaoId, "Aguardando Orçamento");
            simularCriacaoOrcamentosPorItem(itens);
        }
    }

    @Transactional
    private void simularCriacaoOrcamentosPorItem(List<ItemSolicitacaoCompra> itens) {
        Fornecedor fornecedor1 = fornecedorService.buscarFornecedorPorId(1L)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fornecedor com ID 1 não encontrado para simulação de orçamento."));

        Fornecedor fornecedor2 = fornecedorService.buscarFornecedorPorId(2L)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fornecedor com ID 2 não encontrado para simulação de orçamento."));


        for (ItemSolicitacaoCompra item : itens) {
            logger.debug("Simulando orçamentos para o item de solicitação ID: {}", item.getId());

            // --- Orçamento para Fornecedor 1 ---
            OrcamentoRequestDTO orcamentoDto1 = new OrcamentoRequestDTO();
            orcamentoDto1.setSolicitacaoCompraId(item.getSolicitacaoCompra().getId());
            orcamentoDto1.setFornecedorId(fornecedor1.getId());
            orcamentoDto1.setDataCotacao(LocalDate.now());
            BigDecimal precoUnitario1 = BigDecimal.valueOf(Math.random() * 50 + 50).setScale(2, RoundingMode.HALF_UP);

            orcamentoDto1.setNumeroOrcamento("ORC_" + item.getId() + "_FORN1");

            ItemOrcamentoRequestDTO itemOrcamentoRequestDTO1 = new ItemOrcamentoRequestDTO();
            itemOrcamentoRequestDTO1.setProdutoId(item.getProduto().getId());
            itemOrcamentoRequestDTO1.setQuantidade(item.getQuantidade());
            itemOrcamentoRequestDTO1.setPrecoUnitarioCotado(precoUnitario1);
            orcamentoDto1.setItensOrcamento(List.of(itemOrcamentoRequestDTO1));

            // CORRIGIDO: Convertendo StatusOrcamento para String usando .name()
            orcamentoDto1.setStatus(StatusOrcamento.COTADO.name()); // <--- AQUI

            orcamentoService.criarOrcamento(orcamentoDto1);

            // --- Orçamento para Fornecedor 2 ---
            OrcamentoRequestDTO orcamentoDto2 = new OrcamentoRequestDTO();
            orcamentoDto2.setSolicitacaoCompraId(item.getSolicitacaoCompra().getId());
            orcamentoDto2.setFornecedorId(fornecedor2.getId());
            orcamentoDto2.setDataCotacao(LocalDate.now().plusDays(1));
            BigDecimal precoUnitario2 = BigDecimal.valueOf(Math.random() * 60 + 60).setScale(2, RoundingMode.HALF_UP);

            orcamentoDto2.setNumeroOrcamento("ORC_" + item.getId() + "_FORN2");

            ItemOrcamentoRequestDTO itemOrcamentoRequestDTO2 = new ItemOrcamentoRequestDTO();
            itemOrcamentoRequestDTO2.setProdutoId(item.getProduto().getId());
            itemOrcamentoRequestDTO2.setQuantidade(item.getQuantidade());
            itemOrcamentoRequestDTO2.setPrecoUnitarioCotado(precoUnitario2);
            orcamentoDto2.setItensOrcamento(List.of(itemOrcamentoRequestDTO2));

            // CORRIGIDO: Convertendo StatusOrcamento para String usando .name()
            orcamentoDto2.setStatus(StatusOrcamento.COTADO.name()); // <--- E AQUI

            orcamentoService.criarOrcamento(orcamentoDto2);

            item.setStatus("Orçamento Solicitado");
        }

        if (!itens.isEmpty()) {
            solicitacaoCompraService.atualizarSolicitacao(itens.get(0).getSolicitacaoCompra().getId(),
                    itens.get(0).getSolicitacaoCompra());
            solicitacaoCompraService.atualizarStatusSolicitacao(itens.get(0).getSolicitacaoCompra().getId(),
                    "Orçamentos Recebidos");
            logger.info("Orçamentos recebidos para a solicitação ID: {}", itens.get(0).getSolicitacaoCompra().getId());
        }
    }

    @Transactional
    public void aprovarOrcamentoGerarPedido(Long orcamentoId) {
        logger.info("Aprovando orçamento ID: {} e gerando pedido.", orcamentoId);
        Orcamento orcamentoSelecionado = buscarOrcamentoPorId(orcamentoId);

        // A linha abaixo foi removida, pois a lógica de aprovação do status e a
        // geração do pedido já são feitas no OrcamentoService.aprovarOrcamentoGerarPedido(orcamentoId).
        // orcamentoSelecionado.setStatus(SELECIONADO);

        orcamentoService.aprovarOrcamentoGerarPedido(orcamentoId);

        SolicitacaoCompra solicitacao = orcamentoSelecionado.getSolicitacaoCompra();
        solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Compra Aprovada");

        gerarOrdemDeCompra(solicitacao, orcamentoSelecionado);
    }

    private Orcamento buscarOrcamentoPorId(Long orcamentoId) {
        return orcamentoService.buscarOrcamentoPorId(orcamentoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Orçamento não encontrado com ID: " + orcamentoId));
    }

    @Transactional
    private void gerarOrdemDeCompra(SolicitacaoCompra solicitacao, Orcamento orcamentoSelecionado) {
        logger.info("Gerando Ordem de Compra para solicitação ID: {} com orçamento ID: {}", solicitacao.getId(),
                orcamentoSelecionado.getId());

        PedidoCompra novoPedido = new PedidoCompra();
        novoPedido.setFornecedor(orcamentoSelecionado.getFornecedor());
        novoPedido.setDataPedido(LocalDate.now());
        novoPedido.setStatus("Pendente");
        novoPedido.setValorTotal(orcamentoSelecionado.getValorTotal());

        List<ItemPedidoCompra> itensPedido = orcamentoSelecionado.getItensOrcamento().stream()
                .map(itemOrcamento -> {
                    ItemPedidoCompra itemPedido = new ItemPedidoCompra();
                    itemPedido.setProduto(itemOrcamento.getProduto());
                    itemPedido.setQuantidade(itemOrcamento.getQuantidade());
                    itemPedido.setPrecoUnitario(itemOrcamento.getPrecoUnitarioCotado());
                    itemPedido.setPedidoCompra(novoPedido);
                    return itemPedido;
                })
                .collect(Collectors.toList());

        novoPedido.setItens(itensPedido);

        PedidoCompra pedidoSalvo = pedidoCompraService.criarPedidoCompra(novoPedido);

        solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Gerada OC");
        logger.info("Ordem de Compra (ID: {}) gerada para solicitação ID: {}", pedidoSalvo.getId(),
                solicitacao.getId());

        solicitacao.getItens().forEach(itemSolicitacao -> {
            boolean itemIncluidoNoPedido = itensPedido.stream()
                    .anyMatch(ip -> ip.getProduto().getId().equals(itemSolicitacao.getProduto().getId()) &&
                                    ip.getQuantidade().compareTo(itemSolicitacao.getQuantidade()) == 0);
            if (itemIncluidoNoPedido) {
                itemSolicitacao.setStatus("Em Pedido");
            }
        });
        solicitacaoCompraService.atualizarSolicitacao(solicitacao.getId(), solicitacao);
    }

    @Transactional
    public void enviarOrdemDeCompra(Long pedidoId) {
        logger.info("Enviando Ordem de Compra ID: {} para o fornecedor.", pedidoId);
        pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Enviado Fornecedor");
    }

    @Transactional
    public void gerenciarEntregaRecebimento(Long pedidoId) {
        logger.info("Gerenciando entrega/recebimento para Pedido ID: {}", pedidoId);
        pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Recebido");
    }

    @Transactional
    public void enviarParaObra(Long pedidoId) {
        logger.info("Enviando Pedido ID: {} para a obra.", pedidoId);
        pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Enviada Obra");
    }

    @Transactional
    public void atualizarEstoqueRecebimento(Long pedidoId) {
        logger.info("Atualizando estoque após recebimento do Pedido ID: {}", pedidoId);
        PedidoCompra pedido = pedidoCompraService.buscarPedidoCompraPorId(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Pedido de compra não encontrado com ID: " + pedidoId));

        if ("Recebido".equals(pedido.getStatus())) {
            pedido.getItens().forEach(itemPedido -> {
                String codigoMaterial = itemPedido.getProduto().getCodigo();
                BigDecimal quantidadeRecebida = itemPedido.getQuantidade();
                logger.debug("Recebendo material: {} - Quantidade: {} para o pedido ID: {}", codigoMaterial,
                        quantidadeRecebida, pedido.getId());
                estoqueService.receberMaterial(codigoMaterial, quantidadeRecebida, "PEDIDO_" + pedido.getId());
            });

            pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Estoque Atualizado");
            logger.info("Estoque atualizado para o Pedido ID: {}", pedidoId);
        } else {
            logger.warn(
                    "Tentativa de atualizar estoque para Pedido ID: {} que não está no status 'Recebido'. Status atual: {}",
                    pedidoId, pedido.getStatus());
        }
    }

    @Transactional
    public void processoPrestacaoContas(Long pedidoId, String numeroNotaFiscal, LocalDate dataNotaFiscal,
                                        BigDecimal valorNota) {
        logger.info("Iniciando processo de prestação de contas para Pedido ID: {} com NF: {}", pedidoId,
                numeroNotaFiscal);
        PedidoCompra pedido = pedidoCompraService.buscarPedidoCompraPorId(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Pedido de compra não encontrado com ID: " + pedidoId));

        pedido.setNumeroNotaFiscal(numeroNotaFiscal);
        pedido.setDataNotaFiscal(dataNotaFiscal);
        pedido.setValorNotaFiscal(valorNota);
        pedidoCompraService.atualizarPedido(pedido);

        pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Contabilizado");
        logger.info("Pedido {} contabilizado com NF: {}", pedidoId, numeroNotaFiscal);
    }
}
package com.zlogcompras.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.ItemPedidoCompra;
import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.SolicitacaoCompra;

@Service
public class ProcessoCompraService {

    @Autowired
    private SolicitacaoCompraService solicitacaoCompraService;

    @Autowired
    private ItemSolicitacaoCompraService itemSolicitacaoCompraService;

    @Autowired
    private PedidoCompraService pedidoCompraService;

    @Autowired
    private OrcamentoService orcamentoService;

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private EstoqueService estoqueService;

    @Transactional
    public void iniciarProcessoCompra(ItemSolicitacaoCompra itemSolicitacao) {
        Optional<SolicitacaoCompra> optSolicitacao = solicitacaoCompraService
                .buscarSolicitacaoPorId(itemSolicitacao.getSolicitacaoCompra().getId());
        optSolicitacao.ifPresent(solicitacao -> {
            solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Compra Autorizada");
            solicitarOrcamentos(List.of(itemSolicitacao));
        });
    }

    @Transactional
    public void iniciarProcessoCompraPorSolicitacaoId(Long solicitacaoId) {
        Optional<SolicitacaoCompra> optSolicitacao = solicitacaoCompraService.buscarSolicitacaoPorId(solicitacaoId);
        optSolicitacao.ifPresentOrElse(solicitacao -> {
            List<ItemSolicitacaoCompra> itensParaComprar = solicitacao.getItens().stream()
                    .filter(item -> "Aguardando Compra".equalsIgnoreCase(item.getStatus()))
                    .toList();

            if (!itensParaComprar.isEmpty()) {
                solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Compra em Andamento");
                solicitarOrcamentos(itensParaComprar);
            } else {
                System.out.println("Não há itens aguardando compra na solicitação " + solicitacaoId);
            }
        }, () -> System.out.println("Solicitação de compra não encontrada com ID: " + solicitacaoId));
    }

    @Transactional
    public void solicitarOrcamentos(List<ItemSolicitacaoCompra> itens) {
        if (!itens.isEmpty()) {
            Long solicitacaoId = itens.get(0).getSolicitacaoCompra().getId();
            solicitacaoCompraService.atualizarStatusSolicitacao(solicitacaoId, "Aguardando Orçamento");
            simularCriacaoOrcamentosPorItem(itens);
        }
    }

    @Transactional
    private void simularCriacaoOrcamentosPorItem(List<ItemSolicitacaoCompra> itens) {
        for (ItemSolicitacaoCompra item : itens) {
            Optional<Fornecedor> fornecedor1 = fornecedorService.buscarFornecedorPorId(1L);
            Optional<Fornecedor> fornecedor2 = fornecedorService.buscarFornecedorPorId(2L);

            if (fornecedor1 != null) {
                Orcamento orcamento1 = new Orcamento();
                orcamento1.setSolicitacaoCompra(item.getSolicitacaoCompra());
                orcamento1.setFornecedor(fornecedor1);
                orcamento1.setDataCotacao(LocalDate.now());
                orcamento1.setValorTotal(Math.random() * 50 + 50);
                orcamento1.setNumeroOrcamento("ORC_" + item.getId() + "_1");
                orcamentoService.salvarOrcamento(orcamento1);
            }

            if (fornecedor2 != null) {
                Orcamento orcamento2 = new Orcamento();
                orcamento2.setSolicitacaoCompra(item.getSolicitacaoCompra());
                orcamento2.setFornecedor(fornecedor2);
                orcamento2.setDataCotacao(LocalDate.now().plusDays(1));
                orcamento2.setValorTotal(Math.random() * 60 + 60);
                orcamento2.setNumeroOrcamento("ORC_" + item.getId() + "_2");
                orcamentoService.salvarOrcamento(orcamento2);
            }
            item.setStatus("Orçamento Solicitado");
            solicitacaoCompraService.atualizarSolicitacao(item.getSolicitacaoCompra());
        }

        if (!itens.isEmpty()) {
            solicitacaoCompraService.atualizarStatusSolicitacao(itens.get(0).getSolicitacaoCompra().getId(),
                    "Orçamentos Recebidos");
        }
    }

    @Transactional
    public void aprovarOrcamentoGerarPedido(Long orcamentoId) {
        Optional<Orcamento> orcamentoOptional = orcamentoService.buscarOrcamentoPorId(orcamentoId);
        if (orcamentoOptional.isPresent()) {
            Orcamento orcamentoSelecionado = orcamentoOptional.get();
            orcamentoSelecionado.setStatus("Selecionado");
            orcamentoService.salvarOrcamento(orcamentoSelecionado);

            SolicitacaoCompra solicitacao = orcamentoSelecionado.getSolicitacaoCompra();
            solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Compra Aprovada");

            gerarOrdemDeCompra(solicitacao, orcamentoSelecionado);
        } else {
            System.out.println("Orçamento não encontrado com ID: " + orcamentoId);
        }
    }

    @Transactional
    private void gerarOrdemDeCompra(SolicitacaoCompra solicitacao, Orcamento orcamentoSelecionado) {
        PedidoCompra pedidoCompra = new PedidoCompra();
        pedidoCompra.setFornecedor(orcamentoSelecionado.getFornecedor());
        pedidoCompra.setDataPedido(LocalDate.now());
        pedidoCompra.setStatus("Pendente");
        pedidoCompra.setValorTotal(orcamentoSelecionado.getValorTotal());

        List<ItemPedidoCompra> itensPedido = solicitacao.getItens().stream()
                .filter(itemSolicitacao -> "Orçamento Solicitado".equalsIgnoreCase(itemSolicitacao.getStatus()) ||
                        "Compra Autorizada".equalsIgnoreCase(itemSolicitacao.getStatus()))
                .map(itemSolicitacao -> {
                    ItemPedidoCompra itemPedido = new ItemPedidoCompra();
                    itemPedido.setMaterialServico(itemSolicitacao.getMaterialServico());
                    itemPedido.setQuantidade(itemSolicitacao.getQuantidade());
                    itemPedido.setPrecoUnitario(itemSolicitacao.getQuantidade() > 0
                            ? (orcamentoSelecionado.getValorTotal() / itemSolicitacao.getQuantidade())
                            : 0.0);
                    itemPedido.setPedidoCompra(pedidoCompra);
                    return itemPedido;
                })
                .toList();
        pedidoCompra.setItens(itensPedido);

        PedidoCompra novoPedido = pedidoCompraService.criarPedidoCompra(pedidoCompra);
        solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Gerada OC");

        solicitacao.getItens().forEach(itemSolicitacao -> {
            if (itensPedido.stream()
                    .anyMatch(ip -> ip.getMaterialServico().equals(itemSolicitacao.getMaterialServico()))) {
                itemSolicitacao.setStatus("Em Pedido");
            }
        });
        solicitacaoCompraService.atualizarSolicitacao(solicitacao);
    }

    @Transactional
    public void enviarOrdemDeCompra(Long pedidoId) {
        pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Enviado Fornecedor");
    }

    @Transactional
    public void gerenciarEntregaRecebimento(Long pedidoId) {
        pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Recebido");
    }

    @Transactional
    public void enviarParaObra(Long pedidoId) {
        pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Enviada Obra");
    }

    @Transactional
    public void atualizarEstoqueRecebimento(Long pedidoId) {
        Optional<PedidoCompra> pedidoOptional = pedidoCompraService.buscarPedidoCompraPorId(pedidoId);
        pedidoOptional.ifPresent(pedido -> {
            if ("Recebido".equals(pedido.getStatus())) {
                pedido.getItens().forEach(item -> estoqueService.receberMaterial(item.getMaterialServico(),
                        item.getQuantidade(), "PEDIDO_" + pedido.getId()));
                pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Estoque Atualizado");

                Optional<SolicitacaoCompra> optSolicitacao = pedido.getItens().stream()
                        .findFirst()
                        .map(ItemPedidoCompra::getMaterialServico)
                        .map(materialServico -> itemSolicitacaoCompraService
                                .buscarItemSolicitacaoPorMaterialServico(materialServico))
                        .map(optional -> optional.orElse(null))
                        .filter(obj -> obj != null)
                        .filter(obj -> obj instanceof ItemSolicitacaoCompra)
                        .map(obj -> ((ItemSolicitacaoCompra) obj).getSolicitacaoCompra());

                optSolicitacao.ifPresent(solicitacao -> {
                    boolean todosItensRecebidos = solicitacao.getItens().stream()
                            .allMatch(item -> "Em Pedido".equalsIgnoreCase(item.getStatus())
                                    || "Recebido".equalsIgnoreCase(item.getStatus()));
                    if (todosItensRecebidos) {
                        solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Concluída");
                    }
                });
            }
        });
    }

    @Transactional
    public void processoPrestacaoContas(Long pedidoId, String numeroNotaFiscal, LocalDate dataNotaFiscal,
            Double valorNota) {
        pedidoCompraService.buscarPedidoCompraPorId(pedidoId).ifPresent(pedido -> {
            // pedido.setNumeroNotaFiscal(numeroNotaFiscal);
            // pedido.setDataNotaFiscal(dataNotaFiscal);
            // pedido.setValorNotaFiscal(valorNota);
            pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Contabilizado");
            System.out.println("Pedido " + pedidoId + " contabilizado com NF: " + numeroNotaFiscal);
        });
    }
}
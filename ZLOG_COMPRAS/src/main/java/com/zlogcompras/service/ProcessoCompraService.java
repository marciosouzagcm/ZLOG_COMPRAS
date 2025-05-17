package com.zlogcompras.service;

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
public class ProcessoCompraService { // Remova o "<FornecedorService>" aqui

    @Autowired
    private SolicitacaoCompraService solicitacaoCompraService;

    @Autowired
    private ItemSolicitacaoCompraService itemSolicitacaoCompraService;

    @Autowired
    private PedidoCompraService pedidoCompraService;

    @Autowired
    private OrcamentoService orcamentoService;

    @Autowired
    private FornecedorService fornecedorService; // Adicione o FornecedorService

    @Autowired
    private EstoqueService estoqueService;

    // Método para iniciar o processo de compra quando um item não está em estoque
    // (chamado pelo EstoqueService)
    public void iniciarProcessoCompra(ItemSolicitacaoCompra itemSolicitacao) {
        SolicitacaoCompra solicitacao = itemSolicitacao.getSolicitacaoCompra();
        solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Compra Autorizada");
        solicitarOrcamentos(List.of(itemSolicitacao)); // Solicita orçamento apenas para o item atual
    }

    // Método para iniciar o processo de compra para uma solicitação inteira
    // (chamado pelo ProcessoCompraController)
    public void iniciarProcessoCompraPorSolicitacaoId(Long solicitacaoId) {
        SolicitacaoCompra solicitacao = solicitacaoCompraService.buscarSolicitacaoPorId(solicitacaoId);
        if (solicitacao != null) {
            // Buscar todos os itens da solicitação que estão aguardando compra
            List<ItemSolicitacaoCompra> itensParaComprar = itemSolicitacaoCompraService
                    .buscarItensPorSolicitacaoId(solicitacaoId)
                    .stream()
                    .filter(item -> "Aguardando Compra".equals(item.getStatus()))
                    .toList();

            if (!itensParaComprar.isEmpty()) {
                solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Compra em Andamento");
                solicitarOrcamentos(itensParaComprar); // Solicita orçamentos para cada item pendente
            } else {
                System.out.println("Não há itens aguardando compra na solicitação " + solicitacaoId);
            }
        } else {
            System.out.println("Solicitação de compra não encontrada com ID: " + solicitacaoId);
        }
    }

    // Método para solicitar orçamentos para uma lista de itens
    public void solicitarOrcamentos(List<ItemSolicitacaoCompra> itens) {
        // Para cada item, vamos simular a criação de alguns orçamentos
        if (!itens.isEmpty()) {
            SolicitacaoCompra solicitacao = itens.get(0).getSolicitacaoCompra(); // Assumindo que todos os itens são da mesma solicitação
            solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Aguardando Orçamento"); // Atualiza o status da solicitação
            simularCriacaoOrcamentosPorItem(itens);
        }
    }

    // Simulação da criação de orçamentos por item
    private void simularCriacaoOrcamentosPorItem(List<ItemSolicitacaoCompra> itens) {
        for (ItemSolicitacaoCompra item : itens) {
            // Simula a obtenção de orçamentos de diferentes fornecedores para o item
            Fornecedor fornecedor1 = fornecedorService.buscarFornecedorPorId(1L); // Busca o fornecedor pelo ID
            if (fornecedor1 != null) {
                Orcamento orcamento1 = new Orcamento();
                orcamento1.setSolicitacaoCompra(item.getSolicitacaoCompra());
                orcamento1.setFornecedor(fornecedor1);
                orcamento1.setDataCotacao(java.time.LocalDate.now());
                // Aqui você precisaria criar e associar os ItemOrcamento ao orçamento
                // e calcular o valor total baseado neles. Por simplicidade na correção,
                // vamos manter um valor aleatório no nível do orçamento por enquanto.
                // O ideal seria que o valor total fosse calculado no convertToDto do Controller.
                orcamento1.setValorTotal(Math.random() * 50 + 50);
                orcamento1.setNumeroOrcamento("ORC_" + item.getId() + "_1");
                orcamentoService.salvarOrcamento(orcamento1);
            }

            Fornecedor fornecedor2 = fornecedorService.buscarFornecedorPorId(2L); // Busca outro fornecedor pelo ID
            if (fornecedor2 != null) {
                Orcamento orcamento2 = new Orcamento();
                orcamento2.setSolicitacaoCompra(item.getSolicitacaoCompra());
                orcamento2.setFornecedor(fornecedor2);
                orcamento2.setDataCotacao(java.time.LocalDate.now().plusDays(1));
                orcamento2.setValorTotal(Math.random() * 60 + 60);
                orcamento2.setNumeroOrcamento("ORC_" + item.getId() + "_2");
                orcamentoService.salvarOrcamento(orcamento2);
            }
        }
        // Após simular os orçamentos para todos os itens, podemos atualizar o status da solicitação
        if (!itens.isEmpty()) {
            solicitacaoCompraService.atualizarStatusSolicitacao(itens.get(0).getSolicitacaoCompra().getId(),
                    "Orçamentos Recebidos");
        }
    }

    // Método para aprovar um orçamento e gerar uma ordem de compra
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
        }
    }

    // Método para gerar a ordem de compra com base no orçamento selecionado
    private void gerarOrdemDeCompra(SolicitacaoCompra solicitacao, Orcamento orcamentoSelecionado) {
        PedidoCompra pedidoCompra = new PedidoCompra();
        pedidoCompra.setFornecedor(orcamentoSelecionado.getFornecedor());
        // Outros dados do pedido de compra podem ser definidos aqui

        List<ItemPedidoCompra> itensPedido = orcamentoSelecionado.getItensOrcamento().stream()
                .map(itemOrcamento -> {
                    ItemPedidoCompra itemPedido = new ItemPedidoCompra();
                    itemPedido.setMaterialServico(itemOrcamento.getMaterialServico());
                    itemPedido.setQuantidade(itemOrcamento.getQuantidade());
                    itemPedido.setPrecoUnitario(itemOrcamento.getPrecoUnitario());
                    itemPedido.setPedidoCompra(pedidoCompra);
                    return itemPedido;
                })
                .toList();
        pedidoCompra.setItens(itensPedido);

        PedidoCompra novoPedido = pedidoCompraService.criarPedidoCompra(pedidoCompra);
        solicitacaoCompraService.atualizarStatusSolicitacao(solicitacao.getId(), "Gerada OC");
        // Atualizar status dos itens de solicitação conforme necessário
    }

    // Método para simular o envio da ordem de compra ao fornecedor
    public void enviarOrdemDeCompra(Long pedidoId) {
        pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Enviado Fornecedor");
    }

    // Método para gerenciar a entrega e o recebimento
    public void gerenciarEntregaRecebimento(Long pedidoId) {
        pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Recebido");
    }

    // Método para enviar para a obra
    public void enviarParaObra(Long pedidoId) {
        pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Enviada Obra");
    }

    // Método para atualizar o estoque após o recebimento
    public void atualizarEstoqueRecebimento(Long pedidoId) {
        PedidoCompra pedido = pedidoCompraService.buscarPedidoCompraPorId(pedidoId).orElse(null);
        if (pedido != null && pedido.getStatus().equals("Recebido")) {
            pedido.getItens().forEach(item -> estoqueService.receberMaterial(item.getMaterialServico(),
                    item.getQuantidade(), "PEDIDO_" + pedido.getId()));
            pedidoCompraService.atualizarStatusPedidoCompra(pedidoId, "Estoque Atualizado");
        }
    }

    // Método para o processo de prestação de contas
    public void processoPrestacaoContas(Long pedidoId, String numeroNotaFiscal, java.time.LocalDate dataNotaFiscal,
            Double valorNota) {
        // Lógica para registrar a nota fiscal e associá-la ao pedido
        // Atualizar o status do pedido para "Contabilizado"
    }
}
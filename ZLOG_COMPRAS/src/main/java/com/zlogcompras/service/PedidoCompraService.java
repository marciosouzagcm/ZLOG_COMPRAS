package com.zlogcompras.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.ItemPedidoCompra;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.StatusPedidoCompra; // Assumindo que você tenha um Enum para status
import com.zlogcompras.repository.PedidoCompraRepository;

@Service
public class PedidoCompraService {

    @Autowired
    private PedidoCompraRepository pedidoCompraRepository;

    // itemPedidoCompraRepository pode ser removido se todas as manipulações de
    // itens
    // forem feitas através do relacionamento em cascata na entidade PedidoCompra.
    // Mantenha se você ainda precisar de operações diretas em ItemPedidoCompra.
    // @Autowired
    // private ItemPedidoCompraRepository itemPedidoCompraRepository;

    public List<PedidoCompra> listarTodosPedidosCompra() {
        return pedidoCompraRepository.findAll();
    }

    public Optional<PedidoCompra> buscarPedidoCompraPorId(Long id) {
        return pedidoCompraRepository.findById(id);
    }

    @Transactional
    public PedidoCompra criarPedidoCompra(PedidoCompra pedidoCompra) {
        pedidoCompra.setDataPedido(LocalDate.now());
        if (pedidoCompra.getStatus() == null) {
            pedidoCompra.setStatus(StatusPedidoCompra.PENDENTE.name());
        }

        // Se a entidade PedidoCompra tem setItens que gerencia bidirecionalidade
        // e CascadeType.PERSIST está configurado, o JPA persistirá os itens
        // automaticamente.
        // Não é necessário manipular a lista após o save inicial aqui.
        // O método setItens na entidade já se encarregará de adicionar os itens
        // corretamente.
        return pedidoCompraRepository.save(pedidoCompra);
    }

    /**
     * Método para criar um PedidoCompra a partir de um Orcamento aprovado.
     * Este é o método que o OrcamentoService deve chamar.
     * 
     * @param orcamentoAprovado O orçamento que foi aprovado.
     * @return O PedidoCompra recém-criado.
     */
    @Transactional
    public PedidoCompra criarPedidoCompraDoOrcamento(Orcamento orcamentoAprovado) {
        PedidoCompra novoPedido = new PedidoCompra();
        novoPedido.setOrcamento(orcamentoAprovado);
        novoPedido.setDataPedido(LocalDate.now());
        novoPedido.setFornecedor(orcamentoAprovado.getFornecedor());
        novoPedido.setValorTotal(orcamentoAprovado.getValorTotal()); // Linha 70: orcamentoAprovado.getValorTotal() é
                                                                     // BigDecimal, agora setValorTotal espera
                                                                     // BigDecimal
        novoPedido.setStatus(StatusPedidoCompra.PENDENTE.name());
        // novoPedido.setObservacoes(orcamentoAprovado.getObservacoes()); // Se houver
        // observações no Orcamento e no PedidoCompra

        if (orcamentoAprovado.getItensOrcamento() != null && !orcamentoAprovado.getItensOrcamento().isEmpty()) {
            for (ItemOrcamento itemOrcamento : orcamentoAprovado.getItensOrcamento()) {
                ItemPedidoCompra itemPedido = new ItemPedidoCompra();
                itemPedido.setProduto(itemOrcamento.getProduto());
                itemPedido.setQuantidade(itemOrcamento.getQuantidade());
                itemPedido.setPrecoUnitario(itemOrcamento.getPrecoUnitarioCotado());
                itemPedido.setObservacoes(itemOrcamento.getObservacoes());

                novoPedido.addItem(itemPedido); // Usa o método addItem da entidade para associar
            }
        }
        return criarPedidoCompra(novoPedido); // Reutiliza o método de criação para persistir
    }

    @Transactional
    public PedidoCompra atualizarPedidoCompra(PedidoCompra pedidoCompraAtualizado) {
        PedidoCompra pedidoExistente = pedidoCompraRepository.findById(pedidoCompraAtualizado.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido de compra não encontrado com ID: " + pedidoCompraAtualizado.getId()));

        pedidoExistente.setFornecedor(pedidoCompraAtualizado.getFornecedor());
        pedidoExistente.setStatus(pedidoCompraAtualizado.getStatus());
        pedidoExistente.setValorTotal(pedidoCompraAtualizado.getValorTotal());
        pedidoExistente.setObservacoes(pedidoCompraAtualizado.getObservacoes());

        // **Simplificado com orphanRemoval=true e CascadeType.ALL na entidade
        // PedidoCompra**
        // A lógica de setItens na entidade PedidoCompra (que limpa e adiciona)
        // junto com o cascade e orphanRemoval do JPA, lida com a sincronização.
        pedidoExistente.setItens(pedidoCompraAtualizado.getItens());

        return pedidoCompraRepository.save(pedidoExistente);
    }

    public void atualizarStatusPedidoCompra(Long id, String novoStatus) {
        Optional<PedidoCompra> pedidoExistente = pedidoCompraRepository.findById(id);
        pedidoExistente.ifPresent(pedido -> {
            pedido.setStatus(novoStatus);
            pedidoCompraRepository.save(pedido);
        });
    }
}
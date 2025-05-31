package com.zlogcompras.service;

import java.time.LocalDate;
import java.util.ArrayList; // Removido se você prefere Set em alguns lugares
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.ItemPedidoCompra;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.StatusPedidoCompra;
import com.zlogcompras.repository.ItemPedidoCompraRepository; // Assumindo que você tenha um Enum para status
import com.zlogcompras.repository.PedidoCompraRepository;

@Service
public class PedidoCompraService {

    @Autowired
    private PedidoCompraRepository pedidoCompraRepository;

    @Autowired
    private ItemPedidoCompraRepository itemPedidoCompraRepository;

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
            pedidoCompra.setStatus(StatusPedidoCompra.PENDENTE.name()); // Correto
        }

        PedidoCompra novoPedido = pedidoCompraRepository.save(pedidoCompra);

        if (pedidoCompra.getItens() != null && !pedidoCompra.getItens().isEmpty()) {
            // A entidade PedidoCompra tem um método addItem que gerencia a bidirecionalidade
            // e como getItens() retorna List, é melhor passar List para setItens
            novoPedido.setItens(new ArrayList<>(pedidoCompra.getItens())); // Garante que a lista do novoPedido seja populada corretamente
        }

        return novoPedido;
    }

    /**
     * Método para criar um PedidoCompra a partir de um Orcamento aprovado.
     * Este é o método que o OrcamentoService deve chamar.
     * @param orcamentoAprovado O orçamento que foi aprovado.
     * @return O PedidoCompra recém-criado.
     */
    @Transactional
    public PedidoCompra criarPedidoCompraDoOrcamento(Orcamento orcamentoAprovado) {
        PedidoCompra novoPedido = new PedidoCompra();
        novoPedido.setOrcamento(orcamentoAprovado);
        novoPedido.setDataPedido(LocalDate.now());
        novoPedido.setFornecedor(orcamentoAprovado.getFornecedor());
        novoPedido.setValorTotal(orcamentoAprovado.getValorTotal());
        novoPedido.setStatus(StatusPedidoCompra.PENDENTE.name()); // Correto
        // Se Orcamento tiver observações e PedidoCompra também, você pode mapear aqui:
        // novoPedido.setObservacoes(orcamentoAprovado.getObservacoes()); // Se houver observações no Orcamento e no PedidoCompra

        if (orcamentoAprovado.getItensOrcamento() != null && !orcamentoAprovado.getItensOrcamento().isEmpty()) {
            List<ItemPedidoCompra> itensDoPedido = new ArrayList<>();
            for (ItemOrcamento itemOrcamento : orcamentoAprovado.getItensOrcamento()) {
                ItemPedidoCompra itemPedido = new ItemPedidoCompra();
                itemPedido.setProduto(itemOrcamento.getProduto());
                itemPedido.setQuantidade(itemOrcamento.getQuantidade());
                itemPedido.setPrecoUnitario(itemOrcamento.getPrecoUnitarioCotado());
                // Mapeia observações do itemOrcamento para itemPedido, se existirem
                itemPedido.setObservacoes(itemOrcamento.getObservacoes());

                itensDoPedido.add(itemPedido);
            }
            novoPedido.setItens(itensDoPedido);
        }

        return criarPedidoCompra(novoPedido);
    }

    @Transactional
    public PedidoCompra atualizarPedidoCompra(PedidoCompra pedidoCompraAtualizado) {
        PedidoCompra pedidoExistente = pedidoCompraRepository.findById(pedidoCompraAtualizado.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido de compra não encontrado com ID: " + pedidoCompraAtualizado.getId()));

        pedidoExistente.setFornecedor(pedidoCompraAtualizado.getFornecedor());
        pedidoExistente.setStatus(pedidoCompraAtualizado.getStatus());
        pedidoExistente.setValorTotal(pedidoCompraAtualizado.getValorTotal());
        pedidoExistente.setObservacoes(pedidoCompraAtualizado.getObservacoes());

        // Lógica para sincronizar itens: usar Lists e a lógica de equals/hashCode.
        // A entidade PedidoCompra já usa List, vamos manipular como List e usar os métodos addItem/removeItem
        List<ItemPedidoCompra> itensAtuais = pedidoExistente.getItens(); // Isso retorna a lista gerenciada pelo JPA
        List<ItemPedidoCompra> itensRecebidos = pedidoCompraAtualizado.getItens() != null ? pedidoCompraAtualizado.getItens() : new ArrayList<>();

        // 1. Identificar itens para remover (estão em itensAtuais, mas não em itensRecebidos)
        Set<ItemPedidoCompra> itensRecebidosSet = new HashSet<>(itensRecebidos); // Para busca eficiente
        itensAtuais.removeIf(itemAtual -> {
            boolean shouldRemove = !itensRecebidosSet.contains(itemAtual); // Usa equals/hashCode para comparar
            if (shouldRemove) {
                itemAtual.setPedidoCompra(null); // Desvincula para orphanRemoval funcionar
                itemPedidoCompraRepository.delete(itemAtual); // Exclui do banco
            }
            return shouldRemove;
        });

        // 2. Atualizar ou adicionar itens (percorrer itensRecebidos)
        for (ItemPedidoCompra itemNovoOuAtualizado : itensRecebidos) {
            Optional<ItemPedidoCompra> itemExistenteOpt = itensAtuais.stream()
                .filter(itemAtual -> itemAtual.getId() != null && itemAtual.getId().equals(itemNovoOuAtualizado.getId()))
                .findFirst();

            if (itemExistenteOpt.isPresent()) {
                // Atualiza item existente
                ItemPedidoCompra itemExistente = itemExistenteOpt.get();
                itemExistente.setProduto(itemNovoOuAtualizado.getProduto());
                itemExistente.setQuantidade(itemNovoOuAtualizado.getQuantidade());
                itemExistente.setPrecoUnitario(itemNovoOuAtualizado.getPrecoUnitario());
                itemExistente.setObservacoes(itemNovoOuAtualizado.getObservacoes());
                itemPedidoCompraRepository.save(itemExistente);
            } else {
                // Adiciona novo item
                pedidoExistente.addItem(itemNovoOuAtualizado); // Adiciona e já associa bidirecionalmente
                // Não precisa salvar o item individualmente aqui se o CascadeType.ALL estiver configurado corretamente no PedidoCompra
                // Mas, para garantir a consistência e que o item tenha ID antes do save do pai, vamos salvar aqui.
                itemPedidoCompraRepository.save(itemNovoOuAtualizado);
            }
        }
        // O `pedidoExistente.setItens` ou `pedidoExistente.addItem` já manipula a lista gerenciada.
        // Ao final da transação, as alterações serão persistidas.
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
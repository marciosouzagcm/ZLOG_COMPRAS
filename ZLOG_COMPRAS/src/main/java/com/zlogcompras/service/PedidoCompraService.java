package com.zlogcompras.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlogcompras.model.ItemPedidoCompra;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.PedidoCompra; // Import correto
import com.zlogcompras.repository.ItemPedidoCompraRepository;
import com.zlogcompras.repository.PedidoCompraRepository; // Import correto

@Service
public class PedidoCompraService {

    @Autowired
    private PedidoCompraRepository pedidoCompraRepository;

    @Autowired
    private ItemPedidoCompraRepository itemPedidoCompraRepository;
    // REMOVA OU INICIALIZE esta linha se não for usada, ou se for, ajuste o uso.
    // private PedidoCompra pedidoAtualizado; // <--- Esta linha está causando erro

    // No futuro, podemos precisar de FornecedorService para validar o fornecedor

    public List<PedidoCompra> listarTodosPedidosCompra() {
        return pedidoCompraRepository.findAll();
    }

    public Optional<PedidoCompra> buscarPedidoCompraPorId(Long id) {
        return pedidoCompraRepository.findById(id);
    }

    @Transactional
    public PedidoCompra criarPedidoCompra(PedidoCompra pedidoCompra) {
        pedidoCompra.setDataPedido(LocalDate.now());
        pedidoCompra.setStatus("Pendente"); // Status inicial do pedido
        PedidoCompra novoPedido = pedidoCompraRepository.save(pedidoCompra);

        if (pedidoCompra.getItens() != null && !pedidoCompra.getItens().isEmpty()) {
            for (ItemPedidoCompra item : pedidoCompra.getItens()) {
                item.setPedidoCompra(novoPedido);
                // Salvar o item de pedido
                itemPedidoCompraRepository.save(item);
            }
        }

        return novoPedido;
    }

    @Transactional
    public PedidoCompra atualizarPedidoCompra(PedidoCompra pedidoCompraAtualizado) {
        PedidoCompra pedidoExistente = pedidoCompraRepository.findById(pedidoCompraAtualizado.getId())
                .orElseThrow(() -> new RuntimeException("Pedido de compra não encontrado com ID: " + pedidoCompraAtualizado.getId()));

        // Atualize os campos do pedidoExistente com os valores de pedidoCompraAtualizado
        pedidoExistente.setFornecedor(pedidoCompraAtualizado.getFornecedor());
        pedidoExistente.setStatus(pedidoCompraAtualizado.getStatus());
        pedidoExistente.setValorTotal(pedidoCompraAtualizado.getValorTotal());
        // Mantenha a data original de criação se você não quiser que ela seja atualizada:
        // pedidoExistente.setDataPedido(pedidoExistente.getDataPedido());
        // OU, se a dataPedido puder ser atualizada, use:
        // pedidoExistente.setDataPedido(pedidoCompraAtualizado.getDataPedido());

        // Para os itens, você precisará de uma lógica mais elaborada para sincronizar:
        // 1. Remover itens que não estão mais na lista atualizada
        // 2. Atualizar itens existentes
        // 3. Adicionar novos itens
        // Por exemplo:
        pedidoExistente.getItens().clear(); // Limpa os itens existentes
        if (pedidoCompraAtualizado.getItens() != null) {
            for (ItemPedidoCompra item : pedidoCompraAtualizado.getItens()) {
                pedidoExistente.addItem(item); // Usa o método addItem que garante a bidirecionalidade
            }
        }


        // Salve o pedido existente (que agora está atualizado)
        return pedidoCompraRepository.save(pedidoExistente);
    }

    public void atualizarStatusPedidoCompra(Long id, String novoStatus) {
        Optional<PedidoCompra> pedidoExistente = pedidoCompraRepository.findById(id);
        pedidoExistente.ifPresent(pedido -> {
            pedido.setStatus(novoStatus);
            pedidoCompraRepository.save(pedido);
        });
    }

    // Métodos para buscar pedidos por fornecedor, status, etc., podem ser adicionados aqui

    // Este método está lançando uma exceção de "não suportado".
    // Se ele não é necessário ou não será implementado, pode ser removido.
    // Se for necessário, precisa ser implementado.
    void atualizarPedido(PedidoCompra pedido) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void criarPedidoCompra(Orcamento orcamentoAprovado) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
package com.zlogcompras.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import com.zlogcompras.model.dto.ItemPedidoCompraRequestDTO;
import com.zlogcompras.model.dto.PedidoCompraRequestDTO;
import com.zlogcompras.repository.FornecedorRepository;
import com.zlogcompras.repository.PedidoCompraRepository;
import com.zlogcompras.repository.ProdutoRepository;

@Service
public class PedidoCompraService {

    @Autowired
    private PedidoCompraRepository pedidoCompraRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // Conjunto de status válidos para validação
    private static final Set<String> STATUS_VALIDOS = Set.of(
            StatusPedidoCompra.PENDENTE.name(),
            StatusPedidoCompra.APROVADO.name(),
            StatusPedidoCompra.REJEITADO.name(),
            StatusPedidoCompra.ENVIADO.name(),
            StatusPedidoCompra.RECEBIDO.name(), // Corrigido para RECEBIDO
            StatusPedidoCompra.CANCELADO.name());

    // --- Listar todos os pedidos de compra ---
    public List<PedidoCompra> listarTodosPedidosCompra() {
        return pedidoCompraRepository.findAll();
    }

    // --- Buscar pedido de compra por ID ---
    public Optional<PedidoCompra> buscarPedidoCompraPorId(Long id) {
        return pedidoCompraRepository.findById(id);
    }

    // --- Criar um novo PedidoCompra a partir de um DTO de requisição ---
    @Transactional
    public PedidoCompra criarPedidoCompra(PedidoCompraRequestDTO pedidoRequestDTO) {
        PedidoCompra novoPedido = new PedidoCompra();

        novoPedido.setFornecedor(fornecedorRepository.findById(pedidoRequestDTO.getFornecedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Fornecedor não encontrado com ID: " + pedidoRequestDTO.getFornecedorId())));

        novoPedido.setDataPedido(Optional.ofNullable(pedidoRequestDTO.getDataPedido()).orElse(LocalDate.now()));

        String statusFromDTO = pedidoRequestDTO.getStatus();
        if (statusFromDTO != null && !statusFromDTO.isBlank() && STATUS_VALIDOS.contains(statusFromDTO.toUpperCase())) {
            novoPedido.setStatus(statusFromDTO.toUpperCase());
        } else {
            novoPedido.setStatus(StatusPedidoCompra.PENDENTE.name());
        }

        novoPedido.setItens(new ArrayList<>());
        BigDecimal totalCalculado = BigDecimal.ZERO;

        if (pedidoRequestDTO.getItens() != null && !pedidoRequestDTO.getItens().isEmpty()) {
            for (ItemPedidoCompraRequestDTO itemDTO : pedidoRequestDTO.getItens()) {
                ItemPedidoCompra itemPedido = new ItemPedidoCompra();

                itemPedido.setProduto(produtoRepository.findById(itemDTO.getProdutoId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Produto não encontrado com ID: " + itemDTO.getProdutoId())));

                itemPedido.setQuantidade(itemDTO.getQuantidade());

                BigDecimal precoUnitario = Optional.ofNullable(itemDTO.getPrecoUnitario())
                        .orElse(itemPedido.getProduto().getPrecoUnitario());
                itemPedido.setPrecoUnitario(precoUnitario);

                // CORREÇÃO: Conversão de Integer para BigDecimal para a multiplicação
                itemPedido.setSubtotal(precoUnitario.multiply(new BigDecimal(itemDTO.getQuantidade())));

                itemPedido.setNomeProduto(itemDTO.getNomeProduto());
                itemPedido.setCodigoProduto(itemDTO.getCodigoProduto());
                itemPedido.setUnidadeMedida(itemDTO.getUnidadeMedida());
                itemPedido.setObservacoes(itemDTO.getObservacoes());

                novoPedido.addItem(itemPedido);
                totalCalculado = totalCalculado.add(itemPedido.getSubtotal());
            }
        }
        novoPedido.setValorTotal(totalCalculado);

        return pedidoCompraRepository.save(novoPedido);
    }

    // --- Criar um PedidoCompra a partir de um Orçamento aprovado ---
    @Transactional
    public PedidoCompra criarPedidoCompraDoOrcamento(Orcamento orcamentoAprovado) {
        Objects.requireNonNull(orcamentoAprovado, "O orçamento aprovado não pode ser nulo.");
        if (orcamentoAprovado.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do orçamento é obrigatório.");
        }

        PedidoCompra novoPedido = new PedidoCompra();
        novoPedido.setOrcamento(orcamentoAprovado);
        novoPedido.setDataPedido(LocalDate.now());
        novoPedido.setFornecedor(orcamentoAprovado.getFornecedor());
        novoPedido.setStatus(StatusPedidoCompra.PENDENTE.name());

        BigDecimal totalCalculado = BigDecimal.ZERO;
        if (orcamentoAprovado.getItensOrcamento() != null && !orcamentoAprovado.getItensOrcamento().isEmpty()) {
            for (ItemOrcamento itemOrcamento : orcamentoAprovado.getItensOrcamento()) {
                ItemPedidoCompra itemPedido = new ItemPedidoCompra();
                itemPedido.setProduto(itemOrcamento.getProduto());

                // Assumindo que itemOrcamento.getQuantidade() é BigDecimal e
                // ItemPedidoCompra.quantidade é Integer.
                // Se itemOrcamento.getQuantidade() já for Integer, remova o .intValue().
                itemPedido.setQuantidade(itemOrcamento.getQuantidade().intValue());

                itemPedido.setPrecoUnitario(itemOrcamento.getPrecoUnitarioCotado());

                // CORREÇÃO: Multiplica o precoUnitario (BigDecimal) pelo quantidade (Integer,
                // convertido para BigDecimal)
                itemPedido.setSubtotal(itemOrcamento.getPrecoUnitarioCotado()
                        .multiply(new BigDecimal(itemOrcamento.getQuantidade().intValue())));

                itemPedido.setObservacoes(itemOrcamento.getObservacoes());

                if (itemOrcamento.getProduto() != null) {
                    itemPedido.setNomeProduto(itemOrcamento.getProduto().getNome());
                    itemPedido.setCodigoProduto(itemOrcamento.getProduto().getCodigoProduto());
                    itemPedido.setUnidadeMedida(itemOrcamento.getProduto().getUnidadeMedida());
                }

                novoPedido.addItem(itemPedido);
                totalCalculado = totalCalculado.add(itemPedido.getSubtotal());
            }
        }
        novoPedido.setValorTotal(totalCalculado);

        return pedidoCompraRepository.save(novoPedido);
    }

    // --- Atualizar um PedidoCompra existente a partir de um DTO de requisição ---
    @Transactional
    public PedidoCompra atualizarPedidoCompra(Long id, PedidoCompraRequestDTO pedidoRequestDTO) {
        PedidoCompra pedidoExistente = pedidoCompraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido de compra não encontrado com ID: " + id));

        pedidoExistente.setFornecedor(fornecedorRepository.findById(pedidoRequestDTO.getFornecedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Fornecedor não encontrado com ID: " + pedidoRequestDTO.getFornecedorId())));

        pedidoExistente.setDataPedido(
                Optional.ofNullable(pedidoRequestDTO.getDataPedido()).orElse(pedidoExistente.getDataPedido()));

        String statusFromDTO = pedidoRequestDTO.getStatus();
        if (statusFromDTO != null && !statusFromDTO.isBlank()) {
            if (!STATUS_VALIDOS.contains(statusFromDTO.toUpperCase())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + statusFromDTO);
            }
            pedidoExistente.setStatus(statusFromDTO.toUpperCase());
        }

        pedidoExistente.getItens().clear();

        BigDecimal totalCalculado = BigDecimal.ZERO;
        if (pedidoRequestDTO.getItens() != null && !pedidoRequestDTO.getItens().isEmpty()) {
            for (ItemPedidoCompraRequestDTO itemDTO : pedidoRequestDTO.getItens()) {
                ItemPedidoCompra itemPedido = new ItemPedidoCompra();

                itemPedido.setProduto(produtoRepository.findById(itemDTO.getProdutoId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Produto não encontrado com ID: " + itemDTO.getProdutoId())));

                itemPedido.setQuantidade(itemDTO.getQuantidade());

                BigDecimal precoUnitario = Optional.ofNullable(itemDTO.getPrecoUnitario())
                        .orElse(itemPedido.getProduto().getPrecoUnitario());
                itemPedido.setPrecoUnitario(precoUnitario);

                // CORREÇÃO: Multiplicar BigDecimal com Integer (convertido para BigDecimal)
                itemPedido.setSubtotal(precoUnitario.multiply(new BigDecimal(itemDTO.getQuantidade())));

                itemPedido.setNomeProduto(itemDTO.getNomeProduto());
                itemPedido.setCodigoProduto(itemDTO.getCodigoProduto());
                itemPedido.setUnidadeMedida(itemDTO.getUnidadeMedida());
                itemPedido.setObservacoes(itemDTO.getObservacoes());

                pedidoExistente.addItem(itemPedido);
                totalCalculado = totalCalculado.add(itemPedido.getSubtotal());
            }
        }
        pedidoExistente.setValorTotal(totalCalculado);

        return pedidoCompraRepository.save(pedidoExistente);
    }

    // --- Atualizar apenas o status de um pedido de compra ---
    @Transactional
    public void atualizarStatusPedidoCompra(Long id, String novoStatus) {
        if (novoStatus == null || novoStatus.isBlank() || !STATUS_VALIDOS.contains(novoStatus.toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido fornecido: " + novoStatus);
        }

        PedidoCompra pedidoExistente = pedidoCompraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido de compra não encontrado com ID: " + id));

        pedidoExistente.setStatus(novoStatus.toUpperCase());
        pedidoCompraRepository.save(pedidoExistente);
    }

    // --- Buscar pedidos de compra por fornecedor ---
    public List<PedidoCompra> buscarPedidosPorFornecedor(Long fornecedorId) {
        List<PedidoCompra> pedidos = pedidoCompraRepository.findByFornecedorId(fornecedorId);
        if (pedidos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum pedido encontrado para o fornecedor com ID: " + fornecedorId);
        }
        return pedidos;
    }

    // --- Buscar pedidos de compra por status ---
    public List<PedidoCompra> buscarPedidosPorStatus(String status) {
        if (status == null || status.isBlank() || !STATUS_VALIDOS.contains(status.toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido para busca: " + status);
        }
        List<PedidoCompra> pedidos = pedidoCompraRepository.findByStatus(status.toUpperCase());
        if (pedidos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum pedido encontrado com o status: " + status);
        }
        return pedidos;
    }

    // --- Exemplo de método para adicionar itens a um pedido existente (se
    // necessário) ---
    @Transactional
    public PedidoCompra adicionarItens(Long pedidoId, List<ItemPedidoCompraRequestDTO> novosItensDTO) {
        PedidoCompra pedido = pedidoCompraRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido de compra não encontrado com ID: " + pedidoId));

        BigDecimal totalAtualizado = pedido.getValorTotal() != null ? pedido.getValorTotal() : BigDecimal.ZERO;

        for (ItemPedidoCompraRequestDTO itemDTO : novosItensDTO) {
            ItemPedidoCompra novoItem = new ItemPedidoCompra();
            novoItem.setProduto(produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Produto não encontrado com ID: " + itemDTO.getProdutoId())));
            novoItem.setQuantidade(itemDTO.getQuantidade());

            BigDecimal precoUnitario = Optional.ofNullable(itemDTO.getPrecoUnitario())
                    .orElse(novoItem.getProduto().getPrecoUnitario());
            novoItem.setPrecoUnitario(precoUnitario);
            // CORREÇÃO: Multiplicar BigDecimal com Integer (convertido para BigDecimal)
            novoItem.setSubtotal(precoUnitario.multiply(new BigDecimal(itemDTO.getQuantidade())));

            novoItem.setNomeProduto(itemDTO.getNomeProduto());
            novoItem.setCodigoProduto(itemDTO.getCodigoProduto());
            novoItem.setUnidadeMedida(itemDTO.getUnidadeMedida());
            novoItem.setObservacoes(itemDTO.getObservacoes());

            pedido.addItem(novoItem);
            totalAtualizado = totalAtualizado.add(novoItem.getSubtotal());
        }
        pedido.setValorTotal(totalAtualizado);
        return pedidoCompraRepository.save(pedido);
    }

    // --- Deletar um pedido de compra pelo ID ---
    @Transactional
    public void deletar(Long id) {
        PedidoCompra pedido = pedidoCompraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido de compra não encontrado com ID: " + id));
        pedidoCompraRepository.delete(pedido);
    }
}
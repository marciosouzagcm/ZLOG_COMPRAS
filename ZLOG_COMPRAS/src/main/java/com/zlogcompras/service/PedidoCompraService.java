package com.zlogcompras.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.ItemPedidoCompra;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.StatusPedidoCompra; // Importe o enum
import com.zlogcompras.model.dto.ItemPedidoCompraRequestDTO;
import com.zlogcompras.model.dto.PedidoCompraRequestDTO;
import com.zlogcompras.model.dto.PedidoCompraUpdateDTO;
import com.zlogcompras.repository.FornecedorRepository;
import com.zlogcompras.repository.ItemPedidoCompraRepository;
import com.zlogcompras.repository.PedidoCompraRepository;
import com.zlogcompras.repository.ProdutoRepository;

@Service
public class PedidoCompraService {

    private final PedidoCompraRepository pedidoCompraRepository;
    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ItemPedidoCompraRepository itemPedidoCompraRepository;

    @Autowired
    public PedidoCompraService(PedidoCompraRepository pedidoCompraRepository,
                               ProdutoRepository produtoRepository,
                               FornecedorRepository fornecedorRepository,
                               ItemPedidoCompraRepository itemPedidoCompraRepository) {
        this.pedidoCompraRepository = pedidoCompraRepository;
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.itemPedidoCompraRepository = itemPedidoCompraRepository;
    }

    @Transactional
    public PedidoCompra criarPedidoCompra(PedidoCompraRequestDTO requestDTO) {
        PedidoCompra pedidoCompra = new PedidoCompra();

        Fornecedor fornecedor = fornecedorRepository.findById(requestDTO.getFornecedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado."));

        pedidoCompra.setFornecedor(fornecedor);
        pedidoCompra.setDataPedido(LocalDate.now());
        pedidoCompra.setStatus(StatusPedidoCompra.AGUARDANDO_APROVACAO); // Correção: Usando o nome completo do enum
        pedidoCompra.setObservacoes(requestDTO.getObservacoes());

        List<ItemPedidoCompra> itensPedido = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        if (requestDTO.getItens() == null || requestDTO.getItens().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um pedido de compra deve conter itens.");
        }

        for (ItemPedidoCompraRequestDTO itemDTO : requestDTO.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado com ID: " + itemDTO.getProdutoId()));

            ItemPedidoCompra itemPedido = new ItemPedidoCompra();
            itemPedido.setPedidoCompra(pedidoCompra);
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDTO.getQuantidade());
            itemPedido.setPrecoUnitario(itemDTO.getPrecoUnitario());
            itemPedido.setObservacoes(itemDTO.getObservacoes());

            itemPedido.setNomeProduto(produto.getNome());
            itemPedido.setCodigoProduto(produto.getCodigo());
            itemPedido.setUnidadeMedida(produto.getUnidadeMedida());

            BigDecimal subtotal = itemDTO.getPrecoUnitario().multiply(new BigDecimal(itemDTO.getQuantidade()));
            itemPedido.setSubtotal(subtotal);
            valorTotal = valorTotal.add(subtotal);

            itensPedido.add(itemPedido);
        }

        pedidoCompra.setItens(itensPedido);
        pedidoCompra.setValorTotal(valorTotal);

        return pedidoCompraRepository.save(pedidoCompra);
    }

    @Transactional
    public PedidoCompra criarPedidoCompraDoOrcamento(Orcamento orcamento) {
        PedidoCompra pedidoCompra = new PedidoCompra();
        pedidoCompra.setFornecedor(orcamento.getFornecedor());
        pedidoCompra.setDataPedido(LocalDate.now());
        pedidoCompra.setStatus(StatusPedidoCompra.AGUARDANDO_ENVIO); // Correção: Usando o nome completo do enum
        pedidoCompra.setOrcamento(orcamento);

        List<ItemPedidoCompra> itensPedido = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        if (orcamento.getItensOrcamento() == null || orcamento.getItensOrcamento().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O orçamento deve conter itens para gerar um pedido de compra.");
        }

        for (ItemOrcamento itemOrcamento : orcamento.getItensOrcamento()) {
            Produto produto = itemOrcamento.getProduto();
            if (produto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto de item do orçamento não encontrado.");
            }

            ItemPedidoCompra itemPedido = new ItemPedidoCompra();
            itemPedido.setPedidoCompra(pedidoCompra);
            itemPedido.setProduto(produto);
            // Assumindo que getQuantidade() de ItemOrcamento é BigDecimal e setQuantidade() de ItemPedidoCompra aceita int ou BigDecimal.
            // Se ItemPedidoCompra.quantidade for int, certifique-se de que itemOrcamento.getQuantidade() não tem decimais.
            itemPedido.setQuantidade(itemOrcamento.getQuantidade().intValue()); // Ajuste se ItemPedidoCompra.quantidade for BigDecimal
            itemPedido.setPrecoUnitario(itemOrcamento.getPrecoUnitarioCotado());
            itemPedido.setObservacoes(itemOrcamento.getObservacoes());

            itemPedido.setNomeProduto(itemOrcamento.getNomeProduto());
            itemPedido.setCodigoProduto(itemOrcamento.getCodigoProduto());
            itemPedido.setUnidadeMedida(itemOrcamento.getUnidadeMedidaProduto());

            BigDecimal subtotal = itemOrcamento.getPrecoUnitarioCotado().multiply(itemOrcamento.getQuantidade());
            itemPedido.setSubtotal(subtotal);
            valorTotal = valorTotal.add(subtotal);

            itensPedido.add(itemPedido);
        }

        pedidoCompra.setItens(itensPedido);
        pedidoCompra.setValorTotal(valorTotal);

        return pedidoCompraRepository.save(pedidoCompra);
    }

    @Transactional(readOnly = true)
    public List<PedidoCompra> listarTodosPedidos() {
        return pedidoCompraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PedidoCompra buscarPedidoPorId(Long id) {
        return pedidoCompraRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido de Compra não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<PedidoCompra> buscarPedidosPorStatus(StatusPedidoCompra status) {
        return pedidoCompraRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<PedidoCompra> buscarPedidosPorFornecedor(Long fornecedorId) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado."));
        return pedidoCompraRepository.findByFornecedorId(fornecedorId);
    }

    @Transactional
    public PedidoCompra atualizarPedidoCompra(Long id, PedidoCompraUpdateDTO updateDTO) {
        PedidoCompra pedidoExistente = pedidoCompraRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido de Compra não encontrado."));

        if (updateDTO.getFornecedorId() != null
                && !updateDTO.getFornecedorId().equals(pedidoExistente.getFornecedor().getId())) {
            Fornecedor novoFornecedor = fornecedorRepository.findById(updateDTO.getFornecedorId())
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Novo fornecedor não encontrado."));
            pedidoExistente.setFornecedor(novoFornecedor);
        }

        if (updateDTO.getStatus() != null && !updateDTO.getStatus().equals(pedidoExistente.getStatus().name())) {
            try {
                pedidoExistente.setStatus(StatusPedidoCompra.valueOf(updateDTO.getStatus().toUpperCase())); // Correção: Usando o nome completo do enum
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + updateDTO.getStatus());
            }
        }

        Optional.ofNullable(updateDTO.getObservacoes()).ifPresent(pedidoExistente::setObservacoes);

        if (updateDTO.getItens() != null) {
            List<Long> idsItensUpdate = updateDTO.getItens().stream()
                    .filter(itemDTO -> itemDTO.getId() != null)
                    .map(ItemPedidoCompraRequestDTO::getId)
                    .collect(Collectors.toList());

            List<ItemPedidoCompra> itensAtuais = new ArrayList<>(pedidoExistente.getItens());
            itensAtuais.removeIf(existingItem -> !idsItensUpdate.contains(existingItem.getId()));
            pedidoExistente.setItens(new ArrayList<>());
            pedidoExistente.getItens().addAll(itensAtuais);


            BigDecimal novoValorTotal = BigDecimal.ZERO;
            for (ItemPedidoCompraRequestDTO itemDTO : updateDTO.getItens()) {
                if (itemDTO.getId() != null) {
                    ItemPedidoCompra itemExistente = pedidoExistente.getItens().stream()
                            .filter(i -> i.getId().equals(itemDTO.getId()))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Item do pedido não encontrado com ID: " + itemDTO.getId()));

                    Optional.ofNullable(itemDTO.getQuantidade()).ifPresent(q -> {
                        if (q <= 0)
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Quantidade do item deve ser maior que zero.");
                        itemExistente.setQuantidade(q);
                    });
                    Optional.ofNullable(itemDTO.getPrecoUnitario()).ifPresent(p -> {
                        if (p.compareTo(BigDecimal.ZERO) < 0)
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Preço unitário do item não pode ser negativo.");
                        itemExistente.setPrecoUnitario(p);
                    });
                    Optional.ofNullable(itemDTO.getObservacoes()).ifPresent(itemExistente::setObservacoes);

                    itemExistente.setSubtotal(
                            itemExistente.getPrecoUnitario().multiply(new BigDecimal(itemExistente.getQuantidade())));
                } else {
                    Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Produto não encontrado com ID: " + itemDTO.getProdutoId()));

                    ItemPedidoCompra novoItem = new ItemPedidoCompra();
                    novoItem.setPedidoCompra(pedidoExistente);
                    novoItem.setProduto(produto);
                    novoItem.setQuantidade(itemDTO.getQuantidade());
                    novoItem.setPrecoUnitario(itemDTO.getPrecoUnitario());
                    novoItem.setObservacoes(itemDTO.getObservacoes());

                    novoItem.setNomeProduto(produto.getNome());
                    novoItem.setCodigoProduto(produto.getCodigo());
                    novoItem.setUnidadeMedida(produto.getUnidadeMedida());

                    BigDecimal subtotal = itemDTO.getPrecoUnitario().multiply(new BigDecimal(itemDTO.getQuantidade()));
                    novoItem.setSubtotal(subtotal);
                    pedidoExistente.getItens().add(novoItem);
                }
            }

            for (ItemPedidoCompra item : pedidoExistente.getItens()) {
                novoValorTotal = novoValorTotal.add(item.getSubtotal());
            }
            pedidoExistente.setValorTotal(novoValorTotal);
        }

        return pedidoCompraRepository.save(pedidoExistente);
    }

    @Transactional
    public void cancelarPedidoCompra(Long id) {
        PedidoCompra pedido = pedidoCompraRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido de Compra não encontrado."));

        if (pedido.getStatus() == StatusPedidoCompra.CONCLUIDO || // Correção: Usando o nome completo do enum
            pedido.getStatus() == StatusPedidoCompra.CANCELADO) { // Correção: Usando o nome completo do enum
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é possível cancelar um pedido com status: " + pedido.getStatus().getDescricao());
        }

        pedido.setStatus(StatusPedidoCompra.CANCELADO); // Correção: Usando o nome completo do enum
        pedidoCompraRepository.save(pedido);
    }

    @Transactional
    public void deletarPedido(Long id) {
        PedidoCompra pedido = pedidoCompraRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido de Compra não encontrado."));
        pedidoCompraRepository.delete(pedido);
    }

    @Transactional
    public PedidoCompra adicionarItensAoPedido(Long id, List<ItemPedidoCompraRequestDTO> novosItensDto) {
        PedidoCompra pedidoExistente = pedidoCompraRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido de Compra não encontrado."));

        BigDecimal valorTotalAdicional = BigDecimal.ZERO;
        for (ItemPedidoCompraRequestDTO itemDTO : novosItensDto) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado com ID: " + itemDTO.getProdutoId()));

            ItemPedidoCompra novoItem = new ItemPedidoCompra();
            novoItem.setPedidoCompra(pedidoExistente);
            novoItem.setProduto(produto);
            novoItem.setQuantidade(itemDTO.getQuantidade());
            novoItem.setPrecoUnitario(itemDTO.getPrecoUnitario());
            novoItem.setObservacoes(itemDTO.getObservacoes());

            novoItem.setNomeProduto(produto.getNome());
            novoItem.setCodigoProduto(produto.getCodigo());
            novoItem.setUnidadeMedida(produto.getUnidadeMedida());

            BigDecimal subtotal = itemDTO.getPrecoUnitario().multiply(new BigDecimal(itemDTO.getQuantidade()));
            novoItem.setSubtotal(subtotal);
            valorTotalAdicional = valorTotalAdicional.add(subtotal);

            pedidoExistente.getItens().add(novoItem);
        }
        pedidoExistente.setValorTotal(pedidoExistente.getValorTotal().add(valorTotalAdicional));
        return pedidoCompraRepository.save(pedidoExistente);
    }

    @Transactional
    public PedidoCompra atualizarStatusPedido(Long id, StatusPedidoCompra novoStatus) {
        PedidoCompra pedido = pedidoCompraRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido de Compra não encontrado."));
        pedido.setStatus(novoStatus);
        return pedidoCompraRepository.save(pedido);
    }
}
package com.zlogcompras.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set; // Importe Set
import java.util.stream.Collectors; // Importe Collectors

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.ItemPedidoCompra;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.StatusOrcamento;
import com.zlogcompras.model.StatusPedidoCompra; // Certifique-se que está importado!
import com.zlogcompras.model.dto.ItemPedidoCompraRequestDTO;
import com.zlogcompras.model.dto.PedidoCompraRequestDTO;
import com.zlogcompras.repository.FornecedorRepository;
import com.zlogcompras.repository.PedidoCompraRepository;
import com.zlogcompras.repository.ProdutoRepository;

@Service
public class PedidoCompraService {

    private final PedidoCompraRepository pedidoCompraRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;

    // Conjunto de status válidos para validação, agora usando o enum diretamente
    private static final Set<StatusPedidoCompra> STATUS_PERMITIDOS_ATUALIZACAO = Set.of(
            StatusPedidoCompra.PENDENTE,
            StatusPedidoCompra.APROVADO,
            StatusPedidoCompra.REJEITADO,
            StatusPedidoCompra.ENVIADO,
            StatusPedidoCompra.RECEBIDO,
            StatusPedidoCompra.CANCELADO);

    @Autowired
    public PedidoCompraService(PedidoCompraRepository pedidoCompraRepository,
                               FornecedorRepository fornecedorRepository,
                               ProdutoRepository produtoRepository) {
        this.pedidoCompraRepository = pedidoCompraRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
    }

    // --- Listar todos os pedidos de compra ---
    @Transactional(readOnly = true)
    public List<PedidoCompra> listarTodosPedidosCompra() {
        return pedidoCompraRepository.findAll();
    }

    // --- Buscar pedido de compra por ID ---
    @Transactional(readOnly = true)
    public Optional<PedidoCompra> buscarPedidoCompraPorId(Long id) {
        return pedidoCompraRepository.findById(id);
    }

    // --- Criar um novo PedidoCompra a partir de um DTO de requisição ---
    @Transactional
    public PedidoCompra criarPedidoCompra(PedidoCompraRequestDTO pedidoRequestDTO) {
        PedidoCompra novoPedido = new PedidoCompra();

        // Fornecedor é obrigatório
        novoPedido.setFornecedor(fornecedorRepository.findById(pedidoRequestDTO.getFornecedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Fornecedor não encontrado com ID: " + pedidoRequestDTO.getFornecedorId())));

        // Data do pedido sempre é definida pelo serviço como a data atual
        novoPedido.setDataPedido(LocalDate.now());

        // Status inicial é sempre PENDENTE
        novoPedido.setStatus(StatusPedidoCompra.PENDENTE); // <--- CORRIGIDO AQUI! (Atribuindo Enum)

        novoPedido.setItens(new ArrayList<>()); // Inicializa a lista de itens
        BigDecimal totalCalculado = BigDecimal.ZERO;

        if (pedidoRequestDTO.getItens() == null || pedidoRequestDTO.getItens().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O pedido deve conter ao menos um item.");
        }

        for (ItemPedidoCompraRequestDTO itemDTO : pedidoRequestDTO.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado com ID: " + itemDTO.getProdutoId()));

            ItemPedidoCompra itemPedido = new ItemPedidoCompra();
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDTO.getQuantidade());

            // Define o preço unitário cotado, ou usa o preço do produto como fallback
            itemPedido.setPrecoUnitario(itemDTO.getPrecoUnitario());

            // O subtotal é CALCULADO aqui, não vindo do DTO
            BigDecimal quantidadeDecimal = new BigDecimal(itemDTO.getQuantidade());
            itemPedido.setSubtotal(itemDTO.getPrecoUnitario().multiply(quantidadeDecimal));

            // Nome, Código e Unidade de Medida são populados do objeto Produto, não do DTO
            itemPedido.setNomeProduto(produto.getNome());
            itemPedido.setCodigoProduto(produto.getCodigoProduto());
            itemPedido.setUnidadeMedida(produto.getUnidadeMedida());
            itemPedido.setObservacoes(itemDTO.getObservacoes());

            novoPedido.addItem(itemPedido);
            totalCalculado = totalCalculado.add(itemPedido.getSubtotal());
        }
        novoPedido.setValorTotal(totalCalculado);

        return pedidoCompraRepository.save(novoPedido);
    }

    // --- Criar um PedidoCompra a partir de um Orçamento aprovado ---
    @Transactional
    public PedidoCompra criarPedidoCompraDoOrcamento(Orcamento orcamentoAprovado) {
        Objects.requireNonNull(orcamentoAprovado, "O orçamento aprovado não pode ser nulo.");
        
        // Validação crucial: o orçamento DEVE estar APROVADO
        if (orcamentoAprovado.getStatus() != StatusOrcamento.APROVADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Não é possível criar um pedido de compra a partir de um orçamento com status '" + 
                orcamentoAprovado.getStatus().getDescricao() + "'. Apenas orçamentos 'APROVADO' são permitidos.");
        }

        if (orcamentoAprovado.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do orçamento é obrigatório.");
        }

        PedidoCompra novoPedido = new PedidoCompra();
        novoPedido.setOrcamento(orcamentoAprovado);
        novoPedido.setDataPedido(LocalDate.now());
        
        // Fornecedor vem do orçamento aprovado
        novoPedido.setFornecedor(orcamentoAprovado.getFornecedor()); 
        
        // Status inicial do pedido de compra gerado a partir de um orçamento é PENDENTE
        novoPedido.setStatus(StatusPedidoCompra.PENDENTE); // <--- CORRIGIDO AQUI!

        BigDecimal totalCalculado = BigDecimal.ZERO;
        if (orcamentoAprovado.getItensOrcamento() == null || orcamentoAprovado.getItensOrcamento().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O orçamento aprovado não contém itens.");
        }

        for (ItemOrcamento itemOrcamento : orcamentoAprovado.getItensOrcamento()) {
            // Se o produto está no ItemOrcamento, usamos ele. Senão, buscamos pelo ID (garantia).
            Produto produto = itemOrcamento.getProduto() != null ? itemOrcamento.getProduto() :
                produtoRepository.findById(itemOrcamento.getProduto().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Produto não encontrado para o item de orçamento com ID: " + itemOrcamento.getId()));

            ItemPedidoCompra itemPedido = new ItemPedidoCompra();
            itemPedido.setProduto(produto);

            // A quantidade do item do orçamento é BigDecimal, a do item do pedido é Integer
            // Certifique-se de que a conversão é segura (sempre um número inteiro esperado)
            if (itemOrcamento.getQuantidade().stripTrailingZeros().scale() > 0) { // Verifica se tem casas decimais significativas
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade do item do orçamento ID " + itemOrcamento.getId() + " (" + itemOrcamento.getQuantidade() + ") não é um número inteiro válido para um item de pedido de compra.");
            }
            itemPedido.setQuantidade(itemOrcamento.getQuantidade().intValueExact()); // Lança ArithmeticException se não for inteiro

            itemPedido.setPrecoUnitario(itemOrcamento.getPrecoUnitarioCotado());

            // O subtotal é CALCULADO aqui, usando a quantidade Integer e o precoUnitario BigDecimal
            BigDecimal quantidadeDecimal = new BigDecimal(itemPedido.getQuantidade());
            itemPedido.setSubtotal(itemOrcamento.getPrecoUnitarioCotado().multiply(quantidadeDecimal));

            itemPedido.setObservacoes(itemOrcamento.getObservacoes());

            // Nome, Código e Unidade de Medida são populados do objeto Produto
            itemPedido.setNomeProduto(produto.getNome());
            itemPedido.setCodigoProduto(produto.getCodigoProduto());
            itemPedido.setUnidadeMedida(produto.getUnidadeMedida());
            
            novoPedido.addItem(itemPedido);
            totalCalculado = totalCalculado.add(itemPedido.getSubtotal());
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

        // Valida se o pedido pode ser atualizado (ex: não pode atualizar se já foi entregue ou cancelado)
        if (pedidoExistente.getStatus() == StatusPedidoCompra.RECEBIDO || // <--- CORRIGIDO AQUI! (Comparando Enum com Enum)
            pedidoExistente.getStatus() == StatusPedidoCompra.CANCELADO) { // <--- CORRIGIDO AQUI! (Comparando Enum com Enum)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Não é possível atualizar um pedido com status '" + pedidoExistente.getStatus().getDescricao() + "'."); // <--- CORRIGIDO AQUI! (.getDescricao() em Enum)
        }

        // Atualiza fornecedor se o ID no DTO for diferente
        if (!pedidoRequestDTO.getFornecedorId().equals(pedidoExistente.getFornecedor().getId())) {
             pedidoExistente.setFornecedor(fornecedorRepository.findById(pedidoRequestDTO.getFornecedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Fornecedor não encontrado com ID: " + pedidoRequestDTO.getFornecedorId())));
        }
       
        // Data do pedido não é atualizada pelo DTO de request, permanece a original ou é definida pelo serviço.
        // Se a lógica permitir atualização da data, ela deve ser tratada aqui. Por padrão, mantemos a original.
        // pedidoExistente.setDataPedido(Optional.ofNullable(pedidoRequestDTO.getDataPedido()).orElse(pedidoExistente.getDataPedido()));

        // Status não é atualizado por este método principal de atualização, use atualizarStatusPedidoCompra
        // String statusFromDTO = pedidoRequestDTO.getStatus();
        // if (statusFromDTO != null && !statusFromDTO.isBlank()) {
        //     if (!STATUS_PERMITIDOS_ATUALIZACAO.contains(StatusPedidoCompra.valueOf(statusFromDTO.toUpperCase()))) {
        //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + statusFromDTO);
        //     }
        //     pedidoExistente.setStatus(StatusPedidoCompra.valueOf(statusFromDTO.toUpperCase()));
        // }

        // Limpa os itens existentes para adicionar os novos/atualizados
        pedidoExistente.getItens().clear(); 

        BigDecimal totalCalculado = BigDecimal.ZERO;
        if (pedidoRequestDTO.getItens() == null || pedidoRequestDTO.getItens().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O pedido deve conter ao menos um item para atualização.");
        }

        for (ItemPedidoCompraRequestDTO itemDTO : pedidoRequestDTO.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado com ID: " + itemDTO.getProdutoId()));

            ItemPedidoCompra itemPedido = new ItemPedidoCompra();
            
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDTO.getQuantidade());
            itemPedido.setPrecoUnitario(itemDTO.getPrecoUnitario());

            // Subtotal é CALCULADO
            BigDecimal quantidadeDecimal = new BigDecimal(itemDTO.getQuantidade());
            itemPedido.setSubtotal(itemDTO.getPrecoUnitario().multiply(quantidadeDecimal));

            // Nome, Código e Unidade de Medida são populados do objeto Produto
            itemPedido.setNomeProduto(produto.getNome());
            itemPedido.setCodigoProduto(produto.getCodigoProduto());
            itemPedido.setUnidadeMedida(produto.getUnidadeMedida());
            itemPedido.setObservacoes(itemDTO.getObservacoes());

            pedidoExistente.addItem(itemPedido);
            totalCalculado = totalCalculado.add(itemPedido.getSubtotal());
        }
        pedidoExistente.setValorTotal(totalCalculado);

        return pedidoCompraRepository.save(pedidoExistente);
    }

    // --- Atualizar apenas o status de um pedido de compra ---
    @Transactional
    public PedidoCompra atualizarStatusPedidoCompra(Long id, StatusPedidoCompra novoStatus) { // Usando enum aqui
        if (novoStatus == null || !STATUS_PERMITIDOS_ATUALIZACAO.contains(novoStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido fornecido: " + (novoStatus != null ? novoStatus.getDescricao() : "nulo"));
        }

        PedidoCompra pedidoExistente = pedidoCompraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido de compra não encontrado com ID: " + id));
        
        // Regras de transição de status (exemplo básico):
        // Não pode ir para PENDENTE se já foi aprovado, enviado, recebido.
        // Não pode ser cancelado se já foi recebido.
        // As comparações são feitas diretamente com as constantes do enum.
        if (pedidoExistente.getStatus() == StatusPedidoCompra.RECEBIDO && novoStatus != StatusPedidoCompra.RECEBIDO) { // <--- CORRIGIDO AQUI!
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido já recebido não pode mudar de status.");
        }
        if (pedidoExistente.getStatus() == StatusPedidoCompra.CANCELADO && novoStatus != StatusPedidoCompra.CANCELADO) { // <--- CORRIGIDO AQUI!
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido cancelado não pode mudar de status.");
        }
        if (pedidoExistente.getStatus() == StatusPedidoCompra.REJEITADO && novoStatus != StatusPedidoCompra.REJEITADO) { // <--- CORRIGIDO AQUI!
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido rejeitado não pode mudar de status.");
        }
        // Exemplo: se PENDENTE, pode ir para APROVADO, REJEITADO, CANCELADO
        // if (pedidoExistente.getStatus() == StatusPedidoCompra.PENDENTE &&
        //     !(novoStatus == StatusPedidoCompra.APROVADO || novoStatus == StatusPedidoCompra.REJEITADO || novoStatus == StatusPedidoCompra.CANCELADO)) {
        //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transição de status inválida de PENDENTE para " + novoStatus.getDescricao());
        // }


        pedidoExistente.setStatus(novoStatus);
        return pedidoCompraRepository.save(pedidoExistente);
    }

    // --- Buscar pedidos de compra por fornecedor ---
    @Transactional(readOnly = true)
    public List<PedidoCompra> buscarPedidosPorFornecedor(Long fornecedorId) {
        // Não joga exceção NOT_FOUND se a lista estiver vazia, retorna lista vazia
        // Isso permite que o controller decida como lidar com a ausência de resultados.
        return pedidoCompraRepository.findByFornecedorId(fornecedorId);
    }

    // --- Buscar pedidos de compra por status ---
    @Transactional(readOnly = true)
    public List<PedidoCompra> buscarPedidosPorStatus(StatusPedidoCompra status) { // Usando enum aqui
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status para busca não pode ser nulo.");
        }
        // Chamada ao repositório agora aceita o enum diretamente
        return pedidoCompraRepository.findByStatus(status); // <--- CORRIGIDO AQUI!
    }

    // --- Exemplo de método para adicionar itens a um pedido existente (se necessário) ---
    @Transactional
    public PedidoCompra adicionarItens(Long pedidoId, List<ItemPedidoCompraRequestDTO> novosItensDTO) {
        PedidoCompra pedido = pedidoCompraRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido de compra não encontrado com ID: " + pedidoId));

        // Valida se o pedido pode ter itens adicionados (ex: não pode adicionar se já foi finalizado)
        if (pedido.getStatus() == StatusPedidoCompra.RECEBIDO || // <--- CORRIGIDO AQUI!
            pedido.getStatus() == StatusPedidoCompra.CANCELADO || // <--- CORRIGIDO AQUI!
            pedido.getStatus() == StatusPedidoCompra.REJEITADO) { // <--- CORRIGIDO AQUI!
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Não é possível adicionar itens a um pedido com status '" + pedido.getStatus().getDescricao() + "'."); // <--- CORRIGIDO AQUI!
        }

        if (novosItensDTO == null || novosItensDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A lista de novos itens não pode ser nula ou vazia.");
        }

        BigDecimal totalAtualizado = pedido.getValorTotal() != null ? pedido.getValorTotal() : BigDecimal.ZERO;

        for (ItemPedidoCompraRequestDTO itemDTO : novosItensDTO) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado com ID: " + itemDTO.getProdutoId()));

            ItemPedidoCompra novoItem = new ItemPedidoCompra();
            novoItem.setProduto(produto);
            novoItem.setQuantidade(itemDTO.getQuantidade());
            novoItem.setPrecoUnitario(itemDTO.getPrecoUnitario());

            BigDecimal quantidadeDecimal = new BigDecimal(itemDTO.getQuantidade());
            novoItem.setSubtotal(itemDTO.getPrecoUnitario().multiply(quantidadeDecimal));

            novoItem.setNomeProduto(produto.getNome());
            novoItem.setCodigoProduto(produto.getCodigoProduto());
            novoItem.setUnidadeMedida(produto.getUnidadeMedida());
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
        
        // Não permite deletar se o pedido já foi recebido ou está em trânsito/finalizado
        if (pedido.getStatus() == StatusPedidoCompra.RECEBIDO || // <--- CORRIGIDO AQUI!
            pedido.getStatus() == StatusPedidoCompra.ENVIADO || // <--- CORRIGIDO AQUI!
            pedido.getStatus() == StatusPedidoCompra.APROVADO) { // <--- CORRIGIDO AQUI!
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Não é possível deletar um pedido com status '" + pedido.getStatus().getDescricao() + "'."); // <--- CORRIGIDO AQUI!
        }

        pedidoCompraRepository.delete(pedido);
    }
}
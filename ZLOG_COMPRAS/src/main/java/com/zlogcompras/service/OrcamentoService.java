package com.zlogcompras.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.mapper.OrcamentoMapper;
import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusOrcamento;
import com.zlogcompras.model.StatusSolicitacaoCompra;
import com.zlogcompras.model.dto.ItemOrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoListaDTO;
import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoResponseDTO;
import com.zlogcompras.repository.FornecedorRepository;
import com.zlogcompras.repository.OrcamentoRepository;
import com.zlogcompras.repository.ProdutoRepository;
import com.zlogcompras.repository.SolicitacaoCompraRepository;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final SolicitacaoCompraRepository solicitacaoCompraRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final OrcamentoMapper orcamentoMapper;
    private final PedidoCompraService pedidoDeCompraService; // Injete o serviço de PedidoDeCompra

    @Autowired
    public OrcamentoService(OrcamentoRepository orcamentoRepository,
                            SolicitacaoCompraRepository solicitacaoCompraRepository,
                            FornecedorRepository fornecedorRepository,
                            ProdutoRepository produtoRepository,
                            OrcamentoMapper orcamentoMapper,
                            PedidoCompraService pedidoDeCompraService) { // Adicione PedidoDeCompraService
        this.orcamentoRepository = orcamentoRepository;
        this.solicitacaoCompraRepository = solicitacaoCompraRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
        this.orcamentoMapper = orcamentoMapper;
        this.pedidoDeCompraService = pedidoDeCompraService; // Inicialize
    }

    /**
     * Cria um novo orçamento no sistema.
     *
     * @param orcamentoRequestDTO O DTO de requisição contendo os dados do
     * orçamento.
     * @return OrcamentoResponseDTO do orçamento recém-criado.
     * @throws ResponseStatusException Se a Solicitação de Compra, Fornecedor ou
     * Produto não forem encontrados.
     */
    @Transactional
    public OrcamentoResponseDTO criarOrcamento(OrcamentoRequestDTO orcamentoRequestDTO) {

        SolicitacaoCompra solicitacaoCompra = solicitacaoCompraRepository
                .findById(orcamentoRequestDTO.getSolicitacaoCompraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Solicitação de Compra não encontrada com o ID: "
                                + orcamentoRequestDTO.getSolicitacaoCompraId()));

        Fornecedor fornecedor = fornecedorRepository.findById(orcamentoRequestDTO.getFornecedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Fornecedor não encontrado com o ID: " + orcamentoRequestDTO.getFornecedorId()));

        Orcamento orcamento = orcamentoMapper.toEntity(orcamentoRequestDTO);

        orcamento.setSolicitacaoCompra(solicitacaoCompra);
        orcamento.setFornecedor(fornecedor);

        if (orcamento.getDataCotacao() == null) {
            orcamento.setDataCotacao(LocalDate.now());
        }
        if (orcamento.getStatus() == null) {
            // Status inicial do orçamento ao ser criado
            orcamento.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);
        }

        BigDecimal total = BigDecimal.ZERO;
        List<ItemOrcamento> itensDoOrcamento = new ArrayList<>();

        for (ItemOrcamentoRequestDTO itemDTO : orcamentoRequestDTO.getItensOrcamento()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado para o item com ID: " + itemDTO.getProdutoId()));

            ItemOrcamento item = orcamentoMapper.toItemOrcamentoEntity(itemDTO);
            item.setProduto(produto);
            item.setOrcamento(orcamento); // Garante a ligação bidirecional

            itensDoOrcamento.add(item);

            if (item.getPrecoUnitarioCotado() != null && item.getQuantidade() != null) {
                // Cálculo do subtotal para somar no total do orçamento
                BigDecimal itemSubtotal = item.getPrecoUnitarioCotado().multiply(item.getQuantidade());
                // *** REMOVIDO: item.setSubtotal(itemSubtotal); ***
                total = total.add(itemSubtotal);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Preço unitário cotado e quantidade são obrigatórios para os itens do orçamento.");
            }
        }

        orcamento.setItensOrcamento(itensDoOrcamento);
        orcamento.setValorTotal(total);

        Orcamento savedOrcamento = orcamentoRepository.save(orcamento);
        return orcamentoMapper.toResponseDto(savedOrcamento);
    }

    /**
     * Lista todos os orçamentos existentes.
     *
     * @return Uma lista de OrcamentoListaDTO.
     */
    public List<OrcamentoListaDTO> listarTodosOrcamentos() {
        List<Orcamento> orcamentos = orcamentoRepository.findAll();
        return orcamentoMapper.toListaDtoList(orcamentos);
    }

    /**
     * Busca um orçamento por ID.
     *
     * @param id O ID do orçamento.
     * @return Um Optional contendo o Orcamento, se encontrado.
     */
    public Optional<Orcamento> buscarOrcamentoPorId(Long id) {
        return orcamentoRepository.findById(id);
    }

    /**
     * Busca orçamentos associados a uma Solicitação de Compra.
     *
     * @param solicitacaoId O ID da Solicitação de Compra.
     * @return Uma lista de OrcamentoListaDTO.
     * @throws ResponseStatusException Se a Solicitação de Compra não for
     * encontrada.
     */
    public List<OrcamentoListaDTO> buscarOrcamentosPorSolicitacaoCompraId(Long solicitacaoId) {
        if (!solicitacaoCompraRepository.existsById(solicitacaoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Solicitação de Compra não encontrada com o ID: " + solicitacaoId);
        }
        List<Orcamento> orcamentos = orcamentoRepository.findBySolicitacaoCompraId(solicitacaoId);
        return orcamentoMapper.toListaDtoList(orcamentos);
    }

    /**
     * Atualiza um orçamento existente.
     *
     * @param id          O ID do orçamento a ser atualizado.
     * @param orcamentoRequestDTO O DTO de requisição com os dados atualizados.
     * @return OrcamentoResponseDTO do orçamento atualizado.
     * @throws ResponseStatusException Se o orçamento, Solicitação de Compra,
     * Fornecedor ou Produto não forem encontrados.
     */
    @Transactional
    public OrcamentoResponseDTO atualizarOrcamento(Long id, OrcamentoRequestDTO orcamentoRequestDTO) {
        Orcamento orcamentoExistente = orcamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Orçamento não encontrado com o ID: " + id));

        // Impedir atualização se o orçamento já estiver em um status final
        if (orcamentoExistente.getStatus() == StatusOrcamento.APROVADO ||
            orcamentoExistente.getStatus() == StatusOrcamento.REJEITADO ||
            orcamentoExistente.getStatus() == StatusOrcamento.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Não é possível atualizar um orçamento que já está no status: " + orcamentoExistente.getStatus().getDescricao());
        }

        // Atualiza campos básicos do orçamento
        orcamentoMapper.updateEntityFromDto(orcamentoRequestDTO, orcamentoExistente);

        // Atualiza SolicitacaoCompra se o ID for diferente
        if (orcamentoRequestDTO.getSolicitacaoCompraId() != null &&
                !orcamentoRequestDTO.getSolicitacaoCompraId()
                        .equals(orcamentoExistente.getSolicitacaoCompra() != null ? orcamentoExistente.getSolicitacaoCompra().getId() : null)) {
            SolicitacaoCompra novaSolicitacao = solicitacaoCompraRepository
                    .findById(orcamentoRequestDTO.getSolicitacaoCompraId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Nova Solicitação de Compra não encontrada com o ID: "
                                    + orcamentoRequestDTO.getSolicitacaoCompraId()));
            orcamentoExistente.setSolicitacaoCompra(novaSolicitacao);
        }

        // Atualiza Fornecedor se o ID for diferente
        if (orcamentoRequestDTO.getFornecedorId() != null &&
                !orcamentoRequestDTO.getFornecedorId().equals(orcamentoExistente.getFornecedor() != null ? orcamentoExistente.getFornecedor().getId() : null)) {
            Fornecedor novoFornecedor = fornecedorRepository.findById(orcamentoRequestDTO.getFornecedorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Novo Fornecedor não encontrado com o ID: " + orcamentoRequestDTO.getFornecedorId()));
            orcamentoExistente.setFornecedor(novoFornecedor);
        }

        // Lógica para atualização dos itens do orçamento
        List<ItemOrcamentoRequestDTO> itensDto = orcamentoRequestDTO.getItensOrcamento() != null
                ? orcamentoRequestDTO.getItensOrcamento()
                : List.of();

        Set<Long> dtoItemIds = itensDto.stream()
                .map(ItemOrcamentoRequestDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Cria uma nova lista para os itens atualizados, para evitar ConcurrentModificationException
        // e garantir que a lista de itens da entidade seja atualizada corretamente
        List<ItemOrcamento> itensAtualizadosNoOrcamento = new ArrayList<>();

        for (ItemOrcamentoRequestDTO itemDTO : itensDto) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado para o item com ID: " + itemDTO.getProdutoId()));

            if (itemDTO.getPrecoUnitarioCotado() == null || itemDTO.getQuantidade() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Preço unitário cotado e quantidade são obrigatórios para os itens do orçamento.");
            }

            BigDecimal itemSubtotal = itemDTO.getPrecoUnitarioCotado().multiply(itemDTO.getQuantidade());


            if (itemDTO.getId() == null) {
                // Novo item
                ItemOrcamento novoItem = orcamentoMapper.toItemOrcamentoEntity(itemDTO);
                novoItem.setProduto(produto);
                novoItem.setOrcamento(orcamentoExistente);
                // *** REMOVIDO: novoItem.setSubtotal(itemSubtotal); ***
                itensAtualizadosNoOrcamento.add(novoItem);
            } else {
                // Item existente - procurar na lista original do orçamento
                Optional<ItemOrcamento> itemExistenteOpt = orcamentoExistente.getItensOrcamento().stream()
                        .filter(item -> Objects.equals(item.getId(), itemDTO.getId()))
                        .findFirst();

                if (itemExistenteOpt.isPresent()) {
                    ItemOrcamento itemExistente = itemExistenteOpt.get();
                    itemExistente.setProduto(produto);
                    itemExistente.setQuantidade(itemDTO.getQuantidade());
                    itemExistente.setPrecoUnitarioCotado(itemDTO.getPrecoUnitarioCotado());
                    itemExistente.setObservacoes(itemDTO.getObservacoes());
                    // *** REMOVIDO: itemExistente.setSubtotal(itemSubtotal); ***
                    itensAtualizadosNoOrcamento.add(itemExistente);
                } else {
                    // Item com ID informado mas que não foi encontrado na lista original (pode ser um novo item com ID, o que não é o ideal para "novo item")
                    // Ou um ID que foi removido e agora está sendo adicionado novamente.
                    // Para simplificar, trataremos como um novo item aqui se não existir na lista existente.
                    ItemOrcamento novoItem = orcamentoMapper.toItemOrcamentoEntity(itemDTO);
                    novoItem.setProduto(produto);
                    novoItem.setOrcamento(orcamentoExistente);
                    // *** REMOVIDO: novoItem.setSubtotal(itemSubtotal); ***
                    itensAtualizadosNoOrcamento.add(novoItem);
                }
            }
        }
        
        // Remove itens que estavam no orçamento original mas não estão mais no DTO de requisição
        orcamentoExistente.getItensOrcamento().clear(); // Limpa a lista existente
        orcamentoExistente.getItensOrcamento().addAll(itensAtualizadosNoOrcamento); // Adiciona os itens atualizados/novos

        // Recalcula o valor total do orçamento
        BigDecimal novoTotal = BigDecimal.ZERO;
        for (ItemOrcamento item : orcamentoExistente.getItensOrcamento()) {
            if (item.getPrecoUnitarioCotado() != null && item.getQuantidade() != null) {
                novoTotal = novoTotal.add(item.getPrecoUnitarioCotado().multiply(item.getQuantidade()));
            }
        }
        orcamentoExistente.setValorTotal(novoTotal);

        Orcamento updatedOrcamento = orcamentoRepository.save(orcamentoExistente);
        return orcamentoMapper.toResponseDto(updatedOrcamento);
    }

    /**
     * Deleta um orçamento por ID.
     *
     * @param id O ID do orçamento a ser deletado.
     * @throws ResponseStatusException Se o orçamento não for encontrado.
     */
    @Transactional
    public void deletarOrcamento(Long id) {
        if (!orcamentoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Orçamento não encontrado com o ID: " + id);
        }
        // Regra de negócio: Não permitir deletar orçamentos aprovados/rejeitados/cancelados
        Orcamento orcamento = orcamentoRepository.findById(id).get(); // Já verificou que existe
        if (orcamento.getStatus() == StatusOrcamento.APROVADO ||
            orcamento.getStatus() == StatusOrcamento.REJEITADO ||
            orcamento.getStatus() == StatusOrcamento.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Não é possível deletar um orçamento que já está no status: " + orcamento.getStatus().getDescricao());
        }
        orcamentoRepository.deleteById(id);
    }

    /**
     * Aprova um orçamento específico e rejeita os demais da mesma solicitação de compra.
     * Também atualiza o status da Solicitação de Compra e inicia a geração do Pedido de Compra.
     *
     * @param id O ID do orçamento a ser aprovado.
     * @return OrcamentoResponseDTO do orçamento aprovado.
     * @throws ResponseStatusException Se o orçamento não for encontrado,
     * se a Solicitação de Compra associada não for encontrada,
     * ou se o orçamento já estiver em um status final (APROVADO/REJEITADO).
     */
    @Transactional
    public OrcamentoResponseDTO aprovarOrcamento(Long id) {
        // 1. Buscar o orçamento a ser aprovado
        Orcamento orcamentoAprovado = orcamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orçamento não encontrado com ID: " + id));

        // 2. Validar o status atual do orçamento
        if (orcamentoAprovado.getStatus() == StatusOrcamento.APROVADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Orçamento com ID " + id + " já está APROVADO.");
        }
        if (orcamentoAprovado.getStatus() == StatusOrcamento.REJEITADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Orçamento com ID " + id + " já foi REJEITADO e não pode ser aprovado.");
        }
        if (orcamentoAprovado.getStatus() == StatusOrcamento.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Orçamento com ID " + id + " foi CANCELADO e não pode ser aprovado.");
        }


        // 3. Buscar a solicitação de compra associada
        // A solicitação de compra deve existir e estar em um status que permita aprovação de orçamento
        SolicitacaoCompra solicitacaoCompra = Optional.ofNullable(orcamentoAprovado.getSolicitacaoCompra())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Orçamento não possui Solicitação de Compra associada."));

        if (!solicitacaoCompraRepository.existsById(solicitacaoCompra.getId())) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação de Compra associada ao orçamento não encontrada com ID: " + solicitacaoCompra.getId());
        }

        if (solicitacaoCompra.getStatus() == StatusSolicitacaoCompra.CONCLUIDA ||
            solicitacaoCompra.getStatus() == StatusSolicitacaoCompra.CANCELADA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "A Solicitação de Compra associada está no status '" + solicitacaoCompra.getStatus().getDescricao() + "' e não permite aprovação de orçamento.");
        }

        // 4. Mudar o status do orçamento selecionado para APROVADO
        orcamentoAprovado.setStatus(StatusOrcamento.APROVADO);
        // O `dataAtualizacao` será atualizado automaticamente pelo `@PreUpdate` na entidade
        orcamentoRepository.save(orcamentoAprovado); // Salva o orçamento aprovado

        // 5. Rejeitar todos os outros orçamentos da mesma solicitação de compra
        List<Orcamento> outrosOrcamentos = orcamentoRepository.findBySolicitacaoCompraIdAndIdNot(solicitacaoCompra.getId(), id);
        for (Orcamento outroOrcamento : outrosOrcamentos) {
            // Só rejeita se não estiver já em status final
            if (outroOrcamento.getStatus() != StatusOrcamento.APROVADO &&
                outroOrcamento.getStatus() != StatusOrcamento.REJEITADO &&
                outroOrcamento.getStatus() != StatusOrcamento.CANCELADO) {
                outroOrcamento.setStatus(StatusOrcamento.REJEITADO);
                orcamentoRepository.save(outroOrcamento);
            }
        }

        // 6. Atualizar o status da Solicitação de Compra
        solicitacaoCompra.setStatus(StatusSolicitacaoCompra.ORCAMENTO_APROVADO);
        solicitacaoCompraRepository.save(solicitacaoCompra);

        // 7. Gerar o Pedido de Compra a partir do Orçamento Aprovado
        // A partir deste ponto, se o PedidoDeCompraService lançar uma exceção,
        // toda a transação (incluindo as atualizações de status) será revertida.
        pedidoDeCompraService.criarPedidoCompra(orcamentoAprovado);

        return orcamentoMapper.toResponseDto(orcamentoAprovado);
    }
}
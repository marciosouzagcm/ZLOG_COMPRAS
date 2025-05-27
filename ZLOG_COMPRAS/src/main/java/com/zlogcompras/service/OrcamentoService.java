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
import com.zlogcompras.model.StatusOrcamento; // Presumindo que StatusOrcamento é um Enum
import com.zlogcompras.model.StatusSolicitacaoCompra; // Presumindo que StatusSolicitacaoCompra é um Enum
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

    @Autowired
    public OrcamentoService(OrcamentoRepository orcamentoRepository,
                            SolicitacaoCompraRepository solicitacaoCompraRepository,
                            FornecedorRepository fornecedorRepository,
                            ProdutoRepository produtoRepository,
                            OrcamentoMapper orcamentoMapper) {
        this.orcamentoRepository = orcamentoRepository;
        this.solicitacaoCompraRepository = solicitacaoCompraRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
        this.orcamentoMapper = orcamentoMapper;
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
            item.setOrcamento(orcamento);

            itensDoOrcamento.add(item);

            if (item.getPrecoUnitarioCotado() != null && item.getQuantidade() != null) {
                total = total.add(
                        item.getPrecoUnitarioCotado().multiply(BigDecimal.valueOf(item.getQuantidade().longValue())));
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
     * @param id                O ID do orçamento a ser atualizado.
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

        orcamentoMapper.updateEntityFromDto(orcamentoRequestDTO, orcamentoExistente);

        if (orcamentoRequestDTO.getSolicitacaoCompraId() != null &&
                !orcamentoRequestDTO.getSolicitacaoCompraId()
                        .equals(orcamentoExistente.getSolicitacaoCompra().getId())) {
            SolicitacaoCompra novaSolicitacao = solicitacaoCompraRepository
                    .findById(orcamentoRequestDTO.getSolicitacaoCompraId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Nova Solicitação de Compra não encontrada com o ID: "
                                    + orcamentoRequestDTO.getSolicitacaoCompraId()));
            orcamentoExistente.setSolicitacaoCompra(novaSolicitacao);
        }

        if (orcamentoRequestDTO.getFornecedorId() != null &&
                !orcamentoRequestDTO.getFornecedorId().equals(orcamentoExistente.getFornecedor().getId())) {
            Fornecedor novoFornecedor = fornecedorRepository.findById(orcamentoRequestDTO.getFornecedorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Novo Fornecedor não encontrado com o ID: " + orcamentoRequestDTO.getFornecedorId()));
            orcamentoExistente.setFornecedor(novoFornecedor);
        }

        List<ItemOrcamentoRequestDTO> itensDto = orcamentoRequestDTO.getItensOrcamento() != null
                ? orcamentoRequestDTO.getItensOrcamento()
                : List.of();

        Set<Long> dtoItemIds = itensDto.stream()
                .map(ItemOrcamentoRequestDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        orcamentoExistente.getItensOrcamento()
                .removeIf(item -> item.getId() != null && !dtoItemIds.contains(item.getId()));

        for (ItemOrcamentoRequestDTO itemDTO : itensDto) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado para o item com ID: " + itemDTO.getProdutoId()));

            if (itemDTO.getId() == null) {
                ItemOrcamento novoItem = orcamentoMapper.toItemOrcamentoEntity(itemDTO);
                novoItem.setProduto(produto);
                novoItem.setOrcamento(orcamentoExistente);
                orcamentoExistente.getItensOrcamento().add(novoItem);
            } else {
                orcamentoExistente.getItensOrcamento().stream()
                        .filter(item -> Objects.equals(item.getId(), itemDTO.getId()))
                        .findFirst()
                        .ifPresentOrElse(itemExistente -> {
                            itemExistente.setProduto(produto);
                            itemExistente.setQuantidade(itemDTO.getQuantidade());
                            itemExistente.setPrecoUnitarioCotado(itemDTO.getPrecoUnitarioCotado());
                            itemExistente.setObservacoes(itemDTO.getObservacoes());
                        }, () -> {
                            ItemOrcamento novoItem = orcamentoMapper.toItemOrcamentoEntity(itemDTO);
                            novoItem.setProduto(produto);
                            novoItem.setOrcamento(orcamentoExistente);
                            orcamentoExistente.getItensOrcamento().add(novoItem);
                        });
            }
        }

        if (orcamentoExistente.getItensOrcamento().isEmpty()) {
            orcamentoExistente.setValorTotal(BigDecimal.ZERO);
        } else {
            orcamentoExistente.setValorTotal(
                    orcamentoExistente.getItensOrcamento().stream()
                            .map(item -> {
                                if (item.getPrecoUnitarioCotado() == null || item.getQuantidade() == null) {
                                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                            "Preço unitário cotado e quantidade são obrigatórios para os itens do orçamento.");
                                }
                                return item.getPrecoUnitarioCotado()
                                        .multiply(BigDecimal.valueOf(item.getQuantidade().longValue()));
                            })
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

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

        // 2. Verificar o status atual do orçamento
        if (orcamentoAprovado.getStatus() == StatusOrcamento.APROVADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Orçamento com ID " + id + " já está APROVADO.");
        }
        if (orcamentoAprovado.getStatus() == StatusOrcamento.REJEITADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Orçamento com ID " + id + " já foi REJEITADO e não pode ser aprovado.");
        }

        // 3. Buscar a solicitação de compra associada
        Long solicitacaoCompraId = orcamentoAprovado.getSolicitacaoCompra().getId();
        SolicitacaoCompra solicitacaoCompra = solicitacaoCompraRepository.findById(solicitacaoCompraId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação de Compra associada ao orçamento não encontrada com ID: " + solicitacaoCompraId));

        // 4. Mudar o status do orçamento selecionado para APROVADO
        orcamentoAprovado.setStatus(StatusOrcamento.APROVADO);
        orcamentoRepository.save(orcamentoAprovado); // Salva o orçamento aprovado

        // 5. Rejeitar todos os outros orçamentos da mesma solicitação de compra
        List<Orcamento> outrosOrcamentos = orcamentoRepository.findBySolicitacaoCompraIdAndIdNot(solicitacaoCompraId, id);
        for (Orcamento outroOrcamento : outrosOrcamentos) {
            // Só rejeita se não estiver já aprovado ou rejeitado (prevenção, embora a lógica principal evite)
            if (outroOrcamento.getStatus() != StatusOrcamento.APROVADO && outroOrcamento.getStatus() != StatusOrcamento.REJEITADO) {
                outroOrcamento.setStatus(StatusOrcamento.REJEITADO);
                orcamentoRepository.save(outroOrcamento);
            }
        }

        // 6. Atualizar o status da Solicitação de Compra
        solicitacaoCompra.setStatus(StatusSolicitacaoCompra.ORCAMENTO_APROVADO); // Ou um status mais adequado
        solicitacaoCompraRepository.save(solicitacaoCompra);

        // 7. Lógica para gerar Pedido de Compra (deve ser implementada aqui ou em um serviço dedicado)
        System.out.println("Orçamento " + id + " aprovado. SolicitacaoCompra " + solicitacaoCompraId + " atualizada. Lógica para gerar Pedido de Compra deve ser implementada aqui, com base no orçamento aprovado.");
        // Exemplo: pedidoCompraService.gerarPedidoDeCompra(orcamentoAprovado);

        return orcamentoMapper.toResponseDto(orcamentoAprovado);
    }
}
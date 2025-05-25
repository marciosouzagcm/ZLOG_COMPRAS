package com.zlogcompras.service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.zlogcompras.model.dto.ItemOrcamentoInputDTO;
import com.zlogcompras.model.dto.OrcamentoInputDTO;
import com.zlogcompras.model.dto.OrcamentoListaDTO;
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
    private final OrcamentoMapper orcamentoMapper; // Mantido privado e sem getter público

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

    @Transactional
    public OrcamentoResponseDTO salvarOrcamento(OrcamentoInputDTO orcamentoInputDTO) {
        SolicitacaoCompra solicitacaoCompra = solicitacaoCompraRepository
                .findById(orcamentoInputDTO.getSolicitacaoCompraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Solicitação de Compra não encontrada com o ID: "
                                + orcamentoInputDTO.getSolicitacaoCompraId()));

        Fornecedor fornecedor = fornecedorRepository.findById(orcamentoInputDTO.getFornecedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Fornecedor não encontrado com o ID: " + orcamentoInputDTO.getFornecedorId()));

        Orcamento orcamento = orcamentoMapper.toEntity(orcamentoInputDTO);

        orcamento.setSolicitacaoCompra(solicitacaoCompra);
        orcamento.setFornecedor(fornecedor);

        if (orcamento.getDataCotacao() == null) {
            orcamento.setDataCotacao(LocalDate.now());
        }

        if (orcamento.getStatus() == null) {
            orcamento.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);
        }

        if (orcamento.getItensOrcamento() == null || orcamento.getItensOrcamento().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O orçamento deve conter pelo menos um item.");
        }

        for (ItemOrcamento item : orcamento.getItensOrcamento()) {
            item.setOrcamento(orcamento);

            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado para o item com ID: " + item.getProduto().getId()));
            item.setProduto(produto);
        }

        orcamento.setValorTotal(
                orcamento.getItensOrcamento().stream()
                        .map(item -> item.getQuantidade().multiply(item.getPrecoUnitarioCotado()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

        Orcamento savedOrcamento = orcamentoRepository.save(orcamento);
        return orcamentoMapper.toResponseDto(savedOrcamento);
    }

    public List<OrcamentoListaDTO> listarTodosOrcamentos() {
        List<Orcamento> orcamentos = orcamentoRepository.findAll();
        return orcamentoMapper.toListaDtoList(orcamentos);
    }

    // Mantém o retorno de Optional<Orcamento> aqui para que o controller possa
    // mapear para DTO
    public Optional<Orcamento> buscarOrcamentoPorId(Long id) {
        return orcamentoRepository.findById(id);
    }

    public List<OrcamentoListaDTO> buscarOrcamentosPorSolicitacaoCompraId(Long solicitacaoId) {
        if (!solicitacaoCompraRepository.existsById(solicitacaoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Solicitação de Compra não encontrada com o ID: " + solicitacaoId);
        }
        List<Orcamento> orcamentos = orcamentoRepository.findBySolicitacaoCompraId(solicitacaoId);
        return orcamentoMapper.toListaDtoList(orcamentos);
    }

    @Transactional
    public OrcamentoResponseDTO atualizarOrcamento(Long id, OrcamentoInputDTO orcamentoInputDTO) {
        Orcamento orcamentoExistente = orcamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Orçamento não encontrado com o ID: " + id));

        orcamentoMapper.updateEntityFromDto(orcamentoInputDTO, orcamentoExistente);

        if (orcamentoInputDTO.getSolicitacaoCompraId() != null &&
                !orcamentoInputDTO.getSolicitacaoCompraId().equals(orcamentoExistente.getSolicitacaoCompra().getId())) {
            SolicitacaoCompra novaSolicitacao = solicitacaoCompraRepository
                    .findById(orcamentoInputDTO.getSolicitacaoCompraId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Nova Solicitação de Compra não encontrada com o ID: "
                                    + orcamentoInputDTO.getSolicitacaoCompraId()));
            orcamentoExistente.setSolicitacaoCompra(novaSolicitacao);
        }

        if (orcamentoInputDTO.getFornecedorId() != null &&
                !orcamentoInputDTO.getFornecedorId().equals(orcamentoExistente.getFornecedor().getId())) {
            Fornecedor novoFornecedor = fornecedorRepository.findById(orcamentoInputDTO.getFornecedorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Novo Fornecedor não encontrado com o ID: " + orcamentoInputDTO.getFornecedorId()));
            orcamentoExistente.setFornecedor(novoFornecedor);
        }

        List<ItemOrcamentoInputDTO> itensDto = orcamentoInputDTO.getItensOrcamento() != null
                ? orcamentoInputDTO.getItensOrcamento()
                : List.of();

        Set<Long> existingItemIds = orcamentoExistente.getItensOrcamento().stream()
                .map(ItemOrcamento::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> dtoItemIds = itensDto.stream()
                .map(ItemOrcamentoInputDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        orcamentoExistente.getItensOrcamento()
                .removeIf(item -> item.getId() != null && !dtoItemIds.contains(item.getId()));

        for (ItemOrcamentoInputDTO itemDTO : itensDto) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado para o item: " + itemDTO.getProdutoId()));

            if (itemDTO.getId() == null || !existingItemIds.contains(itemDTO.getId())) {
                ItemOrcamento novoItem = orcamentoMapper.toItemOrcamentoEntity(itemDTO);
                novoItem.setProduto(produto);
                novoItem.setOrcamento(orcamentoExistente);
                orcamentoExistente.getItensOrcamento().add(novoItem);
            } else {
                orcamentoExistente.getItensOrcamento().stream()
                        .filter(item -> Objects.equals(item.getId(), itemDTO.getId()))
                        .findFirst()
                        .ifPresent(itemExistente -> {
                            itemExistente.setProduto(produto);
                            itemExistente.setQuantidade(itemDTO.getQuantidade());
                            itemExistente.setPrecoUnitarioCotado(itemDTO.getPrecoUnitario());
                            itemExistente.setObservacoes(itemDTO.getObservacoes());
                        });
            }
        }

        if (orcamentoExistente.getItensOrcamento().isEmpty()) {
            orcamentoExistente.setValorTotal(BigDecimal.ZERO);
        } else {
            orcamentoExistente.setValorTotal(
                    orcamentoExistente.getItensOrcamento().stream()
                            .map(item -> item.getQuantidade().multiply(item.getPrecoUnitarioCotado()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        Orcamento updatedOrcamento = orcamentoRepository.save(orcamentoExistente);
        return orcamentoMapper.toResponseDto(updatedOrcamento);
    }

    @Transactional
    public void deletarOrcamento(Long id) {
        if (!orcamentoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Orçamento não encontrado com o ID: " + id);
        }
        orcamentoRepository.deleteById(id);
    }

    @Transactional
    public void aprovarOrcamentoGerarPedido(Long id) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Orçamento não encontrado com o ID: " + id));

        if (orcamento.getStatus() != StatusOrcamento.ABERTO && orcamento.getStatus() != StatusOrcamento.COTADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Orçamento não pode ser aprovado. Status atual: " + orcamento.getStatus().getDescricao());
        }

        orcamento.setStatus(StatusOrcamento.APROVADO);
        orcamentoRepository.save(orcamento);

        System.out.println(
                "Orçamento " + id + " aprovado. Lógica para gerar Pedido de Compra deve ser implementada aqui.");
    }

    @Transactional
    public Orcamento atualizarOrcamento(Orcamento orcamentoSelecionado) {
        if (orcamentoSelecionado.getId() == null || !orcamentoRepository.existsById(orcamentoSelecionado.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Orçamento não encontrado para atualização com o ID: " + orcamentoSelecionado.getId());
        }
        return orcamentoRepository.save(orcamentoSelecionado);
    }
}
package com.zlogcompras.service;

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
import com.zlogcompras.model.dto.OrcamentoLoteRequestDTO;
import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoResponseDTO;
import com.zlogcompras.repository.FornecedorRepository;
import com.zlogcompras.repository.OrcamentoRepository;
import com.zlogcompras.repository.ProdutoRepository;
import com.zlogcompras.repository.SolicitacaoCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final SolicitacaoCompraRepository solicitacaoCompraRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final OrcamentoMapper orcamentoMapper;
    private final PedidoCompraService pedidoCompraService;

    @Autowired
    public OrcamentoService(OrcamentoRepository orcamentoRepository,
                            SolicitacaoCompraRepository solicitacaoCompraRepository,
                            FornecedorRepository fornecedorRepository,
                            ProdutoRepository produtoRepository,
                            OrcamentoMapper orcamentoMapper,
                            PedidoCompraService pedidoCompraService) {
        this.orcamentoRepository = orcamentoRepository;
        this.solicitacaoCompraRepository = solicitacaoCompraRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
        this.orcamentoMapper = orcamentoMapper;
        this.pedidoCompraService = pedidoCompraService;
    }

    @Transactional
    public OrcamentoResponseDTO criarOrcamento(OrcamentoRequestDTO orcamentoRequestDTO) {
        if (orcamentoRequestDTO.getItensOrcamento() == null || orcamentoRequestDTO.getItensOrcamento().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O orçamento deve conter ao menos um item.");
        }

        SolicitacaoCompra solicitacaoCompra = solicitacaoCompraRepository.findById(orcamentoRequestDTO.getSolicitacaoCompraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação de compra não encontrada."));
        Fornecedor fornecedor = fornecedorRepository.findById(orcamentoRequestDTO.getFornecedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado."));

        BigDecimal valorTotal = BigDecimal.ZERO;
        List<ItemOrcamento> itensOrcamento = new ArrayList<>();

        for (ItemOrcamentoRequestDTO itemDTO : orcamentoRequestDTO.getItensOrcamento()) {
            if (itemDTO.getQuantidade() == null || itemDTO.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade do item do orçamento não pode ser zero ou negativa para o produto ID: " + itemDTO.getProdutoId());
            }
            if (itemDTO.getPrecoUnitarioCotado() == null || itemDTO.getPrecoUnitarioCotado().compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O preço unitário do item do orçamento não pode ser negativo para o produto ID: " + itemDTO.getProdutoId());
            }

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado para o item com ID: " + itemDTO.getProdutoId()));

            ItemOrcamento itemOrcamento = orcamentoMapper.toItemOrcamentoEntity(itemDTO);
            itemOrcamento.setProduto(produto);
            itemOrcamento.setNomeProduto(produto.getNome());
            itemOrcamento.setUnidadeMedidaProduto(produto.getUnidadeMedida());
            itemOrcamento.setCodigoProduto(produto.getCodigoProduto()); // NOVO: Atribui o código do produto

            itensOrcamento.add(itemOrcamento);

            valorTotal = valorTotal.add(itemDTO.getQuantidade().multiply(itemDTO.getPrecoUnitarioCotado()));
        }

        Orcamento orcamento = orcamentoMapper.toEntity(orcamentoRequestDTO);
        if (orcamento == null) {
            orcamento = new Orcamento();
        }

        orcamento.setSolicitacaoCompra(solicitacaoCompra);
        orcamento.setFornecedor(fornecedor);
        orcamento.setDataCotacao(LocalDate.now());
        orcamento.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);
        orcamento.setValorTotal(valorTotal);

        for (ItemOrcamento item : itensOrcamento) {
            item.setOrcamento(orcamento);
        }
        orcamento.setItensOrcamento(itensOrcamento);

        Orcamento savedOrcamento = orcamentoRepository.save(orcamento);
        return orcamentoMapper.toResponseDto(savedOrcamento);
    }

    @Transactional
    public List<OrcamentoResponseDTO> criarOrcamentosEmLote(OrcamentoLoteRequestDTO orcamentoLoteRequestDTO) {
        Long solicitacaoCompraIdDoLote = orcamentoLoteRequestDTO.getSolicitacaoCompraId();
        SolicitacaoCompra solicitacaoCompraPrincipal = solicitacaoCompraRepository.findById(solicitacaoCompraIdDoLote)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação de Compra não encontrada com o ID: " + solicitacaoCompraIdDoLote));

        List<OrcamentoResponseDTO> orcamentosCriados = new ArrayList<>();

        for (OrcamentoRequestDTO orcamentoRequestDTO : orcamentoLoteRequestDTO.getOrcamentos()) {
            if (orcamentoRequestDTO.getSolicitacaoCompraId() == null ||
                !orcamentoRequestDTO.getSolicitacaoCompraId().equals(solicitacaoCompraIdDoLote)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O 'solicitacaoCompraId' em cada orçamento individual do lote deve ser o mesmo do 'solicitacaoCompraId' do lote (" + solicitacaoCompraIdDoLote + ").");
            }

            OrcamentoResponseDTO novoOrcamento = criarOrcamento(orcamentoRequestDTO);
            orcamentosCriados.add(novoOrcamento);
        }

        return orcamentosCriados;
    }

    @Transactional
    public OrcamentoResponseDTO atualizarOrcamento(Long id, OrcamentoRequestDTO orcamentoRequestDTO) {
        Orcamento existingOrcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orçamento não encontrado para o ID: " + id));

        if (existingOrcamento.getStatus() != StatusOrcamento.AGUARDANDO_APROVACAO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Somente orçamentos com status 'AGUARDANDO_APROVACAO' podem ser atualizados.");
        }

        if (orcamentoRequestDTO.getSolicitacaoCompraId() != null &&
            !orcamentoRequestDTO.getSolicitacaoCompraId().equals(existingOrcamento.getSolicitacaoCompra().getId())) {
            SolicitacaoCompra solicitacaoCompra = solicitacaoCompraRepository.findById(orcamentoRequestDTO.getSolicitacaoCompraId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação de compra não encontrada para o ID: " + orcamentoRequestDTO.getSolicitacaoCompraId()));
            existingOrcamento.setSolicitacaoCompra(solicitacaoCompra);
        }

        if (orcamentoRequestDTO.getFornecedorId() != null &&
            !orcamentoRequestDTO.getFornecedorId().equals(existingOrcamento.getFornecedor().getId())) {
            Fornecedor fornecedor = fornecedorRepository.findById(orcamentoRequestDTO.getFornecedorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado para o ID: " + orcamentoRequestDTO.getFornecedorId()));
            existingOrcamento.setFornecedor(fornecedor);
        }

        orcamentoMapper.updateEntityFromDto(orcamentoRequestDTO, existingOrcamento);

        BigDecimal valorTotal = BigDecimal.ZERO;
        List<ItemOrcamento> updatedItens = new ArrayList<>();

        if (orcamentoRequestDTO.getItensOrcamento() == null || orcamentoRequestDTO.getItensOrcamento().isEmpty()) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O orçamento deve conter ao menos um item.");
        }

        java.util.Map<Long, ItemOrcamento> existingItemsMap = existingOrcamento.getItensOrcamento().stream()
                .filter(item -> item.getId() != null)
                .collect(Collectors.toMap(ItemOrcamento::getId, item -> item));

        for (ItemOrcamentoRequestDTO itemDTO : orcamentoRequestDTO.getItensOrcamento()) {
            if (itemDTO.getQuantidade() == null || itemDTO.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade do item do orçamento não pode ser zero ou negativa para o produto ID: " + itemDTO.getProdutoId());
            }
            if (itemDTO.getPrecoUnitarioCotado() == null || itemDTO.getPrecoUnitarioCotado().compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O preço unitário do item do orçamento não pode ser negativo para o produto ID: " + itemDTO.getProdutoId());
            }

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado para o item com ID: " + itemDTO.getProdutoId()));

            ItemOrcamento itemOrcamento;
            if (itemDTO.getId() != null) {
                itemOrcamento = existingItemsMap.get(itemDTO.getId());
                if (itemOrcamento == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de orçamento existente com ID " + itemDTO.getId() + " não encontrado no orçamento.");
                }
                itemOrcamento.setQuantidade(itemDTO.getQuantidade());
                itemOrcamento.setPrecoUnitarioCotado(itemDTO.getPrecoUnitarioCotado());
                itemOrcamento.setObservacoes(itemDTO.getObservacoes());
                itemOrcamento.setProduto(produto);
                itemOrcamento.setNomeProduto(produto.getNome());
                itemOrcamento.setUnidadeMedidaProduto(produto.getUnidadeMedida());
                itemOrcamento.setCodigoProduto(produto.getCodigoProduto()); // NOVO: Atribui o código do produto
            } else {
                itemOrcamento = orcamentoMapper.toItemOrcamentoEntity(itemDTO);
                itemOrcamento.setProduto(produto);
                itemOrcamento.setOrcamento(existingOrcamento);
                itemOrcamento.setNomeProduto(produto.getNome());
                itemOrcamento.setUnidadeMedidaProduto(produto.getUnidadeMedida());
                itemOrcamento.setCodigoProduto(produto.getCodigoProduto()); // NOVO: Atribui o código do produto
            }
            updatedItens.add(itemOrcamento);
            valorTotal = valorTotal.add(itemDTO.getQuantidade().multiply(itemDTO.getPrecoUnitarioCotado()));
        }

        existingOrcamento.getItensOrcamento().clear();
        existingOrcamento.getItensOrcamento().addAll(updatedItens);

        existingOrcamento.setValorTotal(valorTotal);

        Orcamento updatedOrcamento = orcamentoRepository.save(existingOrcamento);
        return orcamentoMapper.toResponseDto(updatedOrcamento);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoListaDTO> listarTodosOrcamentos() {
        List<Orcamento> orcamentos = orcamentoRepository.findAll();
        return orcamentos.stream()
                .map(orcamentoMapper::toListaDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Orcamento> buscarOrcamentoPorId(Long id) {
        return orcamentoRepository.findById(id);
    }

    @Transactional
    public void deletarOrcamento(Long id) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orçamento não encontrado."));
        orcamentoRepository.delete(orcamento);
    }

    @Transactional
    public OrcamentoResponseDTO aprovarOrcamento(Long id) {
        Orcamento orcamentoAprovado = orcamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orçamento não encontrado."));

        if (orcamentoAprovado.getStatus() != StatusOrcamento.AGUARDANDO_APROVACAO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apenas orçamentos com status 'AGUARDANDO_APROVACAO' podem ser aprovados.");
        }

        orcamentoAprovado.setStatus(StatusOrcamento.APROVADO);
        orcamentoRepository.save(orcamentoAprovado);

        SolicitacaoCompra solicitacaoCompra = orcamentoAprovado.getSolicitacaoCompra();
        List<Orcamento> outrosOrcamentos = orcamentoRepository.findBySolicitacaoCompraIdAndIdNot(
                solicitacaoCompra.getId(), orcamentoAprovado.getId());

        for (Orcamento outro : outrosOrcamentos) {
            if (outro.getStatus() == StatusOrcamento.AGUARDANDO_APROVACAO) {
                outro.setStatus(StatusOrcamento.REJEITADO);
                orcamentoRepository.save(outro);
            }
        }

        solicitacaoCompra.setStatus(StatusSolicitacaoCompra.ORCAMENTO_APROVADO);
        solicitacaoCompraRepository.save(solicitacaoCompra);

        pedidoCompraService.criarPedidoCompraDoOrcamento(orcamentoAprovado);

        return orcamentoMapper.toResponseDto(orcamentoAprovado);
    }
}
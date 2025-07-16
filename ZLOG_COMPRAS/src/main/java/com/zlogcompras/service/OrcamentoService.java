package com.zlogcompras.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Adicione esta importação

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
import com.zlogcompras.model.dto.OrcamentoLoteRequestDTO; // Adicione esta importação se for usar o método em lote
import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoResponseDTO;
import com.zlogcompras.repository.FornecedorRepository;
import com.zlogcompras.repository.ItemOrcamentoRepository;
import com.zlogcompras.repository.OrcamentoRepository;
import com.zlogcompras.repository.ProdutoRepository;
import com.zlogcompras.repository.SolicitacaoCompraRepository;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final SolicitacaoCompraRepository solicitacaoCompraRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemOrcamentoRepository itemOrcamentoRepository;
    private final OrcamentoMapper orcamentoMapper;
    private final PedidoCompraService pedidoCompraService;

    public OrcamentoService(OrcamentoRepository orcamentoRepository,
            SolicitacaoCompraRepository solicitacaoCompraRepository,
            FornecedorRepository fornecedorRepository,
            ProdutoRepository produtoRepository,
            ItemOrcamentoRepository itemOrcamentoRepository,
            OrcamentoMapper orcamentoMapper,
            PedidoCompraService pedidoCompraService) {
        this.orcamentoRepository = orcamentoRepository;
        this.solicitacaoCompraRepository = solicitacaoCompraRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
        this.itemOrcamentoRepository = itemOrcamentoRepository;
        this.orcamentoMapper = orcamentoMapper;
        this.pedidoCompraService = pedidoCompraService;
    }

    @Transactional
    public OrcamentoResponseDTO criarOrcamento(OrcamentoRequestDTO orcamentoRequestDTO) {
        SolicitacaoCompra solicitacaoCompra = solicitacaoCompraRepository
                .findById(orcamentoRequestDTO.getSolicitacaoCompraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Solicitação de Compra não encontrada."));

        Fornecedor fornecedor = fornecedorRepository.findById(orcamentoRequestDTO.getFornecedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado."));

        Orcamento orcamento = orcamentoMapper.toEntity(orcamentoRequestDTO);
        orcamento.setSolicitacaoCompra(solicitacaoCompra);
        orcamento.setFornecedor(fornecedor);
        orcamento.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);

        BigDecimal valorTotal = BigDecimal.ZERO;
        List<ItemOrcamento> itensOrcamento = new ArrayList<>();

        if (orcamentoRequestDTO.getItensOrcamento() == null || orcamentoRequestDTO.getItensOrcamento().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O orçamento deve conter itens.");
        }

        for (var itemDTO : orcamentoRequestDTO.getItensOrcamento()) {
            if (itemDTO.getQuantidade() == null || itemDTO.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "A quantidade do item deve ser maior que zero.");
            }
            if (itemDTO.getPrecoUnitarioCotado() == null
                    || itemDTO.getPrecoUnitarioCotado().compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Preço unitário do item não pode ser negativo.");
            }

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado com ID: " + itemDTO.getProdutoId()));

            ItemOrcamento itemOrcamento = new ItemOrcamento();
            itemOrcamento.setProduto(produto);
            itemOrcamento.setQuantidade(itemDTO.getQuantidade());
            itemOrcamento.setPrecoUnitarioCotado(itemDTO.getPrecoUnitarioCotado());
            itemOrcamento.setObservacoes(itemDTO.getObservacoes());
            itemOrcamento.setOrcamento(orcamento);

            itensOrcamento.add(itemOrcamento);
            valorTotal = valorTotal.add(itemDTO.getQuantidade().multiply(itemDTO.getPrecoUnitarioCotado()));
        }

        orcamento.setItensOrcamento(itensOrcamento);
        orcamento.setValorTotal(valorTotal);

        Orcamento orcamentoSalvo = orcamentoRepository.save(orcamento);
        // itensOrcamento.forEach(itemOrcamentoRepository::save); // Descomente se
        // gerenciar itens separadamente
        return orcamentoMapper.toResponseDto(orcamentoSalvo);
    }

    @Transactional
    public OrcamentoResponseDTO atualizarOrcamento(Long id, OrcamentoRequestDTO orcamentoRequestDTO) {
        Orcamento orcamentoExistente = orcamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Orçamento não encontrado com ID: " + id));

        orcamentoMapper.updateEntityFromDto(orcamentoRequestDTO, orcamentoExistente);

        BigDecimal novoValorTotal = BigDecimal.ZERO;
        List<ItemOrcamento> novosItensOrcamento = new ArrayList<>();

        if (orcamentoRequestDTO.getItensOrcamento() == null || orcamentoRequestDTO.getItensOrcamento().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O orçamento deve conter itens ao atualizar.");
        }

        for (var itemDTO : orcamentoRequestDTO.getItensOrcamento()) {
            if (itemDTO.getQuantidade() == null || itemDTO.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Quantidade do item deve ser maior que zero.");
            }
            if (itemDTO.getPrecoUnitarioCotado() == null
                    || itemDTO.getPrecoUnitarioCotado().compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Preço unitário do item não pode ser negativo.");
            }

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado com ID: " + itemDTO.getProdutoId()));

            ItemOrcamento itemAtualizado = orcamentoExistente.getItensOrcamento().stream()
                    .filter(item -> item.getId() != null && item.getId().equals(itemDTO.getId()))
                    .findFirst()
                    .orElse(new ItemOrcamento());

            itemAtualizado.setProduto(produto);
            itemAtualizado.setQuantidade(itemDTO.getQuantidade());
            itemAtualizado.setPrecoUnitarioCotado(itemDTO.getPrecoUnitarioCotado());
            itemAtualizado.setObservacoes(itemDTO.getObservacoes());
            itemAtualizado.setOrcamento(orcamentoExistente);

            if (itemAtualizado.getId() == null && itemOrcamentoRepository != null) {
                // itemOrcamentoRepository.save(itemAtualizado); // Descomente se gerenciar
                // itens separadamente
            }
            novosItensOrcamento.add(itemAtualizado);
            novoValorTotal = novoValorTotal.add(itemDTO.getQuantidade().multiply(itemDTO.getPrecoUnitarioCotado()));
        }

        orcamentoExistente.getItensOrcamento().clear();
        orcamentoExistente.getItensOrcamento().addAll(novosItensOrcamento);
        orcamentoExistente.setValorTotal(novoValorTotal);

        Orcamento orcamentoAtualizado = orcamentoRepository.save(orcamentoExistente);
        return orcamentoMapper.toResponseDto(orcamentoAtualizado);
    }

    @Transactional
    public void aprovarOrcamento(Long orcamentoId) {
        Orcamento orcamentoAprovado = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Orçamento não encontrado com ID: " + orcamentoId));

        if (orcamentoAprovado.getStatus() != StatusOrcamento.AGUARDANDO_APROVACAO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Orçamento não pode ser aprovado. Status atual: " + orcamentoAprovado.getStatus());
        }

        orcamentoAprovado.setStatus(StatusOrcamento.APROVADO);
        orcamentoRepository.save(orcamentoAprovado);

        SolicitacaoCompra solicitacao = orcamentoAprovado.getSolicitacaoCompra();
        if (solicitacao != null) {
            List<Orcamento> outrosOrcamentos = orcamentoRepository
                    .findBySolicitacaoCompraIdAndIdNot(solicitacao.getId(), orcamentoAprovado.getId());
            for (Orcamento outroOrcamento : outrosOrcamentos) {
                if (outroOrcamento.getStatus() == StatusOrcamento.AGUARDANDO_APROVACAO) {
                    outroOrcamento.setStatus(StatusOrcamento.REJEITADO);
                    orcamentoRepository.save(outroOrcamento);
                }
            }
            solicitacao.setStatus(StatusSolicitacaoCompra.ORCAMENTO_APROVADO);
            solicitacaoCompraRepository.save(solicitacao);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Orçamento não está associado a uma Solicitação de Compra.");
        }

        pedidoCompraService.criarPedidoCompraDoOrcamento(orcamentoAprovado);
    }

    // --- Novos métodos adicionados/corrigidos ---

    public OrcamentoResponseDTO buscarOrcamentoPorId(Long id) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Orçamento não encontrado com ID: " + id));
        return orcamentoMapper.toResponseDto(orcamento);
    }

    // Este é o método que o seu teste estava tentando chamar.
    // Ele já existe no código que você forneceu.
    public List<OrcamentoResponseDTO> listarTodosOrcamentos() {
        return orcamentoRepository.findAll().stream()
                .map(orcamentoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletarOrcamento(Long id) {
        if (!orcamentoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Orçamento não encontrado com ID: " + id);
        }
        orcamentoRepository.deleteById(id);
    }

    // Método para recusar um orçamento específico (se for separado da aprovação)
    @Transactional
    public void recusarOrcamento(Long orcamentoId) {
        Orcamento orcamento = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Orçamento não encontrado com ID: " + orcamentoId));

        if (orcamento.getStatus() != StatusOrcamento.AGUARDANDO_APROVACAO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Orçamento não pode ser recusado. Status atual: " + orcamento.getStatus());
        }

        orcamento.setStatus(StatusOrcamento.REJEITADO);
        orcamentoRepository.save(orcamento);
    }

    // Método para concluir um orçamento específico
    @Transactional
    public void concluirOrcamento(Long orcamentoId) {
        Orcamento orcamento = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Orçamento não encontrado com ID: " + orcamentoId));

        if (orcamento.getStatus() == StatusOrcamento.CONCLUIDO || orcamento.getStatus() == StatusOrcamento.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Orçamento já está concluído ou cancelado.");
        }

        orcamento.setStatus(StatusOrcamento.CONCLUIDO);
        orcamentoRepository.save(orcamento);
    }

    /*
     * Exemplo de método para criar orçamentos em lote.
     * Você precisará implementar a lógica real aqui com base no seu
     * `OrcamentoLoteRequestDTO`.
     * Se você não usa essa funcionalidade, pode remover o método e a referência no
     * Controller.
     */
    @Transactional
    public List<OrcamentoResponseDTO> criarOrcamentosEmLote(OrcamentoLoteRequestDTO loteDTO) {
        List<OrcamentoResponseDTO> orcamentosCriados = new ArrayList<>();
        // Exemplo: iterar sobre os orçamentos no lote e chamar o método criarOrcamento
        // para cada um
        for (OrcamentoRequestDTO requestDTO : loteDTO.getOrcamentos()) {
            orcamentosCriados.add(criarOrcamento(requestDTO));
        }
        return orcamentosCriados;
        // throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Funcionalidade
        // de criar orçamentos em lote ainda não implementada.");
    }
}
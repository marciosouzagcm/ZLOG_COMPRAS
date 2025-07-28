package com.zlogcompras.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Adicione esta importação
import java.util.stream.Collectors;

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
import com.zlogcompras.model.dto.ItemOrcamentoRequestDTO; // Adicione esta importação
import com.zlogcompras.model.dto.OrcamentoLoteRequestDTO;
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

        // CORREÇÃO: Operador || (OR lógico)
        if (orcamentoRequestDTO.getItensOrcamento() == null || orcamentoRequestDTO.getItensOrcamento().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O orçamento deve conter itens.");
        }

        for (ItemOrcamentoRequestDTO itemDTO : orcamentoRequestDTO.getItensOrcamento()) {
            // A validação de DTOs com JSR-303 (ex: @NotNull, @DecimalMin) é preferível aqui.
            // Se a validação for movida para o DTO, essas verificações podem ser removidas do serviço.
            // CORREÇÃO: Operador || (OR lógico)
            if (itemDTO.getQuantidade() == null || itemDTO.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "A quantidade do item deve ser maior que zero.");
            }
            // CORREÇÃO: Operador || (OR lógico)
            if (itemDTO.getPrecoUnitarioCotado() == null || itemDTO.getPrecoUnitarioCotado().compareTo(BigDecimal.ZERO) < 0) {
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
            itemOrcamento.setOrcamento(orcamento); // Garante a associação bidirecional

            itensOrcamento.add(itemOrcamento);
            valorTotal = valorTotal.add(itemDTO.getQuantidade().multiply(itemDTO.getPrecoUnitarioCotado()));
        }

        orcamento.setItensOrcamento(itensOrcamento);
        orcamento.setValorTotal(valorTotal);

        Orcamento orcamentoSalvo = orcamentoRepository.save(orcamento);
        // Os itens de orçamento serão salvos em cascata se a configuração de cascata estiver correta em Orcamento
        // Se não, descomente a linha abaixo e garanta @Transactional
        // itensOrcamento.forEach(itemOrcamentoRepository::save);
        return orcamentoMapper.toResponseDto(orcamentoSalvo);
    }

    @Transactional
    public OrcamentoResponseDTO atualizarOrcamento(Long id, OrcamentoRequestDTO orcamentoRequestDTO) {
        Orcamento orcamentoExistente = orcamentoRepository.findById(id)
             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                 "Orçamento não encontrado com ID: " + id));

        // Atualiza campos básicos do orçamento (ex: data, observações)
        orcamentoMapper.updateEntityFromDto(orcamentoRequestDTO, orcamentoExistente);

        BigDecimal novoValorTotal = BigDecimal.ZERO;
        List<ItemOrcamento> novosItensOrcamento = new ArrayList<>();

        // CORREÇÃO: Operador || (OR lógico)
        if (orcamentoRequestDTO.getItensOrcamento() == null || orcamentoRequestDTO.getItensOrcamento().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O orçamento deve conter itens ao atualizar.");
        }

        // Para lidar com a remoção de itens, se orphanRemoval=true estiver configurado
        // na entidade Orcamento para a coleção de itens, simplesmente limpar e adicionar
        // novamente funcionará para remover órfãos e adicionar novos.
        // Se não, será necessário gerenciar remoções e atualizações de itens manualmente.
        orcamentoExistente.getItensOrcamento().clear(); // Limpa a coleção existente para lidar com órfãos

        for (ItemOrcamentoRequestDTO itemDTO : orcamentoRequestDTO.getItensOrcamento()) {
            // CORREÇÃO: Operador || (OR lógico)
            if (itemDTO.getQuantidade() == null || itemDTO.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Quantidade do item deve ser maior que zero.");
            }
            // CORREÇÃO: Operador || (OR lógico)
            if (itemDTO.getPrecoUnitarioCotado() == null || itemDTO.getPrecoUnitarioCotado().compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Preço unitário do item não pode ser negativo.");
            }

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                     "Produto não encontrado com ID: " + itemDTO.getProdutoId()));

            ItemOrcamento itemAtualizado;
            // CORREÇÃO: Operador != (diferente de)
            if (itemDTO.getId()!= null) {
                // Tenta encontrar o item existente para atualização
                // CORREÇÃO: Operador != (diferente de)
                itemAtualizado = orcamentoExistente.getItensOrcamento().stream()
                    .filter(item -> item.getId()!= null && item.getId().equals(itemDTO.getId()))
                    .findFirst()
                    .orElse(new ItemOrcamento()); // Se não encontrar, cria um novo (pode ser um erro se o ID for inválido)
            } else {
                itemAtualizado = new ItemOrcamento(); // Novo item
            }

            itemAtualizado.setProduto(produto);
            itemAtualizado.setQuantidade(itemDTO.getQuantidade());
            itemAtualizado.setPrecoUnitarioCotado(itemDTO.getPrecoUnitarioCotado());
            itemAtualizado.setObservacoes(itemDTO.getObservacoes());
            itemAtualizado.setOrcamento(orcamentoExistente); // Garante a associação bidirecional

            novosItensOrcamento.add(itemAtualizado);
            novoValorTotal = novoValorTotal.add(itemDTO.getQuantidade().multiply(itemDTO.getPrecoUnitarioCotado()));
        }

        orcamentoExistente.getItensOrcamento().addAll(novosItensOrcamento); // Adiciona todos os itens novos/atualizados
        orcamentoExistente.setValorTotal(novoValorTotal);

        Orcamento orcamentoAtualizado = orcamentoRepository.save(orcamentoExistente);
        return orcamentoMapper.toResponseDto(orcamentoAtualizado);
    }

    @Transactional
    public void aprovarOrcamento(Long orcamentoId) {
        Orcamento orcamentoAprovado = orcamentoRepository.findById(orcamentoId)
             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                 "Orçamento não encontrado com ID: " + orcamentoId));

        // CORREÇÃO: Operador != (diferente de)
        if (orcamentoAprovado.getStatus()!= StatusOrcamento.AGUARDANDO_APROVACAO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Orçamento não pode ser aprovado. Status atual: " + orcamentoAprovado.getStatus());
        }

        orcamentoAprovado.setStatus(StatusOrcamento.APROVADO);
        orcamentoRepository.save(orcamentoAprovado);

        SolicitacaoCompra solicitacao = orcamentoAprovado.getSolicitacaoCompra();
        // CORREÇÃO: Operador != (diferente de)
        if (solicitacao!= null) {
            // Rejeita outros orçamentos para a mesma solicitação que ainda estão AGUARDANDO_APROVACAO
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

    @Transactional(readOnly = true) // Otimização para operações de leitura
    public OrcamentoResponseDTO buscarOrcamentoPorId(Long id) {
        // Usar JOIN FETCH ou EntityGraph para garantir que itensOrcamento sejam carregados
        // Exemplo: orcamentoRepository.findByIdWithItens(id) se houver um método customizado
        Orcamento orcamento = orcamentoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                 "Orçamento não encontrado com ID: " + id));
        return orcamentoMapper.toResponseDto(orcamento);
    }

    @Transactional(readOnly = true) // Otimização para operações de leitura
    public List<OrcamentoResponseDTO> listarTodosOrcamentos() {
        // Implementação otimizada para evitar N+1, buscando itensOrcamento com JOIN FETCH
        // Exemplo de método no repositório:
        // @Query("SELECT o FROM Orcamento o JOIN FETCH o.itensOrcamento")
        // List<Orcamento> findAllWithItens();
        return orcamentoRepository.findAll().stream() // Assumindo que findAll() ou um método customizado já carrega os itens
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

        // CORREÇÃO: Operador != (diferente de)
        if (orcamento.getStatus()!= StatusOrcamento.AGUARDANDO_APROVACAO) {
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

        // CORREÇÃO: Operador || (OR lógico)
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
        // CORREÇÃO: Operador || (OR lógico)
        if (loteDTO.getOrcamentos() == null || loteDTO.getOrcamentos().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A lista de orçamentos no lote não pode ser vazia.");
        }
        for (OrcamentoRequestDTO requestDTO : loteDTO.getOrcamentos()) {
            orcamentosCriados.add(criarOrcamento(requestDTO)); // Reutiliza o método de criação individual
        }
        return orcamentosCriados;
        // throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Funcionalidade
        // de criar orçamentos em lote ainda não implementada.");
    }
}
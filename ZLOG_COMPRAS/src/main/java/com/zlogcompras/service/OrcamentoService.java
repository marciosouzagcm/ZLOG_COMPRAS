package com.zlogcompras.service;

import com.zlogcompras.mapper.OrcamentoMapper;
import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusOrcamento;
import com.zlogcompras.model.StatusSolicitacaoCompra; // Importe o enum de status
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
        // Validação inicial para garantir que há itens no orçamento
        if (orcamentoRequestDTO.getItensOrcamento() == null || orcamentoRequestDTO.getItensOrcamento().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O orçamento deve conter ao menos um item.");
        }

        SolicitacaoCompra solicitacaoCompra = solicitacaoCompraRepository.findById(orcamentoRequestDTO.getSolicitacaoCompraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação de compra não encontrada."));
        
        // --- INÍCIO DA VALIDAÇÃO: Impedir Criação de Orçamento para Solicitação Finalizada ---
        // Adicione aqui todos os status que devem impedir a criação de um novo orçamento.
        // Agora usando apenas StatusSolicitacaoCompra.CONCLUIDA
        if (solicitacaoCompra.getStatus() == StatusSolicitacaoCompra.CONCLUIDA || // ALTERADO AQUI (linha 71)
            solicitacaoCompra.getStatus() == StatusSolicitacaoCompra.CANCELADA) { // ATENDIDA REMOVIDO, CONCLUIDA É SUFICIENTE
            
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Não é possível criar um orçamento para a solicitação de compra com status '" + solicitacaoCompra.getStatus().name() + "'.");
        }
        // --- FIM DA VALIDAÇÃO ---

        Fornecedor fornecedor = fornecedorRepository.findById(orcamentoRequestDTO.getFornecedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado."));

        BigDecimal valorTotal = BigDecimal.ZERO;
        List<ItemOrcamento> itensOrcamento = new ArrayList<>();

        for (ItemOrcamentoRequestDTO itemDTO : orcamentoRequestDTO.getItensOrcamento()) {
            // --- INÍCIO DA VALIDAÇÃO: Preços e Quantidades ---
            if (itemDTO.getQuantidade() == null || itemDTO.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade do item do orçamento deve ser maior que zero para o produto ID: " + itemDTO.getProdutoId());
            }
            if (itemDTO.getPrecoUnitarioCotado() == null || itemDTO.getPrecoUnitarioCotado().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O preço unitário do item do orçamento deve ser maior que zero para o produto ID: " + itemDTO.getProdutoId());
            }
            // --- FIM DA VALIDAÇÃO ---

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado para o item com ID: " + itemDTO.getProdutoId()));

            ItemOrcamento itemOrcamento = orcamentoMapper.toItemOrcamentoEntity(itemDTO);
            itemOrcamento.setProduto(produto);
            itemOrcamento.setNomeProduto(produto.getNome());
            itemOrcamento.setUnidadeMedidaProduto(produto.getUnidadeMedida());
            itemOrcamento.setCodigoProduto(produto.getCodigoProduto()); 

            itensOrcamento.add(itemOrcamento);

            valorTotal = valorTotal.add(itemDTO.getQuantidade().multiply(itemDTO.getPrecoUnitarioCotado()));
        }

        Orcamento orcamento = orcamentoMapper.toEntity(orcamentoRequestDTO);

        orcamento.setSolicitacaoCompra(solicitacaoCompra);
        orcamento.setFornecedor(fornecedor);
        orcamento.setDataCotacao(LocalDate.now());
        orcamento.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);
        orcamento.setValorTotal(valorTotal);

        // Associa os itens ao orçamento antes de salvar
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
        
        // --- INÍCIO DA VALIDAÇÃO: Impedir Criação de Orçamento (Lote) para Solicitação Finalizada ---
        // Agora usando apenas StatusSolicitacaoCompra.CONCLUIDA
        if (solicitacaoCompraPrincipal.getStatus() == StatusSolicitacaoCompra.CONCLUIDA || // ALTERADO AQUI (linha 139)
            solicitacaoCompraPrincipal.getStatus() == StatusSolicitacaoCompra.CANCELADA) { // ATENDIDA REMOVIDO, CONCLUIDA É SUFICIENTE
            
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Não é possível criar orçamentos em lote para a solicitação de compra com status '" + solicitacaoCompraPrincipal.getStatus().name() + "'.");
        }
        // --- FIM DA VALIDAÇÃO ---


        List<OrcamentoResponseDTO> orcamentosCriados = new ArrayList<>();

        if (orcamentoLoteRequestDTO.getOrcamentos() == null || orcamentoLoteRequestDTO.getOrcamentos().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A lista de orçamentos no lote não pode ser vazia.");
        }

        for (OrcamentoRequestDTO orcamentoRequestDTO : orcamentoLoteRequestDTO.getOrcamentos()) {
            if (orcamentoRequestDTO.getSolicitacaoCompraId() == null ||
                !orcamentoRequestDTO.getSolicitacaoCompraId().equals(solicitacaoCompraIdDoLote)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O 'solicitacaoCompraId' em cada orçamento individual do lote deve ser o mesmo do 'solicitacaoCompraId' do lote (" + solicitacaoCompraIdDoLote + ").");
            }

            // Reutiliza o método criarOrcamento para aplicar todas as validações já implementadas
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

        // Se o ID da solicitação de compra for diferente no DTO, atualiza a referência
        if (orcamentoRequestDTO.getSolicitacaoCompraId() != null &&
            !orcamentoRequestDTO.getSolicitacaoCompraId().equals(existingOrcamento.getSolicitacaoCompra().getId())) {
            SolicitacaoCompra solicitacaoCompra = solicitacaoCompraRepository.findById(orcamentoRequestDTO.getSolicitacaoCompraId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação de compra não encontrada para o ID: " + orcamentoRequestDTO.getSolicitacaoCompraId()));
            existingOrcamento.setSolicitacaoCompra(solicitacaoCompra);
        }

        // Se o ID do fornecedor for diferente no DTO, atualiza a referência
        if (orcamentoRequestDTO.getFornecedorId() != null &&
            !orcamentoRequestDTO.getFornecedorId().equals(existingOrcamento.getFornecedor().getId())) {
            Fornecedor fornecedor = fornecedorRepository.findById(orcamentoRequestDTO.getFornecedorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado para o ID: " + orcamentoRequestDTO.getFornecedorId()));
            existingOrcamento.setFornecedor(fornecedor);
        }

        // Mapeia outras propriedades do DTO para a entidade existente
        orcamentoMapper.updateEntityFromDto(orcamentoRequestDTO, existingOrcamento);

        BigDecimal valorTotal = BigDecimal.ZERO;
        List<ItemOrcamento> updatedItens = new ArrayList<>();

        if (orcamentoRequestDTO.getItensOrcamento() == null || orcamentoRequestDTO.getItensOrcamento().isEmpty()) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O orçamento deve conter ao menos um item.");
        }

        // Mapeia itens existentes por ID para facilitar a atualização
        java.util.Map<Long, ItemOrcamento> existingItemsMap = existingOrcamento.getItensOrcamento().stream()
                .filter(item -> item.getId() != null) // Filtra itens que já têm ID (persistidos)
                .collect(Collectors.toMap(ItemOrcamento::getId, item -> item));

        for (ItemOrcamentoRequestDTO itemDTO : orcamentoRequestDTO.getItensOrcamento()) {
            // --- INÍCIO DA VALIDAÇÃO: Preços e Quantidades (também na atualização) ---
            if (itemDTO.getQuantidade() == null || itemDTO.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade do item do orçamento deve ser maior que zero para o produto ID: " + itemDTO.getProdutoId());
            }
            if (itemDTO.getPrecoUnitarioCotado() == null || itemDTO.getPrecoUnitarioCotado().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O preço unitário do item do orçamento deve ser maior que zero para o produto ID: " + itemDTO.getProdutoId());
            }
            // --- FIM DA VALIDAÇÃO ---

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado para o item com ID: " + itemDTO.getProdutoId()));

            ItemOrcamento itemOrcamento;
            if (itemDTO.getId() != null) {
                // Se o item já tem ID, tenta encontrá-lo no mapa de itens existentes
                itemOrcamento = existingItemsMap.get(itemDTO.getId());
                if (itemOrcamento == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de orçamento existente com ID " + itemDTO.getId() + " não encontrado no orçamento.");
                }
                // Atualiza as propriedades do item existente
                itemOrcamento.setQuantidade(itemDTO.getQuantidade());
                itemOrcamento.setPrecoUnitarioCotado(itemDTO.getPrecoUnitarioCotado());
                itemOrcamento.setObservacoes(itemDTO.getObservacoes());
                itemOrcamento.setProduto(produto); // Garante que o produto é o correto
                itemOrcamento.setNomeProduto(produto.getNome());
                itemOrcamento.setUnidadeMedidaProduto(produto.getUnidadeMedida());
                itemOrcamento.setCodigoProduto(produto.getCodigoProduto());
            } else {
                // Se o item não tem ID, é um novo item sendo adicionado ao orçamento existente
                itemOrcamento = orcamentoMapper.toItemOrcamentoEntity(itemDTO);
                itemOrcamento.setProduto(produto);
                itemOrcamento.setOrcamento(existingOrcamento); // Associa o novo item ao orçamento pai
                itemOrcamento.setNomeProduto(produto.getNome());
                itemOrcamento.setUnidadeMedidaProduto(produto.getUnidadeMedida());
                itemOrcamento.setCodigoProduto(produto.getCodigoProduto());
            }
            updatedItens.add(itemOrcamento);
            valorTotal = valorTotal.add(itemDTO.getQuantidade().multiply(itemDTO.getPrecoUnitarioCotado()));
        }

        // Limpa os itens existentes e adiciona os itens atualizados/novos
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
        // Adicionar validação de status antes de deletar, se necessário
        // Por exemplo: if (orcamento.getStatus() == StatusOrcamento.APROVADO) { throw ... }
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

        // Rejeita automaticamente outros orçamentos da mesma solicitação que estavam aguardando aprovação
        for (Orcamento outro : outrosOrcamentos) {
            if (outro.getStatus() == StatusOrcamento.AGUARDANDO_APROVACAO) {
                outro.setStatus(StatusOrcamento.REJEITADO);
                orcamentoRepository.save(outro);
            }
        }

        solicitacaoCompra.setStatus(StatusSolicitacaoCompra.ORCAMENTO_APROVADO);
        solicitacaoCompraRepository.save(solicitacaoCompra);

        // --- GERAÇÃO DE PEDIDO DE COMPRA A PARTIR DO ORÇAMENTO APROVADO ---
        // Aqui o PedidoCompraService é chamado para criar o pedido de compra.
        // O PedidoCompraService deve ter sua própria validação para garantir que o orçamento está APPOVED.
        pedidoCompraService.criarPedidoCompraDoOrcamento(orcamentoAprovado); 
        // Descomente a linha acima quando o método estiver pronto para ser invocado

        return orcamentoMapper.toResponseDto(orcamentoAprovado);
    }
}
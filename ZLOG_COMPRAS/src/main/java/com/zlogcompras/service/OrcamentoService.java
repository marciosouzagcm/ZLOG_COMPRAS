package com.zlogcompras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.repository.ItemOrcamentoRepository;
import com.zlogcompras.repository.OrcamentoRepository;

@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private ItemOrcamentoRepository itemOrcamentoRepository;

    public List<Orcamento> listarTodosOrcamentos() {
        return orcamentoRepository.findAll();
    }

    public Optional<Orcamento> buscarOrcamentoPorId(Long id) {
        return orcamentoRepository.findById(id);
    }

    public Orcamento salvarOrcamento(Orcamento orcamento) {
        return orcamentoRepository.save(orcamento);
    }

    public List<Orcamento> buscarOrcamentosPorSolicitacaoCompraId(Long solicitacaoCompraId) {
        List<Orcamento> orcamentos = orcamentoRepository.findBySolicitacaoCompra_Id(solicitacaoCompraId);
        for (Orcamento orcamento : orcamentos) {
            List<ItemOrcamento> itens = itemOrcamentoRepository.findByOrcamento_Id(orcamento.getId());
            orcamento.setItensOrcamento(itens);
        }
        return orcamentos;
    }

    // Método para buscar orçamentos por SolicitacaoCompra (usando a entidade)
    public List<Orcamento> buscarOrcamentosPorSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
        List<Orcamento> orcamentos = orcamentoRepository.findBySolicitacaoCompra(solicitacaoCompra);
        for (Orcamento orcamento : orcamentos) {
            List<ItemOrcamento> itens = itemOrcamentoRepository.findByOrcamento_Id(orcamento.getId());
            orcamento.setItensOrcamento(itens);
        }
        return orcamentos;
    }

    @Autowired(required = false) // Indica que a dependência é opcional
    private ItemSolicitacaoCompraService itemSolicitacaoCompraService;

    // Novo método para buscar orçamentos por ItemSolicitacaoCompra (navegando pelas entidades)
    public List<Orcamento> buscarOrcamentosPorItemSolicitacao(Long itemSolicitacaoId) {
        if (itemSolicitacaoCompraService != null) {
            Optional<ItemSolicitacaoCompra> itemSolicitacaoOptional = itemSolicitacaoCompraService.buscarItemPorId(itemSolicitacaoId);
            if (itemSolicitacaoOptional.isPresent()) {
                SolicitacaoCompra solicitacao = itemSolicitacaoOptional.get().getSolicitacaoCompra();
                return buscarOrcamentosPorSolicitacaoCompra(solicitacao);
            }
        } else {
            // Log de aviso caso o serviço não esteja injetado
            System.out.println("Aviso: ItemSolicitacaoCompraService não está injetado. A busca por ItemSolicitacao não funcionará.");
        }
        return List.of(); // Retorna uma lista vazia caso o item não seja encontrado ou o serviço não esteja disponível
    }

    // Novo método para buscar todos os orçamentos e já carregar seus itens
    public List<Orcamento> buscarTodosOrcamentosComItens() {
        List<Orcamento> orcamentos = orcamentoRepository.findAll();
        for (Orcamento orcamento : orcamentos) {
            List<ItemOrcamento> itens = itemOrcamentoRepository.findByOrcamento_Id(orcamento.getId());
            orcamento.setItensOrcamento(itens);
        }
        return orcamentos;
    }

    // Você pode adicionar outros métodos aqui para buscar orçamentos com filtros específicos,
    // como por fornecedor, data de cotação, etc.

    // Exemplo: Buscar orçamentos por um determinado fornecedor (assumindo que Orcamento tem um campo 'fornecedor')
    public List<Orcamento> buscarOrcamentosPorFornecedorId(Long fornecedorId) {
        return orcamentoRepository.findByFornecedor_Id(fornecedorId);
    }

    // Exemplo: Buscar orçamentos em um determinado intervalo de datas
    public List<Orcamento> buscarOrcamentosPorDataCotacaoBetween(java.time.LocalDate dataInicio, java.time.LocalDate dataFim) {
        return orcamentoRepository.findByDataCotacaoBetween(dataInicio, dataFim);
    }

    // Exemplo: Método para buscar orçamentos com itens para um determinado ID
    public Optional<Orcamento> buscarOrcamentoComItensPorId(Long id) {
        Optional<Orcamento> orcamentoOptional = orcamentoRepository.findById(id);
        orcamentoOptional.ifPresent(orcamento -> {
            List<ItemOrcamento> itens = itemOrcamentoRepository.findByOrcamento_Id(orcamento.getId());
            orcamento.setItensOrcamento(itens);
        });
        return orcamentoOptional;
    }
}
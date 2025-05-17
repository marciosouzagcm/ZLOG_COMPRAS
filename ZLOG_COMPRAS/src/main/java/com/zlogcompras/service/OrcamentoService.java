package com.zlogcompras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlogcompras.model.ItemOrcamento;
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

    // Removido o método incorreto:
    // public List<Orcamento> buscarOrcamentosPorItemSolicitacao(Long itemSolicitacaoId) {
    //     return orcamentoRepository.findByItemSolicitacaoCompraId(itemSolicitacaoId);
    // }

    // Novo método para buscar orçamentos por ItemSolicitacaoCompra (navegando pelas entidades)
    public List<Orcamento> buscarOrcamentosPorItemSolicitacao(Long itemSolicitacaoId) {
        // 1. Buscar o ItemSolicitacaoCompra pelo ID
        // (Assumindo que você tem um serviço para ItemSolicitacaoCompra)
        // ItemSolicitacaoCompra itemSolicitacao = itemSolicitacaoCompraService.buscarItemPorId(itemSolicitacaoId);
        // 2. Se o ItemSolicitacaoCompra for encontrado, buscar a SolicitacaoCompra associada
        // if (itemSolicitacao != null) {
        //     SolicitacaoCompra solicitacao = itemSolicitacao.getSolicitacaoCompra();
        //     return buscarOrcamentosPorSolicitacaoCompra(solicitacao);
        // }
        // return Collections.emptyList(); // Ou outra lógica de tratamento

        // Como não tenho acesso ao seu ItemSolicitacaoCompraService,
        // deixo um esqueleto de como você poderia implementar essa lógica.
        // Você precisará injetar o ItemSolicitacaoCompraService aqui.
        throw new UnsupportedOperationException("Implementar lógica para buscar por ItemSolicitacaoCompra");
    }

    // Outros métodos para buscar orçamentos por fornecedor, status, etc., podem ser adicionados aqui
}
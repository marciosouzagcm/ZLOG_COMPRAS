package com.zlogcompras.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusSolicitacaoCompra;
import com.zlogcompras.repository.SolicitacaoCompraRepository;

@Service
public class ProcessoCompraService {

    private final SolicitacaoCompraRepository solicitacaoCompraRepository;
    // Adicione outros repositórios/serviços necessários aqui (ex: OrcamentoService, EstoqueService)

    @Autowired
    public ProcessoCompraService(SolicitacaoCompraRepository solicitacaoCompraRepository) {
        this.solicitacaoCompraRepository = solicitacaoCompraRepository;
    }

    @Transactional
    public SolicitacaoCompra iniciarProcessoCompraPorSolicitacaoId(Long solicitacaoId) {
        SolicitacaoCompra solicitacao = solicitacaoCompraRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação de Compra não encontrada com ID: " + solicitacaoId));

        // Lógica para iniciar o processo de compra
        // Ex: Mudar o status da solicitação para EM_ANDAMENTO
        if (solicitacao.getStatus() == StatusSolicitacaoCompra.PENDENTE || solicitacao.getStatus() == StatusSolicitacaoCompra.ORCAMENTO_APROVADO) {
            solicitacao.setStatus(StatusSolicitacaoCompra.EM_ANDAMENTO);
            return solicitacaoCompraRepository.save(solicitacao);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível iniciar o processo de compra para a solicitação com status: " + solicitacao.getStatus());
        }
    }

    // Adicione outros métodos de serviço para o processo de compra aqui
    // Ex: aprovarOrcamento, finalizarCompra, etc.

    @Transactional
    public SolicitacaoCompra atualizarStatusSolicitacao(Long solicitacaoId, StatusSolicitacaoCompra novoStatus) {
        SolicitacaoCompra solicitacao = solicitacaoCompraRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação de Compra não encontrada com ID: " + solicitacaoId));

        solicitacao.setStatus(novoStatus);
        return solicitacaoCompraRepository.save(solicitacao);
    }
}
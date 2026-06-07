package com.zlogcompras.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusSolicitacaoCompra;
import com.zlogcompras.repository.SolicitacaoCompraRepository; // Importar o Repositório correto

@Service
public class GerenciamentoSolicitacaoService {

    private final SolicitacaoCompraRepository solicitacaoCompraRepository;

    @Autowired
    public GerenciamentoSolicitacaoService(SolicitacaoCompraRepository solicitacaoCompraRepository) {
        this.solicitacaoCompraRepository = solicitacaoCompraRepository;
    }

    @Transactional
    public SolicitacaoCompra criarSolicitacao(String descricao, String solicitante) {
        SolicitacaoCompra solicitacao = new SolicitacaoCompra();
        solicitacao.setDescricao(descricao);
        solicitacao.setSolicitante(solicitante);
        solicitacao.setDataSolicitacao(LocalDate.now());
        solicitacao.setStatus(StatusSolicitacaoCompra.PENDENTE); // Define o status inicial usando o Enum
        return solicitacaoCompraRepository.save(solicitacao);
    }

    @Transactional
    public SolicitacaoCompra atualizarStatusSolicitacao(Long solicitacaoId, StatusSolicitacaoCompra novoStatus) {
        SolicitacaoCompra solicitacao = solicitacaoCompraRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Solicitação de Compra não encontrada com ID: " + solicitacaoId));

        solicitacao.setStatus(novoStatus); // Define o novo status usando o Enum
        return solicitacaoCompraRepository.save(solicitacao);
    }

    @Transactional
    public SolicitacaoCompra buscarSolicitacaoPorId(Long id) {
        return solicitacaoCompraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Solicitação de Compra não encontrada com ID: " + id));
    }
}
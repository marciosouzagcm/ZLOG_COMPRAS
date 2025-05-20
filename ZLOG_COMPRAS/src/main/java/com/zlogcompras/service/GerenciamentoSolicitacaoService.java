package com.zlogcompras.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.repository.SolicitacaoCompraRepository;

@Service
public class GerenciamentoSolicitacaoService {

    @Autowired
    private SolicitacaoCompraRepository solicitacaoCompraRepository;

    @Transactional
    public SolicitacaoCompra criarNovaSolicitacao(SolicitacaoCompra solicitacao) {
        solicitacao.setDataSolicitacao(LocalDate.now());
        solicitacao.setStatus("Pendente");
        // A persistência dos itens será gerenciada pelo SolicitacaoCompraService.atualizarSolicitacao
        return solicitacaoCompraRepository.save(solicitacao);
    }
}
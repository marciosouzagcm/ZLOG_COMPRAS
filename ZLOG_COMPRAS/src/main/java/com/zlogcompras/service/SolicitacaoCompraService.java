package com.zlogcompras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.repository.SolicitacaoCompraRepository;

@Service
public class SolicitacaoCompraService {

    private final SolicitacaoCompraRepository solicitacaoCompraRepository;

    public SolicitacaoCompraService(SolicitacaoCompraRepository solicitacaoCompraRepository) {
        this.solicitacaoCompraRepository = solicitacaoCompraRepository;
    }

    @Transactional
    public SolicitacaoCompra atualizarSolicitacao(Long id, SolicitacaoCompra solicitacaoAtualizada) {
        Optional<SolicitacaoCompra> solicitacaoExistenteOpt = solicitacaoCompraRepository.findById(id);

        if (solicitacaoExistenteOpt.isPresent()) {
            SolicitacaoCompra solicitacaoExistente = solicitacaoExistenteOpt.get();

            solicitacaoExistente.setDataSolicitacao(solicitacaoAtualizada.getDataSolicitacao());
            solicitacaoExistente.setSolicitante(solicitacaoAtualizada.getSolicitante());
            solicitacaoExistente.setStatus(solicitacaoAtualizada.getStatus());
            solicitacaoExistente.setItens(solicitacaoAtualizada.getItens());

            return solicitacaoCompraRepository.save(solicitacaoExistente);
        } else {
            throw new RuntimeException("Solicitação de compra não encontrada com ID: " + id);
        }
    }

    @Transactional
    public SolicitacaoCompra criarSolicitacao(SolicitacaoCompra solicitacaoCompra) {
        return solicitacaoCompraRepository.save(solicitacaoCompra);
    }

    public Optional<SolicitacaoCompra> buscarSolicitacaoPorId(Long solicitacaoId) {
        return solicitacaoCompraRepository.findById(solicitacaoId);
    }

    public List<SolicitacaoCompra> listarTodas() {
        return solicitacaoCompraRepository.findAll();
    }

    @Transactional
    public boolean deletarSolicitacao(Long id) {
        if (solicitacaoCompraRepository.existsById(id)) {
            solicitacaoCompraRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void atualizarStatusSolicitacao(Long id, String novoStatus) {
        Optional<SolicitacaoCompra> solicitacaoOpt = solicitacaoCompraRepository.findById(id);
        if (solicitacaoOpt.isPresent()) {
            SolicitacaoCompra solicitacao = solicitacaoOpt.get();
            solicitacao.setStatus(novoStatus);
            solicitacaoCompraRepository.save(solicitacao);
        } else {
            throw new RuntimeException("Solicitação de compra não encontrada com ID: " + id);
        }
    }

    void atualizarSolicitacao(SolicitacaoCompra solicitacaoCompra) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
package com.zlogcompras.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; // Import do Enum
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.StatusItemSolicitacao;
import com.zlogcompras.repository.ItemSolicitacaoCompraRepository;

@Service
public class ItemSolicitacaoCompraService {

    private final ItemSolicitacaoCompraRepository itemSolicitacaoCompraRepository;

    @Autowired
    public ItemSolicitacaoCompraService(ItemSolicitacaoCompraRepository itemSolicitacaoCompraRepository) {
        this.itemSolicitacaoCompraRepository = itemSolicitacaoCompraRepository;
    }

    @Transactional
    public ItemSolicitacaoCompra criarItemSolicitacao(ItemSolicitacaoCompra item) {
        // Lógica de validação ou processamento antes de salvar
        return itemSolicitacaoCompraRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<ItemSolicitacaoCompra> listarTodosItensSolicitacao() {
        return itemSolicitacaoCompraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ItemSolicitacaoCompra buscarItemSolicitacaoPorId(Long id) {
        return itemSolicitacaoCompraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de Solicitação de Compra não encontrado com ID: " + id));
    }

    @Transactional
    public ItemSolicitacaoCompra atualizarStatusItem(Long itemId, String novoStatusString) {
        ItemSolicitacaoCompra item = itemSolicitacaoCompraRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de Solicitação de Compra não encontrado com ID: " + itemId));

        try {
            StatusItemSolicitacao novoStatus = StatusItemSolicitacao.valueOf(novoStatusString.toUpperCase());
            item.setStatus(novoStatus); // <--- Correção: Converte String para Enum
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + novoStatusString);
        }

        return itemSolicitacaoCompraRepository.save(item);
    }

    @Transactional
    public void deletarItemSolicitacao(Long id) {
        ItemSolicitacaoCompra item = itemSolicitacaoCompraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de Solicitação de Compra não encontrado com ID: " + id));
        itemSolicitacaoCompraRepository.delete(item);
    }
}
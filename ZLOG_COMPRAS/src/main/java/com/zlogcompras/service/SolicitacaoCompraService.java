package com.zlogcompras.service;

import java.util.HashSet;
import java.util.List; // Importar ItemSolicitacaoCompra
import java.util.Optional; // Importar Produto, pois os itens agora se referem a ele
import java.util.Set;

import org.springframework.stereotype.Service; // Adicionar este repositório
import org.springframework.transaction.annotation.Transactional;

import com.zlogcompras.model.ItemSolicitacaoCompra; // Para um tratamento de erro mais específico
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.repository.ProdutoRepository;
import com.zlogcompras.repository.SolicitacaoCompraRepository;

import jakarta.persistence.EntityNotFoundException; // Importar Set

@Service
public class SolicitacaoCompraService {

    private final SolicitacaoCompraRepository solicitacaoCompraRepository;
    private final ProdutoRepository produtoRepository; // Injetar o repositório de Produto

    // Injeção de dependência via construtor
    public SolicitacaoCompraService(SolicitacaoCompraRepository solicitacaoCompraRepository, ProdutoRepository produtoRepository) {
        this.solicitacaoCompraRepository = solicitacaoCompraRepository;
        this.produtoRepository = produtoRepository; // Inicializar o repositório de Produto
    }

    @Transactional
    public SolicitacaoCompra criarSolicitacao(SolicitacaoCompra solicitacaoCompra) {
        // Antes de salvar a solicitação, precisamos garantir que seus itens
        // tenham a referência correta à solicitação pai e aos produtos.
        if (solicitacaoCompra.getItens() != null && !solicitacaoCompra.getItens().isEmpty()) {
            for (ItemSolicitacaoCompra item : solicitacaoCompra.getItens()) {
                // Garante que o item esteja vinculado à solicitação principal
                item.setSolicitacaoCompra(solicitacaoCompra);

                // ** IMPORTANTE: Validar e buscar o Produto **
                // Se o produto_id do item for nulo ou não existir, lançar exceção.
                if (item.getProduto() == null || item.getProduto().getId() == null) {
                    throw new IllegalArgumentException("Produto associado a um item da solicitação não pode ser nulo ou ter ID nulo.");
                }
                Produto produtoExistente = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto com ID " + item.getProduto().getId() + " não encontrado para o item da solicitação."));
                item.setProduto(produtoExistente); // Atribui o produto gerenciado pelo JPA
            }
        }
        return solicitacaoCompraRepository.save(solicitacaoCompra);
    }

    @Transactional
    public SolicitacaoCompra atualizarSolicitacao(Long id, SolicitacaoCompra solicitacaoAtualizada) {
        SolicitacaoCompra solicitacaoExistente = solicitacaoCompraRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Solicitação de compra não encontrada com ID: " + id));

        // Atualiza os campos básicos da solicitação
        solicitacaoExistente.setDataSolicitacao(solicitacaoAtualizada.getDataSolicitacao());
        solicitacaoExistente.setSolicitante(solicitacaoAtualizada.getSolicitante());
        solicitacaoExistente.setStatus(solicitacaoAtualizada.getStatus());

        // Lógica crucial para lidar com os itens aninhados:
        // A entidade SolicitacaoCompra.setItens() já contém a lógica para adicionar,
        // remover e atualizar itens, incluindo a bidirecionalidade e a validação/busca de Produto.
        // Precisamos garantir que os produtos referenciados nos itens sejam entidades gerenciadas.
        if (solicitacaoAtualizada.getItens() != null) {
            Set<ItemSolicitacaoCompra> itensComProdutosGerenciados = new HashSet<>();
            for (ItemSolicitacaoCompra item : solicitacaoAtualizada.getItens()) {
                if (item.getProduto() == null || item.getProduto().getId() == null) {
                    throw new IllegalArgumentException("Produto associado a um item da solicitação não pode ser nulo ou ter ID nulo.");
                }
                Produto produtoExistente = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto com ID " + item.getProduto().getId() + " não encontrado para o item da solicitação."));
                item.setProduto(produtoExistente); // Atribui o produto gerenciado
                itensComProdutosGerenciados.add(item); // Adiciona o item com produto gerenciado
            }
            solicitacaoExistente.setItens(itensComProdutosGerenciados); // Chama o setter que gerencia a coleção
        } else {
            solicitacaoExistente.setItens(new HashSet<>()); // Se a nova lista for nula, remove todos os itens existentes
        }

        return solicitacaoCompraRepository.save(solicitacaoExistente);
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
        SolicitacaoCompra solicitacao = solicitacaoCompraRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Solicitação de compra não encontrada com ID: " + id));
        solicitacao.setStatus(novoStatus);
        solicitacaoCompraRepository.save(solicitacao);
    }
}
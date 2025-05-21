package com.zlogcompras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlogcompras.model.Produto;
import com.zlogcompras.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired // A injeção de dependência via construtor é geralmente preferida
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Produto criarProduto(Produto produto) {
        // Lógica de negócio adicional antes de salvar, se houver
        // Ex: Validar campos, aplicar regras, etc.
        System.out.println("Serviço: Criando produto: " + produto.getNome());
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodosProdutos() {
        System.out.println("Serviço: Listando todos os produtos.");
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        System.out.println("Serviço: Buscando produto por ID: " + id);
        return produtoRepository.findById(id);
    }

    @Transactional
    public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
        System.out.println("Serviço: Tentando atualizar produto com ID: " + id);
        return produtoRepository.findById(id)
                .map(produtoExistente -> {
                    // Atualiza os campos do produto existente com os dados do produtoAtualizado
                    produtoExistente.setNome(produtoAtualizado.getNome());
                    produtoExistente.setDescricao(produtoAtualizado.getDescricao());
                    produtoExistente.setUnidadeMedida(produtoAtualizado.getUnidadeMedida());
                    produtoExistente.setPrecoUnitario(produtoAtualizado.getPrecoUnitario());
                    // O campo 'version' é gerenciado automaticamente pelo @Version do JPA

                    System.out.println("Serviço: Produto com ID " + id + " atualizado.");
                    return produtoRepository.save(produtoExistente); // Salva as alterações
                })
                .orElseThrow(() -> {
                    System.out.println("Serviço: Produto com ID " + id + " não encontrado para atualização.");
                    return new RuntimeException("Produto não encontrado com ID: " + id);
                });
    }

    @Transactional
    public boolean deletarProduto(Long id) {
        System.out.println("Serviço: Tentando deletar produto com ID: " + id);
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            System.out.println("Serviço: Produto com ID " + id + " deletado.");
            return true;
        }
        System.out.println("Serviço: Produto com ID " + id + " não encontrado para deleção.");
        return false;
    }
}
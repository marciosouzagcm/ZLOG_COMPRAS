package com.zlogcompras.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlogcompras.exceptions.ResourceNotFoundException;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.dto.ProdutoRequestDTO;
import com.zlogcompras.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Converte um DTO de requisição para uma entidade Produto.
     * 
     * @param produtoRequestDTO DTO de entrada.
     * @return A entidade Produto.
     */
    private Produto convertToEntity(ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = new Produto();
        produto.setNome(produtoRequestDTO.getNome());
        produto.setCodigo(produtoRequestDTO.getCodigo());
        produto.setDescricao(produtoRequestDTO.getDescricao());
        produto.setPrecoUnitario(produtoRequestDTO.getPrecoUnitario());
        produto.setEstoque(produtoRequestDTO.getEstoque());
        return produto;
    }

    public Produto criar(ProdutoRequestDTO produtoRequestDTO) {
        Produto novoProduto = convertToEntity(produtoRequestDTO);
        return produtoRepository.save(novoProduto);
    }

    public List<Produto> criarMultiplos(List<ProdutoRequestDTO> produtosRequestDTO) {
        List<Produto> produtosParaSalvar = produtosRequestDTO.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        return produtoRepository.saveAll(produtosParaSalvar);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
    }

    public Produto atualizar(Long id, ProdutoRequestDTO produtoRequestDTO) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        produtoExistente.setNome(produtoRequestDTO.getNome());
        produtoExistente.setCodigo(produtoRequestDTO.getCodigo());
        produtoExistente.setDescricao(produtoRequestDTO.getDescricao());
        produtoExistente.setPrecoUnitario(produtoRequestDTO.getPrecoUnitario());
        produtoExistente.setEstoque(produtoRequestDTO.getEstoque());
        // O campo 'version' será atualizado automaticamente pelo JPA

        return produtoRepository.save(produtoExistente);
    }

    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }
}
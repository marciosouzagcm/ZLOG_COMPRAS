package com.zlogcompras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlogcompras.exceptions.ResourceNotFoundException;
import com.zlogcompras.model.Produto;
import com.zlogcompras.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }
    
    public Produto findById(Long id) {
        return produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
    }

    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }
    
    public void deleteById(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }
}
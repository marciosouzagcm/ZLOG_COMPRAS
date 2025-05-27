package com.zlogcompras.service;

import com.zlogcompras.model.Estoque;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.dto.EstoqueRequestDTO;
import com.zlogcompras.model.dto.EstoqueResponseDTO;
import com.zlogcompras.mapper.EstoqueMapper; // Assumindo que você tem um EstoqueMapper
import com.zlogcompras.repository.EstoqueRepository;
import com.zlogcompras.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueMapper estoqueMapper; // Injeção do EstoqueMapper

    @Autowired
    public EstoqueService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository, EstoqueMapper estoqueMapper) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueMapper = estoqueMapper;
    }

    @Transactional
    public EstoqueResponseDTO criarEstoque(EstoqueRequestDTO estoqueRequestDTO) {
        Produto produto = produtoRepository.findById(estoqueRequestDTO.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + estoqueRequestDTO.getProdutoId()));

        Estoque estoque = estoqueMapper.toEntity(estoqueRequestDTO);
        estoque.setProduto(produto); // Associa o produto ao estoque

        Estoque savedEstoque = estoqueRepository.save(estoque);
        return estoqueMapper.toResponseDto(savedEstoque);
    }

    @Transactional(readOnly = true)
    public List<EstoqueResponseDTO> listarTodosEstoques() {
        return estoqueRepository.findAll().stream()
                .map(estoqueMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstoqueResponseDTO buscarEstoquePorId(Long id) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado com ID: " + id));
        return estoqueMapper.toResponseDto(estoque);
    }

    @Transactional(readOnly = true)
    public EstoqueResponseDTO buscarEstoquePorCodigoProduto(String codigoProduto) {
        // Encontra o produto pelo código
        Produto produto = produtoRepository.findByCodigo(codigoProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com código: " + codigoProduto));

        // Encontra o estoque associado a este produto
        // Você pode ter um findByProduto no seu EstoqueRepository
        Estoque estoque = estoqueRepository.findByProduto(produto) // <--- MÉTODO findByProduto NO REPOSITORY
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o produto com código: " + codigoProduto));
        return estoqueMapper.toResponseDto(estoque);
    }

    @Transactional
    public EstoqueResponseDTO atualizarEstoque(Long id, EstoqueRequestDTO estoqueRequestDTO) {
        Estoque estoqueExistente = estoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado com ID: " + id));

        Produto produto = produtoRepository.findById(estoqueRequestDTO.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + estoqueRequestDTO.getProdutoId()));

        // Atualiza os campos do estoque existente
        estoqueExistente.setProduto(produto); // Pode ser atualizado se o produto mudar
        estoqueExistente.setQuantidadeAtual(estoqueRequestDTO.getQuantidadeAtual());
        estoqueExistente.setDataEntrada(estoqueRequestDTO.getDataEntrada());
        estoqueExistente.setLocalizacao(estoqueRequestDTO.getLocalizacao());

        Estoque updatedEstoque = estoqueRepository.save(estoqueExistente);
        return estoqueMapper.toResponseDto(updatedEstoque);
    }

    @Transactional
    public void deletarEstoque(Long id) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado com ID: " + id));
        estoqueRepository.delete(estoque);
    }
}
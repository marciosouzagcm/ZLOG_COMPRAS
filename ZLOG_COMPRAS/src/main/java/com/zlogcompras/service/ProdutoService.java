package com.zlogcompras.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Importar Collectors

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importar Transactional

import com.zlogcompras.exceptions.ResourceNotFoundException; // Assumindo que você tem essa exceção
import com.zlogcompras.mapper.ProdutoMapper;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.dto.ProdutoRequestDTO;
import com.zlogcompras.model.dto.ProdutoResponseDTO;
import com.zlogcompras.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper; // Injetar o ProdutoMapper

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
    }

    @Transactional // Garante que a operação seja atômica
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO produtoRequestDTO) {
        // Converte o DTO de requisição para a entidade Produto
        Produto produto = produtoMapper.toEntity(produtoRequestDTO);
        // Salva a entidade
        Produto produtoSalvo = produtoRepository.save(produto);
        // Converte a entidade salva para o DTO de resposta
        return produtoMapper.toResponseDto(produtoSalvo);
    }

    @Transactional
    public List<ProdutoResponseDTO> criarMultiplosProdutos(List<ProdutoRequestDTO> produtosRequestDTO) {
        System.out.println("Service: Recebida lista de " + produtosRequestDTO.size() + " produtos para criação.");

        // Mapeia DTOs para entidades, salva e mapeia de volta para DTOs de resposta
        List<Produto> produtosParaSalvar = produtosRequestDTO.stream()
            .map(produtoMapper::toEntity)
            .collect(Collectors.toList());

        List<Produto> produtosSalvos = produtoRepository.saveAll(produtosParaSalvar); // Usar saveAll é mais eficiente

        System.out.println("Service: " + produtosSalvos.size() + " produtos salvos com sucesso.");
        return produtosSalvos.stream()
            .map(produtoMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    public List<ProdutoResponseDTO> listarTodosProdutos() {
        return produtoRepository.findAll().stream()
            .map(produtoMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    public ProdutoResponseDTO buscarProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        return produtoMapper.toResponseDto(produto);
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO produtoRequestDTO) {
        Produto produtoExistente = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + id + " não encontrado para atualização."));

        // Utiliza o mapper para atualizar a entidade existente com os dados do DTO
        produtoMapper.updateEntityFromDto(produtoRequestDTO, produtoExistente);

        // AQUI ESTAVA O ERRO ORIGINAL! Agora usando getCodigoProduto()
        // Exemplo: se você tivesse uma validação aqui, seria:
        // if (produtoRepository.findByCodigoProduto(produtoExistente.getCodigoProduto()).isPresent()) {
        //     // ... lógica
        // }

        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return produtoMapper.toResponseDto(produtoAtualizado);
    }

    @Transactional
    public boolean deletarProduto(Long id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
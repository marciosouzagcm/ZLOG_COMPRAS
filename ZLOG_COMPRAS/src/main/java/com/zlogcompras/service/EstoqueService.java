package com.zlogcompras.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlogcompras.exceptions.ResourceNotFoundException;
import com.zlogcompras.model.Estoque;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.dto.EstoqueRequestDTO;
import com.zlogcompras.model.dto.EstoqueResponseDTO;
import com.zlogcompras.repository.EstoqueRepository;
import com.zlogcompras.repository.ProdutoRepository; // Importe ResourceNotFoundException (no plural)

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EstoqueService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository,
                          ModelMapper modelMapper) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public EstoqueResponseDTO criarEstoque(EstoqueRequestDTO estoqueRequestDTO) {
        Produto produto = produtoRepository.findById(estoqueRequestDTO.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto não encontrado com ID: " + estoqueRequestDTO.getProdutoId()));

        Estoque estoque = modelMapper.map(estoqueRequestDTO, Estoque.class);
        estoque.setProduto(produto);

        // --- CORREÇÃO AQUI: Removido .toLocalDate() ---
        if (estoqueRequestDTO.getQuantidade() != null) {
            estoque.setQuantidade(estoqueRequestDTO.getQuantidade().intValue()); // Converte BigDecimal para Integer
        }
        if (estoqueRequestDTO.getDataUltimaEntrada() != null) {
            estoque.setDataUltimaEntrada(estoqueRequestDTO.getDataUltimaEntrada()); // Atribui LocalDateTime diretamente
        }
        if (estoqueRequestDTO.getDataUltimaSaida() != null) {
            estoque.setDataUltimaSaida(estoqueRequestDTO.getDataUltimaSaida()); // Atribui LocalDateTime diretamente
        }
        // --- FIM DA CORREÇÃO ---


        Estoque salvo = estoqueRepository.save(estoque);
        return modelMapper.map(salvo, EstoqueResponseDTO.class);
    }

    @Transactional
    public List<EstoqueResponseDTO> criarMultiplosEstoques(List<EstoqueRequestDTO> estoqueRequestDTOs) {
        List<Estoque> estoquesParaSalvar = estoqueRequestDTOs.stream()
                .map(dto -> {
                    Produto produto = produtoRepository.findById(dto.getProdutoId())
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Produto não encontrado com ID: " + dto.getProdutoId()));
                    Estoque estoque = modelMapper.map(dto, Estoque.class);
                    estoque.setProduto(produto);

                    // --- CORREÇÃO AQUI: Removido .toLocalDate() ---
                    if (dto.getQuantidade() != null) {
                        estoque.setQuantidade(dto.getQuantidade().intValue()); // Converte BigDecimal para Integer
                    }
                    if (dto.getDataUltimaEntrada() != null) {
                        estoque.setDataUltimaEntrada(dto.getDataUltimaEntrada()); // Atribui LocalDateTime diretamente
                    }
                    if (dto.getDataUltimaSaida() != null) {
                        estoque.setDataUltimaSaida(dto.getDataUltimaSaida()); // Atribui LocalDateTime diretamente
                    }
                    // --- FIM DA CORREÇÃO ---

                    return estoque;
                })
                .collect(Collectors.toList());

        List<Estoque> estoquesSalvos = estoqueRepository.saveAll(estoquesParaSalvar);

        return estoquesSalvos.stream()
                .map(estoque -> modelMapper.map(estoque, EstoqueResponseDTO.class))
                .collect(Collectors.toList());
    }

    public List<EstoqueResponseDTO> listarTodosEstoques() {
        return estoqueRepository.findAll().stream()
                .map(estoque -> modelMapper.map(estoque, EstoqueResponseDTO.class))
                .collect(Collectors.toList());
    }

    public EstoqueResponseDTO buscarEstoquePorId(Long id) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado com ID: " + id));
        return modelMapper.map(estoque, EstoqueResponseDTO.class);
    }

    public EstoqueResponseDTO buscarEstoquePorCodigoProduto(String codigoProduto) {
        Estoque estoque = estoqueRepository.findByProduto_CodigoProduto(codigoProduto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estoque não encontrado para o código do produto: " + codigoProduto));
        return modelMapper.map(estoque, EstoqueResponseDTO.class);
    }

    @Transactional
    public EstoqueResponseDTO atualizarEstoque(Long id, EstoqueRequestDTO estoqueRequestDTO) {
        Estoque estoqueExistente = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado com ID: " + id));

        if (estoqueRequestDTO.getProdutoId() != null &&
                (estoqueExistente.getProduto() == null ||
                 !estoqueExistente.getProduto().getId().equals(estoqueRequestDTO.getProdutoId()))) {
            Produto novoProduto = produtoRepository.findById(estoqueRequestDTO.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Novo Produto não encontrado com ID: " + estoqueRequestDTO.getProdutoId()));
            estoqueExistente.setProduto(novoProduto);
        }

        // --- CORREÇÃO AQUI: Removido .toLocalDate() ---
        Optional.ofNullable(estoqueRequestDTO.getQuantidade())
                .ifPresent(qty -> estoqueExistente.setQuantidade(qty.intValue()));

        Optional.ofNullable(estoqueRequestDTO.getLocalizacao())
                .ifPresent(estoqueExistente::setLocalizacao);

        Optional.ofNullable(estoqueRequestDTO.getDataUltimaEntrada())
                .ifPresent(estoqueExistente::setDataUltimaEntrada); // Atribui LocalDateTime diretamente

        Optional.ofNullable(estoqueRequestDTO.getDataUltimaSaida())
                .ifPresent(estoqueExistente::setDataUltimaSaida); // Atribui LocalDateTime diretamente

        Optional.ofNullable(estoqueRequestDTO.getObservacoes())
                .ifPresent(estoqueExistente::setObservacoes);
        // --- FIM DA CORREÇÃO ---

        Estoque estoqueAtualizado = estoqueRepository.save(estoqueExistente);
        return modelMapper.map(estoqueAtualizado, EstoqueResponseDTO.class);
    }

    @Transactional
    public void deletarEstoque(Long id) {
        if (!estoqueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estoque não encontrado com ID: " + id);
        }
        estoqueRepository.deleteById(id);
    }
}
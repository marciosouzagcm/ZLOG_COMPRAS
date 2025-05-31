package com.zlogcompras.service;

import java.time.LocalDateTime; // Importe LocalDateTime
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
import com.zlogcompras.repository.ProdutoRepository;

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

        // Se a quantidade é um Integer no DTO, remova .intValue()
        // estoque.setQuantidade(estoqueRequestDTO.getQuantidade());

        // Se dataUltimaEntrada não for fornecida no DTO, pode ser definida aqui na criação
        if (estoque.getDataUltimaEntrada() == null) {
            estoque.setDataUltimaEntrada(LocalDateTime.now());
        }

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

                    // Se a quantidade é um Integer no DTO, remova .intValue()
                    // estoque.setQuantidade(dto.getQuantidade());

                    if (estoque.getDataUltimaEntrada() == null) {
                        estoque.setDataUltimaEntrada(LocalDateTime.now());
                    }

                    return estoque;
                })
                .collect(Collectors.toList());

        List<Estoque> estoquesSalvos = estoqueRepository.saveAll(estoquesParaSalvar);

        return estoquesSalvos.stream()
                .map(estoque -> modelMapper.map(estoque, EstoqueResponseDTO.class))
                .collect(Collectors.toList());
    }

    // --- NOVOS MÉTODOS PARA ADICIONAR E RETIRAR QUANTIDADE ---

    @Transactional
    public EstoqueResponseDTO adicionarQuantidadeEstoque(Long estoqueId, Integer quantidadeASomar) {
        Estoque estoqueExistente = estoqueRepository.findById(estoqueId)
            .orElseThrow(() -> new ResourceNotFoundException("Registro de estoque não encontrado com ID: " + estoqueId));

        if (quantidadeASomar == null || quantidadeASomar <= 0) {
            throw new IllegalArgumentException("A quantidade a ser adicionada deve ser um valor positivo.");
        }

        estoqueExistente.setQuantidade(estoqueExistente.getQuantidade() + quantidadeASomar);
        estoqueExistente.setDataUltimaEntrada(LocalDateTime.now()); // Atualiza a data da última entrada

        Estoque estoqueAtualizado = estoqueRepository.save(estoqueExistente);
        return modelMapper.map(estoqueAtualizado, EstoqueResponseDTO.class);
    }

    @Transactional
    public EstoqueResponseDTO retirarQuantidadeEstoque(Long estoqueId, Integer quantidadeARetirar) {
        Estoque estoqueExistente = estoqueRepository.findById(estoqueId)
            .orElseThrow(() -> new ResourceNotFoundException("Registro de estoque não encontrado com ID: " + estoqueId));

        if (quantidadeARetirar == null || quantidadeARetirar <= 0) {
            throw new IllegalArgumentException("A quantidade a ser retirada deve ser um valor positivo.");
        }

        if (estoqueExistente.getQuantidade() < quantidadeARetirar) {
            throw new IllegalArgumentException("Não há estoque suficiente para esta retirada. Quantidade disponível: " + estoqueExistente.getQuantidade());
        }

        estoqueExistente.setQuantidade(estoqueExistente.getQuantidade() - quantidadeARetirar);
        estoqueExistente.setDataUltimaSaida(LocalDateTime.now()); // Atualiza a data da última saída

        Estoque estoqueAtualizado = estoqueRepository.save(estoqueExistente);
        return modelMapper.map(estoqueAtualizado, EstoqueResponseDTO.class);
    }

    // --- FIM DOS NOVOS MÉTODOS ---

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

        Optional.ofNullable(estoqueRequestDTO.getQuantidade())
                // Se a quantidade é um Integer no DTO, remova .intValue() aqui também
                .ifPresent(qty -> estoqueExistente.setQuantidade(qty)); // Alterado de .intValue() para direto

        Optional.ofNullable(estoqueRequestDTO.getLocalizacao())
                .ifPresent(estoqueExistente::setLocalizacao);

        Optional.ofNullable(estoqueRequestDTO.getDataUltimaEntrada())
                .ifPresent(estoqueExistente::setDataUltimaEntrada);

        Optional.ofNullable(estoqueRequestDTO.getDataUltimaSaida())
                .ifPresent(estoqueExistente::setDataUltimaSaida);

        Optional.ofNullable(estoqueRequestDTO.getObservacoes())
                .ifPresent(estoqueExistente::setObservacoes);

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
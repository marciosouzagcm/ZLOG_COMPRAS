package com.zlogcompras.service;

import java.time.LocalDateTime;
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
                // Busca o produto pelo ID fornecido no DTO
                Produto produto = produtoRepository.findById(estoqueRequestDTO.getProdutoId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Produto não encontrado com ID: " + estoqueRequestDTO.getProdutoId()));

                // Mapeia o DTO para a entidade Estoque
                Estoque estoque = modelMapper.map(estoqueRequestDTO, Estoque.class);
                // Associa o produto encontrado ao estoque
                estoque.setProduto(produto);

                // Define a data da última entrada se não for fornecida no DTO
                if (estoque.getDataUltimaEntrada() == null) {
                        estoque.setDataUltimaEntrada(LocalDateTime.now());
                }

                // Salva o novo estoque no repositório
                Estoque salvo = estoqueRepository.save(estoque);
                // Mapeia a entidade salva de volta para um DTO de resposta e o retorna
                return modelMapper.map(salvo, EstoqueResponseDTO.class);
        }

        @Transactional
        public List<EstoqueResponseDTO> criarMultiplosEstoques(List<EstoqueRequestDTO> estoqueRequestDTOs) {
                List<Estoque> estoquesParaSalvar = estoqueRequestDTOs.stream()
                                .map(dto -> {
                                        // Para cada DTO, busca o produto associado
                                        Produto produto = produtoRepository.findById(dto.getProdutoId())
                                                        .orElseThrow(
                                                                        () -> new ResourceNotFoundException(
                                                                                        "Produto não encontrado com ID: "
                                                                                                        + dto.getProdutoId()));
                                        // Mapeia o DTO para a entidade Estoque
                                        Estoque estoque = modelMapper.map(dto, Estoque.class);
                                        // Associa o produto encontrado ao estoque
                                        estoque.setProduto(produto);

                                        // Define a data da última entrada se não for fornecida no DTO
                                        if (estoque.getDataUltimaEntrada() == null) {
                                                estoque.setDataUltimaEntrada(LocalDateTime.now());
                                        }

                                        return estoque;
                                })
                                .collect(Collectors.toList());

                // Salva todos os estoques em lote
                List<Estoque> estoquesSalvos = estoqueRepository.saveAll(estoquesParaSalvar);

                // Mapeia a lista de entidades salvas para uma lista de DTOs de resposta
                return estoquesSalvos.stream()
                                .map(estoque -> modelMapper.map(estoque, EstoqueResponseDTO.class))
                                .collect(Collectors.toList());
        }

        // Métodos de Movimentação de Estoque

        @Transactional
        public EstoqueResponseDTO adicionarQuantidadeEstoque(Long estoqueId, Integer quantidadeASomar) {
                // Busca o registro de estoque pelo ID
                Estoque estoqueExistente = estoqueRepository.findById(estoqueId)
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                "Registro de estoque não encontrado com ID: "
                                                                                + estoqueId));

                // Valida se a quantidade a ser adicionada é positiva
                if (quantidadeASomar == null || quantidadeASomar <= 0) {
                        throw new IllegalArgumentException("A quantidade a ser adicionada deve ser um valor positivo.");
                }

                // Adiciona a quantidade ao estoque existente
                estoqueExistente.setQuantidade(estoqueExistente.getQuantidade() + quantidadeASomar);
                // Atualiza a data da última entrada
                estoqueExistente.setDataUltimaEntrada(LocalDateTime.now());

                // Salva o estoque atualizado
                Estoque estoqueAtualizado = estoqueRepository.save(estoqueExistente);
                // Mapeia e retorna o DTO de resposta
                return modelMapper.map(estoqueAtualizado, EstoqueResponseDTO.class);
        }

        @Transactional
        public EstoqueResponseDTO retirarQuantidadeEstoque(Long estoqueId, Integer quantidadeARetirar) {
                // Busca o registro de estoque pelo ID
                Estoque estoqueExistente = estoqueRepository.findById(estoqueId)
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                "Registro de estoque não encontrado com ID: "
                                                                                + estoqueId));

                // Valida se a quantidade a ser retirada é positiva
                if (quantidadeARetirar == null || quantidadeARetirar <= 0) {
                        throw new IllegalArgumentException("A quantidade a ser retirada deve ser um valor positivo.");
                }

                // Valida se há estoque suficiente para a retirada
                if (estoqueExistente.getQuantidade() < quantidadeARetirar) {
                        throw new IllegalArgumentException(
                                        "Não há estoque suficiente para esta retirada. Quantidade disponível: "
                                                        + estoqueExistente.getQuantidade());
                }

                // Retira a quantidade do estoque existente
                estoqueExistente.setQuantidade(estoqueExistente.getQuantidade() - quantidadeARetirar);
                // Atualiza a data da última saída
                estoqueExistente.setDataUltimaSaida(LocalDateTime.now());

                // Salva o estoque atualizado
                Estoque estoqueAtualizado = estoqueRepository.save(estoqueExistente);
                // Mapeia e retorna o DTO de resposta
                return modelMapper.map(estoqueAtualizado, EstoqueResponseDTO.class);
        }

        // Métodos de Consulta e Gerenciamento

        public List<EstoqueResponseDTO> listarTodosEstoques() {
                // Busca todos os estoques e os mapeia para DTOs de resposta
                return estoqueRepository.findAll().stream()
                                .map(estoque -> modelMapper.map(estoque, EstoqueResponseDTO.class))
                                .collect(Collectors.toList());
        }

        public EstoqueResponseDTO buscarEstoquePorId(Long id) {
                // Busca um estoque pelo ID ou lança exceção se não encontrado
                Estoque estoque = estoqueRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Estoque não encontrado com ID: " + id));
                // Mapeia e retorna o DTO de resposta
                return modelMapper.map(estoque, EstoqueResponseDTO.class);
        }

        public EstoqueResponseDTO buscarEstoquePorCodigoProduto(String codigoProduto) {
                // Busca um estoque pelo código do produto ou lança exceção se não encontrado
                Estoque estoque = estoqueRepository.findByProduto_CodigoProduto(codigoProduto)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Estoque não encontrado para o código do produto: " + codigoProduto));
                // Mapeia e retorna o DTO de resposta
                return modelMapper.map(estoque, EstoqueResponseDTO.class);
        }

        @Transactional
        public EstoqueResponseDTO atualizarEstoque(Long id, EstoqueRequestDTO estoqueRequestDTO) {
                // Busca o estoque existente pelo ID
                Estoque estoqueExistente = estoqueRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Estoque não encontrado com ID: " + id));

                // Verifica se o ID do produto no DTO é diferente do produto atual do estoque
                // ou se o estoque ainda não tem um produto associado
                if (estoqueRequestDTO.getProdutoId() != null &&
                                (estoqueExistente.getProduto() == null ||
                                                !estoqueExistente.getProduto().getId()
                                                                .equals(estoqueRequestDTO.getProdutoId()))) {
                        // Busca o novo produto pelo ID e o associa ao estoque existente
                        Produto novoProduto = produtoRepository.findById(estoqueRequestDTO.getProdutoId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Novo Produto não encontrado com ID: "
                                                                        + estoqueRequestDTO.getProdutoId()));
                        estoqueExistente.setProduto(novoProduto);
                }

                // Atualiza campos do estoque apenas se os valores não forem nulos no DTO
                Optional.ofNullable(estoqueRequestDTO.getQuantidade())
                                .ifPresent(estoqueExistente::setQuantidade); // Removido .intValue()

                Optional.ofNullable(estoqueRequestDTO.getLocalizacao())
                                .ifPresent(estoqueExistente::setLocalizacao);

                Optional.ofNullable(estoqueRequestDTO.getDataUltimaEntrada())
                                .ifPresent(estoqueExistente::setDataUltimaEntrada);

                Optional.ofNullable(estoqueRequestDTO.getDataUltimaSaida())
                                .ifPresent(estoqueExistente::setDataUltimaSaida);

                Optional.ofNullable(estoqueRequestDTO.getObservacoes())
                                .ifPresent(estoqueExistente::setObservacoes);

                // Salva o estoque atualizado
                Estoque estoqueAtualizado = estoqueRepository.save(estoqueExistente);
                // Mapeia e retorna o DTO de resposta
                return modelMapper.map(estoqueAtualizado, EstoqueResponseDTO.class);
        }

        @Transactional
        public void deletarEstoque(Long id) {
                // Verifica se o estoque existe antes de tentar deletar
                if (!estoqueRepository.existsById(id)) {
                        throw new ResourceNotFoundException("Estoque não encontrado com ID: " + id);
                }
                // Deleta o estoque pelo ID
                estoqueRepository.deleteById(id);
        }
}
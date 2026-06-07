package com.zlogcompras.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        private static final Logger auditLogger = LoggerFactory.getLogger(EstoqueService.class);

        private final EstoqueRepository estoqueRepository;
        private final ProdutoRepository produtoRepository;
        private final ModelMapper modelMapper;

        @Autowired
        public EstoqueService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository, ModelMapper modelMapper) {
                this.estoqueRepository = estoqueRepository;
                this.produtoRepository = produtoRepository;
                this.modelMapper = modelMapper;
        }

        @Transactional
        public EstoqueResponseDTO criarEstoque(EstoqueRequestDTO estoqueRequestDTO) {
                Produto produto = produtoRepository.findById(estoqueRequestDTO.getProdutoId())
                                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + estoqueRequestDTO.getProdutoId()));

                Estoque estoque = modelMapper.map(estoqueRequestDTO, Estoque.class);
                estoque.setProduto(produto);

                if (estoque.getDataUltimaEntrada() == null) {
                        estoque.setDataUltimaEntrada(LocalDateTime.now());
                }

                Estoque salvo = estoqueRepository.save(estoque);
                
                auditLogger.info("AUDIT - ESTOQUE CRIADO - Estoque ID: {} | Produto ID: {} (SKU: {}) | Quantidade Inicial: {}", 
                                salvo.getId(), produto.getId(), produto.getCodigo(), salvo.getQuantidade());

                return modelMapper.map(salvo, EstoqueResponseDTO.class);
        }

        @Transactional
        public List<EstoqueResponseDTO> criarMultiplosEstoques(List<EstoqueRequestDTO> estoqueRequestDTOs) {
                List<Estoque> estoquesParaSalvar = estoqueRequestDTOs.stream()
                                .map(dto -> {
                                        Produto produto = produtoRepository.findById(dto.getProdutoId())
                                                        .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + dto.getProdutoId()));
                                        
                                        Estoque estoque = modelMapper.map(dto, Estoque.class);
                                        estoque.setProduto(produto);

                                        if (estoque.getDataUltimaEntrada() == null) {
                                                estoque.setDataUltimaEntrada(LocalDateTime.now());
                                        }

                                        return estoque;
                                })
                                .collect(Collectors.toList());

                List<Estoque> estoquesSalvos = estoqueRepository.saveAll(estoquesParaSalvar);
                auditLogger.info("AUDIT - MULTIPLOS ESTOQUES CRIADOS - Total de registros processados: {}", estoquesSalvos.size());

                return estoquesSalvos.stream()
                                .map(estoque -> modelMapper.map(estoque, EstoqueResponseDTO.class))
                                .collect(Collectors.toList());
        }

        @Transactional
        public EstoqueResponseDTO adicionarQuantidadeEstoque(Long estoqueId, Integer quantidadeASomar) {
                Estoque estoqueExistente = estoqueRepository.findById(estoqueId)
                                .orElseThrow(() -> new ResourceNotFoundException("Registro de estoque não encontrado com ID: " + estoqueId));

                if (quantidadeASomar == null || quantidadeASomar <= 0) {
                        throw new IllegalArgumentException("A quantidade a ser adicionada deve ser um valor positivo.");
                }

                int qtdAnterior = estoqueExistente.getQuantidade();
                estoqueExistente.setQuantidade(qtdAnterior + quantidadeASomar);
                estoqueExistente.setDataUltimaEntrada(LocalDateTime.now());

                Estoque estoqueAtualizado = estoqueRepository.save(estoqueExistente);

                auditLogger.info("AUDIT - ESTOQUE INCREMENTADO - Estoque ID: {} | Produto: {} (SKU: {}) | Qtd Anterior: {} | Adicionado: {} | Nova Qtd: {}", 
                                estoqueId, estoqueExistente.getProduto().getNome(), estoqueExistente.getProduto().getCodigo(), qtdAnterior, quantidadeASomar, estoqueExistente.getQuantidade());

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
                        auditLogger.warn("AUDIT - TENTATIVA DE RETIRADA INVÁLIDA - Estoque ID: {} | SKU: {} | Disponível: {} | Tentativa de Retirada: {}", 
                                        estoqueId, estoqueExistente.getProduto().getCodigo(), estoqueExistente.getQuantidade(), quantidadeARetirar);
                        
                        throw new IllegalArgumentException("Não há estoque suficiente para esta retirada. Quantidade disponível: " + estoqueExistente.getQuantidade());
                }

                int qtdAnterior = estoqueExistente.getQuantidade();
                estoqueExistente.setQuantidade(qtdAnterior - quantidadeARetirar);
                estoqueExistente.setDataUltimaSaida(LocalDateTime.now());

                Estoque estoqueAtualizado = estoqueRepository.save(estoqueExistente);

                auditLogger.info("AUDIT - ESTOQUE DECREMENTADO - Estoque ID: {} | Produto: {} (SKU: {}) | Qtd Anterior: {} | Retirado: {} | Nova Qtd: {}", 
                                estoqueId, estoqueExistente.getProduto().getNome(), estoqueExistente.getProduto().getCodigo(), qtdAnterior, quantidadeARetirar, estoqueExistente.getQuantidade());

                return modelMapper.map(estoqueAtualizado, EstoqueResponseDTO.class);
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
                Long idProduto;
                try {
                        idProduto = Long.valueOf(codigoProduto);
                } catch (NumberFormatException e) {
                        Produto produto = produtoRepository.findByCodigoProduto(codigoProduto)
                                        .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o código informado: " + codigoProduto));
                        idProduto = produto.getId();
                }

                Estoque estoque = estoqueRepository.findByProdutoId(idProduto)
                                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado para o produto informado: " + codigoProduto));
                
                return modelMapper.map(estoque, EstoqueResponseDTO.class);
        }

        @Transactional
        public EstoqueResponseDTO atualizarEstoque(Long id, EstoqueRequestDTO estoqueRequestDTO) {
                Estoque estoqueExistente = estoqueRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado com ID: " + id));

                if (estoqueRequestDTO.getProdutoId() != null &&
                                (estoqueExistente.getProduto() == null || !estoqueExistente.getProduto().getId().equals(estoqueRequestDTO.getProdutoId()))) {
                        
                        Produto novoProduto = produtoRepository.findById(estoqueRequestDTO.getProdutoId())
                                        .orElseThrow(() -> new ResourceNotFoundException("Novo Produto não encontrado com ID: " + estoqueRequestDTO.getProdutoId()));
                        estoqueExistente.setProduto(novoProduto);
                }

                Optional.ofNullable(estoqueRequestDTO.getQuantidade()).ifPresent(estoqueExistente::setQuantidade);
                Optional.ofNullable(estoqueRequestDTO.getLocalizacao()).ifPresent(estoqueExistente::setLocalizacao);
                Optional.ofNullable(estoqueRequestDTO.getDataUltimaEntrada()).ifPresent(estoqueExistente::setDataUltimaEntrada);
                Optional.ofNullable(estoqueRequestDTO.getDataUltimaSaida()).ifPresent(estoqueExistente::setDataUltimaSaida);
                Optional.ofNullable(estoqueRequestDTO.getObservacoes()).ifPresent(estoqueExistente::setObservacoes);

                Estoque estoqueAtualizado = estoqueRepository.save(estoqueExistente);
                auditLogger.info("AUDIT - METADADOS DE ESTOQUE ATUALIZADOS - Estoque ID: {} | SKU: {}", id, estoqueExistente.getProduto().getCodigo());

                return modelMapper.map(estoqueAtualizado, EstoqueResponseDTO.class);
        }

        @Transactional
        public void deletarEstoque(Long id) {
                if (!estoqueRepository.existsById(id)) {
                        throw new ResourceNotFoundException("Estoque não encontrado com ID: " + id);
                }
                estoqueRepository.deleteById(id);
                auditLogger.warn("AUDIT - REMOÇÃO DE REGISTRO - Registro de Estoque ID: {} foi deletado permanentemente.", id);
        }
}
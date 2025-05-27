package com.zlogcompras.service;

import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusItemSolicitacao;
import com.zlogcompras.model.StatusSolicitacaoCompra;
import com.zlogcompras.model.dto.ItemSolicitacaoCompraRequestDTO;
import com.zlogcompras.model.dto.SolicitacaoCompraRequestDTO;
import com.zlogcompras.model.dto.SolicitacaoCompraResponseDTO;
import com.zlogcompras.mapper.SolicitacaoCompraMapper;
import com.zlogcompras.repository.ItemSolicitacaoCompraRepository;
import com.zlogcompras.repository.ProdutoRepository;
import com.zlogcompras.repository.SolicitacaoCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // <--- Import adicionado
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SolicitacaoCompraService {

    private final SolicitacaoCompraRepository solicitacaoCompraRepository;
    private final ItemSolicitacaoCompraRepository itemSolicitacaoCompraRepository;
    private final ProdutoRepository produtoRepository;
    private final SolicitacaoCompraMapper solicitacaoCompraMapper;

    @Autowired
    public SolicitacaoCompraService(SolicitacaoCompraRepository solicitacaoCompraRepository,
                                    ItemSolicitacaoCompraRepository itemSolicitacaoCompraRepository,
                                    ProdutoRepository produtoRepository,
                                    SolicitacaoCompraMapper solicitacaoCompraMapper) {
        this.solicitacaoCompraRepository = solicitacaoCompraRepository;
        this.itemSolicitacaoCompraRepository = itemSolicitacaoCompraRepository;
        this.produtoRepository = produtoRepository;
        this.solicitacaoCompraMapper = solicitacaoCompraMapper;
    }

    @Transactional
    public SolicitacaoCompraResponseDTO criarSolicitacao(SolicitacaoCompraRequestDTO solicitacaoRequestDTO) {
        SolicitacaoCompra solicitacaoCompra = solicitacaoCompraMapper.toEntity(solicitacaoRequestDTO);

        solicitacaoCompra.setDataSolicitacao(LocalDate.now());
        solicitacaoCompra.setStatus(StatusSolicitacaoCompra.PENDENTE);

        Set<ItemSolicitacaoCompra> itens = solicitacaoRequestDTO.getItens().stream()
                .map(itemDTO -> {
                    Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                            .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + itemDTO.getProdutoId()));
                    ItemSolicitacaoCompra item = solicitacaoCompraMapper.toItemEntity(itemDTO);
                    item.setProduto(produto);
                    item.setSolicitacaoCompra(solicitacaoCompra);
                    item.setStatus(StatusItemSolicitacao.AGUARDANDO_ORCAMENTO);
                    return item;
                })
                .collect(Collectors.toSet());

        solicitacaoCompra.setItens(itens);

        SolicitacaoCompra savedSolicitacao = solicitacaoCompraRepository.save(solicitacaoCompra);
        return solicitacaoCompraMapper.toResponseDto(savedSolicitacao);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoCompraResponseDTO> listarTodasSolicitacoes() {
        return solicitacaoCompraRepository.findAll().stream()
                .map(solicitacaoCompraMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SolicitacaoCompraResponseDTO buscarSolicitacaoPorId(Long id) {
        SolicitacaoCompra solicitacaoCompra = solicitacaoCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de Compra não encontrada com ID: " + id));
        return solicitacaoCompraMapper.toResponseDto(solicitacaoCompra);
    }

    @Transactional
    public SolicitacaoCompraResponseDTO atualizarSolicitacao(Long id, SolicitacaoCompraRequestDTO solicitacaoRequestDTO) {
        SolicitacaoCompra solicitacaoExistente = solicitacaoCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de Compra não encontrada com ID: " + id));

        solicitacaoExistente.setSolicitante(solicitacaoRequestDTO.getSolicitante());
        solicitacaoExistente.setDescricao(solicitacaoRequestDTO.getDescricao());
        solicitacaoExistente.setStatus(solicitacaoCompraMapper.mapStringToStatusSolicitacaoCompra(solicitacaoRequestDTO.getStatus()));

        Set<ItemSolicitacaoCompra> itensAtuais = solicitacaoExistente.getItens();
        Set<Long> idsItensExistentes = itensAtuais.stream()
                .map(ItemSolicitacaoCompra::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<ItemSolicitacaoCompra> novosItensOuAtualizados = solicitacaoRequestDTO.getItens().stream()
                .map(itemDTO -> {
                    Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                            .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + itemDTO.getProdutoId()));

                    ItemSolicitacaoCompra item;
                    if (itemDTO.getId() != null && idsItensExistentes.contains(itemDTO.getId())) { // <--- getId() no DTO
                        item = itensAtuais.stream()
                                .filter(i -> i.getId().equals(itemDTO.getId())) // <--- getId() no DTO
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Item de solicitação não encontrado com ID: " + itemDTO.getId())); // <--- getId() no DTO
                        item.setQuantidade(itemDTO.getQuantidade());
                        item.setDescricaoAdicional(itemDTO.getDescricaoAdicional());
                        item.setStatus(solicitacaoCompraMapper.mapStringToStatusItemSolicitacao(itemDTO.getStatus()));
                    } else {
                        item = solicitacaoCompraMapper.toItemEntity(itemDTO);
                        item.setProduto(produto);
                        item.setStatus(StatusItemSolicitacao.AGUARDANDO_ORCAMENTO);
                    }
                    item.setSolicitacaoCompra(solicitacaoExistente);
                    return item;
                })
                .collect(Collectors.toSet());

        itensAtuais.removeIf(itemExistente -> !novosItensOuAtualizados.contains(itemExistente));
        itensAtuais.addAll(novosItensOuAtualizados);

        solicitacaoExistente.setItens(itensAtuais);

        SolicitacaoCompra updatedSolicitacao = solicitacaoCompraRepository.save(solicitacaoExistente);
        return solicitacaoCompraMapper.toResponseDto(updatedSolicitacao);
    }

    @Transactional
    public void deletarSolicitacao(Long id) {
        SolicitacaoCompra solicitacaoCompra = solicitacaoCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de Compra não encontrada com ID: " + id));
        solicitacaoCompraRepository.delete(solicitacaoCompra);
    }
}
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
import org.hibernate.Hibernate; // Importar Hibernate para inicialização lazy

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List; // Adicionar import para List
import java.util.Set;
import java.util.stream.Collectors; // Adicionar import para Collectors

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

        if (solicitacaoRequestDTO.getItens() == null || solicitacaoRequestDTO.getItens().isEmpty()) {
            throw new IllegalArgumentException("A solicitação de compra deve conter pelo menos um item.");
        }

        Set<ItemSolicitacaoCompra> itensProcessados = new LinkedHashSet<>();

        for (ItemSolicitacaoCompraRequestDTO itemDTO : solicitacaoRequestDTO.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + itemDTO.getProdutoId()));

            ItemSolicitacaoCompra item = solicitacaoCompraMapper.toItemEntity(itemDTO);

            item.setProduto(produto);
            item.setSolicitacaoCompra(solicitacaoCompra);
            item.setStatus(StatusItemSolicitacao.AGUARDANDO_ORCAMENTO);

            itensProcessados.add(item);
        }

        solicitacaoCompra.setItens(itensProcessados);

        SolicitacaoCompra savedSolicitacao = solicitacaoCompraRepository.save(solicitacaoCompra);

        // *** ADIÇÃO: Inicializar produtos antes de mapear para DTO de resposta ***
        // Isso é crucial se 'produto' é LAZY e você quer acessá-lo fora da transação.
        // O erro de tipo incompatível na linha 79 pode estar aqui se o import do Hibernate não for org.hibernate.Hibernate
        // ou se você estiver usando um item de forma incorreta.
        // A linha 79 é provavelmente esta:
        // CUIDADO: if (item.getProduto() != null) { item.getProduto().getId(); } // Isso inicializa o proxy
        // ou:
        // for (ItemSolicitacaoCompra item : savedSolicitacao.getItens()) { // Se a linha 79 é a declaração do loop
        //     if (item.getProduto() != null) {
        //         Hibernate.initialize(item.getProduto()); // Garante que o produto lazy seja carregado
        //     }
        // }
        // Se a linha 79 é 'Hibernate.initialize(item);' (passando o item inteiro em vez do produto), pode ser o erro.
        // O correto é passar o proxy lazy que precisa ser inicializado, que é 'item.getProduto()'.

        // FORÇANDO A INICIALIZAÇÃO DE TODOS OS PRODUTOS ASSOCIADOS AOS ITENS
        savedSolicitacao.getItens().forEach(item -> {
            if (item.getProduto() != null) {
                Hibernate.initialize(item.getProduto());
            }
        });

        return solicitacaoCompraMapper.toResponseDto(savedSolicitacao);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoCompraResponseDTO> listarTodasSolicitacoes() {
        List<SolicitacaoCompra> solicitacoes = solicitacaoCompraRepository.findAll();
        // Inicializa os produtos em cada item de cada solicitação
        solicitacoes.forEach(solicitacao -> {
            solicitacao.getItens().forEach(item -> {
                if (item.getProduto() != null) {
                    Hibernate.initialize(item.getProduto());
                }
            });
        });
        return solicitacoes.stream()
                .map(solicitacaoCompraMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SolicitacaoCompraResponseDTO buscarSolicitacaoPorId(Long id) {
        SolicitacaoCompra solicitacao = solicitacaoCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de Compra não encontrada com ID: " + id));

        // Inicializa os produtos em cada item da solicitação
        solicitacao.getItens().forEach(item -> {
            if (item.getProduto() != null) {
                Hibernate.initialize(item.getProduto());
            }
        });

        return solicitacaoCompraMapper.toResponseDto(solicitacao);
    }

    @Transactional
    public SolicitacaoCompraResponseDTO atualizarSolicitacao(Long id, SolicitacaoCompraRequestDTO solicitacaoRequestDTO) {
        SolicitacaoCompra solicitacaoExistente = solicitacaoCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de Compra não encontrada com ID: " + id));

        // Atualizar campos da solicitação principal
        solicitacaoCompraMapper.updateEntityFromDto(solicitacaoRequestDTO, solicitacaoExistente);
        solicitacaoExistente.setDataAtualizacao(null); // @PreUpdate vai preencher

        // Lógica para atualizar/remover/adicionar itens:
        // Primeiro, crie um mapa dos itens existentes para facilitar a busca
        // Isso pode ser mais complexo e requer gerenciar IDs existentes vs. novos DTOs.
        // Por simplicidade, vamos substituir todos os itens para este exemplo,
        // mas em um cenário real, você faria um CRUD nos itens.
        itemSolicitacaoCompraRepository.deleteAll(solicitacaoExistente.getItens()); // Apaga os antigos
        solicitacaoExistente.getItens().clear(); // Limpa a coleção para o Set

        if (solicitacaoRequestDTO.getItens() != null && !solicitacaoRequestDTO.getItens().isEmpty()) {
            Set<ItemSolicitacaoCompra> novosItens = new LinkedHashSet<>();
            for (ItemSolicitacaoCompraRequestDTO itemDTO : solicitacaoRequestDTO.getItens()) {
                Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + itemDTO.getProdutoId()));

                ItemSolicitacaoCompra novoItem = solicitacaoCompraMapper.toItemEntity(itemDTO);
                novoItem.setProduto(produto);
                novoItem.setSolicitacaoCompra(solicitacaoExistente); // Associa ao pai
                novoItem.setStatus(StatusItemSolicitacao.AGUARDANDO_ORCAMENTO); // Ou manter o status do DTO

                novosItens.add(novoItem);
            }
            solicitacaoExistente.setItens(novosItens);
        }

        SolicitacaoCompra updatedSolicitacao = solicitacaoCompraRepository.save(solicitacaoExistente);

        // Inicializar produtos para o DTO de resposta
        updatedSolicitacao.getItens().forEach(item -> {
            if (item.getProduto() != null) {
                Hibernate.initialize(item.getProduto());
            }
        });

        return solicitacaoCompraMapper.toResponseDto(updatedSolicitacao);
    }

    @Transactional
    public void deletarSolicitacao(Long id) {
        SolicitacaoCompra solicitacaoExistente = solicitacaoCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de Compra não encontrada com ID: " + id));
        solicitacaoCompraRepository.delete(solicitacaoExistente);
    }
}
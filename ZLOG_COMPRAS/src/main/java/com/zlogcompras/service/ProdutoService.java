package com.zlogcompras.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlogcompras.exceptions.ResourceNotFoundException;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.dto.ProdutoRequestDTO; // Importe este DTO
import com.zlogcompras.repository.ItemOrcamentoRepository;
import com.zlogcompras.repository.ItemSolicitacaoCompraRepository;
import com.zlogcompras.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemOrcamentoRepository itemOrcamentoRepository;

    @Autowired
    private ItemSolicitacaoCompraRepository itemSolicitacaoCompraRepository;

    /**
     * Cria um novo produto a partir de um DTO de requisição.
     * 
     * @param produtoRequestDTO O DTO contendo os dados do novo produto.
     * @return O produto criado e salvo no banco de dados.
     */
    @Transactional
    public Produto criarProduto(ProdutoRequestDTO produtoRequestDTO) {
        // Mapear o DTO para a entidade Produto
        Produto produto = new Produto();
        // Exemplo: produto.setNome(produtoRequestDTO.getNome());
        // produto.setDescricao(produtoRequestDTO.getDescricao());
        // ... mapeie todos os campos relevantes do DTO para a entidade Produto

        // Para simplificar e compilar, vou apenas preencher alguns campos para exemplo.
        // Você deve ter um método de mapeamento ou um construtor no DTO/Entidade para
        // isso.
        produto.setNome(produtoRequestDTO.getNome());
        produto.setCodigo(produtoRequestDTO.getCodigo());
        produto.setDescricao(produtoRequestDTO.getDescricao());
        produto.setUnidadeMedida(produtoRequestDTO.getUnidadeMedida());
        produto.setPrecoUnitario(produtoRequestDTO.getPrecoUnitario());

        return produtoRepository.save(produto);
    }

    /**
     * Cria múltiplos produtos a partir de uma lista de DTOs de requisição.
     * 
     * @param produtosRequestDTO Uma lista de DTOs contendo os dados dos novos
     *                           produtos.
     * @return Uma lista dos produtos criados e salvos no banco de dados.
     */
    @Transactional
    public List<Produto> criarMultiplosProdutos(List<ProdutoRequestDTO> produtosRequestDTO) {
        // Mapear cada DTO para uma entidade Produto e salvar
        List<Produto> produtos = produtosRequestDTO.stream()
                .map(dto -> {
                    Produto produto = new Produto();
                    // Mapear campos do DTO para a entidade Produto
                    produto.setNome(dto.getNome());
                    produto.setCodigo(dto.getCodigo());
                    produto.setDescricao(dto.getDescricao());
                    produto.setUnidadeMedida(dto.getUnidadeMedida());
                    produto.setPrecoUnitario(dto.getPrecoUnitario());
                    return produto;
                })
                .collect(Collectors.toList());

        return produtoRepository.saveAll(produtos);
    }

    /**
     * Lista todos os produtos existentes.
     * 
     * @return Uma lista de todos os produtos.
     */
    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    /**
     * Busca um produto pelo seu ID.
     * 
     * @param id O ID do produto a ser buscado.
     * @return O produto encontrado.
     * @throws ResourceNotFoundException se o produto não for encontrado.
     */
    public Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
    }

    /**
     * Atualiza um produto existente.
     * 
     * @param id                O ID do produto a ser atualizado.
     * @param produtoRequestDTO O DTO com os dados atualizados do produto.
     * @return O produto atualizado.
     * @throws ResourceNotFoundException se o produto não for encontrado.
     */
    @Transactional
    public Produto atualizarProduto(Long id, ProdutoRequestDTO produtoRequestDTO) {
        return produtoRepository.findById(id)
                .map(produtoExistente -> {
                    // Atualize os campos do produtoExistente com os dados do produtoRequestDTO
                    produtoExistente.setNome(produtoRequestDTO.getNome());
                    produtoExistente.setDescricao(produtoRequestDTO.getDescricao());
                    produtoExistente.setCodigo(produtoRequestDTO.getCodigo());
                    produtoExistente.setUnidadeMedida(produtoRequestDTO.getUnidadeMedida());
                    produtoExistente.setPrecoUnitario(produtoRequestDTO.getPrecoUnitario());
                    // ... outros campos que podem ser atualizados

                    return produtoRepository.save(produtoExistente);
                }).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
    }

    /**
     * Deleta um produto pelo seu ID, removendo primeiro suas dependências.
     * 
     * @param id O ID do produto a ser deletado.
     * @throws ResourceNotFoundException se o produto não for encontrado.
     */
    @Transactional
    public void deletarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        // 1. Deletar todos os itens de ORÇAMENTO associados a este produto
        itemOrcamentoRepository.deleteByProduto_Id(id);
        System.out.println("Itens de orçamento relacionados ao produto " + id + " deletados.");

        // 2. Deletar todos os itens de SOLICITAÇÃO DE COMPRA associados a este produto
        // (se houver)
        itemSolicitacaoCompraRepository.deleteByProduto_Id(id);
        System.out.println("Itens de solicitação de compra relacionados ao produto " + id + " deletados.");

        // 3. Deletar o produto principal
        produtoRepository.delete(produto);
        System.out.println("Produto com ID " + id + " deletado com sucesso.");
    }
}
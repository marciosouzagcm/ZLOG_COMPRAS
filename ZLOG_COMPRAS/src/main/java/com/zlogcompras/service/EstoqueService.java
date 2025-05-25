package com.zlogcompras.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlogcompras.model.Estoque;
import com.zlogcompras.model.ItemSolicitacaoCompra;
import com.zlogcompras.model.MovimentacaoEstoque;
import com.zlogcompras.model.Produto;
import com.zlogcompras.repository.EstoqueRepository;
import com.zlogcompras.repository.MovimentacaoEstoqueRepository;
import com.zlogcompras.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EstoqueService {

    // Constante para prefixo de mensagens de depuração
    private static final String DEBUG_MATERIAL_PREFIX = "DEBUG: Material ";

    private final EstoqueRepository estoqueRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final ItemSolicitacaoCompraService itemSolicitacaoCompraService;
    private final ProdutoRepository produtoRepository;

    // Injeção de dependências via construtor (melhor prática)
    public EstoqueService(EstoqueRepository estoqueRepository,
                          MovimentacaoEstoqueRepository movimentacaoEstoqueRepository,
                          ItemSolicitacaoCompraService itemSolicitacaoCompraService,
                          ProdutoRepository produtoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.itemSolicitacaoCompraService = itemSolicitacaoCompraService;
        this.produtoRepository = produtoRepository;
    }

    /**
     * Busca um registro de estoque pelo código do material.
     *
     * @param codigoMaterial O código do material a ser buscado.
     * @return Um Optional contendo o Estoque, se encontrado.
     */
    public Optional<Estoque> buscarPorCodigoMaterial(String codigoMaterial) {
        return estoqueRepository.findByCodigoMaterial(codigoMaterial);
    }

    /**
     * Cria um novo registro de estoque.
     * Este método é chamado pelo EstoqueController para persistir um novo objeto Estoque.
     *
     * @param estoque O objeto Estoque a ser salvo.
     * @return O Estoque salvo.
     */
    @Transactional
    public Estoque criarEstoque(Estoque estoque) {
        // Você pode adicionar validações aqui, como verificar se o codigoMaterial já existe
        // antes de tentar salvar, se quiser um controle mais explícito.
        // O @Column(unique=true) na entidade já garante a unicidade no nível do banco.
        return estoqueRepository.save(estoque);
    }

    /**
     * Atualiza a quantidade de um registro de estoque existente.
     *
     * @param codigoMaterial O código do material do estoque a ser atualizado.
     * @param estoqueAtualizado Objeto Estoque contendo a nova quantidade.
     * @return O Estoque atualizado.
     * @throws EntityNotFoundException se o Estoque com o códigoMaterial não for encontrado.
     */
    @Transactional
    public Estoque atualizarEstoque(String codigoMaterial, Estoque estoqueAtualizado) {
        Estoque estoqueExistente = estoqueRepository.findByCodigoMaterial(codigoMaterial)
            .orElseThrow(() -> new EntityNotFoundException("Estoque para o material " + codigoMaterial + " não encontrado para atualização."));

        // Atualiza apenas a quantidade. Se houver outros campos que podem ser alterados via PUT, adicione-os aqui.
        estoqueExistente.setQuantidade(estoqueAtualizado.getQuantidade());

        return estoqueRepository.save(estoqueExistente);
    }

    /**
     * Deleta um registro de estoque pelo código do material.
     *
     * @param codigoMaterial O código do material do estoque a ser deletado.
     * @throws EntityNotFoundException se o Estoque com o códigoMaterial não for encontrado.
     */
    @Transactional
    public void deletarEstoque(String codigoMaterial) {
        Estoque estoqueExistente = estoqueRepository.findByCodigoMaterial(codigoMaterial)
            .orElseThrow(() -> new EntityNotFoundException("Estoque para o material " + codigoMaterial + " não encontrado para exclusão."));
        estoqueRepository.delete(estoqueExistente);
        System.out.println(DEBUG_MATERIAL_PREFIX + codigoMaterial + " deletado com sucesso.");
    }

    /**
     * Verifica o estoque para um item de solicitação de compra e atualiza seu status.
     * Se houver estoque suficiente, a quantidade é subtraída do estoque e uma movimentação
     * de saída é registrada. O item da solicitação é então marcado como "Em Estoque".
     * Caso contrário, o item é marcado como "Aguardando Compra".
     *
     * @param itemSolicitacao O item da solicitação de compra a ser verificado.
     * @throws IllegalArgumentException se o Produto associado ao item for nulo ou tiver ID nulo.
     * @throws EntityNotFoundException se o Produto referenciado pelo item não for encontrado no banco de dados.
     */
    @Transactional
    public void verificarEstoque(ItemSolicitacaoCompra itemSolicitacao) {
        // Valida se o Produto associado ao item da solicitação é válido
        if (itemSolicitacao.getProduto() == null || itemSolicitacao.getProduto().getId() == null) {
            throw new IllegalArgumentException("Produto associado ao item da solicitação é nulo ou tem ID nulo.");
        }

        // Busca o Produto no banco de dados para garantir que é uma entidade gerenciada e para obter o código
        Produto produtoDoItem = produtoRepository.findById(itemSolicitacao.getProduto().getId())
                .orElseThrow(() -> new EntityNotFoundException("Produto com ID " + itemSolicitacao.getProduto().getId()
                        + " não encontrado para o item da solicitação."));

        // Obtém o código do material e a quantidade requerida do item
        String codigoMaterial = produtoDoItem.getCodigo(); // Assumindo que a entidade 'Produto' tem um campo 'codigo'
        BigDecimal quantidadeRequerida = itemSolicitacao.getQuantidade();
        Integer quantidadeInteira = quantidadeRequerida.intValue();
        // Use quantidadeInteira onde for necessário um Integer

        // Busca o estoque existente para o material
        Optional<Estoque> estoqueExistenteOpt = estoqueRepository.findByCodigoMaterial(codigoMaterial);

        if (estoqueExistenteOpt.isPresent()) {
            Estoque estoque = estoqueExistenteOpt.get();
            // Compara a quantidade disponível no estoque com a quantidade requerida
            if (estoque.getQuantidade().compareTo(quantidadeRequerida) >= 0) {
                // Se há estoque suficiente:
                System.out.println(DEBUG_MATERIAL_PREFIX + codigoMaterial + " disponível em estoque. Atendendo solicitação.");
                estoque.setQuantidade(estoque.getQuantidade().subtract(quantidadeRequerida)); // Subtrai a quantidade do estoque
                estoqueRepository.save(estoque); // Salva a atualização do estoque
                itemSolicitacaoCompraService.atualizarStatusItem(itemSolicitacao.getId(), "Em Estoque"); // Atualiza o status do item
                // Registra a movimentação de saída no histórico
                registrarMovimentacaoEstoque(codigoMaterial, quantidadeRequerida, "SAIDA",
                        "Solicitação Compra #" + itemSolicitacao.getSolicitacaoCompra().getId());
            } else {
                // Se a quantidade em estoque é insuficiente:
                System.out.println(DEBUG_MATERIAL_PREFIX + codigoMaterial
                        + " não disponível em estoque ou quantidade insuficiente. Marcando para compra.");
                itemSolicitacaoCompraService.atualizarStatusItem(itemSolicitacao.getId(), "Aguardando Compra"); // Atualiza o status do item
            }
        } else {
            // Se o material não for encontrado no estoque:
            System.out.println(DEBUG_MATERIAL_PREFIX + codigoMaterial + " não encontrado no estoque. Marcando para compra.");
            itemSolicitacaoCompraService.atualizarStatusItem(itemSolicitacao.getId(), "Aguardando Compra"); // Atualiza o status do item
        }
    }

    /**
     * Registra o recebimento de material no estoque.
     * Se o material já existe no estoque, a quantidade é adicionada.
     * Caso contrário, um novo registro de estoque é criado.
     * Uma movimentação de entrada é sempre registrada.
     *
     * @param codigoMaterial O código do material.
     * @param quantidadeRecebida A quantidade do material recebido.
     * @param referencia Uma referência para a movimentação (ex: "Nota Fiscal X", "OC Y").
     */
    @Transactional
    public void receberMaterial(String codigoMaterial, BigDecimal quantidadeRecebida, String referencia) {
        Optional<Estoque> estoqueExistenteOpt = estoqueRepository.findByCodigoMaterial(codigoMaterial);
        Estoque estoque;

        if (estoqueExistenteOpt.isPresent()) {
            estoque = estoqueExistenteOpt.get();
            estoque.setQuantidade(estoque.getQuantidade().add(quantidadeRecebida)); // Adiciona a quantidade
        } else {
            // Se o estoque não existe, cria um novo registro
            estoque = new Estoque();
            estoque.setCodigoMaterial(codigoMaterial);
            estoque.setQuantidade(quantidadeRecebida);
            // Considerar adicionar aqui outros campos do Estoque, como nome do material,
            // que poderiam ser obtidos via ProdutoRepository.
        }
        estoqueRepository.save(estoque); // Salva ou atualiza o registro de estoque
        // Registra a movimentação de entrada no histórico
        registrarMovimentacaoEstoque(codigoMaterial, quantidadeRecebida, "ENTRADA", referencia);
    }

    /**
     * Registra uma movimentação de estoque (entrada ou saída) no histórico.
     *
     * @param codigoMaterial O código do material movimentado.
     * @param quantidade A quantidade movimentada.
     * @param tipoMovimentacao O tipo de movimentação ("ENTRADA" ou "SAIDA").
     * @param referencia Uma referência para a movimentação.
     */
    private void registrarMovimentacaoEstoque(String codigoMaterial, BigDecimal quantidade, String tipoMovimentacao,
                                              String referencia) {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setCodigoMaterial(codigoMaterial);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setTipoMovimentacao(tipoMovimentacao);
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        movimentacao.setReferencia(referencia);
        movimentacaoEstoqueRepository.save(movimentacao);
    }
}
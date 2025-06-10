package com.zlogcompras.serviceTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.mapper.OrcamentoMapper;
import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.dto.ItemOrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoResponseDTO;
import com.zlogcompras.repository.FornecedorRepository;
import com.zlogcompras.repository.OrcamentoRepository;
import com.zlogcompras.repository.ProdutoRepository;
import com.zlogcompras.repository.SolicitacaoCompraRepository;
import com.zlogcompras.service.OrcamentoService;
import com.zlogcompras.service.PedidoCompraService;

@ExtendWith(MockitoExtension.class)
class OrcamentoServiceTest {

    @Mock
    private OrcamentoRepository orcamentoRepository;
    @Mock
    private SolicitacaoCompraRepository solicitacaoCompraRepository;
    @Mock
    private FornecedorRepository fornecedorRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private OrcamentoMapper orcamentoMapper;
    @Mock
    private PedidoCompraService pedidoCompraService;

    @InjectMocks
    private OrcamentoService orcamentoService;

    // Dados de exemplo para uso nos testes
    private SolicitacaoCompra mockSolicitacaoCompra;
    private Fornecedor mockFornecedor;
    private Produto mockProduto;
    private Orcamento mockOrcamento;
    private OrcamentoRequestDTO mockOrcamentoRequestDTO;
    private OrcamentoResponseDTO mockOrcamentoResponseDTO;

    @BeforeEach
    void setUp() {
        // Inicializa as entidades mock para reutilização
        mockSolicitacaoCompra = new SolicitacaoCompra();
        mockSolicitacaoCompra.setId(1L);
        mockSolicitacaoCompra.setStatus(com.zlogcompras.model.StatusSolicitacaoCompra.PENDENTE);

        mockFornecedor = new Fornecedor();
        mockFornecedor.setId(1L);

        mockProduto = new Produto();
        mockProduto.setId(100L);
        mockProduto.setNome("Produto Teste");
        mockProduto.setCodigoProduto("PROD-001");
        mockProduto.setUnidadeMedida("UN");

        mockOrcamento = new Orcamento();
        mockOrcamento.setId(1L);
        // Não setamos SolicitacaoCompra e Fornecedor aqui para que o mapper no serviço possa setá-los.
        // Eles serão setados no método de teste específico de criação de orçamento.
        mockOrcamento.setValorTotal(BigDecimal.ZERO);
        mockOrcamento.setItensOrcamento(new ArrayList<>());
        mockOrcamento.setStatus(com.zlogcompras.model.StatusOrcamento.AGUARDANDO_APROVACAO);

        // Configuração de DTOs de exemplo para criar orçamento
        ItemOrcamentoRequestDTO itemDTO = new ItemOrcamentoRequestDTO();
        itemDTO.setProdutoId(100L);
        itemDTO.setQuantidade(BigDecimal.TEN);
        itemDTO.setPrecoUnitarioCotado(BigDecimal.valueOf(50.0));
        itemDTO.setObservacoes("Item de teste");

        List<ItemOrcamentoRequestDTO> itens = new ArrayList<>();
        itens.add(itemDTO);

        mockOrcamentoRequestDTO = new OrcamentoRequestDTO();
        mockOrcamentoRequestDTO.setSolicitacaoCompraId(1L);
        mockOrcamentoRequestDTO.setFornecedorId(1L);
        mockOrcamentoRequestDTO.setItensOrcamento(itens);

        mockOrcamentoResponseDTO = new OrcamentoResponseDTO();
        mockOrcamentoResponseDTO.setId(1L);
        mockOrcamentoResponseDTO.setSolicitacaoCompraId(mockSolicitacaoCompra.getId());
        mockOrcamentoResponseDTO.setFornecedorId(mockFornecedor.getId());
        mockOrcamentoResponseDTO.setValorTotal(BigDecimal.valueOf(500.0));
        mockOrcamentoResponseDTO.setStatus(com.zlogcompras.model.StatusOrcamento.APROVADO.toString());
    }

    @Test
    @DisplayName("Deve criar um orçamento com sucesso")
    void deveCriarOrcamentoComSucesso() {
        // ARRANGE
        when(solicitacaoCompraRepository.findById(anyLong())).thenReturn(Optional.of(mockSolicitacaoCompra));
        when(fornecedorRepository.findById(anyLong())).thenReturn(Optional.of(mockFornecedor));
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(mockProduto));
        when(orcamentoMapper.toEntity(any(OrcamentoRequestDTO.class))).thenReturn(mockOrcamento);
        when(orcamentoRepository.save(any(Orcamento.class))).thenReturn(mockOrcamento);
        when(orcamentoMapper.toItemOrcamentoEntity(any(ItemOrcamentoRequestDTO.class))).thenAnswer(invocation -> {
            ItemOrcamentoRequestDTO dto = invocation.getArgument(0);
            ItemOrcamento item = new ItemOrcamento();
            item.setProduto(mockProduto);
            item.setQuantidade(dto.getQuantidade());
            item.setPrecoUnitarioCotado(dto.getPrecoUnitarioCotado());
            return item;
        });
        when(orcamentoMapper.toResponseDto(any(Orcamento.class))).thenReturn(mockOrcamentoResponseDTO);


        // ACT
        OrcamentoResponseDTO response = orcamentoService.criarOrcamento(mockOrcamentoRequestDTO);

        // ASSERT
        assertNotNull(response);
        assertEquals(1L, response.getId());

        verify(solicitacaoCompraRepository, times(1)).findById(mockOrcamentoRequestDTO.getSolicitacaoCompraId());
        verify(fornecedorRepository, times(1)).findById(mockOrcamentoRequestDTO.getFornecedorId());
        verify(produtoRepository, times(1)).findById(mockOrcamentoRequestDTO.getItensOrcamento().get(0).getProdutoId());
        verify(orcamentoMapper, times(1)).toEntity(mockOrcamentoRequestDTO);
        verify(orcamentoRepository, times(1)).save(any(Orcamento.class));
        verify(orcamentoMapper, times(1)).toResponseDto(any(Orcamento.class));
    }


    @Test
    @DisplayName("Deve lançar exceção se a quantidade do item do orçamento for zero")
    void deveLancarExcecaoSeQuantidadeItemOrcamentoZero() {
        // ARRANGE
        ItemOrcamentoRequestDTO itemDTO = new ItemOrcamentoRequestDTO();
        itemDTO.setProdutoId(100L);
        itemDTO.setQuantidade(BigDecimal.ZERO);
        itemDTO.setPrecoUnitarioCotado(BigDecimal.valueOf(10.0));

        List<ItemOrcamentoRequestDTO> itens = new ArrayList<>();
        itens.add(itemDTO);
        mockOrcamentoRequestDTO.setItensOrcamento(itens);

        // Mocks para as buscas iniciais (Solicitação e Fornecedor)
        when(solicitacaoCompraRepository.findById(anyLong())).thenReturn(Optional.of(mockSolicitacaoCompra));
        when(fornecedorRepository.findById(anyLong())).thenReturn(Optional.of(mockFornecedor));

        // ACT & ASSERT
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orcamentoService.criarOrcamento(mockOrcamentoRequestDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A quantidade do item do orçamento não pode ser zero ou negativa para o produto ID: 100", exception.getReason());

        // Verificações: Solicitacao e Fornecedor são buscados, mas o mapeador do ORÇAMENTO principal não é chamado.
        verify(solicitacaoCompraRepository, times(1)).findById(mockOrcamentoRequestDTO.getSolicitacaoCompraId());
        verify(fornecedorRepository, times(1)).findById(mockOrcamentoRequestDTO.getFornecedorId());
        verify(produtoRepository, never()).findById(anyLong()); // Produto não é buscado se quantidade é inválida
        verify(orcamentoMapper, never()).toEntity(any(OrcamentoRequestDTO.class)); // O mapper do ORÇAMENTO principal não é chamado
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se a quantidade do item do orçamento for negativa")
    void deveLancarExcecaoSeQuantidadeItemOrcamentoNegativa() {
        // ARRANGE
        ItemOrcamentoRequestDTO itemDTO = new ItemOrcamentoRequestDTO();
        itemDTO.setProdutoId(100L);
        itemDTO.setQuantidade(BigDecimal.valueOf(-5));
        itemDTO.setPrecoUnitarioCotado(BigDecimal.valueOf(10.0));

        List<ItemOrcamentoRequestDTO> itens = new ArrayList<>();
        itens.add(itemDTO);
        mockOrcamentoRequestDTO.setItensOrcamento(itens);

        when(solicitacaoCompraRepository.findById(anyLong())).thenReturn(Optional.of(mockSolicitacaoCompra));
        when(fornecedorRepository.findById(anyLong())).thenReturn(Optional.of(mockFornecedor));

        // ACT & ASSERT
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orcamentoService.criarOrcamento(mockOrcamentoRequestDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A quantidade do item do orçamento não pode ser zero ou negativa para o produto ID: 100", exception.getReason());

        verify(solicitacaoCompraRepository, times(1)).findById(mockOrcamentoRequestDTO.getSolicitacaoCompraId());
        verify(fornecedorRepository, times(1)).findById(mockOrcamentoRequestDTO.getFornecedorId());
        verify(produtoRepository, never()).findById(anyLong());
        verify(orcamentoMapper, never()).toEntity(any(OrcamentoRequestDTO.class));
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se o preço unitário do item do orçamento for negativo")
    void deveLancarExcecaoSePrecoUnitarioItemOrcamentoNegativo() {
        // ARRANGE
        ItemOrcamentoRequestDTO itemDTO = new ItemOrcamentoRequestDTO();
        itemDTO.setProdutoId(100L);
        itemDTO.setQuantidade(BigDecimal.TEN);
        itemDTO.setPrecoUnitarioCotado(BigDecimal.valueOf(-1.0));

        List<ItemOrcamentoRequestDTO> itens = new ArrayList<>();
        itens.add(itemDTO);
        mockOrcamentoRequestDTO.setItensOrcamento(itens);

        when(solicitacaoCompraRepository.findById(anyLong())).thenReturn(Optional.of(mockSolicitacaoCompra));
        when(fornecedorRepository.findById(anyLong())).thenReturn(Optional.of(mockFornecedor));

        // ACT & ASSERT
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orcamentoService.criarOrcamento(mockOrcamentoRequestDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("O preço unitário do item do orçamento não pode ser negativo para o produto ID: 100", exception.getReason());

        verify(solicitacaoCompraRepository, times(1)).findById(mockOrcamentoRequestDTO.getSolicitacaoCompraId());
        verify(fornecedorRepository, times(1)).findById(mockOrcamentoRequestDTO.getFornecedorId());
        verify(produtoRepository, never()).findById(anyLong());
        verify(orcamentoMapper, never()).toEntity(any(OrcamentoRequestDTO.class));
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se o produto de item do orçamento não for encontrado ao criar orçamento")
    void deveLancarExcecaoSeProdutoDeItemNaoEncontradoAoCriarOrcamento() {
        // ARRANGE
        Long solicitacaoId = 1L;
        Long fornecedorId = 1L;
        Long produtoInexistenteId = 101L;

        ItemOrcamentoRequestDTO itemDTO = new ItemOrcamentoRequestDTO();
        itemDTO.setProdutoId(produtoInexistenteId);
        itemDTO.setQuantidade(BigDecimal.TEN);
        itemDTO.setPrecoUnitarioCotado(BigDecimal.valueOf(100.0));

        List<ItemOrcamentoRequestDTO> itens = new ArrayList<>();
        itens.add(itemDTO);
        mockOrcamentoRequestDTO.setItensOrcamento(itens);

        when(solicitacaoCompraRepository.findById(solicitacaoId)).thenReturn(Optional.of(mockSolicitacaoCompra));
        when(fornecedorRepository.findById(fornecedorId)).thenReturn(Optional.of(mockFornecedor));
        when(produtoRepository.findById(produtoInexistenteId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orcamentoService.criarOrcamento(mockOrcamentoRequestDTO);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Produto não encontrado para o item com ID: " + produtoInexistenteId, exception.getReason());

        verify(solicitacaoCompraRepository, times(1)).findById(solicitacaoId);
        verify(fornecedorRepository, times(1)).findById(fornecedorId);
        verify(produtoRepository, times(1)).findById(produtoInexistenteId);
        verify(orcamentoMapper, never()).toEntity(any(OrcamentoRequestDTO.class));
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se a quantidade do item for zero ao atualizar orçamento")
    void deveLancarExcecaoSeQuantidadeZeroAoAtualizarOrcamento() {
        // ARRANGE
        Long orcamentoId = 1L;
        OrcamentoRequestDTO updateRequestDTO = new OrcamentoRequestDTO();
        updateRequestDTO.setSolicitacaoCompraId(mockSolicitacaoCompra.getId());
        updateRequestDTO.setFornecedorId(mockFornecedor.getId());

        ItemOrcamentoRequestDTO itemDTOZeroQuantidade = new ItemOrcamentoRequestDTO();
        itemDTOZeroQuantidade.setId(1L);
        itemDTOZeroQuantidade.setProdutoId(mockProduto.getId());
        itemDTOZeroQuantidade.setQuantidade(BigDecimal.ZERO);
        itemDTOZeroQuantidade.setPrecoUnitarioCotado(BigDecimal.valueOf(50.0));
        updateRequestDTO.setItensOrcamento(List.of(itemDTOZeroQuantidade));

        Orcamento existingOrcamento = new Orcamento();
        existingOrcamento.setId(orcamentoId);
        existingOrcamento.setSolicitacaoCompra(mockSolicitacaoCompra);
        existingOrcamento.setFornecedor(mockFornecedor);
        existingOrcamento.setStatus(com.zlogcompras.model.StatusOrcamento.AGUARDANDO_APROVACAO);
        ItemOrcamento existingItem = new ItemOrcamento();
        existingItem.setId(1L);
        existingItem.setProduto(mockProduto);
        existingItem.setQuantidade(BigDecimal.TEN);
        existingItem.setPrecoUnitarioCotado(BigDecimal.valueOf(50.0));
        existingOrcamento.setItensOrcamento(new ArrayList<>(List.of(existingItem)));

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(existingOrcamento));
        // Mockar as chamadas que *sabemos* que vão ocorrer no método antes da validação do item.
        // Se os IDs de solicitação/fornecedor no DTO são iguais aos da entidade existente,
        // a condição `!equals` será falsa e `findById` não será chamado para eles.
        // No entanto, se eles forem diferentes, as chamadas serão feitas.
        // Para este cenário de teste, os IDs são iguais, então as chamadas para `findById` em `solicitacaoCompraRepository`
        // e `fornecedorRepository` não devem ser esperadas *se elas fossem apenas para atualizar as entidades*.
        // Mas a lógica do serviço as chama *antes* da validação dos itens, para setup.
        // A validação da quantidade é a primeira a falhar no loop de itens.
        // Reavaliando, o método `atualizarOrcamento` *sempre* busca `solicitacaoCompraRepository` e `fornecedorRepository`
        // no início, *mesmo que os IDs não mudem*, para popular as entidades no `existingOrcamento` caso o mapper
        // `updateEntityFromDto` precise delas.
        // Não, re-lendo meu próprio código:
        // `if (orcamentoRequestDTO.getSolicitacaoCompraId() != null && !orcamentoRequestDTO.getSolicitacaoCompraId().equals(existingOrcamento.getSolicitacaoCompra().getId()))`
        // Isso SÓ busca se o ID *mudou*. No meu mock de `updateRequestDTO`, o `solicitacaoCompraId` é o mesmo do `mockSolicitacaoCompra.getId()`.
        // Então, essa busca `solicitacaoCompraRepository.findById` *não deveria acontecer* neste cenário.
        // Portanto, a verificação `verify(solicitacaoCompraRepository, times(1)).findById(mockSolicitacaoCompra.getId());`
        // estava de fato incorreta para este caso específico de teste.

        // Removendo a verificação da solicitacaoCompraRepository.findById e fornecedorRepository.findById
        // porque no cenário de teste atual, o ID da solicitação no DTO é o mesmo da existente,
        // então a busca pelo repositório não é acionada.
        // Além disso, a falha na quantidade do item acontece antes que o produtoRepository seja chamado.
        doNothing().when(orcamentoMapper).updateEntityFromDto(any(OrcamentoRequestDTO.class), any(Orcamento.class));

        // ACT & ASSERT
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orcamentoService.atualizarOrcamento(orcamentoId, updateRequestDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A quantidade do item do orçamento não pode ser zero ou negativa para o produto ID: " + mockProduto.getId(), exception.getReason());

        verify(orcamentoRepository, times(1)).findById(orcamentoId);
        verify(solicitacaoCompraRepository, never()).findById(anyLong()); // Não deve ser invocado se o ID não mudou no DTO
        verify(fornecedorRepository, never()).findById(anyLong());     // Não deve ser invocado se o ID não mudou no DTO
        verify(produtoRepository, never()).findById(anyLong());
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
        verify(orcamentoMapper, times(1)).updateEntityFromDto(any(OrcamentoRequestDTO.class), any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve aprovar orçamento com sucesso e rejeitar outros")
    void deveAprovarOrcamentoComSucessoERejeitarOutros() {
        // ARRANGE
        Long orcamentoIdAprovado = 1L;
        Long solicitacaoId = 10L;

        Orcamento orcamentoAprovado = new Orcamento();
        orcamentoAprovado.setId(orcamentoIdAprovado);
        orcamentoAprovado.setStatus(com.zlogcompras.model.StatusOrcamento.AGUARDANDO_APROVACAO);
        SolicitacaoCompra solicitacao = new SolicitacaoCompra();
        solicitacao.setId(solicitacaoId);
        solicitacao.setStatus(com.zlogcompras.model.StatusSolicitacaoCompra.PENDENTE);
        orcamentoAprovado.setSolicitacaoCompra(solicitacao);

        Orcamento outroOrcamento1 = new Orcamento();
        outroOrcamento1.setId(2L);
        outroOrcamento1.setStatus(com.zlogcompras.model.StatusOrcamento.AGUARDANDO_APROVACAO);
        outroOrcamento1.setSolicitacaoCompra(solicitacao);

        Orcamento outroOrcamento2 = new Orcamento();
        outroOrcamento2.setId(3L);
        outroOrcamento2.setStatus(com.zlogcompras.model.StatusOrcamento.AGUARDANDO_APROVACAO);
        outroOrcamento2.setSolicitacaoCompra(solicitacao);

        when(orcamentoRepository.findById(orcamentoIdAprovado)).thenReturn(Optional.of(orcamentoAprovado));
        when(orcamentoRepository.findBySolicitacaoCompraIdAndIdNot(solicitacaoId, orcamentoIdAprovado))
            .thenReturn(List.of(outroOrcamento1, outroOrcamento2));
        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orcamentoMapper.toResponseDto(any(Orcamento.class))).thenReturn(new OrcamentoResponseDTO());
        when(solicitacaoCompraRepository.save(any(SolicitacaoCompra.class))).thenReturn(solicitacao);

        // ACT
        orcamentoService.aprovarOrcamento(orcamentoIdAprovado);

        // ASSERT
        assertEquals(com.zlogcompras.model.StatusOrcamento.APROVADO, orcamentoAprovado.getStatus());
        assertEquals(com.zlogcompras.model.StatusOrcamento.REJEITADO, outroOrcamento1.getStatus());
        assertEquals(com.zlogcompras.model.StatusOrcamento.REJEITADO, outroOrcamento2.getStatus());
        assertEquals(com.zlogcompras.model.StatusSolicitacaoCompra.ORCAMENTO_APROVADO, solicitacao.getStatus());

        verify(orcamentoRepository, times(1)).findById(orcamentoIdAprovado);
        verify(solicitacaoCompraRepository, never()).findById(anyLong());
        verify(orcamentoRepository, times(1)).findBySolicitacaoCompraIdAndIdNot(solicitacaoId, orcamentoIdAprovado);
        verify(orcamentoRepository, times(3)).save(any(Orcamento.class));
        verify(solicitacaoCompraRepository, times(1)).save(solicitacao);
        verify(pedidoCompraService, times(1)).criarPedidoCompraDoOrcamento(orcamentoAprovado);
    }
}
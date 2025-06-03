package com.zlogcompras.serviceTest;

import com.zlogcompras.mapper.OrcamentoMapper;
import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.PedidoCompra;
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusOrcamento;
import com.zlogcompras.model.StatusSolicitacaoCompra;
import com.zlogcompras.model.dto.ItemOrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoResponseDTO;
import com.zlogcompras.repository.FornecedorRepository;
import com.zlogcompras.repository.OrcamentoRepository;
import com.zlogcompras.repository.ProdutoRepository;
import com.zlogcompras.repository.SolicitacaoCompraRepository;
import com.zlogcompras.service.OrcamentoService;
import com.zlogcompras.service.PedidoCompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para OrcamentoService")
public class OrcamentoServiceTest {

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
    private PedidoCompraService pedidoCompraService; // Renomeado para seguir convenção de nomes de bean

    @InjectMocks
    private OrcamentoService orcamentoService;

    // Entidades e DTOs de exemplo para os testes
    private SolicitacaoCompra solicitacaoCompra;
    private Fornecedor fornecedor;
    private Produto produto1;
    private Produto produto2;
    private OrcamentoRequestDTO orcamentoRequestDTO;
    private Orcamento orcamentoAguardandoAprovacao;
    private Orcamento orcamentoRejeitado1;
    private Orcamento orcamentoRejeitado2;
    private Orcamento orcamentoAprovado; // Objeto separado para o teste de aprovação

    private ItemOrcamento itemOrcamento1;
    private ItemOrcamento itemOrcamento2;

    @BeforeEach
    void setUp() {
        // Inicialização de dados comuns para os testes
        solicitacaoCompra = new SolicitacaoCompra();
        solicitacaoCompra.setId(1L);
        solicitacaoCompra.setStatus(StatusSolicitacaoCompra.ABERTA);

        fornecedor = new Fornecedor();
        fornecedor.setId(10L);
        fornecedor.setNome("Fornecedor Teste");

        produto1 = new Produto();
        produto1.setId(100L);
        produto1.setNome("Produto A");
        produto1.setCodigoProduto("PROD001"); // Correção: use setCodigoProduto
        produto1.setUnidadeMedida("UN");

        produto2 = new Produto();
        produto2.setId(101L);
        produto2.setNome("Produto B");
        produto2.setCodigoProduto("PROD002"); // Correção: use setCodigoProduto
        produto2.setUnidadeMedida("KG");

        // DTO de requisição para criação de orçamento
        ItemOrcamentoRequestDTO itemDTO1 = new ItemOrcamentoRequestDTO(null, produto1.getId(),
                BigDecimal.valueOf(2), BigDecimal.valueOf(10.00), "Obs item 1");
        ItemOrcamentoRequestDTO itemDTO2 = new ItemOrcamentoRequestDTO(null, produto2.getId(),
                BigDecimal.valueOf(1.5), BigDecimal.valueOf(20.00), "Obs item 2");

        orcamentoRequestDTO = new OrcamentoRequestDTO(
                solicitacaoCompra.getId(),
                fornecedor.getId(),
                LocalDate.now(),
                null, // Valor total não é enviado no DTO de request
                "ORC-001/2024",
                "7 dias",
                "30/60 dias",
                "Observações do orçamento",
                null, // Status não é enviado no DTO de request
                Arrays.asList(itemDTO1, itemDTO2));

        // Entidade de Orcamento que seria retornada pelo Mapper e salva pelo Repositório
        orcamentoAguardandoAprovacao = new Orcamento();
        orcamentoAguardandoAprovacao.setId(1L);
        orcamentoAguardandoAprovacao.setSolicitacaoCompra(solicitacaoCompra);
        orcamentoAguardandoAprovacao.setFornecedor(fornecedor);
        orcamentoAguardandoAprovacao.setNumeroOrcamento("ORC-001/2024");
        orcamentoAguardandoAprovacao.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);
        orcamentoAguardandoAprovacao.setDataCotacao(LocalDate.now());

        itemOrcamento1 = new ItemOrcamento();
        itemOrcamento1.setId(1000L); // Simula um ID gerado
        itemOrcamento1.setProduto(produto1);
        itemOrcamento1.setQuantidade(BigDecimal.valueOf(2));
        itemOrcamento1.setPrecoUnitarioCotado(BigDecimal.valueOf(10.00));
        itemOrcamento1.setNomeProduto(produto1.getNome());
        itemOrcamento1.setCodigoProduto(produto1.getCodigoProduto()); // Correção: use getCodigoProduto
        itemOrcamento1.setUnidadeMedidaProduto(produto1.getUnidadeMedida());
        itemOrcamento1.setOrcamento(orcamentoAguardandoAprovacao);

        itemOrcamento2 = new ItemOrcamento();
        itemOrcamento2.setId(1001L); // Simula um ID gerado
        itemOrcamento2.setProduto(produto2);
        itemOrcamento2.setQuantidade(BigDecimal.valueOf(1.5));
        itemOrcamento2.setPrecoUnitarioCotado(BigDecimal.valueOf(20.00));
        itemOrcamento2.setNomeProduto(produto2.getNome());
        itemOrcamento2.setCodigoProduto(produto2.getCodigoProduto()); // Correção: use getCodigoProduto
        itemOrcamento2.setUnidadeMedidaProduto(produto2.getUnidadeMedida());
        itemOrcamento2.setOrcamento(orcamentoAguardandoAprovacao);

        orcamentoAguardandoAprovacao.setItensOrcamento(Arrays.asList(itemOrcamento1, itemOrcamento2));
        orcamentoAguardandoAprovacao.setValorTotal(BigDecimal.valueOf(50.00)); // (2*10) + (1.5*20) = 20 + 30 = 50

        // Configuração para orçamentos que serão REJEITADOS na aprovação
        // Crie novas instâncias para evitar que testes anteriores modifiquem o estado
        orcamentoRejeitado1 = new Orcamento();
        orcamentoRejeitado1.setId(2L);
        orcamentoRejeitado1.setSolicitacaoCompra(solicitacaoCompra);
        Fornecedor f1 = new Fornecedor(); f1.setId(11L); f1.setNome("Fornecedor B");
        orcamentoRejeitado1.setFornecedor(f1);
        orcamentoRejeitado1.setNumeroOrcamento("ORC-002/2024");
        orcamentoRejeitado1.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);
        orcamentoRejeitado1.setValorTotal(BigDecimal.valueOf(120.00));

        orcamentoRejeitado2 = new Orcamento();
        orcamentoRejeitado2.setId(3L);
        orcamentoRejeitado2.setSolicitacaoCompra(solicitacaoCompra);
        Fornecedor f2 = new Fornecedor(); f2.setId(12L); f2.setNome("Fornecedor C");
        orcamentoRejeitado2.setFornecedor(f2);
        orcamentoRejeitado2.setNumeroOrcamento("ORC-003/2024");
        orcamentoRejeitado2.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);
        orcamentoRejeitado2.setValorTotal(BigDecimal.valueOf(90.00));

        // Novo orçamento que será aprovado (use uma nova instância para este teste específico)
        orcamentoAprovado = new Orcamento();
        orcamentoAprovado.setId(99L);
        orcamentoAprovado.setSolicitacaoCompra(solicitacaoCompra);
        orcamentoAprovado.setFornecedor(fornecedor);
        orcamentoAprovado.setNumeroOrcamento("ORC-APROVADO/2024");
        orcamentoAprovado.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);
        orcamentoAprovado.setDataCotacao(LocalDate.now()); // Garanta que a data de cotação não seja nula
    }

    @Test
    @DisplayName("Deve criar um orçamento com sucesso e calcular o valor total")
    void deveCriarOrcamentoComSucessoECalcularValorTotal() {
        // Cenário:
        when(solicitacaoCompraRepository.findById(solicitacaoCompra.getId()))
                .thenReturn(Optional.of(solicitacaoCompra));
        when(fornecedorRepository.findById(fornecedor.getId())).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.findById(produto1.getId())).thenReturn(Optional.of(produto1));
        when(produtoRepository.findById(produto2.getId())).thenReturn(Optional.of(produto2));

        // Mock para o MapStruct mapear DTO para entidade Orcamento
        when(orcamentoMapper.toEntity(any(OrcamentoRequestDTO.class))).thenReturn(orcamentoAguardandoAprovacao);

        // Mock para o MapStruct mapear DTOs de ItemOrcamento para entidades ItemOrcamento
        when(orcamentoMapper.toItemOrcamentoEntity(any(ItemOrcamentoRequestDTO.class)))
                .thenReturn(itemOrcamento1, itemOrcamento2); // Retorna item1 na 1ª chamada, item2 na 2ª

        // Mock para o repositório salvar o orçamento
        when(orcamentoRepository.save(any(Orcamento.class))).thenReturn(orcamentoAguardandoAprovacao);

        // Mock para o mapeamento de resposta
        when(orcamentoMapper.toResponseDto(any(Orcamento.class))).thenReturn(new OrcamentoResponseDTO(
                orcamentoAguardandoAprovacao.getId(),
                orcamentoAguardandoAprovacao.getSolicitacaoCompra().getId(),
                orcamentoAguardandoAprovacao.getFornecedor().getId(),
                orcamentoAguardandoAprovacao.getDataCotacao(),
                orcamentoAguardandoAprovacao.getValorTotal(),
                orcamentoAguardandoAprovacao.getNumeroOrcamento(),
                orcamentoAguardandoAprovacao.getPrazoEntrega(),
                orcamentoAguardandoAprovacao.getCondicoesPagamento(),
                orcamentoAguardandoAprovacao.getObservacoes(),
                orcamentoAguardandoAprovacao.getStatus().name(),
                null));

        // Ação:
        OrcamentoResponseDTO response = orcamentoService.criarOrcamento(orcamentoRequestDTO);

        // Verificações:
        assertNotNull(response);
        assertEquals(orcamentoAguardandoAprovacao.getId(), response.getId());
        assertEquals(solicitacaoCompra.getId(), response.getSolicitacaoCompraId());
        assertEquals(fornecedor.getId(), response.getFornecedorId());
        // Verifique o valor total com uma tolerância ou usando setScale para comparação de BigDecimals
        assertEquals(0, BigDecimal.valueOf(50.00).compareTo(response.getValorTotal()));
        assertEquals(StatusOrcamento.AGUARDANDO_APROVACAO.name(), response.getStatus());

        // Verifica se os métodos dos mocks foram chamados
        verify(solicitacaoCompraRepository, times(1)).findById(solicitacaoCompra.getId());
        verify(fornecedorRepository, times(1)).findById(fornecedor.getId());
        verify(produtoRepository, times(1)).findById(produto1.getId());
        verify(produtoRepository, times(1)).findById(produto2.getId());
        verify(orcamentoMapper, times(1)).toEntity(orcamentoRequestDTO);
        verify(orcamentoMapper, times(2)).toItemOrcamentoEntity(any(ItemOrcamentoRequestDTO.class));
        verify(orcamentoRepository, times(1)).save(any(Orcamento.class));
        verify(orcamentoMapper, times(1)).toResponseDto(any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se a Solicitação de Compra não for encontrada ao criar orçamento")
    void deveLancarExcecaoSeSolicitacaoCompraNaoEncontradaAoCriarOrcamento() {
        // Cenário: Solicitação de Compra não existe
        when(solicitacaoCompraRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Ação e Verificação:
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.criarOrcamento(orcamentoRequestDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Solicitação de Compra não encontrada"));
        verify(solicitacaoCompraRepository, times(1)).findById(anyLong());
        verify(fornecedorRepository, never()).findById(anyLong()); // Não deve chamar este método
    }

    @Test
    @DisplayName("Deve lançar exceção se o Fornecedor não for encontrado ao criar orçamento")
    void deveLancarExcecaoSeFornecedorNaoEncontradoAoCriarOrcamento() {
        // Cenário: Fornecedor não existe
        when(solicitacaoCompraRepository.findById(solicitacaoCompra.getId()))
                .thenReturn(Optional.of(solicitacaoCompra));
        when(fornecedorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Ação e Verificação:
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.criarOrcamento(orcamentoRequestDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Fornecedor não encontrado"));
        verify(solicitacaoCompraRepository, times(1)).findById(solicitacaoCompra.getId());
        verify(fornecedorRepository, times(1)).findById(anyLong());
        verify(produtoRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção se um Produto de um item não for encontrado ao criar orçamento")
    void deveLancarExcecaoSeProdutoDeItemNaoEncontradoAoCriarOrcamento() {
        // Cenário: Produto para um item não existe
        when(solicitacaoCompraRepository.findById(solicitacaoCompra.getId()))
                .thenReturn(Optional.of(solicitacaoCompra));
        when(fornecedorRepository.findById(fornecedor.getId())).thenReturn(Optional.of(fornecedor));
        when(orcamentoMapper.toEntity(any(OrcamentoRequestDTO.class))).thenReturn(orcamentoAguardandoAprovacao);
        when(produtoRepository.findById(produto1.getId())).thenReturn(Optional.empty()); // Produto1 não encontrado

        // Ação e Verificação:
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.criarOrcamento(orcamentoRequestDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason()
                .contains("Produto não encontrado para o item com ID: " + produto1.getId()));
        verify(solicitacaoCompraRepository, times(1)).findById(solicitacaoCompra.getId());
        verify(fornecedorRepository, times(1)).findById(fornecedor.getId());
        verify(produtoRepository, times(1)).findById(produto1.getId());
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve aprovar um orçamento e rejeitar os demais da mesma solicitação")
    void deveAprovarOrcamentoERejeitarDemaisDaMesmaSolicitacao() {
        // Cenário:
        when(orcamentoRepository.findById(orcamentoAprovado.getId()))
                .thenReturn(Optional.of(orcamentoAprovado));
        
        // **IMPORTANTE**: Mock para solicitacaoCompraRepository.findById()
        // O serviço chama findById() da Solicitação de Compra para validar e buscar a entidade completa.
        when(solicitacaoCompraRepository.findById(solicitacaoCompra.getId()))
                .thenReturn(Optional.of(solicitacaoCompra));

        // Mock para os outros orçamentos a serem rejeitados
        List<Orcamento> outrosOrcamentos = Arrays.asList(orcamentoRejeitado1, orcamentoRejeitado2);
        when(orcamentoRepository.findBySolicitacaoCompraIdAndIdNot(solicitacaoCompra.getId(),
                orcamentoAprovado.getId()))
                .thenReturn(outrosOrcamentos);

        // Mock para o save de orçamentos (qualquer orçamento, pois ele será salvo várias vezes)
        when(orcamentoRepository.save(any(Orcamento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Retorna o próprio objeto que foi passado para save

        // Mock para o PedidoCompraService criarPedidoCompraDoOrcamento
        when(pedidoCompraService.criarPedidoCompraDoOrcamento(any(Orcamento.class)))
                .thenReturn(new PedidoCompra()); // Retorna uma instância mock de PedidoCompra

        // Mock para o mapeamento de resposta
        when(orcamentoMapper.toResponseDto(any(Orcamento.class))).thenReturn(new OrcamentoResponseDTO(
                orcamentoAprovado.getId(),
                orcamentoAprovado.getSolicitacaoCompra().getId(),
                orcamentoAprovado.getFornecedor().getId(),
                orcamentoAprovado.getDataCotacao(),
                orcamentoAprovado.getValorTotal(), // Pode ser nulo se não for relevante para o teste
                orcamentoAprovado.getNumeroOrcamento(),
                orcamentoAprovado.getPrazoEntrega(),
                orcamentoAprovado.getCondicoesPagamento(),
                orcamentoAprovado.getObservacoes(),
                StatusOrcamento.APROVADO.name(), // O status esperado na resposta
                null));

        // Ação:
        OrcamentoResponseDTO response = orcamentoService.aprovarOrcamento(orcamentoAprovado.getId());

        // Verificações:
        assertNotNull(response);
        assertEquals(StatusOrcamento.APROVADO.name(), response.getStatus(),
                "O orçamento aprovado deve ter o status APROVADO.");

        // Verifica o status dos objetos originais (que foram modificados pelo serviço)
        assertEquals(StatusOrcamento.APROVADO, orcamentoAprovado.getStatus(),
                "O orçamento aprovado deve ter o status APROVADO.");
        assertEquals(StatusOrcamento.REJEITADO, orcamentoRejeitado1.getStatus(),
                "O primeiro orçamento rejeitado deve ter o status REJEITADO.");
        assertEquals(StatusOrcamento.REJEITADO, orcamentoRejeitado2.getStatus(),
                "O segundo orçamento rejeitado deve ter o status REJEITADO.");

        // Verifica se os métodos dos mocks foram chamados
        verify(orcamentoRepository, times(1)).findById(orcamentoAprovado.getId());
        verify(solicitacaoCompraRepository, times(1)).findById(solicitacaoCompra.getId()); // Verifica a nova chamada
        verify(orcamentoRepository, times(1)).findBySolicitacaoCompraIdAndIdNot(solicitacaoCompra.getId(),
                orcamentoAprovado.getId());
        // O save do orcamentoAprovado (1) + 2 saves dos orcamentos rejeitados (2) = 3 saves
        verify(orcamentoRepository, times(3)).save(any(Orcamento.class));
        // O save da solicitacaoCompra
        verify(solicitacaoCompraRepository, times(1)).save(solicitacaoCompra);
        assertEquals(StatusSolicitacaoCompra.ORCAMENTO_APROVADO, solicitacaoCompra.getStatus(),
                "O status da Solicitação de Compra deve ser ORCAMENTO_APROVADO.");
        verify(pedidoCompraService, times(1)).criarPedidoCompraDoOrcamento(orcamentoAprovado);
        verify(orcamentoMapper, times(1)).toResponseDto(any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aprovar um orçamento já APROVADO")
    void deveLancarExcecaoSeOrcamentoJaAprovado() {
        // Cenário: Orçamento já está APROVADO
        // Crie uma nova instância para este teste para não afetar outros
        Orcamento orcamentoAprovadoExistente = new Orcamento();
        orcamentoAprovadoExistente.setId(orcamentoAprovado.getId());
        orcamentoAprovadoExistente.setStatus(StatusOrcamento.APROVADO);
        orcamentoAprovadoExistente.setSolicitacaoCompra(solicitacaoCompra); // Garanta que a solicitação de compra esteja associada

        when(orcamentoRepository.findById(orcamentoAprovadoExistente.getId()))
                .thenReturn(Optional.of(orcamentoAprovadoExistente));

        // Ação e Verificação:
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.aprovarOrcamento(orcamentoAprovadoExistente.getId()));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("já está APROVADO"));
        verify(orcamentoRepository, times(1)).findById(orcamentoAprovadoExistente.getId());
        verify(orcamentoRepository, never()).save(any(Orcamento.class)); // Não deve salvar
        verify(solicitacaoCompraRepository, never()).findById(anyLong()); // Não deve chamar findById da solicitação
        verify(solicitacaoCompraRepository, never()).save(any(SolicitacaoCompra.class)); // Não deve salvar
        verify(pedidoCompraService, never()).criarPedidoCompraDoOrcamento(any(Orcamento.class)); // Não deve criar pedido
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aprovar um orçamento já REJEITADO")
    void deveLancarExcecaoSeOrcamentoJaRejeitado() {
        // Cenário: Orçamento já está REJEITADO
        // Crie uma nova instância para este teste para não afetar outros
        Orcamento orcamentoRejeitadoExistente = new Orcamento();
        orcamentoRejeitadoExistente.setId(orcamentoAprovado.getId()); // Use o mesmo ID para o teste
        orcamentoRejeitadoExistente.setStatus(StatusOrcamento.REJEITADO);
        orcamentoRejeitadoExistente.setSolicitacaoCompra(solicitacaoCompra); // Garanta que a solicitação de compra esteja associada

        when(orcamentoRepository.findById(orcamentoRejeitadoExistente.getId()))
                .thenReturn(Optional.of(orcamentoRejeitadoExistente));

        // Ação e Verificação:
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.aprovarOrcamento(orcamentoRejeitadoExistente.getId()));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("já foi REJEITADO"));
        verify(orcamentoRepository, times(1)).findById(orcamentoRejeitadoExistente.getId());
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
        verify(solicitacaoCompraRepository, never()).findById(anyLong()); // Não deve chamar findById da solicitação
        verify(solicitacaoCompraRepository, never()).save(any(SolicitacaoCompra.class));
        verify(pedidoCompraService, never()).criarPedidoCompraDoOrcamento(any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aprovar um orçamento com Solicitação de Compra CONCLUIDA")
    void deveLancarExcecaoSeSolicitacaoCompraConcluida() {
        // Cenário: Solicitação de Compra já CONCLUIDA
        SolicitacaoCompra solicitacaoConcluida = new SolicitacaoCompra();
        solicitacaoConcluida.setId(solicitacaoCompra.getId());
        solicitacaoConcluida.setStatus(StatusSolicitacaoCompra.CONCLUIDA);

        Orcamento orcamentoParaAprovar = new Orcamento();
        orcamentoParaAprovar.setId(orcamentoAprovado.getId());
        orcamentoParaAprovar.setSolicitacaoCompra(solicitacaoConcluida); // Associe a solicitação concluída

        when(orcamentoRepository.findById(orcamentoParaAprovar.getId()))
                .thenReturn(Optional.of(orcamentoParaAprovar));
        
        // **IMPORTANTE**: Mock para solicitacaoCompraRepository.findById()
        // Retorna a solicitação de compra com status CONCLUIDA
        when(solicitacaoCompraRepository.findById(solicitacaoConcluida.getId()))
                .thenReturn(Optional.of(solicitacaoConcluida));

        // Ação e Verificação:
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.aprovarOrcamento(orcamentoParaAprovar.getId()));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason()
                .contains("Solicitação de Compra associada está no status 'Concluída'"));
        verify(orcamentoRepository, times(1)).findById(orcamentoParaAprovar.getId());
        verify(solicitacaoCompraRepository, times(1)).findById(solicitacaoConcluida.getId()); // Verifica a chamada
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
        verify(solicitacaoCompraRepository, never()).save(any(SolicitacaoCompra.class));
        verify(pedidoCompraService, never()).criarPedidoCompraDoOrcamento(any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve aprovar orçamento quando não há outros orçamentos para a solicitação")
    void deveAprovarOrcamentoSemOutrosOrcamentos() {
        // Cenário: Apenas um orçamento para a solicitação
        when(orcamentoRepository.findById(orcamentoAguardandoAprovacao.getId()))
                .thenReturn(Optional.of(orcamentoAguardandoAprovacao));
        
        // **IMPORTANTE**: Mock para solicitacaoCompraRepository.findById()
        when(solicitacaoCompraRepository.findById(solicitacaoCompra.getId()))
                .thenReturn(Optional.of(solicitacaoCompra));

        // Nenhum outro orçamento para rejeitar
        when(orcamentoRepository.findBySolicitacaoCompraIdAndIdNot(solicitacaoCompra.getId(),
                orcamentoAguardandoAprovacao.getId()))
                .thenReturn(Collections.emptyList());

        when(orcamentoRepository.save(any(Orcamento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Ajuste no mock: criarPedidoCompraDoOrcamento agora retorna um PedidoCompra
        when(pedidoCompraService.criarPedidoCompraDoOrcamento(any(Orcamento.class)))
                .thenReturn(new PedidoCompra()); // Retorna uma instância mock de PedidoCompra

        // Mock para o mapeamento de resposta
        when(orcamentoMapper.toResponseDto(any(Orcamento.class))).thenReturn(new OrcamentoResponseDTO(
            orcamentoAguardandoAprovacao.getId(),
            orcamentoAguardandoAprovacao.getSolicitacaoCompra().getId(),
            orcamentoAguardandoAprovacao.getFornecedor().getId(),
            orcamentoAguardandoAprovacao.getDataCotacao(),
            orcamentoAguardandoAprovacao.getValorTotal(),
            orcamentoAguardandoAprovacao.getNumeroOrcamento(),
            orcamentoAguardandoAprovacao.getPrazoEntrega(),
            orcamentoAguardandoAprovacao.getCondicoesPagamento(),
            orcamentoAguardandoAprovacao.getObservacoes(),
            StatusOrcamento.APROVADO.name(), // O status esperado na resposta é APROVADO
            null
        ));

        // Ação:
        OrcamentoResponseDTO response = orcamentoService.aprovarOrcamento(orcamentoAguardandoAprovacao.getId());

        // Verificações:
        assertNotNull(response);
        assertEquals(StatusOrcamento.APROVADO.name(), response.getStatus());
        assertEquals(StatusOrcamento.APROVADO, orcamentoAguardandoAprovacao.getStatus(),
                "O orçamento aprovado deve ter o status APROVADO.");
        assertEquals(StatusSolicitacaoCompra.ORCAMENTO_APROVADO, solicitacaoCompra.getStatus(),
                "O status da Solicitação de Compra deve ser ORCAMENTO_APROVADO.");

        verify(orcamentoRepository, times(1)).findById(orcamentoAguardandoAprovacao.getId());
        verify(solicitacaoCompraRepository, times(1)).findById(solicitacaoCompra.getId()); // Verifica a chamada
        verify(orcamentoRepository, times(1)).findBySolicitacaoCompraIdAndIdNot(solicitacaoCompra.getId(),
                orcamentoAguardandoAprovacao.getId());
        verify(orcamentoRepository, times(1)).save(orcamentoAguardandoAprovacao); // Apenas um save
        verify(solicitacaoCompraRepository, times(1)).save(solicitacaoCompra);
        verify(pedidoCompraService, times(1)).criarPedidoCompraDoOrcamento(orcamentoAguardandoAprovacao);
        verify(orcamentoMapper, times(1)).toResponseDto(any(Orcamento.class));
    }
}
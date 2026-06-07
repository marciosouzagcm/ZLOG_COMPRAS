package com.zlogcompras.serviceTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.mapper.OrcamentoMapper;
import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.ItemOrcamento;
import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.PedidoCompra; // Importação adicionada para PedidoCompra
import com.zlogcompras.model.Produto;
import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.model.StatusOrcamento;
import com.zlogcompras.model.StatusSolicitacaoCompra;
import com.zlogcompras.model.dto.ItemOrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoRequestDTO;
import com.zlogcompras.model.dto.OrcamentoResponseDTO;
import com.zlogcompras.repository.FornecedorRepository;
import com.zlogcompras.repository.ItemOrcamentoRepository;
import com.zlogcompras.repository.OrcamentoRepository;
import com.zlogcompras.repository.ProdutoRepository;
import com.zlogcompras.repository.SolicitacaoCompraRepository;
import com.zlogcompras.service.OrcamentoService;
import com.zlogcompras.service.PedidoCompraService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    private ItemOrcamentoRepository itemOrcamentoRepository;
    @Mock
    private OrcamentoMapper orcamentoMapper;
    @Mock
    private PedidoCompraService pedidoCompraService;

    @InjectMocks
    private OrcamentoService orcamentoService;

    private SolicitacaoCompra solicitacaoCompra;
    private Fornecedor fornecedor;
    private Produto produto1;
    private Produto produto2;
    private Orcamento orcamento1;
    private Orcamento orcamento2;
    private ItemOrcamento itemOrcamento1;
    private ItemOrcamento itemOrcamento2;

    @BeforeEach
    void setUp() {
        solicitacaoCompra = new SolicitacaoCompra();
        solicitacaoCompra.setId(1L);
        solicitacaoCompra.setStatus(StatusSolicitacaoCompra.ABERTA);

        fornecedor = new Fornecedor();
        fornecedor.setId(10L);
        fornecedor.setRazaoSocial("Fornecedor Teste");

        produto1 = new Produto();
        produto1.setId(100L);
        produto1.setNome("Produto A");
        produto1.setPrecoUnitario(new BigDecimal("50.00"));

        produto2 = new Produto();
        produto2.setId(101L);
        produto2.setNome("Produto B");
        produto2.setPrecoUnitario(new BigDecimal("30.00"));

        // Orcamento 1 - A ser aprovado
        orcamento1 = new Orcamento();
        orcamento1.setId(1L);
        orcamento1.setSolicitacaoCompra(solicitacaoCompra);
        orcamento1.setFornecedor(fornecedor);
        orcamento1.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);
        itemOrcamento1 = new ItemOrcamento();
        itemOrcamento1.setId(1L);
        itemOrcamento1.setOrcamento(orcamento1);
        itemOrcamento1.setProduto(produto1);
        itemOrcamento1.setQuantidade(new BigDecimal("2.00"));
        itemOrcamento1.setPrecoUnitarioCotado(new BigDecimal("45.00"));
        orcamento1.setItensOrcamento(Arrays.asList(itemOrcamento1));
        orcamento1.setValorTotal(new BigDecimal("90.00"));

        // Orcamento 2 - A ser rejeitado
        orcamento2 = new Orcamento();
        orcamento2.setId(2L);
        orcamento2.setSolicitacaoCompra(solicitacaoCompra);
        orcamento2.setFornecedor(fornecedor);
        orcamento2.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);
        itemOrcamento2 = new ItemOrcamento();
        itemOrcamento2.setId(2L);
        itemOrcamento2.setOrcamento(orcamento2);
        itemOrcamento2.setProduto(produto1);
        itemOrcamento2.setQuantidade(new BigDecimal("2.00"));
        itemOrcamento2.setPrecoUnitarioCotado(new BigDecimal("55.00"));
        orcamento2.setItensOrcamento(Arrays.asList(itemOrcamento2));
        orcamento2.setValorTotal(new BigDecimal("110.00"));

        // Configuração genérica para o mapper de DTO para Entity
        when(orcamentoMapper.toEntity(any(OrcamentoRequestDTO.class))).thenAnswer(invocation -> {
            OrcamentoRequestDTO dto = invocation.getArgument(0);
            Orcamento o = new Orcamento();
            // Lógica mínima para que o orcamentoMapper.toEntity retorne um objeto válido
            // para os testes
            // Pode ser necessário popular mais campos aqui se seu mapper for mais complexo
            o.setSolicitacaoCompra(solicitacaoCompra);
            o.setFornecedor(fornecedor);
            o.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);
            o.setItensOrcamento(Collections.emptyList()); // Inicializa a lista para evitar NullPointer
            return o;
        });

        // Configuração genérica para o mapper de Entity para ResponseDTO
        when(orcamentoMapper.toResponseDto(any(Orcamento.class))).thenAnswer(invocation -> {
            Orcamento o = invocation.getArgument(0);
            OrcamentoResponseDTO dto = new OrcamentoResponseDTO();
            dto.setId(o.getId());
            dto.setSolicitacaoCompraId(o.getSolicitacaoCompra() != null ? o.getSolicitacaoCompra().getId() : null);
            dto.setFornecedorId(o.getFornecedor() != null ? o.getFornecedor().getId() : null);
            dto.setStatus(o.getStatus().name());
            dto.setValorTotal(o.getValorTotal());
            // Se houver itens no ResponseDTO, eles também precisariam ser mapeados aqui.
            // Para simplificar e evitar o "UnnecessaryStubbing" se não for usado, deixamos
            // mínimo.
            return dto;
        });

        // Configuração genérica para o mapper de atualização
        doAnswer(invocation -> {
            OrcamentoRequestDTO dto = invocation.getArgument(0);
            Orcamento entity = invocation.getArgument(1);
            entity.setObservacoes(dto.getObservacoes());
            // Atualizar itens se necessário.
            return null;
        }).when(orcamentoMapper).updateEntityFromDto(any(OrcamentoRequestDTO.class), any(Orcamento.class));

    }

    // --- Testes para criarOrcamento ---
    @Test
    @DisplayName("Deve criar um orçamento com sucesso")
    void deveCriarOrcamentoComSucesso() {
        // Given
        ItemOrcamentoRequestDTO itemDTO = new ItemOrcamentoRequestDTO();
        itemDTO.setProdutoId(produto1.getId());
        itemDTO.setQuantidade(new BigDecimal("2.00"));
        itemDTO.setPrecoUnitarioCotado(new BigDecimal("45.00"));

        OrcamentoRequestDTO requestDTO = new OrcamentoRequestDTO();
        requestDTO.setSolicitacaoCompraId(solicitacaoCompra.getId());
        requestDTO.setFornecedorId(fornecedor.getId());
        requestDTO.setItensOrcamento(Arrays.asList(itemDTO));
        requestDTO.setObservacoes("Observação do orçamento");

        when(solicitacaoCompraRepository.findById(solicitacaoCompra.getId()))
                .thenReturn(Optional.of(solicitacaoCompra));
        when(fornecedorRepository.findById(fornecedor.getId())).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.findById(itemDTO.getProdutoId())).thenReturn(Optional.of(produto1)); // Use itemDTO.getProdutoId()

        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(invocation -> {
            Orcamento orc = invocation.getArgument(0);
            orc.setId(1L);
            // Simula a adição dos itens ao orçamento
            ItemOrcamento newItem = new ItemOrcamento();
            newItem.setProduto(produto1);
            newItem.setQuantidade(itemDTO.getQuantidade());
            newItem.setPrecoUnitarioCotado(itemDTO.getPrecoUnitarioCotado());
            newItem.setOrcamento(orc);
            orc.setItensOrcamento(Arrays.asList(newItem));
            orc.setValorTotal(itemDTO.getQuantidade().multiply(itemDTO.getPrecoUnitarioCotado()));

            return orc;
        });

        // When
        OrcamentoResponseDTO responseDTO = orcamentoService.criarOrcamento(requestDTO);

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getId()).isEqualTo(1L);
        assertThat(responseDTO.getStatus()).isEqualTo(StatusOrcamento.AGUARDANDO_APROVACAO.name());
        // Comparar BigDecimal ignorando a escala
        assertThat(responseDTO.getValorTotal().compareTo(new BigDecimal("90.00"))).isEqualTo(0);

        verify(solicitacaoCompraRepository, times(1)).findById(solicitacaoCompra.getId());
        verify(fornecedorRepository, times(1)).findById(fornecedor.getId());
        verify(produtoRepository, times(1)).findById(produto1.getId());
        verify(orcamentoMapper, times(1)).toEntity(requestDTO);
        verify(orcamentoRepository, times(1)).save(any(Orcamento.class));
        verify(orcamentoMapper, times(1)).toResponseDto(any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar orçamento sem itens")
    void deveLancarExcecaoAoCriarOrcamentoSemItens() {
        // Given
        OrcamentoRequestDTO requestDTO = new OrcamentoRequestDTO();
        requestDTO.setSolicitacaoCompraId(solicitacaoCompra.getId());
        requestDTO.setFornecedorId(fornecedor.getId());
        requestDTO.setItensOrcamento(Collections.emptyList());

        when(solicitacaoCompraRepository.findById(solicitacaoCompra.getId()))
                .thenReturn(Optional.of(solicitacaoCompra));
        when(fornecedorRepository.findById(fornecedor.getId())).thenReturn(Optional.of(fornecedor));

        // When / Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.criarOrcamento(requestDTO));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getReason()).isEqualTo("O orçamento deve conter itens.");

        verify(solicitacaoCompraRepository, times(1)).findById(solicitacaoCompra.getId());
        verify(fornecedorRepository, times(1)).findById(fornecedor.getId());
        verify(produtoRepository, never()).findById(anyLong());
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    // --- Testes para atualizarOrcamento ---
    @Test
    @DisplayName("Deve atualizar um orçamento com sucesso")
    void deveAtualizarOrcamentoComSucesso() {
        // Given
        Long orcamentoId = orcamento1.getId();
        ItemOrcamentoRequestDTO itemDTOAtualizado = new ItemOrcamentoRequestDTO();
        itemDTOAtualizado.setId(itemOrcamento1.getId());
        itemDTOAtualizado.setProdutoId(produto1.getId());
        itemDTOAtualizado.setQuantidade(new BigDecimal("3.00"));
        itemDTOAtualizado.setPrecoUnitarioCotado(new BigDecimal("50.00"));
        itemDTOAtualizado.setObservacoes("Observação atualizada");

        OrcamentoRequestDTO requestDTO = new OrcamentoRequestDTO();
        requestDTO.setItensOrcamento(Arrays.asList(itemDTOAtualizado));
        requestDTO.setObservacoes("Nova observação geral do orçamento");

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento1));
        when(produtoRepository.findById(produto1.getId())).thenReturn(Optional.of(produto1));
        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        OrcamentoResponseDTO responseDTO = orcamentoService.atualizarOrcamento(orcamentoId, requestDTO);

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getId()).isEqualTo(orcamentoId);
        // Comparar BigDecimal ignorando a escala
        assertThat(orcamento1.getValorTotal().compareTo(new BigDecimal("150.00"))).isEqualTo(0);
        assertThat(orcamento1.getItensOrcamento()).hasSize(1);
        assertThat(orcamento1.getItensOrcamento().get(0).getQuantidade()).isEqualTo(new BigDecimal("3.00"));
        assertThat(orcamento1.getItensOrcamento().get(0).getPrecoUnitarioCotado()).isEqualTo(new BigDecimal("50.00"));
        assertThat(orcamento1.getObservacoes()).isEqualTo("Nova observação geral do orçamento");

        verify(orcamentoRepository, times(1)).findById(orcamentoId);
        verify(orcamentoMapper, times(1)).updateEntityFromDto(requestDTO, orcamento1);
        verify(produtoRepository, times(1)).findById(produto1.getId());
        verify(orcamentoRepository, times(1)).save(orcamento1);
        verify(orcamentoMapper, times(1)).toResponseDto(orcamento1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar orçamento não encontrado")
    void deveLancarExcecaoAoAtualizarOrcamentoNaoEncontrado() {
        // Given
        Long id = 99L;
        OrcamentoRequestDTO requestDTO = new OrcamentoRequestDTO();
        when(orcamentoRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.atualizarOrcamento(id, requestDTO));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo("Orçamento não encontrado com ID: " + id);
        verify(orcamentoRepository, times(1)).findById(id);
        verify(orcamentoMapper, never()).updateEntityFromDto(any(), any());
        verify(orcamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar orçamento sem itens")
    void deveLancarExcecaoAoAtualizarOrcamentoSemItens() {
        // Given
        Long orcamentoId = orcamento1.getId();
        OrcamentoRequestDTO requestDTO = new OrcamentoRequestDTO();
        requestDTO.setItensOrcamento(Collections.emptyList());

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento1));

        // When / Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.atualizarOrcamento(orcamentoId, requestDTO));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getReason()).isEqualTo("O orçamento deve conter itens ao atualizar.");

        verify(orcamentoRepository, times(1)).findById(orcamentoId);
        verify(orcamentoMapper, times(1)).updateEntityFromDto(requestDTO, orcamento1);
        verify(produtoRepository, never()).findById(anyLong());
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    // --- Teste corrigido para aprovação de orçamento ---
    @Test
    @DisplayName("Deve aprovar um orçamento com sucesso e recusar os outros da mesma solicitação")
    void deveAprovarOrcamentoComSucessoERecusarOutros() {
        // Given
        Long orcamentoAprovadoId = orcamento1.getId();

        when(orcamentoRepository.findById(orcamentoAprovadoId)).thenReturn(Optional.of(orcamento1));
        when(orcamentoRepository.findBySolicitacaoCompraIdAndIdNot(
                        solicitacaoCompra.getId(), orcamentoAprovadoId))
                .thenReturn(Arrays.asList(orcamento2));

        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(invocation -> {
            Orcamento savedOrcamento = invocation.getArgument(0);
            if (savedOrcamento.getId().equals(orcamento1.getId())) {
                orcamento1.setStatus(savedOrcamento.getStatus());
            } else if (savedOrcamento.getId().equals(orcamento2.getId())) {
                orcamento2.setStatus(savedOrcamento.getStatus());
            }
            return savedOrcamento;
        });

        when(solicitacaoCompraRepository.save(any(SolicitacaoCompra.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // CORREÇÃO: Usar when().thenReturn() para método não-void
        // Se criarPedidoCompraDoOrcamento retorna um PedidoCompra, simule o retorno.
        // Se você não precisa de um objeto PedidoCompra real para o teste, pode retornar null
        // ou um mock simples.
        when(pedidoCompraService.criarPedidoCompraDoOrcamento(any(Orcamento.class))).thenReturn(new PedidoCompra()); // ou null, dependendo do que for esperado

        // When
        orcamentoService.aprovarOrcamento(orcamentoAprovadoId);

        // Then
        assertThat(orcamento1.getStatus()).isEqualTo(StatusOrcamento.APROVADO);
        assertThat(orcamento2.getStatus()).isEqualTo(StatusOrcamento.REJEITADO);
        assertThat(solicitacaoCompra.getStatus()).isEqualTo(StatusSolicitacaoCompra.ORCAMENTO_APROVADO);

        verify(orcamentoRepository, times(1)).findById(orcamentoAprovadoId);
        verify(orcamentoRepository, times(1)).findBySolicitacaoCompraIdAndIdNot(solicitacaoCompra.getId(),
                orcamentoAprovadoId);
        verify(orcamentoRepository, times(2)).save(any(Orcamento.class));
        verify(solicitacaoCompraRepository, times(1)).save(solicitacaoCompra);
        verify(pedidoCompraService, times(1)).criarPedidoCompraDoOrcamento(orcamento1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aprovar orçamento não encontrado")
    void deveLancarExcecaoAoAprovarOrcamentoNaoEncontrado() {
        // Given
        Long id = 99L;
        when(orcamentoRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.aprovarOrcamento(id));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo("Orçamento não encontrado com ID: " + id);
        verify(orcamentoRepository, times(1)).findById(id);
        verify(orcamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aprovar orçamento com status inválido")
    void deveLancarExcecaoAoAprovarOrcamentoComStatusInvalido() {
        // Given
        Long id = orcamento1.getId();
        orcamento1.setStatus(StatusOrcamento.APROVADO);

        when(orcamentoRepository.findById(id)).thenReturn(Optional.of(orcamento1));

        // When / Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.aprovarOrcamento(id));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getReason()).contains("Orçamento não pode ser aprovado. Status atual: APROVADO");
        verify(orcamentoRepository, times(1)).findById(id);
        verify(orcamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aprovar orçamento sem solicitação de compra associada")
    void deveLancarExcecaoAoAprovarOrcamentoSemSolicitacaoCompra() {
        // Given
        Long id = orcamento1.getId();
        orcamento1.setSolicitacaoCompra(null);

        when(orcamentoRepository.findById(id)).thenReturn(Optional.of(orcamento1));
        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When / Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.aprovarOrcamento(id));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getReason()).isEqualTo("Orçamento não está associado a uma Solicitação de Compra.");
        verify(orcamentoRepository, times(1)).findById(id);
        verify(orcamentoRepository, times(1)).save(orcamento1);
        verify(solicitacaoCompraRepository, never()).save(any());
    }

    // --- Testes para buscarOrcamentoPorId ---
    @Test
    @DisplayName("Deve buscar um orçamento pelo ID com sucesso")
    void deveBuscarOrcamentoPorIdComSucesso() {
        // Given
        Long id = orcamento1.getId();
        when(orcamentoRepository.findById(id)).thenReturn(Optional.of(orcamento1));

        // When
        OrcamentoResponseDTO result = orcamentoService.buscarOrcamentoPorId(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getStatus()).isEqualTo(StatusOrcamento.AGUARDANDO_APROVACAO.name());
        verify(orcamentoRepository, times(1)).findById(id);
        verify(orcamentoMapper, times(1)).toResponseDto(orcamento1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar orçamento por ID não encontrado")
    void deveLancarExcecaoAoBuscarOrcamentoPorIdNaoEncontrado() {
        // Given
        Long id = 99L;
        when(orcamentoRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.buscarOrcamentoPorId(id));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo("Orçamento não encontrado com ID: " + id);
        verify(orcamentoRepository, times(1)).findById(id);
        verify(orcamentoMapper, never()).toResponseDto(any(Orcamento.class));
    }

    // --- Testes para listarTodosOrcamentos ---
    @Test
    @DisplayName("Deve listar todos os orçamentos com sucesso")
    void deveListarTodosOrcamentosComSucesso() {
        // Given
        List<Orcamento> orcamentos = Arrays.asList(orcamento1, orcamento2);
        when(orcamentoRepository.findAll()).thenReturn(orcamentos);

        // When
        List<OrcamentoResponseDTO> result = orcamentoService.listarTodosOrcamentos();

        // Then
        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(orcamento1.getId());
        assertThat(result.get(1).getId()).isEqualTo(orcamento2.getId());

        verify(orcamentoRepository, times(1)).findAll();
        verify(orcamentoMapper, times(2)).toResponseDto(any(Orcamento.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há orçamentos")
    void deveRetornarListaVaziaQuandoNaoHaOrcamentos() {
        // Given
        when(orcamentoRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<OrcamentoResponseDTO> result = orcamentoService.listarTodosOrcamentos();

        // Then
        assertThat(result).isNotNull().isEmpty();
        verify(orcamentoRepository, times(1)).findAll();
        verify(orcamentoMapper, never()).toResponseDto(any(Orcamento.class));
    }

    // --- Testes para deletarOrcamento ---
    @Test
    @DisplayName("Deve deletar um orçamento com sucesso")
    void deveDeletarOrcamentoComSucesso() {
        // Given
        Long id = 1L;
        when(orcamentoRepository.existsById(id)).thenReturn(true);
        doNothing().when(orcamentoRepository).deleteById(id);

        // When
        orcamentoService.deletarOrcamento(id);

        // Then
        verify(orcamentoRepository, times(1)).existsById(id);
        verify(orcamentoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar orçamento não encontrado")
    void deveLancarExcecaoAoDeletarOrcamentoNaoEncontrado() {
        // Given
        Long id = 99L;
        when(orcamentoRepository.existsById(id)).thenReturn(false);

        // When / Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.deletarOrcamento(id));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo("Orçamento não encontrado com ID: " + id);
        verify(orcamentoRepository, times(1)).existsById(id);
        verify(orcamentoRepository, never()).deleteById(anyLong());
    }

    // --- Testes para recusarOrcamento ---
    @Test
    @DisplayName("Deve recusar um orçamento com sucesso")
    void deveRecusarOrcamentoComSucesso() {
        // Given
        Long orcamentoId = orcamento1.getId();
        orcamento1.setStatus(StatusOrcamento.AGUARDANDO_APROVACAO);

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento1));
        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(invocation -> {
            Orcamento savedOrcamento = invocation.getArgument(0);
            orcamento1.setStatus(savedOrcamento.getStatus());
            return savedOrcamento;
        });

        // When
        orcamentoService.recusarOrcamento(orcamentoId);

        // Then
        assertThat(orcamento1.getStatus()).isEqualTo(StatusOrcamento.REJEITADO);
        verify(orcamentoRepository, times(1)).findById(orcamentoId);
        verify(orcamentoRepository, times(1)).save(orcamento1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar recusar orçamento em status inválido")
    void deveLancarExcecaoAoRecusarOrcamentoComStatusInvalido() {
        // Given
        Long orcamentoId = orcamento1.getId();
        orcamento1.setStatus(StatusOrcamento.APROVADO);

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento1));

        // When / Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.recusarOrcamento(orcamentoId));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getReason()).contains("Orçamento não pode ser recusado. Status atual: APROVADO");
        verify(orcamentoRepository, times(1)).findById(orcamentoId);
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    // --- Testes para concluirOrcamento ---
    @Test
    @DisplayName("Deve concluir um orçamento com sucesso")
    void deveConcluirOrcamentoComSucesso() {
        // Given
        Long orcamentoId = orcamento1.getId();
        orcamento1.setStatus(StatusOrcamento.APROVADO);

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento1));
        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(invocation -> {
            Orcamento savedOrcamento = invocation.getArgument(0);
            orcamento1.setStatus(savedOrcamento.getStatus());
            return savedOrcamento;
        });

        // When
        orcamentoService.concluirOrcamento(orcamentoId);

        // Then
        assertThat(orcamento1.getStatus()).isEqualTo(StatusOrcamento.CONCLUIDO);
        verify(orcamentoRepository, times(1)).findById(orcamentoId);
        verify(orcamentoRepository, times(1)).save(orcamento1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar concluir orçamento já concluído ou cancelado")
    void deveLancarExcecaoAoConcluirOrcamentoJaConcluidoOuCancelado() {
        // Given
        Long orcamentoId = orcamento1.getId();
        orcamento1.setStatus(StatusOrcamento.CONCLUIDO);

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento1));

        // When / Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.concluirOrcamento(orcamentoId));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getReason()).contains("Orçamento já está concluído ou cancelado.");
        verify(orcamentoRepository, times(1)).findById(orcamentoId);
        verify(orcamentoRepository, never()).save(any(Orcamento.class));

        // Testar com status CANCELADO também
        orcamento1.setStatus(StatusOrcamento.CANCELADO);
        exception = assertThrows(ResponseStatusException.class,
                () -> orcamentoService.concluirOrcamento(orcamentoId));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getReason()).contains("Orçamento já está concluído ou cancelado.");
        verify(orcamentoRepository, times(2)).findById(orcamentoId);
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }
}
package com.zlogcompras.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; // Para aprovar o orçamento
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zlogcompras.model.Orcamento;
import com.zlogcompras.model.dto.OrcamentoTabelaDTO;
import com.zlogcompras.service.OrcamentoService;
import com.zlogcompras.service.ProcessoCompraService;

@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    @Autowired
    private OrcamentoService orcamentoService;

    @Autowired
    private ProcessoCompraService processoCompraService;

    @GetMapping
    public ResponseEntity<List<OrcamentoTabelaDTO>> listarTodosOrcamentos() {
        List<OrcamentoTabelaDTO> orcamentosDTO = orcamentoService.listarTodosOrcamentos().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(orcamentosDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoTabelaDTO> buscarOrcamentoPorId(@PathVariable Long id) {
        Optional<Orcamento> orcamentoOptional = orcamentoService.buscarOrcamentoPorId(id);
        return orcamentoOptional.map(orcamento -> new ResponseEntity<>(convertToDto(orcamento), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<OrcamentoTabelaDTO> criarOrcamento(@RequestBody Orcamento orcamento) {
        Orcamento novoOrcamento = orcamentoService.salvarOrcamento(orcamento);
        return new ResponseEntity<>(convertToDto(novoOrcamento), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoTabelaDTO> atualizarOrcamento(@PathVariable Long id,
            @RequestBody Orcamento orcamentoAtualizado) {
        Optional<Orcamento> orcamentoExistente = orcamentoService.buscarOrcamentoPorId(id);
        if (orcamentoExistente.isPresent()) {
            orcamentoAtualizado.setId(id);
            Orcamento orcamentoSalvo = orcamentoService.salvarOrcamento(orcamentoAtualizado);
            return new ResponseEntity<>(convertToDto(orcamentoSalvo), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/solicitacao/{solicitacaoId}")
    public ResponseEntity<List<OrcamentoTabelaDTO>> listarOrcamentosPorSolicitacao(@PathVariable Long solicitacaoId) {
        List<Orcamento> orcamentos = orcamentoService.buscarOrcamentosPorSolicitacaoCompraId(solicitacaoId);
        List<OrcamentoTabelaDTO> orcamentosDTO = orcamentos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(orcamentosDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Void> aprovarOrcamentoGerarPedido(@PathVariable Long id) {
        processoCompraService.aprovarOrcamentoGerarPedido(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private OrcamentoTabelaDTO convertToDto(Orcamento orcamento) {
        BigDecimal precoTotal = orcamento.getItensOrcamento().stream()
                .map(item -> ((BigDecimal) item.getPrecoUnitario()).multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrcamentoTabelaDTO(
                orcamento.getId(),
                orcamento.getDataCotacao(),
                orcamento.getFornecedor().getNome(),
                precoTotal,
                orcamento.getObservacoes(), // Usando observações como um placeholder para prazo de entrega por enquanto
                orcamento.getCondicoesPagamento(),
                orcamento.getObservacoes(),
                orcamento.getItensOrcamento());
    }
}
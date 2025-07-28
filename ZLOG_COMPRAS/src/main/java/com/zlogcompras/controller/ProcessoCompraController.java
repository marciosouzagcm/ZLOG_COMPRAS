package com.zlogcompras.controller;

import com.zlogcompras.model.PedidoCompra; // Importar PedidoCompra
import com.zlogcompras.model.SolicitacaoCompra; // Importar SolicitacaoCompra (se for usada para retornar)
import com.zlogcompras.model.StatusSolicitacaoCompra; // Importar StatusSolicitacaoCompra
import com.zlogcompras.model.dto.SolicitacaoCompraResponseDTO;
import com.zlogcompras.service.ProcessoCompraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Adicionar para o método PUT
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/processo-compra") // Mantido o prefixo /api
public class ProcessoCompraController {

    @Autowired
    private ProcessoCompraService processoCompraService;

    /**
     * Inicia o processo de compra para uma solicitação específica.
     * Altera o status da solicitação para EM_ANDAMENTO e tenta gerar um orçamento inicial.
     *
     * @param solicitacaoId O ID da solicitação de compra.
     * @return ResponseEntity com status 200 OK se a operação for bem-sucedida.
     */
    @PostMapping("/iniciar/{solicitacaoId}")
    public ResponseEntity<Void> iniciarProcessoCompra(@PathVariable Long solicitacaoId) {
        processoCompraService.iniciarProcessoCompraPorSolicitacaoId(solicitacaoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Endpoint para atualizar o status de uma solicitação de compra.
     *
     * @param solicitacaoId O ID da solicitação a ser atualizada.
     * @param novoStatus O novo status a ser aplicado à solicitação.
     * @return ResponseEntity com a SolicitaçãoCompra atualizada e status 200 OK.
     */
    @PutMapping("/{solicitacaoId}/status/{novoStatus}") // Usando PUT para atualização de status
    public ResponseEntity<SolicitacaoCompraResponseDTO> atualizarStatusSolicitacao(
            @PathVariable Long solicitacaoId,
            @PathVariable StatusSolicitacaoCompra novoStatus) {
        SolicitacaoCompraResponseDTO solicitacaoAtualizada = processoCompraService.atualizarStatusSolicitacao(solicitacaoId, novoStatus);
        return ResponseEntity.ok(solicitacaoAtualizada);
    }

    /**
     * Gera um novo Pedido de Compra a partir de um Orçamento previamente aprovado.
     *
     * @param orcamentoId O ID do orçamento aprovado a partir do qual o pedido será gerado.
     * @return ResponseEntity contendo os dados do PedidoCompra recém-criado e status 201 Created.
     */
    @PostMapping("/gerar-pedido/{orcamentoId}")
    public ResponseEntity<PedidoCompra> gerarPedidoDeCompra(
            @PathVariable Long orcamentoId) {
        PedidoCompra novoPedido = processoCompraService.gerarPedidoDeCompra(orcamentoId);
        // Retorna 201 Created, que é o status HTTP apropriado para a criação de um novo recurso.
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }
}
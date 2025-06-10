package com.zlogcompras.controller;

import com.zlogcompras.service.ProcessoCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/processo-compra")
public class ProcessoCompraController {

    @Autowired
    private ProcessoCompraService processoCompraService;

    @PostMapping("/iniciar/{solicitacaoId}")
    public ResponseEntity<Void> iniciarProcessoCompra(@PathVariable Long solicitacaoId) {
        // Delega a lógica de negócio para o serviço
        processoCompraService.iniciarProcessoCompraPorSolicitacaoId(solicitacaoId);

        // Retorna 200 OK para indicar que a requisição foi processada com sucesso
        // e o processo de compra foi iniciado (ou a tentativa foi feita).
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
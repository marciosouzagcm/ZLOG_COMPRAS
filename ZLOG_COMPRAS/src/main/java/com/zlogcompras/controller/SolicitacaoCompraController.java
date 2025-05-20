package com.zlogcompras.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zlogcompras.model.SolicitacaoCompra;
import com.zlogcompras.service.SolicitacaoCompraService;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoCompraController {

    @Autowired
    private SolicitacaoCompraService solicitacaoCompraService;
    // Removido campo 'solicitacao' desnecessário

    @PostMapping
    public ResponseEntity<SolicitacaoCompra> criarSolicitacao(@RequestBody SolicitacaoCompra solicitacaoCompra) {
        System.out.println("Recebida requisição POST para /api/solicitacoes.");
        System.out.println("Payload recebido (POST): " + solicitacaoCompra);
        if (solicitacaoCompra.getItens() != null) {
            solicitacaoCompra.getItens().forEach(item -> System.out.println("   Item: ID=" + item.getId() + ", Material=" + item.getMaterialServico() + ", Qtd=" + item.getQuantidade()));
        }

        SolicitacaoCompra novaSolicitacao;
        novaSolicitacao = solicitacaoCompraService.criarSolicitacao(solicitacaoCompra);
        System.out.println("Solicitação criada com ID: " + novaSolicitacao.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novaSolicitacao);
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoCompra>> listarTodasSolicitacoes() {
        System.out.println("Recebida requisição GET para /api/solicitacoes (listar todas).");
        List<SolicitacaoCompra> solicitacoes = solicitacaoCompraService.listarTodas();
        System.out.println("Total de solicitações encontradas: " + solicitacoes.size());
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoCompra> buscarSolicitacaoPorId(@PathVariable Long id) {
        System.out.println("Recebida requisição GET para /api/solicitacoes/" + id);
        Optional<SolicitacaoCompra> solicitacao = solicitacaoCompraService.buscarSolicitacaoPorId(id);

        if (!solicitacao.isPresent()) {
            System.out.println("Solicitação com ID " + id + " NÃO encontrada.");
            return ResponseEntity.notFound().build();
        } else {
            System.out.println("Solicitação com ID " + id + " encontrada.");
            return ResponseEntity.ok(solicitacao.get());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoCompra> atualizarSolicitacao(@PathVariable Long id, @RequestBody SolicitacaoCompra solicitacaoCompra) {
        System.out.println("Recebida requisição PUT para /api/solicitacoes/" + id + ".");
        System.out.println("Payload recebido (PUT): " + solicitacaoCompra);
        if (solicitacaoCompra.getItens() != null) {
            solicitacaoCompra.getItens().forEach(item -> System.out.println("   Item: ID=" + item.getId() + ", Material=" + item.getMaterialServico() + ", Qtd=" + item.getQuantidade()));
        }

        // A validação de ID no path vs. ID no corpo da requisição deve ser feita
        // APENAS se o ID no corpo for realmente um campo que o cliente pode/deve enviar
        // para identificar o recurso. Na prática, para um PUT em /recursos/{id}, o ID
        // do path é o que define o recurso a ser atualizado. O ID no corpo é ignorado ou
        // usado para garantir que o recurso que está sendo enviado é o correto (se for preenchido).
        // Se você quer que o ID no corpo seja sempre o mesmo que no path, essa validação
        // seria útil, mas para a maioria dos casos de uso RESTful, basta usar o ID do path.
        // A linha abaixo foi removida para simplificar e focar na funcionalidade.
        // if (solicitacaoCompra.getId() == null || !id.equals(solicitacaoCompra.getId())) {
        //     System.out.println("Erro: ID no path (" + id + ") não corresponde ao ID no corpo da requisição (" + solicitacaoCompra.getId() + ").");
        //     return ResponseEntity.badRequest().body(null);
        // }

        // Chama o método do serviço, passando o ID do path e o objeto atualizado
        SolicitacaoCompra solicitacaoAtualizada = solicitacaoCompraService.atualizarSolicitacao(id, solicitacaoCompra);

        if (solicitacaoAtualizada != null) {
            System.out.println("Solicitação com ID " + id + " atualizada com sucesso.");
            return ResponseEntity.ok(solicitacaoAtualizada);
        } else {
            System.out.println("Erro: Solicitação com ID " + id + " não encontrada para atualização.");
            // O serviço agora lança RuntimeException se não encontrar, então um catch seria melhor aqui
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSolicitacao(@PathVariable Long id) {
        System.out.println("Recebida requisição DELETE para /api/solicitacoes/" + id);
        boolean deletado = solicitacaoCompraService.deletarSolicitacao(id);
        if (deletado) {
            System.out.println("Solicitação com ID " + id + " deletada com sucesso.");
            return ResponseEntity.noContent().build();
        } else {
            System.out.println("Erro: Solicitação com ID " + id + " não encontrada para deleção.");
            return ResponseEntity.notFound().build();
        }
    }
}
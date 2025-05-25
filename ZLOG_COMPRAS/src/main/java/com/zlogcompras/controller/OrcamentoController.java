package com.zlogcompras.controller; // Ajuste o pacote conforme a sua estrutura

import com.zlogcompras.model.dto.OrcamentoResponseDTO;
import com.zlogcompras.service.OrcamentoService;
import com.zlogcompras.mapper.OrcamentoMapper; // Assumindo que você tem um mapper

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/orcamentos") // Adicione um path base para o controller
public class OrcamentoController {

    private final OrcamentoService orcamentoService;
    private final OrcamentoMapper orcamentoMapper; // Injete o mapper

    // Construtor para injeção de dependências (recomendado pelo Spring)
    public OrcamentoController(OrcamentoService orcamentoService, OrcamentoMapper orcamentoMapper) {
        this.orcamentoService = orcamentoService;
        this.orcamentoMapper = orcamentoMapper;
    }

    /**
     * Busca um orçamento pelo seu ID.
     * Retorna um OrcamentoResponseDTO se encontrado, ou 404 Not Found caso contrário.
     *
     * @param id O ID do orçamento a ser buscado.
     * @return ResponseEntity contendo OrcamentoResponseDTO e status OK, ou lança ResponseStatusException para 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> buscarOrcamentoPorId(@PathVariable Long id) {
        // O serviço retorna um Optional<Orcamento>.
        // Se presente, mapeamos para OrcamentoResponseDTO usando o mapper.
        // Se não presente, lançamos uma exceção que o Spring converte para HTTP 404.
        OrcamentoResponseDTO orcamentoResponseDTO = orcamentoService.buscarOrcamentoPorId(id)
                .map(orcamentoMapper::toResponseDto) // Converte a entidade para o DTO de resposta
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orçamento não encontrado com ID: " + id));

        // Envolve o DTO de resposta em um ResponseEntity com status HTTP 200 (OK).
        return ResponseEntity.ok(orcamentoResponseDTO);
    }

    // Você pode adicionar outros métodos CRUD aqui, como:
    // @PostMapping
    // @PutMapping("/{id}")
    // @DeleteMapping("/{id}")
    // @GetMapping
}
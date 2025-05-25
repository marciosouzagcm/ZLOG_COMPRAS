package com.zlogcompras.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.zlogcompras.mapper.FornecedorMapper;
import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.model.dto.FornecedorRequestDTO;
import com.zlogcompras.model.dto.FornecedorResponseDTO;
import com.zlogcompras.repository.FornecedorRepository;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final FornecedorMapper fornecedorMapper;

    @Autowired
    public FornecedorService(FornecedorRepository fornecedorRepository, FornecedorMapper fornecedorMapper) {
        this.fornecedorRepository = fornecedorRepository;
        this.fornecedorMapper = fornecedorMapper;
    }

    @Transactional
    public FornecedorResponseDTO criarFornecedor(FornecedorRequestDTO fornecedorRequestDTO) {
        Fornecedor fornecedor = fornecedorMapper.toEntity(fornecedorRequestDTO);

        if (fornecedorRepository.findByCnpj(fornecedor.getCnpj()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um fornecedor cadastrado com este CNPJ.");
        }

        System.out.println("Serviço: Criando fornecedor: " + fornecedor.getNome());

        Fornecedor novoFornecedor = fornecedorRepository.save(fornecedor);

        return fornecedorMapper.toResponseDto(novoFornecedor);
    }

    public List<FornecedorResponseDTO> listarTodosFornecedores() {
        System.out.println("Serviço: Listando todos os fornecedores.");
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();
        return fornecedorMapper.toListaDtoList(fornecedores);
    }

    // AJUSTADO: Agora retorna Optional<Fornecedor>
    public Optional<Fornecedor> buscarFornecedorPorId(Long id) {
        System.out.println("Serviço: Buscando fornecedor por ID: " + id);
        return fornecedorRepository.findById(id); // Retorna Optional<Fornecedor>
    }

    @Transactional
    public FornecedorResponseDTO atualizarFornecedor(Long id, FornecedorRequestDTO fornecedorRequestDTO) {
        System.out.println("Serviço: Tentando atualizar fornecedor com ID: " + id);

        Fornecedor fornecedorExistente = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado com ID: " + id));

        if (!fornecedorExistente.getCnpj().equals(fornecedorRequestDTO.getCnpj())) {
            Optional<Fornecedor> fornecedorComMesmoCnpj = fornecedorRepository.findByCnpj(fornecedorRequestDTO.getCnpj());
            if (fornecedorComMesmoCnpj.isPresent() && !fornecedorComMesmoCnpj.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outro fornecedor com o CNPJ informado.");
            }
        }

        fornecedorMapper.updateEntityFromDto(fornecedorRequestDTO, fornecedorExistente);

        System.out.println("Serviço: Fornecedor com ID " + id + " atualizado.");

        Fornecedor fornecedorAtualizado = fornecedorRepository.save(fornecedorExistente);
        return fornecedorMapper.toResponseDto(fornecedorAtualizado);
    }

    @Transactional
    public void deletarFornecedor(Long id) {
        System.out.println("Serviço: Tentando deletar fornecedor com ID: " + id);
        if (!fornecedorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado com ID: " + id);
        }
        fornecedorRepository.deleteById(id);
        System.out.println("Serviço: Fornecedor com ID " + id + " deletado.");
    }
}
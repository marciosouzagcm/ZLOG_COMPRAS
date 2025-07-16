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

@Service // Indica que esta é uma classe de serviço Spring
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final FornecedorMapper fornecedorMapper;

    @Autowired // Injeta as dependências FornecedorRepository e FornecedorMapper
    public FornecedorService(FornecedorRepository fornecedorRepository, FornecedorMapper fornecedorMapper) {
        this.fornecedorRepository = fornecedorRepository;
        this.fornecedorMapper = fornecedorMapper;
    }

    @Transactional // Garante que a operação seja transacional
    public FornecedorResponseDTO criarFornecedor(FornecedorRequestDTO fornecedorRequestDTO) {
        Fornecedor fornecedor = fornecedorMapper.toEntity(fornecedorRequestDTO);

        // Verifica se já existe um fornecedor com o mesmo CNPJ
        if (fornecedorRepository.findByCnpj(fornecedor.getCnpj()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um fornecedor cadastrado com este CNPJ.");
        }

        // AJUSTE AQUI: Use getRazaoSocial() em vez de getNome()
        System.out.println("Serviço: Criando fornecedor: " + fornecedor.getRazaoSocial());

        Fornecedor novoFornecedor = fornecedorRepository.save(fornecedor); // Salva o novo fornecedor

        return fornecedorMapper.toResponseDto(novoFornecedor); // Converte e retorna o DTO de resposta
    }

    public List<FornecedorResponseDTO> listarTodosFornecedores() {
        System.out.println("Serviço: Listando todos os fornecedores.");
        List<Fornecedor> fornecedores = fornecedorRepository.findAll(); // Busca todos os fornecedores
        return fornecedorMapper.toListaDtoList(fornecedores); // Converte a lista de entidades para DTOs
    }

    // AJUSTADO: Agora retorna Optional<Fornecedor> para indicar que pode não
    // encontrar
    public Optional<Fornecedor> buscarFornecedorPorId(Long id) {
        System.out.println("Serviço: Buscando fornecedor por ID: " + id);
        return fornecedorRepository.findById(id); // Retorna Optional<Fornecedor>
    }

    @Transactional // Garante que a operação seja transacional
    public FornecedorResponseDTO atualizarFornecedor(Long id, FornecedorRequestDTO fornecedorRequestDTO) {
        System.out.println("Serviço: Tentando atualizar fornecedor com ID: " + id);

        // Busca o fornecedor existente ou lança exceção se não encontrado
        Fornecedor fornecedorExistente = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Fornecedor não encontrado com ID: " + id));

        // Verifica se o CNPJ foi alterado e se o novo CNPJ já existe para outro
        // fornecedor
        if (!fornecedorExistente.getCnpj().equals(fornecedorRequestDTO.getCnpj())) {
            Optional<Fornecedor> fornecedorComMesmoCnpj = fornecedorRepository
                    .findByCnpj(fornecedorRequestDTO.getCnpj());
            if (fornecedorComMesmoCnpj.isPresent() && !fornecedorComMesmoCnpj.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Já existe outro fornecedor com o CNPJ informado.");
            }
        }

        // Atualiza a entidade existente com os dados do DTO
        fornecedorMapper.updateEntityFromDto(fornecedorRequestDTO, fornecedorExistente);

        System.out.println("Serviço: Fornecedor com ID " + id + " atualizado.");

        Fornecedor fornecedorAtualizado = fornecedorRepository.save(fornecedorExistente); // Salva as alterações
        return fornecedorMapper.toResponseDto(fornecedorAtualizado); // Converte e retorna o DTO de resposta
    }

    @Transactional // Garante que a operação seja transacional
    public void deletarFornecedor(Long id) {
        System.out.println("Serviço: Tentando deletar fornecedor com ID: " + id);
        // Verifica se o fornecedor existe antes de tentar deletar
        if (!fornecedorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado com ID: " + id);
        }
        fornecedorRepository.deleteById(id); // Deleta o fornecedor
        System.out.println("Serviço: Fornecedor com ID " + id + " deletado.");
    }
}
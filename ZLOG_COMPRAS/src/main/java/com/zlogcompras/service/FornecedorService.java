package com.zlogcompras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlogcompras.model.Fornecedor;
import com.zlogcompras.repository.FornecedorRepository;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    @Autowired // Injeção de dependência via construtor (preferível)
    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional // Garante que a operação seja uma transação de banco de dados
    public Fornecedor criarFornecedor(Fornecedor fornecedor) {
        // Você pode adicionar lógicas de negócio aqui, como validações
        // Ex: Validar CNPJ, verificar se já existe um fornecedor com o mesmo CNPJ
        System.out.println("Serviço: Criando fornecedor: " + fornecedor.getNome());
        return fornecedorRepository.save(fornecedor);
    }

    public List<Fornecedor> listarTodosFornecedores() {
        System.out.println("Serviço: Listando todos os fornecedores.");
        return fornecedorRepository.findAll();
    }

    public Optional<Fornecedor> buscarFornecedorPorId(Long id) { // ID deve ser Long, não long
        System.out.println("Serviço: Buscando fornecedor por ID: " + id);
        return fornecedorRepository.findById(id);
    }

    @Transactional
    public Fornecedor atualizarFornecedor(Long id, Fornecedor fornecedorAtualizado) {
        System.out.println("Serviço: Tentando atualizar fornecedor com ID: " + id);
        return fornecedorRepository.findById(id)
                .map(fornecedorExistente -> {
                    // Atualiza os campos do fornecedor existente com os dados do fornecedorAtualizado
                    fornecedorExistente.setNome(fornecedorAtualizado.getNome());
                    fornecedorExistente.setCnpj(fornecedorAtualizado.getCnpj());
                    fornecedorExistente.setContato(fornecedorAtualizado.getContato());
                    fornecedorExistente.setTelefone(fornecedorAtualizado.getTelefone());
                    fornecedorExistente.setEmail(fornecedorAtualizado.getEmail());
                    // O campo 'version' é gerenciado automaticamente pelo @Version do JPA

                    System.out.println("Serviço: Fornecedor com ID " + id + " atualizado.");
                    return fornecedorRepository.save(fornecedorExistente); // Salva as alterações
                })
                .orElseThrow(() -> {
                    System.out.println("Serviço: Fornecedor com ID " + id + " não encontrado para atualização.");
                    return new RuntimeException("Fornecedor não encontrado com ID: " + id);
                });
    }

    @Transactional
    public boolean deletarFornecedor(Long id) {
        System.out.println("Serviço: Tentando deletar fornecedor com ID: " + id);
        if (fornecedorRepository.existsById(id)) {
            fornecedorRepository.deleteById(id);
            System.out.println("Serviço: Fornecedor com ID " + id + " deletado.");
            return true;
        }
        System.out.println("Serviço: Fornecedor com ID " + id + " não encontrado para deleção.");
        return false;
    }
}
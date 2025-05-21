package com.zlogcompras.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Importe esta anotação

import com.zlogcompras.model.Fornecedor;

@Repository // É uma boa prática adicionar esta anotação para indicar que é um componente de persistência
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    // Métodos personalizados de consulta podem ser adicionados aqui, se necessário
    // Por exemplo, para buscar um fornecedor por CNPJ:
    // Optional<Fornecedor> findByCnpj(String cnpj);
    // Ou para buscar por nome (ignorando caixa e parcial):
    // List<Fornecedor> findByNomeContainingIgnoreCase(String nome);
}
package com.zlogcompras.repository;

import java.util.Optional; // Importação correta para Optional

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zlogcompras.model.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    // Método para buscar um fornecedor por CNPJ (excelente adição)
    Optional<Fornecedor> findByCnpj(String cnpj);

    // Métodos findById e saveAll são fornecidos automaticamente por JpaRepository.
    // Não é necessário sobrescrevê-los ou declará-los novamente, a menos que
    // precise de um comportamento muito específico com EntityGraph, por exemplo.
    // Assim, as linhas problemáticas foram REMOVIDAS.

    // Exemplo de outro método customizado (opcional):
    // List<Fornecedor> findByNomeContainingIgnoreCase(String nome);
}
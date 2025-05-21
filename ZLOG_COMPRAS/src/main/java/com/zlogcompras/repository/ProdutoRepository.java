package com.zlogcompras.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zlogcompras.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Você pode adicionar métodos de busca personalizados aqui se precisar
    // Ex: Optional<Produto> findByNome(String nome);
}
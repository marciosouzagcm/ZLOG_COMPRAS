package com.zlogcompras.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zlogcompras.model.Produto; // Importar Optional

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Método para buscar um produto pelo código (SKU)
    Optional<Produto> findByCodigo(String codigo);

    // Você pode adicionar outros métodos de consulta personalizados aqui, se necessário.
    // Por exemplo:
    // List<Produto> findByNomeContainingIgnoreCase(String nome);
}
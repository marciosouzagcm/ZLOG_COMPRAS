package com.zlogcompras.repository;

import com.zlogcompras.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Método corrigido para usar 'codigoProduto'
    Optional<Produto> findByCodigoProduto(String codigoProduto);
}
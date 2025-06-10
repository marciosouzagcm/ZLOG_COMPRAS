package com.zlogcompras.repository;

import com.zlogcompras.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     * Busca um produto pelo seu código de produto.
     *
     * @param codigoProduto O código único do produto.
     * @return Um Optional contendo o Produto se encontrado, ou um Optional vazio.
     */
    Optional<Produto> findByCodigoProduto(String codigoProduto);
}
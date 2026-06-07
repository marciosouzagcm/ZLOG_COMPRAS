package com.zlogcompras.repository;

import com.zlogcompras.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT p FROM Produto p WHERE p.codigo = :codigoProduto")
    Optional<Produto> findByCodigoProduto(@Param("codigoProduto") String codigoProduto);
}
package com.zlogcompras.repository;

import com.zlogcompras.model.Estoque;
import com.zlogcompras.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByProduto(Produto produto);

    @Query("SELECT e FROM Estoque e WHERE e.produto.id = :idProduto")
    Optional<Estoque> findByProdutoId(@Param("idProduto") Long idProduto);
}
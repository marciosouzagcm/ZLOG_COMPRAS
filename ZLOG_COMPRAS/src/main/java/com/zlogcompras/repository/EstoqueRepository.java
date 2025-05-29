package com.zlogcompras.repository;

import com.zlogcompras.model.Estoque;
import com.zlogcompras.model.Produto; // Certifique-se de que esta importação está correta
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    // Método para buscar estoque pelo objeto Produto
    Optional<Estoque> findByProduto(Produto produto);

    // Método para buscar estoque pelo código do Produto (resolvendo o erro anterior no service)
    // Isso assume que sua entidade Estoque tem uma relação com Produto,
    // e que a entidade Produto tem um campo 'codigoProduto'.
    // Ex: Estoque -> Produto -> codigoProduto
    Optional<Estoque> findByProduto_CodigoProduto(String codigoProduto);
}
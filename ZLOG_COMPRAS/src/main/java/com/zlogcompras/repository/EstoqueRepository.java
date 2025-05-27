package com.zlogcompras.repository;

import com.zlogcompras.model.Estoque;
import com.zlogcompras.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProduto(Produto produto); // <--- ADICIONE ESTE MÉTODO
}
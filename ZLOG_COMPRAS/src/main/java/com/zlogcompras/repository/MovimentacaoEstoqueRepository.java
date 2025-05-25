package com.zlogcompras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zlogcompras.model.MovimentacaoEstoque; // Importar List

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    // Método para buscar movimentações por código de material
    List<MovimentacaoEstoque> findByCodigoMaterialOrderByDataMovimentacaoDesc(String codigoMaterial);

    // Você pode adicionar outros métodos de consulta personalizados aqui, se necessário.
}
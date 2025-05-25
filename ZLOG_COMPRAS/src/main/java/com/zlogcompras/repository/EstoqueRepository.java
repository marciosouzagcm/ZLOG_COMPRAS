package com.zlogcompras.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zlogcompras.model.Estoque; // Importar Optional

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    // Método para buscar um registro de estoque pelo código do material
    Optional<Estoque> findByCodigoMaterial(String codigoMaterial);

    // Você pode adicionar outros métodos de consulta personalizados aqui, se necessário.
}
package com.zlogcompras.repository; // Ajuste o pacote conforme sua estrutura

import java.util.Optional; // Importa Optional

import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository

import com.zlogcompras.model.Role; // Importa a entidade Role

// RoleRepository estende JpaRepository para operações CRUD básicas na entidade Role
// Long é o tipo da chave primária da entidade Role
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Método personalizado para encontrar um papel pelo nome
    // Renomeado o parâmetro de 'user' para 'name' para maior clareza
    Optional<Role> findByName(String name); // <--- ALTERADO AQUI!
}
package com.zlogcompras.repository; // Ajuste o pacote conforme sua estrutura

import com.zlogcompras.model.User; // Importa a entidade User
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository
import java.util.Optional; // Importa Optional para lidar com resultados que podem não existir

// UserRepository estende JpaRepository para operações CRUD básicas na entidade User
// Long é o tipo da chave primária da entidade User
public interface UserRepository extends JpaRepository<User, Long> {

    // Método personalizado para encontrar um usuário pelo nome de usuário
    // O Spring Data JPA gera automaticamente a implementação desta consulta
    Optional<User> findByUsername(String username);
}

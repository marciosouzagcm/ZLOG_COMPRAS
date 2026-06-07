package com.zlogcompras.model; 

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor;

@Data // Anotação do Lombok para gerar getters, setters, toString, equals e hashCode
@Entity // Marca esta classe como uma entidade JPA
@Table(name = "roles") // Mapeia esta entidade para a tabela 'roles' no banco de dados
@NoArgsConstructor // Gera um construtor sem argumentos (necessário pelo JPA e para desserialização)
@AllArgsConstructor // Gera um construtor com todos os argumentos (id, name)
public class Role {

    @Id // Marca o campo 'id' como a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura a geração automática de IDs pelo banco de dados
    private Long id;

    // Mapeia o atributo 'name' para a coluna física 'nome' do TiDB
    @Column(name = "nome", nullable = false, unique = true) 
    private String name; // Ex: "ROLE_ADMIN", "ROLE_USER" (convenção do Spring Security)

    // Construtor adicional para facilitar a criação de Role apenas com o nome.
    // O ID será gerado automaticamente pelo banco de dados.
    public Role(String name) {
        this.name = name;
    }
}
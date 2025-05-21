package com.zlogcompras.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version; // Para a implementação de equals e hashCode

@Entity
@Table(name = "fornecedores") // Nome da tabela no banco de dados
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Nome é obrigatório
    private String nome;

    @Column(nullable = false, unique = true, length = 18) // CNPJ é obrigatório e único (formato XX.XXX.XXX/XXXX-XX)
    private String cnpj;

    @Column(nullable = true) // Contato pode ser opcional
    private String contato;

    @Column(nullable = true, length = 15) // Telefone pode ser opcional (ex: (XX) XXXXX-XXXX)
    private String telefone;

    @Column(nullable = true, unique = true) // Email pode ser opcional, mas se existir, deve ser único
    private String email;

    @Version // Campo para controle de concorrência otimista
    private Long version;

    // Construtor padrão (necessário para JPA)
    public Fornecedor() {
    }

    // Construtor com campos para facilitar a criação (opcional, mas útil)
    public Fornecedor(String nome, String cnpj, String contato, String telefone, String email) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.contato = contato;
        this.telefone = telefone;
        this.email = email;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    // ESTE É O SETTER CORRETO PARA O NOME
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    // --- Métodos equals e hashCode (Importante para coleções e comparação) ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Fornecedor that = (Fornecedor) o;
        return Objects.equals(id, that.id); // Comparar fornecedores pelo ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash baseado no ID
    }

    // --- Método toString (Útil para depuração) ---
    @Override
    public String toString() {
        return "Fornecedor{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", contato='" + contato + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", version=" + version +
                '}';
    }
}
package com.zlogcompras.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "fornecedores")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(nullable = true)
    private String contato;

    @Column(nullable = true, length = 15)
    private String telefone;

    @Column(nullable = true, unique = true)
    private String email;

    @Version
    private Long version;

    // Construtor padrão (necessário para JPA)
    public Fornecedor() {
    }

    // Construtor com campos para facilitar a criação
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

    // --- Métodos equals e hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fornecedor that = (Fornecedor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- Método toString ---
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
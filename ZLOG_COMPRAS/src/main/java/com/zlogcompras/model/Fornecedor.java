package com.zlogcompras.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "fornecedores") // Nome da tabela no banco de dados
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 18) // CNPJ é obrigatório e único
    private String cnpj;

    @NotBlank(message = "A razão social não pode ser nula ou vazia.")
    @Column(name = "razao_social")
    private String razao_social;

    @Column(length = 255)
    private String contato;

    @Column(unique = true, length = 255) // Email é único
    private String email;

    @Column(length = 255, nullable = false) // Endereço é obrigatório
    private String endereco;

    @Column(length = 15)
    private String telefone;

    @Version // Para controle de concorrência otimista
    private Long version;

    // Construtor vazio (necessário para JPA)
    public Fornecedor() {
    }

    // Construtor com todos os campos (opcional, mas útil)
    public Fornecedor(String cnpj, String razao_social, String contato, String email, String endereco,
            String telefone) {
        this.cnpj = cnpj;
        this.razao_social = razao_social;
        this.contato = contato;
        this.email = email;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razao_social;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razao_social = razaoSocial;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
                "id=" + id +
                ", cnpj='" + cnpj + '\'' +
                ", razao_social='" + razao_social + '\'' +
                ", contato='" + contato + '\'' +
                ", email='" + email + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", version=" + version +
                '}';
    }
}
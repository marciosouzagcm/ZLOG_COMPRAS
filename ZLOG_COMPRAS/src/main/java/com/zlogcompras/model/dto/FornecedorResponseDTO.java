package com.zlogcompras.model.dto;

import java.time.LocalDateTime; // Para campos de auditoria, se aplicável

// Não é necessário importar Fornecedor aqui, pois FornecedorResponseDTO é um DTO
// e não deve ter dependência direta com a entidade Fornecedor,
// a menos que esteja mapeando ou convertendo explicitamente, o que não é o caso do DTO em si.

public class FornecedorResponseDTO { // Renomeado para FornecedorResponseDTO

    private Long id;
    private String nome;
    private String razaoSocial;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String email;
    private String contato;
    private String observacoes;
    private Boolean ativo; // Indica se o fornecedor está ativo
    private Long version; // Para controle de concorrência otimista

    // Construtor padrão
    public FornecedorResponseDTO() {
    }

    // Construtor com todos os campos (útil para testes e mapeamento)
    public FornecedorResponseDTO(Long id, String nome, String razaoSocial, String cnpj, String endereco,
                                 String telefone, String email, String contato, String observacoes,
                                 Boolean ativo, Long version) {
        this.id = id;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.contato = contato;
        this.observacoes = observacoes;
        this.ativo = ativo;
        this.version = version;
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

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "FornecedorResponseDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}

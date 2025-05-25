package com.zlogcompras.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FornecedorRequestDTO {

    // ID geralmente não é enviado na criação. Em atualizações (PUT), pode vir no corpo, mas o @PathVariable é mais comum.
    // Pode ser mantido aqui se houver um caso de uso específico, mas é frequentemente removido em DTOs de requisição para POST.
    private Long id;

    @NotBlank(message = "O nome do fornecedor é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "A razão social é obrigatória.")
    @Size(max = 150, message = "A razão social deve ter no máximo 150 caracteres.")
    private String razaoSocial;

    @NotBlank(message = "O CNPJ é obrigatório.")
    // Corrigido o regex para CNPJ: \\d para escapar a barra invertida
    @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$", message = "CNPJ inválido. Use o formato XX.XXX.XXX/YYYY-ZZ.")
    private String cnpj;

    @NotBlank(message = "O endereço é obrigatório.")
    @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres.")
    private String endereco;

    @NotBlank(message = "O telefone é obrigatório.")
    // Corrigido o regex para Telefone: \\d para escapar a barra invertida, e mais flexível para DDD com/sem parênteses
    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}\\-?\\d{4}$", message = "Telefone inválido. Use o formato (XX) XXXX-XXXX ou (XX) XXXXX-XXXX.")
    private String telefone;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
    private String email;

    @Size(max = 100, message = "O nome do contato deve ter no máximo 100 caracteres.")
    private String contato; // Opcional, não precisa de @NotBlank

    @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres.")
    private String observacoes; // Opcional, não precisa de @NotBlank

    // Ativo pode ser true por padrão na criação, mas pode ser enviado para desativar/ativar.
    private Boolean ativo;

    // Construtor padrão
    public FornecedorRequestDTO() {
        this.ativo = true; // Define como ativo por padrão na criação
    }

    // Construtor com todos os campos (útil para testes e mapeamento)
    public FornecedorRequestDTO(Long id, String nome, String razaoSocial, String cnpj, String endereco,
                                 String telefone, String email, String contato, String observacoes, Boolean ativo) {
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

    @Override
    public String toString() {
        return "FornecedorRequestDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cnpj='" + cnpj + '\'' + // Removido o '+' extra aqui
                ", ativo=" + ativo +
                '}';
    }
}
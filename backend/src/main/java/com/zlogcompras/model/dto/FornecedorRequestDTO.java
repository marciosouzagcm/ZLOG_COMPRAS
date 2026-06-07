package com.zlogcompras.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FornecedorRequestDTO {

    @NotBlank(message = "O CNPJ é obrigatório.")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "CNPJ inválido. Use o formato XX.XXX.XXX/XXXX-XX")
    private String cnpj;

    @NotBlank(message = "A Razão Social é obrigatória.")
    @Size(max = 150, message = "A Razão Social deve ter no máximo 150 caracteres.")
    @JsonProperty("razao_social")
    private String razaoSocial; // <-- ATRIBUTO CORRIGIDO

    @Size(max = 255, message = "O Contato deve ter no máximo 255 caracteres.")
    private String contato;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email inválido.")
    @Size(max = 255, message = "O Email deve ter no máximo 255 caracteres.")
    private String email;

    @NotBlank(message = "O endereço é obrigatório.")
    @Size(max = 255, message = "O Endereço deve ter no máximo 255 caracteres.")
    private String endereco;

    @Size(max = 15, message = "O Telefone deve ter no máximo 15 caracteres.")
    private String telefone;

    @Size(max = 500, message = "As Observações devem ter no máximo 500 caracteres.")
    private String observacoes;

    // @NotNull se você quiser que o campo 'ativo' seja sempre enviado
    private Boolean ativo;

    // Construtor padrão (necessário para o Spring e Jackson)
    public FornecedorRequestDTO() {
    }

    // --- Getters e Setters para todos os campos ---

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    // --- GETTER E SETTER CORRIGIDOS PARA razao_social ---
    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
    // ----------------------------------------------------

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
}
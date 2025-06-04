package com.zlogcompras.model.dto;

// Importar LocalDateTime apenas se for usado para campos de auditoria no DTO
// import java.time.LocalDateTime;

public class FornecedorResponseDTO {

    private Long id;

    // Este campo 'razaoSocial' será preenchido com o valor da coluna 'razao_social' do BD
    private String razaoSocial;

    private String cnpj;
    private String endereco;
    private String telefone;
    private String email;
    private String contato;
    private String observacoes; // Presente no retorno
    private Boolean ativo;      // Presente no retorno
    private Long version;

    // Construtor padrão
    public FornecedorResponseDTO() {
    }

    // Construtor com todos os campos
    public FornecedorResponseDTO(Long id, String razaoSocial, String cnpj, String endereco,
                                 String telefone, String email, String contato, String observacoes,
                                 Boolean ativo, Long version) {
        this.id = id;
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
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public String toString() {
        return "FornecedorResponseDTO{" +
                "id=" + id +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", contato='" + contato + '\'' +
                ", observacoes='" + observacoes + '\'' +
                ", ativo=" + ativo +
                ", version=" + version +
                '}';
    }
}
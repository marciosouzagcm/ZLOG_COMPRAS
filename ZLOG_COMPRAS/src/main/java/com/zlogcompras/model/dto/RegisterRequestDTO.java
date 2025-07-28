package com.zlogcompras.model.dto;

// Não precisa de muitos imports para um DTO simples como este
// import java.util.Set; // Exemplo: se fosse receber roles diretamente, mas não é o caso aqui

public class RegisterRequestDTO {

    private String username;
    private String password;
    // Não inclua o password criptografado aqui, nem roles, etc.
    // Este DTO é apenas para receber as informações cruas do cliente.

    // --- Construtores (Opcional, mas boa prática para alguns cenários) ---
    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // --- Getters e Setters (Essenciais para o Spring Mapear o JSON) ---
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // --- toString() (Opcional, útil para depuração) ---
    @Override
    public String toString() {
        return "RegisterRequestDTO{" +
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" + // Não imprima a senha real no log!
                '}';
    }
}
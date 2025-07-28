package com.zlogcompras.model;

import java.util.Collection; // Importa Collection
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data; // Importa a anotação @Data do Lombok

@Data // Anotação do Lombok para gerar getters, setters, toString, equals e hashCode
@Entity // Marca esta classe como uma entidade JPA
@Table(name = "users") // Mapeia esta entidade para a tabela 'users' no banco de dados
public class User implements UserDetails { // <-- ADICIONADO: implements UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    // --- Métodos da interface UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList()); // Ou Collectors.toSet(), ambos funcionam. toList() é comum.
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implemente sua lógica de expiração de conta aqui, se houver
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implemente sua lógica de bloqueio de conta aqui, se houver
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implemente sua lógica de expiração de credenciais aqui, se houver
    }

    @Override
    public boolean isEnabled() {
        return enabled; // Retorna o valor do seu campo 'enabled'
    }
}
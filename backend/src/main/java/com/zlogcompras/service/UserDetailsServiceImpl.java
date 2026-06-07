package com.zlogcompras.service; // Ajuste o pacote conforme sua estrutura

import com.zlogcompras.model.User; // Importa a entidade User
import com.zlogcompras.repository.UserRepository; // Importa o UserRepository
import org.springframework.security.core.GrantedAuthority; // Importa GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Importa SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails
import org.springframework.security.core.userdetails.UserDetailsService; // Importa UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importa UsernameNotFoundException
import org.springframework.stereotype.Service; // Marca esta classe como um serviço Spring
import java.util.Set; // Importa Set
import java.util.stream.Collectors; // Importa Collectors para manipulação de streams

@Service // Indica que esta é uma classe de serviço gerenciada pelo Spring
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository; // Injeta o UserRepository para acessar os dados do usuário

    // Construtor para injeção de dependência do UserRepository
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    // Este método é chamado pelo Spring Security para carregar os detalhes do usuário
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário no banco de dados pelo nome de usuário
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o nome: " + username));

        // Converte os papéis (Roles) do usuário em uma coleção de GrantedAuthority
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // Cria uma SimpleGrantedAuthority para cada papel
                .collect(Collectors.toSet()); // Coleta as autoridades em um Set

        // Retorna um objeto UserDetails (do Spring Security)
        // Este objeto contém o nome de usuário, a senha (criptografada) e as autoridades (papéis)
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
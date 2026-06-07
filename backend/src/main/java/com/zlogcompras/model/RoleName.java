package com.zlogcompras.model; 

public enum RoleName {
    // Definindo os nomes dos papéis que sua aplicação usará
    // É uma boa prática prefixar com "ROLE_" para Spring Security,
    // mas depende da sua implementação exata de UserDetails.
    ROLE_USER, // Um papel para usuários comuns
    ROLE_ADMIN // Um papel para administradores
}
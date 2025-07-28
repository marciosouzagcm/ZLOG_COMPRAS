package com.zlogcompras;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase; // Importe esta anotação
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace; // Importe esta anotação
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest // Anotação que carrega o contexto completo da aplicação Spring Boot para o
				// teste
@ActiveProfiles("test") // Ativa o perfil "test", que carregará o application.properties de teste
// Configura o banco de dados de teste para substituir o banco de dados de
// produção
// Replace.ANY significa que qualquer DataSource configurado será substituído
// por um DataSource em memória
@AutoConfigureTestDatabase(replace = Replace.ANY)
class ZlogComprasApplicationTests {

	@Test
	void contextLoads() {
		// Este teste simples verifica se o contexto da aplicação Spring Boot
		// consegue carregar sem erros. Se este teste passar, significa que
		// todas as suas configurações (incluindo banco de dados H2 para testes)
		// e beans estão sendo inicializados corretamente.
	}

}
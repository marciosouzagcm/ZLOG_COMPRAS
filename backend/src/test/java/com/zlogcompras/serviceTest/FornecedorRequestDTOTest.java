package com.zlogcompras.serviceTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.zlogcompras.model.dto.FornecedorRequestDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("Testes para FornecedorRequestDTO")
public class FornecedorRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar um FornecedorRequestDTO com todos os campos válidos")
    void deveValidarFornecedorRequestDTOValido() {
        // Cenário: Cria um DTO com dados válidos
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setCnpj("11.222.333/0001-44");
        dto.setRazaoSocial("Empresa de Testes S.A.");
        dto.setContato("João da Silva");
        dto.setEmail("contato@empresa.com");
        dto.setEndereco("Rua dos Testes, 123 - Cidade Teste");
        dto.setTelefone("11987654321");
        dto.setObservacoes("Nenhuma observação.");
        dto.setAtivo(true);

        // Ação: Valida o DTO
        Set<ConstraintViolation<FornecedorRequestDTO>> violations = validator.validate(dto);

        // Verificação: Nenhuma violação deve ser encontrada
        assertTrue(violations.isEmpty(), "O DTO deve ser válido quando todos os campos estão corretos.");
    }

    @Test
    @DisplayName("Não deve validar quando CNPJ é nulo ou vazio")
    void naoDeveValidarCnpjNuloOuVazio() {
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setRazaoSocial("Empresa Vazia");
        dto.setEndereco("Rua Teste"); // campo obrigatório preenchido!
        // CNPJ nulo
        dto.setCnpj(null);
        Set<ConstraintViolation<FornecedorRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cnpj")));
        // Não verifique quantidade, apenas se existe a violação do campo esperado

        // CNPJ vazio
        dto.setCnpj("");
        violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cnpj")));
    }

    @Test
    @DisplayName("Não deve validar quando CNPJ tem formato inválido")
    void naoDeveValidarCnpjComFormatoInvalido() {
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setRazaoSocial("Empresa Inválida");
        dto.setEndereco("Rua Teste");

        dto.setCnpj("12345678901234"); // Formato incorreto (sem pontuação)
        Set<ConstraintViolation<FornecedorRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(
                violations.iterator().next().getMessage().contains("CNPJ inválido. Use o formato XX.XXX.XXX/XXXX-XX"));

        dto.setCnpj("11.222.333/0001-XX"); // Caracteres inválidos
        violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(
                violations.iterator().next().getMessage().contains("CNPJ inválido. Use o formato XX.XXX.XXX/XXXX-XX"));
    }

    @Test
    @DisplayName("Não deve validar quando Razão Social é nula, vazia ou excede o tamanho máximo")
    void naoDeveValidarRazaoSocialInvalida() {
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setCnpj("11.222.333/0001-44");
        dto.setEndereco("Rua Teste");

        // Razão Social nula
        dto.setRazaoSocial(null);
        Set<ConstraintViolation<FornecedorRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("A Razão Social é obrigatória."));

        // Razão Social vazia
        dto.setRazaoSocial("");
        violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("A Razão Social é obrigatória."));

        // Razão Social muito longa (excede 150 caracteres)
        dto.setRazaoSocial("a".repeat(151));
        violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage()
                .contains("A Razão Social deve ter no máximo 150 caracteres."));
    }

    @Test
    @DisplayName("Não deve validar quando Endereço é nulo, vazio ou excede o tamanho máximo")
    void naoDeveValidarEnderecoInvalido() {
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setCnpj("11.222.333/0001-44");
        dto.setRazaoSocial("Empresa Endereço");

        // Endereço nulo
        dto.setEndereco(null);
        Set<ConstraintViolation<FornecedorRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("O endereço é obrigatório."));

        // Endereço vazio
        dto.setEndereco("");
        violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("O endereço é obrigatório."));

        // Endereço muito longo (excede 255 caracteres)
        dto.setEndereco("e".repeat(256));
        violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("O Endereço deve ter no máximo 255 caracteres."));
    }

    @Test
    @DisplayName("Não deve validar quando Email tem formato inválido ou excede o tamanho máximo")
    void naoDeveValidarEmailInvalido() {
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setCnpj("11.222.333/0001-44");
        dto.setRazaoSocial("Empresa Email");
        dto.setEndereco("Rua Teste");

        // Email com formato inválido
        dto.setEmail("emailinvalido");
        Set<ConstraintViolation<FornecedorRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));

        dto.setEmail("email@.com");
        violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));

        // Email muito longo (excede 255 caracteres)
        String sufixo = "@dominio.com"; // 12 caracteres
        int maxLocal = 255 - sufixo.length(); // 243
        dto.setEmail("a".repeat(maxLocal) + sufixo); // Exatamente 255 caracteres
        violations = validator.validate(dto);
        assertFalse(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));

        dto.setEmail("a".repeat(maxLocal + 1) + sufixo); // 256 caracteres
        violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("Não deve validar quando Contato excede o tamanho máximo")
    void naoDeveValidarContatoExcedeTamanho() {
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setCnpj("11.222.333/0001-44");
        dto.setRazaoSocial("Empresa Contato");
        dto.setEndereco("Rua Teste");

        // Contato muito longo (excede 255 caracteres)
        dto.setContato("c".repeat(256));
        Set<ConstraintViolation<FornecedorRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("O Contato deve ter no máximo 255 caracteres."));
    }

    @Test
    @DisplayName("Não deve validar quando Telefone excede o tamanho máximo")
    void naoDeveValidarTelefoneExcedeTamanho() {
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setCnpj("11.222.333/0001-44");
        dto.setRazaoSocial("Empresa Telefone");
        dto.setEndereco("Rua Teste");

        // Telefone muito longo (excede 15 caracteres)
        dto.setTelefone("1234567890123456"); // 16 caracteres
        Set<ConstraintViolation<FornecedorRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("O Telefone deve ter no máximo 15 caracteres."));
    }

    @Test
    @DisplayName("Não deve validar quando Observacoes excedem o tamanho máximo")
    void naoDeveValidarObservacoesExcedeTamanho() {
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setCnpj("11.222.333/0001-44");
        dto.setRazaoSocial("Empresa Observacoes");
        dto.setEndereco("Rua Teste");

        // Observações muito longas (excede 500 caracteres)
        dto.setObservacoes("o".repeat(501));
        Set<ConstraintViolation<FornecedorRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage()
                .contains("As Observações devem ter no máximo 500 caracteres."));
    }

    @Test
    @DisplayName("Deve validar DTO com campos opcionais nulos")
    void deveValidarDtoComCamposOpcionaisNulos() {
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setCnpj("11.222.333/0001-44");
        dto.setRazaoSocial("Empresa Opcionais Nulos");
        dto.setEndereco("Rua Principal, 456");
        // Campos de contato, email, telefone, observacoes, ativo são nulos por padrão

        Set<ConstraintViolation<FornecedorRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "O DTO deve ser válido mesmo com campos opcionais nulos.");
    }
}
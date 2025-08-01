# ZLOG Compras: Sistema de Gerenciamento de Compras Empresariais

<p align="center">
  <img src="https://via.placeholder.com/150?text=Logo+ZLOG" alt="Logo ZLOG Compras" width="150"/>
</p>

🎯 **Sobre o Projeto**
O ZLOG Compras é um sistema **Full Stack** robusto e modular, desenvolvido para otimizar e gerenciar o fluxo completo de compras em ambientes empresariais. Composto por um backend em **Spring Boot** e um frontend em **Angular**, nosso objetivo é automatizar e trazer transparência a todas as etapas da aquisição, desde a criação de solicitações de materiais até a geração de pedidos de compra e o controle de recebimento.

Construímos este projeto com foco em qualidade de código, documentação interativa e testes abrangentes, garantindo uma solução confiável e de fácil manutenção.

---

## ✨ Funcionalidades Desenvolvidas (Até o Momento)

Desenvolvemos e validamos as seguintes funcionalidades essenciais:

* **Gerenciamento de Solicitações de Compra:** Crie, visualize e gerencie solicitações de materiais ou serviços de forma intuitiva.
* **Processo de Cotação e Orçamento:**
    * Criação e gestão de orçamentos detalhados para fornecedores.
    * **Validação Crucial:** Impedimos a criação de novos orçamentos para solicitações já finalizadas, assegurando a integridade do processo de compras.
    * Aprovação de orçamentos para dar continuidade ao fluxo.
* **Geração de Pedidos de Compra:**
    * Crie pedidos de compra de forma automatizada, diretamente de orçamentos aprovados.
    * Associação clara com fornecedores e os itens do orçamento original.
* **Validações de Dados Robustas:** Implementamos validações rigorosas para preços e quantidades nos itens de compra e orçamento, garantindo a qualidade e a consistência dos dados inseridos no sistema.
* **Base para Recebimento de Itens:** Já temos a estrutura inicial e os endpoints definidos para o futuro registro e controle do recebimento de itens do pedido.
* **Atualização de Status do Pedido:** Capacidade de atualizar o status do pedido de compra em cada etapa do seu progresso.
* **Frontend - Módulo de Autenticação (Base):**
    * **Tela de Login Funcional:** Implementação da estrutura de componentes para a tela de login, incluindo formulário de entrada de credenciais e comunicação de dados entre componentes pai e filho. (A integração com a API e a autenticação real serão os próximos passos.)

---

## 🛠️ Tecnologias e Ferramentas

Nosso projeto é construído com tecnologias modernas e eficientes:

### Backend (API)

* **Linguagem de Programação:** Java 21
* **Framework:** Spring Boot (v3.3.0)
* **Banco de Dados:** MySQL
* **Persistência:** Spring Data JPA / Hibernate
* **Ferramenta de Build:** Maven
* **Documentação da API:** Swagger UI / OpenAPI
* **Testes:** JUnit (com 18 testes unitários para Fornecedores e Orçamentos, todos com 100% de sucesso!)

### Frontend (Aplicação Web)

* **Framework:** Angular (última versão recomendada, com Standalone Components)
* **Linguagem:** TypeScript
* **Marcação/Estilo:** HTML, CSS / SCSS
* **Gerenciamento de Estado (Inicial):** RxJS Observables (para comunicação entre componentes)
* **Controle de Versão:** Git

## 📂 Estrutura do Projeto

O repositório está organizado de forma a separar claramente o backend do frontend:

ZLOG_COMPRAS/
├── backend/                  # Contém o projeto Spring Boot (API)
│   ├── src/
│   ├── pom.xml
│   └── ...
└── frontend/                 # Contém o projeto Angular (Aplicação Web)
├── src/
├── angular.json
└── ...

## 🚀 Como Executar o Projeto

Para executar o sistema ZLOG Compras completo (backend e frontend), siga os passos abaixo:

### Pré-requisitos

Certifique-se de ter instalado em sua máquina:

* **JDK 21** ou superior
* **Apache Maven**
* Um servidor **MySQL** (lembre-se de configurar as credenciais do banco de dados no arquivo `backend/src/main/resources/application.properties` ou `application.yml`).
* **Node.js e npm** (ou Yarn) - **Versão LTS recomendada**
* **Angular CLI** (Instale globalmente: `npm install -g @angular/cli`)

### 1. Executando o Backend (API)

1.  **Navegue** até o diretório do backend:
    ```bash
    cd ZLOG_COMPRAS/backend
    ```
2.  **Construa o projeto** (opcional, se não estiver usando IDE):
    ```bash
    mvn clean install
    ```
3.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```
    A API estará disponível em `http://localhost:8080` (ou na porta configurada em seu `application.properties`).

### 2. Executando o Frontend (Aplicação Web)

1.  **Abra um NOVO terminal** (mantenha o terminal do backend rodando).
2.  **Navegue** até o diretório do frontend:
    ```bash
    cd ZLOG_COMPRAS/frontend
    ```
3.  **Instale as dependências** do projeto Angular (somente na primeira vez ou se houver alterações no `package.json`):
    ```bash
    npm install
    ```
4.  **Inicie a aplicação Angular:**
    ```bash
    ng serve -o
    ```
    A aplicação frontend será aberta automaticamente no seu navegador em `http://localhost:4200/` (ou em outra porta disponível).

---

## ✅ Verificação e Testes

### Backend (API)

* **Documentação da API (Swagger UI):** Após iniciar a aplicação backend, explore e teste os endpoints da API de forma interativa acessando:
    `http://localhost:8080/swagger-ui.html`
    * **Confirmação:** A API foi testada com sucesso via Swagger UI, demonstrando que todos os endpoints estão respondendo corretamente.
* **Testes Unitários:** Para garantir a qualidade e a estabilidade do código, execute os testes unitários Maven:
    ```bash
    cd ZLOG_COMPRAS/backend
    mvn test
    ```
* **Validação do Banco de Dados:** A conexão e a persistência de dados no MySQL foram verificadas e estão funcionando conforme o esperado.

### Frontend (Aplicação Web)

* **Tela de Login:** Após iniciar o frontend, a tela de login deve ser exibida.
    * **Teste de Simulação:** Insira `teste@email.com` como e-mail e `senha123` como senha e clique em "Entrar". Verifique o console do navegador (F12) para as mensagens de "Login bem-sucedido!". Para credenciais inválidas, a mensagem de erro deve aparecer na tela.

---

## 🤝 Como Contribuir

Este projeto está em constante evolução e valorizamos muito a colaboração da comunidade! Se você é um desenvolvedor, estudante ou entusiasta de software, sinta-se à vontade para:

* **Explorar o Código:** Mergulhe na estrutura do projeto e entenda nossas implementações.
* **Abrir Issues:** Reporte bugs, sugira novas funcionalidades ou melhorias.
* **Submeter Pull Requests:** Contribua com código, novos testes, melhorias na documentação ou refatorações.

Sua contribuição é fundamental para tornar o ZLOG Compras uma solução cada vez mais completa e eficiente!

---

## 📝 Licença
Este projeto está licenciado sob a Licença MIT. Você pode ver os detalhes completos da licença no arquivo LICENSE no repositório.

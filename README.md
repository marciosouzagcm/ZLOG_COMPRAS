# ZLOG Compras: Sistema de Gerenciamento de Compras Empresariais

<p align="center">
  <img src="logo_ZLOG.png" alt="Logo ZLOG Compras" width="150"/>
</p>

🎯 **Sobre o Projeto**
O ZLOG Compras é um sistema **Full Stack** robusto e modular, desenvolvido para otimizar e gerenciar o fluxo completo de compras em ambientesempresariais. Composto por um backend em **Spring Boot** e um frontend em **React (Vite)**, nosso objetivo é automatizar e trazer transparência a todas as etapas da aquisição, desde a criação de solicitações de materiais até a geração de pedidos de compra e o controle de recebimento de estoque.

Construímos este projeto com foco em qualidade de código, integridade relacional, documentação interativa e testes abrangentes, garantindo uma solução confiável e de fácil manutenção.

---

## ✨ Funcionalidades Desenvolvidas (Até o Momento)

Desenvolvemos e validamos as seguintes funcionalidades essenciais:

* **Modelagem e Migração de Dados Coesa:** Banco de dados MySQL gerido via Flyway, eliminando redundâncias de saldos e garantindo integridade referencial estrita entre produtos, movimentações e estoques.
* **Gerenciamento de Solicitações de Compra:** Crie, visualize e gerencie solicitações de materiais ou serviços de forma intuitiva.
* **Processo de Cotação e Orçamento:**
    * Criação e gestão de orçamentos detalhados para fornecedores.
    * **Validação Crucial:** Impedimos a criação de novos orçamentos para solicitações já finalizadas, assegurando a integridade do processo de compras.
    * Aprovação de orçamentos para dar continuidade ao fluxo.
* **Geração de Pedidos de Compra:**
    * Crie pedidos de compra de forma automatizada, diretamente de orçamentos aprovados.
    * Associação clara com fornecedores e os itens do orçamento original.
* **Validações de Dados Robustas:** Implementamos validações rigorosas para preços e quantidades nos itens de compra e orçamento, garantindo a qualidade e a consistência dos dados inseridos no sistema através de validações JPA/Hibernate.
* **Base para Recebimento de Itens e Estoque:** Estrutura inicial e endpoints definidos para o registro de movimentações e controle de saldo centralizado na entidade de estoque.
* **Atualização de Status do Pedido:** Capacidade de atualizar o status do pedido de compra em cada etapa do seu progresso.
* **Frontend - Módulo de Autenticação (Base):**
    * **Tela de Login Funcional em React:** Implementação da estrutura de componentes baseados em TypeScript para a tela de login, incluindo formulário de entrada de credenciais e comunicação eficiente de estados. (A integração com a API para persistência de tokens JWT será o próximo passo.)

---

## 🛠️ Tecnologias e Ferramentas

Nosso projeto é construído com tecnologias modernas e eficientes:

### Backend (API)

* **Linguagem de Programação:** Java 21
* **Framework:** Spring Boot (v3.3.0)
* **Banco de Dados:** MySQL 8
* **Evolução de Esquema:** Flyway Migrations
* **Persistência:** Spring Data JPA / Hibernate
* **Ferramenta de Build:** Maven
* **Documentação da API:** Swagger UI / OpenAPI

### Frontend (Aplicação Web)

* **Ambiente de Execução/Build:** React 18 & Vite
* **Linguagem:** TypeScript
* **Estilização:** TailwindCSS & shadcn/ui
* **Gerenciamento de Estado/Requisições:** Axios & TanStack Query (React Query)
* **Controle de Versão:** Git

---

## 📂 Estrutura do Projeto

O repositório está organizado de forma a separar claramente o backend do frontend:

```text
ZLOG_COMPRAS/
├── backend/                  # Contém o projeto Spring Boot (API)
│   ├── src/
│   │   ├── main/java/        # Código-fonte Java (Controllers, Services, Repositories)
│   │   └── main/resources/   # Configurações e scripts SQL do Flyway (db/migration/)
│   └── pom.xml               # Manifesto de dependências Maven
└── frontend/                 # Contém o projeto React + Vite (Aplicação Web)
    ├── src/                  # Componentes, Páginas, Hooks e Contextos React
    ├── index.html
    ├── package.json          # Manifesto de dependências Node.js
    └── vite.config.ts        # Configurações de compilação do Vite

---

🚀 Como Executar o Projeto
Para executar o sistema ZLOG Compras completo (backend e frontend), siga os passos abaixo:

Pré-requisitos
Certifique-se de ter instalado em sua máquina:

JDK 21 ou superior

Apache Maven

Um servidor MySQL 8 (lembre-se de criar o schema zlog_compras e configurar as credenciais adequadas).

Node.js (Versão LTS estável) e npm (ou Yarn).

1. Configurando e Executando o Backend (API)
Configure o banco de dados alterando as credenciais no arquivo backend/src/main/resources/application.properties ou application.yml:

Properties
spring.datasource.url=jdbc:mysql://localhost:3306/zlog_compras?useSSL=false&serverTimezone=UTC
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=validate
Navegue até o diretório do backend:

Bash
cd ZLOG_COMPRAS/backend

3. **Construa o projeto** (opcional, se não estiver usando uma IDE):
   ```bash
   mvn clean install
   
Execute a aplicação:

Bash
mvn spring-boot:run

   A API estará disponível em `http://localhost:8080`. O Flyway detectará a base de dados e executará os scripts de migração estrutural automaticamente no primeiro boot.

### 2. Executando o Frontend (Aplicação Web)

1. **Abra um NOVO terminal** (mantenha o terminal do backend rodando).
2. **Navegue** até o diretório do frontend:
   ```bash
   cd ZLOG_COMPRAS/frontend
Instale as dependências do projeto React:

Bash
npm install
Inicie a aplicação com o Vite:

Bash
npm run dev

   A aplicação frontend será iniciada e o endereço local gerado (geralmente `http://localhost:5173`) estará disponível no console para acesso no navegador.

---

## ✅ Verificação e Testes

### Backend (API)

* **Documentação da API (Swagger UI):** Após iniciar a aplicação backend, explore e teste os endpoints da API de forma interativa acessando:
  `http://localhost:8080/swagger-ui.html`
  * **Confirmação:** A API foi validada com sucesso via Swagger UI, demonstrando que todos os contratos de endpoints estão respondendo corretamente.
* **Testes Unitários:** Para garantir a qualidade e a estabilidade do código, execute a suíte de testes automatizados:
  ```bash
  cd ZLOG_COMPRAS/backend
  mvn test
  
Status: 18 testes unitários para Fornecedores e Orçamentos executados com 100% de sucesso!

Validação do Banco de Dados: A consistência das tabelas e relacionamentos controlados pelas migrações do Flyway foi integralmente verificada no MySQL.

Frontend (Aplicação Web)
Tela de Login (Componente React): Após iniciar o servidor de desenvolvimento do Vite, a tela de login estruturada com TypeScript será exibida.

Teste de Simulação: Insira teste@email.com como e-mail e senha123 como senha e clique no botão de envio. Abra as ferramentas de desenvolvedor do seu navegador (F12) para validar no Console o fluxo correto de captura e submissão dos dados do formulário.

🤝 Como Contribuir
Este projeto está em constante evolução e valorizamos muito a colaboração da comunidade! Se você é um desenvolvedor, estudante ou entusiasta de software, sainte-se à vontade para:

Explorar o Código: Mergulhe na nossa arquitetura Java/Spring e na componentização moderna em React.

Abrir Issues: Reporte bugs, sugira novas funcionalidades ou melhorias de arquitetura.

Submeter Pull Requests: Contribua com código, novos testes automatizados, melhorias na documentação ou refatorações estruturais.

Sua contribuição é fundamental para tornar o ZLOG Compras uma solução cada vez mais completa e eficiente!

📝 Licença
Este projeto está licensed sob a Licença MIT. Você pode ver os detalhes completos da licença no arquivo LICENSE no repositório.

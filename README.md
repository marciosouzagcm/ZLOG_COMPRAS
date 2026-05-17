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

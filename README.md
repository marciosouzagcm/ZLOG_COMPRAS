# ZLOG Compras: Sistema de Gerenciamento de Compras Empresariais

<p align="center">
  <img src="logo_ZLOG.png" alt="Logo ZLOG Compras" width="150"/>
</p>

🎯 **Sobre o Projeto**
O ZLOG Compras é um sistema **Full Stack** robusto e modular, desenvolvido para otimizar e gerenciar o fluxo completo de compras em ambientes empresariais (Fluxo Procure-to-Pay). Composto por um backend de alta performance em **Spring Boot (Java 21)** e um frontend dinâmico e componentizado em **React (Vite)**, o sistema automatiza e traz transparência a todas as etapas da aquisição, desde a abertura de solicitações de materiais até a geração de pedidos de compra e o rígido controle de saldos de estoque.

O projeto foi reestruturado utilizando conceitos acadêmicos avançados de **Modelagem Relacional de Dados**, aplicando padrões de integridade estrita e eliminando redundâncias na persistência de informações.

---

## ✨ Funcionalidades Desenvolvidas & Corrigidas

* **Modelagem Relacional Coesa (Flyway):** Arquitetura de banco de dados totalmente limpa, isolando o histórico relacional (snapshots de cotações) e garantindo uma relação 1:1 estrita para o controle de saldos de estoque.
* **Gerenciamento de Solicitações de Compra:** Mapeamento intuitivo e criação de requisições de insumos ou serviços por centro de custo.
* **Processo de Cotação e Orçamentos:**
    * Criação e triagem de propostas comerciais de múltiplos fornecedores.
    * **Validação de Estado:** Regra de negócio automatizada que impede novos orçamentos para solicitações de compra já consolidadas ou finalizadas.
* **Geração de Pedidos de Compra:** Conversão ágil e vinculação direta de orçamentos aprovados em ordens de compra formais.
* **Gestão Unificada de Estoque:** Correção estrutural que centraliza a consolidação de saldos físicos na tabela de Estoques, alimentada cronologicamente por um livro-razão de movimentações detalhadas.
* **Frontend - Autenticação & Telas Base:** Interface moderna estruturada em React com TypeScript, utilizando formulários inteligentes para captura de credenciais e gerenciamento centralizado de estados para as próximas etapas de integração com tokens JWT.

---

## 🛠️ Tecnologias e Ferramentas

### Backend (API)
* **Linguagem:** Java 21
* **Framework:** Spring Boot (v3.3.0)
* **Banco de Dados:** MySQL 8 / TiDB Cloud (NewSQL Serverless)
* **Controle de Evolução de Banco:** Flyway Migrations
* **Mapeamento Objeto-Relacional:** Spring Data JPA / Hibernate
* **Gerenciador de Dependências:** Maven
* **Documentação:** Swagger UI / OpenAPI Spec

### Frontend (Aplicação Web)
* **Ecossistema:** React 18 / Vite
* **Linguagem:** TypeScript
* **Estilização:** TailwindCSS / Componentes Declarativos
* **Comunicação com a API:** Axios
* **Controle de Versão:** Git

---

## 📂 Estrutura Estrutural do Repositório

```text
ZLOG_COMPRAS/
├── backend/                  # API Spring Boot
│   ├── src/
│   │   ├── main/java/        # Camadas de Controller, Service, Repository e Entity
│   │   └── main/resources/   # application.properties e scripts Flyway (db/migration/)
│   └── pom.xml               # Configurações de build do Maven
└── frontend/                 # Aplicação Web React
    ├── src/                  # Páginas, Componentes de UI, hooks e serviços
    ├── index.html
    ├── package.json          # Manifesto de dependências Node.js
    └── vite.config.ts        # Arquivo de configuração de bundling do Vite

    🚀 Como Executar o Projeto
Pré-requisitos
JDK 21 ou superior instalado.

Apache Maven configurado no PATH do sistema.

Node.js (Versão LTS Recomendada).

Instância ativa do MySQL 8 local ou cluster TiDB Cloud (Serverless) configurado na nuvem.

1. Configuração e Execução do Backend
Abra o arquivo backend/src/main/resources/application.properties (ou .yml) e configure o apontamento para o seu banco de dados (seja local ou a string de conexão segura fornecida pelo painel do TiDB Cloud):

Properties
spring.datasource.url=jdbc:mysql://<SEU_HOST_TIDB_OU_LOCAL>:3306/zlog_compras?useSSL=true&serverTimezone=UTC
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=validate

2. Via terminal, entre na pasta do backend:
   ```bash
   cd ZLOG_COMPRAS/backend
   
Execute o comando de compilação e inicialização do ecossistema Spring:

Bash
mvn clean install
mvn spring-boot:run

   *O Flyway varrerá o banco automaticamente, criando a estrutura de tabelas corrigida e inserindo as Roles obrigatórias de segurança.*

### 2. Execução do Frontend (React + Vite)

1. Abra uma nova janela de terminal e vá até a pasta do cliente:
   ```bash
   cd ZLOG_COMPRAS/frontend
Instale os pacotes e dependências de Node declarados no projeto:

Bash
npm install
Inicie o servidor de desenvolvimento ultra-rápido do Vite:

Bash
npm run dev

   O console exibirá o endereço local para visualização (geralmente `http://localhost:5173`). Abra o link no seu navegador.

---

## ✅ Verificação, Testes e Qualidade

* **Swagger UI Interativo:** Explore os contratos e teste as rotas da API em tempo real acessando `http://localhost:8080/swagger-ui.html` com o backend em execução.
* **Suíte de Testes Automatizados:** Rode as validações de integridade executando:
   ```bash
   mvn test
   
(Garantia de estabilidade com 18 testes unitários focados nas entidades críticas de Fornecedores e Orçamentos rodando com 100% de sucesso).

Auditoria de Login (Simulação Front): A tela de acesso desenvolvida em React captura os inputs de login (teste@email.com) e valida localmente a consistência do fluxo antes do despacho assíncrono à API, registrando o comportamento no DevTools (F12) do navegador.

🤝 Contribuições e Feedbacks
Este repositório serve como portfólio prático de engenharia de software e modelagem de sistemas. Pull Requests com refatorações, novos casos de testes ou novas views em React são super bem-vindos!

📝 Licença
Distribuído sob a licença MIT. Veja LICENSE para mais informações.
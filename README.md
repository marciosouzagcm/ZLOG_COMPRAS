##ZLOG Compras: Sistema de Gerenciamento de Compras Empresariais
🎯 Sobre o Projeto
O ZLOG Compras é um sistema Full Stack robusto e modular, desenvolvido para otimizar e gerenciar o fluxo completo de compras em ambientes empresariais (Fluxo Procure-to-Pay). Composto por um backend de alta performance em Spring Boot (Java 21) e um frontend dinâmico e componentizado em React (Vite), o sistema automatiza e traz transparência a todas as etapas da aquisição, desde a abertura de solicitações de materiais até a geração de pedidos de compra e o rígido controle de saldos de estoque.

O projeto utiliza conceitos avançados de Modelagem Relacional de Dados, aplicando padrões de integridade estrita, isolamento transacional ACID e eliminação de redundâncias na persistência de informações em um ambiente NewSQL distribuído.

✨ Funcionalidades Desenvolvidas & Corrigidas
Modelagem Relacional Coesa (Flyway & TiDB): Arquitetura de banco de dados NewSQL totalmente limpa, isolando o histórico relacional e garantindo uma relação estrita para o controle de saldos de estoque e snapshots de cotações.

Segurança Baseada em JWT: Ecossistema de segurança configurado com Spring Security e filtros de autenticação por tokens stateless (Bearer JWT), garantindo rotas protegidas de ponta a ponta.

Fluxo Procure-to-Pay Completo:

Solicitações de Compra: Abertura e triagem de demandas por item e centro de custo.

Orçamentos & Concorrência: Lançamento de propostas de múltiplos fornecedores vinculadas à solicitação, com validação de estado automatizada para impedir propostas após o encerramento do processo.

Pedidos de Compra: Geração automática e síncrona do pedido a partir do orçamento aprovado, com regras rígidas de idempotência para evitar duplicidade (409 Conflict).

Gestão e Resiliência de Estoque: * Criação de fichas e movimentação de saldos físicos por meio de operações de soma e subtração auditáveis.

Resolução de Bug Crítico (Tipagem Alfanumérica): Refatoração no EstoqueService para o endpoint de busca por código do produto, implementando uma estratégia híbrida/defensiva que intercepta strings estruturadas (ex: PROD-INF-101) e resolve o ID numérico nativo via repositório, extinguindo falhas de NumberFormatException.

Métricas Gerenciais (Dashboard): Endpoints analíticos integrados para consolidação de dados em tempo real, fornecendo distribuição de requisições por status com cores hexadecimais nativas para componentes visuais e evolução mensal histórica de gastos.

🛠️ Tecnologias e Ferramentas
Backend (API)
Linguagem: Java 21

Framework: Spring Boot (v3.3.0) / Spring Security (JWT)

Banco de Dados: MySQL 8 / TiDB Cloud (NewSQL Serverless)

Controle de Evolução de Banco: Flyway Migrations

Mapeamento Objeto-Relacional: Spring Data JPA / Hibernate

Conversão de Objetos: ModelMapper DTOs

Gerenciador de Dependências: Maven

Documentação: Swagger UI / OpenAPI Spec v3.0

Frontend (Aplicação Web)
Ecossistema: React 18 / Vite

Linguagem: TypeScript

Estilização: TailwindCSS / Componentes Declarativos

Comunicação com a API: Axios (Interceptadores de Token)

Controle de Versão: Git

📂 Estrutura Estrutural do Repositório
Plaintext
ZLOG_COMPRAS/
├── backend/                  # API Spring Boot
│   ├── src/
│   │   ├── main/java/        # Camadas de Controller, Service, Repository, Entity, Config (Security) e DTOs
│   │   └── main/resources/   # application.properties e scripts Flyway (db/migration/)
│   └── pom.xml               # Configurações de build do Maven
└── frontend/                 # Aplicação Web React
    ├── src/                  # Páginas, Componentes de UI, hooks e serviços de API
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
Abra o arquivo backend/src/main/resources/application.properties e configure o apontamento para o seu banco de dados (seja local ou a string de conexão segura fornecida pelo painel do TiDB Cloud):

Properties
spring.datasource.url=jdbc:mysql://<SEU_HOST_TIDB_OU_LOCAL>:3306/zlog_compras?useSSL=true&serverTimezone=UTC
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=validate
Via terminal, entre na pasta do backend:

Bash
cd ZLOG_COMPRAS/backend
Execute o comando de compilação e inicialização do ecossistema Spring:

Bash
mvn clean install
mvn spring-boot:run
O Flyway varrerá o banco automaticamente, criando a estrutura de tabelas corrigida e inserindo as tabelas de controle e as Roles obrigatórias de segurança.

2. Execução do Frontend (React + Vite)
Abra uma nova janela de terminal e vá até a pasta do cliente:

Bash
cd ZLOG_COMPRAS/frontend
Instale os pacotes e dependências de Node declarados no projeto:

Bash
npm install
Inicie o servidor de desenvolvimento do Vite:

Bash
npm run dev
O console exibirá o endereço local para visualização (geralmente http://localhost:5173). Abra o link no seu navegador.

✅ Verification, Testes e Qualidade
Swagger UI Interativo: Explore os contratos e teste todas as rotas operacionais da API (solicitacao-compra, pedido-compra, estoque, dashboard) em tempo real acessando http://localhost:8080/swagger-ui.html com o backend em execução (lembre-se de incluir o token JWT gerado pelo auth-controller no botão Authorize).

Suíte de Testes Automatizados: Rode as validações de integridade executando:

Bash
mvn test
(Garantia de estabilidade com testes unitários focados nas entidades críticas rodando com 100% de sucesso).

Auditoria de Dados Consistente: Movimentações de entrada e saída de estoque geram carimbos automáticos de auditoria temporal (dataEntrada, dataAtualizacao) manipulados diretamente pela camada de persistência.

🤝 Contribuições e Feedbacks
Este repositório serve como portfólio prático de engenharia de software e modelagem de sistemas open-source. Pull Requests com refatorações, novos casos de testes ou novas views analíticas em React são super bem-vindos!

📝 Licença
Distribuído sob a licença MIT. Veja LICENSE para mais informações.
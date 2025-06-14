ZLOG Compras: Sistema de Gerenciamento de Compras Empresariais
🎯 Sobre o Projeto
O ZLOG Compras é um sistema robusto e modular desenvolvido para otimizar e gerenciar o fluxo completo de compras em um ambiente empresarial. Desde a criação de solicitações de materiais até a geração de pedidos de compra e o controle de recebimento, o ZLOG Compras busca automatizar e trazer transparência a todas as etapas do processo de aquisição.

Este projeto está sendo construído com foco em qualidade de código, documentação interativa e testes abrangentes, garantindo uma solução confiável e de fácil manutenção.

✨ Funcionalidades Desenvolvidas (Até o Momento)
As seguintes funcionalidades essenciais foram implementadas e validadas:

Gerenciamento de Solicitações de Compra: Criação, visualização e gestão de solicitações de materiais ou serviços.
Processo de Cotação e Orçamento:
Criação e gerenciamento de orçamentos para fornecedores.
Validação Crucial: Impedimento da criação de novos orçamentos para solicitações que já foram finalizadas, garantindo a integridade do processo.
Aprovação de orçamentos.
Geração de Pedidos de Compra:
Criação automatizada de pedidos de compra a partir de orçamentos aprovados.
Associação clara com fornecedores e itens do orçamento original.
Validações de Dados: Implementação de validações robustas para preços e quantidades nos itens de compra e orçamento, assegurando a qualidade dos dados desde a entrada.
Base para Recebimento de Itens: Estrutura inicial e endpoints definidos para futuras implementações de registro de recebimento de itens do pedido.
Atualização de Status do Pedido: Capacidade de atualizar o status do pedido de compra conforme o avanço do fluxo.
🛠️ Tecnologias e Ferramentas
Linguagem de Programação: Java 21
Framework: Spring Boot (v3.3.0)
Banco de Dados: MySQL
Persistência: Spring Data JPA / Hibernate
Ferramenta de Build: Maven
Documentação da API: Swagger UI / OpenAPI
Testes: JUnit (com 18 testes unitários para Fornecedores e Orçamentos, todos com 100% de sucesso!)
Controle de Versão: Git
🚀 Como Executar o Projeto
Pré-requisitos:
JDK 21 ou superior
Maven
Um servidor MySQL (certifique-se de configurar as credenciais do banco de dados no application.properties ou application.yml).
Clone o repositório:
Bash

git clone https://github.com/marciosouzagcm/ZLOG_COMPRAS.git
Navegue até o diretório do projeto:
Bash

cd ZLOG_COMPRAS
Construa o projeto (opcional, se não estiver usando IDE):
Bash

mvn clean install
Execute a aplicação:
Bash

mvn spring-boot:run
A aplicação estará disponível em http://localhost:8080 (ou na porta configurada).

📖 Documentação da API (Swagger UI)
Após iniciar a aplicação, você pode acessar a documentação interativa da API via Swagger UI em:
http://localhost:8080/swagger-ui.html

✅ Testes Unitários
Para executar os testes unitários do projeto, utilize o comando Maven:

Bash

mvn test
Este comando executará os testes e exibirá o relatório de sucesso ou falha no console.

🤝 Como Contribuir
Este projeto está em constante evolução e toda ajuda é bem-vinda! Se você é um desenvolvedor, estudante ou entusiasta de software, sinta-se à vontade para:

Explorar o Código: Entenda a estrutura e as implementações.
Abrir Issues: Reportar bugs, propor novas funcionalidades ou melhorias.
Submeter Pull Requests: Contribuir com código, testes, documentação ou refatorações.
Sua contribuição é valiosa para tornar o ZLOG Compras uma solução cada vez mais completa e eficiente!

📝 Licença - MIT License

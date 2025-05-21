Documentação da API de Produtos/Materiais
Este documento descreve as operações CRUD (Create, Read, Update, Delete) disponíveis para o recurso Produto (que representa materiais ou serviços) na API.

Base URL: http://localhost:8080/api/produtos

1. Criar Novo Produto (POST)
Cria um novo registro de produto ou material no sistema.

Tipo de Requisição: POST
URL: http://localhost:8080/api/produtos
Corpo da Requisição (JSON):
JSON

{
  "nome": "Televisão 42 polegadas plasma",
  "descricao": "Televisão 42 polegadas plasma, com garantia estendida.",
  "unidadeMedida": "unidade",
  "precoUnitario": 1000.99
}
Observações:
id e version não devem ser incluídos no payload, pois serão gerados automaticamente pelo sistema.
Retorno Esperado (Status 201 Created):
JSON

{
  "id": 3, // Exemplo: ID gerado automaticamente
  "nome": "Televisão 42 polegadas plasma",
  "descricao": "Televisão 42 polegadas plasma, com garantia estendida.",
  "unidadeMedida": "unidade",
  "precoUnitario": 1000.99,
  "version": 0
}
Em caso de erro: Retorna um status de erro (ex: 400 Bad Request se o payload for inválido ou 409 Conflict se o nome já existir, devido à restrição unique = true).
2. Listar Todos os Produtos (GET)
Recupera uma lista de todos os produtos ou materiais cadastrados.

Tipo de Requisição: GET
URL: http://localhost:8080/api/produtos
Corpo da Requisição (JSON): Não aplicável.
Retorno Esperado (Status 200 OK):
JSON

[
  {
    "id": 1,
    "nome": "Caneta Esferográfica",
    "descricao": "Caneta BIC Azul",
    "unidadeMedida": "unidade",
    "precoUnitario": 1.50,
    "version": 0
  },
  {
    "id": 2,
    "nome": "Caderno Universitário",
    "descricao": "Caderno de 10 matérias",
    "unidadeMedida": "unidade",
    "precoUnitario": 25.00,
    "version": 0
  },
  {
    "id": 3,
    "nome": "Televisão 42 polegadas plasma",
    "descricao": "Televisão 42 polegadas plasma, com garantia estendida.",
    "unidadeMedida": "unidade",
    "precoUnitario": 1000.99,
    "version": 0
  }
  // ... outros produtos
]
Em caso de erro: Retorna um status de erro (ex: 500 Internal Server Error).
3. Buscar Produto por ID (GET)
Recupera um produto ou material específico pelo seu ID.

Tipo de Requisição: GET
URL: http://localhost:8080/api/produtos/{id}
Substitua {id} pelo ID do produto desejado. Ex: http://localhost:8080/api/produtos/3
Corpo da Requisição (JSON): Não aplicável.
Retorno Esperado (Status 200 OK):
JSON

{
  "id": 3,
  "nome": "Televisão 42 polegadas plasma",
  "descricao": "Televisão 42 polegadas plasma, com garantia estendida.",
  "unidadeMedida": "unidade",
  "precoUnitario": 1000.99,
  "version": 0
}
Retorno Esperado (Status 404 Not Found):
Se o ID não corresponder a nenhum produto existente.
4. Atualizar Produto (PUT)
Atualiza um produto ou material existente, substituindo seus dados.

Tipo de Requisição: PUT
URL: http://localhost:8080/api/produtos/{id}
Substitua {id} pelo ID do produto a ser atualizado. Ex: http://localhost:8080/api/produtos/3
Corpo da Requisição (JSON):
JSON

{
  "id": 3,
  "nome": "Televisão 42 polegadas plasma (Atualizada)",
  "descricao": "Televisão 42 polegadas plasma, com garantia estendida e smartTV.",
  "unidadeMedida": "unidade",
  "precoUnitario": 1200.50,
  "version": 0 // Importante: Use a 'version' retornada na última leitura
}
Observações:
O id na URL deve corresponder ao id no corpo da requisição.
O campo version é crucial para o controle de concorrência otimista. Ele deve ser enviado de volta com o valor recebido na última leitura do recurso para evitar conflitos de atualização.
Retorno Esperado (Status 200 OK):
JSON

{
  "id": 3,
  "nome": "Televisão 42 polegadas plasma (Atualizada)",
  "descricao": "Televisão 42 polegadas plasma, com garantia estendida e smartTV.",
  "unidadeMedida": "unidade",
  "precoUnitario": 1200.50,
  "version": 1 // Exemplo: A 'version' é incrementada após a atualização
}
Retorno Esperado (Status 404 Not Found):
Se o ID na URL não corresponder a nenhum produto existente.
Retorno Esperado (Status 409 Conflict):
Se a version enviada no payload não corresponder à versão atual do recurso no banco de dados (erro de concorrência).
5. Deletar Produto (DELETE)
Deleta um produto ou material existente pelo seu ID.

Tipo de Requisição: DELETE
URL: http://localhost:8080/api/produtos/{id}
Substitua {id} pelo ID do produto a ser deletado. Ex: http://localhost:8080/api/produtos/3
Corpo da Requisição (JSON): Não aplicável.
Retorno Esperado (Status 204 No Content):
Indica que o produto foi deletado com sucesso e não há conteúdo para retornar.
Retorno Esperado (Status 404 Not Found):
Se o ID não corresponder a nenhum produto existente para deleção.
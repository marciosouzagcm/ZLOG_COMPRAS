Documentação da API de Solicitações de Compra

Este documento descreve as operações CRUD (Create, Read, Update, Delete) disponíveis para o recurso SolicitacaoCompra na API.

Base URL: http://localhost:8080/api/solicitacoes

1. Criar Nova Solicitação de Compra (POST)
Cria uma nova solicitação de compra com seus respectivos itens.

Tipo de Requisição: POST
URL: http://localhost:8080/api/solicitacoes
Corpo da Requisição (JSON):
JSON

{
  "solicitante": "João Silva",
  "status": "Pendente",
  "itens": [
    {
      "materialServico": "Caneta Esferográfica",
      "descricao": "Caneta BIC Azul",
      "quantidade": 10,
      "status": "Pendente"
    },
    {
      "materialServico": "Caderno Universitário",
      "descricao": "Caderno de 10 matérias",
      "quantidade": 2,
      "status": "Pendente"
    }
  ]
}
Observações:
id e version não devem ser incluídos no payload para a SolicitacaoCompra ou seus itens, pois serão gerados automaticamente pelo sistema.
dataSolicitacao também será gerada automaticamente na criação (geralmente como a data atual).
Retorno Esperado (Status 201 Created):
JSON

{
  "id": 1, // Exemplo: ID gerado automaticamente
  "dataSolicitacao": "2025-05-21",
  "solicitante": "João Silva",
  "status": "Pendente",
  "version": 0,
  "itens": [
    {
      "id": 101, // Exemplo: ID gerado automaticamente
      "materialServico": "Caneta Esferográfica",
      "descricao": "Caneta BIC Azul",
      "quantidade": 10,
      "status": "Pendente",
      "version": 0
    },
    {
      "id": 102, // Exemplo: ID gerado automaticamente
      "materialServico": "Caderno Universitário",
      "descricao": "Caderno de 10 matérias",
      "quantidade": 2,
      "status": "Pendente",
      "version": 0
    }
  ]
}
Em caso de erro: Retorna um status de erro (ex: 400 Bad Request se o payload for inválido, 500 Internal Server Error).
2. Listar Todas as Solicitações de Compra (GET)
Recupera uma lista de todas as solicitações de compra existentes.

Tipo de Requisição: GET
URL: http://localhost:8080/api/solicitacoes
Corpo da Requisição (JSON): Não aplicável.
Retorno Esperado (Status 200 OK):
JSON

[
  {
    "id": 1,
    "dataSolicitacao": "2025-05-21",
    "solicitante": "João Silva",
    "status": "Pendente",
    "version": 0,
    "itens": [
      {
        "id": 101,
        "materialServico": "Caneta Esferográfica",
        "descricao": "Caneta BIC Azul",
        "quantidade": 10,
        "status": "Pendente",
        "version": 0
      },
      {
        "id": 102,
        "materialServico": "Caderno Universitário",
        "descricao": "Caderno de 10 matérias",
        "quantidade": 2,
        "status": "Pendente",
        "version": 0
      }
    ]
  },
  {
    "id": 27,
    "dataSolicitacao": "2025-05-21",
    "solicitante": "Beatriz Segal Loiola",
    "status": "Pendente",
    "version": 0,
    "itens": [
      {
        "id": 68,
        "materialServico": "Lapis de Cor",
        "quantidade": 10,
        "descricao": "caixa com diversas cores, minimo 24 cores",
        "status": "Pendente",
        "version": 0
      },
      {
        "id": 69,
        "materialServico": "Caderno Broxura",
        "quantidade": 2,
        "descricao": "Caderno de 20 matérias",
        "status": "Pendente",
        "version": 0
      },
      {
        "id": 70, // Exemplo: ID de item novo após um PUT
        "materialServico": "Caneta marca texto",
        "quantidade": 8,
        "descricao": "CAneta marca texto verde florescente",
        "status": "Pendente",
        "version": 0
      }
    ]
  }
  // ... outras solicitações
]
Em caso de erro: Retorna um status de erro (ex: 500 Internal Server Error).
3. Buscar Solicitação de Compra por ID (GET)
Recupera uma solicitação de compra específica pelo seu ID.

Tipo de Requisição: GET
URL: http://localhost:8080/api/solicitacoes/{id}
Substitua {id} pelo ID da solicitação desejada. Ex: http://localhost:8080/api/solicitacoes/27
Corpo da Requisição (JSON): Não aplicável.
Retorno Esperado (Status 200 OK):
JSON

{
  "id": 27,
  "dataSolicitacao": "2025-05-21",
  "solicitante": "Beatriz Segal Loiola",
  "status": "Pendente",
  "version": 0,
  "itens": [
    {
      "id": 68,
      "materialServico": "Lapis de Cor",
      "quantidade": 10,
      "descricao": "caixa com diversas cores, minimo 24 cores",
      "status": "Pendente",
      "version": 0
    },
    {
      "id": 69,
      "materialServico": "Caderno Broxura",
      "quantidade": 2,
      "descricao": "Caderno de 20 matérias",
      "status": "Pendente",
      "version": 0
    },
    {
      "id": 70,
      "materialServico": "Caneta marca texto",
      "quantidade": 8,
      "descricao": "CAneta marca texto verde florescente",
      "status": "Pendente",
      "version": 0
    }
  ]
}
Retorno Esperado (Status 404 Not Found):
Se o ID não corresponder a nenhuma solicitação existente.
Exemplo de corpo vazio: {} ou resposta padrão do Spring Boot para 404.
4. Atualizar Solicitação de Compra (PUT)
Atualiza uma solicitação de compra existente, substituindo seus dados e itens.

Tipo de Requisição: PUT
URL: http://localhost:8080/api/solicitacoes/{id}
Substitua {id} pelo ID da solicitação a ser atualizada. Ex: http://localhost:8080/api/solicitacoes/27
Corpo da Requisição (JSON):
JSON

{
  "id": 27,
  "dataSolicitacao": "2025-05-21",
  "solicitante": "Beatriz Segal Loiola",
  "status": "Pendente",
  "version": 0,
  "itens": [
      {
          "id": 68,
          "materialServico": "Lapis de Cor",
          "quantidade": 10,
          "descricao": "caixa com diversas cores, minimo 24 cores",
          "status": "Pendente",
          "version": 0
      },
      {
          "id": 69,
          "materialServico": "Caderno Broxura",
          "quantidade": 2,
          "descricao": "Caderno de 20 matérias",
          "status": "Pendente",
          "version": 0
      },
      {
          "materialServico": "Caneta marca texto",
          "quantidade": 8,
          "descricao": "CAneta marca texto verde florescente",
          "status": "Pendente",
          "version": 0
      }
  ]
}
Observações:
O id na URL deve corresponder ao id no corpo da requisição para a SolicitacaoCompra.
Para os itens:
Itens com id existente no payload serão atualizados.
Itens sem id no payload serão considerados novos e adicionados à solicitação.
Itens que existiam na solicitação original mas não estão presentes no novo payload serão removidos (devido ao orphanRemoval = true).
O campo version é importante para o controle de concorrência (versão otimista). Ele deve ser enviado de volta com o valor recebido na última leitura para evitar conflitos de atualização.
Retorno Esperado (Status 200 OK):
JSON

{
  "id": 27,
  "dataSolicitacao": "2025-05-21",
  "solicitante": "Beatriz Segal Loiola",
  "status": "Pendente",
  "version": 1, // Versão atualizada após o PUT
  "itens": [
    {
      "id": 68,
      "materialServico": "Lapis de Cor",
      "quantidade": 10,
      "descricao": "caixa com diversas cores, minimo 24 cores",
      "status": "Pendente",
      "version": 1 // Versão atualizada
    },
    {
      "id": 69,
      "materialServico": "Caderno Broxura",
      "quantidade": 2,
      "descricao": "Caderno de 20 matérias",
      "status": "Pendente",
      "version": 1 // Versão atualizada
    },
    {
      "id": 70, // ID gerado para o novo item
      "materialServico": "Caneta marca texto",
      "quantidade": 8,
      "descricao": "CAneta marca texto verde florescente",
      "status": "Pendente",
      "version": 0 // Nova versão para item novo
    }
  ]
}
Retorno Esperado (Status 404 Not Found):
Se o ID na URL não corresponder a nenhuma solicitação existente.
Retorno Esperado (Status 400 Bad Request):
Se houver inconsistência entre o ID da URL e o ID do payload (se essa validação for reintroduzida no controller).
Se o payload for inválido.
5. Deletar Solicitação de Compra (DELETE)
Deleta uma solicitação de compra existente pelo seu ID.

Tipo de Requisição: DELETE
URL: http://localhost:8080/api/solicitacoes/{id}
Substitua {id} pelo ID da solicitação a ser deletada. Ex: http://localhost:8080/api/solicitacoes/27
Corpo da Requisição (JSON): Não aplicável.
Retorno Esperado (Status 204 No Content):
Indica que a solicitação foi deletada com sucesso e não há conteúdo para retornar.
Retorno Esperado (Status 404 Not Found):
Se o ID não corresponder a nenhuma solicitação existente.
Documentacão da API ZLOG COMPRAS

Esta documentação detalha os endpoints RESTful da aplicação ZLOG Compras, que foi iniciada e está disponível em http://localhost:8080.

1. Módulos e Controladores
A API é organizada pelos seguintes controladores:

EstoqueController
FornecedorController
OrcamentoController
PedidoCompraController
ProcessoCompraController
ProdutoController
SolicitacaoCompraController
2. EstoqueController
Gerencia as operações relacionadas ao estoque de produtos.

2.1. Listar Todos os Estoques
Retorna uma lista de todos os estoques registrados.

Endpoint: GET /api/estoques
Descrição: Recupera todos os registros de estoque.
Requisição:
GET http://localhost:8080/api/estoques
Exemplo de Resposta (JSON):
JSON

[
  {
    "id": 1,
    "produto": {
      "id": 101,
      "nomeProduto": "Caneta Esferográfica",
      "codigoProduto": "CANE001",
      "unidadeMedidaProduto": "UNIDADE"
    },
    "quantidadeAtual": 150,
    "localizacao": "Prateleira A1"
  },
  {
    "id": 2,
    "produto": {
      "id": 102,
      "nomeProduto": "Caderno Universitário",
      "codigoProduto": "CADE001",
      "unidadeMedidaProduto": "UNIDADE"
    },
    "quantidadeAtual": 75,
    "localizacao": "Armário B2"
  }
]
2.2. Buscar Estoque por ID
Retorna um estoque específico pelo seu ID.

Endpoint: GET /api/estoques/{id}
Descrição: Recupera um registro de estoque usando seu ID.
Parâmetros de Path:
id (Long): O ID do estoque.
Requisição:
GET http://localhost:8080/api/estoques/1
Exemplo de Resposta (JSON):
JSON

{
  "id": 1,
  "produto": {
    "id": 101,
    "nomeProduto": "Caneta Esferográfica",
    "codigoProduto": "CANE001",
    "unidadeMedidaProduto": "UNIDADE"
  },
  "quantidadeAtual": 150,
  "localizacao": "Prateleira A1"
}
2.3. Buscar Estoque por Código do Produto
Retorna um estoque específico pelo código do produto.

Endpoint: GET /api/estoques/produto/{codigoProduto}
Descrição: Recupera um registro de estoque associado a um código de produto específico.
Parâmetros de Path:
codigoProduto (String): O código do produto.
Requisição:
GET http://localhost:8080/api/estoques/produto/CANE001
Exemplo de Resposta (JSON):
JSON

{
  "id": 1,
  "produto": {
    "id": 101,
    "nomeProduto": "Caneta Esferográfica",
    "codigoProduto": "CANE001",
    "unidadeMedidaProduto": "UNIDADE"
  },
  "quantidadeAtual": 150,
  "localizacao": "Prateleira A1"
}
2.4. Criar Estoque
Cria um novo registro de estoque.

Endpoint: POST /api/estoques
Descrição: Adiciona um novo item de estoque.
Corpo da Requisição (JSON - EstoqueRequestDTO):
JSON

{
  "produtoId": 103,
  "quantidadeAtual": 200,
  "localizacao": "Armazém Principal"
}
Requisição:
POST http://localhost:8080/api/estoques
Content-Type: application/json

{
  "produtoId": 103,
  "quantidadeAtual": 200,
  "localizacao": "Armazém Principal"
}
Exemplo de Resposta (JSON - 201 Created):
JSON

{
  "id": 3,
  "produto": {
    "id": 103,
    "nomeProduto": "Borracha",
    "codigoProduto": "BORA001",
    "unidadeMedidaProduto": "UNIDADE"
  },
  "quantidadeAtual": 200,
  "localizacao": "Armazém Principal"
}
2.5. Criar Múltiplos Estoques
Cria múltiplos registros de estoque em uma única requisição.

Endpoint: POST /api/estoques/batch
Descrição: Adiciona vários itens de estoque em lote.
Corpo da Requisição (JSON - List&lt;EstoqueRequestDTO>):
JSON

[
  {
    "produtoId": 104,
    "quantidadeAtual": 50,
    "localizacao": "Setor C"
  },
  {
    "produtoId": 105,
    "quantidadeAtual": 120,
    "localizacao": "Setor D"
  }
]
Requisição:
POST http://localhost:8080/api/estoques/batch
Content-Type: application/json

[
  {
    "produtoId": 104,
    "quantidadeAtual": 50,
    "localizacao": "Setor C"
  },
  {
    "produtoId": 105,
    "quantidadeAtual": 120,
    "localizacao": "Setor D"
  }
]
Exemplo de Resposta (JSON - 201 Created):
JSON

[
  {
    "id": 4,
    "produto": {
      "id": 104,
      "nomeProduto": "Clips de Papel",
      "codigoProduto": "CLIP001",
      "unidadeMedidaProduto": "CAIXA"
    },
    "quantidadeAtual": 50,
    "localizacao": "Setor C"
  },
  {
    "id": 5,
    "produto": {
      "id": 105,
      "nomeProduto": "Pasta L",
      "codigoProduto": "PAST001",
      "unidadeMedidaProduto": "PACOTE"
    },
    "quantidadeAtual": 120,
    "localizacao": "Setor D"
  }
]
2.6. Atualizar Estoque
Atualiza um registro de estoque existente pelo seu ID.

Endpoint: PUT /api/estoques/{id}
Descrição: Atualiza as informações de um estoque existente.
Parâmetros de Path:
id (Long): O ID do estoque a ser atualizado.
Corpo da Requisição (JSON - EstoqueRequestDTO):
JSON

{
  "produtoId": 101,
  "quantidadeAtual": 180,
  "localizacao": "Prateleira A1 Nova"
}
Requisição:
PUT http://localhost:8080/api/estoques/1
Content-Type: application/json

{
  "produtoId": 101,
  "quantidadeAtual": 180,
  "localizacao": "Prateleira A1 Nova"
}
Exemplo de Resposta (JSON - 200 OK):
JSON

{
  "id": 1,
  "produto": {
    "id": 101,
    "nomeProduto": "Caneta Esferográfica",
    "codigoProduto": "CANE001",
    "unidadeMedidaProduto": "UNIDADE"
  },
  "quantidadeAtual": 180,
  "localizacao": "Prateleira A1 Nova"
}
2.7. Adicionar Quantidade ao Estoque
Adiciona uma quantidade específica a um estoque existente.

Endpoint: PUT /api/estoques/{id}/adicionar-quantidade
Descrição: Adiciona uma determinada quantidade ao estoque de um item.
Parâmetros de Path:
id (Long): O ID do estoque.
Corpo da Requisição (JSON - EstoqueMovimentacaoDTO):
JSON

{
  "quantidade": 20
}
Requisição:
PUT http://localhost:8080/api/estoques/1/adicionar-quantidade
Content-Type: application/json

{
  "quantidade": 20
}
Exemplo de Resposta (JSON - 200 OK):
JSON

{
  "id": 1,
  "produto": {
    "id": 101,
    "nomeProduto": "Caneta Esferográfica",
    "codigoProduto": "CANE001",
    "unidadeMedidaProduto": "UNIDADE"
  },
  "quantidadeAtual": 200,
  "localizacao": "Prateleira A1 Nova"
}
2.8. Retirar Quantidade do Estoque
Retira uma quantidade específica de um estoque existente.

Endpoint: PUT /api/estoques/{id}/retirar-quantidade
Descrição: Retira uma determinada quantidade do estoque de um item.
Parâmetros de Path:
id (Long): O ID do estoque.
Corpo da Requisição (JSON - EstoqueMovimentacaoDTO):
JSON

{
  "quantidade": 50
}
Requisição:
PUT http://localhost:8080/api/estoques/1/retirar-quantidade
Content-Type: application/json

{
  "quantidade": 50
}
Exemplo de Resposta (JSON - 200 OK):
JSON

{
  "id": 1,
  "produto": {
    "id": 101,
    "nomeProduto": "Caneta Esferográfica",
    "codigoProduto": "CANE001",
    "unidadeMedidaProduto": "UNIDADE"
  },
  "quantidadeAtual": 150,
  "localizacao": "Prateleira A1 Nova"
}
2.9. Deletar Estoque
Deleta um registro de estoque pelo seu ID.

Endpoint: DELETE /api/estoques/{id}
Descrição: Remove um registro de estoque.
Parâmetros de Path:
id (Long): O ID do estoque a ser deletado.
Requisição:
DELETE http://localhost:8080/api/estoques/2
Exemplo de Resposta: 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).
3. FornecedorController
Gerencia as operações relacionadas aos fornecedores.

3.1. Listar Todos os Fornecedores
Retorna uma lista de todos os fornecedores registrados.

Endpoint: GET /api/fornecedores
Descrição: Recupera todos os registros de fornecedores.
Requisição:
GET http://localhost:8080/api/fornecedores
Exemplo de Resposta (JSON):
JSON

[
  {
    "id": 1,
    "nome": "Fornecedor A",
    "cnpj": "11.222.333/0001-44",
    "email": "contato@fornecedorA.com",
    "telefone": "11987654321",
    "endereco": "Rua das Flores, 123"
  },
  {
    "id": 2,
    "nome": "Fornecedor B",
    "cnpj": "55.666.777/0001-88",
    "email": "vendas@fornecedorB.com",
    "telefone": "21998877665",
    "endereco": "Av. Principal, 456"
  }
]
3.2. Buscar Fornecedor por ID
Retorna um fornecedor específico pelo seu ID.

Endpoint: GET /api/fornecedores/{id}
Descrição: Recupera um registro de fornecedor usando seu ID.
Parâmetros de Path:
id (Long): O ID do fornecedor.
Requisição:
GET http://localhost:8080/api/fornecedores/1
Exemplo de Resposta (JSON):
JSON

{
  "id": 1,
  "nome": "Fornecedor A",
  "cnpj": "11.222.333/0001-44",
  "email": "contato@fornecedorA.com",
  "telefone": "11987654321",
  "endereco": "Rua das Flores, 123"
}
3.3. Criar Fornecedor
Cria um novo registro de fornecedor.

Endpoint: POST /api/fornecedores
Descrição: Adiciona um novo fornecedor.
Corpo da Requisição (JSON - FornecedorRequestDTO):
JSON

{
  "nome": "Fornecedor C",
  "cnpj": "99.888.777/0001-00",
  "email": "suporte@fornecedorC.com",
  "telefone": "31912345678",
  "endereco": "Travessa dos Pardais, 789"
}
Requisição:
POST http://localhost:8080/api/fornecedores
Content-Type: application/json

{
  "nome": "Fornecedor C",
  "cnpj": "99.888.777/0001-00",
  "email": "suporte@fornecedorC.com",
  "telefone": "31912345678",
  "endereco": "Travessa dos Pardais, 789"
}
Exemplo de Resposta (JSON - 201 Created):
JSON

{
  "id": 3,
  "nome": "Fornecedor C",
  "cnpj": "99.888.777/0001-00",
  "email": "suporte@fornecedorC.com",
  "telefone": "31912345678",
  "endereco": "Travessa dos Pardais, 789"
}
3.4. Atualizar Fornecedor
Atualiza um registro de fornecedor existente pelo seu ID.

Endpoint: PUT /api/fornecedores/{id}
Descrição: Atualiza as informações de um fornecedor existente.
Parâmetros de Path:
id (Long): O ID do fornecedor a ser atualizado.
Corpo da Requisição (JSON - FornecedorRequestDTO):
JSON

{
  "nome": "Fornecedor A Atualizado",
  "cnpj": "11.222.333/0001-44",
  "email": "novo.contato@fornecedorA.com",
  "telefone": "11987654321",
  "endereco": "Rua das Flores, 123 - Novo"
}
Requisição:
PUT http://localhost:8080/api/fornecedores/1
Content-Type: application/json

{
  "nome": "Fornecedor A Atualizado",
  "cnpj": "11.222.333/0001-44",
  "email": "novo.contato@fornecedorA.com",
  "telefone": "11987654321",
  "endereco": "Rua das Flores, 123 - Novo"
}
Exemplo de Resposta (JSON - 200 OK):
JSON

{
  "id": 1,
  "nome": "Fornecedor A Atualizado",
  "cnpj": "11.222.333/0001-44",
  "email": "novo.contato@fornecedorA.com",
  "telefone": "11987654321",
  "endereco": "Rua das Flores, 123 - Novo"
}
3.5. Deletar Fornecedor
Deleta um registro de fornecedor pelo seu ID.

Endpoint: DELETE /api/fornecedores/{id}
Descrição: Remove um registro de fornecedor.
Parâmetros de Path:
id (Long): O ID do fornecedor a ser deletado.
Requisição:
DELETE http://localhost:8080/api/fornecedores/2
Exemplo de Resposta: 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).
4. OrcamentoController
Gerencia as operações relacionadas aos orçamentos.

4.1. Listar Todos os Orçamentos
Retorna uma lista de todos os orçamentos registrados.

Endpoint: GET /api/orcamentos
Descrição: Recupera todos os registros de orçamentos.
Requisição:
GET http://localhost:8080/api/orcamentos
Exemplo de Resposta (JSON):
JSON

[
  {
    "id": 1,
    "solicitacaoCompra": {
      "id": 1,
      "departamento": "TI",
      "dataSolicitacao": "2025-05-20",
      "status": "PENDENTE_ORCAMENTO"
    },
    "fornecedor": {
      "id": 1,
      "nome": "Fornecedor A",
      "cnpj": "11.222.333/0001-44"
    },
    "dataCotacao": "2025-05-22",
    "valorTotal": 500.00,
    "status": "AGUARDANDO_APROVACAO",
    "itensOrcamento": [
      {
        "id": 10,
        "produto": {
          "id": 101,
          "nomeProduto": "Caneta Esferográfica",
          "codigoProduto": "CANE001",
          "unidadeMedidaProduto": "UNIDADE"
        },
        "quantidade": 100,
        "valorUnitario": 2.50,
        "valorTotalItem": 250.00
      }
    ]
  }
]
4.2. Buscar Orçamento por ID
Retorna um orçamento específico pelo seu ID.

Endpoint: GET /api/orcamentos/{id}
Descrição: Recupera um registro de orçamento usando seu ID.
Parâmetros de Path:
id (Long): O ID do orçamento.
Requisição:
GET http://localhost:8080/api/orcamentos/1
Exemplo de Resposta (JSON):
JSON

{
  "id": 1,
  "solicitacaoCompra": {
    "id": 1,
    "departamento": "TI",
    "dataSolicitacao": "2025-05-20",
    "status": "PENDENTE_ORCAMENTO"
  },
  "fornecedor": {
    "id": 1,
    "nome": "Fornecedor A",
    "cnpj": "11.222.333/0001-44"
  },
  "dataCotacao": "2025-05-22",
  "valorTotal": 500.00,
  "status": "AGUARDANDO_APROVACAO",
  "itensOrcamento": [
    {
      "id": 10,
      "produto": {
        "id": 101,
        "nomeProduto": "Caneta Esferográfica",
        "codigoProduto": "CANE001",
        "unidadeMedidaProduto": "UNIDADE"
      },
      "quantidade": 100,
      "valorUnitario": 2.50,
      "valorTotalItem": 250.00
    }
  ]
}
4.3. Criar Orçamento
Cria um novo registro de orçamento.

Endpoint: POST /api/orcamentos
Descrição: Adiciona um novo orçamento.
Corpo da Requisição (JSON - OrcamentoRequestDTO):
JSON

{
  "solicitacaoCompraId": 2,
  "fornecedorId": 2,
  "dataCotacao": "2025-06-03",
  "itensOrcamento": [
    {
      "produtoId": 102,
      "quantidade": 50,
      "valorUnitario": 10.00
    },
    {
      "produtoId": 103,
      "quantidade": 20,
      "valorUnitario": 5.00
    }
  ]
}
Requisição:
POST http://localhost:8080/api/orcamentos
Content-Type: application/json

{
  "solicitacaoCompraId": 2,
  "fornecedorId": 2,
  "dataCotacao": "2025-06-03",
  "itensOrcamento": [
    {
      "produtoId": 102,
      "quantidade": 50,
      "valorUnitario": 10.00
    },
    {
      "produtoId": 103,
      "quantidade": 20,
      "valorUnitario": 5.00
    }
  ]
}
Exemplo de Resposta (JSON - 201 Created):
JSON

{
  "id": 2,
  "solicitacaoCompra": {
    "id": 2,
    "departamento": "Compras",
    "dataSolicitacao": "2025-06-01",
    "status": "PENDENTE_ORCAMENTO"
  },
  "fornecedor": {
    "id": 2,
    "nome": "Fornecedor B",
    "cnpj": "55.666.777/0001-88"
  },
  "dataCotacao": "2025-06-03",
  "valorTotal": 600.00,
  "status": "AGUARDANDO_APROVACAO",
  "itensOrcamento": [
    {
      "id": 11,
      "produto": {
        "id": 102,
        "nomeProduto": "Caderno Universitário",
        "codigoProduto": "CADE001",
        "unidadeMedidaProduto": "UNIDADE"
      },
      "quantidade": 50,
      "valorUnitario": 10.00,
      "valorTotalItem": 500.00
    },
    {
      "id": 12,
      "produto": {
        "id": 103,
        "nomeProduto": "Borracha",
        "codigoProduto": "BORA001",
        "unidadeMedidaProduto": "UNIDADE"
      },
      "quantidade": 20,
      "valorUnitario": 5.00,
      "valorTotalItem": 100.00
    }
  ]
}
4.4. Atualizar Orçamento
Atualiza um registro de orçamento existente pelo seu ID.

Endpoint: PUT /api/orcamentos/{id}
Descrição: Atualiza as informações de um orçamento existente.
Parâmetros de Path:
id (Long): O ID do orçamento a ser atualizado.
Corpo da Requisição (JSON - OrcamentoRequestDTO):
JSON

{
  "solicitacaoCompraId": 1,
  "fornecedorId": 1,
  "dataCotacao": "2025-05-23",
  "itensOrcamento": [
    {
      "id": 10,
      "produtoId": 101,
      "quantidade": 120,
      "valorUnitario": 2.60
    }
  ]
}
Requisição:
PUT http://localhost:8080/api/orcamentos/1
Content-Type: application/json

{
  "solicitacaoCompraId": 1,
  "fornecedorId": 1,
  "dataCotacao": "2025-05-23",
  "itensOrcamento": [
    {
      "id": 10,
      "produtoId": 101,
      "quantidade": 120,
      "valorUnitario": 2.60
    }
  ]
}
Exemplo de Resposta (JSON - 200 OK):
JSON

{
  "id": 1,
  "solicitacaoCompra": {
    "id": 1,
    "departamento": "TI",
    "dataSolicitacao": "2025-05-20",
    "status": "PENDENTE_ORCAMENTO"
  },
  "fornecedor": {
    "id": 1,
    "nome": "Fornecedor A",
    "cnpj": "11.222.333/0001-44"
  },
  "dataCotacao": "2025-05-23",
  "valorTotal": 312.00,
  "status": "AGUARDANDO_APROVACAO",
  "itensOrcamento": [
    {
      "id": 10,
      "produto": {
        "id": 101,
        "nomeProduto": "Caneta Esferográfica",
        "codigoProduto": "CANE001",
        "unidadeMedidaProduto": "UNIDADE"
      },
      "quantidade": 120,
      "valorUnitario": 2.60,
      "valorTotalItem": 312.00
    }
  ]
}
4.5. Aprovar Orçamento
Aprova um orçamento específico pelo seu ID.

Endpoint: PATCH /api/orcamentos/{id}/aprovar
Descrição: Altera o status de um orçamento para APROVADO.
Parâmetros de Path:
id (Long): O ID do orçamento a ser aprovado.
Requisição:
PATCH http://localhost:8080/api/orcamentos/1/aprovar
Exemplo de Resposta (JSON - 200 OK):
JSON

{
  "id": 1,
  "solicitacaoCompra": {
    "id": 1,
    "departamento": "TI",
    "dataSolicitacao": "2025-05-20",
    "status": "PROCESSANDO_PEDIDO"
  },
  "fornecedor": {
    "id": 1,
    "nome": "Fornecedor A",
    "cnpj": "11.222.333/0001-44"
  },
  "dataCotacao": "2025-05-23",
  "valorTotal": 312.00,
  "status": "APROVADO",
  "itensOrcamento": [
    {
      "id": 10,
      "produto": {
        "id": 101,
        "nomeProduto": "Caneta Esferográfica",
        "codigoProduto": "CANE001",
        "unidadeMedidaProduto": "UNIDADE"
      },
      "quantidade": 120,
      "valorUnitario": 2.60,
      "valorTotalItem": 312.00
    }
  ]
}
4.6. Deletar Orçamento
Deleta um registro de orçamento pelo seu ID.

Endpoint: DELETE /api/orcamentos/{id}
Descrição: Remove um registro de orçamento.
Parâmetros de Path:
id (Long): O ID do orçamento a ser deletado.
Requisição:
DELETE http://localhost:8080/api/orcamentos/2
Exemplo de Resposta: 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).
5. PedidoCompraController
Gerencia as operações relacionadas aos pedidos de compra.

5.1. Listar Todos os Pedidos de Compra
Retorna uma lista de todos os pedidos de compra registrados.

Endpoint: GET /api/pedidos-compra
Descrição: Recupera todos os registros de pedidos de compra.
Requisição:
GET http://localhost:8080/api/pedidos-compra
Exemplo de Resposta (JSON):
JSON

[
  {
    "id": 1,
    "orcamento": {
      "id": 1,
      "valorTotal": 312.00,
      "fornecedor": {
        "id": 1,
        "nome": "Fornecedor A"
      }
    },
    "dataPedido": "2025-06-01",
    "status": "PROCESSANDO",
    "dataEntregaPrevista": "2025-06-10",
    "itensPedidoCompra": [
      {
        "id": 1,
        "produto": {
          "id": 101,
          "nomeProduto": "Caneta Esferográfica"
        },
        "quantidade": 120,
        "valorUnitario": 2.60
      }
    ]
  }
]
5.2. Buscar Pedido de Compra por ID
Retorna um pedido de compra específico pelo seu ID.

Endpoint: GET /api/pedidos-compra/{id}
Descrição: Recupera um registro de pedido de compra usando seu ID.
Parâmetros de Path:
id (Long): O ID do pedido de compra.
Requisição:
GET http://localhost:8080/api/pedidos-compra/1
Exemplo de Resposta (JSON):
JSON

{
  "id": 1,
  "orcamento": {
    "id": 1,
    "valorTotal": 312.00,
    "fornecedor": {
      "id": 1,
      "nome": "Fornecedor A"
    }
  },
  "dataPedido": "2025-06-01",
  "status": "PROCESSANDO",
  "dataEntregaPrevista": "2025-06-10",
  "itensPedidoCompra": [
    {
      "id": 1,
      "produto": {
        "id": 101,
        "nomeProduto": "Caneta Esferográfica"
      },
      "quantidade": 120,
      "valorUnitario": 2.60
    }
  ]
}
5.3. Criar Pedido de Compra
Cria um novo registro de pedido de compra.

Endpoint: POST /api/pedidos-compra
Descrição: Adiciona um novo pedido de compra.
Corpo da Requisição (JSON - PedidoCompra):
JSON

{
  "orcamentoId": 1,
  "dataPedido": "2025-06-03",
  "dataEntregaPrevista": "2025-06-15"
}
Requisição:
POST http://localhost:8080/api/pedidos-compra
Content-Type: application/json

{
  "orcamentoId": 1,
  "dataPedido": "2025-06-03",
  "dataEntregaPrevista": "2025-06-15"
}
Exemplo de Resposta (JSON - 201 Created):
JSON

{
  "id": 2,
  "orcamento": {
    "id": 1,
    "valorTotal": 312.00,
    "fornecedor": {
      "id": 1,
      "nome": "Fornecedor A"
    }
  },
  "dataPedido": "2025-06-03",
  "status": "PROCESSANDO",
  "dataEntregaPrevista": "2025-06-15",
  "itensPedidoCompra": [
    {
      "id": 2,
      "produto": {
        "id": 101,
        "nomeProduto": "Caneta Esferográfica"
      },
      "quantidade": 120,
      "valorUnitario": 2.60
    }
  ]
}
5.4. Atualizar Pedido de Compra
Atualiza um registro de pedido de compra existente pelo seu ID.

Endpoint: PUT /api/pedidos-compra/{id}
Descrição: Atualiza as informações de um pedido de compra existente.
Parâmetros de Path:
id (Long): O ID do pedido de compra a ser atualizado.
Corpo da Requisição (JSON - PedidoCompra):
JSON

{
  "orcamentoId": 1,
  "dataPedido": "2025-06-01",
  "status": "EM_TRANSITO",
  "dataEntregaPrevista": "2025-06-12"
}
Requisição:
PUT http://localhost:8080/api/pedidos-compra/1
Content-Type: application/json

{
  "orcamentoId": 1,
  "dataPedido": "2025-06-01",
  "status": "EM_TRANSITO",
  "dataEntregaPrevista": "2025-06-12"
}
Exemplo de Resposta (JSON - 200 OK):
JSON

{
  "id": 1,
  "orcamento": {
    "id": 1,
    "valorTotal": 312.00,
    "fornecedor": {
      "id": 1,
      "nome": "Fornecedor A"
    }
  },
  "dataPedido": "2025-06-01",
  "status": "EM_TRANSITO",
  "dataEntregaPrevista": "2025-06-12",
  "itensPedidoCompra": [
    {
      "id": 1,
      "produto": {
        "id": 101,
        "nomeProduto": "Caneta Esferográfica"
      },
      "quantidade": 120,
      "valorUnitario": 2.60
    }
  ]
}
5.5. Atualizar Status do Pedido de Compra
Atualiza o status de um pedido de compra específico pelo seu ID.

Endpoint: PUT /api/pedidos-compra/{id}/status
Descrição: Altera o status de um pedido de compra.
Parâmetros de Path:
id (Long): O ID do pedido de compra.
Parâmetros de Query:
status (String): O novo status do pedido (ex: CONCLUIDO, CANCELADO).
Requisição:
PUT http://localhost:8080/api/pedidos-compra/1/status?status=CONCLUIDO
Exemplo de Resposta (JSON - 200 OK):
JSON

{
  "id": 1,
  "orcamento": {
    "id": 1,
    "valorTotal": 312.00,
    "fornecedor": {
      "id": 1,
      "nome": "Fornecedor A"
    }
  },
  "dataPedido": "2025-06-01",
  "status": "CONCLUIDO",
  "dataEntregaPrevista": "2025-06-12",
  "itensPedidoCompra": [
    {
      "id": 1,
      "produto": {
        "id": 101,
        "nomeProduto": "Caneta Esferográfica"
      },
      "quantidade": 120,
      "valorUnitario": 2.60
    }
  ]
}
6. ProcessoCompraController
Gerencia o início de um processo de compra.

6.1. Iniciar Processo de Compra
Inicia um novo processo de compra a partir de uma solicitação de compra.

Endpoint: POST /api/processo-compra/iniciar/{solicitacaoId}
Descrição: Inicia todo o fluxo de processo de compra para uma solicitação específica.
Parâmetros de Path:
solicitacaoId (Long): O ID da solicitação de compra para iniciar o processo.
Requisição:
POST http://localhost:8080/api/processo-compra/iniciar/1
Exemplo de Resposta (JSON - 200 OK):
JSON

{
  "mensagem": "Processo de compra iniciado para solicitação com ID: 1. Orçamentos criados e aguardando aprovação."
}
(Nota: A resposta real pode variar dependendo da lógica interna do serviço, mas uma mensagem de sucesso seria esperada.)
7. ProdutoController
Gerencia as operações relacionadas aos produtos.

7.1. Listar Todos os Produtos
Retorna uma lista de todos os produtos registrados.

Endpoint: GET /api/produtos
Descrição: Recupera todos os registros de produtos.
Requisição:
GET http://localhost:8080/api/produtos
Exemplo de Resposta (JSON):
JSON

[
  {
    "id": 1,
    "nomeProduto": "Caneta Esferográfica Azul",
    "codigoProduto": "CANE001",
    "unidadeMedidaProduto": "UNIDADE"
  },
  {
    "id": 2,
    "nomeProduto": "Caderno Universitário 10 Matérias",
    "codigoProduto": "CADE001",
    "unidadeMedidaProduto": "UNIDADE"
  }
]
7.2. Buscar Produto por ID
Retorna um produto específico pelo seu ID.

Endpoint: GET /api/produtos/{id}
Descrição: Recupera um registro de produto usando seu ID.
Parâmetros de Path:
id (Long): O ID do produto.
Requisição:
GET http://localhost:8080/api/produtos/1
Exemplo de Resposta (JSON):
JSON

{
  "id": 1,
  "nomeProduto": "Caneta Esferográfica Azul",
  "codigoProduto": "CANE001",
  "unidadeMedidaProduto": "UNIDADE"
}
7.3. Criar Produto
Cria um novo registro de produto.

Endpoint: POST /api/produtos
Descrição: Adiciona um novo produto.
Corpo da Requisição (JSON - ProdutoRequestDTO):
JSON

{
  "nomeProduto": "Lápis Preto HB",
  "codigoProduto": "LAPI001",
  "unidadeMedidaProduto": "UNIDADE"
}
Requisição:
POST http://localhost:8080/api/produtos
Content-Type: application/json

{
  "nomeProduto": "Lápis Preto HB",
  "codigoProduto": "LAPI001",
  "unidadeMedidaProduto": "UNIDADE"
}
Exemplo de Resposta (JSON - 201 Created):
JSON

{
  "id": 3,
  "nomeProduto": "Lápis Preto HB",
  "codigoProduto": "LAPI001",
  "unidadeMedidaProduto": "UNIDADE"
}
7.4. Criar Múltiplos Produtos
Cria múltiplos registros de produto em uma única requisição.

Endpoint: POST /api/produtos/multiplos
Descrição: Adiciona vários produtos em lote.
Corpo da Requisição (JSON - List&lt;ProdutoRequestDTO>):
JSON

[
  {
    "nomeProduto": "Grampeador Pequeno",
    "codigoProduto": "GRAM001",
    "unidadeMedidaProduto": "UNIDADE"
  },
  {
    "nomeProduto": "Caixa de Clipes",
    "codigoProduto": "CLIPES001",
    "unidadeMedidaProduto": "CAIXA"
  }
]
Requisição:
POST http://localhost:8080/api/produtos/multiplos
Content-Type: application/json

[
  {
    "nomeProduto": "Grampeador Pequeno",
    "codigoProduto": "GRAM001",
    "unidadeMedidaProduto": "UNIDADE"
  },
  {
    "nomeProduto": "Caixa de Clipes",
    "codigoProduto": "CLIPES001",
    "unidadeMedidaProduto": "CAIXA"
  }
]
Exemplo de Resposta (JSON - 201 Created):
JSON

[
  {
    "id": 4,
    "nomeProduto": "Grampeador Pequeno",
    "codigoProduto": "GRAM001",
    "unidadeMedidaProduto": "UNIDADE"
  },
  {
    "id": 5,
    "nomeProduto": "Caixa de Clipes",
    "codigoProduto": "CLIPES001",
    "unidadeMedidaProduto": "CAIXA"
  }
]
7.5. Atualizar Produto
Atualiza um registro de produto existente pelo seu ID.

Endpoint: PUT /api/produtos/{id}
Descrição: Atualiza as informações de um produto existente.
Parâmetros de Path:
id (Long): O ID do produto a ser atualizado.
Corpo da Requisição (JSON - ProdutoRequestDTO):
JSON

{
  "nomeProduto": "Caneta Esferográfica Preta",
  "codigoProduto": "CANE001",
  "unidadeMedidaProduto": "UNIDADE"
}
Requisição:
PUT http://localhost:8080/api/produtos/1
Content-Type: application/json

{
  "nomeProduto": "Caneta Esferográfica Preta",
  "codigoProduto": "CANE001",
  "unidadeMedidaProduto": "UNIDADE"
}
Exemplo de Resposta (JSON - 200 OK):
JSON

{
  "id": 1,
  "nomeProduto": "Caneta Esferográfica Preta",
  "codigoProduto": "CANE001",
  "unidadeMedidaProduto": "UNIDADE"
}
7.6. Deletar Produto
Deleta um registro de produto pelo seu ID.

Endpoint: DELETE /api/produtos/{id}
Descrição: Remove um registro de produto.
Parâmetros de Path:
id (Long): O ID do produto a ser deletado.
Requisição:
DELETE http://localhost:8080/api/produtos/2
Exemplo de Resposta: 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).
8. SolicitacaoCompraController
Gerencia as operações relacionadas às solicitações de compra.

8.1. Listar Todas as Solicitações de Compra
Retorna uma lista de todas as solicitações de compra registradas.

Endpoint: GET /api/solicitacoes-compra
Descrição: Recupera todas as solicitações de compra.
Requisição:
GET http://localhost:8080/api/solicitacoes-compra
Exemplo de Resposta (JSON):
JSON

[
  {
    "id": 1,
    "departamento": "TI",
    "dataSolicitacao": "2025-05-20",
    "status": "APROVADA",
    "observacoes": "Urgente para novo projeto",
    "itensSolicitacao": [
      {
        "id": 1,
        "produto": {
          "id": 101,
          "nomeProduto": "Caneta Esferográfica",
          "codigoProduto": "CANE001",
          "unidadeMedidaProduto": "UNIDADE"
        },
        "quantidade": 100
      }
    ]
  }
]
8.2. Buscar Solicitação de Compra por ID
Retorna uma solicitação de compra específica pelo seu ID.

Endpoint: GET /api/solicitacoes-compra/{id}
Descrição: Recupera uma solicitação de compra usando seu ID.
Parâmetros de Path:
id (Long): O ID da solicitação de compra.
Requisição:
GET http://localhost:8080/api/solicitacoes-compra/1
Exemplo de Resposta (JSON):
JSON

{
  "id": 1,
  "departamento": "TI",
  "dataSolicitacao": "2025-05-20",
  "status": "APROVADA",
  "observacoes": "Urgente para novo projeto",
  "itensSolicitacao": [
    {
      "id": 1,
      "produto": {
        "id": 101,
        "nomeProduto": "Caneta Esferográfica",
        "codigoProduto": "CANE001",
        "unidadeMedidaProduto": "UNIDADE"
      },
      "quantidade": 100
    }
  ]
}
8.3. Criar Solicitação de Compra
Cria um novo registro de solicitação de compra.

Endpoint: POST /api/solicitacoes-compra
Descrição: Adiciona uma nova solicitação de compra.
Corpo da Requisição (JSON - SolicitacaoCompraRequestDTO):
JSON

{
  "departamento": "Marketing",
  "observacoes": "Para campanha de verão",
  "itensSolicitacao": [
    {
      "produtoId": 102,
      "quantidade": 50
    },
    {
      "produtoId": 103,
      "quantidade": 20
    }
  ]
}
Requisição:
POST http://localhost:8080/api/solicitacoes-compra
Content-Type: application/json

{
  "departamento": "Marketing",
  "observacoes": "Para campanha de verão",
  "itensSolicitacao": [
    {
      "produtoId": 102,
      "quantidade": 50
    },
    {
      "produtoId": 103,
      "quantidade": 20
    }
  ]
}
Exemplo de Resposta (JSON - 201 Created):
JSON

{
  "id": 2,
  "departamento": "Marketing",
  "dataSolicitacao": "2025-06-03",
  "status": "PENDENTE",
  "observacoes": "Para campanha de verão",
  "itensSolicitacao": [
    {
      "id": 2,
      "produto": {
        "id": 102,
        "nomeProduto": "Caderno Universitário",
        "codigoProduto": "CADE001",
        "unidadeMedidaProduto": "UNIDADE"
      },
      "quantidade": 50
    },
    {
      "id": 3,
      "produto": {
        "id": 103,
        "nomeProduto": "Borracha",
        "codigoProduto": "BORA001",
        "unidadeMedidaProduto": "UNIDADE"
      },
      "quantidade": 20
    }
  ]
}
8.4. Atualizar Solicitação de Compra
Atualiza uma solicitação de compra existente pelo seu ID.

Endpoint: PUT /api/solicitacoes-compra/{id}
Descrição: Atualiza as informações de uma solicitação de compra existente.
Parâmetros de Path:
id (Long): O ID da solicitação de compra a ser atualizada.
Corpo da Requisição (JSON - SolicitacaoCompraRequestDTO):
JSON

{
  "departamento": "TI",
  "observacoes": "Urgente para novo projeto - PRIORIDADE ALTA",
  "itensSolicitacao": [
    {
      "id": 1,
      "produtoId": 101,
      "quantidade": 150
    }
  ]
}
Requisição:
PUT http://localhost:8080/api/solicitacoes-compra/1
Content-Type: application/json

{
  "departamento": "TI",
  "observacoes": "Urgente para novo projeto - PRIORIDADE ALTA",
  "itensSolicitacao": [
    {
      "id": 1,
      "produtoId": 101,
      "quantidade": 150
    }
  ]
}
Exemplo de Resposta (JSON - 200 OK):
JSON

{
  "id": 1,
  "departamento": "TI",
  "dataSolicitacao": "2025-05-20",
  "status": "PENDENTE_ORCAMENTO",
  "observacoes": "Urgente para novo projeto - PRIORIDADE ALTA",
  "itensSolicitacao": [
    {
      "id": 1,
      "produto": {
        "id": 101,
        "nomeProduto": "Caneta Esferográfica",
        "codigoProduto": "CANE001",
        "unidadeMedidaProduto": "UNIDADE"
      },
      "quantidade": 150
    }
  ]
}
8.5. Deletar Solicitação de Compra
Deleta um registro de solicitação de compra pelo seu ID.

Endpoint: DELETE /api/solicitacoes-compra/{id}
Descrição: Remove uma solicitação de compra.
Parâmetros de Path:
id (Long): O ID da solicitação de compra a ser deletada.
Requisição:
DELETE http://localhost:8080/api/solicitacoes-compra/2
Exemplo de Resposta: 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).
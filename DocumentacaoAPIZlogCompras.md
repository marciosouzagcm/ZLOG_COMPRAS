Documentação da API ZLOG Compras
Esta documentação detalha os endpoints RESTful da aplicação ZLOG Compras, acessível em http://localhost:8080.

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
Endpoint: GET /api/estoques
Descrição: Recupera todos os registros de estoque.
Exemplo de Requisição:

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
Endpoint: GET /api/estoques/{id}
Descrição: Recupera um registro de estoque utilizando seu ID.
Exemplo de Requisição:

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
Endpoint: GET /api/estoques/produto/{codigoProduto}
Descrição: Recupera um registro de estoque associado a um código de produto específico.
Exemplo de Requisição:

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
Endpoint: POST /api/estoques
Descrição: Adiciona um novo item de estoque.
Exemplo de Requisição:

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
Endpoint: POST /api/estoques/batch
Descrição: Adiciona vários itens de estoque em lote.
Exemplo de Requisição:

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
Endpoint: PUT /api/estoques/{id}
Descrição: Atualiza as informações de um estoque existente.
Exemplo de Requisição:

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
Endpoint: PUT /api/estoques/{id}/adicionar-quantidade
Descrição: Adiciona uma determinada quantidade ao estoque de um item.
Exemplo de Requisição:

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
Endpoint: PUT /api/estoques/{id}/retirar-quantidade
Descrição: Retira uma determinada quantidade do estoque de um item.
Exemplo de Requisição:

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
Endpoint: DELETE /api/estoques/{id}
Descrição: Remove um registro de estoque.
Exemplo de Requisição:

DELETE http://localhost:8080/api/estoques/2
Exemplo de Resposta: 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).

3. FornecedorController
Gerencia as operações relacionadas aos fornecedores.

3.1. Listar Todos os Fornecedores
Endpoint: GET /api/fornecedores
Descrição: Recupera todos os registros de fornecedores.
Exemplo de Requisição:

GET http://localhost:8080/api/fornecedores
Exemplo de Resposta (JSON):

JSON

[
  {
    "id": 1,
    "razaoSocial": "Empresa Exemplo Ltda.",
    "cnpj": "12.345.678/0001-90",
    "endereco": "Rua da Amostra, 100, Centro - Guarulhos/SP",
    "telefone": "(11) 98765-4321",
    "email": "contato@empresaexemplo.com.br",
    "contato": "Fulano de Tal",
    "observacoes": null,
    "ativo": null,
    "version": 0
  },
  {
    "id": 9,
    "razaoSocial": "Empresa Exemplar Ltda.",
    "cnpj": "92.345.888/0001-90",
    "endereco": "Rua da Amostra, 100, Centro - Guarulhos/SP",
    "telefone": "(11) 98765-4321",
    "email": "contato@empresa.com.br",
    "contato": "Fulano de Tal e Tal",
    "observacoes": null,
    "ativo": null,
    "version": 0
  }
]
3.2. Buscar Fornecedor por ID
Endpoint: GET /api/fornecedores/{id}
Descrição: Recupera um registro de fornecedor utilizando seu ID.
Exemplo de Requisição:

GET http://localhost:8080/api/fornecedores/1
Exemplo de Resposta (JSON):

JSON

{
  "id": 1,
  "razaoSocial": "Empresa Exemplo Ltda.",
  "cnpj": "12.345.678/0001-90",
  "endereco": "Rua da Amostra, 100, Centro - Guarulhos/SP",
  "telefone": "(11) 98765-4321",
  "email": "contato@empresaexemplo.com.br",
  "contato": "Fulano de Tal",
  "observacoes": null,
  "ativo": null,
  "version": 0
}
3.3. Criar Fornecedor
Endpoint: POST /api/fornecedores
Descrição: Adiciona um novo fornecedor.
Exemplo de Requisição:

POST http://localhost:8080/api/fornecedores
Content-Type: application/json

{
  "cnpj": "92.345.888/0001-90",
  "razaoSocial": "Empresa Exemplar Ltda.",
  "contato": "Fulano de Tal e Tal",
  "email": "contato@empresa.com.br",
  "endereco": "Rua da Amostra, 100, Centro - Guarulhos/SP",
  "telefone": "(11) 98765-4321"
}
Exemplo de Resposta (JSON - 201 Created):

JSON

{
  "id": 9,
  "razaoSocial": "Empresa Exemplar Ltda.",
  "cnpj": "92.345.888/0001-90",
  "endereco": "Rua da Amostra, 100, Centro - Guarulhos/SP",
  "telefone": "(11) 98765-4321",
  "email": "contato@empresa.com.br",
  "contato": "Fulano de Tal e Tal",
  "observacoes": null,
  "ativo": null,
  "version": 0
}
3.4. Atualizar Fornecedor
Endpoint: PUT /api/fornecedores/{id}
Descrição: Atualiza as informações de um fornecedor existente.
Exemplo de Requisição:

PUT http://localhost:8080/api/fornecedores/1
Content-Type: application/json

{
  "id": 1,
  "cnpj": "92.545.888/0001-90",
  "razaoSocial": "Empresa Exemplar Ltda.",
  "contato": "Fulano de Tal e Tal",
  "email": "contato@empresarialka.com.br",
  "endereco": "Rua da Amostra, 100, Centro - Guarulhos/SP",
  "telefone": "(11) 98765-4321"
}
Exemplo de Resposta (JSON - 200 OK):

JSON

{
  "id": 1,
  "razaoSocial": "Empresa Exemplar Ltda.",
  "cnpj": "92.545.888/0001-90",
  "endereco": "Rua da Amostra, 100, Centro - Guarulhos/SP",
  "telefone": "(11) 98765-4321",
  "email": "contato@empresarialka.com.br",
  "contato": "Fulano de Tal e Tal",
  "observacoes": null,
  "ativo": null,
  "version": 0
}
3.5. Deletar Fornecedor
Endpoint: DELETE /api/fornecedores/{id}
Descrição: Remove um registro de fornecedor.
Exemplo de Requisição:

DELETE http://localhost:8080/api/fornecedores/1
Exemplo de Resposta: 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).

4. OrcamentoController
Gerencia as operações relacionadas aos orçamentos.

4.1. Listar Todos os Orçamentos
Endpoint: GET /api/orcamentos
Descrição: Recupera todos os registros de orçamentos.
Exemplo de Requisição:

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
Endpoint: GET /api/orcamentos/{id}
Descrição: Recupera um registro de orçamento utilizando seu ID.
Exemplo de Requisição:

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
Endpoint: POST /api/orcamentos
Descrição: Adiciona um novo orçamento.
Exemplo de Requisição:

POST http://localhost:8080/api/orcamentos
Content-Type: application/json

{
  "solicitacaoCompraId": 4,
  "fornecedorId": 16,
  "numeroOrcamento": "ORC-NOVO-12345",
  "dataCotacao": "2025-06-11",
  "observacoes": "Cotação para materiais de escritório urgente.",
  "condicoesPagamento": "À vista, 5% de desconto",
  "prazoEntrega": "2 dias úteis",
  "itensOrcamento": [
    {
      "produtoId": 5,
      "quantidade": 25,
      "precoUnitarioCotado": 5.50,
      "codigoProduto": "CANETA-AZUL"
    },
    {
      "produtoId": 6,
      "quantidade": 5,
      "precoUnitarioCotado": 120.00,
      "codigoProduto": "IMPR-LASER-X"
    }
  ]
}
Exemplo de Resposta (JSON - 201 Created):

{
    "id": 8,
    "solicitacaoCompraId": 4,
    "descricaoSolicitacaoCompra": "Materiais para evento cultural 'Festa Junina' - JUNHO/2025",
    "fornecedorId": null,
    "nomeFornecedor": "Gráfica Rápida Qualidade Ltda.",
    "cnpjFornecedor": "67.890.123/0001-40",
    "dataCotacao": "2025-06-11",
    "numeroOrcamento": "ORC-NOVO-12345",
    "status": "AGUARDANDO_APROVACAO",
    "valorTotal": 737.50,
    "observacoes": "Cotação para materiais de escritório urgente.",
    "condicoesPagamento": "À vista, 5% de desconto",
    "prazoEntrega": "2 dias úteis",
    "version": 0,
    "itensOrcamento": [
        {
            "id": 17,
            "produtoId": 5,
            "nomeProduto": "Smartphone X10",
            "codigoProduto": "PQR-456",
            "unidadeMedidaProduto": "UN",
            "quantidade": "25",
            "precoUnitarioCotado": "5.50",
            "subtotal": "137.50",
            "observacoes": null,
            "version": 0
        },
        {
            "id": 18,
            "produtoId": 6,
            "nomeProduto": "Teclado Mecânico RGB",
            "codigoProduto": "LMN-789",
            "unidadeMedidaProduto": "UN",
            "quantidade": "5",
            "precoUnitarioCotado": "120.00",
            "subtotal": "600.00",
            "observacoes": null,
            "version": 0
        }
    ],
    "orcamentoAprovado": null
}


4.3. Criar Orçamento com varios fornecedores
Endpoint: POST /api/orcamentos/lote
Descrição: Adiciona mais de um fornecedor um novo orçamento.
Exemplo de Requisição:

POST http://localhost:8080/api/orcamentos/lote
Content-Type: application/json

JSON

{
  "solicitacaoCompraId": 2,
  "orcamentos": [
    {
      "solicitacaoCompraId": 2,
      "fornecedorId": 9,
      "numeroOrcamento": "REF-ESC-2025-ABC-001",
      "dataCotacao": "2025-06-03",
      "observacoes": "Orçamento do Fornecedor ABC.",
      "condicoesPagamento": "Pagamento em 30 dias após a entrega.",
      "prazoEntrega": "15 dias úteis",
      "itensOrcamento": [
        {
          "produtoId": 16,
          "quantidade": 50,
          "precoUnitarioCotado": 10.00,
          "codigoProduto": "PROD-A01"
        },
        {
          "produtoId": 11,
          "quantidade": 20,
          "precoUnitarioCotado": 5.00,
          "codigoProduto": "PROD-B02"
        }
      ]
    },
    {
      "solicitacaoCompraId": 2,
      "fornecedorId": 11,
      "numeroOrcamento": "REF-ESC-2025-XYZ-002",
      "dataCotacao": "2025-06-05",
      "observacoes": "Orçamento do Fornecedor XYZ.",
      "condicoesPagamento": "Pagamento em 45 dias, 5% de desconto à vista.",
      "prazoEntrega": "10 dias úteis",
      "itensOrcamento": [
        {
          "produtoId": 2,
          "quantidade": 50,
          "precoUnitarioCotado": 11.50,
          "codigoProduto": "PROD-A01"
        },
        {
          "produtoId": 4,
          "quantidade": 20,
          "precoUnitarioCotado": 4.80,
          "codigoProduto": "PROD-B02"
        }
      ]
    }
  ]
}
Exemplo de Resposta (JSON - 200 OK):

JSON

[
    {
        "id": 6,
        "solicitacaoCompraId": 2,
        "descricaoSolicitacaoCompra": "Materiais para evento cultural 'Festival de Inverno' - JUNHO/2025",
        "fornecedorId": null,
        "nomeFornecedor": "Empresa Exemplar Ltda.",
        "cnpjFornecedor": "92.345.888/0001-90",
        "dataCotacao": "2025-06-11",
        "numeroOrcamento": "REF-ESC-2025-ABC-001",
        "status": "AGUARDANDO_APROVACAO",
        "valorTotal": 600.00,
        "observacoes": "Orçamento do Fornecedor ABC.",
        "condicoesPagamento": "Pagamento em 30 dias após a entrega.",
        "prazoEntrega": "15 dias úteis",
        "version": 0,
        "itensOrcamento": [
            {
                "id": 13,
                "produtoId": 16,
                "nomeProduto": "Aspirador de Pó Robô",
                "codigoProduto": "QWE-789",
                "unidadeMedidaProduto": "UN",
                "quantidade": "50",
                "precoUnitarioCotado": "10.00",
                "subtotal": "500.00",
                "observacoes": null,
                "version": 0
            },
            {
                "id": 14,
                "produtoId": 11,
                "nomeProduto": "Fones de Ouvido Bluetooth",
                "codigoProduto": "PST-234",
                "unidadeMedidaProduto": "UN",
                "quantidade": "20",
                "precoUnitarioCotado": "5.00",
                "subtotal": "100.00",
                "observacoes": null,
                "version": 0
            }
        ],
        "orcamentoAprovado": null
    },
    {
        "id": 7,
        "solicitacaoCompraId": 2,
        "descricaoSolicitacaoCompra": "Materiais para evento cultural 'Festival de Inverno' - JUNHO/2025",
        "fornecedorId": null,
        "nomeFornecedor": "Comércio de Alimentos Bom Gosto Ltda.",
        "cnpjFornecedor": "12.345.678/0001-90",
        "dataCotacao": "2025-06-11",
        "numeroOrcamento": "REF-ESC-2025-XYZ-002",
        "status": "AGUARDANDO_APROVACAO",
        "valorTotal": 671.00,
        "observacoes": "Orçamento do Fornecedor XYZ.",
        "condicoesPagamento": "Pagamento em 45 dias, 5% de desconto à vista.",
        "prazoEntrega": "10 dias úteis",
        "version": 0,
        "itensOrcamento": [
            {
                "id": 15,
                "produtoId": 2,
                "nomeProduto": "Monitor LED 27 polegadas",
                "codigoProduto": "ABC-123",
                "unidadeMedidaProduto": "UN",
                "quantidade": "50",
                "precoUnitarioCotado": "11.50",
                "subtotal": "575.00",
                "observacoes": null,
                "version": 0
            },
            {
                "id": 16,
                "produtoId": 4,
                "nomeProduto": "Fiacao 39 mm",
                "codigoProduto": "MABC-123",
                "unidadeMedidaProduto": "UN",
                "quantidade": "20",
                "precoUnitarioCotado": "4.80",
                "subtotal": "96.00",
                "observacoes": null,
                "version": 0
            }
        ],
        "orcamentoAprovado": null
    }
]

4.4. Atualizar Orçamento
Endpoint: PUT /api/orcamentos/{id}
Descrição: Atualiza as informações de um orçamento existente.
Exemplo de Requisição:

PUT http://localhost:8080/api/orcamentos/8
Content-Type: application/json

{
  "solicitacaoCompraId": 4,
  "fornecedorId": 16,  
  "numeroOrcamento": "ORC-NOVO-12345-REVISADO-FINAL",
  "dataCotacao": "2025-06-11",
  "observacoes": "Cotação para materiais de escritório. Valores e prazo revisados com a Gráfica Rápida Qualidade Ltda. Novo item de papelaria adicionado.",
  "condicoesPagamento": "30 dias líquido, com desconto para pagamento à vista.",
  "prazoEntrega": "3 dias úteis",
  "status": "AGUARDANDO_APROVACAO",  
  "itensOrcamento": [
    {
      "id": 17,  
      "produtoId": 5,
      "quantidade": 22,            
      "precoUnitarioCotado": 5.60, 
      "observacoes": "Smartphone X10 - quantidade e preço atualizados.",
      "codigoProduto": "PQR-456" 
    },
    {
      "id": 18,  
      "produtoId": 6,
      "quantidade": 4,             
      "precoUnitarioCotado": 122.00, 
      "observacoes": "Teclado Mecânico RGB - quantidade e preço atualizados.",
      "codigoProduto": "LMN-789"
    },
    {
      "produtoId": 25,             
      "quantidade": 100,
      "precoUnitarioCotado": 0.95,
      "observacoes": "Adicionado novo item: Canetas Esferográficas (Produto ID 25).",
      "codigoProduto": "CANETA-ESFER" 
    }
  ]
}
Exemplo de Resposta (JSON - 200 OK):

JSON

{
    "id": 8,
    "solicitacaoCompraId": 4,
    "descricaoSolicitacaoCompra": "Materiais para evento cultural 'Festa Junina' - JUNHO/2025",
    "fornecedorId": null,
    "nomeFornecedor": "Gráfica Rápida Qualidade Ltda.",
    "cnpjFornecedor": "67.890.123/0001-40",
    "dataCotacao": "2025-06-11",
    "numeroOrcamento": "ORC-NOVO-12345-REVISADO-FINAL",
    "status": "AGUARDANDO_APROVACAO",
    "valorTotal": 706.20,
    "observacoes": "Cotação para materiais de escritório. Valores e prazo revisados com a Gráfica Rápida Qualidade Ltda. Novo item de papelaria adicionado.",
    "condicoesPagamento": "30 dias líquido, com desconto para pagamento à vista.",
    "prazoEntrega": "3 dias úteis",
    "version": 0,
    "itensOrcamento": [
        {
            "id": 17,
            "produtoId": 5,
            "nomeProduto": "Smartphone X10",
            "codigoProduto": "PQR-456",
            "unidadeMedidaProduto": "UN",
            "quantidade": "22",
            "precoUnitarioCotado": "5.60",
            "subtotal": "123.20",
            "observacoes": "Smartphone X10 - quantidade e preço atualizados.",
            "version": 0
        },
        {
            "id": 18,
            "produtoId": 6,
            "nomeProduto": "Teclado Mecânico RGB",
            "codigoProduto": "LMN-789",
            "unidadeMedidaProduto": "UN",
            "quantidade": "4",
            "precoUnitarioCotado": "122.00",
            "subtotal": "488.00",
            "observacoes": "Teclado Mecânico RGB - quantidade e preço atualizados.",
            "version": 0
        },
        {
            "id": 20,
            "produtoId": 25,
            "nomeProduto": "Tênis de Corrida Masculino",
            "codigoProduto": "SDF-456",
            "unidadeMedidaProduto": "PAR",
            "quantidade": "100",
            "precoUnitarioCotado": "0.95",
            "subtotal": "95.00",
            "observacoes": "Adicionado novo item: Canetas Esferográficas (Produto ID 25).",
            "version": 0
        }
    ],
    "orcamentoAprovado": null
}

4.5. Aprovar Orçamento
Endpoint: PATCH /api/orcamentos/{id}/aprovar
Descrição: Altera o status de um orçamento para APROVADO.
Exemplo de Requisição:

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
Endpoint: DELETE /api/orcamentos/{id}
Descrição: Remove um registro de orçamento.
Exemplo de Requisição:

DELETE http://localhost:8080/api/orcamentos/2
Exemplo de Resposta: 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).

5. PedidoCompraController
Gerencia as operações relacionadas aos pedidos de compra.

5.1. Listar Todos os Pedidos de Compra
Endpoint: GET /api/pedidos-compra
Descrição: Recupera todos os registros de pedidos de compra.
Exemplo de Requisição:

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
Endpoint: GET /api/pedidos-compra/{id}
Descrição: Recupera um registro de pedido de compra utilizando seu ID.
Exemplo de Requisição:

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
Endpoint: POST /api/pedidos-compra
Descrição: Adiciona um novo pedido de compra.
Exemplo de Requisição:

POST http://localhost:8080/api/pedidos-compra
Content-Type: application/json

{
  "fornecedorId": 9,
  "dataPedido": "2025-06-07",
  "status": "PENDENTE",
  "itens": [
    {
      "produtoId": 4,
      "quantidade": 5,
      "precoUnitario": 70.00,
      "subtotal": 350.00,
      "nomeProduto": "Fiacao 39 mm",
      "codigoProduto": "MABC-123",
      "unidadeMedida": "UN",
      "observacoes": "Para instalação na obra A"
    },
    {
      "produtoId": 6,
      "quantidade": 2,
      "precoUnitario": 320.50,
      "subtotal": 641.00,
      "nomeProduto": "Teclado Mecânico RGB",
      "codigoProduto": "LMN-789",
      "unidadeMedida": "UN",
      "observacoes": "Para estoque"
    },
    {
      "produtoId": 7,
      "quantidade": 3,
      "precoUnitario": 89.90,
      "subtotal": 269.70,
      "nomeProduto": "Mouse Sem Fio Ergonômico",
      "codigoProduto": "DEF-012",
      "unidadeMedida": "UN",
      "observacoes": ""
    },
    {
      "produtoId": 10,
      "quantidade": 1,
      "precoUnitario": 150.00,
      "subtotal": 150.00,
      "nomeProduto": "Webcam Full HD",
      "codigoProduto": "MNO-901",
      "unidadeMedida": "UN",
      "observacoes": "Para home office"
    }
  ],
  "valorTotal": 1410.70
}
Exemplo de Resposta (JSON - 201 Created):

JSON

{
  "id": 2,
  "fornecedor": {
    "id": 9,
    "cnpj": "92.345.888/0001-90",
    "contato": "Fulano de Tal e Tal",
    "email": "contato@empresa.com.br",
    "endereco": "Rua da Amostra, 100, Centro - Guarulhos/SP",
    "telefone": "(11) 98765-4321",
    "version": 0,
    "razaoSocial": "Empresa Exemplar Ltda."
  },
  "orcamento": null,
  "dataPedido": "2025-06-07",
  "valorTotal": 1410.70,
  "status": "PENDENTE",
  "observacoes": null,
  "itens": [
    {
      "id": 3,
      "produto": {
        "id": 4,
        "codigo": "Fiacao 39 mm",
        "codigoProduto": "MABC-123",
        "nome": "Fiacao 39 mm",
        "descricao": "Fiacao de cobre anti-chama para instalão eletrica predial",
        "unidadeMedida": "UN",
        "precoUnitario": 70.00,
        "categoria": "Materiais Elétricos",
        "estoque": 100,
        "version": 1,
        "dataCriacao": "2025-06-04T15:41:39.059399",
        "dataAtualizacao": "2025-06-05T08:56:50.701895"
      },
      "quantidade": 5,
      "precoUnitario": 70.00,
      "subtotal": 350.00,
      "observacoes": "Para instalação na obra A",
      "nomeProduto": "Fiacao 39 mm",
      "codigoProduto": "MABC-123",
      "unidadeMedida": "UN",
      "version": 0
    },
    {
      "id": 4,
      "produto": {
        "id": 6,
        "codigo": "Teclado Mecânico RGB",
        "codigoProduto": "LMN-789",
        "nome": "Teclado Mecânico RGB",
        "descricao": "Teclado gamer com switches Cherry MX e iluminação RGB personalizável.",
        "unidadeMedida": "UN",
        "precoUnitario": 320.50,
        "categoria": "Periféricos de PC",
        "estoque": 75,
        "version": 0,
        "dataCriacao": "2025-06-06T20:59:56.332926",
        "dataAtualizacao": "2025-06-06T20:59:56.332926"
      },
      "quantidade": 2,
      "precoUnitario": 320.50,
      "subtotal": 641.00,
      "observacoes": "Para estoque",
      "nomeProduto": "Teclado Mecânico RGB",
      "codigoProduto": "LMN-789",
      "unidadeMedida": "UN",
      "version": 0
    },
    {
      "id": 5,
      "produto": {
        "id": 7,
        "codigo": "Mouse Sem Fio Ergonômico",
        "codigoProduto": "DEF-012",
        "nome": "Mouse Sem Fio Ergonômico",
        "descricao": "Mouse óptico sem fio com design ergonômico e bateria de longa duração.",
        "unidadeMedida": "UN",
        "precoUnitario": 89.90,
        "categoria": "Periféricos de PC",
        "estoque": 200,
        "version": 0,
        "dataCriacao": "2025-06-06T20:59:56.33946",
        "dataAtualizacao": "2025-06-06T20:59:56.33946"
      },
      "quantidade": 3,
      "precoUnitario": 89.90,
      "subtotal": 269.70,
      "observacoes": "",
      "nomeProduto": "Mouse Sem Fio Ergonômico",
      "codigoProduto": "DEF-012",
      "unidadeMedida": "UN",
      "version": 0
    },
    {
      "id": 6,
      "produto": {
        "id": 10,
        "codigo": "Webcam Full HD",
        "codigoProduto": "MNO-901",
        "nome": "Webcam Full HD",
        "descricao": "Webcam com resolução 1080p para chamadas de vídeo e streaming.",
        "unidadeMedida": "UN",
        "precoUnitario": 150.00,
        "categoria": "Acessórios de Informática",
        "estoque": 90,
        "version": 0,
        "dataCriacao": "2025-06-06T20:59:56.360973",
        "dataAtualizacao": "2025-06-06T20:59:56.360973"
      },
      "quantidade": 1,
      "precoUnitario": 150.00,
      "subtotal": 150.00,
      "observacoes": "Para home office",
      "nomeProduto": "Webcam Full HD",
      "codigoProduto": "MNO-901",
      "unidadeMedida": "UN",
      "version": 0
    }
  ]
}
5.4. Atualizar Pedido de Compra
Endpoint: PUT /api/pedidos-compra/{id}
Descrição: Atualiza as informações de um pedido de compra existente.
Exemplo de Requisição:

PUT http://localhost:8080/api/pedidos-compra/7
Content-Type: application/json

{
    "id": 1,
    "numeroPedido": "PED-2025-001",
    "dataCriacao": "2025-06-06T10:00:00Z",
    "status": "RECEBIDO",
    "valorTotal": 1500.50,
    "fornecedorId": 9,  
    "itens": [
        {
            "produtoId": 1,
            "codigoProduto": "PROD001",
            "nomeProduto": "Produto A",
            "quantidade": 50,
            "precoUnitario": 100.00,
            "subtotal": 5000.00,
            "unidadeMedida": "UN"
        },
        {
            "produtoId": 2,
            "codigoProduto": "PROD002",
            "nomeProduto": "Produto B",
            "quantidade": 250,
            "precoUnitario": 500.25,
            "subtotal": 125062.50,
            "unidadeMedida": "KG"
        }
    ]
}
Exemplo de Resposta (JSON - 200 OK):

JSON

{
    "id": 7,
    "fornecedor": {
        "id": 9,
        "cnpj": "92.345.888/0001-90",
        "contato": "Fulano de Tal e Tal",
        "email": "contato@empresa.com.br",
        "endereco": "Rua da Amostra, 100, Centro - Guarulhos/SP",
        "telefone": "(11) 98765-4321",
        "version": 0,
        "razaoSocial": "Empresa Exemplar Ltda."
    },
    "orcamento": null,
    "dataPedido": "2025-06-11",
    "valorTotal": 130062.50,
    "status": "RECEBIDO",
    "observacoes": null,
    "itens": [
        {
            "id": 32,
            "produto": {
                "id": 1,
                "codigo": "Cadeira de Escritório",
                "codigoProduto": "XYZ-789",
                "nome": "Cadeira de Escritório",
                "descricao": "Cadeira ergonômica com ajuste de altura e apoio lombar.",
                "unidadeMedida": "UN",
                "precoUnitario": 450.00,
                "categoria": "Mobiliário de Escritório",
                "estoque": 50,
                "version": 0,
                "dataCriacao": "2025-06-04T15:39:56.12736",
                "dataAtualizacao": "2025-06-04T15:39:56.12736"
            },
            "quantidade": 50,
            "precoUnitario": 100.00,
            "subtotal": 5000.00,
            "observacoes": null,
            "nomeProduto": "Produto A",
            "codigoProduto": "PROD001",
            "unidadeMedida": "UN",
            "version": 0
        },
        {
            "id": 33,
            "produto": {
                "id": 2,
                "codigo": "Monitor LED 27 polegadas",
                "codigoProduto": "ABC-123",
                "nome": "Monitor LED 27 polegadas",
                "descricao": "Monitor Full HD com painel IPS e tempo de resposta de 5ms.",
                "unidadeMedida": "UN",
                "precoUnitario": 999.99,
                "categoria": "Eletrônicos",
                "estoque": 120,
                "version": 0,
                "dataCriacao": "2025-06-04T15:39:56.206933",
                "dataAtualizacao": "2025-06-04T15:39:56.206933"
            },
            "quantidade": 250,
            "precoUnitario": 500.25,
            "subtotal": 125062.50,
            "observacoes": null,
            "nomeProduto": "Produto B",
            "codigoProduto": "PROD002",
            "unidadeMedida": "KG",
            "version": 0
        }
    ]
}

5.5. Atualizar Status do Pedido de Compra
Endpoint: PUT /api/pedidos-compra/{id}/status
Descrição: Altera o status de um pedido de compra.
Exemplo de Requisição (via Query Param):

PUT http://localhost:8080/api/pedidos-compra/1/status?novoStatus=CANCELADO
Instruções para Postman (ou similar):

Exemplo de Resposta (JSON - 204 No Content):

1. ProcessoCompraController
Gerencia o início de um processo de compra.

6.1. Iniciar Processo de Compra
Endpoint: POST /api/processo-compra/iniciar/{solicitacaoId}
Descrição: Inicia o fluxo completo de processo de compra para uma solicitação específica.
Exemplo de Requisição:

POST http://localhost:8080/api/processo-compra/iniciar/2
Exemplo de Resposta (JSON - 200 OK):

JSON

{
  "mensagem": "Processo de compra iniciado para solicitação com ID: 1. Orçamentos criados e aguardando aprovação."
}
7. ProdutoController
Gerencia as operações relacionadas aos produtos.

7.1. Listar Todos os Produtos
Endpoint: GET /api/produtos
Descrição: Recupera todos os registros de produtos.
Exemplo de Requisição:

GET http://localhost:8080/api/produtos
Exemplo de Resposta (JSON):

JSON

[
    {
        "id": 1,
        "codigoProduto": "XYZ-789",
        "nome": "Cadeira de Escritório",
        "descricao": "Cadeira ergonômica com ajuste de altura e apoio lombar.",
        "unidadeMedida": "UN",
        "precoUnitario": 450.00,
        "dataCriacao": "2025-06-04",
        "dataAtualizacao": "2025-06-04",
        "version": 0
    },
    {
        "id": 2,
        "codigoProduto": "ABC-123",
        "nome": "Monitor LED 27 polegadas",
        "descricao": "Monitor Full HD com painel IPS e tempo de resposta de 5ms.",
        "unidadeMedida": "UN",
        "precoUnitario": 999.99,
        "dataCriacao": "2025-06-04",
        "dataAtualizacao": "2025-06-04",
        "version": 0
    },
    {
        "id": 4,
        "codigoProduto": "MABC-123",
        "nome": "Fiacao 39 mm",
        "descricao": "Fiacao de cobre anti-chama para instalão eletrica predial",
        "unidadeMedida": "UN",
        "precoUnitario": 70.00,
        "dataCriacao": "2025-06-04",
        "dataAtualizacao": "2025-06-05",
        "version": 1
    },
    {
        "id": 5,
        "codigoProduto": "PQR-456",
        "nome": "Smartphone X10",
        "descricao": "Smartphone com câmera de 108MP, tela OLED e 128GB de armazenamento.",
        "unidadeMedida": "UN",
        "precoUnitario": 2599.00,
        "dataCriacao": "2025-06-06",
        "dataAtualizacao": "2025-06-06",
        "version": 0
    },
    {
        "id": 6,
        "codigoProduto": "LMN-789",
        "nome": "Teclado Mecânico RGB",
        "descricao": "Teclado gamer com switches Cherry MX e iluminação RGB personalizável.",
        "unidadeMedida": "UN",
        "precoUnitario": 320.50,
        "dataCriacao": "2025-06-06",
        "dataAtualizacao": "2025-06-06",
        "version": 0
    },
    {
        "id": 7,
        "codigoProduto": "DEF-012",
        "nome": "Mouse Sem Fio Ergonômico",
        "descricao": "Mouse óptico sem fio com design ergonômico e bateria de longa duração.",
        "unidadeMedida": "UN",
        "precoUnitario": 89.90,
        "dataCriacao": "2025-06-06",
        "dataAtualizacao": "2025-06-06",
        "version": 0
    }
]
7.2. Buscar Produto por ID
Endpoint: GET /api/produtos/{id}
Descrição: Recupera um registro de produto usando seu ID.
Exemplo de Requisição:

GET http://localhost:8080/api/produtos/2
Exemplo de Resposta (JSON):

JSON

{
    "id": 2,
    "codigoProduto": "ABC-123",
    "nome": "Monitor LED 27 polegadas",
    "descricao": "Monitor Full HD com painel IPS e tempo de resposta de 5ms.",
    "unidadeMedida": "UN",
    "precoUnitario": 999.99,
    "dataCriacao": "2025-06-04",
    "dataAtualizacao": "2025-06-04",
    "version": 0
}
7.3. Criar Produto
Endpoint: POST /api/produtos
Descrição: Adiciona um novo produto.
Exemplo de Requisição:

POST http://localhost:8080/api/produtos
Content-Type: application/json

{
    "codigo": "XYZ-799",
    "codigoProduto": "XYZ-799",
    "nome": "Cadeira de Escritório com encosto",
    "descricao": "Cadeira ergonômica com ajuste de altura e apoio lombar e encosto para cabeça.",
    "unidadeMedida": "UN",
    "precoUnitario": 550.00,
    "categoria": "Mobiliário de Escritório",
    "estoque": 150
}
Exemplo de Resposta (JSON - 201 Created):

JSON

{
    "id": 62,
    "codigoProduto": "XYZ-799",
    "nome": "Cadeira de Escritório com encosto",
    "descricao": null,
    "unidadeMedida": "UN",
    "precoUnitario": 550.00,
    "dataCriacao": "2025-06-08",
    "dataAtualizacao": "2025-06-08",
    "version": 0
}
7.4. Criar Múltiplos Produtos
Endpoint: POST /api/produtos/multiplos
Descrição: Adiciona vários produtos em lote.
Exemplo de Requisição:

POST http://localhost:8080/api/produtos/multiplos
Content-Type: application/json

[
  {
    "codigo": "PROD-001",
    "codigoProduto": "PCP-12345",
    "nome": "Smartphone Modelo X",
    "descricao": "Celular de última geração com câmera de 108MP e tela OLED de 6.7 polegadas.",
    "unidadeMedida": "UN",
    "precoUnitario": 2500.00,
    "categoria": "Eletrônicos",
    "estoque": 250
  },
  {
    "codigo": "PROD-002",
    "codigoProduto": "PCP-67890",
    "nome": "Smart TV 55 polegadas",
    "descricao": "TV 4K com sistema operacional inteligente e áudio imersivo.",
    "unidadeMedida": "UN",
    "precoUnitario": 3200.50,
    "categoria": "Eletrônicos",
    "estoque": 80
  },
  {
    "codigo": "PROD-003",
    "codigoProduto": "PCP-11223",
    "nome": "Fones de Ouvido Bluetooth",
    "descricao": "Fones sem fio com cancelamento de ruído e bateria de longa duração.",
    "unidadeMedida": "UN",
    "precoUnitario": 450.99,
    "categoria": "Acessórios",
    "estoque": 400
  },
  {
    "codigo": "PROD-004",
    "codigoProduto": "PCP-44556",
    "nome": "Notebook Gamer Power",
    "descricao": "Laptop de alta performance para jogos e tarefas pesadas, com placa de vídeo RTX.",
    "unidadeMedida": "UN",
    "precoUnitario": 7899.90,
    "categoria": "Informática",
    "estoque": 45
  },
  {
    "codigo": "PROD-005",
    "codigoProduto": "PCP-77889",
    "nome": "Monitor Ultrawide 34",
    "descricao": "Monitor curvo de 34 polegadas com resolução QHD, ideal para produtividade e jogos.",
    "unidadeMedida": "UN",
    "precoUnitario": 1850.00,
    "categoria": "Informática",
    "estoque": 120
  }
]
Exemplo de Resposta (JSON - 201 Created):

JSON

[
    {
        "id": 63,
        "codigoProduto": "PCP-12345",
        "nome": "Smartphone Modelo X",
        "descricao": null,
        "unidadeMedida": "UN",
        "precoUnitario": 2500.00,
        "dataCriacao": "2025-06-08",
        "dataAtualizacao": "2025-06-08",
        "version": 0
    },
    {
        "id": 64,
        "codigoProduto": "PCP-67890",
        "nome": "Smart TV 55 polegadas",
        "descricao": null,
        "unidadeMedida": "UN",
        "precoUnitario": 3200.50,
        "dataCriacao": "2025-06-08",
        "dataAtualizacao": "2025-06-08",
        "version": 0
    },
    {
        "id": 65,
        "codigoProduto": "PCP-11223",
        "nome": "Fones de Ouvido Bluetooth",
        "descricao": null,
        "unidadeMedida": "UN",
        "precoUnitario": 450.99,
        "dataCriacao": "2025-06-08",
        "dataAtualizacao": "2025-06-08",
        "version": 0
    },
    {
        "id": 66,
        "codigoProduto": "PCP-44556",
        "nome": "Notebook Gamer Power",
        "descricao": null,
        "unidadeMedida": "UN",
        "precoUnitario": 7899.90,
        "dataCriacao": "2025-06-08",
        "dataAtualizacao": "2025-06-08",
        "version": 0
    },
    {
        "id": 67,
        "codigoProduto": "PCP-77889",
        "nome": "Monitor Ultrawide 34",
        "descricao": null,
        "unidadeMedida": "UN",
        "precoUnitario": 1850.00,
        "dataCriacao": "2025-06-08",
        "dataAtualizacao": "2025-06-08",
        "version": 0
    }
]
7.5. Atualizar Produto
Endpoint: PUT /api/produtos/{id}
Descrição: Atualiza as informações de um produto existente.
Exemplo de Requisição:

PUT http://localhost:8080/api/produtos/1
Content-Type: application/json

{
  "id": 4,
  "codigo": "MABC-123",
  "codigoProduto": "MABC-123",
  "nome": "Fiacao 39 mm",
  "descricao": "Fiacao de cobre anti-chama para instalão eletrica predial",
  "precoUnitario": 90.00,
  "unidadeMedida": "UN",
  "categoria": "Materiais Elétricos",
  "estoque": 200
}
Exemplo de Resposta (JSON - 200 OK):

JSON

{
    "id": 4,
    "codigoProduto": "MABC-123",
    "nome": "Fiacao 39 mm",
    "descricao": "Fiacao de cobre anti-chama para instalão eletrica predial",
    "unidadeMedida": "UN",
    "precoUnitario": 90.00,
    "dataCriacao": "2025-06-04",
    "dataAtualizacao": "2025-06-05",
    "version": 1
}
7.6. Deletar Produto
Endpoint: DELETE /api/produtos/{id}
Descrição: Remove um registro de produto.
Exemplo de Requisição:

DELETE http://localhost:8080/api/produtos/2
Exemplo de Resposta: 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).

8. SolicitacaoCompraController
Gerencia as operações relacionadas às solicitações de compra.

8.1. Listar Todas as Solicitações de Compra
Endpoint: GET /api/solicitacoes-compra
Descrição: Recupera todas as solicitações de compra.
Exemplo de Requisição:

GET http://localhost:8080/api/solicitacoes-compra
Exemplo de Resposta (JSON):

JSON

[
    {
        "id": 1,
        "solicitacaoCompraId": 1,
        "dataSolicitacao": "2025-06-05",
        "solicitante": "Secretaria Municipal de Cultura - Divisão de Eventos",
        "status": "EM_ANDAMENTO",
        "descricaoSolicitacaoCompra": "Materiais para evento cultural 'Festival de Inverno' - JUNHO/2025",
        "version": 4,
        "itens": [
            {
                "id": 4,
                "produtoId": 1,
                "nomeProduto": "Cadeira de Escritório",
                "codigoProduto": "XYZ-789",
                "unidadeMedidaProduto": "UN",
                "quantidade": 5.000,
                "descricaoAdicional": "Cadeira ergonômica com ajuste de altura e apoio lombar.",
                "status": "AGUARDANDO_ORCAMENTO"
            },
            {
                "id": 5,
                "produtoId": 2,
                "nomeProduto": "Monitor LED 27 polegadas",
                "codigoProduto": "ABC-123",
                "unidadeMedidaProduto": "UN",
                "quantidade": 10.000,
                "descricaoAdicional": "Monitor Full HD com painel IPS e tempo de resposta de 5ms.",
                "status": "AGUARDANDO_ORCAMENTO"
            },
            {
                "id": 6,
                "produtoId": 4,
                "nomeProduto": "Fiacao 39 mm",
                "codigoProduto": "MABC-123",
                "unidadeMedidaProduto": "UN",
                "quantidade": 100.000,
                "descricaoAdicional": "Fiacao de cobre anti-chama para instalação elétrica predial",
                "status": "AGUARDANDO_ORCAMENTO"
            }
        ]
    },
    {
        "id": 2,
        "solicitacaoCompraId": 2,
        "dataSolicitacao": "2025-06-06",
        "solicitante": "Secretaria Municipal de Cultura - Divisão de Eventos",
        "status": "EM_ANDAMENTO",
        "descricaoSolicitacaoCompra": "Materiais para evento cultural 'Festival de Inverno' - JUNHO/2025",
        "version": 1,
        "itens": [
            {
                "id": 7,
                "produtoId": 47,
                "nomeProduto": "Console de Videogame Última Geração",
                "codigoProduto": "PQR-012-E",
                "unidadeMedidaProduto": "UN",
                "quantidade": 10.000,
                "descricaoAdicional": "Gramatura 180g/m²",
                "status": "AGUARDANDO_ORCAMENTO"
            },
            {
                "id": 8,
                "produtoId": 48,
                "nomeProduto": "Kit de Pintura Acrílica",
                "codigoProduto": "STU-345-F",
                "unidadeMedidaProduto": "CJ",
                "quantidade": 3.000,
                "descricaoAdicional": "Com Bluetooth e entrada para microfone",
                "status": "AGUARDANDO_ORCAMENTO"
            },
            {
                "id": 9,
                "produtoId": 45,
                "nomeProduto": "Câmera Fotográfica Digital Profissional",
                "codigoProduto": "JKL-456-C",
                "unidadeMedidaProduto": "UN",
                "quantidade": 15.000,
                "descricaoAdicional": "Para oficinas de arte em escolas",
                "status": "AGUARDANDO_ORCAMENTO"
            },
            {
                "id": 10,
                "produtoId": 49,
                "nomeProduto": "Violão Acústico Iniciante",
                "codigoProduto": "VWX-678-G",
                "unidadeMedidaProduto": "UN",
                "quantidade": 2.000,
                "descricaoAdicional": "UHF, com receptor duplo",
                "status": "AGUARDANDO_ORCAMENTO"
            },
            {
                "id": 11,
                "produtoId": 46,
                "nomeProduto": "Drone com Câmera 4K",
                "codigoProduto": "MNO-789-D",
                "unidadeMedidaProduto": "UN",
                "quantidade": 20.000,
                "descricaoAdicional": "Produto fabricado com insumo vegetal ecologicamente correto",
                "status": "AGUARDANDO_ORCAMENTO"
            }
        ]
    },
    {
        "id": 3,
        "solicitacaoCompraId": 3,
        "dataSolicitacao": "2025-06-06",
        "solicitante": "Secretaria Municipal de Cultura - Divisão de Eventos",
        "status": "EM_ANDAMENTO",
        "descricaoSolicitacaoCompra": "Materiais para evento cultural 'Festival de Inverno' - JUNHO/2025",
        "version": 1,
        "itens": [
            {
                "id": 12,
                "produtoId": 47,
                "nomeProduto": "Console de Videogame Última Geração",
                "codigoProduto": "PQR-012-E",
                "unidadeMedidaProduto": "UN",
                "quantidade": 10.000,
                "descricaoAdicional": "Gramatura 180g/m²",
                "status": "AGUARDANDO_ORCAMENTO"
            },
            {
                "id": 13,
                "produtoId": 49,
                "nomeProduto": "Violão Acústico Iniciante",
                "codigoProduto": "VWX-678-G",
                "unidadeMedidaProduto": "UN",
                "quantidade": 2.000,
                "descricaoAdicional": "UHF, com receptor duplo",
                "status": "AGUARDANDO_ORCAMENTO"
            },
            {
                "id": 14,
                "produtoId": 45,
                "nomeProduto": "Câmera Fotográfica Digital Profissional",
                "codigoProduto": "JKL-456-C",
                "unidadeMedidaProduto": "UN",
                "quantidade": 15.000,
                "descricaoAdicional": "Para oficinas de arte em escolas",
                "status": "AGUARDANDO_ORCAMENTO"
            },
            {
                "id": 15,
                "produtoId": 48,
                "nomeProduto": "Kit de Pintura Acrílica",
                "codigoProduto": "STU-345-F",
                "unidadeMedidaProduto": "CJ",
                "quantidade": 3.000,
                "descricaoAdicional": "Com Bluetooth e entrada para microfone",
                "status": "AGUARDANDO_ORCAMENTO"
            },
            {
                "id": 16,
                "produtoId": 46,
                "nomeProduto": "Drone com Câmera 4K",
                "codigoProduto": "MNO-789-D",
                "unidadeMedidaProduto": "UN",
                "quantidade": 20.000,
                "descricaoAdicional": "Produto fabricado com insumo vegetal ecologicamente correto",
                "status": "AGUARDANDO_ORCAMENTO"
            }
        ]
    }
]
8.2. Buscar Solicitação de Compra por ID
Endpoint: GET /api/solicitacoes-compra/{id}
Descrição: Recupera uma solicitação de compra usando seu ID.
Exemplo de Requisição:

GET http://localhost:8080/api/solicitacoes-compra/1
Exemplo de Resposta (JSON):

JSON

{
    "id": 1,
    "solicitacaoCompraId": 1,
    "dataSolicitacao": "2025-06-05",
    "solicitante": "Secretaria Municipal de Cultura - Divisão de Eventos",
    "status": "EM_ANDAMENTO",
    "descricaoSolicitacaoCompra": "Materiais para evento cultural 'Festival de Inverno' - JUNHO/2025",
    "version": 4,
    "itens": [
        {
            "id": 4,
            "produtoId": 1,
            "nomeProduto": "Cadeira de Escritório",
            "codigoProduto": "XYZ-789",
            "unidadeMedidaProduto": "UN",
            "quantidade": 5.000,
            "descricaoAdicional": "Cadeira ergonômica com ajuste de altura e apoio lombar.",
            "status": "AGUARDANDO_ORCAMENTO"
        },
        {
            "id": 5,
            "produtoId": 2,
            "nomeProduto": "Monitor LED 27 polegadas",
            "codigoProduto": "ABC-123",
            "unidadeMedidaProduto": "UN",
            "quantidade": 10.000,
            "descricaoAdicional": "Monitor Full HD com painel IPS e tempo de resposta de 5ms.",
            "status": "AGUARDANDO_ORCAMENTO"
        },
        {
            "id": 6,
            "produtoId": 4,
            "nomeProduto": "Fiacao 39 mm",
            "codigoProduto": "MABC-123",
            "unidadeMedidaProduto": "UN",
            "quantidade": 100.000,
            "descricaoAdicional": "Fiacao de cobre anti-chama para instalação elétrica predial",
            "status": "AGUARDANDO_ORCAMENTO"
        }
    ]
}
8.3. Criar Solicitação de Compra
Endpoint: POST /api/solicitacoes-compra
Descrição: Adiciona uma nova solicitação de compra.
Exemplo de Requisição:

POST http://localhost:8080/api/solicitacoes-compra
Content-Type: application/json

{
  "solicitante": "Secretaria Municipal de Cultura e Diversidade - Divisão de Eventos",
  "dataSolicitacao": "2025-06-07",
  "status": "PENDENTE",
  "descricao": "Materiais para evento cultural 'Festa Junina' - JUNHO/2025",
  "itens": [
    {
      "produtoId": 45,
      "nomeProduto": "Kit Pincéis Artísticos (12 pçs)",
      "codigoProduto": "PINT001",
      "unidadeMedidaProduto": "KT",
      "quantidade": 15.000,
      "descricaoAdicional": "Para oficinas de arte em escolas",
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "produtoId": 46,
      "nomeProduto": "Tinta Acrílica Artística 1L (Cor Vermelha)",
      "codigoProduto": "TINT002",
      "unidadeMedidaProduto": "LT",
      "quantidade": 20.000,
      "descricaoAdicional": "Produto fabricado com insumo vegetal ecologicamente correto",
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "produtoId": 47,
      "nomeProduto": "Papel Canson A3 (Pacote c/ 100)",
      "codigoProduto": "PAPEL003",
      "unidadeMedidaProduto": "PC",
      "quantidade": 10.000,
      "descricaoAdicional": "Gramatura 180g/m²",
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "produtoId": 48,
      "nomeProduto": "Caixa de Som Amplificada Portátil",
      "codigoProduto": "AUDIO001",
      "unidadeMedidaProduto": "UN",
      "quantidade": 3.000,
      "descricaoAdicional": "Com Bluetooth e entrada para microfone",
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "produtoId": 49,
      "nomeProduto": "Microfone Sem Fio Profissional",
      "codigoProduto": "AUDIO002",
      "unidadeMedidaProduto": "UN",
      "quantidade": 2.000,
      "descricaoAdicional": "UHF, com receptor duplo",
      "status": "AGUARDANDO_ORCAMENTO"
    }
  ]
}
Exemplo de Resposta (JSON - 201 Created):

JSON

{
    "id": 4,
    "solicitacaoCompraId": 4,
    "dataSolicitacao": "2025-06-08",
    "solicitante": "Secretaria Municipal de Cultura e Diversidade - Divisão de Eventos",
    "status": "PENDENTE",
    "descricaoSolicitacaoCompra": "Materiais para evento cultural 'Festa Junina' - JUNHO/2025",
    "version": 0,
    "itens": [
        {
            "id": 17,
            "produtoId": 46,
            "nomeProduto": "Drone com Câmera 4K",
            "codigoProduto": "MNO-789-D",
            "unidadeMedidaProduto": "UN",
            "quantidade": 20.000,
            "descricaoAdicional": "Produto fabricado com insumo vegetal ecologicamente correto",
            "status": "AGUARDANDO_ORCAMENTO"
        },
        {
            "id": 18,
            "produtoId": 45,
            "nomeProduto": "Câmera Fotográfica Digital Profissional",
            "codigoProduto": "JKL-456-C",
            "unidadeMedidaProduto": "UN",
            "quantidade": 15.000,
            "descricaoAdicional": "Para oficinas de arte em escolas",
            "status": "AGUARDANDO_ORCAMENTO"
        },
        {
            "id": 19,
            "produtoId": 47,
            "nomeProduto": "Console de Videogame Última Geração",
            "codigoProduto": "PQR-012-E",
            "unidadeMedidaProduto": "UN",
            "quantidade": 10.000,
            "descricaoAdicional": "Gramatura 180g/m²",
            "status": "AGUARDANDO_ORCAMENTO"
        },
        {
            "id": 20,
            "produtoId": 49,
            "nomeProduto": "Violão Acústico Iniciante",
            "codigoProduto": "VWX-678-G",
            "unidadeMedidaProduto": "UN",
            "quantidade": 2.000,
            "descricaoAdicional": "UHF, com receptor duplo",
            "status": "AGUARDANDO_ORCAMENTO"
        },
        {
            "id": 21,
            "produtoId": 48,
            "nomeProduto": "Kit de Pintura Acrílica",
            "codigoProduto": "STU-345-F",
            "unidadeMedidaProduto": "CJ",
            "quantidade": 3.000,
            "descricaoAdicional": "Com Bluetooth e entrada para microfone",
            "status": "AGUARDANDO_ORCAMENTO"
        }
    ]
}
8.4. Atualizar Solicitação de Compra
Endpoint: PUT /api/solicitacoes-compra/{id}
Descrição: Atualiza as informações de uma solicitação de compra existente.
Exemplo de Requisição:

PUT http://localhost:8080/api/solicitacoes-compra/1
Content-Type: application/json

{
  "id": 1,
  "solicitante": "Secretaria Municipal de Cultura - Divisão de Logistica e Compras",
  "dataSolicitacao": "2025-05-31",
  "status": "PENDENTE",
  "descricao": "Materiais para evento cultural 'Festival de Inverno' - JUNHO/2025",
  "itens": [
    {
      "produtoId": 1,  
      "nomeProduto": "Cadeira de Escritório", 
      "codigoProduto": "XYZ-789", 
      "unidadeMedidaProduto": "UN", 
      "quantidade": 4.000, 
      "descricaoAdicional": "Cadeira ergonômica com ajuste de altura e apoio lombar.", 
      "status": "AGUARDANDO_ORCAMENTO" 
    },
    {
      "produtoId": 2, 
      "nomeProduto": "Monitor LED 27 polegadas", 
      "codigoProduto": "ABC-123", 
      "unidadeMedidaProduto": "UN", 
      "quantidade": 1.000, 
      "descricaoAdicional": "Monitor Full HD com painel IPS e tempo de resposta de 5ms.", 
      "status": "AGUARDANDO_ORCAMENTO" 
    },
    {
      "produtoId": 4, 
      "nomeProduto": "Fiacao 39 mm", 
      "codigoProduto": "MABC-123", 
      "unidadeMedidaProduto": "UN", 
      "quantidade": 10.000, 
      "descricaoAdicional": "Fiacao de cobre anti-chama para instalação elétrica predial", 
      "status": "AGUARDANDO_ORCAMENTO" 
    }
  ]
}
Exemplo de Resposta (JSON - 200 OK):

JSON

{
    "id": 1,
    "solicitacaoCompraId": 1,
    "dataSolicitacao": "2025-06-05",
    "solicitante": "Secretaria Municipal de Cultura - Divisão de Logistica e Compras",
    "status": "PENDENTE",
    "descricaoSolicitacaoCompra": "Materiais para evento cultural 'Festival de Inverno' - JUNHO/2025",
    "version": 4,
    "itens": [
        {
            "id": 22,
            "produtoId": 4,
            "nomeProduto": "Fiacao 39 mm",
            "codigoProduto": "MABC-123",
            "unidadeMedidaProduto": "UN",
            "quantidade": 10.000,
            "descricaoAdicional": "Fiacao de cobre anti-chama para instalação elétrica predial",
            "status": "AGUARDANDO_ORCAMENTO"
        },
        {
            "id": 23,
            "produtoId": 2,
            "nomeProduto": "Monitor LED 27 polegadas",
            "codigoProduto": "ABC-123",
            "unidadeMedidaProduto": "UN",
            "quantidade": 1.000,
            "descricaoAdicional": "Monitor Full HD com painel IPS e tempo de resposta de 5ms.",
            "status": "AGUARDANDO_ORCAMENTO"
        },
        {
            "id": 24,
            "produtoId": 1,
            "nomeProduto": "Cadeira de Escritório",
            "codigoProduto": "XYZ-789",
            "unidadeMedidaProduto": "UN",
            "quantidade": 4.000,
            "descricaoAdicional": "Cadeira ergonômica com ajuste de altura e apoio lombar.",
            "status": "AGUARDANDO_ORCAMENTO"
        }
    ]
}
8.5. Deletar Solicitação de Compra
Endpoint: DELETE /api/solicitacoes-compra/{id}
Descrição: Remove uma solicitação de compra.
Exemplo de Requisição:

DELETE http://localhost:8080/api/solicitacoes-compra/2
Exemplo de Resposta: 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).

# Documentação da API ZLOG COMPRAS

Esta documentação detalha os endpoints RESTful da aplicação ZLOG Compras, disponível em [http://localhost:8080](http://localhost:8080).

---

## 1. Módulos e Controladores

A API é organizada pelos seguintes controladores:

- **EstoqueController**
- **FornecedorController**
- **OrcamentoController**
- **PedidoCompraController**
- **ProcessoCompraController**
- **ProdutoController**
- **SolicitacaoCompraController**

---

## 2. EstoqueController

Gerencia as operações relacionadas ao estoque de produtos.

### 2.1. Listar Todos os Estoques

- **Endpoint:** `GET /api/estoques`
- **Descrição:** Recupera todos os registros de estoque.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/estoques
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 2.2. Buscar Estoque por ID

- **Endpoint:** `GET /api/estoques/{id}`
- **Descrição:** Recupera um registro de estoque usando seu ID.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/estoques/1
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 2.3. Buscar Estoque por Código do Produto

- **Endpoint:** `GET /api/estoques/produto/{codigoProduto}`
- **Descrição:** Recupera um registro de estoque associado a um código de produto específico.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/estoques/produto/CANE001
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 2.4. Criar Estoque

- **Endpoint:** `POST /api/estoques`
- **Descrição:** Adiciona um novo item de estoque.

**Exemplo de Requisição:**

```
POST http://localhost:8080/api/estoques
Content-Type: application/json

{
  "produtoId": 103,
  "quantidadeAtual": 200,
  "localizacao": "Armazém Principal"
}
```

**Exemplo de Resposta (JSON - 201 Created):**

```json
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
```

### 2.5. Criar Múltiplos Estoques

- **Endpoint:** `POST /api/estoques/batch`
- **Descrição:** Adiciona vários itens de estoque em lote.

**Exemplo de Requisição:**

```
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
```

**Exemplo de Resposta (JSON - 201 Created):**

```json
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
```

### 2.6. Atualizar Estoque

- **Endpoint:** `PUT /api/estoques/{id}`
- **Descrição:** Atualiza as informações de um estoque existente.

**Exemplo de Requisição:**

```
PUT http://localhost:8080/api/estoques/1
Content-Type: application/json

{
  "produtoId": 101,
  "quantidadeAtual": 180,
  "localizacao": "Prateleira A1 Nova"
}
```

**Exemplo de Resposta (JSON - 200 OK):**

```json
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
```

### 2.7. Adicionar Quantidade ao Estoque

- **Endpoint:** `PUT /api/estoques/{id}/adicionar-quantidade`
- **Descrição:** Adiciona uma determinada quantidade ao estoque de um item.

**Exemplo de Requisição:**

```
PUT http://localhost:8080/api/estoques/1/adicionar-quantidade
Content-Type: application/json

{
  "quantidade": 20
}
```

**Exemplo de Resposta (JSON - 200 OK):**

```json
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
```

### 2.8. Retirar Quantidade do Estoque

- **Endpoint:** `PUT /api/estoques/{id}/retirar-quantidade`
- **Descrição:** Retira uma determinada quantidade do estoque de um item.

**Exemplo de Requisição:**

```
PUT http://localhost:8080/api/estoques/1/retirar-quantidade
Content-Type: application/json

{
  "quantidade": 50
}
```

**Exemplo de Resposta (JSON - 200 OK):**

```json
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
```

### 2.9. Deletar Estoque

- **Endpoint:** `DELETE /api/estoques/{id}`
- **Descrição:** Remove um registro de estoque.

**Exemplo de Requisição:**

```
DELETE http://localhost:8080/api/estoques/2
```

**Exemplo de Resposta:** 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).

---

## 3. FornecedorController

Gerencia as operações relacionadas aos fornecedores.

### 3.1. Listar Todos os Fornecedores

- **Endpoint:** `GET /api/fornecedores`
- **Descrição:** Recupera todos os registros de fornecedores.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/fornecedores
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 3.2. Buscar Fornecedor por ID

- **Endpoint:** `GET /api/fornecedores/{id}`
- **Descrição:** Recupera um registro de fornecedor usando seu ID.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/fornecedores/1
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 3.3. Criar Fornecedor

- **Endpoint:** `POST /api/fornecedores`
- **Descrição:** Adiciona um novo fornecedor.

**Exemplo de Requisição:**

```
POST http://localhost:8080/api/fornecedores
Content-Type: application/json

{
  "cnpj": "92.345.888/0001-90",
  "razao_social": "Empresa Exemplar Ltda.",
  "contato": "Fulano de Tal e Tal",
  "email": "contato@empresa.com.br",
  "endereco": "Rua da Amostra, 100, Centro - Guarulhos/SP",
  "telefone": "(11) 98765-4321"
}
```

**Exemplo de Resposta (JSON - 201 Created):**

```json
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
```

### 3.4. Atualizar Fornecedor

- **Endpoint:** `PUT /api/fornecedores/{id}`
- **Descrição:** Atualiza as informações de um fornecedor existente.

**Exemplo de Requisição:**

```
PUT http://localhost:8080/api/fornecedores/1
Content-Type: application/json

{
  "id": 1, // 5. ID do Fornecedor: O identificador único do fornecedor a ser atualizado. É fundamental que este ID corresponda ao ID informado na URL da requisição (ex: /api/fornecedores/1). Campo obrigatório.
  "cnpj": "92.545.888/0001-90",//de ser atualizado
  "razao_social": "Empresa Exemplar Ltda.",
  "contato": "Fulano de Tal e Tal",
  "email": "contato@empresarialka.com.br",//deve ser atualizado
  "endereco": "Rua da Amostra, 100, Centro - Guarulhos/SP",
  "telefone": "(11) 98765-4321"
}
```

**Exemplo de Resposta (JSON - 200 OK):**

```json
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
```

### 3.5. Deletar Fornecedor

- **Endpoint:** `DELETE /api/fornecedores/{id}`
- **Descrição:** Remove um registro de fornecedor.

**Exemplo de Requisição:**

```
DELETE http://localhost:8080/api/fornecedores/1
```

**Exemplo de Resposta:** 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).

---

## 4. OrcamentoController

Gerencia as operações relacionadas aos orçamentos.

### 4.1. Listar Todos os Orçamentos
cd,,
- **Endpoint:** `GET /api/orcamentos`
- **Descrição:** Recupera todos os registros de orçamentos.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/orcamentos
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 4.2. Buscar Orçamento por ID

- **Endpoint:** `GET /api/orcamentos/{id}`
- **Descrição:** Recupera um registro de orçamento usando seu ID.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/orcamentos/1
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 4.3. Criar Orçamento

- **Endpoint:** `POST /api/orcamentos`
- **Descrição:** Adiciona um novo orçamento.

**Exemplo de Requisição:**

```
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
```

**Exemplo de Resposta (JSON - 201 Created):**

```json
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
```

### 4.4. Atualizar Orçamento

- **Endpoint:** `PUT /api/orcamentos/{id}`
- **Descrição:** Atualiza as informações de um orçamento existente.

**Exemplo de Requisição:**

```
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
```

**Exemplo de Resposta (JSON - 200 OK):**

```json
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
```

### 4.5. Aprovar Orçamento

- **Endpoint:** `PATCH /api/orcamentos/{id}/aprovar`
- **Descrição:** Altera o status de um orçamento para APROVADO.

**Exemplo de Requisição:**

```
PATCH http://localhost:8080/api/orcamentos/1/aprovar
```

**Exemplo de Resposta (JSON - 200 OK):**

```json
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
```

### 4.6. Deletar Orçamento

- **Endpoint:** `DELETE /api/orcamentos/{id}`
- **Descrição:** Remove um registro de orçamento.

**Exemplo de Requisição:**

```
DELETE http://localhost:8080/api/orcamentos/2
```

**Exemplo de Resposta:** 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).

---

## 5. PedidoCompraController

Gerencia as operações relacionadas aos pedidos de compra.

### 5.1. Listar Todos os Pedidos de Compra

- **Endpoint:** `GET /api/pedidos-compra`
- **Descrição:** Recupera todos os registros de pedidos de compra.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/pedidos-compra
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 5.2. Buscar Pedido de Compra por ID

- **Endpoint:** `GET /api/pedidos-compra/{id}`
- **Descrição:** Recupera um registro de pedido de compra usando seu ID.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/pedidos-compra/1
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 5.3. Criar Pedido de Compra

- **Endpoint:** `POST /api/pedidos-compra`
- **Descrição:** Adiciona um novo pedido de compra.

**Exemplo de Requisição:**

```
POST http://localhost:8080/api/pedidos-compra
Content-Type: application/json

{
  "orcamentoId": 1,
  "dataPedido": "2025-06-03",
  "dataEntregaPrevista": "2025-06-15"
}
```

**Exemplo de Resposta (JSON - 201 Created):**

```json
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
```

### 5.4. Atualizar Pedido de Compra

- **Endpoint:** `PUT /api/pedidos-compra/{id}`
- **Descrição:** Atualiza as informações de um pedido de compra existente.

**Exemplo de Requisição:**

```
PUT http://localhost:8080/api/pedidos-compra/1
Content-Type: application/json

{
  "orcamentoId": 1,
  "dataPedido": "2025-06-01",
  "status": "EM_TRANSITO",
  "dataEntregaPrevista": "2025-06-12"
}
```

**Exemplo de Resposta (JSON - 200 OK):**

```json
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
```

### 5.5. Atualizar Status do Pedido de Compra

- **Endpoint:** `PUT /api/pedidos-compra/{id}/status`
- **Descrição:** Altera o status de um pedido de compra.

**Exemplo de Requisição:**

```
PUT http://localhost:8080/api/pedidos-compra/1/status?status=CONCLUIDO
```

**Exemplo de Resposta (JSON - 200 OK):**

```json
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
```

---

## 6. ProcessoCompraController

Gerencia o início de um processo de compra.

### 6.1. Iniciar Processo de Compra

- **Endpoint:** `POST /api/processo-compra/iniciar/{solicitacaoId}`
- **Descrição:** Inicia todo o fluxo de processo de compra para uma solicitação específica.

**Exemplo de Requisição:**

```
POST http://localhost:8080/api/processo-compra/iniciar/1
```

**Exemplo de Resposta (JSON - 200 OK):**

```json
{
  "mensagem": "Processo de compra iniciado para solicitação com ID: 1. Orçamentos criados e aguardando aprovação."
}
```

---

## 7. ProdutoController

Gerencia as operações relacionadas aos produtos.

### 7.1. Listar Todos os Produtos

- **Endpoint:** `GET /api/produtos`
- **Descrição:** Recupera todos os registros de produtos.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/produtos
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 7.2. Buscar Produto por ID

- **Endpoint:** `GET /api/produtos/{id}`
- **Descrição:** Recupera um registro de produto usando seu ID.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/produtos/1
```

**Exemplo de Resposta (JSON):**

```json
{
  "id": 1,
  "nomeProduto": "Caneta Esferográfica Azul",
  "codigoProduto": "CANE001",
  "unidadeMedidaProduto": "UNIDADE"
}
```

### 7.3. Criar Produto

- **Endpoint:** `POST /api/produtos`
- **Descrição:** Adiciona um novo produto.

**Exemplo de Requisição:**

```
POST http://localhost:8080/api/produtos
Content-Type: application/json

{
  "nomeProduto": "Lápis Preto HB",
  "codigoProduto": "LAPI001",
  "unidadeMedidaProduto": "UNIDADE"
}
```

**Exemplo de Resposta (JSON - 201 Created):**

```json
{
  "id": 3,
  "nomeProduto": "Lápis Preto HB",
  "codigoProduto": "LAPI001",
  "unidadeMedidaProduto": "UNIDADE"
}
```

### 7.4. Criar Múltiplos Produtos

- **Endpoint:** `POST /api/produtos/multiplos`
- **Descrição:** Adiciona vários produtos em lote.

**Exemplo de Requisição:**

```
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
```

**Exemplo de Resposta (JSON - 201 Created):**

```json
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
```

### 7.5. Atualizar Produto

- **Endpoint:** `PUT /api/produtos/{id}`
- **Descrição:** Atualiza as informações de um produto existente.

**Exemplo de Requisição:**

```
PUT http://localhost:8080/api/produtos/1
Content-Type: application/json

{
  "nomeProduto": "Caneta Esferográfica Preta",
  "codigoProduto": "CANE001",
  "unidadeMedidaProduto": "UNIDADE"
}
```

**Exemplo de Resposta (JSON - 200 OK):**

```json
{
  "id": 1,
  "nomeProduto": "Caneta Esferográfica Preta",
  "codigoProduto": "CANE001",
  "unidadeMedidaProduto": "UNIDADE"
}
```

### 7.6. Deletar Produto

- **Endpoint:** `DELETE /api/produtos/{id}`
- **Descrição:** Remove um registro de produto.

**Exemplo de Requisição:**

```
DELETE http://localhost:8080/api/produtos/2
```

**Exemplo de Resposta:** 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).

---

## 8. SolicitacaoCompraController

Gerencia as operações relacionadas às solicitações de compra.

### 8.1. Listar Todas as Solicitações de Compra

- **Endpoint:** `GET /api/solicitacoes-compra`
- **Descrição:** Recupera todas as solicitações de compra.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/solicitacoes-compra
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 8.2. Buscar Solicitação de Compra por ID

- **Endpoint:** `GET /api/solicitacoes-compra/{id}`
- **Descrição:** Recupera uma solicitação de compra usando seu ID.

**Exemplo de Requisição:**

```
GET http://localhost:8080/api/solicitacoes-compra/1
```

**Exemplo de Resposta (JSON):**

```json
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
```

### 8.3. Criar Solicitação de Compra

- **Endpoint:** `POST /api/solicitacoes-compra`
- **Descrição:** Adiciona uma nova solicitação de compra.

**Exemplo de Requisição:**

```
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
```

**Exemplo de Resposta (JSON - 201 Created):**

```json
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
```

### 8.4. Atualizar Solicitação de Compra

- **Endpoint:** `PUT /api/solicitacoes-compra/{id}`
- **Descrição:** Atualiza as informações de uma solicitação de compra existente.

**Exemplo de Requisição:**

```
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
```

**Exemplo de Resposta (JSON - 200 OK):**

```json
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
```

### 8.5. Deletar Solicitação de Compra

- **Endpoint:** `DELETE /api/solicitacoes-compra/{id}`
- **Descrição:** Remove uma solicitação de compra.

**Exemplo de Requisição:**

```
DELETE http://localhost:8080/api/solicitacoes-compra/2
```

**Exemplo de Resposta:** 204 No Content (Requisição bem-sucedida, sem conteúdo para retornar).
---
# Testes Manuais de CRUD via Postman

Este documento contém exemplos de requisições para testar os principais endpoints CRUD do sistema ZLOG_COMPRAS. Use o Postman ou cURL para executar cada teste.

---
## Produto

A entidade **Produto** gerencia os itens disponíveis para compra no sistema.

### Criar Produto

Cria um ou mais novos produtos no sistema.

* **POST** `/api/produtos`
* **Headers:**
    * `Content-Type: application/json`
    * `Accept: application/json`
* **Body (JSON - Exemplo com múltiplos produtos):**

```json
[
  {
    "nome": "Cadeira Ergonômica Presidente",
    "codigo": "CAD-PRES-001",
    "descricao": "Cadeira de escritório com ajuste de altura e encosto lombar.",
    "precoUnitario": 850.00,
    "unidadeMedida": "UN",
    "estoque": 50,
    "categoria": "Móveis de Escritório"
  },
  {
    "nome": "Monitor LED 27 polegadas Full HD",
    "codigo": "MON-LED-27FHD",
    "descricao": "Monitor de alta definição para uso profissional.",
    "precoUnitario": 1200.00,
    "unidadeMedida": "UN",
    "estoque": 30,
    "categoria": "Eletrônicos"
  },
  {
    "nome": "Teclado Mecânico RGB",
    "codigo": "TEC-MEC-RGB",
    "descricao": "Teclado mecânico com iluminação RGB personalizável.",
    "precoUnitario": 350.00,
    "unidadeMedida": "UN",
    "estoque": 75,
    "categoria": "Periféricos"
  },
  {
    "nome": "Mouse Sem Fio Ergonômico",
    "codigo": "MOU-SEM-FIO",
    "descricao": "Mouse sem fio com design ergonômico para conforto.",
    "precoUnitario": 120.00,
    "unidadeMedida": "UN",
    "estoque": 100,
    "categoria": "Periféricos"
  },
  {
    "nome": "Notebook Core i7 16GB RAM",
    "codigo": "NOTE-I7-16GB",
    "descricao": "Notebook de alta performance para produtividade.",
    "precoUnitario": 5500.00,
    "unidadeMedida": "UN",
    "estoque": 20,
    "categoria": "Computadores"
  },
  {
    "nome": "Impressora Multifuncional Tanque de Tinta",
    "codigo": "IMP-TANQUE",
    "descricao": "Impressora com scanner e copiadora, alta capacidade de tinta.",
    "precoUnitario": 950.00,
    "unidadeMedida": "UN",
    "estoque": 15,
    "categoria": "Eletrônicos"
  },
  {
    "nome": "Caneta Esferográfica Bic Cristal Azul (Caixa)",
    "codigo": "CAN-BIC-AZUL",
    "descricao": "Caixa com 50 canetas esferográficas azuis.",
    "precoUnitario": 45.00,
    "unidadeMedida": "CX",
    "estoque": 200,
    "categoria": "Material de Escritório"
  },
  {
    "nome": "Resma de Papel A4 75g (500 folhas)",
    "codigo": "PAPEL-A4-75G",
    "descricao": "Pacote com 500 folhas de papel A4 branco.",
    "precoUnitario": 30.00,
    "unidadeMedida": "PC",
    "estoque": 300,
    "categoria": "Material de Escritório"
  },
  {
    "nome": "Agenda Diária 2025",
    "codigo": "AGE-2025-DIARIA",
    "descricao": "Agenda diária com capa dura para o ano de 2025.",
    "precoUnitario": 60.00,
    "unidadeMedida": "UN",
    "estoque": 150,
    "categoria": "Material de Escritório"
  },
  {
    "nome": "Clipes de Papel Galvanizados (Caixa)",
    "codigo": "CLIP-GALV-PEQ",
    "descricao": "Caixa com 100 clipes de papel pequenos.",
    "precoUnitario": 8.50,
    "unidadeMedida": "CX",
    "estoque": 500,
    "categoria": "Material de Escritório"
  },
  {
    "nome": "Furadeira de Impacto 700W",
    "codigo": "FER-FURA-001",
    "descricao": "Furadeira elétrica com função impacto para alvenaria.",
    "precoUnitario": 280.00,
    "unidadeMedida": "UN",
    "estoque": 25,
    "categoria": "Ferramentas"
  },
  {
    "nome": "Jogo de Chaves Combinadas (12 peças)",
    "codigo": "FER-CHAVE-COMB",
    "descricao": "Kit de chaves combinadas para manutenção geral.",
    "precoUnitario": 180.00,
    "unidadeMedida": "CJ",
    "estoque": 40,
    "categoria": "Ferramentas"
  },
  {
    "nome": "Alicate Universal 8 polegadas",
    "codigo": "FER-ALICATE-UNI",
    "descricao": "Alicate robusto para diversas aplicações.",
    "precoUnitario": 75.00,
    "unidadeMedida": "UN",
    "estoque": 60,
    "categoria": "Ferramentas"
  },
  {
    "nome": "Martelo de Borracha 500g",
    "codigo": "FER-MART-BOR",
    "descricao": "Martelo com cabeça de borracha para não danificar superfícies.",
    "precoUnitario": 40.00,
    "unidadeMedida": "UN",
    "estoque": 80,
    "categoria": "Ferramentas"
  },
  {
    "nome": "Parafusadeira Elétrica 12V",
    "codigo": "FER-PARAF-12V",
    "descricao": "Parafusadeira a bateria, ideal para montagens.",
    "precoUnitario": 320.00,
    "unidadeMedida": "UN",
    "estoque": 35,
    "categoria": "Ferramentas"
  },
  {
    "nome": "Luva de Segurança Pigmentada (Par)",
    "codigo": "EPI-LUV-PIG",
    "descricao": "Luva de segurança com pigmentos na palma para melhor aderência.",
    "precoUnitario": 15.00,
    "unidadeMedida": "PAR",
    "estoque": 200,
    "categoria": "EPI"
  },
  {
    "nome": "Óculos de Proteção Transparente",
    "codigo": "EPI-OCULOS-TRAN",
    "descricao": "Óculos de proteção em policarbonato, anti-embaçante.",
    "precoUnitario": 25.00,
    "unidadeMedida": "UN",
    "estoque": 180,
    "categoria": "EPI"
  },
  {
    "nome": "Protetor Auricular Tipo Concha",
    "codigo": "EPI-PROT-CONC",
    "descricao": "Protetor auditivo tipo concha para ambientes com ruído.",
    "precoUnitario": 55.00,
    "unidadeMedida": "UN",
    "estoque": 120,
    "categoria": "EPI"
  },
  {
    "nome": "Máscara Descartável PFF2 (Caixa)",
    "codigo": "EPI-MASK-PFF2",
    "descricao": "Caixa com 10 máscaras de proteção respiratória PFF2.",
    "precoUnitario": 80.00,
    "unidadeMedida": "CX",
    "estoque": 90,
    "categoria": "EPI"
  },
  {
    "nome": "Capacete de Segurança com Jugular",
    "codigo": "EPI-CAPACETE",
    "descricao": "Capacete de segurança branco com jugular ajustável.",
    "precoUnitario": 90.00,
    "unidadeMedida": "UN",
    "estoque": 70,
    "categoria": "EPI"
  },
  {
    "nome": "Fita Isolante 3M 19mm x 20m",
    "codigo": "ELET-FITA-ISO",
    "descricao": "Fita isolante de alta qualidade para instalações elétricas.",
    "precoUnitario": 12.00,
    "unidadeMedida": "UN",
    "estoque": 250,
    "categoria": "Elétrica"
  },
  {
    "nome": "Lâmpada LED Bulbo 9W Bivolt",
    "codigo": "ELET-LAMP-LED9W",
    "descricao": "Lâmpada LED econômica, luz branca fria.",
    "precoUnitario": 18.00,
    "unidadeMedida": "UN",
    "estoque": 300,
    "categoria": "Elétrica"
  },
  {
    "nome": "Extensão Elétrica 3 Tomadas 5m",
    "codigo": "ELET-EXT-3T5M",
    "descricao": "Extensão com 3 tomadas e cabo de 5 metros.",
    "precoUnitario": 65.00,
    "unidadeMedida": "UN",
    "estoque": 100,
    "categoria": "Elétrica"
  }
]
```

### Consultar Produtos

Recupera informações sobre produtos.

* **GET** `/api/produtos`
    * **Descrição:** Retorna uma lista de todos os produtos cadastrados.
* **GET** `/api/produtos/{id}`
    * **Descrição:** Retorna um produto específico pelo seu ID.
    * **Exemplo:** `/api/produtos/1`

### Atualizar Produto

Atualiza as informações de um produto existente.

* **PUT** `/api/produtos/{id}`
* **Headers:**
    * `Content-Type: application/json`
    * `Accept: application/json`
* **Body (JSON - Exemplo de atualização do produto com ID 16):**

```json
{
  "id": 16,
  "codigo": "LAB-BEQUER",
  "nome": "Béquer de Vidro 500ml",
  "descricao": "Béquer de laboratório em vidro borossilicato, capacidade de 500ml. Preço atualizado.",
  "unidadeMedida": "UN",
  "precoUnitario": 37.50,
  "dataCriacao": "2025-05-28T13:42:27.469856",
  "dataAtualizacao": "2025-05-28T21:37:18.000000",
  "version": 0
}
```

### Deletar Produto

Remove um produto do sistema.

* **DELETE** `/api/produtos/{id}`
    * **Descrição:** Deleta um produto específico pelo seu ID.
    * **Exemplo:** `/api/produtos/1`

---
## Solicitação de Compra

A entidade **Solicitação de Compra** representa um pedido de itens que precisam ser adquiridos.

### Criar Solicitação de Compra

Cria uma nova solicitação de compra com seus itens.

* **POST** `/api/solicitacoes-compra`
* **Headers:**
    * `Content-Type: application/json`
    * `Accept: application/json`
* **Body (JSON):**

```json
{
  "descricao": "Aquisição de materiais diversos para a prefeitura - JUNHO/2025",
  "solicitante": "Secretaria Municipal de Administração - Departamento de Compras",
  "status": "PENDENTE",
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 10.0,
      "descricaoAdicional": "Modelo standard",
      "status": "PENDENTE"
    },
    {
      "produtoId": 5,
      "quantidade": 2.0,
      "descricaoAdicional": "Versão profissional",
      "status": "PENDENTE"
    },
    {
      "produtoId": 8,
      "quantidade": 15.0,
      "descricaoAdicional": null,
      "status": "PENDENTE"
    },
    {
      "produtoId": 12,
      "quantidade": 5.0,
      "descricaoAdicional": "Cor azul",
      "status": "PENDENTE"
    },
    {
      "produtoId": 16,
      "quantidade": 20.0,
      "descricaoAdicional": "Para limpeza",
      "status": "PENDENTE"
    },
    {
      "produtoId": 20,
      "quantidade": 3.0,
      "descricaoAdicional": "Com filtro UV",
      "status": "PENDENTE"
    },
    {
      "produtoId": 24,
      "quantidade": 8.0,
      "descricaoAdicional": null,
      "status": "PENDENTE"
    },
    {
      "produtoId": 28,
      "quantidade": 1.0,
      "descricaoAdicional": "Modelo top de linha",
      "status": "PENDENTE"
    },
    {
      "produtoId": 32,
      "quantidade": 7.0,
      "descricaoAdicional": "Para uso em obras",
      "status": "PENDENTE"
    },
    {
      "produtoId": 36,
      "quantidade": 12.0,
      "descricaoAdicional": null,
      "status": "PENDENTE"
    },
    {
      "produtoId": 3,
      "quantidade": 6.0,
      "descricaoAdicional": "Tamanho M",
      "status": "PENDENTE"
    },
    {
      "produtoId": 7,
      "quantidade": 9.0,
      "descricaoAdicional": null,
      "status": "PENDENTE"
    },
    {
      "produtoId": 10,
      "quantidade": 4.0,
      "descricaoAdicional": "Material resistente",
      "status": "PENDENTE"
    },
    {
      "produtoId": 14,
      "quantidade": 18.0,
      "descricaoAdicional": "Para sinalização",
      "status": "PENDENTE"
    },
    {
      "produtoId": 18,
      "quantidade": 25.0,
      "descricaoAdicional": null,
      "status": "PENDENTE"
    },
    {
      "produtoId": 22,
      "quantidade": 2.0,
      "descricaoAdicional": "Alta potência",
      "status": "PENDENTE"
    },
    {
      "produtoId": 26,
      "quantidade": 11.0,
      "descricaoAdicional": "Para escritório",
      "status": "PENDENTE"
    },
    {
      "produtoId": 30,
      "quantidade": 14.0,
      "descricaoAdicional": null,
      "status": "PENDENTE"
    },
    {
      "produtoId": 34,
      "quantidade": 9.0,
      "descricaoAdicional": "Uso geral",
      "status": "PENDENTE"
    },
    {
      "produtoId": 38,
      "quantidade": 6.0,
      "descricaoAdicional": "Para áreas externas",
      "status": "PENDENTE"
    },
    {
      "produtoId": 40,
      "quantidade": 3.0,
      "descricaoAdicional": null,
      "status": "PENDENTE"
    },
    {
      "produtoId": 2,
      "quantidade": 7.0,
      "descricaoAdicional": "Cor preta",
      "status": "PENDENTE"
    },
    {
      "produtoId": 6,
      "quantidade": 13.0,
      "descricaoAdicional": "Pequeno porte",
      "status": "PENDENTE"
    },
    {
      "produtoId": 9,
      "quantidade": 4.0,
      "descricaoAdicional": "Com rodinhas",
      "status": "PENDENTE"
    },
    {
      "produtoId": 11,
      "quantidade": 22.0,
      "descricaoAdicional": "Pacote econômico",
      "status": "PENDENTE"
    }
  ]
}
```

### Consultar Solicitações de Compra

Recupera informações sobre solicitações de compra.

* **GET** `/api/solicitacoes-compra`
    * **Descrição:** Retorna uma lista de todas as solicitações de compra cadastradas.
* **GET** `/api/solicitacoes-compra/{id}`
    * **Descrição:** Retorna uma solicitação de compra específica pelo seu ID.
    * **Exemplo:** `/api/solicitacoes-compra/1`

### Atualizar Solicitação de Compra

Atualiza as informações de uma solicitação de compra existente, incluindo seus itens.

* **PUT** `/api/solicitacoes-compra/{id}`
* **Headers:**
    * `Content-Type: application/json`
    * `Accept: application/json`
* **Body (JSON - Exemplo de atualização da solicitação com ID 1):**

```json
{
  "id": 1,
  "solicitacaoCompraId": 1,
  "dataSolicitacao": "2025-05-28",
  "solicitante": "Secretaria Municipal de Educação - Departamento de TI",
  "status": "EM_ANDAMENTO",
  "descricao": "Material para reforma e manutenção de escola municipal - ATUALIZADO EM 28 de maio de 2025",
  "version": 1,
  "itens": [
    {
      "id": 1,
      "produtoId": 9,
      "nomeProduto": "Regador de Plástico 5L",
      "codigoProduto": "JARDIM-REGADOR",
      "unidadeMedidaProduto": "UN",
      "quantidade": 7.0,
      "descricaoAdicional": "Cor azul - Necessário para jardim interno",
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "id": 2,
      "produtoId": 3,
      "nomeProduto": "Caderno Universitário 10 Matérias",
      "codigoProduto": "ESCR-CADERNO",
      "unidadeMedidaProduto": "UN",
      "quantidade": 20.0,
      "descricaoAdicional": "Com capa dura",
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "id": 3,
      "produtoId": 17,
      "nomeProduto": "Uniforme Operacional Completo",
      "codigoProduto": "VEST-UNIFORME",
      "unidadeMedidaProduto": "CJ",
      "quantidade": 10.0,
      "descricaoAdicional": "Tamanhos variados (P, M, G)",
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "id": 4,
      "produtoId": 6,
      "nomeProduto": "Luva de Segurança Nitrílica",
      "codigoProduto": "EPI-LUVA003",
      "unidadeMedidaProduto": "PAR",
      "quantidade": 30.0,
      "descricaoAdicional": null,
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "id": 5,
      "produtoId": 12,
      "nomeProduto": "Bola de Futebol Campo Ofic.",
      "codigoProduto": "ESPORTE-BOLA",
      "unidadeMedidaProduto": "UN",
      "quantidade": 3.0,
      "descricaoAdicional": null,
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "id": 6,
      "produtoId": 15,
      "nomeProduto": "Câmera de Segurança IP Full HD",
      "codigoProduto": "SEG-CAM001",
      "unidadeMedidaProduto": "UN",
      "quantidade": 2.0,
      "descricaoAdicional": null,
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "id": null,
      "produtoId": 1,
      "nomeProduto": "Cadeira Ergonômica",
      "codigoProduto": "MOB-CAD001",
      "unidadeMedidaProduto": "UN",
      "quantidade": 5.0,
      "descricaoAdicional": "Para escritórios administrativos",
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "id": null,
      "produtoId": 4,
      "nomeProduto": "Caneta Esferográfica Azul",
      "codigoProduto": "ESCR-CANE01",
      "unidadeMedidaProduto": "CX",
      "quantidade": 10.0,
      "descricaoAdicional": "Caixa com 50 unidades",
      "status": "AGUARDANDO_ORCAMENTO"
    },
    {
      "id": null,
      "produtoId": 10,
      "nomeProduto": "Kit Ferramentas Básico",
      "codigoProduto": "MANUT-KIT01",
      "unidadeMedidaProduto": "CJ",
      "quantidade": 2.0,
      "descricaoAdicional": "Para pequenos reparos",
      "status": "AGUARDANDO_ORCAMENTO"
    }
  ]
}
```

### Deletar Solicitação de Compra

Remove uma solicitação de compra do sistema.

* **DELETE** `/api/solicitacoes-compra/{id}`
    * **Descrição:** Deleta uma solicitação de compra específica pelo seu ID.
    * **Exemplo:** `/api/solicitacoes-compra/1`

---
## Fornecedor

A entidade **Fornecedor** representa as empresas que fornecem produtos ou serviços.

### Criar Fornecedor

Cria um novo fornecedor no sistema.

* **POST** `/api/fornecedores`
* **Headers:**
    * `Content-Type: application/json`
    * `Accept: application/json`
* **Body (JSON):**

```json
{
  "nome": "FoodService Gourmet",
  "razaoSocial": "Gourmet Suprimentos para Gastronomia Ltda.",
  "cnpj": "56.789.012/0001-88",
  "endereco": "Rua da Consolação, 1900, Consolação, São Paulo, SP, 01302-000",
  "telefone": "(11) 1111-2222",
  "email": "compras@foodservicegourmet.com.br",
  "contato": "Ricardo Pereira",
  "observacoes": "Ingredientes, insumos e equipamentos para restaurantes e bares.",
  "ativo": true
}
```

### Consultar Fornecedores

Recupera informações sobre fornecedores.

* **GET** `/api/fornecedores`
    * **Descrição:** Retorna uma lista de todos os fornecedores cadastrados.
* **GET** `/api/fornecedores/{id}`
    * **Descrição:** Retorna um fornecedor específico pelo seu ID.
    * **Exemplo:** `/api/fornecedores/1`

### Atualizar Fornecedor

Atualiza as informações de um fornecedor existente.

* **PUT** `/api/fornecedores/{id}`
* **Headers:**
    * `Content-Type: application/json`
    * `Accept: application/json`
* **Body (JSON - Exemplo de atualização do fornecedor com ID 17):**

```json
{
  "id": 17,
  "nome": "Nome do Fornecedor ID 17",
  "razaoSocial": "Razão Social do Fornecedor ID 17 Ltda.",
  "cnpj": "99.999.999/0001-99",
  "endereco": "Endereço do Fornecedor ID 17",
  "telefone": "(XX) XXXX-XXXX",
  "email": "email.unico@dominio.com",
  "contato": "Contato do Fornecedor ID 17",
  "observacoes": "Observações...",
  "ativo": true
}
```

### Deletar Fornecedor

Remove um fornecedor do sistema.

* **DELETE** `/api/fornecedores/{id}`
    * **Descrição:** Deleta um fornecedor específico pelo seu ID.
    * **Exemplo:** `/api/fornecedores/1`

---
## Estoque

A entidade **Estoque** gerencia as informações de quantidade e localização dos produtos em armazenamento.

### Adicionar/Atualizar Itens em Lote no Estoque

Permite adicionar ou atualizar múltiplos itens de estoque em uma única requisição.

* **POST** `/api/estoques/batch`
* **Headers:**
    * `Content-Type: application/json`
    * `Accept: application/json`
* **Body (JSON - Exemplo de adição/atualização de itens em lote):**

```json
[
  {
    "produtoId": 6,
    "nomeProduto": "Canetas Esferográficas (Pacote c/10)",
    "codigoProduto": "",
    "quantidade": 150,
    "dataEntrada": "2025-05-29T12:00:00",
    "localizacao": "ARMAZEM_F1",
    "observacoes": "Pacote misto de cores."
  },
  {
    "produtoId": 7,
    "nomeProduto": "Fones de Ouvido com Fio P2",
    "codigoProduto": "",
    "quantidade": 80,
    "dataEntrada": "2025-05-29T13:00:00",
    "localizacao": "ARMAZEM_F2",
    "observacoes": "Para uso geral, com microfone."
  },
  {
    "produtoId": 8,
    "nomeProduto": "Pilhas Alcalinas AA (Cartela c/4)",
    "codigoProduto": "",
    "quantidade": 200,
    "dataEntrada": "2025-05-29T14:00:00",
    "localizacao": "ARMAZEM_F3",
    "observacoes": "Validade longa."
  },
  {
    "produtoId": 9,
    "nomeProduto": "Pendrives USB 32GB",
    "codigoProduto": "",
    "quantidade": 70,
    "dataEntrada": "2025-05-29T15:00:00",
    "localizacao": "ARMAZEM_G1",
    "observacoes": "Diversas marcas."
  },
  {
    "produtoId": 10,
    "nomeProduto": "Carregador Portátil (Power Bank)",
    "codigoProduto": "",
    "quantidade": 45,
    "dataEntrada": "2025-05-29T16:00:00",
    "localizacao": "ARMAZEM_G2",
    "observacoes": "10000 mAh, preto."
  },
  {
    "produtoId": 11,
    "nomeProduto": "Garrafas de Água Reutilizáveis 500ml",
    "codigoProduto": "",
    "quantidade": 110,
    "dataEntrada": "2025-05-29T17:00:00",
    "localizacao": "ARMAZEM_G3",
    "observacoes": "BPA Free, cores sortidas."
  },
  {
    "produtoId": 12,
    "nomeProduto": "Kit Chaves de Fenda Precisão",
    "codigoProduto": "",
    "quantidade": 35,
    "dataEntrada": "2025-05-29T18:00:00",
    "localizacao": "ARMAZEM_H1",
    "observacoes": "25 peças para eletrônicos."
  },
  {
    "produtoId": 13,
    "nomeProduto": "Protetor Solar Facial FPS 30",
    "codigoProduto": "",
    "quantidade": 95,
    "dataEntrada": "2025-05-29T19:00:00",
    "localizacao": "ARMAZEM_H2",
    "observacoes": "Toque seco, 50ml."
  },
  {
    "produtoId": 14,
    "nomeProduto": "Organizadores de Mesa Acrílico",
    "codigoProduto": "",
    "quantidade": 30,
    "dataEntrada": "2025-05-29T20:00:00",
    "localizacao": "ARMAZEM_H3",
    "observacoes": "Com 3 compartimentos."
  },
  {
    "produtoId": 15,
    "nomeProduto": "Marcadores de Texto (Conjunto c/5)",
    "codigoProduto": "",
    "quantidade": 120,
    "dataEntrada": "2025-05-29T21:00:00",
    "localizacao": "ARMAZEM_I1",
    "observacoes": "Cores fluorescentes variadas."
  }
]
```

### Consultar Estoque

Recupera informações sobre o estoque de produtos.

* **GET** `/api/estoques`
    * **Descrição:** Retorna uma lista de todos os itens em estoque.
* **GET** `/api/estoques/{produtoId}`
    * **Descrição:** Retorna o item de estoque para um `produtoId` específico.
    * **Exemplo:** `/api/estoques/6`

### Atualizar Item de Estoque

Atualiza as informações de um item de estoque existente.

* **PUT** `/api/estoques/{produtoId}`
* **Headers:**
    * `Content-Type: application/json`
    * `Accept: application/json`
* **Body (JSON - Exemplo de atualização de um item de estoque para o produtoId 6):**

```json
{
  "produtoId": 6,
  "nomeProduto": "Canetas Esferográficas (Pacote c/10) - Atualizado",
  "codigoProduto": "CANETAS-PT10",
  "quantidade": 160,
  "dataEntrada": "2025-05-29T12:00:00",
  "localizacao": "ARMAZEM_F1_NOVO",
  "observacoes": "Pacote misto de cores. Quantidade e localização atualizadas."
}
```

### Deletar Item de Estoque

Remove um item de estoque do sistema.

* **DELETE** `/api/estoque/{produtoId}`
    * **Descrição:** Deleta um item de estoque associado a um `produtoId` específico.
    * **Exemplo:** `/api/estoque/6`

---
## Orçamento

A entidade **Orçamento** registra as cotações de preços de fornecedores para uma solicitação de compra.

### Criar Orçamento

Cria um novo orçamento para uma solicitação de compra.

* **POST** `/api/orcamentos`
* **Headers:**
    * `Content-Type: application/json`
    * `Accept: application/json`
* **Body (JSON):**

```json
{
  "solicitacaoCompraId": 1,
  "fornecedorId": 11,
  "dataCotacao": "2025-05-29",
  "numeroOrcamento": "ORC-SC45-FORN11-001",
  "observacoes": "Primeiro orçamento para a SC 45 - Fornecedor Alfa (simulado)",
  "condicoesPagamento": "30 dias líquido",
  "prazoEntrega": "8 dias úteis",
  "itensOrcamento": [
    {
      "produtoId": 19,
      "quantidade": 50,
      "precoUnitarioCotado": 15.0
    },
    {
      "produtoId": 20,
      "quantidade": 5,
      "precoUnitarioCotado": 250.0
    }
  ]
}
```

### Consultar Orçamentos

Recupera informações sobre orçamentos.

* **GET** `/api/orcamentos`
    * **Descrição:** Retorna uma lista de todos os orçamentos cadastrados.
* **GET** `/api/orcamentos/{id}`
    * **Descrição:** Retorna um orçamento específico pelo seu ID.
    * **Exemplo:** `/api/orcamentos/1`

### Atualizar Orçamento

Atualiza as informações de um orçamento existente.

* **PUT** `/api/orcamentos/{id}`
* **Headers:**
    * `Content-Type: application/json`
    * `Accept: application/json`
* **Body (JSON - Exemplo de atualização de um orçamento com ID 1):**

```json
{
  "id": 1,
  "solicitacaoCompraId": 1,
  "fornecedorId": 11,
  "dataCotacao": "2025-05-29",
  "numeroOrcamento": "ORC-SC45-FORN11-001-ATUALIZADO",
  "observacoes": "Orçamento atualizado com novo número e prazo de entrega.",
  "condicoesPagamento": "45 dias com 50% de entrada",
  "prazoEntrega": "5 dias úteis",
  "itensOrcamento": [
    {
      "id": 1,
      "produtoId": 19,
      "quantidade": 60,
      "precoUnitarioCotado": 14.5
    },
    {
      "id": 2,
      "produtoId": 20,
      "quantidade": 7,
      "precoUnitarioCotado": 245.0
    },
    {
      "id": null,
      "produtoId": 21,
      "quantidade": 10,
      "precoUnitarioCotado": 50.0
    }
  ]
}
```

### Deletar Orçamento

Remove um orçamento do sistema.

* **DELETE** `/api/orcamentos/{id}`
    * **Descrição:** Deleta um orçamento específico pelo seu ID.
    * **Exemplo:** `/api/orcamentos/1`
Documentação da API de Fornecedores
Esta seção descreve os endpoints da API RESTful para gerenciar Fornecedores no sistema ZLOG Compras.

A API de Fornecedores permite realizar operações CRUD (Create, Read, Update, Delete) sobre os dados dos fornecedores.

Estrutura do Objeto Fornecedor
O objeto Fornecedor representa uma entidade fornecedora no sistema e possui os seguintes atributos:

Atributo	Tipo	Descrição	Restrições	Exemplo
id	Long	Identificador único do fornecedor. Gerado automaticamente.	Chave Primária, Gerado Automaticamente	1
nome	String	Nome ou razão social do fornecedor.	Obrigatório	"Fornecedor Alfa Ltda."
cnpj	String	Cadastro Nacional da Pessoa Jurídica.	Obrigatório, Único, Tamanho Máximo 18	"12.345.678/0001-90"
contato	String	Nome do contato principal do fornecedor.	Opcional	"João Silva"
telefone	String	Número de telefone do fornecedor.	Opcional, Tamanho Máximo 15	"(11) 98765-4321"
email	String	Endereço de e-mail do fornecedor.	Opcional, Único	"contato@alfa.com"
version	Long	Campo para controle de concorrência otimista (gerenciado automaticamente pelo JPA).	Gerado Automaticamente	0 (na criação), 1, 2, etc. (nas atualizações)

Exportar para as Planilhas
Endpoints
A URL base para os endpoints de Fornecedores é: /api/fornecedores

1. Criar um Novo Fornecedor
Cria um novo registro de fornecedor no sistema.

Tipo de CRUD: CREATE
URL: POST /api/fornecedores
Requisição:
Método: POST
Content-Type: application/json
Corpo da Requisição (JSON):
JSON

{
  "nome": "Fornecedor Beta Conexões Ltda.",
  "cnpj": "24.145.978/0001-90",
  "contato": "Maria Oliveira",
  "telefone": "(21) 98762-9946",
  "email": "contato@beta.com"
}
Observação: id e version não devem ser incluídos na requisição POST, pois são gerados automaticamente.
Retorno Esperado:
Status: 201 Created
Corpo da Resposta (JSON): O objeto Fornecedor recém-criado, incluindo seu id e version gerados.
JSON

{
  "id": 1,
  "nome": "Fornecedor Beta Conexões Ltda.",
  "cnpj": "24.145.978/0001-90",
  "contato": "Maria Oliveira",
  "telefone": "(21) 98762-9946",
  "email": "contato@beta.com",
  "version": 0
}
Possíveis Erros:
400 Bad Request: Se os dados da requisição estiverem inválidos (ex: CNPJ duplicado, campos obrigatórios ausentes).
415 Unsupported Media Type: Se o Content-Type da requisição não for application/json.
2. Listar Todos os Fornecedores
Retorna uma lista de todos os fornecedores registrados no sistema.

Tipo de CRUD: READ (ALL)
URL: GET /api/fornecedores
Requisição:
Método: GET
Content-Type: Não é necessário enviar Content-Type no cabeçalho.
Corpo da Requisição: Nenhum.
Retorno Esperado:
Status: 200 OK
Corpo da Resposta (JSON): Uma lista de objetos Fornecedor.
JSON

[
  {
    "id": 1,
    "nome": "Fornecedor Alfa Ltda.",
    "cnpj": "12.345.678/0001-90",
    "contato": "João Silva",
    "telefone": "(11) 98765-4321",
    "email": "contato@alfa.com",
    "version": 0
  },
  {
    "id": 2,
    "nome": "Fornecedor Beta Conexões Ltda.",
    "cnpj": "24.145.978/0001-90",
    "contato": "Maria Oliveira",
    "telefone": "(21) 98762-9946",
    "email": "contato@beta.com",
    "version": 0
  }
]
Possíveis Erros:
Nenhum erro HTTP específico esperado para requisições GET válidas, mesmo que a lista esteja vazia.
3. Buscar Fornecedor por ID
Retorna os detalhes de um fornecedor específico com base no seu ID.

Tipo de CRUD: READ (BY ID)
URL: GET /api/fornecedores/{id}
Substitua {id} pelo ID numérico do fornecedor.
Requisição:
Método: GET
Content-Type: Não é necessário.
Corpo da Requisição: Nenhum.
Retorno Esperado:
Status: 200 OK
Corpo da Resposta (JSON): O objeto Fornecedor correspondente ao ID.
JSON

{
  "id": 1,
  "nome": "Fornecedor Alfa Ltda.",
  "cnpj": "12.345.678/0001-90",
  "contato": "João Silva",
  "telefone": "(11) 98765-4321",
  "email": "contato@alfa.com",
  "version": 0
}
Status: 404 Not Found
Corpo da Resposta: Vazio, se nenhum fornecedor for encontrado com o ID fornecido.
4. Atualizar um Fornecedor Existente
Atualiza os dados de um fornecedor existente no sistema.

Tipo de CRUD: UPDATE
URL: PUT /api/fornecedores/{id}
Substitua {id} pelo ID numérico do fornecedor a ser atualizado.
Requisição:
Método: PUT
Content-Type: application/json
Corpo da Requisição (JSON): O objeto Fornecedor com os campos a serem atualizados.
Importante: Inclua o id e o version do fornecedor existente no corpo da requisição para o controle de concorrência otimista.
JSON

{
  "id": 1,
  "nome": "Fornecedor Alfa Ltda. (Atualizado)",
  "cnpj": "12.345.678/0001-90",
  "contato": "João Silva Filho",
  "telefone": "(11) 99887-6655",
  "email": "novo.contato@alfa.com",
  "version": 0  // Use a versão mais recente obtida via GET
}
Retorno Esperado:
Status: 200 OK
Corpo da Resposta (JSON): O objeto Fornecedor atualizado.
JSON

{
  "id": 1,
  "nome": "Fornecedor Alfa Ltda. (Atualizado)",
  "cnpj": "12.345.678/0001-90",
  "contato": "João Silva Filho",
  "telefone": "(11) 99887-6655",
  "email": "novo.contato@alfa.com",
  "version": 1 // A versão será incrementada
}
Possíveis Erros:
404 Not Found: Se o fornecedor com o ID fornecido não for encontrado.
400 Bad Request: Se os dados da requisição estiverem inválidos (ex: CNPJ duplicado com outro fornecedor).
409 Conflict: Se a versão (version) enviada no corpo da requisição não corresponder à versão atual no banco de dados (erro de concorrência otimista).
415 Unsupported Media Type: Se o Content-Type da requisição não for application/json.
5. Deletar um Fornecedor
Remove um fornecedor do sistema com base no seu ID.

Tipo de CRUD: DELETE
URL: DELETE /api/fornecedores/{id}
Substitua {id} pelo ID numérico do fornecedor a ser deletado.
Requisição:
Método: DELETE
Content-Type: Não é necessário.
Corpo da Requisição: Nenhum.
Retorno Esperado:
Status: 204 No Content
Corpo da Resposta: Vazio, indicando que a operação foi bem-sucedida e não há conteúdo a ser retornado.
Status: 404 Not Found
Corpo da Resposta: Vazio, se nenhum fornecedor for encontrado com o ID fornecido.
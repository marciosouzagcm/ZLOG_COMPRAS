# ZLOG_COMPRAS - Sistema Integrado de Solicitação e Rastreabilidade de Compras

## Visão Geral

Este projeto implementa um sistema completo de solicitação, aprovação, compra e rastreabilidade de materiais e serviços, construído com Java e o framework Spring Boot. O objetivo é otimizar o processo de aquisição, desde a requisição inicial até a entrega no destino final e a prestação de contas, garantindo total transparência e controle em cada etapa.

## Funcionalidades Detalhadas

* **Software de Solicitação ou Requisição de Compra:**
    * **Telas:** Interface de usuário a ser desenvolvida para que os usuários possam criar novas solicitações de materiais ou serviços.
    * **Lógica:** `SolicitacaoCompraController` e `SolicitacaoCompraService` para receber, validar e persistir as solicitações no banco de dados. A entidade `SolicitacaoCompra` e `ItemSolicitacaoCompra` modelarão esses dados.

* **Verificação de Estoque:**
    * **Lógica:** O `EstoqueService` consultará o repositório `Estoque` para verificar a quantidade disponível dos itens solicitados.

* **Atendimento da Solicitação e Atualização do Estoque:**
    * **Lógica:** Se o material estiver em estoque, o `EstoqueService` gerará registros na entidade `MovimentacaoEstoque` para saída e atualizará a quantidade na entidade `Estoque`.

* **Validação de Compras Anteriores:**
    * **Lógica:** O `SolicitacaoCompraService` ou um serviço dedicado, como `HistoricoComprasService`, poderá consultar os repositórios de `PedidoCompra` e `ItemPedidoCompra` para obter informações sobre preços praticados, fornecedores anteriores, etc., para os itens solicitados.

* **Compra Autorizada e Apresentação de Orçamentos:**
    * **Workflow:** Aprovadores poderão autorizar as solicitações. A lógica de aprovação pode ser implementada em um `FluxoAprovacaoService` e entidades relacionadas.
    * **Orçamentos:** Funcionalidades permitirão registrar e comparar orçamentos de diferentes `Fornecedor`es para as solicitações aprovadas através de controladores e serviços como `OrcamentoController` e `OrcamentoService`, com a entidade `Orcamento`.

* **Criação de Mapa de Compras:**
    * **Lógica:** Um `MapaComprasService` poderá agregar as necessidades de compra de diversas solicitações aprovadas, facilitando a consolidação de pedidos. Este serviço consultaria as entidades `SolicitacaoCompra` e `ItemSolicitacaoCompra` com status apropriados.

* **Geração de Ordem de Compra:**
    * **Lógica:** O `PedidoCompraService` criará formalmente o pedido para o `Fornecedor`, utilizando informações do mapa de compras e dos orçamentos aprovados. As entidades `PedidoCompra` e `ItemPedidoCompra` armazenarão os detalhes.

* **Envio ao Fornecedor:**
    * **Integração:** Um `ComunicacaoFornecedorService` poderia ser responsável por integrar com sistemas de comunicação para enviar as ordens de compra geradas.

* **Gerenciamento de Entrega e Recebimento:**
    * **Lógica:** Um `EntregaRecebimentoController` e `EntregaRecebimentoService` permitirão acompanhar o status da entrega e registrar o recebimento dos materiais ou serviços, atualizando o status do pedido.

* **Envio para Obra Rastreabilidade:**
    * **Lógica:** A rastreabilidade da movimentação dos materiais até o local da obra pode ser implementada através da entidade `MovimentacaoEstoque` registrando a saída para a obra com informações sobre o destino e possivelmente uma entidade adicional como `MovimentacaoObra` para um controle mais granular. Controladores e serviços específicos seriam necessários.

* **Atualização de Estoque:**
    * **Lógica:** Ao receber os materiais, o `EstoqueService` registrará as entradas na entidade `MovimentacaoEstoque` e atualizará as quantidades na entidade `Estoque`.

* **Lançamento do Pedido no Sistema:**
    * **Lógica:** Todas as informações relevantes do pedido serão persistidas nas entidades `PedidoCompra`, `ItemPedidoCompra` e relacionadas para referência futura.

* **Processo para Prestação de Contas com Histórico de Notas:**
    * **Lógica:** O `NotaFiscalController` e `NotaFiscalService` permitirão o registro das notas fiscais, associando-as aos `PedidoCompra`s correspondentes. Relatórios para prestação de contas podem ser gerados consultando essas entidades.

## Próximos Passos

1.  **Modelagem de Dados Completa:** Detalhar todas as entidades JPA necessárias para suportar as funcionalidades descritas, incluindo relacionamentos e atributos específicos.
2.  **Implementação dos Serviços:** Desenvolver a lógica de negócios em cada serviço, utilizando os repositórios para interagir com o banco de dados e implementando as regras de negócio para cada funcionalidade.
3.  **Criação das APIs REST:** Expor as funcionalidades através de endpoints RESTful nos controladores, permitindo a interação com a aplicação através de requisições HTTP.
4.  **Implementação da Lógica de Rastreabilidade:** Utilizar a entidade `MovimentacaoEstoque` e possivelmente `MovimentacaoObra` para registrar cada etapa da movimentação dos materiais.
5.  **Desenvolvimento da Interface de Usuário:** Criar as telas e a lógica front-end para os usuários interagirem com o sistema.
6.  **Implementação do Workflow de Aprovação:** Definir e implementar o fluxo de aprovação de compras.
7.  **Integrações:** Desenvolver as integrações necessárias com outros sistemas.
8.  **Testes:** Escrever testes unitários e de integração para garantir a qualidade e a robustez do sistema.
9.  **Segurança:** Implementar mecanismos de segurança para proteger a aplicação e os dados.

## Estrutura do Projeto Spring Boot

src/
├── main/
│   ├── java/
│   │   └── com/zlogcompras/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── service/
│   │       └── ZlogComprasApplication.java
│   └── resources/
└── test/
└── java/
└── com/zlogcompras/

## Como Executar a Aplicação

1.  Certifique-se de ter o Java Development Kit instalado.
2.  Utilize um gerenciador de dependências como Maven.
3.  Navegue até o diretório raiz do projeto no seu terminal.
4.  Execute o comando: `mvn spring-boot:run`
5.  A aplicação estará disponível.

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests para melhorias e novas funcionalidades.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

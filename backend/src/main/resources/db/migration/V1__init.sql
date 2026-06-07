-- src/main/resources/db/migration/V1__init.sql
-- ============================================================================
-- 1. ESTRUTURA DE USUÁRIOS, PERMISSÕES E SEGURANÇA
-- ============================================================================

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao DATETIME NOT NULL,
    data_atualizacao DATETIME NOT NULL,
    version BIGINT DEFAULT 0 NOT NULL
);

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- ============================================================================
-- 2. CADASTRO BASE DE PRODUTOS E FORNECECORES (Com Soft Delete)
-- ============================================================================

CREATE TABLE produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE, -- Código único e definitivo (unificado)
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    unidade_medida VARCHAR(20) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    preco_unitario DECIMAL(15,4) NOT NULL DEFAULT 0.0000,
    version BIGINT DEFAULT 0 NOT NULL,
    data_criacao DATETIME NOT NULL,
    data_atualizacao DATETIME NOT NULL,
    deleted_at DATETIME NULL -- Soft Delete
);

CREATE TABLE fornecedores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    razao_social VARCHAR(255) NOT NULL,
    contato VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    endereco VARCHAR(255) NOT NULL,
    telefone VARCHAR(15),
    version BIGINT DEFAULT 0 NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME NULL -- Soft Delete
);

-- ============================================================================
-- 3. FLUXO DE SOLICITAÇÃO DE COMPRAS
-- ============================================================================

CREATE TABLE solicitacoes_compra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_solicitacao DATE NOT NULL,
    solicitante VARCHAR(255) NOT NULL, -- Será substituído por FK de User futuramente
    status VARCHAR(40) NOT NULL,
    descricao TEXT,
    version BIGINT DEFAULT 0 NOT NULL,
    data_criacao DATETIME NOT NULL,
    data_atualizacao DATETIME NOT NULL
);

CREATE TABLE itens_solicitacao_compra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    solicitacao_compra_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade DECIMAL(15,4) NOT NULL,
    descricao_adicional VARCHAR(255),
    status VARCHAR(40) NOT NULL,
    version BIGINT DEFAULT 0 NOT NULL,
    data_criacao DATETIME NOT NULL,
    data_atualizacao DATETIME NOT NULL,
    CONSTRAINT fk_isc_sc FOREIGN KEY (solicitacao_compra_id) REFERENCES solicitacoes_compra(id) ON DELETE CASCADE,
    CONSTRAINT fk_isc_pr FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

-- ============================================================================
-- 4. FLUXO DE COTAÇÕES E ORÇAMENTOS (Correção de Redundâncias)
-- ============================================================================

CREATE TABLE orcamentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    solicitacao_compra_id BIGINT NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    numero_orcamento VARCHAR(100) NOT NULL,
    data_cotacao DATE NOT NULL,
    valor_total DECIMAL(15,4) NOT NULL DEFAULT 0.0000,
    prazo_entrega VARCHAR(100),
    condicoes_pagamento VARCHAR(255),
    observacoes TEXT,
    status VARCHAR(40) NOT NULL,
    version BIGINT DEFAULT 0 NOT NULL,
    data_criacao DATETIME NOT NULL,
    data_atualizacao DATETIME NOT NULL,
    CONSTRAINT fk_orc_sc FOREIGN KEY (solicitacao_compra_id) REFERENCES solicitacoes_compra(id),
    CONSTRAINT fk_orc_fo FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id),
    CONSTRAINT uk_orc_num_forn UNIQUE (numero_orcamento, fornecedor_id) -- Unicidade Composta Garantida
);

CREATE TABLE itens_orcamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    orcamento_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    -- CORREÇÃO: Removidas colunas redundantes de snapshot do produto que quebravam o MapStruct
    quantidade DECIMAL(15,4) NOT NULL,
    preco_unitario_cotado DECIMAL(15,4) NOT NULL,
    observacoes VARCHAR(255),
    version BIGINT DEFAULT 0 NOT NULL,
    CONSTRAINT fk_io_orc FOREIGN KEY (orcamento_id) REFERENCES orcamentos(id) ON DELETE CASCADE,
    CONSTRAINT fk_io_pr FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

-- ============================================================================
-- 5. FLUXO DE PEDIDOS DE COMPRA E NOTAS FISCAIS
-- ============================================================================

CREATE TABLE pedidos_compra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fornecedor_id BIGINT NOT NULL,
    orcamento_id BIGINT NULL,
    data_pedido DATE NOT NULL,
    valor_total DECIMAL(15,4) NOT NULL DEFAULT 0.0000,
    status VARCHAR(40) NOT NULL,
    observacoes TEXT,
    version BIGINT DEFAULT 0 NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_pc_fo FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id),
    CONSTRAINT fk_pc_orc FOREIGN KEY (orcamento_id) REFERENCES orcamentos(id)
);

CREATE TABLE itens_pedido_compra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_compra_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    -- CORREÇÃO: Removidas colunas replicadas do produto. Tipo de dado unificado para DECIMAL(15,4)
    quantidade DECIMAL(15,4) NOT NULL, 
    preco_unitario DECIMAL(15,4) NOT NULL,
    subtotal DECIMAL(15,4) NOT NULL,
    observacoes VARCHAR(500),
    version BIGINT DEFAULT 0 NOT NULL,
    CONSTRAINT fk_ipc_pc FOREIGN KEY (pedido_compra_id) REFERENCES pedidos_compra(id) ON DELETE CASCADE,
    CONSTRAINT fk_ipc_pr FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE TABLE notas_fiscais (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_nota_fiscal VARCHAR(60) NOT NULL UNIQUE,
    data_emissao DATE NOT NULL,
    valor_total DECIMAL(15,4) NOT NULL,
    pedido_compra_id BIGINT NOT NULL,
    CONSTRAINT fk_nf_pc FOREIGN KEY (pedido_compra_id) REFERENCES pedidos_compra(id)
);

-- ============================================================================
-- 6. GESTÃO CENTRALIZADA DE ESTOQUE (Sem duplicidade de saldos)
-- ============================================================================

CREATE TABLE estoques (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL UNIQUE,
    quantidade DECIMAL(15,4) NOT NULL DEFAULT 0.0000, -- Centralização do saldo real
    localizacao VARCHAR(120),
    data_ultima_entrada DATETIME,
    data_ultima_saida DATETIME,
    observacoes VARCHAR(255),
    version BIGINT DEFAULT 0 NOT NULL,
    data_criacao DATETIME NOT NULL,
    data_atualizacao DATETIME NOT NULL,
    CONSTRAINT fk_est_pr FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE CASCADE
);

CREATE TABLE movimentacoes_estoque (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL, -- FK Real conectada diretamente ao Produto
    quantidade DECIMAL(15,4) NOT NULL,
    tipo_movimentacao VARCHAR(20) NOT NULL,
    data_movimentacao DATETIME NOT NULL,
    referencia VARCHAR(100),
    version BIGINT DEFAULT 0 NOT NULL,
    CONSTRAINT fk_me_pr FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

-- ============================================================================
-- 7. CRIAÇÃO DE ÍNDICES ESTRATÉGICOS PARA OTIMIZAÇÃO (Compatível com TiDB Cloud)
-- ============================================================================

CREATE INDEX idx_orcamentos_status_data ON orcamentos(status, data_cotacao);
CREATE INDEX idx_pedidos_status_data ON pedidos_compra(status, data_pedido);
CREATE INDEX idx_mov_estoque_prod_data ON movimentacoes_estoque(produto_id, data_movimentacao);
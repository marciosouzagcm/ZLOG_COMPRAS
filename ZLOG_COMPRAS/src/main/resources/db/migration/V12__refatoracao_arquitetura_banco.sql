-- ============================================================================
-- 1. REFATORAÇÃO DA TABELA: movimentacoes_estoque
-- ============================================================================

-- Remove a coluna antiga se ela ainda existir
ALTER TABLE movimentacoes_estoque DROP COLUMN IF EXISTS codigo_material;

-- Adiciona o índice composto de performance de forma segura
ALTER TABLE movimentacoes_estoque ADD INDEX IF NOT EXISTS idx_movimentacoes_produto_data (produto_id, data_movimentacao);


-- ============================================================================
-- 2. REFATORAÇÃO DA TABELA: orcamentos
-- ============================================================================

-- Adiciona as colunas de auditoria que estavam faltando
ALTER TABLE orcamentos ADD COLUMN IF NOT EXISTS created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM';
ALTER TABLE orcamentos ADD COLUMN IF NOT EXISTS updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM';

-- Cria o índice composto normal apenas se ele não existir
ALTER TABLE orcamentos ADD INDEX IF NOT EXISTS idx_orcamentos_status_data (status, data_cotacao);

-- Uso do CREATE UNIQUE INDEX DDL: É a forma mais segura e compatível com o parser do TiDB
CREATE UNIQUE INDEX IF NOT EXISTS uk_orcamento_fornecedor ON orcamentos (numero_orcamento, fornecedor_id);


-- ============================================================================
-- 3. AJUSTES DE PERFORMANCE EM OUTRAS TABELAS
-- ============================================================================

-- Cria o índice composto para a tabela de pedidos de compra de forma segura
ALTER TABLE pedidos_compra ADD INDEX IF NOT EXISTS idx_pedidos_status_data (status, data_pedido);
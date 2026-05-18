-- Remove a coluna duplicada "name" da tabela roles
-- mantendo o mapeamento atual da entidade Role para a coluna "nome".
ALTER TABLE roles
    DROP COLUMN IF EXISTS name;

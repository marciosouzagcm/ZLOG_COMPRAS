-- 1. Cria a coluna permitindo NULL temporariamente
ALTER TABLE roles ADD COLUMN name VARCHAR(50) NULL;

-- 2. Atualiza as linhas existentes com valores temporários únicos baseados no ID
-- (Substitua 'ROLE_ID_' se quiser, mas garante que não haverá duplicidade)
UPDATE roles SET name = CONCAT('ROLE_ID_', id) WHERE name IS NULL;

-- 3. Agora que os dados estão preenchidos e únicos, aplica a restrição NOT NULL
ALTER TABLE roles MODIFY COLUMN name VARCHAR(50) NOT NULL;

-- 4. Adiciona o índice UNIQUE com segurança
ALTER TABLE roles ADD CONSTRAINT uk_roles_name UNIQUE (name);
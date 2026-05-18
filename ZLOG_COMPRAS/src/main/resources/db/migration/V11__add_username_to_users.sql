-- 1. Cria a coluna apenas como NOT NULL
ALTER TABLE users ADD COLUMN username VARCHAR(100) NOT NULL;

-- 2. Adiciona o índice UNIQUE em uma instrução separada
ALTER TABLE users ADD UNIQUE INDEX idx_users_username (username);
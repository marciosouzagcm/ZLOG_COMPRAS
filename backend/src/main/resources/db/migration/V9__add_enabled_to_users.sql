-- Adiciona a coluna 'enabled' na tabela 'users' com o padrão true (1)
ALTER TABLE users ADD COLUMN enabled TINYINT(1) NOT NULL DEFAULT 1;
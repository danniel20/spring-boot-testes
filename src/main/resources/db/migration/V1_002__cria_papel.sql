CREATE TABLE IF NOT EXISTS papel
(
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    created_at date NOT NULL,
    updated_at date,

    CONSTRAINT uk_nome UNIQUE (nome)
)
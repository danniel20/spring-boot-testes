CREATE TABLE IF NOT EXISTS papel
(
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,

    CONSTRAINT uk_nome UNIQUE (nome)
)
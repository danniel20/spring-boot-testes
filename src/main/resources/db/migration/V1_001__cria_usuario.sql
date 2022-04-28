CREATE TABLE IF NOT EXISTS usuario
(
    id SERIAL PRIMARY KEY,
    data_nascimento date NOT NULL,
    email VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    senha VARCHAR(100) NOT NULL,

    CONSTRAINT uk_email UNIQUE (email),
    CONSTRAINT uk_login UNIQUE (login)
)
CREATE TABLE IF NOT EXISTS foto
(
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    path VARCHAR(255),
    usuario_id BIGINT NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp,

    CONSTRAINT fk_usuario_id FOREIGN KEY(usuario_id) REFERENCES usuario(id)
);

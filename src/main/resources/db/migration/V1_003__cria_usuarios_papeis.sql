CREATE TABLE IF NOT EXISTS usuarios_papeis
(
    usuario_id BIGINT NOT NULL,
    papel_id BIGINT NOT NULL,
    CONSTRAINT usuarios_papeis_pk PRIMARY KEY (usuario_id, papel_id),
    CONSTRAINT fk_papel_id FOREIGN KEY (papel_id) REFERENCES papel(id),
    CONSTRAINT fk_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuario(id)
)
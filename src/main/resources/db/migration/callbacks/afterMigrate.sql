-- Cria usuário administrado com login admin e senha padrão: 123456
INSERT INTO usuario(data_nascimento, email, login, nome, senha, created_at, updated_at)
VALUES('1995-05-01', 'admin@teste.com', 'admin', 'Administrador', '$2a$04$.2s3wWjp1K/j0TVwMTB86eqIMo/js/S1hztorF0foULvgORQjqWs6', current_date, current_date)
ON CONFLICT DO NOTHING;

INSERT INTO papel(nome, created_at, updated_at)
VALUES
    ('ADMIN', current_date, current_date),
    ('USER', current_date, current_date)
ON CONFLICT DO NOTHING;

-- Adiciona papel 'ADMIN' para usuário administrador
INSERT INTO usuarios_papeis(usuario_id, papel_id)
VALUES(1,1)
ON CONFLICT DO NOTHING;
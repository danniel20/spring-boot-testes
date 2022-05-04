-- Cria usuário administrado com login admin e senha padrão: 123456
INSERT INTO usuario(data_nascimento, email, login, nome, senha)
VALUES('1995-05-01', 'admin@teste.com', 'admin', 'Administrador', '$2a$04$.2s3wWjp1K/j0TVwMTB86eqIMo/js/S1hztorF0foULvgORQjqWs6')
ON CONFLICT DO NOTHING;

INSERT INTO papel(nome)
VALUES
    ('ADMIN'),
    ('USER')
ON CONFLICT DO NOTHING;

-- Adiciona papel 'ADMIN' para usuário administrador
INSERT INTO usuarios_papeis(usuario_id, papel_id)
VALUES(1,1)
ON CONFLICT DO NOTHING;
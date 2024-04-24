INSERT INTO papel (nome)
SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM papel WHERE nome = 'ADMIN');

INSERT INTO papel (nome)
SELECT 'ESCRITOR' WHERE NOT EXISTS (SELECT 1 FROM papel WHERE nome = 'ESCRITOR');

INSERT INTO papel (nome)
SELECT 'CLIENTE' WHERE NOT EXISTS (SELECT 1 FROM papel WHERE nome = 'CLIENTE');

INSERT INTO usuario (nome, email, senha, id_papel)
SELECT 'Leonardo Madeira Barbosa da Silva','dev.leomadeira@gmail.com', 'root',
    (SELECT id FROM papel WHERE nome = 'ADMIN')
WHERE NOT EXISTS
    (SELECT 1 FROM usuario WHERE email = 'dev.leomadeira@gmail.com');
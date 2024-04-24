CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Criação da tabela papel
CREATE TABLE IF NOT EXISTS papel (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL
);

-- Criação da tabela usuario
CREATE TABLE IF NOT EXISTS usuario (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    biografia TEXT,
    foto VARCHAR(255),
    data_cadastro TIMESTAMP NOT NULL DEFAULT NOW(),
    ultimo_login TIMESTAMP,
    id_papel UUID REFERENCES papel(id)
);

-- Criação da tabela artigo
CREATE TABLE IF NOT EXISTS artigo (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(255) NOT NULL,
    conteudo VARCHAR(255) UNIQUE NOT NULL,
    data_publicacao TIMESTAMP NOT NULL DEFAULT NOW(),
    resumo VARCHAR(255),
    tags VARCHAR(255),
    status VARCHAR(255),
    id_usuario UUID REFERENCES usuario(id)
);

-- Criação da tabela comentario
CREATE TABLE IF NOT EXISTS comentario (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conteudo TEXT NOT NULL,
    data_comentario TIMESTAMP NOT NULL DEFAULT NOW(),
    id_usuario UUID REFERENCES usuario(id),
    id_artigo UUID REFERENCES artigo(id)
);
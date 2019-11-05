CREATE TABLE pessoa(
    codigo BIGSERIAL PRIMARY KEY,
    nome VARCHAR (50) NOT NULL,
    ativo BOOLEAN NOT NULL ,
    logradouro VARCHAR (50),
    numero VARCHAR (50),
    complemento VARCHAR (50),
    bairro VARCHAR (50),
    cep VARCHAR (12),
    cidade VARCHAR (50),
    estado VARCHAR (15)
);


INSERT INTO pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) values ('gustavo', true, 'av 1', '215', 's/n', 'pq brasilia', '75093', 'anapolis', 'goias');

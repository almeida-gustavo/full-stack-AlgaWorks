CREATE TABLE categoria(
    codigo BIGSERIAL PRIMARY KEY,
    nome VARCHAR (50) NOT NULL
);

INSERT INTO categoria (nome) values ('lazer');
INSERT INTO categoria (nome) values ('alimentação');
INSERT INTO categoria (nome) values ('supermercado');
INSERT INTO categoria (nome) values ('farmácia');
INSERT INTO categoria (nome) values ('outros');
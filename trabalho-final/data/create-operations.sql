CREATE DATABASE IF NOT EXISTS cave_fontana;
USE cave_fontana;

CREATE TABLE Cliente (
    email VARCHAR(100) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL DEFAULT 'CLIENTE',
    CONSTRAINT chk_tipo CHECK (tipo IN ('ADMIN', 'CLIENTE'))
);

CREATE TABLE CategoriaVinho (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT
);

CREATE TABLE Vinho (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    safra YEAR NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    estoque INT NOT NULL,
    id_categoria INT NOT NULL,
    caminho_foto VARCHAR(255) NULL,
    CONSTRAINT chk_preco CHECK (preco >= 0),
    CONSTRAINT chk_estoque CHECK (estoque >= 0),
    FOREIGN KEY (id_categoria)
        REFERENCES CategoriaVinho(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE Sacola (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email_cliente VARCHAR(100) NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT chk_status_sacola CHECK (status IN ('ATIVA', 'CONVERTIDA', 'CANCELADA')),
    FOREIGN KEY (email_cliente)
        REFERENCES Cliente(email)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE ItemSacola (
    id_sacola INT NOT NULL,
    id_vinho INT NOT NULL,
    quantidade INT NOT NULL,
    PRIMARY KEY (id_sacola, id_vinho),
    CONSTRAINT chk_quantidade_sacola CHECK (quantidade > 0),
    FOREIGN KEY (id_sacola)
        REFERENCES Sacola(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_vinho)
        REFERENCES Vinho(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE Pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_conclusao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10,2) NOT NULL,
    status_pagamento VARCHAR(20) NOT NULL,
    status_envio VARCHAR(20) NOT NULL,
    id_sacola INT NOT NULL UNIQUE,
    CONSTRAINT chk_valor_total CHECK (valor_total >= 0),
    CONSTRAINT chk_status_pagamento CHECK (status_pagamento IN ('PENDENTE', 'PAGO', 'CANCELADO')),
    CONSTRAINT chk_status_envio CHECK (status_envio IN ('PENDENTE', 'PREPARANDO', 'ENVIADO', 'ENTREGUE')),
    FOREIGN KEY (id_sacola)
        REFERENCES Sacola(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE ItemPedido (
    id_pedido INT NOT NULL,
    id_vinho INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_pedido, id_vinho),
    CONSTRAINT chk_quantidade_pedido CHECK (quantidade > 0),
    CONSTRAINT chk_preco_unitario CHECK (preco_unitario >= 0),
    FOREIGN KEY (id_pedido)
        REFERENCES Pedido(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_vinho)
        REFERENCES Vinho(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

DROP TABLE ItemSacola;
DROP TABLE ItemPedido;
DROP TABLE Pedido;
DROP TABLE Sacola;
DROP TABLE Vinho;
DROP TABLE Cliente;
DROP TABLE CategoriaVinho;

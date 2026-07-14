CREATE DATABASE lab05;
USE lab05;
-- ========================================
-- ========================================
CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cpf CHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL
);

CREATE TABLE categoria_vinho (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    descricao VARCHAR(255)
);

CREATE TABLE vinho (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    safra INT NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    id_categoria INT NOT NULL,
    UNIQUE(nome, safra),
    FOREIGN KEY (id_categoria) REFERENCES categoria_vinho(id)
);

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_cpf CHAR(11) NOT NULL,
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(30) NOT NULL,
    FOREIGN KEY (cliente_cpf) REFERENCES cliente(cpf),
    CHECK (status IN ('PENDENTE', 'PAGO', 'ENVIADO', 'ENTREGUE', 'CANCELADO'))
);

CREATE TABLE item_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_vinho INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    data_item TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    FOREIGN KEY (id_vinho) REFERENCES vinho(id)
);
-- ========================================
-- ========================================
DROP TABLE item_pedido;
DROP TABLE pedido;
DROP TABLE cliente;
DROP TABLE vinho;
DROP TABLE categoria_vinho;

DROP DATABASE lab05;
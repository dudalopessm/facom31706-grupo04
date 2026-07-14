USE cave_fontana;

INSERT INTO Cliente (email, nome, cpf, senha, tipo) VALUES
('admin@cavefontana.com', 'Administrador', '70017982669', 'admin123', 'ADMIN'),
('ana@email.com', 'Ana Souza', '12345678901', 'senha123', 'CLIENTE'),
('bruno@email.com', 'Bruno Lima', '23456789012', 'senha456', 'CLIENTE'),
('carla@email.com', 'Carla Mendes', '34567890123', 'senha789', 'CLIENTE');

INSERT INTO CategoriaVinho (nome, descricao) VALUES
('Tinto', 'Vinhos tintos encorpados'),
('Branco', 'Vinhos brancos leves e refrescantes'),
('Espumante', 'Vinhos espumantes e proseccos');

INSERT INTO Vinho (nome, safra, descricao, preco, estoque, id_categoria) VALUES
('Cabernet Sauvignon', 2021, 'Vinho tinto seco encorpado com notas de frutas escuras', 89.90, 50, 1),
('Chardonnay', 2022, 'Vinho branco leve com aroma de frutas tropicais', 74.50, 40, 2),
('Prosecco', 2023, 'Espumante italiano fresco e frutado', 119.90, 30, 3),
('Malbec', 2020, 'Vinho tinto argentino com sabor intenso', 99.90, 35, 1),
('Sauvignon Blanc', 2022, 'Vinho branco seco e cítrico', 69.90, 45, 2);

INSERT INTO Sacola (email_cliente, data_criacao, status) VALUES
('ana@email.com', '2026-07-01 10:00:00', 'CONVERTIDA'),
('bruno@email.com', '2026-07-02 14:30:00', 'CONVERTIDA'),
('carla@email.com', '2026-07-08 18:20:00', 'ATIVA'),
('ana@email.com', '2026-07-09 09:15:00', 'CONVERTIDA');

INSERT INTO ItemSacola (id_sacola, id_vinho, quantidade) VALUES
(1, 1, 2),
(1, 2, 1),
(2, 3, 1),
(2, 1, 1),
(3, 2, 2),
(3, 3, 1),
(4, 2, 1);

INSERT INTO Pedido (data_conclusao, valor_total, status_pagamento, status_envio, id_sacola) VALUES
('2026-07-01 10:15:00', 254.30, 'PAGO', 'ENTREGUE', 1),
('2026-07-02 15:00:00', 209.80, 'PAGO', 'ENVIADO', 2),
('2026-07-09 09:30:00', 74.50, 'PAGO', 'PREPARANDO', 4);

INSERT INTO ItemPedido (id_pedido, id_vinho, quantidade, preco_unitario) VALUES
(1, 1, 2, 89.90),
(1, 2, 1, 74.50),
(2, 3, 1, 119.90),
(2, 1, 1, 89.90),
(3, 2, 1, 74.50);

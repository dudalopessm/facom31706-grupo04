INSERT INTO cliente (cpf, nome, email, senha) VALUES
('12345678901', 'João Silva', 'joao.silva@email.com', '123456'),
('23456789012', 'Maria Oliveira', 'maria.oliveira@email.com', '123456'),
('34567890123', 'Carlos Souza', 'carlos.souza@email.com', '123456'),
('45678901234', 'Ana Pereira', 'ana.pereira@email.com', '123456'),
('56789012345', 'Fernanda Costa', 'fernanda.costa@email.com', '123456');

INSERT INTO categoria_vinho (nome, descricao) VALUES
('Tintos Encorpados', 'Vinhos tintos intensos e estruturados'),
('Tintos Leves', 'Vinhos tintos suaves e fáceis de beber'),
('Brancos Secos', 'Vinhos brancos com baixa concentração de açúcar'),
('Rosés', 'Vinhos rosados refrescantes'),
('Espumantes', 'Vinhos com gás carbônico natural');

INSERT INTO vinho (nome, safra, preco, id_categoria) VALUES
('Cabernet Sauvignon Reserva', 2020, 89.90, 1),
('Merlot Tradicional', 2021, 59.90, 2),
('Chardonnay Premium', 2022, 74.50, 3),
('Rosé Provence', 2023, 65.00, 4),
('Espumante Brut Ouro', 2021, 99.90, 5),
('Malbec Argentino', 2020, 79.90, 1),
('Pinot Noir Especial', 2022, 82.50, 2),
('Sauvignon Blanc', 2023, 69.90, 3),
('Rosé Verão', 2024, 54.90, 4),
('Espumante Moscatel', 2023, 45.90, 5);

INSERT INTO pedido (cliente_cpf, data_pedido, status) VALUES
('12345678901', '2025-05-10 14:30:00', 'PENDENTE'),
('23456789012', '2025-05-11 09:15:00', 'PAGO'),
('34567890123', '2025-05-12 16:45:00', 'ENVIADO'),
('45678901234', '2025-05-13 11:20:00', 'ENTREGUE'),
('56789012345', '2025-05-14 18:10:00', 'CANCELADO');

INSERT INTO item_pedido (
    id_pedido,
    id_vinho,
    quantidade,
    preco_unitario
) VALUES
(1, 1, 2, 89.90),
(1, 3, 1, 74.50),
(2, 2, 3, 59.90),
(2, 10, 2, 45.90),
(3, 5, 1, 99.90),
(3, 8, 2, 69.90),
(4, 4, 2, 65.00),
(4, 6, 1, 79.90),
(5, 1, 1, 89.90),
(5, 9, 3, 54.90);
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javaBeans.Pedido;

public class PedidoDAO {

    public int inserir(Pedido pedido) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return inserir(conn, pedido);
        }
    }

    public int inserir(Connection conn, Pedido pedido) throws SQLException {
        String sql = "INSERT INTO Pedido (data_conclusao, valor_total, status_pagamento, status_envio, id_sacola) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setObject(1, pedido.getDataConclusao() != null ? pedido.getDataConclusao() : LocalDateTime.now());
            stmt.setDouble(2, pedido.getValorTotal());
            stmt.setString(3, pedido.getStatusPagamento());
            stmt.setString(4, pedido.getStatusEnvio());
            stmt.setInt(5, pedido.getIdSacola());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public Pedido buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Pedido WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPedido(rs);
                }
            }
        }
        return null;
    }

    public List<Pedido> listarPorCliente(String emailCliente) throws SQLException {
        String sql = "SELECT p.* FROM Pedido p JOIN Sacola s ON p.id_sacola = s.id WHERE s.email_cliente = ? ORDER BY p.data_conclusao DESC";
        List<Pedido> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emailCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPedido(rs));
                }
            }
        }
        return lista;
    }

    public List<Pedido> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Pedido ORDER BY data_conclusao DESC";
        List<Pedido> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearPedido(rs));
            }
        }
        return lista;
    }

    public void atualizarStatusEnvio(int id, String status) throws SQLException {
        String sql = "UPDATE Pedido SET status_envio = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void atualizarStatusPagamento(int id, String status) throws SQLException {
        String sql = "UPDATE Pedido SET status_pagamento = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getInt("id"));
        p.setDataConclusao(rs.getObject("data_conclusao", LocalDateTime.class));
        p.setValorTotal(rs.getDouble("valor_total"));
        p.setStatusPagamento(rs.getString("status_pagamento"));
        p.setStatusEnvio(rs.getString("status_envio"));
        p.setIdSacola(rs.getInt("id_sacola"));
        return p;
    }
}

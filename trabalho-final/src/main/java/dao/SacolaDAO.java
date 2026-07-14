package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javaBeans.Sacola;

public class SacolaDAO {

    public int inserir(Sacola sacola) throws SQLException {
        String sql = "INSERT INTO Sacola (email_cliente, data_criacao, status) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, sacola.getEmailCliente());
            stmt.setObject(2, sacola.getDataCriacao() != null ? sacola.getDataCriacao() : LocalDateTime.now());
            stmt.setString(3, sacola.getStatus());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public Sacola buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Sacola WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearSacola(rs);
                }
            }
        }
        return null;
    }

    public Sacola buscarAtivaPorCliente(String emailCliente) throws SQLException {
        String sql = "SELECT * FROM Sacola WHERE email_cliente = ? AND status = 'ATIVA'";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emailCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearSacola(rs);
                }
            }
        }
        return null;
    }

    public void atualizarStatus(int id, String status) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            atualizarStatus(conn, id, status);
        }
    }

    public void atualizarStatus(Connection conn, int id, String status) throws SQLException {
        String sql = "UPDATE Sacola SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public List<Sacola> listarTodas() throws SQLException {
        String sql = "SELECT * FROM Sacola ORDER BY data_criacao DESC";
        List<Sacola> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearSacola(rs));
            }
        }
        return lista;
    }

    private Sacola mapearSacola(ResultSet rs) throws SQLException {
        Sacola s = new Sacola();
        s.setId(rs.getInt("id"));
        s.setEmailCliente(rs.getString("email_cliente"));
        s.setDataCriacao(rs.getObject("data_criacao", LocalDateTime.class));
        s.setStatus(rs.getString("status"));
        return s;
    }
}

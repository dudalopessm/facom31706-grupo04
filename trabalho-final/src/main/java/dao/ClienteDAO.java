package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javaBeans.Cliente;

public class ClienteDAO {

    public void inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (email, nome, cpf, senha, tipo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getEmail());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getSenha());
            stmt.setString(5, cliente.getTipo() != null ? cliente.getTipo() : "CLIENTE");
            stmt.executeUpdate();
        }
    }

    public Cliente buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE email = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }
        return null;
    }

    public Cliente buscarPorEmailESenha(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE email = ? AND senha = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }
        return null;
    }

    public void alterar(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET nome = ?, cpf = ?, senha = ?, tipo = ? WHERE email = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getSenha());
            stmt.setString(4, cliente.getTipo());
            stmt.setString(5, cliente.getEmail());
            stmt.executeUpdate();
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setEmail(rs.getString("email"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setSenha(rs.getString("senha"));
        c.setTipo(rs.getString("tipo"));
        return c;
    }
}

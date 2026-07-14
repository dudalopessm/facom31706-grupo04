package lab05.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lab05.modelo.Cliente;

public class ClienteDao {
    private Connection connection;

    public ClienteDao(Connection connection) {
        this.connection = connection;
    }

    public void adiciona(Cliente cliente) {
        String sql = "INSERT INTO cliente (cpf, nome, email, senha) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getSenha());
            stmt.execute();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                cliente.setId(keys.getInt(1));
            }
            keys.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Cliente> getLista() {
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setNome(rs.getString("nome"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSenha(rs.getString("senha"));
                clientes.add(cliente);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientes;
    }

    public Cliente buscaPorCpf(String cpf) {
        String sql = "SELECT * FROM cliente WHERE cpf=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            Cliente cliente = null;
            if (rs.next()) {
                cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setNome(rs.getString("nome"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSenha(rs.getString("senha"));
            }
            rs.close();
            stmt.close();
            return cliente;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean altera(Cliente cliente) {
        String sql = "UPDATE cliente SET nome=?, email=?, senha=? WHERE cpf=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getSenha());
            stmt.setString(4, cliente.getCpf());
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean estaVazia() {
        String sql = "SELECT COUNT(*) FROM cliente";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            boolean vazia = rs.getInt(1) == 0;
            rs.close();
            stmt.close();
            return vazia;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean possuiPedidos(String cpf) {
        String sql = "SELECT COUNT(*) FROM pedido WHERE cliente_cpf = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            boolean possui = rs.getInt(1) > 0;
            rs.close();
            stmt.close();
            return possui;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean remove(String cpf) {
        String sql = "DELETE FROM cliente WHERE cpf=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cpf);
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

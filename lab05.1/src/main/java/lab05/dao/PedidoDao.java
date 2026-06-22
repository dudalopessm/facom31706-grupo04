package lab05.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lab05.modelo.Pedido;

public class PedidoDao {
    private Connection connection;

    public PedidoDao(Connection connection) {
        this.connection = connection;
    }

    public void adiciona(Pedido pedido) {
        String sql = "INSERT INTO pedido (cliente_cpf, status) VALUES (?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, pedido.getClienteCpf());
            stmt.setString(2, pedido.getStatus());
            stmt.execute();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                pedido.setId(keys.getInt(1));
            }
            keys.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pedido> getLista() {
        String sql = "SELECT * FROM pedido";
        List<Pedido> pedidos = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pedidos.add(mapPedido(rs));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pedidos;
    }

    public Pedido buscaPorId(int id) {
        String sql = "SELECT * FROM pedido WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Pedido pedido = rs.next() ? mapPedido(rs) : null;
            rs.close();
            stmt.close();
            return pedido;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pedido> buscaPorClienteCpf(String clienteCpf) {
        String sql = "SELECT * FROM pedido WHERE cliente_cpf=?";
        List<Pedido> pedidos = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, clienteCpf);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pedidos.add(mapPedido(rs));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pedidos;
    }

    public boolean altera(Pedido pedido) {
        String sql = "UPDATE pedido SET cliente_cpf=?, status=?, data_pedido=CURRENT_TIMESTAMP WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, pedido.getClienteCpf());
            stmt.setString(2, pedido.getStatus());
            stmt.setInt(3, pedido.getId());
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean possuiItens(int idPedido) {
        String sql = "SELECT COUNT(*) FROM item_pedido WHERE id_pedido = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPedido);
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

    public boolean remove(int id) {
        String sql = "DELETE FROM pedido WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removePorClienteCpfEData(String clienteCpf, String data) {
        String sql = "DELETE FROM pedido WHERE cliente_cpf=? AND data_pedido=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, clienteCpf);
            stmt.setString(2, data);
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Pedido mapPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id"));
        pedido.setClienteCpf(rs.getString("cliente_cpf"));
        pedido.setDataPedido(rs.getString("data_pedido"));
        pedido.setStatus(rs.getString("status"));
        return pedido;
    }
}

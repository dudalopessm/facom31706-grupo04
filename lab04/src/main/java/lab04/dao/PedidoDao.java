package lab04.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lab04.modelo.Pedido;

public class PedidoDao {

    private Connection connection;

    public PedidoDao(Connection connection) {
        this.connection = connection;
    }

    public void adiciona(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_cliente, status) VALUES (?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, pedido.getIdCliente());
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
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setIdCliente(rs.getInt("id_cliente"));
                pedido.setDataPedido(rs.getString("data_pedido"));
                pedido.setStatus(rs.getString("status"));
                pedidos.add(pedido);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pedidos;
    }

    public boolean altera(Pedido pedido) {
        String sql = "UPDATE pedido SET id_cliente=?, status=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pedido.getIdCliente());
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

    public boolean remove(Pedido pedido) {
        String sql = "DELETE FROM pedido WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pedido.getId());
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
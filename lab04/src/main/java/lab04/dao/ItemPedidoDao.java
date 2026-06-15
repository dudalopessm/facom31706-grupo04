package lab04.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lab04.modelo.ItemPedido;

public class ItemPedidoDao {

    private Connection connection;

    public ItemPedidoDao(Connection connection) {
        this.connection = connection;
    }

    public void adiciona(ItemPedido item) {
        String sql = "INSERT INTO item_pedido (id_pedido, id_vinho, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, item.getIdPedido());
            stmt.setInt(2, item.getIdVinho());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoUnitario());
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ItemPedido> getLista() {
        String sql = "SELECT * FROM item_pedido";
        List<ItemPedido> itens = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ItemPedido item = new ItemPedido();
                item.setId(rs.getInt("id"));
                item.setIdPedido(rs.getInt("id_pedido"));
                item.setIdVinho(rs.getInt("id_vinho"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnitario(rs.getDouble("preco_unitario"));
                itens.add(item);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itens;
    }

    public List<ItemPedido> getListaPorPedido(int idPedido) {
        String sql = "SELECT * FROM item_pedido WHERE id_pedido=?";
        List<ItemPedido> itens = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ItemPedido item = new ItemPedido();
                item.setId(rs.getInt("id"));
                item.setIdPedido(rs.getInt("id_pedido"));
                item.setIdVinho(rs.getInt("id_vinho"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnitario(rs.getDouble("preco_unitario"));
                itens.add(item);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itens;
    }

    public boolean altera(ItemPedido item) {
        String sql = "UPDATE item_pedido SET id_pedido=?, id_vinho=?, quantidade=?, preco_unitario=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, item.getIdPedido());
            stmt.setInt(2, item.getIdVinho());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoUnitario());
            stmt.setInt(5, item.getId());
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean remove(ItemPedido item) {
        String sql = "DELETE FROM item_pedido WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, item.getId());
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
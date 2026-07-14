package lab07.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lab07.modelo.ItemPedido;

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
        String sql = "SELECT ip.*, v.nome as vinho_nome FROM item_pedido ip JOIN vinho v ON v.id = ip.id_vinho ORDER BY ip.data_item";
        List<ItemPedido> itens = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                itens.add(mapItem(rs));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itens;
    }

    public List<ItemPedido> getListaPorPedido(int idPedido) {
        String sql = "SELECT ip.*, v.nome as vinho_nome FROM item_pedido ip JOIN vinho v ON v.id = ip.id_vinho WHERE id_pedido=? ORDER BY ip.data_item";
        List<ItemPedido> itens = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                itens.add(mapItem(rs));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itens;
    }

    public ItemPedido buscaPorPedidoVinho(int idPedido, int idVinho) {
        String sql = "SELECT ip.*, v.nome as vinho_nome FROM item_pedido ip JOIN vinho v ON v.id = ip.id_vinho WHERE id_pedido=? AND id_vinho=? LIMIT 1";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPedido);
            stmt.setInt(2, idVinho);
            ResultSet rs = stmt.executeQuery();
            ItemPedido item = rs.next() ? mapItem(rs) : null;
            rs.close();
            stmt.close();
            return item;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemPedido buscaPorPedidoVinhoData(int idPedido, int idVinho, String dataItem) {
        String sql = "SELECT ip.*, v.nome as vinho_nome FROM item_pedido ip JOIN vinho v ON v.id = ip.id_vinho WHERE id_pedido=? AND id_vinho=? AND data_item=? LIMIT 1";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPedido);
            stmt.setInt(2, idVinho);
            stmt.setString(3, dataItem);
            ResultSet rs = stmt.executeQuery();
            ItemPedido item = rs.next() ? mapItem(rs) : null;
            rs.close();
            stmt.close();
            return item;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean remove(int id) {
        String sql = "DELETE FROM item_pedido WHERE id=?";
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

    public boolean atualizaQuantidade(int id, int novaQuantidade) {
        String sql = "UPDATE item_pedido SET quantidade=?, data_item=CURRENT_TIMESTAMP WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, novaQuantidade);
            stmt.setInt(2, id);
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ItemPedido mapItem(ResultSet rs) throws SQLException {
        ItemPedido item = new ItemPedido();
        item.setId(rs.getInt("id"));
        item.setIdPedido(rs.getInt("id_pedido"));
        item.setIdVinho(rs.getInt("id_vinho"));
        item.setQuantidade(rs.getInt("quantidade"));
        item.setPrecoUnitario(rs.getDouble("preco_unitario"));
        item.setVinhoNome(rs.getString("vinho_nome"));
        item.setDataItem(rs.getString("data_item"));
        return item;
    }
}

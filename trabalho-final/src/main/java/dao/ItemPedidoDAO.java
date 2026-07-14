package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javaBeans.ItemPedido;
import javaBeans.Vinho;

public class ItemPedidoDAO {

    public void inserir(ItemPedido item) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            inserir(conn, item);
        }
    }

    public void inserir(Connection conn, ItemPedido item) throws SQLException {
        String sql = "INSERT INTO ItemPedido (id_pedido, id_vinho, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getIdPedido());
            stmt.setInt(2, item.getIdVinho());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoUnitario());
            stmt.executeUpdate();
        }
    }

    public List<ItemPedido> listarPorPedido(int idPedido) throws SQLException {
        String sql = "SELECT ip.*, v.* FROM ItemPedido ip JOIN Vinho v ON ip.id_vinho = v.id WHERE ip.id_pedido = ?";
        List<ItemPedido> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemPedido item = new ItemPedido();
                    item.setIdPedido(rs.getInt("id_pedido"));
                    item.setIdVinho(rs.getInt("id_vinho"));
                    item.setQuantidade(rs.getInt("quantidade"));
                    item.setPrecoUnitario(rs.getDouble("preco_unitario"));

                    Vinho v = new Vinho();
                    v.setId(rs.getInt("v.id"));
                    v.setNome(rs.getString("v.nome"));
                    v.setSafra(rs.getInt("v.safra"));
                    v.setDescricao(rs.getString("v.descricao"));
                    v.setPreco(rs.getDouble("v.preco"));
                    v.setEstoque(rs.getInt("v.estoque"));
                    v.setIdCategoria(rs.getInt("v.id_categoria"));
                    v.setCaminhoFoto(rs.getString("v.caminho_foto"));
                    item.setVinho(v);

                    lista.add(item);
                }
            }
        }
        return lista;
    }
}

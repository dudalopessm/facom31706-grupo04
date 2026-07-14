package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javaBeans.ItemSacola;
import javaBeans.Vinho;

public class ItemSacolaDAO {

    public void inserir(ItemSacola item) throws SQLException {
        String sql = "INSERT INTO ItemSacola (id_sacola, id_vinho, quantidade) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getIdSacola());
            stmt.setInt(2, item.getIdVinho());
            stmt.setInt(3, item.getQuantidade());
            stmt.executeUpdate();
        }
    }

    public void atualizarQuantidade(int idSacola, int idVinho, int quantidade) throws SQLException {
        String sql = "UPDATE ItemSacola SET quantidade = ? WHERE id_sacola = ? AND id_vinho = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, idSacola);
            stmt.setInt(3, idVinho);
            stmt.executeUpdate();
        }
    }

    public void remover(int idSacola, int idVinho) throws SQLException {
        String sql = "DELETE FROM ItemSacola WHERE id_sacola = ? AND id_vinho = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSacola);
            stmt.setInt(2, idVinho);
            stmt.executeUpdate();
        }
    }

    public List<ItemSacola> listarPorSacola(int idSacola) throws SQLException {
        String sql = "SELECT isc.*, v.* FROM ItemSacola isc JOIN Vinho v ON isc.id_vinho = v.id WHERE isc.id_sacola = ?";
        List<ItemSacola> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSacola);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemSacola item = new ItemSacola();
                    item.setIdSacola(rs.getInt("id_sacola"));
                    item.setIdVinho(rs.getInt("id_vinho"));
                    item.setQuantidade(rs.getInt("quantidade"));

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

    public ItemSacola buscarItem(int idSacola, int idVinho) throws SQLException {
        String sql = "SELECT isc.*, v.* FROM ItemSacola isc JOIN Vinho v ON isc.id_vinho = v.id WHERE isc.id_sacola = ? AND isc.id_vinho = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSacola);
            stmt.setInt(2, idVinho);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ItemSacola item = new ItemSacola();
                    item.setIdSacola(rs.getInt("id_sacola"));
                    item.setIdVinho(rs.getInt("id_vinho"));
                    item.setQuantidade(rs.getInt("quantidade"));

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

                    return item;
                }
            }
        }
        return null;
    }
}

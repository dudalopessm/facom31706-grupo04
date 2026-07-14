package lab05.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lab05.modelo.Vinho;

public class VinhoDao {
    private Connection connection;

    public VinhoDao(Connection connection) {
        this.connection = connection;
    }

    public void adiciona(Vinho vinho) {
        String sql = "INSERT INTO vinho (nome, safra, preco, id_categoria) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, vinho.getNome());
            stmt.setInt(2, vinho.getSafra());
            stmt.setDouble(3, vinho.getPreco());
            stmt.setInt(4, vinho.getIdCategoria());
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Vinho> getLista() {
        String sql = "SELECT v.*, c.nome as categoria_nome FROM vinho v JOIN categoria_vinho c ON c.id = v.id_categoria";
        List<Vinho> vinhos = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vinhos.add(mapVinho(rs));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vinhos;
    }

    public Vinho buscaPorNomeSafra(String nome, int safra) {
        String sql = "SELECT v.*, c.nome as categoria_nome FROM vinho v JOIN categoria_vinho c ON c.id = v.id_categoria WHERE v.nome=? AND v.safra=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setInt(2, safra);
            ResultSet rs = stmt.executeQuery();
            Vinho vinho = rs.next() ? mapVinho(rs) : null;
            rs.close();
            stmt.close();
            return vinho;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Vinho buscaPorId(int id) {
        String sql = "SELECT v.*, c.nome as categoria_nome FROM vinho v JOIN categoria_vinho c ON c.id = v.id_categoria WHERE v.id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Vinho vinho = rs.next() ? mapVinho(rs) : null;
            rs.close();
            stmt.close();
            return vinho;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Vinho mapVinho(ResultSet rs) throws SQLException {
        Vinho vinho = new Vinho();
        vinho.setId(rs.getInt("id"));
        vinho.setNome(rs.getString("nome"));
        vinho.setSafra(rs.getInt("safra"));
        vinho.setPreco(rs.getDouble("preco"));
        vinho.setIdCategoria(rs.getInt("id_categoria"));
        try { vinho.setCategoriaNome(rs.getString("categoria_nome")); } catch (SQLException e) { }
        return vinho;
    }

    public boolean altera(Vinho vinho) {
        String sql = "UPDATE vinho SET nome=?, safra=?, preco=?, id_categoria=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, vinho.getNome());
            stmt.setInt(2, vinho.getSafra());
            stmt.setDouble(3, vinho.getPreco());
            stmt.setInt(4, vinho.getIdCategoria());
            stmt.setInt(5, vinho.getId());
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean estaVazia() {
        String sql = "SELECT COUNT(*) FROM vinho";
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

    public boolean possuiItensPedido(int idVinho) {
        String sql = "SELECT COUNT(*) FROM item_pedido WHERE id_vinho = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idVinho);
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

    public boolean removePorNomeSafra(String nome, int safra) {
        String sql = "DELETE FROM vinho WHERE nome=? AND safra=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setInt(2, safra);
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

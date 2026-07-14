package lab07.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lab07.modelo.Vinho;

public class VinhoDao {
    private Connection connection;

    public VinhoDao(Connection connection) {
        this.connection = connection;
    }

    public List<Vinho> buscaPorTermo(String termo) {
        String sql = "SELECT v.*, c.nome as categoria_nome FROM vinho v JOIN categoria_vinho c ON c.id = v.id_categoria WHERE v.nome LIKE ?";
        List<Vinho> vinhos = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + termo + "%");
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
}

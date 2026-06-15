package lab04.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lab04.modelo.CategoriaVinho;

public class CategoriaVinhoDao {

    private Connection connection;

    public CategoriaVinhoDao(Connection connection) {
        this.connection = connection;
    }

    public void adiciona(CategoriaVinho categoria) {
        String sql = "INSERT INTO categoria_vinho (nome, descricao) VALUES (?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CategoriaVinho> getLista() {
        String sql = "SELECT * FROM categoria_vinho";
        List<CategoriaVinho> categorias = new ArrayList<CategoriaVinho>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CategoriaVinho categoria = new CategoriaVinho();
                categoria.setId(rs.getInt("id"));
                categoria.setNome(rs.getString("nome"));
                categoria.setDescricao(rs.getString("descricao"));
                categorias.add(categoria);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categorias;
    }

    public CategoriaVinho buscaPorId(int id) {
        String sql = "SELECT * FROM categoria_vinho WHERE id=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            CategoriaVinho categoria = null;
            if (rs.next()) {
                categoria = new CategoriaVinho();
                categoria.setId(rs.getInt("id"));
                categoria.setNome(rs.getString("nome"));
                categoria.setDescricao(rs.getString("descricao"));
            }

            rs.close();
            stmt.close();
            return categoria;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean altera(CategoriaVinho categoria) {
        String sql = "UPDATE categoria_vinho SET nome=?, descricao=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.setInt(3, categoria.getId());
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean possuiVinhos(int idCategoria) {
        String sql = "SELECT COUNT(*) FROM vinho WHERE id_categoria = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idCategoria);

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
    
    public boolean remove(CategoriaVinho categoria) {
        String sql = "DELETE FROM categoria_vinho WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, categoria.getId());
            int linhas = stmt.executeUpdate();
            stmt.close();
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

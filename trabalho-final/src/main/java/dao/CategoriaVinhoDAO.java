package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javaBeans.CategoriaVinho;

public class CategoriaVinhoDAO {

    public int inserir(CategoriaVinho categoria) throws SQLException {
        String sql = "INSERT INTO CategoriaVinho (nome, descricao) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public void alterar(CategoriaVinho categoria) throws SQLException {
        String sql = "UPDATE CategoriaVinho SET nome = ?, descricao = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.setInt(3, categoria.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM CategoriaVinho WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public CategoriaVinho buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM CategoriaVinho WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
        }
        return null;
    }

    public List<CategoriaVinho> listarTodos() throws SQLException {
        String sql = "SELECT * FROM CategoriaVinho ORDER BY nome";
        List<CategoriaVinho> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearCategoria(rs));
            }
        }
        return lista;
    }

    private CategoriaVinho mapearCategoria(ResultSet rs) throws SQLException {
        CategoriaVinho c = new CategoriaVinho();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setDescricao(rs.getString("descricao"));
        return c;
    }
}

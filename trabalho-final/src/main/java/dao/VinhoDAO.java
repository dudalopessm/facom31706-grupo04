package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javaBeans.Vinho;

public class VinhoDAO {

    public int inserir(Vinho vinho) throws SQLException {
        String sql = "INSERT INTO Vinho (nome, safra, descricao, preco, estoque, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vinho.getNome());
            stmt.setInt(2, vinho.getSafra());
            stmt.setString(3, vinho.getDescricao());
            stmt.setDouble(4, vinho.getPreco());
            stmt.setInt(5, vinho.getEstoque());
            stmt.setInt(6, vinho.getIdCategoria());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public void alterar(Vinho vinho) throws SQLException {
        String sql = "UPDATE Vinho SET nome = ?, safra = ?, descricao = ?, preco = ?, estoque = ?, id_categoria = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vinho.getNome());
            stmt.setInt(2, vinho.getSafra());
            stmt.setString(3, vinho.getDescricao());
            stmt.setDouble(4, vinho.getPreco());
            stmt.setInt(5, vinho.getEstoque());
            stmt.setInt(6, vinho.getIdCategoria());
            stmt.setInt(7, vinho.getId());
            stmt.executeUpdate();
        }
    }

    public void atualizarCaminhoFoto(int idVinho, String caminho) throws SQLException {
        String sql = "UPDATE Vinho SET caminho_foto = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, caminho);
            stmt.setInt(2, idVinho);
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM Vinho WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Vinho buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Vinho WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearVinho(rs);
                }
            }
        }
        return null;
    }

    public List<Vinho> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Vinho ORDER BY nome";
        List<Vinho> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearVinho(rs));
            }
        }
        return lista;
    }

    public List<Vinho> listarPorCategoria(int idCategoria) throws SQLException {
        String sql = "SELECT * FROM Vinho WHERE id_categoria = ? ORDER BY nome";
        List<Vinho> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearVinho(rs));
                }
            }
        }
        return lista;
    }

    private Vinho mapearVinho(ResultSet rs) throws SQLException {
        Vinho v = new Vinho();
        v.setId(rs.getInt("id"));
        v.setNome(rs.getString("nome"));
        v.setSafra(rs.getInt("safra"));
        v.setDescricao(rs.getString("descricao"));
        v.setPreco(rs.getDouble("preco"));
        v.setEstoque(rs.getInt("estoque"));
        v.setIdCategoria(rs.getInt("id_categoria"));
        v.setCaminhoFoto(rs.getString("caminho_foto"));
        return v;
    }
}

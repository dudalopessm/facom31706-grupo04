package lab00;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/InserirServlet")
public class InserirServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
            	    "jdbc:mysql://localhost:3306/lab00", "root", "admin");

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO pessoas (nome, email) VALUES (?, ?)");
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.executeUpdate();
            conn.close();

            response.sendRedirect("cadastro_teste.html?sucesso=1");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Erro: " + e.getMessage());
        }
    }
}
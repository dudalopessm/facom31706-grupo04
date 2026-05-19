package lab01;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/ConsultarServlet")
public class ConsultarServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<h2>Pessoas cadastradas:</h2>");
        out.println("<table border='1'><tr><th>ID</th><th>Nome</th><th>Email</th></tr>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
            	    "jdbc:mysql://localhost:3306/lab01", "root", "admin");

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM pessoas");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("nome") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("</tr>");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<tr><td colspan='3'>Erro: " + e.getMessage() + "</td></tr>");
        }

        out.println("</table>");
        out.println("<br><a href='cadastro_teste.html'>Voltar</a>");
    }
}
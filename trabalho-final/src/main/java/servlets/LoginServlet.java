package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ClienteDAO;
import javaBeans.Cliente;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (email == null || senha == null || email.trim().isEmpty() || senha.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?erro=campos_vazios");
            return;
        }

        try {
            ClienteDAO dao = new ClienteDAO();
            Cliente cliente = dao.buscarPorEmailESenha(email, senha);

            if (cliente != null) {
                HttpSession session = request.getSession();
                session.setAttribute("clienteLogado", cliente);
                response.sendRedirect("loja.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp?erro=invalido");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/erro.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}

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
            request.setAttribute("erro", "campos_vazios");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            ClienteDAO dao = new ClienteDAO();
            Cliente cliente = dao.buscarPorEmailESenha(email, senha);

            if (cliente != null) {
                HttpSession session = request.getSession();
                session.setAttribute("cliente", cliente);
                response.sendRedirect("loja.jsp");
            } else {
                request.setAttribute("erro", "invalido");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "erro_interno");
            request.getRequestDispatcher("erro.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}

package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ClienteDAO;
import javaBeans.Cliente;

@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String senha = request.getParameter("senha");
        String confirmarSenha = request.getParameter("confirmarSenha");

        if (email == null || nome == null || cpf == null || senha == null) {
            response.sendRedirect(request.getContextPath() + "/cadastro.jsp?erro=campos_vazios");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            response.sendRedirect(request.getContextPath() + "/cadastro.jsp?erro=senhas_diferentes");
            return;
        }

        try {
            ClienteDAO dao = new ClienteDAO();

            if (dao.buscarPorEmail(email) != null) {
                response.sendRedirect(request.getContextPath() + "/cadastro.jsp?erro=email_existente");
                return;
            }

            Cliente cliente = new Cliente();
            cliente.setEmail(email);
            cliente.setNome(nome);
            cliente.setCpf(cpf.replaceAll("\\D", ""));
            cliente.setSenha(senha);
            cliente.setTipo("CLIENTE");

            dao.inserir(cliente);
            response.sendRedirect(request.getContextPath() + "/login.jsp?cadastro=ok");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cadastro.jsp?erro=erro_interno");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/cadastro.jsp");
    }
}

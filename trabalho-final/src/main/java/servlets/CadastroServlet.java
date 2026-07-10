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
            request.setAttribute("erro", "campos_vazios");
            request.getRequestDispatcher("cadastro.jsp").forward(request, response);
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            request.setAttribute("erro", "senhas_diferentes");
            request.getRequestDispatcher("cadastro.jsp").forward(request, response);
            return;
        }

        try {
            ClienteDAO dao = new ClienteDAO();

            if (dao.buscarPorEmail(email) != null) {
                request.setAttribute("erro", "email_existente");
                request.getRequestDispatcher("cadastro.jsp").forward(request, response);
                return;
            }

            Cliente cliente = new Cliente();
            cliente.setEmail(email);
            cliente.setNome(nome);
            cliente.setCpf(cpf.replaceAll("\\D", ""));
            cliente.setSenha(senha);
            cliente.setTipo("CLIENTE");

            dao.inserir(cliente);
            request.setAttribute("cadastro", "ok");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "erro_interno");
            request.getRequestDispatcher("cadastro.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("cadastro.jsp").forward(request, response);
    }
}

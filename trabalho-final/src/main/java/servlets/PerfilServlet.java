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

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("clienteLogado") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Cliente cliente = (Cliente) session.getAttribute("clienteLogado");

        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String senhaAtual = request.getParameter("senhaAtual");
        String novaSenha = request.getParameter("novaSenha");
        String confirmarSenha = request.getParameter("confirmarSenha");

        if (nome == null || cpf == null || senhaAtual == null) {
            response.sendRedirect(request.getContextPath() + "/perfil.jsp?erroPerfil=campos_vazios");
            return;
        }

        try {
            ClienteDAO dao = new ClienteDAO();
            Cliente verificado = dao.buscarPorEmailESenha(cliente.getEmail(), senhaAtual);

            if (verificado == null) {
                response.sendRedirect(request.getContextPath() + "/perfil.jsp?erroPerfil=senha_atual_incorreta");
                return;
            }

            if (novaSenha != null && !novaSenha.isEmpty()) {
                if (!novaSenha.equals(confirmarSenha)) {
                    response.sendRedirect(request.getContextPath() + "/perfil.jsp?erroPerfil=senhas_nao_coincidem");
                    return;
                }
                cliente.setSenha(novaSenha);
            }

            cliente.setNome(nome);
            cliente.setCpf(cpf.replaceAll("\\D", ""));

            dao.alterar(cliente);
            session.setAttribute("clienteLogado", cliente);

            response.sendRedirect(request.getContextPath() + "/perfil.jsp?sucesso=ok");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/erro.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/perfil.jsp");
    }
}

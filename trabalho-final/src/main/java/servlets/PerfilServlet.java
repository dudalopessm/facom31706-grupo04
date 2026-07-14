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
            request.setAttribute("erroPerfil", "Preencha todos os campos obrigat\u00F3rios.");
            request.getRequestDispatcher("/perfil.jsp").forward(request, response);
            return;
        }

        try {
            ClienteDAO dao = new ClienteDAO();
            Cliente verificado = dao.buscarPorEmailESenha(cliente.getEmail(), senhaAtual);

            if (verificado == null) {
                request.setAttribute("erroPerfil", "Senha atual incorreta.");
                request.getRequestDispatcher("/perfil.jsp").forward(request, response);
                return;
            }

            if (novaSenha != null && !novaSenha.isEmpty()) {
                if (!novaSenha.equals(confirmarSenha)) {
                    request.setAttribute("erroPerfil", "A nova senha e a confirma\u00E7\u00E3o n\u00E3o coincidem.");
                    request.getRequestDispatcher("/perfil.jsp").forward(request, response);
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
            request.setAttribute("erroPerfil", "Erro ao atualizar perfil: " + e.getMessage());
            request.getRequestDispatcher("/perfil.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/perfil.jsp").forward(request, response);
    }
}

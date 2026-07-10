package lab07;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab07.dao.ClienteDao;
import lab07.modelo.Cliente;

@WebServlet("/CadastroServlet")
public class CadastroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String cpf = request.getParameter("cpf");
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        String destino;

        if (cpf == null || cpf.trim().isEmpty()
                || nome == null || nome.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || senha == null || senha.trim().isEmpty()) {
            request.setAttribute("erro", "Todos os campos sao obrigatorios.");
            destino = "/cadastro-erro.jsp";
        } else if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            request.setAttribute("erro", "CPF deve ter exatamente 11 digitos numericos.");
            destino = "/cadastro-erro.jsp";
        } else if (!email.contains("@")) {
            request.setAttribute("erro", "Email invalido.");
            destino = "/cadastro-erro.jsp";
        } else {
            try {
                Connection connection = new ConnectionFactory().getConnection();
                ClienteDao dao = new ClienteDao(connection);

                if (dao.buscaPorCpf(cpf) != null) {
                    request.setAttribute("erro", "Ja existe um cliente com o CPF " + cpf + ".");
                    connection.close();
                    destino = "/cadastro-erro.jsp";
                } else {
                    Cliente c = new Cliente();
                    c.setCpf(cpf);
                    c.setNome(nome);
                    c.setEmail(email);
                    c.setSenha(senha);
                    dao.adiciona(c);
                    connection.close();

                    request.setAttribute("nome", nome);
                    destino = "/cadastro-sucesso.jsp";
                }
            } catch (Exception e) {
                request.setAttribute("erro", "Erro no banco de dados: " + e.getMessage());
                destino = "/cadastro-erro.jsp";
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(destino);
        dispatcher.forward(request, response);
    }
}

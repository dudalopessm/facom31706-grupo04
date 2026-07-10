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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String cpf = request.getParameter("usuario");
        String senha = request.getParameter("senha");

        String destino;

        try {
            Connection connection = new ConnectionFactory().getConnection();
            ClienteDao dao = new ClienteDao(connection);
            Cliente cliente = dao.buscaPorCpfESenha(cpf, senha);
            connection.close();

            if (cliente != null) {
                request.setAttribute("usuario", cliente.getNome());
                destino = "/sucesso.jsp";
            } else {
                request.setAttribute("erro", "CPF ou senha invalidos.");
                destino = "/erro.jsp";
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro no banco de dados: " + e.getMessage());
            destino = "/erro.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(destino);
        dispatcher.forward(request, response);
    }
}

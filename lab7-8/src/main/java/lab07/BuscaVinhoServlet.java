package lab07;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab07.dao.VinhoDao;
import lab07.modelo.Vinho;

@WebServlet("/BuscaVinhoServlet")
public class BuscaVinhoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String nome = request.getParameter("nome");
        String safraStr = request.getParameter("safra");
        String destino;

        try {
            int safra = Integer.parseInt(safraStr);

            Connection connection = new ConnectionFactory().getConnection();
            VinhoDao dao = new VinhoDao(connection);
            Vinho vinho = dao.buscaPorNomeSafra(nome, safra);
            connection.close();

            if (vinho != null) {
                destino = "/vinho-detalhe.jsp?id=" + vinho.getId();
            } else {
                request.setAttribute("buscaErro", "Nenhum vinho encontrado para \"" + nome + "\" safra " + safra + ".");
                destino = "/vinho-erro.jsp";
            }

        } catch (NumberFormatException e) {
            request.setAttribute("buscaErro", "Safra deve ser um numero valido.");
            destino = "/vinho-erro.jsp";
        } catch (Exception e) {
            request.setAttribute("buscaErro", "Erro ao buscar vinho: " + e.getMessage());
            destino = "/vinho-erro.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(destino);
        dispatcher.forward(request, response);
    }
}

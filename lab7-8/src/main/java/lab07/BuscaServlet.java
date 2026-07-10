package lab07;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab07.dao.VinhoDao;
import lab07.modelo.Vinho;

@WebServlet("/BuscaServlet")
public class BuscaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String termo = request.getParameter("termo");
        String destino;

        if (termo == null || termo.trim().isEmpty()) {
            request.setAttribute("erro", "Digite um termo para busca.");
            destino = "/busca-erro.jsp";
        } else {
            try {
                Connection connection = new ConnectionFactory().getConnection();
                VinhoDao dao = new VinhoDao(connection);
                List<Vinho> resultados = dao.buscaPorTermo(termo);
                connection.close();

                if (resultados.isEmpty()) {
                    request.setAttribute("erro", "Nenhum vinho encontrado para \"" + termo + "\".");
                    destino = "/busca-erro.jsp";
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<table>");
                    sb.append("<tr><th>ID</th><th>Nome</th><th>Safra</th><th>Preco</th><th>Categoria</th></tr>");
                    for (Vinho v : resultados) {
                        sb.append("<tr>");
                        sb.append("<td>").append(v.getId()).append("</td>");
                        sb.append("<td>").append(v.getNome()).append("</td>");
                        sb.append("<td>").append(v.getSafra()).append("</td>");
                        sb.append("<td>R$ ").append(String.format("%.2f", v.getPreco())).append("</td>");
                        sb.append("<td>").append(v.getCategoriaNome()).append("</td>");
                        sb.append("</tr>");
                    }
                    sb.append("</table>");

                    request.setAttribute("termo", termo);
                    request.setAttribute("mensagem", sb.toString());
                    destino = "/busca-resultado.jsp";
                }
            } catch (Exception e) {
                request.setAttribute("erro", "Erro no banco de dados: " + e.getMessage());
                destino = "/busca-erro.jsp";
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(destino);
        dispatcher.forward(request, response);
    }
}

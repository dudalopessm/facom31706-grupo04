package lab04;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import lab04.dao.VinhoDao;
import lab04.modelo.Vinho;

@WebServlet("/VinhoServlet")
public class VinhoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String acao = request.getParameter("acao");

        out.println("<!DOCTYPE html>");
        out.println("<html lang='pt-BR'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Cave Fontana</title>");
        out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css'>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='page'>");

        out.println("<div class='header'>");
        out.println("<div class='logo'><i class='ti ti-bottle'></i></div>");
        out.println("<div>");
        out.println("<div class='page-title'>Cave Fontana</div>");
        out.println("<div class='page-sub'>Vinhos</div>");
        out.println("</div>");
        out.println("</div>");

        out.println("<div class='card'>");

        Connection connection = new ConnectionFactory().getConnection();
        VinhoDao dao = new VinhoDao(connection);

        try {
            if ("inserir".equals(acao)) {
                Vinho v1 = new Vinho();
                v1.setNome("Salton Septimum");
                v1.setSafra(2020);
                v1.setPreco(204.9);
                v1.setIdCategoria(1);
                dao.adiciona(v1);
                out.println("<h2>Vinho inserido com sucesso!</h2>");
                out.println("<p>Salton Septimum (2020, R$ 204,90)</p>");

            } else if ("alterar".equals(acao)) {
                Vinho vAlterar = new Vinho();
                vAlterar.setId(1);
                vAlterar.setNome("Cabernet Sauvignon Gran Reserva");
                vAlterar.setSafra(2017);
                vAlterar.setPreco(159.90);
                vAlterar.setIdCategoria(1);
                boolean alterado = dao.altera(vAlterar);
                if (alterado) {
                    out.println("<h2>Vinho alterado com sucesso!</h2>");
                    out.println("<p>Vinho id=1 atualizado para: Cabernet Sauvignon Gran Reserva (2017, R$ 159,90)</p>");
                } else {
                    out.println("<h2>Registro não encontrado.</h2>");
                    out.println("<p>Nenhum vinho com id=1 existe na base.</p>");
                }
            } else if ("remover".equals(acao)) {
                Vinho vinho = new Vinho();
                vinho.setId(1);
                if (dao.possuiItensPedido(vinho.getId())) {
                    out.println("Não é possível remover este vinho, pois ele faz parte de um ou mais pedidos.");
                    return;
                }
                boolean removido = dao.remove(vinho);
                if (removido) {
                    out.println("Vinho removido com sucesso!");
                } else {
                    out.println("Vinho não encontrado.");
                }
            } else {
                List<Vinho> vinhos = dao.getLista();
                out.println("<h2>Vinhos cadastrados</h2>");
                out.println("<table><tr><th>ID</th><th>Nome</th><th>Safra</th><th>Preço</th><th>ID Categoria</th></tr>");
                for (Vinho v : vinhos) {
                    out.println("<tr>");
                    out.println("<td>" + v.getId() + "</td>");
                    out.println("<td>" + v.getNome() + "</td>");
                    out.println("<td>" + v.getSafra() + "</td>");
                    out.println("<td>R$ " + String.format("%.2f", v.getPreco()) + "</td>");
                    out.println("<td>" + v.getIdCategoria() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Erro: " + e.getMessage() + "</p>");
        }

        out.println("</div>");
        out.println("<a class='back' href='index.html'>&#8592; Voltar</a>");
        out.println("</div></body></html>");
    }
}
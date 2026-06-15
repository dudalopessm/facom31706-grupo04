package lab04;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import lab04.dao.ItemPedidoDao;
import lab04.dao.PedidoDao;
import lab04.modelo.ItemPedido;
import lab04.modelo.Pedido;

@WebServlet("/PedidoServlet")
public class PedidoServlet extends HttpServlet {

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
        out.println("<div class='page-sub'>Pedidos</div>");
        out.println("</div>");
        out.println("</div>");

        out.println("<div class='card'>");

        Connection connection = new ConnectionFactory().getConnection();
        PedidoDao dao = new PedidoDao(connection);
        ItemPedidoDao itemDao = new ItemPedidoDao(connection);

        try {
            if ("inserir".equals(acao)) {
                Pedido p1 = new Pedido();
                p1.setIdCliente(3);
                p1.setStatus("pendente");
                dao.adiciona(p1);

                ItemPedido i1 = new ItemPedido();
                i1.setIdPedido(p1.getId());
                i1.setIdVinho(2);
                i1.setQuantidade(3);
                i1.setPrecoUnitario(54.50);
                itemDao.adiciona(i1);

                ItemPedido i2 = new ItemPedido();
                i2.setIdPedido(p1.getId());
                i2.setIdVinho(4);
                i2.setQuantidade(1);
                i2.setPrecoUnitario(145.00);
                itemDao.adiciona(i2);

                out.println("<h2>Pedido inserido com sucesso!</h2>");
                out.println("<p>Pedido #" + p1.getId() + " (pendente) com 2 itens.</p>");

            } else if ("alterar".equals(acao)) {
                Pedido pAlterar = new Pedido();
                pAlterar.setId(1);
                pAlterar.setIdCliente(1);
                pAlterar.setStatus("enviado");
                boolean alterado = dao.altera(pAlterar);
                if (alterado) {
                    out.println("<h2>Pedido alterado com sucesso!</h2>");
                    out.println("<p>Pedido id=1 atualizado: status 'enviado'</p>");
                } else {
                    out.println("<h2>Registro não encontrado.</h2>");
                    out.println("<p>Nenhum pedido com id=1 existe na base.</p>");
                }


            } else if ("remover".equals(acao)) {
            	Pedido pedido = new Pedido();
                pedido.setId(1);
                if (dao.possuiItens(pedido.getId())) {
                    out.println("Não é possível remover este pedido, pois ele possui itens cadastrados.");
                    return;
                }
                boolean removido = dao.remove(pedido);
                if (removido) {
                    out.println("Pedido removido com sucesso!");
                } else {
                    out.println("Pedido não encontrado.");
                }
            } else {
                List<Pedido> pedidos = dao.getLista();
                out.println("<h2>Pedidos cadastrados</h2>");
                out.println("<table><tr><th>ID</th><th>ID Cliente</th><th>Data</th><th>Status</th></tr>");
                for (Pedido p : pedidos) {
                    out.println("<tr>");
                    out.println("<td>" + p.getId() + "</td>");
                    out.println("<td>" + p.getIdCliente() + "</td>");
                    out.println("<td>" + p.getDataPedido() + "</td>");
                    out.println("<td>" + p.getStatus() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");

                out.println("</div><div class='card'>");

                List<ItemPedido> itens = itemDao.getLista();
                out.println("<h2>Itens dos pedidos</h2>");
                out.println("<table><tr><th>ID</th><th>ID Pedido</th><th>ID Vinho</th><th>Quantidade</th><th>Preço Unit.</th></tr>");
                for (ItemPedido item : itens) {
                    out.println("<tr>");
                    out.println("<td>" + item.getId() + "</td>");
                    out.println("<td>" + item.getIdPedido() + "</td>");
                    out.println("<td>" + item.getIdVinho() + "</td>");
                    out.println("<td>" + item.getQuantidade() + "</td>");
                    out.println("<td>R$ " + String.format("%.2f", item.getPrecoUnitario()) + "</td>");
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
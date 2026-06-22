package lab05;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import lab05.dao.ItemPedidoDao;
import lab05.dao.PedidoDao;
import lab05.dao.VinhoDao;
import lab05.modelo.ItemPedido;
import lab05.modelo.Pedido;
import lab05.modelo.Vinho;

@WebServlet("/PedidoServlet")
public class PedidoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String acao = request.getParameter("acao");

        out.println("<!DOCTYPE html>");
        out.println("<html lang='pt-BR'><head><meta charset='UTF-8'>");
        out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css'>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head><body><div style='padding:8px;'>");

        Connection connection = new ConnectionFactory().getConnection();
        PedidoDao dao = new PedidoDao(connection);
        ItemPedidoDao itemDao = new ItemPedidoDao(connection);
        VinhoDao vinhoDao = new VinhoDao(connection);

        try {
            if ("inserir".equals(acao)) {
                String clienteCpf = request.getParameter("clienteCpf");
                String status = request.getParameter("status");
                String vinhoNome = request.getParameter("vinhoNome");
                int safra = parseInt(request.getParameter("safra"));
                int quantidade = Integer.parseInt(request.getParameter("quantidade"));

                Vinho vinho = vinhoDao.buscaPorNomeSafra(vinhoNome, safra);
                if (vinho == null) {
                    out.println("<p class='result-msg error'>Vinho \"" + vinhoNome + "\" safra " + safra + " n\u00e3o encontrado. Verifique o nome e a safra.</p>");
                } else {
                    Pedido p = new Pedido();
                    p.setClienteCpf(clienteCpf);
                    p.setStatus(status);
                    dao.adiciona(p);

                    ItemPedido item = new ItemPedido();
                    item.setIdPedido(p.getId());
                    item.setIdVinho(vinho.getId());
                    item.setQuantidade(quantidade);
                    item.setPrecoUnitario(vinho.getPreco());
                    itemDao.adiciona(item);

                    out.println("<p class='result-msg success'>Pedido #" + p.getId() + " inserido com sucesso com 1 item (\"" + vinhoNome + "\" qtd: " + quantidade + ")!</p>");
                }

            } else if ("alterar".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String clienteCpf = request.getParameter("clienteCpf");
                String status = request.getParameter("status");
                Pedido atual = dao.buscaPorId(id);
                if (atual == null) {
                    out.println("<p class='result-msg error'>Pedido id=" + id + " n\u00e3o encontrado.</p>");
                } else if (atual.getClienteCpf().equals(clienteCpf) && atual.getStatus().equals(status)) {
                    out.println("<p class='result-msg info'>Nenhuma altera\u00e7\u00e3o foi feita. Os dados s\u00e3o id\u00eanticos.</p>");
                } else {
                    Pedido p = new Pedido();
                    p.setId(id);
                    p.setClienteCpf(clienteCpf);
                    p.setStatus(status);
                    dao.altera(p);
                    out.println("<p class='result-msg success'>Pedido id=" + id + " alterado com sucesso!</p>");
                }

            } else if ("remover".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                if (dao.possuiItens(id)) {
                    out.println("<p class='result-msg info'>N\u00e3o \u00e9 poss\u00edvel remover: pedido possui itens cadastrados.</p>");
                } else if (dao.remove(id)) {
                    out.println("<p class='result-msg success'>Pedido id=" + id + " removido com sucesso!</p>");
                } else {
                    out.println("<p class='result-msg error'>Pedido id=" + id + " n\u00e3o encontrado.</p>");
                }

            } else if ("removerPorCpfData".equals(acao)) {
                String clienteCpf = request.getParameter("clienteCpf");
                String data = request.getParameter("data");
                if (dao.removePorClienteCpfEData(clienteCpf, data)) {
                    out.println("<p class='result-msg success'>Pedido(s) do CPF " + clienteCpf + " na data " + data + " removido(s) com sucesso!</p>");
                } else {
                    out.println("<p class='result-msg error'>Nenhum pedido encontrado para CPF " + clienteCpf + " na data " + data + ".</p>");
                }

            } else if ("inserirItem".equals(acao)) {
                int idPedido = Integer.parseInt(request.getParameter("idPedido"));
                String vinhoNome = request.getParameter("vinhoNome");
                int safra = parseInt(request.getParameter("safra"));
                int quantidade = Integer.parseInt(request.getParameter("quantidade"));

                Vinho vinho = vinhoDao.buscaPorNomeSafra(vinhoNome, safra);
                if (vinho == null) {
                    out.println("<p class='result-msg error'>Vinho \"" + vinhoNome + "\" safra " + safra + " n\u00e3o encontrado. Verifique o nome e a safra.</p>");
                } else {
                    ItemPedido item = new ItemPedido();
                    item.setIdPedido(idPedido);
                    item.setIdVinho(vinho.getId());
                    item.setQuantidade(quantidade);
                    item.setPrecoUnitario(vinho.getPreco());
                    itemDao.adiciona(item);
                    out.println("<p class='result-msg success'>Item (\"" + vinhoNome + "\" qtd: " + quantidade + ") adicionado ao pedido #" + idPedido + " com sucesso!</p>");
                }

            } else if ("removerItem".equals(acao)) {
                int idPedido = Integer.parseInt(request.getParameter("idPedido"));
                String vinhoNome = request.getParameter("vinhoNome");
                int safra = parseInt(request.getParameter("safra"));
                int qtdRemover = Integer.parseInt(request.getParameter("quantidade"));
                String dataItem = request.getParameter("dataItem");

                Vinho vinho = vinhoDao.buscaPorNomeSafra(vinhoNome, safra);
                if (vinho == null) {
                    out.println("<p class='result-msg error'>Vinho \"" + vinhoNome + "\" safra " + safra + " n\u00e3o encontrado.</p>");
                } else {
                    ItemPedido item = itemDao.buscaPorPedidoVinhoData(idPedido, vinho.getId(), dataItem);
                    if (item == null) {
                        out.println("<p class='result-msg error'>Item n\u00e3o encontrado no pedido #" + idPedido + " com essa data.</p>");
                    } else if (qtdRemover > item.getQuantidade()) {
                        out.println("<p class='result-msg error'>Quantidade a remover (" + qtdRemover + ") maior que a quantidade existente (" + item.getQuantidade() + ").</p>");
                    } else if (qtdRemover == item.getQuantidade()) {
                        itemDao.remove(item.getId());
                        out.println("<p class='result-msg success'>Item \"" + vinhoNome + "\" removido completamente do pedido #" + idPedido + ".</p>");
                    } else {
                        int novaQtd = item.getQuantidade() - qtdRemover;
                        itemDao.atualizaQuantidade(item.getId(), novaQtd);
                        out.println("<p class='result-msg success'>Quantidade do item \"" + vinhoNome + "\" atualizada para " + novaQtd + " no pedido #" + idPedido + ".</p>");
                    }
                }

            } else if ("buscar".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Pedido p = dao.buscaPorId(id);
                if (p == null) {
                    out.println("<p class='result-msg error'>Pedido id=" + id + " n\u00e3o encontrado.</p>");
                } else {
                    out.println("<h3>Pedido</h3>");
                    out.println("<table><tr><th>ID</th><th>CPF Cliente</th><th>Data</th><th>Status</th></tr>");
                    out.println("<tr><td>" + p.getId() + "</td><td>" + p.getClienteCpf()
                        + "</td><td>" + p.getDataPedido() + "</td><td>" + p.getStatus() + "</td></tr>");
                    out.println("</table>");

                    List<ItemPedido> itens = itemDao.getListaPorPedido(id);
                    if (!itens.isEmpty()) {
                        out.println("<h3>Itens do pedido</h3>");
                        out.println("<table><tr><th>ID</th><th>Vinho</th><th>Quantidade</th><th>Pre\u00e7o Unit.</th><th>Data/Hora</th></tr>");
                        for (ItemPedido item : itens) {
                            out.println("<tr><td>" + item.getId() + "</td><td>" + item.getVinhoNome()
                                + "</td><td>" + item.getQuantidade()
                                + "</td><td>R$ " + String.format("%.2f", item.getPrecoUnitario())
                                + "</td><td>" + item.getDataItem() + "</td></tr>");
                        }
                        out.println("</table>");
                    }
                }

            } else if ("buscarPorCpf".equals(acao)) {
                String clienteCpf = request.getParameter("clienteCpf");
                List<Pedido> pedidos = dao.buscaPorClienteCpf(clienteCpf);
                if (pedidos.isEmpty()) {
                    out.println("<p class='result-msg error'>Nenhum pedido encontrado para o CPF " + clienteCpf + ".</p>");
                } else {
                    out.println("<h3>Pedidos do CPF " + clienteCpf + "</h3>");
                    out.println("<table><tr><th>ID</th><th>CPF Cliente</th><th>Data</th><th>Status</th></tr>");
                    for (Pedido p : pedidos) {
                        out.println("<tr><td>" + p.getId() + "</td><td>" + p.getClienteCpf()
                            + "</td><td>" + p.getDataPedido() + "</td><td>" + p.getStatus() + "</td></tr>");
                    }
                    out.println("</table>");
                }

            } else {
                List<Pedido> pedidos = dao.getLista();
                out.println("<h3>Pedidos</h3>");
                out.println("<table><tr><th>ID</th><th>CPF Cliente</th><th>Data</th><th>Status</th></tr>");
                for (Pedido p : pedidos) {
                    out.println("<tr><td>" + p.getId() + "</td><td>" + p.getClienteCpf()
                        + "</td><td>" + p.getDataPedido() + "</td><td>" + p.getStatus() + "</td></tr>");
                }
                out.println("</table>");

                List<ItemPedido> itens = itemDao.getLista();
                out.println("<h3>Itens dos pedidos</h3>");
                out.println("<table><tr><th>ID</th><th>ID Pedido</th><th>Vinho</th><th>Quantidade</th><th>Pre\u00e7o Unit.</th><th>Data/Hora</th></tr>");
                for (ItemPedido item : itens) {
                    out.println("<tr><td>" + item.getId() + "</td><td>" + item.getIdPedido()
                        + "</td><td>" + item.getVinhoNome() + "</td><td>" + item.getQuantidade()
                        + "</td><td>R$ " + String.format("%.2f", item.getPrecoUnitario())
                        + "</td><td>" + item.getDataItem() + "</td></tr>");
                }
                out.println("</table>");
            }

            connection.close();
        } catch (Exception e) {
            out.println("<p class='result-msg error'>Erro: " + e.getMessage() + "</p>");
        }

        out.println("</div></body></html>");
    }

    private int parseInt(String s) {
        if (s == null || s.trim().isEmpty()) return 0;
        return Integer.parseInt(s.trim());
    }
}

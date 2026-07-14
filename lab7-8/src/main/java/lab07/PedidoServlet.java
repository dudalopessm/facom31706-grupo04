package lab07;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import lab07.dao.ClienteDao;
import lab07.dao.ItemPedidoDao;
import lab07.dao.PedidoDao;
import lab07.dao.VinhoDao;
import lab07.modelo.ItemPedido;
import lab07.modelo.Pedido;
import lab07.modelo.Vinho;

@WebServlet("/PedidoServlet")
public class PedidoServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        getServletContext().log("PedidoServlet inicializado");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        super.service(request, response);
    }

    @Override
    public void destroy() {
        getServletContext().log("PedidoServlet finalizado");
        super.destroy();
    }

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
        ClienteDao clienteDao = new ClienteDao(connection);

        try {
            if ("inserir".equals(acao)) {
                String clienteCpf = request.getParameter("clienteCpf");
                String status = request.getParameter("status");

                if (clienteDao.buscaPorCpf(clienteCpf) == null) {
                    out.println("<p class='result-msg error'>Cliente CPF " + clienteCpf + " nao encontrado. Cadastre o cliente primeiro.</p>");
                } else {
                    Pedido p = new Pedido();
                    p.setClienteCpf(clienteCpf);
                    p.setStatus(status);
                    dao.adiciona(p);
                    out.println("<p class='result-msg success'>Pedido #" + p.getId() + " inserido com sucesso!</p>");
                }

            } else if ("alterar".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String clienteCpf = request.getParameter("clienteCpf");
                String status = request.getParameter("status");

                if (clienteDao.buscaPorCpf(clienteCpf) == null) {
                    out.println("<p class='result-msg error'>Cliente CPF " + clienteCpf + " nao encontrado. Cadastre o cliente primeiro.</p>");
                } else {
                    Pedido atual = dao.buscaPorId(id);
                    if (atual == null) {
                        out.println("<p class='result-msg error'>Pedido id=" + id + " nao encontrado.</p>");
                    } else if (atual.getClienteCpf().equals(clienteCpf) && atual.getStatus().equals(status)) {
                        out.println("<p class='result-msg info'>Nenhuma alteracao foi feita. Os dados sao identicos.</p>");
                    } else {
                        Pedido p = new Pedido();
                        p.setId(id);
                        p.setClienteCpf(clienteCpf);
                        p.setStatus(status);
                        dao.altera(p);
                        out.println("<p class='result-msg success'>Pedido id=" + id + " alterado com sucesso!</p>");
                    }
                }

            } else if ("remover".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                if (dao.possuiItens(id)) {
                    out.println("<p class='result-msg info'>Nao e possivel remover: pedido possui itens cadastrados.</p>");
                } else if (dao.remove(id)) {
                    out.println("<p class='result-msg success'>Pedido id=" + id + " removido com sucesso!</p>");
                } else {
                    out.println("<p class='result-msg error'>Pedido id=" + id + " nao encontrado.</p>");
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

                if (dao.buscaPorId(idPedido) == null) {
                    out.println("<p class='result-msg error'>Pedido id=" + idPedido + " nao encontrado. Crie o pedido primeiro.</p>");
                } else {
                    Vinho vinho = vinhoDao.buscaPorNomeSafra(vinhoNome, safra);
                    if (vinho == null) {
                        out.println("<p class='result-msg error'>Vinho \"" + vinhoNome + "\" safra " + safra + " nao encontrado. Crie o vinho primeiro.</p>");
                    } else {
                        ItemPedido existente = itemDao.buscaPorPedidoVinho(idPedido, vinho.getId());
                        if (existente != null) {
                            int novaQtd = existente.getQuantidade() + quantidade;
                            itemDao.atualizaQuantidade(existente.getId(), novaQtd);
                            out.println("<p class='result-msg success'>Quantidade do item (\"" + vinhoNome + "\") atualizada para " + novaQtd + " no pedido #" + idPedido + ".</p>");
                        } else {
                            ItemPedido item = new ItemPedido();
                            item.setIdPedido(idPedido);
                            item.setIdVinho(vinho.getId());
                            item.setQuantidade(quantidade);
                            item.setPrecoUnitario(vinho.getPreco());
                            itemDao.adiciona(item);
                            out.println("<p class='result-msg success'>Item (\"" + vinhoNome + "\" qtd: " + quantidade + ") adicionado ao pedido #" + idPedido + " com sucesso!</p>");
                        }
                    }
                }

            } else if ("removerItem".equals(acao)) {
                int idPedido = Integer.parseInt(request.getParameter("idPedido"));
                String vinhoNome = request.getParameter("vinhoNome");
                int safra = parseInt(request.getParameter("safra"));
                int qtdRemover = Integer.parseInt(request.getParameter("quantidade"));
                String dataItem = request.getParameter("dataItem");

                Vinho vinho = vinhoDao.buscaPorNomeSafra(vinhoNome, safra);
                if (vinho == null) {
                    out.println("<p class='result-msg error'>Vinho \"" + vinhoNome + "\" safra " + safra + " nao encontrado.</p>");
                } else {
                    ItemPedido item = itemDao.buscaPorPedidoVinhoData(idPedido, vinho.getId(), dataItem);
                    if (item == null) {
                        out.println("<p class='result-msg error'>Item nao encontrado no pedido #" + idPedido + " com essa data.</p>");
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

            } else if ("alterarItem".equals(acao)) {
                int idPedido = Integer.parseInt(request.getParameter("idPedido"));
                String vinhoNome = request.getParameter("vinhoNome");
                int safra = parseInt(request.getParameter("safra"));
                String dataItem = request.getParameter("dataItem");
                int novaQuantidade = Integer.parseInt(request.getParameter("novaQuantidade"));

                Vinho vinho = vinhoDao.buscaPorNomeSafra(vinhoNome, safra);
                if (vinho == null) {
                    out.println("<p class='result-msg error'>Vinho \"" + vinhoNome + "\" safra " + safra + " nao encontrado.</p>");
                } else {
                    ItemPedido item = itemDao.buscaPorPedidoVinhoData(idPedido, vinho.getId(), dataItem);
                    if (item == null) {
                        out.println("<p class='result-msg error'>Item nao encontrado no pedido #" + idPedido + " com essa data.</p>");
                    } else if (item.getQuantidade() == novaQuantidade) {
                        out.println("<p class='result-msg info'>Nenhuma alteracao foi feita. Os dados sao identicos.</p>");
                    } else {
                        itemDao.atualizaQuantidade(item.getId(), novaQuantidade);
                        out.println("<p class='result-msg success'>Quantidade do item \"" + vinhoNome + "\" atualizada para " + novaQuantidade + " no pedido #" + idPedido + ".</p>");
                    }
                }

            } else if ("listarItens".equals(acao)) {
                List<ItemPedido> itens = itemDao.getLista();
                out.println("<h3>Todos os itens dos pedidos</h3>");
                out.println("<table><tr><th>ID</th><th>ID Pedido</th><th>Vinho</th><th>Quantidade</th><th>Preco Unit.</th><th>Data/Hora</th></tr>");
                for (ItemPedido item : itens) {
                    out.println("<tr><td>" + item.getId() + "</td><td>" + item.getIdPedido()
                        + "</td><td>" + item.getVinhoNome() + "</td><td>" + item.getQuantidade()
                        + "</td><td>R$ " + String.format("%.2f", item.getPrecoUnitario())
                        + "</td><td>" + item.getDataItem() + "</td></tr>");
                }
                out.println("</table>");

            } else if ("buscar".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Pedido p = dao.buscaPorId(id);
                if (p == null) {
                    out.println("<p class='result-msg error'>Pedido id=" + id + " nao encontrado.</p>");
                } else {
                    out.println("<h3>Pedido</h3>");
                    out.println("<table><tr><th>ID</th><th>CPF Cliente</th><th>Data</th><th>Status</th></tr>");
                    out.println("<tr><td>" + p.getId() + "</td><td>" + p.getClienteCpf()
                        + "</td><td>" + p.getDataPedido() + "</td><td>" + p.getStatus() + "</td></tr>");
                    out.println("</table>");

                    List<ItemPedido> itens = itemDao.getListaPorPedido(id);
                    if (!itens.isEmpty()) {
                        out.println("<h3>Itens do pedido</h3>");
                        out.println("<table><tr><th>ID</th><th>Vinho</th><th>Quantidade</th><th>Preco Unit.</th><th>Data/Hora</th></tr>");
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

            } else if ("buscarItensPorPedido".equals(acao)) {
                int idPedido = Integer.parseInt(request.getParameter("idPedido"));
                List<ItemPedido> itens = itemDao.getListaPorPedido(idPedido);
                if (itens.isEmpty()) {
                    out.println("<p class='result-msg info'>Nenhum item encontrado no pedido #" + idPedido + ".</p>");
                } else {
                    out.println("<h3>Itens do pedido #" + idPedido + "</h3>");
                    out.println("<table><tr><th>ID</th><th>Vinho</th><th>Quantidade</th><th>Preco Unit.</th><th>Data/Hora</th></tr>");
                    for (ItemPedido item : itens) {
                        out.println("<tr><td>" + item.getId() + "</td><td>" + item.getVinhoNome()
                            + "</td><td>" + item.getQuantidade()
                            + "</td><td>R$ " + String.format("%.2f", item.getPrecoUnitario())
                            + "</td><td>" + item.getDataItem() + "</td></tr>");
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
            }

            connection.close();
        } catch (NumberFormatException e) {
            out.println("<p class='result-msg error'>Valor invalido para um campo numerico. Verifique os dados informados.</p>");
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

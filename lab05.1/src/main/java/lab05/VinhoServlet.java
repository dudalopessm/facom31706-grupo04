package lab05;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import lab05.dao.CategoriaVinhoDao;
import lab05.dao.VinhoDao;
import lab05.modelo.Vinho;

@WebServlet("/VinhoServlet")
public class VinhoServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        getServletContext().log("VinhoServlet inicializado");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        super.service(request, response);
    }

    @Override
    public void destroy() {
        getServletContext().log("VinhoServlet finalizado");
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
        VinhoDao dao = new VinhoDao(connection);
        CategoriaVinhoDao categoriaDao = new CategoriaVinhoDao(connection);

        try {
            if ("inserir".equals(acao)) {
                if (categoriaDao.estaVazia()) {
                    imprimeErroDependencia(out,
                        "Para se ter um vinho precisamos de uma categoria primeiro. Crie uma categoria de vinhos",
                        "categoria_vinho.html", "Ir para Categorias de vinho");
                } else {
                String nome = request.getParameter("nome");
                int safra = parseInt(request.getParameter("safra"));
                double preco = Double.parseDouble(request.getParameter("preco"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                Vinho v = new Vinho();
                v.setNome(nome);
                v.setSafra(safra);
                v.setPreco(preco);
                v.setIdCategoria(idCategoria);
                dao.adiciona(v);
                out.println("<p class='result-msg success'>Vinho \"" + nome + "\" inserido com sucesso!</p>");
                }

            } else if ("alterar".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String nome = request.getParameter("nome");
                int safra = parseInt(request.getParameter("safra"));
                double preco = Double.parseDouble(request.getParameter("preco"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                Vinho atual = dao.buscaPorId(id);
                if (atual == null) {
                    out.println("<p class='result-msg error'>Vinho id=" + id + " n\u00e3o encontrado.</p>");
                } else if (atual.getNome().equals(nome) && atual.getSafra() == safra
                        && Math.abs(atual.getPreco() - preco) < 0.001 && atual.getIdCategoria() == idCategoria) {
                    out.println("<p class='result-msg info'>Nenhuma altera\u00e7\u00e3o foi feita. Os dados s\u00e3o id\u00eanticos.</p>");
                } else {
                    Vinho v = new Vinho();
                    v.setId(id);
                    v.setNome(nome);
                    v.setSafra(safra);
                    v.setPreco(preco);
                    v.setIdCategoria(idCategoria);
                    dao.altera(v);
                    out.println("<p class='result-msg success'>Vinho id=" + id + " alterado com sucesso!</p>");
                }

            } else if ("remover".equals(acao)) {
                String nome = request.getParameter("nome");
                int safra = parseInt(request.getParameter("safra"));
                Vinho v = dao.buscaPorNomeSafra(nome, safra);
                if (v == null) {
                    out.println("<p class='result-msg error'>Vinho \"" + nome + "\" safra " + safra + " n\u00e3o encontrado.</p>");
                } else if (dao.possuiItensPedido(v.getId())) {
                    out.println("<p class='result-msg info'>N\u00e3o \u00e9 poss\u00edvel remover: vinho faz parte de um ou mais pedidos.</p>");
                } else if (dao.removePorNomeSafra(nome, safra)) {
                    out.println("<p class='result-msg success'>Vinho \"" + nome + "\" safra " + safra + " removido com sucesso!</p>");
                } else {
                    out.println("<p class='result-msg error'>Vinho \"" + nome + "\" safra " + safra + " n\u00e3o encontrado.</p>");
                }

            } else if ("buscar".equals(acao)) {
                String nome = request.getParameter("nome");
                int safra = parseInt(request.getParameter("safra"));
                Vinho v = dao.buscaPorNomeSafra(nome, safra);
                if (v == null) {
                    out.println("<p class='result-msg error'>Vinho \"" + nome + "\" safra " + safra + " n\u00e3o encontrado.</p>");
                } else {
                    out.println("<table><tr><th>ID</th><th>Nome</th><th>Safra</th><th>Pre\u00e7o</th><th>ID Categoria</th></tr>");
                    out.println("<tr><td>" + v.getId() + "</td><td>" + v.getNome() + "</td><td>" + v.getSafra()
                        + "</td><td>R$ " + String.format("%.2f", v.getPreco()) + "</td><td>" + v.getIdCategoria() + "</td></tr>");
                    out.println("</table>");
                }

            } else {
                List<Vinho> vinhos = dao.getLista();
                out.println("<table><tr><th>ID</th><th>Nome</th><th>Safra</th><th>Pre\u00e7o</th><th>ID Categoria</th></tr>");
                for (Vinho v : vinhos) {
                    out.println("<tr><td>" + v.getId() + "</td><td>" + v.getNome() + "</td><td>" + v.getSafra()
                        + "</td><td>R$ " + String.format("%.2f", v.getPreco()) + "</td><td>" + v.getIdCategoria() + "</td></tr>");
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

    private void imprimeErroDependencia(PrintWriter out, String mensagem, String pagina, String textoBotao) {
        out.println("<p class='result-msg error'>" + mensagem + "</p>");
        out.println("<a class='btn primary' href='" + pagina + "' target='_top'>" + textoBotao + "</a>");
    }
}

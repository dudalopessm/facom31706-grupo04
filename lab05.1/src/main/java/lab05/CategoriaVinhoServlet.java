package lab05;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import lab05.dao.CategoriaVinhoDao;
import lab05.modelo.CategoriaVinho;

@WebServlet("/CategoriaVinhoServlet")
public class CategoriaVinhoServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        getServletContext().log("CategoriaVinhoServlet inicializado");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        super.service(request, response);
    }

    @Override
    public void destroy() {
        getServletContext().log("CategoriaVinhoServlet finalizado");
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
        CategoriaVinhoDao dao = new CategoriaVinhoDao(connection);

        try {
            if ("inserir".equals(acao)) {
                String nome = request.getParameter("nome");
                String descricao = request.getParameter("descricao");
                if (dao.buscaPorNome(nome) != null) {
                    out.println("<p class='result-msg error'>J\u00e1 existe uma categoria com o nome \"" + nome + "\".</p>");
                } else {
                    CategoriaVinho c = new CategoriaVinho();
                    c.setNome(nome);
                    c.setDescricao(descricao);
                    dao.adiciona(c);
                    out.println("<p class='result-msg success'>Categoria \"" + nome + "\" inserida com sucesso!</p>");
                }

            } else if ("alterar".equals(acao)) {
                String nomeOriginal = request.getParameter("nomeOriginal");
                String novoNome = request.getParameter("nome");
                String descricao = request.getParameter("descricao");
                CategoriaVinho cat = dao.buscaPorNome(nomeOriginal);
                if (cat == null) {
                    out.println("<p class='result-msg error'>Categoria \"" + nomeOriginal + "\" n\u00e3o encontrada.</p>");
                } else if (cat.getNome().equals(novoNome) && (cat.getDescricao() == null ? descricao == null : cat.getDescricao().equals(descricao))) {
                    out.println("<p class='result-msg info'>Nenhuma altera\u00e7\u00e3o foi feita. Os dados s\u00e3o id\u00eanticos.</p>");
                } else if (!nomeOriginal.equals(novoNome) && dao.buscaPorNome(novoNome) != null) {
                    out.println("<p class='result-msg error'>J\u00e1 existe outra categoria com o nome \"" + novoNome + "\".</p>");
                } else {
                    cat.setNome(novoNome);
                    cat.setDescricao(descricao);
                    dao.altera(cat);
                    out.println("<p class='result-msg success'>Categoria \"" + nomeOriginal + "\" alterada com sucesso!</p>");
                }

            } else if ("remover".equals(acao)) {
                String nome = request.getParameter("nome");
                if (dao.possuiVinhos(nome)) {
                    out.println("<p class='result-msg info'>N\u00e3o \u00e9 poss\u00edvel remover: existem vinhos cadastrados nesta categoria.</p>");
                } else if (dao.removePorNome(nome)) {
                    out.println("<p class='result-msg success'>Categoria \"" + nome + "\" removida com sucesso!</p>");
                } else {
                    out.println("<p class='result-msg error'>Categoria \"" + nome + "\" n\u00e3o encontrada.</p>");
                }

            } else if ("buscar".equals(acao)) {
                String nome = request.getParameter("nome");
                CategoriaVinho c = dao.buscaPorNome(nome);
                if (c == null) {
                    out.println("<p class='result-msg error'>Categoria \"" + nome + "\" n\u00e3o encontrada.</p>");
                } else {
                    out.println("<table><tr><th>ID</th><th>Nome</th><th>Descri\u00e7\u00e3o</th></tr>");
                    out.println("<tr><td>" + c.getId() + "</td><td>" + c.getNome() + "</td><td>" + c.getDescricao() + "</td></tr>");
                    out.println("</table>");
                }

            } else {
                List<CategoriaVinho> categorias = dao.getLista();
                out.println("<table><tr><th>ID</th><th>Nome</th><th>Descri\u00e7\u00e3o</th></tr>");
                for (CategoriaVinho c : categorias) {
                    out.println("<tr><td>" + c.getId() + "</td><td>" + c.getNome() + "</td><td>" + c.getDescricao() + "</td></tr>");
                }
                out.println("</table>");
            }

            connection.close();
        } catch (NumberFormatException e) {
            out.println("<p class='result-msg error'>Valor inv\u00e1lido para um campo num\u00e9rico. Verifique os dados informados.</p>");
        } catch (Exception e) {
            out.println("<p class='result-msg error'>Erro: " + e.getMessage() + "</p>");
        }

        out.println("</div></body></html>");
    }
}

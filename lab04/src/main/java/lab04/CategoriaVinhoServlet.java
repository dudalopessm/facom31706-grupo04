package lab04;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import lab04.dao.CategoriaVinhoDao;
import lab04.modelo.CategoriaVinho;

@WebServlet("/CategoriaVinhoServlet")
public class CategoriaVinhoServlet extends HttpServlet {

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
        out.println("<div class='page-sub'>Categorias</div>");
        out.println("</div>");
        out.println("</div>");

        out.println("<div class='card'>");

        Connection connection = new ConnectionFactory().getConnection();
        CategoriaVinhoDao dao = new CategoriaVinhoDao(connection);

        try {
            if ("inserir".equals(acao)) {
                CategoriaVinho cat1 = new CategoriaVinho();
                cat1.setNome("Espumantes");
                cat1.setDescricao("Tipo de vinho produzido com gás carbônico dissolvido. Também é conhecido como um vinho com perlage, que significa borbulhas.");
                dao.adiciona(cat1);
                out.println("<h2>Categoria inserida com sucesso!</h2>");
                out.println("<p>Espumantes</p>");

            } else if ("alterar".equals(acao)) {
                CategoriaVinho catAlterar = new CategoriaVinho();
                catAlterar.setId(1);
                catAlterar.setNome("Tintos Encorpados Premium");
                catAlterar.setDescricao("Vinhos tintos encorpados de alta qualidade");

                CategoriaVinho atual = dao.buscaPorId(catAlterar.getId());

                if (atual == null) {
                    out.println("<h2>Registro não encontrado.</h2>");
                    out.println("<p>Nenhuma categoria com id=1 existe na base.</p>");
                } else if (Objects.equals(atual.getNome(), catAlterar.getNome())
                        && Objects.equals(atual.getDescricao(), catAlterar.getDescricao())) {
                    out.println("<h2>Nenhuma alteração foi feita.</h2>");
                    out.println("<p>A tabela permaneceu a mesma.</p>");
                } else {
                    dao.altera(catAlterar);
                    out.println("<h2>Categoria alterada com sucesso!</h2>");
                    out.println("<p>Categoria id=1 atualizada para: Tintos Encorpados Premium</p>");
                }
            } else if ("remover".equals(acao)) {
                CategoriaVinho catRemover = new CategoriaVinho();
                catRemover.setId(4);
                if (dao.possuiVinhos(catRemover.getId())) {
                    out.println("<h2>Não é possível remover esta categoria.</h2>");
                    out.println("<p>Existem vinhos cadastrados nela.</p>");
                } else {
                    boolean removido = dao.remove(catRemover);
                    if (removido) {
                        out.println("<h2>Categoria removida com sucesso.</h2>");
                    } else {
                        out.println("<h2>Categoria não encontrada.</h2>");
                        out.println("<p>Nenhuma categoria com id=4 existe na base.</p>");
                    }
                }
            } else {
                List<CategoriaVinho> categorias = dao.getLista();
                out.println("<h2>Categorias de vinho cadastradas</h2>");
                out.println("<table><tr><th>ID</th><th>Nome</th><th>Descrição</th></tr>");
                for (CategoriaVinho c : categorias) {
                    out.println("<tr>");
                    out.println("<td>" + c.getId() + "</td>");
                    out.println("<td>" + c.getNome() + "</td>");
                    out.println("<td>" + c.getDescricao() + "</td>");
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
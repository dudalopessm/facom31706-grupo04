package lab04;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import lab04.dao.ClienteDao;
import lab04.modelo.Cliente;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {

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
        out.println("<div class='page-sub'>Clientes</div>");
        out.println("</div>");
        out.println("</div>");

        out.println("<div class='card'>");

        Connection connection = new ConnectionFactory().getConnection();
        ClienteDao dao = new ClienteDao(connection);

        try {
            if ("inserir".equals(acao)) {
                Cliente cli1 = new Cliente();
                cli1.setNome("Jefferson");
                cli1.setEmail("jefferson@gmail.com");
                cli1.setSenha("senha1234");
                dao.adiciona(cli1);
                out.println("<h2>Cliente inserido com sucesso!</h2>");
                out.println("<p>Jefferson</p>");

            } else if ("alterar".equals(acao)) {
                Cliente cliAlterar = new Cliente();
                cliAlterar.setId(1);
                cliAlterar.setNome("Joao Marcos");
                cliAlterar.setEmail("joao.marcos@cavefontana.com");
                cliAlterar.setSenha("novaSenha123");
                boolean alterado = dao.altera(cliAlterar);
                if (alterado) {
                    out.println("<h2>Cliente alterado com sucesso!</h2>");
                    out.println("<p>Cliente id=1 atualizado para: Joao Marcos</p>");
                } else {
                    out.println("<h2>Registro não encontrado.</h2>");
                    out.println("<p>Nenhum cliente com id=1 existe na base.</p>");
                }
            } else if ("remover".equals(acao)) {
                Cliente cliente = new Cliente();
                cliente.setId(1);
                if (dao.possuiPedidos(cliente.getId())) {
                    out.println("Não é possível remover este cliente, pois ele possui pedidos cadastrados.");
                    return;
                }
                boolean removido = dao.remove(cliente);
                if (removido) {
                    out.println("Cliente removido com sucesso!");
                } else {
                    out.println("Cliente não encontrado.");
                }
            } else {
                List<Cliente> clientes = dao.getLista();
                out.println("<h2>Clientes cadastrados</h2>");
                out.println("<table><tr><th>ID</th><th>Nome</th><th>Email</th><th>Senha</th></tr>");
                for (Cliente c : clientes) {
                    out.println("<tr>");
                    out.println("<td>" + c.getId() + "</td>");
                    out.println("<td>" + c.getNome() + "</td>");
                    out.println("<td>" + c.getEmail() + "</td>");
                    out.println("<td>" + c.getSenha() + "</td>");
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
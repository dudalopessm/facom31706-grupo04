package lab05;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import lab05.dao.ClienteDao;
import lab05.modelo.Cliente;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        getServletContext().log("ClienteServlet inicializado");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        super.service(request, response);
    }

    @Override
    public void destroy() {
        getServletContext().log("ClienteServlet finalizado");
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
        ClienteDao dao = new ClienteDao(connection);

        try {
            if ("inserir".equals(acao)) {
                String cpf = request.getParameter("cpf");
                String nome = request.getParameter("nome");
                String email = request.getParameter("email");
                String senha = request.getParameter("senha");
                Cliente c = new Cliente();
                c.setCpf(cpf);
                c.setNome(nome);
                c.setEmail(email);
                c.setSenha(senha);
                dao.adiciona(c);
                out.println("<p class='result-msg success'>Cliente \"" + nome + "\" (CPF " + cpf + ") inserido com sucesso!</p>");

            } else if ("alterar".equals(acao)) {
                String cpf = request.getParameter("cpf");
                String nome = request.getParameter("nome");
                String email = request.getParameter("email");
                String senha = request.getParameter("senha");
                Cliente atual = dao.buscaPorCpf(cpf);
                if (atual == null) {
                    out.println("<p class='result-msg error'>Cliente CPF " + cpf + " n\u00e3o encontrado.</p>");
                } else if (atual.getNome().equals(nome) && atual.getEmail().equals(email) && atual.getSenha().equals(senha)) {
                    out.println("<p class='result-msg info'>Nenhuma altera\u00e7\u00e3o foi feita. Os dados s\u00e3o id\u00eanticos.</p>");
                } else {
                    Cliente c = new Cliente();
                    c.setCpf(cpf);
                    c.setNome(nome);
                    c.setEmail(email);
                    c.setSenha(senha);
                    dao.altera(c);
                    out.println("<p class='result-msg success'>Cliente CPF " + cpf + " alterado com sucesso!</p>");
                }

            } else if ("remover".equals(acao)) {
                String cpf = request.getParameter("cpf");
                if (dao.possuiPedidos(cpf)) {
                    out.println("<p class='result-msg info'>N\u00e3o \u00e9 poss\u00edvel remover: cliente possui pedidos cadastrados.</p>");
                } else if (dao.remove(cpf)) {
                    out.println("<p class='result-msg success'>Cliente CPF " + cpf + " removido com sucesso!</p>");
                } else {
                    out.println("<p class='result-msg error'>Cliente CPF " + cpf + " n\u00e3o encontrado.</p>");
                }

            } else if ("buscar".equals(acao)) {
                String cpf = request.getParameter("cpf");
                Cliente c = dao.buscaPorCpf(cpf);
                if (c == null) {
                    out.println("<p class='result-msg error'>Cliente CPF " + cpf + " n\u00e3o encontrado.</p>");
                } else {
                    out.println("<table><tr><th>ID</th><th>CPF</th><th>Nome</th><th>Email</th><th>Senha</th></tr>");
                    out.println("<tr><td>" + c.getId() + "</td><td>" + c.getCpf() + "</td><td>" + c.getNome() + "</td><td>" + c.getEmail() + "</td><td>" + c.getSenha() + "</td></tr>");
                    out.println("</table>");
                }

            } else {
                List<Cliente> clientes = dao.getLista();
                out.println("<table><tr><th>ID</th><th>CPF</th><th>Nome</th><th>Email</th><th>Senha</th></tr>");
                for (Cliente c : clientes) {
                    out.println("<tr><td>" + c.getId() + "</td><td>" + c.getCpf() + "</td><td>" + c.getNome() + "</td><td>" + c.getEmail() + "</td><td>" + c.getSenha() + "</td></tr>");
                }
                out.println("</table>");
            }

            connection.close();
        } catch (Exception e) {
            out.println("<p class='result-msg error'>Erro: " + e.getMessage() + "</p>");
        }

        out.println("</div></body></html>");
    }
}

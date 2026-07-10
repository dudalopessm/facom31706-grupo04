package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.CategoriaVinhoDAO;
import dao.VinhoDAO;
import javaBeans.CategoriaVinho;
import javaBeans.Cliente;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("cliente") == null) {
            response.sendRedirect("../login.jsp");
            return;
        }

        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (!"ADMIN".equals(cliente.getTipo())) {
            response.sendRedirect("../loja.jsp");
            return;
        }

        String acao = request.getParameter("acao");
        if (acao == null) {
            acao = "";
        }

        try {
            switch (acao) {
                case "inserirCategoria":
                    inserirCategoria(request, response);
                    break;
                case "alterarCategoria":
                    alterarCategoria(request, response);
                    break;
                case "excluirCategoria":
                    excluirCategoria(request, response);
                    break;
                case "excluirVinho":
                    excluirVinho(request, response);
                    break;
                default:
                    response.sendRedirect("vinhos.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("../erro.jsp").forward(request, response);
        }
    }

    private void inserirCategoria(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CategoriaVinhoDAO dao = new CategoriaVinhoDAO();
        CategoriaVinho c = new CategoriaVinho();
        c.setNome(request.getParameter("nome"));
        c.setDescricao(request.getParameter("descricao"));
        dao.inserir(c);
        request.getRequestDispatcher("categorias.jsp").forward(request, response);
    }

    private void alterarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CategoriaVinhoDAO dao = new CategoriaVinhoDAO();
        CategoriaVinho c = new CategoriaVinho();
        c.setId(Integer.parseInt(request.getParameter("id")));
        c.setNome(request.getParameter("nome"));
        c.setDescricao(request.getParameter("descricao"));
        dao.alterar(c);
        request.getRequestDispatcher("categorias.jsp").forward(request, response);
    }

    private void excluirCategoria(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CategoriaVinhoDAO dao = new CategoriaVinhoDAO();
        int id = Integer.parseInt(request.getParameter("id"));
        dao.excluir(id);
        request.getRequestDispatcher("categorias.jsp").forward(request, response);
    }

    private void excluirVinho(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        VinhoDAO dao = new VinhoDAO();
        int id = Integer.parseInt(request.getParameter("id"));
        dao.excluir(id);
        request.getRequestDispatcher("vinhos.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("vinhos.jsp");
    }
}

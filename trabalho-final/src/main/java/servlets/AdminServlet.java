package servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.CategoriaVinhoDAO;
import dao.ItemPedidoDAO;
import dao.PedidoDAO;
import dao.VinhoDAO;
import javaBeans.CategoriaVinho;
import javaBeans.Cliente;
import javaBeans.ItemPedido;
import javaBeans.Vinho;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("clienteLogado") == null) {
            response.sendRedirect("../login.jsp");
            return;
        }

        Cliente cliente = (Cliente) session.getAttribute("clienteLogado");
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
                case "atualizarEnvio":
                    atualizarEnvio(request, response);
                    break;
                case "cancelarPedido":
                    cancelarPedido(request, response);
                    break;
                default:
                    response.sendRedirect("vinhos.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        }
    }

    private void inserirCategoria(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CategoriaVinhoDAO dao = new CategoriaVinhoDAO();
        CategoriaVinho c = new CategoriaVinho();
        c.setNome(request.getParameter("nome"));
        c.setDescricao(request.getParameter("descricao"));
        dao.inserir(c);
        response.sendRedirect("admin/categorias.jsp");
    }

    private void alterarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CategoriaVinhoDAO dao = new CategoriaVinhoDAO();
        CategoriaVinho c = new CategoriaVinho();
        c.setId(Integer.parseInt(request.getParameter("id")));
        c.setNome(request.getParameter("nome"));
        c.setDescricao(request.getParameter("descricao"));
        dao.alterar(c);
        response.sendRedirect("admin/categorias.jsp");
    }

    private void excluirCategoria(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        CategoriaVinhoDAO dao = new CategoriaVinhoDAO();
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            dao.excluir(id);
            response.sendRedirect(request.getContextPath() + "/sucesso.jsp?voltar=" + URLEncoder.encode("admin/categorias.jsp", "UTF-8"));
        } catch (SQLException e) {
            String msg;
            if (e.getMessage() != null && e.getMessage().contains("foreign key")) {
                msg = "N\u00E3o foi poss\u00EDvel excluir esta categoria: existem vinhos vinculados a ela. Remova ou altere os vinhos primeiro.";
            } else {
                msg = "Erro de banco ao excluir categoria: " + e.getMessage();
            }
            response.sendRedirect(request.getContextPath() + "/sucesso.jsp?voltar=" + URLEncoder.encode("admin/categorias.jsp", "UTF-8")
                + "&erro=" + URLEncoder.encode(msg, "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/sucesso.jsp?voltar=" + URLEncoder.encode("admin/categorias.jsp", "UTF-8")
                + "&erro=" + URLEncoder.encode("Erro inesperado ao excluir categoria: " + e.getMessage(), "UTF-8"));
        }
    }

    private void excluirVinho(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        VinhoDAO dao = new VinhoDAO();
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            dao.excluir(id);
            response.sendRedirect(request.getContextPath() + "/sucesso.jsp?voltar=" + URLEncoder.encode("admin/vinhos.jsp", "UTF-8"));
        } catch (SQLException e) {
            String msg;
            if (e.getMessage() != null && e.getMessage().contains("foreign key")) {
                String err = e.getMessage().toLowerCase();
                if (err.contains("itemsacola")) {
                    msg = "N\u00E3o foi poss\u00EDvel excluir este vinho: ele est\u00E1 na sacola de algum cliente.";
                } else if (err.contains("itempedido")) {
                    msg = "N\u00E3o foi poss\u00EDvel excluir este vinho: ele est\u00E1 em pedidos j\u00E1 realizados.";
                } else {
                    msg = "N\u00E3o foi poss\u00EDvel excluir: o vinho est\u00E1 referenciado em sacolas ou pedidos.";
                }
            } else {
                msg = "Erro de banco ao excluir vinho: " + e.getMessage();
            }
            response.sendRedirect(request.getContextPath() + "/sucesso.jsp?voltar=" + URLEncoder.encode("admin/vinhos.jsp", "UTF-8")
                + "&erro=" + URLEncoder.encode(msg, "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/sucesso.jsp?voltar=" + URLEncoder.encode("admin/vinhos.jsp", "UTF-8")
                + "&erro=" + URLEncoder.encode("Erro inesperado ao excluir vinho: " + e.getMessage(), "UTF-8"));
        }
    }

    private void atualizarEnvio(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PedidoDAO dao = new PedidoDAO();
        int id = Integer.parseInt(request.getParameter("idPedido"));
        String status = request.getParameter("statusEnvio");
        try {
            dao.atualizarStatusEnvio(id, status);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("popupErro", "Erro ao atualizar status: " + e.getMessage());
        }
        response.sendRedirect("admin/pedidos.jsp?aba=pedidos");
    }

    private void cancelarPedido(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PedidoDAO pedidoDAO = new PedidoDAO();
        ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
        VinhoDAO vinhoDAO = new VinhoDAO();
        int id = Integer.parseInt(request.getParameter("idPedido"));
        try {
            pedidoDAO.atualizarStatusPagamento(id, "CANCELADO");
            List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(id);
            for (ItemPedido item : itens) {
                Vinho vinho = vinhoDAO.buscarPorId(item.getIdVinho());
                if (vinho != null) {
                    vinhoDAO.atualizarEstoque(item.getIdVinho(), vinho.getEstoque() + item.getQuantidade());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("popupErro", "Erro ao cancelar pedido: " + e.getMessage());
        }
        response.sendRedirect("admin/pedidos.jsp?aba=pedidos");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("vinhos.jsp");
    }
}

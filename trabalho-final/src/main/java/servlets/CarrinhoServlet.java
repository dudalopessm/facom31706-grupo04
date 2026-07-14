package servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ItemSacolaDAO;
import dao.SacolaDAO;
import dao.VinhoDAO;
import javaBeans.Cliente;
import javaBeans.ItemSacola;
import javaBeans.Sacola;
import javaBeans.Vinho;
import java.time.LocalDateTime;

@WebServlet("/carrinho")
public class CarrinhoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("clienteLogado") == null) {
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        Cliente cliente = (Cliente) session.getAttribute("clienteLogado");
        String acao = request.getParameter("acao");

        try {
            SacolaDAO sacolaDAO = new SacolaDAO();
            ItemSacolaDAO itemDAO = new ItemSacolaDAO();
            VinhoDAO vinhoDAO = new VinhoDAO();

            Sacola sacola = sacolaDAO.buscarAtivaPorCliente(cliente.getEmail());

            if ("adicionar".equals(acao)) {
                int idVinho = Integer.parseInt(request.getParameter("idVinho"));
                int quantidade = Integer.parseInt(request.getParameter("quantidade"));

                Vinho vinho = vinhoDAO.buscarPorId(idVinho);
                if (vinho == null) {
                    request.getRequestDispatcher("loja.jsp").forward(request, response);
                    return;
                }

                int qtdAtual = 0;
                if (sacola != null) {
                    ItemSacola existente = itemDAO.buscarItem(sacola.getId(), idVinho);
                    if (existente != null) {
                        qtdAtual = existente.getQuantidade();
                    }
                }

                if (qtdAtual + quantidade > vinho.getEstoque()) {
                    request.setAttribute("erro", "estoque_insuficiente");
                    request.setAttribute("idVinho", idVinho);
                    request.getRequestDispatcher("vinho.jsp").forward(request, response);
                    return;
                }

                if (sacola == null) {
                    sacola = new Sacola();
                    sacola.setEmailCliente(cliente.getEmail());
                    sacola.setDataCriacao(LocalDateTime.now());
                    sacola.setStatus("ATIVA");
                    int idSacola = sacolaDAO.inserir(sacola);
                    sacola.setId(idSacola);
                }

                ItemSacola itemExistente = itemDAO.buscarItem(sacola.getId(), idVinho);
                if (itemExistente != null) {
                    itemDAO.atualizarQuantidade(sacola.getId(), idVinho, itemExistente.getQuantidade() + quantidade);
                } else {
                    ItemSacola item = new ItemSacola();
                    item.setIdSacola(sacola.getId());
                    item.setIdVinho(idVinho);
                    item.setQuantidade(quantidade);
                    itemDAO.inserir(item);
                }

                request.getRequestDispatcher("carrinho.jsp").forward(request, response);
            } else if ("atualizar".equals(acao)) {
                int idVinho = Integer.parseInt(request.getParameter("idVinho"));
                int quantidade = Integer.parseInt(request.getParameter("quantidade"));

                if (sacola != null) {
                    if (quantidade <= 0) {
                        itemDAO.remover(sacola.getId(), idVinho);
                    } else {
                        itemDAO.atualizarQuantidade(sacola.getId(), idVinho, quantidade);
                    }
                }
                request.getRequestDispatcher("carrinho.jsp").forward(request, response);
            } else if ("remover".equals(acao)) {
                int idVinho = Integer.parseInt(request.getParameter("idVinho"));

                if (sacola != null) {
                    ItemSacola item = itemDAO.buscarItem(sacola.getId(), idVinho);
                    if (item != null) {
                        if (item.getQuantidade() > 1) {
                            itemDAO.atualizarQuantidade(sacola.getId(), idVinho, item.getQuantidade() - 1);
                            request.getRequestDispatcher("carrinho.jsp").forward(request, response);
                            return;
                        } else {
                            itemDAO.remover(sacola.getId(), idVinho);
                            List<ItemSacola> itensRestantes = itemDAO.listarPorSacola(sacola.getId());
                            if (itensRestantes.isEmpty()) {
                                sacolaDAO.atualizarStatus(sacola.getId(), "CANCELADA");
                            }
                        }
                    }
                }
                response.sendRedirect(request.getContextPath() + "/sucesso.jsp?voltar=" + URLEncoder.encode("carrinho.jsp", "UTF-8"));
            } else {
                request.getRequestDispatcher("loja.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("erro.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("carrinho.jsp").forward(request, response);
    }
}

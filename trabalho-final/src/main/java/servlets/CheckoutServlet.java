package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ConnectionFactory;
import dao.ItemPedidoDAO;
import dao.ItemSacolaDAO;
import dao.PedidoDAO;
import dao.SacolaDAO;
import dao.VinhoDAO;
import javaBeans.Cliente;
import javaBeans.ItemPedido;
import javaBeans.ItemSacola;
import javaBeans.Pedido;
import javaBeans.Sacola;
import javaBeans.Vinho;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("clienteLogado") == null) {
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        Cliente cliente = (Cliente) session.getAttribute("clienteLogado");

        try {
            SacolaDAO sacolaDAO = new SacolaDAO();
            ItemSacolaDAO itemSacolaDAO = new ItemSacolaDAO();

            Sacola sacola = sacolaDAO.buscarAtivaPorCliente(cliente.getEmail());
            if (sacola == null) {
                request.setAttribute("erro", "sacola_vazia");
                request.getRequestDispatcher("carrinho.jsp").forward(request, response);
                return;
            }

            List<ItemSacola> itens = itemSacolaDAO.listarPorSacola(sacola.getId());
            if (itens.isEmpty()) {
                request.setAttribute("erro", "sacola_vazia");
                request.getRequestDispatcher("carrinho.jsp").forward(request, response);
                return;
            }

            double valorTotal = 0;
            for (ItemSacola item : itens) {
                valorTotal += item.getSubtotal();
            }

            Connection conn = null;
            try {
                conn = ConnectionFactory.getConnection();
                conn.setAutoCommit(false);

                PedidoDAO pedidoDAO = new PedidoDAO();
                ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
                VinhoDAO vinhoDAO = new VinhoDAO();

                sacolaDAO.atualizarStatus(conn, sacola.getId(), "CONVERTIDA");

                Pedido pedido = new Pedido();
                pedido.setDataConclusao(LocalDateTime.now());
                pedido.setValorTotal(valorTotal);
                pedido.setStatusPagamento("PAGO");
                pedido.setStatusEnvio("PENDENTE");
                pedido.setIdSacola(sacola.getId());
                int idPedido = pedidoDAO.inserir(conn, pedido);

                for (ItemSacola item : itens) {
                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setIdPedido(idPedido);
                    itemPedido.setIdVinho(item.getIdVinho());
                    itemPedido.setQuantidade(item.getQuantidade());
                    itemPedido.setPrecoUnitario(item.getVinho().getPreco());
                    itemPedidoDAO.inserir(conn, itemPedido);

                    Vinho v = vinhoDAO.buscarPorId(item.getIdVinho());
                    int novoEstoque = v.getEstoque() - item.getQuantidade();
                    vinhoDAO.atualizarEstoque(conn, item.getIdVinho(), novoEstoque);
                }

                conn.commit();
                request.setAttribute("idPedido", idPedido);
                request.getRequestDispatcher("confirmacao.jsp").forward(request, response);
            } catch (Exception ex) {
                if (conn != null) {
                    try { conn.rollback(); } catch (SQLException rb) { }
                }
                throw ex;
            } finally {
                if (conn != null) {
                    try { conn.setAutoCommit(true); } catch (SQLException ignored) { }
                    ConnectionFactory.closeConnection(conn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("erro.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }
}

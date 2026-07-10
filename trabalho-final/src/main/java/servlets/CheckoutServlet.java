package servlets;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ItemPedidoDAO;
import dao.ItemSacolaDAO;
import dao.PedidoDAO;
import dao.SacolaDAO;
import javaBeans.Cliente;
import javaBeans.ItemPedido;
import javaBeans.ItemSacola;
import javaBeans.Pedido;
import javaBeans.Sacola;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("cliente") == null) {
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        Cliente cliente = (Cliente) session.getAttribute("cliente");

        try {
            SacolaDAO sacolaDAO = new SacolaDAO();
            ItemSacolaDAO itemSacolaDAO = new ItemSacolaDAO();
            PedidoDAO pedidoDAO = new PedidoDAO();
            ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();

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

            sacolaDAO.atualizarStatus(sacola.getId(), "CONVERTIDA");

            Pedido pedido = new Pedido();
            pedido.setDataConclusao(LocalDateTime.now());
            pedido.setValorTotal(valorTotal);
            pedido.setStatusPagamento("PAGO");
            pedido.setStatusEnvio("PENDENTE");
            pedido.setIdSacola(sacola.getId());
            int idPedido = pedidoDAO.inserir(pedido);

            for (ItemSacola item : itens) {
                ItemPedido itemPedido = new ItemPedido();
                itemPedido.setIdPedido(idPedido);
                itemPedido.setIdVinho(item.getIdVinho());
                itemPedido.setQuantidade(item.getQuantidade());
                itemPedido.setPrecoUnitario(item.getVinho().getPreco());
                itemPedidoDAO.inserir(itemPedido);
            }

            request.setAttribute("idPedido", idPedido);
            request.getRequestDispatcher("confirmacao.jsp").forward(request, response);
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

package lab04;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab04.dao.*;
import lab04.modelo.*;

@WebServlet("/PopularServlet")
public class PopularServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Connection connection = null;

        try {

            connection = new ConnectionFactory().getConnection();

            PreparedStatement stmt =
                    connection.prepareStatement("SELECT COUNT(*) FROM categoria_vinho");
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                rs.close();
                stmt.close();

                out.println("{\"ok\": false, \"erro\": \"Banco ja foi populado.\"}");
                return;
            }

            rs.close();
            stmt.close();

            CategoriaVinhoDao catDao = new CategoriaVinhoDao(connection);

            CategoriaVinho cat1 = new CategoriaVinho();
            cat1.setNome("Tintos Encorpados");
            cat1.setDescricao("Vinhos tintos com corpo e taninos marcantes");
            catDao.adiciona(cat1);

            CategoriaVinho cat2 = new CategoriaVinho();
            cat2.setNome("Brancos Leves");
            cat2.setDescricao("Vinhos brancos frescos e leves");
            catDao.adiciona(cat2);

            CategoriaVinho cat3 = new CategoriaVinho();
            cat3.setNome("Champagnes");
            cat3.setDescricao("Espumantes da regiao de Champagne");
            catDao.adiciona(cat3);

            CategoriaVinho cat4 = new CategoriaVinho();
            cat4.setNome("Roses");
            cat4.setDescricao("Vinhos roses refrescantes");
            catDao.adiciona(cat4);

            VinhoDao vinhoDao = new VinhoDao(connection);

            Vinho v1 = new Vinho();
            v1.setNome("Cabernet Sauvignon Reserva");
            v1.setSafra(2019);
            v1.setPreco(89.90);
            v1.setIdCategoria(1);
            vinhoDao.adiciona(v1);

            Vinho v2 = new Vinho();
            v2.setNome("Chardonnay Classic");
            v2.setSafra(2021);
            v2.setPreco(54.50);
            v2.setIdCategoria(2);
            vinhoDao.adiciona(v2);

            Vinho v3 = new Vinho();
            v3.setNome("Moet Chandon Imperial");
            v3.setSafra(2018);
            v3.setPreco(320.00);
            v3.setIdCategoria(3);
            vinhoDao.adiciona(v3);

            Vinho v4 = new Vinho();
            v4.setNome("Whispering Angel Rose");
            v4.setSafra(2022);
            v4.setPreco(145.00);
            v4.setIdCategoria(4);
            vinhoDao.adiciona(v4);

            ClienteDao clienteDao = new ClienteDao(connection);

            Cliente cli1 = new Cliente();
            cli1.setNome("Eduarda Lopes");
            cli1.setEmail("eduarda@gmail.com");
            cli1.setSenha("senha123");
            clienteDao.adiciona(cli1);

            Cliente cli2 = new Cliente();
            cli2.setNome("Anderson Gabriel");
            cli2.setEmail("anderson@cavefontana.com");
            cli2.setSenha("senha456");
            clienteDao.adiciona(cli2);

            Cliente cli3 = new Cliente();
            cli3.setNome("Kamily Cristina");
            cli3.setEmail("kamily@yahoo.com");
            cli3.setSenha("senha789");
            clienteDao.adiciona(cli3);

            PedidoDao pedidoDao = new PedidoDao(connection);

            Pedido p1 = new Pedido();
            p1.setIdCliente(1);
            p1.setStatus("pendente");
            pedidoDao.adiciona(p1);

            Pedido p2 = new Pedido();
            p2.setIdCliente(2);
            p2.setStatus("confirmado");
            pedidoDao.adiciona(p2);

            ItemPedidoDao itemDao = new ItemPedidoDao(connection);

            ItemPedido i1 = new ItemPedido();
            i1.setIdPedido(p1.getId());
            i1.setIdVinho(1);
            i1.setQuantidade(2);
            i1.setPrecoUnitario(89.90);
            itemDao.adiciona(i1);

            ItemPedido i2 = new ItemPedido();
            i2.setIdPedido(p1.getId());
            i2.setIdVinho(3);
            i2.setQuantidade(1);
            i2.setPrecoUnitario(320.00);
            itemDao.adiciona(i2);

            ItemPedido i3 = new ItemPedido();
            i3.setIdPedido(p2.getId());
            i3.setIdVinho(2);
            i3.setQuantidade(3);
            i3.setPrecoUnitario(54.50);
            itemDao.adiciona(i3);

            out.println("{\"ok\": true}");

        } catch (Exception e) {

            e.printStackTrace();

            out.println("{\"ok\": false, \"erro\": \"" +
                    e.getMessage().replace("\"", "'") + "\"}");

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
    }
}

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, javaBeans.*, dao.*" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Meus Pedidos &mdash; Cave Fontana</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="container" style="padding-top:130px;padding-bottom:60px;">
  <h1 style="font-size:32px;font-style:italic;margin-bottom:24px">Meus Pedidos</h1>

  <% if (!temCliente) { %>
    <p class="vazio"><a href="login.jsp" style="color:var(--amber-soft)">Fa&ccedil;a login</a> para ver seus pedidos.</p>
  <% } else {
      PedidoDAO pedidoDAO = new PedidoDAO();
      List<Pedido> pedidos = pedidoDAO.listarPorCliente(clienteLogado.getEmail());

      if (pedidos.isEmpty()) { %>
        <p class="vazio">Voc&ecirc; ainda n&atilde;o possui pedidos.</p>
        <div style="text-align:center"><a href="loja.jsp" class="botao">Ir &agrave;s compras</a></div>
      <% } else { %>
        <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Pedido #</th>
              <th>Data</th>
              <th>Valor Total</th>
              <th>Pagamento</th>
              <th>Envio</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <% for (Pedido pedido : pedidos) { %>
            <tr>
              <td><%= pedido.getId() %></td>
              <td><%= pedido.getDataConclusao() != null ? pedido.getDataConclusao().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "" %></td>
              <td>R$ <%= String.format("%.2f", pedido.getValorTotal()) %></td>
              <td><span class="status status-<%= pedido.getStatusPagamento().toLowerCase() %>"><%= pedido.getStatusPagamento() %></span></td>
              <td><span class="status status-<%= pedido.getStatusEnvio().toLowerCase() %>"><%= pedido.getStatusEnvio() %></span></td>
              <td><a href="detalhePedido.jsp?id=<%= pedido.getId() %>" class="botao botao-pequeno">Detalhes</a></td>
            </tr>
            <% } %>
          </tbody>
        </table>
        </div>
      <% } %>
  <% } %>
</div>

<%@ include file="rodape.jsp" %>
<script src="js/script.js"></script>
</body>
</html>

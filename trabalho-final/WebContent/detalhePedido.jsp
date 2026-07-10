<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, javaBeans.*, dao.*" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Detalhe do Pedido &mdash; Cave Fontana</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<%
    if (!temCliente) { response.sendRedirect("login.jsp"); return; }

    int idPedido = Integer.parseInt(request.getParameter("id"));
    PedidoDAO pedidoDAO = new PedidoDAO();
    Pedido pedido = pedidoDAO.buscarPorId(idPedido);

    if (pedido == null) { response.sendRedirect("historico.jsp"); return; }

    SacolaDAO sacolaDAO = new SacolaDAO();
    Sacola sacola = sacolaDAO.buscarPorId(pedido.getIdSacola());
    if (sacola == null || !sacola.getEmailCliente().equals(clienteLogado.getEmail())) {
        response.sendRedirect("historico.jsp");
        return;
    }

    ItemPedidoDAO itemDAO = new ItemPedidoDAO();
    List<ItemPedido> itens = itemDAO.listarPorPedido(idPedido);
%>

<div class="container" style="padding-top:130px;padding-bottom:60px;">
  <a href="historico.jsp" class="voltar">
    <svg class="icon" style="width:14px;height:14px"><use href="#i-chevron"></use></svg>
    Voltar ao hist&oacute;rico
  </a>
  <h1 style="font-size:28px;font-style:italic;margin-bottom:16px">Pedido #<%= pedido.getId() %></h1>

  <div class="dados-cliente">
    <p><strong>Data:</strong> <%= pedido.getDataConclusao() != null ? pedido.getDataConclusao().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "" %></p>
    <p><strong>Pagamento:</strong> <span class="status status-<%= pedido.getStatusPagamento().toLowerCase() %>"><%= pedido.getStatusPagamento() %></span></p>
    <p><strong>Envio:</strong> <span class="status status-<%= pedido.getStatusEnvio().toLowerCase() %>"><%= pedido.getStatusEnvio() %></span></p>
  </div>

  <h2 style="font-family:'Inter',sans-serif;font-size:13px;letter-spacing:0.08em;text-transform:uppercase;color:var(--text-muted);font-weight:600;margin:24px 0 12px">Itens</h2>

  <div class="table-wrap">
  <table>
    <thead>
      <tr>
        <th>Produto</th>
        <th>Quantidade</th>
        <th>Pre&ccedil;o Unit.</th>
        <th>Subtotal</th>
      </tr>
    </thead>
    <tbody>
      <% for (ItemPedido item : itens) { %>
      <tr>
        <td>
          <img src="<%= item.getVinho().getCaminhoFoto() != null ? item.getVinho().getCaminhoFoto() : "images/vinhos/sem-foto.jpg" %>"
               alt="<%= item.getVinho().getNome() %>"
               onerror="this.src='images/vinhos/sem-foto.jpg';" class="miniatura">
          <span style="vertical-align:middle"><%= item.getVinho().getNome() %></span>
        </td>
        <td><%= item.getQuantidade() %></td>
        <td>R$ <%= String.format("%.2f", item.getPrecoUnitario()) %></td>
        <td>R$ <%= String.format("%.2f", item.getSubtotal()) %></td>
      </tr>
      <% } %>
    </tbody>
  </table>
  </div>

  <div style="display:flex;justify-content:flex-end;margin-top:16px">
    <span style="font-size:16px;font-weight:600">Total: <span style="color:var(--amber-soft);font-size:20px">R$ <%= String.format("%.2f", pedido.getValorTotal()) %></span></span>
  </div>
</div>

<%@ include file="rodape.jsp" %>
<script src="js/script.js"></script>
</body>
</html>

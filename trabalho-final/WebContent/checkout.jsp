<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, javaBeans.*, dao.*" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cave Fontana - Checkout</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="checkout-wrap">
  <div class="container" style="max-width:700px">
    <h1 style="font-size:32px;font-style:italic;margin-bottom:8px">Finalizar Compra</h1>
    <p style="color:var(--text-muted);margin-bottom:30px">Revise seu pedido antes de confirmar.</p>

    <% if (!temCliente) { %>
      <p class="vazio"><a href="login.jsp" style="color:var(--amber-soft)">Fa&ccedil;a login</a> para finalizar a compra.</p>
    <% } else {
        SacolaDAO sacolaDAO = new SacolaDAO();
        ItemSacolaDAO itemDAO = new ItemSacolaDAO();
        Sacola sacola = sacolaDAO.buscarAtivaPorCliente(clienteLogado.getEmail());

        if (sacola == null) { response.sendRedirect("carrinho.jsp"); return; }

        List<ItemSacola> itens = itemDAO.listarPorSacola(sacola.getId());
        if (itens.isEmpty()) { response.sendRedirect("carrinho.jsp"); return; }

        double total = 0;
    %>

    <div class="table-wrap">
    <table>
      <thead>
        <tr>
          <th>Produto</th>
          <th>Qtd</th>
          <th>Pre&ccedil;o Unit.</th>
          <th>Subtotal</th>
        </tr>
      </thead>
      <tbody>
        <% for (ItemSacola item : itens) {
            total += item.getSubtotal();
        %>
        <tr>
          <td><%= item.getVinho().getNome() %></td>
          <td><%= item.getQuantidade() %></td>
          <td>R$ <%= String.format("%.2f", item.getVinho().getPreco()) %></td>
          <td>R$ <%= String.format("%.2f", item.getSubtotal()) %></td>
        </tr>
        <% } %>
      </tbody>
    </table>
    </div>

    <div style="display:flex;justify-content:flex-end;margin:16px 0 24px">
      <span style="font-size:18px;font-weight:600">Total: <span style="color:var(--amber-soft);font-size:22px">R$ <%= String.format("%.2f", total) %></span></span>
    </div>

    <div class="dados-cliente">
      <h3>Dados do Cliente</h3>
      <p><strong>Nome:</strong> <%= clienteLogado.getNome() %></p>
      <p><strong>Email:</strong> <%= clienteLogado.getEmail() %></p>
      <p><strong>CPF:</strong> <%= clienteLogado.getCpf() %></p>
    </div>

    <form action="checkout" method="post" style="display:flex;gap:12px;margin-top:24px;flex-wrap:wrap">
      <button type="submit" class="botao" style="padding:15px 32px;font-size:16px"
              onclick="return confirm('Confirmar pagamento?')">Confirmar Pagamento</button>
      <a href="carrinho.jsp" class="botao botao-ghost">Voltar ao carrinho</a>
    </form>
    <% } %>
  </div>
</div>

<%@ include file="rodape.jsp" %>
<script src="js/script.js"></script>
</body>
</html>

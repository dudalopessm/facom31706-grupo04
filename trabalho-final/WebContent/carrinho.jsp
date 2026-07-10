<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, javaBeans.*, dao.*" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Sacola &mdash; Cave Fontana</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="container" style="padding-top: 130px; padding-bottom: 60px;">
  <h1 style="font-size:32px;font-style:italic;margin-bottom:24px">Minha Sacola</h1>

  <% if (!temCliente) { %>
    <p class="vazio"><a href="login.jsp" style="color:var(--amber-soft)">Fa&ccedil;a login</a> para ver seu carrinho.</p>
  <% } else {
      SacolaDAO sacolaDAO = new SacolaDAO();
      ItemSacolaDAO itemDAO = new ItemSacolaDAO();
      Sacola sacola = sacolaDAO.buscarAtivaPorCliente(clienteLogado.getEmail());

      if (sacola == null) { %>
        <p class="vazio">Sua sacola est&aacute; vazia.</p>
        <div style="text-align:center"><a href="loja.jsp" class="botao">Ir &agrave;s compras</a></div>
      <% } else {
          List<ItemSacola> itens = itemDAO.listarPorSacola(sacola.getId());
          if (itens.isEmpty()) { %>
            <p class="vazio">Sua sacola est&aacute; vazia.</p>
            <div style="text-align:center"><a href="loja.jsp" class="botao">Ir &agrave;s compras</a></div>
          <% } else {
              double total = 0;
  %>
  <div class="table-wrap">
  <table>
    <thead>
      <tr>
        <th>Produto</th>
        <th>Pre&ccedil;o</th>
        <th>Quantidade</th>
        <th>Subtotal</th>
        <th>A&ccedil;&atilde;o</th>
      </tr>
    </thead>
    <tbody>
      <% for (ItemSacola item : itens) {
          total += item.getSubtotal();
          String foto = (item.getVinho().getCaminhoFoto() != null) ? item.getVinho().getCaminhoFoto() : "images/vinhos/sem-foto.jpg";
      %>
      <tr>
        <td>
          <img src="<%= foto %>" alt="<%= item.getVinho().getNome() %>"
               onerror="this.src='images/vinhos/sem-foto.jpg';" class="miniatura">
          <span style="vertical-align:middle"><%= item.getVinho().getNome() %></span>
        </td>
        <td>R$ <%= String.format("%.2f", item.getVinho().getPreco()) %></td>
        <td>
          <form action="carrinho" method="post" class="form-qtd">
            <input type="hidden" name="acao" value="atualizar">
            <input type="hidden" name="idVinho" value="<%= item.getIdVinho() %>">
            <input type="number" name="quantidade" value="<%= item.getQuantidade() %>" min="0" max="<%= item.getVinho().getEstoque() %>"
                   onchange="this.form.submit()">
          </form>
        </td>
        <td>R$ <%= String.format("%.2f", item.getSubtotal()) %></td>
        <td>
          <form action="carrinho" method="post">
            <input type="hidden" name="acao" value="remover">
            <input type="hidden" name="idVinho" value="<%= item.getIdVinho() %>">
            <button type="submit" class="botao-remover botao-pequeno" data-confirm="Remover este item?">Remover</button>
          </form>
        </td>
      </tr>
      <% } %>
    </tbody>
  </table>
  </div>

  <div style="display:flex;justify-content:space-between;align-items:center;margin-top:20px;flex-wrap:wrap;gap:16px">
    <span style="font-size:18px;font-weight:600">Total: <span style="color:var(--amber-soft);font-size:22px">R$ <%= String.format("%.2f", total) %></span></span>
    <div class="acoes-carrinho">
      <a href="loja.jsp" class="botao botao-ghost">Continuar comprando</a>
      <a href="checkout.jsp" class="botao">Finalizar Compra</a>
    </div>
  </div>
  <% } } } %>
</div>

<%@ include file="rodape.jsp" %>
<script src="js/script.js"></script>
</body>
</html>

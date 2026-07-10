<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Pedido Confirmado &mdash; Cave Fontana</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="confirmacao">
  <div class="container">
    <svg class="icone-sucesso" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <path d="M4 12l5 5L20 6"/>
    </svg>
    <h1>Pagamento Confirmado!</h1>
    <%
        Object idAttr = request.getAttribute("idPedido");
        String idPedidoStr = (idAttr != null) ? idAttr.toString() : request.getParameter("idPedido");
        if (idPedidoStr != null) {
    %>
    <p>Seu pedido <strong>#<%= idPedidoStr %></strong> foi registrado com sucesso.</p>
    <% } else { %>
    <p>Seu pedido foi registrado com sucesso.</p>
    <% } %>
    <p>Obrigado por comprar na Cave Fontana!</p>
    <div class="acoes">
      <a href="historico.jsp" class="botao">Ver Meus Pedidos</a>
      <a href="loja.jsp" class="botao botao-ghost">Continuar Comprando</a>
    </div>
  </div>
</div>

<%@ include file="rodape.jsp" %>
<script src="js/script.js"></script>
</body>
</html>

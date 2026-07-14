<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="javaBeans.*, dao.*" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cave Fontana</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<%
    String idParam = request.getParameter("id");
    if (idParam == null || idParam.isEmpty()) {
        Object idAttr = request.getAttribute("idVinho");
        idParam = (idAttr != null) ? idAttr.toString() : null;
    }
    int id = Integer.parseInt(idParam);
    VinhoDAO dao = new VinhoDAO();
    Vinho vinho = dao.buscarPorId(id);

    if (vinho == null) {
        response.sendRedirect("loja.jsp");
        return;
    }

    CategoriaVinhoDAO catDAO = new CategoriaVinhoDAO();
    CategoriaVinho cat = catDAO.buscarPorId(vinho.getIdCategoria());

    String foto = (vinho.getCaminhoFoto() != null) ? vinho.getCaminhoFoto() : "images/vinhos/sem-foto.jpg";
%>

<div class="wine-detail">
  <div class="container detail-layout">
    <div>
      <div class="breadcrumb">
        <a href="loja.jsp">Adega</a> / <span><%= vinho.getNome() %></span>
      </div>
      <div class="detail-image-wrap">
        <img src="<%= foto %>" alt="<%= vinho.getNome() %>"
             onerror="this.onerror=null;this.src='images/vinhos/sem-foto.jpg';">
      </div>
    </div>

    <div class="detail-info">
      <span class="wine-card__tag"><%= cat != null ? cat.getNome() : "" %></span>
      <h1><%= vinho.getNome() %></h1>
      <span class="safra">Safra <%= vinho.getSafra() %></span>
      <p class="preco">R$ <%= String.format("%.2f", vinho.getPreco()) %></p>
      <%
    boolean adminView = temCliente && "ADMIN".equals(clienteLogado.getTipo());
%>
<p class="estoque"><%= vinho.getEstoque() > 0 ? vinho.getEstoque() + " unidades em estoque" : (adminView ? "* Esgotado" : "Produto indispon\u00edvel") %></p>

      <p class="descricao"><%= vinho.getDescricao() != null ? vinho.getDescricao() : "" %></p>

      <% if (temCliente && vinho.getEstoque() > 0) { %>
        <form action="carrinho" method="post" class="form-adicionar">
          <input type="hidden" name="acao" value="adicionar">
          <input type="hidden" name="idVinho" value="<%= vinho.getId() %>">
          <label for="quantidade">Quantidade</label>
          <input type="number" name="quantidade" id="quantidade" value="1" min="1" max="<%= vinho.getEstoque() %>">
          <button type="submit" class="add-btn" data-name="<%= vinho.getNome() %>" style="margin-top:12px;display:inline-flex">
            <svg class="icon"><use href="#i-plus"></use></svg><span>Adicionar à sacola</span>
          </button>
        </form>
      <% } else if (!temCliente) { %>
        <p style="margin-top: 20px;"><a href="login.jsp" class="botao" style="text-decoration:none">Faça login</a> para adicionar ao carrinho.</p>
      <% } else { %>
        <p class="msg-erro" style="margin-top:20px">Produto indisponível no momento.</p>
      <% } %>

      <%
          String erroVinho = (String) request.getAttribute("erro");
          if (erroVinho == null) erroVinho = request.getParameter("erro");
      %>
      <% if ("estoque_insuficiente".equals(erroVinho)) { %>
        <p class="msg-erro">Estoque insuficiente para esta quantidade.</p>
      <% } %>
    </div>
  </div>
</div>

<%@ include file="rodape.jsp" %>
<script src="js/script.js"></script>
</body>
</html>

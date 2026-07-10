<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, javaBeans.*, dao.*" %>
<%
    Cliente admin = (Cliente) session.getAttribute("cliente");
    if (admin == null || !"ADMIN".equals(admin.getTipo())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin &mdash; Vinhos &mdash; Cave Fontana</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="../css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="admin-layout">
  <div class="container">
    <div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:12px">
      <h1 style="margin:0">Gerenciar Vinhos</h1>
      <a href="cadastroVinho.jsp" class="botao">Novo Vinho</a>
    </div>

    <%
        VinhoDAO vinhoDAO = new VinhoDAO();
        CategoriaVinhoDAO catDAO = new CategoriaVinhoDAO();
        List<Vinho> vinhos = vinhoDAO.listarTodos();
    %>

    <div class="table-wrap" style="margin-top:20px">
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Foto</th>
          <th>Nome</th>
          <th>Safra</th>
          <th>Preço</th>
          <th>Estoque</th>
          <th>Categoria</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        <% for (Vinho vinho : vinhos) {
            CategoriaVinho cat = catDAO.buscarPorId(vinho.getIdCategoria());
            String foto = (vinho.getCaminhoFoto() != null) ? "../" + vinho.getCaminhoFoto() : "../images/vinhos/sem-foto.jpg";
        %>
        <tr>
          <td><%= vinho.getId() %></td>
          <td><img src="<%= foto %>" alt="" onerror="this.src='../images/vinhos/sem-foto.jpg';" class="miniatura" style="width:36px;height:46px"></td>
          <td><%= vinho.getNome() %></td>
          <td><%= vinho.getSafra() %></td>
          <td>R$ <%= String.format("%.2f", vinho.getPreco()) %></td>
          <td><%= vinho.getEstoque() %></td>
          <td><%= cat != null ? cat.getNome() : "" %></td>
          <td>
            <a href="editarVinho.jsp?id=<%= vinho.getId() %>" class="botao botao-pequeno">Editar</a>
            <form action="../admin" method="post" class="form-inline" style="margin-left:6px"
                  onsubmit="return confirm('Excluir vinho <%= vinho.getNome() %>?')">
              <input type="hidden" name="acao" value="excluirVinho">
              <input type="hidden" name="id" value="<%= vinho.getId() %>">
              <button type="submit" class="botao-remover botao-pequeno">Excluir</button>
            </form>
          </td>
        </tr>
        <% } %>
      </tbody>
    </table>
    </div>
  </div>
</div>

<script src="../js/script.js"></script>
</body>
</html>

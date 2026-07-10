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
<title>Admin &mdash; Categorias &mdash; Cave Fontana</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="../css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="admin-layout">
  <div class="container">
    <h1>Gerenciar Categorias</h1>

    <%
        CategoriaVinhoDAO dao = new CategoriaVinhoDAO();
        List<CategoriaVinho> categorias = dao.listarTodos();
    %>

    <h2>Nova Categoria</h2>
    <form action="../admin" method="post" class="formulario form-inline" style="gap:10px;flex-wrap:wrap;max-width:100%">
      <input type="hidden" name="acao" value="inserirCategoria">
      <input type="text" name="nome" placeholder="Nome" required style="padding:8px 12px;background:var(--surface);border:1px solid var(--line);border-radius:8px;color:var(--text)">
      <input type="text" name="descricao" placeholder="Descrição" style="padding:8px 12px;background:var(--surface);border:1px solid var(--line);border-radius:8px;color:var(--text)">
      <button type="submit" class="botao botao-pequeno">Adicionar</button>
    </form>

    <h2>Categorias Existentes</h2>
    <div class="table-wrap">
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Nome</th>
          <th>Descrição</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        <% for (CategoriaVinho cat : categorias) { %>
        <tr>
          <td><%= cat.getId() %></td>
          <td><%= cat.getNome() %></td>
          <td><%= cat.getDescricao() != null ? cat.getDescricao() : "" %></td>
          <td>
            <form action="../admin" method="post" class="form-inline" style="gap:6px">
              <input type="hidden" name="acao" value="alterarCategoria">
              <input type="hidden" name="id" value="<%= cat.getId() %>">
              <input type="text" name="nome" value="<%= cat.getNome() %>" required style="padding:6px 10px;background:var(--surface);border:1px solid var(--line);border-radius:6px;color:var(--text);width:100px">
              <input type="text" name="descricao" value="<%= cat.getDescricao() != null ? cat.getDescricao() : "" %>" style="padding:6px 10px;background:var(--surface);border:1px solid var(--line);border-radius:6px;color:var(--text);width:140px">
              <button type="submit" class="botao botao-pequeno">Salvar</button>
            </form>
            <form action="../admin" method="post" class="form-inline" style="margin-left:6px"
                  onsubmit="return confirm('Excluir categoria <%= cat.getNome() %>?')">
              <input type="hidden" name="acao" value="excluirCategoria">
              <input type="hidden" name="id" value="<%= cat.getId() %>">
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

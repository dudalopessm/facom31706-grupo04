<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, javaBeans.*, dao.*" %>
<%
    Cliente admin = (Cliente) session.getAttribute("clienteLogado");
    if (admin == null || !"ADMIN".equals(admin.getTipo())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    int vinhoId = -1;
    String paramId = request.getParameter("id");
    if (paramId != null && !paramId.isEmpty()) {
        try { vinhoId = Integer.parseInt(paramId); } catch (NumberFormatException e) {}
    }
    if (vinhoId < 0) { response.sendRedirect("vinhos.jsp"); return; }

    VinhoDAO vinhoDAO = new VinhoDAO();
    Vinho vinho = vinhoDAO.buscarPorId(vinhoId);
    if (vinho == null) { response.sendRedirect("vinhos.jsp"); return; }

    CategoriaVinhoDAO catDAO = new CategoriaVinhoDAO();
    List<CategoriaVinho> categorias = catDAO.listarTodos();

    String mensagemErro = (String) request.getAttribute("erro");
    String mensagemAviso = (String) request.getAttribute("aviso");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cave Fontana - Editar Vinho</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="../css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="admin-layout">
  <div class="container" style="max-width:600px">
    <h1>Editar Vinho</h1>
    <a href="vinhos.jsp" class="voltar">
      <svg class="icon" style="width:14px;height:14px"><use href="#i-chevron"></use></svg>
      Voltar
    </a>

    <% if (mensagemErro != null) { %><p class="msg-erro"><%= mensagemErro %></p><% } %>
    <% if (mensagemAviso != null) { %><p class="msg-erro"><%= mensagemAviso %></p><% } %>

    <form action="../admin/vinho?acao=alterar" method="post" enctype="multipart/form-data" class="formulario" style="max-width:100%">
      <input type="hidden" name="id" value="<%= vinho.getId() %>">

      <div class="account-field">
        <label for="nome">Nome</label>
        <input type="text" name="nome" id="nome" value="<%= vinho.getNome() %>" required>
      </div>
      <div class="account-field">
        <label for="safra">Safra</label>
        <input type="number" name="safra" id="safra" value="<%= vinho.getSafra() %>" min="1900" max="2100" required>
      </div>
      <div class="account-field">
        <label for="descricao">Descri&ccedil;&atilde;o</label>
        <textarea name="descricao" id="descricao" rows="4" style="background:var(--surface);border:1px solid var(--line);border-radius:10px;padding:11px 13px;color:var(--text);font-family:inherit;width:100%"><%= vinho.getDescricao() != null ? vinho.getDescricao() : "" %></textarea>
      </div>
      <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px">
        <div class="account-field">
          <label for="preco">Pre&ccedil;o (R$)</label>
          <input type="number" name="preco" id="preco" step="0.01" min="0" value="<%= vinho.getPreco() %>" required>
        </div>
        <div class="account-field">
          <label for="estoque">Estoque</label>
          <input type="number" name="estoque" id="estoque" min="0" value="<%= vinho.getEstoque() %>" required>
        </div>
      </div>
      <div class="account-field">
        <label for="idCategoria">Categoria</label>
        <select name="idCategoria" id="idCategoria" required>
          <option value="">Selecione...</option>
          <% for (CategoriaVinho cat : categorias) {
              boolean selected = cat.getId() == vinho.getIdCategoria();
          %>
            <option value="<%= cat.getId() %>" <%= selected ? "selected" : "" %>><%= cat.getNome() %></option>
          <% } %>
        </select>
      </div>
      <% String foto = (vinho.getCaminhoFoto() != null) ? "../" + vinho.getCaminhoFoto() : "../images/vinhos/sem-foto.jpg"; %>
      <div class="foto-atual">
        <p>Foto atual:</p>
        <img src="<%= foto %>" alt="<%= vinho.getNome() %>" onerror="this.onerror=null;this.src='../images/vinhos/sem-foto.jpg';" class="foto-preview">
      </div>
      <div class="account-field">
        <label for="foto">Nova Foto (deixe em branco para manter a atual)</label>
        <input type="file" name="foto" id="foto" accept="image/jpeg,image/png"
               style="background:var(--surface);border:1px solid var(--line);border-radius:10px;padding:10px;color:var(--text);width:100%">
      </div>
      <button type="submit" class="botao" style="margin-top:10px;width:100%;justify-content:center">Salvar Altera&ccedil;&otilde;es</button>
    </form>
  </div>
</div>

<script src="../js/script.js"></script>
</body>
</html>

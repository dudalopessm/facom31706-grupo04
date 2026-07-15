<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="javaBeans.*" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cave Fontana - Meu Perfil</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<%
    if (!temCliente) {
        response.sendRedirect("login.jsp");
        return;
    }

    String erroPerfil = (String) request.getAttribute("erroPerfil");
    if (erroPerfil == null) erroPerfil = request.getParameter("erroPerfil");
    String sucesso = request.getParameter("sucesso");
%>

<div class="container" style="padding-top:130px;padding-bottom:60px;max-width:500px">
  <a href="loja.jsp" class="voltar">
    <svg class="icon" style="width:14px;height:14px"><use href="#i-chevron"></use></svg>
    Voltar &agrave; loja
  </a>

  <h1 style="font-size:28px;font-style:italic;margin-bottom:4px">Meu Perfil</h1>
  <p style="color:var(--text-muted);margin-bottom:24px;font-size:14px">
    <jsp:getProperty name="clienteLogado" property="email" />
  </p>

  <% if ("ok".equals(sucesso)) { %>
    <p class="msg-sucesso">Perfil atualizado com sucesso!</p>
  <% } %>
  <% if (erroPerfil != null) { %>
    <p class="msg-erro"><%= erroPerfil %></p>
  <% } %>

  <form action="perfil" method="post" class="formulario" style="max-width:100%">
    <div class="account-field">
      <label for="nome">Nome</label>
      <input type="text" name="nome" id="nome" value="<%= clienteLogado.getNome() %>" required>
    </div>

    <div class="account-field">
      <label for="cpf">CPF</label>
      <input type="text" name="cpf" id="cpf" value="<%= clienteLogado.getCpf() %>" required>
    </div>

    <div class="account-field">
      <label for="senhaAtual">Senha atual</label>
      <input type="password" name="senhaAtual" id="senhaAtual" placeholder="••••••••" required>
      <p style="font-size:11px;color:var(--text-muted);margin-top:4px">Necess&aacute;ria para confirmar qualquer altera&ccedil;&atilde;o.</p>
    </div>

    <div class="account-field">
      <label for="novaSenha">Nova senha (deixe em branco para manter a atual)</label>
      <input type="password" name="novaSenha" id="novaSenha" placeholder="••••••••">
    </div>

    <div class="account-field">
      <label for="confirmarSenha">Confirmar nova senha</label>
      <input type="password" name="confirmarSenha" id="confirmarSenha" placeholder="••••••••">
    </div>

    <button type="submit" class="botao" style="margin-top:12px;width:100%;justify-content:center">Salvar altera&ccedil;&otilde;es</button>
  </form>

  <div style="text-align:center;margin-top:20px">
    <a href="logout" style="color:var(--text-muted);font-size:13px">Sair da conta</a>
  </div>
</div>

<%@ include file="rodape.jsp" %>
<script src="js/script.js"></script>
</body>
</html>

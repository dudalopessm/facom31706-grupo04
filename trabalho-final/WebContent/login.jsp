<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Entrar &mdash; Cave Fontana</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="page-center">
  <div class="container">
    <h1>Entrar</h1>
    <p class="sub">Acesse sua conta para comprar nossos vinhos.</p>

    <%
        String erroLogin = (String) request.getAttribute("erro");
        if (erroLogin == null) erroLogin = request.getParameter("erro");
        String cadastroOk = (String) request.getAttribute("cadastro");
        if (cadastroOk == null) cadastroOk = request.getParameter("cadastro");
    %>
    <% if ("invalido".equals(erroLogin)) { %>
      <p class="msg-erro">Email ou senha inv&aacute;lidos.</p>
    <% } else if ("campos_vazios".equals(erroLogin)) { %>
      <p class="msg-erro">Preencha todos os campos.</p>
    <% } %>
    <% if ("ok".equals(cadastroOk)) { %>
      <p class="msg-sucesso">Cadastro realizado! Fa&ccedil;a login.</p>
    <% } %>

    <form action="login" method="post" class="formulario" style="margin:0 auto">
      <div class="account-field">
        <label for="email">E-mail</label>
        <input type="email" name="email" id="email" placeholder="seu@email.com" autocomplete="email" required>
      </div>
      <div class="account-field">
        <label for="senha">Senha</label>
        <input type="password" name="senha" id="senha" placeholder="••••••••" autocomplete="current-password" required>
      </div>
      <button type="submit" class="botao account-submit">Entrar</button>
    </form>

    <p class="link-extra">Ainda n&atilde;o tem conta? <a href="cadastro.jsp">Criar conta</a></p>
  </div>
</div>

<%@ include file="rodape.jsp" %>
</body>
</html>

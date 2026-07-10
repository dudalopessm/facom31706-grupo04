<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Criar Conta &mdash; Cave Fontana</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="page-center">
  <div class="container">
    <h1>Criar Conta</h1>
    <p class="sub">Cadastre-se para comprar nossos vinhos selecionados.</p>

    <%
        String erro = (String) request.getAttribute("erro");
        if (erro == null) erro = request.getParameter("erro");
    %>
    <% if ("campos_vazios".equals(erro)) { %>
      <p class="msg-erro">Preencha todos os campos.</p>
    <% } else if ("senhas_diferentes".equals(erro)) { %>
      <p class="msg-erro">As senhas n&atilde;o conferem.</p>
    <% } else if ("email_existente".equals(erro)) { %>
      <p class="msg-erro">Este email j&aacute; est&aacute; cadastrado.</p>
    <% } else if ("erro_interno".equals(erro)) { %>
      <p class="msg-erro">Erro interno ao cadastrar. Tente novamente.</p>
    <% } %>

    <form action="cadastro" method="post" class="formulario" style="margin:0 auto">
      <div class="account-field">
        <label for="nome">Nome completo</label>
        <input type="text" name="nome" id="nome" placeholder="Seu nome" autocomplete="name" required>
      </div>
      <div class="account-field">
        <label for="email">E-mail</label>
        <input type="email" name="email" id="email" placeholder="seu@email.com" autocomplete="email" required>
      </div>
      <div class="account-field">
        <label for="cpf">CPF</label>
        <input type="text" name="cpf" id="cpf" placeholder="000.000.000-00" inputmode="numeric" required>
      </div>
      <div class="account-field">
        <label for="senha">Senha</label>
        <input type="password" name="senha" id="senha" placeholder="••••••••" autocomplete="new-password" required>
      </div>
      <div class="account-field">
        <label for="confirmarSenha">Confirmar senha</label>
        <input type="password" name="confirmarSenha" id="confirmarSenha" placeholder="••••••••" required>
      </div>
      <button type="submit" class="botao account-submit">Criar conta</button>
    </form>

    <p class="link-extra">J&aacute; tem conta? <a href="login.jsp">Fazer login</a></p>
  </div>
</div>

<%@ include file="rodape.jsp" %>
</body>
</html>

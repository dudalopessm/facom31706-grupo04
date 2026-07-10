<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    isErrorPage="true" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Erro &mdash; Cave Fontana</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
<style>
  .erro-page { text-align: center; padding: 140px 20px 60px; min-height: 70vh; display: flex; align-items: center; justify-content: center; }
  .erro-page h1 { font-size: 48px; font-style: italic; color: var(--wine-soft); }
  .erro-page p { color: var(--text-muted); margin: 16px 0 30px; font-size: 15px; }
  .erro-page .erro-detalhe { font-size: 12px; color: var(--text-muted); opacity: 0.6; max-width: 400px; margin: 0 auto; }
</style>
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="erro-page">
  <div class="container">
    <svg style="width:56px;height:56px;color:var(--wine-soft);margin:0 auto 16px" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6">
      <circle cx="12" cy="12" r="10"/>
      <path d="M12 8v4"/>
      <path d="M12 16h0"/>
    </svg>
    <h1>Ops! Algo deu errado.</h1>
    <p>Desculpe pelo inconveniente. Um erro inesperado ocorreu.</p>
    <% if (exception != null) { %>
      <p class="erro-detalhe"><%= exception.getMessage() %></p>
    <% } %>
    <a href="loja.jsp" class="botao">Voltar &agrave; loja</a>
  </div>
</div>

<%@ include file="rodape.jsp" %>
</body>
</html>

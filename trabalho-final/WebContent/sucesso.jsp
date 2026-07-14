<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cave Fontana - Sucesso</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
</head>
<body style="background:var(--bg);color:var(--text);font-family:'Inter',sans-serif;min-height:100vh;display:flex;align-items:center;justify-content:center;margin:0">
<%
    String voltar = request.getParameter("voltar");
    if (voltar == null || voltar.isEmpty()) {
        voltar = "loja.jsp";
    }
    String erro = request.getParameter("erro");
    boolean temErro = erro != null && !erro.isEmpty();
%>
<div style="text-align:center;max-width:440px;padding:40px">
    <% if (temErro) { %>
        <svg style="width:64px;height:64px;color:var(--wine-soft);margin:0 auto 20px;display:block" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <circle cx="12" cy="12" r="10"/>
            <path d="M12 8v4"/>
            <path d="M12 16h0"/>
        </svg>
        <h1 style="font-family:'Fraunces',Georgia,serif;font-size:22px;font-style:italic;color:var(--wine-soft);margin:0 0 12px"><%= erro %></h1>
    <% } else { %>
        <svg style="width:64px;height:64px;color:var(--amber-soft);margin:0 auto 20px;display:block" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M4 12l5 5L20 6"/>
        </svg>
        <h1 style="font-family:'Fraunces',Georgia,serif;font-size:28px;font-style:italic;color:var(--text);margin:0 0 8px">Opera&ccedil;&atilde;o conclu&iacute;da com sucesso!</h1>
    <% } %>
    <a href="<%= voltar %>" class="botao" style="margin-top:20px;text-decoration:none;display:inline-flex">Voltar</a>
</div>
</body>
</html>

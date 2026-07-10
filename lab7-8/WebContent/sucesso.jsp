<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Login com Sucesso</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <p class="result-msg success">
      <i class="ti ti-circle-check"></i>
      Login realizado com sucesso! Bem-vindo, <strong><%= request.getAttribute("usuario") != null ? request.getAttribute("usuario") : "" %></strong>.
    </p>
    <a class="btn primary" href="index.jsp"><i class="ti ti-home"></i> Voltar ao inicio</a>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

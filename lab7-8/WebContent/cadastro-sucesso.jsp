<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cadastro Realizado</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <p class="result-msg success">
      <i class="ti ti-circle-check"></i>
      Cadastro realizado com sucesso para <strong><%= request.getAttribute("nome") != null ? request.getAttribute("nome") : "" %></strong>!
    </p>
    <a class="btn primary" href="login.jsp"><i class="ti ti-login"></i> Ir para Login</a>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Erro no Cadastro</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <p class="result-msg error">
      <i class="ti ti-alert-triangle"></i>
      <%= request.getAttribute("erro") != null ? request.getAttribute("erro") : "Erro ao realizar cadastro." %>
    </p>
    <a class="btn primary" href="cadastro.jsp"><i class="ti ti-user-plus"></i> Tentar novamente</a>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

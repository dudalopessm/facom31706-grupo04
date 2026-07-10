<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Resultado da Busca</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <p class="result-msg success">
      <i class="ti ti-circle-check"></i>
      Resultado para "<strong><%= request.getAttribute("termo") != null ? request.getAttribute("termo") : "" %></strong>":
      <%= request.getAttribute("mensagem") != null ? request.getAttribute("mensagem") : "" %>
    </p>
    <a class="btn primary" href="busca.jsp"><i class="ti ti-search"></i> Nova busca</a>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

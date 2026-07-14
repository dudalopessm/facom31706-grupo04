<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana - Erro na Busca</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <p class="result-msg error">
      <i class="ti ti-alert-triangle"></i>
      <%= request.getAttribute("buscaErro") != null ? request.getAttribute("buscaErro") : "Nenhum vinho encontrado." %>
    </p>
    <div class="form-actions" style="margin-top:12px;">
      <a class="btn primary" href="vinhos.jsp"><i class="ti ti-search"></i> Nova busca</a>
    </div>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

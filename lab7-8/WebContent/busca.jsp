<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana - Busca</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <h2><i class="ti ti-search"></i> Buscar Produto</h2>
    <form class="form-grid" action="BuscaServlet" method="post">
      <div class="form-row">
        <label for="termo">Termo</label>
        <input type="text" id="termo" name="termo" placeholder="Digite nome do produto..." required>
      </div>
      <div class="form-actions">
        <button class="btn primary" type="submit"><i class="ti ti-search"></i> Buscar</button>
      </div>
    </form>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

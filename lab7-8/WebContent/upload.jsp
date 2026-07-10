<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana - Upload</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <h2><i class="ti ti-upload"></i> Upload de Arquivos</h2>
    <form action="UploadServlet" method="post" enctype="multipart/form-data">
      <div class="form-grid">
        <div class="form-row">
          <label for="arquivo1">Arquivo 1</label>
          <input type="file" id="arquivo1" name="arquivo1">
        </div>
        <div class="form-row">
          <label for="arquivo2">Arquivo 2</label>
          <input type="file" id="arquivo2" name="arquivo2">
        </div>
        <div class="form-actions">
          <button class="btn primary" type="submit"><i class="ti ti-upload"></i> Enviar</button>
        </div>
      </div>
    </form>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

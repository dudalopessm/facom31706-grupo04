<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana - Lab 7-8</title>
</head>
<body>
<div class="root">
  <jsp:include page="header.jsp" />

  <div class="sections">
    <div class="card">
      <div class="card-head"><i class="ti ti-login"></i><span>Login</span></div>
      <div class="actions">
        <a class="btn primary" href="login.jsp"><i class="ti ti-login"></i> Entrar</a>
      </div>
    </div>
    <div class="card">
      <div class="card-head"><i class="ti ti-user-plus"></i><span>Cadastro</span></div>
      <div class="actions">
        <a class="btn primary" href="cadastro.jsp"><i class="ti ti-user-plus"></i> Cadastrar</a>
      </div>
    </div>
    <div class="card">
      <div class="card-head"><i class="ti ti-search"></i><span>Busca de Produtos</span></div>
      <div class="actions">
        <a class="btn primary" href="busca.jsp"><i class="ti ti-search"></i> Buscar</a>
      </div>
    </div>
    <div class="card">
      <div class="card-head"><i class="ti ti-upload"></i><span>Upload de Arquivos</span></div>
      <div class="actions">
        <a class="btn primary" href="upload.jsp"><i class="ti ti-upload"></i> Enviar arquivos</a>
      </div>
    </div>
    <div class="card">
      <div class="card-head"><i class="ti ti-download"></i><span>Downloads</span></div>
      <div class="actions">
        <a class="btn primary" href="downloads.jsp"><i class="ti ti-download"></i> Baixar arquivos</a>
      </div>
    </div>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

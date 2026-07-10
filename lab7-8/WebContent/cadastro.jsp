<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana - Cadastro</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <h2><i class="ti ti-user-plus"></i> Cadastro de Usuario</h2>
    <form class="form-grid" action="CadastroServlet" method="post">
      <div class="form-row">
        <label for="nome">Nome</label>
        <input type="text" id="nome" name="nome" required>
      </div>
      <div class="form-row">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" required>
      </div>
      <div class="form-row">
          <label for="cpf">CPF</label>
          <input type="text" id="cpf" name="cpf" maxlength="11" pattern="\d{11}" title="Digite apenas os 11 digitos do CPF" required>
        </div>
      <div class="form-row">
        <label for="senha">Senha</label>
        <input type="password" id="senha" name="senha" required>
      </div>
      <div class="form-actions">
        <button class="btn primary" type="submit"><i class="ti ti-device-floppy"></i> Cadastrar</button>
      </div>
    </form>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

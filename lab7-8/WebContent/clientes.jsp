<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana - Clientes</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <h2><i class="ti ti-list"></i> Listar clientes</h2>
    <a class="btn primary" href="ClienteServlet" target="cli-result"><i class="ti ti-refresh"></i> Carregar lista</a>
    <div style="margin-top:8px;"><iframe name="cli-result" style="width:100%;border:none;min-height:60px;"></iframe></div>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-plus"></i> Cadastrar cliente</legend>
      <form class="form-grid" action="ClienteServlet" method="post" target="cli-result">
        <input type="hidden" name="acao" value="inserir">
        <div class="form-row">
          <label for="c-cpf">CPF</label>
          <input type="text" id="c-cpf" name="cpf" maxlength="11" pattern="\d{11}" title="Digite apenas os 11 digitos do CPF" required>
        </div>
        <div class="form-row">
          <label for="c-nome">Nome</label>
          <input type="text" id="c-nome" name="nome" required>
        </div>
        <div class="form-row">
          <label for="c-email">Email</label>
          <input type="email" id="c-email" name="email" required>
        </div>
        <div class="form-row">
          <label for="c-senha">Senha</label>
          <input type="text" id="c-senha" name="senha" required>
        </div>
        <div class="form-actions">
          <button class="btn primary" type="submit"><i class="ti ti-device-floppy"></i> Cadastrar</button>
        </div>
      </form>
    </fieldset>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-pencil"></i> Alterar cliente</legend>
      <form class="form-grid" action="ClienteServlet" method="post" target="cli-result">
        <input type="hidden" name="acao" value="alterar">
        <div class="form-row">
          <label for="c-alt-cpf">CPF</label>
          <input type="text" id="c-alt-cpf" name="cpf" maxlength="11" pattern="\d{11}" title="Digite apenas os 11 digitos do CPF" required>
        </div>
        <div class="form-row">
          <label for="c-alt-nome">Novo nome</label>
          <input type="text" id="c-alt-nome" name="nome" required>
        </div>
        <div class="form-row">
          <label for="c-alt-email">Novo email</label>
          <input type="email" id="c-alt-email" name="email" required>
        </div>
        <div class="form-row">
          <label for="c-alt-senha">Nova senha</label>
          <input type="text" id="c-alt-senha" name="senha" required>
        </div>
        <div class="form-actions">
          <button class="btn primary" type="submit"><i class="ti ti-device-floppy"></i> Alterar</button>
        </div>
      </form>
    </fieldset>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-search"></i> Consultar cliente por CPF</legend>
      <form class="form-grid" action="ClienteServlet" method="get" target="cli-result">
        <input type="hidden" name="acao" value="buscar">
        <div class="form-row">
          <label for="c-buscar-cpf">CPF</label>
          <input type="text" id="c-buscar-cpf" name="cpf" maxlength="11" pattern="\d{11}" title="Digite apenas os 11 digitos do CPF" required>
        </div>
        <div class="form-actions">
          <button class="btn primary" type="submit"><i class="ti ti-search"></i> Consultar</button>
        </div>
      </form>
    </fieldset>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-trash"></i> Remover cliente por CPF</legend>
      <form class="form-grid" action="ClienteServlet" method="post" target="cli-result">
        <input type="hidden" name="acao" value="remover">
        <div class="form-row">
          <label for="c-rem-cpf">CPF</label>
          <input type="text" id="c-rem-cpf" name="cpf" maxlength="11" pattern="\d{11}" title="Digite apenas os 11 digitos do CPF" required>
        </div>
        <div class="form-actions">
          <button class="btn primary" type="submit"><i class="ti ti-trash"></i> Remover</button>
        </div>
      </form>
    </fieldset>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

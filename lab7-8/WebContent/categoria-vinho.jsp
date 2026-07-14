<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana - Categorias</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <h2><i class="ti ti-list"></i> Listar categorias</h2>
    <a class="btn primary" href="CategoriaVinhoServlet" target="cat-result"><i class="ti ti-refresh"></i> Carregar lista</a>
    <div style="margin-top:8px;"><iframe name="cat-result" style="width:100%;border:none;min-height:60px;"></iframe></div>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-plus"></i> Cadastrar categoria</legend>
      <form class="form-grid" action="CategoriaVinhoServlet" method="post" target="cat-result">
        <input type="hidden" name="acao" value="inserir">
        <div class="form-row">
          <label for="cat-nome">Nome</label>
          <input type="text" id="cat-nome" name="nome" required>
        </div>
        <div class="form-row">
          <label for="cat-descricao">Descricao</label>
          <input type="text" id="cat-descricao" name="descricao">
        </div>
        <div class="form-actions">
          <button class="btn primary" type="submit"><i class="ti ti-device-floppy"></i> Cadastrar</button>
        </div>
      </form>
    </fieldset>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-pencil"></i> Alterar categoria</legend>
      <form class="form-grid" action="CategoriaVinhoServlet" method="post" target="cat-result">
        <input type="hidden" name="acao" value="alterar">
        <div class="form-row">
          <label for="cat-alt-nome-original">Nome atual</label>
          <input type="text" id="cat-alt-nome-original" name="nomeOriginal" required>
        </div>
        <div class="form-row">
          <label for="cat-alt-nome">Novo nome</label>
          <input type="text" id="cat-alt-nome" name="nome" required>
        </div>
        <div class="form-row">
          <label for="cat-alt-descricao">Nova descricao</label>
          <input type="text" id="cat-alt-descricao" name="descricao">
        </div>
        <div class="form-actions">
          <button class="btn primary" type="submit"><i class="ti ti-device-floppy"></i> Alterar</button>
        </div>
      </form>
    </fieldset>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-search"></i> Consultar categoria por nome</legend>
      <form class="form-grid" action="CategoriaVinhoServlet" method="get" target="cat-result">
        <input type="hidden" name="acao" value="buscar">
        <div class="form-row">
          <label for="cat-buscar-nome">Nome</label>
          <input type="text" id="cat-buscar-nome" name="nome" required>
        </div>
        <div class="form-actions">
          <button class="btn primary" type="submit"><i class="ti ti-search"></i> Consultar</button>
        </div>
      </form>
    </fieldset>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-trash"></i> Remover categoria por nome</legend>
      <form class="form-grid" action="CategoriaVinhoServlet" method="post" target="cat-result">
        <input type="hidden" name="acao" value="remover">
        <div class="form-row">
          <label for="cat-rem-nome">Nome</label>
          <input type="text" id="cat-rem-nome" name="nome" required>
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

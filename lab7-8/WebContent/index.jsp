<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana</title>
</head>
<body>
<div class="root">
  <jsp:include page="header.jsp" />

  <div class="sections">
    <div class="card">
      <div class="card-head"><i class="ti ti-tag"></i><<span>Categorias de Vinho</span></div>
      <p>Cadastrar, alterar, consultar e remover categorias de vinhos.</p>
      <div class="actions">
        <a class="btn primary" href="categoria-vinho.jsp"><i class="ti ti-tag"></i> Acessar</a>
      </div>
    </div>
    <div class="card">
      <div class="card-head"><i class="ti ti-bottle"></i><span>Vinhos</span></div>
      <p>Listar, buscar por nome+safra, enviar fotos e baixar arquivos.</p>
      <div class="actions">
        <a class="btn primary" href="vinhos.jsp"><i class="ti ti-bottle"></i> Acessar</a>
      </div>
    </div>
    <div class="card">
      <div class="card-head"><i class="ti ti-user"></i><span>Clientes</span></div>
      <p>Cadastrar, alterar, consultar e remover clientes por CPF.</p>
      <div class="actions">
        <a class="btn primary" href="clientes.jsp"><i class="ti ti-user"></i> Acessar</a>
      </div>
    </div>
    <div class="card">
      <div class="card-head"><i class="ti ti-shopping-cart"></i><span>Pedidos</span></div>
      <p>Gerenciar pedidos: status, itens e consultas por CPF.</p>
      <div class="actions">
        <a class="btn primary" href="pedidos.jsp"><i class="ti ti-shopping-cart"></i> Acessar</a>
      </div>
    </div>
    <div class="card">
      <div class="card-head"><i class="ti ti-list-details"></i><span>Itens do Pedido</span></div>
      <p>Inserir, alterar e remover vinhos dos pedidos.</p>
      <div class="actions">
        <a class="btn primary" href="item-pedido.jsp"><i class="ti ti-list-details"></i> Acessar</a>
      </div>
    </div>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

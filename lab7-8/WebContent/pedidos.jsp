<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana - Pedidos</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <h2><i class="ti ti-list"></i> Listar pedidos</h2>
    <a class="btn primary" href="PedidoServlet" target="ped-result"><i class="ti ti-refresh"></i> Carregar lista</a>
    <div style="margin-top:8px;"><iframe name="ped-result" style="width:100%;border:none;min-height:60px;"></iframe></div>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-plus"></i> Cadastrar pedido</legend>
      <form class="form-grid" action="PedidoServlet" method="post" target="ped-result">
        <input type="hidden" name="acao" value="inserir">
        <div class="form-row">
          <label for="p-cliente-cpf">CPF do cliente</label>
          <input type="text" id="p-cliente-cpf" name="clienteCpf" maxlength="11" pattern="\d{11}" title="Digite apenas os 11 digitos do CPF" required>
        </div>
        <div class="form-row">
          <label for="p-status">Status</label>
          <select id="p-status" name="status" required>
            <option value="">Selecione...</option>
            <option value="PENDENTE">PENDENTE</option>
            <option value="PAGO">PAGO</option>
            <option value="ENVIADO">ENVIADO</option>
            <option value="ENTREGUE">ENTREGUE</option>
            <option value="CANCELADO">CANCELADO</option>
          </select>
        </div>
        <div class="form-actions">
          <button class="btn primary" type="submit"><i class="ti ti-device-floppy"></i> Cadastrar</button>
        </div>
      </form>
    </fieldset>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-pencil"></i> Alterar pedido</legend>
      <form class="form-grid" action="PedidoServlet" method="post" target="ped-result">
        <input type="hidden" name="acao" value="alterar">
        <div class="form-row">
          <label for="p-alt-id">ID do pedido</label>
          <input type="number" id="p-alt-id" name="id" required min="1">
        </div>
        <div class="form-row">
          <label for="p-alt-cliente-cpf">Novo CPF do cliente</label>
          <input type="text" id="p-alt-cliente-cpf" name="clienteCpf" maxlength="11" pattern="\d{11}" title="Digite apenas os 11 digitos do CPF" required>
        </div>
        <div class="form-row">
          <label for="p-alt-status">Novo status</label>
          <select id="p-alt-status" name="status" required>
            <option value="">Selecione...</option>
            <option value="PENDENTE">PENDENTE</option>
            <option value="PAGO">PAGO</option>
            <option value="ENVIADO">ENVIADO</option>
            <option value="ENTREGUE">ENTREGUE</option>
            <option value="CANCELADO">CANCELADO</option>
          </select>
        </div>
        <div class="form-actions">
          <button class="btn primary" type="submit"><i class="ti ti-device-floppy"></i> Alterar</button>
        </div>
      </form>
    </fieldset>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-search"></i> Consultar pedidos por CPF do cliente</legend>
      <form class="form-grid" action="PedidoServlet" method="get" target="ped-result">
        <input type="hidden" name="acao" value="buscarPorCpf">
        <div class="form-row">
          <label for="p-buscar-cpf">CPF do cliente</label>
          <input type="text" id="p-buscar-cpf" name="clienteCpf" maxlength="11" pattern="\d{11}" title="Digite apenas os 11 digitos do CPF" required>
        </div>
        <div class="form-actions">
          <button class="btn primary" type="submit"><i class="ti ti-search"></i> Consultar</button>
        </div>
      </form>
    </fieldset>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-trash"></i> Remover pedido por CPF + data/hora</legend>
      <form class="form-grid" action="PedidoServlet" method="post" target="ped-result">
        <input type="hidden" name="acao" value="removerPorCpfData">
        <div class="form-row">
          <label for="p-rem-cpf">CPF do cliente</label>
          <input type="text" id="p-rem-cpf" name="clienteCpf" maxlength="11" pattern="\d{11}" title="Digite apenas os 11 digitos do CPF" required>
        </div>
        <div class="form-row">
          <label for="p-rem-data">Data/Hora (YYYY-MM-DD HH:MM:SS)</label>
          <input type="text" id="p-rem-data" name="data" placeholder="Ex: 2026-06-21 23:47:15" pattern="\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}" title="Formato: YYYY-MM-DD HH:MM:SS" required>
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

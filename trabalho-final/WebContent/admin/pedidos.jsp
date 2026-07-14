<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, javaBeans.*, dao.*" %>
<%
    Cliente admin = (Cliente) session.getAttribute("clienteLogado");
    if (admin == null || !"ADMIN".equals(admin.getTipo())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String aba = request.getParameter("aba");
    if (aba == null) aba = "pedidos";

    PedidoDAO pedidoDAO = new PedidoDAO();
    List<Pedido> todosPedidos = pedidoDAO.listarTodos();

    SacolaDAO sacolaDAO = new SacolaDAO();
    List<Sacola> todasSacolas = sacolaDAO.listarTodas();

    ClienteDAO clienteDAO = new ClienteDAO();
    ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
    ItemSacolaDAO itemSacolaDAO = new ItemSacolaDAO();

    String popupErro = (String) session.getAttribute("popupErro");
    if (popupErro != null) {
        session.removeAttribute("popupErro");
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cave Fontana - Pedidos</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="../css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="admin-layout">
  <div class="container">
    <h1>Pedidos e Sacolas</h1>

    <%-- ABAS --%>
    <div class="account-tabs" role="tablist" style="margin-bottom:24px;display:inline-flex;width:auto;max-width:300px">
      <a href="pedidos.jsp?aba=pedidos" class="account-tab <%= "pedidos".equals(aba) ? "active" : "" %>"
         style="<%= "pedidos".equals(aba) ? "background:var(--amber);color:#1C1B19" : "color:var(--text-muted)" %>;text-decoration:none"
         role="tab" aria-selected="<%= "pedidos".equals(aba) %>">Pedidos</a>
      <a href="pedidos.jsp?aba=sacolas" class="account-tab <%= "sacolas".equals(aba) ? "active" : "" %>"
         style="<%= "sacolas".equals(aba) ? "background:var(--amber);color:#1C1B19" : "color:var(--text-muted)" %>;text-decoration:none"
         role="tab" aria-selected="<%= "sacolas".equals(aba) %>">Sacolas</a>
    </div>

    <%-- ABA PEDIDOS --%>
    <% if ("pedidos".equals(aba)) { %>
      <div class="table-wrap" style="margin-top:0">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Cliente</th>
            <th>Data</th>
            <th>Valor Total</th>
            <th>Pagamento</th>
            <th>Envio</th>
            <th>A&ccedil;&otilde;es</th>
          </tr>
        </thead>
        <tbody>
          <% for (Pedido pedido : todosPedidos) {
              Sacola s = sacolaDAO.buscarPorId(pedido.getIdSacola());
              Cliente c = (s != null) ? clienteDAO.buscarPorEmail(s.getEmailCliente()) : null;
          %>
          <tr>
            <td><%= pedido.getId() %></td>
            <td><%= c != null ? c.getNome() : "" %></td>
            <td><%= pedido.getDataConclusao() != null ? pedido.getDataConclusao().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "" %></td>
            <td>R$ <%= String.format("%.2f", pedido.getValorTotal()) %></td>
            <td><span class="status status-<%= pedido.getStatusPagamento().toLowerCase() %>"><%= pedido.getStatusPagamento() %></span></td>
            <td><span class="status status-<%= pedido.getStatusEnvio().toLowerCase() %>"><%= pedido.getStatusEnvio() %></span></td>
            <td>
              <div style="display:flex;gap:6px;align-items:center">
                <% if (!"CANCELADO".equals(pedido.getStatusPagamento())) { %>
                  <form action="../admin" method="post" style="display:inline">
                    <input type="hidden" name="acao" value="atualizarEnvio">
                    <input type="hidden" name="idPedido" value="<%= pedido.getId() %>">
                    <select name="statusEnvio" onchange="this.form.submit()"
                            style="padding:4px 8px;border-radius:6px;background:var(--surface);border:1px solid var(--line);color:var(--text);font-size:12px">
                      <option value="PENDENTE" <%= "PENDENTE".equals(pedido.getStatusEnvio()) ? "selected" : "" %>>PENDENTE</option>
                      <option value="PREPARANDO" <%= "PREPARANDO".equals(pedido.getStatusEnvio()) ? "selected" : "" %>>PREPARANDO</option>
                      <option value="ENVIADO" <%= "ENVIADO".equals(pedido.getStatusEnvio()) ? "selected" : "" %>>ENVIADO</option>
                      <option value="ENTREGUE" <%= "ENTREGUE".equals(pedido.getStatusEnvio()) ? "selected" : "" %>>ENTREGUE</option>
                    </select>
                  </form>
                  <form action="../admin" method="post" style="display:inline"
                        onsubmit="return confirm('Cancelar pedido #<%= pedido.getId() %>?')">
                    <input type="hidden" name="acao" value="cancelarPedido">
                    <input type="hidden" name="idPedido" value="<%= pedido.getId() %>">
                    <button type="submit" class="botao-remover botao-pequeno">Cancelar</button>
                  </form>
                <% } else { %>
                  <span style="font-size:11px;color:var(--text-muted)"></span>
                <% } %>
                <button onclick="toggleDetalhe('pedido-<%= pedido.getId() %>')" class="botao botao-pequeno" style="background:var(--surface-2);color:var(--text)">Itens</button>
              </div>
            </td>
          </tr>
          <tr id="pedido-<%= pedido.getId() %>" style="display:none">
            <td colspan="8" style="background:var(--surface);padding:16px 24px">
              <%
                  List<ItemPedido> itensPedido = itemPedidoDAO.listarPorPedido(pedido.getId());
                  if (!itensPedido.isEmpty()) {
              %>
              <table style="margin:0">
                <thead>
                  <tr>
                    <th>Vinho</th>
                    <th>Qtd</th>
                    <th>Pre&ccedil;o Unit.</th>
                    <th>Subtotal</th>
                  </tr>
                </thead>
                <tbody>
                  <% for (ItemPedido item : itensPedido) { %>
                  <tr>
                    <td>
                      <img src="<%= item.getVinho().getCaminhoFoto() != null ? "../" + item.getVinho().getCaminhoFoto() : "../images/vinhos/sem-foto.jpg" %>"
                           alt="" onerror="this.onerror=null;this.src='../images/vinhos/sem-foto.jpg';" class="miniatura">
                      <%= item.getVinho().getNome() %>
                    </td>
                    <td><%= item.getQuantidade() %></td>
                    <td>R$ <%= String.format("%.2f", item.getPrecoUnitario()) %></td>
                    <td>R$ <%= String.format("%.2f", item.getSubtotal()) %></td>
                  </tr>
                  <% } %>
                </tbody>
              </table>
              <% } else { %>
                <p style="color:var(--text-muted);margin:0">Nenhum item encontrado.</p>
              <% } %>
            </td>
          </tr>
          <% } %>
        </tbody>
      </table>
      </div>
      <% if (todosPedidos.isEmpty()) { %>
        <p class="vazio">Nenhum pedido encontrado.</p>
      <% } %>
    <% } %>

    <%-- ABA SACOLAS --%>
    <% if ("sacolas".equals(aba)) {
        String statusFiltro = request.getParameter("statusSacola");
    %>
      <div class="filters" style="margin-bottom:20px">
        <a href="pedidos.jsp?aba=sacolas" class="filter-chip <%= (statusFiltro == null) ? "active" : "" %>"
           style="<%= (statusFiltro == null) ? "background:var(--amber);color:#1C1B19" : "color:var(--text-muted)" %>;text-decoration:none">Todas</a>
        <a href="pedidos.jsp?aba=sacolas&statusSacola=ATIVA" class="filter-chip <%= "ATIVA".equals(statusFiltro) ? "active" : "" %>"
           style="<%= "ATIVA".equals(statusFiltro) ? "background:var(--amber);color:#1C1B19" : "color:var(--text-muted)" %>;text-decoration:none">Ativas</a>
        <a href="pedidos.jsp?aba=sacolas&statusSacola=CONVERTIDA" class="filter-chip <%= "CONVERTIDA".equals(statusFiltro) ? "active" : "" %>"
           style="<%= "CONVERTIDA".equals(statusFiltro) ? "background:var(--amber);color:#1C1B19" : "color:var(--text-muted)" %>;text-decoration:none">Convertidas</a>
      </div>

      <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Cliente</th>
            <th>Data Cria&ccedil;&atilde;o</th>
            <th>Status</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <% for (Sacola sacola : todasSacolas) {
              if ("CANCELADA".equals(sacola.getStatus())) continue;
              if (statusFiltro != null && !statusFiltro.equals(sacola.getStatus())) continue;
              Cliente c = clienteDAO.buscarPorEmail(sacola.getEmailCliente());
          %>
          <tr>
            <td><%= sacola.getId() %></td>
            <td><%= c != null ? c.getNome() : "" %></td>
            <td><%= sacola.getDataCriacao() != null ? sacola.getDataCriacao().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "" %></td>
            <td>
              <% if ("ATIVA".equals(sacola.getStatus())) { %>
                <span class="status status-pendente">ATIVA</span>
              <% } else if ("CONVERTIDA".equals(sacola.getStatus())) { %>
                <span class="status status-pago">CONVERTIDA</span>
              <% } else { %>
                <span class="status status-cancelado">CANCELADA</span>
              <% } %>
            </td>
            <td>
              <button onclick="toggleDetalhe('sacola-<%= sacola.getId() %>')" class="botao botao-pequeno" style="background:var(--surface-2);color:var(--text)">Itens</button>
            </td>
          </tr>
          <tr id="sacola-<%= sacola.getId() %>" style="display:none">
            <td colspan="5" style="background:var(--surface);padding:16px 24px">
              <%
                  List<ItemSacola> itens = itemSacolaDAO.listarPorSacola(sacola.getId());
                  if (!itens.isEmpty()) {
                    double totalSacola = 0;
              %>
              <table style="margin:0">
                <thead>
                  <tr>
                    <th>Vinho</th>
                    <th>Qtd</th>
                    <th>Pre&ccedil;o Unit.</th>
                    <th>Subtotal</th>
                  </tr>
                </thead>
                <tbody>
                  <% for (ItemSacola item : itens) {
                      totalSacola += item.getSubtotal();
                  %>
                  <tr>
                    <td>
                      <img src="<%= item.getVinho().getCaminhoFoto() != null ? "../" + item.getVinho().getCaminhoFoto() : "../images/vinhos/sem-foto.jpg" %>"
                           alt="" onerror="this.onerror=null;this.src='../images/vinhos/sem-foto.jpg';" class="miniatura">
                      <%= item.getVinho().getNome() %>
                    </td>
                    <td><%= item.getQuantidade() %></td>
                    <td>R$ <%= String.format("%.2f", item.getVinho().getPreco()) %></td>
                    <td>R$ <%= String.format("%.2f", item.getSubtotal()) %></td>
                  </tr>
                  <% } %>
                </tbody>
                <tfoot>
                  <tr>
                    <td colspan="3" style="text-align:right;font-weight:600">Total:</td>
                    <td style="font-weight:700;color:var(--amber-soft)">R$ <%= String.format("%.2f", totalSacola) %></td>
                  </tr>
                </tfoot>
              </table>
              <% } else { %>
                <p style="color:var(--text-muted);margin:0">Sacola vazia.</p>
              <% } %>
            </td>
          </tr>
          <% } %>
        </tbody>
      </table>
      </div>
    <% } %>

  </div>
</div>

<script>
  function toggleDetalhe(id) {
    var el = document.getElementById(id);
    if (el) { el.style.display = el.style.display === 'none' ? 'table-row' : 'none'; }
  }
</script>
<script src="../js/script.js"></script>
<% if (popupErro != null) { %>
<script>alert("<%= popupErro %>");</script>
<% } %>
</body>
</html>

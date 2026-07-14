<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, lab07.modelo.Vinho, lab07.ConnectionFactory, lab07.dao.VinhoDao, java.sql.Connection" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana - Vinhos</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <h2><i class="ti ti-search"></i> Buscar Vinho</h2>
    <form class="form-grid" action="BuscaVinhoServlet" method="post">
      <div class="form-row">
        <label for="v-nome">Nome</label>
        <input type="text" id="v-nome" name="nome" required>
      </div>
      <div class="form-row">
        <label for="v-safra">Safra</label>
        <input type="number" id="v-safra" name="safra" min="1900" required>
      </div>
      <div class="form-actions">
        <button class="btn primary" type="submit"><i class="ti ti-search"></i> Buscar</button>
      </div>
    </form>
    <%
      String erro = (String) request.getAttribute("buscaErro");
      if (erro != null) {
    %>
      <p class="result-msg error" style="margin-top:10px;"><%= erro %></p>
    <%
      }
    %>
  </div>

  <div class="card">
    <h2><i class="ti ti-list"></i> Todos os Vinhos</h2>
    <%
      try {
        Connection conn = new ConnectionFactory().getConnection();
        VinhoDao dao = new VinhoDao(conn);
        List<Vinho> vinhos = dao.getLista();
        conn.close();

        if (vinhos.isEmpty()) {
    %>
      <p class="result-msg info">Nenhum vinho cadastrado.</p>
    <%
        } else {
    %>
    <table>
      <tr><th>ID</th><th>Nome</th><th>Safra</th><th>Preco</th><th>Categoria</th><th>Detalhes</th></tr>
    <%
          for (Vinho v : vinhos) {
    %>
      <tr>
        <td><%= v.getId() %></td>
        <td><%= v.getNome() %></td>
        <td><%= v.getSafra() %></td>
        <td>R$ <%= String.format("%.2f", v.getPreco()) %></td>
        <td><%= v.getCategoriaNome() %></td>
        <td><a class="btn primary" href="vinho-detalhe.jsp?id=<%= v.getId() %>"><i class="ti ti-eye"></i> Ver</a></td>
      </tr>
    <%
          }
    %>
    </table>
    <%
        }
      } catch (Exception e) {
    %>
      <p class="result-msg error">Erro ao carregar vinhos: <%= e.getMessage() %></p>
    <%
      }
    %>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

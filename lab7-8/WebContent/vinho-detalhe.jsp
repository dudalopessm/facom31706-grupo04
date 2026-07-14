<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.File, lab07.modelo.Vinho, lab07.ConnectionFactory, lab07.dao.VinhoDao, java.sql.Connection" %>
<%
  String idParam = request.getParameter("id");
  Vinho vinho = null;
  if (idParam != null) {
    try {
      Connection conn = new ConnectionFactory().getConnection();
      VinhoDao dao = new VinhoDao(conn);
      vinho = dao.buscaPorId(Integer.parseInt(idParam));
      conn.close();
    } catch (Exception e) { }
  }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana - <%= vinho != null ? vinho.getNome() : "Detalhe do Vinho" %></title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <%
    String uploadMsg = (String) request.getAttribute("uploadMsg");
    String uploadTipo = (String) request.getAttribute("uploadTipo");
    if (uploadMsg != null) {
  %>
    <div class="card">
      <p class="result-msg <%= uploadTipo != null ? uploadTipo : "info" %>"><%= uploadMsg %></p>
    </div>
  <%
    }
  %>

  <%
    if (vinho == null) {
  %>
  <div class="card">
    <p class="result-msg error">Vinho nao encontrado.</p>
    <a class="btn primary" href="vinhos.jsp"><i class="ti ti-bottle"></i> Voltar para Vinhos</a>
  </div>
  <%
    } else {
      int vId = vinho.getId();
  %>
  <div class="card">
    <h2><i class="ti ti-bottle"></i> <%= vinho.getNome() %> (<%= vinho.getSafra() %>)</h2>
    <table>
      <tr><th>ID</th><td><%= vinho.getId() %></td></tr>
      <tr><th>Nome</th><td><%= vinho.getNome() %></td></tr>
      <tr><th>Safra</th><td><%= vinho.getSafra() %></td></tr>
      <tr><th>Preco</th><td>R$ <%= String.format("%.2f", vinho.getPreco()) %></td></tr>
      <tr><th>Categoria</th><td><%= vinho.getCategoriaNome() %></td></tr>
    </table>
  </div>

  <div class="card">
    <h2><i class="ti ti-photo"></i> Fotos do Vinho</h2>
    <%
      String fotosDir = application.getRealPath("/arquivos/fotos/" + vId);
      File dir = new File(fotosDir);
      File[] fotos = dir.exists() ? dir.listFiles() : null;
      if (fotos == null || fotos.length == 0) {
    %>
      <p class="result-msg info">Nenhuma foto enviada para este vinho.</p>
    <%
      } else {
    %>
    <table>
      <tr><th>Foto</th><th>Tamanho</th><th>Download</th></tr>
    <%
        for (File f : fotos) {
          String nome = f.getName();
    %>
      <tr>
        <td><%= nome %></td>
        <td><%= f.length() %> bytes</td>
        <td><a class="btn primary" href="DownloadServlet?dir=fotos/<%= vId %>&arquivo=<%= java.net.URLEncoder.encode(nome, "UTF-8") %>"><i class="ti ti-download"></i> Baixar</a></td>
      </tr>
    <%
        }
    %>
    </table>
    <%
      }
    %>
  </div>

  <div class="card">
    <fieldset>
      <legend><i class="ti ti-upload"></i> Enviar Fotos</legend>
      <form action="UploadFotoServlet" method="post" enctype="multipart/form-data">
        <input type="hidden" name="idVinho" value="<%= vId %>">
        <div class="form-grid">
          <div class="form-row">
            <label for="foto1">Foto 1</label>
            <input type="file" id="foto1" name="foto1" required>
          </div>
          <div class="form-row">
            <label for="foto2">Foto 2</label>
            <input type="file" id="foto2" name="foto2">
          </div>
          <div class="form-actions">
            <button class="btn primary" type="submit"><i class="ti ti-upload"></i> Enviar Fotos</button>
          </div>
        </div>
      </form>
    </fieldset>
  </div>
  <%
    }
  %>

  <a class="back" href="vinhos.jsp"><i class="ti ti-arrow-left"></i> Voltar para lista de vinhos</a>
  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

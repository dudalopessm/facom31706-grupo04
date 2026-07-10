<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.File" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cave Fontana - Downloads</title>
</head>
<body>
<div class="page">
  <jsp:include page="header.jsp" />

  <div class="card">
    <h2><i class="ti ti-download"></i> Arquivos Disponiveis</h2>
    <%
      String dirPath = application.getRealPath("/arquivos");
      File dir = new File(dirPath);
      File[] arquivos = dir.exists() ? dir.listFiles() : null;

      if (arquivos == null || arquivos.length == 0) {
    %>
      <p class="result-msg info">Nenhum arquivo enviado ainda.</p>
    <%
      } else {
    %>
    <table>
      <tr><th>Arquivo</th><th>Tamanho (bytes)</th><th>Download</th></tr>
    <%
        for (File f : arquivos) {
          if (f.isFile()) {
            String nome = f.getName();
            long tamanho = f.length();
    %>
      <tr>
        <td><%= nome %></td>
        <td><%= tamanho %></td>
        <td><a class="btn primary" href="DownloadServlet?arquivo=<%= java.net.URLEncoder.encode(nome, "UTF-8") %>"><i class="ti ti-download"></i> Baixar</a></td>
      </tr>
    <%
          }
        }
    %>
    </table>
    <%
      }
    %>
  </div>

  <jsp:include page="footer.jsp" />
</div>
</body>
</html>

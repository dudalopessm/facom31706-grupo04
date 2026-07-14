<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="javaBeans.Cliente, java.util.*, javaBeans.CategoriaVinho, dao.CategoriaVinhoDAO" %>
<%
    Cliente rodapeCliente = (Cliente) session.getAttribute("clienteLogado");
    boolean rodapeLogado = rodapeCliente != null && rodapeCliente.getEmail() != null && !rodapeCliente.getEmail().isEmpty();
    boolean rodapeAdmin = rodapeLogado && "ADMIN".equals(rodapeCliente.getTipo());
    String ctx = request.getContextPath();

    List<CategoriaVinho> footerCategorias = new ArrayList<>();
    try {
        CategoriaVinhoDAO footerCatDAO = new CategoriaVinhoDAO();
        footerCategorias = footerCatDAO.listarTodos();
    } catch (Exception e) { e.printStackTrace(); }
%>
<div class="site-footer">
  <div class="container">
    <div class="footer-top">
      <div class="footer-brand">
        <svg class="brand-mark" viewBox="0 0 40 40" fill="none" stroke="currentColor" stroke-width="1.4" aria-hidden="true" style="color: var(--amber-soft); width: 28px; height: 28px;">
          <path d="M8 32V19C8 11.8 13.4 6 20 6s12 5.8 12 13v13" />
          <path d="M8 32h24" stroke-linecap="round"/>
          <circle cx="20" cy="16.5" r="3.2"/>
        </svg>
        <p>Uma adega boutique dedicada a vinhos de pequenos produtores, com curadoria e guarda cuidadosas.</p>
      </div>

      <div class="footer-col">
        <h4>Loja</h4>
        <ul>
          <li><a href="<%= ctx %>/loja.jsp">Todos os vinhos</a></li>
          <% for (CategoriaVinho cat : footerCategorias) { %>
            <li><a href="<%= ctx %>/loja.jsp?categoria=<%= java.net.URLEncoder.encode(cat.getNome(), "UTF-8") %>"><%= cat.getNome() + "s" %></a></li>
          <% } %>
        </ul>
      </div>

      <div class="footer-col">
        <h4>Cave Fontana</h4>
        <ul>
          <% if (rodapeAdmin) { %>
            <li><a href="<%= ctx %>/perfil.jsp">Minha Conta</a></li>
            <li><a href="<%= ctx %>/admin/vinhos.jsp">Admin</a></li>
            <li><a href="<%= ctx %>/admin/pedidos.jsp">Pedidos</a></li>
          <% } else if (rodapeLogado) { %>
            <li><a href="<%= ctx %>/perfil.jsp">Minha Conta</a></li>
            <li><a href="<%= ctx %>/carrinho.jsp">Sacola</a></li>
            <li><a href="<%= ctx %>/historico.jsp">Pedidos</a></li>
          <% } else { %>
            <li><a href="<%= ctx %>/login.jsp">Minha conta</a></li>
            <li><a href="<%= ctx %>/carrinho.jsp">Sacola</a></li>
            <li><a href="<%= ctx %>/historico.jsp">Pedidos</a></li>
          <% } %>
        </ul>
      </div>
    </div>
  </div>
</div>

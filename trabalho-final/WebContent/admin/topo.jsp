<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="javaBeans.Cliente" %>
<%
    Cliente adminLogado = (Cliente) session.getAttribute("cliente");
    String contexto = request.getContextPath();
%>
<header class="site-header" id="siteHeader" style="border-bottom-color:var(--line)">
  <div class="container">
    <a href="<%= contexto %>/admin/vinhos.jsp" class="brand" aria-label="Admin Cave Fontana">
      <svg class="brand-mark admin-topo" viewBox="0 0 40 40" fill="none" stroke="currentColor" stroke-width="1.4" aria-hidden="true" style="color:var(--amber)">
        <path d="M8 32V19C8 11.8 13.4 6 20 6s12 5.8 12 13v13" />
        <path d="M8 32h24" stroke-linecap="round"/>
        <circle cx="20" cy="16.5" r="3.2"/>
      </svg>
      <span class="brand-word">
        <span class="cave" style="font-size:16px">Admin</span>
        <span class="fontana">Cave Fontana</span>
      </span>
    </a>

    <nav class="nav-links" aria-label="Navegação administrativa" style="display:flex;align-items:center;gap:24px">
      <a href="vinhos.jsp">Vinhos</a>
      <a href="categorias.jsp">Categorias</a>
      <a href="<%= contexto %>/loja.jsp" style="color:var(--text-muted);font-size:13px">Ver Loja</a>
      <a href="<%= contexto %>/logout" style="color:var(--text-muted);font-size:13px">Sair</a>
    </nav>
  </div>
</header>

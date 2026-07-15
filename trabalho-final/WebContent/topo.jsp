<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="javaBeans.Cliente, dao.SacolaDAO, dao.ItemSacolaDAO, javaBeans.Sacola, java.util.List, javaBeans.ItemSacola" %>
<jsp:useBean id="clienteLogado" class="javaBeans.Cliente" scope="session" />
<%
    String contexto = request.getContextPath();
    boolean temCliente = clienteLogado.getEmail() != null && !clienteLogado.getEmail().isEmpty();

    int cartCount = 0;
    if (temCliente) {
        try {
            SacolaDAO sacolaDAO = new SacolaDAO();
            Sacola sacola = sacolaDAO.buscarAtivaPorCliente(clienteLogado.getEmail());
            if (sacola != null) {
                ItemSacolaDAO itemDAO = new ItemSacolaDAO();
                List<ItemSacola> itens = itemDAO.listarPorSacola(sacola.getId());
                for (ItemSacola item : itens) {
                    cartCount += item.getQuantidade();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
%>
<svg width="0" height="0" style="position:absolute" aria-hidden="true">
  <defs>
    <symbol id="i-cart" viewBox="0 0 24 24"><path d="M3 4h2l2.4 12.2a2 2 0 0 0 2 1.6h7.6a2 2 0 0 0 2-1.6L21 8H6"/><circle cx="9.5" cy="20.5" r="1.4"/><circle cx="17.5" cy="20.5" r="1.4"/></symbol>
    <symbol id="i-menu" viewBox="0 0 24 24"><path d="M4 7h16M4 12h16M4 17h16"/></symbol>
    <symbol id="i-close" viewBox="0 0 24 24"><path d="M5 5l14 14M19 5L5 19"/></symbol>
    <symbol id="i-arrow" viewBox="0 0 24 24"><path d="M5 12h14M13 6l6 6-6 6"/></symbol>
    <symbol id="i-chevron" viewBox="0 0 24 24"><path d="M6 9l6 6 6-6"/></symbol>
    <symbol id="i-plus" viewBox="0 0 24 24"><path d="M12 5v14M5 12h14"/></symbol>
    <symbol id="i-check" viewBox="0 0 24 24"><path d="M4 12l5 5L20 6"/></symbol>
    <symbol id="i-user" viewBox="0 0 24 24"><circle cx="12" cy="8" r="3.6"/><path d="M4.5 20c1.4-4 4.2-6 7.5-6s6.1 2 7.5 6"/></symbol>
    <symbol id="i-package" viewBox="0 0 24 24"><path d="M3.5 8.5 12 4l8.5 4.5v7L12 20l-8.5-4.5v-7Z"/><path d="M3.5 8.5 12 13l8.5-4.5M12 13v7"/></symbol>
  </defs>
</svg>

<header class="site-header" id="siteHeader">
  <div class="container">
    <a href="<%= contexto %>/loja.jsp" class="brand" aria-label="Cave Fontana, início">
      <svg class="brand-mark" viewBox="0 0 40 40" fill="none" stroke="currentColor" stroke-width="1.4" aria-hidden="true">
        <path d="M8 32V19C8 11.8 13.4 6 20 6s12 5.8 12 13v13" />
        <path d="M8 32h24" stroke-linecap="round"/>
        <path d="M20 6v3" stroke-linecap="round"/>
        <circle cx="20" cy="16.5" r="3.2"/>
      </svg>
      <span class="brand-word">
        <span class="cave">Cave</span>
        <span class="fontana">Fontana</span>
      </span>
    </a>

    <nav class="nav-links" id="navLinks" aria-label="Navegação principal">
      <a href="<%= contexto %>/loja.jsp">Adega</a>
      <% if (temCliente) { %>
        <% if ("ADMIN".equals(clienteLogado.getTipo())) { %>
          <a href="<%= contexto %>/admin/pedidos.jsp">Pedidos <svg class="icon" style="width:14px;height:14px;display:inline-block;vertical-align:-2px;margin-left:2px"><use href="#i-package"></use></svg></a>
          <a href="<%= contexto %>/admin/vinhos.jsp">Admin</a>
        <% } else { %>
          <a href="<%= contexto %>/historico.jsp">Pedidos <svg class="icon" style="width:14px;height:14px;display:inline-block;vertical-align:-2px;margin-left:2px"><use href="#i-package"></use></svg></a>
        <% } %>
      <% } else { %>
        <a href="login.jsp">Entrar</a>
      <% } %>
    </nav>

    <div class="header-actions">
      <div class="profile-wrap">
        <button class="icon-btn" aria-label="Entrar ou criar conta" id="profileBtn" aria-expanded="false" aria-controls="accountPanel">
          <svg class="icon"><use href="#i-user"></use></svg>
        </button>

        <div class="account-panel" id="accountPanel" role="dialog" aria-label="Conta">
          <% if (temCliente) { %>
            <div style="text-align:center;padding:10px 0;color:var(--amber-soft);font-weight:600">
              Ol&aacute;, <jsp:getProperty name="clienteLogado" property="nome" />
            </div>
            <a href="<%= contexto %>/perfil.jsp" class="btn btn-primary account-submit" style="display:block;text-align:center;text-decoration:none;margin-bottom:8px">Editar Perfil</a>
            <a href="<%= contexto %>/logout" class="btn btn-primary account-submit" style="display:block;text-align:center;text-decoration:none;background:var(--wine)">Sair</a>
          <% } else { %>
          <div class="account-tabs" role="tablist">
            <button class="account-tab" data-tab="entrar" role="tab" aria-selected="true">Entrar</button>
            <button class="account-tab" data-tab="cadastro" role="tab" aria-selected="false">Criar conta</button>
          </div>

          <form class="account-panel-form is-active" data-form="entrar" action="<%= contexto %>/login" method="post">
            <div class="account-field">
              <label for="loginEmail">E-mail</label>
              <input type="email" name="email" id="loginEmail" placeholder="seu@email.com" autocomplete="email" required>
            </div>
            <div class="account-field">
              <label for="loginSenha">Senha</label>
              <input type="password" name="senha" id="loginSenha" placeholder="••••••••" autocomplete="current-password" required>
            </div>
            <button type="submit" class="btn btn-primary account-submit">Entrar</button>
            <p class="account-switch">Ainda n&atilde;o tem conta? <a href="#" data-switch="cadastro">Criar conta</a></p>
          </form>

          <form class="account-panel-form" data-form="cadastro" action="<%= contexto %>/cadastro" method="post">
            <div class="account-field">
              <label for="cadNome">Nome completo</label>
              <input type="text" name="nome" id="cadNome" placeholder="Seu nome" autocomplete="name" required>
            </div>
            <div class="account-field">
              <label for="cadEmail">E-mail</label>
              <input type="email" name="email" id="cadEmail" placeholder="seu@email.com" autocomplete="email" required>
            </div>
            <div class="account-field">
              <label for="cadCpf">CPF</label>
              <input type="text" name="cpf" id="cadCpf" placeholder="000.000.000-00" inputmode="numeric" required>
            </div>
            <div class="account-field">
              <label for="cadSenha">Senha</label>
              <input type="password" name="senha" id="cadSenha" placeholder="••••••••" autocomplete="new-password" required>
            </div>
            <div class="account-field">
              <label for="cadConfirmar">Confirmar senha</label>
              <input type="password" name="confirmarSenha" id="cadConfirmar" placeholder="••••••••" required>
            </div>
            <button type="submit" class="btn btn-primary account-submit">Criar conta</button>
            <p class="account-switch">J&aacute; tem conta? <a href="#" data-switch="entrar">Entrar</a></p>
          </form>
          <% } %>
        </div>
      </div>

      <% if (temCliente && "ADMIN".equals(clienteLogado.getTipo())) { %>
        <a href="<%= contexto %>/admin/pedidos.jsp" class="icon-btn" aria-label="Pedidos">
          <svg class="icon"><use href="#i-package"></use></svg>
        </a>
      <% } else { %>
        <a href="<%= contexto %>/carrinho.jsp" class="icon-btn" aria-label="Ver sacola" id="cartBtn">
          <svg class="icon"><use href="#i-cart"></use></svg>
          <span class="cart-badge <%= cartCount > 0 ? "is-visible" : "" %>" id="cartBadge"><%= cartCount %></span>
        </a>
      <% } %>

      <button class="icon-btn menu-toggle" id="menuToggle" aria-label="Abrir menu" aria-expanded="false" aria-controls="navLinks">
        <svg class="icon" id="menuIcon"><use href="#i-menu"></use></svg>
      </button>
    </div>
  </div>
</header>

<div class="toast" id="toast" role="status" aria-live="polite">
  <svg class="icon"><use href="#i-check"></use></svg>
  <span id="toastText">Adicionado à sacola</span>
</div>

<% if (temCliente) { %>
<div style="display:none" id="clienteLogado" data-email="<%= clienteLogado.getEmail() %>" data-nome="<%= clienteLogado.getNome() %>" data-tipo="<%= clienteLogado.getTipo() %>"></div>
<% } %>

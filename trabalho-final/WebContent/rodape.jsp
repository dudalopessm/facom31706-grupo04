<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
          <li><a href="<%= request.getContextPath() %>/loja.jsp">Todos os vinhos</a></li>
          <li><a href="<%= request.getContextPath() %>/loja.jsp">Tintos</a></li>
          <li><a href="<%= request.getContextPath() %>/loja.jsp">Brancos</a></li>
          <li><a href="<%= request.getContextPath() %>/loja.jsp">Espumantes</a></li>
        </ul>
      </div>

      <div class="footer-col">
        <h4>Cave Fontana</h4>
        <ul>
          <li><a href="<%= request.getContextPath() %>/login.jsp">Minha conta</a></li>
          <li><a href="<%= request.getContextPath() %>/carrinho.jsp">Sacola</a></li>
          <li><a href="<%= request.getContextPath() %>/historico.jsp">Pedidos</a></li>
        </ul>
      </div>

      <div class="footer-col">
        <h4>Receba novidades</h4>
        <form class="newsletter-form" onsubmit="return false">
          <input type="email" placeholder="seu@email.com" aria-label="E-mail para newsletter">
          <button type="submit" aria-label="Inscrever-se">
            <svg class="icon"><use href="#i-arrow"></use></svg>
          </button>
        </form>
      </div>
    </div>

    <div class="footer-bottom">
      <span>&copy; 2026 Cave Fontana &mdash; Projeto academico de Programacao para Internet (UFU)</span>
      <span>Beba com modera&ccedil;&atilde;o.</span>
    </div>
  </div>
</div>

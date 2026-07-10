<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, javaBeans.*, dao.*" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cave Fontana &mdash; Vinhos selecionados</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<a class="skip-link" href="#loja">Pular para a adega</a>

<%@ include file="topo.jsp" %>

<main id="top">

  <section class="hero">
    <div class="container">
      <div class="hero-copy">
        <p class="eyebrow hero-eyebrow">Adega boutique de vinhos selecionados</p>
        <h1>
          <span>Cave</span>
          <span class="accent">Fontana</span>
        </h1>
        <p class="lede">Rótulos garimpados de pequenos produtores, guardados à temperatura certa e servidos com a calma de quem entende de espera. Sua adega, sem sair de casa.</p>
        <div class="hero-actions">
          <a href="#loja" class="btn btn-primary">Explorar vinhos</a>
          <a href="#contato" class="btn btn-ghost">Fale conosco <svg class="icon"><use href="#i-arrow"></use></svg></a>
        </div>
      </div>

      <div class="hero-art" aria-hidden="true">
        <svg viewBox="0 0 460 560" fill="none">
          <defs>
            <radialGradient id="lantern" cx="50%" cy="50%" r="50%">
              <stop offset="0%" stop-color="#E0AD6B" stop-opacity="0.9"/>
              <stop offset="100%" stop-color="#E0AD6B" stop-opacity="0"/>
            </radialGradient>
            <linearGradient id="archFade" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="#EDE6D6" stop-opacity="0.5"/>
              <stop offset="100%" stop-color="#EDE6D6" stop-opacity="0.08"/>
            </linearGradient>
          </defs>
          <circle cx="230" cy="130" r="150" fill="url(#lantern)"/>
          <g stroke="url(#archFade)" stroke-width="1.4">
            <path d="M40 520V300c0-55 40-95 95-95s95 40 95 95v220"/>
            <path d="M230 520V260c0-66 48-114 115-114s115 48 115 114v260"/>
          </g>
          <g transform="translate(226,60)" stroke="#E0AD6B" stroke-width="1.5" fill="none">
            <line x1="4" y1="0" x2="4" y2="24"/>
            <rect x="-10" y="24" width="28" height="34" rx="6"/>
            <line x1="-10" y1="40" x2="18" y2="40"/>
          </g>
          <g fill="#C98A3B" opacity="0.85">
            <rect x="78" y="360" width="16" height="70" rx="6"/>
            <rect x="83" y="345" width="6" height="18" rx="2"/>
          </g>
          <g fill="#7A2C3B" opacity="0.9">
            <rect x="278" y="330" width="18" height="100" rx="7"/>
            <rect x="284" y="312" width="6" height="20" rx="2"/>
          </g>
          <g fill="#E0AD6B" opacity="0.8">
            <rect x="330" y="350" width="16" height="80" rx="6"/>
            <rect x="335" y="333" width="6" height="19" rx="2"/>
          </g>
        </svg>
      </div>
    </div>

    <div class="scroll-cue">
      <span>Role para descobrir</span>
      <svg class="icon"><use href="#i-chevron"></use></svg>
    </div>
  </section>

  <section class="value-strip reveal">
    <div class="container">
      <div class="value-item">
        <svg class="icon"><use href="#i-leaf"></use></svg>
        <div>
          <h3>Pequenos produtores</h3>
          <p>Parcerias diretas com vinícolas familiares, sem intermediários no caminho até você.</p>
        </div>
      </div>
      <div class="value-item">
        <svg class="icon"><use href="#i-flask"></use></svg>
        <div>
          <h3>Rótulos selecionados a mão</h3>
          <p>Cada vinho passa por degustação da nossa curadoria antes de entrar na adega.</p>
        </div>
      </div>
      <div class="value-item">
        <svg class="icon"><use href="#i-drop"></use></svg>
        <div>
          <h3>Guarda em temperatura controlada</h3>
          <p>Do estoque até a entrega, seus vinhos viajam na temperatura ideal de conservação.</p>
        </div>
      </div>
    </div>
  </section>

  <section class="shop-section" id="loja">
    <div class="container">
      <div class="shop-head reveal">
        <div>
          <p class="eyebrow">Coleção atual</p>
          <h2>A Adega</h2>
        </div>
        <%
            CategoriaVinhoDAO catDAO = new CategoriaVinhoDAO();
            List<CategoriaVinho> categorias = catDAO.listarTodos();
        %>
        <div class="filters" id="filters" role="group" aria-label="Filtrar por categoria">
          <button class="filter-chip" data-filter="todos" aria-pressed="true">Todos</button>
          <% for (CategoriaVinho cat : categorias) { %>
            <button class="filter-chip" data-filter="cat<%= cat.getId() %>" aria-pressed="false"><%= cat.getNome() %></button>
          <% } %>
        </div>
      </div>

      <%
          VinhoDAO vinhoDAO = new VinhoDAO();
          List<Vinho> vinhos = vinhoDAO.listarTodos();
      %>

      <div class="wine-grid" id="wineGrid">
        <%
            String bottleSVG = "<svg viewBox=\"0 0 60 150\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\"><path d=\"M24 10h12v18l6 10v96a6 6 0 0 1-6 6H24a6 6 0 0 1-6-6V38l6-10V10Z\"/><path d=\"M18 55h24\" opacity=\"0.5\"/><rect x=\"18\" y=\"70\" width=\"24\" height=\"34\" fill=\"currentColor\" opacity=\"0.14\" stroke=\"none\"/></svg>";
            String[] catColors = {"#C98A3B", "#E0AD6B", "#7A2C3B", "#D98E96", "#E8D9A0"};

            for (Vinho vinho : vinhos) {
                CategoriaVinho cat = catDAO.buscarPorId(vinho.getIdCategoria());
                String catName = cat != null ? cat.getNome() : "";
                String catSlug = "cat" + (cat != null ? cat.getId() : "0");
                int colorIdx = (vinho.getIdCategoria() - 1) % catColors.length;
                String accent = catColors[colorIdx];
        %>
        <article class="wine-card reveal" data-category="<%= catSlug %>" style="--accent:<%= accent %>">
          <div class="wine-card__bottle"><%= bottleSVG %></div>
          <span class="wine-card__tag"><%= catName %></span>
          <h3><%= vinho.getNome() %></h3>
          <p class="wine-card__meta"><%= vinho.getSafra() %></p>
          <div class="wine-card__row">
            <span class="wine-card__price">R$ <%= String.format("%.2f", vinho.getPreco()) %> <small>/ 750ml</small></span>
            <% if (temCliente && vinho.getEstoque() > 0) { %>
              <form action="carrinho" method="post" style="display:inline">
                <input type="hidden" name="acao" value="adicionar">
                <input type="hidden" name="idVinho" value="<%= vinho.getId() %>">
                <input type="hidden" name="quantidade" value="1">
                <button class="add-btn" data-name="<%= vinho.getNome() %>">
                  <svg class="icon"><use href="#i-plus"></use></svg><span>Adicionar</span>
                </button>
              </form>
            <% } else if (!temCliente) { %>
              <a href="login.jsp" class="add-btn">
                <svg class="icon"><use href="#i-plus"></use></svg><span>Adicionar</span>
              </a>
            <% } %>
          </div>
        </article>
        <% } %>
      </div>

      <% if (vinhos.isEmpty()) { %>
        <p class="vazio">Nenhum vinho dispon&iacute;vel no momento.</p>
      <% } %>
    </div>
  </section>

</main>

<%@ include file="rodape.jsp" %>

<script src="js/script.js"></script>
</body>
</html>

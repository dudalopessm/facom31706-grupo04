(function(){
  "use strict";

  /* ---------- HEADER ON SCROLL ---------- */
  var header = document.getElementById("siteHeader");
  function onScroll(){
    if(window.scrollY > 40){ header.classList.add("is-scrolled"); }
    else{ header.classList.remove("is-scrolled"); }
  }
  document.addEventListener("scroll", onScroll, { passive: true });
  onScroll();

  /* ---------- MOBILE MENU ---------- */
  var menuToggle = document.getElementById("menuToggle");
  var navLinks = document.getElementById("navLinks");
  var menuIcon = document.getElementById("menuIcon");

  if(menuToggle && navLinks && menuIcon){
    menuToggle.addEventListener("click", function(){
      var isOpen = navLinks.classList.toggle("is-open");
      menuToggle.setAttribute("aria-expanded", isOpen ? "true" : "false");
      menuIcon.innerHTML = isOpen ? '<use href="#i-close"></use>' : '<use href="#i-menu"></use>';
    });
    navLinks.querySelectorAll("a").forEach(function(link){
      link.addEventListener("click", function(){
        navLinks.classList.remove("is-open");
        menuToggle.setAttribute("aria-expanded", "false");
        menuIcon.innerHTML = '<use href="#i-menu"></use>';
      });
    });
  }

  /* ---------- ACCOUNT PANEL ---------- */
  var profileBtn = document.getElementById("profileBtn");
  var accountPanel = document.getElementById("accountPanel");
  var accountTabs = document.querySelectorAll(".account-tab");
  var accountForms = document.querySelectorAll(".account-panel-form");

  function setAccountTab(tab){
    if(!accountTabs.length) return;
    accountTabs.forEach(function(t){
      t.setAttribute("aria-selected", t.getAttribute("data-tab") === tab ? "true" : "false");
    });
    accountForms.forEach(function(f){
      f.classList.toggle("is-active", f.getAttribute("data-form") === tab);
    });
  }

  var openAccountPanel, closeAccountPanel;
  if(profileBtn && accountPanel){
    openAccountPanel = function(tab){
      accountPanel.classList.add("is-open");
      profileBtn.setAttribute("aria-expanded", "true");
      if(tab){ setAccountTab(tab); }
      var firstInput = accountPanel.querySelector(".account-panel-form.is-active input");
      if(firstInput){ setTimeout(function(){ firstInput.focus(); }, 200); }
    };
    closeAccountPanel = function(){
      accountPanel.classList.remove("is-open");
      profileBtn.setAttribute("aria-expanded", "false");
    };
    profileBtn.addEventListener("click", function(e){
      e.stopPropagation();
      if(accountPanel.classList.contains("is-open")){ closeAccountPanel(); }
      else{ openAccountPanel(); }
    });
    accountTabs.forEach(function(tab){
      tab.addEventListener("click", function(){ setAccountTab(tab.getAttribute("data-tab")); });
    });
    if(accountPanel.querySelectorAll){
      accountPanel.querySelectorAll("[data-switch]").forEach(function(link){
        link.addEventListener("click", function(e){
          e.preventDefault();
          setAccountTab(link.getAttribute("data-switch"));
        });
      });
    }
    accountPanel.addEventListener("click", function(e){ e.stopPropagation(); });
    document.addEventListener("click", function(){ closeAccountPanel(); });
    document.addEventListener("keydown", function(e){
      if(e.key === "Escape"){ closeAccountPanel(); }
    });
  }

  /* ---------- NAV LINKS ACCOUNT ---------- */
  document.getElementById("navPerfil") && document.getElementById("navPerfil").addEventListener("click", function(e){
    e.preventDefault();
    if(navLinks){ navLinks.classList.remove("is-open"); }
    if(menuToggle){ menuToggle.setAttribute("aria-expanded", "false"); }
    if(menuIcon){ menuIcon.innerHTML = '<use href="#i-menu"></use>'; }
    if(openAccountPanel){ openAccountPanel("entrar"); }
    else{ window.location.href = this.getAttribute("href"); }
  });
  document.getElementById("navPedidos") && document.getElementById("navPedidos").addEventListener("click", function(e){
    e.preventDefault();
    if(navLinks){ navLinks.classList.remove("is-open"); }
    if(menuToggle){ menuToggle.setAttribute("aria-expanded", "false"); }
    if(menuIcon){ menuIcon.innerHTML = '<use href="#i-menu"></use>'; }
    if(openAccountPanel){ openAccountPanel("entrar"); }
    else{ window.location.href = this.getAttribute("href"); }
  });

  /* ---------- CARRINHO ---------- */
  var cartCount = parseInt(document.getElementById("cartBadge") && document.getElementById("cartBadge").textContent) || 0;
  var cartBadge = document.getElementById("cartBadge");
  var toast = document.getElementById("toast");
  var toastText = document.getElementById("toastText");
  var toastTimer;

  document.querySelectorAll(".add-btn").forEach(function(btn){
    btn.addEventListener("click", function(e){
      var temCliente = !!document.getElementById('clienteLogado');
      if (!temCliente) {
        window.location.href = 'cadastro.jsp';
        return;
      }
      e.preventDefault();
      var form = btn.closest("form");
      if(form){
        var qtdInput = form.querySelector("input[name='quantidade']");
        var qtd = qtdInput ? parseInt(qtdInput.value) || 1 : 1;
        cartCount += qtd;
        if(cartBadge){
          cartBadge.textContent = cartCount;
          cartBadge.classList.add("is-visible", "bump");
          setTimeout(function(){ cartBadge.classList.remove("bump"); }, 380);
        }

        var original = btn.innerHTML;
        btn.classList.add("is-added");
        btn.innerHTML = '<svg class="icon"><use href="#i-check"></use></svg><span>Na sacola</span>';
        setTimeout(function(){
          btn.classList.remove("is-added");
          btn.innerHTML = original;
        }, 1400);

        var name = btn.getAttribute("data-name") || "Vinho";
        if(toastText && toast){
          toastText.textContent = name + " adicionado à sacola";
          toast.classList.add("is-visible");
          clearTimeout(toastTimer);
          toastTimer = setTimeout(function(){ toast.classList.remove("is-visible"); }, 2200);
        }

        setTimeout(function(){ form.submit(); }, 300);
      }
    });
  });

  /* ---------- REVEAL ON SCROLL ---------- */
  function observeReveals(){
    var items = document.querySelectorAll(".reveal:not(.is-visible)");
    if(!("IntersectionObserver" in window)){
      items.forEach(function(el){ el.classList.add("is-visible"); });
      return;
    }
    var io = new IntersectionObserver(function(entries){
      entries.forEach(function(entry){
        if(entry.isIntersecting){
          entry.target.classList.add("is-visible");
          io.unobserve(entry.target);
        }
      });
    }, { threshold: 0.15 });
    items.forEach(function(el){ io.observe(el); });
  }
  observeReveals();

  /* ---------- CONFIRM DELETE ---------- */
  document.querySelectorAll("[data-confirm]").forEach(function(el){
    el.addEventListener("click", function(e){
      if(!confirm(el.getAttribute("data-confirm"))){ e.preventDefault(); }
    });
  });

})();

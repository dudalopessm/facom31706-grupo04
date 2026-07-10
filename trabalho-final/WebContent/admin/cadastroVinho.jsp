<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, javaBeans.*, dao.*" %>
<%
    Cliente admin = (Cliente) session.getAttribute("cliente");
    if (admin == null || !"ADMIN".equals(admin.getTipo())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String mensagemErro = null;

    if ("POST".equalsIgnoreCase(request.getMethod())) {
        try {
            VinhoDAO vinhoDAO = new VinhoDAO();
            Vinho vinho = new Vinho();

            UploadVinhoBean upload = new UploadVinhoBean();
            upload.setDiretorio("images/vinhos");
            upload.setSize(2);
            upload.setExtensoesPermitidas("jpg,jpeg,png");

            if (upload.processarUpload(request)) {
                vinho.setNome(upload.getParametro("nome"));
                vinho.setSafra(Integer.parseInt(upload.getParametro("safra")));
                vinho.setDescricao(upload.getParametro("descricao"));
                vinho.setPreco(Double.parseDouble(upload.getParametro("preco")));
                vinho.setEstoque(Integer.parseInt(upload.getParametro("estoque")));
                vinho.setIdCategoria(Integer.parseInt(upload.getParametro("idCategoria")));

                int idVinho = vinhoDAO.inserir(vinho);

                if (upload.temArquivo()) {
                    if (upload.salvarArquivo(application, idVinho)) {
                        vinhoDAO.atualizarCaminhoFoto(idVinho, "images/vinhos/" + idVinho + ".jpg");
                    } else {
                        mensagemErro = "Vinho cadastrado, mas houve um problema ao salvar a foto: " + upload.getErro();
                    }
                }

                if (mensagemErro == null) {
                    response.sendRedirect("vinhos.jsp");
                    return;
                }
            } else if (upload.getErro() != null) {
                mensagemErro = upload.getErro();
            }
        } catch (Exception e) {
            mensagemErro = "Erro ao cadastrar vinho: " + e.getMessage();
            e.printStackTrace();
        }
    }

    CategoriaVinhoDAO catDAO = new CategoriaVinhoDAO();
    List<CategoriaVinho> categorias = catDAO.listarTodos();
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cadastrar Vinho &mdash; Cave Fontana</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,340;0,9..144,500;0,9..144,600;1,9..144,500;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="../css/estilo.css">
</head>
<body>
<%@ include file="topo.jsp" %>

<div class="admin-layout">
  <div class="container" style="max-width:600px">
    <h1>Cadastrar Novo Vinho</h1>
    <a href="vinhos.jsp" class="voltar">
      <svg class="icon" style="width:14px;height:14px"><use href="#i-chevron"></use></svg>
      Voltar
    </a>

    <% if (mensagemErro != null) { %>
      <p class="msg-erro"><%= mensagemErro %></p>
    <% } %>

    <jsp:useBean id="uploadVinho" class="javaBeans.UploadVinhoBean" scope="page" />
    <jsp:setProperty name="uploadVinho" property="diretorio" value="images/vinhos" />
    <jsp:setProperty name="uploadVinho" property="size" value="2" />
    <jsp:setProperty name="uploadVinho" property="extensoesPermitidas" value="jpg,jpeg,png" />

    <form action="cadastroVinho.jsp" method="post" enctype="multipart/form-data" class="formulario" style="max-width:100%">

      <div class="account-field">
        <label for="nome">Nome</label>
        <input type="text" name="nome" id="nome" required>
      </div>

      <div class="account-field">
        <label for="safra">Safra</label>
        <input type="number" name="safra" id="safra" min="1900" max="2100" required>
      </div>

      <div class="account-field">
        <label for="descricao">Descri&ccedil;&atilde;o</label>
        <textarea name="descricao" id="descricao" rows="4" style="background:var(--surface);border:1px solid var(--line);border-radius:10px;padding:11px 13px;color:var(--text);font-family:inherit;width:100%"></textarea>
      </div>

      <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px">
        <div class="account-field">
          <label for="preco">Pre&ccedil;o (R$)</label>
          <input type="number" name="preco" id="preco" step="0.01" min="0" required>
        </div>
        <div class="account-field">
          <label for="estoque">Estoque</label>
          <input type="number" name="estoque" id="estoque" min="0" required>
        </div>
      </div>

      <div class="account-field">
        <label for="idCategoria">Categoria</label>
        <select name="idCategoria" id="idCategoria" required>
          <option value="">Selecione...</option>
          <% for (CategoriaVinho cat : categorias) { %>
            <option value="<%= cat.getId() %>"><%= cat.getNome() %></option>
          <% } %>
        </select>
      </div>

      <div class="account-field">
        <label for="foto">Foto do Vinho</label>
        <input type="file" name="foto" id="foto" accept="image/jpeg,image/png"
               style="background:var(--surface);border:1px solid var(--line);border-radius:10px;padding:10px;color:var(--text);width:100%">
      </div>

      <button type="submit" class="botao" style="margin-top:10px;width:100%;justify-content:center">Cadastrar Vinho</button>
    </form>
  </div>
</div>

<script src="../js/script.js"></script>
</body>
</html>

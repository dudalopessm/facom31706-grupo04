# Laboratório 7-8 — Evolução do Lab05.1

## O que mudou e por quê

### 1. HTML estático → JSP com componentes reusáveis

**Antes (lab05.1):** Cada página era um `.html` puro, com o cabeçalho, menu e rodapé copiados manualmente em cada arquivo. Se quisesse alterar o menu, precisava editar **todas as páginas**.

```html
<!-- cliente.html — cabeçalho duplicado -->
<div class="header">
  <div class="logo"><i class="ti ti-user"></i></div>
  <div>
    <div class="page-title">Cave Fontana</div>
    <div class="page-sub">Clientes</div>
  </div>
</div>
```

**Agora (lab7-8):** Criamos `header.jsp` e `footer.jsp` — componentes separados que são **incluídos dinamicamente** via `<jsp:include>`. O menu agora vive em um único lugar.

```jsp
<!-- index.jsp — reaproveita os componentes -->
<body>
  <jsp:include page="header.jsp" />   <!-- puxa o menu -->
  ...conteúdo principal...
  <jsp:include page="footer.jsp" />   <!-- puxa o rodapé -->
</body>
```

**Por quê?** DRY (Don't Repeat Yourself). Com JSP podemos fatorar o visual comum e reutilizá-lo em N páginas. Uma alteração no menu reflete em todas as páginas instantaneamente.

---

### 2. Resposta direta do Servlet → Servlet como controlador com <jsp:forward>

**Antes (lab05.1):** O `ClienteServlet` escrevia HTML diretamente com `out.println()` — o Servlet era ao mesmo tempo **controlador** e **visão**. Misturava lógica de negócio com HTML.

```java
// ClienteServlet.java — escreve HTML na unha
out.println("<table><tr><th>ID</th><th>Nome</th></tr>");
out.println("<tr><td>" + c.getId() + "</td><td>" + c.getNome() + "</td></tr>");
```

**Agora (lab7-8):** O Servlet **apenas processa a requisição** e **encaminha o fluxo** (`RequestDispatcher.forward()`) para páginas JSP distintas — uma de sucesso, outra de erro. Separação clara entre controle e apresentação (MVC).

```java
// LoginServlet.java — lógica + encaminhamento
if (cliente != null) {
    request.setAttribute("usuario", cliente.getNome());
    destino = "/sucesso.jsp";             ← forward para sucesso
} else {
    destino = "/erro.jsp";                 ← forward para erro
}
RequestDispatcher dispatcher = request.getRequestDispatcher(destino);
dispatcher.forward(request, response);
```

**Por quê?** Facilita manutenção: o designer mexe no JSP sem encostar no Java, e o desenvolvedor mexe no Servlet sem emendar HTML. O `<jsp:forward>` (equivalente ao `RequestDispatcher.forward()`) impede que o usuário veja a URL de processamento.

---

### 3. Upload de arquivos (funcionalidade nova)

**Antes (lab05.1):** Não existia upload. Os formulários enviavam apenas dados textuais (`application/x-www-form-urlencoded`).

**Agora (lab7-8):** Dois arquivos podem ser enviados simultaneamente via formulário com `enctype="multipart/form-data"`. Usamos as bibliotecas `commons-fileupload` e `commons-io` para processar os arquivos no servidor.

```jsp
<!-- upload.jsp — dois campos de arquivo -->
<form action="UploadServlet" method="post" enctype="multipart/form-data">
  <input type="file" name="arquivo1" />
  <input type="file" name="arquivo2" />
  <button type="submit">Enviar</button>
</form>
```

```java
// UploadServlet.java — itera sobre os itens do formulário
List<FileItem> items = upload.parseRequest(request);
for (FileItem item : items) {
    if (!item.isFormField()) {
        item.write(new File(diretorio, nomeArquivo));  // salva o arquivo
    }
}
```

Os arquivos ficam na pasta `WebContent/arquivos/` e podem ser baixados via `DownloadServlet` que usa `Content-Disposition: attachment`.

**Por quê?** Aplicações reais precisam lidar com arquivos — fotos, documentos, relatórios. O `commons-fileupload` abstrai a complexidade do parsing multipart.

---

### 4. Dados fixos → Banco de dados MySQL

**Antes (lab05.1):** Já usava MySQL com as mesmas 5 tabelas (`cliente`, `categoria_vinho`, `vinho`, `pedido`, `item_pedido`).

**Agora (lab7-8):** Mantivemos o mesmo banco, mas agora os servlets (Login, Cadastro, Busca) se conectam a ele via `ConnectionFactory` + DAOs, ao invés de usar dados fixos no código.

**Por quê?** Dados fixos não persistem. Com o banco, um cliente cadastrado continua existindo depois que o servidor reinicia, e pode fazer login depois de se cadastrar.

---

## Fluxo de funcionamento do sistema

### Navegação geral

```
index.jsp
  │
  ├── login.jsp  ──→ LoginServlet ──→ sucesso.jsp  (CPF + senha OK)
  │                                     └── erro.jsp  (inválido)
  │
  ├── cadastro.jsp ──→ CadastroServlet ──→ cadastro-sucesso.jsp (inseriu no banco)
  │                                          └── cadastro-erro.jsp (validação)
  │
  ├── busca.jsp ──→ BuscaServlet ──→ busca-resultado.jsp (tabela com vinhos)
  │                                    └── busca-erro.jsp (nenhum resultado)
  │
  ├── upload.jsp ──→ UploadServlet ──→ página de resultado (links para download)
  │
  └── downloads.jsp ──→ DownloadServlet (baixa o arquivo)
```

### Fluxo detalhado do Login (exemplo)

```
1. Usuário acessa login.jsp
2. Preenche CPF + Senha e submete o formulário (POST /LoginServlet)
3. LoginServlet recebe os parâmetros (request.getParameter)
4. Abre conexão com o banco (ConnectionFactory)
5. Chama ClienteDao.buscaPorCpfESenha(cpf, senha)
6. Se encontrou: request.setAttribute("usuario", nome)
                → RequestDispatcher.forward("/sucesso.jsp")
   Se não:      request.setAttribute("erro", "CPF ou senha inválidos")
                → RequestDispatcher.forward("/erro.jsp")
7. A página JSP de destino (sucesso.jsp ou erro.jsp) renderiza a resposta
   usando os atributos colocados no request
8. O usuário nunca vê a URL do Servlet na barra de endereços (forward é interno)
```

### Fluxo detalhado do Upload

```
1. Usuário acessa upload.jsp
2. Seleciona dois arquivos e submete (POST /UploadServlet, multipart/form-data)
3. UploadServlet verifica se é multipart
4. Cria DiskFileItemFactory + ServletFileUpload
5. Chama upload.parseRequest(request) → obtém List<FileItem>
6. Para cada FileItem que não é campo de formulário:
   a. Extrai o nome original do arquivo
   b. Salva em WebContent/arquivos/ via item.write()
7. Exibe mensagem de sucesso com cada arquivo salvo
8. Usuário pode ir para downloads.jsp para ver a lista e baixar
```

### Fluxo detalhado do Download

```
1. Usuário acessa downloads.jsp
2. A JSP lista os arquivos da pasta /arquivos usando File.listFiles()
3. Cada arquivo tem um link para DownloadServlet?arquivo=nome
4. Ao clicar, DownloadServlet:
   a. Localiza o arquivo no diretório
   b. Configura Content-Disposition: attachment
   c. Copia o conteúdo do arquivo para o response.getOutputStream()
5. O navegador inicia o download do arquivo
```

---

## Estrutura final de diretórios

```
C:\duda\workspace\lab7-8\
├── WebContent/
│   ├── index.jsp              ← página inicial (usa <jsp:include>)
│   ├── header.jsp             ← componente reutilizável
│   ├── footer.jsp             ← componente reutilizável
│   ├── login.jsp              ← formulário de login
│   ├── sucesso.jsp            ← forward após login OK
│   ├── erro.jsp               ← forward após login inválido
│   ├── cadastro.jsp           ← formulário de cadastro
│   ├── cadastro-sucesso.jsp   ← forward após cadastro OK
│   ├── cadastro-erro.jsp      ← forward após erro no cadastro
│   ├── busca.jsp              ← formulário de busca
│   ├── busca-resultado.jsp    ← forward com resultados
│   ├── busca-erro.jsp         ← forward sem resultados
│   ├── upload.jsp             ← formulário com 2 inputs file
│   ├── downloads.jsp          ← lista arquivos para download
│   ├── css/style.css          ← estilos
│   ├── arquivos/              ← pasta onde os uploads são salvos
│   └── WEB-INF/
│       ├── web.xml
│       └── lib/
│           ├── commons-fileupload-1.5.jar
│           ├── commons-io-2.11.0.jar
│           └── mysql-connector-j-9.7.0.jar
│
└── src/main/java/lab07/
    ├── ConnectionFactory.java
    ├── LoginServlet.java       ← processa login, forward para sucesso/erro
    ├── CadastroServlet.java    ← processa cadastro, forward para sucesso/erro
    ├── BuscaServlet.java       ← processa busca, forward para resultado/erro
    ├── UploadServlet.java      ← processa upload de 2 arquivos
    ├── DownloadServlet.java    ← serve arquivos para download
    ├── modelo/
    │   ├── Cliente.java
    │   ├── CategoriaVinho.java
    │   ├── Vinho.java
    │   ├── Pedido.java
    │   └── ItemPedido.java
    └── dao/
        ├── ClienteDao.java
        ├── CategoriaVinhoDao.java
        ├── VinhoDao.java
        ├── PedidoDao.java
        └── ItemPedidoDao.java
```

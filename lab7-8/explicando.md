# Laboratório 7-8 — Cave Fontana

## 1. O que é JSP e componentes reusáveis (`<jsp:include>`)

**JSP (JavaServer Pages)** é uma tecnologia que permite mesclar HTML com código Java dentro de uma mesma página. Diferente de um arquivo `.html` estático, uma página JSP é processada no servidor (o Tomcat a compila em um Servlet) antes de enviar a resposta ao navegador. Isso permite lógica dinâmica e, principalmente, a **reutilização de componentes visuais**.

### Os dois componentes reusáveis

Criamos dois arquivos JSP independentes que contêm trechos de HTML repetidos em todas as páginas do sistema:

| Componente | Arquivo | O que contém |
|---|---|---|
| Cabeçalho + menu | `WebContent/header.jsp` | Logo "Cave Fontana", subtítulo e barra de navegação com links para Início, Categorias, Vinhos, Clientes, Pedidos e Itens |
| Rodapé | `WebContent/footer.jsp` | Linha separadora e texto de copyright |

### Onde a action `<jsp:include>` foi usada

Em **todas** as páginas JSP principais do sistema, os componentes são integrados via `<jsp:include>`:

```
index.jsp            ─┐
categoria-vinho.jsp   │
vinhos.jsp            ├── <jsp:include page="header.jsp" />
clientes.jsp          │    ... conteúdo da página ...
pedidos.jsp           │    <jsp:include page="footer.jsp" />
item-pedido.jsp       │
vinho-detalhe.jsp     │
vinho-erro.jsp        ┘
```

Exemplo em `vinhos.jsp:12` e `vinhos.jsp:79`:
```jsp
<body>
<div class="page">
  <jsp:include page="header.jsp" />    <!-- linha 12: insere cabeçalho + menu -->

  <!-- conteúdo principal da página: busca, tabela de vinhos, etc. -->

  <jsp:include page="footer.jsp" />    <!-- linha 79: insere rodapé -->
</div>
</body>
```

**Por que `<jsp:include>`?** É uma **inclusão dinâmica**: a cada requisição, o conteúdo do componente é processado e inserido. Se o `header.jsp` mudar (ex.: adicionar um link novo no menu), a alteração reflete automaticamente em todas as páginas que o incluem — sem editar arquivo por arquivo.

---

## 2. Servlet processa requisição e encaminha com `<jsp:forward>`

O enunciado pede que, dado o login, cadastro ou busca existente, um **Servlet** receba a requisição e **encaminhe o fluxo** para páginas JSP distintas conforme o resultado (sucesso ou erro).

No nosso contexto da Cave Fontana, implementamos isso na **busca de vinhos por nome + safra**.

### Como funciona

**Página de origem:** `vinhos.jsp` — contém um formulário de busca com dois campos:

```jsp
<!-- vinhos.jsp — formulário de busca -->
<form action="BuscaVinhoServlet" method="post">
  <input type="text"  name="nome"  required>   <!-- nome do vinho -->
  <input type="number" name="safra" required>  <!-- ano da safra -->
  <button type="submit">Buscar</button>
</form>
```

**Servlet controlador:** `BuscaVinhoServlet` (`src/main/java/lab07/BuscaVinhoServlet.java:30-36`) — recebe os parâmetros, consulta o banco e decide para onde redirecionar:

```java
Vinho vinho = dao.buscaPorNomeSafra(nome, safra);  // consulta no banco

if (vinho != null) {
    // SUCESSO: encontrou o vinho → forward para página de detalhes
    destino = "/vinho-detalhe.jsp?id=" + vinho.getId();
} else {
    // ERRO: não encontrou → forward para página de erro com mensagem
    request.setAttribute("buscaErro", "Nenhum vinho encontrado...");
    destino = "/vinho-erro.jsp";
}

RequestDispatcher dispatcher = request.getRequestDispatcher(destino);
dispatcher.forward(request, response);
```

O `RequestDispatcher.forward()` é o equivalente em Java puro da tag `<jsp:forward>`. Ambos realizam um **redirecionamento interno** no servidor: o navegador nunca vê a URL do Servlet, apenas a URL da página original.

### Páginas de destino

| Resultado | Forward para | O que exibe |
|---|---|---|
| Vinho encontrado | `vinho-detalhe.jsp?id=X` | Detalhes do vinho + formulário de upload de fotos + links de download |
| Não encontrado | `vinho-erro.jsp` | Mensagem "Nenhum vinho encontrado" + botão "Nova busca" |

### Diagrama do fluxo

```
vinhos.jsp ──(POST nome+safra)──→ BuscaVinhoServlet
                                       │
                         ┌─────────────┴─────────────┐
                         ▼                           ▼
                 vinho-detalhe.jsp             vinho-erro.jsp
               (detalhes + upload           (mensagem de erro
                + download fotos)            + link para voltar)
```

---

## 3. Upload de dois arquivos e download

### Upload

O sistema permite enviar **duas fotos** de um mesmo vinho. O formulário está em `vinho-detalhe.jsp`, acessível após buscar um vinho ou clicar em "Ver" na listagem.

**Formulário** (`vinho-detalhe.jsp:86-99`):

```jsp
<form action="UploadFotoServlet" method="post" enctype="multipart/form-data">
  <input type="hidden" name="idVinho" value="<%= vId %>">  <!-- ID do vinho -->
  <input type="file" name="foto1" required>                 <!-- primeira foto -->
  <input type="file" name="foto2">                          <!-- segunda foto (opcional) -->
  <button type="submit">Enviar Fotos</button>
</form>
```

**`enctype="multipart/form-data"`** é obrigatório — sem ele o navegador não envia o conteúdo binário do arquivo, apenas o nome.

**Servlet processador:** `UploadFotoServlet` (`src/main/java/lab07/UploadFotoServlet.java:59-76`) usa a biblioteca `commons-fileupload` (arquivo `.jar` em `WEB-INF/lib/`):

```java
DiskFileItemFactory factory = new DiskFileItemFactory();
ServletFileUpload upload = new ServletFileUpload(factory);
List<FileItem> items = upload.parseRequest(request);  // parseia o multipart

for (FileItem item : items) {
    if (!item.isFormField()) {                         // é um arquivo, não campo texto
        String nomeArquivo = new File(item.getName()).getName();
        File arquivoSalvo = new File(diretorio, nomeArquivo);
        item.write(arquivoSalvo);                     // salva no disco
    }
}
```

As fotos são salvas em `WebContent/arquivos/fotos/<id_vinho>/`. Cada vinho tem sua própria subpasta.

Após o upload, o servlet faz **forward de volta** para `vinho-detalhe.jsp` com uma mensagem de sucesso ou erro, mantendo o usuário na mesma página.

### Download

Na mesma página `vinho-detalhe.jsp`, abaixo do formulário de upload, são listadas as fotos já enviadas para aquele vinho:

```jsp
<a class="btn primary" href="DownloadServlet?dir=fotos/<%= vId %>&arquivo=<%= nome %>">
  <i class="ti ti-download"></i> Baixar
</a>
```

**Servlet de download:** `DownloadServlet` (`src/main/java/lab07/DownloadServlet.java:37-56`) localiza o arquivo no disco e o envia ao navegador com o cabeçalho HTTP `Content-Disposition: attachment`, que força o download:

```java
response.setContentType(mimeType);
response.setHeader("Content-Disposition",
        "attachment; filename=\"" + file.getName() + "\"");

try (FileInputStream fis = new FileInputStream(file);
     OutputStream os = response.getOutputStream()) {
    byte[] buffer = new byte[4096];
    int bytesRead;
    while ((bytesRead = fis.read(buffer)) != -1) {
        os.write(buffer, 0, bytesRead);
    }
}
```

O parâmetro `dir=fotos/<id>` permite ao servlet localizar a subpasta correta do vinho, cumprindo o requisito de "baixar em seus respectivos contextos".

---

## Bibliotecas externas

| Arquivo | Função |
|---|---|
| `commons-fileupload-1.5.jar` | Processamento de formulários `multipart/form-data` (upload) |
| `commons-io-2.11.0.jar` | Dependência do `commons-fileupload` |
| `mysql-connector-j-9.7.0.jar` | Conexão JDBC com MySQL |

Os `.jar` ficam em `WebContent/WEB-INF/lib/` e são automaticamente incluídos no classpath pelo Tomcat.

---

## Estrutura final

```
lab7-8/
├── WebContent/
│   ├── header.jsp               ← componente reutilizável (cabecalho + menu)
│   ├── footer.jsp               ← componente reutilizável (rodapé)
│   ├── index.jsp                ← página inicial
│   ├── categoria-vinho.jsp      ← CRUD de categorias (usa <jsp:include>)
│   ├── vinhos.jsp               ← lista + busca (usa <jsp:include>)
│   ├── vinho-detalhe.jsp        ← detalhe + upload + download (usa <jsp:include>)
│   ├── vinho-erro.jsp           ← forward de erro na busca (usa <jsp:include>)
│   ├── clientes.jsp             ← CRUD de clientes (usa <jsp:include>)
│   ├── pedidos.jsp              ← CRUD de pedidos (usa <jsp:include>)
│   ├── item-pedido.jsp          ← CRUD de itens (usa <jsp:include>)
│   ├── css/style.css
│   ├── arquivos/fotos/<id>/     ← fotos enviadas por vinho
│   └── WEB-INF/
│       ├── web.xml
│       └── lib/
│           ├── commons-fileupload-1.5.jar
│           ├── commons-io-2.11.0.jar
│           └── mysql-connector-j-9.7.0.jar
│
└── src/main/java/lab07/
    ├── BuscaVinhoServlet.java    ← busca vinho nome+safra, forward sucesso/erro
    ├── UploadFotoServlet.java    ← upload de 2 fotos, forward com mensagem
    ├── DownloadServlet.java      ← serve arquivos para download
    ├── ConnectionFactory.java    ← conexão JDBC com MySQL
    ├── modelo/  (Cliente, CategoriaVinho, Vinho, Pedido, ItemPedido)
    └── dao/     (reaproveitados do lab05)
```

Os servlets de CRUD (`CategoriaVinhoServlet`, `ClienteServlet`, `PedidoServlet`) e a estrutura de modelo/DAO foram reaproveitados do laboratório anterior e não são detalhados aqui.

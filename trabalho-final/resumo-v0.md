# Arquitetura do Sistema — Cave Fontana (E-commerce de Vinhos)

## 1. Visão Geral

Este documento descreve a arquitetura do projeto acadêmico **Cave Fontana**, um e-commerce de vinhos desenvolvido para a disciplina de Programação para Internet (UFU). O sistema segue o padrão **JSP + Servlets + JavaBeans puros**, sem frameworks modernos, rodando em Apache Tomcat 9 com banco MySQL.

A arquitetura segue o padrão **Model-View-Controller (MVC)** adaptado para JSP clássico:

```
┌─────────────────────────────────────────────────────────┐
│                   CLIENTE (Navegador)                     │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP Request
                         ▼
┌─────────────────────────────────────────────────────────┐
│              CONTROLLER (Servlets)                        │
│  Recebe requisições, coordena a lógica,                   │
│  usa request.setAttribute() + forward() para a View       │
└────────────┬────────────────────────────┬────────────────┘
             │                            │
             ▼                            ▼
┌────────────────────┐    ┌──────────────────────────────┐
│    MODEL (JavaBeans)│    │     VIEW (JSP)               │
│  • Dados (Cliente,  │    │  • Páginas .jsp              │
│    Vinho, Pedido)   │    │  • Expressions <%= %>        │
│  • Regras (Upload)  │    │  • JSP Actions <jsp:useBean> │
│  • Acesso a Dados   │    │  • Scriptlets <% %>          │
│    (DAOs)           │    │  • CSS + JS                  │
└────────────┬────────┘    └──────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────────┐
│              BANCO DE DADOS (MySQL)                      │
│  cave_fontana: Cliente, CategoriaVinho, Vinho,           │
│  Sacola, ItemSacola, Pedido, ItemPedido                  │
└─────────────────────────────────────────────────────────┘
```

---

## 2. Tecnologias e Justificativas

### Stack obrigatória (conforme especificação da disciplina)

| Camada | Tecnologia | Por quê? |
|--------|-----------|----------|
| **Frontend** | HTML5 + CSS3 + JavaScript puro | Sem frameworks JS (React/Vue). Demonstra domínio de HTML semântico, CSS moderno e JS vanilla |
| **Backend** | JSP + Servlets + JavaBeans | Tecnologias "raiz" Java EE. JSP gera HTML dinâmico, Servlets controlam o fluxo, JavaBeans encapsulam dados |
| **Servidor** | Apache Tomcat 9 | Container Servlet/JSP padrão da disciplina |
| **Banco** | MySQL + JDBC puro | Sem ORM (Hibernate/JPA). JDBC com `DriverManager`, `Connection`, `PreparedStatement` |
| **Upload** | Apache Commons FileUpload + Commons IO | Bibliotecas externas adicionadas manualmente em `WEB-INF/lib` |

### Por que não usar frameworks?
O objetivo é demonstrar compreensão dos fundamentos:
- **Sem Spring** — para não esconder o ciclo de vida de Servlets
- **Sem Hibernate** — para mostrar SQL e JDBC manualmente
- **Sem JSF** — para usar JSP com scriptlets e expressions puras
- **Sem Maven** — para gerenciar dependências manualmente (JARs em `WEB-INF/lib`)

---

## 3. Estrutura de Diretórios (explicada)

```
trabalho-final/
│
├── src/main/java/                  ← Código-fonte Java
│   ├── javaBeans/                  ← MODEL: classes JavaBeans
│   │   ├── Cliente.java
│   │   ├── CategoriaVinho.java
│   │   ├── Vinho.java
│   │   ├── Sacola.java
│   │   ├── ItemSacola.java
│   │   ├── Pedido.java
│   │   ├── ItemPedido.java
│   │   └── UploadVinhoBean.java
│   │
│   ├── dao/                        ← DATA ACCESS: acesso ao banco (JDBC)
│   │   ├── ConnectionFactory.java  ← Singleton de conexão MySQL
│   │   ├── ClienteDAO.java
│   │   ├── CategoriaVinhoDAO.java
│   │   ├── VinhoDAO.java
│   │   ├── SacolaDAO.java
│   │   ├── ItemSacolaDAO.java
│   │   ├── PedidoDAO.java
│   │   └── ItemPedidoDAO.java
│   │
│   └── servlets/                   ← CONTROLLER: recebem requisições
│       ├── LoginServlet.java
│       ├── LogoutServlet.java
│       ├── CadastroServlet.java
│       ├── CarrinhoServlet.java
│       ├── CheckoutServlet.java
│       └── AdminServlet.java
│
├── WebContent/                     ← VIEW: arquivos entregues ao navegador
│   ├── *.jsp                       ← Páginas JSP públicas (14 arquivos)
│   ├── admin/*.jsp                 ← Páginas JSP administrativas (5 arquivos)
│   ├── css/estilo.css              ← Design system completo
│   ├── js/script.js                ← Interações do lado do cliente
│   ├── images/vinhos/              ← Fotos dos vinhos (upload do admin)
│   │   └── sem-foto.jpg            ← Imagem padrão para vinhos sem foto
│   └── WEB-INF/
│       ├── web.xml                 ← Deployment descriptor
│       └── lib/                    ← JARs externos (mysql-connector, commons-*)
│
├── data/                           ← Scripts SQL
│   ├── create-operations.sql       ← CREATE DATABASE + todas as tabelas
│   └── populate-database.sql       ← INSERTs de exemplo
│
├── build/classes/                  ← Classes compiladas (gerado pelo Eclipse)
│
├── .classpath                      ← Configuração do Eclipse (classpath)
├── .project                        ← Configuração do projeto Eclipse
└── .settings/                      ← Configurações do Eclipse (facets, runtime)
```

### Por que essa organização?
A estrutura segue o padrão **Dynamic Web Project** do Eclipse, que separa:
- **Código fonte** (`src/main/java/`) do conteúdo web (`WebContent/`)
- **Lógica de negócio** (JavaBeans + DAOs) da **apresentação** (JSPs)
- **Controladores** (Servlets) como ponte entre os dois

---

## 4. Modelo MVC Detalhado

### 4.1. Model (Modelo)

O Model é composto por duas categorias de classes:

#### JavaBeans (encapsulam dados)
Seguem a convenção: atributos `private`, construtor padrão, getters/setters públicos.

```java
// Exemplo: Vinho.java
public class Vinho {
    private int id;
    private String nome;
    private int safra;
    private double preco;
    private String caminhoFoto;  // ← caminho relativo da imagem

    public Vinho() {}  // ← construtor padrão (obrigatório)

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    // ... demais getters/setters
}
```

**Uso com JSP Actions:**
```jsp
<jsp:useBean id="clienteLogado" class="javaBeans.Cliente" scope="session" />
<jsp:getProperty name="clienteLogado" property="nome" />
```

#### DAOs (acesso a dados)
Cada DAO encapsula as operações SQL de uma entidade. Usam JDBC puro:

```java
// Exemplo: VinhoDAO.inserir() com retorno do ID gerado
public int inserir(Vinho vinho) throws SQLException {
    String sql = "INSERT INTO Vinho (nome, safra, preco, ...) VALUES (?, ?, ?, ...)";
    Connection conn = ConnectionFactory.getConnection();
    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    // seta parâmetros...
    stmt.executeUpdate();
    ResultSet rs = stmt.getGeneratedKeys();
    if (rs.next()) return rs.getInt(1);  // ← retorna o ID gerado pelo AUTO_INCREMENT
}
```

**ConnectionFactory** gerencia a conexão com o banco:
```java
public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost:3306/cave_fontana";
    private static final String USUARIO = "root";
    private static final String SENHA = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
```

### 4.2. View (Visão) — JSPs

As páginas JSP usam três tipos de elementos do JSP:

| Elemento | Sintaxe | Para que serve |
|----------|---------|----------------|
| **Diretivas** | `<%@ page ... %>` | Configurar página, imports, errorPage |
| **Diretivas** | `<%@ include file="..." %>` | Reaproveitar header/footer/menu |
| **Scriptlets** | `<% ... %>` | Lógica Java embutida (loops, condicionais) |
| **Expressões** | `<%= ... %>` | Exibir valores de variáveis/beans |
| **Actions** | `<jsp:useBean>`, `<jsp:setProperty>`, `<jsp:getProperty>` | Trabalhar com JavaBeans sem código Java |

**Exemplo de página JSP completa:**
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    import="java.util.*, javaBeans.*, dao.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cave Fontana</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
    <%@ include file="topo.jsp" %>   <!-- reaproveitamento -->

    <%
        // scriptlet: lógica Java
        VinhoDAO dao = new VinhoDAO();
        List<Vinho> vinhos = dao.listarTodos();
        for (Vinho v : vinhos) {
    %>
    <div class="card-vinho">
        <h3><%= v.getNome() %></h3>           <!-- expression -->
        <p>R$ <%= String.format("%.2f", v.getPreco()) %></p>
    </div>
    <% } %>

    <%@ include file="rodape.jsp" %>
</body>
</html>
```

#### Reuso de componentes via `<%@ include %>`
O `topo.jsp` e `rodape.jsp` são incluídos em todas as páginas, garantindo:
- Header fixo com navegação e painel de login
- Footer com links e newsletter
- SVG sprite de ícones

#### Reuso de JavaBeans via `<jsp:useBean>`
O `Cliente` é um bean session-scoped acessível de qualquer página:
```jsp
<jsp:useBean id="clienteLogado" class="javaBeans.Cliente" scope="session" />
```

O `UploadVinhoBean` é configurado via `<jsp:setProperty>` nas páginas de admin:
```jsp
<jsp:useBean id="uploadVinho" class="javaBeans.UploadVinhoBean" scope="page" />
<jsp:setProperty name="uploadVinho" property="diretorio" value="images/vinhos" />
<jsp:setProperty name="uploadVinho" property="size" value="2" />
<jsp:setProperty name="uploadVinho" property="extensoesPermitidas" value="jpg,jpeg,png" />
```

### 4.3. Controller (Controlador) — Servlets

Os Servlets recebem requisições HTTP, processam a lógica e encaminham para a JSP correta usando **`RequestDispatcher.forward()`**.

#### Padrão de fluxo em cada Servlet:

```
1. Browser faz POST para /login, /carrinho, /checkout, etc.
                      │
2. Servlet recebe a requisição (doPost)
    ├─ Extrai parâmetros: request.getParameter("email")
    ├─ Chama DAOs: ClienteDAO.buscarPorEmailESenha()
    ├─ Toma decisão: if (cliente != null)
    │
3. Servlet define atributos: request.setAttribute("erro", "invalido")
                      │
4. Servlet encaminha: request.getRequestDispatcher("login.jsp").forward(request, response)
                      │
5. JSP renderiza: lê request.getAttribute("erro") e exibe mensagem
```

**Por que `forward` em vez de `sendRedirect`?**
- `forward` mantém os mesmos objetos `request` e `response`
- Permite passar dados via `request.setAttribute()`
- Evita expor parâmetros na URL (`?erro=invalido`)
- O JSP pode acessar os atributos sem perda de informação

### 4.4. Por que essa separação?

| Camada | Responsabilidade | Exemplo |
|--------|-----------------|---------|
| **JSP** | Exibir dados, HTML, CSS, JS | Mostrar lista de vinhos em cards |
| **Servlet** | Receber input, validar, decidir | Validar login, redirecionar |
| **JavaBean** | Carregar/guardar dados | Armazenar dados do cliente |
| **DAO** | Conversar com o banco | Buscar vinhos no MySQL |

Essa separação permite:
- **Manutenção**: alterar o SQL sem mexer no HTML
- **Reuso**: mesmo DAO usado por várias páginas
- **Testabilidade**: lógica separada da apresentação

---

## 5. Fluxos de Negócio (passo a passo)

### 5.1. Cadastro e Login

```
CADASTRO                          LOGIN
  │                                 │
  ├─ GET /cadastro                  ├─ GET /login
  │   → CadastroServlet.doGet()     │   → LoginServlet.doGet()
  │   → forward("cadastro.jsp")     │   → forward("login.jsp")
  │                                 │
  ├─ Formulário → POST /cadastro    ├─ Formulário → POST /login
  │   → CadastroServlet.doPost()    │   → LoginServlet.doPost()
  │   ├─ Valida campos              │   ├─ Valida credenciais
  │   ├─ Verifica email repetido    │   ├─ Seta cliente na session
  │   ├─ Insere no banco            │   ├─ forward("loja.jsp") [ok]
  │   ├─ forward("login.jsp") [ok]  │   └─ forward("login.jsp") [erro]
  │   └─ forward("cadastro.jsp")    │
```

### 5.2. Catálogo (loja.jsp)

```
GET /loja.jsp
  │
  ├─ topo.jsp (include)
  │   ├─ <jsp:useBean id="clienteLogado" scope="session" />
  │   └─ <jsp:getProperty property="nome" />
  │
  ├─ Loja.jsp (scriptlets)
  │   ├─ VinhoDAO.listarTodos() → List<Vinho>
  │   ├─ CategoriaVinhoDAO.listarTodos() → filtros
  │   └─ Para cada vinho:
  │       ├─ bottle SVG + nome + safra + preço
  │       ├─ Se logado + estoque > 0: botão "Adicionar" → POST /carrinho
  │       └─ Se não logado: link para /login
  │
  └─ rodape.jsp (include)
```

### 5.3. Carrinho (Sacola + ItemSacola)

A sacola segue o ciclo **CRUD**:

| Operação | Ação | Servlet |
|----------|------|---------|
| **Create** | Primeiro item → cria Sacola ATIVA + insere ItemSacola | `CarrinhoServlet` |
| **Read** | Lista itens da sacola ativa (join com Vinho) | `carrinho.jsp` |
| **Update** | Altera quantidade de ItemSacola | `CarrinhoServlet` |
| **Delete** | Remove ItemSacola | `CarrinhoServlet` |

```
Adicionar ao carrinho:
  POST /carrinho (acao=adicionar, idVinho=X, quantidade=1)
    ├─ Verifica se cliente tem Sacola ATIVA
    ├─ Se não: cria Sacola(email_cliente, status='ATIVA')
    ├─ Verifica se vinho já está na sacola
    │   ├─ Sim: atualiza quantidade (soma)
    │   └─ Não: insere novo ItemSacola
    ├─ forward("carrinho.jsp")
```

### 5.4. Checkout (Sacola → Pedido)

```
POST /checkout
  ├─ Busca Sacola ATIVA do cliente
  ├─ Lista ItemSacola (com join Vinho para preços)
  ├─ Calcula valorTotal
  ├─ Muda Sacola.status para 'CONVERTIDA'
  ├─ Cria Pedido(data, valorTotal, 'PAGO', 'PENDENTE', idSacola)
  ├─ Para cada ItemSacola:
  │   └─ Insere ItemPedido(idPedido, idVinho, quantidade, precoUnitario)
  │      ↑ preco congelado no momento da compra
  ├─ forward("confirmacao.jsp") com idPedido
```

### 5.5. Upload da Foto do Vinho

Este é o fluxo mais complexo, dividido em **duas fases**:

#### Fase 1: UploadVinhoBean.processarUpload(request)
- Parseia o `multipart/form-data`
- Extrai campos de formulário (nome, safra, preço...) → armazena em `Map<String, String>`
- Se houver arquivo de imagem: valida extensão (jpg/jpeg/png) e tamanho (máx 2MB)
- Armazena o `FileItem` sem gravá-lo em disco

#### Fase 2: admin/cadastroVinho.jsp (self-submit)
```
POST /admin/cadastroVinho.jsp (enctype=multipart)
  │
  ├─ UploadVinhoBean.processarUpload(request)
  │   ├─ Extrai: nome, safra, descricao, preco, estoque, idCategoria
  │   └─ Guarda FileItem da foto (se houver)
  │
  ├─ VinhoDAO.inserir(vinho) → retorna id gerado
  │
  ├─ Se tem arquivo:
  │   ├─ UploadVinhoBean.salvarArquivo(application, id)
  │   │   └─ Grava em disco: /images/vinhos/<id>.jpg
  │   └─ VinhoDAO.atualizarCaminhoFoto(id, "images/vinhos/<id>.jpg")
  │
  └─ redirect("vinhos.jsp")
```

**Por que duas fases?**
A foto é salva como `<id>.jpg`, onde `id` é gerado pelo banco via `AUTO_INCREMENT`. Portanto:
1. Primeiro insere o vinho no banco → obtém o `id`
2. Depois salva o arquivo em disco usando esse `id` como nome

**Exibição da foto nas páginas:**
```jsp
<%
    String foto = (vinho.getCaminhoFoto() != null) ? vinho.getCaminhoFoto() : "images/vinhos/sem-foto.jpg";
%>
<img src="<%= foto %>" alt="<%= vinho.getNome() %>"
     onerror="this.src='images/vinhos/sem-foto.jpg';">
```

---

## 6. Banco de Dados

### Modelo Entidade-Relacionamento (textual)

```
Cliente (email PK, nome, cpf UNIQUE, senha, tipo)
    │
    ├─< Sacola (id PK, email_cliente FK, data_criacao, status)
    │           status: 'ATIVA' | 'CONVERTIDA' | 'CANCELADA'
    │    │
    │    ├─< ItemSacola (id_sacola PK, id_vinho PK, quantidade)
    │    │
    │    └── Pedido (id PK, data_conclusao, valor_total, 
    │                status_pagamento, status_envio, id_sacola FK UNIQUE)
    │         status_pagamento: 'PENDENTE' | 'PAGO' | 'CANCELADO'
    │         status_envio: 'PENDENTE' | 'PREPARANDO' | 'ENVIADO' | 'ENTREGUE'
    │              │
    │              └─< ItemPedido (id_pedido PK, id_vinho PK, quantidade, preco_unitario)
    │
    └── CategoriaVinho (id PK, nome UNIQUE, descricao)
         │
         └─< Vinho (id PK, nome, safra, descricao, preco, estoque, 
                     id_categoria FK, caminho_foto NULL)
```

### Regras de negócio implementadas via CONSTRAINT:

```sql
CHECK (preco >= 0)                          -- Vinho
CHECK (estoque >= 0)                        -- Vinho
CHECK (quantidade > 0)                      -- ItemSacola, ItemPedido
CHECK (status IN ('ATIVA','CONVERTIDA','CANCELADA'))  -- Sacola
CHECK (valor_total >= 0)                    -- Pedido
CHECK (preco_unitario >= 0)                 -- ItemPedido
```

### Diferenciais do modelo:

1. **`caminho_foto VARCHAR(255) NULL`** — A imagem do vinho nunca vai para o banco (nada de BLOB). Apenas o caminho relativo do arquivo é armazenado.
2. **`preco_unitario` em ItemPedido** — O preço é "congelado" no momento da compra, protegendo contra alterações futuras no preço do vinho.
3. **`id_sacola UNIQUE` em Pedido** — Uma sacola só pode gerar um pedido (impede conversão duplicada).
4. **`ON DELETE RESTRICT`** nas FKs principais — Impede excluir cliente/vinho/categoria que possuam referências.

---

## 7. Design System (CSS)

O design system está centralizado em `css/estilo.css` usando variáveis CSS customizadas (`:root`):

```css
:root {
  --bg: #1C1B19;           /* Fundo escuro principal */
  --surface: #242019;      /* Superfície de cards/tabelas */
  --surface-2: #2C2620;    /* Superfície secundária */
  --text: #EDE6D6;         /* Texto principal (bege claro) */
  --text-muted: #A79A85;   /* Texto secundário */
  --amber: #C98A3B;        /* Cor de destaque principal */
  --amber-soft: #E0AD6B;   /* Variação mais clara */
  --wine: #5B1F2B;         /* Tom vinho (erros, ações destrutivas) */
  --wine-soft: #7A2C3B;
  --line: rgba(237,230,214,0.12);  /* Bordas sutis */
  --line-strong: rgba(237,230,214,0.22);  /* Bordas mais fortes */
  --radius-arch: 132px 132px 10px 10px;   /* Borda arqueada dos cards */
}
```

### Tipografia
- **Google Fonts**: `Fraunces` (cabeçalhos, itálico, serifada) + `Inter` (corpo, sans-serif)
- Cabeçalhos com `font-style: italic` (estilo vinícola)
- Eyebrow labels em Inter, uppercase, 12.5px

### Ícones
SVG `<symbol>` inline sprite (definido em `topo.jsp`):
- i-cart, i-user, i-search, i-menu, i-close, i-arrow, i-chevron
- i-plus, i-check, i-drop, i-leaf, i-flask, i-package

Uso: `<svg class="icon"><use href="#i-cart"></use></svg>`

---

## 8. Interações JavaScript (`js/script.js`)

Todas em JavaScript puro (sem bibliotecas):

| Interação | Técnica | Descrição |
|-----------|---------|-----------|
| **Header glassmorphism** | `scroll` event listener | Adiciona `is-scrolled` → `backdrop-filter: blur` |
| **Mobile menu** | `classList.toggle("is-open")` | Hamburger abre nav em telas pequenas |
| **Account panel** | `classList.toggle("is-open")` | Dropdown de login/cadastro, fecha ao clicar fora ou ESC |
| **Filtros** | `aria-pressed` toggle | Mostra/esconde cards via classe `is-hidden` |
| **Cart badge** | Animação CSS `bump` | Escala o badge ao adicionar item |
| **Toast** | `setTimeout` | Notificação temporária (2.2s) |
| **Reveal** | `IntersectionObserver` | Fade-in dos elementos ao scroll |
| **Confirm** | `data-confirm` attribute | `confirm()` antes de ações destrutivas |

---

## 9. Sessão e Escopo de Beans

| Bean | Escopo | Onde é criado | Onde é usado |
|------|--------|---------------|--------------|
| `Cliente` (`clienteLogado`) | `session` | `<jsp:useBean>` em `topo.jsp` (lê da session) | Todas as páginas que incluem `topo.jsp` |
| `UploadVinhoBean` (`uploadVinho`) | `page` | `<jsp:useBean>` em `admin/cadastroVinho.jsp`, `admin/editarVinho.jsp` | Apenas na própria página |

### Hierarquia de escopos JSP:
```
page     → válido apenas na página atual
request  → válido durante o forward/include
session  → válido para todas as páginas do mesmo usuário
application → válido para toda a aplicação (todos os usuários)
```

---

## 10. Tratamento de Erros

### No web.xml:
```xml
<error-page>
    <error-code>404</error-code>
    <location>/erro404.html</location>
</error-page>
<error-page>
    <error-code>500</error-code>
    <location>/erro.jsp</location>
</error-page>
<error-page>
    <exception-type>java.lang.Exception</exception-type>
    <location>/erro.jsp</location>
</error-page>
```

### Nas páginas JSP:
```jsp
<%@ page errorPage="erro.jsp" %>
```

A página `erro.jsp` usa:
```jsp
<%@ page isErrorPage="true" %>
```
... permitindo acessar o objeto implícito `exception` para exibir detalhes do erro.

---

## 11. Resumo das Classes

### JavaBeans (8)
| Classe | Atributos | Métodos notáveis |
|--------|-----------|------------------|
| `Cliente` | email, nome, cpf, senha, tipo | get/set |
| `CategoriaVinho` | id, nome, descricao | get/set |
| `Vinho` | id, nome, safra, descricao, preco, estoque, idCategoria, caminhoFoto | get/set |
| `Sacola` | id, emailCliente, dataCriacao, status | get/set |
| `ItemSacola` | idSacola, idVinho, quantidade, vinho | getSubtotal() |
| `Pedido` | id, dataConclusao, valorTotal, statusPagamento, statusEnvio, idSacola | get/set |
| `ItemPedido` | idPedido, idVinho, quantidade, precoUnitario, vinho | getSubtotal() |
| `UploadVinhoBean` | diretorio, size, extensoesPermitidas, erro | processarUpload(), salvarArquivo() |

### DAOs (8)
| Classe | Operações SQL |
|--------|--------------|
| `ConnectionFactory` | Conexão/fechamento |
| `ClienteDAO` | INSERT, SELECT por email, SELECT por email+senha |
| `CategoriaVinhoDAO` | INSERT, UPDATE, DELETE, SELECT por id, SELECT ALL |
| `VinhoDAO` | INSERT (com generated keys), UPDATE, DELETE, SELECT por id, ALL, por categoria, atualizarCaminhoFoto |
| `SacolaDAO` | INSERT, SELECT por id, SELECT ativa por cliente, UPDATE status |
| `ItemSacolaDAO` | INSERT, UPDATE quantidade, DELETE, SELECT por sacola (com join) |
| `PedidoDAO` | INSERT, SELECT por id, SELECT por sacola, SELECT por cliente |
| `ItemPedidoDAO` | INSERT, SELECT por pedido (com join) |

### Servlets (6)
| Servlet | Ações POST |
|---------|-----------|
| `LoginServlet` | login → forward sucesso/erro |
| `LogoutServlet` | logout → invalida sessão → forward |
| `CadastroServlet` | cadastro → valida → insere → forward |
| `CarrinhoServlet` | adicionar, atualizar, remover → forward |
| `CheckoutServlet` | checkout → converte sacola → pedido → forward |
| `AdminServlet` | CRUD categorias + excluir vinho → forward |

---

## 12. Telas (19 páginas)

### Públicas (14)
| Tela | Arquivo | Funcionalidade |
|------|---------|----------------|
| **Início** | `index.jsp` | Redireciona para loja |
| **Login** | `login.jsp` | Formulário de autenticação |
| **Cadastro** | `cadastro.jsp` | Formulário de registro |
| **Loja** | `loja.jsp` | Catálogo com hero, filtros, grid de vinhos |
| **Detalhe** | `vinho.jsp` | Detalhes do vinho + "Adicionar à sacola" |
| **Carrinho** | `carrinho.jsp` | Itens da sacola com quantidades editáveis |
| **Checkout** | `checkout.jsp` | Resumo + confirmação de pagamento |
| **Confirmação** | `confirmacao.jsp` | Sucesso do pedido |
| **Histórico** | `historico.jsp` | Pedidos do cliente |
| **Detalhe Pedido** | `detalhePedido.jsp` | Itens de um pedido específico |
| **Erro** | `erro.jsp` | Página de erro genérica |
| **404** | `erro404.html` | Página estática para HTTP 404 |

### Componentes reutilizados via `<%@ include %>`
| Componente | Arquivo | Uso |
|------------|---------|-----|
| **Header** | `topo.jsp` | Todas as páginas públicas |
| **Footer** | `rodape.jsp` | Todas as páginas públicas |

### Administrativas (5)
| Tela | Arquivo | Funcionalidade |
|------|---------|----------------|
| **Header Admin** | `admin/topo.jsp` | Navegação administrativa |
| **Categorias** | `admin/categorias.jsp` | CRUD de categorias |
| **Vinhos** | `admin/vinhos.jsp` | Listagem com miniaturas |
| **Cadastrar Vinho** | `admin/cadastroVinho.jsp` | Formulário + upload de foto (self-submit) |
| **Editar Vinho** | `admin/editarVinho.jsp` | Edição + troca de foto (self-submit) |

---

## 13. Considerações Finais

### Princípios seguidos
1. **Didático sobre otimizado** — código com scriptlets legíveis, comentários explicativos
2. **JSP "raiz"** — sem frameworks, usando os objetos implícitos do JSP
3. **Separação de concerns** — Servlet controla, DAO acessa dados, JSP apresenta
4. **Segurança** — PreparedStatement (protege contra SQL injection), validação de extensão e tamanho no upload

### Limitações (conscientes, para um projeto acadêmico)
- Sem criptografia de senha (em produção, usar bcrypt/hash)
- Checkout simulado (sem gateway de pagamento real)
- Estoque não é debitado no momento da compra (apenas no checkout)
- Vazamento de escopo: `<jsp:useBean>` cria Cliente vazio na sessão mesmo para não-logados

### Para executar o projeto
1. Importar no Eclipse como **Dynamic Web Project**
2. Adicionar JARs em `WEB-INF/lib`: mysql-connector-java, commons-fileupload, commons-io
3. Executar scripts SQL em `data/` no MySQL
4. Configurar `ConnectionFactory.java` com usuário/senha do seu MySQL
5. Deploy no Tomcat 9+ e acessar `http://localhost:8080/trabalho-final`
6. Admin padrão: `admin@cavefontana.com` / `admin123`

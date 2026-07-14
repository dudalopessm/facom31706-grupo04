# Arquitetura do Sistema — Cave Fontana (E-commerce de Vinhos) — v1

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

## 3. Estrutura de Diretórios

```
trabalho-final/
│
├── src/main/java/
│   ├── javaBeans/                  ← MODEL: 8 classes JavaBeans
│   │   ├── Cliente.java
│   │   ├── CategoriaVinho.java
│   │   ├── Vinho.java
│   │   ├── Sacola.java
│   │   ├── ItemSacola.java
│   │   ├── Pedido.java
│   │   ├── ItemPedido.java
│   │   └── UploadVinhoBean.java    ← upload em duas fases
│   │
│   ├── dao/                        ← DATA ACCESS: 8 classes JDBC
│   │   ├── ConnectionFactory.java
│   │   ├── ClienteDAO.java
│   │   ├── CategoriaVinhoDAO.java
│   │   ├── VinhoDAO.java           ← atualizarEstoque, atualizarCaminhoFoto
│   │   ├── SacolaDAO.java          ← overload com Connection
│   │   ├── ItemSacolaDAO.java
│   │   ├── PedidoDAO.java          ← overload com Connection,
│   │   │                              atualizarStatusEnvio/Pagamento
│   │   └── ItemPedidoDAO.java      ← overload com Connection
│   │
│   └── servlets/                   ← CONTROLLER: 8 servlets
│       ├── LoginServlet.java
│       ├── LogoutServlet.java
│       ├── CadastroServlet.java
│       ├── CarrinhoServlet.java
│       ├── CheckoutServlet.java    ← transação JDBC (commit/rollback)
│       ├── AdminServlet.java       ← categorias + excluir + status envio + cancelar
│       ├── PerfilServlet.java      ← edição de perfil (nome, CPF, senha)
│       └── VinhoServlet.java       ← cadastro/edição + upload de foto
│
├── WebContent/
│   ├── *.jsp                       ← 15 páginas públicas
│   ├── admin/*.jsp                 ← 6 páginas administrativas
│   ├── css/estilo.css              ← design system + modal
│   ├── js/script.js                ← header, toast, account panel, reveal, confirm
│   ├── images/vinhos/              ← fotos salvas pelo upload
│   │   └── sem-foto.jpg
│   └── WEB-INF/
│       ├── web.xml                 ← error-pages (404, 500, Exception)
│       └── lib/                    ← mysql-connector, commons-fileupload, commons-io
│
├── data/
│   ├── create-operations.sql
│   └── populate-database.sql
│
├── build/classes/
├── .classpath
├── .project
└── .settings/
```

---

## 4. Modelo MVC Detalhado

### 4.1. Model (Modelo)

#### JavaBeans (8 classes)
Seguem a convenção: atributos `private`, construtor padrão, getters/setters públicos.

| Bean | Atributos principais | Métodos notáveis |
|------|---------------------|------------------|
| `Cliente` | email, nome, cpf, senha, tipo | get/set |
| `CategoriaVinho` | id, nome, descricao | get/set |
| `Vinho` | id, nome, safra, descricao, preco, estoque, idCategoria, caminhoFoto | get/set |
| `Sacola` | id, emailCliente, dataCriacao, status | get/set |
| `ItemSacola` | idSacola, idVinho, quantidade, vinho | getSubtotal() |
| `Pedido` | id, dataConclusao, valorTotal, statusPagamento, statusEnvio, idSacola | get/set |
| `ItemPedido` | idPedido, idVinho, quantidade, precoUnitario, vinho | getSubtotal() |
| `UploadVinhoBean` | diretorio, size, extensoesPermitidas, erro, fileItem, parametros | processarUpload(request), salvarArquivo(context, idVinho), temArquivo() |

**Reuso via JSP Actions:**
- `Cliente` — `<jsp:useBean id="clienteLogado" scope="session">` em `topo.jsp`, exibido via `<jsp:getProperty property="nome">`
- `UploadVinhoBean` — declarado com `<jsp:useBean>` e `<jsp:setProperty>` nos JSPs admin (mesmo após refatoração para VinhoServlet, mantido como documentação didática)

#### DAOs (8 classes)

| DAO | Métodos principais |
|-----|-------------------|
| `ConnectionFactory` | `getConnection()` — singleton JDBC |
| `ClienteDAO` | inserir, buscarPorEmail, buscarPorEmailESenha, alterar |
| `CategoriaVinhoDAO` | inserir, alterar, excluir, buscarPorId, listarTodos |
| `VinhoDAO` | inserir (RETURN_GENERATED_KEYS), alterar, excluir, buscarPorId, listarTodos, listarPorCategoria, atualizarCaminhoFoto, atualizarEstoque(id, estoque) / atualizarEstoque(conn, id, estoque) |
| `SacolaDAO` | inserir, buscarPorId, buscarAtivaPorCliente, atualizarStatus(id, status) / atualizarStatus(conn, id, status), listarTodas |
| `ItemSacolaDAO` | inserir, atualizarQuantidade, remover, listarPorSacola, buscarItem |
| `PedidoDAO` | inserir(pedido) / inserir(conn, pedido), buscarPorId, listarPorCliente, listarTodos, atualizarStatusEnvio, atualizarStatusPagamento |
| `ItemPedidoDAO` | inserir(item) / inserir(conn, item), listarPorPedido |

**Overloads com `Connection`:** SacolaDAO, PedidoDAO, ItemPedidoDAO e VinhoDAO possuem versões dos métodos `inserir`, `atualizarStatus` e `atualizarEstoque` que recebem `Connection` como parâmetro. Isso permite que o **CheckoutServlet** use uma única conexão com `conn.setAutoCommit(false)` + `conn.commit()` / `conn.rollback()`, garantindo atomicidade nas 4 operações do checkout (atualizar sacola, inserir pedido, inserir itens, debitar estoque).

### 4.2. View (Visão) — JSPs

| Elemento | Sintaxe | Para que serve |
|----------|---------|----------------|
| **Diretivas** | `<%@ page ... %>` | Configurar página, imports, errorPage |
| **Diretivas** | `<%@ include file="..." %>` | Reaproveitar header/footer/menu |
| **Scriptlets** | `<% ... %>` | Lógica Java embutida (loops, condicionais) |
| **Expressões** | `<%= ... %>` | Exibir valores de variáveis/beans |
| **Actions** | `<jsp:useBean>`, `<jsp:setProperty>`, `<jsp:getProperty>` | Trabalhar com JavaBeans |

### 4.3. Controller (Controlador) — Servlets (8)

| Servlet | URL | Método | Fluxo |
|---------|-----|--------|-------|
| `LoginServlet` | `/login` | POST | Valida credenciais → session + redirect `loja.jsp` (ok) ou forward `login.jsp` (erro) |
| `LogoutServlet` | `/logout` | GET | Invalida sessão → redirect `login.jsp` |
| `CadastroServlet` | `/cadastro` | POST | Valida → insere cliente → forward `login.jsp` (ok) ou `cadastro.jsp` (erro) |
| `CarrinhoServlet` | `/carrinho` | POST | Adiciona/atualiza/remove itens. **Remover**: quantidade > 1 decrementa, = 1 exclui. Valida estoque total (carrinho + adição) |
| `CheckoutServlet` | `/checkout` | POST | **Transação JDBC**: setAutoCommit(false) → atualiza sacola → insere pedido → insere itens → debita estoque → commit. Rollback em qualquer falha |
| `AdminServlet` | `/admin` | POST | CRUD categorias, excluir vinho (com FK amigável), atualizar status_envio, cancelar pedido (restaura estoque) |
| `PerfilServlet` | `/perfil` | POST | Editar nome, CPF, senha. Valida senha atual antes de alterar. Atualiza bean na session |
| `VinhoServlet` | `/admin/vinho` | POST | `acao=inserir`: insere vinho + upload foto. `acao=alterar`: edita vinho + upload foto (se nova). Valida safra ≥ 1900 |

**Padrão de comunicação:** Servlets usam `request.setAttribute()` para erros + `request.getRequestDispatcher("pagina.jsp").forward(request, response)`. Operações bem-sucedidas usam `response.sendRedirect()`. Exclusões redirecionam para `sucesso.jsp`.

### 4.4. Por que essa separação?

| Camada | Responsabilidade | Exemplo |
|--------|-----------------|---------|
| **JSP** | Exibir dados, HTML, CSS, JS | Mostrar lista de vinhos em cards + modal de detalhes |
| **Servlet** | Receber input, validar, decidir | Cadastrar/editar vinho com upload de foto |
| **JavaBean** | Carregar/guardar dados, lógica de negócio | UploadVinhoBean: parsear multipart + salvar arquivo |
| **DAO** | Conversar com o banco | VinhoDAO.inserir() com RETURN_GENERATED_KEYS |

---

## 5. Fluxos de Negócio

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
  │   ├─ Insere no banco            │   ├─ redirect("loja.jsp") [ok]
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
  ├─ Hero section (apenas quando !temCliente mostra botão "Login")
  │
  ├─ Shop section
  │   ├─ Filtros: <a> links com ?categoria=Nome (server-side filtering)
  │   ├─ VinhoDAO.listarTodos() ou listarPorCategoria()
  │   └─ Para cada vinho:
  │       ├─ Card com foto real (se houver) ou SVG de garrafa
  │       ├─ Cliente: estoque ≤ 0 → card oculto
  │       ├─ Admin: vê todos + "* Estoque: 0" nos esgotados
  │       ├─ Clique no card → modal com detalhes (foto, nome, descrição, add ao carrinho)
  │       └─ Botão "Adicionar" → POST /carrinho
  │
  └─ rodape.jsp (include) — links dinâmicos por categoria carregados do banco
```

### 5.3. Carrinho (Sacola + ItemSacola)

| Operação | Ação | Detalhe |
|----------|------|---------|
| **Create** | Primeiro item → cria Sacola ATIVA + insere ItemSacola | CarrinhoServlet |
| **Read** | Lista itens da sacola ativa (JOIN com Vinho) | carrinho.jsp |
| **Update** | Altera quantidade de ItemSacola | CarrinhoServlet |
| **Delete** | Remove ItemSacola | **Quantidade > 1**: decrementa -1. **= 1**: exclui item. Se sacola ficar vazia → CANCELADA |

```
Adicionar ao carrinho:
  POST /carrinho (acao=adicionar, idVinho=X, quantidade=1)
    ├─ Verifica se cliente tem Sacola ATIVA
    ├─ Se não: cria Sacola(email_cliente, status='ATIVA')
    ├─ Calcula total: qtdAtual + nova. Se > estoque → erro "estoque insuficiente"
    ├─ Verifica se vinho já está na sacola
    │   ├─ Sim: atualiza quantidade (soma)
    │   └─ Não: insere novo ItemSacola
    ├─ forward("carrinho.jsp")
```

**Persistência entre sessões:** A Sacola é salva no banco. Ao deslogar, a sessão é invalidada mas a sacola permanece. Ao logar novamente, `SacolaDAO.buscarAtivaPorCliente(email)` recupera tudo.

### 5.4. Checkout (Sacola → Pedido) com Transação JDBC

```
POST /checkout
  ├─ Busca Sacola ATIVA do cliente
  ├─ Lista ItemSacola + calcula valorTotal
  ├─ **Abre conexão manual: conn.setAutoCommit(false)**
  ├─ sacolaDAO.atualizarStatus(conn, id, 'CONVERTIDA')
  ├─ pedidoDAO.inserir(conn, pedido) → idPedido
  ├─ Para cada ItemSacola:
  │   ├─ itemPedidoDAO.inserir(conn, itemPedido)
  │   └─ vinhoDAO.atualizarEstoque(conn, idVinho, novoEstoque)
  ├─ **conn.commit()** — todas as 4 operações são atômicas
  ├─ Se qualquer exceção: **conn.rollback()**
  └─ forward("confirmacao.jsp") com idPedido
```

### 5.5. Upload da Foto do Vinho (duas fases)

O `UploadVinhoBean` separa o upload em dois momentos para contornar o problema do ID auto-increment:

**Fase 1 — `processarUpload(request)`:** parseia o `multipart/form-data`, extrai campos de formulário para `Map<String, String>`, valida extensão/tamanho do arquivo, armazena o `FileItem` sem gravar em disco.

**Fase 2 — `salvarArquivo(context, idVinho)`:** grava o `FileItem` em disco como `images/vinhos/<id>.jpg` usando o ID gerado pelo banco.

O `VinhoServlet` (`/admin/vinho`) orquestra ambas as fases:
```
POST /admin/vinho?acao=inserir (enctype=multipart)
  ├─ upload.processarUpload(request) → extrai campos + valida arquivo
  ├─ Valida safra ≥ 1900
  ├─ vinhoDAO.inserir(vinho) → retorna id gerado
  ├─ Se upload.temArquivo():
  │   ├─ upload.salvarArquivo(application, id)
  │   └─ vinhoDAO.atualizarCaminhoFoto(id, caminho)
  └─ redirect("vinhos.jsp")
```

---

## 6. Banco de Dados

### Modelo Entidade-Relacionamento

```
Cliente (email PK, nome, cpf UNIQUE, senha, tipo)
    │                  tipo: 'ADMIN' | 'CLIENTE'
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

### Regras de negócio (CONSTRAINTs)

```sql
CHECK (preco >= 0)
CHECK (estoque >= 0)
CHECK (quantidade > 0)
CHECK (status IN ('ATIVA','CONVERTIDA','CANCELADA'))
CHECK (status_pagamento IN ('PENDENTE','PAGO','CANCELADO'))
CHECK (status_envio IN ('PENDENTE','PREPARANDO','ENVIADO','ENTREGUE'))
```

### Diferenciais do modelo

1. **`caminho_foto VARCHAR(255) NULL`** — apenas o caminho relativo, sem BLOB.
2. **`preco_unitario` em ItemPedido** — preço congelado na compra.
3. **`id_sacola UNIQUE` em Pedido** — uma sacola = um pedido.
4. **`ON DELETE RESTRICT`** — impede exclusão de registros referenciados (com mensagem amigável tratada nos servlets).
5. **Coluna `tipo` em Cliente** — 'ADMIN' | 'CLIENTE' (com CHECK constraint).

---

## 7. Design System (CSS)

Variáveis CSS em `:root`:

```css
--bg: #1C1B19;           --surface: #242019;    --surface-2: #2C2620;
--text: #EDE6D6;         --text-muted: #A79A85;
--amber: #C98A3B;        --amber-soft: #E0AD6B;
--wine: #5B1F2B;         --wine-soft: #7A2C3B;
--line: rgba(237,230,214,0.12);   --line-strong: rgba(237,230,214,0.22);
--radius-arch: 132px 132px 10px 10px;
```

### Tipografia
- **Google Fonts**: `Fraunces` (headings, itálico) + `Inter` (corpo)
- Eyebrow labels: Inter, uppercase, 12.5px, cor `--amber-soft`

### Ícones (7 símbolos SVG em `topo.jsp`)
- `i-cart`, `i-menu`, `i-close`, `i-arrow`, `i-chevron`, `i-plus`, `i-check`, `i-user`, `i-package`

### Modal
- Overlay `position: fixed` com `background: rgba(0,0,0,0.75)`
- Card com 2 colunas (foto + info), responsivo (1 coluna mobile)
- JS: `style.display = 'flex'` / `'none'` para exibir/ocultar

---

## 8. Interações JavaScript (`js/script.js`)

Todas em vanilla JS (sem bibliotecas):

| Interação | Técnica |
|-----------|---------|
| **Header glassmorphism** | scroll event → `is-scrolled` class |
| **Mobile menu** | `classList.toggle("is-open")` |
| **Account panel** | toggle + `aria-expanded` + fecha ao clicar fora/ESC |
| **Cart badge** | animação CSS `bump` ao adicionar |
| **Toast** | `setTimeout` 2.2s |
| **Reveal** | `IntersectionObserver` (threshold 0.15) |
| **Confirm delete** | `data-confirm` attribute → `confirm()` |

**Modal (loja.jsp):** handler inline com `addEventListener('click')` nas cards (`.js-modal-card`). Ignora cliques em `<button>`, `<a>`, `<input>`, `<select>`. Preenche o modal via `data-*` attributes do card.

---

## 9. Sessão e Escopo de Beans

| Bean | Escopo | Criado por | Usado em |
|------|--------|------------|----------|
| `Cliente` (`clienteLogado`) | `session` | `<jsp:useBean>` em `topo.jsp`, populado pelo `LoginServlet` | Todas as páginas via `topo.jsp` |
| `UploadVinhoBean` | `page` | `<jsp:useBean>` nos admin JSPs | `cadastroVinho.jsp`, `editarVinho.jsp` |

---

## 10. Tratamento de Erros

### web.xml
```xml
<error-page><error-code>404</error-code><location>/erro404.html</location></error-page>
<error-page><error-code>500</error-code><location>/erro.jsp</location></error-page>
<error-page><exception-type>java.lang.Exception</exception-type><location>/erro.jsp</location></error-page>
<error-page><exception-type>java.sql.SQLException</exception-type><location>/erro.jsp</location></error-page>
<error-page><exception-type>java.lang.NumberFormatException</exception-type><location>/erro.jsp</location></error-page>
```

### FK constraints
Mensagens amigáveis ao tentar excluir categoria com vinhos ou vinho em sacolas/pedidos. Detectadas via `e.getMessage().contains("foreign key")` no AdminServlet.

### Safra < 1900
Validação server-side no `VinhoServlet` com mensagem: "A safra deve ser maior ou igual a 1900."

---

## 11. Resumo das Classes

### JavaBeans (8)
| Classe | Atributos principais | Métodos notáveis |
|--------|---------------------|------------------|
| `Cliente` | email, nome, cpf, senha, tipo | get/set |
| `CategoriaVinho` | id, nome, descricao | get/set |
| `Vinho` | id, nome, safra, descricao, preco, estoque, idCategoria, caminhoFoto | get/set |
| `Sacola` | id, emailCliente, dataCriacao, status | get/set |
| `ItemSacola` | idSacola, idVinho, quantidade, vinho | getSubtotal() |
| `Pedido` | id, dataConclusao, valorTotal, statusPagamento, statusEnvio, idSacola | get/set |
| `ItemPedido` | idPedido, idVinho, quantidade, precoUnitario, vinho | getSubtotal() |
| `UploadVinhoBean` | diretorio, size, extensoesPermitidas, erro, parametros, fileItem | processarUpload(request), salvarArquivo(context, id), temArquivo() |

### DAOs (8)
| Classe | Operações SQL |
|--------|--------------|
| `ConnectionFactory` | `getConnection()` |
| `ClienteDAO` | INSERT, SELECT por email, SELECT por email+senha, UPDATE |
| `CategoriaVinhoDAO` | INSERT, UPDATE, DELETE, SELECT por id + ALL |
| `VinhoDAO` | INSERT (generated keys), UPDATE, DELETE, SELECT por id/ALL/categoria, atualizarCaminhoFoto, atualizarEstoque (com overload Connection) |
| `SacolaDAO` | INSERT, SELECT por id, SELECT ativa por cliente, UPDATE status (com overload Connection), SELECT ALL |
| `ItemSacolaDAO` | INSERT, UPDATE quantidade, DELETE por item, SELECT por sacola (JOIN), SELECT por id |
| `PedidoDAO` | INSERT (com overload Connection), SELECT por id, SELECT por cliente, SELECT ALL, UPDATE status_envio, UPDATE status_pagamento |
| `ItemPedidoDAO` | INSERT (com overload Connection), SELECT por pedido (JOIN) |

### Servlets (8)
| Servlet | Ações POST |
|---------|-----------|
| `LoginServlet` | login → redirect/forward |
| `LogoutServlet` | [GET] logout → invalida sessão → redirect |
| `CadastroServlet` | cadastro → valida → insere → forward |
| `CarrinhoServlet` | adicionar (valida estoque total), atualizar, remover (decrementa ou exclui) |
| `CheckoutServlet` | **transação JDBC** → converte sacola → pedido + baixa estoque |
| `AdminServlet` | CRUD categorias, excluir vinho (FK amigável), atualizar status_envio, cancelar pedido (restaura estoque) |
| `PerfilServlet` | editar nome, CPF, senha (valida senha atual) |
| `VinhoServlet` | `acao=inserir` / `acao=alterar` → upload foto + validar safra |

---

## 12. Telas (21 páginas)

### Públicas (15)
| Tela | Arquivo | Funcionalidade |
|------|---------|----------------|
| **Início** | `index.jsp` | Redirect para loja |
| **Login** | `login.jsp` | Formulário standalone (fallback) |
| **Cadastro** | `cadastro.jsp` | Formulário standalone (fallback) |
| **Loja** | `loja.jsp` | Hero + filtros server-side + grid de cards + modal de detalhes |
| **Detalhe** | `vinho.jsp` | Página standalone de detalhes do vinho (fallback) |
| **Carrinho** | `carrinho.jsp` | Tabela com quantidades editáveis + remover incremental |
| **Checkout** | `checkout.jsp` | Resumo + confirmação com transação |
| **Confirmação** | `confirmacao.jsp` | Check verde + id do pedido |
| **Histórico** | `historico.jsp` | Pedidos do cliente com status badges |
| **Detalhe Pedido** | `detalhePedido.jsp` | Itens de um pedido específico |
| **Perfil** | `perfil.jsp` | Editar nome, CPF, senha |
| **Sucesso** | `sucesso.jsp` | "Operação concluída" + botão Voltar |
| **Erro** | `erro.jsp` | `isErrorPage="true"`, mostra `exception.getMessage()` |
| **404** | `erro404.html` | Página estática |

### Componentes reutilizados
| Componente | Arquivo |
|------------|---------|
| **Header público** | `topo.jsp` — header fixo, nav, account panel (login/cadastro ou perfil), ícone carrinho/pedidos |
| **Footer público** | `rodape.jsp` — links dinâmicos de categorias + links de conta (admin vs cliente vs visitante) |

### Administrativas (6)
| Tela | Arquivo | Funcionalidade |
|------|---------|----------------|
| **Header Admin** | `admin/topo.jsp` | Nav: Vinhos, Categorias, Pedidos, Ver Loja, Sair |
| **Categorias** | `admin/categorias.jsp` | CRUD inline via AdminServlet |
| **Vinhos** | `admin/vinhos.jsp` | Listagem com miniaturas + "* 0" nos esgotados |
| **Cadastrar Vinho** | `admin/cadastroVinho.jsp` | View pura (submit → VinhoServlet) |
| **Editar Vinho** | `admin/editarVinho.jsp` | View pura (submit → VinhoServlet) |
| **Pedidos/Sacolas** | `admin/pedidos.jsp` | Abas: Pedidos (editar status_envio, cancelar c/ restauro de estoque) + Sacolas (ATIVA/CONVERTIDA, sem CANCELADA) |

---

## 13. Regras de Negócio Completo

### Estoque
- Cliente não pode adicionar mais que o estoque total (qtd carrinho + nova)
- Admin sempre vê quantidade, com `*` se zerado
- Cliente não vê vinhos esgotados na loja
- Ao confirmar pagamento: estoque -= quantidade
- Ao cancelar pedido (admin): estoque += quantidade

### Carrinho
- Primeiro item: cria Sacola ATIVA
- Remover: se qtd > 1 → decrementa; se qtd = 1 → exclui item
- Último item removido: Sacola → CANCELADA
- Sacola persiste entre sessões (salva no banco)

### Admin
- Vê todos os pedidos de todos os usuários
- Edita status_envio (dropdown com auto-submit)
- Cancela pedido → status_pagamento = CANCELADO + restaura estoque
- Vê sacolas ATIVA e CONVERTIDA (CANCELADA oculta)
- FK constraint → mensagem amigável (não o erro SQL bruto)

### Perfil
- Acessível pelo ícone de usuário → "Editar Perfil" → página dedicada
- Altera nome, CPF, senha
- Senha atual obrigatória para qualquer alteração

---

## 14. Considerações Finais

### Melhorias em relação à v0
- **Transação JDBC** no checkout (atomicidade)
- **VinhoServlet** extraindo lógica de negócio dos JSPs
- **Estoque completo**: validação ao adicionar, baixa no checkout, restauro ao cancelar
- **Remover incremental** no carrinho (decrementa em vez de excluir)
- **Persistência da sacola** entre sessões
- **Modal** de detalhes do vinho na loja
- **Admin pedidos/sacolas** com edição de status e cancelamento
- **Perfil** em página dedicada
- **Dead code removido** (5 métodos não usados)
- **Exceções tratadas** (sem catch vazio)
- **SVG sprite limpo** (apenas ícones usados)
- **Títulos padronizados** (hífen normal em vez de em-dash)

### Para executar
1. Importar no Eclipse como **Dynamic Web Project**
2. JARs em `WEB-INF/lib`: mysql-connector-java, commons-fileupload, commons-io
3. Executar `data/create-operations.sql` e `data/populate-database.sql`
4. Configurar `ConnectionFactory.java` com usuário/senha do MySQL
5. Deploy no Tomcat 9+ → `http://localhost:8080/trabalho-final`
6. Admin: `admin@cavefontana.com` / `admin123`

<style>
  :root {
    --bg-base: #0B0F14;
    --bg-card: #111827;
    --bg-hover: #161B22;
    --border: #1F2937;
    --border-strong: #374151;

    --coffee-dark: #3C2415;
    --coffee-medium: #6F4E37;
    --coffee-accent: #A67B5B;
    --coffee-cream: #E8D5C4;
    --coffee-latte: #F5E6D0;

    --blue-primary: #1E40AF;
    --blue-secondary: #3B82F6;
    --blue-accent: #60A5FA;
    --blue-electric: #38BDF8;

    --success: #22C55E;
    --warning: #EAB308;
    --error: #EF4444;
    --muted: #6B7280;

    --text-primary: #F9FAFB;
    --text-secondary: #D1D5DB;
    --text-muted: #9CA3AF;

    --radius: 8px;
    --radius-sm: 4px;
    --radius-lg: 12px;
  }

  body {
    font-family: 'Segoe UI', system-ui, -apple-system, sans-serif;
    background: var(--bg-base);
    color: var(--text-primary);
    line-height: 1.6;
    max-width: 900px;
    margin: 0 auto;
    padding: 24px;
  }

  h1 {
    font-size: 2rem;
    font-weight: 700;
    color: var(--coffee-cream);
    border-bottom: 2px solid var(--coffee-medium);
    padding-bottom: 12px;
    display: flex;
    align-items: center;
    gap: 12px;
  }

  h1::before {
    content: "☕";
    font-size: 1.8rem;
  }

  h2 {
    font-size: 1.3rem;
    font-weight: 600;
    color: var(--coffee-accent);
    margin-top: 32px;
    margin-bottom: 12px;
    padding: 8px 12px;
    background: linear-gradient(90deg, var(--coffee-dark), transparent);
    border-left: 3px solid var(--coffee-accent);
    border-radius: 0 var(--radius) var(--radius) 0;
  }

  h3 {
    font-size: 1.05rem;
    font-weight: 600;
    color: var(--text-secondary);
    margin-top: 24px;
    margin-bottom: 8px;
  }

  a {
    color: var(--blue-secondary);
    text-decoration: none;
  }

  a:hover {
    color: var(--blue-accent);
    text-decoration: underline;
  }

  .badge {
    display: inline-block;
    padding: 2px 10px;
    border-radius: var(--radius-sm);
    font-size: 0.75rem;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    margin-right: 6px;
  }

  .badge-fire { background: #EF4444; color: white; }
  .badge-high { background: #F97316; color: white; }
  .badge-med { background: #EAB308; color: #0B0F14; }
  .badge-low { background: #22C55E; color: #0B0F14; }

  .badge-phase1 { background: var(--coffee-dark); color: var(--coffee-cream); border: 1px solid var(--coffee-accent); }
  .badge-phase2 { background: #1E3A5F; color: var(--blue-accent); border: 1px solid var(--blue-secondary); }
  .badge-phase3 { background: #1A3A1A; color: #4ADE80; border: 1px solid #22C55E; }

  .tag {
    display: inline-block;
    padding: 1px 8px;
    border-radius: var(--radius-sm);
    font-size: 0.7rem;
    font-family: 'JetBrains Mono', 'Fira Code', monospace;
    background: var(--bg-hover);
    color: var(--text-muted);
    border: 1px solid var(--border);
  }

  /* ── Listas regulares ── */
  ul {
    list-style: none;
    padding-left: 0;
  }

  ul li {
    padding: 6px 12px;
    margin-bottom: 4px;
    border-radius: var(--radius);
    background: var(--bg-card);
    border: 1px solid var(--border);
    transition: background 0.15s, border-color 0.15s;
    font-size: 0.9rem;
  }

  ul li:hover {
    background: var(--bg-hover);
    border-color: var(--border-strong);
  }

  /* Bullet só em listas não-task (sem checkbox do VS Code) */
  ul li::before {
    content: "□";
    margin-right: 10px;
    color: var(--muted);
    font-size: 0.85rem;
  }

  ul li.done::before {
    content: "☑";
    color: var(--success);
  }

  /* ── Task lists (- [ ] / - [x]) ── */
  ul.contains-task-list {
    list-style: none;
    padding-left: 0;
  }

  ul.contains-task-list li {
    list-style: none;
    padding: 6px 12px 6px 8px;
    margin-bottom: 4px;
    border-radius: var(--radius);
    background: var(--bg-card);
    border: 1px solid var(--border);
    transition: background 0.15s, border-color 0.15s;
    font-size: 0.9rem;
    display: flex;
    align-items: flex-start;
    gap: 8px;
  }

  ul.contains-task-list li:hover {
    background: var(--bg-hover);
    border-color: var(--border-strong);
  }

  /* Remove o bullet fantasma que o VS Code coloca */
  ul.contains-task-list li::before {
    display: none;
    content: none;
  }

  /* Checkbox customizado VS Code */
  ul.contains-task-list li input[type="checkbox"] {
    appearance: none;
    -webkit-appearance: none;
    -moz-appearance: none;
    flex-shrink: 0;
    width: 16px;
    height: 16px;
    margin: 3px 0 0 0;
    border: 1.5px solid var(--border-strong);
    border-radius: 3px;
    background: var(--bg-base);
    cursor: default;
    position: relative;
    transition: background 0.15s, border-color 0.15s;
  }

  ul.contains-task-list li input[type="checkbox"]:checked {
    background: var(--blue-secondary);
    border-color: var(--blue-secondary);
  }

  ul.contains-task-list li input[type="checkbox"]:checked::after {
    content: "";
    position: absolute;
    left: 4px;
    top: 1px;
    width: 5px;
    height: 9px;
    border: solid white;
    border-width: 0 2px 2px 0;
    transform: rotate(45deg);
  }

  ul.contains-task-list li input[type="checkbox"]:hover {
    border-color: var(--blue-accent);
  }

  .section-summary {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    margin-bottom: 16px;
  }

  .stat-card {
    background: var(--bg-card);
    border: 1px solid var(--border);
    border-radius: var(--radius);
    padding: 8px 16px;
    font-size: 0.85rem;
    color: var(--text-muted);
  }

  .stat-card strong {
    color: var(--text-primary);
    font-size: 1.1rem;
  }

  table {
    width: 100%;
    border-collapse: separate;
    border-spacing: 0;
    border-radius: var(--radius);
    overflow: hidden;
    border: 1px solid var(--border);
    font-size: 0.9rem;
    margin: 12px 0;
  }

  th {
    background: var(--coffee-dark);
    color: var(--coffee-cream);
    font-weight: 600;
    text-align: left;
    padding: 8px 12px;
    font-size: 0.8rem;
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }

  td {
    padding: 8px 12px;
    border-top: 1px solid var(--border);
    background: var(--bg-card);
    color: var(--text-secondary);
  }

  td:first-child {
    font-family: 'JetBrains Mono', 'Fira Code', monospace;
    font-size: 0.8rem;
    color: var(--text-muted);
    white-space: nowrap;
  }

  tr:hover td {
    background: var(--bg-hover);
  }

  blockquote {
    border-left: 3px solid var(--coffee-medium);
    background: var(--bg-card);
    padding: 12px 16px;
    margin: 16px 0;
    border-radius: 0 var(--radius) var(--radius) 0;
    color: var(--text-muted);
    font-size: 0.9rem;
  }

  blockquote strong {
    color: var(--coffee-accent);
  }

  hr {
    border: none;
    border-top: 1px solid var(--border);
    margin: 24px 0;
  }

  .progress-bar {
    display: flex;
    height: 6px;
    border-radius: 3px;
    overflow: hidden;
    background: var(--bg-hover);
    margin: 4px 0 12px;
  }

  .progress-fill {
    height: 100%;
    border-radius: 3px;
    transition: width 0.3s;
  }

  .progress-fill.fire { background: linear-gradient(90deg, #EF4444, #F97316); }
  .progress-fill.high { background: linear-gradient(90deg, #F97316, #EAB308); }
  .progress-fill.med { background: linear-gradient(90deg, #EAB308, #22C55E); }
  .progress-fill.low { background: linear-gradient(90deg, #22C55E, #3B82F6); }
</style>

# coffe_server — TODO

<blockquote>
<strong>Readiness:</strong> 🟢 7.5/10 · <strong>Revisão completa em 2026-08-03</strong> — arquitetura, segurança, qualidade de código e frontend. <br>
Autenticação é <strong>cookie-only</strong>: o JWT não vem mais no body de login/register, só no HttpOnly cookie. <br>
Rate limiting ativo na security chain (Bucket4j + Redis) com conexões lazy e filtro condicional. <br>
Abstração Redis completa (Fase 8 concluída — inclui <code>@EnableConfigurationProperties</code> no <code>RedisConfig</code>). <br>
Frontend migrado: <code>frontend/</code> removido, dashboard liquid-glass em <code>src/main/resources/static/app/</code> (servido em <code>/app</code>). <br>
TLS habilitado (<code>server.ssl</code>, PKCS12, HTTP/2); <code>client-auth=none</code> → mTLS ainda não ativo. <br>
392 testes passando via CI/CD (5 jobs, Java 21, Postgres 17). <br>
CORS ainda ausente no <code>SecurityConfig</code>; Testcontainers + JaCoCo no <code>pom.xml</code>.
</blockquote>

---

## 📊 Progresso Geral

<div class="section-summary">
  <div class="stat-card"><strong>8</strong> / 10<br><span class="tag">🧱 Arquitetura</span></div>
  <div class="stat-card"><strong>10</strong> / 18<br><span class="tag">🖥️ Backend</span></div>
  <div class="stat-card"><strong>6</strong> / 8<br><span class="tag">🎨 Frontend</span></div>
  <div class="stat-card"><strong>7</strong> / 12<br><span class="tag">🔒 Segurança</span></div>
  <div class="stat-card"><strong>3</strong> / 6<br><span class="tag">🔌 APIs</span></div>
  <div class="stat-card"><strong>5</strong> / 8<br><span class="tag">🗄️ Banco de Dados</span></div>
  <div class="stat-card"><strong>6</strong> / 8<br><span class="tag">⚙️ Infraestrutura</span></div>
  <div class="stat-card"><strong>4</strong> / 8<br><span class="tag">📈 Observabilidade</span></div>
  <div class="stat-card"><strong>7</strong> / 8<br><span class="tag">📚 Documentação</span></div>
  <div class="stat-card"><strong>3</strong> / 6<br><span class="tag">🚀 Performance</span></div>
  <div class="stat-card"><strong>8</strong> / 10<br><span class="tag">🧪 Testes</span></div>
  <div class="stat-card"><strong>4</strong> / 7<br><span class="tag">♻️ Refatoração</span></div>
  <div class="stat-card"><strong>3</strong> / 5<br><span class="tag">🤖 MCP</span></div>
  <div class="stat-card"><strong>1</strong> / 5<br><span class="tag">📦 Modularização</span></div>
  <div class="stat-card"><strong>5</strong> / 8<br><span class="tag">🚀 Melhorias Futuras</span></div>
</div>

> Status: `✅ Concluído` · `🚧 Em andamento` · `📝 TODO` · `⛔ Bloqueado`
> Prioridade: `🔥 Alta` · `🟡 Média` · `🟢 Baixa`

---

## 🧱 Arquitetura

<span class="badge badge-phase2">Fundação</span>

- [x] **CookieSystem refatorado para o domínio** — `CookieManager` + `CookieFactory` como portas em `domain/interfaces/Cookies/`; `HttpCookieWriter` como SPI na application; `CookieMapper`/`HttpCookieWriterManeger` na infra. Eliminou a violação de `CookieService` na infraestrutura.
  <span class="tag">commit 141eb1d · domain/interfaces/Cookies/</span>
- [x] **MachineNotFoundException movido para `domain/exception/`** — exceção de domínio, não mais em `shared/`.
  <span class="tag">commit 363334f · domain/exception/MachineNotFoundException.java</span>
- [x] **Typos em classes públicas corrigidos** — `Provider` (era `Provaider`), `ExternalAccount`, `JwtTokenResolver`, `BCryptPasswordService`, `GoogleCalendarTools`, `GoogleCalendarService` etc. Renomeados sem quebrar migração (a partir da correção de acoplamento).
  <span class="tag">commits e4b55f1 · bba8d2d · 0ab7bd6</span>
- [ ] **Mover `TokenResolverManager` de infra para application** — é orquestrador (use case), não implementação concreta. Hoje vive em `infrastructure/services/Auth/Token/`. Impacto: deixa o chain de resolução testável sem contexto Spring.
  <span class="tag">🟡 Média · infrastructure/services/Auth/Token/TokenResolverManager.java</span>
- [ ] **`OAuth2UserProvisioningService` usa `JpaUserRepository` direto** — bypassa a porta `UserRepository` do domínio (Dependency Inversion). Trocar para injetar a porta e mapear `UserEntity` → `User`. A autoridade `"ROLE_"+role` e o `passwordHash` UUID já foram corrigidos.
  <span class="tag">🔥 Alta · infrastructure/services/OAuth/OAuth2UserProvisioningService.java:20-23</span>
- [ ] **Enriquecer domínio (models anêmicos)** — mover regras para os models: `Machine.wakeOnLan()`, `ExternalAccount.refreshTokenIfExpired()`, `User` validando email/role. Reduce lógica espalhada em services.
  <span class="tag">🟢 Baixa · domain/models/</span>
- [ ] **`Domain/Database` + `domain/interfaces/Database` — renomear diretórios com typos** — `Adpter`, `Provaider`, `Arry`, `Ratelimit` seguem como nomes de pacote/arquivo. Nomes de classe já corrigidos; pacotes só em nova versão (breaking de import).
  <span class="tag">🟢 Baixa · pacotes infra</span>
- [ ] **ADRs documentados** — registrar decisões `ADR-003` (maven modules) e `ADR-005` (MCP module) quando decididas.
  <span class="tag">🟡 Média · docs/architecture/adr/</span>

---

## 🖥️ Backend

<span class="badge badge-phase1">Núcleo do servidor</span>

### ✅ Concluído

- [x] **Autenticação cookie-only** — login/register não devolvem JWT no body; token só no HttpOnly cookie (`access_token`). Frontend usa cookie e lê sessão via `/api/test`.
  <span class="tag">commit c4d575e · AuthenticationController.java</span>
- [x] **Endpoint `/auth/logout`** — revoga o cookie com `Max-Age=0`.
  <span class="tag">commit f87e29b · AuthenticationController.java:69-75</span>
- [x] **`MachineEntity` userId mapping corrigido** — `userId` agora é atribuído ao `@ManyToOne user`; elimina `ConstraintViolationException`.
  <span class="tag">commit 160ae3e · MachineEntity.java:62-69</span>
- [x] **`MachineRepositoryAdapter.setOwner()` implementado** — usa `updateOwner(machineId, userId)` do JPA em vez de retornar `new User()` vazio.
  <span class="tag">commit 160ae3e · MachineRepositoryAdapter.java:84-90</span>
- [x] **`JwtTokenService.extractIdSubject` retorna `Optional<Long>`** — sem `null` no contrato.
  <span class="tag">commit 3a574f6 · JwtTokenService.java:57-76</span>
- [x] **`User.toString()` sem vazar `passwordHash`** — BCrypt hash não exposto em logs.
  <span class="tag">commit 2d36b5f · User.java:98-102</span>
- [x] **`User.changePassword` valida tamanho mínimo (8)** — lança `InvalidPasswordException` (domínio).
  <span class="tag">commit ff95760 · User.java:58-61</span>
- [x] **401 em falha de autenticação** — `SecurityConfig` configura `authenticationEntryPoint` → 401; filter limpa contexto.
  <span class="tag">commit 3a574f6 · SecurityConfig.java:76-80</span>

### 📝 Pendências

- [ ] **`UserService` é esqueleto** — só construtor, sem métodos. Implementar CRUD básico (listar, atualizar perfil/role, habilitar/desabilitar) usando a porta `UserRepository`.
  <span class="tag">🟡 Média · application/services/Users/UserService.java</span>
- [ ] **`MachineService` lança `UsernameNotFoundException` (Spring)** — vazamento de framework na camada de aplicação. Criar `UserNotFoundException` no domínio e usar `Optional` para resolver o usuário.
  <span class="tag">🟡 Média · MachineService.java:28</span>
- [ ] **Wake-on-LAN não implementado** — `Machine` tem `wolEnabled`/`macAddress`, mas nenhum magic packet é enviado. Implementar service de WoL (UDP broadcast porta 9) + rota.
  <span class="tag">🟡 Média · domain/models/Machine/</span>
- [ ] **Tailscale sem integração real** — campo `tailscaleNodeKey` existe; consumir API/serviço Tailscale para status e chave dos nós.
  <span class="tag">🟢 Baixa · domain/models/Machine/</span>
- [ ] **Validação de senha inconsistente no register** — `AuthenticationController.register` valida apenas `length 1..500`; reusar a mesma política do `changePassword` (mínimo 8, composição) via DTO/validação de domínio.
  <span class="tag">🟡 Média · AuthenticationController.java:53</span>
- [ ] **DTOs com Bean Validation real** — `@NotBlank`, `@Email`, `@Size` nos DTOs de auth; hoje a validação é manual no controller. Dependency `spring-boot-starter-validation` já presente (pom.xml:148-151) — só falta aplicar nos DTOs.
  <span class="tag">🟡 Média · application/dto/Auth/ · pom.xml:148-151</span>

---

## 🎨 Frontend

<span class="badge badge-phase3">Dashboard liquid-glass</span>

> O frontend vive em `src/main/resources/static/app/` e é servido diretamente pelo Spring Boot em `/app/**`. Detalhes completos em **`src/main/resources/static/app/TODO.md`**.

### ✅ Concluído

- [x] **Dashboard liquid-glass reescrito** — header toolbar glass, seções (Usuário → Servidor → Sistema│Rede → Discos → Containers → Máquinas → Calendário → MCP → Logs ao vivo), KPIs sem `null`, modal de máquina, refresh, logs simulados.
  <span class="tag">commits 3932bf4 · 803b452 · static/app/</span>
- [x] **`frontend/` antigo removido** — migração concluída para `static/app/`; nada copiado em build.
  <span class="tag">static/app/ · HomeController redirects</span>
- [x] **Páginas de login e dashboard funcional** — `HomeController` redireciona `/` → `/app/index.html` e `/login` → `/app/pages/login.html`.
  <span class="tag">HomeController.java:9-18</span>
- [x] **XSS eliminado no Modal e badges MCP** — `trustHtml` explícito, `textContent` por padrão; revisado por security-tester + Playwright.
  <span class="tag">static/app/components/Modal.js · pages/mcp.js</span>
- [x] **Responsividade 1280 / 640 / 480** — header em grid de 3 zonas, nav ícones-only em <600px.
  <span class="tag">static/app/styles/layout.css</span>
- [x] **Design system tokens coffee + blue** — `tokens.css` com `--glass-*`, z-index header 250.
  <span class="tag">static/app/styles/tokens.css</span>

### 🚧 Em andamento

- [ ] **Centralização do header** — grid 3 zonas já aplicado; falta re-validar com usuário curto/longo e screenshots finais.
  <span class="tag">🚧 Em andamento · static/app/TODO.md</span>

### 📝 Pendências

- [ ] **WebSocket real (`/ws`)** — hoje `wss://localhost:8080/ws` retorna 404; decidir entre status push real ou remover tentativa.
  <span class="tag">🟡 Média · static/app/websocket/</span>
- [ ] **Logs ao vivo com dados reais** — substituir `randomLogEntry()` quando backend expuser eventos MCP ou `/api/audit/logs`.
  <span class="tag">🟢 Baixa · static/app/mock/logs.js</span>
- [ ] **Data provider por página** — trocar os 9 mocks no topo de `dashboard.js` por fetch com fallback mock quando `/api/info/*` existir.
  <span class="tag">🟢 Baixa · static/app/pages/dashboard.js</span>
- [ ] **`smoke-spring.mjs` defasado** — usa credenciais inexistentes; atualizar para `admin_teste`/`Senha123!`.
  <span class="tag">🟢 Baixa · scripts de verificação</span>

---

## 🔒 Segurança

<span class="badge badge-fire">Crítico</span>

### ✅ Concluído

- [x] **Rate limiting em `/auth/login` e `/auth/register`** — `RateLimitFilter` na chain (Bucket4j distribuído via Redis, filtro condicional via `coffee.ratelimit.enabled`), conexões lazy, boot não depende do Redis.
  <span class="tag">commits 7038077 · 6fe2e71 · 899ab83 · Filter/Ratelimit/</span>
- [x] **TLS habilitado** — `server.ssl.enabled=true`, keystore PKCS12, HTTP/2 ativo.
  <span class="tag">application.properties:42-47</span>
- [x] **OAuth2 authority corrigida** — `"ROLE_"+role` (sem espaço) → usuários OAuth2 acessam rotas protegidas.
  <span class="tag">OAuth2UserProvisioningService.java:43</span>
- [x] **OAuth2 cria usuário com passwordHash** — UUID randômico evita `ConstraintViolationException` (`password_hash NOT NULL`).
  <span class="tag">OAuth2UserProvisioningService.java:38</span>
- [x] **`JwtTokenService.verifyToken` null-safe** — não estoura em token ausente/malformado.
  <span class="tag">commit 3a574f6 · JwtTokenService.java:40-55</span>
- [x] **Logout revoga cookie** — `/auth/logout` com `Max-Age=0`.
  <span class="tag">AuthenticationController.java:69-75</span>
- [x] **`.env` fora do tracking** — credenciais não commitadas (commit 8645845).
  <span class="tag">.gitignore · .env</span>

### 📝 Pendências

- [ ] **CORS configuration** — confirmado na auditoria: `SecurityConfig.securityFilterChain` não define nenhum `CorsConfigurationSource` (apenas `.csrf(disable)` + filters). Necessário para clientes web fora de `/app` (ex.: PS3, CLI browser).
  <span class="tag">🔥 Alta · SecurityConfig.java:40-89</span>
- [ ] **`RateLimitFilter` engole exceções silenciosamente** — `catch (IllegalArgumentException | Exception)` vazios; logar (SLF4J) e diferenciar política por rota (`LOGIN` vs `REGISTER` vs `API`), hoje força sempre `RateLimitPolicy.API`.
  <span class="tag">🟡 Média · RateLimitFilter.java:52-57</span>
- [ ] **SameSite=Strict nos cookies** — `CookieMapper` usa `"Lax"`; migrar para `Strict` e expor como campo opcional no `CookieDomain` (default seguro).
  <span class="tag">🟡 Média · infrastructure/Mappers/Cookies/CookieMapper.java:18</span>
- [ ] **mTLS (client-auth=none → require)** — TLS server-side pronto; falta CA própria (ADR-004), emissão de certs client e `X509AuthenticationFilter`. Decisão híbrida JWT + mTLS (ADR-001) já tomada.
  <span class="tag">⛔ Bloqueado (aguarda decisão de CA) · application.properties:47</span>
- [ ] **Permissões granulares / scopes (Fase 5)** — `Role` hoje é `ADMIN/USER/MCP/API`; modelar `permissions`, tabela N:N, scopes no JWT e `@PreAuthorize` por permissão.
  <span class="tag">🟡 Média · domain/enums/Role.java</span>

---

## 🔌 APIs

<span class="badge badge-phase2">Contratos REST + MCP</span>

### ✅ Concluído

- [x] **`/api/test` health/sessão** — endpoint público que ecoa o usuário autenticado; usado pelo frontend como check de sessão.
  <span class="tag">APIController.java</span>
- [x] **MCP server configurado** — `spring.ai.mcp.server.*` (base `/mcp`, timeout 300s), rota protegida com `hasAuthority("MCP")`.
  <span class="tag">application.properties:56-59 · SecurityConfig.java:50</span>
- [x] **Login/Register/Logout REST** — endpoints funcionais com cookies.
  <span class="tag">AuthenticationController.java</span>

### 📝 Pendências

- [ ] **Versionamento de API** — hoje rotas são `/auth/*`, `/api/test`, `/api/calendar/*` sem `/api/v1`. Definir ADR-002 (recomendação: URL path) e aplicar nos controllers.
  <span class="tag">🔥 Alta · todos controllers</span>
- [ ] **`CalendarController` exposto sem proteção** — `@PreAuthorize("permitAll()")` no nível da classe deixa `/api/calendar/events` e `/api/calendar/debug/auth` abertos; proteger por endpoint/role.
  <span class="tag">🔥 Alta · mcp/tools/CalendarController.java:18</span>
- [ ] **`System.out/err` em produção** — `CalendarController` e `GoogleCalendarTools` usam prints; trocar por SLF4J com contexto estruturado.
  <span class="tag">🟡 Média · CalendarController.java:31,43,46</span>

---

## 🗄️ Banco de Dados

<span class="badge badge-phase1">PostgreSQL 17 (prod) / H2 (dev)</span>

### ✅ Concluído

- [x] **Scripts SQL `docs/sql/`** — ordem de criação `00_init` → `06_roles` (groups, user, linux_user, machine, external_account, roles).
  <span class="tag">docs/sql/*.sql</span>
- [x] **Profiles H2/test com seed** — `data-h2.sql` (admin_teste + user `t`) e `test-data.sql`; `ddl-auto=create-drop`.
  <span class="tag">application-h2.properties · test/application-test.properties</span>
- [x] **Persistence User + Machine completa** — entity, mapper, adapter e Spring Data repo para ambos.
  <span class="tag">infrastructure/db/User/ · db/Machine/</span>
- [x] **Entidades LinuxUser e Groups criadas** — model + entity; relacionamentos M:1 com groups.
  <span class="tag">infrastructure/db/LinuxUser/Entity/</span>
- [x] **Entidade ExternalAccount criada** — model + entity para OAuth externo.
  <span class="tag">infrastructure/db/User/Entity/ExternalAccountEntity.java</span>

### 📝 Pendências

- [ ] **Persistence `LinuxUser`/`Groups` incompleta** — entities existem, faltam: Spring Data repos, mappers, adapters e portas de domínio (`LinuxUserRepository`).
  <span class="tag">🔥 Alta · infrastructure/db/LinuxUser/</span>
- [ ] **Persistence `ExternalAccount` incompleta** — entity existe, faltam repo, mapper, adapter e porta de domínio.
  <span class="tag">🔥 Alta · infrastructure/db/User/Entity/ExternalAccountEntity.java</span>
- [ ] **Vincular `ExternalAccount` durante OAuth2** — o login Google não persiste a conta externa nem o token de refresh do usuário.
  <span class="tag">🟡 Média · OAuth2UserProvisioningService.java</span>

---

## ⚙️ Infraestrutura

<span class="badge badge-phase2">Adapters + Integrações externas</span>

### ✅ Concluído

- [x] **Abstração Redis (Ports & Adapters) completa** — `Connection`, `DatabaseClientProvider`, `DatabaseProperties` no domínio; `RedisClientProvider` (lazy) + `RedisClientConnectionAdapter` + `StringByteArrayCodec` na infra. `@EnableConfigurationProperties(RedisProperties)` registrado no `RedisConfig`.
  <span class="tag">commits 77ab29a · 067be32 · RedisConfig.java:7</span>
- [x] **Bucket4j conectado à abstração** — `Bucket4jConfig` cria `RateLimit` via provider; `PolicyProvider` implementado.
  <span class="tag">config/ratelimit/Bucket4jConfig.java</span>
- [x] **Properties `coffee.redis.*` em todos os profiles** — instâncias `cache` e `rate-limit` (prod/h2/test).
  <span class="tag">application*.properties</span>
- [x] **Logging estruturado** — `logback-spring.xml` com rolling file appenders e separação de erros.
  <span class="tag">commit c36aca6 · logback-spring.xml</span>
- [x] **CI/CD GitHub Actions** — 5 jobs (build, testes, CodeQL, integração), Java 21 temurin, Postgres 17 service, profile `test`.
  <span class="tag">.github/workflows/ci-cd.yml</span>
- [x] **`security-lab` docker-compose** — ambiente isolado para testes de segurança.
  <span class="tag">security-lab/docker-compose.yml</span>

### 📝 Pendências

- [ ] **`@PreDestroy` para fechar conexões Redis** — `RedisClientProvider` precisa fechar clients/connections no shutdown para evitar vazamento de threads Netty.
  <span class="tag">🔥 Alta · RedisClientProvider.java</span>
- [ ] **TLS/SSL nas conexões Redis** — hoje `redis://` sem TLS; usar `RedisURI.Builder` com `.withSsl(true)`.
  <span class="tag">🟡 Média · RedisClientProvider.java:45-47</span>
- [ ] **Senha Redis** — campo `password` em `RedisClientInstace` (domínio) + provider.
  <span class="tag">🟡 Média · domain/Database/redis/RedisClientInstace.java</span>

---

## 📈 Observabilidade

<span class="badge badge-phase3">Monitoramento</span>

### ✅ Concluído

- [x] **Actuator no classpath** — `spring-boot-starter-actuator` presente.
  <span class="tag">pom.xml</span>
- [x] **Logs coloridos + rolling** — `CoffeColorConverter` + appenders de arquivo com separação de erro.
  <span class="tag">config/logger/ · logback-spring.xml</span>
- [x] **CI/CD reports** — jobs de teste e CodeQL no pipeline.
  <span class="tag">ci-cd.yml</span>
- [x] **Levels de log por package** — `com.quitto=DEBUG`, security TRACE (dev).
  <span class="tag">application.properties:49-53</span>

### 📝 Pendências

- [ ] **MDC (request ID / user ID / session ID)** — adicionar filtro que popula MDC em cada requisição para correlação de logs.
  <span class="tag">🔥 Alta · infra security</span>
- [ ] **Métricas de autenticação e endpoints** — Micrometer: tentativas de login (sucesso/falha), latência e taxa de erro por rota.
  <span class="tag">🟡 Média · Micrometer</span>
- [ ] **`/api/health` detalhado** — status do banco (Postgres/H2), Google Calendar, disk (`/mnt/mount/data/backups`), Docker, Tailscale.
  <span class="tag">🟡 Média · Actuator + custom indicators</span>
- [ ] **Audit logging (Fase 6)** — `AuditLog` model + `@Auditable` annotation + endpoint `/api/audit/logs` (admin) para eventos sensíveis.
  <span class="tag">🟢 Baixa · domain + aspect</span>

---

## 📚 Documentação

<span class="badge badge-phase2">Docs viva</span>

### ✅ Concluído

- [x] **`docs/architecture/arquiteture.md`** — documentação completa de arquitetura (camadas, fluxos, glossário).
  <span class="tag">docs/architecture/</span>
- [x] **`docs/architecture/redis-abstraction.md`** — doc da abstração Redis (Ports & Adapters).
  <span class="tag">docs/architecture/</span>
- [x] **`docs/audits/`** — `relatorio-completo.md` + `backend-audit.md` com análise de arquitetura/segurança.
  <span class="tag">docs/audits/</span>
- [x] **`docs/UX/VISUAL-IDENTITY.md`** — design system coffee + blue (tokens, componentes, acessibilidade).
  <span class="tag">docs/UX/</span>
- [x] **`README.md`** — badges e visão geral.
  <span class="tag">README.md</span>
- [x] **`arquiture.drawio`** — diagrama de arquitetura sob `docs/architecture/`.
  <span class="tag">docs/architecture/arquiture.drawio</span>
- [x] **`.agents/AGENTS.md` + `IA_README.md`** — contexto de arquitetura para agentes.
  <span class="tag">.agents/</span>

### 📝 Pendências

- [ ] **ADR-001 autenticação (JWT + mTLS híbrido)** — decisão tomada, falta registrar o ADR escrito em `docs/architecture/adr/`.
  <span class="tag">🟡 Média · docs/architecture/adr/</span>
- [ ] **README principal por fazer** — atualmente é mínimo; documentar setup, profiles, endpoints e fluxo MCP.
  <span class="tag">🟢 Baixa · README.md</span>

---

## 🚀 Performance

<span class="badge badge-phase3">Otimizações</span>

### ✅ Concluído

- [x] **HTTP/2 + TLS** — `server.http2.enabled=true`.
  <span class="tag">application.properties:5</span>
- [x] **Pool Lettuce configurado** — `max-active=16`, `max-idle=8`, `min-idle=2`.
  <span class="tag">application.properties:74-78</span>
- [x] **Conexões Redis lazy** — clientes Lettuce baratos no construtor, conexões sob demanda cacheadas.
  <span class="tag">RedisClientProvider.java</span>

### 📝 Pendências

- [ ] **Cachear `GoogleNetHttpTransport`** — `newTrustedTransport()` é criado em toda chamada de `getCalendar()` (resource leak: threads/fds). Cachear o transporte e reutilizar.
  <span class="tag">🔥 Alta · external/GoogleCalendarClient.java:35</span>
- [ ] **Caching de consultas com Redis** — a instância `cache` já está declarada; usar para sessões/serviços quando necessário.
  <span class="tag">🟢 Baixa · infra</span>
- [ ] **`JwtTokenService` lê `@Value` field (KEY)** — mover para constructor injection (imutabilidade, testabilidade).
  <span class="tag">🟢 Baixa · JwtTokenService.java:21-22</span>

---

## 🧪 Testes

<span class="badge badge-phase1">Qualidade</span>

### ✅ Concluído

- [x] **392 testes passando** — `TestSuite` + unit (domínio/app/infra) + integração (auth/login/register/cookies) + security + MCP + Redis.
  <span class="tag">commit e4aa9c8 · src/test/java/com/quitto/server/</span>
- [x] **Testes unitários de domínio** — `UserTest`, `MachineTest`, `ExternalAccountTest`, `LinuxUserTest`, `GroupsTest`, `CookieDomainTest`.
  <span class="tag">unit/domain/</span>
- [x] **Testes de infraestrutura** — `JwtTokenServiceTest`, `TokenResolverTest`, `TokenResolverManagerTest`, `RedisAbstractionTest`, `CookieManagerAdapterTest`, `HttpCookieServiceTest`.
  <span class="tag">unit/infrastructure/</span>
- [x] **Testes de aplicação** — `UserAuthenticationServiceTest`, `AuthenticationControllerTest`.
  <span class="tag">unit/application/</span>
- [x] **Testes de integração** — `LoginIntegrationTest`, `RegisterIntegrationTest`, `AuthenticationIntegrationTest`, `CookieSystemIntegrationTest`.
  <span class="tag">integration/</span>
- [x] **Testes de segurança** — `SecurityTest` (rotas protegidas) e `McpToolTest`.
  <span class="tag">security/ · mcp/</span>
- [x] **CI roda testes** — job no pipeline com perfil `test` e Postgres.
  <span class="tag">ci-cd.yml</span>
- [x] **Profile `test` isolado** — `application-test.properties` com H2 + `test-data.sql`.
- [x] **Testcontainers + JaCoCo no build** — dependências `testcontainers`/`postgresql`/`junit-jupiter` (BOM 1.20.4) e plugin JaCoCo 0.8.15 (threshold 0.00, sem enforcement) no `pom.xml`.
  <span class="tag">pom.xml:185-200 · 236-277</span>
  <span class="tag">test/resources/</span>

### 📝 Pendências

- [ ] **Testes para o rate limiting** — `RateLimitFilter`/`Bucket4jRateLimiter` sem cobertura direta (testar 429 e policy).
  <span class="tag">🟡 Média · security/</span>
- [ ] **Testes para `OAuth2UserProvisioningService`** — sem teste do auto-provisionamento (criação com UUID, authority `ROLE_*`).
  <span class="tag">🟡 Média · services/OAuth/</span>

---

## ♻️ Refatoração

<span class="badge badge-phase2">Qualidade de código</span>

### ✅ Concluído

- [x] **Constructor injection generalizada** — `SecurityConfig`, `MachineRepositoryAdapter`, `OAuth2UserProvisioningService`, `CookieManagerAdapter`, `HttpCookieWriterManeger` etc.
  <span class="tag">commits 0ab7bd6 · 899ab83</span>
- [x] **Bean duplicado `TokenService` removido** — `JwtTokenAdapter` deixou de ser `@Component`.
  <span class="tag">commit 0ab7bd6</span>
- [x] **Imports mortos removidos** — limpeza em `dashboard.js`, `mcp.js`, CSS e classes Java.
  <span class="tag">commits e4aa9c8 · 94456b9</span>
- [x] **`Optional` no lugar de `null`** — `extractIdSubject`, resolvers, repositories.
  <span class="tag">domain + infra</span>

### 📝 Pendências

- [ ] **Field injection em `GoogleCalendarService` e `GoogleCalendarTools`** — `@Autowired` em campos públicos; trocar para constructor injection.
  <span class="tag">🔥 Alta · mcp/services + mcp/tools</span>
- [ ] **`System.out/err` → SLF4J** — `CalendarController`, `GoogleCalendarTools` (ver Observabilidade).
  <span class="tag">🟡 Média · mcp/</span>
- [ ] **`RateLimit` interface na camada certa** — hoje em `infrastructure/interfaces/Ratelimit/`; como contrato de negócio deveria viver no domínio (`domain/interfaces/`).
  <span class="tag">🟢 Baixa · infra interfaces</span>

---

## 🤖 MCP

<span class="badge badge-phase1">Agentes de IA</span>

### ✅ Concluído

- [x] **`GoogleCalendarTools` com `@Component`** — `@Tool listEvents()` descoberto pelo Spring AI.
  <span class="tag">mcp/tools/GoogleCalendarTools.java:18</span>
- [x] **MCP server exposto em `/mcp`** — WebMVC transport, `ROLE_MCP` protegida.
  <span class="tag">application.properties:56-59 · SecurityConfig.java:50</span>
- [x] **`listEvents` funcional** — via `GoogleCalendarClient` + `GoogleAuthService`.
  <span class="tag">GoogleCalendarService.java:23-33</span>

### 📝 Pendências

- [ ] **`GoogleCalendarService.createEvent()` é stub** — retorna `""`. Implementar chamada real à API e expor `@Tool` para criar eventos.
  <span class="tag">🔥 Alta · GoogleCalendarService.java:19-21</span>
- [ ] **MCP tools de máquinas** — status e Wake-on-LAN via `@Tool` (precisa de WoL implementado no backend).
  <span class="tag">🟡 Média · mcp/tools/</span>
- [ ] **MCP tool de health check do servidor** — expor `/api/health` como tool.
  <span class="tag">🟢 Baixa · mcp/tools/</span>

---

## 📦 Modularização Maven

<span class="badge badge-phase2">Evolução para multi-module</span>

### ✅ Concluído

- [x] **Domínio 100% puro** — `domain/` sem imports de Spring/Jakarta/Lettuce; extraível como JAR.
  <span class="tag">domain/</span>

### 📝 Pendências

- [ ] **Estrutura multi-module** — `server-domain` (puro), `server-application`, `server-infrastructure`, `server-mcp`, `server-boot` (executável). Definir ADR-003.
  <span class="tag">🔥 Alta · ADR-003</span>
- [ ] **`server-mcp` como módulo independente** — agentes consumirem só o JAR do MCP sem subir o server todo. Definir ADR-005.
  <span class="tag">🟡 Média · ADR-005</span>
- [ ] **Publicar `server-domain` no Maven Local/GitHub Packages** — consumível por CLI (Rust/Python) e PS3.
  <span class="tag">🟢 Baixa · packaging</span>

---

## 🚀 Melhorias Futuras

<span class="badge badge-phase3">Backlog</span>

### ✅ Concluído (parte do escopo)

- [x] **Seed de usuários** — `admin_teste` (ADMIN) e `t` (USER) no H2/test.
  <span class="tag">data-h2.sql · test-data.sql</span>
- [x] **Logout** — sessão revogável via cookie.
  <span class="tag">AuthenticationController.java</span>
- [x] **Certificados públicos no repo** — `chore(certs)` para TLS.
  <span class="tag">commit a4f7840</span>

### 📝 Pendências

- [ ] **OAuth2 GitHub** — `Provider.GITHUB` já existe; adicionar registration e provisionamento genérico.
  <span class="tag">🟡 Média · application.properties</span>
- [ ] **Jobs assíncronos (Fase 7)** — model `Job` (BACKUP/RESTORE/SYNC), execução `@Async`, `POST /api/jobs` → 202 + polling.
  <span class="tag">🟡 Média · domain + application</span>
- [ ] **Backup controllers via Coffee-SDK** — ler `/mnt/mount/data/backups`, listar/criar/restaurar, agendar.
  <span class="tag">🟢 Baixa · domain</span>
- [ ] **UnixUserService (Linux)** — parsear `/etc/passwd` + `/etc/group`, sincronizar com o banco, vincular `User` ↔ `LinuxUser`.
  <span class="tag">🟢 Baixa · application</span>
- [ ] **API Key resolver** — novo `TokenResolver` para `X-API-Key` (extensível via chain).
  <span class="tag">🟢 Baixa · infra/security/Filter/Token/</span>

---

## 📋 Decisões Arquiteturais a Revisar

> Cada decisão deve ser registrada em `docs/architecture/adr/` após definição.

| # | Decisão | Status | Impacto |
|---|---------|--------|---------|
| ADR-001 | **Estratégia de autenticação: JWT + mTLS híbrido vs JWT only vs mTLS only** | ✅ **Decidido: JWT + mTLS híbrido** (falta escrever o ADR) | SecurityConfig, FilterChain, SDK clients |
| ADR-002 | **Versionamento de API: URL path (`/api/v1/`) vs Header vs Query** | 🔴 Pendente | Todos Controllers, SDK clients |
| ADR-003 | **Modularização Maven: multi-module vs single-module** | 🔴 Pendente | Build, deploy, SDK extraction |
| ADR-004 | **mTLS para homelab: CA própria (step-ca) vs certs auto-assinados vs managed** | 🔴 Pendente | Infra, cert rotation |
| ADR-005 | **MCP como módulo separado vs integrado no server** | 🔴 Pendente | Deploy, agent consumption |

---

<blockquote>
<strong>📅 Gerado em:</strong> 2026-08-03 · <strong>Baseado em auditoria completa do código-fonte</strong> (docs, backend, frontend, infra, testes, CI).<br>
<strong>🎯 Meta:</strong> Fechar os 🔥 Alta (OAuth2 via porta de domínio, CORS, persistência LinuxUser/ExternalAccount, `createEvent`, `@PreDestroy` Redis, transporte Google cacheado, versionamento de API, multi-module) antes de escalar features novas.<br>
<strong>✅ Desde o último audit:</strong> CookieSystem no domínio, typos corrigidos, `Optional` em `extractIdSubject`, 401 em falha de auth, `setOwner` implementado, rate limiting ativo, abstração Redis completa (14/14), TLS + HTTP/2, dashboard liquid-glass em `/app`, CI/CD com 392 testes.<br>
<strong>🧭 Frontend:</strong> backlog detalhado vive em <code>src/main/resources/static/app/TODO.md</code>.
</blockquote>

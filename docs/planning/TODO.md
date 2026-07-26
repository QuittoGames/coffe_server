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

  .progress-fill.fire { background: linear-gradient(90deg, #EF4444, #F97316); width: 0%; }
  .progress-fill.high { background: linear-gradient(90deg, #F97316, #EAB308); width: 0%; }
  .progress-fill.med { background: linear-gradient(90deg, #EAB308, #22C55E); width: 0%; }
  .progress-fill.low { background: linear-gradient(90deg, #22C55E, #3B82F6); width: 0%; }
</style>

# coffe_server — TODO

<blockquote>
<strong>Readiness:</strong> 🟡 5/10 · <strong>Baseado em audit completo</strong> — architecture, exception-handling, security. <br>
Sprint de preparação antes de implementar features reais: backup, Unix profile, WOL, Tailscale.
</blockquote>

---

## 🔥 Fase 1 — Bloqueantes

<span class="badge badge-phase1">Fazer AGORA</span>
<span class="badge badge-fire">15 itens</span>

<div class="progress-bar"><div class="progress-fill fire" style="width: 0%"></div></div>

Esses problemas impedem qualquer feature nova. Cada item resolvido aqui evita que a dívida técnica se propague.

### Arquitetura (Domínio)

- [ ] **Mover `CookieService` para o domínio** — `toFrameworkCookie()` vaza `jakarta.servlet.http.Cookie` na porta do domínio. Criar `CookieFrameworkConverter` na infraestrutura.
  <span class="tag">domain/interfaces/Cookies/CookieService.java</span>

- [ ] **`AuthenticationController` depender da interface `CookieManager`, não da implementação `CookieManagerAdapter`** — adicionar `toFrameworkCookie()` e `writeCookie()` na interface.
  <span class="tag">AuthenticationController.java / CookieManager.java</span>

- [ ] **`OAuth2UserProvisioningService` usar `UserRepository` (porta do domínio) ao invés de `JpaUserRepository`** — bypassa toda a camada de domínio.
  <span class="tag">OAuth2UserProvisioningService.java</span>

### Stubs Perigosos

- [ ] **`MachineRepositoryAdapter.setOwner()` retorna `new User()` vazio** — implementar de verdade ou lançar `UnsupportedOperationException`.
  <span class="tag">MachineRepositoryAdapter.java:85-87</span>

- [ ] **`MachineEntity` construtor ignora `userId`** — o parâmetro nunca é mapeado pro campo `@ManyToOne user`, causa violação de constraint NOT NULL.
  <span class="tag">MachineEntity.java:42-44, 62-65</span>

### Segurança

- [ ] **Authority do OAuth2 com espaço** — `"ROLE_ "` produz `"ROLE_ USER"` em vez de `"ROLE_USER"`. Nenhum usuário OAuth2 funciona.
  <span class="tag">OAuth2UserProvisioningService.java:41</span>

- [ ] **`User.toString()` vaza `passwordHash`** — BCrypt hash exposto em logs.
  <span class="tag">User.java:97-100</span>

- [ ] **`CalendarController` com `@PreAuthorize("permitAll()")`** — endpoints expostos. Remover ou proteger por endpoint.
  <span class="tag">CalendarController.java:18</span>

- [ ] **Adicionar rate limiting em `/auth/login` e `/auth/register`** — sem proteção contra brute force.
  <span class="tag">AuthenticationController.java</span>

### Autenticação

- [ ] **`JwtAuthenticationFilter` sempre chama `doFilter()` mesmo após falha** — retornar 401 e interromper o chain.
  <span class="tag">JwtAuthenticationFilter.java:64-77</span>

- [ ] **`JwtTokenService.extractIdSubject()` pode lançar NPE** — `getSubject()` retorna null, `isBlank()` quebra.
  <span class="tag">JwtTokenService.java:67</span>

- [ ] **`JwtTokenService.verifyToken()` pode lançar NPE** — mesmo problema do `getSubject()`.
  <span class="tag">JwtTokenService.java:49</span>

### Persistência

- [ ] **`LinuxUser` e `Groups` sem infraestrutura JPA** — mappers, adapters, repositories tudo vazio.
  <span class="tag">infrastructure/db/LinuxUser/</span>

- [ ] **`ExternalAccount` sem mapper, adapter, repository** — Entity existe, resto falta.
  <span class="tag">infrastructure/db/ExternalAccount/</span>

- [ ] **`GoogleCalendarClient.getCalendar()` cria HTTP transport em toda chamada** — resource leak (threads, file descriptors).
  <span class="tag">GoogleCalendarClient.java:35</span>

---

## 🟡 Fase 2 — Importantes

<span class="badge badge-phase2">Fazer junto com features</span>
<span class="badge badge-high">12 itens</span>

<div class="progress-bar"><div class="progress-fill high" style="width: 0%"></div></div>

### Refatorações

- [ ] **Mover `TokenResolverManager` para `application/services/Auth/Token/`** — é orquestrador (use case), não infraestrutura.

- [ ] **Converter field injection para constructor injection**:
  - `UserRepositoryAdapter.java`
  - `GoogleCalendarService.java`
  - `GoogleCalendarTools.java`

- [ ] **Mover `MachineNotFoundException` de `shared/exception/` para `domain/exception/`**

### Exception Handling

- [ ] **Criar `UserNotFoundException` no domínio** — substituir `UsernameNotFoundException` (Spring Security) no `MachineService`.

- [ ] **Adicionar `@ExceptionHandler` no `AuthExceptionHandler`** — para `IllegalArgumentException`, `MachineNotFoundException`, `JWTVerificationException`.

- [ ] **Remover catch genérico `Exception` no `GoogleCalendarTools` e `CalendarController`** — erros silenciados retornam lista vazia. Propagar ou retornar erro estruturado.

### User Service

- [ ] **Implementar `UserService` com CRUD básico** — só tem construtor vazio.

### Segurança

- [ ] **Adicionar CORS configuration** — bean `CorsConfigurationSource` com origins explícitas.
  <span class="tag">SecurityConfig.java</span>

- [ ] **Adicionar validação de senha** — min 8 chars, maiúscula, minúscula, número.
  <span class="tag">AuthenticationController.java:61-63</span>

- [ ] **Adicionar SameSite=Strict nos cookies** — campo novo no `CookieDomain` record.
  <span class="tag">CookieDomain.java / HttpCookieService.java</span>

### Observability

- [ ] **Criar endpoint de health check com status do banco** — além do Actuator básico.

- [ ] **Adicionar audit logging** — eventos de login, criação de usuário, operações sensíveis.

---

## 🟢 Fase 3 — Features

<span class="badge badge-phase3">Próximos passos</span>
<span class="badge badge-med">4 features</span>

<div class="progress-bar"><div class="progress-fill med" style="width: 0%"></div></div>

### Backup Control

- [ ] Definir modelo de domínio (`Backup`, `BackupSchedule`, `BackupStatus`)
- [ ] Criar repositório (porta no domínio)
- [ ] Implementar adapter (compressão, SCP, storage local/S3)
- [ ] Adicionar dependências: compressão, SCP/S3 SDK no `pom.xml`
- [ ] Criar service + controller + MCP tools

### Unix Profile Reader

- [ ] Completar infraestrutura de `LinuxUser` e `Groups` (Fase 1)
- [ ] Implementar leitura real via SSH (JSch ou Apache SSHD)
- [ ] Adicionar dependência SSH no `pom.xml`
- [ ] Criar service para sincronizar Linux users com banco

### Wake-on-LAN

- [ ] Implementar envio de magic packet (UDP raw)
- [ ] Criar endpoint `POST /api/machines/:id/wake`
- [ ] Adicionar dependência JOLP ou raw UDP no `pom.xml`

### Tailscale Integration

- [ ] Implementar cliente API Tailscale
- [ ] Sincronizar status das máquinas

---

## 📊 Progresso Geral

<div class="section-summary">
  <div class="stat-card">
    <strong>0</strong> / 15<br><span class="tag">🔥 Fase 1</span>
  </div>
  <div class="stat-card">
    <strong>0</strong> / 12<br><span class="tag badge-high">Fase 2</span>
  </div>
  <div class="stat-card">
    <strong>0</strong> / 4<br><span class="tag badge-med">Fase 3</span>
  </div>
  <div class="stat-card">
    <strong>0</strong> / 31<br><span class="tag">Total</span>
  </div>
</div>

<hr>

<blockquote>
<strong>📅 Gerado em:</strong> 2026-07-26 · <strong>Auditado por:</strong> architecture-analyzer, exception-analyzer, security-explore, security-tester, codebase-explainer<br>
<strong>🎯 Meta:</strong> Completar Fase 1 antes de começar qualquer feature nova.
</blockquote>

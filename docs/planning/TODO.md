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
<strong>Readiness:</strong> 🟡 6/10 · <strong>Revisão completa em 2026-08-01</strong> — architecture, security, code quality. <br>
Autenticação é <strong>cookie-only</strong>: o JWT não vem mais no body de login/register, só no HttpOnly cookie. <br>
Rate limiting distribuído ativo na security chain (Bucket4j + Redis) com conexões lazy. <br>
Abstração Redis ampliada (DatabaseProperties + getAdpterConnector) com properties <code>coffee.redis.*</code>. <br>
</blockquote>

---

## 🔥 Fase 1 — Bloqueantes (PRIORIDADE MÁXIMA)

<span class="badge badge-phase1">Fazer AGORA</span>
<span class="badge badge-fire">8 itens</span>

<div class="progress-bar"><div class="progress-fill fire" style="width: 63%"></div></div>

Esses bugs **impedem features novas** ou causam falhas em produção. Cada item resolvido aqui desbloqueia o resto.

### 🐛 Bugs Críticos

- [x] **`MachineEntity` construtor ignora `userId`** — O parâmetro `userId` nunca é atribuído ao campo `@ManyToOne user`. `MachineMapper.toInfra()` passa `machine.getUserId()` mas o valor é silenciosamente descartado. Causa `ConstraintViolationException` (NOT NULL).
  <span class="tag">MachineEntity.java:62-65</span>

- [x] **`MachineRepositoryAdapter.setOwner()` retorna `new User()` vazio** — Perde todos os dados do owner. Método na interface `MachineRepository` deveria ser removido ou implementado corretamente.
  <span class="tag">MachineRepositoryAdapter.java:85-87</span>

- [ ] **OAuth2 authority com espaço** — `"ROLE_ "` produz `"ROLE_ USER"` em vez de `"ROLE_USER"`. Nenhum usuário OAuth2 consegue acessar rotas protegidas.
  <span class="tag">OAuth2UserProvisioningService.java:41</span>

- [ ] **OAuth2UserProvisioningService usa `JpaUserRepository` direto** — Bypassa toda a camada de domínio. Deveria usar `UserRepository` (porta do domínio).
  <span class="tag">OAuth2UserProvisioningService.java:20-23</span>

- [ ] **OAuth2 cria usuário sem passwordHash** — `UserEntity.password_hash` é `nullable = false`. Novo usuário OAuth2 criado via `new UserEntity()` sem `setPasswordHash()` causa `ConstraintViolationException`.
  <span class="tag">OAuth2UserProvisioningService.java:35-38</span>

- [x] **`JwtAuthenticationFilter` não retorna 401 em caso de falha** — `filterChain.doFilter()` é chamado incondicionalmente mesmo quando a autenticação falha. A requisição prossegue sem autenticação em vez de retornar 401.
  <span class="tag">JwtAuthenticationFilter.java:77</span>

- [x] **`User.toString()` vaza `passwordHash`** — BCrypt hash exposto em logs e mensagens de debug.
  <span class="tag">User.java:97-99</span>

### 🔴 Segurança Imediata

- [x] **Adicionar rate limiting em `/auth/login` e `/auth/register`** — RateLimitFilter na security chain com Bucket4j distribuído via Redis; conexão Redis criada de forma lazy (boot não depende do Redis no ar).
  <span class="tag">RateLimitFilter.java · Bucket4jRateLimiter.java</span>

---

## 🟡 Fase 2 — Refatorações e Correções

<span class="badge badge-phase2">Fazer junto com features</span>
<span class="badge badge-high">16 itens</span>

<div class="progress-bar"><div class="progress-fill high" style="width: 0%"></div></div>

### Clean Architecture

- [ ] **Mover `TokenResolverManager` para `application/services/Auth/Token/`** — É orquestrador (use case), não infraestrutura.
  <span class="tag">infrastructure/services/Auth/Token/TokenResolverManager.java</span>

- [ ] **`MachineService` lança `UsernameNotFoundException` (Spring)** — Vazamento de framework na camada de aplicação. Criar `UserNotFoundException` no domínio.
  <span class="tag">MachineService.java:28</span>

- [x] **Mover `MachineNotFoundException` de `shared/exception/` para `domain/exception/`**
  <span class="tag">shared/exception/MachineNotFoundException.java</span>

### Injection

- [ ] **Converter field injection para constructor injection**:
  - `UserRepositoryAdapter.java` — `@Autowired JpaUserRepository`
  - `GoogleCalendarService.java` — `@Autowired GoogleCalendarClient`
  - `GoogleCalendarTools.java` — `@Autowired GoogleCalendarService`, `@Autowired GoogleAuthService`

### Exception Handling

- [ ] **Adicionar `@ExceptionHandler` no `AuthExceptionHandler`** — Para `IllegalArgumentException`, `MachineNotFoundException`, `JWTVerificationException`, `InvalidTokenException`.
  <span class="tag">AuthExceptionHandler.java</span>

- [ ] **Remover catch genérico `Exception` e `System.out/err`** — Em `GoogleCalendarTools` e `CalendarController`. Substituir por SLF4J + erro estruturado.
  <span class="tag">GoogleCalendarTools.java / CalendarController.java</span>

### Service Gaps

- [ ] **Implementar `UserService` com CRUD básico** — Só tem construtor vazio.
  <span class="tag">UserService.java</span>

- [ ] **`GoogleCalendarService.createEvent()` implementar de verdade** — Retorna `""` vazio (stub).
  <span class="tag">GoogleCalendarService.java:19-21</span>

### Persistência Incompleta

- [ ] **Completar `LinuxUser` e `Groups` persistence** — Entities existem, faltam: Spring Data repos, mappers, adapters, domain repository interfaces.
  <span class="tag">infrastructure/db/LinuxUser/</span>

- [ ] **Completar `ExternalAccount` persistence** — Entity existe, faltam: Spring Data repo, mapper, adapter, domain repository interface.
  <span class="tag">infrastructure/db/User/Entity/ExternalAccountEntity.java</span>

### Segurança

- [ ] **Adicionar CORS configuration** — Bean `CorsConfigurationSource` com origins explícitas.
  <span class="tag">SecurityConfig.java</span>

- [ ] **Adicionar validação de senha** — Min 8 chars, maiúscula, minúscula, número.
  <span class="tag">AuthenticationController.java:62</span>

- [ ] **Adicionar SameSite=Strict nos cookies** — Atualmente `SameSite("Lax")`. Mudar para `Strict` e adicionar campo opcional no `CookieDomain`.
  <span class="tag">CookieMapper.java:18 / CookieDomain.java</span>

- [ ] **`CalendarController` proteger endpoints** — Remover `@PreAuthorize("permitAll()")` e proteger por endpoint ou role.
  <span class="tag">CalendarController.java:18</span>

### Resource Leaks

- [ ] **`GoogleCalendarClient.getCalendar()` cachear HTTP transport** — Novo `GoogleNetHttpTransport.newTrustedTransport()` criado em toda chamada (resource leak: threads, file descriptors).
  <span class="tag">GoogleCalendarClient.java:34-35</span>

---

## 🟠 Fase 3 — Próximas Features

<span class="badge badge-phase3">Próximos passos</span>
<span class="badge badge-med">7 features</span>

<div class="progress-bar"><div class="progress-fill med" style="width: 0%"></div></div>

### Rate Limiting

- [ ] Adicionar dependência `spring-boot-starter-webmvc-test` (já existe) + configurar bucket4j ou resilience4j
- [ ] Criar filter/ interceptor para rate limiting em `/auth/login` e `/auth/register`
- [ ] Configurar limites: N tentativas por minuto por IP
- [ ] Retornar `429 Too Many Requests` com headers de rate limit

### MCP Services

- [ ] Completar `GoogleCalendarService.createEvent()` — implementar chamada real à API
- [ ] Adicionar `@Tool` para criar eventos no Google Calendar
- [ ] Adicionar MCP tools para gerenciamento de máquinas (status, WOL)
- [ ] Adicionar MCP tool para health check do servidor
- [ ] Garantir que MCP tools sejam corretamente descobertas pelo Spring AI (`@Component` já adicionado)

### OAuth2 External

- [ ] Adicionar suporte a GitHub OAuth2 (Provider enum já existe)
- [ ] Criar `OAuth2UserProvisioningService` genérico (não só Google)
- [ ] Corrigir `OAuth2UserProvisioningService` para usar `UserRepository` (porta do domínio)
- [ ] Vincular `ExternalAccount` ao usuário no banco durante OAuth2

### Coffee-SDK Connect

- [ ] Definir interface de conexão com SDK próprio (REST local ou Unix socket)
- [ ] Criar health client do SDK: `coffee server status` → CPU, RAM, Disk, Docker, Postgres, serviços
- [ ] Expor endpoints no servidor para o SDK consumir
- [ ] Documentar formato de resposta do SDK

### Frontend

- [ ] Construir dashboard administrativo com Thymeleaf (preservar paleta de cores CSS existente)
- [ ] Página de login funcional (já existe template)
- [ ] Página de gerenciamento de máquinas
- [ ] Página de visualização de backups
- [ ] Design system: usar a paleta coffee + blue já definida no CSS

### Backup Controllers

- [ ] Usar Coffee-SDK para ler diretório `/mnt/mount/data/backups` (produção)
- [ ] Listar backups disponíveis
- [ ] Criar backup (via SDK ou script)
- [ ] Restaurar backup
- [ ] Agendar backups recorrentes
- [ ] Domain model: `Backup`, `BackupSchedule`, `BackupStatus`

### UnixUserService

- [ ] Usar Coffee-SDK para parsear `/etc/passwd` e `/etc/group` do servidor
- [ ] Sincronizar usuários e grupos Unix com o banco
- [ ] Vincular `User` do sistema com `LinuxUser`
- [ ] Interface de domínio: `LinuxUserRepository`
- [ ] Completar infraestrutura JPA de `LinuxUser` e `Groups`

---

## 🔵 Fase 4 — Observabilidade

<span class="badge badge-phase3" style="background:#1a2a4a; color:#60A5FA; border-color: #3B82F6;">Nova</span>
<span class="badge badge-med">5 itens</span>

<div class="progress-bar"><div class="progress-fill med" style="width: 0%"></div></div>

### Logs Estruturados

- [ ] Adicionar MDC (Mapped Diagnostic Context) com request ID, user ID, session ID em cada requisição
- [ ] Substituir `System.out.println` e `System.err.println` por SLF4J em todo o codebase
- [ ] Configurar logback-spring.xml com formato estruturado (JSON ou padrão coffee)

### Métricas

- [ ] Coletar métricas de uso dos endpoints (Spring Actuator + Micrometer)
- [ ] Métricas de autenticação: tentativas de login, sucessos, falhas
- [ ] Métricas de performance: latency dos endpoints, taxa de erro

### Health Checks

- [ ] Criar endpoint `/api/health` com status detalhado:
  - Banco de dados (PostgreSQL / H2)
  - Google Calendar API
  - Disk usage (espaço em /mnt/mount/data/backups)
  - Docker (se aplicável)
  - Tailscale (se configurado)
- [ ] Configurar grupos de health check no Actuator

### Diagnóstico

- [ ] Endpoint `/api/status` com informações do servidor:
  - Uptime
  - Versão do server
  - Perfil ativo (dev/prod)
  - Dependências externas (UP/DOWN)
- [ ] Endpoint `/api/debug/auth` (já existe em CalendarController) — mover para rota apropriada

### Audit Logging

- [ ] Logar eventos importantes com estrutura padronizada:
  - Login (sucesso/falha)
  - Registro de usuário
  - Criação de máquina
  - Alteração de owner
  - Operações de backup
  - Ações MCP
- [ ] Incluir em cada evento: timestamp, user ID, ação, target, resultado, IP

---

## 🟣 Fase 5 — Sistema de Permissões

<span class="badge badge-phase3" style="background:#2a1a3a; color:#C084FC; border-color: #A855F7;">Nova</span>
<span class="badge badge-med">4 itens</span>

<div class="progress-bar"><div class="progress-fill med" style="width: 0%"></div></div>

### Roles

- [ ] Expandir `Role` enum: `ADMIN`, `DEVELOPER`, `USER`, `MACHINE`, `API`, `MCP`
- [ ] Cada role com conjunto padrão de permissões

### Permissions (granulares)

- [ ] Modelar sistema de permissões no domínio:
  - `machine.read`
  - `machine.execute` (WOL, restart)
  - `service.restart`
  - `backup.create`
  - `backup.restore`
  - `user.manage`
- [ ] Tabela `permissions` no banco
- [ ] Relacionamento N:N entre roles e permissions

### Scopes

- [ ] Adicionar conceito de scope para tokens JWT
- [ ] Validação de scope no `JwtAuthenticationFilter`
- [ ] Endpoints expõem required scopes via annotation

### Integração

- [ ] `@PreAuthorize` com permisssões (ex: `hasPermission('machine.execute')`)
- [ ] Seeds no `data-h2.sql` com roles + permissions padrão
- [ ] Endpoint `/api/permissions` para consulta

---

## ⚫ Fase 6 — Auditoria

<span class="badge badge-phase3" style="background:#1a1a1a; color:#F9FAFB; border-color: #6B7280;">Nova</span>
<span class="badge badge-low">3 itens</span>

<div class="progress-bar"><div class="progress-fill low" style="width: 0%"></div></div>

### Audit Model

- [ ] Domain model `AuditLog`:
  - `id`, `userId`, `username`, `action`, `target`, `targetId`, `result` (SUCCESS/FAILURE), `details`, `ipAddress`, `timestamp`
- [ ] Interface `AuditRepository` no domínio

### Audit Infrastructure

- [ ] Entity JPA `AuditLogEntity`
- [ ] Mapper + Adapter + Spring Data Repository
- [ ] Serviço de auditoria (sync ou async)

### Audit Middleware

- [ ] Criar `@Auditable` annotation para marcar endpoints auditáveis
- [ ] Aspect/interceptor para log automático
- [ ] Endpoint `/api/audit/logs` (admin only)

---

## ⚪ Fase 7 — Jobs Assíncronos

<span class="badge badge-phase3" style="background:#1a2a1a; color:#4ADE80; border-color: #22C55E;">Nova</span>
<span class="badge badge-low">3 itens</span>

<div class="progress-bar"><div class="progress-fill low" style="width: 0%"></div></div>

### Job Model

- [ ] Domain model `Job`:
  - `id`, `type` (BACKUP, RESTORE, SYNC), `status` (PENDING, RUNNING, COMPLETED, FAILED), `progress` (0-100), `result`, `createdAt`, `completedAt`, `userId`
- [ ] Interface `JobRepository` no domínio

### Job Infrastructure

- [ ] Entity JPA + Mapper + Adapter + Repository
- [ ] Serviço de job com execução assíncrona (`@Async` ou `TaskExecutor`)
- [ ] Callback de progresso

### Job API

- [ ] `POST /api/jobs` — criar job (retorna `202 Accepted` + job ID)
- [ ] `GET /api/jobs/{id}` — status do job
- [ ] `GET /api/jobs` — listar jobs do usuário
- [ ] Exemplo: `POST /backup/create` → `202 { jobId: "82ad91" }` → `GET /jobs/82ad91` → `{ status: "running", progress: 65% }`

---

## 🟤 Fase 8 — Redis Abstraction (Ports & Adapters)

<span class="badge badge-phase3" style="background:#3C2415; color:#E8D5C4; border-color: #A67B5B;">Nova</span>
<span class="badge badge-med">14 itens</span>

<div class="progress-bar"><div class="progress-fill med" style="width: 93%"></div></div>

### ✅ Concluído

- [x] **`Connection.java` — Domain port enriquecida** — Adicionados métodos `isOpen()` e `close()` (antes era interface vazia).
  <span class="tag">domain/Database/Connection.java</span>

- [x] **`DatabaseClientProvider.java` — Import corrigido** — Trocado `java.sql.Connection` (JDBC) por `Connection` do domínio. Fix crítico que impedia compilação.
  <span class="tag">domain/interfaces/Database/DatabaseClientProvider.java</span>

- [x] **`RedisClientConnectionAdpter.java` — Adapter refatorado** — Agora recebe `StatefulRedisConnection` (não só `RedisAsyncCommands`), implementa `isOpen()` e `close()`.
  <span class="tag">infrastructure/Adpter/in/RedisClientConnectionAdpter.java</span>

- [x] **`RedisClientProvider.java` — Provider reescrito** — Implementa `get(String name)` corretamente, lê `RedisProperties`, cria conexões Lettuce, encapsula via adapter.
  <span class="tag">infrastructure/services/Provaider/redis/RedisClientProvider.java</span>

- [x] **`RedisClientConnection.java` removido — `RedisProperties` agora usa `RedisClientInstace` do domínio** — `getInstances()` retorna `List<RedisClientInstace>`. `RedisClientInstace` estende `DatabaseClient` (já tem name, host, port, enabled), eliminando duplicata de infra POJO.
  <span class="tag">RedisClientConnection.java removido · RedisProperties.java · RedisClientProvider.java</span>

- [x] **`StringByteArrayCodec.java` — Package corrigido** — Movido de `ratelimit` para `config.redis.Codec` e API ajustada para o `RedisCodec` real do Lettuce 6.8.2.
  <span class="tag">infrastructure/config/redis/Codec/StringByteArrayCodec.java</span>

- [x] **`Bucket4jConfig.java` — Rate limit config** — Bucket4j configurado com `LettuceBasedProxyManager`.
  <span class="tag">infrastructure/config/ratelimit/Bucket4jConfig.java</span>

- [x] **`RedisArryCodec.java` — Interface de codec** — Extends Lettuce `RedisCodec<String, byte[]>`.
  <span class="tag">infrastructure/interfaces/Codec/RedisArryCodec.java</span>

- [x] **`DatabaseProperties.java` — Interface de domínio para config de banco** — Define `getInstances()`; `RedisProperties` agora a implementa, desacoplando a porta de propriedades.
  <span class="tag">domain/Database/DatabaseProperties.java · RedisProperties.java</span>

- [x] **`DatabaseClientProvider` enriquecido** — Porta agora recebe `P extends DatabaseProperties` e expõe `getAdpterConnector(name)` + `getProvaiders(properties)`, substituindo `get(name)`.
  <span class="tag">domain/interfaces/Database/DatabaseClientProvider.java</span>

- [x] **`RedisClientProvider` — Conexões lazy** — Clientes Lettuce criados no construtor (baratos, sem rede); conexões reais sob demanda no primeiro uso e cacheadas em `ConcurrentHashMap`. Boot não depende do Redis no ar.
  <span class="tag">infrastructure/services/Provaider/redis/RedisClientProvider.java</span>

- [x] **`Bucket4jConfig` conectado à abstração** — Cria bean `RateLimit` (`Bucket4jRateLimiter`) via `RedisClientProvider` + `PolicyProvider`, com proxy manager lazy. `Bucket4jPolicyProvider` implementa a `PolicyProvider`.
  <span class="tag">Bucket4jConfig.java · Bucket4jPolicyProvider.java · Bucket4jRateLimiter.java</span>

- [x] **Properties `coffee.redis.*` e `coffee.ratelimit.enabled` adicionadas** — Instâncias `cache` e `rate-limit` declaradas no `application.properties`, `application-h2.properties` e `application-test.properties`.
  <span class="tag">application.properties · application-h2.properties · application-test.properties</span>

### 📝 Pendente

- [ ] **Registrar `@EnableConfigurationProperties(RedisProperties.class)** — Sem isso o Spring não faz o binding das properties. Atualmente `properties.getInstances()` retorna `null`.
  <span class="tag">RedisConfig.java / ServerApplication.java</span>

- [ ] **Configurar TLS/SSL nas conexões Redis** — Usar `rediss://` ou suporte a `RedisURI.Builder` com SSL.
  <span class="tag">RedisClientProvider.java</span>

- [ ] **Adicionar suporte a senha Redis** — Campo `password` em `RedisClientInstace` (domain model) e `RedisClientProvider`. Prover ao criar URI.
  <span class="tag">RedisClientInstace.java · RedisClientProvider.java</span>

- [ ] **@PreDestroy para fechar conexões** — `RedisClientProvider` precisa fechar todas as conexões no shutdown pra evitar vazamento de threads Netty.
  <span class="tag">RedisClientProvider.java</span>

---

## 🔒 Segurança — mTLS e Autenticação Unificada

<span class="badge badge-phase1" style="background:#8B0000; color:white; border-color:#FF4444;">Pesquisa + Decisão</span>
<span class="badge badge-fire">4 itens</span>

<div class="progress-bar"><div class="progress-fill fire" style="width: 0%"></div></div>

O feedback aponta que a autenticação atual (JWT + cookie + OAuth2 + ACL por driver) está confusa. A sugestão é simplificar para **um método único reforçado com mTLS**.

### Pesquisa Necessária

- [ ] **Pesquisar implementação de mTLS no Spring Boot 4.x** — Entender como configurar mutual TLS com certificados client/server, truststore/keystore, e integração com `SecurityFilterChain`.
  - Por que: mTLS elimina necessidade de JWT para autenticação mútua serviço-a-serviço; handshake TLS garante identidade de ambas as partes
  - Abordagem: `server.ssl.*` properties + `SecurityConfig` com `X509AuthenticationFilter` ou custom filter

- [ ] **Avaliar trade-offs: mTLS vs JWT para este cenário** — Comparar complexidade operacional (rotação de certs, CA privada), latência do handshake, suporte a clientes diversos (browser, CLI, agents MCP).
  - Por que: mTLS é excelente para service-to-service, mas browsers não suportam bem client certs; MCP agents podem ou não suportar
  - Decisão: provavelmente **híbrido** — mTLS para comunicação servidor↔SDK/CLI, JWT/OAuth2 para browsers e MCP

- [ ] **Definir estratégia de certificado para homelab** — CA própria (step-ca, smallstep, cfssl), certificados de curta duração (24h-7d), renovação automática via cert-manager ou script.
  - Por que: sem CA gerenciada, mTLS vira pesadelo operacional
  - Abordagem: `step-ca` + `step` CLI para emissão/renovação; distribuir certs via SDK

### Implementação (após decisão)

- [ ] **Criar `MtlsAuthenticationFilter` ou configurar `X509AuthenticationFilter`** — Extrair subject do certificado cliente, mapear para `User`/`Machine` do domínio, popular `SecurityContext`.
  - Dependência: decisão da pesquisa acima
  - Localização: `infrastructure/security/Filter/`

---

## 🌐 API — Versionamento e Contratos

<span class="badge badge-phase2">Planejamento</span>
<span class="badge badge-high">3 itens</span>

<div class="progress-bar"><div class="progress-fill high" style="width: 0%"></div></div>

### Estratégia de Versionamento

- [ ] **Definir estratégia de versionamento de API** — URL path (`/api/v1/...`), header (`Accept: application/vnd.coffe.v1+json`), ou query param. Documentar decisão no `docs/architecture/api-versioning.md`.
  - Por que: evita breaking changes futuros; clientes (SDK, MCP, Web) precisam saber como evoluir
  - Recomendação inicial: **URL path** (`/api/v1/`) — simples, visível, compatível com cache/CDN

- [ ] **Aplicar versionamento nos controllers existentes** — `AuthenticationController`, `APIController`, `CalendarController`, `MachineController` (futuro).
  - Exemplo: `@RequestMapping("/api/v1/auth")`
  - Manter `/api/test` como health check sem versão (ou `/api/v1/health`)

- [ ] **Criar DTOs versionados ou usar OpenAPI para contratos** — Separar `v1` DTOs de `v2` quando houver breaking change; gerar spec OpenAPI automaticamente (`springdoc-openapi`).
  - Por que: contratos claros facilitam geração de cliente (SDK Rust/Python, TypeScript)

---

## 🧱 Arquitetura — Modularização Maven (MCP + SDK)

<span class="badge badge-phase2">Pesquisa + Execução</span>
<span class="badge badge-high">5 itens</span>

<div class="progress-bar"><div class="progress-fill high" style="width: 0%"></div></div>

O feedback sugere avaliar módulos Maven separados para MCP e SDK. Isso alinha com a estratégia SDK documentada no relatório de arquitetura.

### Pesquisa

- [ ] **Pesquisar estrutura multi-module Maven para Spring Boot 4.x** — Best practices: `pom.xml` pai com `<packaging>pom</packaging>`, modules com dependências bem definidas, build order, profiles.
  - Referência: Spring Boot multi-module samples, Gradle vs Maven trade-offs

- [ ] **Avaliar separação: `coffe-mcp` vs `coffe-server`** — MCP tools (`GoogelCalenderTools`, `CalendarController`, `GoogleCalendarService`) podem virar módulo independente que o server importa? Vantagem: agents MCP podem usar só o JAR do MCP sem subir o server todo.

### Estrutura Proposta (após pesquisa)

- [ ] **Criar módulo `server-domain` (JAR puro)** — Domain models, interfaces (ports), value objects, enums, exceptions. **Zero dependências**.
  - Consumido por: `server-application`, `server-infrastructure`, `server-mcp`, `coffe-sdk` (externo)

- [ ] **Criar módulo `server-mcp` (JAR)** — MCP tools, services, controllers relacionados a MCP. Depende de `server-domain` + `spring-ai-mcp`.
  - Permite: agents consumirem `server-mcp` como library; server importa como dependency

- [ ] **Criar módulo `server-infrastructure` (JAR)** — JPA adapters, security (JWT, mTLS), external clients (Google, GitHub), Redis. Depende de `server-domain` + `server-application`.

- [ ] **Criar módulo `server-boot` (executável)** — `ServerApplication`, `application.properties`, templates, static. Depende de TODOS os módulos acima. É o artefato de deploy.

---

## 📋 Decisões Arquiteturais a Revisar

> Esta seção centraliza decisões de design que o feedback trouxe e que impactam múltiplas áreas. Cada decisão deve ser documentada em `docs/architecture/adr/` (Architecture Decision Records) após definição.

| # | Decisão | Status | Impacto | ADR Sugerido |
|---|---------|--------|---------|--------------|
| ADR-001 | **Estratégia de autenticação: JWT + mTLS híbrido vs JWT only vs mTLS only** | ✅ **Decidido: JWT + mTLS híbrido** | SecurityConfig, FilterChain, SDK clients, MCP agents | `adr-001-auth-strategy.md` |
| ADR-002 | **Versionamento de API: URL path (`/api/v1/`) vs Header vs Query** | 🔴 Pendente | Todos Controllers, OpenAPI, SDK clients | `adr-002-api-versioning.md` |
| ADR-003 | **Modularização Maven: multi-module vs single-module** | 🔴 Pendente | Build, deploy, dependency graph, SDK extraction | `adr-003-maven-modules.md` |
| ADR-004 | **mTLS para homelab: CA própria (step-ca) vs certs auto-assinados vs managed (Let's Encrypt + ACME)** | 🔴 Pendente | Infra, cert rotation, client provisioning | `adr-004-mtls-ca.md` |
| ADR-005 | **MCP como módulo separado vs integrado no server** | 🔴 Pendente | Deploy, agent consumption, versionamento independente | `adr-005-mcp-module.md` |

---

## 📊 Progresso Geral

<div class="section-summary">
  <div class="stat-card">
    <strong>5</strong> / 8<br><span class="tag">🔥 Fase 1</span>
  </div>
  <div class="stat-card">
    <strong>1</strong> / 16<br><span class="tag badge-high">Fase 2</span>
  </div>
  <div class="stat-card">
    <strong>0</strong> / 7<br><span class="tag badge-med">Fase 3</span>
  </div>
  <div class="stat-card">
    <strong>0</strong> / 5<br><span class="tag">Fase 4</span>
  </div>
  <div class="stat-card">
    <strong>0</strong> / 4<br><span class="tag">Fase 5</span>
  </div>
  <div class="stat-card">
    <strong>0</strong> / 3<br><span class="tag">Fase 6</span>
  </div>
  <div class="stat-card">
    <strong>0</strong> / 3<br><span class="tag">Fase 7</span>
  </div>
  <div class="stat-card">
    <strong>13</strong> / 14<br><span class="tag">Fase 8</span>
  </div>
  <div class="stat-card">
    <strong>19</strong> / 60<br><span class="tag">Total</span>
  </div>
</div>

<hr>

<blockquote>
<strong>📅 Gerado em:</strong> 2026-08-01 · <strong>Baseado em análise completa do código-fonte</strong><br>
<strong>🎯 Meta:</strong> Completar Fase 1 antes de começar qualquer feature nova. Bugs críticos de Machine, OAuth2 e JWT Filter impedem produção.<br>
<strong>✅ O que já foi feito desde o último audit:</strong> CookieSystem refatorado (CookieManager + CookieFactory no domínio), typos corrigidos (JwtTokenResolver, BCryptPasswordService, Provider, ExternalAccount, GoogleCalendarTools, etc.), JwtTokenService.extractIdSubject retorna Optional, verifyToken() safe. <br>
MachineEntity userId mapping corrigido, MachineRepositoryAdapter.setOwner() implementado, JwtAuthenticationFilter retorna 401 em falha, User.toString() sem vazar passwordHash. <br>
Autenticação cookie-only (JWT fora do body de login/register), rate limiting ativo na security chain (Bucket4j + Redis lazy), abstração Redis ampliada com DatabaseProperties + getAdpterConnector/getProvaiders, properties coffee.redis.* em todos os profiles, Bucket4jConfig conectado via RedisClientProvider + Bucket4jPolicyProvider.
</blockquote>

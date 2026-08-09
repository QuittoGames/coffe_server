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
    /* Tags longas (ex.: WIP com caminho de arquivo) quebram para a
       própria linha em vez de estourar ao lado do texto. */
    flex-wrap: wrap;
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
    background-color: var(--blue-secondary);
    border-color: var(--blue-secondary);
    /* Check desenhado via SVG data-URI — ::after/::before não renderizam
       de forma confiável em <input> (replaced element): ficava torto e
       descentralizado. Com background-image o check fica nítido no centro. */
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3E%3Cpath fill='none' stroke='white' stroke-width='2.4' stroke-linecap='round' stroke-linejoin='round' d='M3 8.5 L6.5 12 L13 4.5'/%3E%3C/svg%3E");
    background-size: 12px 12px;
    background-position: center;
    background-repeat: no-repeat;
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
<strong>Readiness:</strong> 🟢 8.0/10 · <strong>Revisão completa em 2026-08-03</strong> — arquitetura, segurança, qualidade de código e frontend. <br>
📅 <strong>Gerado em:</strong> 2026-08-06 · atualizado 2026-08-08 · <strong>173 tarefas</strong> · <strong>86 concluídas</strong> · <strong>85 pendentes</strong> · <strong>2 em andamento</strong> · <strong>13 erros do dia</strong> · <strong>cobertura: 49.7%</strong> <br>
Documento refatorado a partir do original (backup: <code>C:\Users\Quitto\AppData\Local\Temp\opencode\TODO.md.backup-20260806</code>) — reorganizado nas seções obrigatórias, sem perda de conteúdo.
</blockquote>
---

# 🚧 Em Andamento

> Acesso rápido às tarefas que estão sendo trabalhadas **agora** — features e tarefas menores reunidas em um único contexto.
> **Regra:** a tarefa em andamento **vive aqui** (movida da seção dona, nunca duplicada); ao concluir, volta para a seção dona com `[x]`. Novas WIP entram automaticamente no grupo correto.

## ⚡ Features

### Ecossistema IA Provider — correção dos endpoints

- [ ] **Correção dos endpoints de listagem de modelos** — pesquisa concluída: 11/37 providers com URL de listagem errada (ver `docs/research/model-listing-endpoints.md`); aplicar correções Nível 1-3 + secrets + exposição MCP.
  <span class="tag">🚧 Em andamento · infrastructure/IA/ · volta p/ 🖥️ Backend</span>
  - [ ] **Nível 1 — só mudar `getApiBaseURL()` (1 linha cada):** Together `.xyz`→`.ai` · Cohere +`/v1` · Perplexity +`/v1`
  - [ ] **Nível 2 — sobrescrever `modelsUrl()` (path custom):** DeepInfra `/models/list` · Novita `/openai/v1` · Ollama `/api/tags` · Cloudflare `/ai/models/search` · Fireworks `/v1/accounts/{id}/models` · Azure `api-version` obrigatória
  - [ ] **Nível 3 — sobrescrever `fetchModelsFromApi()` com SDK/assinatura:** Vertex (parent `publishers` + OAuth2) · watsonx (`version` query + IAM) · OCI (`compartmentId` + signing) · Bedrock (SigV4 via AWS SDK)
  - [ ] **Transversal:** paginação (`pageSize`/`pageToken`/`nextPageToken`) nos providers com listas longas (OpenAI, Cohere, Google AI Studio, Fireworks, OCI)
  - [ ] **Secrets reais:** implementar `CoffeAgentService.getEnvKey()` lendo env vars (hoje retorna placeholder `"key_temp"`)
  - [x] **Expor `ProvaiderIAService.getModels()`** como API
  - [ ] Regressão: `./mvnw test` verde após as correções

### CoffeAgent WebSocket adapter

- [ ] **CoffeAgent WebSocket adapter** — adapter da comunicação WebSocket do coffe-agent (processo que roda no Linux do servidor) com o Spring; base para o agente local (Jarvis) consumir o server em tempo real.
  <span class="tag">🚧 Em andamento · infrastructure/services/CoffeAgent/ · volta p/ 🖥️ Backend</span>
  - [ ] Definir protocolo de mensagens (JSON) entre coffe-agent ↔ server
  - [ ] Criar WebSocket handler + endpoint no Spring (ex.: `/ws/agent`)
  - [ ] Registrar rota na security chain (acesso do agente local)
  - [ ] Testar handshake + envio/recepção de mensagens (integração)
  - [ ] Documentar fluxo no `docs/architecture/` ao concluir

## 🔧 Tarefas menores

> Quick wins — viva no 🧾 [Daily Board](#daily-errors): resolva do simples para o complexo, marque `[x]` no board **e** na seção dona.

- [ ] **`JwtTokenService` `@Value` → constructor injection** — imutabilidade e testabilidade do serviço de token.
  <span class="tag">🟢 Baixa · 🚀 Performance · Nível 1</span>
- [ ] **`System.out/err` → SLF4J** em `CalendarController` e `GoogleCalendarTools`.
  <span class="tag">🟡 Média · 🔌 APIs · ♻️ Refatoração · Nível 1</span>
- [ ] **Log do `RateLimitFilter` com placeholder literal** — trocar `"RateLimit begin {}"` por SLF4J real; diferenciar política por rota (LOGIN/REGISTER/API).
  <span class="tag">🟡 Média · 🔒 Segurança · Nível 1</span>
- [ ] **README principal mínimo** — setup, profiles, endpoints, fluxo MCP.
  <span class="tag">🟢 Baixa · 📚 Documentação · Nível 1</span>

> ⚠️ Estas 4 tasks **vivem no board** (🧾 Daily Errors · Nível 1) e **na seção dona** — duplicadas aqui **apenas como atalho** do daily. Marque `[x]` nos três lugares para fechar.

---

# Progresso Geral

<div class="section-summary">
  <div class="stat-card"><strong>8</strong> / 10<br><span class="tag">🧱 Arquitetura</span></div>
  <div class="stat-card"><strong>10</strong> / 18<br><span class="tag">🖥️ Backend</span></div>
  <div class="stat-card"><strong>7</strong> / 8<br><span class="tag">🎨 Frontend</span></div>
  <div class="stat-card"><strong>7</strong> / 12<br><span class="tag">🔒 Segurança</span></div>
  <div class="stat-card"><strong>3</strong> / 6<br><span class="tag">🔌 APIs</span></div>
  <div class="stat-card"><strong>5</strong> / 8<br><span class="tag">🗄️ Banco de Dados</span></div>
  <div class="stat-card"><strong>6</strong> / 8<br><span class="tag">⚙️ Infraestrutura</span></div>
  <div class="stat-card"><strong>4</strong> / 8<br><span class="tag">📈 Observabilidade</span></div>
  <div class="stat-card"><strong>8</strong> / 8<br><span class="tag">📚 Documentação</span></div>
  <div class="stat-card"><strong>3</strong> / 6<br><span class="tag">🚀 Performance</span></div>
  <div class="stat-card"><strong>9</strong> / 10<br><span class="tag">🧪 Testes</span></div>
  <div class="stat-card"><strong>4</strong> / 7<br><span class="tag">♻️ Refatoração</span></div>
  <div class="stat-card"><strong>3</strong> / 5<br><span class="tag">🤖 MCP</span></div>
  <div class="stat-card"><strong>1</strong> / 5<br><span class="tag">📦 Modularização</span></div>
  <div class="stat-card"><strong>5</strong> / 8<br><span class="tag">🚀 Melhorias Futuras</span></div>
</div>

> Status: `✅ Concluído` · `🚧 Em andamento` · `📝 TODO` · `⛔ Bloqueado`
> Prioridade: `🔥 Alta` · `🟡 Média` · `🟢 Baixa`

---

## 📊 Progresso por Categoria

<div class="section-summary">
  <div class="stat-card"><strong>153</strong> tarefas<br><span class="tag">total</span></div>
  <div class="stat-card"><strong>86</strong> concluídas<br><span class="tag">✅ done</span></div>
  <div class="stat-card"><strong>65</strong> pendentes<br><span class="tag">📝 todo</span></div>
  <div class="stat-card"><strong>2</strong> em andamento<br><span class="tag">🚧 wip</span></div>
  <div class="stat-card"><strong>13</strong> erros do dia<br><span class="tag">🧾 daily</span></div>
  <div class="stat-card"><strong>49.7%</strong> cobertura<br><span class="tag">done / total</span></div>
</div>

| Categoria | Progresso |
|---|---|
| 🧱 Arquitetura | <div class="progress-bar"><div class="progress-fill low" style="width:80%"></div></div> 8/10 |
| 🖥️ Backend | <div class="progress-bar"><div class="progress-fill med" style="width:56%"></div></div> 10/18 |
| 🎨 Frontend | <div class="progress-bar"><div class="progress-fill med" style="width:88%"></div></div> 7/8 |
| 🔒 Segurança | <div class="progress-bar"><div class="progress-fill med" style="width:58%"></div></div> 7/12 |
| 🔌 APIs | <div class="progress-bar"><div class="progress-fill med" style="width:50%"></div></div> 3/6 |
| 🗄️ Banco de Dados | <div class="progress-bar"><div class="progress-fill med" style="width:63%"></div></div> 5/8 |
| ⚙️ Infraestrutura | <div class="progress-bar"><div class="progress-fill med" style="width:75%"></div></div> 6/8 |
| 📈 Observabilidade | <div class="progress-bar"><div class="progress-fill med" style="width:50%"></div></div> 4/8 |
| 📚 Documentação | <div class="progress-bar"><div class="progress-fill low" style="width:100%"></div></div> 8/8 |
| 🚀 Performance | <div class="progress-bar"><div class="progress-fill med" style="width:50%"></div></div> 3/6 |
| 🧪 Testes | <div class="progress-bar"><div class="progress-fill low" style="width:90%"></div></div> 9/10 |
| ♻️ Refatoração | <div class="progress-bar"><div class="progress-fill med" style="width:57%"></div></div> 4/7 |
| 🤖 MCP | <div class="progress-bar"><div class="progress-fill med" style="width:60%"></div></div> 3/5 |
| 📦 Modularização | <div class="progress-bar"><div class="progress-fill fire" style="width:20%"></div></div> 1/5 |
| 🚀 Melhorias Futuras | <div class="progress-bar"><div class="progress-fill med" style="width:63%"></div></div> 5/8 |

---

## ✅ Concluídas vs Pendentes

- **86 concluídas** · **85 pendentes** · **2 em andamento** — total **173 tarefas**
- **13 erros do dia** no board de correções pós-bateria (Nível 1 → Nível 3)
- **Cobertura de conclusão: 49.7%**
- 🎯 **Meta atual:** fechar os 🔥 Alta (OAuth2 via porta de domínio, CORS, persistência LinuxUser/ExternalAccount, `createEvent`, transporte Google cacheado, versionamento de API, multi-module) antes de escalar features novas.

---

## 🔄 Últimas Alterações

- **Autenticação é `cookie-only`** — o JWT não vem mais no body de login/register, só no HttpOnly cookie.
- **Rate limiting ativo** na security chain (Bucket4j + Redis) com conexões lazy e filtro condicional.
- **Abstração Redis completa** (Fase 8 concluída — inclui `@EnableConfigurationProperties` no `RedisConfig`).
- **Frontend migrado** — `frontend/` removido, dashboard liquid-glass em `src/main/resources/static/app/` (servido em `/app`).
- **TLS habilitado** (`server.ssl`, PKCS12, HTTP/2); `client-auth=none` → mTLS ainda não ativo.
- **Bateria de testes 2026-08-03:** 195 únicos / 380 brutos · 0 falhas / 0 erros / 3 skipped · 38,4 s · JaCoCo 59,2% linha / 54,5% branch. Chain de segurança agora efetiva nos testes (Filter 19% → 83,8%); **403 ordem-dependente corrigido**; **CI falha em teste** (continue-on-error removido).
- **CORS ainda ausente** no `SecurityConfig`; Testcontainers + JaCoCo no `pom.xml` (sem enforcement).
- **2026-08-04:** Redis com senha + TLS no provider (`RedisURI.Builder` + `@PreDestroy` shutdown), registries IA refatorados para o contrato 1:N (`ModelsRegistry`/`AIProviderRegistry`), smoke real contra Redis 7.4.10 remoto validado (6380), import fantasma `jdk.tools.jlink.resources` removido.
- **2026-08-08:** Centralização do header validada 6/6 (1280/640/480 × `t`/`admin_teste`) — BUG 640px com nome longo corrigido: `.header-actions .btn` ganhou `flex-shrink: 1` + ellipsis no `.user-name` + `title` no avatarBtn; `probe-ellipsis-final.mjs` e `validate-header-final.mjs` (com `btnNavOverlap` + filtro WS 404) em `coffee-smoke`; evidências e screenshots em `static/app/TODO.md` + `dropdown-debug/`.

---

# Features To Code

> Features funcionais a implementar: backend, frontend, APIs, banco de dados e MCP.
> Prioridade: `🔥 Alta` · `🟡 Média` · `🟢 Baixa` · Complexidade: `🔴 Grande` · `🟡 Média` · `🟢 Pequena`

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
- [x] **Centralização do header validada 6/6** — re-validação em 1280/640/480 × usuário curto (`t`) e longo (`admin_teste`): sem overflow, sem overlap btn/nav, nav centralizada; bug de estouro do `.btn` em 640px corrigido com ellipsis no `.user-name` + `title` (tooltip). Evidências e screenshots em `static/app/TODO.md` + `validate-header-final.mjs`.
  <span class="tag">static/app/components/Header.js · static/app/styles/layout.css</span>
- [x] **Design system tokens coffee + blue** — `tokens.css` com `--glass-*`, z-index header 250.
  <span class="tag">static/app/styles/tokens.css</span>

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

- [ ] **Versionamento de API** — hoje rotas são `/auth/*`, `/api/test`, `/api/calendar/*` sem `/api/v1`. Decidir ADR-002 (registrado como Proposed — recomendação: URL path) e aplicar nos controllers.
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

# Code Structure Features

> Estrutura interna, segurança e qualidade do código: configuração, refatoração e melhorias estruturais.
> Prioridade: `🔥 Alta` · `🟡 Média` · `🟢 Baixa`

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
- [x] **Testes de segurança efetivos (chain aplicada)** — `.apply(springSecurity())` nos 5 builders MockMvc; cobertura de `infrastructure.security.Filter` subiu de 19% → 83,8% (prova de que o JWT filter é exercitado).
  <span class="tag">2026-08-03 · SecurityTest + integration/* · F-1</span>
- [x] **403 ordem-dependente corrigido (isolamento de `SecurityContextHolder`)** — `McpToolTest` não limpava o contexto (mock OAuth2) e vazava para `SecurityTest`; corrigido com `@AfterEach clearContext()` + `clearContext()` defensivo no `@BeforeEach` do `SecurityTest`.
  <span class="tag">2026-08-03 · mcp/McpToolTest.java · security/SecurityTest.java · F-6</span>

### 📝 Pendências

- [ ] **`JwtTokenResolver` não valida o esquema `Bearer `** — `header.replace("Bearer ", "")` aceita qualquer valor de header e extrai o token mesmo sem o prefixo. Exigir `filter(v -> v.startsWith("Bearer "))` antes do `replace`; reverter o teste `tokenWithoutBearerPrefix_returns401` (hoje documenta 200 + `admin_teste`) para 401.
  <span class="tag">🟡 Média · infrastructure/security/Filter/Token/JwtTokenResolver.java · F-7</span>
- [ ] **CORS configuration** — confirmado na auditoria: `SecurityConfig.securityFilterChain` não define nenhum `CorsConfigurationSource` (apenas `.csrf(disable)` + filters). Necessário para clientes web fora de `/app` (ex.: PS3, CLI browser).
  <span class="tag">🔥 Alta · SecurityConfig.java:40-89</span>
- [ ] **`RateLimitFilter` engole exceções silenciosamente** — `catch (IllegalArgumentException | Exception)` vazios; logar (SLF4J) e diferenciar política por rota (`LOGIN` vs `REGISTER` vs `API`), hoje força sempre `RateLimitPolicy.API`.
  <span class="tag">🟡 Média · RateLimitFilter.java:52-57</span>
- [ ] **SameSite=Strict nos cookies** — `CookieMapper` usa `"Lax"`; migrar para `Strict` e expor como campo opcional no `CookieDomain` (default seguro).
  <span class="tag">🟡 Média · infrastructure/Mappers/Cookies/CookieMapper.java:18</span>
- [ ] **mTLS (client-auth=none → require)** — TLS server-side pronto; falta CA própria (ADR-004 — `docs/architecture/adr/ADR-004-ca-mtls-homelab.md`, opções documentadas), emissão de certs client e `X509AuthenticationFilter`. Decisão híbrida JWT + mTLS (ADR-001) já tomada.
  <span class="tag">⛔ Bloqueado (aguarda decisão de CA) · application.properties:47</span>
- [ ] **Permissões granulares / scopes (Fase 5)** — `Role` hoje é `ADMIN/USER/MCP/API`; modelar `permissions`, tabela N:N, scopes no JWT e `@PreAuthorize` por permissão.
  <span class="tag">🟡 Média · domain/enums/Role.java</span>

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
- [x] **CI/CD — testes agora bloqueiam o pipeline** — `continue-on-error: true` removido do step TestSuite; testes vermelhos = build vermelho.
  <span class="tag">2026-08-03 · ci-cd.yml</span>
- [x] **`@PreDestroy` para fechar conexões Redis** — `RedisClientProvider.shutdown()` fecha connections e clients (`client.shutdown(0, 1, TimeUnit.SECONDS)`), eliminando vazamento de threads Netty.
  <span class="tag">2026-08-04 · RedisClientProvider.java</span>
- [x] **TLS/SSL nas conexões Redis** — `RedisURI.Builder` com `.withSsl(true)` quando `useSsl` ativo na instância.
  <span class="tag">2026-08-04 · RedisClientProvider.java · RedisClientInstace.java</span>
- [x] **Senha Redis** — campo `password` em `RedisClientInstace` (domínio) + `.withPassword()` no provider (só quando não-blank); env vars `REDIS_CACHE_PASSWORD`/`REDIS_CACHE_SSL` e `REDIS_RATELIMIT_PASSWORD`/`REDIS_RATELIMIT_SSL` mapeadas no `application.properties`.
  <span class="tag">2026-08-04 · domain/Database/redis/RedisClientInstace.java · application.properties</span>
- [x] **`security-lab` docker-compose** — ambiente isolado para testes de segurança.
  <span class="tag">security-lab/docker-compose.yml</span>

### 📝 Pendências

- [ ] **Drift de config no Redis remoto (6380)** — instância rate-limit roda SEM `requirepass` (AUTH → ERR), mas o `.env` espera `REDIS_RATELIMIT_PASSWORD`; 6379 (cache) inacessível de fora. Alinhar config do servidor com o `.env`.
  <span class="tag">🟡 Média · verificado 2026-08-04 · Redis 7.4.10 remoto</span>

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

# Architecture Reports

> Relatórios de arquitetura, refatoração e evolução estrutural.
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
- [ ] **Decidir ADRs 002-005** — os 5 ADRs já estão registrados em `docs/architecture/adr/` (2026-08-06: 001 Accepted, 002/003/005 Proposed, 004 Open); falta confirmar as decisões pendentes (versionamento `/api/v1`, multi-module, CA mTLS, módulo MCP) e refletir no `docs/planning/TODO.md`.
  <span class="tag">🟡 Média · docs/architecture/adr/</span>

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
- [x] **`AIRegistry` refatorado para o contrato 1:N** — interface genérica `AIRegistry<K, V>` agora expõe `Optional<List<V>> find(...)` (provider → lista de modelos); `AIProviderRegistry` adapta com `List::of`.
  <span class="tag">2026-08-04 · domain/interfaces/IA/AIRegistry.java · infrastructure/services/IA/Provaiders/Reagistry/AIProviderRegistry.java</span>
- [x] **`ModelsRegistry` refatorado para 1:N** — `AIRegistry<AIProvider, Model>` com `Map<AIProvider, List<Model>>`: `find(provider)` devolve a lista do provider, `find(String)` busca por `id`/`providerModelId` (case-insensitive), `getAll()` achata com flatMap. Corrigido type argument errado (`List<Model>` → `Model`) e removido stub morto `sync()` que quebrava a compilação.
  <span class="tag">2026-08-04 · infrastructure/services/IA/Models/Registry/ModelsRegistry.java</span>
- [x] **Import fantasma removido** — `import jdk.tools.jlink.resources.plugins;` (auto-import do VS Code apontando pra pacote interno do JDK) quebrou o build; removido do `ModelsRegistry`.
  <span class="tag">2026-08-04 · ModelsRegistry.java:17</span>

### 📝 Pendências

- [ ] **Field injection em `GoogleCalendarService` e `GoogleCalendarTools`** — `@Autowired` em campos públicos; trocar para constructor injection.
  <span class="tag">🔥 Alta · mcp/services + mcp/tools</span>
- [ ] **`System.out/err` → SLF4J** — `CalendarController`, `GoogleCalendarTools` (ver Observabilidade).
  <span class="tag">🟡 Média · mcp/</span>
- [ ] **`RateLimit` interface na camada certa** — hoje em `infrastructure/interfaces/Ratelimit/`; como contrato de negócio deveria viver no domínio (`domain/interfaces/`).
  <span class="tag">🟢 Baixa · infra interfaces</span>

---

## 📦 Modularização Maven

<span class="badge badge-phase2">Evolução para multi-module</span>

### ✅ Concluído

- [x] **Domínio 100% puro** — `domain/` sem imports de Spring/Jakarta/Lettuce; extraível como JAR.
  <span class="tag">domain/</span>

### 📝 Pendências

- [ ] **Estrutura multi-module** — `server-domain` (puro), `server-application`, `server-infrastructure`, `server-mcp`, `server-boot` (executável). Decidir ADR-003 (registrado como Proposed).
  <span class="tag">🔥 Alta · ADR-003</span>
- [ ] **`server-mcp` como módulo independente** — agentes consumirem só o JAR do MCP sem subir o server todo. Decidir ADR-005 (registrado como Proposed).
  <span class="tag">🟡 Média · ADR-005</span>
- [ ] **Publicar `server-domain` no Maven Local/GitHub Packages** — consumível por CLI (Rust/Python) e PS3.
  <span class="tag">🟢 Baixa · packaging</span>

---

# Documentation

> Documentação viva do ecossistema Coffee.
> Prioridade: `🔥 Alta` · `🟡 Média` · `🟢 Baixa`

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

- [x] **ADR-001 autenticação (JWT + mTLS híbrido)** — decisão tomada e ADR escrito em `docs/architecture/adr/ADR-001-jwt-mtls-autenticacao.md` (2026-08-06).
  <span class="tag">🟡 Média · docs/architecture/adr/</span>
- [ ] **README principal por fazer** — atualmente é mínimo; documentar setup, profiles, endpoints e fluxo MCP.
  <span class="tag">🟢 Baixa · README.md</span>

---

# Tests

> Qualidade e cobertura de testes.
> Prioridade: `🔥 Alta` · `🟡 Média` · `🟢 Baixa`

---

## 🧪 Testes

<span class="badge badge-phase1">Qualidade</span>

### ✅ Concluído

- [x] **Bateria de testes 100% verde** — 195 únicos / 380 brutos (surefire conta o wrapper `TestSuite` em dobro), **0 failures / 0 errors / 3 skipped** · 38,4 s · profile `test`.
  <span class="tag">2026-08-03 · ./mvnw test</span>
- [x] **Chain de segurança efetiva nos testes web** — `.apply(springSecurity())` nos 5 builders MockMvc; cobertura de `security.Filter` 19% → 83,8% (prova de que a validação JWT é exercitada).
  <span class="tag">2026-08-03 · SecurityTest + integration/*</span>
- [x] **Falha ordem-dependente eliminada (403)** — `McpToolTest` poluía o `SecurityContextHolder`; isolamento com `@AfterEach clearContext()` (+ defensivo no `SecurityTest`). Repro: `-Dtest=McpToolTest,SecurityTest`.
  <span class="tag">2026-08-03 · mcp/McpToolTest.java · security/SecurityTest.java</span>
- [x] **CI falha em teste** — `continue-on-error: true` removido do step "Run Integration Tests (TestSuite)" do `ci-cd.yml`; testes vermelhos agora bloqueiam o pipeline.
  <span class="tag">2026-08-03 · .github/workflows/ci-cd.yml</span>
- [x] **Testes unitários de domínio** — `UserTest`, `MachineTest`, `ExternalAccountTest`, `LinuxUserTest`, `GroupsTest`, `CookieDomainTest`.
  <span class="tag">unit/domain/</span>
- [x] **Testes de infraestrutura** — `JwtTokenServiceTest`, `TokenResolverTest`, `TokenResolverManagerTest`, `RedisAbstractionTest` (15), `CookieManagerAdapterTest`, `HttpCookieServiceTest`, `ModelsRegistryTest` (8, contrato 1:N da `AIRegistry`).
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
  <span class="tag">test/resources/</span>
- [x] **Testcontainers + JaCoCo no build** — dependências `testcontainers`/`postgresql`/`junit-jupiter` (BOM 1.20.4) e plugin JaCoCo 0.8.15 (threshold 0.00, sem enforcement) no `pom.xml`.
  <span class="tag">pom.xml:185-200 · 236-277</span>

### 📝 Pendências

- [ ] **Testes para o rate limiting** — `RateLimitFilter`/`Bucket4jRateLimiter` sem cobertura direta (testar 429 e policy).
  <span class="tag">🟡 Média · security/</span>
- [ ] **Testes para `OAuth2UserProvisioningService`** — sem teste do auto-provisionamento (criação com UUID, authority `ROLE_*`).
  <span class="tag">🟡 Média · services/OAuth/</span>

---

# Daily Errors

> Board de correções pós-bateria de testes — resolva do **simples** para o **complexo**.
> Prioridade: `🟡 Média` · `🟢 Baixa`

---

## 🧾 Correções Pós-Bateria de Testes

<span class="badge badge-phase1">Daily Board</span>

> Bateria 2026-08-03: **195 únicos / 380 brutos · 0 falhas · 0 erros · 3 skipped · 38,4 s** · JaCoCo 60,7% instr / 59,2% linha / 54,5% branch.
> Evidências completas: `reports/test-report.md`. Cada item referencia a seção dona do detalhe — **sem duplicar conteúdo**.
> Regra do board: resolver do **simples** para o **complexo**; marque `[x]` aqui **e** na seção dona.

### 🟢 Nível 1 — Simples (≤ 30 min)

- [ ] **Log do `RateLimitFilter` com placeholder literal** — trocar `"RateLimit begin {}"` por SLF4J real; diferenciar política por rota (LOGIN/REGISTER/API).
  <span class="tag">🟡 Média · 🔒 Segurança</span>
- [ ] **`JwtTokenService` `@Value` → constructor injection** — imutabilidade e testabilidade do serviço de token.
  <span class="tag">🟢 Baixa · 🚀 Performance</span>
- [ ] **`System.out/err` → SLF4J** em `CalendarController` e `GoogleCalendarTools`.
  <span class="tag">🟡 Média · 🔌 APIs · ♻️ Refatoração</span>
- [ ] **README principal mínimo** — setup, profiles, endpoints, fluxo MCP.
  <span class="tag">🟢 Baixa · 📚 Documentação</span>

### 🟡 Nível 2 — Médio (30 min – 1 h)

- [ ] **Endurecer `JwtTokenResolver` (exigir esquema `Bearer `)** — hoje aceita token sem prefixo; reverter teste `tokenWithoutBearerPrefix_returns401` para 401.
  <span class="tag">🟡 Média · 🔒 Segurança · F-7</span>
- [ ] **Teste isolado de rate limiting (429)** — `Filter.Ratelimt` em 10%; habilitar bean via `@TestConfiguration` (ou Redis Testcontainers).
  <span class="tag">🟡 Média · 🧪 Testes</span>
- [ ] **Testes de persistence `Machine` (0-8%) e `LinuxUser` (0%)** — cobrir adapter/mapper/entity.
  <span class="tag">🟡 Média · 🗄️ Banco de Dados</span>
- [ ] **`RateLimit` interface para o domínio** — hoje em `infrastructure/interfaces/Ratelimit/`.
  <span class="tag">🟢 Baixa · ♻️ Refatoração</span>
- [ ] **Alinhar JDK local ↔ CI** — validar com a mesma versão do pipeline (CI: Java 21 temurin; local: 25).
  <span class="tag">🟡 Média · ⚙️ Infraestrutura</span>

### 🔴 Nível 3 — Complexo (1–2+ dias)

- [ ] **Testes de `OAuth2UserProvisioningService`** — auto-provisionamento (UUID, authority `ROLE_*`); `OAuth` em 18,8%.
  <span class="tag">🟡 Média · 🧪 Testes</span>
- [ ] **Rate limit com Redis real / Testcontainers em integração** — remover o Postgres service morto do CI; exercitar 429 de ponta a ponta.
  <span class="tag">🟡 Média · ⚙️ Infraestrutura · 🧪 Testes</span>
- [ ] **Testcontainers de verdade (substituir H2 na integração)** — Postgres real para os testes de repositório.
  <span class="tag">🟡 Média · 🧪 Testes</span>
- [ ] **Enforcement de cobertura JaCoCo** — subir o threshold (ex.: ≥50%) para impedir regressão de qualidade.
  <span class="tag">🟡 Média · 🧪 Testes</span>

---

# 📋 Decisões Arquiteturais a Revisar

> Cada decisão deve ser registrada em `docs/architecture/adr/` após definição.

| # | Decisão | Status | Impacto |
|---|---------|--------|---------|
| ADR-001 | **Estratégia de autenticação: JWT + mTLS híbrido vs JWT only vs mTLS only** | ✅ **Accepted** — escrito em `docs/architecture/adr/` (2026-08-06) | SecurityConfig, FilterChain, SDK clients |
| ADR-002 | **Versionamento de API: URL path (`/api/v1/`) vs Header vs Query** | 📝 **Proposed** — ADR registrado; aguarda confirmação do path | Todos Controllers, SDK clients |
| ADR-003 | **Modularização Maven: multi-module vs single-module** | 📝 **Proposed** — ADR registrado; aguarda confirmação | Build, deploy, SDK extraction |
| ADR-004 | **mTLS para homelab: CA própria (step-ca) vs certs auto-assinados vs managed** | ⏳ **Open** — ADR registrado com opções; sem decisão | Infra, cert rotation |
| ADR-005 | **MCP como módulo separado vs integrado no server** | 📝 **Proposed** — ADR registrado; aguarda confirmação | Deploy, agent consumption |

---

# 📝 Como Modelos de IA Devem Utilizar Este TODO

Este documento é a **fonte única de verdade operacional** do coffe_server. Antes de executar qualquer tarefa, o modelo de IA deve:

1. **Ler o `# Progresso Geral`** — a meta atual e as últimas alterações definem o contexto de prioridade.
2. **Localizar a tarefa na seção dona** — cada item pertence a uma seção de Features, Estrutura, Arquitetura, Documentação ou Testes. A regra do board de correções é: marque `[x]` aqui **e** na seção dona — nunca duplicar conteúdo.
3. **Respeitar a hierarquia de prioridade** — `🔥 Alta` primeiro; `🟡 Média` e `🟢 Baixa` conforme a ordem do board (`simples → complexo`).
4. **Verificar a arquitetura antes de implementar** — valide se a mudança respeita Clean Architecture (domínio puro, application orquestra, infra implementa portas). Use `docs/architecture/arquiteture.md` e `docs/architecture/redis-abstraction.md` como referência.
5. **Registrar decisões como ADR** — qualquer decisão estrutural nova deve entrar na tabela `# 📋 Decisões Arquiteturais a Revisar` e, após definição, ser escrita em `docs/architecture/adr/`.
6. **Nunca excluir ou reescrever tarefas** — apenas mover de seção quando a classificação estiver errada, removendo apenas duplicatas literais.

---

# 🏠 Relatório do Projeto

> Referência rápida do ecossistema coffe_server.

## Documentação

| Documento | Descrição |
|---|---|
| [Arquitetura](docs/architecture/arquiteture.md) | Documentação completa de arquitetura — camadas, fluxos, glossário de interfaces |
| [Redis Abstraction](docs/architecture/redis-abstraction.md) | Abstração Redis (Ports & Adapters) — Connection, DatabaseClientProvider, Lettuce |
| [Auditoria Completa](docs/audits/relatorio-completo.md) | Mapeamento estrutural, violações, estratégia SDK |
| [Backend Audit](docs/audits/backend-audit.md) | Auditoria de backend e segurança |
| [Visual Identity](docs/UX/VISUAL-IDENTITY.md) | Design system coffee + blue — tokens, componentes, acessibilidade |
| [README](README.md) | Visão geral e badges do projeto |
| [AGENTS.md](.agents/AGENTS.md) | Contexto de arquitetura para agentes de IA |

## Arquivos de Planejamento

| Arquivo | Descrição |
|---|---|
| `docs/planning/TODO.md` | **Este documento** — fonte única de verdade operacional |
| `docs/planning/plain.md` | Notas de planejamento original (objetivo, fluxos, decisões) |
| `docs/architecture/arquiture.drawio` | Diagrama de arquitetura (draw.io) |
| `src/main/resources/static/app/TODO.md` | Backlog detalhado do frontend (dashboard liquid-glass) |

---

<blockquote>
<strong>📅 Gerado em:</strong> 2026-08-03 · atualizado 2026-08-04 · <strong>Baseado em auditoria completa do código-fonte</strong> (docs, backend, frontend, infra, testes, CI).<br>
<strong>🎯 Meta:</strong> Fechar os 🔥 Alta (OAuth2 via porta de domínio, CORS, persistência LinuxUser/ExternalAccount, `createEvent`, transporte Google cacheado, versionamento de API, multi-module) antes de escalar features novas.<br>
<strong>✅ Desde o último audit:</strong> CookieSystem no domínio, typos corrigidos, `Optional` em `extractIdSubject`, 401 em falha de auth, `setOwner` implementado, rate limiting ativo, abstração Redis completa (14/14), TLS + HTTP/2, dashboard liquid-glass em `/app`, **bateria de testes 100% verde (195 únicos / 0 falhas / 38,4 s)**, chain de segurança efetiva nos testes (Filter 83,8%), **403 ordem-dependente corrigido**, **CI falha em teste** (continue-on-error removido).<br>
<strong>✅ 2026-08-04:</strong> Redis com senha + TLS no provider (`RedisURI.Builder` + `@PreDestroy`), registries IA no contrato 1:N (`ModelsRegistry`/`AIProviderRegistry`), smoke real contra Redis 7.4.10 remoto, import fantasma removido.<br>
<strong>🧭 Frontend:</strong> backlog detalhado vive em <code>src/main/resources/static/app/TODO.md</code>.
</blockquote>

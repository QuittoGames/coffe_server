<style>
:root{--bg:#0B0F14;--el:#111827;--hov:#161B22;--bd:#1F2937;--bd2:#374151;--c1:#3C2415;--c2:#6F4E37;--c3:#A67B5B;--c4:#E8D5C4;--bl:#3B82F6;--bl2:#60A5FA;--ok:#22C55E;--warn:#EAB308;--err:#EF4444;--m:#6B7280;--t1:#F9FAFB;--t2:#D1D5DB;--t3:#9CA3AF;--r:8px;--rs:4px}
body{font-family:'Segoe UI',system-ui,sans-serif;background:var(--bg);color:var(--t1);line-height:1.6;max-width:960px;margin:0 auto;padding:24px}
h1{font-size:2rem;font-weight:700;color:var(--c4);border-bottom:2px solid var(--c2);padding-bottom:12px}
h1::before{content:"☕";font-size:1.8rem;margin-right:10px}
h2{font-size:1.3rem;font-weight:600;color:var(--c3);margin-top:32px;margin-bottom:12px;padding:8px 12px;background:linear-gradient(90deg,var(--c1),transparent);border-left:3px solid var(--c3);border-radius:0 var(--r) var(--r) 0}
h3{font-size:1.05rem;font-weight:600;color:var(--t2);margin-top:24px;margin-bottom:8px}
a{color:var(--bl);text-decoration:none}
a:hover{color:var(--bl2);text-decoration:underline}
.tag{display:inline-block;padding:1px 8px;border-radius:var(--rs);font-size:.7rem;font-family:Consolas,monospace;background:var(--hov);color:var(--t3);border:1px solid var(--bd)}
ul{list-style:none;padding-left:0}
ul li{padding:6px 12px;margin-bottom:4px;border-radius:var(--r);background:var(--el);border:1px solid var(--bd);font-size:.9rem}
ul li::before{content:"□";margin-right:10px;color:var(--m)}
ul li.done::before{content:"☑";color:var(--ok)}
table{width:100%;border-collapse:separate;border-spacing:0;border-radius:var(--r);overflow:hidden;border:1px solid var(--bd);font-size:.85rem;margin:12px 0}
th{background:var(--c1);color:var(--c4);font-weight:600;text-align:left;padding:8px 12px;font-size:.75rem;text-transform:uppercase;letter-spacing:.5px}
td{padding:8px 12px;border-top:1px solid var(--bd);background:var(--el);color:var(--t2)}
tr:hover td{background:var(--hov)}
blockquote{border-left:3px solid var(--c2);background:var(--el);padding:12px 16px;border-radius:0 var(--r) var(--r) 0;color:var(--t3);font-size:.9rem}
blockquote strong{color:var(--c3)}
hr{border:none;border-top:1px solid var(--bd);margin:24px 0}
.stat-card{background:var(--el);border:1px solid var(--bd);border-radius:var(--r);padding:8px 16px;font-size:.85rem;color:var(--t3);display:inline-block;margin:0 8px 8px 0}
.stat-card strong{color:var(--t1);font-size:1.1rem}
.task-done{opacity:.75}
.task-blocked{color:var(--err)}
.badge-prio{display:inline-block;padding:1px 8px;border-radius:var(--rs);font-size:.7rem;font-family:Consolas,monospace;font-weight:600;text-transform:uppercase;letter-spacing:.5px}
.badge-crit{background:var(--err);color:#fff}
.badge-high{background:#F97316;color:#fff}
.badge-med{background:var(--warn);color:var(--bg)}
.badge-low{background:var(--ok);color:var(--bg)}
.status-blocked{color:var(--err);font-weight:600}
</style>

# coffe_server — TODO

> **SOURCE OF TRUTH** — Tasks identificadas por **IDs estáveis** (TSK-001..TSK-107; próximo livre: **TSK-108**). O **estado canônico é a checkbox** de cada task: `[ ]` TODO · `[>]` IN_PROGRESS · `[x]` DONE · `[!]` BLOCKED · `[-]` CANCELLED. **Dashboard, Metrics e Active Work são derivados** e NÃO contêm estado independente (nunca duplicar descrição/estado).

---

# Dashboard

> Contém apenas IDs (derivação das checkboxes em `# Tasks`).

## AGORA

- TSK-047, TSK-001, TSK-005, TSK-015, TSK-028, TSK-016, TSK-054, TSK-007

## PRÓXIMO

- 🔒 Segurança: TSK-002, TSK-004, TSK-006, TSK-008, TSK-009, TSK-010, TSK-011, TSK-012, TSK-013, TSK-014, TSK-017, TSK-018, TSK-019, TSK-020, TSK-107
- 🐛 Bugs: TSK-021, TSK-022, TSK-023, TSK-024, TSK-025, TSK-027, TSK-029, TSK-030, TSK-031
- 🧱 Arquitetura: TSK-032, TSK-033, TSK-034, TSK-035, TSK-036, TSK-037, TSK-038, TSK-039, TSK-040, TSK-041, TSK-042, TSK-043, TSK-044, TSK-045
- 🤖 IA: TSK-048, TSK-049, TSK-051, TSK-052, TSK-053
- 🔌 MCP: TSK-055, TSK-056, TSK-057, TSK-059
- 🌐 WebSocket: TSK-060, TSK-061, TSK-062, TSK-063, TSK-064, TSK-065
- ⚙️ Infra: TSK-066, TSK-067, TSK-068, TSK-069, TSK-070
- 🧪 Testes: TSK-071, TSK-072, TSK-073, TSK-075, TSK-076, TSK-077, TSK-078, TSK-079, TSK-080, TSK-081, TSK-082, TSK-083, TSK-084
- ✨ Features: TSK-085, TSK-086, TSK-087, TSK-088, TSK-089, TSK-090, TSK-091, TSK-092, TSK-093, TSK-094, TSK-095, TSK-096, TSK-097
- 🎨 Frontend: TSK-098, TSK-099, TSK-100, TSK-101
- 📦 Modularização/Docs: TSK-104, TSK-105
- 🧹 Cleanup: TSK-046

## BLOQUEADO

- TSK-003 — ADR-004 sem decisão (mTLS)
- TSK-058 — OAuth2 Google será refatorado (createEvent stub)
- TSK-074 — faltam repos/mappers/adapters de LinuxUser
- TSK-102 — ADR-005 em aberto
- TSK-103 — ADR-003 em aberto

## Em execução

- TSK-050 — IA endpoints Nível 1
- TSK-106 — Commit do refactor Token resolvers (REVIEW)

## Metrics

> Informação **derivada** das checkboxes de status (uma por task em `# Tasks`). Não editar manualmente.

<span class="stat-card"><strong>107</strong> Total</span><span class="stat-card"><strong>98</strong> TODO</span><span class="stat-card"><strong>2</strong> IN_PROGRESS</span><span class="stat-card"><strong>2</strong> DONE</span><span class="stat-card"><strong>5</strong> BLOCKED</span><span class="stat-card"><strong>0</strong> CANCELLED em tasks</span><span class="stat-card"><strong>8</strong> AGORA</span><span class="stat-card"><strong>91</strong> PRÓXIMO</span><span class="stat-card"><strong>2</strong> Em execução</span>

> **Notas históricas** (auditoria 2026-08-09/10, 10 subagentes — informação legada, não derivada):
> - Build: **231 testes · 0 failures · 0 errors · 3 skipped · 38s · 25 Testcontainers Postgres**
> - Cobertura: **JaCoCo 59,2% linha / 54,5% branch** · Readiness 🟢 **8.3/10** · **4 CRITICAL** · **45 novas da auditoria**
> - Legado: 193 tasks · 106 ativas · **87 done** · contagens por categoria (🔒 21, 🐛 10, 🧱 15, 🤖 7, 🔌 5, 🌐 6, ⚙️ 5, 🧪 14, ✨ 13, 🎨 4, 📦 4, 🧹 2)
> - 5 claims obsoletas/canceladas marcadas `[-]` na seção própria (sem ID — fora da contagem de tasks).
> - Backup do TODO anterior: `C:\Users\Quitto\AppData\Local\Temp\opencode_backup_TODO_20260810.md`

## Active Work

> Derivado das tasks `[>]` (IN_PROGRESS) em `# Tasks`. Sem estado duplicado.

- **TSK-050** — IA endpoints Nível 1 (Together `.xyz`→`.ai`, Cohere `/v1`, Perplexity `/v1`). Pesquisa: `docs/research/model-listing-endpoints.md`.
- **TSK-106** — Commit do refactor Token resolvers + typo `IndepotecyKey` (REVIEW). Próximo: `./mvnw.cmd test` → revisar diff → corrigir typo → commit.

---

# Tasks

> Estado canônico: a checkbox antes do metadado `Priority` em cada task. `[ ]` TODO · `[>]` IN_PROGRESS · `[x]` DONE · `[!]` BLOCKED · `[-]` CANCELLED.

## 🔒 Segurança

### TSK-001 — JwtTokenResolver exige esquema `Bearer `

- [ ] **Priority:** CRITICAL
- **Category:** SECURITY · **Owner:** BOTH · **Estimate:** 30m

**Objective:** Exigir o prefixo `Bearer ` no header `Authorization` — hoje o resolver aceita token sem prefixo. Reverter o teste `tokenWithoutBearerPrefix_returns401` para esperar 401.

**Acceptance Criteria:**
- [ ] header sem `Bearer ` → não resolve (filtro → 401)
- [ ] header `Bearer <token>` → token resolvido
- [ ] testes da chain atualizados e build verde

**Validation:** `.\mvnw.cmd test`

**Notes:** Breaking p/ clientes que enviam token sem prefixo. Relacionada a TSK-017.

### TSK-002 — CORS ausente no SecurityConfig

- [ ] **Priority:** CRITICAL
- **Category:** SECURITY · **Owner:** BOTH · **Estimate:** 1h

**Objective:** Adicionar CORS no `SecurityConfig` (necessário p/ PS3 e web). Sensível na security chain — revisão humana.

### TSK-003 — mTLS `client-auth=require` + X509 filter

- [!] **Priority:** HIGH
- **Category:** SECURITY · **Owner:** QUITTO
- **Requires Decision:** YES · **ADR:** ADR-004 · **Blocked By:** ADR-004

**Objective:** Ativar mTLS `client-auth=require` + X509 filter. Decisão ADR-001 (JWT + mTLS híbrido); hoje `client-auth=none`; depende da CA (ADR-004). ⛔ Não implementar sem decisão.

### TSK-004 — Permissões granulares / scopes

- [ ] **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** BOTH

**Objective:** Permissões N:N, scopes JWT, `@PreAuthorize`. Sensível — revisar design antes.

### TSK-005 — RateLimitFilter: catch vazios + política por rota

- [ ] **Priority:** CRITICAL
- **Category:** SECURITY · **Owner:** BOTH · **Estimate:** 30m

**Objective:** Preencher os catch vazios do `RateLimitFilter` e aplicar política por rota (LOGIN/REGISTER/API).

**Validation:** `.\mvnw.cmd test`

**Notes:** Comportamento de segurança — revisão humana.

### TSK-006 — Higiene de secrets: `.env`/`keys/` fora do git + `.env.example`

- [ ] **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** Garantir `.env`/`keys/` fora do git e criar `.env.example`. Relacionada a TSK-018.

### TSK-007 — RateLimitFilter log placeholder → SLF4J real

- [ ] **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT · **Estimate:** 15m

**Objective:** Trocar o log placeholder `"RateLimit begin {}"` por SLF4J real.

### TSK-008 — MDC request ID / user ID / session ID

- [ ] **Priority:** HIGH
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** Adicionar MDC (request ID, user ID, session ID) nos logs.

### TSK-009 — Cookie domain hardcoded → configurável

- [ ] **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** `HttpCookieService.setDomain("coffe_server")` → propriedade configurável.

### TSK-010 — Security TRACE dev → profile dev

- [ ] **Priority:** LOW
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** Mover configuração TRACE para o profile `dev`.

### TSK-011 — `CookieDomain` campo `sameSite` (default seguro)

- [ ] **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** Adicionar campo `sameSite` ao `CookieDomain`.

### TSK-012 — Sanitizar secrets/keys em logs

- [ ] **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** Evitar vazamento de secrets/keys em logs.

### TSK-013 — Teste rate limit 429

- [ ] **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** Testar políticas de rate limit (429) em `/auth/login` e `/auth/register`.

### TSK-014 — API Key resolver (`X-API-Key`)

- [ ] **Priority:** LOW
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** Adicionar resolver `X-API-Key` na chain (OCP: novo `@Component TokenResolver`).

### TSK-015 — Chaves TLS privadas fora do JAR

- [ ] **Priority:** CRITICAL
- **Category:** SECURITY · **Owner:** BOTH

**Objective:** Mover chaves TLS privadas de `resources/keys` para externo/env. Não empacotar chaves privadas no JAR.

**Notes:** Mudança sensível de infra — revisão humana.

### TSK-016 — CookieMapper SameSite `Lax` → `Strict`

- [ ] **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT · **Estimate:** 30m

**Objective:** SameSite `Strict` no CookieMapper (CSRF). Verificar impacto no OAuth2 (redirects).

### TSK-017 — JwtAuthenticationFilter: nomes + `recoverToken`

- [ ] **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** Renomear variáveis (`NE`, `JTWVE`) e separar `recoverToken`. Relacionada a TSK-001.

### TSK-018 — Confirmar `.gitignore` cobre `keys/` e certs

- [ ] **Priority:** LOW
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** Confirmar `.gitignore` cobre `keys/` e certificados.

### TSK-019 — AuthExceptionHandler: 500 genérico

- [ ] **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** Handler retorna 500 genérico sem vazar stack trace/detalhes internos.

### TSK-020 — `OAuth2UserProvisioningService` → porta `UserRepository` (DIP)

- [ ] **Priority:** HIGH
- **Category:** SECURITY · **Owner:** AGENT

**Objective:** Trocar JPA direto pela porta `UserRepository`. OAuth2 Google será refatorado depois (TSK-043) — aplicar DIP mesmo assim.

### TSK-107 — JwtTokenService só emite tokens; validação via Spring Security

- [ ] **Priority:** HIGH
- **Category:** SECURITY · **Owner:** BOTH

**Objective:** `JwtTokenService` somente emite tokens; validação (signature/expiration/issuer/claims) via `JwtDecoder` + `JwtAuthenticationConverter`; eliminar `extractIdSubject` na maioria dos usos (`Authentication`/`@AuthenticationPrincipal Jwt`).

**Notes:** Mudança sensível/breaking — revisão humana.

## 🐛 Bugs

### TSK-021 — Validação de senha register inconsistente (1..500 vs min 8)

- [ ] **Priority:** MEDIUM
- **Category:** BUG · **Owner:** AGENT

**Objective:** Unificar regra de senha no register (hoje `@Size` 1..500 no DTO vs min 8 no service).

### TSK-022 — DTOs sem Bean Validation

- [ ] **Priority:** MEDIUM
- **Category:** BUG · **Owner:** AGENT

**Objective:** Adicionar `@NotBlank`/`@Email`/`@Size` nos DTOs de auth.

### TSK-023 — `RegisterResponseDTO.Token` maiúsculo

- [ ] **Priority:** LOW
- **Category:** BUG · **Owner:** AGENT

**Objective:** `Token` → `token` (inconsistente com `LoginResponseDTO`).

### TSK-024 — `MachineService`: exceção Spring → domínio

- [ ] **Priority:** MEDIUM
- **Category:** BUG · **Owner:** AGENT

**Objective:** `UsernameNotFoundException` (Spring) → `UserNotFoundException` (domínio).

### TSK-025 — HomeController: validar redirects pós-build Vite

- [ ] **Priority:** LOW
- **Category:** BUG · **Owner:** AGENT

**Objective:** Conferir redirects/rotas do frontend após build do Vite.

### TSK-026 — MCP base-url com aspas literal removidas

- [x] **Priority:** LOW
- **Category:** BUG · **Owner:** AGENT

**Objective:** Remover aspas literais do base-url em `application.properties:57`.

### TSK-027 — Drift Redis remoto 6380 (rate-limit sem `requirepass`)

- [ ] **Priority:** MEDIUM
- **Category:** BUG · **Owner:** QUITTO

**Objective:** Alinhar config do servidor Redis (6380) com o `.env` (rate-limit roda sem AUTH).

### TSK-028 — `JwtTokenService` `@Value` field (claim falsa) → constructor

- [ ] **Priority:** MEDIUM
- **Category:** BUG · **Owner:** AGENT · **Estimate:** 30m

**Objective:** Converter `@Value("${api.security.key}")` field (claim falsa de constructor injection) para construtor explícito com `@Value` no parâmetro.

**Acceptance Criteria:**
- [ ] `JwtTokenService` sem field injection
- [ ] propriedade `api.security.key` resolvida nos profiles (default/h2/test)
- [ ] testes de integração auth continuam verdes

**Validation:** `.\mvnw.cmd test`

**Notes:** Relacionada a TSK-047 (constructor injection em toda a chain de auth).

### TSK-029 — Typo porta: `genareteToken` → `generateToken`

- [ ] **Priority:** LOW
- **Category:** BUG · **Owner:** AGENT

**Objective:** Corrigir typo na porta `TokenService` e em todos os usos.

### TSK-030 — Typo porta: `resolver` → `resolve`

- [ ] **Priority:** LOW
- **Category:** BUG · **Owner:** AGENT

**Objective:** Corrigir typo na porta `TokenResolver`.

### TSK-031 — `isIs_active()` → `isActive()`

- [ ] **Priority:** LOW
- **Category:** BUG · **Owner:** AGENT

**Objective:** Corrigir getter de `Groups`.

## 🧱 Arquitetura

### TSK-032 — `TokenResolverManager` infra → application (use case)

- [ ] **Priority:** MEDIUM
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Mover `TokenResolverManager` para `application/services/Auth/Token/` (use case, não infra).

### TSK-033 — Domínio anêmico: `Machine.wakeOnLan()`, `ExternalAccount.refreshTokenIfExpired()`

- [ ] **Priority:** LOW
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Mover regras de negócio para os models do domínio.

### TSK-034 — Pacotes com typos (`Adpter`, `Provaider`, `Arry`, `Ratelimit`, `LinuxAcount`) — rename breaking

- [ ] **Priority:** LOW
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Renomear pacotes com typos (breaking de imports — planejar com cuidado).

### TSK-035 — `CookieService` infra → domínio (porta)

- [ ] **Priority:** MEDIUM
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Mover a interface `CookieService` para `domain/interfaces/` e separar `writeCookie()`/`toFrameworkCookie()` (Jakarta) num adapter.

### TSK-036 — `extractIdSubject()` → `Optional<Long>`

- [ ] **Priority:** MEDIUM
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Porta `TokenService.extractIdSubject` retorna `Optional<ID>` em vez de `null`.

### TSK-037 — `MachineRepositoryAdapter.setOwner()` retorna `new User()` vazio

- [ ] **Priority:** MEDIUM
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Corrigir stub de `setOwner()` no adapter (LSP).

### TSK-038 — Field injection → constructor injection

- [ ] **Priority:** MEDIUM
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Trocar `@Autowired` em campos por construtor (UserRepositoryAdapter, GoogelCalenderTools e demais).

### TSK-039 — `System.out/err` → SLF4J

- [ ] **Priority:** LOW
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Logs estruturados nos MCP tools/controllers.

### TSK-040 — MCP `GoogelCalenderTools` sem `@Component`

- [ ] **Priority:** MEDIUM
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Adicionar `@Component` para os `@Tool` serem descobertos.

### TSK-041 — `@Tool` com `@SneakyThrows` → try/catch

- [ ] **Priority:** MEDIUM
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Substituir `@SneakyThrows` por tratamento explícito (Lombok opcional).

### TSK-042 — MCP tools: separar autenticação por tool

- [ ] **Priority:** MEDIUM
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Cada `@Tool` resolve auth própria em vez de `System.getenv("TOKEN")` global.

### TSK-043 — OAuth2 Google refatoração (arquitetura)

- [ ] **Priority:** MEDIUM
- **Category:** ARCH · **Owner:** QUITTO

**Objective:** Refatorar serviços OAuth2 Google (fonte: `.agents/IA_README.md`). **Não usar como base de análise** até o cliente pedir.

### TSK-044 — Redis: `RedisArryCodec`/typos de pacote — reavaliar camada

- [ ] **Priority:** LOW
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Avaliar interface `RedisArryCodec` (infra) e typos `Arry`/`Adpter`/`Provaider`.

### TSK-045 — Domínio: `User` sem hash em `toString`

- [x] **Priority:** LOW
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Não expor `passwordHash` em `toString()`.

### TSK-054 — MCP: `GoogleCalenderService.createEvent()` retorna `""` — stub

- [ ] **Priority:** MEDIUM
- **Category:** ARCH · **Owner:** AGENT

**Objective:** Implementar `createEvent()` real (ou remover o stub).

## 🤖 IA

### TSK-047 — Constructor injection na chain de auth (Spring)

- [ ] **Priority:** HIGH
- **Category:** IA · **Owner:** AGENT

**Objective:** Trocar field injection por constructor injection em toda a cadeia de autenticação (filtro, resolvers, serviços). Pré-requisito p/ TSK-028.

**Acceptance Criteria:**
- [ ] nenhum `@Autowired` em campo na cadeia de auth
- [ ] build verde com testes de integração auth

**Validation:** `.\mvnw.cmd test`

### TSK-048 — `BaseProvider`: expor `getModels()` 1:N (provider → List<AIModel>)

- [ ] **Priority:** HIGH
- **Category:** IA · **Owner:** AGENT

**Objective:** Ajustar `BaseProvider`/registry para o contrato `AIRegistry.find(K)` → `Optional<List<V>>` (1:N por provider).

### TSK-049 — `ServiceProvider` enum 1:1 com `AIProvider` (registry)

- [ ] **Priority:** MEDIUM
- **Category:** IA · **Owner:** AGENT

**Objective:** Garantir que cada constante de `ServiceProvider` tenha exatamente um `AIProvider` registrado.

### TSK-050 — IA endpoints Nível 1 (URLs de listagem)

- [>] **Priority:** HIGH
- **Category:** IA · **Owner:** AGENT

**Objective:** Corrigir URLs de listagem de modelos (Nível 1: Together `.xyz`→`.ai`, Cohere `/v1`, Perplexity `/v1`).

**Notes:** Pesquisa: `docs/research/model-listing-endpoints.md`. 11 de 37 provedores com URL incorreta (30%).

### TSK-051 — IA endpoints Nível 2 (modelsUrl custom)

- [ ] **Priority:** MEDIUM
- **Category:** IA · **Owner:** AGENT

**Objective:** Sobrescrever `modelsUrl()` nos provedores com path custom (DeepInfra, Novita, Ollama, Cloudflare, Fireworks, Azure OpenAI).

### TSK-052 — IA endpoints Nível 3 (SDK/assinatura)

- [ ] **Priority:** LOW
- **Category:** IA · **Owner:** AGENT

**Objective:** Sobrescrever `fetchModelsFromApi()` com SDK/assinatura (Vertex AI, watsonx, OCI, AWS Bedrock).

### TSK-053 — `CoffeAgentService.getEnvKey()` placeholder → secrets reais

- [ ] **Priority:** MEDIUM
- **Category:** IA · **Owner:** AGENT

**Objective:** Substituir retorno `"key_temp"` por leitura real de env vars (ex.: `OPENAI_API_KEY`).

## 🔌 MCP

### TSK-055 — MCP: `GoogleCalendarTools` (nome corrigido) com `@Component`

- [ ] **Priority:** MEDIUM
- **Category:** MCP · **Owner:** AGENT

**Objective:** Garantir descoberta dos `@Tool` (classe renomeada + `@Component`).

### TSK-056 — MCP: autenticação por tool (sem `System.getenv("TOKEN")` global)

- [ ] **Priority:** MEDIUM
- **Category:** MCP · **Owner:** AGENT

**Objective:** Cada `@Tool` resolve auth própria (OAuth2/security context).

### TSK-057 — MCP: `@SneakyThrows` → try/catch explícito

- [ ] **Priority:** MEDIUM
- **Category:** MCP · **Owner:** AGENT

**Objective:** Remover Lombok opcional dos tools MCP.

### TSK-058 — MCP: `GoogleCalendarService.createEvent()` stub → real

- [!] **Priority:** MEDIUM
- **Category:** MCP · **Owner:** AGENT
- **Blocked By:** OAuth2 Google refatoração (TSK-043)

**Objective:** Implementar `createEvent()` real (hoje retorna `""`).

### TSK-059 — MCP: separar REST `CalendarController` da camada MCP

- [ ] **Priority:** MEDIUM
- **Category:** MCP · **Owner:** AGENT

**Objective:** `CalendarController` (REST) fora de `mcp/tools/` — reposicionar (ADR-005).

## 🌐 WebSocket

### TSK-060 — STOMP: handshake com token JWT (cookie/header)

- [ ] **Priority:** MEDIUM
- **Category:** WEBSOCKET · **Owner:** AGENT

**Objective:** Autenticar handshake WebSocket reutilizando a chain de resolvers.

### TSK-061 — STOMP: `@MessageMapping` protegidos por role

- [ ] **Priority:** MEDIUM
- **Category:** WEBSOCKET · **Owner:** AGENT

**Objective:** Autorizar destinos por role via Spring Security + STOMP.

### TSK-062 — STOMP: tópico `/topic/status` (máquinas/eventos)

- [ ] **Priority:** MEDIUM
- **Category:** WEBSOCKET · **Owner:** AGENT

**Objective:** Broadcast de eventos de máquinas/backup em tempo real.

### TSK-063 — WebSocket: heartbeat + reconexão no client

- [ ] **Priority:** LOW
- **Category:** WEBSOCKET · **Owner:** AGENT

**Objective:** Heartbeat STOMP e reconexão resiliente no front.

### TSK-064 — WebSocket: fila por usuário `/user/queue/*`

- [ ] **Priority:** LOW
- **Category:** WEBSOCKET · **Owner:** AGENT

**Objective:** Mensagens privadas por usuário autenticado.

### TSK-065 — WebSocket: testes de integração (STOMP client)

- [ ] **Priority:** MEDIUM
- **Category:** WEBSOCKET · **Owner:** AGENT

**Objective:** Testar handshake, auth e tópicos com STOMP client real.

## ⚙️ Infra

### TSK-066 — Deployment: Dockerfile multi-stage + compose prod

- [ ] **Priority:** MEDIUM
- **Category:** INFRA · **Owner:** QUITTO

**Objective:** Dockerfile multi-stage (build → runtime) e `docker-compose.prod.yml` (app + postgres + redis cache/rate-limit).

### TSK-067 — Secrets via env vars (fora do `application.properties`)

- [ ] **Priority:** MEDIUM
- **Category:** INFRA · **Owner:** QUITTO

**Objective:** Mover secrets (DB, JWT, OAuth2, Redis) para env vars; manter defaults locais apenas nos profiles dev/test.

### TSK-068 — Health checks (Actuator) para Postgres, Redis, OAuth2

- [ ] **Priority:** LOW
- **Category:** INFRA · **Owner:** AGENT

**Objective:** Expor health de dependências via Actuator.

### TSK-069 — Logging: padrão estruturado (JSON) em produção

- [ ] **Priority:** LOW
- **Category:** INFRA · **Owner:** AGENT

**Objective:** Logback JSON em prod (parseável), texto colorido em dev.

### TSK-070 — CI: GitHub Actions (build + testes + coverage)

- [ ] **Priority:** MEDIUM
- **Category:** INFRA · **Owner:** AGENT

**Objective:** Workflow Maven com testes (profile test) e relatório de cobertura.

## 🧪 Testes

### TSK-071 — Testes unitários do domínio (sem Spring)

- [ ] **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Cobrir enums, exceções, `CookieDomain`, `User`, `Machine` sem contexto Spring.

### TSK-072 — Testes dos TokenResolvers + manager (unitário)

- [ ] **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Testar chain de resolvers (cookie, bearer, ausência) isoladamente.

### TSK-073 — Testes dos repositórios (JPA com H2)

- [ ] **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Testar adapters/mappers (User, Machine) com H2.

### TSK-074 — Testes LinuxUser/Groups (persistence)

- [!] **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT
- **Blocked By:** faltam repos/mappers/adapters de LinuxUser

**Objective:** Testar persistência de LinuxUser/Groups quando implementada.

### TSK-075 — Testes de integração REST (MockMvc + H2)

- [ ] **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Testar `/auth/*`, `/api/test` e calendário via MockMvc.

### TSK-076 — Testes de segurança (401/403 nas rotas protegidas)

- [ ] **Priority:** HIGH
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Validar acesso negado sem token e com roles incorretas.

### TSK-077 — Testes de rate limit (429)

- [ ] **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Validar Bucket4j em `/auth/login`/`/auth/register`.

### TSK-078 — Testes JWT (geração, expiração, tamper)

- [ ] **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Testar `JwtTokenService` (token válido, expirado, assinatura adulterada).

### TSK-079 — Testes dos providers de IA (mock HTTP)

- [ ] **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Testar parsing de modelos com respostas mockadas por provider.

### TSK-080 — Testes do OAuth2 Google (mock de fluxo)

- [ ] **Priority:** LOW
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Testar provisioning com mock do OAuth2User. (OAuth2 será refatorado — TSK-043.)

### TSK-081 — Testes do Redis (Testcontainers)

- [ ] **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Testar a abstração Redis (provider, adapter, codec) com Testcontainers.

### TSK-082 — Testes de snapshot das rotas (API contract)

- [ ] **Priority:** LOW
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Fixar formato de resposta das rotas (body + status) contra mudanças acidentais.

### TSK-083 — Testes MCP tools (métodos `@Tool`)

- [ ] **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Testar chamadas e erros das tools MCP (calendar, future AI).

### TSK-084 — E2E happy path (login → token → API)

- [ ] **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Objective:** Fluxo completo via HTTP real (Testcontainers): register/login/access.

## ✨ Features

### TSK-085 — CRUD de Máquinas (REST)

- [ ] **Priority:** HIGH
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Endpoints CRUD de máquinas + permissão por owner (relacionado a TSK-004).

### TSK-086 — Wake-on-LAN (magic packet)

- [ ] **Priority:** MEDIUM
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Enviar magic packet para `macAddress` (domain `Machine.wakeOnLan()` — TSK-033).

### TSK-087 — Integração Tailscale (listar máquinas)

- [ ] **Priority:** MEDIUM
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Consultar máquinas via API Tailscale usando `tailscaleNodeKey`.

### TSK-088 — CRUD de Usuários (admin)

- [ ] **Priority:** MEDIUM
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Admin gerencia usuários (ativar/desativar, role) — base p/ multi-usuário.

### TSK-089 — CRUD LinuxUser/Groups

- [ ] **Priority:** MEDIUM
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Persistência completa de LinuxUser/Groups (desbloqueia TSK-074).

### TSK-090 — Mapper/Adapter de ExternalAccount

- [ ] **Priority:** LOW
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Completar persistência de contas OAuth externas (mapper + adapter).

### TSK-091 — `UserService` real (não esqueleto)

- [ ] **Priority:** LOW
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Implementar operações do `UserService` (hoje só construtor vazio).

### TSK-092 — Backup de arquivos (módulo)

- [ ] **Priority:** MEDIUM
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Módulo de backup de arquivos/sistemas (visão do plain.md).

### TSK-093 — Google Tasks (integração)

- [ ] **Priority:** LOW
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Integração com Google Tasks (reuso da infra OAuth2).

### TSK-094 — MCP tools de IA (modelos + chat)

- [ ] **Priority:** MEDIUM
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Expor modelos/chat via MCP usando o ecossistema de providers (TSK-048..053).

### TSK-095 — Página de login Thymeleaf (`login.html`)

- [ ] **Priority:** MEDIUM
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Criar `login.html` referenciado por `HomeController` (hoje 404).

### TSK-096 — Dashboard do usuário (Thymeleaf)

- [ ] **Priority:** MEDIUM
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Página pós-login com dados do usuário/máquinas.

### TSK-097 — Refresh token (rota + cookie)

- [ ] **Priority:** MEDIUM
- **Category:** FEATURE · **Owner:** AGENT

**Objective:** Fluxo de refresh (novo JWT) com rota dedicada.

## 🎨 Frontend

### TSK-098 — Landing page Thymeleaf alinhada à identidade Coffee

- [ ] **Priority:** MEDIUM
- **Category:** FRONTEND · **Owner:** AGENT

**Objective:** `index.html` coerente com `docs/UX/VISUAL-IDENTITY.md` (dark-first, café + azul).

### TSK-099 — CSS do app centralizado (design system)

- [ ] **Priority:** MEDIUM
- **Category:** FRONTEND · **Owner:** AGENT

**Objective:** Unificar `static/css/app.css` com os tokens do design system (variáveis CSS).

### TSK-100 — Empty states e loading (UX)

- [ ] **Priority:** LOW
- **Category:** FRONTEND · **Owner:** AGENT

**Objective:** Aplicar estados vazios com CTA e loading (skeleton) conforme identidade visual.

### TSK-101 — Responsividade básica (mobile)

- [ ] **Priority:** LOW
- **Category:** FRONTEND · **Owner:** AGENT

**Objective:** Layout mínimo utilizável em mobile (dashboard e login).

## 📦 Modularização / Docs

### TSK-102 — MCP como módulo Maven separado (`server-mcp`)

- [!] **Priority:** MEDIUM
- **Category:** MOD · **Owner:** AGENT
- **Blocked By:** ADR-005 em aberto

**Objective:** Extrair camada MCP para módulo próprio (tools + services, sem controllers REST).

### TSK-103 — Multi-module Maven (domain/application/infra/boot)

- [!] **Priority:** MEDIUM
- **Category:** MOD · **Owner:** AGENT
- **Blocked By:** ADR-003 em aberto

**Objective:** Estrutura multi-module conforme ADR-003 (domínio 100% puro, zero dependências).

### TSK-104 — Atualizar docs após refactors (arquitetura/audit)

- [ ] **Priority:** LOW
- **Category:** MOD · **Owner:** AGENT

**Objective:** Refletir no `docs/architecture/arquiteture.md` e `docs/audits/relatorio-completo.md` as mudanças de estrutura/API já aplicadas.

### TSK-105 — ADRs propostos → decisão

- [ ] **Priority:** LOW
- **Category:** MOD · **Owner:** QUITTO

**Objective:** Decidir ADR-002 (versionamento `/api/v1/`), ADR-003 (modularização), ADR-005 (módulo MCP). Atualizar status no `docs/architecture/adr/README.md`.

## 🧹 Cleanup

### TSK-046 — Código morto / imports não usados / logs de debug

- [ ] **Priority:** LOW
- **Category:** CLEANUP · **Owner:** AGENT

**Objective:** Varrer código morto, imports não usados e `System.out/err` remanescentes.

### TSK-106 — Commit do refactor Token resolvers + typo `IndepotecyKey` (REVIEW)

- [>] **Priority:** HIGH
- **Category:** CLEANUP · **Owner:** AGENT

**Objective:** Revisar diff do refactor dos resolvers; corrigir typo `IndepotecyKey`; rodar `.\mvnw.cmd test`; commitar.

**Notes:** Refactor em andamento — `HttpTokenRequestContext` e portas TokenResolver/TokenRequestContext renomeadas; testes precisam de ajuste de imports (LSP sinalizando).

---

## ✅ Completed

> Marcador de conclusão — **não é uma task** (sem ID). O estado das tasks DONE vive nas próprias checkboxes `[x]` em `# Tasks`.

- TSK-026 — aspas literais removidas do MCP base-url
- TSK-045 — `User.toString()` sem hash de senha
- Claims legadas de conclusão (87 done na auditoria 2026-08-10) — consolidadas nas checkboxes das tasks correspondentes; sem duplicação de estado aqui.

## 🗑️ Obsoletas / Canceladas

> Claims legadas da auditoria — **sem IDs** e **fora da contagem** de tasks (107). Marcadas `[-]` apenas para registro histórico.

- [-] mTLS TLS server-side completo (na real: só `client-auth=none`; mTLS bloqueado no ADR-004 — ver TSK-003)
- [-] `login.html` existia (na real: template não existe — ver TSK-095)
- [-] OAuth2 Google validado como base de arquitetura (na real: será refatorado — TSK-043; não usar como base)
- [-] Redis rate-limit configurado com AUTH (na real: drift no servidor 6380 — ver TSK-027)
- [-] `extractIdSubject` retorna `Optional<Long>` (na real: mudança pendente — ver TSK-036)

---

## Task Registry

> Sumário por ID. **Derivado** das checkboxes em `# Tasks` — não editar manualmente. Estados: `[ ]` TODO · `[>]` IN_PROGRESS · `[x]` DONE · `[!]` BLOCKED · `[-]` CANCELLED.

| ID | Estado | Prio | Categoria | Título curto |
|----|--------|------|-----------|--------------|
| TSK-001 | `[ ]` | CRITICAL | 🔒 | JwtTokenResolver exige `Bearer ` |
| TSK-002 | `[ ]` | CRITICAL | 🔒 | CORS no SecurityConfig |
| TSK-003 | `[!]` | HIGH | 🔒 | mTLS (ADR-004) |
| TSK-004 | `[ ]` | MEDIUM | 🔒 | Permissões granulares/scopes |
| TSK-005 | `[ ]` | CRITICAL | 🔒 | RateLimitFilter catch + política |
| TSK-006 | `[ ]` | MEDIUM | 🔒 | Higiene secrets + `.env.example` |
| TSK-007 | `[ ]` | MEDIUM | 🔒 | RateLimitFilter SLF4J |
| TSK-008 | `[ ]` | HIGH | 🔒 | MDC logs |
| TSK-009 | `[ ]` | MEDIUM | 🔒 | Cookie domain configurável |
| TSK-010 | `[ ]` | LOW | 🔒 | TRACE dev → profile |
| TSK-011 | `[ ]` | MEDIUM | 🔒 | `CookieDomain.sameSite` |
| TSK-012 | `[ ]` | MEDIUM | 🔒 | Sanitizar secrets em logs |
| TSK-013 | `[ ]` | MEDIUM | 🔒 | Teste rate limit 429 |
| TSK-014 | `[ ]` | LOW | 🔒 | API Key resolver |
| TSK-015 | `[ ]` | CRITICAL | 🔒 | Chaves TLS fora do JAR |
| TSK-016 | `[ ]` | MEDIUM | 🔒 | SameSite Strict |
| TSK-017 | `[ ]` | MEDIUM | 🔒 | Filter nomes + `recoverToken` |
| TSK-018 | `[ ]` | LOW | 🔒 | `.gitignore` keys/certs |
| TSK-019 | `[ ]` | MEDIUM | 🔒 | 500 genérico no handler |
| TSK-020 | `[ ]` | HIGH | 🔒 | OAuth2 → porta UserRepository |
| TSK-021 | `[ ]` | MEDIUM | 🐛 | Validação senha register |
| TSK-022 | `[ ]` | MEDIUM | 🐛 | Bean Validation DTOs |
| TSK-023 | `[ ]` | LOW | 🐛 | `RegisterResponseDTO.Token` |
| TSK-024 | `[ ]` | MEDIUM | 🐛 | Exceção domínio em MachineService |
| TSK-025 | `[ ]` | LOW | 🐛 | Redirects pós-build Vite |
| TSK-026 | `[x]` | LOW | 🐛 | MCP base-url aspas |
| TSK-027 | `[ ]` | MEDIUM | 🐛 | Drift Redis 6380 |
| TSK-028 | `[ ]` | MEDIUM | 🐛 | `@Value` field → constructor |
| TSK-029 | `[ ]` | LOW | 🐛 | Typo `genareteToken` |
| TSK-030 | `[ ]` | LOW | 🐛 | Typo `resolver` |
| TSK-031 | `[ ]` | LOW | 🐛 | `isIs_active()` |
| TSK-032 | `[ ]` | MEDIUM | 🧱 | TokenResolverManager → app |
| TSK-033 | `[ ]` | LOW | 🧱 | Domínio anêmico |
| TSK-034 | `[ ]` | LOW | 🧱 | Pacotes com typos |
| TSK-035 | `[ ]` | MEDIUM | 🧱 | CookieService → domínio |
| TSK-036 | `[ ]` | MEDIUM | 🧱 | `extractIdSubject` Optional |
| TSK-037 | `[ ]` | MEDIUM | 🧱 | Stub `setOwner()` |
| TSK-038 | `[ ]` | MEDIUM | 🧱 | Constructor injection |
| TSK-039 | `[ ]` | LOW | 🧱 | System.out → SLF4J |
| TSK-040 | `[ ]` | MEDIUM | 🧱 | Tools `@Component` |
| TSK-041 | `[ ]` | MEDIUM | 🧱 | `@SneakyThrows` → try/catch |
| TSK-042 | `[ ]` | MEDIUM | 🧱 | Auth por tool |
| TSK-043 | `[ ]` | MEDIUM | 🧱 | Refactor OAuth2 Google |
| TSK-044 | `[ ]` | LOW | 🧱 | RedisArryCodec camada |
| TSK-045 | `[x]` | LOW | 🧱 | `User.toString` sem hash |
| TSK-046 | `[ ]` | LOW | 🧹 | Código morto/imports |
| TSK-047 | `[ ]` | HIGH | 🤖 | Constructor injection auth |
| TSK-048 | `[ ]` | HIGH | 🤖 | BaseProvider 1:N |
| TSK-049 | `[ ]` | MEDIUM | 🤖 | ServiceProvider 1:1 |
| TSK-050 | `[>]` | HIGH | 🤖 | IA endpoints Nível 1 |
| TSK-051 | `[ ]` | MEDIUM | 🤖 | IA endpoints Nível 2 |
| TSK-052 | `[ ]` | LOW | 🤖 | IA endpoints Nível 3 |
| TSK-053 | `[ ]` | MEDIUM | 🤖 | getEnvKey real |
| TSK-054 | `[ ]` | MEDIUM | 🧱 | createEvent stub |
| TSK-055 | `[ ]` | MEDIUM | 🔌 | GoogleCalendarTools `@Component` |
| TSK-056 | `[ ]` | MEDIUM | 🔌 | Auth por tool MCP |
| TSK-057 | `[ ]` | MEDIUM | 🔌 | MCP try/catch |
| TSK-058 | `[!]` | MEDIUM | 🔌 | createEvent real |
| TSK-059 | `[ ]` | MEDIUM | 🔌 | CalendarController fora de MCP |
| TSK-060 | `[ ]` | MEDIUM | 🌐 | STOMP handshake JWT |
| TSK-061 | `[ ]` | MEDIUM | 🌐 | STOMP roles |
| TSK-062 | `[ ]` | MEDIUM | 🌐 | Tópico `/topic/status` |
| TSK-063 | `[ ]` | LOW | 🌐 | Heartbeat/reconexão |
| TSK-064 | `[ ]` | LOW | 🌐 | Fila `/user/queue/*` |
| TSK-065 | `[ ]` | MEDIUM | 🌐 | Testes STOMP |
| TSK-066 | `[ ]` | MEDIUM | ⚙️ | Dockerfile + compose prod |
| TSK-067 | `[ ]` | MEDIUM | ⚙️ | Secrets env vars |
| TSK-068 | `[ ]` | LOW | ⚙️ | Health checks |
| TSK-069 | `[ ]` | LOW | ⚙️ | Logging JSON prod |
| TSK-070 | `[ ]` | MEDIUM | ⚙️ | CI GitHub Actions |
| TSK-071 | `[ ]` | MEDIUM | 🧪 | Testes domínio |
| TSK-072 | `[ ]` | MEDIUM | 🧪 | Testes resolvers |
| TSK-073 | `[ ]` | MEDIUM | 🧪 | Testes repositórios H2 |
| TSK-074 | `[!]` | MEDIUM | 🧪 | Testes LinuxUser/Groups |
| TSK-075 | `[ ]` | MEDIUM | 🧪 | Integração REST MockMvc |
| TSK-076 | `[ ]` | HIGH | 🧪 | Testes segurança 401/403 |
| TSK-077 | `[ ]` | MEDIUM | 🧪 | Testes rate limit |
| TSK-078 | `[ ]` | MEDIUM | 🧪 | Testes JWT |
| TSK-079 | `[ ]` | MEDIUM | 🧪 | Testes providers IA |
| TSK-080 | `[ ]` | LOW | 🧪 | Testes OAuth2 mock |
| TSK-081 | `[ ]` | MEDIUM | 🧪 | Testes Redis Testcontainers |
| TSK-082 | `[ ]` | LOW | 🧪 | Snapshot rotas |
| TSK-083 | `[ ]` | MEDIUM | 🧪 | Testes MCP tools |
| TSK-084 | `[ ]` | MEDIUM | 🧪 | E2E happy path |
| TSK-085 | `[ ]` | HIGH | ✨ | CRUD Máquinas |
| TSK-086 | `[ ]` | MEDIUM | ✨ | Wake-on-LAN |
| TSK-087 | `[ ]` | MEDIUM | ✨ | Tailscale |
| TSK-088 | `[ ]` | MEDIUM | ✨ | CRUD Usuários |
| TSK-089 | `[ ]` | MEDIUM | ✨ | CRUD LinuxUser/Groups |
| TSK-090 | `[ ]` | LOW | ✨ | ExternalAccount mapper |
| TSK-091 | `[ ]` | LOW | ✨ | UserService real |
| TSK-092 | `[ ]` | MEDIUM | ✨ | Backup de arquivos |
| TSK-093 | `[ ]` | LOW | ✨ | Google Tasks |
| TSK-094 | `[ ]` | MEDIUM | ✨ | MCP tools IA |
| TSK-095 | `[ ]` | MEDIUM | ✨ | Página login Thymeleaf |
| TSK-096 | `[ ]` | MEDIUM | ✨ | Dashboard usuário |
| TSK-097 | `[ ]` | MEDIUM | ✨ | Refresh token |
| TSK-098 | `[ ]` | MEDIUM | 🎨 | Landing page identidade |
| TSK-099 | `[ ]` | MEDIUM | 🎨 | CSS design system |
| TSK-100 | `[ ]` | LOW | 🎨 | Empty states/loading |
| TSK-101 | `[ ]` | LOW | 🎨 | Responsividade mobile |
| TSK-102 | `[!]` | MEDIUM | 📦 | Módulo MCP (ADR-005) |
| TSK-103 | `[!]` | MEDIUM | 📦 | Multi-module (ADR-003) |
| TSK-104 | `[ ]` | LOW | 📦 | Atualizar docs |
| TSK-105 | `[ ]` | LOW | 📦 | Decidir ADRs |
| TSK-106 | `[>]` | HIGH | 🧹 | Commit refactor resolvers |
| TSK-107 | `[ ]` | HIGH | 🔒 | JwtTokenService só emite |

---

# Decisões (ADRs)

> Estado dos ADRs — ver `docs/architecture/adr/README.md` (fonte canônica). Referência rápida.

| ADR | Título | Status |
|-----|--------|--------|
| ADR-001 | JWT + mTLS híbrido | ✅ Accepted |
| ADR-002 | Versionamento de API (`/api/v1/`) | 📝 Proposed (TSK-105) |
| ADR-003 | Modularização Maven | 📝 Proposed (TSK-103) |
| ADR-004 | CA para mTLS | ⏳ Open (TSK-003) |
| ADR-005 | MCP módulo separado | 📝 Proposed (TSK-102) |

---

# 🤖 Agent Protocol

> Regras para agentes que editam este arquivo — **obrigatório**.

1. **Estado canônico = checkbox** em `# Tasks`. Nunca duplicar descrição/estado em Dashboard, Metrics, Active Work ou Registry (são derivados — atualizar apenas as checkboxes).
2. **IDs estáveis**: TSK-001..TSK-107. Próximo ID: **TSK-108**. Nunca reutilizar IDs cancelados.
3. **Formatos de status**: `[ ]` TODO · `[>]` IN_PROGRESS · `[x]` DONE · `[!]` BLOCKED · `[-]` CANCELLED. Tasks BLOCKED devem declarar `Blocked By` (task ou ADR). Tasks CANCELLED/obsoletas sem ID ficam na seção `## 🗑️ Obsoletas / Canceladas`.
4. **Estrutura mínima por task**: `### TSK-XXX — Título` → linha de status+`Priority` → metadados (`Category`, `Owner`, `Estimate`) → `**Objective:**`. Blocos `Acceptance Criteria`/`Validation`/`Notes` apenas quando houver critérios verificáveis.
5. **Mover estado**: se uma task entra/sai de DONE/BLOCKED/IN_PROGRESS, o Dashboard e o Registry são atualizados **na mesma edição** (derivação imediata).
6. **Sempre concluir com** `.\mvnw.cmd test` antes de marcar algo DONE relacionado a código.

---

> **Documento mantido por:** Quitto · **Última atualização:** 2026-08-11
> **Backup anterior:** `C:\Users\Quitto\AppData\Local\Temp\opencode_backup_TODO_20260810.md`
> **Propósito:** Documentação viva — atualize conforme o código evoluir.

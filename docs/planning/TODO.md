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
</style>

# coffe_server — TODO

> **Readiness:** 🟢 8.3/10 · **Auditoria 2026-08-09/10:** 10 subagentes (claims verificadas, bugs, arquitetura, frontend real).
> **Totais:** 192 tasks · 106 ativas · 86 done · 104 pendentes (99 TODO + 5 bloqueadas) · 2 em execução · 4 CRITICAL · 45 novas · 5 obsoletas/canceladas.
> **Build:** 231 testes · 0 failures · 0 errors · 3 skipped · 38s · 25 Testcontainers Postgres · JaCoCo 59,2% linha / 54,5% branch.
> **Backup do TODO antigo:** `C:\Users\Quitto\AppData\Local\Temp\opencode_backup_TODO_20260810.md`.

---

# Dashboard

## 🚨 AGORA — próxima ação (7)

| ID | Task | Prio | Est. |
|---|---|---|---|
| TSK-026 | MCP: `spring.ai.mcp.server.base-url` com aspas em `application.properties:57` — remover | 🟡 | 10m |
| TSK-047 | Secrets IA: `CoffeAgentService.getEnvKey()` lê env vars (hoje `"key_temp"`) | 🔴 | 30m |
| TSK-001 | JwtTokenResolver exige esquema `Bearer ` (F-7); reverter teste p/ 401 | 🔴 | 30m |
| TSK-028 | `JwtTokenService` ainda `@Value` field (claim falsa) → constructor injection | 🟡 | 30m |
| TSK-016 | CookieMapper SameSite `Lax` → `Strict` | 🟡 | 30m |
| TSK-054 | `RateLimit` interface: infra → domínio (`domain/interfaces/`) | 🟡 | 1h |
| TSK-007 | RateLimitFilter log placeholder → SLF4J real | 🟡 | 15m |

## 📋 PRÓXIMO (14)

| ID | Task | Prio |
|---|---|---|
| TSK-020 | `OAuth2UserProvisioningService` JPA direto → porta `UserRepository` | 🔴 |
| TSK-089 | Persistence `LinuxUser`/`Groups` — repos/mappers/adapters/porta | 🔴 |
| TSK-012 | Sanitização de secrets em logs | 🟡 |
| TSK-004 | Permissões granulares / scopes | 🟡 |
| TSK-017 | JwtAuthenticationFilter: variáveis `NE`/`JTWVE` + separar `recoverToken` | 🟡 |
| TSK-002 | CORS ausente no `SecurityConfig` | 🔴 |
| TSK-022 | DTOs sem Bean Validation | 🟡 |
| TSK-006 | Higiene de secrets: `.env`/`keys/` fora do git + `.env.example` | 🟡 |
| TSK-010 | Security TRACE dev → profile `dev` | 🟢 |
| TSK-019 | AuthExceptionHandler: 500 genérico (sem vazar detalhes) | 🟡 |
| TSK-008 | MDC request ID / user ID / session ID | 🔴 |
| TSK-011 | `CookieDomain` campo `sameSite` (default seguro) | 🟡 |
| TSK-021 | Validação de senha register inconsistente | 🟡 |
| TSK-013 | Teste rate limit 429 | 🟡 |

## ⛔ BLOQUEADO (5)

| ID | Task | Motivo |
|---|---|---|
| TSK-003 | mTLS `client-auth=require` | ADR-004 sem decisão |
| TSK-058 | `GoogleCalendarService.createEvent()` stub | OAuth2 Google será refatorado |
| TSK-074 | Persistence `LinuxUser` testes (0%) | Faltam repos/mappers/adapters |
| TSK-102 | Extrair `server-mcp` como módulo | ADR-005 em aberto |
| TSK-103 | Publicar `server-domain` no Maven | ADR-003 em aberto |

## 📊 MÉTRICAS

<span class="stat-card"><strong>192</strong> total · 106 ativas + 86 done</span>
<span class="stat-card"><strong>99</strong> TODO prontos</span>
<span class="stat-card"><strong>5</strong> bloqueadas</span>
<span class="stat-card"><strong>2</strong> em execução · 1 REVIEW + 1 IN_PROGRESS</span>
<span class="stat-card"><strong>4</strong> CRITICAL</span>
<span class="stat-card"><strong>45</strong> novas da auditoria</span>
<span class="stat-card"><strong>5</strong> obsoletas/canceladas</span>
<span class="stat-card"><strong>231</strong> testes · 0 falhas · 38s</span>
<span class="stat-card"><strong>59,2%</strong> linha JaCoCo · 54,5% branch</span>

| Categoria | Ativas |
|---|---|
| 🔒 Segurança | 20 |
| 🐛 Bugs | 11 |
| 🧱 Arquitetura | 15 |
| 🤖 IA | 7 |
| 🔌 MCP | 5 |
| 🌐 WebSocket | 6 |
| ⚙️ Infra | 5 |
| 🧪 Testes | 14 |
| ✨ Features | 13 |
| 🎨 Frontend | 4 |
| 📦 Modularização/Docs | 4 |
| 🧹 Cleanup | 2 |

## 🗑️ OBSOLETAS / CANCELADAS (5)

- "`frontend/` antigo removido" — **falso**: `frontend/` é a fonte Vite MPA (build → `static/app/`, b540c61)
- "`static/app/TODO.md`" — **inexistente**; backlog vive em `frontend/TODO.md`
- "WS `/ws` 404" — **desatualizado**: STOMP `/protocol` implementado; faltam handshake + UI
- "Typos corrigidos" — **parcialmente falso**: `IndepotecyKey`, pacotes `Adpter`/`Provaider`/`Arry` seguem com typos
- "Constructor injection generalizada" — **falso**: `JwtTokenService` segue `@Value` (TSK-028)

---

# Active Work

- **TSK-106 · 🚧 REVIEW · 🔴** — Refactor Token resolvers **não commitado**: `Filter/Token/` movido p/ `security/Token/`, testes atualizados, `JpaMachineRepository.java` alterado (9 arquivos M/D + untracked; typo `IndepotecyKey`). Próxima ação: `.\mvnw.cmd test` → revisar diff → corrigir typo → commit.
- **TSK-050 · 🚧 IN_PROGRESS · 🟡** — IA endpoints Nível 1 (Together `.xyz`→`.ai`, Cohere `/v1`, Perplexity `/v1`). Pesquisa: `docs/research/model-listing-endpoints.md`.

---

# Backlog (resumo)

> 86 done destacados por categoria. Registro completo com status individual: Task Registry abaixo.

## 🔒 Segurança
- [x] Rate limiting `/auth/login` + `/auth/register` (Bucket4j + Redis 7.4.10)
- [x] TLS + HTTP/2; OAuth2 authority corrigida; logout revoga cookie
- [x] `JwtTokenService.verifyToken` null-safe; `.env` fora do git; chain testada (filtro 83,8%)
- [ ] 20 ativas → `TSK-001..020` (mTLS, CORS, Bearer, keys TLS, SameSite…)

## 🐛 Bugs
- [x] `setOwner()` implementado; `extractIdSubject` Optional; `User.toString` sem hash
- [ ] 11 ativas → `TSK-021..031`

## 🧱 Arquitetura
- [x] CookieSystem no domínio; `MachineNotFoundException` no domínio; typos de classe corrigidos; `AIRegistry` 1:N
- [ ] 15 ativas → `TSK-032..045` + `TSK-054`

## 🤖 IA
- [x] Registries 1:N (`ModelsRegistry`, `AIProviderRegistry`); import fantasma removido
- [ ] 7 ativas → `TSK-047..053`

## 🔌 MCP
- [x] `GoogleCalendarTools` @Component; MCP `/mcp` com `ROLE_MCP`; `listEvents` funcional
- [ ] 5 ativas → `TSK-055..059`

## 🌐 WebSocket
- [x] STOMP 1.2 no front (`/protocol`, `@SendTo /queue/coffee-agent`); smoke `sendRequest`
- [ ] 6 ativas → `TSK-060..065`

## ⚙️ Infra
- [x] Abstração Redis completa; `@PreDestroy`; TLS/senha Redis; logback rolling; CI/CD 5 jobs; compose security-lab
- [ ] 5 ativas → `TSK-066..070`

## 🧪 Testes
- [x] Bateria 231 verdes (25 Testcontainers Postgres); unidade/integração/segurança; Testcontainers+JaCoCo no pom
- [ ] 14 ativas → `TSK-071..084`

## ✨ Features
- [x] Auth cookie-only; logout; `MachineEntity` mapping; dashboard liquid-glass; MCP server configurado; `/api/test`
- [ ] 13 ativas → `TSK-085..097`

## 🎨 Frontend
- [x] Dashboard liquid-glass; `frontend/` Vite MPA → `static/app/`; XSS eliminado; responsividade 6/6; tokens coffee+blue
- [ ] 4 ativas → `TSK-098..101`

## 📦 Modularização / Docs
- [x] Domínio 100% puro (extraível); `docs/architecture` + ADRs 001-005 registrados
- [ ] 4 ativas → `TSK-102..105`

## 🧹 Cleanup
- [ ] 2 ativas → `TSK-046` + `TSK-106` (REVIEW)

---

# Task Registry

> 106 tasks · IDs estáveis (não ordenam por categoria) · agrupadas por categoria p/ leitura. Prioridade: 🔴 CRIT · 🔴 Alta · 🟡 Média · 🟢 Baixa. Status: ✅ TODO · 🚧 em execução · ⛔ bloqueado.

## 🔒 Segurança (20)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-001 | `JwtTokenResolver` exige esquema `Bearer ` (hoje aceita sem prefixo); reverter `tokenWithoutBearerPrefix_returns401` → 401 | 🔴 CRIT | ✅ TODO · AGORA (30m) |
| TSK-002 | CORS ausente no `SecurityConfig` — necessário p/ PS3 e web | 🔴 CRIT | ✅ TODO · PRÓXIMO (1h) |
| TSK-003 | mTLS `client-auth=require` + X509 filter | 🔴 | ⛔ BLOQ · ADR-004 |
| TSK-004 | Permissões granulares/scopes (N:N, scopes JWT, `@PreAuthorize`) | 🟡 | ✅ TODO · PRÓXIMO |
| TSK-005 | RateLimitFilter: catch vazios + política por rota (LOGIN/REGISTER/API) | 🔴 CRIT | ✅ TODO (30m) |
| TSK-006 | Higiene secrets: `.env`/`keys/` fora do git + `.env.example` | 🟡 | ✅ TODO · PRÓXIMO |
| TSK-007 | RateLimitFilter log `"RateLimit begin {}"` → SLF4J real | 🟡 | ✅ TODO · AGORA (15m) |
| TSK-008 | MDC request ID / user ID / session ID | 🔴 | ✅ TODO · PRÓXIMO |
| TSK-009 | Cookie domain hardcoded `"coffe_server"` → configurável | 🟡 | ✅ TODO |
| TSK-010 | Security TRACE dev → profile `dev` | 🟢 | ✅ TODO · PRÓXIMO |
| TSK-011 | `CookieDomain` campo `sameSite` (default seguro) | 🟡 | ✅ TODO · PRÓXIMO |
| TSK-012 | Sanitizar secrets/keys em logs | 🟡 | ✅ TODO · PRÓXIMO |
| TSK-013 | Teste rate limit 429 (políticas) | 🟡 | ✅ TODO · PRÓXIMO |
| TSK-014 | API Key resolver (`X-API-Key`) na chain | 🟢 | ✅ TODO |
| TSK-015 | Chaves TLS privadas no JAR (`resources/keys`) → externo/env | 🔴 CRIT | ✅ TODO |
| TSK-016 | CookieMapper SameSite `Lax` → `Strict` | 🟡 | ✅ TODO · AGORA (30m) |
| TSK-017 | JwtAuthenticationFilter: `NE`/`JTWVE` + separar `recoverToken` | 🟡 | ✅ TODO · PRÓXIMO |
| TSK-018 | Confirmar `.gitignore` cobre `keys/` e certs | 🟢 | ✅ TODO |
| TSK-019 | AuthExceptionHandler: 500 genérico sem vazar detalhes | 🟡 | ✅ TODO · PRÓXIMO |
| TSK-020 | `OAuth2UserProvisioningService` → porta `UserRepository` (DIP) | 🔴 | ✅ TODO · PRÓXIMO |

## 🐛 Bugs (11)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-021 | Validação de senha register inconsistente (1..500 vs min 8) | 🟡 | ✅ TODO · PRÓXIMO |
| TSK-022 | DTOs sem Bean Validation (`@NotBlank/@Email/@Size`) | 🟡 | ✅ TODO · PRÓXIMO |
| TSK-023 | `RegisterResponseDTO.Token` maiúsculo (inconsistente com login) | 🟢 | ✅ TODO |
| TSK-024 | `MachineService`: `UsernameNotFoundException` (Spring) → `UserNotFoundException` | 🟡 | ✅ TODO |
| TSK-025 | HomeController: validar redirects pós-build Vite | 🟢 | ✅ TODO |
| TSK-026 | `application.properties:57`: aspas literais no MCP base-url | 🟡 | ✅ TODO · AGORA (10m) |
| TSK-027 | Drift Redis remoto 6380 (rate-limit sem `requirepass`) | 🟡 | ✅ TODO |
| TSK-028 | `JwtTokenService` `@Value` field (claim falsa) → constructor | 🟡 | ✅ TODO · AGORA (30m) |
| TSK-029 | Typo porta: `genareteToken` → `generateToken` | 🟢 | ✅ TODO |
| TSK-030 | Typo porta: `resolver` → `resolve` | 🟢 | ✅ TODO |
| TSK-031 | `isIs_active()` → `isActive()` | 🟢 | ✅ TODO |

## 🧱 Arquitetura (15)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-032 | `TokenResolverManager` infra → application (use case) | 🟡 | ✅ TODO |
| TSK-033 | Domínio anêmico: `Machine.wakeOnLan()`, `ExternalAccount.refreshTokenIfExpired()` | 🟢 | ✅ TODO |
| TSK-034 | Pacotes com typos (`Adpter`, `Provaider`, `Arry`, `Ratelimit`, `LinuxAcount`) — rename breaking | 🟢 | ✅ TODO |
| TSK-035 | `domain/Repository/users` → `User` | 🟢 | ✅ TODO |
| TSK-036 | ADR-002: versionamento `/api/v1` — decidir + aplicar | 🔴 | ✅ TODO |
| TSK-037 | ADR-003: multi-module — decidir | 🔴 | ✅ TODO |
| TSK-038 | ADR-004: CA mTLS — decidir | 🟡 | ✅ TODO |
| TSK-039 | ADR-005: MCP módulo separado — decidir | 🟡 | ✅ TODO |
| TSK-040 | `CalendarController` REST em `mcp/` → application | 🟡 | ✅ TODO |
| TSK-041 | `shared/` cross-cutting — definir destino | 🟢 | ✅ TODO |
| TSK-042 | Regra de camadas: MCP tools delegam p/ use cases | 🟡 | ✅ TODO |
| TSK-043 | OAuth2 Google será refatorado — não usar como base (IA_README) | 🟢 | ✅ TODO |
| TSK-044 | `GoogleCalenderService` (typo `Calender`) — rename | 🟢 | ✅ TODO |
| TSK-045 | Typo porta: `getAdpterConnector` → `getAdapterConnector` | 🟢 | ✅ TODO |
| TSK-054 | `RateLimit` interface infra → domínio | 🟡 | ✅ TODO · AGORA (1h) |

## 🤖 IA (7)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-047 | `getEnvKey()` lê env vars (hoje `"key_temp"`) | 🔴 | ✅ TODO · AGORA (30m) |
| TSK-048 | Listagem Nível 2 (DeepInfra, Novita, Ollama, Cloudflare, Fireworks, Azure) | 🟡 | ✅ TODO |
| TSK-049 | Listagem Nível 3 (Vertex, watsonx, OCI, Bedrock — SDK/SigV4) | 🟡 | ✅ TODO |
| TSK-050 | Listagem Nível 1 (Together `.ai`, Cohere `/v1`, Perplexity `/v1`) | 🟡 | 🚧 IN_PROGRESS |
| TSK-051 | Paginação (pageSize/pageToken/nextPageToken) | 🟢 | ✅ TODO |
| TSK-052 | Expor `getModels()` via MCP/API | 🟢 | ✅ TODO |
| TSK-053 | Regressão IA pós-correções | 🟡 | ✅ TODO |

## 🔌 MCP (5)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-055 | `listEvents` estável durante refactor OAuth2 | 🟡 | ✅ TODO |
| TSK-056 | Tools de máquinas (status/WoL) — depende WoL | 🟡 | ✅ TODO |
| TSK-057 | Tool health check | 🟢 | ✅ TODO |
| TSK-058 | `createEvent()` stub → real | 🔴 | ⛔ BLOQ · OAuth2 refactor |
| TSK-059 | Documentar fluxo MCP | 🟢 | ✅ TODO |

## 🌐 WebSocket (6)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-060 | Protocolo JSON coffe-agent ↔ server | 🟡 | ✅ TODO |
| TSK-061 | Handler + endpoint `/ws/agent` (STOMP `/protocol` existe) | 🟡 | ✅ TODO |
| TSK-062 | Rota WS na security chain | 🟡 | ✅ TODO |
| TSK-063 | Handshake validado (cookie) + smoke com/sem sessão | 🟡 | ✅ TODO |
| TSK-064 | UI `sendRequest()` real p/ WS | 🟢 | ✅ TODO |
| TSK-065 | Doc fluxo WS em `docs/architecture/` | 🟢 | ✅ TODO |

## ⚙️ Infra (5)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-066 | Alinhar Redis remoto (6380) com `.env` | 🟡 | ✅ TODO |
| TSK-067 | Testcontainers no CI (validar Docker/Linux) | 🟡 | ✅ TODO |
| TSK-068 | `smoke-spring.mjs` defasado (`admin_teste`/`Senha123!`) | 🟢 | ✅ TODO |
| TSK-069 | Alinhar JDK local (25) ↔ CI (21 temurin) | 🟡 | ✅ TODO |
| TSK-070 | Pipeline Vite documentado (b540c61) | 🟢 | ✅ TODO |

## 🧪 Testes (14)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-071 | Cobertura `security.Filter` ≥ 83,8% pós-refactors | 🟡 | ✅ TODO |
| TSK-072 | Testes `RateLimitFilter`/Bucket4j (429, política) | 🟡 | ✅ TODO |
| TSK-073 | Testes `OAuth2UserProvisioningService` (UUID, ROLE_*) | 🟡 | ✅ TODO |
| TSK-074 | Persistence `LinuxUser` (0%) — exige mappers/adapters | 🟡 | ⛔ BLOQ |
| TSK-075 | Testes `mcp/` (tools Calendar) | 🟡 | ✅ TODO |
| TSK-076 | Testes `IA` (BaseProvider parse, 404/405) | 🟡 | ✅ TODO |
| TSK-077 | JaCoCo threshold ≥ 50% (enforcement) | 🟡 | ✅ TODO |
| TSK-078 | Regressão typos renomeados (compile) | 🟢 | ✅ TODO |
| TSK-079 | Isolamento 403 ordem-dependente (SecurityContextHolder) | 🟡 | ✅ TODO |
| TSK-080 | Smoke Redis 7.4.10 — manter | 🟢 | ✅ TODO |
| TSK-081 | Fluxo login/register completo | 🟢 | ✅ TODO |
| TSK-082 | Regressão cookie-only (sem JWT no body) | 🟡 | ✅ TODO |
| TSK-083 | Config profiles (h2/test) | 🟢 | ✅ TODO |
| TSK-084 | CI testes bloqueantes (continue-on-error removido) | 🟢 | ✅ TODO |

## ✨ Features (13)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-085 | `UserService` esqueleto → CRUD via porta | 🟡 | ✅ TODO |
| TSK-086 | Wake-on-LAN (magic packet UDP 9) + rota | 🟡 | ✅ TODO |
| TSK-087 | Tailscale integração (status nós) | 🟢 | ✅ TODO |
| TSK-088 | Persistence `ExternalAccount` | 🔴 | ✅ TODO |
| TSK-089 | Persistence `LinuxUser`/`Groups` | 🔴 | ✅ TODO · PRÓXIMO |
| TSK-090 | Vincular `ExternalAccount` no login OAuth2 | 🟡 | ✅ TODO |
| TSK-091 | `/api/health` detalhado | 🟡 | ✅ TODO |
| TSK-092 | Logs ao vivo reais (backend) | 🟢 | ✅ TODO |
| TSK-093 | Jobs assíncronos (BACKUP/RESTORE/SYNC) | 🟡 | ✅ TODO |
| TSK-094 | Backup controllers (`/mnt/mount/data/backups`) | 🟢 | ✅ TODO |
| TSK-095 | `UnixUserService` (parse `/etc/passwd`, `/etc/group`) | 🟢 | ✅ TODO |
| TSK-096 | OAuth2 GitHub (Provider.GITHUB) | 🟡 | ✅ TODO |
| TSK-097 | Bean Validation + `spring-boot-starter-validation` | 🟡 | ✅ TODO |

## 🎨 Frontend (4)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-098 | WebSocket UI `sendRequest` + handshake | 🟡 | ✅ TODO |
| TSK-099 | Logs ao vivo no front (`/api/audit/logs`) | 🟢 | ✅ TODO |
| TSK-100 | Data provider por página (fetch com fallback) | 🟢 | ✅ TODO |
| TSK-101 | Backlog frontend → `frontend/TODO.md` (fonte `frontend/` → `static/app/`) | 🟢 | ✅ TODO |

## 📦 Modularização / Docs (4)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-102 | Extrair `server-mcp` módulo Maven | 🟡 | ⛔ BLOQ · ADR-005 |
| TSK-103 | Publicar `server-domain` Maven Local/GH Packages | 🟢 | ⛔ BLOQ · ADR-003 |
| TSK-104 | README principal (setup, profiles, MCP) | 🟢 | ✅ TODO |
| TSK-105 | Atualizar docs pós-mudanças | 🟢 | ✅ TODO |

## 🧹 Cleanup (2)

| ID | Task | Prio | Status |
|---|---|---|---|
| TSK-046 | Imports fantasma VS Code (ex.: `jdk.tools`) | 🟢 | ✅ TODO |
| TSK-106 | Commit do refactor Token resolvers + typo `IndepotecyKey` | 🔴 | 🚧 REVIEW |

---

# Decisões (ADRs)

| ADR | Decisão | Status |
|---|---|---|
| ADR-001 | JWT + mTLS híbrido (JWT primário, mTLS X.509 opcional) | ✅ Accepted |
| ADR-002 | Versionamento de API no path (`/api/v1`) | 📝 Proposed |
| ADR-003 | Modularização Maven multi-module | 📝 Proposed |
| ADR-004 | CA própria p/ mTLS homelab (step-ca vs self-signed) | ⏳ Open |
| ADR-005 | MCP como módulo separado | 📝 Proposed |

# 🤖 Agent Protocol

1. **Comece pelo Dashboard**: AGORA → PRÓXIMO → BLOQUEADO; depois realize o trabalho com base no registro.
2. **IDs são estáveis** (TSK-001..106): nunca renumere; novas tarefas entram como descobertas e reavalie.
3. **Respeite as camadas** (Clean Architecture): domínio puro; application define contratos; infra implementa (ver `docs/architecture/`).
4. **Não reescreva o TODO**; atualize status/detalhes pontualmente e ajuste métricas se totais mudarem.
5. **Toda decisão estrutural vira ADR**; registre antes de implementar.
6. **Claims antigas vs. código**: auditoria encontrou 5 obsoletas — não confie em `README` antigos sem checar o `frontend/` real.
7. **Testes sempre**: `.\mvnw.cmd test` (231 testes · 38s) antes de dar como concluído.

---

*Última atualização: 2026-08-10 · Auditado por 10 subagentes · Backup do TODO anterior: `C:\Users\Quitto\AppData\Local\Temp\opencode_backup_TODO_20260810.md`*

# Coffee Server Dashboard — TODO (Frontend)

> **Última atualização:** 2026-08-08
> **Base:** Quitto UI System (dark-first, developer-focused, desktop-app aesthetic)
> **Stack:** HTML/CSS/JS vanilla (ES modules) + Thymeleaf (páginas) — servido em `/app/**` pelo Spring Boot

---

## ✅ Concluído — WebSocket integrado ao backend (STOMP `/protocol`)

<span class="badge badge-med">Concluído</span>

### 1. `connection.js` reescrito como cliente STOMP 1.2
- [x] **Endpoint real** — `ws(s)://host/protocol` (rota padrão do WebSocket; mesmo-origem por padrão; `setWsUrl()` para override)
- [x] **Handshake STOMP** — frame `CONNECT` com `accept-version:1.2,1.1,1.0` + `heart-beat:30000,30000` + `host`
- [x] **Assinatura** — no `CONNECTED`, `SUBSCRIBE` em `/queue/coffee-agent` (destino `@SendTo` do backend)
- [x] **Parser de frames** — frames NUL-terminated (`\0`); MESSAGE → `JSON.parse` → dispatch; heartbeat EOL (`\n`) ignorado
- [x] **Envio** — `sendRequest(payload)` faz `SEND` para `/protocol/agent` (app prefix do backend, `@MessageMapping("/agent")`; payload `RequestAgentDTO`)
- [x] **Reconnect backoff** — `RECONNECT_BASE_MS=1000` → `RECONNECT_MAX_MS=30000` (dobra); desiste após `MAX_CONSECUTIVE_FAILURES`; se o endpoint nunca abriu (1º fail), para já (evita storm de handshake no rate limit)
- [x] **API preservada** — `setWsUrl`, `onMessage`, `setupWsListeners`, `startWs`, `disconnect` (+ alias `stopWs`)
- [x] **Auth gate** — `connect()` só abre com sessão local (`getUser()`); `onAuthChange(!user) → stopWs()` no logout (browser nunca fala com o broker sem usuário logado)

### 2. `handlers.js` — detecção de `ResponseContentAgent`
- [x] **Branch novo** — mensagem `{ status, userUse, action, operation }` (sem `type`/`event`) detectada por `typeof action === 'string' && status !== undefined` → toast `ACTION (operation): status` (erro para `error|failed|denied|unavailable`) + `EVENTS.DATA_REFRESH` domain `agent`
- [x] **Eventos legados mantidos** — switch `WS_EVENTS` (machine.status, mcp.invoke, metrics, health) intacto como fallback

### 3. Wiring nas páginas
- [x] **`dashboard.js`** — `registerHandlers({ showToast: toast })` antes de `startWs()`; removido `onMessage: () => {}`
- [x] **`mcp.js`** — import `toast` adicionado; `registerHandlers({ showToast: toast })` antes de `startWs()`; removido `onMessage: () => {}`

### 4. Contrato do backend (referência)
- `WebSocketConfig.java` — endpoint `/protocol` (raw), app prefix `/protocol`, simple broker `/topic` + `/queue`
- `AgentWebSocketController.java` — `@MessageMapping("/agent")` + `@SendTo("/queue/coffee-agent")`; responde `ResponseContentAgent(status, userUse, action, operation)`
- `RequestAgentDTO` = `{ token, idempotencyKey, content: { action, content } }`
- `RateLimitFilter.java` — handshake `/protocol` excluído do rate limit (não queima o bucket)

---

## ✅ Concluído — Redesign do Dashboard (Painel de Controle MCP)

<span class="badge badge-med">Concluído</span>

### 1. `dashboard.html` reescrito (nova ordem de seções)
- [x] **Ordem final:** header → console strip (`data-server-status`) → Usuário → Servidor (KPIs) → Sistema│Rede → Discos → Containers → Máquinas → Calendário│Criar projeto → MCP Manager → Logs ao vivo → statusbar
- [x] **Seção Atividade REMOVIDA** — substituída pelos Logs ao vivo (feed terminal); `data-activity-card` não existe mais
- [x] **Hooks de dados novos** — `data-kpi-row`, `data-system-card`/`data-network-card` + `data-system-meta`, `data-disks-grid` + `data-disks-meta`, `data-containers-count`/`data-containers-body`, `data-machines-count`/`data-machines-list`, `data-calendar-week` + `data-project-form` (project-name/language/path/submit/hint), `data-mcp-preview`, `data-live-logs` + `data-logs-meta`
- [x] **Card Usuário mantido** — renderizado via `renderUserCard()` com identidade real da sessão + `mockCurrentUser` (uid 1000, /bin/bash); skeletons iniciais no HTML
- [x] **Botão refresh** (`data-refresh`) — 500ms com estado `.loading`, re-render parcial; link "MCP" no header da página

### 2. `dashboard.js` reescrito
- [x] **Renderers** — `renderUserCard`, `renderKpiRow` (4 KPIs), `infoRow` + `renderSystem`/`renderNetwork` (info grid mono), `renderDisks` (cards com tom por uso ≥90% danger / ≥80% warn / senão blue), `renderContainers` (toasts restart/stop simulados), `renderMachines` (toast WOL + modal detalhes via lazy-import `Modal.js`), `renderCalendar` (semana seg→dom, hoje destacado, eventos blue/coffee), `renderProjectForm`/`setupProjectForm` (select linguagens, submit simulado com toast + log), `renderMcpPreview`, `renderLiveLogs`/`appendLiveLog`/`startLiveLogs`/`stopLiveLogs`
- [x] **Logs ao vivo** — `setInterval` 3s com `randomLogEntry()`, pausa em `document.hidden`, respeita `prefers-reduced-motion`, lista limitada a 50, auto-scroll
- [x] **Helpers limpos** — removidos padrões `el(...) && ...` de `infoRow`/`renderSystem`/`renderCalendar` (código reto e legível)

### 3. Estilos novos em `pages.css`
- [x] `.info-grid`/`.info-row`/`.info-label`/`.info-value` — dados em mono, hairline sutil, sem gradiente
- [x] `.disks-grid`/`.disk-card`/`.disk-head`/`.disk-pct.blue|warn|danger`/`.disk-foot`
- [x] `.calendar-week`/`.calendar-day(.today)`/`.calendar-event(.blue|.coffee)` — grid 7 colunas, colapsa em <720px
- [x] `.project-form`/`.project-form-actions`
- [x] `.log-card`/`.log-list`/`.log-entry(.success|.error|.info|.warn)` — feed terminal `--bg-console`, max-height 260px, scroll interno

### 4. Bug corrigido
- [x] **`KpiCard.js` renderizava "null"** — `append(..., unit ? el(...) : null)` com `unit` vazio virava texto `null` (ex: `2/4null`). Fix: append condicional com `if (unit)`

### 5. Validação (Playwright, 1280×900, sessionStorage `coffee_user`)
- [x] **Seções na ordem correta** — Usuário → Servidor → Sistema│Rede → Discos → Containers (3 ativos + 1 parado) → Máquinas (4) → Calendário (7 dias, eventos + empty states) → MCP (4 servers) → Logs
- [x] **KPIs sem `null`** — `2/4`, `3`, `3 online`, `22d 20h 0m`
- [x] **Criar projeto funcional** — toast "Projeto coffe-sdk-test criado (Rust) — simulado"
- [x] **Logs ao vivo** — 12 → 13 entradas no tick de 3s
- [x] **Modal de máquina** — lazy import OK, detalhes (IP/MAC/Tailscale/WOL) renderizados, ESC fecha
- [x] **Refresh** — estado `.loading` e re-render OK
- [x] **Validação** — servido diretamente de `static/app/`; única divergência de console são os erros de WebSocket (esperado: servidor estático não expõe `/ws`)

### 6. Revisão por subagentes (2026-08-02)
- [x] **refactoring-engineer** — imports mortos removidos (`qsa`, `updateHeaderStatus`, `togglePanel` em dashboard.js; `updateHeaderStatus` em mcp.js); grid do calendário duplicado corrigido (dias vão direto ao container); ~60 linhas de CSS morto removidas (`.activity-*`, `.terminal-strip`/`.term-*`, `.grid-kpi`); logs ao vivo param de fato no `visibilitychange` e restart chama `stopLiveLogs` primeiro
- [x] **software-architect** — frontend saudável; components "dumb" (troca mock→real não toca componentes); 9 mocks importados no topo de dashboard.js (ponto único de troca); `websocket/handlers.js` nunca chamado e `EVENTS.DATA_REFRESH` sem listener (contrato morto)
- [x] **security-tester** — 0 XSS executável hoje; **F1 corrigido** (`Modal.js`: body string vira `textContent` por padrão, HTML só com `trustHtml: true`; callers dashboard/mcp marcados com flag + comentário); **F2 corrigido** (`mcp.js`: badge de status construído com `el()`+`text`, sem interpolação em HTML string); F3 documentado (role do `sessionStorage` forjável — mitigado por backend cookie HttpOnly)
- [x] **Re-validado Playwright pós-fixes** — modal com `trustHtml:true` renderiza HTML; body default renderiza `<img onerror>` como texto (hasImg false, pwned false); badges MCP `ok/ok/ok/err` OK

---

## ✅ Concluído — Header Liquid Glass (Toolbar)

<span class="badge badge-med">Concluído</span>

### 1. Redesign do header como toolbar liquid glass
- [x] **Toolbar central substituindo o mini-panel de navegação** — Logo + endereço fake do servidor + nav primária inline + status + menu do usuário
- [x] **API `renderHeader` reescrita** — `{ title, active, address }` substitui `path`; navegação embutida no header
  - <span class="tag">components/Header.js</span>
- [x] **MiniPanel virou no-op** — Nav movida para o header; `.mini-panel { display: none; }` por compatibilidade
  - <span class="tag">components/MiniPanel.js</span>
- [x] **Fake server address** — Pill `❯ server/home` (ou `server/mcp`), prompt `❯` azul piscando (`addrBlink`), host/page coloridos, hover com `--coffee-glow`
- [x] **Nav inline** — `.toolbar-nav` com links pill (Dashboard/Máquinas/Containers/MCP), ícone + label, estado `.active` com glow azul
- [x] **Status pill** — `online/offline` com dot, atualizado via `updateHeaderStatus`
- [x] **Dropdown do usuário** — Abre sobre tudo (fix z-index), Logout funcional, `stopPropagation` + click global

### 2. Bug corrigido: dropdown abria escondido atrás do mini-panel
- [x] **Causa raiz confirmada** — `.dropdown-menu` é filho do `.header` (stacking context `z-index: var(--z-sticky)` = 200) e o `.mini-panel` também 200, vindo depois no DOM → cobria o menu aberto
  - `elementFromPoint` retornou `NAV.mini-panel`, `menuIsBehindMini: true`
- [x] **Fix** — Token `--z-header: 250` adicionado em `tokens.css`; `.header` usa `var(--z-header)` (acima de qualquer sticky)
- [x] **Verificado** — `topElement: DIV.dropdown-label`, `headerZ: 250`

### 3. Glass tokens e estilos
- [x] **Tokens glass em `tokens.css`** — `--glass-blur 14px`, `--glass-blur-sm 8px`, `--glass-bg rgba(10,14,20,0.55)`, `--glass-bg-strong rgba(13,18,26,0.72)`, `--glass-border rgba(255,255,255,0.08)`, `--glass-border-strong rgba(255,255,255,0.14)`, `--glass-highlight rgba(255,255,255,0.06)`, `--glass-warm rgba(166,123,91,0.10)`
- [x] **`.header` em glass** — `backdrop-filter: blur(14px) saturate(1.3)`, borda `--glass-border`, `::after` com gradiente café a 18% de opacidade na base, hover intensifica
- [x] **`.dropdown-menu` glass** — `--glass-bg-strong` + blur 8px + borda glass
- [x] **`// ` → `❯ `** — Terminal-prefix trocado em todas as páginas (dashboard, mcp, login) + Modal; cor do prefixo `--blue-accent`

### 4. Responsividade
- [x] **Header nunca estoura** — `.header > * { min-width: 0 }`, `.toolbar-nav` com `overflow-x: auto` (scrollbar oculta), `.header-actions` com `flex-shrink: 1` (mudou de `0` na centralização) + ellipsis no `.user-name`
- [x] **Verificado em 1280×900** — dropdown abre sobre tudo, 4 links de nav, endereço OK
- [x] **Verificado em 640×800** — botão do usuário dentro do header, sem overflow da nav
- [x] **BUG 480px RESOLVIDO** — Media `max-width: 600px` esconde `.toolbar-label` (nav vira ícones-only), links 36px centralizados, `.header` padding reduzido; verificado em 420×800 (`toolbarLabelsVisible: 0`)
  - <span class="tag">layout.css</span>

---

## ✅ Concluído — Centralização do conteúdo do header

<span class="badge badge-phase2">Concluído</span>

- [x] **Header em grid de 3 zonas (centralizado)** — `.header` usa `grid-template-columns: 1fr auto 1fr`; `.header-left` (logo + address) à esquerda, nav no centro, actions à direita
  - [x] `Header.js` reestruturado — `.header-left` envolve logo + address; nav e actions mantidas
  - [x] `layout.css` — grid 3 zonas aplicado; `.toolbar-nav` com `margin-left: auto` encolhe e rola internamente
- [x] **Re-verificado em 1280 / 640 / 480 (com usuário curto E longo)** — `validate-header-final.mjs` · 6/6 PASS
  - [x] **1280px** (`t` e `admin_teste`) — sem overflow, sem overlap btn/nav, nav centrada (delta 4px), address/logo/label/status visíveis
  - [x] **640px** (`t` e `admin_teste`) — sem overflow, btn contido (538–624), sem overlap, address/logo/status ocultos, label visível
  - [x] **480px** (`t` e `admin_teste`) — sem overflow, nav ícones-only, nav centrada (delta 0px)
- [x] **BUG 640px nome longo RESOLVIDO** — `.btn` de 116px transbordava dos 86px do `.header-actions` (justify flex-end + `flex-shrink: 0`) sobre a nav. Fix: `flex-shrink: 1` + `min-width: 0` + `max-width: 100%` no btn, `.user-name` com ellipsis (`overflow: hidden; text-overflow: ellipsis`), `title` do usuário para tooltip do nome completo
  - <span class="tag">components/Header.js · styles/layout.css</span>
- [x] **Screenshots finais em `dropdown-debug/`** — `header-1280-short|long.png`, `header-640-short|long.png`, `header-480-short|long.png`

---

## 📋 Pendências do frontend

<span class="badge badge-phase3">Pendente</span>

- [ ] **Calibrar glass** — Intensidade do blur, tom do café (`--coffee-glow`), animação reativa (hover/scroll) conforme feedback visual
- [ ] **`smoke-spring.mjs` defasado** — Script de smoke usa credenciais inexistentes (`e2e_user`/`Senha123!`); atualizar para `t`/`t` ou `admin_teste_user`/`Senha123!`
- [ ] **WebSocket — validar cookie no handshake do servidor** — `SecurityConfig` mantém o handshake `/protocol` sem `permitAll` explícito (cai em `authenticated()`); hoje a restrição é o auth gate do front + JWT do cookie. Endurecer validação explícita de sessão no backend quando o contrato de handshake for decidido (ver backend TODO — segurança)
- [x] **WebSocket — rate limit `/protocol`** — `RateLimitFilter` agora exclui o handshake `/protocol` (rota padrão do WebSocket); não queima o bucket
- [ ] **WebSocket — smoke test com/sem cookie** — validar que com sessão (`GET /api/test` ok) o socket conecta e recebe `ResponseContentAgent`; sem sessão, `startWs()` não abre conexão
- [ ] **WebSocket — envio real de requests** — `sendRequest(payload)` existe no front, mas nenhuma UI chama; expor quando o backend processar `RequestAgentDTO` de verdade
- [ ] **Logs ao vivo com dados reais** — Hoje usa `randomLogEntry()` do mock; quando o backend expuser eventos MCP ou `/api/audit/logs` (Fase 4/6), ligar no feed de logs
- [ ] **Data provider por página** — `dashboard.js` importa 9 mocks no topo (ponto único de troca). Quando o backend expuser `/api/info/*`, `/api/calendar/events`, `/api/projects`, criar `data/` provider por página (fetch + fallback mock) sem tocar nos renderers
- [ ] **Modal trustHtml** — callers atuais marcam `trustHtml: true` com valores mock; quando a API real chegar, escapar cada campo ou construir o body com `el()`/`textContent`

---

## 🧪 Scripts de verificação

> Local: `C:\Users\Quitto\AppData\Local\Temp\opencode\coffee-smoke\`

| Script | Objetivo | Status |
|---|---|---|
| `debug-stack.mjs` | Confirmar causa raiz do dropdown (stacking context) | ✅ Rodado |
| `debug-toolbar.mjs` | Validar toolbar em 1280×900 | ✅ Rodado |
| `debug-toolbar-small.mjs` | Validar toolbar em 640×800 | ✅ Rodado |
| `debug-toolbar-480.mjs` | Validar toolbar em 480×800 com usuário longo | ✅ Rodado (revelou bug de estouro) |
| `probe-640-long.mjs` | Probe do bug de overlap btn/nav em 640px + nome longo | ✅ Rodado |
| `validate-header-final.mjs` | Validação final 6/6 (1280/640/480 × t/admin_teste) com métrica `btnNavOverlap` + filtro do ruído WebSocket | ✅ Rodado |
| `smoke-spring.mjs` | Smoke geral do servidor | ⚠️ Defasado |

---

> **Nota de estrutura:** O frontend vive em `src/main/resources/static/app/` e é servido diretamente pelo Spring Boot em `/app/**` — sem etapa de cópia/sincronização manual. Editar sempre nesta pasta.

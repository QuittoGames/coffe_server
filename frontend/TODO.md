# Coffee Server Dashboard — TODO (Frontend)

> **Última atualização:** 2026-08-01
> **Base:** Quitto UI System (dark-first, developer-focused, desktop-app aesthetic)
> **Stack:** HTML/CSS/JS vanilla (ES modules) + Thymeleaf (páginas) — servido em `/app/**` pelo Spring Boot

---

## ✅ Concluído — Header Liquid Glass (Toolbar)

<span class="badge badge-med">Concluído</span>

### 1. Redesign do header como toolbar liquid glass
- [x] **Toolbar central substituindo o mini-panel de navegação** — Logo + endereço fake do servidor + nav primária inline + status + menu do usuário
- [x] **API `renderHeader` reescrita** — `{ title, active, address }` substitui `path`; navegação embutida no header
  - `<span class="tag">frontend/components/Header.js</span>`
- [x] **MiniPanel virou no-op** — Nav movida para o header; `.mini-panel { display: none; }` por compatibilidade
  - `<span class="tag">frontend/components/MiniPanel.js</span>`
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

### 4. Responsividade (em progresso)
- [x] **Header nunca estoura** — `.header > * { min-width: 0 }`, `.toolbar-nav` com `overflow-x: auto` (scrollbar oculta), `.header-actions` com `flex-shrink: 0`
- [x] **Verificado em 1280×900** — dropdown abre sobre tudo, 4 links de nav, endereço OK
- [x] **Verificado em 640×800** — botão do usuário dentro do header, sem overflow da nav
- [ ] **BUG ABERTO: header estoura em viewport estreito com nome longo** — Em 480px com `admin_teste_user`, `headerScrollW: 755 > headerClientW: 480`; botão do usuário vai para x=610 (fora da tela)
  - <span class="tag">layout.css .header</span> — precisa encolher/esconder elementos em telas < 600px

---

## 🔄 Em andamento — Centralização do conteúdo do header

<span class="badge badge-phase2">Em progresso</span>

- [ ] **Header em grid de 3 zonas (centralizado)** — Esquerda (logo + address), centro (nav), direita (ações)
  - [x] `Header.js` reestruturado — `.header-left` envolve logo + address; nav e actions mantidas
  - [ ] `layout.css` — converter `.header` de flex para grid `1fr auto 1fr` (ou flex com centralização real) e estilizar `.header-left`
  - [ ] Re-verificar em 1280 / 640 / 480 (com usuário curto E longo)
  - [ ] Screenshots finais em `dropdown-debug/`

---

## 📋 Pendências do frontend

<span class="badge badge-phase3">Pendente</span>

- [ ] **Responsividade fina da toolbar** — Decidir o que esconder em viewports < 600px (ex.: address pill, labels da nav, status) sem perder acesso ao menu do usuário
- [ ] **Calibrar glass** — Intensidade do blur, tom do café (`--coffee-glow`), animação reativa (hover/scroll) conforme feedback visual
- [ ] **`smoke-spring.mjs` defasado** — Script de smoke usa credenciais inexistentes (`e2e_user`/`Senha123!`); atualizar para `t`/`t` ou `admin_teste_user`/`Senha123!`
- [ ] **WebSocket 404** — `wss://localhost:8080/ws` não existe no servidor; decidir se implementa status push real ou remove a tentativa de conexão
- [ ] **Página MCP pós-redesign** — Re-verificar visualmente (`server/mcp`, link ativo MCP)

---

## 🧪 Scripts de verificação

> Local: `C:\Users\Quitto\AppData\Local\Temp\opencode\coffee-smoke\`

| Script | Objetivo | Status |
|---|---|---|
| `debug-stack.mjs` | Confirmar causa raiz do dropdown (stacking context) | ✅ Rodado |
| `debug-toolbar.mjs` | Validar toolbar em 1280×900 | ✅ Rodado |
| `debug-toolbar-small.mjs` | Validar toolbar em 640×800 | ✅ Rodado |
| `debug-toolbar-480.mjs` | Validar toolbar em 480×800 com usuário longo | ✅ Rodado (revelou bug de estouro) |
| `smoke-spring.mjs` | Smoke geral do servidor | ⚠️ Defasado |

---

> **Nota de sincronização:** Editar sempre em `frontend/` e sincronizar com `src/main/resources/static/app/` via `robocopy frontend src\main\resources\static\app /E` + `.\mvnw.cmd -q process-resources`. Ambos espelhados com 0 diffs de hash.

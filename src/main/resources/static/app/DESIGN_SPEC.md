# Coffee Server Dashboard — Design Specification

> **Versão:** 1.0
> **Data:** Agosto 2026
> **Base:** Quitto UI System (Dark-first, Developer-focused, Desktop-app aesthetic) + Coffee Brand Identity
> **Referência Visual:** D:\Projects\MCP\src\web (estética terminal/devtool)

---

## 1. Brand Essence & Design Philosophy

### Identidade Visual
O Coffee Server Dashboard **não é um SaaS genérico** — é uma **ferramenta de engenharia de infraestrutura**. Sua identidade reflete isso: instrumental, precisa, sem enfeites. Como uma máquina de espresso profissional: cada elemento existe por função, não por decoração.

### Personalidade
| Atributo | Manifestação Visual |
|----------|---------------------|
| **Profissional** | Layout limpo, hierarquia clara, sem ruído visual |
| **Técnico** | Tipografia monospace para dados, labels precisos, feedback objetivo |
| **Confiável** | Cores consistentes, estados previsíveis, motion funcional |
| **Instrumental** | Toda interface é uma ferramenta — não uma página de marketing |

### Metáfora Central
> **Uma máquina de café profissional + Terminal de operações.** Painéis escuros, botões iluminados, mostradores precisos. O design não pede atenção — ele serve quem opera.

---

## 2. Color Palette (CSS Custom Properties)

### 2.1 Base Surfaces (Depth Hierarchy)
```css
:root {
  /* ── Superfícies ── */
  --bg-base:      #0B0F14;  /* Canvas principal — fundo da página */
  --bg-elevated:  #111827;  /* Cards, painéis, sidebars — 1 nível acima */
  --bg-hover:     #161B22;  /* Hover de cards e linhas */
  --bg-active:    #1F2937;  /* Estado ativo/pressionado */

  /* ── Bordas ── */
  --border:       #1F2937;  /* Divisores sutis, bordas padrão */
  --border-strong:#374151;  /* Borda ativa, foco, destaque estrutural */
}
```

### 2.2 Coffee Warm (Brand Identity)
```css
  /* ── Coffee Warm ── */
  --coffee-dark:   #3C2415;  /* Background de badges, gradientes */
  --coffee-medium: #6F4E37;  /* Bordas de headings, linhas de destaque */
  --coffee-accent: #A67B5B;  /* Títulos secundários, detalhes */
  --coffee-cream:  #E8D5C4;  /* Headings primários (texto claro) */
  --coffee-latte:  #F5E6D0;  /* Superfícies quentes, backgrounds alternativos */
```

### 2.3 Blue Tech (Action & Interaction)
```css
  /* ── Blue Tech ── */
  --blue-primary:   #1E40AF;  /* Botões primários, links */
  --blue-secondary: #3B82F6;  /* Primary actions, links, foco */
  --blue-accent:    #60A5FA;  /* Hover states, detalhes */
  --blue-electric:  #38BDF8;  /* Highlights, status ativo, alerts */
  --blue-glow:      rgba(59, 130, 246, 0.15); /* Glow para hover/foco */
```

### 2.4 Semantic Colors (State Feedback Only)
```css
  /* ── Semântica ── */
  --success: #22C55E;    /* Verde — sucesso, online */
  --warning: #EAB308;    /* Amarelo — atenção, alerta */
  --error:   #EF4444;    /* Vermelho — erro, crítico */
  --muted:   #6B7280;    /* Cinza — metadados, secundário */
```

### 2.5 Text Colors
```css
  --text-primary:   #F9FAFB;  /* Corpo principal, headings */
  --text-secondary: #D1D5DB;  /* Texto de suporte, labels */
  --text-muted:     #9CA3AF;  /* Placeholder, timestamps, metadados */
  --text-dim:       #5A626E;  /* Texto muito sutil (comentários) */
```

---

## 3. Typography

### 3.1 Font Families
```css
  --font-ui:   'JetBrains Mono', 'Fira Code', 'Cascadia Code', 'Victor Mono', monospace;
  --font-mono: 'JetBrains Mono', 'Fira Code', 'Cascadia Code', monospace;
```
**Decisão:** Uma única família monospace para TUDO (UI + Code). Reforça a estética "terminal/devtool" consistentemente.

### 3.2 Type Scale
```css
  --text-xs:    0.70rem;   /* 11.2px — tags, badges, metadados */
  --text-sm:    0.75rem;   /* 12px   — labels secundários */
  --text-base:  0.875rem;  /* 14px   — corpo padrão */
  --text-md:    0.95rem;   /* 15.2px — corpo destacado */
  --text-lg:    1rem;      /* 16px   — headings menores */
  --text-xl:    1.125rem;  /* 18px   — h3/h4 */
  --text-2xl:   1.3rem;    /* 20.8px — h2 */
  --text-3xl:   1.5rem;    /* 24px   — h1 */
  --text-4xl:   2rem;      /* 32px   — display, hero */
```

### 3.3 Typeface Roles
| Elemento | Size | Weight | Cor | Decoração |
|----------|------|--------|-----|-----------|
| `h1` | `--text-3xl` | 700 | `--coffee-cream` | `border-left: 3px solid --coffee-medium` |
| `h2` | `--text-2xl` | 600 | `--coffee-accent` | `background: linear-gradient(90deg, --coffee-dark, transparent)` |
| `h3` | `--text-xl` | 600 | `--text-secondary` | — |
| `h4` | `--text-lg` | 500 | `--text-secondary` | `text-transform: uppercase; letter-spacing: 1px` |
| Body | `--text-base` | 400 | `--text-primary` | — |
| Small/Label | `--text-sm` | 400 | `--text-muted` | — |
| Code/Data | `--text-sm` | 400 | `--blue-accent` | — |
| Tag/Badge | `--text-xs` | 600 | `--text-muted` | `text-transform: uppercase; letter-spacing: 0.5px` |

---

## 4. Spacing & Sizing System

### 4.1 Base Unit: 8px
```css
  --space-1:  4px;   /* 0.5x — exceção para badges, tags */
  --space-2:  8px;   /* 1x   — unidade base */
  --space-3:  12px;  /* 1.5x — padding interno de cards */
  --space-4:  16px;  /* 2x   — padding padrão */
  --space-5:  24px;  /* 3x   — gap entre seções */
  --space-6:  32px;  /* 4x   — margem entre blocos grandes */
  --space-8:  48px;  /* 6x   — margem entre páginas */
```

### 4.2 Layout Constraints
```css
  --content-max: 1400px;  /* Dashboard amplo */
  --content-narrow: 900px; /* Documentação/leitura */
  --sidebar-width: 260px;
  --header-height: 56px;
  --statusbar-height: 28px;
```

---

## 5. Border Radius System
```css
  --radius-sm:   4px;      /* Badges, tags, inputs, botões */
  --radius:      8px;      /* PADRÃO — Cards, modais, dropdowns */
  --radius-lg:   12px;     /* Containers principais, janelas */
  --radius-xl:   16px;     /* Modais grandes, hero */
  --radius-pill: 9999px;   /* Status pills, avatares */
```

---

## 6. Shadow System
```css
  --shadow-sm:     0 1px 3px rgba(0,0,0,0.3);
  --shadow:        0 2px 8px rgba(0,0,0,0.4);
  --shadow-md:     0 4px 16px rgba(0,0,0,0.5);
  --shadow-lg:     0 8px 24px rgba(0,0,0,0.55);
  --shadow-glow:   0 0 20px var(--blue-glow);  /* Hover interativo */
  --shadow-glow-sm:0 0 8px var(--blue-glow);   /* Focus inputs */
```

---

## 7. Layout Structure (Application Shell)

```
┌─────────────────────────────────────────────────────────────────────┐
│  Header (56px)                                                      │
│  ┌────────────┐                                    ┌────────────┐  │
│  │ Coffee Logo │ Breadcrumb / Path               │ User Avatar │  │
│  └────────────┘                                    └────────────┘  │
├─────────────────────────────────────────────────────────────────────┤
│  ┌──────────┐                                                      │  │
│  │ Sidebar  │              Main Content (flex: 1)                  │  │
│  │ (260px)  │  ┌──────────────────────────────────────────────┐   │  │
│  │          │  │ Section: User Overview                        │   │  │
│  │ Nav:     │  ├──────────────────────────────────────────────┤   │  │
│  │ Machines │  │ Section: Infrastructure Cards (CPU/RAM/Disk)  │   │  │
│  │ Containers│ ├──────────────────────────────────────────────┤   │  │
│  │ MCP      │  │ Section: Machines Grid                        │   │  │
│  │ Settings │  ├──────────────────────────────────────────────┤   │  │
│  │          │  │ Section: Containers List                      │   │  │
│  │          │  ├──────────────────────────────────────────────┤   │  │
│  │          │  │ Section: MCP Manager Preview                  │   │  │
│  │          │  └──────────────────────────────────────────────┘   │  │
│  │          │                                                     │  │
│  └──────────┘                                                     │  │
├─────────────────────────────────────────────────────────────────────┤
│  Status Bar (28px)  │  Connection: ●  │  Version  │  Uptime      │
└─────────────────────────────────────────────────────────────────────┘
```

**Regras:**
- Sidebar **colapsível** (toggle no header)
- Scroll **interno no main** — nunca no viewport global
- Grid responsivo: `repeat(auto-fit, minmax(320px, 1fr))`

---

## 8. Component Library

### 8.1 Card (`.card`)
```css
.card {
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: var(--space-4);
  box-shadow: var(--shadow);
  transition: background 0.15s, border-color 0.15s, transform 0.15s, box-shadow 0.15s;
}
.card:hover {
  background: var(--bg-hover);
  border-color: var(--border-strong);
}
.card-interactive:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-glow);
  border-color: var(--blue-secondary);
}
.card-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: var(--space-3);
  padding-bottom: var(--space-2);
  border-bottom: 1px solid var(--border);
}
.card-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--coffee-accent);
  text-transform: uppercase;
  letter-spacing: 1px;
}
```

### 8.2 Button (`.btn`)
```css
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  border-radius: var(--radius-sm);
  padding: var(--space-2) var(--space-4);
  font-size: var(--text-base);
  font-weight: 500;
  font-family: var(--font-ui);
  cursor: pointer;
  border: 1px solid transparent;
  transition: all 0.15s;
}
.btn:disabled { opacity: 0.5; cursor: not-allowed; }

/* Variants */
.btn-primary {
  background: var(--blue-secondary);
  color: white;
  border-color: var(--blue-secondary);
}
.btn-primary:hover:not(:disabled) {
  background: var(--blue-accent);
  border-color: var(--blue-accent);
  box-shadow: var(--shadow-glow);
}

.btn-secondary {
  background: transparent;
  color: var(--text-primary);
  border-color: var(--border);
}
.btn-secondary:hover:not(:disabled) {
  background: var(--bg-hover);
  border-color: var(--border-strong);
}

.btn-danger {
  background: transparent;
  color: var(--error);
  border-color: var(--error);
}
.btn-danger:hover:not(:disabled) {
  background: rgba(239, 68, 68, 0.1);
  border-color: var(--error);
}

.btn-ghost {
  background: transparent;
  color: var(--text-secondary);
  border-color: transparent;
}
.btn-ghost:hover:not(:disabled) {
  color: var(--text-primary);
  background: var(--bg-hover);
}

.btn-sm { padding: var(--space-1) var(--space-3); font-size: var(--text-sm); }
.btn-lg { padding: var(--space-3) var(--space-6); font-size: var(--text-lg); }
```

### 8.3 Input (`.input`)
```css
.input {
  width: 100%;
  background: var(--bg-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  color: var(--text-primary);
  padding: var(--space-2) var(--space-3);
  font-size: var(--text-base);
  font-family: var(--font-ui);
  transition: border-color 0.15s, box-shadow 0.15s;
}
.input::placeholder { color: var(--text-dim); }
.input:focus {
  outline: none;
  border-color: var(--blue-secondary);
  box-shadow: var(--shadow-glow-sm);
}
.input-error { border-color: var(--error); }
.input-error:focus { box-shadow: 0 0 0 3px rgba(239,68,68,0.2); }
.input-success { border-color: var(--success); }
```

### 8.4 Table (`.table`)
```css
.table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  border-radius: var(--radius);
  overflow: hidden;
  border: 1px solid var(--border);
  font-size: var(--text-sm);
}
.table th {
  background: var(--coffee-dark);
  color: var(--coffee-cream);
  font-weight: 600;
  text-align: left;
  padding: var(--space-2) var(--space-3);
  font-size: var(--text-xs);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  position: sticky;
  top: 0;
  z-index: 1;
}
.table td {
  padding: var(--space-2) var(--space-3);
  border-top: 1px solid var(--border);
  background: var(--bg-elevated);
  color: var(--text-secondary);
}
.table tbody tr:hover td {
  background: var(--bg-hover);
}
.table tbody tr.selected td {
  background: rgba(59, 130, 246, 0.08);
}
```

### 8.5 Badge / Tag / Status Pill
```css
.badge {
  display: inline-flex;
  align-items: center;
  padding: 2px var(--space-2);
  border-radius: var(--radius-pill);
  font-size: var(--text-xs);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.badge-online { background: rgba(34,197,94,0.15); color: var(--success); border: 1px solid rgba(34,197,94,0.2); }
.badge-offline { background: rgba(239,68,68,0.12); color: var(--error); border: 1px solid rgba(239,68,68,0.18); }
.badge-warning { background: rgba(234,179,8,0.15); color: var(--warning); border: 1px solid rgba(234,179,8,0.2); }
.badge-processing { background: rgba(59,130,246,0.15); color: var(--blue-accent); border: 1px solid rgba(59,130,246,0.2); }

.tag {
  display: inline-block;
  padding: 1px var(--space-2);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
  font-family: var(--font-mono);
  background: var(--bg-hover);
  color: var(--text-muted);
  border: 1px solid var(--border);
}
```

### 8.6 Progress Bar (`.progress`)
```css
.progress {
  height: 6px;
  border-radius: 3px;
  overflow: hidden;
  background: var(--bg-hover);
}
.progress-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.3s var(--easing-standard);
}
.progress-fill.normal { background: linear-gradient(90deg, var(--success), var(--blue-secondary)); }
.progress-fill.warning { background: linear-gradient(90deg, var(--warning), var(--error)); }
.progress-fill.critical { background: linear-gradient(90deg, var(--error), #dc2626); }
```

### 8.7 Modal (`.modal`)
```css
.modal-overlay {
  position: fixed; inset: 0;
  background: rgba(0,0,0,0.7);
  display: flex; align-items: center; justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.2s var(--easing-entrance);
}
.modal {
  background: var(--bg-elevated);
  border: 1px solid var(--border-strong);
  border-radius: var(--radius-lg);
  width: 90%;
  max-width: 560px;
  max-height: 85vh;
  display: flex; flex-direction: column;
  animation: slideUp 0.3s var(--easing-entrance);
}
.modal-header {
  padding: var(--space-4);
  border-bottom: 1px solid var(--border);
  display: flex; align-items: center; justify-content: space-between;
}
.modal-title { font-size: var(--text-xl); font-weight: 600; color: var(--coffee-cream); }
.modal-close { background: none; border: none; color: var(--text-muted); cursor: pointer; padding: var(--space-1); }
.modal-body { padding: var(--space-4); overflow-y: auto; }
.modal-footer { padding: var(--space-3) var(--space-4); border-top: 1px solid var(--border); display: flex; justify-content: flex-end; gap: var(--space-2); }
```

### 8.8 Toast (`.toast`)
```css
.toast-container { position: fixed; bottom: var(--space-5); right: var(--space-5); z-index: 2000; display: flex; flex-direction: column; gap: var(--space-2); }
.toast { padding: var(--space-3) var(--space-4); border-radius: var(--radius); font-size: var(--text-sm); display: flex; align-items: center; gap: var(--space-2); min-width: 280px; max-width: 420px; animation: slideInRight 0.3s var(--easing-entrance); box-shadow: var(--shadow-lg); }
.toast-success { background: rgba(34,197,94,0.9); color: white; border: 1px solid var(--success); }
.toast-error { background: rgba(239,68,68,0.9); color: white; border: 1px solid var(--error); }
.toast-info { background: rgba(59,130,246,0.9); color: white; border: 1px solid var(--blue-secondary); }
.toast-warning { background: rgba(234,179,8,0.9); color: #0B0F14; border: 1px solid var(--warning); }
```

### 8.9 Skeleton Loading (`.skeleton`)
```css
.skeleton { background: linear-gradient(90deg, var(--bg-elevated) 25%, var(--bg-hover) 50%, var(--bg-elevated) 75%); background-size: 200% 100%; animation: shimmer 1.5s infinite; border-radius: var(--radius-sm); }
.skeleton-text { height: 1rem; width: 100%; }
.skeleton-title { height: 1.5rem; width: 60%; }
.skeleton-card { height: 200px; width: 100%; }
@keyframes shimmer { 0% { background-position: -200% 0; } 100% { background-position: 200% 0; } }
```

### 8.10 Breadcrumb / Path Bar (`.path-bar`)
```css
.path-bar {
  display: flex; align-items: center; gap: var(--space-1);
  padding: var(--space-2) var(--space-3);
  background: var(--bg-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  font-size: var(--text-sm);
  overflow-x: auto;
}
.path-seg { color: var(--text-secondary); }
.path-seg:last-child { color: var(--blue-accent); font-weight: 500; }
.path-sep { color: var(--border); }
```

---

## 9. Motion & Animation

### 9.1 Timing Tokens
```css
  --duration-fast:   100ms;   /* Micro-interações: hover, focus */
  --duration-normal: 200ms;   /* Transições padrão: accordion, toggle */
  --duration-slow:   300ms;   /* Transições de layout: sidebar, modal */

  --easing-standard:  cubic-bezier(0.4, 0, 0.2, 1);
  --easing-entrance:  cubic-bezier(0, 0, 0.2, 1);
  --easing-exit:      cubic-bezier(0.4, 0, 1, 1);
```

### 9.2 Keyframe Animations
```css
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes slideUp { from { transform: translateY(8px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
@keyframes slideDown { from { transform: translateY(-8px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
@keyframes slideInRight { from { transform: translateX(100%); opacity: 0; } to { transform: translateX(0); opacity: 1; } }
@keyframes pulse { 0%,100% { opacity: 1; } 50% { opacity: 0.5; } }
@keyframes spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
```

### 9.3 Reduced Motion
```css
@media (prefers-reduced-motion: reduce) {
  *, *::before, *::after {
    animation-duration: 0.01ms !important;
    transition-duration: 0.01ms !important;
  }
}
```

---

## 10. Iconography

| Uso | Tamanho | Estilo |
|-----|---------|--------|
| Inline com texto/metadados | 16px | Lucide outline, 2px stroke |
| UI Controls (botões, toolbar) | 20px | Lucide outline, 2px stroke |
| Primary actions / Navegação | 24px | Lucide outline, 2px stroke |
| Estados vazios / Status grandes | 32px | Lucide outline, 2px stroke |

**Regra:** `color: currentColor` — herdam cor do texto pai. `stroke-width: 2`.

---

## 11. UX Principles (Non-Negotiable)

1. **Keyboard-first** — Todo componente interativo tem foco visível (`:focus-visible { outline: 2px solid var(--blue-secondary); outline-offset: 2px; }`) e atalhos
2. **Consistência visual** — Mesmo componente = mesmo comportamento visual
3. **Estados claros** — Default, hover, active, focus, disabled, loading, error — todos definidos
4. **Sem gradientes decorativos** — Cor sólida ou token semântico apenas
5. **Sem animações pesadas** — Máx 300ms, preferencialmente 150-200ms
6. **Scroll interno** — Layout nunca scrolla globalmente; painéis scrollam independentemente
7. **Empty state = CTA** — Nunca ilustrações decorativas em estados vazios
8. **Feedback imediato** — Toda ação do usuário produz resposta visual em <100ms
9. **Terminal aesthetic** — Prefix `//` em headings, monospace everywhere, green values para outputs

---

## 12. Copy & Tone of Voice

| Ruim | Bom |
|------|-----|
| "Ocorreu um erro inesperado" | "Falha ao conectar ao banco. Verifique se o PostgreSQL está rodando." |
| "Submeter" | "Criar máquina" |
| "Operação realizada com sucesso" | "Máquina adicionada." |
| "Sessão expirada" | "Sua sessão expirou. Faça login novamente." |

**Labels de Ação:**
| Ação | Label |
|------|-------|
| Criar | "+ Criar" |
| Editar | "Editar" |
| Excluir | "Excluir" |
| Salvar | "Salvar" |
| Cancelar | "Cancelar" |
| Confirmar | "Confirmar" |
| Voltar | "Voltar" |

---

## 13. Responsive Breakpoints

```css
/* Desktop First — Breakpoints descendentes */
@media (max-width: 1200px) { /* Sidebar compacta */ }
@media (max-width: 900px)  { /* Sidebar vira drawer */ }
@media (max-width: 720px)  { /* Cards empilham, tabelas ocultam colunas */ }
@media (max-width: 600px)  { /* Grid 1 coluna, modais full-width */ }
@media (max-width: 420px)  { /* Padding reduzido, font-size base */ }
```

---

## 14. Accessibility (WCAG AA)

| Combinação | Ratio | Status |
|------------|-------|--------|
| `--text-primary` on `--bg-base` | 16.5:1 | ✅ AAA |
| `--text-secondary` on `--bg-base` | 11.2:1 | ✅ AAA |
| `--text-muted` on `--bg-base` | 7.5:1 | ✅ AA |
| `--blue-secondary` on `--bg-base` | 5.8:1 | ✅ AA |
| `--error` on `--bg-base` | 5.3:1 | ✅ AA |
| `--success` on `--bg-base` | 6.1:1 | ✅ AA |

---

## 15. File Structure

```
src/main/resources/static/app/
├── index.html                 # Entry point (redirects to login or dashboard)
├── pages/
│   ├── login.html             # Login page
│   ├── dashboard.html         # Main dashboard
│   └── mcp.html               # MCP Manager
├── components/
│   ├── Card.js                # Card component
│   ├── Button.js              # Button component
│   ├── Input.js               # Input component
│   ├── Table.js               # Table component
│   ├── Modal.js               # Modal component
│   ├── Toast.js               # Toast notifications
│   ├── Badge.js               # Badge/Status pills
│   ├── Progress.js            # Progress bars
│   ├── Skeleton.js            # Skeleton loading
│   ├── PathBar.js             # Breadcrumb/Path bar
│   ├── Sidebar.js             # Navigation sidebar
│   ├── Header.js              # Top header
│   ├── StatusBar.js           # Bottom status bar
│   ├── UserAvatar.js          # User avatar dropdown
│   ├── MachineCard.js         # Machine display card
│   ├── ContainerRow.js        # Container table row
│   └── MCPCard.js             # MCP server card
├── services/
│   ├── api.js                 # API client (fetch wrapper)
│   ├── auth.js                # Auth state management
│   ├── websocket.js           # WebSocket connection manager
│   ├── machine.js             # Machine data service
│   ├── container.js           # Container data service
│   ├── mcp.js                 # MCP data service
│   └── notification.js        # Toast/notification service
├── api/
│   ├── endpoints.js           # Endpoint constants
│   ├── types.js               # TypeScript-like JSDoc types
│   └── mappers.js             # DTO ↔ Domain mappers
├── websocket/
│   ├── connection.js          # WebSocket connection
│   ├── events.js              # Event definitions
│   └── handlers.js            # Message handlers
├── auth/
│   ├── session.js             # Session validation
│   ├── login.js               # Login flow
│   └── logout.js              # Logout flow
├── utils/
│   ├── dom.js                 # DOM helpers
│   ├── format.js              # Formatting (bytes, dates, durations)
│   ├── validation.js          # Client-side validation
│   └── constants.js           # App constants
├── mock/
│   ├── user.js                # Mock user data
│   ├── machines.js            # Mock machines data
│   ├── containers.js          # Mock containers data
│   └── mcp.js                 # Mock MCP data
├── assets/
│   ├── logo.svg               # Coffee Server logo
│   └── favicon.ico
└── styles/
    ├── tokens.css             # CSS custom properties (design tokens)
    ├── base.css               # Reset, base styles
    ├── components.css         # Component styles
    ├── layout.css             # Layout (shell, grid, sidebar)
    ├── pages.css              # Page-specific styles
    ├── utilities.css          # Utility classes
    └── main.css               # Entry point (imports all)
```

---

## 16. Implementation Priority

| Phase | Deliverables | Dependencies |
|-------|-------------|--------------|
| **1. Foundation** | tokens.css, base.css, main.css, DOM utils, format utils | — |
| **2. Core Components** | Button, Card, Input, Badge, Progress, Skeleton | Phase 1 |
| **3. Layout Components** | Header, Sidebar, StatusBar, PathBar, Modal, Toast | Phase 2 |
| **4. Auth System** | login.html, auth service, session management, cookie handling | Phase 1 |
| **5. API Layer** | api.js, endpoints, types, mappers | Phase 1 |
| **6. WebSocket Structure** | connection, events, handlers (mock-ready) | Phase 1 |
| **7. Dashboard Page** | dashboard.html, MachineCard, ContainerRow, mock data | Phases 2-6 |
| **8. Login Page** | login.html, login form, validation, redirect logic | Phase 4 |
| **9. MCP Page** | mcp.html, MCPCard, mock data | Phases 2-3 |
| **10. Polish** | Responsive, animations, a11y, error states, empty states | All |

---

> **Nota:** Este documento é a fonte da verdade para decisões visuais. Qualquer desvio deve ser justificado e documentado.
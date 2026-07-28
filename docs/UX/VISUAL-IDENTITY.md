# Coffee Server — Visual Identity & Design System

> **Propósito:** Documento oficial de identidade visual do ecossistema Coffee. Define cores, tipografia, componentes, tom e diretrizes de marca para garantir consistência entre frontend (Thymeleaf), CLI, MCP tools e futuros clientes.
>
> **Baseline:** Quitto UI System (dark-first, developer-focused, desktop-app aesthetic)
> **Inspiração:** Windows 11 Fluent UI, GNOME Adwaita, VS Code, JetBrains IDEs, terminal tools

---

## Sumário

1. [Brand Essence](#1-brand-essence)
2. [Color Palette](#2-color-palette)
3. [Typography](#3-typography)
4. [Spacing & Sizing](#4-spacing--sizing)
5. [Border Radius System](#5-border-radius-system)
6. [Shadow System](#6-shadow-system)
7. [Layout Structure](#7-layout-structure)
8. [Component Library](#8-component-library)
9. [Logo & Branding](#9-logo--branding)
10. [Iconography](#10-iconography)
11. [Motion & Animation](#11-motion--animation)
12. [UX Principles](#12-ux-principles)
13. [Copy & Tone of Voice](#13-copy--tone-of-voice)
14. [Accessibility](#14-accessibility)
15. [Dark Mode Only](#15-dark-mode-only)

---

## 1. Brand Essence

### Identidade

O Coffee Server não é um produto vendido — é uma **ferramenta de engenharia**. Sua identidade visual reflete isso: instrumental, precisa, sem enfeites. Como uma máquina de espresso profissional: cada elemento existe por função, não por decoração.

### Personalidade

| Atributo | Como se manifesta |
|----------|-------------------|
| **Profissional** | Layout limpo, hierarquia clara, sem ruído visual |
| **Técnico** | Tipografia monospace para dados, labels precisos, feedback objetivo |
| **Confiável** | Cores consistentes, estados previsíveis, motion funcional |
| **Instrumental** | Toda interface é uma ferramenta — não uma página de marketing |

### Metáfora Central

> **Uma máquina de café profissional.** Painéis escuros, botões iluminados, mostradores precisos. Cada controle tem uma função clara. O design não pede atenção — ele serve quem opera.

### Público

- **Quitto** — administrador do homelab, engenheiro dos sistemas
- **Agentes de IA** — consomem MCP tools, não UI visual
- **Usuários convidados** — acesso limitado por permissões

---

## 2. Color Palette

### 2.1 Base (Surface)

As superfícies seguem uma hierarquia clara de profundidade. O fundo mais escuro é a base; cada camada acima é sutilmente mais clara.

```css
/* ── Superfícies ── */
--bg-base:      #0B0F14;  /* Canvas principal — fundo da página */
--bg-elevated:  #111827;  /* Cards, painéis, sidebars — 1 nível acima */
--bg-hover:     #161B22;  /* Hover de cards e linhas */
--bg-active:    #1F2937;  /* Estado ativo/pressionado */

/* ── Bordas ── */
--border:       #1F2937;  /* Divisores sutis, bordas padrão */
--border-strong:#374151;  /* Borda ativa, foco, destaque estrutural */
```

**Regra de profundidade:** Use `--bg-elevated` para componentes que precisam se destacar do canvas. Nunca use `--bg-base` dentro de `--bg-elevated` — a hierarquia visual se perde.

### 2.2 Coffee Warm (Identidade Principal)

Os tons de café são a **assinatura visual** do ecossistema. Eles aparecem em:

- Títulos e headings
- Badges de fase/status
- Acentos em bordas e linhas divisoras
- Elementos de destaque não-interativos

```css
/* ── Coffee Warm ── */
--coffee-dark:  #3C2415;  /* Background de badges, gradientes */
--coffee-medium:#6F4E37;  /* Bordas de headings, linhas de destaque */
--coffee-accent:#A67B5B;  /* Títulos secundários, detalhes */
--coffee-cream: #E8D5C4;  /* Headings primários (texto claro) */
--coffee-latte: #F5E6D0;  /* Superfícies quentes, backgrounds alternativos */
```

**Hierarquia de uso:**
- `--coffee-cream`: Headings `h1`, títulos de seção
- `--coffee-accent`: Headings `h2`, labels de badge
- `--coffee-medium`: Bordas esquerdas de heading, linhas decorativas
- `--coffee-dark`: Fundo de badges, gradientes de heading

### 2.3 Blue Tech (Ação e Interação)

O azul é a **cor de ação**. Botões, links, inputs focados, indicadores de atividade — tudo que é interativo usa a paleta azul.

```css
/* ── Blue Tech ── */
--blue-primary:   #1E40AF;  /* Botões primários, links */
--blue-secondary: #3B82F6;  /* Primary actions, links, foco */
--blue-accent:    #60A5FA;  /* Hover states, detalhes */
--blue-electric:  #38BDF8;  /* Highlights, status ativo, alerts */
```

**Regra:** Azul é para ação. Café é para identidade. Se um elemento é clicável, use azul. Se é decorativo/estrutural, use café.

### 2.4 Semantic Colors

Cores semânticas são reservadas para **feedback de estado**:
- `--success`: Operação concluída, status online
- `--warning`: Atenção, recurso quase no limite
- `--error`: Erro, falha, status crítico
- `--muted`: Texto secundário, metadados, timestamps

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
```

**Contraste mínimo (WCAG AA):**
- `--text-primary` sobre `--bg-base`: **16.5:1** ✅
- `--text-secondary` sobre `--bg-base`: **11.2:1** ✅
- `--text-muted` sobre `--bg-base`: **7.5:1** ✅

### 2.6 Badge Colors (Sistema de Progresso)

```css
.badge-fire { background: #EF4444; color: white; }    /* 🔥 Crítico/Blqueante */
.badge-high { background: #F97316; color: white; }    /* 🟡 Alta prioridade */
.badge-med  { background: #EAB308; color: #0B0F14; }  /* 🟢 Média prioridade */
.badge-low  { background: #22C55E; color: #0B0F14; }  /* 🔵 Baixa prioridade */
```

### 2.7 Phase Badges

```css
.badge-phase1 { background: var(--coffee-dark); color: var(--coffee-cream); border: 1px solid var(--coffee-accent); }
.badge-phase2 { background: #1E3A5F; color: var(--blue-accent); border: 1px solid var(--blue-secondary); }
.badge-phase3 { background: #1A3A1A; color: #4ADE80; border: 1px solid #22C55E; }
```

### 2.8 Progress Bars

```css
.progress-fill.fire { background: linear-gradient(90deg, #EF4444, #F97316); }
.progress-fill.high { background: linear-gradient(90deg, #F97316, #EAB308); }
.progress-fill.med  { background: linear-gradient(90deg, #EAB308, #22C55E); }
.progress-fill.low  { background: linear-gradient(90deg, #22C55E, #3B82F6); }
```

---

## 3. Typography

### 3.1 Font Families

```css
--font-ui:   'Segoe UI', system-ui, -apple-system, 'Inter', sans-serif;
--font-mono: 'JetBrains Mono', 'Fira Code', 'Cascadia Code', 'Victor Mono', monospace;
```

| Papel | Família | Uso |
|-------|---------|-----|
| **UI** | `--font-ui` (Segoe UI / system-ui) | Headings, labels, corpo, botões |
| **Code** | `--font-mono` (JetBrains Mono) | Código, dados, timestamps, metadados técnicos |

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

| Elemento | Size | Weight | Family | Cor |
|----------|------|--------|--------|-----|
| `h1` | `--text-3xl` | 700 | `--font-ui` | `--coffee-cream` |
| `h2` | `--text-2xl` | 600 | `--font-ui` | `--coffee-accent` |
| `h3` | `--text-xl` | 600 | `--font-ui` | `--text-secondary` |
| `h4` | `--text-lg` | 500 | `--font-ui` | `--text-secondary` |
| Body | `--text-base` | 400 | `--font-ui` | `--text-primary` |
| Small | `--text-sm` | 400 | `--font-ui` | `--text-muted` |
| Code | `--text-sm` | 400 | `--font-mono` | `--blue-light` |
| Tag | `--text-xs` | 600 | `--font-mono` | `--text-muted` |

### 3.4 Heading Decorations

Headings `h2` têm um tratamento visual especial — gradiente e borda esquerda:

```css
h2 {
  background: linear-gradient(90deg, var(--coffee-dark), transparent);
  border-left: 3px solid var(--coffee-accent);
  padding: 8px 12px;
  border-radius: 0 var(--radius) var(--radius) 0;
}
```

---

## 4. Spacing & Sizing

### 4.1 Base Unit

O sistema usa **8px como unidade base** de espaçamento. Todos os valores de margin, padding e gap são múltiplos de 8px:

```css
--space-1:  4px;   /* .25x — exceção para badges, tags */
--space-2:  8px;   /* 1x   — unidade base */
--space-3:  12px;  /* 1.5x — padding de cards */
--space-4:  16px;  /* 2x   — padding padrão */
--space-5:  24px;  /* 3x   — seções */
--space-6:  32px;  /* 4x   — entre seções grandes */
--space-8:  48px;  /* 6x   — margem entre páginas */
```

### 4.2 Layout Max-Width

Para documentação e dashboards:

```css
--content-max: 900px;   /* Leitura confortável */
--content-wide: 1200px; /* Dashboards densos */
```

---

## 5. Border Radius System

```css
--radius-sm:   4px;    /* Badges, tags, indicators */
--radius:      8px;    /* PADRÃO — botões, inputs, cards, menus */
--radius-lg:   12px;   /* Containers principais, painéis, modais */
--radius-window: 10px; /* Janelas, modais grandes, popovers */
--radius-pill: 9999px; /* Pílulas, avatares, badges circulares */
```

**Regra:** Use `--radius` (8px) para 90% dos componentes. `--radius-lg` só para containers que agrupam múltiplos componentes.

---

## 6. Shadow System

```css
--shadow-soft:   0 2px 8px rgba(0, 0, 0, 0.25);
--shadow-default: 0 2px 8px rgba(0, 0, 0, 0.5);
--shadow-medium: 0 4px 16px rgba(0, 0, 0, 0.55);
--shadow-window: 0 8px 24px rgba(0, 0, 0, 0.35);
--shadow-strong: 0 12px 32px rgba(0, 0, 0, 0.45);
```

**Uso:**
- `--shadow-soft`: Hover de cards
- `--shadow-default`: Cards, tabelas, blocos de código
- `--shadow-medium`: Dropdowns, menus, popovers
- `--shadow-window`: Modais, janelas
- `--shadow-strong`: Notificações toast, alertas flutuantes

---

## 7. Layout Structure

### 7.1 Application Shell (Desktop-First)

```
┌──────────────────────────────────────────────┐
│  Header (48-56px)                             │
│  Logo + Breadcrumb + User Menu + Status       │
├──────────┬───────────────────────────────────┤
│ Sidebar  │ Main Content (flex: 1)             │
│ (240-    │ Internal scroll                    │
│  280px)  │                                    │
│          │ ┌─────────────────────────────────┐│
│ Nav      │ │ Card / Panel / List             ││
│ Machines │ │                                 ││
│ Backup   │ │                                 ││
│ Users    │ └─────────────────────────────────┘│
│ Config   │                                    │
├──────────┴───────────────────────────────────┤
│  Status Bar (24-32px)                         │
│  Service Status | Uptime | Version            │
└──────────────────────────────────────────────┘
```

### 7.2 Document Layout (Documentação)

Para páginas de documentação especificações:

```
┌──────────────────────────────────────────────┐
│  Header compacto (logo + breadcrumb)          │
├──────────────────────────────────────────────┤
│                                               │
│  # Título (h1)                                │
│  Blockquote com metadados                     │
│                                               │
│  ## Seção (h2)                                │
│                                               │
│  ### Subseção (h3)                            │
│                                               │
│  Tabelas / Listas / Blocos de código          │
│                                               │
├──────────────────────────────────────────────┤
│  Footer (copyright, versão)                   │
└──────────────────────────────────────────────┘
```

### 7.3 Grid & Cards

Cards em grid seguem uma largura mínima de 320px com auto-fill:

```css
.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: var(--space-4);
}
```

---

## 8. Component Library

### 8.1 Card

```css
.card {
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 16px;
  box-shadow: var(--shadow-soft);
  transition: background 0.15s, border-color 0.15s;
}
.card:hover {
  background: var(--bg-hover);
  border-color: var(--border-strong);
}
```

**Variantes:**
- `.card` — padrão
- `.card-interactive` — com hover state (clicável)
- `.card-danger` — borda `--error`
- `.card-success` — borda `--success`

### 8.2 Button

```css
.btn {
  border-radius: var(--radius);
  padding: 8px 16px;
  font-weight: 500;
  font-size: var(--text-base);
  cursor: pointer;
  transition: background 0.15s, box-shadow 0.15s;
}
.btn-primary {
  background: var(--blue-secondary);
  color: white;
  border: 1px solid transparent;
}
.btn-primary:hover {
  background: var(--blue-accent);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
}
.btn-secondary {
  background: transparent;
  color: var(--text-primary);
  border: 1px solid var(--border);
}
.btn-secondary:hover {
  background: var(--bg-hover);
  border-color: var(--border-strong);
}
.btn-danger {
  background: transparent;
  color: var(--error);
  border: 1px solid var(--error);
}
.btn-danger:hover {
  background: rgba(239, 68, 68, 0.1);
}
```

### 8.3 Input

```css
.input {
  background: var(--bg-base);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  color: var(--text-primary);
  padding: 8px 12px;
  font-size: var(--text-base);
  transition: border-color 0.15s, box-shadow 0.15s;
}
.input:focus {
  border-color: var(--blue-secondary);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
  outline: none;
}
.input::placeholder {
  color: var(--text-muted);
}
.input-error {
  border-color: var(--error);
}
.input-error:focus {
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.15);
}
```

### 8.4 Table

```css
table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  border-radius: var(--radius);
  overflow: hidden;
  border: 1px solid var(--border);
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
  background: var(--bg-elevated);
  color: var(--text-secondary);
}
tr:hover td {
  background: var(--bg-hover);
}
```

### 8.5 Badge / Tag

```css
.badge {
  display: inline-block;
  padding: 2px 10px;
  border-radius: var(--radius-sm);
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.tag {
  display: inline-block;
  padding: 1px 8px;
  border-radius: var(--radius-sm);
  font-size: 0.7rem;
  font-family: var(--font-mono);
  background: var(--bg-hover);
  color: var(--text-muted);
  border: 1px solid var(--border);
}
```

### 8.6 Blockquote

```css
blockquote {
  border-left: 3px solid var(--coffee-medium);
  background: var(--bg-elevated);
  padding: 12px 16px;
  border-radius: 0 var(--radius) var(--radius) 0;
  color: var(--text-muted);
  font-size: 0.9rem;
}
blockquote strong {
  color: var(--coffee-accent);
}
```

### 8.7 Window / Modal

```css
.window {
  background: var(--bg-elevated);
  border: 1px solid var(--border-strong);
  border-radius: var(--radius-window);
  box-shadow: var(--shadow-window);
}
.window-header {
  padding: 12px 16px;
  border-bottom: 1px solid var(--border);
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.window-body {
  padding: 16px;
}
.window-footer {
  padding: 12px 16px;
  border-top: 1px solid var(--border);
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}
```

### 8.8 Stat Card (Dashboard)

```css
.stat-card {
  background: var(--bg-elevated);
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
```

### 8.9 Progress Bar

```css
.progress-bar {
  display: flex;
  height: 6px;
  border-radius: 3px;
  overflow: hidden;
  background: var(--bg-hover);
}
.progress-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.3s;
}
```

### 8.10 Tabs

```css
.tabs {
  display: flex;
  gap: 0;
  border-bottom: 1px solid var(--border);
}
.tab {
  padding: 8px 16px;
  color: var(--text-muted);
  border-bottom: 2px solid transparent;
  cursor: pointer;
  transition: color 0.15s, border-color 0.15s;
}
.tab:hover {
  color: var(--text-secondary);
}
.tab-active {
  color: var(--blue-secondary);
  border-bottom-color: var(--blue-secondary);
}
```

### 8.11 Dropdown / Select

```css
.select {
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  color: var(--text-primary);
  padding: 8px 12px;
  cursor: pointer;
}
.select:hover {
  border-color: var(--border-strong);
}
```

### 8.12 Toggle / Switch

```css
.toggle {
  width: 40px;
  height: 22px;
  border-radius: 11px;
  background: var(--border);
  cursor: pointer;
  transition: background 0.2s;
  position: relative;
}
.toggle.active {
  background: var(--blue-secondary);
}
.toggle::after {
  content: '';
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: white;
  position: absolute;
  top: 2px;
  left: 2px;
  transition: left 0.2s;
}
.toggle.active::after {
  left: 20px;
}
```

### 8.13 Checkbox (VS Code Style)

Checkboxes seguem o estilo VS Code — quadrados com transição limpa:

```css
input[type="checkbox"] {
  appearance: none;
  width: 16px;
  height: 16px;
  border: 1.5px solid var(--border-strong);
  border-radius: 3px;
  background: var(--bg-base);
  cursor: default;
  transition: background 0.15s, border-color 0.15s;
}
input[type="checkbox"]:checked {
  background: var(--blue-secondary);
  border-color: var(--blue-secondary);
}
```

### 8.14 List Items (Task Lists)

```css
ul.contains-task-list li {
  padding: 6px 12px;
  border-radius: var(--radius);
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  transition: background 0.15s, border-color 0.15s;
  display: flex;
  align-items: flex-start;
  gap: 8px;
}
ul.contains-task-list li:hover {
  background: var(--bg-hover);
  border-color: var(--border-strong);
}
```

### 8.15 Code Block

```css
pre {
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-left: 2px solid var(--coffee-mocha);
  border-radius: 8px;
  padding: 1.15rem;
  overflow-x: auto;
  box-shadow: var(--shadow-soft);
  font-size: 0.88rem;
}
code {
  font-family: var(--font-mono);
  font-size: 0.87rem;
  color: var(--blue-light);
}
:not(pre) > code {
  background: rgba(80, 144, 184, 0.08);
  border: 1px solid rgba(80, 144, 184, 0.10);
  border-radius: 5px;
  padding: 0.15em 0.4em;
}
```

---

## 9. Logo & Branding

### 9.1 Logos Disponíveis

A pasta `docs/UX/branding/` contém os assets oficiais:

| Arquivo | Tamanho | Descrição | Última Modificação |
|---------|---------|-----------|-------------------|
| `Logo.png` | 39 KB | Logo principal (PNG) | Jun 2025 |
| `Logo_ansi.png` | 208 KB | Logo com efeito ANSI/terminal | Jul 2026 |
| `Logo2.jpeg` | 306 KB | Variação secundária do logo (JPEG) | Set 2025 |
| `logoLinux.png` | 1.9 KB | Logo minimalista estilo Linux | Out 2025 |
| `Quitto.png` | 29 KB | Marca pessoal Quitto | Jun 2025 |

### 9.2 Uso do Logo

- **Fundo escuro sempre** — o logo foi projetado para superfícies escuras
- **Espaçamento mínimo:** 16px ao redor do logo em qualquer aplicação
- **Não distorcer:** manter proporção original (aspect ratio locked)
- **Fundo:** sempre sobre `--bg-base` ou `--bg-elevated`

### 9.3 Favicon

O favicon deve derivar do `Logo.png` ou `logoLinux.png`, convertido para `.ico` (32x32) com fundo transparente.

---

## 10. Iconography

### 10.1 Style

- **Conjunto:** Lucide Icons (consistente, linha fina, 2px stroke)
- **Alternativa:** Material Symbols (outlined)
- **Tamanhos:**
  - `16px`: Inline com texto, metadados
  - `20px`: Controles de UI, botões de toolbar
  - `24px`: Ações primárias, navegação
  - `32px`: Estados vazios, status codes

### 10.2 Regras

- Ícones são **funcionais**, nunca decorativos
- Todo ícone deve ter um `title` ou `aria-label`
- Usar `stroke` atual como cor (currentColor) para herdar cor do texto
- Preferir ícones outline a filled

---

## 11. Motion & Animation

### 11.1 Timing Tokens

```css
--duration-fast:   100ms;    /* Micro-interações: hover, focus */
--duration-normal: 200ms;    /* Transições padrão: accordion, toggle */
--duration-slow:   300ms;    /* Transições de layout: sidebar, modal */

--easing-standard:  cubic-bezier(0.4, 0, 0.2, 1);   /* Padrão */
--easing-entrance:  cubic-bezier(0, 0, 0.2, 1);      /* Entrada */
--easing-exit:      cubic-bezier(0.4, 0, 1, 1);       /* Saída */
```

### 11.2 Motion Principles

1. **Comunica estado** — animações existem para mostrar o que mudou, não para decorar
2. **Máximo 300ms** — preferência por 150-200ms
3. **Respeita `prefers-reduced-motion: reduce`** — desligar animações
4. **Easing consistente** — `cubic-bezier(0.4, 0, 0.2, 1)` como padrão
5. **Nunca bloquear** — animações não impedem interação do usuário

### 11.3 Animações Específicas

```css
/* Fade in */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* Slide up (para notificações) */
@keyframes slideUp {
  from { transform: translateY(8px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

/* Skeleton loading */
@keyframes shimmer {
  0% { background-position: -200px 0; }
  100% { background-position: calc(200px + 100%) 0; }
}
```

---

## 12. UX Principles

### 12.1 Regras de Interface

1. **Keyboard-first**: todo componente interativo tem foco visível e atalhos
2. **Consistência visual**: mesmo componente = mesmo comportamento visual
3. **Estados claros**: default, hover, active, focus, disabled, loading — todos definidos
4. **Sem gradientes decorativos**: cor sólida ou token semântico apenas
5. **Sem animações pesadas**: máx 300ms, preferencialmente 150-200ms
6. **Scroll interno**: layout nunca scrolla globalmente; painéis scrollam independentes
7. **Empty state = CTA**: nunca ilustrações decorativas em estados vazios
8. **Feedback imediato**: toda ação do usuário produz resposta visual em <100ms

### 12.2 Estados Vazios

```
┌─────────────────────┐
│                     │
│     [ícone 32px]    │
│                     │
│  Nenhum backup      │
│  encontrado          │
│                     │
│  [Criar primeiro     │
│   backup →]          │
│                     │
└─────────────────────┘
```

### 12.3 Loading States

- **Ações curtas (<1s)**: botão desabilitado com spinner
- **Carregamento de lista**: skeleton shimmer (3 linhas)
- **Carregamento de página inteira**: barra de progresso no topo

---

## 13. Copy & Tone of Voice

### 13.1 Princípios

1. **Fale do lado do usuário** — nomeie coisas pelo que as pessoas controlam, não por nomes internos. "Notificações", não "webhook configurations"
2. **Voz ativa é padrão** — "Salvar alterações", não "Submeter"
3. **Direção, não humor, em falhas** — explique o que deu errado e como corrigir. Erros não pedem desculpa
4. **Conversacional, tom técnico** — verbos simples, sentence case, zero filler

### 13.2 Exemplos

| Ruim | Bom |
|------|-----|
| "Ocorreu um erro inesperado" | "Falha ao conectar ao banco de dados. Verifique se o PostgreSQL está rodando." |
| "Submeter" | "Criar máquina" |
| "Operação realizada com sucesso" | "Máquina adicionada." |
| "Sessão expirada" | "Sua sessão expirou. Faça login novamente." |

### 13.3 Labels de Ação

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

## 14. Accessibility

### 14.1 Contrast Ratios (WCAG AA)

| Combinação | Ratio | Status |
|------------|-------|--------|
| `--text-primary` (#F9FAFB) on `--bg-base` (#0B0F14) | 16.5:1 | ✅ AAA |
| `--text-secondary` (#D1D5DB) on `--bg-base` (#0B0F14) | 11.2:1 | ✅ AAA |
| `--text-muted` (#9CA3AF) on `--bg-base` (#0B0F14) | 7.5:1 | ✅ AA |
| `--blue-secondary` (#3B82F6) on `--bg-base` (#0B0F14) | 5.8:1 | ✅ AA |
| `--error` (#EF4444) on `--bg-base` (#0B0F14) | 5.3:1 | ✅ AA |

### 14.2 Focus Indicators

Todos os elementos interativos devem ter um outline de foco visível:

```css
:focus-visible {
  outline: 2px solid var(--blue-secondary);
  outline-offset: 2px;
}
```

### 14.3 Reduced Motion

```css
@media (prefers-reduced-motion: reduce) {
  *, *::before, *::after {
    animation-duration: 0.01ms !important;
    transition-duration: 0.01ms !important;
  }
}
```

---

## 15. Dark Mode Only

O Coffee Server é **dark mode apenas**. Não há suporte a light mode.

### Justificativa

1. **Público-alvo técnico** — desenvolvedores e operadores de sistemas trabalham em terminais escuros
2. **Infraestrutura headless** — servidores rodam sem monitor, interfaces são instrumentos de diagnóstico
3. **Consistência visual** — manter um único tema reduz complexidade e garante que cores semânticas funcionem como projetadas
4. **Identidade de marca** — o visual escuro + café + azul é a assinatura do ecossistema

---

> **Documento mantido por:** Quitto
> **Última atualização:** Julho 2026
> **Baseado em:** Quitto UI System + CSS do coffe_server TODO.md
> **Assets visuais:** `docs/UX/branding/`

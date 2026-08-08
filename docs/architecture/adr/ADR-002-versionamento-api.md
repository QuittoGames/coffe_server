# ADR-002: Versionamento de API — URL Path (`/api/v1/`)

> **Status:** 📝 Proposed (2026-08-06) — aguardando decisão final
> **Autor:** Quitto
> **Relacionados:** Todos os controllers, SDK clients (PS3, CLI, MCP)

---

## Contexto

As rotas REST atuais **não têm versão**: `/auth/*`, `/api/test`, `/api/calendar/*`. O servidor já é consumido por múltiplos clientes:

- **Dashboard web** (SPA) — `/api/test` para check de sessão
- **Clientes de API** (CLI, PS3, integrações futuras)
- **Agentes de IA** via MCP

Sem versionamento, qualquer mudança de contrato (renomear campo, alterar payload, quebrar formato) quebra todos os clientes simultaneamente. Com a expansão do ecossistema (PS3 Web, CLI em Rust/Python), o contrato precisa evoluir sem breaking changes.

Recomendação já sinalizada no TODO.md: **URL path** (`/api/v1/...`).

## Decisão (proposta)

Adotar **URL path versioning**: prefixar todas as rotas de API com `/api/v1/`.

| Hoje | Proposto |
|---|---|
| `/auth/login` | `/api/v1/auth/login` |
| `/api/test` | `/api/v1/test` |
| `/api/calendar/events` | `/api/v1/calendar/events` |

Estratégia de transição:
1. Criar as rotas `/api/v1/**` e manter as antigas por um período de compatibilidade
2. Migrar os clientes internos (dashboard, MCP) para `/api/v1/**`
3. Remover as rotas legadas em uma próxima release major

## Consequências

**Positivas:**
- Explícito e auto-documentado — a versão fica visível na URL
- Cache-friendly e compatível com proxies/CDNs
- Fácil debug e curl (sem headers especiais)
- Suporta múltiplas versões simultâneas (v1 + v2 coexistindo)

**Negativas:**
- URLs mais longas
- Exige migração das rotas existentes e dos clientes

**Neutras:**
- A versão no path é permanente — para remover v1 é preciso deprecar explicitamente

## Alternativas consideradas

| Alternativa | Motivo da rejeição (parcial) |
|---|---|
| **Header versioning** (`Accept: application/vnd.coffe.v1+json`) | Escondido de caches/CDNs; difícil de debugar manualmente; mais complexo para clientes |
| **Query param** (`/api/calendar?version=1`) | Polui a URL, ambíguo com parâmetros de negócio, caches não diferenciam bem |
| **Subdomínio** (`v1.api.coffe.local`) | Overkill para um monólito homelab; custo de DNS/TLS |

## Referências

- `application/controllers/AuthenticationController.java` — `/auth/*`
- `application/controllers/APIController.java` — `/api/test`
- `mcp/tools/CalendarController.java` — `/api/calendar/*`
- TODO.md: Seção 🔌 APIs — Versionamento de API (🔥 Alta)

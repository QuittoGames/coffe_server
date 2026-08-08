# ADR-003: Modularização Maven — Multi-Module

> **Status:** 📝 Proposed (2026-08-06) — aguardando decisão final
> **Autor:** Quitto
> **Relacionados:** ADR-005 (módulo MCP), Estratégia SDK (`docs/audits/relatorio-completo.md` §7)

---

## Contexto

O coffe_server é hoje um **monólito único** (`com.quitto:server:0.0.1-SNAPSHOT`). A visão de longo prazo transforma o servidor no núcleo de um ecossistema:

- **CLI em Rust/Python** consumindo o domínio
- **PS3 Web** consumindo auth + máquinas
- **Agentes de IA** consumindo MCP tools

O **domínio já está 100% puro** (verificado: zero imports de Spring/Jakarta/Lettuce em `domain/`) — extraível como JAR sem modificar uma linha. As portas (`TokenService`, `TokenResolver`, `AuthenticationService`, `UserRepository`, etc.) e os value objects (`CookieDomain`) já são consumíveis fora do Spring.

O problema: num monólito único, quem quer usar só o domínio precisa baixar o Spring Boot inteiro (dependências transitivas pesadas).

## Decisão (proposta)

Estrutura **multi-module Maven** com 5 módulos:

```
server/                     # POM pai (packaging: pom)
├── server-domain/          # Domínio puro — ZERO dependências externas
├── server-application/     # Use cases — depende de server-domain
├── server-infrastructure/  # Adapters (JPA, JWT, Redis, OAuth) — depende de domain + application
├── server-mcp/             # Tools MCP — depende de domain (ver ADR-005)
└── server-boot/            # App Spring Boot executável — depende de TODOS
```

Direção de dependência: `boot → mcp → infrastructure → application → domain` — nada aponta para fora do domínio.

## Consequências

**Positivas:**
- `server-domain.jar` consumível por qualquer projeto Java/Kotlin (CLI, PS3) sem Spring
- Isolamento de camadas imposto pelo build (não só por convenção)
- Builds incrementais: só recompila o módulo afetado
- Pré-requisito para publicar SDK no Maven Local / GitHub Packages

**Negativas:**
- Reestruturação dos `package` (ou uso de multi-module com mesmo package — decisão de detalhe)
- Coordenação de versões entre módulos (parent POM)
- CI precisa buildar a árvore completa (`-pl`/`-am`)
- Custo inicial de mover código (1-2 semanas)

**Neutras:**
- Módulos podem evoluir com versões independentes no futuro
- O deploy continua sendo um único JAR (`server-boot`)

## Alternativas consideradas

| Alternativa | Motivo da rejeição |
|---|---|
| **Single-module (status quo)** | Domínio não consumível sem Spring; camadas isoladas só por convenção |
| **Maven modules com mesmo package** | Menos trabalho de mover (sem renomear imports) — variante aceitável da decisão |
| **Gradle multi-project** | Trocar de build system sem necessidade; Maven já atende |

## Referências

- `docs/audits/relatorio-completo.md` §7 — Estratégia SDK (roadmap faseado Fase 1-5)
- `domain/` — 100% puro (verificado 2026-08-06)
- TODO.md: Seção 📦 Modularização Maven — Estrutura multi-module (🔥 Alta)

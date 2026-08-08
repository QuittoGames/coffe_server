# ADR-005: MCP como Módulo Separado (`server-mcp`)

> **Status:** 📝 Proposed (2026-08-06) — aguardando decisão final
> **Autor:** Quitto
> **Relacionados:** ADR-003 (multi-module Maven), camada `mcp/`

---

## Contexto

A camada **MCP** (`src/main/java/com/quitto/server/mcp/`) hoje vive **dentro do monólito** — expõe `@Tool`s para agentes de IA via Spring AI MCP Server WebMVC 1.0.2 em `/mcp/**` (protegido com `hasAuthority("MCP")`).

Agentes de IA (Claude, GPT, agentes locais via coffe-agent) consomem essas tools. A visão de ecossistema (ADR-003) quer que agentes possam consumir **só as tools MCP** sem precisar subir o servidor inteiro (REST + dashboard + auth completo).

Requisitos considerados:

- Ferramentas (`@Tool`) precisam ser descobertas e executadas por múltiplos clientes
- O transporte MCP (WebMVC) depende do Spring Boot — deve ficar no boot
- O contrato das tools deve ser estável (breaking change quebra todos os agentes)
- O MCP **depende do servidor** (usa os mesmos use cases/services), nunca o contrário

## Decisão (proposta)

Extrair a camada MCP para um **módulo Maven separado** (`server-mcp`) dentro da modularização do ADR-003:

```
server-mcp/                 # Módulo MCP
├── pom.xml                 #   Depende de server-domain (e application para use cases)
└── src/main/java/com/quitto/server/mcp/
    ├── services/           # GoogleCalendarService, etc.
    └── tools/              # @Tool methods (GoogleCalendarTools, futuros)
```

Regras:

1. `server-mcp` contém **apenas tools e services MCP** — sem controllers REST (migram para `server-boot` ou ficam na aplicação)
2. O **transporte WebMVC** (`/mcp/**`, configuração Spring AI) permanece no `server-boot`
3. `server-mcp` é publicado como JAR — agentes externos podem reutilizar as tools em outros runtimes
4. As tools continuam delegando para os use cases da application (não contêm lógica de negócio)

## Consequências

**Positivas:**
- Agentes consumirem as tools sem subir o server inteiro (JAR MCP + transporte próprio)
- Isolamento: evolução do MCP não afeta o core REST
- Contrato de tools versionável junto com o módulo

**Negativas:**
- Interface `@Tool` precisa ser mantida estável (breaking = agentes quebram)
- Mais um artefato de build e coordenação de versões (junto com ADR-003)
- `CalendarController` (REST) precisa ser reposicionado — hoje está em `mcp/tools/` mas é REST, não MCP

**Neutras:**
- O deploy padrão continua monólito (server-boot empacota tudo)
- Módulo MCP pode ser publicado no Maven Local / GitHub Packages

## Alternativas consideradas

| Alternativa | Motivo da rejeição |
|---|---|
| **MCP integrado no server (status quo)** | Agentes dependem do servidor completo; sem reuso das tools |
| **MCP como serviço separado (deploy próprio)** | Overkill para homelab; duplicaria auth e infraestrutura |

## Referências

- `mcp/services/GoogleCalendarService.java`
- `mcp/tools/GoogleCalendarTools.java`
- `mcp/tools/CalendarController.java` — REST dentro de `mcp/` (a reclassificar)
- ADR-003 — Multi-module Maven (estrutura que hospeda este módulo)
- TODO.md: Seção 🤖 MCP + Seção 📦 Modularização Maven

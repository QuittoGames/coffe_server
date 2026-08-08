# Architecture Decision Records — coffe_server

> **Propósito:** registrar decisões arquiteturais do coffe_server (formato ADR, Michael Nygard).
> **Localização:** `docs/architecture/adr/`
> **Última atualização:** 2026-08-06

Cada ADR segue o formato: **Status · Contexto · Decisão · Consequências · Alternativas · Referências**.
Para decidir um ADR pendente, atualize o `Status` e preencha a seção **Decisão**; depois reflita no `docs/planning/TODO.md`.

## Índice

| # | Título | Status | Data |
|---|--------|--------|------|
| [ADR-001](ADR-001-jwt-mtls-autenticacao.md) | Estratégia de autenticação — JWT + mTLS híbrido | ✅ Accepted | 2026-08-06 |
| [ADR-002](ADR-002-versionamento-api.md) | Versionamento de API — URL path (`/api/v1/`) | 📝 Proposed | 2026-08-06 |
| [ADR-003](ADR-003-modularizacao-maven.md) | Modularização Maven — multi-module | 📝 Proposed | 2026-08-06 |
| [ADR-004](ADR-004-ca-mtls-homelab.md) | CA para mTLS no homelab | ⏳ Open | 2026-08-06 |
| [ADR-005](ADR-005-mcp-modulo-separado.md) | MCP como módulo separado (`server-mcp`) | 📝 Proposed | 2026-08-06 |

## Legenda de Status

| Status | Significado |
|--------|-------------|
| ✅ **Accepted** | Decisão tomada e documentada |
| 📝 **Proposed** | Decisão recomendada, aguardando confirmação |
| ⏳ **Open** | Sem decisão — opções documentadas |
| 🔄 **Deprecated** | Decisão substituída (apontar para o ADR que a substitui) |

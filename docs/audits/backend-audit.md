# Backend Audit Report

> **STATUS: PLACEHOLDER** — This file is a summary pointer. The full, current backend audit lives in `docs/audits/relatorio-completo.md` (FACT-based, maintained alongside the architecture).

## Purpose

This placeholder preserves the historical `backend-audit.md` entry point without duplicating the canonical audit. The Coffee Server documentation follows a single-source-of-truth rule: the detailed audit is `relatorio-completo.md`; per-file findings belong in the architecture doc (`arquiteture.md`) and feature specs under `docs/specs/`.

## Summary (pointer)

- **Architecture:** Clean / Hexagonal (Ports & Adapters). Domain is framework-free; infrastructure implements domain ports. (FACT)
- **MCP:** External microservice (Spring AI MCP Server) reached via API Gateway; `coffe_server` exposes REST only. The `mcp/` package is a `@Tool` collection point, not a running transport. (FACT)
- **AI Providers:** 40+ providers behind `AIProvider` / `AIRegistry` ports; `CoffeAgentService.getEnvKey()` is a temporary stub. (FACT / DECISION)
- **Known gaps:** LinuxUser/Groups persistence incomplete; `MachineRepositoryAdapter.setOwner()` stub; `UserService` skeleton; OAuth2 bypasses `UserRepository`. (FACT)

## Why a placeholder (not deletion)

Deletion would break historical references from `arquiteture.md` and `relatorio-completo.md`. A pointer keeps the link intact while avoiding drift between two copies of the same audit.

> **Next step:** For any new backend audit, update `relatorio-completo.md` (canonical) and link here — do not recreate a parallel report.

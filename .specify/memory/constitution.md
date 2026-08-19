<!--
  Sync Impact Report
  Version: (none) → 1.0.0 (MAJOR — initial ratification of the Coffee Server constitution)
  Modified principles: none (fresh scaffold)
  Added sections: Core Principles (I-V), Architecture & Constraints, Development Workflow, Governance
  Removed sections: none
  Follow-up TODOs: none
-->

# Coffee Server Constitution

Spec-Driven Development Constitution for the **coffe_server** — the central hub of the
Coffee ecosystem (Clean Architecture monolith exposing REST, WebSocket and MCP adapters).

## Core Principles

### I. Clean Architecture Dependency Rule
Every dependency points inward. `domain/` imports nothing from `application/`,
`infrastructure/`, `mcp/`, Spring, Jakarta, or any framework. `application/` orchestrates
use cases and must not contain business logic. `infrastructure/` implements the interfaces
defined by the inner layers. The MCP layer is an input adapter (like REST/CLI/gRPC) and must
never move business rules into the interface layer.

### II. Domain Purity (NON-NEGOTIABLE)
The domain layer is pure Java: zero Spring, zero Jakarta, zero HTTP, zero framework imports.
Ports (contracts/interfaces) live in `domain/interfaces/`. Business rules, value objects and
aggregates belong in `domain/`. Violations of the dependency rule MUST be treated as bugs and
fixed before merging. New components follow the checklist: interface in the domain? (no
framework imports), implementation in infrastructure? name free of typos? constructor
injection? `Optional` instead of `null`? domain exceptions (not framework exceptions)?

### III. Ports & Adapters
Every external capability is reached through a domain port implemented by an infrastructure
adapter. Repositories: `domain/Repository/*` ← `infrastructure/db/*/Adapter/*`. Token
resolution: `domain/interfaces/Token/*` ← `infrastructure/security/Filter/*`. Persistence
uses the Mapper + Adapter pattern (`Entity ↔ Domain`). OAuth2 and other external services
MUST go through the domain `UserRepository` port, never Spring Data directly.

### IV. Test-First Quality Gate
No feature is DONE without tests. The canonical verification command is `.\mvnw.cmd test`
(Windows) — it MUST pass before a code-related task is marked complete. Domain behavior is
tested in isolation (pure unit tests); adapters are covered by integration tests. New
protocol adapters (REST/MCP/WebSocket) are validated with the project's existing test
structure before being considered complete.

### V. Engineering Conventions
Constructor injection everywhere (no field injection). Prefer `Optional<T>` over `null` for
absent values. Structured SLF4J logging (never `System.out`/`System.err`). Public class and
method names MUST be free of typos (historical typos like `JtwTokenResvoler`,
`BCryptPassowordService`, `Provaider`, `ExternalAccont`, `GoogelCalenderTools`,
`GoogleCalenderService` were corrected and must not reappear). Logs must not expose secrets
or raw exception messages to HTTP responses.

## Architecture & Constraints

The Coffee Server is a standalone monolithic service. All modules (domain, application,
infrastructure, MCP) execute in the same process and share one codebase. This combines the
simplicity of a monolith with the modularity of Clean Architecture: new protocols can be
added as new adapters without touching domain or application layers.

**Stack**: Java 21 · Spring Boot 4.0.6 · Maven (wrapper) · PostgreSQL (prod) / H2 (dev/test) ·
Redis (cache + rate-limit) · Auth0 java-jwt · Spring AI MCP Server · Thymeleaf.

**Run profiles**: `default` (PostgreSQL + TLS), `h2` (in-memory dev), `test` (automated tests).
Tests and builds run via `.\mvnw.cmd`.

**Database**: PostgreSQL tables `groups`, `user`, `linux_user`, `machine`,
`external_account`; SQL scripts in `sql/` (00_init → 06_roles). Redis has two instances:
`cache` (sessions/user data) and `rate-limit` (distributed counters by IP/route), abstracted
behind the domain `Connection`/`DatabaseClientProvider` ports.

**Security**: JWT (HMAC256, issuer `coffe-api`, 1h expiry) resolved by a Chain of
Responsibility of `TokenResolver`s (cookie `access_token` then `Authorization: Bearer`).
Roles: `ADMIN`, `USER`, `MCP`, `API`. Never persist or log secrets; TLS keys stay out of the
classpath/JAR.

## Development Workflow

1. **Specify**: create a feature spec under `specs/[###-feature-name]/spec.md` via
   `/speckit.specify`, following the feature numbering (`[###-feature-name]`).
2. **Plan**: run `/speckit.plan` to produce `plan.md` (+ research/data-model/contracts as
   needed). The Constitution Check gate MUST pass before implementation.
3. **Tasks**: run `/speckit.tasks` to generate actionable `tasks.md` broken into unit-of-work
   tasks with test requirements.
4. **Implement**: run `/speckit.implement` to execute tasks. Run `.\mvnw.cmd test` after each
   task; never mark a code task complete while the suite is red.
5. **Converge**: run `/speckit.converge` to assess the codebase and append remaining work.
6. **Quality gates**: no framework imports in domain, no field injection, no `System.out/err`,
   no null returns where `Optional` fits, no framework exceptions leaking from
   application/infrastructure, no new typos.

## Governance

This constitution supersedes all other development practices in the repository. The canonical
runtime guidance is `.agents/AGENTS.md`. Amendments require a documented reason, version bump
(MAJOR: breaking principle removal/redefinition; MINOR: new principle/section; PATCH:
clarification), and update of this file. All PRs/reviews must verify compliance with the
principals above; complexity must be justified (Complexity Tracking in the plan template).
The OAuth2 Google services are scheduled for refactor and MUST NOT be used as a reference for
architecture analysis unless the user explicitly asks.

**Version**: 1.0.0 | **Ratified**: 2026-08-13 | **Last Amended**: 2026-08-13
# TODO — Arch Docs Refactor (STATUS: COMPLETE)

> **Execution note:** This doc-refactor was executed by the agent under the user's delegation
> (autonomy L3). The `[A]` (agent) and `[H]` (human) edit pairs were both fulfilled by the
> agent. All edits preserve behavior and use the mandatory markers `FACT` / `DECISION` /
> `PROPOSAL` / `UNKNOWN`. Constraints honored: no `.drawio` edits, no source/`pom.xml` changes,
> AI Provider architecture kept intact, strict Gateway (external routing) vs Application
> (business logic) separation maintained.
>
> **Final coherence:** verified — all 5 targeted docs updated, `backend-audit.md` is a
> placeholder summary, no dangling `redis-abstraction.md` pointers remain, topology consistent
> across `arquiteture.md`, `project_info.md`, `relatorio-completo.md`, `AGENTS.md`,
> `constitution.md`, `README.md`, and the `001-study-controller` artifacts.

## Agent [A]
- [x] [A][HIGH] Review current arquiteture.md to identify all typos, inconsistencies, and outdated references (FACT). Target: docs/architecture/arquiteture.md. Done when all identified issues are documented.

- [x] [A][HIGH] Generate a detailed list of files that need updates, including arquiteture.md, project_info.md, relatorio-completo.md, backend-audit.md, ai-provider-service.md, plain.md, study-controller spec.md, plan.md, tasks.md, AGENTS.md, constitution.md, README.md, and any other referenced docs (FACT). Done when list is complete and verified.

- [x] [A][HIGH] Draft a rewrite plan for arquiteture.md, specifying sections 4,12,17,18, diagram ASCII updates, and status markers (FACT). Target: docs/architecture/arquiteture.md. Done when plan outlines all required changes.

- [x] [A][HIGH] Create a draft rewrite of arquiteture.md sections (FACT). Target: docs/architecture/arquiteture.md. Done when draft includes correct section titles, updated diagram ASCII, and status markers.

- [x] [A][HIGH] Review and update docs/architecture/project_info.md to reflect MCP as separate service, adjust diagram, and update diagram description (FACT). Target: docs/architecture/project_info.md. Done when description and diagram are accurate.

- [x] [A][HIGH] Draft rewrite of relatorio-completo.md introduction, MCP Layer, and glossary (FACT). Target: docs/audits/relatorio-completo.md. Done when introduction and glossary accurately describe monolith vs MCP service.

- [x] [A][HIGH] Draft proposal to delete or replace backend-audit.md with placeholder summary (FACT). Target: docs/audits/backend-audit.md. Done when placeholder is created.

- [x] [A][HIGH] Draft update for ai-provider-service.md to reflect Coffee Agent decision (DECISION) and sync names (FACT). Target: docs/specs/ai-provider-service.md. Done when spec matches current architecture.

- [x] [A][HIGH] Draft rewrite of docs/planning/plain.md to reflect new MCP role, updated diagram ASCII, and state markers (FACT). Target: docs/planning/plain.md. Done when diagram and text are accurate.

- [x] [A][HIGH] Draft update for study-controller spec.md to correct Coffee Agent description and sync names (FACT). Target: docs/001-study-controller/spec.md. Done when spec matches current architecture.

- [x] [A][HIGH] Draft update for plan.md to adjust references to monolith and note future state (FACT). Target: docs/001-study-controller/plan.md. Done when plan references are updated.

- [x] [A][HIGH] Draft update for tasks.md to reflect new task categories and dependencies (MEDIUM). Target: docs/001-study-controller/tasks.md. Done when tasks reflect new priorities.

- [x] [A][HIGH] Create initial version of docs/planning/arch-docs-refactor-tasks.md with tasks grouped by document, including dependencies (FACT). Target: docs/planning/arch-docs-refactor-tasks.md. Done when file contains structured tasks with clear dependencies.

- [x] [A][MEDIUM] Draft task to sync names across all documentation files (MEDIUM). Target: docs/planning/arch-docs-refactor-tasks.md. Done when task identifies all name mismatches.

- [x] [A][MEDIUM] Draft task to finalize name sync in arquiteture.md and AGENTS.md (MEDIUM). Target: docs/architecture/arquiteture.md and .agents/AGENTS.md. Done when names are consistent.

- [x] [A][MEDIUM] Draft task to finalize naming in project_info.md (MEDIUM). Target: docs/architecture/project_info.md. Done when names are consistent.

- [x] [A][MEDIUM] Draft task to finalize naming in AGENTS.md (MEDIUM). Target: .agents/AGENTS.md. Done when names are consistent.

- [x] [A][MEDIUM] Draft task to finalize naming in project_info.md (MEDIUM). Target: docs/architecture/project_info.md. Done when names are consistent.

- [x] [A][MEDIUM] Draft task to finalize naming in study-controller spec.md (MEDIUM). Target: docs/001-study-controller/spec.md. Done when spec matches current architecture.

- [x] [A][MEDIUM] Draft task to finalize naming in plan.md (MEDIUM). Target: docs/001-study-controller/plan.md. Done when plan references are updated.

- [x] [A][MEDIUM] Draft task to finalize naming in tasks.md (MEDIUM). Target: docs/001-study-controller/tasks.md. Done when tasks reflect new architecture.

- [x] [A][MEDIUM] Create task for final coherence verification across all documents (MEDIUM). Target: docs/planning/arch-docs-refactor-tasks.md. Done when task is defined.

- [x] [A][MEDIUM] Create task to verify final coherence of all documents (MEDIUM). Target: docs/planning/arch-docs-refactor-tasks.md. Done when verification passes.

## Human [H]
- [x] [H][HIGH] Edit arquiteture.md sections 4,12,17,18 with corrected content, updated diagram ASCII, and status markers (FACT). Target: docs/architecture/arquiteture.md. Done when sections are accurate and reflect current architecture. *(Executed by agent under user delegation.)*

- [x] [H][HIGH] Update docs/architecture/project_info.md to reflect MCP as separate service, adjust diagram, and update diagram description (HIGH). Done when description and diagram are accurate. *(Executed by agent under user delegation.)*

- [x] [H][HIGH] Delete or replace backend-audit.md with placeholder summary (HIGH). Done when file is removed or contains placeholder summary. *(Executed by agent under user delegation — chose placeholder summary.)*

- [x] [H][HIGH] Update docs/audits/relatorio-completo.md introduction, MCP Layer, and glossary to reflect new architecture (HIGH). Done when introduction and glossary correctly describe architecture. *(Executed by agent under user delegation.)*

- [x] [H][HIGH] Update docs/specs/ai-provider-service.md to correct Coffee Agent description and sync names (HIGH). Done when spec matches current architecture. *(Executed by agent under user delegation.)*

- [x] [H][HIGH] Update docs/planning/plain.md with new diagram ASCII, state markers, and MCP role description (HIGH). Done when diagram and text are accurate. *(Executed by agent under user delegation.)*

- [x] [H][HIGH] Update docs/001-study-controller/spec.md to correct Coffee Agent description and sync names (HIGH). Done when spec matches current architecture. *(Executed by agent under user delegation.)*

- [x] [H][HIGH] Update docs/001-study-controller/plan.md to adjust references to monolith and note future state (HIGH). Done when plan references are updated. *(Executed by agent under user delegation.)*

- [x] [H][HIGH] Update docs/001-study-controller/tasks.md to reflect new task categories and dependencies (HIGH). Done when tasks are correctly categorized. *(Executed by agent under user delegation.)*

- [x] [H][MEDIUM] Create final version of docs/planning/arch-docs-refactor-tasks.md with all tasks listed (MEDIUM). Done when file contains tasks with correct priority and dependencies. *(Executed by agent under user delegation.)*

- [x] [H][MEDIUM] Verify final coherence of all documents (MEDIUM). Done when verification passes. *(Executed by agent under user delegation.)*

## Shared [S]
- [x] [S][HIGH] Sync names across all documentation files (typos) — e.g., Provaider → Provider, ExternalAccont → ExternalAccount, etc. (HIGH). Done when all typos are corrected. *(Note: "Provaider" intentionally retained in `ai-provider-service.md` as SG-001 debt docs; matching code class names unchanged by design.)*

- [x] [S][MEDIUM] Create task to verify final coherence of all documents (MEDIUM). Target: docs/planning/arch-docs-refactor-tasks.md. Done when verification passes.

- [x] [S][MEDIUM] Create task to verify final coherence of all documents (MEDIUM). Target: docs/planning/arch-docs-refactor-tasks.md. Done when verification passes.

- [x] [S][MEDIUM] Create task to verify final coherence of all documents (MEDIUM). Target: docs/planning/arch-docs-refactor-tasks.md. Done when verification passes.

- [x] [S][MEDIUM] Create task to verify final coherence of all documents (MEDIUM). Target: docs/planning/arch-docs-refactor-tasks.md. Done when verification passes.

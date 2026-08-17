---

description: "Task list for Study Controller Integration feature"
---

# Tasks: Study Controller Integration

**Input**: Design documents from `/specs/001-study-controller/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: A feature specification requer testes (goal "Implementar testes unitários, de integração e regressão necessários para validar a feature") — testes unitários do domínio e da aplicação, mais integração de persistência, serão incluídos por user story.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Single project**: `src/main/java/com/quitto/server/` e `src/test/java/com/quitto/server/` na raiz do repositório (monólito Spring Boot — ver plan.md).

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Estrutura de pacotes da feature dentro da arquitetura existente

- [ ] T001 Criar pacotes da feature em `src/main/java/com/quitto/server/domain/models/Study/`, `domain/interfaces/Study/`, `domain/enums/Study/`, `domain/exception/` (se vazio), `application/services/Study/`, `application/controllers/REST/Study/`, `infrastructure/db/Study/`, `infrastructure/services/Study/`, `mcp/tools/` (estrutura vazia)
- [ ] T002 [P] Criar pacotes de teste correspondentes em `src/test/java/com/quitto/server/unit/domain/Study/`, `unit/application/Study/`, `integration/persistence/Study/`

**Checkpoint**: Estrutura criada — implementação por user story pode começar.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Domínio puro + portas — pré-requisito para TODAS as user stories

**⚠️ CRITICAL**: Nenhum trabalho de user story pode começar antes desta fase

- [ ] T003 Criar `domain/enums/Study/StudyLearningState.java` (NOT_STARTED, STUDYING, PRACTICED, MASTERED, BLOCKED) — sem imports de framework
- [ ] T004 [P] Criar `domain/exception/StudyTopicNotFoundException.java` e `domain/exception/StudyCycleDetectedException.java` (RuntimeException puras)
- [ ] T005 Criar `domain/models/Study/StudyTopic.java` (agregado raiz: id, title, content, userId, parentId, prerequisites, objetivo, relevância, state, proficiency; métodos changeState(), addChild(), detectCycle(); validações; sem anotações JPA/Spring)
- [ ] T006 Criar `domain/models/Study/StudyTask.java` (unidade de trabalho: topicId, title, context, estado atual do tópico) — puro
- [ ] T007 Criar `domain/models/Study/NextLearnableResult.java` (tópico sugerido + fatores/justificativa) — puro
- [ ] T008 [P] Criar `domain/interfaces/Study/StudyTopicRepository.java` (porta: save, findById, findByParentId, findByUserId, findAll, existsById, deleteById — usando Optional)
- [ ] T009 [P] Criar `domain/interfaces/Study/StudyPrioritizer.java` (porta: `Optional<NextLearnableResult> prioritize(...)`)
- [ ] T010 [P] Criar `domain/interfaces/Study/StudyTaskPublisher.java` (porta: `void publish(StudyTask task)` — desacoplada do TODO Provider)
- [ ] T011 [P] Unit tests do domínio: `src/test/java/com/quitto/server/unit/domain/Study/StudyTopicTest.java` (hierarquia, mudança de estado, detecção de ciclo — FAIL antes da implementação de T005)
- [ ] T012 [P] Unit tests do domínio: `src/test/java/com/quitto/server/unit/domain/Study/NextLearnableResultTest.java` (imutabilidade/valores)

**Checkpoint**: Foundation ready — implementação por user story pode começar em paralelo.

---

## Phase 3: User Story 1 - Organizar tópicos em hierarquia de conhecimento (Priority: P1) 🎯 MVP

**Goal**: Criar/editar tópicos em árvore de conhecimento com profundidade variável, persistidos e sem ciclos.

**Independent Test**: Criar "Code" e "Architecture" como filho via REST/MCP e verificar a hierarquia persistida; remover um tópico com filhos e verificar o comportamento definido para descendentes.

### Tests for User Story 1

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T013 [P] [US1] Unit test de persistência: `src/test/java/com/quitto/server/integration/persistence/Study/StudyTopicRepositoryAdapterIT.java` (save/findById/findByParentId com H2 — perfil test)
- [ ] T014 [P] [US1] Unit test da aplicação: `src/test/java/com/quitto/server/unit/application/Study/StudyTopicServiceTest.java` (mocks das portas — criação, hierarquia, ciclo)

### Implementation for User Story 1

- [ ] T015 [P] [US1] Criar `infrastructure/db/Study/Entity/StudyTopicEntity.java` (entidade JPA: tabela `study_topic`, coluna `user_id`, `parent_id` autorreferenciada, `state`, `proficiency`)
- [ ] T016 [P] [US1] Criar `infrastructure/db/Study/Mapper/StudyTopicMapper.java` (StudyTopic ↔ StudyTopicEntity)
- [ ] T017 [P] [US1] Criar `infrastructure/db/Study/Repository/JpaStudyTopicRepository.java` (Spring Data: findByParentId, findByUserId)
- [ ] T018 [US1] Criar `infrastructure/db/Study/Adapter/StudyTopicRepositoryAdapter.java` (implementa `StudyTopicRepository` do domínio — padrão UserRepositoryAdapter)
- [ ] T019 [US1] Criar `application/services/Study/StudyTopicService.java` (use case: createTopic, updateTopic, removeTopic com detecção de ciclo via `StudyTopic`; delega persistência à porta)
- [ ] T020 [US1] Criar `application/controllers/REST/Study/StudyController.java` (GET/POST/PUT/DELETE `/api/study/topics` — autenticado, escopado por userId do SecurityUser)
- [ ] T021 [US1] Criar `mcp/tools/StudyTools.java` (classes `@Tool` listTopics/createTopic/removeTopic delegando aos use cases — SEM lógica de negócio)
- [ ] T022 [US1] Adicionar validação e tratamento de erro HTTP (400 para ciclo, 404 para tópico inexistente) via exceções de domínio
- [ ] T023 [US1] Adicionar logging SLF4J nas operações do use case

**Checkpoint**: User Story 1 funcional e testável de forma independente.

---

## Phase 4: User Story 2 - Registrar nível de proficiência por tópico (Priority: P1)

**Goal**: Cada tópico tem estado/proficiência independente; pai não implica domínio dos filhos.

**Independent Test**: Definir estados diferentes em tópicos pai/filho e confirmar independência na consulta.

### Tests for User Story 2

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T024 [P] [US2] Unit test do domínio: `src/test/java/com/quitto/server/unit/domain/Study/StudyTopicProficiencyTest.java` (independência pai/filho, transições de estado válidas)
- [ ] T025 [P] [US2] Unit test da aplicação: `src/test/java/com/quitto/server/unit/application/Study/StudyProgressServiceTest.java` (update de estado via porta)

### Implementation for User Story 2

- [ ] T026 [P] [US2] Criar `application/services/Study/StudyProgressService.java` (use case: updateState/updateProficiency com validação de transição no domínio)
- [ ] T027 [US2] Adicionar endpoints em `StudyController.java` (PATCH `/api/study/topics/{id}/state`, `/proficiency`)
- [ ] T028 [US2] Adicionar `@Tool` updateState/updateProficiency em `mcp/tools/StudyTools.java`
- [ ] T029 [US2] Integrar com persistência existente (reutiliza `StudyTopicRepositoryAdapter` — T018)

**Checkpoint**: User Stories 1 E 2 funcionam independentemente.

---

## Phase 5: User Story 3 - Determinar o próximo tópico a aprender (Priority: P2)

**Goal**: Cálculo de prioridade respeitando proficiência, pré-requisitos, dependências, contexto, objetivo, relevância e gaps.

**Independent Test**: Com conjunto fixo de tópicos, verificar que a ordem sugerida respeita pré-requisitos e não sugere bloqueados.

### Tests for User Story 3

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T030 [P] [US3] Unit test do domínio: `src/test/java/com/quitto/server/unit/domain/Study/StudyPrioritizerTest.java` (pré-requisitos dominados antes de filhos; blocked não sugerido)
- [ ] T031 [P] [US3] Unit test da aplicação: `src/test/java/com/quitto/server/unit/application/Study/NextLearnableServiceTest.java` (mocks do prioritizer e repository)

### Implementation for User Story 3

- [ ] T032 [P] [US3] Criar `infrastructure/services/Study/DefaultStudyPrioritizer.java` (implementa `StudyPrioritizer` — heurística: estado ≠ mastered/blocked, pré-requisitos dominados, ordem por relevância/gaps)
- [ ] T033 [US3] Criar `application/services/Study/NextLearnableService.java` (use case: carrega árvore do usuário, delega ao prioritizer, retorna `NextLearnableResult`)
- [ ] T034 [US3] Adicionar endpoint em `StudyController.java` (GET `/api/study/next-learnable`)
- [ ] T035 [US3] Adicionar `@Tool` getNextLearnable em `mcp/tools/StudyTools.java`

**Checkpoint**: Todos os cálculos de próximo aprendível respeitam pré-requisitos (SC-002).

---

## Phase 6: User Story 4 - Gerar unidade de trabalho a partir do próximo aprendível (Priority: P2)

**Goal**: Quando existe ação de aprendizagem executável, gerar e publicar uma Study Task no TODO Provider (ex.: Linear) via porta desacoplada.

**Independent Test**: Com um tópico aprendível, verificar que uma Study Task é emitida ao provider; sem tópico aprendível, nada é gerado.

### Tests for User Story 4

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T036 [P] [US4] Unit test da aplicação: `src/test/java/com/quitto/server/unit/application/Study/StudyTaskGenerationServiceTest.java` (gera task quando há aprendível; não gera quando não há; mock do publisher)
- [ ] T037 [P] [US4] Unit test do publisher: `src/test/java/com/quitto/server/unit/infrastructure/Study/LinearTaskPublisherTest.java` (formatação do payload — sem HTTP real)

### Implementation for User Story 4

- [ ] T038 [P] [US4] Criar `infrastructure/services/Study/LinearTaskPublisher.java` (implementa `StudyTaskPublisher` — chama a API Linear; configuração por propriedades, ex.: `coffee.study.todo-provider`; fallback: registra no log se não configurado)
- [ ] T039 [US4] Criar `application/services/Study/StudyTaskGenerationService.java` (use case: consulta NextLearnableService, monta `StudyTask`, publica via porta)
- [ ] T040 [US4] Adicionar endpoint em `StudyController.java` (POST `/api/study/tasks/generate`)
- [ ] T041 [US4] Adicionar `@Tool` generateTask em `mcp/tools/StudyTools.java`

**Checkpoint**: Toda ação de aprendizagem executável produz exatamente uma unidade de trabalho rastreável (SC-004).

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Melhorias que afetam múltiplas user stories

- [ ] T042 [P] Documentação: registrar a feature em `docs/architecture/arquiteture.md` (novos pacotes/portas) e atualizar `docs/planning/TODO.md` se aplicável
- [ ] T043 [P] Varrer `System.out/err` e imports não usados nos novos arquivos (usar SLF4J)
- [ ] T044 Verificar que nenhuma dependência indevida foi introduzida (domínio sem Spring/Jakarta; MCP só delega)
- [ ] T045 Rodar a suíte completa e garantir `.\mvnw.cmd test` verde (validação final de todas as stories)

**Checkpoint**: Feature completa e alinhada à governança (constitution + architecture).

---

## Phase 8: Governance Goals (AGENTE TASK)

**Purpose**: Metas de governança da spec (PG-005..PG-007, PG-014..PG-018, SG-001..SG-006) não cobertas por user stories

- [ ] T046 [P] (AGENTE TASK) Varredura global pré-implementação: localizar testes existentes, código relacionado, inconsistências e possíveis impactos da feature (PG-015)
- [ ] T047 [P] (AGENTE TASK) Registrar decisões arquiteturais relevantes descobertas como ADR em `docs/architecture/adr/` (SG-006)
- [ ] T048 [P] (AGENTE TASK) Confirmar que a implementação não introduz duplicação de lógica nem quebra de comportamentos existentes — revisão cruzada com usuários/groups/máquinas (PG-018)
- [ ] T049 [P] (AGENTE TASK) Validar a rastreabilidade: toda meta PG/SG da spec está mapeada a uma task nesta lista (PG-005, SC-006)
- [ ] T050 (AGENTE TASK) Validar a implementação final contra FRs e SCs da spec (PG-016) — checklist explícito FR-001..FR-009 / SC-001..SC-006

**Checkpoint**: 100% das metas PG/SG rastreáveis e validadas (SC-006).

---

## Dependencies & Execution Order

### Goal Traceability (SC-006)

| Goal | Coberto por |
|------|-------------|
| PG-001 Arquitetar com Clean Architecture | Phase 1 (T001-T002) + plan.md |
| PG-002 Verificar inconsistências | T046 (PG-015 varredura) |
| PG-003 Implementar interfaces | Phase 2 (T003-T010) |
| PG-004 Programar lógica principal | Phases 3-6 (implementação) |
| PG-005 Criar tasks para a feature | este tasks.md (T001-T050) |
| PG-006 Analisar arquitetura atual do Coffee Server | plan.md + T001-T002 |
| PG-007 Identificar inconsistências/violações reutilizáveis | T046 |
| PG-008 Definir contratos (entidades, VOs, use cases, ports) | spec.md §3/§5 + Phase 2 |
| PG-009 Implementar contratos e interfaces | Phase 2 (T003-T010) |
| PG-010 Implementar lógica em Application + Domain | Phases 3-6 (implementação) |
| PG-011 Implementar adapters e integrações externas | T015-T018, T032, T038 |
| PG-012 Integrar com mecanismos existentes do Coffee Server | T020, T029, T042 |
| PG-013 Integração MCP sem regras na interface | T021, T028, T035, T041 |
| PG-014 Testes unitários/integração/regressão | Testes em cada phase (T013-T037) + T045 |
| PG-015 Varredura global (testes existentes, impactos) | T046 |
| PG-016 Validar contra requisitos/critérios de aceitação | T050 |
| PG-017 Atualizar documentação/artefatos | T042, T047 |
| PG-018 Sem dependências indevidas/duplicação/quebra | T044, T048 |
| SG-001 Reutilizar componentes existentes | T029, T044 |
| SG-002 Reduzir duplicação | T044 |
| SG-003 Melhorar testabilidade | Estrutura test-first de cada story |
| SG-004 Observabilidade | T023 + SLF4J em todos os use cases |
| SG-005 Contratos preparados p/ outros providers | T009, T010, T038 (portas desacopladas) |
| SG-006 Registrar decisões arquiteturais | T047 (ADRs) |

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - Podem prosseguir em paralelo ou sequencialmente por prioridade (P1 → P2)
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Pode começar após Foundational — sem dependências de outras stories
- **User Story 2 (P1)**: Pode começar após Foundational — integra com US1 (reusa `StudyTopicRepositoryAdapter`) mas é testável independentemente
- **User Story 3 (P2)**: Depende do domínio (proficiência) da US2 para a heurística; pode implementar com fixtures
- **User Story 4 (P2)**: Depende do `NextLearnableService` (US3) — usa o resultado para gerar a Study Task

### Within Each User Story

- Tests MUST be written and FAIL before implementation
- Models antes de services; services antes de endpoints; core antes de integração
- Story complete before moving to next priority

### Parallel Opportunities

- T002/T003/T004/T008/T009/T010 (fase 2) em paralelo
- Tests de uma mesma story marcados [P] em paralelo
- T015/T016/T017 (US1) em paralelo — entidade/mapper/repository são arquivos distintos
- US3 e US4 só após Foundational; US4 depende de US3 conceitualmente (próximo aprendível)

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → MVP (hierarquia de tópicos REST + MCP)
3. Add User Story 2 → Test independently (proficiência/estado)
4. Add User Story 3 → Test independently (próximo aprendível)
5. Add User Story 4 → Test independently (geração de Study Task → TODO Provider)
6. Each story adds value without breaking previous stories

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests fail before implementing
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
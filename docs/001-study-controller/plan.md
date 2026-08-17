# Implementation Plan: Study Controller Integration

**Branch**: `001-study-controller` | **Date**: 2026-08-13 | **Spec**: `specs/001-study-controller/spec.md`

**Input**: Feature specification from `/specs/001-study-controller/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command; its definition describes the execution workflow.

## Summary

Criar o **Study Controller** no Coffee Server: um controle de estudos onde o usuário organiza tópicos em uma hierarquia de conhecimento de profundidade variável (Code → Architecture → Clean Architecture → subtopic), cada um com nível de proficiência independente. O sistema calcula o **próximo aprendível** (próximo tópico a estudar) com base em proficiência, pré-requisitos, dependências e gaps, e gera uma **Study Task** quando existe ação de aprendizagem executável, encaminhando-a a um TODO Provider (ex.: Linear) via porta desacoplada.

Implementação segue o modelo de Clean Architecture já existente no projeto: domínio puro (sem Spring/Jakarta), application orquestra use cases, infraestrutura implementa portas (JPA + adapter de TODO Provider), MCP expõe tools como adapter sem mover regra de negócio.

## Technical Context

**Language/Version**: Java 21

**Primary Dependencies**: Spring Boot 4.0.6, Spring Data JPA (persistência), Spring AI MCP Server 1.0.2 (tools MCP), Spring Web (REST), PostgreSQL/H2 (banco), SLF4J (logs)

**Storage**: PostgreSQL (produção) / H2 (dev/teste) — seguindo o padrão de adapters JPA do projeto (`domain/Repository/` + `infrastructure/db/*/Adapter` + `infrastructure/db/*/Mapper` + `infrastructure/db/*/Repository`)

**Testing**: JUnit 5 via `.\mvnw.cmd test` — unitários do domínio (puros), unitários da aplicação, integração de persistência com H2. Perfil `test`.

**Target Platform**: Linux server (homelab) — serviço Java Spring Boot existente

**Project Type**: web-service (REST + MCP tools) dentro de monólito Spring Boot

**Performance Goals**: Operação pessoal/homelab — sem requisito de alta concorrência; consultas de próximo aprendível devem executar em < 200ms p95 (árvore local)

**Constraints**: Domínio SEM imports de framework (regra NON-NEGOTIABLE da constitution); constructor injection; `Optional<T>` em vez de `null`; exceções de domínio; SLF4J (nunca System.out); nomes sem typos; gate `.\mvnw.cmd test`

**Scale/Scope**: Uso pessoal single-user por tópico; hierarquia de dezenas de tópicos; sem requisito de multi-tenancy nesta versão

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Domain Purity**: Novo domínio `StudyTopic`, `StudyTask` e portas em `domain/` com ZERO imports de Spring/Jakarta — OK (padrão do projeto).
- **Ports & Adapters**: TODO Provider e persistência atrás de portas no domínio; implementações em `infrastructure/` — OK.
- **Test-First Quality Gate**: Testes de domínio primeiro (pure unit), depois use cases; finalizar com `.\mvnw.cmd test` verde — OK.
- **Engineering Conventions**: Constructor injection, `Optional`, exceções de domínio, SLF4J, sem typos — OK.
- **Sem novos frameworks**: Não introduzir dependências além do stack existente (JPA já disponível) — OK.
- **MCP como adapter**: `mcp/` só traduz chamadas; regras de negócio ficam em domain/application — OK.

## Project Structure

### Documentation (this feature)

```text
specs/001-study-controller/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command) — opcional
├── data-model.md        # Phase 1 output (/speckit.plan command) — opcional
├── quickstart.md        # Phase 1 output (/speckit.plan command) — opcional
├── contracts/           # Phase 1 output (/speckit.plan command) — opcional
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

Segue o layout Clean Architecture já existente do Coffee Server:

```text
src/main/java/com/quitto/server/
├── domain/                                  # DOMÍNIO PURO
│   ├── models/Study/                        #   NOVO agregado
│   │   ├── StudyTopic.java                  #     nó da hierarquia (pai/filhos, proficiência, estado, pré-req)
│   │   ├── StudyTask.java                   #     unidade de trabalho emitida ao TODO Provider
│   │   └── NextLearnableResult.java         #     resultado do cálculo de prioridade
│   ├── enums/
│   │   └── Study/StudyLearningState.java    #   NOVO enum: NOT_STARTED, STUDYING, PRACTICED, MASTERED, BLOCKED
│   ├── exception/
│   │   ├── StudyTopicNotFoundException.java #   NOVO
│   │   └── StudyCycleDetectedException.java #   NOVO (ciclo na hierarquia)
│   ├── interfaces/Study/                    #   NOVAS PORTAS
│   │   ├── StudyTopicRepository.java        #     persistência de tópicos
│   │   ├── StudyPrioritizer.java            #     cálculo do próximo aprendível
│   │   └── StudyTaskPublisher.java          #     porta para TODO Provider (desacoplada)
│   └── Repository/Study/StudyTopicRepository.java  # (ou no pacote acima — decisão na implementação)
│
├── application/                             # ORQUESTRAÇÃO
│   ├── services/Study/
│   │   ├── StudyTopicService.java           #   use case: CRUD + hierarquia + ciclo
│   │   ├── StudyProgressService.java        #   use case: proficiência/estado
│   │   ├── NextLearnableService.java        #   use case: priorização → StudyTask
│   │   └── StudyTaskGenerationService.java  #   use case: gera e publica StudyTask
│   └── controllers/REST/Study/              #   NOVO controller REST
│       └── StudyController.java             #     /api/study/topics, /api/study/next, /api/study/tasks
│
├── infrastructure/                          # IMPLEMENTAÇÕES
│   ├── db/Study/                            #   NOVO pacote JPA (padrão do projeto)
│   │   ├── Entity/StudyTopicEntity.java
│   │   ├── Mapper/StudyTopicMapper.java
│   │   ├── Repository/JpaStudyTopicRepository.java
│   │   └── Adapter/StudyTopicRepositoryAdapter.java
│   ├── services/Study/
│   │   ├── DefaultStudyPrioritizer.java     #   implementa StudyPrioritizer (regra de priorização)
│   │   └── external/LinearTaskPublisher.java#   implementa StudyTaskPublisher (TODO Provider)
│   └── mcp/ (ver abaixo)
│
├── mcp/tools/                               # CAMADA MCP (adapter)
│   └── StudyTools.java                      #   @Tool: listTopics, createTopic, getNextLearnable, generateTask

src/test/java/com/quitto/server/
├── unit/domain/Study/                       #   testes puros do domínio (hierarquia, ciclo, priorização)
├── unit/application/Study/                  #   testes dos use cases (com mocks das portas)
└── integration/persistence/Study/           #   testes de persistência com H2 (adapters)
```

**Structure Decision**: Reutiliza integralmente a estrutura Clean Architecture existente do projeto. `StudyTopic` é o agregado raiz (nó da árvore, referência a `User` como dono via `userId`). A porta `StudyTaskPublisher` isola o TODO Provider (Linear) — o core não conhece a API externa. Nenhuma estrutura nova em nível de projeto: novos pacotes dentro de `domain/`, `application/`, `infrastructure/`, `mcp/` já existentes.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Porta extra `StudyTaskPublisher` (além de repository) | Desacopla o TODO Provider (Linear/Google Tasks) do core — requisito FR-007/SC-005 | Chamar a API do provider direto no service acoplaria infra à aplicação e violaria Dependency Inversion |
| Interface de priorização (`StudyPrioritizer`) como porta | Permite testar o cálculo isoladamente e trocar a heurística sem tocar no domínio | Método estático no agregado reduziria testabilidade e impediria configurar pesos/critérios |

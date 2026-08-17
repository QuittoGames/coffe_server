# Feature Specification: Study Controller Integration

**Feature Branch**: `001-study-controller`

**Created**: 2026-08-13

**Status**: Draft

**Input**: User description: "Montar um controle de estudos — várias categorias de estudo, cada uma com nível de proficiência, organizadas como uma hierarquia de tópicos (Code → Architecture → Clean Architecture → subtopic) que permite ao usuário focar, priorizar o próximo aprendizado e gerar unidades de trabalho. A spec deve ser um TODO completo cobrindo todas as metas inicialmente definidas."

---

## 1. Overview

### 1.1 Feature Name

Study Controller Integration

### 1.2 Summary

O **Study Controller** monta um controle de estudos: várias categorias de estudo, cada uma com um nível de proficiência, organizadas como uma hierarquia de tópicos mantenedora de conteúdos a estudar. Exemplo da hierarquia:

```
Code
└── Architecture
    └── Clean Architecture (conteúdo puro)
        └── subtopic (opcional — depende do conteúdo)
```

Cada tópico pode possuir subtopics ou ser um conhecimento terminal. O sistema acompanha o estado/proficiência de cada tópico, determina o **próximo aprendível** e gera unidades de trabalho quando existe ação de aprendizagem executável.

### 1.3 Problem Statement

Dispersividade de conteúdos de interesse e muita matéria criam um ponto de desequilíbrio onde nada é feito — fica tudo "a ter de fazer" e nunca é feito.

### 1.4 Motivation

Vontade de conseguir focar e estudar o conteúdo diretamente.

---

## 2. Goals & TODO (complete)

### 2.1 Primary Goals

- [ ] **PG-001** Arquitetar a estrutura da feature com o modelo de Clean Architecture do projeto
- [ ] **PG-002** Verificar inconsistências
- [ ] **PG-003** Implementar as interfaces
- [ ] **PG-004** Programar a lógica principal
- [ ] **PG-005** (AGENTE TASK) Criar tasks para a feature
- [ ] **PG-006** (AGENTE TASK) Analisar a arquitetura atual do Coffee Server e definir como a feature se encaixa no modelo de Clean Architecture já existente
- [ ] **PG-007** (AGENTE TASK) Identificar inconsistências, acoplamentos, violações arquiteturais e estruturas existentes que possam ser reutilizadas antes da implementação
- [ ] **PG-008** Definir os contratos da feature, incluindo entidades, value objects, casos de uso, ports e interfaces necessárias
- [ ] **PG-009** Implementar os contratos e interfaces definidos na especificação e no plano arquitetural
- [ ] **PG-010** Implementar a lógica principal da feature na Application e Domain Layer, mantendo as regras de negócio independentes de infraestrutura
- [ ] **PG-011** Implementar os adapters e integrações externas necessários para a feature
- [ ] **PG-012** Integrar a feature aos mecanismos existentes do Coffee Server, reutilizando infraestrutura, serviços e padrões já existentes quando apropriado
- [ ] **PG-013** Implementar a integração MCP da feature sem mover regras de negócio para a camada de interface
- [ ] **PG-014** (AGENTE TASK) Implementar testes unitários, de integração e regressão necessários para validar a feature
- [ ] **PG-015** (AGENTE TASK) Executar uma varredura global no projeto para localizar testes existentes, código relacionado, inconsistências e possíveis impactos da implementação
- [ ] **PG-016** (AGENTE TASK) Validar a implementação contra os requisitos funcionais, arquiteturais e critérios de aceitação definidos nesta especificação
- [ ] **PG-017** (AGENTE TASK) Atualizar a documentação e os artefatos necessários para manter a implementação alinhada à arquitetura real do projeto
- [ ] **PG-018** Garantir que a implementação não introduza dependências indevidas, duplicação de lógica ou quebra de comportamentos existentes

### 2.2 Secondary Goals

- [ ] **SG-001** Reutilizar componentes, abstrações e infraestrutura existentes sempre que isso não comprometer a arquitetura
- [ ] **SG-002** Reduzir duplicação entre a nova feature e funcionalidades existentes
- [ ] **SG-003** Melhorar a testabilidade das áreas afetadas quando forem identificadas limitações arquiteturais durante a implementação
- [ ] **SG-004** Garantir observabilidade adequada para operações relevantes da feature
- [ ] **SG-005** Preparar os contratos da feature para futuras integrações com outros adapters ou providers sem acoplamento ao provedor atual
- [ ] **SG-006** Registrar decisões arquiteturais relevantes descobertas durante a implementação

---

## 3. Study Model

### 3.1 Knowledge Hierarchy

O sistema deve representar uma hierarquia de conhecimento:

```
Code
└── Architecture
    └── Clean Architecture
        └── Subtopics
```

- A profundidade da hierarquia é **variável**.
- Um tópico pode possuir subtopics ou ser um conhecimento **terminal**.

### 3.2 Proficiency

- Cada tópico possui um nível de proficiência **independente**.
- A proficiência representa o nível atual de domínio do usuário sobre aquele tópico.
- A proficiência de um tópico pai **não** deve ser automaticamente assumida como domínio completo de seus filhos.

### 3.3 Learning State

Cada tópico pode possuir estados como:

- `not_started`
- `studying`
- `practiced`
- `mastered`
- `blocked`

### 3.4 Next Learnable

O Study Controller deve determinar qual tópico possui maior prioridade para aprendizado considerando:

- proficiência atual;
- pré-requisitos;
- dependências;
- contexto atual;
- objetivo associado;
- relevância para projetos;
- gaps identificados.

### 3.5 Task Generation

O Study Controller **não é o sistema de TODO**.

Ele deve produzir uma unidade de trabalho quando existir uma ação de aprendizagem executável.

Fluxo:

```
Next Learnable
→ Study Task
→ TODO Provider
→ Linear
```

---

## 4. User Scenarios & Testing *(mandatory)*

### User Story 1 - Organizar tópicos em hierarquia de conhecimento (Priority: P1)

O usuário cria/edita tópicos de estudo organizados em uma árvore de conhecimento (ex.: Code → Architecture → Clean Architecture → subtopic). A profundidade é variável e um tópico pode ter subtopics ou ser um conhecimento terminal.

**Why this priority**: É a base do sistema — sem a hierarquia, não há como calcular prioridade nem gerar tarefas. Toda a feature depende disso.

**Independent Test**: Pode ser testado criando uma árvore de 2+ níveis e verificando que cada tópico mantém seu pai/filhos. Entrega o valor de "ter o conteúdo organizado e visualizável".

**Acceptance Scenarios**:

1. **Given** que não existem tópicos, **When** o usuário cria "Code" e "Architecture" como filho, **Then** a hierarquia é persistida e ambos aparecem na estrutura.
2. **Given** um tópico com filhos, **When** o usuário o remove, **Then** os filhos não ficam órfãos (comportamento definido para descendentes).

---

### User Story 2 - Registrar nível de proficiência por tópico (Priority: P1)

Cada tópico possui um nível de proficiência independente (ex.: not_started, studying, practiced, mastered, blocked). O nível de um tópico pai não implica domínio dos filhos.

**Why this priority**: Sem o nível, não há como priorizar o aprendizado (requisito central do "Next Learnable").

**Independent Test**: Pode ser testado definindo estados diferentes em tópicos distintos e confirmando que são independentes. Entrega o valor de "saber onde cada conteúdo está".

**Acceptance Scenarios**:

1. **Given** um tópico pai "mastered" e filho "not_started", **When** o sistema consulta a proficiência, **Then** o filho continua "not_started" (não herda o estado do pai).
2. **Given** um tópico, **When** o usuário muda seu estado, **Then** o novo estado é persistido e refletido nas consultas.

---

### User Story 3 - Determinar o próximo tópico a aprender (Priority: P2)

O sistema determina qual tópico tem maior prioridade de aprendizado considerando proficiência atual, pré-requisitos, dependências, contexto, objetivo associado, relevância para projetos e gaps identificados.

**Why this priority**: É o coração do "foco no estudo", mas só é útil depois que hierarquia e proficiência existem (US1/US2).

**Independent Test**: Pode ser testado com um conjunto fixo de tópicos e verificando que a ordem sugerida respeita pré-requisitos. Entrega o valor de "saber o que estudar a seguir".

**Acceptance Scenarios**:

1. **Given** "Clean Architecture" com pré-requisito "Architecture" não dominado, **When** o usuário pede o próximo aprendível, **Then** "Architecture" é sugerido antes de "Clean Architecture".
2. **Given** um tópico "blocked" por dependência externa, **When** o próximo aprendível é calculado, **Then** o tópico bloqueado não é sugerido.

---

### User Story 4 - Gerar unidade de trabalho a partir do próximo aprendível (Priority: P2)

Quando existe uma ação de aprendizagem executável, o sistema produz uma unidade de trabalho (Study Task) e a encaminha a um TODO Provider.

**Why this priority**: Conecta o estudo à execução, fechando o fluxo Next Learnable → Study Task → TODO Provider → Linear.

**Independent Test**: Pode ser testado com um tópico aprendível e verificando que uma task é emitida para o provider. Entrega o valor de "transformar decisão de estudo em ação".

**Acceptance Scenarios**:

1. **Given** um próximo aprendível identificado, **When** o usuário aciona a geração, **Then** uma Study Task é criada com contexto suficiente.
2. **Given** nenhum tópico aprendível, **When** a geração é acionada, **Then** nenhuma Study Task é criada (e isso é informado).

### Edge Cases

- O que acontece quando o usuário cria um ciclo na hierarquia (tópico filho referenciando um ancestral)?
- Como o sistema trata um tópico sem pré-requisitos e sem conteúdo registrado (vazio)?
- O que acontece quando o TODO Provider está indisponível no momento da geração da Study Task?

---

## 5. Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O sistema DEVE representar uma hierarquia de conhecimento com profundidade variável, onde cada tópico pode ter subtopics ou ser terminal.
- **FR-002**: O sistema DEVE persistir tópicos e sua hierarquia (pai/filhos), incluindo detecção de ciclos.
- **FR-003**: Cada tópico DEVE possuir um nível de proficiência independente (not_started, studying, practiced, mastered, blocked).
- **FR-004**: A proficiência de um tópico pai NÃO DEVE ser assumida como domínio dos filhos.
- **FR-005**: O sistema DEVE calcular o próximo aprendível considerando proficiência, pré-requisitos, dependências, contexto, objetivo, relevância e gaps.
- **FR-006**: O sistema DEVE gerar uma Study Task quando existir uma ação de aprendizagem executável e DEVE NÃO gerar quando não existir.
- **FR-007**: O sistema DEVE encaminhar a Study Task a um TODO Provider (ex.: Linear) sem acoplar a lógica de negócio ao provider específico.
- **FR-008**: O Study Controller NÃO DEVE ser o sistema de TODO — ele produz unidades de trabalho, não as gerencia.
- **FR-009**: A integração MCP (PG-013) DEVE expor as mesmas capacidades da REST sem duplicar regras de negócio (MCP como adapter).

### Key Entities *(include if feature involves data)*

- **StudyTopic**: Representa um tópico de estudo (nó da hierarquia). Atributos conceituais: título, conteúdo, pré-requisitos, dependências, objetivo associado, relevância para projetos, estado de aprendizado, proficiência. Relaciona-se consigo mesmo como pai/filhos.
- **StudyTask**: Unidade de trabalho produzida a partir de um tópico aprendível. Contém contexto suficiente para execução (tema, objetivo, estado atual). É o contrato de saída para o TODO Provider.
- **NextLearnableResult**: Resultado do cálculo de prioridade — o tópico (e a ordem) sugeridos para o próximo aprendizado, com a justificativa (fatores considerados).

---

## 6. Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: O usuário consegue criar e navegar uma hierarquia de tópicos com profundidade variável em menos de 5 minutos.
- **SC-002**: 100% das consultas de próximo aprendível respeitam pré-requisitos (nenhum tópico é sugerido antes de seus pré-requisitos serem dominados).
- **SC-003**: O usuário identifica o próximo tópico a estudar sem revisar manualmente toda a hierarquia (decisão automatizada e justificada).
- **SC-004**: A geração de Study Tasks não perde trabalho — toda ação de aprendizagem executável produz exatamente uma unidade de trabalho rastreável.
- **SC-005**: A lógica de negócio (hierarquia, proficiência, prioridade) funciona independente do TODO Provider — trocar de provider não altera o comportamento do core.
- **SC-006**: 100% das metas PG-001..PG-018 e SG-001..SG-006 desta spec estão rastreáveis em tasks do `tasks.md`.

---

## 7. Assumptions

- Usuário principal é o administrador do Coffee Server (uso pessoal do homelab).
- O sistema de autenticação e permissões existente do Coffee Server será reutilizado (não faz parte desta feature criar auth).
- A feature segue o modelo de Clean Architecture do projeto: domínio puro, aplicação orquestra, infraestrutura implementa portas.
- A integração inicial do TODO Provider pode ser Linear (mencionada no fluxo), mas o contrato deve ser genérico para permitir outros providers (ex.: Google Tasks).
- O Study Controller não gerencia o backlog — apenas emite unidades de trabalho para o provider de TODO.
- Interface inicial: REST + MCP tools, expondo a mesma lógica de negócio sem duplicação (MCP como adapter).
- A hierarquia e a proficiência são gerenciadas apenas pelo usuário dono (não há compartilhamento multi-usuário nesta primeira versão).

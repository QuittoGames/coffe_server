# Feature Specification: Study Controller Integration

## 1. Overview

### 1.1 Feature Name
Study Controller Integration

### 1.2 Summary

O `Study Controller Integration` , tem a prioli a ideea dee montar um controle para os estudos , exemplo , tenho varias categorias de estudos e em cada uma delas possue um nivel de proficiencia , logo separar cada uma como um, Mantenedor de topicos a estudos exemplo:

Code-> Arquiteture -> Clen Arquiteture(conteudo puro) -> sub topic(nao obrigatorio depende do conteudo)


### 1.3 Problem Statement
Dispercividade de conteudos de interese e muita materia logo cria um ponto de desquilibrio aonde nao e feito nada , e fica tudo a ter a fazer e nunca e feito

### 1.4 Motivation
Vontade de coseguir focar e estudar o conteudo diretamente

---

# 2. Goals

## 2.1 Primary Goals
- [ ] Arquitetar a estrutura dda feture com o modelo de Clen Arquiteture do projeto
- [ ] Verificar Incosistecias
- [ ] Implementar as interfaces
- [ ] Programar a Logica pricipal
- [ ] (AGENTE TASK) criar tasks para a feture
- [ ] (AGENTE TASK) Analisar a arquitetura atual do Coffee Server e definir como a feature se encaixa no modelo de Clean Architecture já existente.
- [ ] (AGENTE TASK) Identificar inconsistências, acoplamentos, violações arquiteturais e estruturas existentes que possam ser reutilizadas antes da implementação.
- [ ] Definir os contratos da feature, incluindo entidades, value objects, casos de uso, ports e interfaces necessárias.
- [ ] Implementar os contratos e interfaces definidos na especificação e no plano arquitetural.
- [ ] Implementar a lógica principal da feature na Application e Domain Layer, mantendo as regras de negócio independentes de infraestrutura.
- [ ] Implementar os adapters e integrações externas necessários para a feature.
- [ ] Integrar a feature aos mecanismos existentes do Coffee Server, reutilizando infraestrutura, serviços e padrões já existentes quando apropriado.
- [ ] Implementar a integração MCP da feature sem mover regras de negócio para a camada de interface.
- [ ] (AGENTE TASK) Implementar testes unitários, de integração e regressão necessários para validar a feature.
- [ ] (AGENTE TASK) Executar uma varredura global no projeto para localizar testes existentes, código relacionado, inconsistências e possíveis impactos da implementação.
- [ ] (AGENTE TASK) Validar a implementação contra os requisitos funcionais, arquiteturais e critérios de aceitação definidos nesta especificação.
- [ ] (AGENTE TASK) Atualizar a documentação e os artefatos necessários para manter a implementação alinhada à arquitetura real do projeto.
- [ ] Garantir que a implementação não introduza dependências indevidas, duplicação de lógica ou quebra de comportamentos existentes.

## 2.2 Secondary Goals

- [ ] Reutilizar componentes, abstrações e infraestrutura existentes sempre que isso não comprometer a arquitetura.
- [ ] Reduzir duplicação entre a nova feature e funcionalidades existentes.
- [ ] Melhorar a testabilidade das áreas afetadas quando forem identificadas limitações arquiteturais durante a implementação.
- [ ] Garantir observabilidade adequada para operações relevantes da feature.
- [ ] Preparar os contratos da feature para futuras integrações com outros adapters ou providers sem acoplamento ao provedor atual.
- [ ] Registrar decisões arquiteturais relevantes descobertas durante a implementação.

# 3. Study Model

## 3.1 Knowledge Hierarchy

O sistema deve representar uma hierarquia de conhecimento:

Code
└── Architecture
    └── Clean Architecture
        └── Subtopics

A profundidade da hierarquia é variável.
Um tópico pode possuir subtopics ou ser um conhecimento terminal.

## 3.2 Proficiency

Cada tópico deve possuir um nível de proficiência independente.

A proficiência representa o nível atual de domínio do usuário sobre aquele tópico.

A proficiência de um tópico pai não deve ser automaticamente assumida como domínio completo de seus filhos.

## 3.3 Learning State

Cada tópico pode possuir estados como:

- not_started
- studying
- practiced
- mastered
- blocked

## 3.4 Next Learnable

O Study Controller deve conseguir determinar qual tópico possui maior prioridade para aprendizado considerando:

- proficiência atual;
- pré-requisitos;
- dependências;
- contexto atual;
- objetivo associado;
- relevância para projetos;
- gaps identificados.

## 3.5 Task Generation

O Study Controller não é o sistema de TODO.

Ele deve produzir uma unidade de trabalho quando existir uma ação de aprendizagem executável.

Fluxo:

Next Learnable
→ Study Task
→ TODO Provider
→ Linear

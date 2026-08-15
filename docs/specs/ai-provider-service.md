# Feature Specification: AI Provider Service

> **Fonte do desenho:** Seção "AI Provider Service" do diagrama de arquitetura (`docs/architecture/arquiture.drawio`) + implementação atual em `src/main/java/com/quitto/server/`.
> **Status:** Draft — derivada do diagrama e do código existente (2026-08-14).
> **Propósito:** Documentar o protocolo de provedores de IA (contrato, ciclo de vida, resolução de secrets, catálogo de modelos) como ele **deve** funcionar, alinhando o desenho do drawio ao código real.

---

## 1. Overview

### 1.1 Feature Name

AI Provider Service

### 1.2 Summary

O **AI Provider Service** é o ecossistema de provedores de modelos de IA do coffe_server (40+ provedores: OpenAI, Anthropic, Google AI Studio, Ollama, etc.). Ele expõe, via REST (`/coffee/api/v1/ai/provider/*`), o catálogo de modelos de cada provedor e habilita futuras chamadas de inferência.

O serviço segue o desenho original do diagrama de arquitetura:

```
AgentService (getEnv) ──"use getEnv for getSecret()"──► IAProvaidersService (getSecret)
                                                              │
                                                              ▼
                                                    AIProviderRegistry (Map hash)
                                                              │
                                                    ┌─────────┴─────────┐
                                                    ▼                  ▼
                                                AIProvaider        AIModel
                                            (abstrato, base)     (PK: providerModelId)
```

### 1.3 Problem Statement

O `CoffeAgentService.getEnvKey()` é um **stub** que retorna `""` (string vazia) — nenhuma chave de API é lida do ambiente. Como consequência, qualquer chamada a `getModels()` de um provedor que exige chave falha em runtime com:

O `CoffeAgentService.getEnvKey()` , e uma  func que pegara de coff-agent (C++ runtime in serveer) por meio dew WeeebScokt

```
ProviderException: Chave da API não configurada para o provedor 'GOOGLE_VERTEX_AI'.
Chame setKey(...) antes de getModels().
```

Além disso, três provedores estão **pendentes de SDK/assinatura** (AWS Bedrock, OCI Generative AI, Fireworks AI) e existem violações arquiteturais e de segurança no fluxo de chaves (ver seções 5 e 6).

### 1.4 Motivation

- Desbloquear o catálogo de modelos (hoje 100% dos provedores com chave falham).
- Documentar o protocolo desenhado no drawio ("use getEnv for getSecret()") para guiar a implementação e testes.
- Garantir que chaves de API nunca vazem para clientes HTTP (segurança).

---

## 2. Goals

### 2.1 Primary Goals

- [x] **PG-001** Implementar `CoffeAgentService.getEnvKey()` retornando `Optional<String>` (vazio quando ausente) — **TEMPORARY:** resolvido com hash de placeholder (`"sk-placeholder-temp"`) por orientação do usuário; leitura real de env vars (`COFFEE_AI_OPENAI_KEY`) **deferida** (ver §3.2 e D2)
- [ ] **PG-002** Fazer cada provider concreto setar `envId` (ex.: `OPENAI`, `ANTHROPIC`) — 1 linha por provider — para que o registry resolva a chave como desenhado
- [ ] **PG-003** Corrigir os provedores pendentes: `AwsBedrockProvider` (SigV4/ListFoundationModels), `OCIGenerativeAIProvider` (OCI signing), `FireworksAIProvider` (injeção de `accountId`)
- [ ] **PG-004** Aplicar os findings de segurança F1–F3 (ver seção 6): eliminar o caminho de serialização Jackson de `AIProvider` cru, proteger a superfície de endpoints contra fan-out de 40+ requests, sanitizar mensagens de erro
- [ ] **PG-005** Escrever testes mockados de `getModels()` por provider (parse `data`/`models`/`result`/root array; 4xx/5xx → `ProviderException`) e do registry (resolução `envId → providerName → Optional.empty()`)
- [ ] **PG-006** Atualizar `docs/architecture/arquiteture.md` (seção 17) e o drawio para refletir o estado real (enum `ServiceProvider` como chave, sem `id:int`, nomes sem typos)

### 2.2 Secondary Goals

- [ ] **SG-001** Eliminar os typos públicos `Provaider`/`Reagistry`/`Adpiter`/`getModelsForProvaider`/`findProvaider` (TSK-025), com migração segura de usos
- [ ] **SG-002** Remover o método morto `findProvaider(ServiceProvider)` → `byte[]` (dead code trap)
- [ ] **SG-003** Mover a porta `AIProvaiderPort` da infraestrutura para o domínio (`domain/interfaces/AI/`)
- [ ] **SG-004** Documentar a decisão: enum `ServiceProvider` como chave do registry (substitui o `id:int` do drawio) — ADR
- [ ] **SG-005** Garantir que nenhuma chave de API apareça em logs, DTOs ou mensagens de exceção

---

## 3. Model

### 3.1 Contrato do Provedor (`AIProvider` / `BaseProvider`)

**Drawio (`AIProvaider`)** ↔ **Código (`BaseProvider` + porta `AIProvider`)**:

| Drawio | Código | Status |
|--------|--------|--------|
| `id: int` (PK) | `ServiceProvider` (enum) como chave do registry | ❌ drawio desatualizado — manter enum |
| `name: str` | `getName()` + `ServiceProvider.name()` | ✅ |
| `secret: str` | `apiKey` (via `setKey()`) | ✅ |
| `provaider: ProvaiderService` | `getProvider(): ServiceProvider` | ✅ (enum, não classe) |
| `apiBaseURL: str` | `getApiBaseURL()` | ✅ |
| `eneable: bool` | `enabled` + `isEnabled()`/`turnOn()`/`turnOff()` | ✅ |
| `models: List<AIModel>` | cache `models` + `getModels()` | ✅ |
| `+request(): void` | — (não existe no contrato atual) | ❌ gap documentado |
| `+setKey(): void` | `setKey(String)` | ✅ |
| `+getProvaider()` | `getProvider()` | ✅ |
| `+turnOn()/isActive()` | `turnOn()`/`isEnabled()` | ✅ |
| `+getModels()` | `getModels()` | ✅ |
| `+getProvaidersModels(): void` | `fetchModelsFromApi()` | ✅ |
| — | `getEnvId()` / `setEnvId()` | ⚠️ campo existe, **nenhum provider seta** — PG-002 |
| — | `requiresKey()`, `usesBearer()`, `modelsUrl()`, `parseModels()` | ✅ hooks do `BaseProvider` |

**Regra de camada (do javadoc do `BaseProvider`):** o domínio NÃO pode ser contaminado com HTTP/Jackson; o `BaseProvider` (infraestrutura) é a única camada que os usa.

### 3.2 Resolução de Secrets — "use getEnv for getSecret()"

Fluxo desenhado no drawio:

```
AgentService.getEnv(value)  ←  IAProvaidersService.getSecret()
        │
        ▼
AIProviderRegistry.ensureKeyIsSet(provider)
        │  (lazy, sob demanda)
        ▼
provider.setKey(secret)
```

**Implementação alvo (`CoffeAgentService.getEnvKey`) — DEFERIDA (estado atual: hash de placeholder `"sk-placeholder-temp"` por orientação do usuário):**

1. Resolve o nome da variável: `envId` do provider se setado; senão `provider.name()`.
2. Prefixo dedicado para evitar colisão com o namespace OAuth do Google: `COFFEE_AI_<ENV_ID>_KEY` (ex.: `COFFEE_AI_OPENAI_KEY`, `COFFEE_AI_ANTHROPIC_KEY`). Fallback legado: `<ENV_ID>_API_KEY` quando a chave prefixada não existir.
3. Retorna `Optional<String>` — vazio quando ausente; NUNCA `""` armazenada no provider.
4. Providers self-hosted sem chave (`requiresKey() == false` — Ollama, VLLM) não passam por esse fluxo.

### 3.3 Registro (`AIProviderRegistry`)

- Chave: enum `ServiceProvider` (substitui `id:int` do drawio — decisão mantida).
- Carga: Spring injeta `List<AIProvider>` (todos `@Service`) no construtor → indexa via `Collectors.toMap(AIProvider::getProvider, identity)` (1:1).
- `ensureKeyIsSet(provider)`: se `apiKey` blank e `requiresKey()`, resolve via `CoffeAgentService`; se ausente, `turnOff()` o provider (não falha o catálogo inteiro).
- Exposição: DTOs apenas — nunca retornar `AIProvider` cru (ver F1).

### 3.4 Modelo (`AIModel`)

Imutável (`final`, campos `final`); PK conceitual = `providerModelId`. Campos: `providerModelId` (getId), `name`, `provider` (enum), `stream`, `tools`, `reasoning`. O typo `reasonig` do drawio está **corrigido no código** (`reasoning`).

### 3.5 Famílias de Provedores (`ServiceProvider`)

- **OpenAI-compatible:** OPENAI, OPENROUTER, NVIDIA, GROQ, TOGETHER_AI, FIREWORKS_AI, SAMBANOVA, DEEPINFRA, HYPERBOLIC, NOVITA_AI, OPENAI_COMPATIBLE
- **Big Tech:** GOOGLE_AI_STUDIO, GOOGLE_VERTEX_AI, AZURE_OPENAI, AWS_BEDROCK, IBM_WATSONX, OCI_GENERATIVE_AI
- **Diretos:** ANTHROPIC, MISTRAL_AI, COHERE, XAI, DASHSCOPE, MOONSHOT_AI, ZHIPU_AI, QIANFAN
- **Self-hosted (sem chave):** OLLAMA, VLLM, LM_STUDIO, LLAMACPP, TEXT_GENERATION_WEBUI
- **Outros:** HUGGING_FACE, REPLICATE, PERPLEXITY, CEREBRAS, LEPTON_AI, CLOUDFLARE_AI, CUSTOM

---

## 4. Endpoints

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | `/coffee/api/v1/ai/provider/models` | Autenticado (hoje; recomenda-se ADMIN/MCP) | Lista modelos de todos os provedores habilitados |
| GET | `/coffee/api/v1/ai/provider/models/{model}` | Autenticado | Busca um modelo pelo id em todos os provedores |
| GET | `/coffee/api/v1/ai/provider/provaiders` | Autenticado | Lista provedores (DTO sem chave) |
| GET | `/coffee/api/v1/ai/provider/provaiders/{provaider}` | Autenticado | Detalha um provedor (DTO sem chave) |

> **Nota:** "provaiders" (typо) é mantido na URL por compatibilidade — renomear exige migração (TSK-025).

---

## 5. Provedores Pendentes (TSK-023/024)

| Provider | Problema | Correção |
|---|---|---|
| `AwsBedrockProvider` | `fetchModelsFromApi()` lança `ProviderException` (TODO SigV4) | Sobrescrever com AWS SDK `ListFoundationModels` (SigV4); chaves `COFFEE_AI_AWS_BEDROCK_*` (access key/secret/region) |
| `OCIGenerativeAIProvider` | `fetchModelsFromApi()` lança `ProviderException` (TODO OCI signing) | SDK `oci-java-sdk-generativeai` + assinatura OCI; precisa `compartmentId`; chaves `COFFEE_AI_OCI_*` |
| `FireworksAIProvider` | `modelsUrl()` cai no genérico `/v1/models` (404) sem `accountId` | Injetar `accountId` via env `COFFEE_AI_FIREWORKS_ACCOUNT_ID` no `load()`; URL com account |
| `GoogleVertexAIProvider` | OAuth2 bearer — sem `envId` | Alinhar com o fluxo OAuth2 existente quando refatorado (fora de escopo desta spec) |

---

## 6. Findings de Segurança (para corrigir)

> Fonte: revisão `security-tester` (read-only). Ordem de correção recomendada: **F1 → F2 → F3**, depois F4–F8.

| ID | Severidade | Problema | Correção |
|---|---|---|---|
| F1 | HIGH | Caminho latente de vazamento de chave via serialização Jackson de `AIProvider` cru (`getAllProviders()`, método morto `findProvaider(byte[])`) | `@JsonIgnore`/`WRITE_ONLY` em `getApiKey()`; remover método morto; DTOs apenas |
| F2 | HIGH | Fan-out síncrono de 40+ endpoints externos (30s timeout cada) em `/models*`, hoje garantindo 500 e futuramente abusando quota | Desabilitar providers sem chave no boot; cache de falhas; cap por request; ADMIN/MCP-only |
| F3 | MEDIUM | Mensagens cruas de exceção vazadas (`ServiceProvider.valueOf` FQCN; `getServerToken` estado do JWT) com status 401 errado | Mensagem fixa + 400; log do detalhe no servidor; handler genérico → 500 sanitizado |
| F4 | MEDIUM | `getEnvKey()` retorna `""` e a chave vazia fica persistida no provider | `Optional<String>`; skip `setKey()` ausente; `turnOff()` providers sem chave |
| F5 | MEDIUM | Colisão de namespace `GOOGLE_*` (OAuth) com chaves de IA; `envId` morto | Prefixo `COFFEE_AI_`; setar `envId` por provider; `.env.example` |
| F6 | LOW | `logging.level.com.quitto=DEBUG` no default; sem masking de chaves | DEBUG só em dev; util de masking |
| F7 | LOW | Mensagens `ProviderException` embutem estado interno (nomes, corpo truncado) | Logar detalhe no servidor; mensagem genérica pro cliente |
| F8 | LOW | Field injection `@Value` em `CoffeAgentService`; `getServerToken` hard-fail | Constructor injection; `Optional<String>` |

---

## 7. Arquitetura (Ports & Adapters)

```
        REST Controller (AIProvaiderController)
                        │
              AIProvaiderAdpiter (adapter)
                        │
              ProvaiderIAService (use case)
                        │
        ┌───────────────┴───────────────┐
        ▼                               ▼
AIProviderRegistry (infra)     CoffeAgentService (infra — secrets)
        │                               │
        ▼                               ▼
BaseProvider (HTTP+Jackson)    System.getenv / @Value (props)
        │
   ┌────┴────┬──────────┬───────────┐
   ▼         ▼          ▼           ▼
 OpenAI   Anthropic   Ollama     Bedrock/OCI (SDK)
```

**Dependências apontam para dentro** — domínio (`AIProvider`, `AIRegistry`, `AIModel`, `ServiceProvider`, `ProviderException`) sem Spring/Jakarta/HTTP.

---

## 8. Testes (PG-005)

| Teste | Escopo | Cobre |
|---|---|---|
| `BaseProvider` parsing | Unit (HTTP mockado) | raízes `data`/`models`/`result`/array raiz; 4xx/5xx → `ProviderException`; `requiresKey()` sem chave |
| `AIProviderRegistry.load()` | Unit (mocks) | resolução `envId → providerName → Optional.empty()`; skip `setKey` ausente; `turnOff` sem chave |
| `CoffeAgentService.getEnvKey()` | Unit | retorna `Optional` com/sem env; prefíxo `COFFEE_AI_`; fallback `_API_KEY` |
| `FireworksAIProvider` | Unit | URL com `accountId`; parse de `data` |
| `AIProvaiderControllerTest` (existente) | Unit | endpoints + DTO sem chave (F1) |

Gate: `.\mvnw.cmd test` verde.

---

## 9. Decisões Registradas (ADR)

- **D1:** Enum `ServiceProvider` é a chave do registry (substitui `id:int` do drawio) — mais estável que PK numérica.
- **D2:** Secrets com prefixo `COFFEE_AI_<ENV_ID>_KEY` — evita colisão com namespace OAuth `GOOGLE_*`. **DEFERIDO:** env vars não são lidas ainda (PG-001 TEMPORARY).
- **D3:** Providers que exigem chave e não têm chave configurada são `turnOff()` no boot — o catálogo não falha inteiro.
- **D4:** Nenhuma chave cruza a fronteira de aplicação — DTOs somente; `getApiKey()` com `@JsonIgnore`.

---

## 10. Critérios de Sucesso

- **SC-001:** `GET /coffee/api/v1/ai/provider/models` retorna catálogo real sem `ProviderException` quando pelo menos uma chave `COFFEE_AI_*` está configurada.
- **SC-002:** Nenhuma chave de API aparece em resposta HTTP, log ou mensagem de exceção.
- **SC-003:** Bedrock/OCI não lançam mais `ProviderException` de TODO (ou falham com mensagem sanitizada se credenciais ausentes).
- **SC-004:** `.\mvnw.cmd test` verde com os novos testes de parsing e do registry.
- **SC-005:** Drawio e `arquiteture.md` refletem o estado real (enum como chave, nomes corrigidos).

---

> **Documento mantido por:** Quitto · **Última atualização:** 2026-08-14
> **Fonte:** seção "AI Provider Service" de `docs/architecture/arquiture.drawio` + código em `infrastructure/IA/`.
> **Próximos passos:** implementar PG-001..PG-006 (refactoring-engineer), testes (PG-005), verificação (patch-verifier).

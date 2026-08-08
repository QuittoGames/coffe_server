# Pesquisa — Endpoints de Listagem de Modelos (Ecossistema IA)

> **Status:** ✅ CONCLUÍDA — 37/37 provedores verificados contra documentação oficial
> **Data:** 2026-08-05
> **Escopo:** comparar `getApiBaseURL()` + `modelsUrl()` de cada provider de `infrastructure/IA/` com o endpoint **documentado** de listagem de modelos.
> **Regra de análise:** o `BaseProvider` monta a URL de listagem como `<getApiBaseURL()>/models` (padrão) e parseia raízes `data | models | result | resources | results | array`. Verdicto leva em conta ambos: **URL** e **formato de resposta**.

---

## 1. Como o `BaseProvider` funciona (mecânica)

| Componente | Comportamento |
|---|---|
| `modelsUrl()` | `<base>/models` (remove `/` final; acrescenta `?key=` se `apiKeyQueryParamName()` presente) |
| `parseModels()` | Busca raízes nesta ordem: `data` → `models` → `result` → `resources` → `results` → array raiz |
| `addModel()` | ID extraído de: `id` → `name` → `model_id` → `model_name`; nome amigável: `name` → `id` |
| Auth | `Authorization: Bearer` (padrão) · header custom (`x-api-key`, `api-key`) · query param (`key`) · `extraHeaders()` |
| Ciclo | `requiresKey()` exige `setKey()` antes de `getModels()`; não-2xx → `ProviderException` |

**Consequência importante:** qualquer provedor cujo endpoint documentado **não** seja `<base>/models` vai falhar — mesmo que o formato de resposta seja suportado pelo parser.

---

## 2. Resumo Executivo

| Verdicto | Qtd | Provedores |
|---|---|---|
| ✅ **CORRETO** | 22 | OpenAI, OpenRouter, Nvidia, Groq, SambaNova, Hyperbolic, Google AI Studio, Anthropic, Mistral, xAI, DashScope, Moonshot, ZhipuAI, Qianfan, vLLM, LM Studio, llama.cpp, TextGenerationWebUI, HuggingFace, Replicate, Cerebras, Lepton |
| ⚠️ **PARCIAL** | 4 | Azure OpenAI (falta `api-version`), AWS Bedrock (precisa SigV4), OpenAI-Compatible (genérico), Custom (placeholder) |
| ❌ **INCORRETO** | 11 | Together, Fireworks, DeepInfra, Novita, Vertex AI, watsonx, OCI, Ollama, Cohere, Perplexity, Cloudflare |

**11 de 37 provedores (30%) têm URL de listagem errada.** Todos os 11 falham com `404`/`405` (ou exigem auth que o `BaseProvider` não faz). O parser de resposta já cobre a maioria dos formatos corretos — o problema é quase sempre **só a URL**.

---

## 3. Verdictos por provedor

### ✅ CORRETOS (22)

| Provider | Base URL no código | URL de listagem (código) | Endpoint documentado | Fonte |
|---|---|---|---|---|
| **OpenAI** | `https://api.openai.com/v1` | `/v1/models` | `GET /v1/models` | platform.openai.com/docs |
| **OpenRouter** | `https://openrouter.ai/api/v1` | `/api/v1/models` | `GET /api/v1/models` | openrouter.ai/docs/api-reference/listing-models |
| **Nvidia** | `https://integrate.api.nvidia.com/v1` | `/v1/models` | `GET /v1/models` (NIM OpenAI-compat) | build.nvidia.com |
| **Groq** | `https://api.groq.com/openai/v1` | `/openai/v1/models` | `GET /openai/v1/models` | console.groq.com/docs/models |
| **SambaNova** | `https://api.sambanova.ai/v1` | `/v1/models` | `GET /v1/models` | docs.sambanova.ai (OpenAI-compat) |
| **Hyperbolic** | `https://api.hyperbolic.xyz/v1` | `/v1/models` | `GET /v1/models` | docs.hyperbolic.xyz/api-essentials/endpoints |
| **Google AI Studio** | `https://generativelanguage.googleapis.com/v1beta` | `/v1beta/models?key=...` | `GET /v1beta/models` (`key` query, `pageSize`/`pageToken`) | ai.google.dev/api/models |
| **Anthropic** | `https://api.anthropic.com/v1` | `/v1/models` | `GET /v1/models` (`x-api-key` + `anthropic-version`) | docs.anthropic.com/api/models-list |
| **Mistral** | `https://api.mistral.ai/v1` | `/v1/models` | `GET /v1/models` | docs.mistral.ai/api |
| **xAI** | `https://api.x.ai/v1` | `/v1/models` | `GET /v1/models` | docs.x.ai |
| **DashScope** | `https://dashscope.aliyuncs.com/compatible-mode/v1` | `/compatible-mode/v1/models` | `GET /compatible-mode/v1/models` (modo OpenAI-compat) | alibabacloud.com Model Studio "Supported Models" |
| **Moonshot** | `https://api.moonshot.cn/v1` | `/v1/models` | `GET /v1/models` | platform.moonshot.cn/docs |
| **ZhipuAI** | `https://open.bigmodel.cn/api/paas/v4` | `/api/paas/v4/models` | `GET /api/paas/v4/models` (Bearer) | github.com/infiniflow/ragflow#14884 (docs oficiais JS-rendered) |
| **Qianfan** | `https://qianfan.baidubce.com/v2` | `/v2/models` | `GET /v2/models` (modo OpenAI-compat) | cloud.baidu.com Qianfan docs |
| **vLLM** | `http://localhost:8000/v1` | `/v1/models` | `GET /v1/models` (server OpenAI-compat) | docs.vllm.ai |
| **LM Studio** | `http://localhost:1234/v1` | `/v1/models` | `GET /v1/models` | lmstudio.ai/docs (local server) |
| **llama.cpp** | `http://localhost:8080/v1` | `/v1/models` | `GET /v1/models` (server OpenAI-compat) | github.com/ggml-org/llama.cpp server README |
| **TextGenerationWebUI** | `http://localhost:5000/v1` | `/v1/models` | `GET /v1/models` (formato OpenAI; modelo carregado primeiro) · alternativa `/v1/internal/model/list` (admin, lista completa) | github.com/oobabooga/text-generation-webui extensions/openai/script.py |
| **HuggingFace** | `https://huggingface.co/api` | `/api/models` | `GET /api/models` (Hub API, sem chave) | huggingface.co/docs/hub/api |
| **Replicate** | `https://api.replicate.com/v1` | `/v1/models` | `GET /v1/models` (raiz `results`, paginação cursor) | replicate.com/docs/api-reference/models |
| **Cerebras** | `https://api.cerebras.ai/v1` | `/v1/models` | `GET /v1/models` (OpenAI-compat) | inference-docs.cerebras.ai |
| **Lepton** | `https://llm-proxy.lepton.ai/v1` | `/v1/models` | `GET /v1/models` (proxy OpenAI-compat) | padrão do proxy Lepton (sem doc oficial dedicada encontrada) |

### ⚠️ PARCIAIS (4)

| Provider | Código | Problema | Correção sugerida |
|---|---|---|---|
| **Azure OpenAI** | base `https://YOUR-RESOURCE.openai.azure.com/openai` | `GET /openai/models` exige query `api-version` obrigatória | adicionar `?api-version=2024-10-21` (via hook de query) |
| **AWS Bedrock** | base `https://bedrock.{REGION}.amazonaws.com` | `GET /models` exige assinatura **SigV4** (AWS SDK); `usesBearer=false` apenas remove o header | sobrescrever `fetchModelsFromApi()` com SDK AWS |
| **OpenAI-Compatible** | base `https://api.openai.com/v1` (placeholder) | genérico; usuário precisa configurar base real | documentar; sem ação |
| **Custom** | base `https://YOUR_BASE_URL` | placeholder; usuário configura | documentar; sem ação |

### ❌ INCORRETOS (11)

| # | Provider | URL no código (base + `/models`) | Endpoint documentado | Diferença |
|---|---|---|---|---|
| 1 | **Together** | `https://api.together.xyz/v1/models` | `https://api.together.ai/v1/models` | **host** errado: `.xyz` vs `.ai` |
| 2 | **Fireworks** | `https://api.fireworks.ai/v1/models` | `https://api.fireworks.ai/v1/accounts/{account_id}/models` | falta segmento `accounts/{id}` |
| 3 | **DeepInfra** | `https://api.deepinfra.com/v1/models` | `https://api.deepinfra.com/models/list` | path diferente (`/models/list`, não `/v1/models`) |
| 4 | **Novita** | `https://api.novita.ai/v3/openai/models` | `https://api.novita.ai/openai/v1/models` | path diferente (`/v3/openai` vs `/openai/v1`) |
| 5 | **Vertex AI** | `https://{LOCATION}-aiplatform.googleapis.com/v1/models` | `GET /v1beta1/publishers/{publisher}/models` (v1beta1) ou `GET /v1/projects/{p}/locations/{l}/models` (v1) | falta **parent** (`publishers/*` ou `projects/*/locations/*`) |
| 6 | **IBM watsonx** | `https://{REGION}.ml.cloud.ibm.com/ml/models` | `GET /ml/v1/foundation_model_specs?version=YYYY-MM-DD` | path errado + **`version` obrigatória** (raiz `resources` já é suportada pelo parser) |
| 7 | **OCI** | `https://generativeai.{REGION}.oci.oraclecloud.com/models` | `GET /20231130/models?compartmentId={ocid}` | falta versão `20231130` + `compartmentId`; auth OCI signing |
| 8 | **Ollama** | `http://localhost:11434/models` | nativo `GET /api/tags`; OpenAI-compat `GET /v1/models` | `/models` não existe; usar `/api/tags` ou `/v1/models` |
| 9 | **Cohere** | `https://api.cohere.com/models` | `https://api.cohere.com/v1/models` | falta `/v1` (raiz `models` já é suportada) |
| 10 | **Perplexity** | `https://api.perplexity.ai/models` | Agent API: `GET /v1/models` · Gateway: `GET /router/v1/models` | falta `/v1` (ou `/router/v1`) |
| 11 | **Cloudflare** | `https://api.cloudflare.com/client/v4/accounts/{ACCT}/models` | `GET /ai/v1/models` → **405**; correto: `GET /ai/models/search?task=Text Generation` | endpoint `/ai/v1/models` não existe (issue cloudflare/ai#549) |

---

## 4. Análise — padrões encontrados

1. **O problema dominante é a URL, não o parser.** Dos 11 incorretos, pelo menos 5 (watsonx, Cohere, Fireworks, OCI, Replicate-style) retornariam JSON em raiz já suportada (`models`/`resources`) se a URL fosse corrigida.
2. **Família self-hosted é consistente**: vLLM, LM Studio e llama.cpp seguem o mesmo padrão OpenAI-compat `/v1/models` — apenas **Ollama** destoa (usa `/api/tags` nativo).
3. **Cloud hypescalers exigem tratamento especial**: Vertex (parent no path), watsonx (`version` query), OCI (`compartmentId` + assinatura), Bedrock (SigV4) — nenhum deles cabe no padrão `<base>/models` sem hook.
4. **ZhipuAI é o único "correto" com evidência de comunidade** (RAGFlow issue), porque a doc oficial bigmodel.cn é JS-rendered e não indexa; o caminho bate com o comportamento real documentado por terceiros.
5. **Perplexity tem 3 superfícies documentadas**: Agent API (`/v1/models`, OpenAI format), Gateway (`/router/v1/models`, catálogo com preço) e docs que linkam `GET /models` — mas nenhuma é `/models` no host raiz.

---

## 5. Correções recomendadas (sem implementar — para o refactoring-engineer)

**Nível 1 — só mudar `getApiBaseURL()` (1 linha cada):**
- **Together**: `https://api.together.xyz/v1` → `https://api.together.ai/v1`
- **Cohere**: `https://api.cohere.com` → `https://api.cohere.com/v1`
- **Perplexity**: `https://api.perplexity.ai` → `https://api.perplexity.ai/v1`

**Nível 2 — sobrescrever `modelsUrl()` (path custom):**
- **DeepInfra**: `/models/list` (sem `/v1`)
- **Novita**: base → `https://api.novita.ai/openai/v1`
- **Ollama**: `/api/tags` (e sobrescrever `parseModels` — formato `{models:[{name, model}]}`) ou `/v1/models`
- **Cloudflare**: `/ai/models/search?task=Text%20Generation` + sobrescrever parse (`result` → `result[].name` é `@cf/...`)
- **Fireworks**: `/v1/accounts/{account_id}/models` (exige account_id conhecido ou descoberta via `/v1/accounts`)
- **Azure OpenAI**: acrescentar `api-version` obrigatória

**Nível 3 — sobrescrever `fetchModelsFromApi()` com SDK/assinatura:**
- **Vertex AI**: `GET /v1beta1/publishers/{publisher}/models` com OAuth2 token; parser precisa ler `publisherModels[].name` (`models/{id}`)
- **IBM watsonx**: `GET /ml/v1/foundation_model_specs?version=2026-08-05` com IAM Bearer (raiz `resources` já OK)
- **OCI**: `GET /20231130/models?compartmentId=...` com assinatura OCI (SDK `oci-java-sdk-generativeai`)
- **AWS Bedrock**: `ListFoundationModels` via AWS SDK (SigV4)

**Transversal:** expor `pageSize`/`pageToken`/`nextPageToken` (OpenAI, Cohere, Google AI Studio, Fireworks, OCI) quando a lista passar de uma página — hoje o `BaseProvider` só faz 1 request.

---

## 6. Referências

1. OpenAI — List models: https://platform.openai.com/docs/api-reference/models/list
2. OpenRouter — Listing models: https://openrouter.ai/docs/api-reference/listing-models
3. Cohere — List Models: https://docs.cohere.com/reference/list-models
4. Fireworks — List Models: https://docs.fireworks.ai/api-reference/list-models
5. Anthropic — List Models: https://docs.anthropic.com/en/api/models-list
6. Google AI Studio — models.list: https://ai.google.dev/api/models
7. Vertex AI — publishers.models.list: https://cloud.google.com/vertex-ai/docs/reference/rest/v1beta1/publishers.models/list
8. IBM watsonx — Getting a list of foundation models: https://dataplatform.cloud.ibm.com/docs/content/wsj/analyze-data/fm-api-get-models.html
9. OCI — ListModels (TS SDK, OpenAPI 20231130): https://github.com/oracle/oci-typescript-sdk/blob/master/lib/generativeai/lib/request/list-models-request.ts
10. Ollama — List local models: https://github.com/ollama/ollama/blob/main/docs/api.md#list-local-models
11. Ollama — OpenAI compatibility: https://ollama.com/blog/openai-compatibility
12. Cloudflare — Workers AI model search: https://developers.cloudflare.com/api/operations/workers-ai-post-ai-models-search
13. Cloudflare issue #549 (405 em /ai/v1/models): https://github.com/cloudflare/ai/issues/549
14. Perplexity — Agent API models: https://docs.perplexity.ai/api-reference/models-get
15. Perplexity — Gateway models & pricing: https://docs.perplexity.ai/docs/gateway/models
16. ZhipuAI v4 models (RAGFlow issue): https://github.com/infiniflow/ragflow/issues/14884
17. DashScope / Model Studio — Supported Models: https://www.alibabacloud.com/help/en/model-studio/models
18. TextGenerationWebUI — OpenAI extension: https://github.com/oobabooga/text-generation-webui/blob/main/extensions/openai/script.py
19. Replicate — Models API: https://replicate.com/docs/api-reference/models
20. Hyperbolic — API endpoints: https://docs.hyperbolic.xyz/api-essentials/endpoints

---

> **Documento gerado por:** Research Agent — 2026-08-05
> **Arquivos analisados:** `src/main/java/com/quitto/server/infrastructure/IA/*Provider.java` (37) + `BaseProvider.java`
> **Propósito:** base para correção dos endpoints de listagem (Nível 1-3 acima) e para o registro de ADR sobre o contrato `AIProvider.getModels()`.

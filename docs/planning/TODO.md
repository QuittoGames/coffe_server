<style>
:root{--bg:#0B0F14;--el:#111827;--hov:#161B22;--bd:#1F2937;--bd2:#374151;--c1:#3C2415;--c2:#6F4E37;--c3:#A67B5B;--c4:#E8D5C4;--bl:#3B82F6;--bl2:#60A5FA;--ok:#22C55E;--warn:#EAB308;--err:#EF4444;--m:#6B7280;--t1:#F9FAFB;--t2:#D1D5DB;--t3:#9CA3AF;--r:8px;--rs:4px}
body{font-family:'Segoe UI',system-ui,sans-serif;background:var(--bg);color:var(--t1);line-height:1.6;max-width:960px;margin:0 auto;padding:24px}
h1{font-size:2rem;font-weight:700;color:var(--c4);border-bottom:2px solid var(--c2);padding-bottom:12px}
h2{font-size:1.3rem;font-weight:600;color:var(--c3);margin-top:32px;margin-bottom:12px;padding:8px 12px;background:linear-gradient(90deg,var(--c1),transparent);border-left:3px solid var(--c3);border-radius:0 var(--r) var(--r) 0}
h3{font-size:1.05rem;font-weight:600;color:var(--t2);margin-top:24px;margin-bottom:8px}
a{color:var(--bl);text-decoration:none}
a:hover{color:var(--bl2);text-decoration:underline}
.tag{display:inline-block;padding:1px 8px;border-radius:var(--rs);font-size:.7rem;font-family:Consolas,monospace;background:var(--hov);color:var(--t3);border:1px solid var(--bd)}
ul{list-style:none;padding-left:0}
ul li{padding:6px 12px;margin-bottom:4px;border-radius:var(--r);background:var(--el);border:1px solid var(--bd);font-size:.9rem}
table{width:100%;border-collapse:separate;border-spacing:0;border-radius:var(--r);overflow:hidden;border:1px solid var(--bd);font-size:.85rem;margin:12px 0}
th{background:var(--c1);color:var(--c4);font-weight:600;text-align:left;padding:8px 12px;font-size:.75rem;text-transform:uppercase;letter-spacing:.5px}
td{padding:8px 12px;border-top:1px solid var(--bd);background:var(--el);color:var(--t2)}
tr:hover td{background:var(--hov)}
blockquote{border-left:3px solid var(--c2);background:var(--el);padding:12px 16px;border-radius:0 var(--r) var(--r) 0;color:var(--t3);font-size:.9rem}
blockquote strong{color:var(--c3)}
hr{border:none;border-top:1px solid var(--bd);margin:24px 0}
.stat-card{background:var(--el);border:1px solid var(--bd);border-radius:var(--r);padding:8px 16px;font-size:.85rem;color:var(--t3);display:inline-block;margin:0 8px 8px 0}
.stat-card strong{color:var(--t1);font-size:1.1rem}
.task-done{opacity:.75}
.task-blocked{color:var(--err)}
.badge-prio{display:inline-block;padding:1px 8px;border-radius:var(--rs);font-size:.7rem;font-family:Consolas,monospace;font-weight:600;text-transform:uppercase;letter-spacing:.5px}
.badge-high{background:#F97316;color:#fff}
.badge-med{background:var(--warn);color:var(--bg)}
.badge-low{background:var(--ok);color:var(--bg)}
.status-blocked{color:var(--err);font-weight:600}
</style>

# coffe_server — TODO

> **SOURCE OF TRUTH** — Tasks identificadas por **IDs estáveis** (TSK-001..TSK-047; próximo livre: **TSK-048**). O **estado canônico é a checkbox** de cada task: `[ ]` TODO · `[>]` IN_PROGRESS · `[x]` DONE · `[!]` BLOCKED · `[-]` CANCELLED. **Dashboard, Metrics e Active Work são derivados** das checkboxes e NÃO contêm estado independente. Backlog reconstruído do zero (2026-08-13) a partir de verificação direta do código — nenhuma task foi herdada do backlog anterior sem evidência.

---

# Dashboard

> Contém apenas IDs (derivação das checkboxes em `# Tasks`).

## AGORA

- — (nenhuma task em progresso)

## PRÓXIMO

- **SECURITY:** TSK-001, TSK-002, TSK-003, TSK-004, TSK-005, TSK-006, TSK-007, TSK-008, TSK-009, TSK-010
- **BUG:** TSK-011, TSK-012, TSK-013, TSK-014
- **ARCHITECTURE:** TSK-015, TSK-016, TSK-017, TSK-018, TSK-019, TSK-020, TSK-021
- **IA:** TSK-022, TSK-023, TSK-024, TSK-025, TSK-026
- **MCP:** TSK-027, TSK-028, TSK-029
- **WEBSOCKET:** TSK-030, TSK-031, TSK-032
- **INFRA:** TSK-033, TSK-034
- **TESTS:** TSK-035, TSK-036, TSK-037, TSK-038, TSK-039
- **FEATURE:** TSK-040, TSK-041, TSK-042
- **CLEANUP:** TSK-043, TSK-044, TSK-045, TSK-046
- **DOCS:** TSK-047

## BLOQUEADO

- — (nenhuma task bloqueada)

## Metrics

> Informação **derivada** das checkboxes de status em `# Tasks`. Não editar manualmente.

<span class="stat-card"><strong>47</strong> Total</span><span class="stat-card"><strong>47</strong> TODO</span><span class="stat-card"><strong>0</strong> IN_PROGRESS</span><span class="stat-card"><strong>0</strong> DONE</span><span class="stat-card"><strong>0</strong> BLOCKED</span><span class="stat-card"><strong>0</strong> CANCELLED</span>

## Active Work

> Derivado das tasks `[>]` (IN_PROGRESS) em `# Tasks`. Sem estado duplicado.

- — (nenhuma)

---

# Tasks

> Estado canônico: a checkbox antes do metadado `Priority` em cada task. `[ ]` TODO · `[>]` IN_PROGRESS · `[x]` DONE · `[!]` BLOCKED · `[-]` CANCELLED.

## SECURITY

### TSK-001 — JwtTokenResolver exige esquema `Bearer ` no header Authorization

- [ ] **Status:** TODO
- **Priority:** HIGH
- **Category:** SECURITY · **Owner:** BOTH

**Context:** `JwtTokenResolver.java:17` faz `v.replace("Bearer ", "")` sem exigir o prefixo — um header `Authorization: <token sem prefixo>` é aceito como token válido.

**Objective:** Exigir o prefixo `Bearer `: sem ele, o resolver deve retornar `Optional.empty()` (filtro → 401).

**Expected:** header sem `Bearer ` não resolve token; header `Bearer <token>` resolve normalmente; testes da chain atualizados.

**Validation:** `.\mvnw.cmd test`

**Notes:** Mudança de comportamento — clientes que enviam token sem prefixo quebram (intencional). Relacionada a TSK-036.

**References:** `src/main/java/com/quitto/server/infrastructure/security/Token/JwtTokenResolver.java:17`

### TSK-002 — CORS ausente no SecurityConfig

- [ ] **Status:** TODO
- **Priority:** HIGH
- **Category:** SECURITY · **Owner:** BOTH

**Context:** Nenhuma configuração `CorsConfigurationSource`/`cors()` existe no projeto (grep por `CorsConfiguration` = 0 hits em `src/main/java`). O frontend estático serve do mesmo domínio hoje, mas clientes web externos (PS3, apps) serão bloqueados.

**Objective:** Adicionar CORS configurado no `SecurityConfig` (origens permitidas por propriedade, métodos e headers para `Authorization`/`Content-Type`).

**Expected:** requisições cross-origin autenticadas funcionam; configuração via propriedade (`coffee.cors.*`); testes de segurança continuam verdes.

**Validation:** `.\mvnw.cmd test`

**Notes:** Sensível na security chain — revisão humana antes de fechar.

**References:** `src/main/java/com/quitto/server/infrastructure/security/SecurityConfig.java`

### TSK-003 — Chaves TLS privadas fora do JAR

- [ ] **Status:** TODO
- **Priority:** HIGH
- **Category:** SECURITY · **Owner:** HUMAN

**Context:** `src/main/resources/keys/` contém `server.p12`, `server.key`, `server.csr`, `server.crt`, `ca.key`, `ca.crt` commitados — chaves privadas são empacotadas no JAR (`server.ssl.key-store=classpath:keys/server.p12`).

**Objective:** Mover as chaves privadas para fora do classpath (caminho externo/env) e remover os arquivos de `resources/keys` do repositório.

**Expected:** JAR sem material privado; TLS lê o keystore de caminho absoluto/env; `.gitignore` cobre o diretório de chaves.

**Validation:** `.\mvnw.cmd build` + boot local com keystore externo.

**Notes:** Mudança sensível de infraestrutura — decisão humana. Não empacotar chaves privadas no artefato final.

**References:** `src/main/resources/keys/*`, `src/main/resources/application.properties`

### TSK-004 — OAuth2UserProvisioningService usa JpaUserRepository direto (DIP)

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Context:** `OAuth2UserProvisioningService.java:14-15,33` injeta e usa `JpaUserRepository` (Spring Data) diretamente, ignorando a porta `UserRepository` do domínio.

**Objective:** Trocar pela porta `UserRepository` (injeção da interface do domínio).

**Expected:** serviço OAuth2 depende só de contratos do domínio; testes de integração de login OAuth2 (se houver) continuam verdes.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/infrastructure/services/OAuth/OAuth2UserProvisioningService.java:14-15,33`

### TSK-005 — AuthExceptionHandler: 500 genérico e sem vazar mensagens cruas

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Context:** O handler mapeia `IllegalArgumentException` → 401 com a mensagem crua da exceção no body (vaza detalhes internos) e não há handler genérico para 500.

**Objective:** Mensagem 401 sanitizada (sem refletir `e.getMessage()`); adicionar `@ExceptionHandler(Exception.class)` → 500 genérico sem detalhes internos; logar a causa via SLF4J.

**Expected:** nenhuma resposta HTTP expõe stack trace/mensagem interna; 500 padronizado.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/shared/exception/AuthExceptionHandler.java`

### TSK-006 — CookieDomain sem sameSite + CookieMapper hardcoda `Lax`

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Context:** `CookieMapper.java:18` fixa `sameSite("Lax")`; `CookieDomain` não possui campo `sameSite` — o atributo não é configurável por cookie.

**Objective:** Adicionar campo `sameSite` ao `CookieDomain` (default seguro) e fazer o `CookieMapper` usar o valor do VO em vez de constante.

**Expected:** `CookieDomain` valida valores permitidos; mapper propaga o atributo; testes de cookie system atualizados e verdes.

**Validation:** `.\mvnw.cmd test`

**Notes:** Verificar impacto no fluxo OAuth2 (redirects com `Strict`).

**References:** `src/main/java/com/quitto/server/infrastructure/security/Token/Cookies/CookieMapper.java:18`, `src/main/java/com/quitto/server/domain/valueobject/Cookie/CookieDomain.java`

### TSK-007 — Register: Bean Validation no DTO + regra única de senha

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Context:** `AuthenticationController.java:66-68` valida senha manualmente (1..500) e responde 401 em caso de falha; `RegisterDTO` não usa Bean Validation; `SpringAuthenticationService.register()` não aplica a regra mínima de 8 caracteres que existe apenas em `User.changePassword()`.

**Objective:** Unificar a regra de senha (mín. 8) no service/domínio e usar Bean Validation (`@NotBlank`, `@Email`, `@Size`) nos DTOs de auth; falha de validação deve retornar 400, não 401.

**Expected:** `POST /auth/register` com senha curta → 400 com corpo de erro; DTOs validados; testes de registro ajustados e verdes.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/application/controllers/Auth/AuthenticationController.java:66-68`, `src/main/java/com/quitto/server/application/dto/Auth/RegisterDTO.java`, `src/main/java/com/quitto/server/domain/models/User/User.java:77-80`

### TSK-008 — Logging DEBUG de security no profile default

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** SECURITY · **Owner:** AGENT

**Context:** `application.properties` mantém `logging.level.org.springframework.security=DEBUG` (e TRACE) no profile padrão — ruído e exposição de detalhes de autenticação em produção.

**Objective:** Mover o nível DEBUG/TRACE para o profile de dev (ex.: `application-h2.properties` ou profile `dev`), deixando o default em INFO/WARN.

**Expected:** boot default sem logs DEBUG de security; dev continua verboso.

**Validation:** `.\mvnw.cmd build`

**References:** `src/main/resources/application.properties`

### TSK-009 — .env.example ausente

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** SECURITY · **Owner:** AGENT

**Context:** O projeto importa `.env` via `spring.config.import=optional:file:.env[.properties]` e lê secrets de env vars, mas não existe `.env.example` documentando as variáveis (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `SERVER_API_KEY`, `GOOGLE_CLIENT_ID`, `GOOGLE_SECRET_API`, `REDIS_*`).

**Objective:** Criar `.env.example` com todas as variáveis usadas (valores vazios/placeholder) e garantir que `.env` está fora do git.

**Expected:** novo dev consegue configurar o ambiente sem ler o código; `.env` ausente do repositório.

**Validation:** inspeção visual + `git status` sem `.env`.

**References:** `.env` (local), `src/main/resources/application.properties`

### TSK-010 — Profile h2 herda TLS do default

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** SECURITY · **Owner:** AGENT

**Context:** `application.properties` define `server.ssl.enabled=true` e `application-h2.properties` não sobrescreve `server.ssl.*` — o profile h2 herda TLS (com keystore do classpath), forçando HTTPS no ambiente de dev local.

**Objective:** No profile h2, desabilitar TLS explicitamente (`server.ssl.enabled=false`) ou sobrescrever o keystore.

**Expected:** boot com profile h2 em HTTP puro; profile default continua TLS.

**Validation:** `.\mvnw spring-boot:run -Dspring-boot.run.profiles=h2` acessível via HTTP.

**References:** `src/main/resources/application-h2.properties`, `src/main/resources/application.properties`

---

## BUG

### TSK-011 — AuthenticationController.login: bloco vazio de idempotência

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** BUG · **Owner:** AGENT

**Context:** `AuthenticationController.java:47-49` contém `if(operationKeyManager.validated(key)){}` — bloco vazio (placeholder): a idempotência é verificada mas nada é feito com o resultado.

**Objective:** Definir o comportamento: se a chave já foi validada, responder a operação anterior (cache) ou ignorar; senão, implementar/remover o bloco. Remover código morto.

**Expected:** login com chave repetida tem comportamento definido e testado; sem blocos vazios.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/application/controllers/Auth/AuthenticationController.java:44-49`

### TSK-012 — CacheService.search lança exceção de idempotência em cache miss

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** BUG · **Owner:** AGENT

**Context:** `CacheService.java:44-54` — quando a chave não existe no Redis, `search()` lança `InvalidIdempotencyKeyException` em vez de retornar `Optional.empty()`. Semântica incorreta: cache miss é ausência, não erro de chave inválida.

**Objective:** Retornar `Optional.empty()` em cache miss; exceção de idempotência só para chave genuinamente inválida (ex.: null/blank).

**Expected:** `search("chave-inexistente")` → `Optional.empty()`; testes do CacheService (se houver) atualizados.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/infrastructure/services/Cache/CacheService.java:44-54`

### TSK-013 — MachineService vaza UsernameNotFoundException do Spring

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** BUG · **Owner:** AGENT

**Context:** `MachineService.java:3,28` importa e lança `org.springframework.security.core.userdetails.UsernameNotFoundException` na camada de aplicação — vazamento de framework para fora da infraestrutura.

**Objective:** Substituir por exceção de domínio (ex.: `UserNotFoundException` em `domain/exception/`) e ajustar o handler HTTP correspondente.

**Expected:** aplicação não referencia classes do Spring Security; 404/400 padronizado no endpoint que usa o service.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/application/services/Machine/MachineService.java:3,28`

### TSK-014 — User.equals por email sem hashCode

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** BUG · **Owner:** AGENT

**Context:** `User.java:102-115` implementa `equals` por email, mas não há `hashCode()` — violação do contrato equals/hashCode (documentada no javadoc :25-27). Quebra coleções hash (Set/Map) com `User`.

**Objective:** Implementar `hashCode()` consistente com `equals` (baseado no email).

**Expected:** `equals`/`hashCode` consistentes; testes de domínio de `User` cobrem o contrato.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/domain/models/User/User.java:25-27,102-115`

---

## ARCHITECTURE

### TSK-015 — CookieService: interface morta com Jakarta na infra

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** ARCHITECTURE · **Owner:** AGENT

**Context:** `infrastructure/interfaces/Cookies/CookieService.java` é uma interface de contrato (deveria ser domínio) que expõe `HttpServletResponse` (Jakarta) e não possui implementação nem usos — o fluxo atual usa `CookieManager` (domínio) + `HttpCookieWriter` (application). É código morto.

**Objective:** Remover a interface morta (e o diretório `infrastructure/interfaces/Cookies/` se vazio).

**Expected:** `CookieService` não existe mais; build verde sem referências pendentes.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/infrastructure/interfaces/Cookies/CookieService.java`

### TSK-016 — TokenResolverManager na infra → application (use case)

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** ARCHITECTURE · **Owner:** AGENT

**Context:** `TokenResolverManager` (orquestrador da chain de resolvers) está em `infrastructure/services/Auth/Token/` — é lógica de orquestração (use case), não implementação de infraestrutura.

**Objective:** Mover para `application/services/Auth/Token/`, mantendo as portas `TokenResolver`/`TokenRequestContext` no domínio e os resolvers na infra.

**Expected:** dependências continuam apontando para dentro; testes de resolvers/manager ajustam imports e ficam verdes.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/infrastructure/services/Auth/Token/TokenResolverManager.java`

### TSK-017 — RedisArryCodec em camada errada (infra em vez de domínio)

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** ARCHITECTURE · **Owner:** AGENT

**Context:** `infrastructure/interfaces/Codec/RedisArryCodec.java:7` estende `RedisCodec<String, byte[]>` (Lettuce) e fica em `infrastructure/interfaces/` — contrato que deveria pertencer ao domínio, mas dependendo de Lettuce não pode. Camada inconsistente.

**Objective:** Reavaliar: mover a interface para `domain/interfaces/` (se desacoplável) ou documentar/renomear como adapter de infra (`RedisCodecAdapter`), corrigindo o nome `Arry` → `Array`.

**Expected:** camada da interface consistente com a regra de dependências; sem `interfaces/` dentro de infra para contratos.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/infrastructure/interfaces/Codec/RedisArryCodec.java:7`

### TSK-018 — Machine anêmico: criar comportamento `wakeOnLan()` no domínio

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** ARCHITECTURE · **Owner:** AGENT

**Context:** `Machine` é data bag (só campos/getters) — não possui `wakeOnLan()` nem regras de negócio relacionadas a WOL/Tailscale.

**Objective:** Adicionar comportamento ao modelo de domínio (ex.: `wakeOnLan()` validando `macAddress`/`wolEnabled` antes de delegar o envio do magic packet à infra).

**Expected:** `Machine` com regras de WOL no domínio; testes unitários de domínio cobrem o método.

**Validation:** `.\mvnw.cmd test`

**Notes:** Pré-requisito conceitual de TSK-041.

**References:** `src/main/java/com/quitto/server/domain/models/Machine/Machine.java`

### TSK-019 — LinuxUser/Groups: persistência incompleta

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** ARCHITECTURE · **Owner:** AGENT

**Context:** Existem domain models (`LinuxUser`, `Groups`) e entities JPA (`GroupsEntity`, `LinuxUserEntity`), mas faltam mappers, adapters e Spring Data repositories — persistência não conectada às portas do domínio.

**Objective:** Implementar mapper + adapter + repository para `LinuxUser` e `Groups` (padrão dos demais agregados) e registrar portas no domínio se necessário.

**Expected:** `LinuxUserRepository`/`GroupsRepository` utilizáveis; testes de persistência com H2/Testcontainers verdes.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/domain/models/LinuxAccount/`, `src/main/java/com/quitto/server/infrastructure/db/LinuxUser/`

### TSK-020 — ExternalAccount: mapper + adapter de persistência

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** ARCHITECTURE · **Owner:** AGENT

**Context:** Domain model `ExternalAccount` e `ExternalAccountEntity` existem, mas não há mapper/adapter conectando-os à persistência.

**Objective:** Completar o adapter de `ExternalAccount` (entity → domain → entity) seguindo o padrão de `UserRepositoryAdapter`.

**Expected:** contas OAuth externas persistíveis via porta de repositório; testes de persistência verdes.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/domain/models/ExternalAccount/`, `src/main/java/com/quitto/server/infrastructure/db/User/Entity/ExternalAccountEntity.java`

### TSK-021 — User: construtor público vazio permite estado inválido

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** ARCHITECTURE · **Owner:** AGENT

**Context:** `User.java:53-55` expõe `public User()` sem campos — permite criar agregado raiz sem nome/email/role/hash.

**Objective:** Remover o construtor vazio ou torná-lo privado/package-private, forçando criação pelos construtores completos (ou factory).

**Expected:** instâncias de `User` sempre válidas; usos existentes ajustados; testes verdes.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/domain/models/User/User.java:53-55`

---

## IA

### TSK-022 — CoffeAgentService.getEnvKey() retorna vazio (stub)

- [ ] **Status:** TODO
- **Priority:** HIGH
- **Category:** IA · **Owner:** AGENT

**Context:** `CoffeAgentService.java:22-24` retorna `""` para qualquer `providerName` — sem secrets reais, a listagem/chamada de modelos falha em runtime (blocker do ecossistema IA).

**Objective:** Ler a API key de variável de ambiente (ex.: `OPENAI_API_KEY`, `ANTHROPIC_API_KEY` conforme `ServiceProvider`) e devolver `Optional<String>` (vazio quando ausente).

**Expected:** providers recebem chave real quando a env existe; ausência tratada sem exceção genérica.

**Validation:** `.\mvnw.cmd test` (com mocks) + smoke com env setada.

**References:** `src/main/java/com/quitto/server/infrastructure/services/CoffeAgent/CoffeAgentService.java:22-24`

### TSK-023 — IA endpoints Nível 2: modelsUrl() custom por provedor

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** IA · **Owner:** AGENT

**Context:** Pesquisa `docs/research/model-listing-endpoints.md` aponta 11 providers com URL de listagem incorreta. Nível 1 (Together/Cohere/Perplexity) já corrigido; falta o Nível 2: sobrescrever `modelsUrl()` para DeepInfra (`/models/list`), Novita (`/openai/v1`), Ollama (`/api/tags`), Cloudflare (`/ai/models/search`), Fireworks (`accounts/{id}`), Azure (`api-version`).

**Objective:** Implementar as URLs custom nos providers listados, mantendo o parser do `BaseProvider` (ajustando `parseModels` onde o formato diferir, ex.: Ollama).

**Expected:** `getModels()` retorna catálogo real para os 6 providers; testes com HTTP mockado verdes (TSK-037).

**Validation:** `.\mvnw.cmd test`

**Notes:** Ollama exige override de parse (`{models:[{name, model}]}`); Cloudflare exige `task=Text Generation`.

**References:** `docs/research/model-listing-endpoints.md`, `src/main/java/com/quitto/server/infrastructure/IA/BaseProvider.java`

### TSK-024 — IA endpoints Nível 3: SDK/assinatura (Vertex, watsonx, OCI, Bedrock)

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** IA · **Owner:** AGENT

**Context:** TODOs no código (`AwsBedrockProvider.java:45`, `OCIGenerativeAIProvider.java:45`, `FireworksAIProvider.java:39`) indicam pendência de auth por SDK/assinatura (SigV4, OCI signing, IAM) — não cabem no padrão Bearer do `BaseProvider`.

**Objective:** Sobrescrever `fetchModelsFromApi()` com SDK/assinatura para AWS Bedrock, IBM watsonx, OCI e Vertex AI.

**Expected:** listagem de modelos funcional nos 4 provedores cloud; sem TODOs pendentes.

**Validation:** `.\mvnw.cmd test` (mocks) — execução real depende de credenciais.

**References:** `src/main/java/com/quitto/server/infrastructure/IA/AwsBedrockProvider.java:45`, `OCIGenerativeAIProvider.java:45`, `FireworksAIProvider.java:39`, `docs/research/model-listing-endpoints.md`

### TSK-025 — Typos IA: AIProvaider*, getModelsForProvaider, ProvaiderIAService, packages

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** IA · **Owner:** AGENT

**Context:** Typos públicos persistem: `AIProvaiderController`, `AIProvaiderPort`, `AIProvaiderAdpiter`, `ProvaiderIAService`, método `getModelsForProvaider` (`AIRegistry.java:27`), `findProvaider`, packages `Provaiders`/`Reagistry`.

**Objective:** Renomear classes, métodos e packages para `Provider`/`Registry` (ex.: `AIProviderController`, `AIProviderRegistry`, `getModelsForProvider`, `findProvider`), atualizando todos os imports e usos.

**Expected:** zero typos de `Provaider`/`Reagistry`/`Adpiter` no código; build verde.

**Validation:** `.\mvnw.cmd test`

**Notes:** Renomeação de packages quebra imports — planejar com cuidado (grep completo antes).

**References:** `src/main/java/com/quitto/server/application/controllers/IA/`, `src/main/java/com/quitto/server/infrastructure/services/IA/`

### TSK-026 — CoffeAgentService: field injection `@Value` → constructor

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** IA · **Owner:** AGENT

**Context:** `CoffeAgentService.java:19-20` usa `@Value` em campos (field injection) — a convenção do projeto é constructor injection.

**Objective:** Converter para construtor com `@Value` no parâmetro (padrão já usado em `JwtTokenService`).

**Expected:** sem field injection no serviço; propriedades resolvidas nos profiles; testes verdes.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/infrastructure/services/CoffeAgent/CoffeAgentService.java:19-20`

---

## MCP

### TSK-027 — GoogleCalendarService.createEvent() stub + field injection

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** MCP · **Owner:** AGENT

**Context:** `GoogleCalendarService.java:19-21` — `createEvent()` retorna `""` (stub); o client é campo `@Autowired` público (:16-17).

**Objective:** Implementar `createEvent()` real (chamada à API Calendar) e trocar para constructor injection.

**Expected:** criação de evento retorna o evento criado (id); sem field injection; testes MCP ajustados (TSK-038/TSK-083 antigo).

**Validation:** `.\mvnw.cmd test`

**Notes:** Depende do fluxo OAuth2 Google existente (a ser refatorado futuramente — fora de escopo deste TODO).

**References:** `src/main/java/com/quitto/server/mcp/services/GoogleCalendarService.java:16-21`

### TSK-028 — GoogleCalendarTools: injection + logging + tratamento de exceções

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** MCP · **Owner:** AGENT

**Context:** `GoogleCalendarTools` usa campos `@Autowired` públicos (:21-25), `System.err.println` (:40,42) e engole exceções retornando lista vazia — falhas de MCP ficam invisíveis para o agente.

**Objective:** Constructor injection, SLF4J no lugar de `System.err`, e propagar/descrever erros na resposta da tool (não lista vazia silenciosa).

**Expected:** tool MCP reporta erro ao agente; logs estruturados; sem field injection.

**Validation:** `.\mvnw.cmd test` (McpToolTest)

**References:** `src/main/java/com/quitto/server/mcp/tools/GoogleCalendarTools.java`

### TSK-029 — CalendarController (REST) dentro de mcp/tools + permitAll + println

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** MCP · **Owner:** AGENT

**Context:** `mcp/tools/CalendarController.java` é controller REST (não MCP) com `@PreAuthorize("permitAll()")` e `System.out/err.println` (:31,43,46) — camada MCP misturada com REST público.

**Objective:** Mover para `application/controllers/` (REST), restringir acesso (autenticado) e trocar prints por SLF4J.

**Expected:** nenhum controller REST em `mcp/`; endpoints de calendário autenticados; logs estruturados.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/mcp/tools/CalendarController.java`

---

## WEBSOCKET

### TSK-030 — WebSocket handshake sem autenticação JWT

- [ ] **Status:** TODO
- **Priority:** HIGH
- **Category:** WEBSOCKET · **Owner:** AGENT

**Context:** `WebSocketConfig.java` registra `/protocol` sem `HandshakeInterceptor` nem filtro — o endpoint STOMP é acessível sem token, ignorando a chain de resolvers do projeto.

**Objective:** Autenticar o handshake reutilizando `TokenResolverManager` (cookie/header), rejeitando conexões sem JWT válido.

**Expected:** handshake sem token falha; com token válido conecta; testes STOMP cobrem (TSK-038).

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/infrastructure/config/WebSocket/WebSocketConfig.java`, `src/main/java/com/quitto/server/infrastructure/services/Auth/Token/TokenResolverManager.java`

### TSK-031 — Destinos STOMP sem proteção por role + allowedOrigins ausente

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** WEBSOCKET · **Owner:** AGENT

**Context:** `AgentWebSocketController.java:29-30` expõe `@MessageMapping("/agent")` sem controle de autorização; `WebSocketConfig` não define `setAllowedOrigins`.

**Objective:** Proteger destinos por role (ex.: `ROLE_MCP` para `/agent`) e configurar origens permitidas por propriedade.

**Expected:** envio sem a role correta é rejeitado no canal STOMP; origens configuráveis.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/application/controllers/WebSocket/AgentWebSocketController.java:29-30`, `src/main/java/com/quitto/server/infrastructure/config/WebSocket/WebSocketConfig.java`

### TSK-032 — AgentWebSocketController responde resposta canônica

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** WEBSOCKET · **Owner:** AGENT

**Context:** O controller chama `requestManager.request(...)` mas responde ao cliente com texto fixo ("processed"), descartando o resultado real do protocolo do agente (`CoffeAgentRequestManager`).

**Objective:** Retornar a resposta real do `CoffeAgentRequestManager` ao cliente (mapear para o formato de saída do canal).

**Expected:** cliente STOMP recebe resposta útil (conteúdo do agente), não placeholder.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/application/controllers/WebSocket/AgentWebSocketController.java`, `src/main/java/com/quitto/server/infrastructure/services/CoffeAgent/CoffeAgentRequestManager.java`

---

## INFRA

### TSK-033 — Dockerfile multi-stage + docker-compose prod ausentes

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** INFRA · **Owner:** HUMAN

**Context:** Não existem `Dockerfile`, `docker-compose.yml` nem `docker-compose.prod.yml` no repositório (glob não encontrou).

**Objective:** Criar Dockerfile multi-stage (build Maven → runtime JRE 21) e compose de produção (app + postgres + redis cache/rate-limit), com secrets via env.

**Expected:** `docker compose up` sobe o ecossistema completo; JAR sem chaves TLS empacotadas (relacionado a TSK-003).

**Validation:** `docker compose build` + `docker compose up` smoke.

**Notes:** Decisão humana (infra do homelab).

### TSK-034 — CI GitHub Actions ausente

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** INFRA · **Owner:** HUMAN

**Context:** Não existe `.github/workflows/` — build/testes não rodam em CI.

**Objective:** Workflow Maven (Java 21) com `.\mvnw.cmd test` no profile de teste e report de cobertura.

**Expected:** PRs rodam a suíte completa; falhas bloqueiam merge.

**Validation:** push de teste do workflow.

---

## TESTS

### TSK-035 — Teste de rate limit (429) em /auth/login e /auth/register

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Context:** O `RateLimitFilter` implementa política por rota (enum `RateLimitPolicy`) e responde 429, mas não há teste cobrindo o estouro de limite.

**Objective:** Testar que exceder o limite em login/register retorna 429 (e que o limite normal passa).

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/infrastructure/security/Filter/RateLimit/`, `src/test/java/`

### TSK-036 — Testes de segurança 401/403 nas rotas protegidas

- [ ] **Status:** TODO
- **Priority:** HIGH
- **Category:** TESTS · **Owner:** AGENT

**Context:** Existem testes de integração de auth, mas não há suíte dedicada validando 401 sem token e 403 com role incorreta nas rotas protegidas (ex.: `/api/**`, `/mcp/**`).

**Objective:** Testar acesso negado sem token, com token inválido e com role errada; incluir o caso do `Bearer` obrigatório (TSK-001).

**Validation:** `.\mvnw.cmd test`

### TSK-037 — Testes dos providers de IA (HTTP mockado)

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Context:** Nenhum teste cobre `BaseProvider`/providers concretos — parsing de modelos e erros HTTP não são validados.

**Objective:** Testar parsing de `getModels()` com respostas mockadas por provider (raízes `data`/`models`/`result`, erros 4xx/5xx → `ProviderException`).

**Validation:** `.\mvnw.cmd test`

**Notes:** Pré-requisito para fechar TSK-023/024 com segurança.

### TSK-038 — Testes WebSocket (handshake, auth e tópicos)

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Context:** Não há testes STOMP/WebSocket cobrindo o endpoint `/protocol`.

**Objective:** Testar handshake sem/com token (TSK-030), envio sem role (TSK-031) e resposta do `/agent` (TSK-032).

**Validation:** `.\mvnw.cmd test`

### TSK-039 — E2E happy path (register → login → cookie → /api/test)

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** TESTS · **Owner:** AGENT

**Context:** Os testes existentes cobrem partes do fluxo, mas não o caminho feliz completo via HTTP real.

**Objective:** Fluxo completo com Testcontainers (Postgres): registrar, logar, capturar cookie, acessar `/api/test` autenticado.

**Validation:** `.\mvnw.cmd test`

---

## FEATURE

### TSK-040 — CRUD de máquinas REST

- [ ] **Status:** TODO
- **Priority:** HIGH
- **Category:** FEATURE · **Owner:** AGENT

**Context:** `MachineRepository` (porta) e `MachineRepositoryAdapter` estão completos e `MachineService` existe, mas não há controller REST de máquinas — o domínio está sem exposição.

**Objective:** Endpoints CRUD de máquinas (listar por owner, criar, atualizar, deletar) com permissão por dono.

**Expected:** REST de máquinas funcional e autenticado; testes de integração cobrindo ownership.

**Validation:** `.\mvnw.cmd test`

### TSK-041 — Wake-on-LAN (magic packet)

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** FEATURE · **Owner:** AGENT

**Context:** `Machine` tem `macAddress`/`wolEnabled`, mas nenhuma implementação envia magic packet (UDP 9).

**Objective:** Implementar envio de magic packet na infra (ex.: `WakeOnLanService`), validando via domínio (TSK-018).

**Expected:** endpoint/ferramenta aciona WOL de máquina com `wolEnabled=true`; testes unitários do pacote.

**Validation:** `.\mvnw.cmd test`

### TSK-042 — Integração Tailscale (listar máquinas)

- [ ] **Status:** TODO
- **Priority:** MEDIUM
- **Category:** FEATURE · **Owner:** AGENT

**Context:** `Machine` possui `tailscaleNodeKey`, mas nenhuma integração consulta a API Tailscale.

**Objective:** Serviço de integração (infra) que lista/verifica máquinas via API Tailscale usando a chave do node.

**Expected:** status das máquinas consultável; falha de integração tratada sem derrubar o fluxo.

**Validation:** `.\mvnw.cmd test` (mocks)

---

## CLEANUP

### TSK-043 — Varredura System.out/err e imports não usados

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** CLEANUP · **Owner:** AGENT

**Context:** Restam `System.out/err` (CalendarController já em TSK-029, GoogleCalendarTools em TSK-028 — aqui a varredura geral) e imports não usados/duplicados no código.

**Objective:** Varrer todo `src/main/java` e `src/test/java` removendo prints restantes (→ SLF4J) e imports mortos.

**Expected:** zero `System.out/err` no código; imports limpos.

**Validation:** `.\mvnw.cmd test`

### TSK-044 — Typos Redis/Database: getAdpterConnector, getProvaiders, DatabaseProvaider

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** CLEANUP · **Owner:** AGENT

**Context:** `DatabaseClientProvider` declara `getAdpterConnector(...)`/`getProvaiders(...)` e o package `infrastructure/services/DatabaseProvaider/` mantém o typo `Provaider`.

**Objective:** Renomear métodos e package para `getAdapterConnector`/`getProviders`/`DatabaseProvider`, atualizando usos (`CacheService`, `RedisClientProvider`, `Bucket4jRateLimiter`).

**Expected:** zero typos de `Adpter`/`Provaider` no ecossistema Redis/Database.

**Validation:** `.\mvnw.cmd test`

**Notes:** `RedisClientInstace` tem javadoc documentando que o typo histórico NÃO deve ser renomeado — manter.

**References:** `src/main/java/com/quitto/server/domain/interfaces/Database/DatabaseClientProvider.java`, `src/main/java/com/quitto/server/infrastructure/services/DatabaseProvaider/`

### TSK-045 — HttpCookieWriterManeger → HttpCookieWriterManager

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** CLEANUP · **Owner:** AGENT

**Context:** Classe `HttpCookieWriterManeger` (application/interfaces/Cookies) mantém o typo `Maneger`.

**Objective:** Renomear para `HttpCookieWriterManager` e atualizar usos.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/application/interfaces/Cookies/HttpCookieWriterManeger.java`

### TSK-046 — User: parâmetro `passowrd` no construtor

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** CLEANUP · **Owner:** AGENT

**Context:** `User.java:45` — o construtor `User(long, String, String passowrd, ...)` usa o parâmetro com typo.

**Objective:** Renomear para `password`.

**Validation:** `.\mvnw.cmd test`

**References:** `src/main/java/com/quitto/server/domain/models/User/User.java:45`

---

## DOCS

### TSK-047 — ADRs e redis-abstraction deletados sem commit; docs de arquitetura desatualizadas

- [ ] **Status:** TODO
- **Priority:** LOW
- **Category:** DOCS · **Owner:** BOTH

**Context:** `git status` mostra `docs/architecture/adr/ADR-001..005`, `README.md` e `redis-abstraction.*` como **deletados sem commit**; `docs/architecture/arquiteture.md` ainda descreve classes renomeadas/removidas (ex.: `CookieService` na infra, `JtwTokenResvoler`, `RegisterResponseDTO`).

**Objective:** Decidir o destino das deleções (commit de remoção ou restore) e atualizar `arquiteture.md`/`relatorio-completo.md` para refletir o estado real (resolvers renomeados, idempotency, cookie-only auth, packages corrigidos).

**Expected:** `git status` limpo (deleções decididas); docs de arquitetura sem referências a classes inexistentes.

**Validation:** inspeção de `git status` + revisão das docs.

**References:** `docs/architecture/`, `docs/audits/`

---

## Completed

> Marcador de conclusão — **não é uma task** (sem ID). O estado das tasks DONE vive nas próprias checkboxes `[x]` em `# Tasks`. Nenhuma task foi carregada como DONE nesta reconstrução; refactors verificados no código (token resolvers + `Optional`, typos de classes públicas, idempotency, rate-limit por rota, constructor injection em JWT/filter, URLs de listagem Nível 1, frontend Vite, UserService completo) foram confirmados e por isso **não geraram tasks**.

## Cancelled / Obsolete

> Claims legadas do backlog anterior **sem IDs** e **fora da contagem** de tasks — registradas apenas para histórico, pois o backlog foi reconstruído do zero com evidência.

- [-] Typos em classes públicas (`JtwTokenResvoler`, `BCryptPassowordService`, `Provaider`, `ExternalAccont`, `GoogelCalenderTools`, `GoogleCalenderService`, `LoginDTO.passoword`) — verificados como corrigidos no código
- [-] `JwtTokenService.extractIdSubject` retornava `null` — verificado: retorna `Optional<Long>`
- [-] `MachineRepositoryAdapter.setOwner()` retornava `new User()` vazio — verificado: `setOwner` real (retorna `Optional<Machine>` via `updateOwner`)
- [-] `RateLimitFilter` com catch vazios — verificado: logging SLF4J + `sendError` implementados
- [-] `UserService` esqueleto — verificado: implementado
- [-] `login.html` inexistente — verificado: existe (`templates/` + frontend `static/app/pages/login.html`)
- [-] `RegisterResponseDTO`/`LoginResponseDTO` — removidos (fluxo cookie-only)
- [-] mTLS (client-auth=require) — sem ADR ativa (arquivos ADR deletados sem commit); fora de escopo até decisão
- [-] Modularização Maven multi-module — sem ADR ativa; fora de escopo até decisão

---

## Task Registry

> Sumário por ID. **Derivado** das checkboxes em `# Tasks` — não editar manualmente. Estados: `[ ]` TODO · `[>]` IN_PROGRESS · `[x]` DONE · `[!]` BLOCKED · `[-]` CANCELLED.

| ID | Estado | Prio | Categoria | Título curto |
|----|--------|------|-----------|--------------|
| TSK-001 | `[ ]` | HIGH | SECURITY | JwtTokenResolver exige `Bearer ` |
| TSK-002 | `[ ]` | HIGH | SECURITY | CORS ausente no SecurityConfig |
| TSK-003 | `[ ]` | HIGH | SECURITY | Chaves TLS fora do JAR |
| TSK-004 | `[ ]` | MEDIUM | SECURITY | OAuth2 → porta UserRepository |
| TSK-005 | `[ ]` | MEDIUM | SECURITY | Handler 500 genérico + sem vazar mensagens |
| TSK-006 | `[ ]` | MEDIUM | SECURITY | CookieDomain.sameSite + mapper |
| TSK-007 | `[ ]` | MEDIUM | SECURITY | Register: Bean Validation + regra de senha |
| TSK-008 | `[ ]` | LOW | SECURITY | DEBUG security → dev |
| TSK-009 | `[ ]` | LOW | SECURITY | .env.example |
| TSK-010 | `[ ]` | MEDIUM | SECURITY | h2 herda TLS |
| TSK-011 | `[ ]` | MEDIUM | BUG | Bloco vazio de idempotência no login |
| TSK-012 | `[ ]` | MEDIUM | BUG | CacheService.search em cache miss |
| TSK-013 | `[ ]` | MEDIUM | BUG | MachineService vaza exceção Spring |
| TSK-014 | `[ ]` | LOW | BUG | User.equals sem hashCode |
| TSK-015 | `[ ]` | MEDIUM | ARCHITECTURE | CookieService interface morta |
| TSK-016 | `[ ]` | MEDIUM | ARCHITECTURE | TokenResolverManager → application |
| TSK-017 | `[ ]` | LOW | ARCHITECTURE | RedisArryCodec camada errada |
| TSK-018 | `[ ]` | MEDIUM | ARCHITECTURE | Machine.wakeOnLan() no domínio |
| TSK-019 | `[ ]` | MEDIUM | ARCHITECTURE | LinuxUser/Groups persistence |
| TSK-020 | `[ ]` | LOW | ARCHITECTURE | ExternalAccount mapper/adapter |
| TSK-021 | `[ ]` | LOW | ARCHITECTURE | User construtor vazio |
| TSK-022 | `[ ]` | HIGH | IA | getEnvKey stub |
| TSK-023 | `[ ]` | MEDIUM | IA | Endpoints Nível 2 (modelsUrl) |
| TSK-024 | `[ ]` | LOW | IA | Endpoints Nível 3 (SDK) |
| TSK-025 | `[ ]` | MEDIUM | IA | Typos AIProvaider/Reagistry |
| TSK-026 | `[ ]` | LOW | IA | CoffeAgentService constructor injection |
| TSK-027 | `[ ]` | MEDIUM | MCP | createEvent stub + field injection |
| TSK-028 | `[ ]` | MEDIUM | MCP | Tools: injection + SLF4J + erros |
| TSK-029 | `[ ]` | MEDIUM | MCP | CalendarController fora de mcp |
| TSK-030 | `[ ]` | HIGH | WEBSOCKET | Handshake sem auth |
| TSK-031 | `[ ]` | MEDIUM | WEBSOCKET | Destinos sem role + origins |
| TSK-032 | `[ ]` | MEDIUM | WEBSOCKET | Resposta canônica do /agent |
| TSK-033 | `[ ]` | MEDIUM | INFRA | Dockerfile + compose prod |
| TSK-034 | `[ ]` | MEDIUM | INFRA | CI GitHub Actions |
| TSK-035 | `[ ]` | MEDIUM | TESTS | Rate limit 429 |
| TSK-036 | `[ ]` | HIGH | TESTS | Segurança 401/403 |
| TSK-037 | `[ ]` | MEDIUM | TESTS | Providers IA mockados |
| TSK-038 | `[ ]` | MEDIUM | TESTS | WebSocket STOMP |
| TSK-039 | `[ ]` | MEDIUM | TESTS | E2E happy path |
| TSK-040 | `[ ]` | HIGH | FEATURE | CRUD de máquinas REST |
| TSK-041 | `[ ]` | MEDIUM | FEATURE | Wake-on-LAN |
| TSK-042 | `[ ]` | MEDIUM | FEATURE | Tailscale |
| TSK-043 | `[ ]` | LOW | CLEANUP | System.out/err + imports |
| TSK-044 | `[ ]` | LOW | CLEANUP | Typos Redis/Database |
| TSK-045 | `[ ]` | LOW | CLEANUP | HttpCookieWriterManeger |
| TSK-046 | `[ ]` | LOW | CLEANUP | User param passowrd |
| TSK-047 | `[ ]` | LOW | DOCS | ADRs deletados + docs desatualizadas |

---

# Agent Protocol

> Regras para agentes que editam este arquivo — **obrigatório**.

1. **Estado canônico = checkbox** em `# Tasks`. Nunca duplicar descrição/estado em Dashboard, Metrics, Active Work ou Registry (são derivados — atualizar apenas as checkboxes).
2. **IDs estáveis**: TSK-001..TSK-047. Próximo ID: **TSK-048**. Nunca reutilizar IDs cancelados.
3. **Formatos de status**: `[ ]` TODO · `[>]` IN_PROGRESS · `[x]` DONE · `[!]` BLOCKED · `[-]` CANCELLED. Tasks BLOCKED devem declarar `Blocked By` (task, ADR ou decisão). Tasks CANCELLED/obsoletas sem ID ficam na seção `## Cancelled / Obsolete`.
4. **Estrutura mínima por task**: `### TSK-XXX — Ação clara` → linha de status+`Priority` → metadados (`Category`, `Owner`, `Estimate`) → `**Context:**` + `**Objective:**`. Blocos `Expected`/`Validation`/`Notes`/`References` apenas quando houver conteúdo verificável.
5. **Mover estado**: se uma task entra/sai de DONE/BLOCKED/IN_PROGRESS, o Dashboard e o Registry são atualizados **na mesma edição** (derivação imediata).
6. **Sempre concluir com** `.\mvnw.cmd test` antes de marcar algo DONE relacionado a código.
7. **Sem emojis** em tasks, seções ou CSS (marcadores ASCII apenas: `[ ]`/`[x]`).

---

> **Documento mantido por:** Quitto · **Última atualização:** 2026-08-13
> **Propósito:** Documentação viva — atualize conforme o código evoluir.
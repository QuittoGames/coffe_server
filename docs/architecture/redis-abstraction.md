# Redis Abstraction — Clean Architecture (Ports & Adapters)

> **Status:** ✅ Implementado (2026-07-30) · Atualizado 2026-08-05
> **Stack:** Lettuce 6.8.2 + Spring Boot 4.0.6
> **Propósito:** Abstrair acesso ao Redis de forma que o domínio não dependa de bibliotecas externas (Lettuce, Netty).
> **Validação:** Smoke test real contra Redis 7.4.10 remoto (2026-08-04).

---

## 📐 Arquitetura

### Diagrama de Camadas

```
┌─────────────────────────────────────────────────────────────────┐
│  DOMAIN LAYER  (puro — zero frameworks)                         │
│                                                                  │
│  ┌──────────────────────────────┐   ┌─────────────────────────┐ │
│  │  Connection<T>  (interface)  │   │  DatabaseClientProvider │ │
│  │  ─────────────────────────── │   │  (interface / porta)    │ │
│  │  + isOpen(): boolean         │   │  ─────────────────────  │ │
│  │  + close(): void             │   │  + getAdpterConnector(  │ │
│  └──────────┬───────────────────┘   │    name): T             │ │
│             │                        │  + getProvaiders(      │ │
│             │                        │    P): Map<String, T>  │ │
│  ┌──────────▼────────────────────────▼─────────────────────┐  │
│  │  DatabaseClient  (model)                                │  │
│  │  name, host, port, enabled                              │  │
│  └─────────────────────────────────────────────────────────┘  │
│             ▲                                                  │
│             │ extends                                           │
│  ┌──────────┴───────────┐   ┌───────────────────────────────┐ │
│  │  RedisClientInstace  │   │  DatabaseProperties          │ │
│  │  ──────────────────  │   │  (interface de config)       │ │
│  │  + password: String  │   │  ──────────────────────────  │ │
│  │  + useSsl: boolean   │   │  Implementada pela infra     │ │
│  └──────────┬───────────┘   └──────────────┬────────────────┘ │
│             │                              │                   │
│  ┌──────────▼──────────────────────────────▼────────────────┐  │
│  │  DatabaseClientProvider<T extends Connection,           │  │
│  │                       P extends DatabaseProperties>     │  │
│  │  Porta genérica — contrato do provider de conexões      │  │
│  └─────────────────────────────────────────────────────────┘  │
├─────────────────────────────────────────────────────────────────┤
│  INFRASTRUCTURE LAYER  (Lettuce + Netty + Spring)               │
│                                                                  │
│  ┌────────────────────────────┐  ┌──────────────────────────┐  │
│  │ RedisClientConnectionAdpter │  │ RedisClientProvider       │  │
│  │ implements Connection        │  │ @Component               │  │
│  │ ─────────────────────────── │  │ @ConditionalOnProperty   │  │
│  │ Wraps: StatefulRedisConn    │  │  (coffee.redis.enabled)  │  │
│  │        + RedisAsyncCommands │  │ implements                │  │
│  └──────────┬──────────────────┘  │   DatabaseClientProvider │  │
│             │                     │ ─────────────────────────│  │
│             │                     │ Lê properties → cria     │  │
│  ┌──────────┴──────────────────┐  │ clients → conexões lazy  │  │
│  │ StringByteArrayCodec        │  │ Map<String, Adapter>     │  │
│  │ @Component                  │  │ + shutdown() @PreDestroy │  │
│  │ implements RedisArryCodec   │  └────────────┬─────────────┘  │
│  └─────────────────────────────┘               │               │
│                                                 │               │
│  ┌──────────────────────────────────────────────▼───────────┐  │
│  │ RedisProperties (config)                                 │  │
│  │ @ConfigurationProperties(prefix="coffee.redis")          │  │
│  │ implements DatabaseProperties                            │  │
│  │ List<RedisClientInstace> instances                       │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Consumers:  Bucket4jConfig → RateLimit (Bucket4jRateLimiter)  │
└─────────────────────────────────────────────────────────────────┘
```

### Fluxo de Dependência

```
DOMAIN  ◄──────  INFRASTRUCTURE  ◄──────  Lettuce / Netty
──────           ──────────────           ─────────────────
Connection       RedisClientConnectionAdapter  io.lettuce.core.*
DatabaseClient   implements Connection         io.netty.buffer.*
DatabaseProperties
DatabaseClientProvider   RedisClientProvider
                 implements DatabaseClientProvider
```

**Regra respeitada:** domínio não importa Spring, Jakarta, Lettuce ou Netty. ✅

---

## 🧩 Componentes

### Domínio (ports)

| Componente | Arquivo | Responsabilidade |
|---|---|---|
| `Connection<T>` | `domain/Database/Connection.java` | **Porta.** Contrato de conexão: `isOpen()` e `close()` |
| `DatabaseClient` | `domain/Database/DatabaseClient.java` | **Modelo.** Dados de config de um cliente (name, host, port, enabled) |
| `RedisClientInstace` | `domain/Database/redis/RedisClientInstace.java` | **Modelo.** Extends `DatabaseClient` + campos `password` e `useSsl` |
| `DatabaseProperties` | `domain/Database/DatabaseProperties.java` | **Interface.** Contrato de propriedades de banco (implementada pela infra) |
| `DatabaseClientProvider<T extends Connection, P extends DatabaseProperties>` | `domain/interfaces/Database/DatabaseClientProvider.java` | **Porta.** `getAdpterConnector(name)` → conexão; `getProvaiders(P)` → mapa |

### Infraestrutura (adapters)

| Componente | Arquivo | Responsabilidade |
|---|---|---|
| `RedisClientConnectionAdapter` | `infrastructure/Adapters/in/RedisClientConnectionAdapter.java` | **Adapter.** Wrapper Lettuce → `Connection` do domínio; expõe `getCommands()` (async) e `putHash()` |
| `RedisClientProvider` | `infrastructure/services/DatabaseProvaider/redis/RedisClientProvider.java` | **Provider.** `@Component` condicional (`coffee.redis.enabled=true`); lê config → cria `RedisClient` Lettuce no construtor; conexões **lazy** cacheadas em `ConcurrentHashMap`; `getConnection(name)` valida `isOpen()`; `@PreDestroy shutdown()` fecha tudo |
| `RedisProperties` | `infrastructure/config/redis/RedisProperties.java` | **Config.** `@ConfigurationProperties(prefix="coffee.redis")`, implementa `DatabaseProperties` (domínio); usa `RedisClientInstace` do domínio |
| `RedisConfig` | `infrastructure/config/redis/RedisConfig.java` | **Config.** `@EnableConfigurationProperties(RedisProperties.class)` |
| `StringByteArrayCodec` | `infrastructure/config/redis/Codec/StringByteArrayCodec.java` | **Codec.** Key=String(UTF-8), Value=byte[]; implementa `RedisArryCodec` |

---

## 🔄 Fluxo de Uso

Um consumidor obtém uma conexão Redis assim (constructor injection):

```java
@Service
public class MeuServico {

    private final DatabaseClientProvider<RedisClientConnectionAdapter, RedisProperties> redisProvider;

    public MeuServico(DatabaseClientProvider<RedisClientConnectionAdapter, RedisProperties> redisProvider) {
        this.redisProvider = redisProvider;
    }

    public void exemplo() {
        RedisClientConnectionAdapter conn = redisProvider.getAdpterConnector("cache");

        if (conn.isOpen()) {
            RedisAsyncCommands<String, byte[]> cmd = conn.getCommands();
            cmd.set("chave", "valor".getBytes());
        }
    }
}
```

Para quem precisa da conexão bruta (ex.: Bucket4j), o `RedisClientProvider` oferece `getConnection(name)` — devolve a `StatefulRedisConnection<String, byte[]>` e lança `DataAccessResourceFailureException` se indisponível:

```java
RedisAsyncCommands<String, byte[]> commands = provider.getConnection("rate-limit").async();
```

Internamente, o `RedisClientProvider`:

1. **Construtor:** recebe `RedisProperties` e cria os `RedisClient` Lettuce (baratos — sem conexão de rede) para cada instância configurada
2. `buildUri(RedisClientInstace)` monta a `RedisURI` via **`RedisURI.Builder`** (em vez de string `"redis://host:port"`) — necessário para suportar senha e TLS sem problemas de URL-encoding (ex.: senhas com `@` ou `:`)
3. **Conexões lazy:** `getAdpterConnector(name)` usa `computeIfAbsent` — a conexão só é criada no primeiro uso e fica cacheada
4. `connect(name)` cria a `StatefulRedisConnection` com o `StringByteArrayCodec` e encapsula em `RedisClientConnectionAdapter`; lança `IllegalArgumentException` se a instância não existe
5. `getConnection(name)` valida o nome e o estado da conexão (`isOpen()`), lançando `DataAccessResourceFailureException` se indisponível
6. **Shutdown:** `@PreDestroy shutdown()` fecha todas as conexões (`close()`) e todos os clients (`client.shutdown(0, 1, TimeUnit.SECONDS)`) — elimina vazamento de threads Netty

---

## 🏗️ Por que `RedisClientInstace` em vez de um POJO de infra?

Originalmente `RedisProperties` usava `RedisClientConnection` (infra POJO com `name`, `host`, `port`). Agora usa `RedisClientInstace`, que estende `DatabaseClient` do domínio.

**Vantagens:**
- **Sem duplicação** — `DatabaseClient` já tem name, host, port, enabled
- **Domínio rico** — se no futuro precisar de regras como `isValid()` ou `connectionString()`, é no domínio que elas ficam
- **Spring ainda consegue fazer binding** — `DatabaseClient` tem setters, `@ConfigurationProperties` funciona normalmente
- **Um a menos pra manter** — `RedisClientConnection.java` foi removido

O trade-off é que o domínio ganha setters (que idealmente não existiriam num modelo puro), mas é um preço aceitável pela eliminação da duplicata.

---

## 🔐 Segurança

### TLS/SSL — ✅ Implementado

O `RedisClientProvider` usa `RedisURI.Builder.redis(host, port)` e aplica `.withSsl(true)` quando a instância tem `useSsl` ativo:

```java
// infrastructure/services/DatabaseProvaider/redis/RedisClientProvider.java
private RedisURI buildUri(RedisClientInstace config) {
    RedisURI.Builder builder = RedisURI.Builder.redis(config.getHost(), config.getPort());

    if (config.getPassword() != null && !config.getPassword().isBlank()) {
        builder.withPassword(config.getPassword());
    }
    if (config.isUseSsl()) {
        builder.withSsl(true);
    }

    return builder.build();
}
```

### Autenticação — ✅ Implementado

`RedisClientInstace` (domínio) tem o campo `password` desde 2026-08-04. A senha é aplicada via `builder.withPassword()` **somente quando não-blank** — instâncias sem `requirepass` continuam funcionando.

### Properties

As instâncias são configuradas via env vars mapeadas no `application.properties`:

```properties
## Redis Cache
coffee.redis.instances[0].name=cache
coffee.redis.instances[0].host=${REDIS_HOST}
coffee.redis.instances[0].port=${REDIS_CACHE_PORT:6379}
coffee.redis.instances[0].password=${REDIS_CACHE_PASSWORD:}
coffee.redis.instances[0].use-ssl=${REDIS_CACHE_SSL:false}

## Redis Rate Limit
coffee.redis.instances[1].name=rate-limit
coffee.redis.instances[1].host=${REDIS_HOST}
coffee.redis.instances[1].port=${REDIS_RATELIMIT_PORT:6379}
coffee.redis.instances[1].password=${REDIS_RATELIMIT_PASSWORD:}
coffee.redis.instances[1].use-ssl=${REDIS_RATELIMIT_SSL:false}
```

### Resource Management — ✅ Implementado

`@PreDestroy shutdown()` no `RedisClientProvider` fecha conexões e clients no shutdown do Spring, evitando vazamento de threads Netty.

---

## 🧩 Integração com Rate Limit (Bucket4j)

O `Bucket4jConfig` consome a abstração Redis através do provider:

```java
@Configuration
@ConditionalOnProperty(prefix = "coffee.ratelimit", name = "enabled", havingValue = "true")
public class Bucket4jConfig {

    @Bean("loginBucket")
    public BucketConfiguration loginBuckeConfig() { /* ... */ }

    @Bean
    public RateLimit rateLimit(RedisClientProvider provider, PolicyProvider policyProvider) {
        return new Bucket4jRateLimiter(provider, policyProvider);
    }
}
```

O `Bucket4jRateLimiter` cria o `ProxyManager` distribuído de forma **lazy** — apenas na primeira chamada de `tryConsume` — conectando na instância `"rate-limit"`. O boot do servidor **não depende** do Redis estar no ar.

---

## 📋 Estado Atual & Pendências

### ✅ Concluído (2026-07-30 → 2026-08-04)

| Item | Status |
|---|---|
| Portas `Connection` / `DatabaseClientProvider` no domínio | ✅ |
| Adapters Lettuce na infra (`RedisClientConnectionAdapter`, `RedisClientProvider`) | ✅ |
| `@EnableConfigurationProperties(RedisProperties.class)` no `RedisConfig` | ✅ |
| Properties `coffee.redis.*` em todos os profiles (prod/h2/test) | ✅ |
| Instâncias `cache` e `rate-limit` separadas | ✅ |
| Senha Redis (`password` + `withPassword`) | ✅ |
| TLS/SSL (`useSsl` + `withSsl(true)`) | ✅ |
| `@PreDestroy` para fechar conexões no shutdown | ✅ |
| `Bucket4jConfig` conectado ao provider (`RateLimit` bean) | ✅ |
| Smoke test real contra Redis 7.4.10 remoto (porta 6380) | ✅ |

### 📝 Pendências

| Prioridade | Item | Nota |
|---|---|---|
| 🟡 | **Drift de config no Redis remoto (6380)** | Instância rate-limit roda SEM `requirepass` (AUTH → ERR), mas o `.env` espera `REDIS_RATELIMIT_PASSWORD`; 6379 (cache) inacessível de fora. Alinhar config do servidor com o `.env` (verificado 2026-08-04). |
| 🟢 | **Typos de pacote** — `Adpter` → `Adapter`, `Provaider` → `Provider`, `Arry` → `Array` | Classes de conexão já corrigidas (`RedisClientConnectionAdapter` em `Adapters/in/`); nomes de **pacote** seguem com typo (`DatabaseProvaider`, `RedisArryCodec`) — só em nova versão (breaking de import). |

---

> **Documentação mantida por:** Quitto
> **Última atualização:** 2026-08-05
> **Propósito:** Documentação viva — atualize conforme a arquitetura evoluir.

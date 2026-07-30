# Redis Abstraction — Clean Architecture (Ports & Adapters)

> **Status:** ✅ Implementado (2026-07-30)
> **Stack:** Lettuce 6.8.2 + Spring Boot 4.0.6
> **Propósito:** Abstrair acesso ao Redis de forma que o domínio não dependa de bibliotecas externas (Lettuce, Netty).

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
│  │  + close(): void             │   │  + get(name: String): T │ │
│  └──────────┬───────────────────┘   └───────────┬─────────────┘ │
│             │                                    │              │
│  ┌──────────▼────────────────────────────────────▼───────────┐  │
│  │  DatabaseClient  (model)                                  │  │
│  │  name, host, port, enabled                               │  │
│  └──────────────────────────────────────────────────────────┘  │
│             ▲                                                  │
│             │ extends                                           │
│  ┌──────────┴───────────┐                                      │
│  │  RedisClientInstace  │                                      │
│  │  (marcador de tipo)  │                                      │
│  └──────────────────────┘                                      │
├─────────────────────────────────────────────────────────────────┤
│  INFRASTRUCTURE LAYER  (Lettuce + Netty + Spring)               │
│                                                                  │
│  ┌────────────────────────────┐  ┌──────────────────────────┐  │
│  │ RedisClientConnectionAdpter  │  │ RedisClientProvider      │  │
│  │ implements Connection        │  │ implements               │  │
│  │ ─────────────────────────── │  │   DatabaseClientProvider │  │
│  │ Wraps: StatefulRedisConn    │  │ ─────────────────────────│  │
│  │        + RedisAsyncCommands │  │ Lê properties → cria     │  │
│  └──────────┬──────────────────┘  │ conexões → Map<String,   │  │
│             │                     │   RedisClientConnAdpter> │  │
│             │                     └────────────┬─────────────┘  │
│             │                                  │               │
│  ┌──────────┴──────────────────┐  ┌─────────────▼───────────┐  │
│  │ StringByteArrayCodec        │  │  RedisProperties         │  │
│  │ @Component                  │  │  @ConfigurationProperties│  │
│  │ implements RedisArryCodec   │  │  prefix="coffee.redis"   │  │
│  │  │                             │  List<RedisClientInstace>   │  │
│  └─────────────────────────────┘  └─────────────────────────┘  │
│                                                                  │
│  Consumers (future):  Bucket4jConfig,  RateLimit,  HealthChecks │
└─────────────────────────────────────────────────────────────────┘
```

### Fluxo de Dependência

```
DOMAIN  ◄──────  INFRASTRUCTURE  ◄──────  Lettuce / Netty
──────           ──────────────           ─────────────────
Connection       RedisClientConnAdpter    io.lettuce.core.*
DatabaseClient   implements Connection    io.netty.buffer.*
Provider         
                 RedisClientProvider
                 implements DatabaseClientProvider
```

**Regra respeitada:** domínio não importa Spring, Jakarta, Lettuce ou Netty. ✅

---

## 🧩 Componentes

### Domínio (ports)

| Componente | Arquivo | Responsabilidade |
|---|---|---|
| `Connection<T>` | `domain/Database/Connection.java` | **Porta.** Contrato de conexão: `isOpen()` e `close()` |
| `DatabaseClient` | `domain/Database/DatabaseClient.java` | **Modelo.** Dados de config de um cliente (name, host, port) |
| `RedisClientInstace` | `domain/Database/redis/RedisClientInstace.java` | **Modelo.** Extends `DatabaseClient`, marcador de tipo |
| `DatabaseClientProvider<T>` | `domain/interfaces/Database/DatabaseClientProvider.java` | **Porta.** `get(name)` → conexão pelo nome |

### Infraestrutura (adapters)

| Componente | Arquivo | Responsabilidade |
|---|---|---|
| `RedisClientConnectionAdpter` | `infrastructure/Adpter/in/RedisClientConnectionAdpter.java` | **Adapter.** Wrapper Lettuce → `Connection` do domínio |
| `RedisClientProvider` | `infrastructure/services/Provaider/redis/RedisClientProvider.java` | **Provider.** Lê config → cria connections → `get(name)` |
| `RedisProperties` | `infrastructure/config/redis/RedisProperties.java` | **Config.** `@ConfigurationProperties(prefix="coffee.redis")`, usa `RedisClientInstace` do domínio |
| `StringByteArrayCodec` | `infrastructure/config/redis/Codec/StringByteArrayCodec.java` | **Codec.** Key=String(UTF-8), Value=byte[] |
| `RedisArryCodec` | `infrastructure/interfaces/Codec/RedisArryCodec.java` | **Interface.** Extends `RedisCodec<String, byte[]>` |

---

## 🔄 Fluxo de Uso

Um consumidor obtém uma conexão Redis assim:

```java
@Autowired
private DatabaseClientProvider<RedisClientConnectionAdpter> redisProvider;

public void exemplo() {
    RedisClientConnectionAdpter conn = redisProvider.get("cache");
    
    if (conn.isOpen()) {
        RedisAsyncCommands<String, byte[]> cmd = conn.getCommands();
        cmd.set("chave", "valor".getBytes());
    }
}
```

Internamente, o `RedisClientProvider`:

1. Recebe `RedisProperties` no construtor
2. Itera sobre `instances` (cada uma com name, host, port — `RedisClientInstace` do domínio)
3. Cria `RedisClient.create("redis://host:port")`
4. Cria `connection = client.connect(new StringByteArrayCodec())`
5. Encapsula em `new RedisClientConnectionAdpter(connection)`
6. Armazena no `Map<String, RedisClientConnectionAdpter>`
7. `get(name)` busca no map ou lança `IllegalArgumentException`

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

### TLS/SSL

Atualmente as conexões usam `redis://` (sem TLS). Para produção com dados sensíveis:

```java
// Substituir
RedisClient.create("redis://" + host + ":" + port);

// Por
RedisClient.create(RedisURI.Builder.redis(host, port)
    .withSsl(true)
    .withPassword(password)
    .build());
```

### Autenticação

`RedisClientInstace` ainda não tem campo `password`. Necessário adicionar no domain model e prover ao criar a `RedisURI`.

### Resource Management

`@PreDestroy` no `RedisClientProvider` é necessário para fechar conexões no shutdown.

---

## 📋 Pendências

| Prioridade | Item | Responsável |
|---|---|---|
| 🔴 | Registrar `@EnableConfigurationProperties(RedisProperties.class)` | Spring config |
| 🔴 | Adicionar properties `coffee.redis.*` no `application.properties` | Config |
| 🟡 | Adicionar TLS/SSL (`rediss://`) | Security |
| 🟡 | Adicionar campo `password` em `RedisClientInstace` | Security |
| 🟡 | `@PreDestroy` para fechar conexões no shutdown | Resource leak |
| 🟡 | Conectar `Bucket4jConfig` com `DatabaseClientProvider` | Integração |
| 🟢 | Renomear typos: `Adpter` → `Adapter`, `Provaider` → `Provider`, `Arry` → `Array` | Cleanup |

<p align="center">
  <code>coffe_server</code>
</p>

<p align="center">
  Backend central de homelab — autenticação JWT, OAuth2 Google, gerenciamento de máquinas, integração Linux, ecossistema de provedores de IA e servidor MCP para agentes de IA.
  <br/>
  <br/>
  <img src="https://img.shields.io/badge/Java-21-%23ED8B00?logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Spring_Boot-4.0.6-%236DB33F?logo=spring&logoColor=white" alt="Spring Boot 4.0.6"/>
  <img src="https://img.shields.io/badge/PostgreSQL-17-%234169E1?logo=postgresql&logoColor=white" alt="PostgreSQL 17"/>
  <img src="https://img.shields.io/badge/Redis-7-%23DC382D?logo=redis&logoColor=white" alt="Redis 7"/>
  <img src="https://img.shields.io/badge/MCP-Spring_AI_1.0.2-%23F36F2E?logo=openai&logoColor=white" alt="Spring AI MCP 1.0.2"/>
  <img src="https://img.shields.io/badge/license-MIT-blue" alt="MIT License"/>
</p>

---

## Sobre

coffe_server é o backend central de um ecossistema pessoal de produtividade. Não é uma aplicação genérica — foi construída por um desenvolvedor para resolver problemas reais do próprio ambiente: centralizar serviços, automatizar tarefas, integrar máquinas do homelab, expor ferramentas para agentes de IA e manter controle total sobre a infraestrutura.

O projeto nasceu da necessidade de um hub que unificasse:

- Autenticação centralizada (JWT + OAuth2) para servir múltiplos frontends e agentes
- Gerenciamento de máquinas da rede local (Tailscale, Wake-on-LAN)
- Integração com serviços Google (Calendar, futuramente Tasks)
- **Ecossistema de provedores de IA** — registro de 40+ provedores com seus modelos (OpenAI, Anthropic, Ollama e mais) para alimentar agentes
- **Cache distribuído com Redis** — sessões de usuário e rate limiting em instâncias dedicadas
- Exposição de ferramentas via MCP para agentes de IA (Claude, GPT, etc.)
- Gerenciamento de usuários Linux no servidor

Além de resolver problemas práticos, o projeto tem uma forte finalidade educacional — é um estudo contínuo de arquitetura de software, boas práticas de backend, segurança e integração entre sistemas.

---

## Objetivos

- **Centralizar** — unificar autenticação, máquinas e serviços em um único backend
- **Automatizar** — reduzir tarefas manuais via APIs e agentes de IA
- **Integrar** — conectar Google, Linux, máquinas da rede e agentes em um ecossistema coerente
- **Aprender** — aplicar Clean Architecture, segurança, integrações e engenharia de software na prática
- **Controlar** — manter autonomia total sobre o ambiente, sem dependência de serviços terceiros fechados

---

## Arquitetura

O projeto segue **Arquitetura Hexagonal** (Ports & Adapters), também conhecida como **Clean Architecture**. O princípio central é que o domínio não depende de nada externo — frameworks, bancos de dados e detalhes de infraestrutura são plugáveis através de interfaces.

```
External Client
        │
        ▼
 MCP / REST / CLI / Other Adapters
        │
        ▼
   MVC Controllers (Entry Layer)
        │
        ▼
     Application Layer
       (Use Cases)
        │
        ▼
      Domain Layer
(Entities, Value Objects,
 Domain Services, Interfaces)
        │
        ▼
 Infrastructure Layer
(Repositories, File System,
 Docker, Git, etc.)
```

### Camadas

**Domínio** — modelos, value objects, enums e interfaces (portas). Nenhuma dependência de framework. O código aqui é Java puro, sem Spring, Jakarta ou qualquer biblioteca externa.

**Aplicação** — orquestração de use cases. Coordena as interfaces do domínio sem conter lógica de negócio. Controllers, DTOs e application services vivem aqui.

**Infraestrutura** — implementações concretas das portas definidas pelo domínio. Adaptadores JPA, serviços de segurança (JWT, BCrypt), clientes HTTP (Google Calendar, OAuth2) e tudo que depende de frameworks.

**MCP** — camada opcional que expõe ferramentas via Model Context Protocol para agentes de IA. Não contém lógica de negócio — apenas traduz chamadas MCP em use cases da aplicação.

### Fluxo de Autenticação

```
                   Http Request
                        │
                        ▼
           JwtAuthenticationFilter
                        │
               "Me dá um token"
                        │
                        ▼
             TokenResolverManager
                        │
         Itera sobre resolvers[]
         ┌───────────────┼───────────────┐
         ▼               ▼               ▼
   CookieToken   JwtTokenResolver   ApiKeyResolver
    Resolver        (Bearer)        (futuro)
         │               │               │
         └───────┬───────┘               │
                 ▼                       ▼
          Optional<String>         Optional<String>
                 │                       │
                 └───────────┬───────────┘
                             ▼
                     Primeiro token encontrado
                             │
                             ▼
                     JwtAuthenticationFilter
                         valida o token
                             │
                             ▼
                       SecurityContext
```

O sistema de resolução de token segue **Chain of Responsibility**. Cada `TokenResolver` sabe ler um token de uma fonte diferente (cookie, header Authorization, futuramente API Key). O `TokenResolverManager` itera sobre os resolvers até encontrar um token válido. O filtro de autenticação não precisa saber de onde o token veio — ele só pergunta "tem um token aí?".

---

## Tecnologias

| Componente | Tecnologia | Motivo |
|---|---|---|
| **Linguagem** | Java 21 | Records, pattern matching, segurança de tipos e ecossistema maduro |
| **Framework** | Spring Boot 4.0.6 | Base do backend — injeção de dependências, segurança, persistência e suporte a MCP |
| **Segurança** | Spring Security + Auth0 java-jwt 4.5.2 | Autenticação e autorização robustas com JWT HMAC256 |
| **Banco (prod)** | PostgreSQL 17 | Confiabilidade, recursos avançados e integração com o ambiente Linux |
| **Banco (dev/test)** | H2 in-memory | Desenvolvimento ágil sem dependência de banco externo |
| **Cache/RateLimit** | Redis 7 (Lettuce) | Instâncias dedicadas para cache de usuário e controle de taxa distribuído |
| **ORM** | Spring Data JPA + Hibernate | Mapeamento objeto-relacional com repository pattern |
| **OAuth2** | Spring Security OAuth2 Client + Google Auth Library 1.23.0 | Login social Google com auto-provisionamento de usuários |
| **IA** | Ecossistema próprio (BaseProvider + 38 providers) | Registro de provedores + catálogo de modelos: OpenAI, Anthropic, Ollama, Google AI Studio e mais |
| **IA/MCP** | Spring AI MCP Server WebMVC 1.0.2 | Exposição de ferramentas para agentes de IA via Model Context Protocol |
| **Templates** | Thymeleaf | Páginas web simples para landing page e login |
| **Build** | Maven | Gerenciamento de dependências e build reproduzível |
| **Monitoria** | Spring Actuator | Health checks e métricas da aplicação |

---

## Organização do Projeto

```
com.quitto.server/
│
├── ServerApplication.java              # Entry point
│
├── application/                         # ORQUESTRAÇÃO (use cases)
│   ├── controllers/                     #   REST controllers
│   ├── dto/                             #   DTOs de request/response
│   └── services/                        #   UserAuthenticationService, MachineService
│
├── domain/                              # DOMÍNIO PURO (zero frameworks)
│   ├── enums/                           #   Role, Provider, ServiceProvider (40+ provedores IA)
│   ├── exception/                       #   AuthenticationException, ProviderException, InvalidTokenException
│   ├── interfaces/                      #   PORTAS: TokenResolver, TokenService, AIProvider, AIRegistry
│   ├── models/                          #   User, Machine, LinuxUser, ExternalAccount, AIModel (IA)
│   ├── Database/                        #   Connection, DatabaseClient, RedisClientInstace
│   ├── Repository/                      #   UserRepository, MachineRepository
│   └── valueobject/                     #   CookieDomain
│
├── infrastructure/                      # IMPLEMENTAÇÕES CONCRETAS
│   ├── IA/                              #   BaseProvider + OpenAI/Anthropic/Ollama (38 providers)
│   ├── db/                              #   Adaptadores JPA (Entity, Mapper, Adapter, Repository)
│   ├── external/                        #   GoogleAuthService, GoogleCalendarClient
│   ├── security/                        #   JwtAuthenticationFilter, SecurityConfig, SecurityUser
│   ├── config/redis/                    #   RedisProperties, RedisConfig, StringByteArrayCodec
│   ├── Adapters/in/                     #   RedisClientConnectionAdapter (Lettuce → Connection)
│   └── services/                        #   SpringAuthenticationService, JwtTokenService, BCrypt,
│                                       #   AIProviderRegistry, RedisClientProvider, CoffeAgentService
│
├── mcp/                                 # CAMADA MCP (Spring AI)
│   ├── services/                        #   GoogleCalendarService
│   └── tools/                           #   GoogleCalendarTools, CalendarController
│
├── shared/                              # CROSS-CUTTING
│   └── exception/                       #   AuthExceptionHandler, NotEnableExceptions
│
└── resources/
    ├── application.properties           # Config principal (PostgreSQL + Redis + OAuth2 + JWT)
    ├── application-h2.properties        # Dev profile (H2)
    ├── data-h2.sql                      # Seed data
    ├── logback-spring.xml
    ├── static/css/
    └── templates/                       # index.html, login.html
```

### Responsabilidade dos diretórios

| Diretório | Responsabilidade |
|---|---|
| `domain/` | Coração do sistema. Modelos, regras de negócio, interfaces (portas). Sem dependência de frameworks. |
| `application/` | Orquestração. Controllers recebem requests, services orquestram use cases, DTOs transportam dados. |
| `infrastructure/` | Implementação. Tudo que depende de bibliotecas externas — JPA, Spring Security, BCrypt, HTTP clients, Lettuce (Redis), providers de IA. |
| `mcp/` | Camada de agentes de IA. Tools e serviços expostos via Model Context Protocol. |
| `shared/` | Código transversal. Tratamento global de exceções, utilitários compartilhados. |

---

## Instalação

### Requisitos

- Java 21+
- Maven (ou use o wrapper `mvnw`)
- PostgreSQL 17 (produção) ou H2 (desenvolvimento)
- Conta Google Cloud Platform com OAuth2 configurado (para login social)

### Dependências

O projeto gerencia dependências via Maven. Para instalar:

```bash
git clone https://github.com/seu-usuario/coffe_server.git
cd coffe_server
./mvnw clean install
```

### Configuração de ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
DB_URL=jdbc:postgresql://localhost:5432/coffe_server
DB_USERNAME=postgres
DB_PASSWORD=sua_senha
GOOGLE_CLIENT_ID=seu_client_id
GOOGLE_SECRET_API=seu_client_secret
SERVER_API_KEY=sua_chave_jwt_super_secreta
```

### Executar

```bash
# Com PostgreSQL (produção)
./mvnw spring-boot:run

# Com H2 in-memory (desenvolvimento)
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2

# Build completo
./mvnw clean install
```

---

## Configuração

### Profiles

| Profile | Banco | JWT Key | Uso |
|---|---|---|---|
| `default` | PostgreSQL | `SERVER_API_KEY` (env) | Produção |
| `h2` | H2 in-memory | `test-h2-jwt-secret-key-for-local-testing` | Desenvolvimento local |
| `test` | H2 | — | Testes automatizados |

### Variáveis de ambiente

| Variável | Obrigatória | Descrição |
|---|---|---|
| `DB_URL` | Sim | URL de conexão com PostgreSQL |
| `DB_USERNAME` | Sim | Usuário do banco |
| `DB_PASSWORD` | Sim | Senha do banco |
| `GOOGLE_CLIENT_ID` | Sim | Client ID do Google OAuth2 |
| `GOOGLE_SECRET_API` | Sim | Client secret do Google OAuth2 |
| `SERVER_API_KEY` | Sim | Chave secreta para assinatura JWT |
| `REDIS_CACHE_PASSWORD` | Não | Senha da instância Redis `cache` |
| `REDIS_CACHE_SSL` | Não | Habilita TLS na instância Redis `cache` |
| `REDIS_RATELIMIT_PASSWORD` | Não | Senha da instância Redis `rate-limit` |
| `REDIS_RATELIMIT_SSL` | Não | Habilita TLS na instância Redis `rate-limit` |

### Banco de dados

Scripts SQL disponíveis em `sql/`:

| Ordem | Arquivo | Tabela |
|---|---|---|
| 1 | `00_init.sql` | Schema completo |
| 2 | `01_groups.sql` | `groups` |
| 3 | `02_user.sql` | `user` |
| 4 | `03_linux_user.sql` | `linux_user` |
| 5 | `04_machine.sql` | `machine` |
| 6 | `05_external_account.sql` | `external_account` |
| 7 | `06_roles.sql` | Roles PostgreSQL |

O acesso ao Redis (cache de usuário + rate limiting) é abstraído via Ports & Adapters — o domínio define `Connection`/`DatabaseClientProvider`, e a infraestrutura implementa com Lettuce (`RedisClientProvider`, `RedisClientConnectionAdapter`). Detalhes em `docs/architecture/redis-abstraction.md`.

---

## Uso

### Endpoints

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| `GET` | `/` | Público | Landing page |
| `GET` | `/login` | Público | Página de login |
| `POST` | `/auth/login` | Público | Login → JWT + cookie |
| `POST` | `/auth/register` | Público | Registro → JWT |
| `GET` | `/api/test` | Público | Health check |
| `GET` | `/api/calendar/test` | Público | Teste Calendar |
| `GET` | `/api/calendar/events` | Público | Lista eventos Google Calendar |
| `GET` | `/mcp/**` | `ROLE_MCP` | MCP Tools (agentes IA) |
| `GET` | `/oauth2/authorization/google` | Público | Inicia OAuth2 Google |

### Exemplos

**Login:**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"name": "admin_teste", "password": "admin"}'
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "date": "2026-07-23T12:00:00"
}
```

**Requisição autenticada (cookie):**

```bash
curl -X GET http://localhost:8080/api/test \
  -b "access_token=eyJhbGciOiJIUzI1NiIs..."
```

**Requisição autenticada (Bearer):**

```bash
curl -X GET http://localhost:8080/api/test \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
```

---

## Desenvolvimento

### Ambiente local

```bash
# Terminal 1 — servidor com H2
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2

# Terminal 2 — testes
./mvnw test
```

### Testes

| Suite | Tipo | Cobertura |
|---|---|---|
| `CookieSystemTest` | Unitário | CookieDomain, resolvers, manager (24 testes) |
| `CookieSystemIntegrationTest` | Integração | Login real, JWT, chain de resolvers (16 testes) |
| `ServerApplicationTests` | Smoke | Contexto da aplicação carrega |

```bash
# Rodar todos os testes
./mvnw test

# Testes com profile específico
./mvnw test -Dspring.profiles.active=test
```

### Convenções de código

1. **Interfaces no domínio** — toda porta/contrato pertence a `domain/interfaces/`
2. **Implementação na infra** — toda implementação concreta pertence a `infrastructure/`
3. **Prefira `Optional<T>`** a `null` para valores ausentes
4. **Constructor injection** sempre — mais testável e imutável
5. **Logs estruturados (SLF4J)** — nunca `System.out`
6. **Exceções do domínio** — não expor exceções de framework nas camadas superiores

---

## Filosofia

Este projeto não é apenas software — é um ambiente construído por um desenvolvedor para seu próprio uso, com necessidades reais e decisões arquiteturais deliberadas.

Cada componente foi pensado a partir de um problema concreto: autenticação que funcione tanto para browser quanto para API clients, gerenciamento de máquinas que integre Tailscale e Wake-on-LAN, exposição de ferramentas via MCP para agentes de IA que precisam interagir com o ambiente de forma consistente.

A escolha por Clean Architecture não é dogmática — é prática. O domínio puro permite que a lógica de negócio seja testada sem Spring, reutilizada em outros projetos (CLIs, frontends, agentes) e mantida sem acoplamento a frameworks que mudam a cada versão.

O projeto está em evolução constante. Algumas áreas estão maduras (autenticação JWT, sistema de tokens, adaptadores de repositório), outras ainda em construção (gerenciamento Linux, Google Calendar, MCP tools). Cada nova funcionalidade é uma oportunidade de aplicar conceitos de engenharia de software e melhorar a arquitetura.

> Ferramentas construídas por quem as usa tendem a ser mais precisas, mais simples e mais honestas.

---

## Licença

MIT — use, estude, modifique. Este projeto existe para ser útil e para ensinar.
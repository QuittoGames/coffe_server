---
description: Servidor pessoal multifuncional com Clean Architecture — autenticação JWT, OAuth2 Google, gerenciamento de máquinas, integração Linux, servidor MCP para agentes de IA, controle de backup e sistema multi-usuário com permissões.
---

# coffe_server

## Coffee Server Architecture

The **Coffee Server** is the outermost application of the Coffee ecosystem. It is designed as an **independent monolithic service** that exposes the capabilities of the Coffee SDK through the **Model Context Protocol (MCP)** while internally following **Clean Architecture**.

Although the Coffee Server uses a traditional **MVC** structure at its entry point, the MVC layer is **not responsible for business logic**. It acts only as an adapter between external protocols and the application's use cases.

The architecture is organized as follows:

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
 DNF, Docker, Git, etc.)
```

The **MCP layer** is treated as an **input adapter**, similar to a REST API, CLI, or gRPC interface. Its only responsibility is to translate incoming requests into application use cases and transform the results back into MCP responses.

This design ensures that the business rules remain completely independent of the communication protocol. The Domain and Application layers have no knowledge of MCP, HTTP, or any framework-specific technology.

The Infrastructure layer implements the interfaces defined by the inner layers and communicates with operating system resources, package managers, Docker, Git repositories, the file system, and any other external dependency required by the Coffee ecosystem.

From a deployment perspective, the Coffee Server behaves like a **standalone monolithic service**. All modules execute within the same process and share the same codebase, avoiding the operational complexity of a microservice architecture. However, because it exposes its functionality through MCP, it behaves externally like a dedicated platform service that can be consumed by multiple AI agents, desktop applications, CLIs, or future integrations.

This approach combines the simplicity and performance of a monolith with the modularity provided by Clean Architecture. New protocols (REST, CLI, WebSocket, gRPC, or additional MCP transports) can be added as new adapters without requiring changes to the Domain or Application layers.

The result is a highly maintainable architecture where:

* Business rules remain framework-independent.
* Communication protocols are isolated in the outer layer.
* Infrastructure details are fully encapsulated.
* Multiple clients can reuse the same application logic.
* The Coffee Server acts as the central orchestration point for the entire Coffee ecosystem while remaining loosely coupled to the technologies used to access it.

---

## 1. Descrição do Projeto

**coffe_server** é o servidor central do ecossistema pessoal do Quitto — um hub multifuncional pensado para estudos, automação e gerenciamento de homelab. A visão de longo prazo é um sistema tipo "Jarvis" (Iron Man): um backend que integra autenticação centralizada, gerenciamento de máquinas (Wake-on-LAN, Tailscale), usuários Linux, serviços Google (Calendar, Tasks), servidor MCP para agentes de IA, **controle de backup** e **sistema de permissões multi-usuário**.

Ele funciona como um **servidor geral** que pode ser acessado por outras pessoas que o administrador permitir, cada uma com seu nível de acesso. A ideia é que seja o cérebro do homelab — máquinas, arquivos, calendário, tarefas, automação — tudo exposto via REST e MCP Tools para agentes de IA, formando a base de um ecossistema de agentes consistente.

O projeto segue **Clean Architecture / Arquitetura Hexagonal** com camadas bem definidas: domínio puro (sem frameworks), application (orquestração), infraestrutura (implementações concretas) e MCP (camada de agentes de IA).

Usuários finais: Quitto (dev/admin), agentes de IA via MCP, usuários convidados com permissões específicas, e futuros usuários do PS3 (Project Setup 3 Web).

---

## 2. Stack Técnica

| Componente | Tecnologia |
|---|---|
| **Linguagem** | Java 21 |
| **Framework** | Spring Boot 4.0.6 |
| **Build** | Maven (com Wrapper) |
| **Segurança** | Spring Security + Auth0 java-jwt 4.5.2 |
| **Banco (prod)** | PostgreSQL (via Spring Data JPA + Hibernate) |
| **Banco (dev/test)** | H2 in-memory |
| **OAuth2** | Spring Security OAuth2 Client + Google Auth Library 1.23.0 |
| **IA/MCP** | Spring AI MCP Server WebMVC 1.0.2 |
| **Templates** | Thymeleaf |
| **Monitoria** | Spring Actuator |
| **Geração de código** | Lombok (opcional) |

---

## 3. Estrutura do Projeto

```
src/main/java/com/quitto/server/
│
├── ServerApplication.java                  # Entry point
│
├── application/                            # ORQUESTRAÇÃO (use cases)
│   ├── controllers/                        #   REST controllers
│   │   ├── AuthenticationController.java   #     POST /auth/login, /auth/register
│   │   ├── APIController.java              #     GET /api/test
│   │   └── HomeController.java             #     GET /, /login
│   ├── dto/Auth/                           #   LoginDTO, LoginResponseDTO, etc.
│   └── services/                           #   UserAuthenticationService, MachineService, UserService
│
├── domain/                                 # DOMÍNIO PURO (zero frameworks)
│   ├── enums/                              #   Role, Provaider
│   ├── exception/                          #   AuthenticationException, InvalidTokenException
│   ├── interfaces/                         #   PORTAS: TokenResolver, TokenService, AuthenticationService...
│   ├── models/                             #   User, Machine, LinuxUser, Groups, ExternalAccont
│   ├── Repository/                         #   UserRepository, MachineRepository
│   └── valueobject/                        #   CookieDomain
│
├── infrastructure/                         # IMPLEMENTAÇÕES CONCRETAS
│   ├── config/logger/                      #   CoffeColorConverter
│   ├── db/                                 #   Adaptadores JPA (Entity, Mapper, Adapter, Repository)
│   │   ├── User/                           #     UserEntity, UserMapper, UserRepositoryAdapter
│   │   ├── Machine/                        #     MachineEntity, MachineMapper, MachineRepositoryAdapter
│   │   └── LinuxUser/                      #     GroupsEntity, LinuxUserEntity (sem adapter ainda)
│   ├── external/                           #   GoogleAuthService, GoogleCalendarClient
│   ├── interfaces/Cookies/                 #   ⚠ CookieService (devia estar no domínio)
│   ├── security/                           #   JwtAuthenticationFilter, SecurityConfig, SecurityUser
│   └── services/                           #   SpringAuthenticationService, JwtTokenService, BCrypt...
│
├── mcp/                                    # CAMADA MCP (Spring AI)
│   ├── services/                           #   GoogleCalenderService
│   └── tools/                              #   GoogelCalenderTools, CalendarController
│
├── shared/exception/                       # AuthExceptionHandler, MachineNotFoundException
└── resources/
    ├── application.properties              # Config principal (PostgreSQL + OAuth2 + JWT)
    ├── application-h2.properties           # Dev profile (H2)
    ├── data-h2.sql                         # Seed data (admin_teste)
    ├── logback-spring.xml
    ├── static/css/
    └── templates/                          # index.html, login.html
```

---

## 4. Arquitetura

### Camadas (Clean Architecture)

```
┌──────────────────────────────────────────────┐
│           APPLICATION LAYER                   │
│  Controllers → DTOs → Services (use cases)   │
├──────────────────────────────────────────────┤
│            DOMAIN LAYER (PURO)                │
│  Models / VOs / Enums / Interfaces (Portas)  │
│  NENHUMA dependência de framework            │
├──────────────────────────────────────────────┤
│         INFRASTRUCTURE LAYER                  │
│  JPA Adapters / Security / BCrypt / OAuth2   │
│  Implementa as interfaces do domínio         │
├──────────────────────────────────────────────┤
│             MCP LAYER                         │
│  Tools @Tool / Services / REST endpoints     │
└──────────────────────────────────────────────┘
```

### Regras rígidas:

1. **Domínio NÃO importa nada de infraestrutura** — zero Spring, zero Jakarta, zero HTTP
2. **Aplicação orquestra** o domínio, mas não contém lógica de negócio
3. **Infraestrutura implementa** interfaces do domínio (Adapter pattern)
4. **Dependências apontam para dentro** — sempre para o domínio

---

## 5. Fluxos Principais

### 5.1 Autenticação (Login)

```
Request → JwtAuthenticationFilter → TokenResolverManager (Chain)
  ├── CookieTokenResolver → lê cookie "access_token"
  └── JtwTokenResvoler → lê header "Authorization: Bearer"
→ JwtTokenService.verifyToken() → UserRepository → SecurityUser → SecurityContextHolder
```

**POST /auth/login:** AuthenticationController → UserAuthenticationService → SpringAuthenticationService (AuthenticationManager) → JwtTokenService → {token, date} + Set-Cookie

**POST /auth/register:** Similar, mas cria novo User com Role.USER antes de gerar o token.

### 5.2 Token Resolution (Chain of Responsibility)

```
TokenResolverManager iterates over resolvers[]
  ├── CookieTokenResolver  → cookie "access_token"
  ├── JtwTokenResvoler     → header "Authorization: Bearer"
  └── (futuro) ApiKeyResolver
→ Retorna o primeiro token encontrado como Optional<String>
```

### 5.3 OAuth2 Google

```
/oauth2/authorization/google → Google OAuth2 Provider → Redirect com código
→ OAuth2UserProvisioningService.loadUser()
  → Busca email no banco
  → Se existe: autentica
  → Se não: cria UserEntity com Role.USER
```

### 5.4 MCP (Agentes de IA)

```
Agente de IA (Claude, GPT) → HTTP /mcp/** → GoogelCalenderTools ( @Tool )
  → GoogleAuthService → GoogleCalendarClient → GoogleCalenderService
```

---

## 6. Banco de Dados

### PostgreSQL (produção)

Tabelas: `groups`, `user`, `linux_user`, `machine`, `external_account`

Relacionamentos:
- `user` 1:1 → `linux_user` (conta Linux)
- `user` 1:N → `machine` (máquinas gerenciadas)
- `user` 1:N → `external_account` (contas OAuth2)
- `linux_user` M:1 → `groups` (grupo Unix)

### Roles

`ADMIN`, `USER`, `MCP`, `API`

### Scripts SQL

Ordem de criação em `sql/`:
1. `00_init.sql` — groups
2. `01_groups.sql` — groups
3. `02_user.sql` — user
4. `03_linux_user.sql` — linux_user
5. `04_machine.sql` — machine
6. `05_external_account.sql` — external_account
7. `06_roles.sql` — roles

---

## 7. Endpoints

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | `/` | Público | Landing page (Thymeleaf) |
| GET | `/login` | Público | Página de login |
| POST | `/auth/login` | Público | Login → JWT + cookie |
| POST | `/auth/register` | Público | Registro → JWT |
| GET | `/api/test` | Público | Health check |
| GET | `/api/calendar/test` | Público | Teste Calendar |
| GET | `/mcp/**` | ROLE_MCP | MCP Tools (agentes IA) |
| GET | `/oauth2/authorization/google` | Público | OAuth2 Google |

---

## 8. Comandos de Build e Teste

```bash
# Build completo
./mvnw clean install

# Rodar com PostgreSQL (produção)
./mvnw spring-boot:run

# Rodar com H2 (desenvolvimento)
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2

# Rodar testes
./mvnw test

# Testes com profile específico
./mvnw test -Dspring.profiles.active=test
```

### Testes existentes

| Arquivo | Tipo | Qtd |
|---|---|---|
| `ServerApplicationTests` | Smoke test (context loads) | 1 |
| `CookieSystemTest` | Unitários (CookieDomain, resolvers, manager, HTTP adapter) | 24 |
| `CookieSystemIntegrationTest` | Integração (login real, JWT, chain de resolvers) | 16 |

---

## 9. Problemas Conhecidos

### Typos em classes públicas (NÃO renomear sem migração)

| Classe atual | Nome correto |
|---|---|
| `JtwTokenResvoler` | `JwtTokenResolver` |
| `BCryptPassowordService` | `BCryptPasswordService` |
| `Provaider` | `Provider` |
| `ExternalAccont` | `ExternalAccount` |
| `GoogelCalenderTools` | `GoogleCalendarTools` |
| `GoogleCalenderService` | `GoogleCalendarService` |

### Violações arquiteturais (para refatorar)

- `CookieService` está na infra → deveria estar no **domínio** (é uma porta)
- `TokenResolverManager` está na infra → deveria estar na **application**
- `JwtTokenService.extractIdSubject()` retorna `null` → deveria retornar `Optional<Long>`
- `OAuth2UserProvisioningService` usa JPA direto em vez da porta `UserRepository`
- **Field injection** em alguns serviços (preferir constructor injection)

### Funcionalidades incompletas

- **LinuxUser/Groups**: domain + entity existem, mas **faltam** mappers, adapters e repositórios
- **Google Calendar**: `createEvent()` retorna `""` (stub)
- **MachineRepositoryAdapter.setOwner()**: retorna `new User()` vazio
- **UserService**: é só esqueleto (construtor vazio)
- **Wake-on-LAN**: campos no model existem, sem implementação real
- **Tailscale**: campos existem, sem integração real

---

## 10. Convenções do Projeto

### Arquitetura e Organização

- **Clean Architecture**: domínio puro, application orquestra, infra implementa
- **Naming**: classes em **inglês**, repositórios com sufixo `Repository`, services com sufixo `Service`
- **Token resolution**: Chain of Responsibility (`TokenResolverManager`)
- **JWT**: HMAC256, issuer `"coffe-api"`, expiry 1 hora
- **Profiles**: `default` (PostgreSQL), `h2` (dev), `test` (testes)

### Boas práticas ao codificar

1. **Interfaces no domínio** — toda porta/contrato de negócio pertence ao `domain/interfaces/`
2. **Implementação na infra** — toda implementação concreta pertence à `infrastructure/`
3. **Preferir `Optional<T>`** a `null` para valores ausentes
4. **Constructor injection** sempre — mais testável e imutável
5. **Logs estruturados (SLF4J)** — nunca `System.out`
6. **Sem nomes com typos** — revisar antes de criar classes públicas
7. **Exceções do domínio** — não expor exceções de framework nas camadas superiores

### Checklist para novos componentes

- [ ] A interface está no domínio? (sem imports de framework)
- [ ] A implementação está na infraestrutura?
- [ ] O nome está livre de typos?
- [ ] Usa constructor injection?
- [ ] Retorna `Optional` em vez de `null`?
- [ ] As exceções são do domínio?

---

## 11. Configuração de Ambiente

### Variáveis de ambiente (`.env`)

```
DB_URL=jdbc:postgresql://localhost:5432/coffe_server
DB_USERNAME=postgres
DB_PASSWORD=***
GOOGLE_CLIENT_ID=***
GOOGLE_SECRET_API=***
SERVER_API_KEY=***
```

### Profiles

| Profile | Banco | JWT Key | Uso |
|---|---|---|---|
| `default` | PostgreSQL | `SERVER_API_KEY` (env) | Produção/dev real |
| `h2` | H2 in-memory | `test-h2-jwt-secret-key-for-local-testing` | Desenvolvimento local |
| `test` | H2 (via `application-test.properties`) | — | Testes automatizados |

---

## 12. Recursos Úteis

- **Documentação de arquitetura completa**: `doc/arquiteture.md`
- **Notas do agente Claude**: `.agents/IA_README.md` (OAuth2 Google não deve ser base para análise de arquitetura — será refatorado)
- **Notas de planejamento**: `doc/plain.md` (visão geral, motivações, integrações futuras)
- **README**: `README.md` (vazio — documentação por fazer)
- **Licença**: MIT (`LICENSE`)


**OBS:**

The services of OAuth 2 for Google, nao devem ser usados como base para analises de arquitetura ja que seram refatorados posteriometne , a analise ou uso deles so deve ocorrer quadno o `client` pedir

**Folders**, pastas vazias nao devem pesar na analise, ja que estao ai meio que prontas para quando forem nessesarias

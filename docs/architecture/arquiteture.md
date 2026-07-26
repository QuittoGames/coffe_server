<style>
  /* ══════════════════════════════════════════════
     Coffee Dev — 3 layers distincts
     Layer 1: Dark Modern (base/foundation)
     Layer 2: Blue Tech   (code/technical)
     Layer 3: Coffee Warm (emphasis/comfort)
     ══════════════════════════════════════════════ */

  :root {
    /* ── Layer 1: DARK MODERN ── */
    --bg-deep: #0d0d10;
    --bg-surface: #121216;
    --bg-elevated: #1a1a20;
    --bg-hover: #1e1e26;
    --border-color: rgba(255, 255, 255, 0.06);
    --border-active: rgba(255, 255, 255, 0.12);
    --shadow-soft: 0 2px 8px rgba(0, 0, 0, 0.5);
    --shadow-medium: 0 4px 16px rgba(0, 0, 0, 0.55);
    --text-primary: #cdd2d8;
    --text-secondary: #8a929e;
    --text-muted: #5a626e;
    --text-dim: #3a4048;

    /* ── Layer 2: BLUE TECH ── */
    --blue-light: #7aadcf;
    --blue-mid:   #5090b8;
    --blue-deep:  #2a4a6a;
    --blue-glow:  rgba(80, 144, 184, 0.13);

    /* ── Layer 3: COFFEE WARM ── */
    --coffee-cream:  #d4b892;
    --coffee-latte:  #b89870;
    --coffee-mocha:  #8b6d4a;
    --coffee-espresso:#3a2a1e;

    /* ── Typography ── */
    --font-sans: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif;
    --font-mono: 'JetBrains Mono', 'Fira Code', 'Cascadia Code', 'Victor Mono', monospace;
  }

a
  /* ══════════════════════════════════════════════
     LAYER 1 — DARK MODERN
     Pure dark surfaces, no hue contamination.
     The canvas where everything sits.
     ══════════════════════════════════════════════ */

  body {
    background: var(--bg-deep);
    color: var(--text-primary);
    font-family: var(--font-sans);
    line-height: 1.75;
    max-width: 960px;
    margin: 0 auto;
    padding: 2rem 1.5rem;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }

  h1 {
    font-size: 2rem;
    font-weight: 600;
    color: var(--text-primary);
    letter-spacing: -0.02em;
    border-bottom: 1px solid var(--border-color);
    padding-bottom: 0.6rem;
    margin-bottom: 1.5rem;
  }

  h4 {
    font-size: 1rem;
    font-weight: 500;
    color: var(--text-primary);
    margin-top: 1.5em;
  }

  h5, h6 {
    color: var(--text-secondary);
    font-size: 0.9rem;
  }

  blockquote {
    background: var(--bg-surface);
    border-radius: 0 8px 8px 0;
    padding: 0.9rem 1.25rem;
    margin: 1.25rem 0;
    border: 1px solid var(--border-color);
    border-left-width: 3px;
    box-shadow: var(--shadow-soft);
  }

  blockquote p {
    margin: 0.15rem 0;
    color: var(--text-secondary);
    font-size: 0.92rem;
  }

  pre {
    background: var(--bg-surface);
    border: 1px solid var(--border-color);
    border-radius: 8px;
    padding: 1.15rem;
    overflow-x: auto;
    box-shadow: var(--shadow-soft);
    position: relative;
    font-size: 0.88rem;
  }

  pre::before {
    content: '▎ code';
    display: block;
    font-size: 0.62rem;
    text-transform: uppercase;
    letter-spacing: 0.08em;
    color: var(--text-dim);
    margin-bottom: 0.7rem;
    font-family: var(--font-sans);
  }

  pre code {
    color: var(--text-primary);
    background: none;
    padding: 0;
    font-size: 0.86rem;
  }

  :not(pre) > code {
    background: var(--bg-elevated);
    border: 1px solid var(--border-color);
    border-radius: 5px;
    padding: 0.15em 0.4em;
    font-size: 0.84em;
  }

  table {
    width: 100%;
    border-collapse: separate;
    border-spacing: 0;
    margin: 1.25rem 0;
    background: var(--bg-surface);
    border-radius: 8px;
    border: 1px solid var(--border-color);
    overflow: hidden;
    box-shadow: var(--shadow-soft);
  }

  th, td {
    padding: 0.6rem 0.9rem;
    text-align: left;
    border-bottom: 1px solid var(--border-color);
  }

  th {
    background: var(--bg-elevated);
    font-weight: 500;
    font-size: 0.78rem;
    text-transform: uppercase;
    letter-spacing: 0.05em;
  }

  tr:last-child td {
    border-bottom: none;
  }

  tr:hover td {
    background: var(--bg-hover);
  }

  hr {
    border: none;
    height: 1px;
    opacity: 0.12;
    margin: 2.25rem 0;
  }

  ul, ol {
    padding-left: 1.4rem;
  }

  li {
    margin: 0.25rem 0;
  }

  img {
    border-radius: 8px;
    border: 1px solid var(--border-color);
    max-width: 100%;
    box-shadow: var(--shadow-soft);
  }

  .mermaid {
    background: var(--bg-surface);
    border-radius: 8px;
    padding: 1rem;
    border: 1px solid var(--border-color);
  }

  ::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  ::-webkit-scrollbar-track {
    background: var(--bg-deep);
  }

  ::-webkit-scrollbar-thumb {
    border-radius: 3px;
  }


  /* ══════════════════════════════════════════════
     LAYER 2 — BLUE TECH
     Code, links, technical highlights.
     The "engineering" voice.
     ══════════════════════════════════════════════ */

  h3 {
    font-size: 1.15rem;
    font-weight: 500;
    color: var(--blue-mid);
    margin-top: 2em;
    margin-bottom: 0.5em;
  }

  code {
    font-family: var(--font-mono);
    font-size: 0.87rem;
    color: var(--blue-light);
  }

  p > code, li > code, td > code {
    background: var(--blue-glow);
    border: 1px solid rgba(80, 144, 184, 0.10);
    color: var(--blue-light);
  }

  a {
    color: var(--blue-mid);
    text-decoration: none;
    border-bottom: 1px solid transparent;
    transition: color 0.18s ease, border-color 0.18s ease;
  }

  em {
    color: var(--blue-mid);
    font-style: italic;
  }

  pre code.language-tree,
  pre code.language-ascii {
    color: var(--blue-light);
  }

  ::selection {
    background: var(--blue-deep);
    color: var(--text-primary);
  }

  .hljs-keyword { color: var(--blue-light); }
  .hljs-string  { color: var(--coffee-latte); }
  .hljs-comment { color: var(--text-dim); font-style: italic; }


  /* ══════════════════════════════════════════════
     LAYER 3 — COFFEE WARM (fundo)
     Always present, never shouting.
     Like the ambient warmth of a coffee shop —
     you feel it, you don't stare at it.
     ══════════════════════════════════════════════ */

  h2 {
    font-size: 1.45rem;
    font-weight: 500;
    color: var(--text-primary);
    border-left: 3px solid var(--coffee-mocha);
    padding-left: 0.75rem;
    margin-top: 2.5em;
    margin-bottom: 0.6em;
  }

  blockquote {
    border-left: 3px solid var(--coffee-mocha);
  }

  blockquote::before {
    content: '';
    display: block;
    width: 1.5rem;
    height: 2px;
    background: var(--coffee-mocha);
    opacity: 0.3;
    margin-bottom: 0.5rem;
    border-radius: 1px;
  }

  hr {
    background: linear-gradient(90deg, transparent, var(--coffee-mocha), transparent);
  }

  th {
    border-bottom: 2px solid var(--coffee-mocha);
    color: var(--text-secondary);
    font-weight: 500;
    font-size: 0.78rem;
    text-transform: uppercase;
    letter-spacing: 0.05em;
  }

  pre {
    border-left: 2px solid var(--coffee-mocha);
  }

  ::-webkit-scrollbar-thumb {
    background: var(--coffee-espresso);
  }

  ::-webkit-scrollbar-thumb:hover {
    background: var(--coffee-mocha);
  }

  /* subtle background warmth — like a warm lamp in the room */
  blockquote,
  pre,
  table {
    box-shadow: var(--shadow-soft), inset 0 0 0 1px rgba(139, 109, 74, 0.03);
  }


  /* ── Link interaction crosses layers ── */
  a:hover {
    color: var(--coffee-cream);
    border-bottom-color: var(--coffee-mocha);
  }


  /* ══════════════════════════════════════════════
     PRINT
     ══════════════════════════════════════════════ */

  @media print {
    body {
      background: white;
      color: #222;
    }
    pre, blockquote, table {
      background: #f5f5f5;
      border-color: #ccc;
      box-shadow: none;
    }
    h1, h2 { color: #1a1a2e; }
    h2 { border-left-color: #8b6d4a; }
    h3 { color: #2a4a6a; }
    strong { color: #6F4E37; }
    code { color: #2a4a6a; }
    pre::before { color: #999; }
  }
</style>

# Documentação de Arquitetura — Server App (coffe_server)

> **Versão:** 0.0.1-SNAPSHOT
> **Stack:** Java 21 + Spring Boot 4.0.6
> **Propósito:** Servidor pessoal multifuncional — autenticação JWT, OAuth2 Google, gerenciamento de máquinas, integração Linux, MCP para agentes de IA, controle de backup e sistema multi-usuário com permissões.

---

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

## Sumário

1. [Visão Geral do Projeto](#1-visão-geral-do-projeto)
2. [Filosofia de Arquitetura](#2-filosofia-de-arquitetura)
3. [Estrutura de Diretórios](#3-estrutura-de-diretórios)
4. [Sistema de Resolução de Token (TokenResolver)](#4-sistema-de-resolução-de-token-tokenresolver)
   - 4.1 [TokenResolver (Interface)](#41-tokenresolver-interface)
   - 4.2 [TokenRequestContext (Interface)](#42-tokenrequestcontext-interface)
   - 4.3 [HttpTokenRequestContext (Adapter)](#43-httptokenrequestcontext-adapter)
   - 4.4 [CookieTokenResolver (Implementação)](#44-cookietokenresolver-implementação)
   - 4.5 [JtwTokenResvoler (Implementação)](#45-jtwtokenresvoler-implementação)
   - 4.6 [TokenResolverManager (Orquestrador)](#46-tokenresolvermanager-orquestrador)
   - 4.7 [Como Usar — Guia Prático](#47-como-usar--guia-prático)
   - 4.8 [Criando um Novo Resolver](#48-criando-um-novo-resolver)
5. [Sistema de Cookies](#5-sistema-de-cookies)
   - 5.1 [CookieDomain (Value Object)](#51-cookiedomain-value-object)
   - 5.2 [CookieService (Interface)](#52-cookieservice-interface)
   - 5.3 [HttpCookieService (Implementação)](#53-httpcookieservice-implementação)
6. [Sistema de Autenticação](#6-sistema-de-autenticação)
   - 6.1 [Fluxo de Login](#61-fluxo-de-login)
   - 6.2 [Fluxo de Registro](#62-fluxo-de-registro)
   - 6.3 [AuthenticationService (Interface)](#63-authenticationservice-interface)
   - 6.4 [SpringAuthenticationService (Implementação)](#64-springauthenticationservice-implementação)
   - 6.5 [UserAuthenticationService (Use Case)](#65-userauthenticationservice-use-case)
7. [Sistema de Autorização (JWT)](#7-sistema-de-autorização-jwt)
   - 7.1 [TokenService (Interface)](#71-tokenservice-interface)
   - 7.2 [JwtTokenService (Implementação)](#72-jwttokenservice-implementação)
   - 7.3 [JwtAuthenticationFilter (Filter)](#73-jwtauthenticationfilter-filter)
   - 7.4 [SecurityUser (Record)](#74-securityuser-record)
   - 7.5 [SecurityConfig](#75-securityconfig)
8. [Camada de Domínio](#8-camada-de-domínio)
   - 8.1 [Modelos de Domínio](#81-modelos-de-domínio)
   - 8.2 [Value Objects](#82-value-objects)
   - 8.3 [Enums](#83-enums)
   - 8.4 [Exceções de Domínio](#84-exceções-de-domínio)
   - 8.5 [Interfaces de Repositório](#85-interfaces-de-repositório)
9. [Camada de Aplicação](#9-camada-de-aplicação)
   - 9.1 [Controllers](#91-controllers)
   - 9.2 [DTOs](#92-dtos)
   - 9.3 [Application Services](#93-application-services)
10. [Camada de Infraestrutura](#10-camada-de-infraestrutura)
    - 10.1 [Adaptadores de Persistência (JPA)](#101-adaptadores-de-persistência-jpa)
    - 10.2 [PasswordService (BCrypt)](#102-passwordservice-bcrypt)
    - 10.3 [Exception Handler](#103-exception-handler)
11. [Integração Google OAuth2](#11-integração-google-oauth2)
12. [Integração MCP / Spring AI](#12-integração-mcp--spring-ai)
13. [Banco de Dados](#13-banco-de-dados)
14. [Como Adicionar um Novo Fluxo de Autenticação](#14-como-adicionar-um-novo-fluxo-de-autenticação)
15. [Boas Práticas & Convenções](#15-boas-práticas--convenções)
16. [Glossário de Interfaces](#16-glossário-de-interfaces)

---

## 1. Visão Geral do Projeto

O **coffe_server** é o servidor central do ecossistema pessoal do Quitto — um hub multifuncional para estudos, automação e gerenciamento de homelab. Ele funciona como um backend geral que expõe:

- **API REST** para autenticação (login/register) e gerenciamento de recursos
- **Autenticação JWT** via cookie ou header `Authorization: Bearer`
- **Login social** via Google OAuth2
- **Integração MCP** (Model Context Protocol) para agentes de IA
- **Gerenciamento de Máquinas** com suporte a Tailscale e Wake-on-LAN
- **Controle de backup** de arquivos e sistemas
- **Sistema multi-usuário com permissões** — acesso concedido pelo administrador para outras pessoas
- **Integração com Linux** (users, groups, sistema de arquivos)

### Stack Tecnológica

| Componente | Tecnologia |
|---|---|
| Runtime | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Segurança | Spring Security + Auth0 java-jwt |
| Banco | PostgreSQL (produção) / H2 (dev/teste) |
| ORM | Spring Data JPA |
| OAuth2 | Spring Security OAuth2 Client |
| IA/MCP | Spring AI MCP Server 1.0.2 |
| Build | Maven |
| Templates | Thymeleaf |

---

## 2. Filosofia de Arquitetura

O projeto segue **Arquitetura Hexagonal** (Ports & Adapters), também conhecida como **Clean Architecture**. O princípio central é:

> **O domínio não depende de nada externo.** Frameworks, bancos de dados e detalhes de infraestrutura são plugáveis através de interfaces (portas).

### Diagrama de Camadas

```
┌──────────────────────────────────────────────────────────────┐
│                     APPLICATION LAYER                         │
│  Controllers  │  DTOs  │  Application Services               │
│  (orquestração de use cases, sem lógica de negócio)          │
├──────────────────────────────────────────────────────────────┤
│                       DOMAIN LAYER                            │
│  Models  │  Value Objects  │  Interfaces (Ports)  │  Enums   │
│  (regras de negócio PURAS, sem frameworks, sem Spring)       │
├──────────────────────────────────────────────────────────────┤
│                    INFRASTRUCTURE LAYER                        │
│  Adapters  │  JPA  │  Security  │  External APIs              │
│  (implementa as interfaces do domínio)                        │
├──────────────────────────────────────────────────────────────┤
│                       MCP LAYER                                │
│  Tools  │  Services  │  (Integração com agentes de IA)        │
└──────────────────────────────────────────────────────────────┘
```

### Regras da Arquitetura

1. **Domínio NÃO importa nada de infraestrutura** (zero Spring, zero Jakarta, zero HTTP)
2. **Aplicação orquestra** o domínio, mas não contém lógica de negócio
3. **Infraestrutura implementa** interfaces do domínio (adapter pattern)
4. **Dependências apontam para dentro** — sempre para o domínio

---

## 3. Estrutura de Diretórios

```
com.quitto.server/
│
├── ServerApplication.java              # Entry point
│
├── application/                         # CAMADA DE APLICAÇÃO
│   ├── controllers/                     # REST controllers
│   │   ├── APIController.java           #   GET /api/test
│   │   ├── AuthenticationController.java # POST /auth/login, /auth/register
│   │   └── HomeController.java          #   GET /, /login (Thymeleaf)
│   │
│   ├── dto/
│   │   └── Auth/
│   │       ├── LoginDTO.java            #   {name, password}
│   │       ├── LoginResponseDTO.java    #   {token, date}
│   │       ├── RegisterDTO.java         #   {name, password, email}
│   │       └── RegisterResponseDTO.java #   {Token, date}
│   │
│   └── services/
│       ├── Auth/
│       │   └── UserAuthenticationService.java  # Use case: login/register
│       ├── Machine/
│       │   └── MachineService.java             # Use case: change owner
│       └── Users/
│           └── UserService.java                # (em construção)
│
├── domain/                               # CAMADA DE DOMÍNIO (PURA)
│   ├── enums/
│   │   ├── Provaider.java               #   GOOGLE, GITHUB
│   │   └── Role.java                    #   ADMIN, USER, MCP, API
│   │
│   ├── exception/
│   │   ├── AuthenticationException.java  # Base para falhas de auth
│   │   └── InvalidTokenException.java    # Token inválido/expirado
│   │
│   ├── interfaces/                       # PORTAS (contratos)
│   │   ├── Auth/
│   │   │   ├── AuthenticationService.java # authenticate / register
│   │   │   └── PasswordService.java       # encode / matches
│   │   └── Token/
│   │       ├── TokenRequestContext.java   # getHeader / getCookie
│   │       ├── TokenResolver.java         # resolve(RequestContext)
│   │       └── TokenService.java          # generate / verify / extract
│   │
│   ├── models/
│   │   ├── ExternalAccount/
│   │   │   └── ExternalAccont.java       # Conta OAuth externa
│   │   ├── LinuxAcount/
│   │   │   ├── Groups.java              # Grupo Linux
│   │   │   └── LinuxUser.java           # Usuário Linux
│   │   ├── Machine/
│   │   │   └── Machine.java             # Máquina gerenciada
│   │   └── User/
│   │       └── User.java                # Agregado raiz do sistema
│   │
│   ├── Repository/                       # Portas de repositório
│   │   ├── Machine/
│   │   │   └── MachineRepository.java
│   │   └── users/
│   │       └── UserRepository.java
│   │
│   └── valueobject/
│       └── CookieDomain.java            # Cookie imutável e validado
│
├── infrastructure/                       # CAMADA DE INFRAESTRUTURA
│   ├── config/
│   │   └── logger/
│   │       └── CoffeColorConverter.java  # Logback colorido
│   │
│   ├── db/                               # PERSISTÊNCIA (JPA)
│   │   ├── LinuxUser/
│   │   │   └── Entity/
│   │   │       ├── GroupsEntity.java
│   │   │       └── LinuxUserEntity.java
│   │   ├── Machine/
│   │   │   ├── Adapter/
│   │   │   │   └── MachineRepositoryAdapter.java
│   │   │   ├── Entity/
│   │   │   │   └── MachineEntity.java
│   │   │   ├── Mapper/
│   │   │   │   └── MachineMapper.java
│   │   │   └── Repository/
│   │   │       └── JpaMachineRepository.java
│   │   └── User/
│   │       ├── Adapter/
│   │       │   └── UserRepositoryAdapter.java
│   │       ├── Entity/
│   │       │   ├── ExternalAccountEntity.java
│   │       │   └── UserEntity.java
│   │       ├── Mapper/
│   │       │   └── UserMapper.java
│   │       └── Repository/
│   │           └── JpaUserRepository.java
│   │
│   ├── external/
│   │   ├── google/
│   │   │   └── GoogleAuthService.java   # Obtém tokens OAuth2
│   │   └── GoogleCalendarClient.java     # Cliente Google Calendar API
│   │
│   ├── interfaces/                       # ⚠ Interfaces que DEVERIAM estar no domínio
│   │   └── Cookies/
│   │       └── CookieService.java
│   │
│   ├── security/
│   │   ├── Filter/
│   │   │   ├── Adapter/
│   │   │   │   └── HttpTokenRequestContext.java  # HttpServletRequest → TokenRequestContext
│   │   │   ├── Token/
│   │   │   │   ├── CookieTokenResolver.java       # Lê cookie "access_token"
│   │   │   │   └── JtwTokenResvoler.java          # Lê header Authorization
│   │   │   └── JwtAuthenticationFilter.java       # Filter principal
│   │   ├── SecurityConfig.java                    # Config Spring Security
│   │   └── SecurityUser.java                      # Principal leve
│   │
│   └── services/
│       ├── Auth/
│       │   ├── SpringAuthenticationService.java   # Implementa AuthenticationService
│       │   ├── Token/
│       │   │   ├── Cookies/
│       │   │   │   └── HttpCookieService.java     # Implementa CookieService
│       │   │   ├── Jtw/
│       │   │   │   └── JwtTokenService.java       # Implementa TokenService
│       │   │   └── TokenResolverManager.java      # ⚠ Orquestrador (devia estar na aplicação)
│       │   └── UserDetailsServiceImpl.java        # UserDetailsService do Spring
│       ├── BCrypt/
│       │   └── BCryptPassowordService.java        # Implementa PasswordService
│       └── OAuth/
│           └── OAuth2UserProvisioningService.java # Auto-provisionamento OAuth2
│
├── mcp/                                  # CAMADA MCP (Spring AI)
│   ├── services/
│   │   └── GoogleCalenderService.java    # Serviço de calendário
│   └── tools/
│       ├── CalendarController.java       # REST endpoints /api/calendar
│       └── GoogelCalenderTools.java      # @Tool para agentes de IA
│
├── shared/                               # CROSS-CUTTING
│   ├── exception/
│   │   ├── AuthExceptionHandler.java     # @RestControllerAdvice
│   │   └── MachineNotFoundException.java
│   └── (constants/, helpers/, utils/ vazios)
│
└── resources/
    ├── application.properties            # Config principal
    ├── application-h2.properties         # Dev profile (H2)
    ├── data-h2.sql                       # Seed data (dev)
    ├── logback-spring.xml               # Logging config
    ├── static/css/app.css                # Estilos
    └── templates/
        ├── index.html                    # Landing page
        └── login.html                    # Página de login
```

---

## 4. Sistema de Resolução de Token (TokenResolver)

Este é o **coração da arquitetura de autenticação** do sistema. O objetivo é simples:

> **O filtro de autenticação não deve saber de onde o token veio.**

Ele só pergunta: *"Tem um token aí?"* — e alguém responde.

### Fluxo Completo

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

### 4.1 TokenResolver (Interface)

**Localização:** `domain/interfaces/Token/TokenResolver.java`

```java
public interface TokenResolver {
    Optional<String> resolver(TokenRequestContext request);
}
```

**Responsabilidade:** Abstrair a origem de um token JWT. Cada implementação sabe ler token de um lugar diferente (cookie, header, query param, WebSocket, etc.).

**Contrato:**
- **Entrada:** Um `TokenRequestContext` (abstração do request HTTP)
- **Saída:** `Optional<String>` — o token se encontrado, ou `Optional.empty()` se não
- **Efeito colateral:** Zero. É pura leitura.

**Por que `Optional`?** Porque o token pode não existir naquela fonte. `Optional.empty()` é mais seguro que `null` — força o caller a tratar a ausência.

**Por que no domínio?** Porque `TokenResolver` é um **contrato** (porta). A regra "extrair token de um request" é um conceito de negócio, independente de como o request chega (HTTP, WebSocket, mensageria).

### 4.2 TokenRequestContext (Interface)

**Localização:** `domain/interfaces/Token/TokenRequestContext.java`

```java
public interface TokenRequestContext {
    Optional<String> getHeader(String name);
    Optional<CookieDomain> getCookie(String name);
}
```

**Responsabilidade:** Abstrair o acesso a headers e cookies de um request, sem expor detalhes de infraestrutura (HttpServletRequest, Jakarta, etc.).

**Métodos:**

| Método | Retorno | Descrição |
|---|---|---|
| `getHeader(name)` | `Optional<String>` | Retorna o valor de um header HTTP, se existir |
| `getCookie(name)` | `Optional<CookieDomain>` | Retorna um cookie como Value Object, se existir |

**Por que `CookieDomain` e não `jakarta.servlet.http.Cookie`?** Porque `CookieDomain` é uma classe pura do domínio (sem frameworks). Se usássemos `jakarta.servlet.http.Cookie`, a interface dependeria de Jakarta — e o domínio não pode depender de frameworks.

### 4.3 HttpTokenRequestContext (Adapter)

**Localização:** `infrastructure/security/Filter/Adapter/HttpTokenRequestContext.java`

```java
public class HttpTokenRequestContext implements TokenRequestContext {
    private final HttpServletRequest request;

    public HttpTokenRequestContext(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Optional<String> getHeader(String name) {
        return Optional.ofNullable(request.getHeader(name));
    }

    @Override
    public Optional<CookieDomain> getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return Optional.of(new CookieDomain(
                    cookie.getName(), cookie.getValue(),
                    cookie.isHttpOnly(), cookie.getSecure(),
                    cookie.getPath(), cookie.getMaxAge()
                ));
            }
        }
        return Optional.empty();
    }
}
```

**Responsabilidade:** Adaptar `HttpServletRequest` (Jakarta/JEE) para `TokenRequestContext` (domínio).

**Pattern:** **Adapter** — converte uma interface externa em uma interface do domínio.

**Por que isso é importante?** Imagine que amanhã você troque o Spring Web por outro framework (Ex: Vert.x, Helidon). Ou que o request venha de um WebSocket. Você só precisa criar um novo Adapter — a interface `TokenRequestContext` e todos os `TokenResolver` permanecem inalterados.

### 4.4 CookieTokenResolver (Implementação)

**Localização:** `infrastructure/security/Filter/Token/CookieTokenResolver.java`

```java
@Component
public class CookieTokenResolver implements TokenResolver {
    @Override
    public Optional<String> resolver(TokenRequestContext request) {
        return request.getCookie("access_token")
            .map(CookieDomain::value);
    }
}
```

**Responsabilidade:** Extrair o token JWT do cookie chamado `access_token`.

**Fluxo:**
1. Pede ao `request` o cookie `"access_token"`
2. Se existir, mapeia `CookieDomain → String` (extrai só o valor)
3. Se não existir, retorna `Optional.empty()`

**Por que `@Component`?** Para que o Spring injete automaticamente esta implementação na lista de resolvers do `TokenResolverManager`. Graças à injeção de `List<TokenResolver>`, qualquer novo `@Component TokenResolver` entra automaticamente na chain.

### 4.5 JtwTokenResvoler (Implementação)

**Localização:** `infrastructure/security/Filter/Token/JtwTokenResvoler.java`

```java
@Component
public class JtwTokenResvoler implements TokenResolver {
    @Override
    public Optional<String> resolver(TokenRequestContext request) {
        return request.getHeader("Authorization")
            .filter(v -> !v.isBlank())
            .map(v -> v.replace("Bearer ", ""));
    }
}
```

**Responsabilidade:** Extrair o token JWT do header `Authorization: Bearer <token>`.

**Fluxo:**
1. Pede ao request o header `"Authorization"`
2. Se o valor não for vazio, remove o prefixo `"Bearer "`
3. Retorna o token limpo

**Por que `filter(v -> !v.isBlank())`?** Para evitar processar headers vazios — se o header existe mas está vazio, não é um token válido.

### 4.6 TokenResolverManager (Orquestrador)

**Localização:** `infrastructure/services/Auth/Token/TokenResolverManager.java`

```java
@Service
public class TokenResolverManager {
    private final List<TokenResolver> resolvers;

    public TokenResolverManager(List<TokenResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public Optional<String> resolve(TokenRequestContext context) {
        for (TokenResolver resolver : resolvers) {
            Optional<String> token = resolver.resolver(context);
            if (token.isPresent()) return token;
        }
        return Optional.empty();
    }
}
```

**Responsabilidade:** Orquestrar a chain de resolvers. Tenta cada um até encontrar um token.

**Pattern:** **Chain of Responsibility** — cada resolver é um elo na corrente. O primeiro que consegue resolver "ganha".

**Ordem de resolução:** A ordem depende da ordem em que os beans são registrados no Spring. Tipicamente:
1. `CookieTokenResolver` — verifica cookie primeiro (SPA/browser)
2. `JtwTokenResvoler` — fallback para header Authorization (API clients)

**Por que essa ordem?** Em um SPA (Single Page Application), o token geralmente está em um cookie HttpOnly. Para clientes de API (mobile, CLI), o token vem no header. A ordem prioriza o caso mais comum para o público-alvo.

### 4.7 Como Usar — Guia Prático

#### Exemplo 1: Usando o Manager para resolver um token

```java
// Em qualquer lugar que precise de um token
@Autowired
private TokenResolverManager manager;

public void algumMetodo(HttpServletRequest request) {
    TokenRequestContext context = new HttpTokenRequestContext(request);

    Optional<String> token = manager.resolve(context);

    token.ifPresentOrElse(
        t -> System.out.println("Token encontrado: " + t),
        () -> System.out.println("Nenhum token encontrado")
    );
}
```

#### Exemplo 2: Usando apenas um resolver específico

```java
@Autowired
private CookieTokenResolver cookieResolver;

public void testarCookie(HttpServletRequest request) {
    TokenRequestContext context = new HttpTokenRequestContext(request);

    Optional<String> token = cookieResolver.resolver(context);
    // Só verifica cookie, não tenta header
}
```

#### Exemplo 3: Mockando para testes

```java
// Teste unitário do CookieTokenResolver
@Test
void testCookieTokenResolver() {
    TokenRequestContext context = mock(TokenRequestContext.class);
    when(context.getCookie("access_token"))
        .thenReturn(Optional.of(new CookieDomain("access_token", "meu-jwt", true, true, "/", null)));

    CookieTokenResolver resolver = new CookieTokenResolver();
    Optional<String> result = resolver.resolver(context);

    assertEquals("meu-jwt", result.get());
}

// Teste unitário do TokenResolverManager
@Test
void testManagerChain() {
    TokenResolver first = mock(TokenResolver.class);
    TokenResolver second = mock(TokenResolver.class);

    when(first.resolver(any())).thenReturn(Optional.empty());
    when(second.resolver(any())).thenReturn(Optional.of("token-da-reserva"));

    TokenResolverManager manager = new TokenResolverManager(List.of(first, second));
    Optional<String> result = manager.resolve(mock(TokenRequestContext.class));

    assertEquals("token-da-reserva", result.get());
}
```

### 4.8 Criando um Novo Resolver

Para adicionar uma nova fonte de token (ex: query parameter, WebSocket header, API Key):

```java
@Component  // ← Spring descobre automaticamente
public class ApiKeyTokenResolver implements TokenResolver {
    @Override
    public Optional<String> resolver(TokenRequestContext request) {
        return request.getHeader("X-API-Key")
            .filter(key -> !key.isBlank());
    }
}
```

**Pronto.** O `TokenResolverManager` automaticamente incluirá este novo resolver na chain. Nenhuma outra classe precisa ser modificada — isso é **Open/Closed Principle** em ação.

---

## 5. Sistema de Cookies

### 5.1 CookieDomain (Value Object)

**Localização:** `domain/valueobject/CookieDomain.java`

```java
public record CookieDomain(
    String name,
    String value,
    boolean httpOnly,
    boolean secure,
    String path,
    Integer maxAge
) {
    public CookieDomain {
        Objects.requireNonNull(name, "Cookie name must not be null");
        Objects.requireNonNull(value, "Cookie value must not be null");
        if (name.isEmpty() || name.contains("=") || name.contains(";") || name.contains(",")) {
            throw new IllegalArgumentException("Invalid cookie name: " + name);
        }
    }

    public static CookieDomain of(String name, String value) {
        return new CookieDomain(name, value, true, true, "/", null);
    }

    public static CookieDomain of(String name, String value, String path, Integer maxAgeInSeconds) {
        return new CookieDomain(name, value, true, true, path, maxAgeInSeconds);
    }
}
```

**Responsabilidade:** Representar um cookie de forma imutável, segura e independente de framework.

**Por que um Value Object?**
- **Imutabilidade:** Depois de criado, um `CookieDomain` não pode ser alterado — seguro para passar entre camadas
- **Auto-validação:** O construtor valida o nome (RFC 6265) — nomes inválidos são pegos na criação
- **Factory methods:** `CookieDomain.of(name, value)` já configura `httpOnly=true, secure=true, path="/"` — defaults seguros
- **Sem framework:** Não depende de `jakarta.servlet.http.Cookie` — pode ser usado no domínio

**Métodos:**

| Método | Descrição |
|---|---|
| `CookieDomain(...)` | Construtor canônico com validação RFC 6265 |
| `CookieDomain.of(name, value)` | Cria cookie com defaults seguros (HttpOnly, Secure, path=/) |
| `CookieDomain.of(name, value, path, maxAge)` | Cria cookie com path e expiry personalizados |

### 5.2 CookieService (Interface)

**Localização:** `infrastructure/interfaces/Cookies/CookieService.java`

> ⚠ **Nota arquitetural:** Esta interface está na camada de infraestrutura e **deveria estar no domínio**. É uma porta (port) que define um contrato de criação de cookies.

```java
public interface CookieService {
    CookieDomain createCookie(String name, String value);
    CookieDomain createCookie(String name, String value, String path, Integer maxAgeInSeconds);
    <T> T toFrameworkCookie(CookieDomain domainCookie);
    void writeCookie(HttpServletResponse response, CookieDomain cookie);
}
```

**Métodos:**

| Método | Descrição |
|---|---|
| `createCookie(name, value)` | Cria `CookieDomain` com defaults seguros |
| `createCookie(name, value, path, maxAge)` | Cria `CookieDomain` com parâmetros customizados |
| `toFrameworkCookie(domainCookie)` | Converte `CookieDomain` → cookie do framework (Jakarta Servlet, etc.) |
| `writeCookie(response, cookie)` | Escreve o cookie na resposta HTTP |

**Por que o tipo genérico `<T>` em `toFrameworkCookie`?** Para que a mesma interface possa ser usada com diferentes frameworks web. A implementação concreta define o tipo de retorno.

### 5.3 HttpCookieService (Implementação)

**Localização:** `infrastructure/services/Auth/Token/Cookies/HttpCookieService.java`

```java
@Service
public class HttpCookieService implements CookieService {
    @Override
    public CookieDomain createCookie(String name, String value) {
        return CookieDomain.of(name, value);
    }

    @Override
    public CookieDomain createCookie(String name, String value, String path, Integer maxAgeInSeconds) {
        return CookieDomain.of(name, value, path, maxAgeInSeconds);
    }

    @Override
    public Cookie toFrameworkCookie(CookieDomain domainCookie) {
        Cookie cookie = new Cookie(domainCookie.name(), domainCookie.value());
        cookie.setHttpOnly(domainCookie.httpOnly());
        cookie.setSecure(domainCookie.secure());
        cookie.setPath(domainCookie.path());
        cookie.setDomain("coffe_server");
        if (domainCookie.maxAge() != null) {
            cookie.setMaxAge(domainCookie.maxAge());
        }
        return cookie;
    }

    @Override
    public void writeCookie(HttpServletResponse response, CookieDomain cookieDomain) {
        Objects.requireNonNull(response, "response cannot be null");
        Objects.requireNonNull(cookieDomain, "cookieDomain cannot be null");
        Cookie cookie = toFrameworkCookie(cookieDomain);
        response.addCookie(cookie);
    }
}
```

**Responsabilidade:** Implementar `CookieService` para o ecossistema Jakarta Servlet (Spring Web).

**Como usar:**

```java
@Autowired
private CookieService cookieService;

public void loginResponse(HttpServletResponse response) {
    // 1. Cria o cookie no domínio (seguro, validado)
    CookieDomain cookie = cookieService.createCookie("access_token", "meu-jwt-aqui");

    // 2. Escreve na resposta HTTP
    cookieService.writeCookie(response, cookie);
}
```

---

## 6. Sistema de Autenticação

### 6.1 Fluxo de Login

```
Cliente                          Server
  │                                │
  │  POST /auth/login              │
  │  {name, password}              │
  │ ──────────────────────────────►│
  │                                │
  │                    AuthenticationController
  │                        │
  │                    UserAuthenticationService
  │                        │
  │               ┌────────┴────────┐
  │               │                 │
  │          Authentication    TokenService
  │          Service (domain)  (domain)
  │               │                 │
  │          SpringAuth          JwtToken
  │          Service             Service
  │          (infra)             (infra)
  │               │                 │
  │          AuthenticationManager  │
  │          (Spring Security)      │
  │               │                 │
  │          UserDetailsServiceImpl │
  │               │                 │
  │          UserRepository         │
  │               │                 │
  │          BCrypt verify          │
  │               │                 │
  │          ◄────┴───────►         │
  │               │                 │
  │          User (domínio)    JWT token
  │               │                 │
  │          ◄────┴─────────────────┘
  │               │
  │          CookieService
  │               │
  │   ◄───────────┴────────────
  │  { token, date }  + Set-Cookie: access_token=...
```

### 6.2 Fluxo de Registro

Similar ao login, mas:
1. `SpringAuthenticationService.register()` verifica unicidade de nome/email
2. Cria um novo `User` com `Role.USER`
3. Persiste via `UserRepository`
4. Gera o JWT para o novo usuário

### 6.3 AuthenticationService (Interface)

**Localização:** `domain/interfaces/Auth/AuthenticationService.java`

```java
public interface AuthenticationService {
    User authenticate(String username, String password) throws AuthenticationException;
    User register(String name, String password, String email);
}
```

**Responsabilidade:** Contrato para autenticação e registro de usuários.

**Por que registrar está junto com autenticar?** Porque são operações complementares do mesmo agregado (`User`). Poderiam ser interfaces separadas, mas a coesão é alta.

### 6.4 SpringAuthenticationService (Implementação)

**Localização:** `infrastructure/services/Auth/SpringAuthenticationService.java`

```java
@Service
public class SpringAuthenticationService implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final PasswordService passwordService;
    private final UserRepository userRepository;

    public User authenticate(String username, String password) throws AuthenticationException {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
            return userRepository.findByName(auth.getName()).orElseThrow();
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new AuthenticationException("Authentication failed", e);
        }
    }

    public User register(String name, String password, String email) {
        if (userRepository.findByName(name).isPresent())
            throw new IllegalArgumentException("Username already exists");
        if (userRepository.findByEmail(email).isPresent())
            throw new IllegalArgumentException("Email already exists");

        User user = new User(name, passwordService.encode(password), email, Role.USER);
        return userRepository.save(user);
    }
}
```

**Responsabilidade:** Adaptar Spring Security (`AuthenticationManager`) para o contrato `AuthenticationService` do domínio.

**Pontos importantes:**
- Traduz exceções do Spring Security para `AuthenticationException` do domínio
- O `AuthenticationManager` delega para `UserDetailsServiceImpl` que busca o usuário no banco
- O `register()` verifica unicidade e codifica a senha com `PasswordService`

### 6.5 UserAuthenticationService (Use Case)

**Localização:** `application/services/Auth/UserAuthenticationService.java`

```java
@Service
public class UserAuthenticationService {
    private final AuthenticationService authenticationService;
    private final TokenService<Long> tokenService;

    public UserAuthenticationService(
            AuthenticationService authenticationService,
            TokenService<Long> tokenService) {
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    public String login(String name, String password) throws AuthenticationException {
        User user = authenticationService.authenticate(name, password);
        return tokenService.genareteToken(user.getId());
    }

    public String register(String name, String password, String email) {
        User user = authenticationService.register(name, password, email);
        return tokenService.genareteToken(user.getId());
    }
}
```

**Responsabilidade:** Orquestrar o fluxo de login/registro (use case da aplicação).

**Não contém lógica de negócio** — apenas coordena as interfaces do domínio. Isso é correto para a camada de aplicação.

---

## 7. Sistema de Autorização (JWT)

### 7.1 TokenService (Interface)

**Localização:** `domain/interfaces/Token/TokenService.java`

```java
public interface TokenService<ID extends Number> {
    String genareteToken(Long id);
    boolean verifyToken(String token);
    ID extractIdSubject(String token);
}
```

**Responsabilidade:** Contrato para manipulação de tokens JWT.

**Generics `<ID extends Number>`:** O tipo do subject (identificador do usuário) é genérico, mas restrito a subtipos de `Number` (`Long`, `Integer`, etc.). Isso permite que diferentes implementações usem diferentes tipos de ID.

**Métodos:**

| Método | Descrição |
|---|---|
| `genareteToken(Long id)` | Gera um token JWT para o usuário com o ID fornecido |
| `verifyToken(String token)` | Verifica se o token é válido (assinatura, expiry, issuer) |
| `extractIdSubject(String token)` | Extrai o ID do usuário do token |

### 7.2 JwtTokenService (Implementação)

**Localização:** `infrastructure/services/Auth/Token/Jtw/JwtTokenService.java`

```java
@Service
public class JwtTokenService implements TokenService<Long> {
    @Value("${api.security.key}")
    private String KEY;

    public String genareteToken(@NotNull Long id) {
        Algorithm algorithm = Algorithm.HMAC256(this.KEY);
        return JWT.create()
            .withIssuer("coffe-api")
            .withSubject(String.valueOf(id))
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
            .sign(algorithm);
    }

    public boolean verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(this.KEY);
        String subject = JWT.require(algorithm)
            .withIssuer("coffe-api")
            .build()
            .verify(token)
            .getSubject();
        return !subject.isBlank();
    }

    public Long extractIdSubject(String token) {
        Algorithm algorithm = Algorithm.HMAC256(this.KEY);
        String subject = JWT.require(algorithm)
            .withIssuer("coffe-api")
            .build()
            .verify(token)
            .getSubject();
        if (!subject.isBlank()) return Long.parseLong(subject);
        return null; // ⚠ Deveria retornar Optional<Long>
    }
}
```

**Detalhes Técnicos:**

| Característica | Valor |
|---|---|
| Algoritmo | HMAC256 (simétrico) |
| Issuer | `coffe-api` |
| Expiry | 1 hora (3600000ms) |
| Subject | ID do usuário como String |

**Por que HMAC256 simétrico?** Simplicidade. Para um único serviço, um segredo compartilhado é suficiente. Se o sistema crescer para múltiplos microsserviços, RSA/EC seria mais adequado (permite verificação sem expor a chave privada).

### 7.3 JwtAuthenticationFilter (Filter)

**Localização:** `infrastructure/security/Filter/JwtAuthenticationFilter.java`

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService<Long> tokenService;
    private final TokenResolverManager manager;
    private final UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) {
        try {
            // 1. Recupera o token (não sabe de onde vem)
            String token = recoverToken(request);

            // 2. Verifica se é válido
            if (!tokenService.verifyToken(token))
                throw new InvalidTokenException("Token inválido ou expirado");

            // 3. Extrai o ID do usuário
            Long id = tokenService.extractIdSubject(token);

            // 4. Carrega o usuário do banco
            User userDomain = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // 5. Cria o principal de segurança
            var user = new SecurityUser(id, userDomain.getName(), userDomain.getRole());
            var authorities = List.of(new SimpleGrantedAuthority(user.role().name()));
            var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);

            // 6. Popula o SecurityContext
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (NullPointerException | JWTVerificationException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    public String recoverToken(HttpServletRequest request) {
        TokenRequestContext context = new HttpTokenRequestContext(request);
        return manager.resolve(context)
            .orElseThrow(() -> new IllegalArgumentException("token is required"));
    }
}
```

**Responsabilidade:** Interceptar todas as requisições HTTP, extrair e validar o JWT, e configurar o contexto de segurança do Spring.

**Fluxo Detalhado:**

```
1. recoverToken(request)
   │
   ├── Cria HttpTokenRequestContext (adapter)
   │
   ├── manager.resolve(context)
   │   ├── CookieTokenResolver → tenta cookie "access_token"
   │   └── JwtTokenResolver → fallback: header "Authorization: Bearer"
   │
   └── Retorna token ou lança IllegalArgumentException

2. tokenService.verifyToken(token)
   │
   ├── Decodifica JWT com HMAC256
   ├── Verifica issuer "coffe-api"
   ├── Verifica assinatura
   └── Verifica expiry

3. tokenService.extractIdSubject(token)
   │
   └── Extrai subject → Long (ID do usuário)

4. repository.findById(id)
   │
   └── Busca User no banco de dados

5. Cria SecurityContext
   ├── SecurityUser { id, name, role }
   └── UsernamePasswordAuthenticationToken → SecurityContextHolder
```

### 7.4 SecurityUser (Record)

**Localização:** `infrastructure/security/SecurityUser.java`

```java
public record SecurityUser(Long id, String name, Role role) {}
```

**Responsabilidade:** Principal de segurança leve — contém apenas o necessário para autorização.

**Por que um record separado do `User` do domínio?**
- O `User` do domínio contém dados sensíveis (password hash)
- O `SecurityUser` é um DTO de segurança, exposto no `SecurityContext`
- Segurança: nunca colocar password hash no contexto de segurança

### 7.5 SecurityConfig

**Localização:** `infrastructure/security/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/test").permitAll()
                .requestMatchers("/mcp/**").hasAuthority("MCP")
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(jwtAuthenticationFilter,
                             UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth -> oauth
                .userInfoEndpoint(userInfo ->
                    userInfo.userService(oauthService)));
        return http.build();
    }
}
```

**Regras de Rota:**

| Rota | Acesso | Motivo |
|---|---|---|
| `/auth/**` | Público | Login e registro |
| `/api/test` | Público | Health check |
| `/mcp/**` | `ROLE_MCP` | Agentes de IA |
| `/admin/**` | `ROLE_ADMIN` | Administração |
| `/login`, `/` | Público | Páginas Thymeleaf |
| Demais | Autenticado | Requer JWT válido |

---

## 8. Camada de Domínio

### 8.1 Modelos de Domínio

#### User (Agregado Raiz)

**Localização:** `domain/models/User/User.java`

```java
public class User {
    private long id;
    private String name;
    private String passowrdHash;
    private String email;
    private Role role;
    private LinuxUser linuxUser;
    private List<Machine> machine;
}
```

**Responsabilidade:** Agregado raiz do sistema. Representa um usuário da plataforma.

**Relacionamentos:**
- `1:1` com `LinuxUser` (conta Linux associada)
- `1:N` com `Machine` (máquinas gerenciadas)

#### Machine

**Localização:** `domain/models/Machine/Machine.java`

```java
public class Machine {
    private Long id;
    private String hostname;
    private String tailscaleNodeKey;
    private String currentIp;
    private String macAddress;
    private boolean wolEnabled;
    private boolean status;
    private String OS;
    private Long userId;
}
```

**Responsabilidade:** Representa uma máquina gerenciada por um usuário.

**Métodos de negócio:**
- `changeOwner(User user)` — transfere propriedade

#### ExternalAccont

**Localização:** `domain/models/ExternalAccount/ExternalAccont.java`

```java
public class ExternalAccont {
    private Long id;
    private Long user_id;
    private Provaider provaider;
    private String external_client;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiresAt;
}
```

**Responsabilidade:** Vincular um usuário local a uma conta OAuth externa (Google, GitHub).

#### LinuxUser e Groups

```java
public class LinuxUser {
    private int uid;
    private String name;
    private String shell;
    private String homeDir;
    private Groups group;
}

public class Groups {
    private int GID;
    private String name;
    private boolean is_active;
}
```

**Responsabilidade:** Representar contas de usuário Linux para gerenciamento de servidores.

### 8.2 Value Objects

#### CookieDomain (já detalhado na seção 5.1)

Value Objects são **imutáveis**, **auto-validáveis** e **comparáveis por valor**. `CookieDomain` é o único VO atualmente implementado.

### 8.3 Enums

#### Role

```java
public enum Role { ADMIN, USER, MCP, API }
```

| Role | Uso |
|---|---|
| `ADMIN` | Acesso total ao sistema |
| `USER` | Usuário padrão |
| `MCP` | Acesso aos endpoints MCP (agentes de IA) |
| `API` | Acesso a APIs específicas |

#### Provaider

```java
public enum Provaider { GOOGLE, GITHUB }
```

Provedores OAuth2 suportados.

### 8.4 Exceções de Domínio

#### AuthenticationException

```java
public class AuthenticationException extends RuntimeException { }
```

Base para falhas de autenticação. Usada pelo `AuthenticationService`.

#### InvalidTokenException

```java
public class InvalidTokenException extends AuthenticationException { }
```

Específica para tokens JWT inválidos ou expirados.

### 8.5 Interfaces de Repositório

#### UserRepository

```java
public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
```

#### MachineRepository

```java
public interface MachineRepository {
    Machine save(Machine machine);
    Optional<Machine> findById(long id);
    Optional<Machine> findByHostname(String hostname);
    Optional<Machine> findByMacAddress(String macAddress);
    Optional<Machine> findByTailscaleNodeKey(String tailscaleNodeKey);
    List<Machine> findAll();
    boolean existsById(long id);
    void deleteById(long id);
    void delete(Machine machine);
}
```

---

## 9. Camada de Aplicação

### 9.1 Controllers

#### AuthenticationController

**Rotas:**

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/auth/login` | Autentica usuário, retorna JWT + cookie |
| `POST` | `/auth/register` | Registra novo usuário, retorna JWT |

**Detalhe importante:** O login retorna o token tanto no body JSON quanto no cookie `Set-Cookie`. Isso suporta tanto clientes SPA (cookie) quanto APIs (Bearer header).

#### APIController

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/test` | Retorna nome do usuário autenticado |

#### HomeController

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/` | Página inicial (Thymeleaf) |
| `GET` | `/login` | Página de login (Thymeleaf) |

### 9.2 DTOs

| DTO | Campos | Uso |
|---|---|---|
| `LoginDTO` | `name`, `password` | Request de login |
| `LoginResponseDTO` | `token`, `date` | Response de login |
| `RegisterDTO` | `name`, `password`, `email` | Request de registro |
| `RegisterResponseDTO` | `Token`, `date` | Response de registro |
| `ErrorResponse` | `msg` | Response de erro |

### 9.3 Application Services

| Service | Responsabilidade |
|---|---|
| `UserAuthenticationService` | Orquestrar login/register |
| `MachineService` | Transferir propriedade de máquina |
| `UserService` | (em construção) |

---

## 10. Camada de Infraestrutura

### 10.1 Adaptadores de Persistência (JPA)

O padrão é **Repository Adapter**:

```
UserRepository (domínio)  ←── UserRepositoryAdapter (infra)  ←── JpaUserRepository (Spring Data)
     ↑                             ↑                                  ↑
  (porta)                     (adapter)                          (JPA concreto)
```

Cada adapter:
1. Recebe objeto de domínio
2. Converte para entidade JPA via Mapper
3. Chama o JpaRepository
4. Converte resultado de volta para domínio
5. Retorna objeto de domínio

#### UserRepositoryAdapter

```java
// Estrutura conceitual
public class UserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository jpa;

    public User save(User user) {
        UserEntity entity = UserMapper.toInfra(user);
        UserEntity saved = jpa.save(entity);
        return UserMapper.toDomain(saved);
    }

    public Optional<User> findById(Long id) {
        return jpa.findById(id).map(UserMapper::toDomain);
    }
    // ...
}
```

### 10.2 PasswordService (BCrypt)

**Localização:** `infrastructure/services/BCrypt/BCryptPassowordService.java`

```java
@Service
public class BCryptPassowordService implements PasswordService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encode(String raw) { return encoder.encode(raw); }
    public boolean matches(String raw, String hash) { return encoder.matches(raw, hash); }
}
```

**Responsabilidade:** Implementar hashing de senha com BCrypt via Spring Security.

### 10.3 Exception Handler

**Localização:** `shared/exception/AuthExceptionHandler.java`

```java
@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException e) {
        return ResponseEntity.status(401)
            .body(new ErrorResponse("Invalid username or password"));
    }
}
```

**Responsabilidade:** Capturar exceções de autenticação e retornar respostas HTTP 401 padronizadas.

---

## 11. Integração Google OAuth2

### Fluxo OAuth2

```
Usuário clica "Entrar com Google"
        │
        ▼
/oauth2/authorization/google
        │
        ▼
Google OAuth2 Provider
  (tela de consentimento Google)
        │
        ▼
Redirect com código de autorização
        │
        ▼
OAuth2UserProvisioningService.loadUser()
        │
        ├── Busca usuário por email no banco
        │
        ├── Se existe: retorna usuário existente
        │
        └── Se não existe: cria novo UserEntity com Role.USER
        │
        ▼
DefaultOAuth2User com role authority
        │
        ▼
Usuário autenticado via Google
```

### OAuth2UserProvisioningService

**Localização:** `infrastructure/services/OAuth/OAuth2UserProvisioningService.java`

**Responsabilidade:** Auto-provisionar usuários no primeiro login com Google. Se o email já existe no banco, apenas autentica. Se não, cria o usuário automaticamente.

---

## 12. Integração MCP / Spring AI

### Arquitetura MCP

```
Agente de IA (Claude, GPT, etc.)
        │
        ├── HTTP → /mcp/** (Spring AI MCP Server)
        │       │
        │       └── GoogelCalenderTools.listEvents() [@Tool]
        │               │
        │               ├── SecurityContextHolder.getAuthentication()
        │               ├── GoogleAuthService.getAuthorizedClient()
        │               ├── GoogleCalendarClient.getCalendar()
        │               └── GoogleCalenderService.listEvents()
        │
        └── HTTP → /api/calendar/events (REST)
                │
                └── CalendarController.listEvents()
```

### Componentes MCP

| Componente | Responsabilidade |
|---|---|
| `GoogelCalenderTools` | Expõe `@Tool` para agentes de IA via MCP |
| `GoogleCalenderService` | Lógica de negócio do Google Calendar |
| `CalendarController` | REST endpoints para calendário |
| `GoogleAuthService` | Obtém tokens OAuth2 autorizados |
| `GoogleCalendarClient` | Cliente da API Google Calendar |

---

## 13. Banco de Dados

### Schema (PostgreSQL)

```
┌───────────┐       ┌──────────────┐       ┌──────────┐
│   user    │ 1:1   │  linux_user  │ M:1   │  groups  │
│           │───────│              │───────│          │
│ id (PK)   │       │ uid (PK)     │       │ gid (PK) │
│ name (UQ) │       │ name (UQ)    │       │ name (UQ)│
│ password  │       │ shell        │       │ is_active│
│ hash      │       │ home_dir     │       └──────────┘
│ email (UQ)│       │ gid (FK)     │
│ role      │       │ user_id (FK) │
└─────┬─────┘       └──────────────┘
      │
      │ 1:N
      │
┌─────┴─────────┐
│    machine     │
│                │
│ id (PK)        │
│ hostname (UQ)  │
│ tailscale_key  │
│ current_ip     │
│ mac_address    │
│ wol_enabled    │
│ status         │
│ os             │
│ user_id (FK)   │
└────────────────┘
      │
      │ 1:N
      │
┌─────┴──────────────┐
│ external_account    │
│                     │
│ id (PK)             │
│ user_id (FK)        │
│ provider (GOOGLE|...)│
│ external_client     │
│ access_token        │
│ refresh_token       │
│ expires_at          │
└─────────────────────┘
```

---

## 14. Como Adicionar um Novo Fluxo de Autenticação

### Exemplo: Adicionar autenticação por API Key

#### Passo 1: Criar o Resolver

```java
@Component
public class ApiKeyTokenResolver implements TokenResolver {
    @Override
    public Optional<String> resolver(TokenRequestContext request) {
        return request.getHeader("X-API-Key")
            .filter(key -> !key.isBlank());
    }
}
```

**Nada mais precisa ser modificado.** O Spring automaticamente injeta este novo resolver na chain.

#### Passo 2: (Opcional) Criar um serviço de validação

Se API Keys têm validação diferente de JWT:

```java
// Interface no domínio
public interface ApiKeyService {
    boolean validateKey(String apiKey);
    Long extractUserId(String apiKey);
}

// Implementação na infraestrutura
@Service
public class MyApiKeyService implements ApiKeyService {
    // ...
}
```

#### Passo 3: Modificar o Filter para aceitar múltiplos tipos

```java
// No JwtAuthenticationFilter, você pode verificar o tipo de token
String token = recoverToken(request);

if (isJwt(token)) {
    // validar como JWT
} else if (isApiKey(token)) {
    // validar como API Key
}
```

---

## 15. Boas Práticas & Convenções

### Convenções de Nomenclatura

| Conceito | Convenção | Exemplo Correto | Evitar |
|---|---|---|---|
| Interface de domínio | Nome claro do contrato | `TokenResolver` | — |
| Implementação | Prefixo do framework + nome | `JwtTokenService` | `JtwTokenResvoler` ❌ |
| Adapter | Framework + Interface | `HttpTokenRequestContext` | — |
| Value Object | Nome do conceito | `CookieDomain` | — |
| Use Case | Ação + Service | `UserAuthenticationService` | — |

### O que Evitar

1. **Interfaces na camada errada** — Toda interface que define um contrato de negócio pertence ao **domínio**, não à infraestrutura
2. **Null em vez de Optional** — Prefira `Optional<T>` para valores que podem ou não existir
3. **Field injection** — Prefira constructor injection (mais testável, imutável)
4. **System.out/System.err** — Use logs estruturados (SLF4J)
5. **Typos em nomes de classes/métodos** — Afetam a legibilidade e a API pública

### Checklist para Novos Componentes

- [ ] A interface está no domínio? (sem framework imports)
- [ ] A implementação está na infraestrutura? (com framework imports)
- [ ] O nome está livre de typos?
- [ ] Usa constructor injection?
- [ ] Retorna `Optional` em vez de `null`?
- [ ] As exceções são do domínio, não do framework?

---

## 16. Glossário de Interfaces

| Interface | Localização | Propsósito | Implementações |
|---|---|---|---|
| `TokenResolver` | `domain/interfaces/Token` | Extrair token de um request | `CookieTokenResolver`, `JtwTokenResvoler` |
| `TokenRequestContext` | `domain/interfaces/Token` | Abstrair acesso a headers/cookies | `HttpTokenRequestContext` |
| `TokenService<ID>` | `domain/interfaces/Token` | Gerar/verificar/extrair tokens JWT | `JwtTokenService` |
| `AuthenticationService` | `domain/interfaces/Auth` | Autenticar/registrar usuários | `SpringAuthenticationService` |
| `PasswordService` | `domain/interfaces/Auth` | Codificar/verificar senhas | `BCryptPassowordService` |
| `CookieService` | ⚠ `infrastructure/interfaces/Cookies` | Criar/gerenciar cookies | `HttpCookieService` |
| `UserRepository` | `domain/Repository/users` | Persistir/recuperar usuários | `UserRepositoryAdapter` |
| `MachineRepository` | `domain/Repository/Machine` | Persistir/recuperar máquinas | `MachineRepositoryAdapter` |

---

> **Documentação mantida por:** Quitto
> **Última atualização:** Julho 2026
> **Propósito:** Documentação viva — atualize conforme a arquitetura evoluir.

# ADR-001: Estratégia de Autenticação — JWT + mTLS Híbrido

> **Status:** ✅ Accepted (2026-08-06)
> **Autor:** Quitto
> **Relacionados:** ADR-004 (CA para mTLS)

---

## Contexto

O coffe_server é um servidor homelab central exposto a múltiplos clientes:

- **Dashboard web** (SPA em `src/main/resources/static/app/`)
- **Clientes de API** (CLI, PS3, integrações)
- **Agentes de IA** via MCP (`/mcp/**`)
- **Usuários convidados** com permissões controladas

A autenticação atual funciona com **JWT HMAC256** (Auth0 java-jwt 4.5.2), entregue via:

1. **Cookie HttpOnly** `access_token` (SPA/browser) — `CookieTokenResolver`
2. **Header `Authorization: Bearer`** (API clients) — `JwtTokenResolver`

O TLS server-side já está habilitado (`server.ssl.enabled=true`, keystore PKCS12, HTTP/2) com `client-auth=none`. O ambiente é um homelab pessoal onde **máquinas da mesma rede** (via Tailscale) precisam de confiança de dispositivo além de identidade de usuário.

A decisão foi tomada antes da escrita deste documento (registrada no `docs/planning/TODO.md`); este ADR formaliza e documenta a decisão.

## Decisão

Adotar **JWT + mTLS híbrido**:

- **JWT** é o mecanismo **primário de identidade de usuário** — stateless, revogável por expiração (1h), independente de transporte. Vale para todos os clientes (web, API, MCP).
- **mTLS** é uma **camada adicional de confiança de dispositivo/transporte** — client certificates para comunicações máquina-a-máquina e infraestrutura (agentes locais, serviços internos, Tailscale), ativada de forma incremental sobre o TLS existente.

A cadeia de resolução de token (Chain of Responsibility) permanece intacta; o mTLS não substitui o JWT, apenas adiciona verificação de cliente na camada de transporte.

## Consequências

**Positivas:**
- JWT cobre qualquer cliente com um único mecanismo (cookie OU header)
- mTLS adiciona confiança de dispositivo para comunicações internas/agentes
- Implementação incremental: mTLS não bloqueia o JWT
- Compatível com o ecossistema atual (nenhum cliente existente quebra)

**Negativas:**
- Dois mecanismos de segurança para manter e documentar
- mTLS exige emissão e rotação de certificados de cliente (depende do ADR-004)
- Complexidade de debug quando ambos os layers falham

**Neutras:**
- `SecurityConfig` precisa de ajuste: `client-auth` de `none` → `require`/`want` + `X509AuthenticationFilter` quando o ADR-004 for decidido

## Alternativas consideradas

| Alternativa | Motivo da rejeição |
|---|---|
| **JWT only** | Não oferece confiança de dispositivo para comunicações máquina-a-máquina no homelab |
| **mTLS only** | Inviável para browsers/SPA e clientes externos (emissão de certs por usuário) |

## Referências

- `infrastructure/security/Filter/JwtAuthenticationFilter.java`
- `infrastructure/security/Filter/Token/CookieTokenResolver.java`
- `infrastructure/security/Filter/Token/JwtTokenResolver.java`
- `infrastructure/security/SecurityConfig.java`
- `application.properties` — `server.ssl.*`
- TODO.md: Seção 🔒 Segurança — mTLS (client-auth=none → require)

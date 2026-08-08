# ADR-004: CA para mTLS no Homelab

> **Status:** ⏳ Open (2026-08-06) — sem decisão tomada; documenta opções para decidir
> **Autor:** Quitto
> **Relacionados:** ADR-001 (JWT + mTLS híbrido)

---

## Contexto

O **TLS server-side já está habilitado** (`server.ssl.enabled=true`, keystore PKCS12, HTTP/2, `client-auth=none`). Para ativar o mTLS (decisão do ADR-001), o servidor precisa **validar certificados de cliente** — o que exige uma **CA (Autoridade Certificadora)** que emita e assine esses certificados.

O ambiente é um homelab pessoal com máquinas conectadas via **Tailscale**, agentes locais (coffe-agent) e serviços internos. Requisitos para a CA:

- Emissão de certificados de cliente (identity)
- **Rotação** de certificados (certs expiram)
- **Revogação** quando uma máquina/agente perde confiança
- Baixo custo operacional — não é uma empresa, é um homelab

## Opções

### Opção A — CA própria (step-ca)

Instalar **step-ca** (Smallstep) como CA privada no servidor; emitir certs para cada dispositivo/agente via `step` CLI ou ACME.

- ✅ Controle total (emitir, renovar, revogar quando quiser)
- ✅ Integra bem com Tailscale (node certs)
- ✅ Modelo ACME embutido facilita rotação automática
- ❌ Custo operacional: mais um serviço para manter e monitorar
- ❌ Chave raiz da CA é um **single point of failure** — exige backup seguro e offline

### Opção B — Certificados auto-assinados por dispositivo

Cada cliente gera seu próprio self-signed cert; o servidor mantém uma allowlist de public keys.

- ✅ Simples: sem infraestrutura nova
- ✅ Sem custo operacional
- ❌ Sem hierarquia de confiança (cada cert é uma âncora)
- ❌ Rotação e revogação manuais (editar a allowlist)
- ❌ Escala mal: mais de 5-10 dispositivos vira dor de cabeça

### Opção C — CA gerenciada / pública (Let's Encrypt, ZeroSSL)

Usar uma CA pública para emitir client certs.

- ✅ Zero manutenção de infraestrutura
- ❌ **Client certs de CAs públicas são praticamente inviáveis** (CAs públicas como Let's Encrypt não emitem client certificates; custo por cert se existir)
- ❌ Dependência externa para um homelab privado

## Decisão (pendente)

**Nenhuma decisão tomada ainda.** A recomendação técnica preliminar é a **Opção A (step-ca)** por atender melhor os requisitos de rotação/revogação e integrar com Tailscale, mas a escolha depende do trade-off aceitável de operação vs controle.

> ⚠️ **Bloqueio:** enquanto o ADR-004 não for decidido, o mTLS permanece `client-auth=none` (task 🔒 Segurança — mTLS, ⛔ Bloqueado).

## Consequências por opção

| Aspecto | A (step-ca) | B (auto-assinados) | C (managed) |
|---|---|---|---|
| Controle | Total | Alto (allowlist) | Nenhum |
| Rotação automática | ✅ ACME | ❌ Manual | ✅ (se existisse) |
| Revogação | ✅ | ❌ | ✅ (se existisse) |
| Custo operacional | Médio | Baixo | Baixo |
| Escala p/ homelab | ✅ | ⚠️ ≤ ~5 dispositivos | ❌ |

## Referências

- `application.properties` — `server.ssl.*` (TLS server-side ativo)
- ADR-001 — JWT + mTLS híbrido (decisão que motiva este ADR)
- TODO.md: Seção 🔒 Segurança — mTLS (client-auth=none → require)

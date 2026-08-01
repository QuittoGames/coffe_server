#!/usr/bin/env bash
# =============================================================================
# 05-auth-test.sh — Authentication Security Testing
# =============================================================================
# Testes:
#   - JWT expirado / inválido / alterado
#   - Ausência de token
#   - Permissão insuficiente
#   - Brute force controlado (rate limit)
# =============================================================================

set -euo pipefail

TARGET_URL="${1:-http://localhost:8080}"
REPORT_DIR="${2:-/workspace/logs/security-lab}"
LOG_DIR="${3:-$REPORT_DIR/logs}"
TIMESTAMP="$(date -u '+%Y%m%d_%H%M%S')"

mkdir -p "$LOG_DIR/auth-test"
AUTH_LOG="$LOG_DIR/auth-test/auth-${TIMESTAMP}.log"
AUTH_RESULTS="$REPORT_DIR/metrics/auth-results-${TIMESTAMP}.json"

log_info() { echo "  [*] $1"; }
log_ok()   { echo "  [+] $1"; }
log_warn() { echo "  [!] $1"; }
log_fail() { echo "  [-] $1"; }
log_detail() { echo "       $1"; }

# ──────── 1. Obter um token válido para testes ────────
# O servidor agora é cookie-only: o JWT não vem no body, e sim no header
# Set-Cookie: access_token=<jwt>. Extraímos da resposta.
log_info "1/5 Obter token de referência"
extract_access_token() {
  sed -n 's/^[Ss]et-[Cc]ookie: access_token=\([^;]*\).*/\1/p' | head -n1
}

REGISTER_HEADERS=$(curl -s -D - -o /dev/null -X POST "$TARGET_URL/auth/register" \
  -H 'Content-Type: application/json' \
  -d '{"name":"sec_test_user_'"$TIMESTAMP"'","password":"Test@123!","email":"sec_'"$TIMESTAMP"'@test.com"}' 2>/dev/null)

VALID_TOKEN=$(echo "$REGISTER_HEADERS" | extract_access_token)

if [ -z "$VALID_TOKEN" ]; then
  log_warn "Register failed — trying login with admin_teste"
  LOGIN_HEADERS=$(curl -s -D - -o /dev/null -X POST "$TARGET_URL/auth/login" \
    -H 'Content-Type: application/json' \
    -d '{"name":"admin_teste","password":"admin123"}' 2>/dev/null)
  VALID_TOKEN=$(echo "$LOGIN_HEADERS" | extract_access_token)
fi

if [ -z "$VALID_TOKEN" ]; then
  log_warn "No valid token obtained — using dummy for some tests"
  VALID_TOKEN="dummy-test-token"
fi

log_detail "Token: ${VALID_TOKEN:0:20}..."

# ──────── 2. JWT Manipulation Tests ────────
log_info "2/5 JWT Security Tests"
JWT_RESULTS="$LOG_DIR/auth-test/jwt-results.txt"
: > "$JWT_RESULTS"

# 2a. Sem token
echo "--- [2a] Request without token ---" >> "$JWT_RESULTS"
STATUS=$(curl -s -o /dev/null -w '%{http_code}' "$TARGET_URL/api/test" 2>/dev/null)
echo "GET /api/test (no auth): $STATUS" >> "$JWT_RESULTS"
echo "$STATUS" > "$LOG_DIR/auth-test/no-token-status.txt"

# 2b. Token vazio
echo "--- [2b] Empty token ---" >> "$JWT_RESULTS"
STATUS=$(curl -s -o /dev/null -w '%{http_code}' "$TARGET_URL/api/test" \
  -H 'Authorization: Bearer ' 2>/dev/null)
echo "GET /api/test (empty Bearer): $STATUS" >> "$JWT_RESULTS"

# 2c. Token claramente inválido
echo "--- [2c] Invalid token ---" >> "$JWT_RESULTS"
STATUS=$(curl -s -o /dev/null -w '%{http_code}' "$TARGET_URL/api/test" \
  -H 'Authorization: Bearer invalid.token.here' 2>/dev/null)
echo "GET /api/test (invalid token): $STATUS" >> "$JWT_RESULTS"

# 2d. Token com payload alterado
echo "--- [2d] Tampered token ---" >> "$JWT_RESULTS"
TAMPERED="${VALID_TOKEN}xx"
STATUS=$(curl -s -o /dev/null -w '%{http_code}' "$TARGET_URL/api/test" \
  -H "Authorization: Bearer $TAMPERED" 2>/dev/null)
echo "GET /api/test (tampered token): $STATUS" >> "$JWT_RESULTS"

# 2e. Token mal formatado
echo "--- [2e] Malformed token ---" >> "$JWT_RESULTS"
STATUS=$(curl -s -o /dev/null -w '%{http_code}' "$TARGET_URL/api/test" \
  -H 'Authorization: Bearer not-a-jwt' 2>/dev/null)
echo "GET /api/test (malformed): $STATUS" >> "$JWT_RESULTS"

# 2f. Token em cookie inválido
echo "--- [2f] Wrong cookie ---" >> "$JWT_RESULTS"
STATUS=$(curl -s -o /dev/null -w '%{http_code}' "$TARGET_URL/api/test" \
  --cookie "access_token=invalid" 2>/dev/null)
echo "GET /api/test (invalid cookie): $STATUS" >> "$JWT_RESULTS"

# 2g. SQL Injection na autenticação
echo "--- [2g] Auth SQL Injection ---" >> "$JWT_RESULTS"
STATUS=$(curl -s -o /dev/null -w '%{http_code}' -X POST "$TARGET_URL/auth/login" \
  -H 'Content-Type: application/json' \
  -d '{"name":"admin_teste","password":"'\'' OR 1=1 --"}' 2>/dev/null)
echo "POST /auth/login (SQLi): $STATUS" >> "$JWT_RESULTS"

cat "$JWT_RESULTS"

# ──────── 3. Permission Testing ────────
log_info "3/5 Permission Access Tests"

# 3a. MCP endpoint (requer ROLE_MCP)
MCP_STATUS=$(curl -s -o /dev/null -w '%{http_code}' "$TARGET_URL/mcp/" \
  -H "Authorization: Bearer $VALID_TOKEN" 2>/dev/null)
log_detail "MCP endpoint (USER token): $MCP_STATUS (expected 403)"

# 3b. Admin endpoint (requer ROLE_ADMIN)
ADMIN_STATUS=$(curl -s -o /dev/null -w '%{http_code}' "$TARGET_URL/admin/" \
  -H "Authorization: Bearer $VALID_TOKEN" 2>/dev/null)
log_detail "Admin endpoint (USER token): $ADMIN_STATUS (expected 403)"

# ──────── 4. Brute Force / Rate Limit Test ────────
log_info "4/5 Rate Limit / Brute Force Test"
log_detail "Sending 20 requests to /auth/login with wrong password..."

BF_LOG="$LOG_DIR/auth-test/brute-force-${TIMESTAMP}.txt"
: > "$BF_LOG"

RATE_LIMITED=false
FIRST_429=""
for i in $(seq 1 20); do
  STATUS=$(curl -s -o /dev/null -w '%{http_code}' -X POST "$TARGET_URL/auth/login" \
    -H 'Content-Type: application/json' \
    -d '{"name":"admin_teste","password":"wrong_password_'"$i"'"}' 2>/dev/null)
  echo "$i: $STATUS" >> "$BF_LOG"
  if [ "$STATUS" = "429" ] && [ -z "$FIRST_429" ]; then
    RATE_LIMITED=true
    FIRST_429="$i"
  fi
done

if [ "$RATE_LIMITED" = true ]; then
  log_ok "Rate limiting detected at attempt $FIRST_429"
else
  log_warn "No rate limiting detected after 20 attempts"
fi

# ──────── 5. Summary ────────
log_info "5/5 Compiling auth test results"

NO_TOKEN=$(cat "$LOG_DIR/auth-test/no-token-status.txt" 2>/dev/null || echo 0)

jq -n \
  --arg no_token "$NO_TOKEN" \
  --arg mcp_status "${MCP_STATUS:-000}" \
  --arg admin_status "${ADMIN_STATUS:-000}" \
  --argjson rate_limited "$(echo "$RATE_LIMITED" | sed 's/true/true/;s/false/false/')" \
  --arg rate_limit_at "${FIRST_429:-null}" \
  '{
    no_auth_status: ($no_token|tonumber),
    mcp_endpoint_status: ($mcp_status|tonumber),
    admin_endpoint_status: ($admin_status|tonumber),
    rate_limit_detected: $rate_limited,
    rate_limit_attempt: $rate_limit_at
  }' \
  > "$AUTH_RESULTS"

cat "$JWT_RESULTS" >> "$AUTH_LOG"
cat "$BF_LOG" >> "$AUTH_LOG"

log_ok "Auth testing completed"
exit 0

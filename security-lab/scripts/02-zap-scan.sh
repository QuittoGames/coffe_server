#!/usr/bin/env bash
# =============================================================================
# 02-zap-scan.sh — OWASP ZAP Security Scan
# =============================================================================
# Executa Baseline Scan (passivo) — detecta:
#   XSS, SQL Injection, CSRF, headers inseguros, cookies inseguros
# Modo: baseline (seguro) para CI; full scan apenas LAB explícito
# =============================================================================

set -euo pipefail

TARGET_URL="${1:-http://localhost:8080}"
REPORT_DIR="${2:-/workspace/logs/security-lab}"
LOG_DIR="${3:-$REPORT_DIR/logs}"
TIMESTAMP="$(date -u '+%Y%m%d_%H%M%S')"

mkdir -p "$LOG_DIR/zap-scan"

log_info() { echo "  [*] $1"; }
log_ok()   { echo "  [+] $1"; }
log_fail() { echo "  [-] $1"; }

# ── Verificar ZAP ──
if [ ! -f /opt/zap/zap.sh ]; then
  log_fail "OWASP ZAP not found — skipping scan"
  exit 42
fi

log_info "Starting OWASP ZAP Baseline Scan against $TARGET_URL"

# Extrair host e port do target
HOST=$(echo "$TARGET_URL" | sed -E 's|https?://([^:/]+).*|\1|')
PORT=$(echo "$TARGET_URL" | sed -E 's|https?://[^:/]+:?([0-9]*).*|\1|')
PORT="${PORT:-80}"

ZAP_REPORT="$REPORT_DIR/zap-report-${TIMESTAMP}.html"
ZAP_JSON="$REPORT_DIR/metrics/zap-results-${TIMESTAMP}.json"

# ── Executar ZAP em modo daemon ──
log_info "Starting ZAP daemon..."
/opt/zap/zap.sh -daemon -port 8090 \
  -config api.disablekey=true \
  -config database.recoverylog=false \
  -host 127.0.0.1 \
  > "$LOG_DIR/zap-scan/zap-daemon-${TIMESTAMP}.log" 2>&1 &
ZAP_PID=$!

# Aguardar ZAP iniciar
for i in $(seq 1 30); do
  if curl -s -o /dev/null http://127.0.0.1:8090 2>/dev/null; then
    log_ok "ZAP daemon ready (attempt $i)"
    break
  fi
  sleep 2
done

# ── Baseline Scan ──
log_info "Running ZAP Baseline Scan..."
python3 /opt/zap/zap-api-scan.py \
  -t "$TARGET_URL" \
  -f openapi \
  -r "$ZAP_REPORT" \
  -J "$ZAP_JSON" \
  -I \
  -z "-config api.disablekey=true -config connection.timeoutInSecs=120" \
  2>&1 | tee -a "$LOG_DIR/zap-scan/zap-scan-${TIMESTAMP}.log" || true

# ── Parar ZAP ──
kill "$ZAP_PID" 2>/dev/null || true
sleep 2

# ── Extrair alertas do JSON ──
if [ -f "$ZAP_JSON" ]; then
  TOTAL_ALERTS=$(jq '.site[0].alerts | length' "$ZAP_JSON" 2>/dev/null || echo 0)
  HIGH_ALERTS=$(jq '[.site[0].alerts[] | select(.riskcode == "3")] | length' "$ZAP_JSON" 2>/dev/null || echo 0)
  MED_ALERTS=$(jq '[.site[0].alerts[] | select(.riskcode == "2")] | length' "$ZAP_JSON" 2>/dev/null || echo 0)

  log_info "ZAP Scan Results:"
  echo "    Total alerts: $TOTAL_ALERTS"
  echo "    High:   $HIGH_ALERTS"
  echo "    Medium: $MED_ALERTS"

  jq -n \
    --arg total "$TOTAL_ALERTS" \
    --arg high "$HIGH_ALERTS" \
    --arg med "$MED_ALERTS" \
    '{total: ($total|tonumber), high: ($high|tonumber), medium: ($med|tonumber)}' \
    > "$REPORT_DIR/metrics/zap-summary-${TIMESTAMP}.json"
else
  log_warn "ZAP JSON results not found"
fi

log_ok "ZAP scan completed — report: $ZAP_REPORT"
exit 0

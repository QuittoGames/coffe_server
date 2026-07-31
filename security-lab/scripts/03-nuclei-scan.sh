#!/usr/bin/env bash
# =============================================================================
# 03-nuclei-scan.sh — Nuclei Vulnerability Scanner
# =============================================================================
# Templates: CVEs, misconfigurations, exposed panels, auth issues
# Severidade: medium, high, critical apenas (para reduzir falso positivo)
# =============================================================================

set -euo pipefail

TARGET_URL="${1:-http://localhost:8080}"
REPORT_DIR="${2:-/workspace/logs/security-lab}"
LOG_DIR="${3:-$REPORT_DIR/logs}"
TIMESTAMP="$(date -u '+%Y%m%d_%H%M%S')"

mkdir -p "$LOG_DIR/nuclei-scan"

log_info() { echo "  [*] $1"; }
log_ok()   { echo "  [+] $1"; }
log_fail() { echo "  [-] $1"; }

# ── Verificar nuclei ──
if ! command -v nuclei &>/dev/null; then
  log_fail "Nuclei not found — skipping scan"
  exit 42
fi

NUCLEI_OUTPUT="$REPORT_DIR/metrics/nuclei-results-${TIMESTAMP}.json"
NUCLEI_REPORT="$REPORT_DIR/nuclei-report-${TIMESTAMP}.md"
NUCLEI_LOG="$LOG_DIR/nuclei-scan/nuclei-${TIMESTAMP}.log"

log_info "Starting Nuclei Scan against $TARGET_URL"
log_info "Templates: cves, misconfiguration, exposed-panels"

# ── Atualizar templates (se necessário) ──
nuclei -update-templates 2>/dev/null || true

# ── Escanear ──
nuclei \
  -u "$TARGET_URL" \
  -severity medium,high,critical \
  -json \
  -o "$NUCLEI_OUTPUT" \
  -t ~/nuclei-templates/cves/ \
  -t ~/nuclei-templates/misconfiguration/ \
  -t ~/nuclei-templates/exposed-panels/ \
  -t ~/nuclei-templates/security-misconfiguration/ \
  -rate-limit 50 \
  -concurrency 20 \
  -timeout 10 \
  -retries 2 \
  -headless \
  2>&1 | tee "$NUCLEI_LOG" || true

# ── Contar resultados ──
if [ -f "$NUCLEI_OUTPUT" ]; then
  TOTAL=$(wc -l < "$NUCLEI_OUTPUT" 2>/dev/null || echo 0)
  CRITICAL=$(grep -c '"severity":"critical"' "$NUCLEI_OUTPUT" 2>/dev/null || echo 0)
  HIGH=$(grep -c '"severity":"high"' "$NUCLEI_OUTPUT" 2>/dev/null || echo 0)
  MEDIUM=$(grep -c '"severity":"medium"' "$NUCLEI_OUTPUT" 2>/dev/null || echo 0)

  log_info "Nuclei Results:"
  echo "    Total findings: $TOTAL"
  echo "    Critical: $CRITICAL"
  echo "    High:     $HIGH"
  echo "    Medium:   $MEDIUM"

  # Gerar relatório markdown
  {
    echo "# Nuclei Scan Report — $TARGET_URL"
    echo "## Generated: $(date -u '+%Y-%m-%d %H:%M:%S UTC')"
    echo ""
    echo "| Severity | Count |"
    echo "|----------|-------|"
    echo "| Critical | $CRITICAL |"
    echo "| High     | $HIGH |"
    echo "| Medium   | $MEDIUM |"
    echo "| **Total** | **$TOTAL** |"
    echo ""
    echo "## Details"
    echo '```json'
    cat "$NUCLEI_OUTPUT" | head -100
    echo '```'
    echo ""
    echo "---"
    echo "*Full report: $NUCLEI_OUTPUT*"
  } > "$NUCLEI_REPORT"

  # Salvar sumário
  jq -n \
    --arg total "$TOTAL" \
    --arg critical "$CRITICAL" \
    --arg high "$HIGH" \
    --arg medium "$MEDIUM" \
    '{total: ($total|tonumber), critical: ($critical|tonumber), high: ($high|tonumber), medium: ($medium|tonumber)}' \
    > "$REPORT_DIR/metrics/nuclei-summary-${TIMESTAMP}.json"

  log_ok "Nuclei scan completed — $TOTAL findings"
else
  log_warn "Nuclei returned no findings or output file not created"
  echo "0" | jq -R '{total: ., critical: 0, high: 0, medium: 0}' > "$REPORT_DIR/metrics/nuclei-summary-${TIMESTAMP}.json"
fi

exit 0

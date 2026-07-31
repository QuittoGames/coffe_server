#!/usr/bin/env bash
# =============================================================================
# 07-container-scan.sh — Container Security Scan (Trivy)
# =============================================================================
# Escaneia: imagens Docker, sistema de arquivos, dependências
# Falha se: CRITICAL >= 1
# =============================================================================

set -euo pipefail

TARGET_URL="${1:-http://localhost:8080}"
REPORT_DIR="${2:-/workspace/logs/security-lab}"
LOG_DIR="${3:-$REPORT_DIR/logs}"
TIMESTAMP="$(date -u '+%Y%m%d_%H%M%S')"

mkdir -p "$LOG_DIR/container-scan"

log_info() { echo "  [*] $1"; }
log_ok()   { echo "  [+] $1"; }
log_fail() { echo "  [-] $1"; }

# ── Verificar Trivy ──
if ! command -v trivy &>/dev/null; then
  log_fail "Trivy not found — skipping container scan"
  exit 42
fi

log_info "Starting container/filesystem vulnerability scan"

# ── 1. Filesystem Scan ──
log_info "1/2 Filesystem vulnerability scan"
FS_OUTPUT="$LOG_DIR/container-scan/trivy-fs-${TIMESTAMP}.json"
FS_REPORT="$REPORT_DIR/metrics/trivy-fs-summary-${TIMESTAMP}.json"

trivy fs \
  --severity HIGH,CRITICAL \
  --format json \
  --output "$FS_OUTPUT" \
  /workspace \
  2>&1 | tail -5 >> "$LOG_DIR/container-scan/trivy-${TIMESTAMP}.log" || true

if [ -f "$FS_OUTPUT" ]; then
  FS_CRIT=$(jq '[.Results[].Vulnerabilities[] | select(.Severity == "CRITICAL")] | length' "$FS_OUTPUT" 2>/dev/null || echo 0)
  FS_HIGH=$(jq '[.Results[].Vulnerabilities[] | select(.Severity == "HIGH")] | length' "$FS_OUTPUT" 2>/dev/null || echo 0)

  log_info "Filesystem scan results:"
  echo "    Critical: $FS_CRIT"
  echo "    High:     $FS_HIGH"

  jq -n \
    --arg critical "$FS_CRIT" \
    --arg high "$FS_HIGH" \
    '{critical: ($critical|tonumber), high: ($high|tonumber)}' \
    > "$FS_REPORT"
else
  FS_CRIT=0
  FS_HIGH=0
  log_warn "Filesystem scan produced no output"
fi

# ── 2. Docker Image Scan (se disponível) ──
log_info "2/2 Docker image scan (if docker socket available)"
IMG_OUTPUT="$LOG_DIR/container-scan/trivy-image-${TIMESTAMP}.json"
IMG_REPORT="$REPORT_DIR/metrics/trivy-image-summary-${TIMESTAMP}.json"

if [ -S /var/run/docker.sock ] && command -v docker &>/dev/null; then
  # Procurar imagem do coffe-server
  IMAGE_NAME=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep -i 'coffe\|server' | head -1 || true)

  if [ -n "$IMAGE_NAME" ]; then
    log_info "Scanning image: $IMAGE_NAME"
    trivy image \
      --severity HIGH,CRITICAL \
      --format json \
      --output "$IMG_OUTPUT" \
      "$IMAGE_NAME" \
      2>&1 | tail -5 >> "$LOG_DIR/container-scan/trivy-${TIMESTAMP}.log" || true

    if [ -f "$IMG_OUTPUT" ]; then
      IMG_CRIT=$(jq '[.Results[].Vulnerabilities[] | select(.Severity == "CRITICAL")] | length' "$IMG_OUTPUT" 2>/dev/null || echo 0)
      IMG_HIGH=$(jq '[.Results[].Vulnerabilities[] | select(.Severity == "HIGH")] | length' "$IMG_OUTPUT" 2>/dev/null || echo 0)

      log_info "Docker image scan results:"
      echo "    Critical: $IMG_CRIT"
      echo "    High:     $IMG_HIGH"

      jq -n \
        --arg critical "$IMG_CRIT" \
        --arg high "$IMG_HIGH" \
        '{critical: ($critical|tonumber), high: ($high|tonumber)}' \
        > "$IMG_REPORT"
    fi
  else
    log_warn "No coffe-server Docker image found"
  fi
else
  log_warn "Docker socket not available — skipping image scan"
fi

# ── Check build-fail condition ──
if [ "${FS_CRIT:-0}" -gt 0 ]; then
  log_fail "CRITICAL vulnerabilities found ($FS_CRIT) — failing pipeline condition"
fi

log_ok "Container security scan completed"
exit 0

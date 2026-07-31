#!/usr/bin/env bash
# =============================================================================
# entrypoint.sh — Security Runner Entrypoint
# =============================================================================
# Orquestra a execução dos módulos de segurança em sequência.
# Uso: ./entrypoint.sh [--module <name>] [--help]
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
WORKSPACE="$(cd "$SCRIPT_DIR/.." && pwd)"
REPORT_DIR="${REPORT_DIR:-$WORKSPACE/logs/security-lab}"
LOG_DIR="${LOG_DIR:-$REPORT_DIR/logs}"
TARGET_URL="${TARGET_URL:-http://localhost:8080}"
TIMESTAMP="$(date -u '+%Y%m%d_%H%M%S')"

# ── Nerd Fonts / ASCII fallback ──
if [ -t 1 ] && command -v tput &>/dev/null && tput setaf 1 &>/dev/null; then
  ICON_CHECK="\xee\x80\xac"   # 
  ICON_CROSS="\xee\x80\xb0"   # 
  ICON_GEAR="\xee\x93\x99"    # 
  ICON_SHIELD="\xee\xb2\x84"  # 
  ICON_CHART="\xee\x93\x80"   # 
  ICON_BUG="\xee\x94\x88"     # 
  ICON_CLOCK="\xee\x90\xa5"   # 
  ICON_WARN="\xee\xb3\xa1"    # 
  ICON_FILE="\xee\x8e\x80"    # 
else
  ICON_CHECK="[OK]"
  ICON_CROSS="[FAIL]"
  ICON_GEAR="[RUN]"
  ICON_SHIELD="[SEC]"
  ICON_CHART="[CHART]"
  ICON_BUG="[BUG]"
  ICON_CLOCK="[TIME]"
  ICON_WARN="[WARN]"
  ICON_FILE="[FILE]"
fi

# ── Utilitários ──
log_info()  { echo -e "  ${ICON_GEAR} $1"; }
log_ok()    { echo -e "  ${ICON_CHECK} $1"; }
log_fail()  { echo -e "  ${ICON_CROSS} $1"; }
log_warn()  { echo -e "  ${ICON_WARN} $1"; }
log_step()  { echo -e "\n${ICON_SHIELD} ── $1 ──"; }

cleanup() {
  local exit_code=$?
  log_info "Cleaning up processes..."
  pkill -f "k6" 2>/dev/null || true
  pkill -f "locust" 2>/dev/null || true
  pkill -f "python3 start.py" 2>/dev/null || true
  pkill -f "zap.sh" 2>/dev/null || true
  exit $exit_code
}
trap cleanup EXIT SIGINT SIGTERM

mkdir -p "$LOG_DIR" "$REPORT_DIR/metrics" "$REPORT_DIR/screenshots"

# ── Help ──
show_help() {
  echo "Security Runner — coffe_server DevSecOps Pipeline"
  echo ""
  echo "Uso: docker run --rm coffe-security-runner [OPTIONS]"
  echo ""
  echo "Opções:"
  echo "  --all                  Executa todos os módulos (padrão)"
  echo "  --module <name>        Executa apenas um módulo específico"
  echo "  --list-modules         Lista módulos disponíveis"
  echo "  --target <url>         Define target URL (default: http://localhost:8080)"
  echo "  --help                 Mostra esta ajuda"
  echo ""
  echo "Módulos disponíveis:"
  echo "  01-load-test       k6 load testing (normal/spike/stress)"
  echo "  02-zap-scan        OWASP ZAP baseline scan"
  echo "  03-nuclei-scan     Nuclei vulnerability scanner"
  echo "  04-api-fuzzing     ffuf API fuzzing"
  echo "  05-auth-test       Authentication & JWT testing"
  echo "  06-mhddos-test     MHDDoS resilience test (LAB MODE)"
  echo "  07-container-scan  Trivy container vulnerability scan"
  echo "  08-chaos-test      Chaos testing (container/networking)"
  echo "  09-log-collector   Log collection after tests"
  echo "  10-report          Generate final report"
  echo ""
  echo "Exemplos:"
  echo "  docker run --rm coffe-security-runner --all"
  echo "  docker run --rm coffe-security-runner --module 01-load-test"
  echo "  docker run --rm coffe-security-runner --module 06-mhddos-test"
}

MODULES=()
RUN_ALL=false

while [[ $# -gt 0 ]]; do
  case "$1" in
    --help|-h)
      show_help
      exit 0
      ;;
    --all|-a)
      RUN_ALL=true
      shift
      ;;
    --module|-m)
      MODULES+=("$2")
      shift 2
      ;;
    --target|-t)
      TARGET_URL="$2"
      shift 2
      ;;
    --list-modules)
      echo "Available modules:"
      for f in "$SCRIPT_DIR"/0[0-9]-*.sh; do
        name=$(basename "$f" .sh)
        desc=$(head -5 "$f" | grep "^#" | tail -1 | sed 's/^# //')
        echo "  $name — $desc"
      done
      exit 0
      ;;
    *)
      MODULES+=("$1")
      shift
      ;;
  esac
done

if [ ${#MODULES[@]} -eq 0 ] || [ "$RUN_ALL" = true ]; then
  for f in "$SCRIPT_DIR"/0[0-9]-*.sh; do
    name=$(basename "$f" .sh)
    MODULES+=("$name")
  done
fi

# ══════════════════════════════════════════════════════════════════════════════
# EXECUÇÃO
# ══════════════════════════════════════════════════════════════════════════════
echo ""
echo "╔══════════════════════════════════════════════════════════════╗"
echo "║     ☕ coffe_server — Security Lab Runner                   ║"
echo "║     ${TARGET_URL}                              ║"
echo "║     $(date -u '+%Y-%m-%d %H:%M:%S UTC')                           ║"
echo "╚══════════════════════════════════════════════════════════════╝"
echo ""

# Validar contexto de segurança
CONTEXT_FILE="$WORKSPACE/security-test-context.json"
if [ -f "$CONTEXT_FILE" ]; then
  ENV_TYPE=$(grep -oP '"environment":\s*"\K[^"]+' "$CONTEXT_FILE")
  AUTHORIZED=$(grep -oP '"authorized":\s*\K[^,}]+' "$CONTEXT_FILE" | head -1)
  if [ "$ENV_TYPE" != "LAB" ] && [ "$ENV_TYPE" != "STAGING" ]; then
    log_fail "Environment '$ENV_TYPE' not authorized for security testing!"
    log_fail "ABORTING — Only LAB/STAGING environments are allowed."
    exit 1
  fi
  if [ "$AUTHORIZED" != "true" ]; then
    log_fail "Target not authorized for security testing!"
    log_fail "ABORTING — Set 'authorized': true in security-test-context.json"
    exit 1
  fi
  log_ok "Security context validated: $ENV_TYPE (authorized)"
else
  log_warn "No security-test-context.json found — proceeding with defaults"
fi

# Verificar se o target está acessível
if ! curl -s -o /dev/null -w '' "$TARGET_URL/api/test" 2>/dev/null; then
  log_warn "Target $TARGET_URL appears to be DOWN. Tests may fail."
fi

RESULTS_FILE="$REPORT_DIR/metrics/results.json"
echo '{"modules":{}, "start_time":"'"$(date -u -Iseconds)"'", "target":"'"$TARGET_URL"'"}' > "$RESULTS_FILE"

# Executar cada módulo
for module in "${MODULES[@]}"; do
  SCRIPT="$SCRIPT_DIR/$module.sh"
  if [ ! -f "$SCRIPT" ]; then
    log_warn "Module '$module' not found — skipping"
    continue
  fi

  log_step "Starting Module: $module"
  MODULE_START=$(date +%s)

  if bash "$SCRIPT" "$TARGET_URL" "$REPORT_DIR" "$LOG_DIR"; then
    log_ok "Module $module completed successfully"
    STATUS="passed"
  else
    EXIT_CODE=$?
    if [ "$EXIT_CODE" -eq 42 ]; then
      log_warn "Module $module skipped (not applicable)"
      STATUS="skipped"
    else
      log_fail "Module $module failed with exit code $EXIT_CODE"
      STATUS="failed"
    fi
  fi

  MODULE_END=$(date +%s)
  DURATION=$((MODULE_END - MODULE_START))

  # Update results
  jq --arg m "$module" \
    --arg s "$STATUS" \
    --arg d "$DURATION" \
    '.modules[$m] = {"status": $s, "duration_seconds": ($d|tonumber)}' \
    "$RESULTS_FILE" > "${RESULTS_FILE}.tmp" && mv "${RESULTS_FILE}.tmp" "$RESULTS_FILE"
done

# ── Report final ──
log_step "Generating Final Report"
bash "$SCRIPT_DIR/10-report.sh" "$TARGET_URL" "$REPORT_DIR" "$LOG_DIR"

echo ""
echo "╔══════════════════════════════════════════════════════════════╗"
echo "║  Security Lab Complete                                     ║"
echo "║  Reports: $REPORT_DIR"
echo "╚══════════════════════════════════════════════════════════════╝"

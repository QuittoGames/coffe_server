#!/usr/bin/env bash
# =============================================================================
# 09-log-collector.sh — Log Collector
# =============================================================================
# Coleta logs de todas as fontes disponíveis:
#   - Application logs (se montados)
#   - Docker logs (se socket disponível)
#   - System metrics (CPU, RAM, Disk, Network)
# =============================================================================

set -euo pipefail

TARGET_URL="${1:-http://localhost:8080}"
REPORT_DIR="${2:-/workspace/logs/security-lab}"
LOG_DIR="${3:-$REPORT_DIR/logs}"
TIMESTAMP="$(date -u '+%Y%m%d_%H%M%S')"

COLLECTED_DIR="$LOG_DIR/collected-${TIMESTAMP}"
mkdir -p "$COLLECTED_DIR"

log_info() { echo "  [*] $1"; }
log_ok()   { echo "  [+] $1"; }
log_detail() { echo "       $1"; }

log_info "Collecting logs from all available sources..."

# ──────── 1. Application Logs ────────
APP_LOG_DIR="$LOG_DIR/../.."
if [ -d "$APP_LOG_DIR" ]; then
  log_info "1/5 Application logs"
  find "$APP_LOG_DIR" -name "*.log" -type f 2>/dev/null | while read -r f; do
    cp "$f" "$COLLECTED_DIR/application-$(basename "$f")" 2>/dev/null || true
  done
  log_detail "Collected from $APP_LOG_DIR"
fi

# ──────── 2. Docker Logs ────────
if [ -S /var/run/docker.sock ] && command -v docker &>/dev/null; then
  log_info "2/5 Docker logs"
  DOCKER_LOG="$COLLECTED_DIR/docker-containers.log"
  docker ps -a --format '{{.Names}}' 2>/dev/null | while read -r container; do
    {
      echo "═══════════════════════════════════════════"
      echo " Container: $container"
      echo " Timestamp: $(date -u '+%Y-%m-%d %H:%M:%S UTC')"
      echo "═══════════════════════════════════════════"
      docker logs --tail 100 "$container" 2>&1
      echo ""
    } >> "$DOCKER_LOG" 2>/dev/null || true
  done
  log_detail "Collected from $(docker ps -q 2>/dev/null | wc -l) running containers"
fi

# ──────── 3. System Logs ────────
log_info "3/5 System metrics"

# CPU info
{
  echo "═══════════════════════════════════════════"
  echo " System Metrics — $(date -u '+%Y-%m-%d %H:%M:%S UTC')"
  echo "═══════════════════════════════════════════"
  echo ""
  echo "── CPU ──"
  top -bn1 | head -5 2>/dev/null || echo "top not available"
  echo ""
  echo "── Memory ──"
  free -h 2>/dev/null || echo "free not available"
  echo ""
  echo "── Disk ──"
  df -h / /workspace 2>/dev/null || echo "df not available"
  echo ""
  echo "── Network Connections (port 8080) ──"
  ss -tn state established "( dport = :8080 or sport = :8080 )" 2>/dev/null | tail -n +2 | wc -l
  echo ""
  echo "── Processes ──"
  ps aux --sort=-%mem | head -10 2>/dev/null || echo "ps not available"
} > "$COLLECTED_DIR/system-metrics-${TIMESTAMP}.log"

log_detail "System metrics collected"

# ──────── 4. Application Metrics (se actuator disponível) ────────
log_info "4/5 Application metrics"
ACTUATOR_LOG="$COLLECTED_DIR/actuator-metrics-${TIMESTAMP}.json"

# Tentar coletar métricas do actuator
for endpoint in health info metrics env; do
  curl -s --connect-timeout 5 "$TARGET_URL/actuator/$endpoint" \
    >> "$ACTUATOR_LOG" 2>/dev/null || echo "{\"${endpoint}\":\"unavailable\"}" >> "$ACTUATOR_LOG"
  echo "" >> "$ACTUATOR_LOG"
done
log_detail "Actuator metrics collected"

# ──────── 5. Test Logs ────────
log_info "5/5 Test execution logs"
if [ -d "$LOG_DIR" ]; then
  find "$LOG_DIR" -name "*.log" -type f 2>/dev/null | while read -r f; do
    rel_path="${f#$LOG_DIR/}"
    cp "$f" "$COLLECTED_DIR/test-${rel_path//\//-}" 2>/dev/null || true
  done
  log_detail "Test logs collected from $LOG_DIR"
fi

# ─── Compress ───
log_info "Compressing collected logs..."
tar -czf "$REPORT_DIR/logs/all-logs-${TIMESTAMP}.tar.gz" -C "$COLLECTED_DIR" . 2>/dev/null || true

log_info "Log collection summary:"
log_detail " Collected: $(find "$COLLECTED_DIR" -type f | wc -l) files"
log_detail " Archive: $REPORT_DIR/logs/all-logs-${TIMESTAMP}.tar.gz"

log_ok "Log collection completed"
exit 0

#!/usr/bin/env bash
# =============================================================================
# 01-load-test.sh — Load Testing Module (k6)
# =============================================================================
# Cenários: normal (100 usuários, 5min), spike (10→1000), stress (ramp up)
# Coleta: latência p50/p95/p99, taxa de erro, throughput
# =============================================================================

set -euo pipefail

TARGET_URL="${1:-http://localhost:8080}"
REPORT_DIR="${2:-/workspace/logs/security-lab}"
LOG_DIR="${3:-$REPORT_DIR/logs}"
TIMESTAMP="$(date -u '+%Y%m%d_%H%M%S')"
K6_SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../k6-scripts" && pwd)"

mkdir -p "$LOG_DIR/load-test"

log_info() { echo "  [*] $1"; }
log_ok()   { echo "  [+] $1"; }
log_fail() { echo "  [-] $1"; }

# ── Verificar k6 ──
if ! command -v k6 &>/dev/null; then
  log_fail "k6 not found — skipping load tests"
  exit 42
fi

log_info "Starting load tests against $TARGET_URL"
log_info "Results: $REPORT_DIR/metrics/load-test-*.json"

# ──────── 1. Normal Load Test ────────
log_info "1/3 Normal Load Test — 100 VUs, 3min"
export K6_VUS=100
export K6_DURATION=3m
export TEST_TYPE=normal

k6 run --quiet \
  --out json="$LOG_DIR/load-test/normal-${TIMESTAMP}.json" \
  --summary-export="$REPORT_DIR/metrics/load-normal-summary-${TIMESTAMP}.json" \
  "$K6_SCRIPT_DIR/normal.js" 2>&1 | tee -a "$LOG_DIR/load-test/normal-${TIMESTAMP}.log" || true

log_ok "Normal load test completed"

# ──────── 2. Spike Test ────────
log_info "2/3 Spike Test — 10→500 VUs sudden spike"
export K6_VUS=10

k6 run --quiet \
  --out json="$LOG_DIR/load-test/spike-${TIMESTAMP}.json" \
  --summary-export="$REPORT_DIR/metrics/load-spike-summary-${TIMESTAMP}.json" \
  "$K6_SCRIPT_DIR/spike.js" 2>&1 | tee -a "$LOG_DIR/load-test/spike-${TIMESTAMP}.log" || true

log_ok "Spike test completed"

# ──────── 3. Stress Test ────────
log_info "3/3 Stress Test — incremental ramp up"
export K6_VUS=20

k6 run --quiet \
  --out json="$LOG_DIR/load-test/stress-${TIMESTAMP}.json" \
  --summary-export="$REPORT_DIR/metrics/load-stress-summary-${TIMESTAMP}.json" \
  "$K6_SCRIPT_DIR/stress.js" 2>&1 | tee -a "$LOG_DIR/load-test/stress-${TIMESTAMP}.log" || true

log_ok "Stress test completed"

# ──────── Summary ────────
log_info "Load test results saved to:"
echo "    $REPORT_DIR/metrics/load-normal-summary-${TIMESTAMP}.json"
echo "    $REPORT_DIR/metrics/load-spike-summary-${TIMESTAMP}.json"
echo "    $REPORT_DIR/metrics/load-stress-summary-${TIMESTAMP}.json"

exit 0

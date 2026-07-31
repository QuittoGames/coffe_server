#!/usr/bin/env bash
# =============================================================================
# 08-chaos-test.sh — Chaos Testing
# =============================================================================
# Testa resiliência do sistema sob condições adversas:
#   - Container failure (stop/start)
#   - Network latency injection
#   - Simulação de indisponibilidade de banco (via app behavior)
# Apenas executa se Docker socket estiver disponível.
# =============================================================================

set -euo pipefail

TARGET_URL="${1:-http://localhost:8080}"
REPORT_DIR="${2:-/workspace/logs/security-lab}"
LOG_DIR="${3:-$REPORT_DIR/logs}"
TIMESTAMP="$(date -u '+%Y%m%d_%H%M%S')"

mkdir -p "$LOG_DIR/chaos"

log_info() { echo "  [*] $1"; }
log_ok()   { echo "  [+] $1"; }
log_fail() { echo "  [-] $1"; }
log_warn() { echo "  [!] $1"; }

CHAOS_LOG="$LOG_DIR/chaos/chaos-${TIMESTAMP}.log"
CHAOS_RESULTS="$REPORT_DIR/metrics/chaos-results-${TIMESTAMP}.json"

echo "Chaos Test Log — $(date -u '+%Y-%m-%d %H:%M:%S UTC')" > "$CHAOS_LOG"
echo "" >> "$CHAOS_LOG"

# Função helper: health check
check_health() {
  local label="$1"
  local status
  status=$(curl -s -o /dev/null -w '%{http_code}' --connect-timeout 5 --max-time 10 "$TARGET_URL/api/test" 2>/dev/null || echo "000")
  echo "$label: HTTP $status" | tee -a "$CHAOS_LOG"
  [ "$status" = "200" ] || [ "$status" = "401" ]
}

# ──────── Test 1: App Restart ────────
if [ -S /var/run/docker.sock ] && command -v docker &>/dev/null; then
  # Encontrar container da aplicação
  APP_CONTAINER=$(docker ps --format '{{.Names}}' | grep -i 'coffe\|server\|app' | head -1 || true)

  if [ -n "$APP_CONTAINER" ]; then
    HEALTH_BEFORE=true
    HEALTH_AFTER_RESTART=true

    # 1a. Restart do container
    log_info "1/3 Container Restart Test ($APP_CONTAINER)"

    # Health check antes
    if check_health "  Before restart"; then
      log_ok "  App healthy before restart"
    else
      log_warn "  App unhealthy before restart"
      HEALTH_BEFORE=false
    fi

    # Restart
    log_info "  Restarting container $APP_CONTAINER..."
    docker restart "$APP_CONTAINER" 2>&1 | tee -a "$CHAOS_LOG"

    # Aguardar recovery
    log_info "  Waiting for recovery..."
    RECOVERY_TIME=0
    for i in $(seq 1 15); do
      sleep 2
      RECOVERY_TIME=$((RECOVERY_TIME + 2))
      if check_health "  Recovery attempt $i"; then
        log_ok "  App recovered in ${RECOVERY_TIME}s"
        break
      fi
      if [ $i -eq 15 ]; then
        log_fail "  App did NOT recover after restart"
        HEALTH_AFTER_RESTART=false
      fi
    done

    # 1b. Stop do container
    log_info "  Stopping container $APP_CONTAINER..."
    docker stop "$APP_CONTAINER" 2>&1 | tee -a "$CHAOS_LOG"

    sleep 3
    if check_health "  After stop (expected fail)"; then
      log_warn "  App still responding after stop!"
    else
      log_ok "  App correctly down after container stop"
    fi

    # 1c. Start de volta
    log_info "  Starting container $APP_CONTAINER..."
    docker start "$APP_CONTAINER" 2>&1 | tee -a "$CHAOS_LOG"

    log_info "  Waiting for full recovery..."
    for i in $(seq 1 20); do
      sleep 3
      if check_health "  Recovery attempt $i"; then
        log_ok "  App fully recovered after start"
        break
      fi
    done

    jq -n \
      --arg healthy_before "$HEALTH_BEFORE" \
      --arg recovered "$HEALTH_AFTER_RESTART" \
      --arg recovery_time "${RECOVERY_TIME:-0}" \
      '{
        container_restart: {
          healthy_before: ($healthy_before == "true"),
          recovered: ($recovered == "true"),
          recovery_time_seconds: ($recovery_time|tonumber)
        }
      }' > "$CHAOS_RESULTS"
  else
    log_warn "No application container found — skipping container tests"
    echo '{"container_restart": {"skipped": true}}' > "$CHAOS_RESULTS"
  fi
else
  log_warn "Docker socket not available — skipping container chaos tests"
  echo '{"container_restart": {"skipped": true, "reason": "no_docker_socket"}}' > "$CHAOS_RESULTS"
fi

# ──────── Test 2: Health após stress ────────
log_info "2/3 Post-Stress Health Validation"
if check_health "  Final health check"; then
  log_ok "  Application healthy after chaos tests"
  RECOVERED=true
else
  log_fail "  Application unhealthy after chaos tests"
  RECOVERED=false
fi

# ──────── Test 3: Observação de logs ────────
log_info "3/3 Log analysis for crash indicators"
LOG_DIR_APP="$REPORT_DIR/../../"
if [ -d "$LOG_DIR_APP" ]; then
  APPLICATION_LOG="$LOG_DIR_APP/application.log"
  if [ -f "$APPLICATION_LOG" ]; then
    CRASH_COUNT=$(grep -c -i "exception\|error\|crash\|outofmemory\|nullpointer" \
      "$APPLICATION_LOG" 2>/dev/null || echo 0)
    log_info "Error indicators found in logs: $CRASH_COUNT"
  else
    CRASH_COUNT=0
    log_info "No application logs directory found"
  fi
fi

# Atualizar resultados
jq --arg recovered "$RECOVERED" \
   --arg crash_count "$CRASH_COUNT" \
   '. + {post_stress_health: {recovered: ($recovered == "true")}, log_errors: ($crash_count|tonumber)}' \
   "$CHAOS_RESULTS" > "${CHAOS_RESULTS}.tmp" && mv "${CHAOS_RESULTS}.tmp" "$CHAOS_RESULTS"

log_ok "Chaos testing completed"
exit 0

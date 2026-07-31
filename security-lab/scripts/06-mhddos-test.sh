#!/usr/bin/env bash
# =============================================================================
# 06-mhddos-test.sh — MHDDoS Controlled Resilience Test
# =============================================================================
#
# ⚠  MODO: LAB_RESILIENCE_TEST_ONLY
# ⚠  APENAS para ambiente local autorizado.
# ⚠  Uso controlado — threads limitadas, timeout automático.
#
# Objetivo: Avaliar resiliência do servidor sob tráfego HTTP intenso.
# Não é um ataque real — é um teste de resistência controlado.
#
# Monitora: CPU, RAM, HTTP errors, logs da aplicação
# Para automaticamente após o tempo limite.
# =============================================================================

set -euo pipefail

TARGET_URL="${1:-http://localhost:8080}"
REPORT_DIR="${2:-/workspace/logs/security-lab}"
LOG_DIR="${3:-$REPORT_DIR/logs}"
TIMESTAMP="$(date -u '+%Y%m%d_%H%M%S')"

MHDDOS_DIR="/opt/MHDDoS"
MAX_THREADS="${MHDDOS_MAX_THREADS:-50}"
MAX_DURATION="${MHDDOS_MAX_DURATION:-120}"  # segundos

mkdir -p "$LOG_DIR/mhddos"

log_info() { echo "  [*] $1"; }
log_ok()   { echo "  [+] $1"; }
log_fail() { echo "  [-] $1"; }
log_warn() { echo "  [!] $1"; }

# ── VALIDAÇÃO DE SEGURANÇA ──
# Só executa se for localhost
if ! echo "$TARGET_URL" | grep -qE 'localhost|127\.0\.0\.1|host\.docker\.internal'; then
  log_fail "TARGET $TARGET_URL NÃO É LOCALHOST"
  log_fail "MHDDoS module requires localhost target"
  log_fail "ABORTING for safety"
  exit 1
fi

# Verificar arquivo de contexto
CONTEXT_FILE="/workspace/security-test-context.json"
if [ -f "$CONTEXT_FILE" ]; then
  MHDDOS_ENABLED=$(jq -r '.modules.mhddos_resilience.enabled // false' "$CONTEXT_FILE")
  MHDDOS_MODE=$(jq -r '.modules.mhddos_resilience.mode // ""' "$CONTEXT_FILE")
  if [ "$MHDDOS_ENABLED" != "true" ] || [ "$MHDDOS_MODE" != "LAB_RESILIENCE_TEST_ONLY" ]; then
    log_fail "MHDDoS not authorized in context file"
    exit 1
  fi
  log_ok "MHDDoS authorized via security-test-context.json"
fi

# ── Verificar MHDDoS ──
if [ ! -d "$MHDDOS_DIR" ]; then
  log_fail "MHDDoS not found at $MHDDOS_DIR"
  exit 42
fi

# ── Extrair host:port do target ──
HOST=$(echo "$TARGET_URL" | sed -E 's|https?://([^:/]+).*|\1|')
PORT=$(echo "$TARGET_URL" | sed -E 's|https?://[^:/]+:?([0-9]*).*|\1|')
PORT="${PORT:-80}"

# ── Calcular end time ──
END_TIME=$((SECONDS + MAX_DURATION))

log_info "═══ MHDDoS Controlled Resilience Test ═══"
log_info " Target:     $TARGET_URL"
log_info " Host:       $HOST"
log_info " Port:       $PORT"
log_info " Max threads: $MAX_THREADS"
log_info " Max duration: ${MAX_DURATION}s"
log_info " Mode:       LAB_RESILIENCE_TEST_ONLY"
echo ""

# ── Iniciar monitoramento de recursos em background ──
STATS_LOG="$LOG_DIR/mhddos/stats-${TIMESTAMP}.csv"
echo "timestamp,cpu_percent,memory_percent,conn_count" > "$STATS_LOG"

monitor_resources() {
  while [ $SECONDS -lt $END_TIME ]; do
    CPU=$(top -bn1 | grep "Cpu(s)" | awk '{print $2}' | cut -d'.' -f1 2>/dev/null || echo 0)
    MEM=$(free | grep Mem | awk '{printf "%.1f", $3/$2 * 100}' 2>/dev/null || echo 0)
    CONN=$(ss -tn state established "( dport = :$PORT or sport = :$PORT )" 2>/dev/null | tail -n +2 | wc -l || echo 0)
    echo "$(date -u '+%H:%M:%S'),$CPU,$MEM,$CONN" >> "$STATS_LOG"
    sleep 2
  done
}
monitor_resources &
MONITOR_PID=$!

# ── Verificar saúde inicial ──
log_info "Verificando saúde inicial do servidor..."
for i in 1 2 3; do
  HEALTH=$(curl -s -o /dev/null -w '%{http_code}' "$TARGET_URL/api/test" 2>/dev/null || echo "000")
  echo "  Health check #$i: $HEALTH"
  sleep 1
done

# ── Executar MHDDoS (Layer7 - GET Flood controlado) ──
log_info "Iniciando MHDDoS — GET flood (threads=$MAX_THREADS)"
log_info "Auto-stop em ${MAX_DURATION}s"

cd "$MHDDOS_DIR"

# Executar MHDDoS com timeout
# Método GET: ataque HTTP GET controlado
# --debug para logs detalhados
timeout "$MAX_DURATION" python3 start.py \
  GET "$TARGET_URL" "$MAX_THREADS" 0 \
  > "$LOG_DIR/mhddos/mhddos-get-${TIMESTAMP}.log" 2>&1 &
MHDDOS_PID=$!

log_info "MHDDoS PID: $MHDDOS_PID"

# ── Enquanto MHDDoS roda, testar disponibilidade ──
AVAILABILITY_LOG="$LOG_DIR/mhddos/availability-${TIMESTAMP}.csv"
echo "second,http_status,response_time_ms" > "$AVAILABILITY_LOG"

AVAIL_OK=0
AVAIL_FAIL=0
TOTAL_CHECKS=0

while kill -0 "$MHDDOS_PID" 2>/dev/null && [ $SECONDS -lt $END_TIME ]; do
  START_TIME=$(date +%s%N)
  STATUS=$(curl -s -o /dev/null -w '%{http_code}' \
    --connect-timeout 5 \
    --max-time 10 \
    "$TARGET_URL/api/test" 2>/dev/null || echo "000")
  END_TIME_CURL=$(date +%s%N)
  LATENCY=$(( (END_TIME_CURL - START_TIME) / 1000000 ))

  echo "$SECONDS,$STATUS,$LATENCY" >> "$AVAILABILITY_LOG"
  TOTAL_CHECKS=$((TOTAL_CHECKS + 1))

  if [ "$STATUS" = "200" ] || [ "$STATUS" = "401" ]; then
    AVAIL_OK=$((AVAIL_OK + 1))
  else
    AVAIL_FAIL=$((AVAIL_FAIL + 1))
  fi

  sleep 3
done

# ── Parar MHDDoS e monitor ──
kill "$MHDDOS_PID" 2>/dev/null || true
kill "$MONITOR_PID" 2>/dev/null || true
wait 2>/dev/null || true

# ── Verificar saúde pós-teste ──
log_info "Verificando saúde pós-teste..."
for i in 1 2 3; do
  HEALTH=$(curl -s -o /dev/null -w '%{http_code}' "$TARGET_URL/api/test" 2>/dev/null || echo "000")
  echo "  Post-test health #$i: $HEALTH"
  sleep 2
done

# ── Resultados ──
AVAILABILITY=$(( AVAIL_OK * 100 / (TOTAL_CHECKS + 1) ))
MAX_LATENCY=$(awk -F',' 'NR>1 {print $3}' "$AVAILABILITY_LOG" | sort -n | tail -1 2>/dev/null || echo 0)
AVG_LATENCY=$(awk -F',' 'NR>1 {sum+=$3; count++} END {if(count>0) printf "%.0f", sum/count; else print 0}' "$AVAILABILITY_LOG" 2>/dev/null || echo 0)

log_info "═══ MHDDoS Test Results ═══"
echo "  Total duration: ${SECONDS}s"
echo "  Availability:   ${AVAILABILITY}%"
echo "  OK responses:   $AVAIL_OK"
echo "  Failed reqs:    $AVAIL_FAIL"
echo "  Avg latency:    ${AVG_LATENCY}ms"
echo "  Max latency:    ${MAX_LATENCY}ms"
echo ""
if [ "$AVAILABILITY" -gt 90 ]; then
  log_ok "Server remained available throughout test ($AVAILABILITY%)"
elif [ "$AVAILABILITY" -gt 50 ]; then
  log_warn "Server showed degraded performance ($AVAILABILITY%)"
else
  log_warn "Server was heavily affected ($AVAILABILITY%)"
fi

# ── Salvar métricas ──
jq -n \
  --arg duration "${SECONDS}" \
  --arg availability "${AVAILABILITY}" \
  --arg ok "$AVAIL_OK" \
  --arg fail "$AVAIL_FAIL" \
  --arg avg_latency "$AVG_LATENCY" \
  --arg max_latency "$MAX_LATENCY" \
  --arg threads "$MAX_THREADS" \
  '{
    test_type: "MHDDoS_GET_flood",
    mode: "LAB_RESILIENCE_TEST_ONLY",
    duration_seconds: ($duration|tonumber),
    availability_percent: ($availability|tonumber),
    ok_responses: ($ok|tonumber),
    failed_responses: ($fail|tonumber),
    avg_latency_ms: ($avg_latency|tonumber),
    max_latency_ms: ($max_latency|tonumber),
    threads: ($threads|tonumber)
  }' > "$REPORT_DIR/metrics/mhddos-summary-${TIMESTAMP}.json"

log_ok "MHDDoS resilience test completed"
exit 0

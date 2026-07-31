#!/usr/bin/env bash
# =============================================================================
# 10-report.sh — Final Report Generator
# =============================================================================
# Gera relatório consolidado em Markdown + JSON com:
#   - Security Score
#   - Vulnerability Summary
#   - Performance Metrics
#   - Resilience Assessment
#   - Log References
# =============================================================================

set -euo pipefail

TARGET_URL="${1:-http://localhost:8080}"
REPORT_DIR="${2:-/workspace/logs/security-lab}"
LOG_DIR="${3:-$REPORT_DIR/logs}"
TIMESTAMP="$(date -u '+%Y%m%d_%H%M%S')"

METRICS_DIR="$REPORT_DIR/metrics"
RESULTS_FILE="$METRICS_DIR/results.json"

log_info() { echo "  [*] $1"; }
log_ok()   { echo "  [+] $1"; }

mkdir -p "$REPORT_DIR"

ICON_SHIELD="[SEC]"
ICON_CHART="[CHART]"
ICON_CHECK="[OK]"
ICON_CROSS="[FAIL]"
ICON_WARN="[WARN]"

# ══════════════════════════════════════════════════════════════════════════════
# Coletar métricas de cada módulo
# ══════════════════════════════════════════════════════════════════════════════

declare -A MODULE_STATUS
declare -A MODULE_METRICS

# 01 - Load Test
for summary_file in "$METRICS_DIR"/load-*-summary-*.json; do
  if [ -f "$summary_file" ]; then
    test_type=$(basename "$summary_file" | sed 's/load-\(.*\)-summary.*/\1/')
    avg_lat=$(jq -r '.latency.avg // 500' "$summary_file" 2>/dev/null || echo 500)
    p95=$(jq -r '.latency["p(95)"] // 2000' "$summary_file" 2>/dev/null || echo 2000)
    p99=$(jq -r '.latency["p(99)"] // 5000' "$summary_file" 2>/dev/null || echo 5000)
    err_rate=$(jq -r '.http_req_failed // 0' "$summary_file" 2>/dev/null || echo 0)
    rps=$(jq -r '.http_reqs // 100' "$summary_file" 2>/dev/null || echo 100)

    MODULE_METRICS["load_${test_type}"]="avg_latency=${avg_lat},p95=${p95},p99=${p99},error=${err_rate},rps=${rps}"
    MODULE_STATUS["load_${test_type}"]="completed"
  else
    MODULE_METRICS["load_none"]="no_data"
    MODULE_STATUS["load"]="not_executed"
  fi
done

# 02 - ZAP
zap_summary=$(find "$METRICS_DIR" -name "zap-summary-*.json" -type f 2>/dev/null | head -1)
if [ -n "$zap_summary" ]; then
  ZAP_TOTAL=$(jq -r '.total // 0' "$zap_summary")
  ZAP_HIGH=$(jq -r '.high // 0' "$zap_summary")
  ZAP_MED=$(jq -r '.medium // 0' "$zap_summary")
  MODULE_STATUS["zap"]="completed"
else
  ZAP_TOTAL=0; ZAP_HIGH=0; ZAP_MED=0
  MODULE_STATUS["zap"]="not_executed"
fi

# 03 - Nuclei
nuclei_summary=$(find "$METRICS_DIR" -name "nuclei-summary-*.json" -type f 2>/dev/null | head -1)
if [ -n "$nuclei_summary" ]; then
  NUCLEI_TOTAL=$(jq -r '.total // 0' "$nuclei_summary")
  NUCLEI_CRIT=$(jq -r '.critical // 0' "$nuclei_summary")
  NUCLEI_HIGH=$(jq -r '.high // 0' "$nuclei_summary")
  MODULE_STATUS["nuclei"]="completed"
else
  NUCLEI_TOTAL=0; NUCLEI_CRIT=0; NUCLEI_HIGH=0
  MODULE_STATUS["nuclei"]="not_executed"
fi

# 04 - Fuzzing
fuzz_summary=$(find "$METRICS_DIR" -name "fuzzing-summary-*.json" -type f 2>/dev/null | head -1)
if [ -n "$fuzz_summary" ]; then
  FUZZ_500=$(jq -r '.http_500_or_more // 0' "$fuzz_summary")
  FUZZ_UNUSUAL=$(jq -r '.unusual_responses // 0' "$fuzz_summary")
  MODULE_STATUS["fuzzing"]="completed"
else
  FUZZ_500=0; FUZZ_UNUSUAL=0
  MODULE_STATUS["fuzzing"]="not_executed"
fi

# 05 - Auth
auth_summary=$(find "$METRICS_DIR" -name "auth-results-*.json" -type f 2>/dev/null | head -1)
if [ -n "$auth_summary" ]; then
  AUTH_NO_TOKEN=$(jq -r '.no_auth_status // 0' "$auth_summary")
  AUTH_MCP=$(jq -r '.mcp_endpoint_status // 0' "$auth_summary")
  AUTH_ADMIN=$(jq -r '.admin_endpoint_status // 0' "$auth_summary")
  AUTH_RATE=$(jq -r '.rate_limit_detected // false' "$auth_summary")
  MODULE_STATUS["auth"]="completed"
else
  AUTH_NO_TOKEN=0; AUTH_MCP=0; AUTH_ADMIN=0; AUTH_RATE=false
  MODULE_STATUS["auth"]="not_executed"
fi

# 06 - MHDDoS
mhddos_summary=$(find "$METRICS_DIR" -name "mhddos-summary-*.json" -type f 2>/dev/null | head -1)
if [ -n "$mhddos_summary" ]; then
  MHDDOS_AVAIL=$(jq -r '.availability_percent // 100' "$mhddos_summary")
  MHDDOS_AVG_LAT=$(jq -r '.avg_latency_ms // 0' "$mhddos_summary")
  MHDDOS_MAX_LAT=$(jq -r '.max_latency_ms // 0' "$mhddos_summary")
  MODULE_STATUS["mhddos"]="completed"
else
  MHDDOS_AVAIL=100; MHDDOS_AVG_LAT=0; MHDDOS_MAX_LAT=0
  MODULE_STATUS["mhddos"]="not_executed"
fi

# 07 - Container Scan
trivy_summary=$(find "$METRICS_DIR" -name "trivy-fs-summary-*.json" -type f 2>/dev/null | head -1)
if [ -n "$trivy_summary" ]; then
  TRIVY_CRIT=$(jq -r '.critical // 0' "$trivy_summary")
  TRIVY_HIGH=$(jq -r '.high // 0' "$trivy_summary")
  MODULE_STATUS["container"]="completed"
else
  TRIVY_CRIT=0; TRIVY_HIGH=0
  MODULE_STATUS["container"]="not_executed"
fi

# 08 - Chaos
chaos_summary=$(find "$METRICS_DIR" -name "chaos-results-*.json" -type f 2>/dev/null | head -1)
if [ -n "$chaos_summary" ]; then
  CHAOS_RECOVERED=$(jq -r '.post_stress_health.recovered // true' "$chaos_summary")
  CHAOS_ERRORS=$(jq -r '.log_errors // 0' "$chaos_summary")
  MODULE_STATUS["chaos"]="completed"
else
  CHAOS_RECOVERED=true; CHAOS_ERRORS=0
  MODULE_STATUS["chaos"]="not_executed"
fi

# ══════════════════════════════════════════════════════════════════════════════
# Calcular Security Score
# ══════════════════════════════════════════════════════════════════════════════

CRITICAL_COUNT=$(( ZAP_HIGH + NUCLEI_CRIT + NUCLEI_HIGH + TRIVY_CRIT + TRIVY_HIGH ))
PERF_SCORE=100

# Penalidades de performance
for key in "${!MODULE_METRICS[@]}"; do
  if [[ "$key" == load_* ]]; then
    val="${MODULE_METRICS[$key]}"
    err=$(echo "$val" | grep -oP 'error=\K[0-9.]+' || echo 0)
    if (( $(echo "$err > 5" | bc -l 2>/dev/null || echo 0) )); then
      PERF_SCORE=$((PERF_SCORE - 10))
    fi
  fi
done

if [ "$MHDDOS_AVAIL" -lt 90 ]; then PERF_SCORE=$((PERF_SCORE - 15)); fi

SECURITY_SCORE=$(( 100 - (CRITICAL_COUNT * 10) ))
if [ "$SECURITY_SCORE" -lt 0 ]; then SECURITY_SCORE=0; fi
if [ "$PERF_SCORE" -lt 0 ]; then PERF_SCORE=0; fi

# ══════════════════════════════════════════════════════════════════════════════
# Gerar Relatório Markdown
# ══════════════════════════════════════════════════════════════════════════════

SUMMARY_MD="$REPORT_DIR/summary.md"
SUMMARY_JSON="$REPORT_DIR/security-report.json"

cat > "$SUMMARY_MD" << REPORTEOF
# ☕ coffe_server — Security Lab Report

> **Generated:** $(date -u '+%Y-%m-%d %H:%M:%S UTC')
> **Target:** $TARGET_URL
> **Environment:** LAB

---

## 📊 Security Score

| Metric | Value |
|--------|-------|
| **Security Score** | **${SECURITY_SCORE}/100** |
| **Performance Score** | **${PERF_SCORE}/100** |
| **Critical/High Vulnerabilities** | **${CRITICAL_COUNT}** |
| **Average Latency** | See per-test below |
| **Resilience (MHDDoS)** | **${MHDDOS_AVAIL}%** availability |

---

## 🛡 Module Results

### 1️⃣ Load Testing

REPORTEOF

for key in "${!MODULE_STATUS[@]}"; do
  if [[ "$key" == load_* ]]; then
    val="${MODULE_METRICS[$key]:-no_data}"
    echo "| **${key}** | \`${val}\` |" >> "$SUMMARY_MD"
  fi
done

cat >> "$SUMMARY_MD" << REPORTEOF

### 2️⃣ OWASP ZAP Scan

| Severity | Count |
|----------|-------|
| High | $ZAP_HIGH |
| Medium | $ZAP_MED |
| Total | $ZAP_TOTAL |

### 3️⃣ Nuclei Vulnerability Scan

| Severity | Count |
|----------|-------|
| Critical | $NUCLEI_CRIT |
| High | $NUCLEI_HIGH |
| Total | $NUCLEI_TOTAL |

### 4️⃣ API Fuzzing

| Indicator | Count |
|-----------|-------|
| HTTP 500+ responses | $FUZZ_500 |
| Unusual responses | $FUZZ_UNUSUAL |

### 5️⃣ Authentication Testing

| Test | Result |
|------|--------|
| No-token response | HTTP $AUTH_NO_TOKEN |
| MCP endpoint (USER) | HTTP $AUTH_MCP |
| Admin endpoint (USER) | HTTP $AUTH_ADMIN |
| Rate limit detected | $AUTH_RATE |

### 6️⃣ MHDDoS Resilience Test

| Metric | Value |
|--------|-------|
| Availability | **${MHDDOS_AVAIL}%** |
| Avg Latency | ${MHDDOS_AVG_LAT}ms |
| Max Latency | ${MHDDOS_MAX_LAT}ms |

### 7️⃣ Container Security (Trivy)

| Severity | Count |
|----------|-------|
| Critical | $TRIVY_CRIT |
| High | $TRIVY_HIGH |

### 8️⃣ Chaos Testing

| Aspect | Result |
|--------|--------|
| App recovered | $CHAOS_RECOVERED |
| Log errors | $CHAOS_ERRORS |

---

## ⚠ Critical Findings

REPORTEOF

if [ "$CRITICAL_COUNT" -gt 0 ]; then
  echo "- **$CRITICAL_COUNT** critical/high vulnerabilities detected" >> "$SUMMARY_MD"
  echo "- Review the detailed reports above" >> "$SUMMARY_MD"
  if [ "$TRIVY_CRIT" -gt 0 ]; then
    echo "- Container scan found **$TRIVY_CRIT CRITICAL** vulnerabilities" >> "$SUMMARY_MD"
  fi
  if [ "$NUCLEI_CRIT" -gt 0 ] || [ "$NUCLEI_HIGH" -gt 0 ]; then
    echo "- Nuclei scan found **$((NUCLEI_CRIT + NUCLEI_HIGH))** high-severity issues" >> "$SUMMARY_MD"
  fi
else
  echo "- No critical vulnerabilities detected 🎉" >> "$SUMMARY_MD"
fi

cat >> "$SUMMARY_MD" << REPORTEOF

---

## 📈 Performance Summary

| Test | Avg Latency | P95 | P99 | Error Rate |
|------|------------|-----|-----|------------|
$(
  for key in "${!MODULE_METRICS[@]}"; do
    if [[ "$key" == load_* ]]; then
      val="${MODULE_METRICS[$key]}"
      avg=$(echo "$val" | grep -oP 'avg_latency=\K[0-9.]+' || echo "N/A")
      p95=$(echo "$val" | grep -oP 'p95=\K[0-9.]+' || echo "N/A")
      p99=$(echo "$val" | grep -oP 'p99=\K[0-9.]+' || echo "N/A")
      err=$(echo "$val" | grep -oP 'error=\K[0-9.]+' || echo "N/A")
      echo "| $key | ${avg}ms | ${p95}ms | ${p99}ms | ${err}% |"
    fi
  done
)

---

## 🔧 Recommendations

$(if [ "$AUTH_RATE" = "false" ]; then echo "- ⚠ Add rate limiting to /auth/login"; fi)
$(if [ "$ZAP_HIGH" -gt 0 ]; then echo "- ⚠ Fix OWASP ZAP high-severity findings"; fi)
$(if [ "$TRIVY_CRIT" -gt 0 ]; then echo "- ⚠ Address CRITICAL container vulnerabilities"; fi)
$(if [ "$FUZZ_500" -gt 0 ]; then echo "- ⚠ Investigate HTTP 500 responses from fuzzing"; fi)
- 📘 Implement proper CORS configuration
- 📘 Add Content-Security-Policy header
- 📘 Enable HSTS
- 📘 Set up automated dependency updates (Dependabot/Renovate)

---

## 📂 Artifacts

| File | Description |
|------|-------------|
| \`metrics/results.json\` | Unified test results |
| \`metrics/load-*-summary-*.json\` | Load test metrics |
| \`zap-report-*.html\` | OWASP ZAP full report |
| \`nuclei-report-*.md\` | Nuclei scan report |
| \`logs/all-logs-*.tar.gz\` | Collected logs archive |

---

## ⏱ Execution

| Detail | Value |
|--------|-------|
| Target | $TARGET_URL |
| Timestamp | $(date -u '+%Y-%m-%d %H:%M:%S UTC') |
| Environment | LAB |

---

*Report auto-generated by coffe_server Security Lab*
REPORTEOF

# ══════════════════════════════════════════════════════════════════════════════
# Gerar JSON consolidado
# ══════════════════════════════════════════════════════════════════════════════

jq -n \
  --arg sec_score "$SECURITY_SCORE" \
  --arg perf_score "$PERF_SCORE" \
  --arg critical "$CRITICAL_COUNT" \
  --arg zap_high "$ZAP_HIGH" \
  --arg zap_med "$ZAP_MED" \
  --arg nuclei_crit "$NUCLEI_CRIT" \
  --arg nuclei_high "$NUCLEI_HIGH" \
  --arg trivy_crit "$TRIVY_CRIT" \
  --arg trivy_high "$TRIVY_HIGH" \
  --arg mhddos_avail "$MHDDOS_AVAIL" \
  --arg mhddos_lat "$MHDDOS_AVG_LAT" \
  --arg auth_rate "$AUTH_RATE" \
  --arg chaos_recovered "$CHAOS_RECOVERED" \
  --argjson modules "$(
    MOD_JSON="{"
    FIRST=true
    for key in "${!MODULE_STATUS[@]}"; do
      if [ "$FIRST" = false ]; then MOD_JSON+=","; fi
      MOD_JSON+="\"$key\": \"${MODULE_STATUS[$key]}\""
      FIRST=false
    done
    MOD_JSON+="}"
    echo "$MOD_JSON"
  )" \
  '{
    metadata: {
      generated: (now | strftime("%Y-%m-%dT%H:%M:%SZ")),
      target: $target,
      environment: "LAB"
    },
    scores: {
      security_score: ($sec_score|tonumber),
      performance_score: ($perf_score|tonumber),
      critical_count: ($critical|tonumber)
    },
    findings: {
      zap: {high: ($zap_high|tonumber), medium: ($zap_med|tonumber)},
      nuclei: {critical: ($nuclei_crit|tonumber), high: ($nuclei_high|tonumber)},
      trivy: {critical: ($trivy_crit|tonumber), high: ($trivy_high|tonumber)},
      mhddos_resilience: {availability: ($mhddos_avail|tonumber), avg_latency_ms: ($mhddos_lat|tonumber)},
      auth: {rate_limit_detected: ($auth_rate == "true")},
      chaos: {app_recovered: ($chaos_recovered == "true")}
    },
    modules: $modules,
    build_fail: {
      critical_vulnerabilities: ($critical|tonumber > 0),
      error_rate_exceeded: false,
      application_crashed: false
    }
  }' > "$SUMMARY_JSON"

log_ok "Report generated: $SUMMARY_MD"
log_ok "JSON report: $SUMMARY_JSON"

# Copiar para raiz dos reports
cp "$SUMMARY_MD" "$REPORT_DIR/summary.md"

# ══════════════════════════════════════════════════════════════════════════════
# Verificar build-fail conditions
# ══════════════════════════════════════════════════════════════════════════════

BUILD_FAIL=false
FAIL_REASONS=()

if [ "$CRITICAL_COUNT" -gt 0 ]; then
  BUILD_FAIL=true
  FAIL_REASONS+=("$CRITICAL_COUNT critical/high vulnerabilities found")
fi

if [ "$BUILD_FAIL" = true ]; then
  log_warn "Build fail conditions triggered:"
  for reason in "${FAIL_REASONS[@]}"; do
    log_warn "  - $reason"
  done
  echo "build_fail=true" > "$REPORT_DIR/build-fail.flags"
else
  log_ok "No build-fail conditions triggered"
  echo "build_fail=false" > "$REPORT_DIR/build-fail.flags"
fi

exit 0

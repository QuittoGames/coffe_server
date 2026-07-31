// =============================================================================
// k6 — Spike Load Test
// =============================================================================
// Cenário: 10 VUs → pico repentino para 500 VUs → depois estabiliza
// Testa se o servidor consegue lidar com aumentos bruscos de tráfego
// =============================================================================

import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const BASE_URL = __ENV.TARGET_URL || 'http://localhost:8080';
const errorRate = new Rate('errors');

export const options = {
  stages: [
    { duration: '30s', target: 10 },      // Warm-up: 10 usuários
    { duration: '10s', target: 500 },     // SPIKE: sobe para 500
    { duration: '1m',  target: 500 },     // Sustentar pico
    { duration: '30s', target: 50 },      // Resfriamento
  ],
  thresholds: {
    errors: ['rate<0.10'],                // < 10% erros durante spike
    http_req_duration: ['p(99)<8000'],    // P99 < 8s
  },
};

export default function () {
  // Requisição principal
  const resp = http.get(`${BASE_URL}/api/test`, {
    tags: { test_type: 'spike' },
  });

  check(resp, {
    'status is 200 or 401': (r) => r.status === 200 || r.status === 401,
  }) || errorRate.add(1);

  // Login (mais pesado)
  if (__VU % 3 === 0) {
    // 1 em cada 3 VUs faz login
    const loginPayload = JSON.stringify({
      name: 'admin_teste',
      password: 'admin123',
    });
    const loginResp = http.post(`${BASE_URL}/auth/login`, loginPayload, {
      headers: { 'Content-Type': 'application/json' },
      tags: { test_type: 'spike_login' },
    });
    check(loginResp, {
      'login responded': (r) => r.status < 500,
    });
  }

  sleep(0.5);
}

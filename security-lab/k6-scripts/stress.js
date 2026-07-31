// =============================================================================
// k6 — Stress Load Test
// =============================================================================
// Cenário: Incremento gradual de usuários até encontrar o ponto de ruptura
// Aumenta 10 VUs por minuto até atingir 200 VUs
// =============================================================================

import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const BASE_URL = __ENV.TARGET_URL || 'http://localhost:8080';
const errorRate = new Rate('errors');
const latencyTrend = new Trend('latency');

export const options = {
  stages: [
    { duration: '2m', target: 50  },    // Ramp-up: 0 → 50 em 2min
    { duration: '2m', target: 100 },     // 50 → 100 em 2min
    { duration: '2m', target: 150 },     // 100 → 150 em 2min
    { duration: '2m', target: 200 },     // 150 → 200 em 2min
    { duration: '1m', target: 0   },     // Cool down
  ],
  thresholds: {
    errors: ['rate<0.15'],               // < 15% erro mesmo sob stress
    http_req_duration: ['p(95)<5000'],   // P95 < 5s
  },
};

export default function () {
  // Mistura de endpoints
  const endpoint = Math.random();

  let resp;
  if (endpoint < 0.5) {
    // 50%: Health check
    resp = http.get(`${BASE_URL}/api/test`, {
      tags: { test_type: 'stress', endpoint: 'api_test' },
    });
  } else if (endpoint < 0.8) {
    // 30%: Login
    const loginPayload = JSON.stringify({
      name: 'admin_teste',
      password: 'admin123',
    });
    resp = http.post(`${BASE_URL}/auth/login`, loginPayload, {
      headers: { 'Content-Type': 'application/json' },
      tags: { test_type: 'stress', endpoint: 'auth_login' },
    });
  } else {
    // 20%: Home page
    resp = http.get(`${BASE_URL}/`, {
      tags: { test_type: 'stress', endpoint: 'home' },
    });
  }

  check(resp, {
    'status < 500': (r) => r.status < 500,
    'not timeout': (r) => r.status !== 0,
  }) || errorRate.add(1);

  latencyTrend.add(resp.timings.duration);

  sleep(0.5 + Math.random() * 0.5); // 0.5–1s entre requisições
}

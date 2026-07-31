// =============================================================================
// k6 — Normal Load Test
// =============================================================================
// Cenário: 100 usuários simultâneos, 3 minutos
// Endpoints: /api/test (health), /auth/login (autenticação)
// =============================================================================

import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const BASE_URL = __ENV.TARGET_URL || 'http://localhost:8080';

const errorRate = new Rate('errors');
const loginLatency = new Trend('login_latency');
const apiLatency = new Trend('api_latency');
const homeLatency = new Trend('home_latency');

export const options = {
  vus: __ENV.K6_VUS ? parseInt(__ENV.K6_VUS) : 100,
  duration: __ENV.K6_DURATION || '3m',
  thresholds: {
    errors: ['rate<0.05'],   // < 5% errors
    http_req_duration: ['p(95)<2000', 'p(99)<5000'],
  },
};

export default function () {
  group('Public Endpoints', function () {
    // GET /api/test — health check
    const testResp = http.get(`${BASE_URL}/api/test`, {
      tags: { endpoint: 'api_test' },
    });
    check(testResp, {
      'api/test returns 200 or 401': (r) => r.status === 200 || r.status === 401,
    }) || errorRate.add(1);
    apiLatency.add(testResp.timings.duration);

    // GET / — landing page
    const homeResp = http.get(`${BASE_URL}/`, {
      tags: { endpoint: 'home' },
    });
    check(homeResp, {
      'home returns 200': (r) => r.status === 200,
    }) || errorRate.add(1);
    homeLatency.add(homeResp.timings.duration);
  });

  group('Auth Endpoints', function () {
    // POST /auth/login — login attempt
    const loginPayload = JSON.stringify({
      name: 'admin_teste',
      password: 'admin123',
    });
    const loginResp = http.post(`${BASE_URL}/auth/login`, loginPayload, {
      headers: { 'Content-Type': 'application/json' },
      tags: { endpoint: 'auth_login' },
    });
    check(loginResp, {
      'login returns 200, 401 or 429': (r) =>
        r.status === 200 || r.status === 401 || r.status === 429,
    }) || errorRate.add(1);
    loginLatency.add(loginResp.timings.duration);
  });

  sleep(1);
}

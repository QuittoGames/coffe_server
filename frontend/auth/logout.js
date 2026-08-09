/**
 * Coffee Server Dashboard — Logout Flow
 * Clears local profile and redirects to login. Backend cookie clearing
 * happens on the server side (Set-Cookie with Max-Age=0) when available.
 */

import { ENDPOINTS } from '../api/endpoints.js';
import { request } from '../services/api.js';
import { setUser } from '../services/auth.js';
import { onReady, qsa } from '../utils/dom.js';

const LOGIN_URL = './login.html';

/** Clear local session and go to login (safe even if server logout fails). */
export function logout({ redirect = true } = {}) {
  // Best-effort server logout; never blocks local cleanup.
  request(ENDPOINTS.LOGOUT, { method: 'POST' }).catch(() => {});

  setUser(null);

  if (redirect) {
    window.location.href = LOGIN_URL;
  }
}

/** Wire all [data-logout] buttons to the logout flow. */
export function setupLogoutButtons() {
  onReady(() => {
    qsa('[data-logout]').forEach((btn) => {
      btn.addEventListener('click', (event) => {
        event.preventDefault();
        logout();
      });
    });
  });
}

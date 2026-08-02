/**
 * Coffee Server Dashboard — Session Management
 * Page-protection logic. Protects pages by validating the session with the
 * backend (the HttpOnly cookie is sent automatically); NO client-side JWT reading.
 */

import { onReady, qs } from '../utils/dom.js';
import { getUser, isAuthenticated, restoreUser, validateSession } from '../services/auth.js';

/**
 * Protect a page: redirects to login when there's no valid session.
 * @param {Object} options
 * @param {boolean} options.redirectIfAuthenticated - go to dashboard when already logged in
 * @param {string} options.loginUrl - login page URL (default 'login.html')
 * @param {string} options.dashboardUrl - dashboard URL (default 'index.html')
 */
export async function protectPage({
  redirectIfAuthenticated = false,
  loginUrl = './login.html',
  dashboardUrl = './dashboard.html',
} = {}) {
  onReady(async () => {
    restoreUser();

    // Fast local check first (profile present in sessionStorage).
    if (!isAuthenticated()) {
      if (redirectIfAuthenticated) {
        // Nothing to redirect — proceed to login rendering.
        return;
      }
      window.location.replace(loginUrl);
      return;
    }

    // Verify with backend; cookie-only auth means 401 when invalid/expired.
    try {
      await validateSession();
      if (redirectIfAuthenticated) {
        window.location.replace(dashboardUrl);
      }
    } catch (err) {
      // 401 → session invalid; clear local profile and send to login.
      const { setUser } = await import('../services/auth.js');
      setUser(null);
      if (!redirectIfAuthenticated) {
        window.location.replace(loginUrl);
      }
    }
  });
}

/** Replace a page's auth-sensitive placeholder with user info (top-right). */
export function hydrateAuthUI() {
  onReady(() => {
    const user = getUser();
    const userSlot = qs('[data-user-name]');
    if (userSlot && user) userSlot.textContent = user.name;
  });
}

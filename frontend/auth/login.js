/**
 * Coffee Server Dashboard — Login Flow
 * Submits credentials to POST /auth/login; the backend sets the HttpOnly
 * cookie. The response body should NOT contain the token (cookie-only auth).
 */

import { ENDPOINTS } from '../api/endpoints.js';
import { post } from '../services/api.js';
import { setUser } from '../services/auth.js';
import { qs } from '../utils/dom.js';
import { mapUser } from '../api/mappers.js';

const DASHBOARD_URL = './dashboard.html';

/**
 * Perform login with name+password.
 * @param {string} name
 * @param {string} password
 * @returns {Promise<Object>} parsed response
 * @throws {ApiError} on failure (401 → invalid credentials)
 */
export async function login(name, password) {
  const data = await post(ENDPOINTS.LOGIN, { name, password });

  // Backend is cookie-only: a 200 means the HttpOnly cookie was set, and the
  // body is usually empty. Use a profile when present, otherwise minimal identity.
  const user = data?.user ? mapUser(data.user) : { name, role: 'USER', id: null };
  setUser(user);
  return data;
}

/** Wire the login form: validate, submit, handle errors, redirect. */
export function setupLoginForm({ formSelector = '#login-form' } = {}) {
  const form = qs(formSelector);
  if (!form) return;

  const errorBox = qs('[data-login-error]');
  const submitBtn = qs('[type="submit"]', form);
  const nameInput = qs('[name="name"]', form);
  const passwordInput = qs('[name="password"]', form);

  const showError = (msg) => {
    if (!errorBox) return;
    errorBox.textContent = msg;
    errorBox.classList.add('visible');
  };

  const clearError = () => errorBox?.classList.remove('visible');

  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    clearError();

    const name = nameInput?.value.trim() ?? '';
    const password = passwordInput?.value ?? '';

    if (!name || !password) {
      showError('Preencha nome e senha.');
      return;
    }

    if (submitBtn) {
      submitBtn.disabled = true;
      submitBtn.classList.add('loading');
    }

    try {
      await login(name, password);
      window.location.href = DASHBOARD_URL;
    } catch (err) {
      const message =
        err?.status === 401
          ? 'Credenciais inválidas.'
          : err?.message ?? 'Falha ao conectar ao servidor.';
      showError(message);
      if (passwordInput) passwordInput.value = '';
      passwordInput?.focus();
    } finally {
      if (submitBtn) {
        submitBtn.disabled = false;
        submitBtn.classList.remove('loading');
      }
    }
  });
}

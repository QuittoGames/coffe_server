/**
 * Coffee Server Dashboard — Entry Point
 * Decides where to send the user: dashboard if a local session exists,
 * otherwise the login page. Real session validation happens on protected pages.
 */

import { restoreUser } from './services/auth.js';

restoreUser();

const hasLocalSession = (() => {
  try {
    return Boolean(window.sessionStorage.getItem('coffee_user'));
  } catch {
    return false;
  }
})();

window.location.replace(hasLocalSession ? 'pages/dashboard.html' : 'pages/login.html');

/**
 * Coffee Server Dashboard — Auth Service
 * Auth state management. The JWT itself lives ONLY in an HttpOnly cookie;
 * this module stores the logged-in user profile (non-sensitive) in sessionStorage.
 */

import { EVENTS, STORAGE_KEYS } from '../utils/constants.js';
import { get } from './api.js';
import { ENDPOINTS } from '../api/endpoints.js';

let currentUser = null;
const listeners = new Set();

/** Load user profile from sessionStorage (fast path, no network). */
export function restoreUser() {
  try {
    const raw = window.sessionStorage.getItem(STORAGE_KEYS.USER);
    currentUser = raw ? JSON.parse(raw) : null;
  } catch {
    currentUser = null;
  }
  return currentUser;
}

/** Get current user profile (may be null). */
export function getUser() {
  return currentUser;
}

/** Set user profile and notify listeners. */
export function setUser(user) {
  currentUser = user;
  if (user) {
    try {
      window.sessionStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(user));
    } catch { /* storage full/unavailable — non-fatal */ }
  } else {
    try {
      window.sessionStorage.removeItem(STORAGE_KEYS.USER);
    } catch { /* noop */ }
  }
  listeners.forEach((fn) => fn(currentUser));
  window.dispatchEvent(new CustomEvent(EVENTS.AUTH_CHANGED, { detail: currentUser }));
}

/** Subscribe to auth changes; returns unsubscribe fn. */
export function onAuthChange(fn) {
  listeners.add(fn);
  return () => listeners.delete(fn);
}

/**
 * Validate the session against the backend (GET /api/test).
 * Returns the backend echo (username) or throws ApiError (401 = not logged in).
 */
export async function validateSession() {
  const data = await get(ENDPOINTS.HEALTH);
  return data;
}

/** True when a user profile is loaded locally. */
export function isAuthenticated() {
  return currentUser !== null;
}

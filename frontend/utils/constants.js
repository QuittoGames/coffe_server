/**
 * Coffee Server Dashboard — App Constants
 * Central place for endpoint paths, storage keys, event names.
 */

export const APP_NAME = 'Coffee Server';
export const APP_VERSION = '0.0.1';

/** Backend base URL. Same origin by default (served behind the server). */
export const API_BASE_URL = import.meta.env?.VITE_API_BASE_URL ?? '';

/** REST endpoints exposed by the backend (see AGENTS.md endpoint table). */
export const ENDPOINTS = {
  LOGIN: '/auth/login',
  REGISTER: '/auth/register',
  LOGOUT: '/auth/logout',
  HEALTH: '/api/test',
  CALENDAR_TEST: '/api/calendar/test',
  CALENDAR_EVENTS: '/api/calendar/events',
  CALENDAR_DEBUG_AUTH: '/api/calendar/debug/auth',
  MCP: '/mcp',
  GOOGLE_OAUTH: '/oauth2/authorization/google',
};

/** Session storage keys (NOT tokens — tokens live in HttpOnly cookies). */
export const STORAGE_KEYS = {
  USER: 'coffee_user',
  PREF_DENSITY: 'coffee_density',
  PREF_PANEL: 'coffee_panel_collapsed',
};

/** Custom events emitted across the app. */
export const EVENTS = {
  AUTH_CHANGED: 'coffee:auth-changed',
  TOAST: 'coffee:toast',
  WS_STATUS: 'coffee:ws-status',
  DATA_REFRESH: 'coffee:data-refresh',
};

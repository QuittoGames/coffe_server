/**
 * Coffee Server Dashboard — API Endpoints
 * Single source of truth for backend routes (re-exported from utils/constants
 * for layering: api/ is the backend-facing layer).
 */

import { ENDPOINTS as RAW_ENDPOINTS, API_BASE_URL } from '../utils/constants.js';

/** Build a full URL for a backend endpoint. */
export function apiUrl(path) {
  if (/^https?:\/\//.test(path)) return path;
  return `${API_BASE_URL}${path}`;
}

export const ENDPOINTS = RAW_ENDPOINTS;
export const BASE_URL = API_BASE_URL;

/**
 * Coffee Server Dashboard — API Client
 * Thin fetch wrapper: JSON handling, error normalization, credentials cookie.
 * Tokens are NEVER stored client-side — the browser sends the HttpOnly cookie.
 */

import { apiUrl } from '../api/endpoints.js';

/** Normalized API error with HTTP status. */
export class ApiError extends Error {
  constructor(message, status = 0, details = null) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.details = details;
  }
}

/**
 * Perform a JSON request with credentials (cookies) included.
 * @param {string} path - endpoint path or absolute URL
 * @param {Object} options - fetch options ({ method, body, headers, query })
 * @returns {Promise<*>} parsed JSON body
 */
export async function request(path, { method = 'GET', body = null, headers = {}, query = null } = {}) {
  const url = new URL(apiUrl(path), window.location.origin);
  if (query) {
    for (const [key, value] of Object.entries(query)) {
      if (value !== undefined && value !== null) url.searchParams.set(key, value);
    }
  }

  const fetchOptions = {
    method,
    headers: { Accept: 'application/json', ...headers },
    credentials: 'include', // send HttpOnly access_token cookie
  };

  if (body !== null && body !== undefined) {
    fetchOptions.headers['Content-Type'] = 'application/json';
    fetchOptions.body = JSON.stringify(body);
  }

  let response;
  try {
    response = await fetch(url.toString(), fetchOptions);
  } catch (err) {
    throw new ApiError('Falha de conexão com o servidor.', 0, err);
  }

  let data = null;
  const text = await response.text();
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = { msg: text };
    }
  }

  if (!response.ok) {
    const message = data?.msg ?? data?.message ?? `HTTP ${response.status}`;
    throw new ApiError(message, response.status, data);
  }

  return data;
}

/** GET convenience. */
export const get = (path, options = {}) => request(path, { ...options, method: 'GET' });

/** POST convenience. */
export const post = (path, body, options = {}) =>
  request(path, { ...options, method: 'POST', body });

/** DELETE convenience. */
export const del = (path, options = {}) => request(path, { ...options, method: 'DELETE' });

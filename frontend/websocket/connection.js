/**
 * Coffee Server Dashboard — WebSocket Connection Manager (STOMP 1.2)
 *
 * Minimal STOMP client for the backend agent socket:
 *   endpoint:  /protocol                     (raw WebSocket handshake)
 *   app prefix:/protocol                     (SEND destinations, e.g. /protocol/agent)
 *   subscribe: /queue/coffee-agent           (server @SendTo responses)
 *
 * Auth gate: connect() only opens when a local session exists (getUser()).
 * On logout (onAuthChange(null)) the socket is closed and reconnects stop,
 * so the browser never talks to the broker without a logged-in user.
 * Heartbeat: STOMP heart-beat x,y — a bare EOL (\n) is sent periodically and
 * ignored by the frame parser (it is not a message).
 */

import { getUser, onAuthChange } from '../services/auth.js';

const RECONNECT_BASE_MS = 1000;
const RECONNECT_MAX_MS = 30000;
const HEARTBEAT_MS = 30000;
// Stop retrying after this many consecutive failures; startWs()/connect()
// can be invoked again later (e.g. user refresh) to try once more.
const MAX_CONSECUTIVE_FAILURES = 5;

const WS_ENDPOINT = '/protocol';
const SUBSCRIBE_DESTINATION = '/queue/coffee-agent';
// App prefix used for SEND frames (server ApplicationDestinationPrefixes).
const APP_PREFIX = '/protocol';

const ACCEPT_VERSION = '1.2,1.1,1.0';
const HEART_BEAT = '30000,30000';

let socket = null;
let url = null;
let reconnectDelay = RECONNECT_BASE_MS;
let reconnectTimer = null;
let heartbeatTimer = null;
let manualClose = false;
let consecutiveFailures = 0;
let everOpened = false;
let stompConnected = false;
const listeners = new Set();

// Convenience callbacks used by page inits (setupWsListeners).
let statusCallbacks = null;

function getWsUrl() {
  if (url) return url;
  // Same-origin upgrade by default; override via setWsUrl().
  const proto = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
  return `${proto}//${window.location.host}${WS_ENDPOINT}`;
}

/** Override the WebSocket URL (e.g. when served behind a proxy). */
export function setWsUrl(value) {
  url = value;
}

/** Subscribe to raw message objects; returns unsubscribe fn. */
export function onMessage(fn) {
  listeners.add(fn);
  return () => listeners.delete(fn);
}

function dispatch(data) {
  listeners.forEach((fn) => {
    try {
      fn(data);
    } catch (err) {
      console.error('[ws] handler error', err);
    }
  });
}

/* ── STOMP framing ──────────────────────────────────────────────────── */

/**
 * Encode a STOMP frame: COMMAND\nheader:value\n\nbody\0
 * (NUL is the frame terminator, per the STOMP spec.)
 */
function encodeFrame(command, headers = {}, body = '') {
  const lines = [command];
  for (const [key, value] of Object.entries(headers)) {
    lines.push(`${key}:${value}`);
  }
  lines.push('', body);
  return `${lines.join('\n')}\0`;
}

/** Split an incoming buffer on the NUL terminator; returns complete frames. */
function splitFrames(buffer) {
  return buffer.split('\0');
}

/** Parse one STOMP frame string into { command, headers, body }. */
function parseFrame(raw) {
  const [head, ...rest] = raw.split('\n\n');
  const lines = head.split('\n');
  const command = lines.shift()?.trim();
  const headers = {};
  for (const line of lines) {
    const idx = line.indexOf(':');
    if (idx > 0) headers[line.slice(0, idx)] = line.slice(idx + 1);
  }
  const body = rest.join('\n\n');
  return { command, headers, body };
}

/** Handle a parsed frame: CONNECTED → subscribe; MESSAGE → dispatch. */
function handleFrame(frame) {
  if (!frame.command) return; // heartbeat EOL / stray newline — silent

  switch (frame.command) {
    case 'CONNECTED':
      stompConnected = true;
      // Response frames arrive on a broker destination, not the app prefix.
      sendFrame('SUBSCRIBE', {
        id: 'sub-0',
        destination: SUBSCRIBE_DESTINATION,
        ack: 'auto',
      });
      statusCallbacks?.onOpen?.();
      return;

    case 'MESSAGE':
      dispatch(JSON.parse(frame.body || '{}'));
      return;

    case 'ERROR':
      console.warn('[ws] STOMP ERROR', frame.body);
      return;

    case 'RECEIPT':
      return; // ack for our SUBSCRIBE — no action

    default:
      console.debug('[ws] frame', frame.command);
  }
}

function sendFrame(command, headers = {}, body = '') {
  if (!socket || socket.readyState !== WebSocket.OPEN) return;
  socket.send(encodeFrame(command, headers, body));
}

/**
 * Send a request to the agent over STOMP (SEND to app prefix + @MessageMapping).
 * e.g. sendRequest({ token, idempotencyKey, content: { action, content } })
 */
export function sendRequest(payload = {}) {
  if (!stompConnected) return;
  sendFrame(
    'SEND',
    { destination: `${APP_PREFIX}/agent`, 'content-type': 'application/json' },
    JSON.stringify(payload),
  );
}

/* ── Connection lifecycle ───────────────────────────────────────────── */

function scheduleReconnect() {
  if (manualClose) return;
  consecutiveFailures += 1;
  // If the endpoint never opened once, it likely doesn't exist on the
  // backend — give up after the first failed attempt instead of burning
  // requests on a storm of reconnect handshakes (rate-limit friendly).
  const maxFailures = everOpened ? MAX_CONSECUTIVE_FAILURES : 1;
  if (consecutiveFailures > maxFailures) {
    clearTimeout(reconnectTimer);
    return;
  }
  clearTimeout(reconnectTimer);
  reconnectTimer = setTimeout(() => {
    reconnectDelay = Math.min(reconnectDelay * 2, RECONNECT_MAX_MS);
    connect();
  }, reconnectDelay);
}

function startHeartbeat() {
  clearInterval(heartbeatTimer);
  heartbeatTimer = setInterval(() => {
    if (socket && socket.readyState === WebSocket.OPEN && stompConnected) {
      // STOMP heartbeat: bare EOL, ignored by the parser.
      socket.send('\n');
    }
  }, HEARTBEAT_MS);
}

function stopHeartbeat() {
  clearInterval(heartbeatTimer);
  heartbeatTimer = null;
}

/**
 * Connect (or reconnect) to the agent STOMP endpoint.
 * Auth gate: without a logged-in session the socket is never opened.
 */
export function connect(options = {}) {
  const { onStatus } = options;
  manualClose = false;

  if (!getUser()) {
    onStatus?.('closed');
    return; // no session — stay offline until startWs()/connect() after login
  }

  if (socket && (socket.readyState === WebSocket.OPEN || socket.readyState === WebSocket.CONNECTING)) {
    return;
  }

  try {
    socket = new WebSocket(getWsUrl());
  } catch (err) {
    onStatus?.('error', err.message);
    scheduleReconnect();
    return;
  }

  let buffer = '';

  socket.onopen = () => {
    everOpened = true;
    reconnectDelay = RECONNECT_BASE_MS;
    consecutiveFailures = 0;
    stompConnected = false;
    // STOMP handshake: CONNECT with version + heartbeat negotiation.
    socket.send(
      encodeFrame('CONNECT', {
        'accept-version': ACCEPT_VERSION,
        'heart-beat': HEART_BEAT,
        host: window.location.host,
      }),
    );
    startHeartbeat();
    onStatus?.('open');
  };

  socket.onmessage = (event) => {
    buffer += event.data;
    const frames = splitFrames(buffer);
    // Last chunk is incomplete (no NUL yet) — keep it for the next message.
    buffer = frames.pop() ?? '';
    for (const raw of frames) {
      try {
        handleFrame(parseFrame(raw));
      } catch (err) {
        console.error('[ws] frame error', err);
      }
    }
  };

  socket.onclose = () => {
    stopHeartbeat();
    stompConnected = false;
    onStatus?.('closed');
    scheduleReconnect();
  };

  socket.onerror = (err) => {
    onStatus?.('error', err?.message);
  };
}

/** Close the WebSocket and stop reconnecting. */
export function disconnect() {
  manualClose = true;
  clearTimeout(reconnectTimer);
  stopHeartbeat();
  stompConnected = false;
  if (socket) {
    socket.onclose = null;
    socket.close();
    socket = null;
  }
}

/** Alias of disconnect() — used by the auth-gate logout hook. */
export function stopWs() {
  disconnect();
}

/** Register page-level WS callbacks (status changes + raw messages). */
export function setupWsListeners({ onOpen = null, onClose = null, onMessage: onMessageCb = null } = {}) {
  statusCallbacks = { onOpen, onClose };
  if (onMessageCb) {
    onMessage(onMessageCb);
  }
}

/** Start the WebSocket connection, wiring registered status callbacks. */
export function startWs() {
  // Explicit (re)start from a page — reset the failure bookkeeping so a
  // fresh attempt is made. Internal reconnects must NOT reset these.
  consecutiveFailures = 0;
  reconnectDelay = RECONNECT_BASE_MS;
  everOpened = false;
  connect({
    onStatus: (state, err) => {
      if (state === 'error') console.warn('[ws] erro de conexão', err);
    },
  });
}

/* ── Auth gate ──────────────────────────────────────────────────────── */

// Single registration: when the session disappears (logout), close the
// socket and stop reconnecting. startWs() is invoked again after login.
onAuthChange((user) => {
  if (!user) stopWs();
});

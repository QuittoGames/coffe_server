/**
 * Coffee Server Dashboard — WebSocket Connection Manager
 * Reconnect logic with exponential backoff. When the backend has no WS
 * endpoint yet, `connect()` simply stays disconnected without crashing.
 */

const RECONNECT_BASE_MS = 1000;
const RECONNECT_MAX_MS = 30000;
const HEARTBEAT_MS = 30000;
// Stop retrying after this many consecutive failures; startWs()/connect()
// can be invoked again later (e.g. user refresh) to try once more.
const MAX_CONSECUTIVE_FAILURES = 5;

let socket = null;
let url = null;
let reconnectDelay = RECONNECT_BASE_MS;
let reconnectTimer = null;
let heartbeatTimer = null;
let manualClose = false;
let consecutiveFailures = 0;
const listeners = new Set();

// Convenience callbacks used by page inits (setupWsListeners).
let statusCallbacks = null;

function getWsUrl() {
  if (url) return url;
  // Same-origin upgrade by default; override via setUrl().
  const proto = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
  return `${proto}//${window.location.host}/ws`;
}

/** Override the WebSocket URL (e.g. when served behind a proxy). */
export function setWsUrl(value) {
  url = value;
}

/** Subscribe to raw messages; returns unsubscribe fn. */
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

function scheduleReconnect() {
  if (manualClose) return;
  consecutiveFailures += 1;
  if (consecutiveFailures > MAX_CONSECUTIVE_FAILURES) {
    // Give up silently until someone calls startWs()/connect() again.
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
    if (socket && socket.readyState === WebSocket.OPEN) {
      socket.send(JSON.stringify({ type: 'ping' }));
    }
  }, HEARTBEAT_MS);
}

function stopHeartbeat() {
  clearInterval(heartbeatTimer);
  heartbeatTimer = null;
}

/** Connect (or reconnect) to the WebSocket endpoint. */
export function connect(options = {}) {
  const { onStatus } = options;
  manualClose = false;
  consecutiveFailures = 0;

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

  socket.onopen = () => {
    reconnectDelay = RECONNECT_BASE_MS;
    consecutiveFailures = 0;
    startHeartbeat();
    onStatus?.('open');
  };

  socket.onmessage = (event) => {
    try {
      dispatch(JSON.parse(event.data));
    } catch {
      dispatch({ type: 'raw', data: event.data });
    }
  };

  socket.onclose = () => {
    stopHeartbeat();
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
  if (socket) {
    socket.onclose = null;
    socket.close();
    socket = null;
  }
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
  connect({
    onStatus: (state, err) => {
      if (statusCallbacks) {
        if (state === 'open') statusCallbacks.onOpen?.();
        else if (state === 'closed') statusCallbacks.onClose?.();
      }
      if (state === 'error') console.warn('[ws] erro de conexão', err);
    },
  });
}

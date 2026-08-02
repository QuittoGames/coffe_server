/**
 * Coffee Server Dashboard — WebSocket Message Handlers
 * Routes incoming WS messages to app-level reactions (toast, refresh triggers).
 * Default mode: no-op passthrough so pages work without a live WS backend.
 */

import { WS_EVENTS, WS_EVENT_LABELS } from './events.js';
import { onMessage } from './connection.js';
import { EVENTS } from '../utils/constants.js';

/** Register default handlers. Returns an unsubscribe function. */
export function registerHandlers({ showToast = null } = {}) {
  const unsub = onMessage((message) => {
    const type = message?.type ?? message?.event;

    switch (type) {
      case WS_EVENTS.MACHINE_STATUS: {
        const hostname = message.hostname ?? 'unknown';
        const online = Boolean(message.online);
        showToast?.(`${hostname} ${online ? 'online' : 'offline'}`, online ? 'success' : 'error');
        window.dispatchEvent(new CustomEvent(EVENTS.DATA_REFRESH, { detail: { domain: 'machines' } }));
        break;
      }

      case WS_EVENTS.MCP_INVOKE: {
        showToast?.(`MCP: ${message.tool ?? 'tool'} → ${message.status ?? 'ok'}`, 'info');
        break;
      }

      case WS_EVENTS.METRICS:
      case WS_EVENTS.HEALTH:
        window.dispatchEvent(new CustomEvent(EVENTS.DATA_REFRESH, { detail: { domain: type } }));
        break;

      case 'ping':
      case 'pong':
        break; // heartbeat — silent

      default:
        // Unknown event: keep silent in mock mode.
        if (type && WS_EVENT_LABELS[type]) {
          window.dispatchEvent(new CustomEvent(EVENTS.DATA_REFRESH));
        }
    }
  });

  return unsub;
}

/**
 * Coffee Server Dashboard — WebSocket Message Handlers
 * Routes incoming WS messages to app-level reactions (toast, refresh triggers).
 *
 * Two message shapes are handled:
 *  1. Agent responses (ResponseContentAgent, from STOMP /queue/coffee-agent):
 *       { status, userUse, action, operation }
 *     — action/operation are strings; status is a result keyword.
 *  2. Legacy event envelopes (future server events): { type|event, ... }
 *     — matched against WS_EVENTS.
 */

import { WS_EVENTS, WS_EVENT_LABELS } from './events.js';
import { onMessage } from './connection.js';
import { EVENTS } from '../utils/constants.js';

/** result keywords treated as failure for the agent response toast. */
const FAILURE_STATUSES = new Set(['error', 'failed', 'denied', 'unavailable']);

/** True when the message is a ResponseContentAgent payload. */
function isAgentResponse(message) {
  return (
    message &&
    typeof message === 'object' &&
    typeof message.action === 'string' &&
    message.status !== undefined
  );
}

function handleAgentResponse(message, showToast) {
  const { status, action, operation } = message;
  const label = `${action}${operation ? ` (${operation})` : ''}`;
  const ok = !FAILURE_STATUSES.has(String(status).toLowerCase());

  showToast?.(`${label}: ${status}`, ok ? 'success' : 'error');

  // Agent state may have changed (machines, config, files…) — refresh views.
  window.dispatchEvent(new CustomEvent(EVENTS.DATA_REFRESH, { detail: { domain: 'agent' } }));
}

/** Register default handlers. Returns an unsubscribe function. */
export function registerHandlers({ showToast = null } = {}) {
  const unsub = onMessage((message) => {
    // ResponseContentAgent frame first — it has no `type`/`event` field.
    if (isAgentResponse(message)) {
      handleAgentResponse(message, showToast);
      return;
    }

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

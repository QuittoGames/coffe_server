/**
 * Coffee Server Dashboard — WebSocket Event Definitions
 * Event names that the server may emit in the future. Kept as constants so
 * handlers can be swapped from mock mode to live mode without renaming.
 */

export const WS_EVENTS = {
  /** Machine status changed (hostname, online/offline, metrics). */
  MACHINE_STATUS: 'machine.status',
  /** New machine registered. */
  MACHINE_ADDED: 'machine.added',
  /** Machine removed. */
  MACHINE_REMOVED: 'machine.removed',

  /** Container lifecycle event. */
  CONTAINER_STATUS: 'container.status',

  /** MCP tool invocation result. */
  MCP_INVOKE: 'mcp.invoke',

  /** Infrastructure metrics sample (cpu/mem/disk/network). */
  METRICS: 'metrics.sample',

  /** Server health aggregate. */
  HEALTH: 'health.update',
};

/** Map a WS_EVENTS key to a human label (used in logs/toasts). */
export const WS_EVENT_LABELS = {
  [WS_EVENTS.MACHINE_STATUS]: 'machine status',
  [WS_EVENTS.MACHINE_ADDED]: 'machine added',
  [WS_EVENTS.MACHINE_REMOVED]: 'machine removed',
  [WS_EVENTS.CONTAINER_STATUS]: 'container status',
  [WS_EVENTS.MCP_INVOKE]: 'mcp invoke',
  [WS_EVENTS.METRICS]: 'metrics',
  [WS_EVENTS.HEALTH]: 'health',
};

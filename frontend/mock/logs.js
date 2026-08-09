/**
 * Coffee Server Dashboard — Mock Live Logs
 * Terminal-style log entries. Types: success | error | info | warn.
 * Initial entries are timestamped relative to module load (oldest first,
 * newest at the bottom, like a real terminal). randomLogEntry() feeds the
 * live interval — a deterministic cycle so the demo is stable.
 */

function minutesAgo(minutes) {
  return new Date(Date.now() - minutes * 60 * 1000).toTimeString().slice(0, 8);
}

export const mockLogs = [
  { time: minutesAgo(22), type: 'info', message: 'mcp server coffee-mcp-server registrado em /mcp' },
  { time: minutesAgo(18), type: 'success', message: 'health.check → postgres up · 34ms' },
  { time: minutesAgo(15), type: 'warn', message: 'disco /mnt/mount/data/backups em 93%' },
  { time: minutesAgo(12), type: 'success', message: 'calendar.list_events · 3 eventos · 214ms' },
  { time: minutesAgo(9), type: 'error', message: 'backup.list falhou — endpoint não implementado' },
  { time: minutesAgo(6), type: 'info', message: 'sessão validada via cookie httpOnly' },
  { time: minutesAgo(4), type: 'warn', message: 'tailscale latency elevada · 41ms' },
  { time: minutesAgo(2), type: 'success', message: 'machine.status → quittoserver online · 88ms' },
];

const LIVE_POOL = [
  { type: 'info', message: 'heartbeat recebido' },
  { type: 'success', message: 'redis-cache ping · 3ms' },
  { type: 'info', message: 'tailscale netcheck ok' },
  { type: 'success', message: 'machine.status → media-box online · 112ms' },
  { type: 'warn', message: 'cpu pico em coffee-server · 15%' },
  { type: 'error', message: 'wake.on.lan falhou para lab-nuc — offline' },
  { type: 'success', message: 'google calendar token renovado' },
  { type: 'info', message: 'audit log gravado · login ok' },
  { type: 'warn', message: 'memória em 42% — dentro do esperado' },
];

let seq = 0;

/** Next live entry for the simulated feed (stable cycle). */
export function randomLogEntry() {
  const entry = LIVE_POOL[seq % LIVE_POOL.length];
  seq += 1;
  return { type: entry.type, message: entry.message };
}

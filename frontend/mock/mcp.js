/**
 * Coffee Server Dashboard — Mock MCP Data
 * MCP servers/tools exposed by the backend (`/mcp/**` via Spring AI).
 * The backend has GoogleCalendarTools; machine/health tools are planned.
 */

export const mockMCP = {
  serverInfo: {
    name: 'coffee-mcp-server',
    version: '0.0.1',
    protocol: 'MCP',
    status: 'online',
    uptimeSeconds: 10800,
    transport: 'WebMVC (HTTP)',
    endpoint: '/mcp',
  },
  servers: [
    {
      id: 'mcp-calendar',
      name: 'Google Calendar',
      description: 'List and create events on the linked Google Calendar.',
      status: 'online',
      provider: 'GOOGLE',
      tools: ['calendar.list_events', 'calendar.create_event', 'calendar.debug_auth'],
      calls: 128,
      lastCall: '2026-08-01T09:42:00Z',
      endpoint: '/mcp/calendar',
    },
    {
      id: 'mcp-machines',
      name: 'Machines',
      description: 'Query machine status, Wake-on-LAN and Tailscale state.',
      status: 'online',
      provider: 'COFFEE',
      tools: ['machine.list', 'machine.status', 'machine.wake'],
      calls: 41,
      lastCall: '2026-08-01T09:15:00Z',
      endpoint: '/mcp/machines',
    },
    {
      id: 'mcp-health',
      name: 'Server Health',
      description: 'Health check of server services (DB, Redis, disk, uptime).',
      status: 'online',
      provider: 'COFFEE',
      tools: ['health.check', 'status.overview', 'status.dependencies'],
      calls: 23,
      lastCall: '2026-08-01T08:50:00Z',
      endpoint: '/mcp/health',
    },
    {
      id: 'mcp-backup',
      name: 'Backup',
      description: 'List, create and restore backups (planned).',
      status: 'planned',
      provider: 'COFFEE',
      tools: ['backup.list', 'backup.create', 'backup.restore'],
      calls: 0,
      lastCall: null,
      endpoint: '/mcp/backup',
    },
  ],
};

/** Recent MCP invocation log (for the terminal strip / activity). */
export const mockMCPLog = [
  { time: '09:42:11', server: 'calendar', tool: 'list_events', status: 'ok', ms: 214 },
  { time: '09:15:03', server: 'machines', tool: 'machine.status', status: 'ok', ms: 88 },
  { time: '08:50:47', server: 'health', tool: 'health.check', status: 'ok', ms: 34 },
  { time: '08:12:29', server: 'calendar', tool: 'debug_auth', status: 'err', ms: 0 },
];

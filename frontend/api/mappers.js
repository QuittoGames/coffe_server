/**
 * Coffee Server Dashboard — API Mappers
 * Transform backend DTOs into frontend-friendly domain objects.
 * Mock data already uses the frontend shape; mappers normalize real API payloads.
 */

/** Normalize a backend user payload. */
export function mapUser(dto) {
  if (!dto) return null;
  return {
    id: dto.id,
    name: dto.name,
    email: dto.email ?? '',
    role: dto.role ?? 'USER',
    createdAt: dto.createdAt ?? null,
    linuxUser: dto.linuxUser
      ? {
          uid: dto.linuxUser.uid,
          name: dto.linuxUser.name,
          shell: dto.linuxUser.shell,
          homeDir: dto.linuxUser.homeDir,
          group: dto.linuxUser.group,
        }
      : null,
    machinesCount: dto.machinesCount ?? 0,
  };
}

/** Normalize a backend machine payload into the dashboard shape. */
export function mapMachine(dto) {
  if (!dto) return null;
  return {
    id: dto.id,
    hostname: dto.hostname,
    tailscaleNodeKey: dto.tailscaleNodeKey ?? null,
    currentIp: dto.currentIp ?? null,
    macAddress: dto.macAddress ?? '—',
    wolEnabled: Boolean(dto.wolEnabled),
    status: dto.status ? 'online' : 'offline',
    os: dto.os ?? 'unknown',
    cpu: dto.cpu ?? 0,
    memory: dto.memory ?? 0,
    disk: dto.disk ?? 0,
    uptimeSeconds: dto.uptimeSeconds ?? null,
    userId: dto.userId,
  };
}

/** Normalize a list payload; tolerates `{ machines: [...] }` and `[...]`. */
export function mapMachineList(payload) {
  const list = Array.isArray(payload) ? payload : payload?.machines ?? [];
  return list.map(mapMachine).filter(Boolean);
}

/** Normalize a backend health payload. */
export function mapHealth(dto) {
  if (!dto) return null;
  return {
    database: dto.database ?? 'down',
    redis: dto.redis ?? 'down',
    googleCalendar: dto.googleCalendar ?? 'down',
    diskUsagePercent: dto.diskUsagePercent ?? 0,
  };
}

/** Normalize an MCP server payload. */
export function mapMCPServer(dto) {
  if (!dto) return null;
  return {
    id: dto.id,
    name: dto.name,
    description: dto.description ?? '',
    status: dto.status ?? 'offline',
    provider: dto.provider ?? 'COFFEE',
    tools: Array.isArray(dto.tools) ? dto.tools : [],
    calls: dto.calls ?? 0,
    lastCall: dto.lastCall ?? null,
    endpoint: dto.endpoint ?? '/mcp',
  };
}

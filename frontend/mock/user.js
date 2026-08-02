/**
 * Coffee Server Dashboard — Mock User Data
 * Mirrors the backend `User` domain model (name, email, role, linuxUser).
 * Replace with services/auth + services/api when wired to the backend.
 */

export const mockCurrentUser = {
  id: 1,
  name: 'quitto',
  email: 'quitto@coffee.local',
  role: 'ADMIN',
  createdAt: '2026-01-15T10:30:00Z',
  linuxUser: {
    uid: 1000,
    name: 'quitto',
    shell: '/bin/bash',
    homeDir: '/home/quitto',
    group: { gid: 1000, name: 'quitto', isActive: true },
  },
  machinesCount: 3,
};

/** All users (for future admin view). */
export const mockUsers = [
  {
    id: 1,
    name: 'quitto',
    email: 'quitto@coffee.local',
    role: 'ADMIN',
    linuxUid: 1000,
    status: 'online',
    createdAt: '2026-01-15T10:30:00Z',
  },
  {
    id: 2,
    name: 'henrique',
    email: 'henrique@coffee.local',
    role: 'USER',
    linuxUid: 1001,
    status: 'offline',
    createdAt: '2026-02-02T14:00:00Z',
  },
  {
    id: 3,
    name: 'jarvis',
    email: 'jarvis@coffee.local',
    role: 'MCP',
    linuxUid: null,
    status: 'online',
    createdAt: '2026-03-10T08:00:00Z',
  },
];

/**
 * Coffee Server Dashboard — Mock Containers Data
 * NOTE: The current backend data model has NO Container entity (drawio model:
 * User 1:N Machine, User 0..1 LinuxUser, Groups 1:N LinuxUser, User 1:N ExternalAccount).
 * Containers are a future domain; this mock keeps the dashboard complete.
 */

export const mockContainers = [
  {
    id: 'c-9f3a',
    name: 'postgres',
    image: 'postgres:17',
    status: 'running',
    state: 'Up 12 days',
    port: '5432/tcp',
    cpu: 3,
    memory: 24,
    createdAt: '2026-01-15T11:00:00Z',
    host: 'quittoserver',
  },
  {
    id: 'c-8d2b',
    name: 'redis-cache',
    image: 'redis:7-alpine',
    status: 'running',
    state: 'Up 12 days',
    port: '6379/tcp',
    cpu: 1,
    memory: 8,
    createdAt: '2026-01-15T11:05:00Z',
    host: 'quittoserver',
  },
  {
    id: 'c-7c1a',
    name: 'coffee-server',
    image: 'quitto/coffee-server:0.0.1',
    status: 'running',
    state: 'Up 3 hours',
    port: '8080/tcp',
    cpu: 15,
    memory: 32,
    createdAt: '2026-01-15T11:10:00Z',
    host: 'quittoserver',
  },
  {
    id: 'c-6e0f',
    name: 'portainer',
    image: 'portainer/portainer-ce',
    status: 'stopped',
    state: 'Exited (0) 2 days ago',
    port: '9000/tcp',
    cpu: 0,
    memory: 0,
    createdAt: '2026-02-01T09:00:00Z',
    host: 'quittoserver',
  },
];

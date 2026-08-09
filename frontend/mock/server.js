/**
 * Coffee Server Dashboard — Mock Server Data
 * Sistema / Rede / Discos. Derived from mockMachines[0] where sensible
 * (hostname, OS, uptime, cpu, memory) plus real homelab details
 * (kernel, backup mount, tailscale node from the server).
 * Replace with `/api/status` or `/info/*` when the backend exposes them.
 */

const GIB = 1024 ** 3;

export const mockServer = {
  system: {
    hostname: 'quittoserver',
    os: 'Ubuntu 24.04 LTS',
    kernel: '6.8.0-117-generic',
    uptimeSeconds: 1972800, // 22d 17h
    cpuModel: 'Intel Xeon E5-2650 v3 · 8 cores',
    cpu: 27, // %
    memoryTotal: Math.round(1.9 * GIB), // 1.9 GiB (homelab real)
    memory: 42, // %
  },
  network: {
    tailscaleIp: '100.73.31.49',
    tailscaleNode: 'ts-node-4f8a2c',
    localIp: '192.168.1.10',
    interfaces: ['eth0', 'tailscale0'],
    gateway: '192.168.1.1',
    rx: 12, // % simulated usage
  },
  disks: [
    { mount: '/', total: 120 * GIB, used: 74 * GIB },                    // ~61%
    { mount: '/home', total: 80 * GIB, used: 28 * GIB },                 // ~35%
    { mount: '/mnt/mount/data', total: 500 * GIB, used: 430 * GIB },     // ~86% → warn
    { mount: '/mnt/mount/data/backups', total: 500 * GIB, used: 465 * GIB }, // ~93% → danger
  ],
};

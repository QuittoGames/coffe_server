/**
 * Coffee Server Dashboard — Dashboard Page Init
 * Renders: user card, KPI row, infrastructure bars, machines, containers, MCP preview.
 * Uses mock data now; swap the imports to services/api when wired to the backend.
 */

import { onReady, qs, el } from '../utils/dom.js';
import { formatBytes } from '../utils/format.js';
import { protectPage } from '../auth/session.js';
import { getUser } from '../services/auth.js';
import { renderHeader, updateHeaderStatus } from '../components/Header.js';
import { renderStatusBar, updateStatusBarState } from '../components/StatusBar.js';
import { renderMiniPanel, togglePanel } from '../components/MiniPanel.js';
import { setupToastListener } from '../components/Toast.js';
import { setupWsListeners, startWs } from '../websocket/connection.js';
import { kpiCard } from '../components/KpiCard.js';
import { machineCard } from '../components/MachineCard.js';
import { containerRow } from '../components/ContainerRow.js';
import { mcpCard } from '../components/McpCard.js';

// Mock data — swap for services/api when ready.
import { mockCurrentUser } from '../mock/user.js';
import { mockMachines } from '../mock/machines.js';
import { mockContainers } from '../mock/containers.js';
import { mockMCP } from '../mock/mcp.js';

function renderUserCard() {
  const root = qs('[data-user-card]');
  if (!root) return;
  // Real session identity (whoever logged in); Linux profile details are
  // placeholders until the backend exposes a profile endpoint.
  const session = getUser();
  const user = {
    ...mockCurrentUser,
    name: session?.name ?? mockCurrentUser.name,
    role: session?.role ?? mockCurrentUser.role,
  };
  root.replaceChildren(
    el('div', {
      class: 'card-header',
      html: `<span class="card-title"><span class="terminal-prefix">❯ </span>${user.name}</span>`,
    }),
    el('div', {
      class: 'card-body',
      html: `
        <div class="flex flex-col gap-1">
          <span class="text-muted">${user.email}</span>
          <span><span class="badge badge-processing">${user.role}</span>
          ${user.linuxUser ? `<span class="tag mono">uid ${user.linuxUser.uid}</span>` : ''}
          ${user.linuxUser ? `<span class="tag mono">${user.linuxUser.shell}</span>` : ''}</span>
        </div>
      `,
    }),
  );
}

function renderKpiRow() {
  const root = qs('[data-kpi-row]');
  if (!root) return;

  const online = mockMachines.filter((m) => m.status === 'online').length;
  const running = mockContainers.filter((c) => c.status === 'running').length;
  const mcpOnline = mockMCP.servers.filter((s) => s.status === 'online').length;

  const kpis = [
    { label: 'máquinas online', value: `${online}/${mockMachines.length}`, trendText: 'homelab', trend: online >= 2 ? 'up' : 'down' },
    { label: 'containers ativos', value: running, trendText: mockContainers.length - running > 0 ? `${mockContainers.length - running} parado` : 'todos ativos', trend: running >= 3 ? 'up' : 'down' },
    { label: 'serviços MCP', value: mcpOnline, unit: 'online', trendText: 'via /mcp', trend: 'up' },
    { label: 'uptime', value: '22d', trendText: 'quittoserver', trend: 'up' },
  ];

  root.replaceChildren(...kpis.map(kpiCard));
}

function renderInfra() {
  const setBar = (key, pct, label) => {
    const bar = qs(`[data-infra-${key}]`);
    const labelNode = qs(`[data-infra-${key}-label]`);
    if (bar) bar.style.width = `${Math.min(100, pct)}%`;
    if (labelNode) labelNode.textContent = label;
  };

  // Simulated host metrics for quittoserver.
  const cpu = 27;
  const mem = 42;
  const disk = 61;
  const net = 12;

  setBar('cpu', cpu, `${cpu}%`);
  setBar('mem', mem, `${mem}% (${formatBytes(1024 * 1024 * 1024 * 0.8)})`);
  setBar('disk', disk, `${disk}% (${formatBytes(1024 * 1024 * 1024 * 2)})`);
  setBar('net', net, `${net}%`);
}

function renderMachines() {
  const root = qs('[data-machines-list]');
  const count = qs('[data-machines-count]');
  if (!root) return;
  root.replaceChildren(...mockMachines.map((m) =>
    machineCard(m, {
      onWake: (machine) => {
        import('../components/Toast.js').then(({ toast }) =>
          toast(`WOL enviado para ${machine.hostname} (simulado)`, 'info'));
      },
      onOpen: (machine) => {
        import('../components/Modal.js').then(({ openModal }) =>
          openModal({
            title: machine.hostname,
            body: `
              <div class="flex flex-col gap-2">
                <span class="text-muted">IP: <code>${machine.currentIp ?? '—'}</code></span>
                <span class="text-muted">MAC: <code>${machine.macAddress}</code></span>
                <span class="text-muted">Tailscale: <code>${machine.tailscaleNodeKey}</code></span>
                <span class="text-muted">WOL: <code>${machine.wolEnabled ? 'habilitado' : 'desabilitado'}</code></span>
                <span class="text-muted">OS: <code>${machine.os}</code></span>
              </div>
            `,
          }));
      },
    }),
  ));
  if (count) count.textContent = `${mockMachines.length} registradas`;
}

function renderContainers() {
  const body = qs('[data-containers-body]');
  const count = qs('[data-containers-count]');
  if (!body) return;
  body.replaceChildren(...mockContainers.map((c) => containerRow(c)));
  if (count) count.textContent = `${mockContainers.filter((c) => c.status === 'running').length} ativos`;
}

function renderMcpPreview() {
  const root = qs('[data-mcp-preview]');
  if (!root) return;
  root.replaceChildren(...mockMCP.servers.map((s) => mcpCard(s)));
}

onReady(async () => {
  // Protect page: no local session → login.
  await protectPage();

  renderHeader('[data-header]', { title: 'Coffee Server', active: 'dashboard', address: 'server/home' });
  renderStatusBar('[data-statusbar]', { uptimeSeconds: 1972800 });
  renderMiniPanel('[data-mini-panel]', { active: 'dashboard' });

  setupToastListener();
  setupWsListeners({ onOpen: () => updateStatusBarState('open'), onClose: () => updateStatusBarState('closed'), onMessage: () => {} });
  startWs();

  renderUserCard();
  renderKpiRow();
  renderInfra();
  renderMachines();
  renderContainers();
  renderMcpPreview();

  // Refresh button — re-renders with skeleton shimmer for realism.
  const refreshBtn = qs('[data-refresh]');
  refreshBtn?.addEventListener('click', async () => {
    refreshBtn.disabled = true;
    refreshBtn.classList.add('loading');
    await new Promise((r) => setTimeout(r, 500));
    renderKpiRow();
    renderInfra();
    renderMachines();
    renderContainers();
    refreshBtn.disabled = false;
    refreshBtn.classList.remove('loading');
  });
});

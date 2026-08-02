/**
 * Coffee Server Dashboard — MCP Manager Page Init
 * Renders MCP server cards and the recent call log from mock data.
 */

import { onReady, qs, el } from '../utils/dom.js';
import { protectPage } from '../auth/session.js';
import { renderHeader } from '../components/Header.js';
import { renderStatusBar, updateStatusBarState } from '../components/StatusBar.js';
import { renderMiniPanel } from '../components/MiniPanel.js';
import { renderServerStatus } from '../components/ServerStatus.js';
import { setupToastListener } from '../components/Toast.js';
import { mcpCard } from '../components/McpCard.js';
import { setupWsListeners, startWs } from '../websocket/connection.js';

import { mockMCP, mockMCPLog } from '../mock/mcp.js';

function renderServerInfo() {
  const root = qs('[data-mcp-server-info]');
  if (!root) return;
  const info = mockMCP.serverInfo;

  const services = mockMCP.servers.map((s) => ({
    name: s.name.toLowerCase().replace(/\s+/g, '-'),
    state: s.status,
    latency: s.calls > 0 ? `${s.calls} calls` : '—',
    led: s.status === 'online' ? 'ok' : s.status === 'planned' ? 'warn' : 'err',
  }));

  renderServerStatus(root, {
    prompt: `// ${info.name} · ${info.transport} · endpoint ${info.endpoint}`,
    command: `coffee mcp status`,
    services,
    activity: [4, 8, 6, 14, 20, 16, 24, 30, 22, 36, 40, 32, 46, 52, 44, 60, 68, 62, 74],
    activityLabel: `protocolo ${info.protocol} · ${info.status}`,
  });
}

function renderMcpGrid() {
  const root = qs('[data-mcp-grid]');
  const count = qs('[data-mcp-count]');
  if (!root) return;
  const servers = mockMCP.servers;
  root.replaceChildren(...servers.map((s) =>
    mcpCard(s, {
      onOpen: (server) => {
        import('../components/Modal.js').then(({ openModal }) =>
          openModal({
            title: server.name,
            // trustHtml: body é template estático com valores do mock (confiável hoje).
            trustHtml: true,
            body: `
              <div class="flex flex-col gap-2">
                <span class="text-muted">Provedor: <code>${server.provider}</code></span>
                <span class="text-muted">Endpoint: <code>${server.endpoint}</code></span>
                <span class="text-muted">Chamadas: <code>${server.calls}</code></span>
                <span class="text-muted">Status: <code>${server.status}</code></span>
              </div>
            `,
          }));
      },
    }),
  ));
  if (count) count.textContent = `${servers.filter((s) => s.status === 'online').length} online`;
}

function renderMcpLog() {
  const body = qs('[data-mcp-log-body]');
  if (!body) return;
  body.replaceChildren(...mockMCPLog.map((entry) => {
    const tr = el('tr');
    const ok = entry.status === 'ok';
    const statusCell = el('td', { class: 'mono' });
    statusCell.append(el('span', { class: `badge ${ok ? 'badge-online' : 'badge-offline'}`, text: entry.status }));
    tr.append(
      el('td', { class: 'mono', text: entry.time }),
      el('td', { text: entry.server }),
      el('td', { class: 'mono', text: entry.tool }),
      statusCell,
      el('td', { class: 'mono', text: entry.ms > 0 ? `${entry.ms}ms` : '—' }),
    );
    return tr;
  }));
}

onReady(async () => {
  await protectPage();

  renderHeader('[data-header]', { title: 'Coffee Server', active: 'mcp', address: 'server/mcp' });
  renderStatusBar('[data-statusbar]', { uptimeSeconds: 1972800 });
  renderMiniPanel('[data-mini-panel]', { active: 'mcp' });

  setupToastListener();
  setupWsListeners({ onOpen: () => updateStatusBarState('open'), onClose: () => updateStatusBarState('closed'), onMessage: () => {} });
  startWs();

  renderServerInfo();
  renderMcpGrid();
  renderMcpLog();

  const refreshBtn = qs('[data-mcp-refresh]');
  refreshBtn?.addEventListener('click', async () => {
    refreshBtn.disabled = true;
    refreshBtn.classList.add('loading');
    await new Promise((r) => setTimeout(r, 500));
    renderMcpGrid();
    refreshBtn.disabled = false;
    refreshBtn.classList.remove('loading');
  });
});

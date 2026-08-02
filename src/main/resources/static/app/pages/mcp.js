/**
 * Coffee Server Dashboard — MCP Manager Page Init
 * Renders MCP server cards and the recent call log from mock data.
 */

import { onReady, qs, el } from '../utils/dom.js';
import { protectPage } from '../auth/session.js';
import { renderHeader, updateHeaderStatus } from '../components/Header.js';
import { renderStatusBar, updateStatusBarState } from '../components/StatusBar.js';
import { renderMiniPanel } from '../components/MiniPanel.js';
import { setupToastListener } from '../components/Toast.js';
import { mcpCard } from '../components/McpCard.js';
import { setupWsListeners, startWs } from '../websocket/connection.js';

import { mockMCP, mockMCPLog } from '../mock/mcp.js';

function renderServerInfo() {
  const root = qs('[data-mcp-server-info]');
  if (!root) return;
  const info = mockMCP.serverInfo;
  root.innerHTML = `
    <span class="term-dim">// ${info.name} · ${info.transport} · endpoint ${info.endpoint}</span><br>
    <span class="term-ok">✔</span> protocolo <span class="term-cmd">${info.protocol}</span> ·
    <span class="term-ok">✔</span> status <span class="term-cmd">${info.status}</span>
  `;
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
    tr.append(
      el('td', { class: 'mono', text: entry.time }),
      el('td', { text: entry.server }),
      el('td', { class: 'mono', text: entry.tool }),
      el('td', { html: `<span class="badge ${ok ? 'badge-online' : 'badge-offline'}">${entry.status}</span>` }),
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

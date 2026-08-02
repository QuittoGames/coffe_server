/**
 * Coffee Server Dashboard — Dashboard Page Init
 * Renders: user card, KPI row, sistema/rede, discos, containers, máquinas,
 * calendário + criar projeto, MCP preview, logs ao vivo.
 * Uses mock data now; swap the imports to services/api when wired to the backend.
 */

import { onReady, qs, el, escapeHtml } from '../utils/dom.js';
import { formatBytes, formatDuration, formatPercent, clamp } from '../utils/format.js';
import { protectPage } from '../auth/session.js';
import { getUser } from '../services/auth.js';
import { renderHeader } from '../components/Header.js';
import { renderStatusBar, updateStatusBarState } from '../components/StatusBar.js';
import { renderMiniPanel } from '../components/MiniPanel.js';
import { renderServerStatus } from '../components/ServerStatus.js';
import { setupToastListener, toast } from '../components/Toast.js';
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
import { mockServer } from '../mock/server.js';
import { mockCalendarEvents } from '../mock/calendar.js';
import { mockLanguages } from '../mock/projects.js';
import { mockLogs, randomLogEntry } from '../mock/logs.js';

/* ── Usuário ─────────────────────────────────────────────────────────── */

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
      html: `<span class="card-title"><span class="terminal-prefix">❯ </span>${escapeHtml(user.name)}</span>`,
    }),
    el('div', {
      class: 'card-body',
      html: `
        <div class="flex flex-col gap-1">
          <span class="text-muted">${escapeHtml(user.email)}</span>
          <span><span class="badge badge-processing">${escapeHtml(user.role)}</span>
          ${user.linuxUser ? `<span class="tag mono">uid ${escapeHtml(user.linuxUser.uid)}</span>` : ''}
          ${user.linuxUser ? `<span class="tag mono">${escapeHtml(user.linuxUser.shell)}</span>` : ''}</span>
        </div>
      `,
    }),
  );
}

/* ── KPIs ────────────────────────────────────────────────────────────── */

function renderKpiRow() {
  const root = qs('[data-kpi-row]');
  if (!root) return;

  const online = mockMachines.filter((m) => m.status === 'online').length;
  const running = mockContainers.filter((c) => c.status === 'running').length;
  const mcpOnline = mockMCP.servers.filter((s) => s.status === 'online').length;
  const uptime = mockServer.system.uptimeSeconds;

  const kpis = [
    { label: 'máquinas online', value: `${online}/${mockMachines.length}`, trendText: 'homelab', trend: online >= 2 ? 'up' : 'down' },
    { label: 'containers ativos', value: running, trendText: mockContainers.length - running > 0 ? `${mockContainers.length - running} parado` : 'todos ativos', trend: running >= 3 ? 'up' : 'down' },
    { label: 'serviços MCP', value: mcpOnline, unit: 'online', trendText: 'via /mcp', trend: 'up' },
    { label: 'uptime', value: formatDuration(uptime), trendText: mockServer.system.hostname, trend: 'up' },
  ];

  root.replaceChildren(...kpis.map(kpiCard));
}

/* ── Sistema│Rede ────────────────────────────────────────────────────── */

/** One key/value row in the info grid (mono value — data language). */
function infoRow(label, value, { mono = true, valueClass = '' } = {}) {
  const row = el('div', { class: 'info-row', attrs: { role: 'row' } });
  row.append(
    el('span', { class: 'info-label', text: label }),
    el('span', { class: `info-value${mono ? ' mono' : ''} ${valueClass}`.trim(), text: value }),
  );
  return row;
}

/** CPU / mem bars inside the system card. */
function renderSystem() {
  const root = qs('[data-system-card] .info-grid');
  if (!root) return;

  const { system } = mockServer;
  const memUsed = Math.round((system.memory / 100) * system.memoryTotal);

  const cpuBar = el('div', { class: 'info-progress' });
  const cpuLabel = el('div', { class: 'progress-label' });
  cpuLabel.append(
    el('span', { text: 'cpu' }),
    el('span', { text: formatPercent(system.cpu, 0) }),
  );
  const cpuTrack = el('div', { class: 'progress' });
  cpuTrack.append(el('div', { class: 'progress-fill blue', style: `width: ${clamp(system.cpu)}%` }));
  cpuBar.append(cpuLabel, cpuTrack);

  const memBar = el('div', { class: 'info-progress' });
  const memLabel = el('div', { class: 'progress-label' });
  memLabel.append(
    el('span', { text: 'memória' }),
    el('span', { text: formatPercent(system.memory, 0) }),
  );
  const memTrack = el('div', { class: 'progress' });
  memTrack.append(el('div', { class: 'progress-fill blue', style: `width: ${clamp(system.memory)}%` }));
  memBar.append(memLabel, memTrack);

  root.replaceChildren(
    infoRow('hostname', system.hostname),
    infoRow('os', system.os),
    infoRow('kernel', system.kernel),
    infoRow('cpu', system.cpuModel),
    infoRow('memória', `${formatBytes(memUsed)} / ${formatBytes(system.memoryTotal)}`),
    cpuBar,
    memBar,
  );
}

function renderNetwork() {
  const root = qs('[data-network-card] .info-grid');
  if (!root) return;

  const { network } = mockServer;
  const meta = qs('[data-system-meta]');
  if (meta) meta.textContent = `${mockServer.system.hostname} · tailscale`;

  root.replaceChildren(
    infoRow('tailscale', network.tailscaleIp, { valueClass: 'text-success' }),
    infoRow('node', network.tailscaleNode),
    infoRow('lan', network.localIp),
    infoRow('gateway', network.gateway),
    infoRow('interfaces', network.interfaces.join(', ')),
    infoRow('rx', formatPercent(network.rx, 0)),
  );
}

/* ── Discos ──────────────────────────────────────────────────────────── */

function renderDisks() {
  const root = qs('[data-disks-grid]');
  if (!root) return;

  const meta = qs('[data-disks-meta]');
  const worst = mockServer.disks.reduce((acc, d) => Math.max(acc, (d.used / d.total) * 100), 0);
  if (meta) meta.textContent = `pior uso ${formatPercent(worst, 0)}`;

  root.replaceChildren(...mockServer.disks.map((disk) => {
    const pct = clamp((disk.used / disk.total) * 100);
    const tone = pct >= 90 ? 'danger' : pct >= 80 ? 'warn' : 'blue';
    const card = el('div', { class: 'disk-card', attrs: { 'data-disk-mount': disk.mount } });

    const head = el('div', { class: 'disk-head' });
    head.append(
      el('span', { class: 'disk-mount mono', text: disk.mount }),
      el('span', { class: `disk-pct ${tone}`, text: formatPercent(pct, 0) }),
    );
    card.append(head);

    const bar = el('div', { class: 'progress' });
    bar.append(el('div', { class: `progress-fill ${tone === 'danger' ? 'critical' : tone === 'warn' ? 'warning' : 'blue'}`, style: `width: ${pct}%` }));
    card.append(bar);

    const foot = el('div', { class: 'disk-foot mono' });
    foot.append(
      el('span', { text: `${formatBytes(disk.used)} usados` }),
      el('span', { text: `${formatBytes(disk.total)} total` }),
    );
    card.append(foot);

    return card;
  }));
}

/* ── Containers ──────────────────────────────────────────────────────── */

function renderContainers() {
  const body = qs('[data-containers-body]');
  const count = qs('[data-containers-count]');
  if (!body) return;
  body.replaceChildren(...mockContainers.map((c) =>
    containerRow(c, {
      onRestart: (container) => toast(`restart ${container.name} (simulado)`, 'info'),
      onStop: (container) => toast(`stop ${container.name} (simulado)`, 'warning'),
    }),
  ));
  if (count) count.textContent = `${mockContainers.filter((c) => c.status === 'running').length} ativos`;
}

/* ── Máquinas ────────────────────────────────────────────────────────── */

function renderMachines() {
  const root = qs('[data-machines-list]');
  const count = qs('[data-machines-count]');
  if (!root) return;
  root.replaceChildren(...mockMachines.map((m) =>
    machineCard(m, {
      onWake: (machine) => toast(`WOL enviado para ${machine.hostname} (simulado)`, 'info'),
      onOpen: (machine) => {
        import('../components/Modal.js').then(({ openModal }) =>
          openModal({
            title: machine.hostname,
            // trustHtml: body é template estático com valores do mock (confiável hoje).
            // Quando os dados vierem da API real, usar nodes/textContent ou escapar cada campo.
            trustHtml: true,
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

/* ── Calendário│Criar projeto ────────────────────────────────────────── */

const WEEK_DAYS = ['seg', 'ter', 'qua', 'qui', 'sex', 'sáb', 'dom'];

function renderCalendar() {
  const root = qs('[data-calendar-week]');
  if (!root) return;

  const monday = new Date();
  const offset = (monday.getDay() + 6) % 7; // 0 = Monday
  monday.setDate(monday.getDate() - offset);

  const byDay = new Map();
  mockCalendarEvents.forEach((evt) => {
    if (!byDay.has(evt.dayOffset)) byDay.set(evt.dayOffset, []);
    byDay.get(evt.dayOffset).push(evt);
  });

  const days = [];
  for (let i = 0; i < 7; i += 1) {
    const date = new Date(monday);
    date.setDate(monday.getDate() + i);
    const today = new Date();
    const isToday =
      date.getDate() === today.getDate() &&
      date.getMonth() === today.getMonth() &&
      date.getFullYear() === today.getFullYear();

    const day = el('div', {
      class: `calendar-day${isToday ? ' today' : ''}`,
      attrs: { role: 'listitem' },
    });

    const head = el('div', { class: 'calendar-day-head' });
    head.append(
      el('span', { class: 'calendar-day-name', text: WEEK_DAYS[i] }),
      el('span', { class: `calendar-day-num${isToday ? ' today' : ''}`, text: String(date.getDate()).padStart(2, '0') }),
    );
    day.appendChild(head);

    const list = el('div', { class: 'calendar-day-events' });
    const dayEvents = byDay.get(i) ?? [];
    dayEvents.forEach((evt) => {
      const evtEl = el('div', { class: `calendar-event ${evt.tone}` });
      evtEl.append(
        el('span', { class: 'calendar-event-time mono', text: evt.time }),
        el('span', { class: 'calendar-event-title', text: evt.title }),
      );
      list.appendChild(evtEl);
    });
    if (!dayEvents.length) {
      list.append(el('span', { class: 'calendar-day-empty', text: '—' }));
    }
    day.appendChild(list);
    days.push(day);
  }

  root.replaceChildren(...days);
}

function renderProjectForm() {
  const select = qs('[data-project-form] select[name="language"]');
  if (!select) return;
  select.replaceChildren(...mockLanguages.map((lang) =>
    el('option', { attrs: { value: lang }, text: lang }),
  ));
}

function setupProjectForm() {
  const form = qs('[data-project-form]');
  if (!form) return;
  form.addEventListener('submit', (event) => {
    event.preventDefault();
    const name = form.elements.name?.value?.trim() ?? '';
    const language = form.elements.language?.value ?? '';
    const path = form.elements.path?.value?.trim() ?? '';

    if (!name) {
      toast('Informe um nome para o projeto.', 'error');
      return;
    }

    const button = qs('[data-project-submit]');
    if (button) {
      button.disabled = true;
      button.classList.add('loading');
    }
    setTimeout(() => {
      if (button) {
        button.disabled = false;
        button.classList.remove('loading');
      }
      toast(`Projeto ${name} criado (${language}) — simulado`, 'success');
      appendLiveLog({ type: 'success', message: `projeto ${name} criado · ${language}` });
      form.reset();
    }, 500);
  });
}

/* ── MCP Preview ─────────────────────────────────────────────────────── */

function renderMcpPreview() {
  const root = qs('[data-mcp-preview]');
  if (!root) return;
  root.replaceChildren(...mockMCP.servers.map((s) => mcpCard(s)));
}

/* ── Logs ao vivo ────────────────────────────────────────────────────── */

const LOG_TYPES = ['success', 'error', 'info', 'warn'];
let liveLogTimer = null;

/** Current time as HH:MM:SS. */
function nowTime() {
  return new Date().toTimeString().slice(0, 8);
}

function logEntry({ time, type, message }) {
  const entry = el('div', { class: `log-entry ${type}` });
  entry.append(
    el('span', { class: 'log-time mono', text: time }),
    el('span', { class: `log-type mono`, text: `[${type}]` }),
    el('span', { class: 'log-message mono', text: message }),
  );
  return entry;
}

function renderLiveLogs() {
  const root = qs('[data-live-logs]');
  if (!root) return;
  root.replaceChildren(...mockLogs.map(logEntry));
}

function appendLiveLog({ type, message }) {
  const root = qs('[data-live-logs]');
  if (!root) return;
  const entry = logEntry({ time: nowTime(), type: LOG_TYPES.includes(type) ? type : 'info', message });
  root.appendChild(entry);
  // Keep the list bounded — drop the oldest entries.
  while (root.children.length > 50) root.firstElementChild?.remove();
  root.scrollTop = root.scrollHeight;
}

function startLiveLogs() {
  const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  if (reduceMotion) return; // Live feed is motion — respect reduced motion.
  stopLiveLogs();
  liveLogTimer = setInterval(() => {
    // Pause while the tab is hidden — no need to burn cycles.
    if (document.hidden) return;
    appendLiveLog(randomLogEntry());
  }, 3000);
}

function stopLiveLogs() {
  if (liveLogTimer) {
    clearInterval(liveLogTimer);
    liveLogTimer = null;
  }
}

/* ── Init ────────────────────────────────────────────────────────────── */

onReady(async () => {
  // Protect page: no local session → login.
  await protectPage();

  renderHeader('[data-header]', { title: 'Coffee Server', active: 'dashboard', address: 'server/home' });
  renderStatusBar('[data-statusbar]', { uptimeSeconds: mockServer.system.uptimeSeconds });
  renderMiniPanel('[data-mini-panel]', { active: 'dashboard' });
  renderServerStatus('[data-server-status]');

  setupToastListener();
  setupWsListeners({ onOpen: () => updateStatusBarState('open'), onClose: () => updateStatusBarState('closed'), onMessage: () => {} });
  startWs();

  renderUserCard();
  renderKpiRow();
  renderSystem();
  renderNetwork();
  renderDisks();
  renderContainers();
  renderMachines();
  renderCalendar();
  renderProjectForm();
  setupProjectForm();
  renderMcpPreview();
  renderLiveLogs();
  startLiveLogs();

  document.addEventListener('visibilitychange', () => {
    if (document.hidden) stopLiveLogs();
    else startLiveLogs();
  });

  // Refresh button — re-renders with skeleton shimmer for realism.
  const refreshBtn = qs('[data-refresh]');
  refreshBtn?.addEventListener('click', async () => {
    refreshBtn.disabled = true;
    refreshBtn.classList.add('loading');
    await new Promise((r) => setTimeout(r, 500));
    renderKpiRow();
    renderSystem();
    renderNetwork();
    renderDisks();
    renderContainers();
    renderMachines();
    renderCalendar();
    renderMcpPreview();
    refreshBtn.disabled = false;
    refreshBtn.classList.remove('loading');
  });
});

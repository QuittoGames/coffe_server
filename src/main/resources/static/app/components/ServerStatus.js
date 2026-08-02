/**
 * Coffee Server Dashboard — Server Status Component
 * Console strip: preserved terminal prompt as thesis + service chips
 * with LED state and latency + a single activity sparkline (SVG).
 *
 * This is the instrument panel of the homelab: one place where
 * the machine's pulse lives. Everything else on the page stays quiet.
 */

import { el, qs } from '../utils/dom.js';

const DEFAULT_SERVICES = [
  { name: 'postgres',  state: 'up',     latency: '21ms', led: 'ok' },
  { name: 'redis',     state: 'up',     latency: '3ms',  led: 'ok' },
  { name: 'mcp',       state: 'online', latency: '—',    led: 'ok' },
  { name: 'tailscale', state: 'up',     latency: '41ms', led: 'ok' },
];

/** Build a minimal sparkline as an SVG string (values are numbers — safe). */
function sparkline(values, { width = 320, height = 36, pad = 4 } = {}) {
  if (!values || values.length < 2) return '';
  const min = Math.min(...values);
  const max = Math.max(...values);
  const span = max - min || 1;
  const stepX = (width - pad * 2) / (values.length - 1);

  const points = values.map((v, i) => {
    const x = pad + i * stepX;
    const y = pad + (height - pad * 2) * (1 - (v - min) / span);
    return `${x.toFixed(1)},${y.toFixed(1)}`;
  });

  const polyline = points.join(' ');
  const area = `${pad},${height - pad} ${polyline} ${width - pad},${height - pad}`;

  return `
    <svg class="console-spark" viewBox="0 0 ${width} ${height}" preserveAspectRatio="none"
         aria-hidden="true" focusable="false" role="img">
      <path class="console-spark-grid" d="M ${pad} ${height / 2} L ${width - pad} ${height / 2}"/>
      <polygon class="console-spark-area" points="${area}"/>
      <polyline class="console-spark-line" points="${polyline}"/>
    </svg>
  `;
}

/**
 * Render the console strip into a placeholder element.
 * @param {HTMLElement|string} target - element or selector
 * @param {Object} options
 * @param {string} options.prompt  - terminal prompt line (default 'quitto@quittoserver:~$ coffee server status')
 * @param {string} options.command - the typed command
 * @param {Array<{name,state,latency,led}>} options.services - service chips
 * @param {number[]} options.activity - sparkline values (activity over time)
 * @param {string} options.activityLabel - label under the sparkline
 */
export function renderServerStatus(target, {
  prompt = 'quitto@quittoserver:~$',
  command = 'coffee server status',
  services = DEFAULT_SERVICES,
  activity = [10, 14, 12, 20, 26, 22, 30, 34, 28, 40, 44, 38, 52, 48, 60, 55, 70, 66, 78],
  activityLabel = 'atividade · 24h',
} = {}) {
  const root = typeof target === 'string' ? qs(target) : target;
  if (!root) return null;

  const strip = el('div', { class: 'console-strip', attrs: { 'data-server-status': '' } });

  // Head: preserved prompt (thesis) + overall state badge
  const head = el('div', { class: 'console-head' });
  const promptEl = el('div', { class: 'console-prompt' });
  promptEl.append(
    el('span', { class: 'console-caret', text: '❯', attrs: { 'aria-hidden': 'true' } }),
    el('span', { class: 'console-prompt-user', text: prompt }),
    el('span', { class: 'console-prompt-cmd', text: ` ${command}` }),
  );
  head.append(
    promptEl,
    el('span', { class: 'console-badge', html: `<span class="console-badge-dot"></span> healthy` }),
  );
  strip.append(head);

  // Service chips
  const chips = el('div', { class: 'console-services', attrs: { role: 'list' } });
  services.forEach((svc) => {
    const chip = el('div', { class: 'console-chip', attrs: { role: 'listitem', title: `${svc.name}: ${svc.state}` } });
    chip.append(
      el('span', { class: `console-led ${svc.led || 'ok'}`, attrs: { 'aria-hidden': 'true' } }),
      el('span', { class: 'console-chip-name', text: svc.name }),
      el('span', { class: 'console-chip-latency', text: svc.latency }),
      el('span', { class: `console-chip-state ${svc.state}`, text: svc.state }),
    );
    chips.appendChild(chip);
  });
  strip.append(chips);

  // Sparkline footer — the single pulse on the page
  const chart = el('div', { class: 'console-chart' });
  chart.innerHTML = sparkline(activity);
  strip.append(chart);

  if (activityLabel) {
    strip.append(el('div', { class: 'console-chart-label', text: activityLabel }));
  }

  root.replaceChildren(strip);
  return strip;
}

export default { renderServerStatus };

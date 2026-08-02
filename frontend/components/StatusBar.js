/**
 * Coffee Server Dashboard — StatusBar Component
 * Bottom bar: connection dot, version, uptime, link to health page.
 */

import { el, qs } from '../utils/dom.js';
import { APP_NAME, APP_VERSION } from '../utils/constants.js';
import { formatDuration } from '../utils/format.js';

/** Render the status bar into a placeholder element. */
export function renderStatusBar(target, { uptimeSeconds = null } = {}) {
  const root = typeof target === 'string' ? qs(target) : target;
  if (!root) return;

  const bar = el('footer', { class: 'statusbar' });

  const conn = el('span', { class: 'statusbar-item' });
  conn.append(
    el('span', { class: 'status-dot ok', attrs: { 'data-statusbar-dot': '' } }),
    el('span', { attrs: { 'data-statusbar-text': '' }, text: 'connected' }),
  );

  const version = el('span', { class: 'statusbar-item', text: `${APP_NAME} v${APP_VERSION}` });
  const uptime = el('span', { class: 'statusbar-item', text: `uptime ${formatDuration(uptimeSeconds)}` });

  const right = el('div', { class: 'statusbar-right' });
  right.append(version, uptime);

  bar.append(conn, right);
  root.replaceChildren(bar);
  return bar;
}

/** Update connection state in the status bar. */
export function updateStatusBarState(state) {
  const dot = qs('[data-statusbar-dot]');
  const text = qs('[data-statusbar-text]');
  if (!dot || !text) return;
  if (state === 'open') {
    dot.classList.add('ok');
    dot.classList.remove('busy', 'err');
    text.textContent = 'connected';
  } else if (state === 'connecting') {
    dot.classList.add('busy');
    dot.classList.remove('ok', 'err');
    text.textContent = 'connecting';
  } else {
    dot.classList.add('err');
    dot.classList.remove('ok', 'busy');
    text.textContent = 'disconnected';
  }
}

export default { renderStatusBar, updateStatusBarState };

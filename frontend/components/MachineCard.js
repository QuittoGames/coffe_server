/**
 * Coffee Server Dashboard — MachineCard Component
 * Displays one machine: status dot, hostname, meta (IP/OS/tailscale), actions.
 */

import { el } from '../utils/dom.js';
import { icon } from '../utils/icons.js';
import { formatBytes, formatDuration } from '../utils/format.js';

/**
 * Build a machine card/list item.
 * @param {Object} machine - normalized machine object
 * @param {Object} options - { onWake, onOpen }
 * @returns {HTMLElement}
 */
export function machineCard(machine, { onWake = null, onOpen = null } = {}) {
  const statusClass =
    machine.status === 'online' ? 'online'
    : machine.status === 'standby' ? 'standby'
    : 'offline';

  const item = el('div', {
    class: 'machine-item',
    attrs: { role: 'listitem', 'data-machine-id': machine.id },
  });

  item.append(el('span', { class: `machine-status ${statusClass}`, attrs: { 'aria-hidden': 'true' } }));

  const info = el('div', { class: 'machine-info' });
  const name = el('div', { class: 'machine-name' });
  name.append(
    el('span', { text: machine.hostname }),
    el('span', { class: 'tag', text: machine.os }),
  );

  const metaParts = [machine.currentIp ?? 'no ip', machine.tailscaleNodeKey ? `ts:${machine.tailscaleNodeKey}` : ''];
  if (machine.status === 'online' && machine.uptimeSeconds) {
    metaParts.push(`up ${formatDuration(machine.uptimeSeconds)}`);
  }
  info.append(name, el('div', { class: 'machine-meta', text: metaParts.filter(Boolean).join('  ·  ') }));
  item.appendChild(info);

  const actions = el('div', { class: 'machine-actions' });
  if (machine.wolEnabled && onWake) {
    const wakeBtn = el('button', {
      class: 'icon-btn',
      attrs: { type: 'button', 'aria-label': `Wake ${machine.hostname}`, title: 'Wake on LAN' },
    });
    wakeBtn.append(icon('power', { size: 16, title: 'Wake on LAN' }));
    wakeBtn.addEventListener('click', (event) => {
      event.stopPropagation();
      onWake(machine);
    });
    actions.appendChild(wakeBtn);
  }

  if (onOpen) {
    const openBtn = el('button', {
      class: 'icon-btn',
      attrs: { type: 'button', 'aria-label': `Abrir ${machine.hostname}`, title: 'Detalhes' },
    });
    openBtn.append(icon('arrow-up-right', { size: 16, title: 'Detalhes' }));
    openBtn.addEventListener('click', (event) => {
      event.stopPropagation();
      onOpen(machine);
    });
    actions.appendChild(openBtn);
  }

  item.appendChild(actions);

  if (onOpen) {
    item.addEventListener('click', () => onOpen(machine));
  }

  return item;
}

export default { machineCard };

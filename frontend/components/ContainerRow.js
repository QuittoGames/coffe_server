/**
 * Coffee Server Dashboard — ContainerRow Component
 * Table row for a container: name/image, status badge, port, CPU/mem.
 */

import { el } from '../utils/dom.js';
import { icon } from '../utils/icons.js';

/**
 * Build a table row for a container.
 * @param {Object} container - normalized container object
 * @param {Object} options - { onRestart, onStop }
 * @returns {HTMLTableRowElement}
 */
export function containerRow(container, { onRestart = null, onStop = null } = {}) {
  const tr = el('tr', { attrs: { 'data-container-id': container.id } });

  const nameCell = el('td');
  const nameWrap = el('div', { class: 'flex flex-col gap-1' });
  nameWrap.append(
    el('span', { class: 'text-primary', text: container.name }),
    el('span', { class: 'tag mono', text: container.image }),
  );
  nameCell.appendChild(nameWrap);

  const statusCell = el('td');
  const running = container.status === 'running';
  statusCell.appendChild(
    el('span', {
      class: `badge ${running ? 'badge-online badge-pulse' : 'badge-offline'}`,
      text: running ? 'running' : 'stopped',
    }),
  );

  const stateCell = el('td', { class: 'mono', text: container.state ?? '—' });
  const portCell = el('td', { class: 'mono', text: container.port ?? '—' });

  const metricsCell = el('td');
  metricsCell.append(
    el('span', { class: 'text-dim', text: `cpu ${container.cpu ?? 0}%` }),
    el('span', { class: 'text-dim', text: ` · mem ${container.memory ?? 0}%` }),
  );

  const actionsCell = el('td');
  const actions = el('div', { class: 'flex gap-2 justify-end' });
  if (onRestart && running) {
    const restartBtn = el('button', {
      class: 'btn btn-sm btn-ghost',
      attrs: { type: 'button' },
      text: 'restart',
    });
    restartBtn.prepend(icon('rotate-ccw', { size: 14, title: 'Reiniciar' }));
    restartBtn.addEventListener('click', () => onRestart(container));
    actions.appendChild(restartBtn);
  }
  if (onStop && running) {
    const stopBtn = el('button', {
      class: 'btn btn-sm btn-ghost text-error',
      attrs: { type: 'button' },
      text: 'stop',
    });
    stopBtn.prepend(icon('square', { size: 14, title: 'Parar' }));
    stopBtn.addEventListener('click', () => onStop(container));
    actions.appendChild(stopBtn);
  }
  actionsCell.appendChild(actions);

  tr.append(nameCell, statusCell, stateCell, portCell, metricsCell, actionsCell);
  return tr;
}

export default { containerRow };

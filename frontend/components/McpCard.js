/**
 * Coffee Server Dashboard — MCPCard Component
 * Card for an MCP server: name, status, description, tool chips, stats.
 */

import { el } from '../utils/dom.js';
import { icon } from '../utils/icons.js';
import { timeAgo } from '../utils/format.js';

/**
 * Build an MCP server card.
 * @param {Object} server - normalized MCP server object
 * @param {Object} options - { onOpen }
 * @returns {HTMLElement}
 */
export function mcpCard(server, { onOpen = null } = {}) {
  const card = el('div', { class: 'mcp-card', attrs: { 'data-mcp-id': server.id } });

  const header = el('div', { class: 'mcp-card-header' });
  const title = el('div', { class: 'mcp-card-title' });
  title.append(el('span', { text: server.name }));

  const statusBadge =
    server.status === 'online'
      ? el('span', { class: 'badge badge-online badge-pulse', text: 'online' })
      : server.status === 'planned'
        ? el('span', { class: 'badge badge-idle', text: 'planned' })
        : el('span', { class: 'badge badge-offline', text: 'offline' });
  header.append(title, statusBadge);

  card.append(header, el('p', { class: 'mcp-card-desc', text: server.description }));

  if (server.tools.length > 0) {
    const tools = el('div', { class: 'mcp-card-tools' });
    server.tools.forEach((tool) => tools.appendChild(el('span', { class: 'tool-chip', text: tool })));
    card.appendChild(tools);
  }

  const footer = el('div', { class: 'mcp-card-footer' });
  const stats = el('div', { class: 'mcp-card-stats' });
  stats.append(
    el('span', { text: `${server.calls ?? 0} calls` }),
    el('span', { text: server.lastCall ? ` · last ${timeAgo(server.lastCall)}` : ' · never called' }),
  );
  footer.appendChild(stats);

  if (onOpen) {
    const openBtn = el('button', { class: 'btn btn-sm btn-ghost', attrs: { type: 'button' }, text: 'abrir' });
    openBtn.prepend(icon('arrow-up-right', { size: 14, title: 'Abrir' }));
    openBtn.addEventListener('click', () => onOpen(server));
    footer.appendChild(openBtn);
  }

  card.appendChild(footer);
  return card;
}

export default { mcpCard };

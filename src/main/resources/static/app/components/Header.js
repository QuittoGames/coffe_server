/**
 * Coffee Server Dashboard — Header Component
 * Liquid-glass toolbar: logo, fake server address, primary nav, status, user menu.
 */

import { el, qs, onReady } from '../utils/dom.js';
import { icon } from '../utils/icons.js';
import { getUser } from '../services/auth.js';
import { setupLogoutButtons } from '../auth/logout.js';

const NAV_ITEMS = [
  { id: 'dashboard', label: 'Dashboard', icon: 'layout-dashboard', href: './dashboard.html' },
  { id: 'machines', label: 'Máquinas', icon: 'server', href: './dashboard.html#machines' },
  { id: 'containers', label: 'Containers', icon: 'box', href: './dashboard.html#containers' },
  { id: 'mcp', label: 'MCP', icon: 'brain-circuit', href: './mcp.html' },
];

/**
 * Render the toolbar header into a placeholder element.
 * @param {HTMLElement|string} target - element or selector
 * @param {Object} options
 * @param {string} options.title - logo text (default 'Coffee Server')
 * @param {string} options.active - active nav item id (default 'dashboard')
 * @param {string} options.address - fake server address (default 'server/home')
 */
export function renderHeader(target, { title = 'Coffee Server', active = 'dashboard', address = 'server/home' } = {}) {
  const root = typeof target === 'string' ? qs(target) : target;
  if (!root) return;

  const user = getUser();

  const header = el('header', { class: 'header glass' });

  // Zona esquerda: logo + endereço fake
  const left = el('div', { class: 'header-left' });

  const logoLink = el('a', { class: 'header-logo', attrs: { href: './dashboard.html', 'aria-label': 'Dashboard' } });
  logoLink.append(
    el('img', {
      class: 'header-logo-img',
      attrs: { src: '../assets/Logo.png', alt: 'Coffee Server logo' },
    }),
    el('span', {
      class: 'header-logo-text',
      html: `Coffee <span class="accent">Server</span>`,
    }),
  );

  // Fake server address — like a terminal prompt: server/<page>
  const addr = el('a', {
    class: 'header-address',
    attrs: { href: './dashboard.html', title: 'Endereço do servidor (simulado)' },
  });
  addr.append(
    el('span', { class: 'addr-prompt', text: '❯' }),
    el('span', { class: 'addr-path', html: `<span class="addr-host">server</span><span class="addr-sep">/</span><span class="addr-page">${escapeHtml(address.replace(/^server\//, ''))}</span>` }),
  );
  left.append(logoLink, addr);
  header.append(left);

  // Zona central: nav primária
  const nav = el('nav', { class: 'toolbar-nav', attrs: { 'aria-label': 'Navegação principal' } });
  NAV_ITEMS.forEach((item) => {
    const link = el('a', {
      class: `toolbar-link${item.id === active ? ' active' : ''}`,
      attrs: { href: item.href, 'aria-current': item.id === active ? 'page' : null },
    });
    link.append(
      icon(item.icon, { size: 16, class: 'toolbar-icon' }),
      el('span', { class: 'toolbar-label', text: item.label }),
    );
    nav.appendChild(link);
  });
  header.append(nav);

  // Zona direita: status + usuário
  const actions = el('div', { class: 'header-actions' });

  const status = el('span', { class: 'header-status', attrs: { title: 'Conexão com o servidor' } });
  status.append(
    el('span', { class: 'status-dot', attrs: { 'data-status-dot': '' } }),
    el('span', { attrs: { 'data-status-text': '' }, text: 'online' }),
  );
  actions.appendChild(status);

  if (user) {
    const userMenu = el('div', { class: 'dropdown' });
    const avatarBtn = el('button', {
      class: 'btn btn-sm btn-ghost',
      attrs: { type: 'button', 'aria-haspopup': 'menu', 'data-dropdown-toggle': '' },
    });
    avatarBtn.append(
      icon('user', { size: 16, class: 'btn-icon' }),
      el('span', { text: user.name }),
    );

    const menu = el('div', { class: 'dropdown-menu hidden', attrs: { role: 'menu' } });
    const logoutItem = el('button', {
      class: 'dropdown-item',
      attrs: { type: 'button', role: 'menuitem', 'data-logout': '' },
      text: 'Logout',
    });
    logoutItem.prepend(icon('log-out', { size: 16, title: 'Sair' }));
    menu.append(
      el('div', { class: 'dropdown-label', text: user.role ?? 'USER' }),
      logoutItem,
    );
    userMenu.append(avatarBtn, menu);
    actions.appendChild(userMenu);

    // Toggle dropdown
    avatarBtn.addEventListener('click', (event) => {
      event.stopPropagation();
      menu.classList.toggle('hidden');
    });
    document.addEventListener('click', () => menu.classList.add('hidden'));
  }

  header.append(actions);
  root.replaceChildren(header);

  setupLogoutButtons();
  return header;
}

/** Update the header status pill (e.g. from WS connection state). */
export function updateHeaderStatus(state) {
  const dot = qs('[data-status-dot]');
  const text = qs('[data-status-text]');
  if (!dot || !text) return;
  if (state === 'online') {
    dot.classList.remove('offline');
    text.textContent = 'online';
  } else {
    dot.classList.add('offline');
    text.textContent = state === 'connecting' ? 'connecting' : 'offline';
  }
}

function escapeHtml(value) {
  return String(value ?? '').replace(/[&<>"']/g, (c) => ({
    '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;',
  })[c]);
}

export default { renderHeader, updateHeaderStatus };

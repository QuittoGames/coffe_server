/**
 * Coffee Server Dashboard — Modal Component
 * Accessible modal: overlay + dialog, ESC to close, focus trap basics.
 */

import { el, qs, onReady } from '../utils/dom.js';

let lastFocused = null;

/**
 * Open a modal with custom content.
 * @param {Object} options
 * @param {string} options.title
 * @param {HTMLElement|string} options.body - node or HTML string
 * @param {HTMLElement[]} options.footer - footer buttons
 * @param {Function} options.onClose
 * @returns {HTMLElement} the modal node
 */
export function openModal({ title, body, footer = [], onClose = null }) {
  lastFocused = document.activeElement;

  const overlay = el('div', { class: 'modal-overlay', attrs: { role: 'presentation' } });
  const modal = el('div', {
    class: 'modal',
    attrs: { role: 'dialog', 'aria-modal': 'true', 'aria-label': title },
  });

  const header = el('div', { class: 'modal-header' });
  const titleNode = el('div', {
    class: 'modal-title',
    html: `<span class="terminal-prefix">❯ </span>${escapeTitle(title)}`,
  });
  const closeBtn = el('button', {
    class: 'modal-close',
    attrs: { 'aria-label': 'Fechar', type: 'button' },
    text: '×',
  });
  closeBtn.addEventListener('click', () => closeModal());
  header.append(titleNode, closeBtn);

  const bodyNode = el('div', { class: 'modal-body' });
  if (typeof body === 'string') bodyNode.innerHTML = body;
  else if (body) bodyNode.appendChild(body);

  modal.append(header, bodyNode);

  if (footer.length > 0) {
    const footerNode = el('div', { class: 'modal-footer' });
    footer.forEach((btn) => footerNode.appendChild(btn));
    modal.appendChild(footerNode);
  }

  overlay.appendChild(modal);
  document.body.appendChild(overlay);

  function closeModal() {
    overlay.remove();
    document.removeEventListener('keydown', onKeydown);
    if (lastFocused) lastFocused.focus?.();
    onClose?.();
  }

  function onKeydown(event) {
    if (event.key === 'Escape') {
      closeModal();
      return;
    }
    if (event.key === 'Tab') {
      // Simple focus containment within the modal.
      const focusables = qs('button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])', modal);
      const list = Array.from(focusables).filter((n) => !n.disabled);
      if (list.length === 0) return;
      const first = list[0];
      const last = list[list.length - 1];
      if (event.shiftKey && document.activeElement === first) {
        event.preventDefault();
        last.focus();
      } else if (!event.shiftKey && document.activeElement === last) {
        event.preventDefault();
        first.focus();
      }
    }
  }

  document.addEventListener('keydown', onKeydown);
  overlay.addEventListener('click', (event) => {
    if (event.target === overlay) closeModal();
  });

  // Focus the first focusable after a tick.
  onReady(() => {
    const first = qs('button, [href], input, select, textarea', modal);
    first?.focus();
  });

  return modal;
}

function escapeTitle(title) {
  return String(title ?? '').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

export default { openModal };

/**
 * Coffee Server Dashboard — Toast Component
 * Non-blocking notification toasts. Create container on first use.
 */

import { el } from '../utils/dom.js';
import { icon } from '../utils/icons.js';
import { EVENTS } from '../utils/constants.js';

let container = null;
const TOAST_DURATION_MS = 4000;

function getContainer() {
  if (!container) {
    container = el('div', { class: 'toast-container', attrs: { 'aria-live': 'polite' } });
    document.body.appendChild(container);
  }
  return container;
}

/**
 * Show a toast notification.
 * @param {string} message
 * @param {'success'|'error'|'info'|'warning'} type
 * @param {Object} options
 */
export function toast(message, type = 'info', { duration = TOAST_DURATION_MS } = {}) {
  const node = el('div', { class: `toast toast-${type}` });
  const text = el('span', { text: message });
  const close = el('button', {
    class: 'toast-close',
    attrs: { 'aria-label': 'Fechar' },
  });
  close.append(icon('x', { size: 16 }));
  close.addEventListener('click', () => dismiss(node));
  node.append(text, close);

  getContainer().appendChild(node);

  const timer = setTimeout(() => dismiss(node), duration);
  node.addEventListener('mouseenter', () => clearTimeout(timer));
  node.addEventListener('mouseleave', () => setTimeout(() => dismiss(node), duration));
}

function dismiss(node) {
  node.style.transition = 'opacity var(--duration-normal), transform var(--duration-normal)';
  node.style.opacity = '0';
  node.style.transform = 'translateX(12px)';
  setTimeout(() => node.remove(), 200);
}

/** Global event listener so any module can `dispatchEvent(new CustomEvent(EVENTS.TOAST, ...))`. */
export function setupToastListener() {
  window.addEventListener(EVENTS.TOAST, (event) => {
    const { message, type, duration } = event.detail ?? {};
    toast(message, type, { duration });
  });
}

/**
 * Coffee Server Dashboard — DOM Helpers
 * Small, dependency-free helpers for querying and building DOM.
 */

/** Query a single element; returns null when absent. */
export const qs = (selector, root = document) => root.querySelector(selector);

/** Query multiple elements as an array. */
export const qsa = (selector, root = document) =>
  Array.from(root.querySelectorAll(selector));

/** Create an element with class names, attributes, and children. */
export function el(tag, { class: className = '', attrs = {}, text = '', html = '' } = {}) {
  const node = document.createElement(tag);
  if (className) node.className = className;
  for (const [key, value] of Object.entries(attrs)) {
    if (value === null || value === undefined) continue;
    if (key === 'dataset') Object.assign(node.dataset, value);
    else if (key === 'aria') Object.assign(node.aria, value);
    else node.setAttribute(key, value);
  }
  if (text) node.textContent = text;
  if (html) node.innerHTML = html;
  return node;
}

/** Set text of an element only if it exists. */
export function setText(node, text) {
  if (node) node.textContent = text;
}

/** Toggle a class on a node; returns the new state. */
export function toggleClass(node, className, force) {
  if (!node) return force;
  const next = force === undefined ? !node.classList.contains(className) : force;
  node.classList.toggle(className, next);
  return next;
}

/** Show/hide a node using the `.hidden` utility. */
export function setHidden(node, hidden) {
  if (!node) return;
  toggleClass(node, 'hidden', hidden);
}

/** Escape user content to avoid XSS when injecting HTML. */
export function escapeHtml(value) {
  return String(value ?? '')
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

/** Run once the DOM is ready. Returns a promise. */
export function onReady(fn) {
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => fn(), { once: true });
  } else {
    fn();
  }
}

/** Debounce a function (e.g. search inputs). */
export function debounce(fn, delay = 200) {
  let timer;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => fn(...args), delay);
  };
}

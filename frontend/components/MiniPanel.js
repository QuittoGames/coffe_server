/**
 * Coffee Server Dashboard - MiniPanel Component (legacy)
 *
 * A navegação primária agora vive na toolbar do header (components/Header.js).
 * Este componente é mantido apenas para compatibilidade de API: as páginas
 * ainda chamam renderMiniPanel(), mas ele não renderiza mais a segunda barra.
 */

import { qs } from '../utils/dom.js';

export function renderMiniPanel(target) {
  const root = typeof target === 'string' ? qs(target) : target;
  if (!root) return;
  // No-op: a toolbar do header já contém a navegação.
  root.replaceChildren();
  return root;
}

/** Kept for API compatibility. */
export function togglePanel() {
  // no-op
}

export default { renderMiniPanel, togglePanel };

/**
 * Coffee Server Dashboard — Skeleton Component
 * Placeholder loading states matching the real component shapes.
 */

import { el } from '../utils/dom.js';

/** Skeleton card block with header + rows. */
export function skeletonCard({ rows = 3 } = {}) {
  const card = el('div', { class: 'card' });
  card.append(el('div', { class: 'skeleton skeleton-title' }));
  for (let i = 0; i < rows; i += 1) {
    card.append(el('div', { class: 'skeleton skeleton-text', attrs: { style: `margin-top:${i ? 8 : 12}px; width:${80 - i * 15}%` } }));
  }
  return card;
}

/** Skeleton machine list item. */
export function skeletonListItem() {
  const item = el('div', { class: 'list-item' });
  item.append(el('div', { class: 'skeleton skeleton-avatar' }));
  const body = el('div', { class: 'flex-1' });
  body.append(el('div', { class: 'skeleton skeleton-text', attrs: { style: 'width:40%' } }));
  body.append(el('div', { class: 'skeleton skeleton-text', attrs: { style: 'width:65%; margin-top:8px; height:12px' } }));
  item.appendChild(body);
  return item;
}

/** Fill a container with skeletons. */
export function renderSkeletons(container, count = 3, builder = skeletonCard) {
  container.replaceChildren();
  for (let i = 0; i < count; i += 1) {
    container.appendChild(builder());
  }
}

export default { skeletonCard, skeletonListItem, renderSkeletons };

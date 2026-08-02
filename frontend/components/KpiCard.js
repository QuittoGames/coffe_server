/**
 * Coffee Server Dashboard — KpiCard Component
 * Stat/KPI card: label, value with unit, optional trend line.
 */

import { el } from '../utils/dom.js';

/**
 * Build a KPI stat card.
 * @param {Object} kpi
 * @param {string} kpi.label
 * @param {string|number} kpi.value
 * @param {string} [kpi.unit]
 * @param {string} [kpi.trendText] e.g. '+2.1%'
 * @param {'up'|'down'|'flat'} [kpi.trend]
 * @param {string} [kpi.class] extra class on the value (e.g. 'blue', 'coffee')
 * @returns {HTMLElement}
 */
export function kpiCard({ label, value, unit = '', trendText = '', trend = 'flat', class: valueClass = '' } = {}) {
  const item = el('div', { class: 'kpi-item' });

  item.append(el('div', { class: 'kpi-label', text: label }));

  const valueNode = el('div', { class: 'kpi-value' });
  valueNode.append(
    el('span', { class: valueClass ? `stat-value ${valueClass}` : '', text: String(value ?? '—') }),
    unit ? el('span', { class: 'kpi-unit', text: unit }) : null,
  );
  item.appendChild(valueNode);

  if (trendText) {
    const trendNode = el('div', { class: `kpi-trend ${trend === 'up' ? 'up' : trend === 'down' ? 'down' : ''}` });
    trendNode.append(el('span', { text: trend === 'up' ? '▲' : trend === 'down' ? '▼' : '•' }), el('span', { text: trendText }));
    item.appendChild(trendNode);
  }

  return item;
}

export default { kpiCard };

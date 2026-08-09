/**
 * Coffee Server Dashboard — Icon System
 * Self-contained inline SVG icons (Lucide v1.28.0, ISC license).
 * No CDN, no build step: paths are bundled here so the dashboard works offline.
 *
 * Usage:
 *   import { icon, iconHtml } from '../utils/icons.js';
 *   button.append(icon('power', { class: 'icon', title: 'Wake on LAN' }));
 *   button.append(icon('user', { size: 16, class: 'btn-icon' }));
 *
 * License: Lucide icons are ISC licensed (https://lucide.dev/license).
 */

/* Each entry: [tag, attributes] as shipped by lucide v1.28.0. */
const ICONS = {
  'layout-dashboard': [
    ['rect', { width: '7', height: '9', x: '3', y: '3', rx: '1' }],
    ['rect', { width: '7', height: '5', x: '14', y: '3', rx: '1' }],
    ['rect', { width: '7', height: '9', x: '14', y: '12', rx: '1' }],
    ['rect', { width: '7', height: '5', x: '3', y: '16', rx: '1' }],
  ],
  server: [
    ['rect', { width: '20', height: '8', x: '2', y: '2', rx: '2', ry: '2' }],
    ['rect', { width: '20', height: '8', x: '2', y: '14', rx: '2', ry: '2' }],
    ['line', { x1: '6', x2: '6.01', y1: '6', y2: '6' }],
    ['line', { x1: '6', x2: '6.01', y1: '18', y2: '18' }],
  ],
  box: [
    ['path', { d: 'M21 8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16Z' }],
    ['path', { d: 'm3.3 7 8.7 5 8.7-5' }],
    ['path', { d: 'M12 22V12' }],
  ],
  'brain-circuit': [
    ['path', { d: 'M12 5a3 3 0 1 0-5.997.125 4 4 0 0 0-2.526 5.77 4 4 0 0 0 .556 6.588A4 4 0 1 0 12 18Z' }],
    ['path', { d: 'M9 13a4.5 4.5 0 0 0 3-4' }],
    ['path', { d: 'M6.003 5.125A3 3 0 0 0 6.401 6.5' }],
    ['path', { d: 'M3.477 10.896a4 4 0 0 1 .585-.396' }],
    ['path', { d: 'M6 18a4 4 0 0 1-1.967-.516' }],
    ['path', { d: 'M12 13h4' }],
    ['path', { d: 'M12 18h6a2 2 0 0 1 2 2v1' }],
    ['path', { d: 'M12 8h8' }],
    ['path', { d: 'M16 8V5a2 2 0 0 1 2-2' }],
    ['circle', { cx: '16', cy: '13', r: '.5' }],
    ['circle', { cx: '18', cy: '3', r: '.5' }],
    ['circle', { cx: '20', cy: '21', r: '.5' }],
    ['circle', { cx: '20', cy: '8', r: '.5' }],
  ],
  user: [
    ['path', { d: 'M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2' }],
    ['circle', { cx: '12', cy: '7', r: '4' }],
  ],
  'log-out': [
    ['path', { d: 'm16 17 5-5-5-5' }],
    ['path', { d: 'M21 12H9' }],
    ['path', { d: 'M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4' }],
  ],
  power: [
    ['path', { d: 'M12 2v10' }],
    ['path', { d: 'M18.4 6.6a9 9 0 1 1-12.77.04' }],
  ],
  'arrow-up-right': [
    ['path', { d: 'M7 7h10v10' }],
    ['path', { d: 'M7 17 17 7' }],
  ],
  ellipsis: [
    ['circle', { cx: '12', cy: '12', r: '1' }],
    ['circle', { cx: '19', cy: '12', r: '1' }],
    ['circle', { cx: '5', cy: '12', r: '1' }],
  ],
  x: [
    ['path', { d: 'M18 6 6 18' }],
    ['path', { d: 'm6 6 12 12' }],
  ],
  'chevron-right': [
    ['path', { d: 'm9 18 6-6-6-6' }],
  ],
  'arrow-up': [
    ['path', { d: 'm5 12 7-7 7 7' }],
    ['path', { d: 'M12 19V5' }],
  ],
  'arrow-down': [
    ['path', { d: 'M12 5v14' }],
    ['path', { d: 'm19 12-7 7-7-7' }],
  ],
  minus: [
    ['path', { d: 'M5 12h14' }],
  ],
  activity: [
    ['path', { d: 'M22 12h-2.48a2 2 0 0 0-1.93 1.46l-2.35 8.36a.25.25 0 0 1-.48 0L9.24 2.18a.25.25 0 0 0-.48 0l-2.35 8.36A2 2 0 0 1 4.49 12H2' }],
  ],
  terminal: [
    ['path', { d: 'M12 19h8' }],
    ['path', { d: 'm4 17 6-6-6-6' }],
  ],
  cpu: [
    ['path', { d: 'M12 20v2' }],
    ['path', { d: 'M12 2v2' }],
    ['path', { d: 'M17 20v2' }],
    ['path', { d: 'M17 2v2' }],
    ['path', { d: 'M2 12h2' }],
    ['path', { d: 'M2 17h2' }],
    ['path', { d: 'M2 7h2' }],
    ['path', { d: 'M20 12h2' }],
    ['path', { d: 'M20 17h2' }],
    ['path', { d: 'M20 7h2' }],
    ['path', { d: 'M7 20v2' }],
    ['path', { d: 'M7 2v2' }],
    ['rect', { x: '4', y: '4', width: '16', height: '16', rx: '2' }],
    ['rect', { x: '8', y: '8', width: '8', height: '8', rx: '1' }],
  ],
  calendar: [
    ['path', { d: 'M8 2v3' }],
    ['path', { d: 'M16 2v3' }],
    ['rect', { x: '3', y: '3', width: '18', height: '18', rx: '2' }],
    ['path', { d: 'M3 9h18' }],
  ],
  coffee: [
    ['path', { d: 'M10 2v2' }],
    ['path', { d: 'M14 2v2' }],
    ['path', { d: 'M16 8a1 1 0 0 1 1 1v8a4 4 0 0 1-4 4H7a4 4 0 0 1-4-4V9a1 1 0 0 1 1-1h14a4 4 0 1 1 0 8h-1' }],
    ['path', { d: 'M6 2v2' }],
  ],
  'refresh-cw': [
    ['path', { d: 'M3 12a9 9 0 0 1 9-9 9.75 9.75 0 0 1 6.74 2.74L21 8' }],
    ['path', { d: 'M21 3v5h-5' }],
    ['path', { d: 'M21 12a9 9 0 0 1-9 9 9.75 9.75 0 0 1-6.74-2.74L3 16' }],
    ['path', { d: 'M8 16H3v5' }],
  ],
  plus: [
    ['path', { d: 'M5 12h14' }],
    ['path', { d: 'M12 5v14' }],
  ],
  check: [
    ['path', { d: 'M20 6 9 17l-5-5' }],
  ],
  info: [
    ['circle', { cx: '12', cy: '12', r: '10' }],
    ['path', { d: 'M12 16v-4' }],
    ['path', { d: 'M12 8h.01' }],
  ],
  'triangle-alert': [
    ['path', { d: 'm21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3' }],
    ['path', { d: 'M12 9v4' }],
    ['path', { d: 'M12 17h.01' }],
  ],
  'rotate-ccw': [
    ['path', { d: 'M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8' }],
    ['path', { d: 'M3 3v5h5' }],
  ],
  square: [
    ['rect', { width: '18', height: '18', x: '3', y: '3', rx: '2' }],
  ],
  play: [
    ['path', { d: 'M5 5a2 2 0 0 1 3.008-1.728l11.997 6.998a2 2 0 0 1 .003 3.458l-12 7A2 2 0 0 1 5 19z' }],
  ],
  globe: [
    ['circle', { cx: '12', cy: '12', r: '10' }],
    ['path', { d: 'M12 2a14.5 14.5 0 0 0 0 20 14.5 14.5 0 0 0 0-20' }],
    ['path', { d: 'M2 12h20' }],
  ],
  layers: [
    ['path', { d: 'M12.83 2.18a2 2 0 0 0-1.66 0L2.6 6.08a1 1 0 0 0 0 1.83l8.58 3.91a2 2 0 0 0 1.66 0l8.58-3.9a1 1 0 0 0 0-1.83z' }],
    ['path', { d: 'M2 12a1 1 0 0 0 .58.91l8.6 3.91a2 2 0 0 0 1.65 0l8.58-3.9A1 1 0 0 0 22 12' }],
    ['path', { d: 'M2 17a1 1 0 0 0 .58.91l8.6 3.91a2 2 0 0 0 1.65 0l8.58-3.9A1 1 0 0 0 22 17' }],
  ],
  network: [
    ['rect', { x: '16', y: '16', width: '6', height: '6', rx: '1' }],
    ['rect', { x: '2', y: '16', width: '6', height: '6', rx: '1' }],
    ['rect', { x: '9', y: '2', width: '6', height: '6', rx: '1' }],
    ['path', { d: 'M5 16v-3a1 1 0 0 1 1-1h12a1 1 0 0 1 1 1v3' }],
    ['path', { d: 'M12 12V8' }],
  ],
  database: [
    ['ellipse', { cx: '12', cy: '5', rx: '9', ry: '3' }],
    ['path', { d: 'M3 5V19A9 3 0 0 0 21 19V5' }],
    ['path', { d: 'M3 12A9 3 0 0 0 21 12' }],
  ],
  folder: [
    ['path', { d: 'M20 20a2 2 0 0 0 2-2V8a2 2 0 0 0-2-2h-7.9a2 2 0 0 1-1.69-.9L9.6 3.9A2 2 0 0 0 7.93 3H4a2 2 0 0 0-2 2v13a2 2 0 0 0 2 2Z' }],
  ],
  'hard-drive': [
    ['path', { d: 'M10 16h.01' }],
    ['path', { d: 'M2.212 11.577a2 2 0 0 0-.212.896V18a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-5.527a2 2 0 0 0-.212-.896L18.55 5.11A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z' }],
    ['path', { d: 'M21.946 12.013H2.054' }],
    ['path', { d: 'M6 16h.01' }],
  ],
};

const SVG_NS = 'http://www.w3.org/2000/svg';

/**
 * Create an inline SVG icon element.
 * @param {string} name - icon key from ICONS registry
 * @param {Object} [options]
 * @param {number} [options.size=16] - width/height in px
 * @param {string} [options.class] - extra class names (e.g. 'icon', 'btn-icon')
 * @param {number} [options.strokeWidth=2]
 * @param {string} [options.title] - accessible title (adds <title> + role="img")
 * @returns {SVGSVGElement}
 */
export function icon(name, { size = 16, class: className = '', strokeWidth = 2, title = null } = {}) {
  const def = ICONS[name] || ICONS.info;
  const svg = document.createElementNS(SVG_NS, 'svg');
  svg.setAttribute('viewBox', '0 0 24 24');
  svg.setAttribute('width', String(size));
  svg.setAttribute('height', String(size));
  svg.setAttribute('fill', 'none');
  svg.setAttribute('stroke', 'currentColor');
  svg.setAttribute('stroke-width', String(strokeWidth));
  svg.setAttribute('stroke-linecap', 'round');
  svg.setAttribute('stroke-linejoin', 'round');
  svg.setAttribute('class', `icon ${className}`.trim());
  if (title) {
    svg.setAttribute('role', 'img');
    const t = document.createElementNS(SVG_NS, 'title');
    t.textContent = title;
    svg.appendChild(t);
  } else {
    svg.setAttribute('aria-hidden', 'true');
    svg.setAttribute('focusable', 'false');
  }

  for (const [tag, attrs] of def) {
    const node = document.createElementNS(SVG_NS, tag);
    for (const [key, value] of Object.entries(attrs)) node.setAttribute(key, value);
    svg.appendChild(node);
  }
  return svg;
}

/**
 * Build an icon as an HTML string (for inline html: templates where needed).
 * All data is static path data — never pass user content here.
 * @param {string} name - icon key
 * @param {Object} [options] - same as icon()
 * @returns {string}
 */
export function iconHtml(name, { size = 16, class: className = '', strokeWidth = 2, title = null } = {}) {
  const def = ICONS[name] || ICONS.info;
  const body = def
    .map(([tag, attrs]) => {
      const attrStr = Object.entries(attrs)
        .map(([k, v]) => `${k}="${v}"`)
        .join(' ');
      return `<${tag} ${attrStr}></${tag}>`;
    })
    .join('');
  const titleEl = title ? `<title>${title}</title>` : '';
  const aria = title ? 'role="img"' : 'aria-hidden="true" focusable="false"';
  return (
    `<svg class="icon ${className}" width="${size}" height="${size}" viewBox="0 0 24 24" fill="none" ` +
    `stroke="currentColor" stroke-width="${strokeWidth}" stroke-linecap="round" stroke-linejoin="round" ${aria}>` +
    `${titleEl}${body}</svg>`
  );
}

export default { icon, iconHtml };

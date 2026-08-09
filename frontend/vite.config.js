/**
 * Coffee Server — Vite config (Multi-Page App)
 *
 * Fonte: ./ (HTMLs + JS + CSS + assets do dashboard)
 * Build: ../src/main/resources/static/app  (servido pelo Spring em /app/**)
 *
 * Entradas (MPA):
 *   - index.html              → redirect para login/dashboard
 *   - pages/dashboard.html    → dashboard.js
 *   - pages/login.html        → script inline (auth/session.js)
 *   - pages/mcp.html          → mcp.js
 *
 * base '/app/' garante que os assets gerados (/app/assets/*-[hash]) sejam
 * servidos pelo Spring Boot a partir de src/main/resources/static/app/assets.
 */
import { defineConfig } from 'vite';
import { resolve } from 'node:path';

export default defineConfig({
  root: '.',
  base: '/app/',
  publicDir: false,
  build: {
    outDir: '../src/main/resources/static/app',
    emptyOutDir: true,
    // 1 CSS único compartilhado por todas as páginas (dashboard, mcp, login usam main.css)
    cssCodeSplit: false,
    rollupOptions: {
      input: {
        index: resolve(import.meta.dirname, 'index.html'),
        dashboard: resolve(import.meta.dirname, 'pages/dashboard.html'),
        login: resolve(import.meta.dirname, 'pages/login.html'),
        mcp: resolve(import.meta.dirname, 'pages/mcp.html'),
      },
    },
  },
});

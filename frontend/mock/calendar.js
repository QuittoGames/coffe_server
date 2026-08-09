/**
 * Coffee Server Dashboard — Mock Calendar Data
 * Weekly view: events relative to Monday (dayOffset 0-6).
 * Tones map to the design tokens (blue = tech/action, coffee = identity).
 * Replace with Google Calendar events when the backend exposes them.
 */

export const mockCalendarEvents = [
  { dayOffset: 0, time: '09:00', title: 'Revisão semanal', tone: 'blue' },
  { dayOffset: 1, time: '14:30', title: 'Google Tasks sync', tone: 'coffee' },
  { dayOffset: 3, time: '19:00', title: 'Estudo Java · Spring Boot', tone: 'blue' },
  { dayOffset: 5, time: '10:00', title: 'Backup manual', tone: 'coffee' },
];

/**
 * Coffee Server Dashboard — API Types (JSDoc)
 * Type contracts for DTOs and domain objects. No runtime code — documentation
 * only, aligned with the backend's domain models.
 */

/**
 * @typedef {Object} UserDTO
 * @property {number} id
 * @property {string} name
 * @property {string} email
 * @property {'ADMIN'|'USER'|'MCP'|'API'} role
 * @property {string} createdAt ISO date
 */

/**
 * @typedef {Object} LoginRequestDTO
 * @property {string} name
 * @property {string} password
 */

/**
 * @typedef {Object} RegisterRequestDTO
 * @property {string} name
 * @property {string} password
 * @property {string} email
 */

/**
 * @typedef {Object} LoginResponseDTO
 * @property {string} date ISO timestamp of login (token lives in HttpOnly cookie)
 */

/**
 * @typedef {Object} MachineDTO
 * @property {number} id
 * @property {string} hostname
 * @property {string|null} tailscaleNodeKey
 * @property {string|null} currentIp
 * @property {string} macAddress
 * @property {boolean} wolEnabled
 * @property {boolean} status
 * @property {string} os
 * @property {number} userId
 */

/**
 * @typedef {Object} LinuxUserDTO
 * @property {number} uid
 * @property {string} name
 * @property {string} shell
 * @property {string} homeDir
 * @property {import('./types.js').GroupsDTO} group
 */

/**
 * @typedef {Object} GroupsDTO
 * @property {number} gid
 * @property {string} name
 * @property {boolean} isActive
 */

/**
 * @typedef {Object} ExternalAccountDTO
 * @property {number} id
 * @property {number} userId
 * @property {'GOOGLE'|'GITHUB'} provider
 * @property {string} externalClient
 * @property {string|null} expiresAt
 */

/**
 * @typedef {Object} HealthStatus
 * @property {'ok'|'degraded'|'down'} database
 * @property {'ok'|'degraded'|'down'} redis
 * @property {'ok'|'down'} googleCalendar
 * @property {number} diskUsagePercent
 */

/**
 * @typedef {Object} MCPServerDTO
 * @property {string} id
 * @property {string} name
 * @property {string} description
 * @property {'online'|'offline'|'planned'} status
 * @property {string} provider
 * @property {string[]} tools
 * @property {number} calls
 * @property {string|null} lastCall
 */

export default {};

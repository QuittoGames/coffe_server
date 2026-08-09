package com.quitto.server.infrastructure.CoffeAgent.content.Actions;

public enum AgentActions {

    // Execution
    RUN,
    RUN_COMMAND,

    // File system
    READ,
    WRITE,
    CREATE,
    DELETE,
    LIST,
    EXISTS,

    // Search
    SEARCH,
    SEARCH_KEY,
    SEARCH_FILE,
    SEARCH_CONTENT,

    // Server information
    STATUS,
    INFO,
    HEALTH,
    VERSION,

    // Configuration
    GET_CONFIG,
    SET_CONFIG,

    // Environment
    GET_ENV,
    GET_PROPERTY,

    // Network / communication
    PING,
    CONNECT,
    DISCONNECT,

    // Process management
    PROCESS_LIST,
    PROCESS_INFO,

    // Package / dependency management
    PACKAGE_LIST,
    PACKAGE_INFO,

    // Agent management
    AGENT_INFO,
    AGENT_STATUS,
    AGENT_CONFIG,

    // Data
    GET,
    SET,

    // Control
    CANCEL,
    ERROR
}

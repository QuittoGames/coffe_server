package com.quitto.server.domain.exception;

public class MachineNotFoundException extends RuntimeException {
    public MachineNotFoundException(String message) {
        super(message);
    }

    public MachineNotFoundException(Long machineId) {
        super("Machine not found with id: " + machineId);
    }
}
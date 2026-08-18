package com.quitto.server.domain.exception.Resource;

/**
 * Exceção de domínio para <strong>máquina não encontrada</strong>.
 * Sinaliza que uma {@code Machine} não existe em operações de
 * repositório/serviço. Foi movida de {@code shared/exception/} para o domínio
 * (commit 363334f) mantendo as exceções de negócio na camada correta.
 */
public class MachineNotFoundException extends RuntimeException {
    public MachineNotFoundException(String message) {
        super(message);
    }

    public MachineNotFoundException(Long machineId) {
        super("Machine not found with id: " + machineId);
    }
}

package com.quitto.server.domain.Repository.Machine;

import java.util.List;
import java.util.Optional;

import com.quitto.server.domain.models.Machine.Machine;

/**
 * Porta de persistência do agregado {@link Machine} (máquinas gerenciadas do
 * homelab). Implementada na infraestrutura (adapter JPA:
 * {@code MachineRepositoryAdapter} + {@code JpaMachineRepository}). Oferece
 * CRUD, buscas por identificadores únicos (hostname, MAC, Tailscale) e
 * transferência de propriedade.
 */
public interface MachineRepository {

    Machine save(Machine machine);

    Optional<Machine> findById(long id);

    Optional<Machine> findByHostname(String hostname);

    Optional<Machine> findByMacAddress(String macAddress);

    Optional<Machine> findByTailscaleNodeKey(String tailscaleNodeKey);

    Optional<Machine> setOwner(long machineId, long userId);

    List<Machine> findAll();

    boolean existsById(long id);

    boolean existsByHostname(String hostname);

    boolean existsByMacAddress(String macAddress);

    void deleteById(long id);

    void delete(Machine machine);
}

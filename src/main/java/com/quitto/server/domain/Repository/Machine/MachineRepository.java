package com.quitto.server.domain.Repository.Machine;

import java.util.List;
import java.util.Optional;

import com.quitto.server.domain.models.Machine.Machine;

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

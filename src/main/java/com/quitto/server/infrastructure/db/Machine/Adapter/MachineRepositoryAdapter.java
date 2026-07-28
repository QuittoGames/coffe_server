package com.quitto.server.infrastructure.db.Machine.Adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.quitto.server.domain.Repository.Machine.MachineRepository;
import com.quitto.server.domain.models.Machine.Machine;
import com.quitto.server.infrastructure.db.Machine.Entity.MachineEntity;
import com.quitto.server.infrastructure.db.Machine.Mapper.MachineMapper;
import com.quitto.server.infrastructure.db.Machine.Repository.JpaMachineRepository;

@Repository
public class MachineRepositoryAdapter implements MachineRepository {

    private final JpaMachineRepository repository;

    public MachineRepositoryAdapter(JpaMachineRepository repository){
        this.repository = repository;
    }

    @Override
    public Machine save(Machine machine) {
        var entity = MachineMapper.toInfra(machine);
        MachineEntity saved = repository.save(entity);
        return MachineMapper.toDomain(saved);
    }

    @Override
    public Optional<Machine> findById(long id) {
        Optional<MachineEntity> machineEntity = repository.findById(id);

        if (machineEntity.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(MachineMapper.toDomain(machineEntity.get()));
    }

    @Override
    public Optional<Machine> findByHostname(String hostname) {
        Optional<MachineEntity> machineEntity = repository.findByHostname(hostname);

        if (machineEntity.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(MachineMapper.toDomain(machineEntity.get()));
    }

    @Override
    public Optional<Machine> findByMacAddress(String macAddress) {
        Optional<MachineEntity> machineEntity = repository.findByMacAddress(macAddress);

        if (machineEntity.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(MachineMapper.toDomain(machineEntity.get()));
    }

    @Override
    public Optional<Machine> findByTailscaleNodeKey(String tailscaleNodeKey) {
        Optional<MachineEntity> machineEntity = repository.findByTailscaleNodeKey(tailscaleNodeKey);

        if (machineEntity.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(MachineMapper.toDomain(machineEntity.get()));
    }

    @Override
    public List<Machine> findAll() {
        return repository.findAll()
                .stream()
                .map(MachineMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Machine> setOwner(long machineId, long userId) {
        int updated = repository.updateOwner(machineId, userId);
        if (updated == 0) {
            return Optional.empty();
        }
        return findById(machineId);
    }

    @Override
    public boolean existsById(long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByHostname(String hostname) {
        return repository.existsByHostname(hostname);
    }

    @Override
    public boolean existsByMacAddress(String macAddress) {
        return repository.existsByMacAddress(macAddress);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(Machine machine) {
        var entity = MachineMapper.toInfra(machine);
        repository.delete(entity);
    }
}

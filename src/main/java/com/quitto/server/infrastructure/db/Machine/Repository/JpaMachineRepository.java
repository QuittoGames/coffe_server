package com.quitto.server.infrastructure.db.Machine.Repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.quitto.server.infrastructure.db.Machine.Entity.MachineEntity;

public interface JpaMachineRepository extends JpaRepository<MachineEntity,Long>{

    Optional<MachineEntity> findById(long id);

    Optional<MachineEntity> findByHostname(String hostname);

    Optional<MachineEntity> findByMacAddress(String macAddress);

    Optional<MachineEntity> findByTailscaleNodeKey(String tailscaleNodeKey);

    List<MachineEntity> findAll();

    boolean existsById(long id);

    boolean existsByHostname(String hostname);

    boolean existsByMacAddress(String macAddress);

    @Modifying
    @Query(value = "UPDATE machine SET user_id = :userId WHERE id = :machineId", nativeQuery = true)
    int updateOwner(@Param("machineId") long machineId, @Param("userId") long userId);

    void deleteById(long id);

    void delete(MachineEntity machine);
}

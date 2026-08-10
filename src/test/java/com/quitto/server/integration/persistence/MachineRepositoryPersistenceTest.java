package com.quitto.server.integration.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.quitto.server.domain.Repository.Machine.MachineRepository;
import com.quitto.server.domain.Repository.users.UserRepository;
import com.quitto.server.domain.enums.Role;
import com.quitto.server.domain.models.Machine.Machine;
import com.quitto.server.domain.models.User.User;

/**
 * Testes de persistência do {@link MachineRepositoryAdapter} contra PostgreSQL
 * real (Testcontainers). Cobre save com FK {@code user_id}, buscas,
 * {@code setOwner} (UPDATE nativo), exists, findAll, delete e unicidade.
 *
 * <p><strong>Risco coberto:</strong> a entidade JPA exige {@code user_id}
 * NOT NULL; o mapper monta o {@code user} só quando {@code userId != null} —
 * máquina sem dono deve falhar com violação de constraint.</p>
 */
class MachineRepositoryPersistenceTest extends PostgresPersistenceTestSupport {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private UserRepository userRepository;

    private Long ownerId;

    private int machineSeq;

    @BeforeEach
    void seedOwner() {
        User owner = userRepository.save(
                new User("machine-owner", "$2a$10$hashhashhashhashhashhashhashhash", "owner@test.dev", Role.USER));
        ownerId = owner.getId();
    }

    private Machine newMachine(String hostname) {
        machineSeq++;
        String ip = "10.0.0." + (machineSeq + 10);
        String mac = String.format("AA:BB:CC:DD:EE:%02X", machineSeq);
        return new Machine(null, hostname, "ts-key-" + hostname, ip, mac,
                true, true, "Ubuntu 24.04", ownerId);
    }

    @Test
    void save_persistsMachineWithUserId() {
        Machine saved = machineRepository.save(newMachine("server-1"));

        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getHostname()).isEqualTo("server-1");
        assertThat(saved.getUserId()).isEqualTo(ownerId);
    }

    @Test
    void findById_returnsSavedMachine() {
        Machine saved = machineRepository.save(newMachine("server-2"));

        Optional<Machine> result = machineRepository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getHostname()).isEqualTo("server-2");
        assertThat(result.get().getUserId()).isEqualTo(ownerId);
    }

    @Test
    void findById_missing_returnsEmpty() {
        assertThat(machineRepository.findById(999_999L)).isEmpty();
    }

    @Test
    void findByHostname_returnsMachine() {
        machineRepository.save(newMachine("server-by-hostname"));

        Optional<Machine> result = machineRepository.findByHostname("server-by-hostname");

        assertThat(result).isPresent();
        assertThat(result.get().getTailscaleNodeKey()).isEqualTo("ts-key-server-by-hostname");
    }

    @Test
    void findByMacAddress_returnsMachine() {
        Machine saved = machineRepository.save(newMachine("server-by-mac"));

        Optional<Machine> result = machineRepository.findByMacAddress(saved.getMacAddress());

        assertThat(result).isPresent();
        assertThat(result.get().getHostname()).isEqualTo("server-by-mac");
    }

    @Test
    void findByTailscaleNodeKey_returnsMachine() {
        machineRepository.save(newMachine("server-by-ts"));

        Optional<Machine> result = machineRepository.findByTailscaleNodeKey("ts-key-server-by-ts");

        assertThat(result).isPresent();
        assertThat(result.get().getHostname()).isEqualTo("server-by-ts");
    }

    @Test
    void findAll_returnsAllMachines() {
        machineRepository.save(newMachine("list-m-1"));
        machineRepository.save(newMachine("list-m-2"));

        List<Machine> all = machineRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void setOwner_updatesUserId() {
        Machine saved = machineRepository.save(newMachine("transfer-1"));
        User newOwner = userRepository.save(
                new User("new-owner", "$2a$10$hashhashhashhashhashhashhashhash", "new-owner@test.dev", Role.USER));

        Optional<Machine> updated = machineRepository.setOwner(saved.getId(), newOwner.getId());

        assertThat(updated).isPresent();
        assertThat(updated.get().getUserId()).isEqualTo(newOwner.getId());
    }

    @Test
    void setOwner_unknownMachine_returnsEmpty() {
        assertThat(machineRepository.setOwner(999_999L, ownerId)).isEmpty();
    }

    @Test
    void existsById_and_existsByHostname_reflectDatabaseState() {
        Machine saved = machineRepository.save(newMachine("exists-m"));

        assertThat(machineRepository.existsById(saved.getId())).isTrue();
        assertThat(machineRepository.existsByHostname("exists-m")).isTrue();
        assertThat(machineRepository.existsByHostname("missing-m")).isFalse();
    }

    @Test
    void deleteById_removesMachine() {
        Machine saved = machineRepository.save(newMachine("to-delete-m"));

        machineRepository.deleteById(saved.getId());

        assertThat(machineRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void save_duplicateHostname_violatesUniqueConstraint() {
        machineRepository.save(newMachine("dup-host"));

        assertThatThrownBy(() -> machineRepository.save(newMachine("dup-host")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_machineWithoutOwner_violatesNotNullConstraint() {
        Machine withoutOwner = new Machine(null, "no-owner", "ts-key-no-owner", "10.0.0.9",
                "AA:BB:CC:DD:EE:99", true, true, "Ubuntu", null);

        assertThatThrownBy(() -> machineRepository.save(withoutOwner))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

package com.quitto.server.unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.quitto.server.domain.models.Machine.Machine;
import com.quitto.server.domain.models.User.User;
import com.quitto.server.domain.enums.Role;

@DisplayName("Machine Domain Tests")
class MachineTest {

    @Test
    @DisplayName("creates Machine with full constructor including userId")
    void createsMachineWithFullConstructor() {
        Machine machine = new Machine(
            1L, "server-01", "ts-key-abc", "192.168.1.100",
            "AA:BB:CC:DD:EE:FF", true, true, "Ubuntu 24.04", 42L
        );

        assertEquals(1L, machine.getId());
        assertEquals("server-01", machine.getHostname());
        assertEquals("ts-key-abc", machine.getTailscaleNodeKey());
        assertEquals("192.168.1.100", machine.getCurrentIp());
        assertEquals("AA:BB:CC:DD:EE:FF", machine.getMacAddress());
        assertTrue(machine.isStatus());
        assertTrue(machine.isWolEnabled());
        assertEquals("Ubuntu 24.04", machine.getOS());
        assertEquals(42L, machine.getUserId());
    }

    @Test
    @DisplayName("creates Machine without userId")
    void createsMachineWithoutUserId() {
        Machine machine = new Machine(
            1L, "server-01", "ts-key", "10.0.0.1",
            "11:22:33:44:55:66", false, false, "Debian 12"
        );

        assertEquals("server-01", machine.getHostname());
        assertFalse(machine.isStatus());
        assertFalse(machine.isWolEnabled());
        assertNull(machine.getUserId());
    }

    @Test
    @DisplayName("creates Machine with no-args constructor")
    void createsMachineWithNoArgsConstructor() {
        Machine machine = new Machine();
        assertNotNull(machine);
        assertNull(machine.getId());
        assertNull(machine.getHostname());
    }

    @Test
    @DisplayName("changeOwner updates userId")
    void changeOwner_updatesUserId() {
        Machine machine = new Machine();
        User newOwner = new User(99L, "owner", "hash", "owner@email.com", Role.ADMIN);
        machine.changeOwner(newOwner);
        assertEquals(99L, machine.getUserId());
    }

    @Test
    @DisplayName("changeOwner with null user throws NPE")
    void changeOwner_withNullUser_throwsException() {
        Machine machine = new Machine();
        assertThrows(NullPointerException.class, () -> machine.changeOwner(null));
    }

    @Test
    @DisplayName("setCurrentIp updates IP address")
    void setCurrentIp_updatesIp() {
        Machine machine = new Machine();
        machine.setCurrentIp("10.0.0.50");
        assertEquals("10.0.0.50", machine.getCurrentIp());
    }

    @Test
    @DisplayName("setWolEnabled toggles WOL flag")
    void setWolEnabled_togglesWol() {
        Machine machine = new Machine();
        machine.setWolEnabled(true);
        assertTrue(machine.isWolEnabled());
        machine.setWolEnabled(false);
        assertFalse(machine.isWolEnabled());
    }

    @Test
    @DisplayName("setOS updates OS field")
    void setOS_updatesOs() {
        Machine machine = new Machine();
        machine.setOS("Fedora 40");
        assertEquals("Fedora 40", machine.getOS());
    }

    @Test
    @DisplayName("setStatus updates status")
    void setStatus_updatesStatus() {
        Machine machine = new Machine();
        machine.setStatus(true);
        assertTrue(machine.isStatus());
    }

    @Test
    @DisplayName("equals by ID")
    void equals_byId() {
        Machine m1 = new Machine(1L, "srv", null, null, null, false, false, null);
        Machine m2 = new Machine(1L, "srv", null, null, null, false, false, null);
        assertEquals(m1, m2);
    }

    @Test
    @DisplayName("not equals for different IDs")
    void notEqualsForDifferentIds() {
        Machine m1 = new Machine(1L, "srv", null, null, null, false, false, null);
        Machine m2 = new Machine(2L, "srv", null, null, null, false, false, null);
        assertNotEquals(m1, m2);
    }

    @Test
    @DisplayName("not equals against different class")
    void notEqualsForDifferentClass() {
        Machine m = new Machine(1L, "srv", null, null, null, false, false, null);
        assertNotEquals(m, "not-a-machine");
    }

    @Test
    @DisplayName("equals same reference")
    void equalsSameReference() {
        Machine m = new Machine(1L, "srv", null, null, null, false, false, null);
        assertEquals(m, m);
    }

    @Test
    @DisplayName("toString contains hostname and ID")
    void toString_containsFields() {
        Machine m = new Machine(1L, "server-01", "ts-key", "10.0.0.1",
            "AA:BB:CC:DD:EE:FF", true, true, "Ubuntu", 5L);
        String str = m.toString();
        assertTrue(str.contains("server-01"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("10.0.0.1"));
    }
}

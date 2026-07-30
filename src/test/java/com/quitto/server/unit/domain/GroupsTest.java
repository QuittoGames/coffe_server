package com.quitto.server.unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.quitto.server.domain.models.LinuxAccount.Groups;

@DisplayName("Groups Domain Tests")
class GroupsTest {

    @Test
    @DisplayName("creates Groups with constructor")
    void createsGroupsWithConstructor() {
        Groups group = new Groups(100, "admin", true);
        assertEquals(100, group.getGID());
        assertEquals("admin", group.getName());
        assertTrue(group.isActive());
    }

    @Test
    @DisplayName("creates Groups with no-args")
    void createsGroupsWithNoArgsConstructor() {
        Groups group = new Groups();
        assertNotNull(group);
        assertEquals(0, group.getGID());
        assertNull(group.getName());
        assertFalse(group.isActive());
    }

    @Test
    @DisplayName("setName updates name")
    void setName_updatesName() {
        Groups group = new Groups(100, "old", true);
        group.setName("new");
        assertEquals("new", group.getName());
    }

    @Test
    @DisplayName("setActive toggles active state")
    void setActive_togglesState() {
        Groups group = new Groups(100, "devs", false);
        group.setActive(true);
        assertTrue(group.isActive());
        group.setActive(false);
        assertFalse(group.isActive());
    }

    @Test
    @DisplayName("equals by GID")
    void equalsByGid() {
        Groups g1 = new Groups(100, "admin", true);
        Groups g2 = new Groups(100, "different", false);
        assertEquals(g1, g2);
    }

    @Test
    @DisplayName("not equals for different GID")
    void notEqualsForDifferentGid() {
        Groups g1 = new Groups(100, "admin", true);
        Groups g2 = new Groups(200, "admin", true);
        assertNotEquals(g1, g2);
    }

    @Test
    @DisplayName("not equals against different class")
    void notEqualsForDifferentClass() {
        Groups g = new Groups(100, "admin", true);
        assertNotEquals(g, "not-a-group");
    }

    @Test
    @DisplayName("equals same reference")
    void equalsSameReference() {
        Groups g = new Groups(100, "admin", true);
        assertEquals(g, g);
    }

    @Test
    @DisplayName("toString contains GID and name")
    void toString_containsInfo() {
        Groups g = new Groups(100, "admin", true);
        String str = g.toString();
        assertTrue(str.contains("100"));
        assertTrue(str.contains("admin"));
    }

    @Test
    @DisplayName("handles root group (GID 0)")
    void handlesRootGroup() {
        Groups root = new Groups(0, "root", true);
        assertEquals(0, root.getGID());
        assertEquals("root", root.getName());
        assertTrue(root.isActive());
    }

    @Test
    @DisplayName("handles inactive group")
    void handlesInactiveGroup() {
        Groups inactive = new Groups(999, "inactive", false);
        assertFalse(inactive.isActive());
    }

    @Test
    @DisplayName("setName accepts null")
    void setName_acceptsNull() {
        Groups group = new Groups(100, "admin", true);
        group.setName(null);
        assertNull(group.getName());
    }
}

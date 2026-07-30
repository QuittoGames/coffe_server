package com.quitto.server.unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.quitto.server.domain.models.LinuxAccount.Groups;
import com.quitto.server.domain.models.LinuxAccount.LinuxUser;

@DisplayName("LinuxUser Domain Tests")
class LinuxUserTest {

    @Test
    @DisplayName("creates LinuxUser with constructor")
    void createsLinuxUserWithConstructor() {
        LinuxUser user = new LinuxUser(1000, "quitto", "/bin/bash", "/home/quitto");
        assertEquals(1000, user.getUid());
        assertEquals("quitto", user.getName());
        assertEquals("/bin/bash", user.getShell());
        assertEquals("/home/quitto", user.getHomeDir());
    }

    @Test
    @DisplayName("creates LinuxUser with no-args")
    void createsLinuxUserWithNoArgsConstructor() {
        LinuxUser user = new LinuxUser();
        assertNotNull(user);
        assertEquals(0, user.getUid());
        assertNull(user.getName());
    }

    @Test
    @DisplayName("sets and gets group association")
    void setsGroupAssociation() {
        LinuxUser user = new LinuxUser(1000, "quitto", "/bin/bash", "/home/quitto");
        Groups group = new Groups(100, "admin", true);

        assertNull(user.getGroup());
        user.setGroup(group);
        assertEquals("admin", user.getGroup().getName());
    }

    @Test
    @DisplayName("sets group to null")
    void setsGroupToNull() {
        LinuxUser user = new LinuxUser(1000, "quitto", "/bin/bash", "/home/quitto");
        user.setGroup(new Groups(100, "admin", true));
        user.setGroup(null);
        assertNull(user.getGroup());
    }

    @Test
    @DisplayName("setName updates name")
    void setName_updatesName() {
        LinuxUser user = new LinuxUser(1000, "old_name", "/bin/bash", "/home/old");
        user.setName("new_name");
        assertEquals("new_name", user.getName());
    }

    @Test
    @DisplayName("setShell updates shell")
    void setShell_updatesShell() {
        LinuxUser user = new LinuxUser(1000, "quitto", "/bin/bash", "/home/quitto");
        user.setShell("/bin/zsh");
        assertEquals("/bin/zsh", user.getShell());
    }

    @Test
    @DisplayName("equals by UID")
    void equalsByUid() {
        LinuxUser u1 = new LinuxUser(1000, "a", "/bin/bash", "/home/a");
        LinuxUser u2 = new LinuxUser(1000, "b", "/bin/zsh", "/home/b");
        assertEquals(u1, u2);
    }

    @Test
    @DisplayName("not equals for different UID")
    void notEqualsForDifferentUid() {
        LinuxUser u1 = new LinuxUser(1000, "a", "/bin/bash", "/home/a");
        LinuxUser u2 = new LinuxUser(1001, "b", "/bin/bash", "/home/b");
        assertNotEquals(u1, u2);
    }

    @Test
    @DisplayName("not equals against different class")
    void notEqualsForDifferentClass() {
        LinuxUser u = new LinuxUser(1000, "quitto", "/bin/bash", "/home/quitto");
        assertNotEquals(u, "not-a-user");
    }

    @Test
    @DisplayName("equals same reference")
    void equalsSameReference() {
        LinuxUser u = new LinuxUser(1000, "quitto", "/bin/bash", "/home/quitto");
        assertEquals(u, u);
    }

    @Test
    @DisplayName("toString contains user info")
    void toString_containsInfo() {
        LinuxUser u = new LinuxUser(1000, "quitto", "/bin/bash", "/home/quitto");
        u.setGroup(new Groups(100, "admin", true));
        String str = u.toString();
        assertTrue(str.contains("1000"));
        assertTrue(str.contains("quitto"));
        assertTrue(str.contains("admin"));
    }

    @Test
    @DisplayName("handles root user (UID 0)")
    void handlesRootUser() {
        LinuxUser root = new LinuxUser(0, "root", "/bin/bash", "/root");
        assertEquals(0, root.getUid());
        assertEquals("root", root.getName());
    }
}

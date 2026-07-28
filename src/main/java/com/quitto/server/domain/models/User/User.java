package com.quitto.server.domain.models.User;

import java.util.List;

import com.quitto.server.domain.enums.Role;
import com.quitto.server.domain.models.LinuxAccount.LinuxUser;
import com.quitto.server.domain.models.Machine.Machine;

public class User {
    private long id;
    private String name;
    private String passwordHash;
    private String email;
    private Role role;
    private LinuxUser linuxUser;
    private List<Machine> machine;

    public User(String name, String passwordHash, String email, Role role){
        this.name = name;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }

    public User(long id, String name, String passowrd, String email, Role role) {
        this.id = id;
        this.name = name;
        this.passwordHash = passowrd;
        this.email = email;
        this.role = role;
    }

    public User() {

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public void changePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LinuxUser getLinuxUser() {
        return linuxUser;
    }

    public void setLinuxUser(LinuxUser linuxUser) {
        this.linuxUser = linuxUser;
    }

    public List<Machine> getMachine() {
        return machine;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        User other = (User) obj;
        return email != null && email.equals(other.email);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", role="
                + role + ", linuxUser=" + linuxUser + ", machine=" + machine + "]";
    }

}

package com.quitto.server.infrastructure.db.Machine.Entity;

import com.quitto.server.infrastructure.db.User.Entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "machine")
public class MachineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true, name = "hostname")
    private String hostname;

    @Column(nullable = false, unique = true, name = "tailscale_node_key")
    private String tailscaleNodeKey;

    @Column(nullable = false, unique = true, name = "current_ip")
    private String currentIp;

    @Column(nullable = true, unique = true, name = "mac_address")
    private String macAddress;

    @Column(nullable = false, name = "wol_enabled")
    private boolean wolEnabled;

    @Column(nullable = false, name = "status")
    private boolean status;

    @Column(nullable = true, length = 50, unique = false, name = "os")
    private String OS;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    public MachineEntity() {

    }

    public MachineEntity(Long id, String hostname, String tailscaleNodeKey, String currentIp, String macAddress,
            boolean wolEnabled, boolean status, String OS) {
        this.id = id;
        this.hostname = hostname;
        this.tailscaleNodeKey = tailscaleNodeKey;
        this.currentIp = currentIp;
        this.macAddress = macAddress;
        this.wolEnabled = wolEnabled;
        this.status = status;
        this.OS = OS;
    }

    public MachineEntity(Long id, String hostname, String tailscaleNodeKey, String currentIp, String macAddress,
            boolean wolEnabled, boolean status, String oS, Long userId) {
        this(id, hostname, tailscaleNodeKey, currentIp, macAddress, wolEnabled, status, oS);
        if (userId != null) {
            this.user = new UserEntity();
            this.user.setId(userId);
        }
    }

    public Long getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public String getTailscaleNodeKey() {
        return tailscaleNodeKey;
    }

    public String getCurrentIp() {
        return currentIp;
    }

    public void setCurrentIp(String currentIp) {
        this.currentIp = currentIp;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public boolean isWolEnabled() {
        return wolEnabled;
    }

    public void setWolEnabled(boolean wolEnabled) {
        this.wolEnabled = wolEnabled;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "MachineEntity [id=" + id + ", hostname=" + hostname + ", tailscaleNodeKey=" + tailscaleNodeKey
                + ", currentIp=" + currentIp + ", macAddress=" + macAddress + ", wolEnabled=" + wolEnabled + ", status="
                + status + ", OS=" + OS + ", userId=" + getUserId() + "]";
    }
}

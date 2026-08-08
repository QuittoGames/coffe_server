package com.quitto.server.domain.models.Machine;

import com.quitto.server.domain.models.User.User;

/**
 * Representa uma máquina gerenciada por um usuário no homelab.
 *
 * <p>Cada máquina carrega as informações necessárias para o gerenciamento
 * remoto: identificação de rede (hostname, IP atual, endereço MAC), chave do
 * nó no Tailscale e suporte a Wake-on-LAN ({@code wolEnabled} + MAC). O
 * vínculo com o dono é feito pelo campo {@code userId}.</p>
 *
 * <p><strong>Decisões de projeto:</strong></p>
 * <ul>
 *   <li><strong>Igualdade por identidade:</strong> duas máquinas são iguais
 *       se possuem o mesmo {@code id} maior que zero — não comparação por
 *       valor.</li>
 *   <li><strong>{@code changeOwner}:</strong> a transferência de propriedade
 *       apenas atualiza o {@code userId} — a validação do dono é
 *       responsabilidade das camadas de aplicação/infraestrutura.</li>
 *   <li>O campo do sistema operacional chama-se {@code OS} (padronizado),
 *       mas o parâmetro do construtor é {@code oS} — grafia histórica
 *       preservada.</li>
 * </ul>
 */
public class Machine {
    private Long id;
    private String hostname;
    private String tailscaleNodeKey;
    private String currentIp;
    private String macAddress;
    private boolean wolEnabled;
    private boolean status;
    private String OS;
    private Long userId;

    public Machine() {
    }

    public Machine(Long id, String hostname, String tailscaleNodeKey, String currentIp, String macAddress,
            boolean status, boolean wolEnabled, String oS) {
        this.id = id;
        this.hostname = hostname;
        this.tailscaleNodeKey = tailscaleNodeKey;
        this.currentIp = currentIp;
        this.macAddress = macAddress;
        this.status = status;
        this.wolEnabled = wolEnabled;
        this.OS = oS;
    }

    public Machine(Long id, String hostname, String tailscaleNodeKey, String currentIp, String macAddress,
            boolean status, boolean wolEnabled, String oS, Long userId) {
        this(id, hostname, tailscaleNodeKey, currentIp, macAddress, status, wolEnabled, oS);
        this.userId = userId;
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

    public String getOS() {
        return OS;
    }

    public void setOS(String os) {
        OS = os;
    }

    public Long getUserId() {
        return userId;
    }

    public void changeOwner(User user) {
        this.userId = user.getId();
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        if (obj == this){
            return true;
        }

        Machine other = (Machine) obj;
        return this.id > 0 && this.id == other.id;
    }

    @Override
    public String toString() {
        return "Machine [id=" + id + ", hostname=" + hostname + ", tailscaleNodeKey=" + tailscaleNodeKey
                + ", currentIp=" + currentIp + ", macAddress=" + macAddress + ", wolEnabled=" + wolEnabled
                + ", status=" + status + ", OS=" + OS + ", userId=" + userId + "]";
    }

}

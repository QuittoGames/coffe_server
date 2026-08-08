package com.quitto.server.domain.models.LinuxAccount;

/**
 * Representa uma conta de usuário Linux do servidor (arquivo
 * {@code /etc/passwd}).
 *
 * <p>Contém os dados básicos de uma conta Unix: {@code uid}, nome de login,
 * shell padrão e diretório home. O vínculo com um grupo é feito pelo campo
 * {@code group} (relação M:1 com {@link Groups}).</p>
 *
 * <p><strong>Igualdade por identidade:</strong> dois usuários são iguais se
 * possuem o mesmo {@code uid} maior que zero — não comparação por valor.</p>
 */
public class LinuxUser {
    private int uid;
    private String name;
    private String shell;
    private String homeDir;
    private Groups group;

    public LinuxUser() {
    }

    public LinuxUser(int uid, String name, String shell, String homeDir) {
        this.uid = uid;
        this.name = name;
        this.shell = shell;
        this.homeDir = homeDir;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getShell() {
        return shell;
    }

    public String getHomeDir() {
        return homeDir;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        if (obj == this){
            return true;
        }

        LinuxUser other = (LinuxUser) obj;
        return this.uid > 0 && this.uid == other.uid;
    }

    @Override
    public String toString() {
        return "LinuxUser [uid=" + uid + ", name=" + name + ", shell=" + shell + ", homeDir=" + homeDir + ", group=" + group + "]";
    }
}

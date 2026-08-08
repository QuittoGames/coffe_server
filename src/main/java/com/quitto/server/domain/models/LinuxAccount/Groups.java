package com.quitto.server.domain.models.LinuxAccount;

/**
 * Representa um grupo Unix do servidor (arquivo {@code /etc/group}).
 *
 * <p>Identificado pelo {@code GID} (Group ID). Usado no gerenciamento de
 * permissões baseadas em grupos Linux, com estado ativo/inativo
 * ({@code is_active}).</p>
 *
 * <p><strong>Igualdade por identidade:</strong> dois grupos são iguais se
 * possuem o mesmo {@code GID} maior que zero — não comparação por valor.</p>
 */
public class Groups {
    private int GID;
    private String name;
    private boolean is_active;

    public Groups() {

    }

    public Groups(int GID, String name, boolean is_active) {
        this.GID = GID;
        this.name = name;
        this.is_active = is_active;
    }

    public int getGID() {
        return GID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return is_active;
    }

    public void setActive(boolean is_active) {
        this.is_active = is_active;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        if (obj == this){
            return true;
        }

        Groups other = (Groups) obj;
        return this.GID > 0 && this.GID == other.GID;
    }

    @Override
    public String toString() {
        return "Groups [GID=" + GID + ", name=" + name + ", is_active=" + is_active + "]";
    }
}

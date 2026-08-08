package com.quitto.server.domain.Database.redis;

import com.quitto.server.domain.Database.DatabaseClient;

/**
 * Configuração de uma instância Redis concreta — estende {@link DatabaseClient}
 * com senha (autenticação) e TLS/SSL. O typo histórico {@code Instace} NÃO deve
 * ser renomeado (quebraria imports; correção fica para versão com migração).
 */
public class RedisClientInstace extends DatabaseClient {

    private String password;
    private boolean useSsl;

    public String getPassword() {
        return password;
    }

    public boolean isUseSsl() {
        return useSsl;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUseSsl(boolean useSsl) {
        this.useSsl = useSsl;
    }

}

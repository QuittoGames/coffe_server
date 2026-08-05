package com.quitto.server.domain.Database.redis;

import com.quitto.server.domain.Database.DatabaseClient;

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

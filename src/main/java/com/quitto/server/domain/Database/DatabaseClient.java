package com.quitto.server.domain.Database;

/**
 * Configuração base de um cliente de banco de dados (nome, host, porta, enabled).
 * POJO do domínio com setters para binding de configuração (ex.: {@code @ConfigurationProperties}).
 * Estendido por especializações como {@link RedisClientInstace}.
 */
public class DatabaseClient {
    private String name;
    private String host;
    private int port;
    private boolean enabled;

    public String getName() {
        return name;
    }
    public String getHost() {
        return host;
    }
    public int getPort() {
        return port;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}

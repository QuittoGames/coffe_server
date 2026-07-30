package com.quitto.server.infrastructure.Adpter.in;

import com.quitto.server.domain.Database.Connection;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

public class RedisClientConnectionAdpter implements Connection {

    private final StatefulRedisConnection<String, byte[]> connection;
    private final RedisAsyncCommands<String, byte[]> commands;

    public RedisClientConnectionAdpter(StatefulRedisConnection<String, byte[]> connection) {
        this.connection = connection;
        this.commands = connection.async();
    }

    @Override
    public boolean isOpen() {
        return connection.isOpen();
    }

    @Override
    public void close() {
        connection.close();
    }

    public RedisAsyncCommands<String, byte[]> getCommands() {
        return commands;
    }
}

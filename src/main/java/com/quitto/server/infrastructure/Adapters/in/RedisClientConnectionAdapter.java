package com.quitto.server.infrastructure.Adapters.in;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.quitto.server.domain.interfaces.Database.Connection;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

public class RedisClientConnectionAdapter implements Connection {

    private final String clientName;

    private final StatefulRedisConnection<String, byte[]> connection;
    private final RedisAsyncCommands<String, byte[]> commands;

    public RedisClientConnectionAdapter(StatefulRedisConnection<String, byte[]> connection, String clientName) {
        this.connection = connection;
        this.commands = connection.async();
        this.clientName = clientName;
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

    public StatefulRedisConnection<String, byte[]> getConnection() {
        return connection;
    }

    @Override
    public void insert(String key, byte[] value) {
        commands.set(key, value);
    }

    @Override
    public void insert(String key, byte[] value, long ttlSeconds) {
        commands.setex(key, ttlSeconds, value);
    }

    @Override
    public void delete(String key) {
        commands.del(key);
    }

    @Override
    public void expire(String key, long ttlSeconds) {
        commands.expire(key, ttlSeconds);
    }

    @Override
    public void persist(String key) {
        commands.persist(key);
    }

    /* ---------- Hash ---------- */

    public void putHash(String key, Map<String, byte[]> values) {
        commands.hset(key, values);
    }

    public void putHashField(String key, String field, byte[] value) {
        commands.hset(key, field, value);
    }

    public void deleteHashField(String key, String... fields) {
        commands.hdel(key, fields);
    }

    public void deleteHash(String key) {
        commands.del(key);
    }

    /* ---------- Util ---------- */

    public void rename(String oldKey, String newKey) {
        commands.rename(oldKey, newKey);
    }

    public void flushDatabase() {
        commands.flushdb(); // WARN , DANGER COMMAND
    }

    public void flushAll() {
        commands.flushall();
    }

    @Override
    public String search(String key) {
        RedisFuture<byte[]> future = commands.get(key);
        try {
            byte[] value = future.get(connection.getTimeout().toMillis(), TimeUnit.MILLISECONDS);
            return value == null ? null : new String(value, StandardCharsets.UTF_8);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while searching Redis key: " + key, e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Failed to search Redis key: " + key, e);
        } catch (TimeoutException e) {
            throw new IllegalStateException("Timed out while searching Redis key: " + key, e);
        }
    }
}

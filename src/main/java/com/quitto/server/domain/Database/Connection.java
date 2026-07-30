package com.quitto.server.domain.Database;

public interface Connection<T extends DatabaseClient> {

    boolean isOpen();

    void close();
}

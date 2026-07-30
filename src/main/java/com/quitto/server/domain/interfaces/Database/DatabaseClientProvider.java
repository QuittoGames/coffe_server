package com.quitto.server.domain.interfaces.Database;

import com.quitto.server.domain.Database.Connection;

public interface DatabaseClientProvider<T extends Connection> {

    T get(String name);

}

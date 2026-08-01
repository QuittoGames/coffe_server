package com.quitto.server.domain.interfaces.Database;

import java.util.Map;

import com.quitto.server.domain.Database.Connection;
import com.quitto.server.domain.Database.DatabaseProperties;

public interface DatabaseClientProvider<T extends Connection , P extends DatabaseProperties> {

    T getAdpterConnector(String name);

    Map<String, T> getProvaiders(P properties);

}

package com.quitto.server.domain.Database;

import java.util.List;

public interface DatabaseProperties {

    List<? extends DatabaseClient> getInstances();

}

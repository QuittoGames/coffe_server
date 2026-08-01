package com.quitto.server.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quitto.server.domain.Database.Connection;
import com.quitto.server.domain.Database.DatabaseClient;
import com.quitto.server.domain.Database.DatabaseProperties;
import com.quitto.server.domain.Database.redis.RedisClientInstace;
import com.quitto.server.domain.interfaces.Database.DatabaseClientProvider;

@ExtendWith(MockitoExtension.class)
@DisplayName("Redis Abstraction Tests")
class RedisAbstractionTest {

    @Test
    @DisplayName("Connection.isOpen returns correct state")
    void connection_isOpenReturnsCorrectState() {
        Connection<?> openConn = mock(Connection.class);
        when(openConn.isOpen()).thenReturn(true);
        assertTrue(openConn.isOpen());

        Connection<?> closedConn = mock(Connection.class);
        when(closedConn.isOpen()).thenReturn(false);
        assertFalse(closedConn.isOpen());
    }

    @Test
    @DisplayName("Connection.close does not throw")
    void connection_closeDoesNotThrow() {
        Connection<?> conn = mock(Connection.class);
        doNothing().when(conn).close();
        assertDoesNotThrow(() -> conn.close());
        verify(conn, times(1)).close();
    }

    @Test
    @DisplayName("Connection can be queried multiple times")
    void connection_isOpenCalledMultipleTimes() {
        Connection<?> conn = mock(Connection.class);
        when(conn.isOpen()).thenReturn(true, true, false);

        assertTrue(conn.isOpen());
        assertTrue(conn.isOpen());
        assertFalse(conn.isOpen());
        verify(conn, times(3)).isOpen();
    }

    @Test
    @DisplayName("RedisClientInstace extends DatabaseClient")
    void redisClientInstance_extendsDatabaseClient() {
        RedisClientInstace instance = new RedisClientInstace();
        assertInstanceOf(DatabaseClient.class, instance);
    }

    @Test
    @DisplayName("RedisClientInstace setters and getters")
    void redisClientInstance_settersAndGetters() {
        RedisClientInstace instance = new RedisClientInstace();

        instance.setName("cache");
        instance.setHost("localhost");
        instance.setPort(6379);
        instance.setEnabled(true);

        assertEquals("cache", instance.getName());
        assertEquals("localhost", instance.getHost());
        assertEquals(6379, instance.getPort());
        assertTrue(instance.isEnabled());
    }

    @Test
    @DisplayName("RedisClientInstace default state")
    void redisClientInstance_defaultState() {
        RedisClientInstace instance = new RedisClientInstace();

        assertNull(instance.getName());
        assertNull(instance.getHost());
        assertEquals(0, instance.getPort());
        assertFalse(instance.isEnabled());
    }

    @Test
    @DisplayName("RedisClientInstace can toggle enabled")
    void redisClientInstance_canToggleEnabled() {
        RedisClientInstace instance = new RedisClientInstace();
        instance.setEnabled(false);
        assertFalse(instance.isEnabled());

        instance.setEnabled(true);
        assertTrue(instance.isEnabled());

        instance.setEnabled(false);
        assertFalse(instance.isEnabled());
    }

    @Test
    @DisplayName("RedisClientInstace port accepts range")
    void redisClientInstance_portAcceptsRange() {
        RedisClientInstace instance = new RedisClientInstace();

        instance.setPort(0);
        assertEquals(0, instance.getPort());

        instance.setPort(65535);
        assertEquals(65535, instance.getPort());

        instance.setPort(6379);
        assertEquals(6379, instance.getPort());
    }

    @Test
    @DisplayName("DatabaseClient base class works")
    void databaseClient_gettersAndSetters() {
        DatabaseClient client = new DatabaseClient();

        client.setName("test-db");
        client.setHost("192.168.1.100");
        client.setPort(5432);
        client.setEnabled(true);

        assertEquals("test-db", client.getName());
        assertEquals("192.168.1.100", client.getHost());
        assertEquals(5432, client.getPort());
        assertTrue(client.isEnabled());
    }

    @Test
    @DisplayName("DatabaseClientProvider returns connection by name")
    void provider_returnsConnectionByName() {
        @SuppressWarnings("unchecked")
        DatabaseClientProvider<Connection<?>, DatabaseProperties> provider = mock(DatabaseClientProvider.class);
        Connection<?> mockConnection = mock(Connection.class);

        doReturn(mockConnection).when(provider).getAdpterConnector("cache");
        when(mockConnection.isOpen()).thenReturn(true);

        Connection<?> result = provider.getAdpterConnector("cache");
        assertNotNull(result);
        assertTrue(result.isOpen());
    }

    @Test
    @DisplayName("DatabaseClientProvider throws when name not found")
    void provider_throwsWhenNameNotFound() {
        @SuppressWarnings("unchecked")
        DatabaseClientProvider<Connection<?>, DatabaseProperties> provider = mock(DatabaseClientProvider.class);
        when(provider.getAdpterConnector("nonexistent")).thenThrow(new IllegalArgumentException("Unknown database: nonexistent"));

        assertThrows(IllegalArgumentException.class, () -> provider.getAdpterConnector("nonexistent"));
    }

    @Test
    @DisplayName("DatabaseClientProvider returns different connections")
    void provider_returnsDifferentConnections() {
        @SuppressWarnings("unchecked")
        DatabaseClientProvider<Connection<?>, DatabaseProperties> provider = mock(DatabaseClientProvider.class);
        Connection<?> cacheConn = mock(Connection.class);
        Connection<?> sessionConn = mock(Connection.class);

        doReturn(cacheConn).when(provider).getAdpterConnector("cache");
        doReturn(sessionConn).when(provider).getAdpterConnector("session");

        assertSame(cacheConn, provider.getAdpterConnector("cache"));
        assertSame(sessionConn, provider.getAdpterConnector("session"));
        assertNotSame(cacheConn, sessionConn);
    }

    @Test
    @DisplayName("DatabaseClientProvider connection lifecycle")
    void provider_connectionLifecycle() {
        @SuppressWarnings("unchecked")
        DatabaseClientProvider<Connection<?>, DatabaseProperties> provider = mock(DatabaseClientProvider.class);
        Connection<?> conn = mock(Connection.class);

        doReturn(conn).when(provider).getAdpterConnector("cache");
        doNothing().when(conn).close();

        Connection<?> obtained = provider.getAdpterConnector("cache");
        obtained.close();
        verify(conn, times(1)).close();
    }
}

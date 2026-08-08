package com.quitto.server.integration.persistence;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.quitto.server.infrastructure.db.Machine.Adapter.MachineRepositoryAdapter;
import com.quitto.server.infrastructure.db.User.Adapter.UserRepositoryAdapter;

/**
 * Base compartilhada para testes de persistência com Postgres real via
 * Testcontainers. O container é estático — inicia uma única vez por JVM e é
 * reutilizado pelas subclasses (User e Machine).
 *
 * <p><strong>Regra:</strong> este teste NÃO toca o Postgres de produção —
 * o container roda localmente no Docker com um banco descartável
 * ({@code coffe_test}).</p>
 *
 * <p>O slice {@code @DataJpaTest} carrega apenas JPA (entities + repositories
 * Spring Data). Os adapters são importados explicitamente via
 * {@code @Import} porque não são interfaces Spring Data.</p>
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({UserRepositoryAdapter.class, MachineRepositoryAdapter.class})
@Testcontainers
abstract class PostgresPersistenceTestSupport {

    static final String TEST_DB_NAME = "coffe_test";
    static final String TEST_USER = "test";
    static final String TEST_PASSWORD = "test";

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName(TEST_DB_NAME)
            .withUsername(TEST_USER)
            .withPassword(TEST_PASSWORD);

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        // Sobrescreve o H2Dialect do profile `test` — o container é Postgres 17.
        registry.add("spring.jpa.properties.hibernate.dialect",
                () -> "org.hibernate.dialect.PostgreSQLDialect");
        // Evita rodar o test-data.sql do H2 contra o Postgres.
        registry.add("spring.sql.init.mode", () -> "never");
        registry.add("spring.sql.init.data-locations", () -> "");
    }
}

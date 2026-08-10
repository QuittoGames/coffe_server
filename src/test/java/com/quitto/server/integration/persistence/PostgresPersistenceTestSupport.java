package com.quitto.server.integration.persistence;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import com.quitto.server.infrastructure.db.Machine.Adapter.MachineRepositoryAdapter;
import com.quitto.server.infrastructure.db.User.Adapter.UserRepositoryAdapter;

/**
 * Base compartilhada para testes de persistência com Postgres real via
 * Testcontainers. O container é iniciado UMA única vez por JVM e fica vivo até
 * o shutdown — nunca é parado entre classes de teste.
 *
 * <p><strong>Por que não usar {@code @Container}/{@code @Testcontainers}:</strong>
 * com a anotação o container é parado ao fim de cada classe de teste. Como as
 * subclasses (User e Machine) compartilham o MESMO {@code ApplicationContext}
 * cacheado pelo Spring (mesma config), a 2ª classe reutilizaria a JDBC URL da
 * 1ª com a porta antiga → {@code Connection refused}. O start manual em
 * {@code static {}} mantém a mesma instância/porta durante toda a JVM.</p>
 *
 * <p><strong>Regra:</strong> este teste NÃO toca o Postgres de produção —
 * o container roda localmente no Docker com um banco descartável
 * ({@code coffe_test}).</p>
 *
 * <p>O slice {@code @DataJpaTest} carrega apenas JPA (entities + repositories
 * Spring Data). Os adapters são importados explicitamente via
 * {@code @Import} porque não são interfaces Spring Data. Com
 * {@code Replace.NONE} o {@code ddl-auto=create-drop} precisa ser declarado
 * explicitamente (senão o Hibernate não cria o schema no Postgres).</p>
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({UserRepositoryAdapter.class, MachineRepositoryAdapter.class})
@ActiveProfiles("test")
abstract class PostgresPersistenceTestSupport {

    static final String TEST_DB_NAME = "coffe_test";
    static final String TEST_USER = "test";
    static final String TEST_PASSWORD = "test";

    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName(TEST_DB_NAME)
            .withUsername(TEST_USER)
            .withPassword(TEST_PASSWORD);

    static {
        POSTGRES.start();
        Runtime.getRuntime().addShutdownHook(new Thread(POSTGRES::stop));
    }

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        // Cria o schema no container — o @DataJpaTest com Replace.NONE não
        // aplica o create-drop padrão do H2.
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        // Sobrescreve o H2Dialect do profile `test` — o container é Postgres 17.
        registry.add("spring.jpa.properties.hibernate.dialect",
                () -> "org.hibernate.dialect.PostgreSQLDialect");
        // Evita rodar o test-data.sql do H2 contra o Postgres.
        registry.add("spring.sql.init.mode", () -> "never");
        registry.add("spring.sql.init.data-locations", () -> "");
    }
}

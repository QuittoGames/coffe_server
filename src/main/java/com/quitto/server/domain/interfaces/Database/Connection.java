package com.quitto.server.domain.interfaces.Database;

import com.quitto.server.domain.Database.DatabaseClient;

/**
 * Porta de conexão com um banco de dados (Clean Architecture / Ports &amp; Adapters).
 *
 * <p>Define o contrato mínimo de uma conexão: verificar se está aberta,
 * fechar e executar operações básicas de escrita e de ciclo de vida de
 * chaves (inserir com ou sem TTL, deletar, expirar e persistir).</p>
 *
 * <p><strong>Por quê existe:</strong> o domínio não deve conhecer bibliotecas
 * de conexão concretas (Lettuce, JDBC, Netty). Qualquer banco (Redis,
 * PostgreSQL, etc.) pode implementar esta porta na infraestrutura — ex.: o
 * {@code RedisClientConnectionAdapter} encapsula o Lettuce.</p>
 *
 * @param <T> tipo da configuração do banco, subtipo de {@link DatabaseClient}
 */
public interface Connection<T extends DatabaseClient> {

    boolean isOpen();

    void close();

    void insert(String key, byte[] value);

    void insert(String key, byte[] value, long ttl);

    void delete(String key);

    void expire(String key, long ttl);

    void persist(String key);

    String search(String key);
}

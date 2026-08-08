package com.quitto.server.domain.interfaces.Database;

import java.util.List;

import com.quitto.server.domain.Database.DatabaseClient;

/**
 * Contrato de configuração de propriedades de bancos de dados.
 *
 * <p>Porta do domínio implementada pela infraestrutura — ex.: a classe
 * {@code RedisProperties} com {@code @ConfigurationProperties(prefix="coffee.redis")}.
 * Expõe a lista de instâncias de banco configuradas, permitindo que o
 * domínio itere sobre elas sem conhecer a fonte da configuração (arquivo,
 * env vars, etc.).</p>
 *
 * <p><strong>Por quê existe:</strong> inverte a dependência de configuração —
 * quem consome chama {@link #getInstances()} e recebe modelos do domínio
 * ({@code DatabaseClient}), nunca detalhes de framework.</p>
 */
public interface DatabaseProperties {

    List<? extends DatabaseClient> getInstances();

}

package com.quitto.server.domain.interfaces.Database;

import java.util.Map;

/**
 * Porta genérica de provedor de conexões de banco de dados.
 *
 * <p>Define o contrato para obter uma conexão pelo nome da instância
 * ({@code getAdpterConnector}) ou construir um mapa de conexões a partir das
 * propriedades configuradas ({@code getProvaiders}). A implementação concreta
 * vive na infraestrutura — ex.: {@code RedisClientProvider} usando Lettuce,
 * com conexões lazy cacheadas.</p>
 *
 * <p><strong>Por quê existe:</strong> o domínio precisa de acesso a banco de
 * dados sem conhecer o cliente concreto. Qualquer banco que implementar
 * {@link Connection} pode ser plugado atrás desta porta.</p>
 *
 * @param <T> tipo da conexão retornada, subtipo de {@link Connection}
 * @param <P> tipo das propriedades de configuração, subtipo de
 *            {@link DatabaseProperties}
 */
public interface DatabaseClientProvider<T extends Connection , P extends DatabaseProperties> {

    T getAdpterConnector(String name);

    Map<String, T> getProvaiders(P properties);

}

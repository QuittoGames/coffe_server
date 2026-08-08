package com.quitto.server.domain.interfaces.Serializer;

/**
 * Porta de domínio para <strong>serialização genérica</strong> de objetos em
 * {@code byte[]}. Complementa o ecossistema de dados do servidor (ex.: sessões
 * ou cache serializados como JSON). Implementação concreta na infraestrutura.
 */
public interface Serializer {

    <T> byte[] serialize(T object);

}
